package com.t10.communication;

import com.t10.model.dto.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationProducer {

    private static final Logger log = LoggerFactory.getLogger(NotificationProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateNotification(NotificationDTO dto) {
        // Impostiamo la coda di risposta di t10
        dto.setReplyTo(ClientRabbitConfig.REPLY_QUEUE);

        log.info("T10 - Invio richiesta notifica: userId={}, type={}, title={}",
                dto.getUserId(), dto.getType(), dto.getTitle());

        rabbitTemplate.convertAndSend(
                ClientRabbitConfig.NOTIFICATION_EXCHANGE,
                ClientRabbitConfig.ROUTING_KEY,
                dto
        );
    }
}
