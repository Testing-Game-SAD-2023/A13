package com.service;

import com.communication.ReplyProducer;
import com.mapper.NotificationMapper;
import com.model.Notification;

// === IMPORT DALLA LIBRERIA CONDIVISA (Quelli che usi per RabbitMQ) ===
import com.a13.notification.client.dto.NotificationDTO;
import com.a13.notification.client.dto.NotificationResponseDTO;
// ======================================================================

// DTO LOCALI (Quelli che usi per le API REST verso il frontend)
import com.model.dto.NotificationRestDTO;
import com.model.mapper.NotificationRestMapper;
import com.model.repository.NotificationRepository;

import com.service.sse.SseConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final ReplyProducer replyProducer;
    private final NotificationRestMapper restMapper; // Mapper per le API REST
    private final SseConnectionManager sseManager;

    // Costruttore unificato con tutte le dipendenze
    public NotificationService(NotificationRepository notificationRepository,
                               NotificationMapper notificationMapper,
                               ReplyProducer replyProducer,
                               NotificationRestMapper restMapper,
                               SseConnectionManager sseManager) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.replyProducer = replyProducer;
        this.restMapper = restMapper;
        this.sseManager = sseManager;
    }

    // =========================================================================
    // METODO ISCRIZIONE SSE
    // =========================================================================

    public SseEmitter subscribe(Long userId) {
        return sseManager.subscribe(userId);
    }

    // =========================================================================
    // METODI MESSAGING
    // =========================================================================

    /**
     * Gestisce una richiesta di creazione notifica arrivata via RabbitMQ.
     */
    @Transactional
    public void processIncomingNotification(NotificationDTO dto) {
        log.info("Elaborazione notifica per userId={}, type={}", dto.getUserId(), dto.getType());

        try {
            // 1. DTO -> Entity
            Notification notification = notificationMapper.toEntity(dto);

            // 2. Salvataggio sul DB
            Notification saved = notificationRepository.save(notification);
            log.info("Notifica salvata con id={}", saved.getId());

            // 3. Invio live al frontend
            NotificationRestDTO restDTO = restMapper.toDTO(saved);
            sseManager.dispatch(saved.getUserId(), restDTO);

            // 4. Invio risposta solo se è stata richiesta
            if (dto.getReplyTo() != null && !dto.getReplyTo().isBlank()) {
                NotificationResponseDTO response = new NotificationResponseDTO(
                        saved.getId(),
                        "OK",
                        "Notification created successfully"
                );
                replyProducer.sendReply(dto.getReplyTo(), response);
            } else {
                log.info("Nessuna replyTo fornita, non invio risposta");
            }

        } catch (Exception ex) {
            log.error("Errore durante l'elaborazione della notifica", ex);

            if (dto.getReplyTo() != null && !dto.getReplyTo().isBlank()) {
                NotificationResponseDTO errorResponse = new NotificationResponseDTO(
                        null,
                        "ERROR",
                        "Failed to create notification: " + ex.getMessage()
                );
                replyProducer.sendReply(dto.getReplyTo(), errorResponse);
            }

            // eventualmente rilancia ex per retry/DLQ
        }
    }

    // =========================================================================
    // METODI REST API
    // =========================================================================

    // Recupera notifiche di un utente con filtri semplici:
    // - read = false  -> solo non lette (priorità)
    // - type != null  -> solo di quel tipo
    // Se entrambi presenti, diamo priorità a "non lette".
    @Transactional(readOnly = true)
    public List<NotificationRestDTO> getUserNotifications(Long userId,
                                                          Boolean read,
                                                          String type) {

        List<Notification> notifications;

        if (Boolean.FALSE.equals(read)) {
            // filtro: solo non lette (Priorità 1)
            notifications = notificationRepository
                    .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        } else if (type != null && !type.isBlank()) { // Aggiunto !type.isBlank() per robustezza
            // filtro: solo per tipo (Priorità 2, se 'read' non è esplicitamente false)
            notifications = notificationRepository
                    .findByUserIdAndTypeOrderByCreatedAtDesc(userId, type);
        } else {
            // nessun filtro: tutte le notifiche dell'utente
            notifications = notificationRepository
                    .findByUserIdOrderByCreatedAtDesc(userId);
        }

        return notifications.stream()
                .map(restMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotificationRestDTO getNotification(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (!notification.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Notification does not belong to this user");
        }

        return restMapper.toDTO(notification);
    }

    @Transactional
    public void markNotificationAsRead(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (!notification.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Notification does not belong to this user");
        }

        // Utilizza il save() per l'update in caso di modifica (è efficiente grazie a Transactional e Session)
        if (!notification.isRead()) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    @Transactional
    public void deleteNotification(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (!notification.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Notification does not belong to this user");
        }

        notificationRepository.delete(notification);
    }

    @Transactional
    public void clearNotificationsByUser(Long userId) {
        List<Notification> notifications =
                notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        // delete(Iterable) di JPA è efficiente
        notificationRepository.deleteAll(notifications);
    }
}