package com.a13.notification.client.service;

import com.a13.notification.client.config.NotificationConfig;
import com.a13.notification.client.dto.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class NotificationProducer {

    private static final Logger log = LoggerFactory.getLogger(NotificationProducer.class);

    private final RabbitTemplate rabbitTemplate;
    private final String replyQueue; // Ora è final e iniettato dal costruttore

    // Costruttore aggiornato: riceve sia il template che la coda
    public NotificationProducer(RabbitTemplate rabbitTemplate, String replyQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.replyQueue = replyQueue;
    }

    /**
     * Invia una richiesta di creazione notifica al broker.
     * @param dto il DTO della notifica da creare
     */
    public void sendCreateNotification(NotificationDTO dto) {
        // Se il servizio ospite ha configurato una coda di risposta, la impostiamo
        if (replyQueue != null && !replyQueue.isEmpty() && !replyQueue.equals("null")) {
            dto.setReplyTo(replyQueue);
        }

        log.info("Client Library - Invio richiesta notifica: userId={}, type={}, title={}, replyTo={}",
                dto.getUserId(), dto.getType(), dto.getTitle(), dto.getReplyTo());

        rabbitTemplate.convertAndSend(
                NotificationConfig.EXCHANGE,
                NotificationConfig.ROUTING_KEY,
                dto
        );
    }
}
