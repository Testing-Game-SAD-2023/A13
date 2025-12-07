package com.communication;

import com.model.dto.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationProducer {

    private static final Logger log = LoggerFactory.getLogger(NotificationProducer.class);

    // Devono corrispondere a quelli del servizio T9
    public static final String NOTIFICATION_EXCHANGE = "notifications.exchange";
    public static final String ROUTING_KEY = "notifications.create";

    private final RabbitTemplate rabbitTemplate;

    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Invia una richiesta di creazione notifica al servizio T9.
     * replyToQueue è il nome della coda su cui questo microservizio
     * vuole ricevere la risposta.
     */
    public void sendCreateNotification(NotificationDTO dto, String replyToQueue) {
        dto.setReplyTo(replyToQueue);

        log.info("Invio richiesta notifica a exchange={}, routingKey={}, userId={}, type={}",
                NOTIFICATION_EXCHANGE, ROUTING_KEY, dto.getUserId(), dto.getType());

        rabbitTemplate.convertAndSend(NOTIFICATION_EXCHANGE, ROUTING_KEY, dto);
    }
}
