package com.communication;

import com.a13.notification.client.dto.NotificationResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
/**
 * Classe per la generazione delle notifiche di risposta laddove il servizio mittente ne necessiti
 * */
@Component
public class ReplyProducer {

    private static final Logger log = LoggerFactory.getLogger(ReplyProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public ReplyProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Invia una risposta sulla coda indicata, se presente, nel campo replyTo del DTO ricevuto.
     */
    public void sendReply(String replyToQueue, NotificationResponseDTO responseDTO) {
        if (replyToQueue == null || replyToQueue.isBlank()) {
            log.warn("replyToQueue nullo o vuoto, nessuna risposta inviata");
            return;
        }

        log.info("Invio risposta su coda {} con status={} e notificationId={}",
                replyToQueue, responseDTO.getStatus(), responseDTO.getNotificationId());

        // Usa il default exchange ("") e come routing key il nome della coda di reply
        rabbitTemplate.convertAndSend(replyToQueue, responseDTO);
    }
}
