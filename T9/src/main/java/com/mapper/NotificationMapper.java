package com.mapper;

import com.model.Notification;

import com.a13.notification.client.dto.NotificationDTO;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    // Converte un NotificationDTO  in una entity Notification.

    public Notification toEntity(NotificationDTO dto) {
        if (dto == null) {
            return null;
        }

        Notification notification = new Notification();

        // Campi principali (copiati dal DTO)
        notification.setUserId(dto.getUserId());
        notification.setType(dto.getType());
        notification.setTitle(dto.getTitle());
        notification.setBody(dto.getBody());

        return notification;
    }


    // Converte una entity Notification in un NotificationDTO .

    public NotificationDTO toDto(Notification notification, String replyTo) {
        if (notification == null) {
            return null;
        }

        NotificationDTO dto = new NotificationDTO();

        // Campi principali (copiati nel DTO)
        dto.setUserId(notification.getUserId());
        dto.setType(notification.getType());
        dto.setTitle(notification.getTitle());
        dto.setBody(notification.getBody());

        dto.setReplyTo(replyTo);

        return dto;
    }
}
