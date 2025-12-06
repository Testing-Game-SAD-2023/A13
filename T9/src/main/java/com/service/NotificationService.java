package com.service;

import com.communication.ReplyProducer;
import com.mapper.NotificationMapper;
import com.model.Notification;
import com.model.dto.NotificationDTO;
import com.model.dto.NotificationResponseDTO;
import com.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final ReplyProducer replyProducer;

    public NotificationService(NotificationRepository notificationRepository,
                               NotificationMapper notificationMapper,
                               ReplyProducer replyProducer) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.replyProducer = replyProducer;
    }

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

            // 3. Costruzione risposta
            NotificationResponseDTO response = new NotificationResponseDTO(
                    saved.getId(),
                    "OK",
                    "Notification created successfully"
            );

            // 4. Invio risposta, se il chiamante ha fornito una coda di reply
            if (dto.getReplyTo() != null && !dto.getReplyTo().isBlank()) {
                replyProducer.sendReply(dto.getReplyTo(), response);
            } else {
                log.info("Nessuna replyTo fornita, non invio risposta");
            }

        } catch (Exception ex) {
            log.error("Errore durante l'elaborazione della notifica", ex);

            // In caso di errore, se esiste una replyTo, prova a inviare un esito di errore
            if (dto.getReplyTo() != null && !dto.getReplyTo().isBlank()) {
                NotificationResponseDTO errorResponse = new NotificationResponseDTO(
                        null,
                        "ERROR",
                        "Failed to create notification: " + ex.getMessage()
                );
                replyProducer.sendReply(dto.getReplyTo(), errorResponse);
            }

            // qui eventualmente potresti rilanciare l'eccezione per far scattare retry/DLQ
        }
    }
}
