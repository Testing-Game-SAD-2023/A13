package com.t10;

import com.a13.notification.client.dto.NotificationResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class T10ReplyListener {

    private static final Logger log = LoggerFactory.getLogger(T10ReplyListener.class);

    // Ascolta la coda definita nella configurazione
    @RabbitListener(queues = T10RabbitConfig.REPLY_QUEUE_NAME)
    public void handleReply(NotificationResponseDTO response) {
        log.info("==============================================");
        log.info("T10 - RISPOSTA RICEVUTA DA T9!");
        log.info("ID Notifica: {}", response.getNotificationId());
        log.info("Stato: {}", response.getStatus());
        log.info("Messaggio: {}", response.getMessage());
        log.info("==============================================");
    }
}
