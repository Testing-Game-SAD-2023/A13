package com.controller;

import com.model.dto.NotificationRestDTO;
import com.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Rende persistente la connessione sse con il client
    @GetMapping(path = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long userId) {
        return notificationService.subscribe(userId);
    }

    // Recupera tutte le notifiche di un user (con filtri opzionali)
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationRestDTO>> getNotifications(
            @PathVariable Long userId,
            @RequestParam(required = false) Boolean read,
            @RequestParam(required = false) String type
    ) {
        List<NotificationRestDTO> notifications =
                notificationService.getUserNotifications(userId, read, type);

        return ResponseEntity.ok(notifications);
    }

    // Recupera una singola notifica per ID
    @GetMapping("/{userId}/{notificationId}")
    public ResponseEntity<NotificationRestDTO> getNotification(
            @PathVariable Long userId,
            @PathVariable Long notificationId
    ) {
        NotificationRestDTO dto =
                notificationService.getNotification(userId, notificationId);

        return ResponseEntity.ok(dto);
    }

    // Segna una notifica come letta
    @PatchMapping("/{userId}/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long userId,
            @PathVariable Long notificationId
    ) {
        notificationService.markNotificationAsRead(userId, notificationId);
        return ResponseEntity.noContent().build();
    }

    // Elimina una singola notifica
    @DeleteMapping("/{userId}/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long userId,
            @PathVariable Long notificationId
    ) {
        notificationService.deleteNotification(userId, notificationId);
        return ResponseEntity.noContent().build();
    }

    // Elimina tutte le notifiche di un user
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearNotifications(
            @PathVariable Long userId
    ) {
        notificationService.clearNotificationsByUser(userId);
        return ResponseEntity.noContent().build();
    }

}
