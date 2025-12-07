package com.communication;

import com.model.dto.NotificationResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class NotificationResponseListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationResponseListener.class);

    /**
     * Ascolta la coda di risposta specifica di questo microservizio.
     */
    @RabbitListener(queues = ClientRabbitConfig.REPLY_QUEUE)
    public void handleNotificationResponse(@Payload NotificationResponseDTO response) {
        log.info("Ricevuta risposta da T9: status={}, notificationId={}, message={}",
                response.getStatus(), response.getNotificationId(), response.getMessage());

        // Qui puoi:
        // - aggiornare stato interno
        // - loggare
        // - notificare altre parti del sistema, ecc.
    }
}
