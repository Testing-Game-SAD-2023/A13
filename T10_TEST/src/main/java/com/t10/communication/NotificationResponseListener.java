package com.t10.communication;

import com.t10.model.dto.NotificationResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class NotificationResponseListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationResponseListener.class);

    @RabbitListener(queues = ClientRabbitConfig.REPLY_QUEUE)
    public void handleNotificationResponse(@Payload NotificationResponseDTO response) {
        log.info("T10 - Risposta da T9: status={}, notificationId={}, message={}",
                response.getStatus(), response.getNotificationId(), response.getMessage());
    }
}
