package com.mapper;

import com.model.Notification;
import com.model.dto.NotificationDTO;
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

        Notification notification = new Notification();

        // Campi principali
        notification.setUserId(dto.getUserId());
        notification.setType(dto.getType());
        notification.setTitle(dto.getTitle());
        notification.setBody(dto.getBody());

        // createdAt:
        // - se il chiamante l'ha valorizzato, lo usiamo
        // - altrimenti lasciamo che il costruttore/entity usi Instant.now()
        if (dto.getCreatedAt() != null) {
            notification.setCreatedAt(dto.getCreatedAt());
        }

        // isRead rimane al valore di default (false)

        return notification;
    }

    /**
     * Facoltativo: converte da entity a DTO, utile se vuoi rispondere via RabbitMQ.
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
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setReplyTo(replyTo);

        return dto;
    }
}
