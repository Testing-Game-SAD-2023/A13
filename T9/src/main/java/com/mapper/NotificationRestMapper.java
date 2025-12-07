package com.model.mapper;

import com.model.Notification;
import com.model.dto.NotificationRestDTO;
import org.springframework.stereotype.Component;

@Component
public class NotificationRestMapper {

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
