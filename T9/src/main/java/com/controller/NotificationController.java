package com.controller;

import com.model.dto.NotificationResponseDTO;
import com.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    //  Recupera tutte le notifiche di un user (con filtri opzionali)
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotifications(
            @PathVariable Long userId,
            @RequestParam(required = false) Boolean read,
            @RequestParam(required = false) String type
    ) {
        List<NotificationResponseDTO> notifications =
                notificationService.getUserNotifications(userId, read, type);

        return ResponseEntity.ok(notifications);
    }

    //  Recupera una singola notifica per ID
    @GetMapping("/{userId}/{notificationId}")
    public ResponseEntity<NotificationResponseDTO> getNotification(
            @PathVariable Long userId,
            @PathVariable Long notificationId
    ) {
        NotificationResponseDTO dto =
                notificationService.getNotification(userId, notificationId);

        return ResponseEntity.ok(dto);
    }

    //  Segna una notifica come letta
    @PatchMapping("/{userId}/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long userId,
            @PathVariable Long notificationId
    ) {
        notificationService.markNotificationAsRead(userId, notificationId);
        return ResponseEntity.noContent().build();
    }

    //  Elimina una singola notifica
    @DeleteMapping("/{userId}/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long userId,
            @PathVariable Long notificationId
    ) {
        notificationService.deleteNotification(userId, notificationId);
        return ResponseEntity.noContent().build();
    }

    //  Elimina tutte le notifiche di un user
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearNotifications(
            @PathVariable Long userId
    ) {
        notificationService.clearNotificationsByUser(userId);
        return ResponseEntity.noContent().build();
    }
}
