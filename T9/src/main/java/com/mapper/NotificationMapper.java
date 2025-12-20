package com.mapper;

import com.model.Notification;
// IMPORTA IL DTO DALLA LIBRERIA CONDIVISA
import com.a13.notification.client.dto.NotificationDTO;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    /**
     * Converte un NotificationDTO (RabbitMQ) in una entity Notification (DB).
     */
    public Notification toEntity(NotificationDTO dto) {
        if (dto == null) {
            return null;
        }

        // Creando la nuova Entity, il suo costruttore fa già: this.createdAt = Instant.now();
        Notification notification = new Notification();

        // Campi principali (copiati dal DTO)
        notification.setUserId(dto.getUserId());
        notification.setType(dto.getType());
        notification.setTitle(dto.getTitle());
        notification.setBody(dto.getBody());

        // createdAt: NON viene più letto dal DTO.
        // L'Entity userà il suo default (Instant.now()).

        // isRead rimane al valore di default (false)

        return notification;
    }

    /**
     * Facoltativo: converte da entity a DTO.
     */
    public NotificationDTO toDto(Notification notification, String replyTo) {
        if (notification == null) {
            return null;
        }

        NotificationDTO dto = new NotificationDTO();
        dto.setUserId(notification.getUserId());
        dto.setType(notification.getType());
        dto.setTitle(notification.getTitle());
        dto.setBody(notification.getBody());

        // createdAt: NON viene copiato nel DTO perché il campo non esiste più nella libreria.

        dto.setReplyTo(replyTo);

        return dto;
    }
}
