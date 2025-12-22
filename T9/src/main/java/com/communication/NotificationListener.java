package com.communication;

import com.a13.notification.client.dto.NotificationDTO;
import com.service.NotificationService;
import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
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
    @AsyncListener(operation = @AsyncOperation(
            channelName = "notifications.create.queue",
            payloadType = NotificationDTO.class,
            description = "Elabora notifiche asincrone ricevute sulla coda notifications.create.queue"
    ))
    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleNotificationMessage(@Payload NotificationDTO dto) {
        log.info("Ricevuto messaggio su coda {} per userId={}, type={}",
                RabbitMQConfig.NOTIFICATION_QUEUE, dto.getUserId(), dto.getType());

        try {
            notificationService.processIncomingNotification(dto);
        } catch (Exception e) {
            log.error("Errore durante l'elaborazione della notifica da DTO", e);
        }
    }
}