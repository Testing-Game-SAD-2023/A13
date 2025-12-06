package com.communication;

import com.model.dto.NotificationDTO;
import com.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    private final NotificationService notificationService;

    public NotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Ascolta la coda di ingresso delle notifiche.
     * Il nome della coda deve coincidere con quello definito in RabbitMQConfig.
     */
    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleNotificationMessage(@Payload NotificationDTO dto) {
        log.info("Ricevuto messaggio su coda {} per userId={}, type={}",
                RabbitMQConfig.NOTIFICATION_QUEUE, dto.getUserId(), dto.getType());

        try {
            // Delega al service la logica applicativa:
            // - mapping DTO -> entity
            // - salvataggio su DB
            // - eventuale risposta su replyTo
            notificationService.processIncomingNotification(dto);
        } catch (Exception e) {
            log.error("Errore durante l'elaborazione della notifica da DTO", e);
            // qui in futuro potresti aggiungere DLQ, retry, ecc.
        }
    }
}
