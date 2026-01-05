package com.model.mapper;

import com.model.Notification;
import com.model.dto.NotificationRestDTO;
import org.springframework.stereotype.Component;

 //Mapper responsabile della conversione tra l'entity Notification e il  NotificationRestDTO

@Component
public class NotificationRestMapper {

    // Converte una entity Notification in un NotificationRestDTO .

    public NotificationRestDTO toDTO(Notification n) {
        return new NotificationRestDTO(
                n.getId(),
                n.getUserId(),
                n.getType(),
                n.getTitle(),
                n.getBody(),
                n.isRead(),
                n.getCreatedAt()
        );
    }
}
