package com.controller;

import com.model.Notification;
import com.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // -------------------------------
    // RESTITUISCE LE NOTIFICHE (TUTTE)
    // -------------------------------
    @GetMapping("/get_notifications")
    public ResponseEntity<Page<Notification>> getNotifications(
            @RequestParam("playerId") Long playerId,     // usa direttamente l'ID del player
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "type", required = false) List<String> types,
            @RequestParam(value = "isRead", required = false) Boolean isRead) {

        if (playerId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Page<Notification> notifications;

        if (types != null && !types.isEmpty()) {
            if (isRead != null) {
                notifications = notificationService.getNotificationsByPlayerAndTypesAndIsRead(
                        playerId, types, isRead, page, size);
            }
            else {
                notifications = notificationService.getNotificationsByPlayerAndTypes(
                        playerId, types, page, size);
            }
        }
        else if (isRead != null) {
            notifications = notificationService.getNotificationsByPlayerAndReadStatus(
                    playerId, isRead, page, size);
        }
        else {
            notifications = notificationService.getNotificationsByPlayer(playerId, page, size);
        }

        return ResponseEntity.ok(notifications);
    }

    // -------------------------------
    // RESTITUISCE LE NOTIFICHE NON LETTE
    // -------------------------------
    @GetMapping("/read_notifications")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@RequestParam("playerId") Long playerId) {
        if (playerId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<Notification> unreadNotifications = notificationService.getUnreadNotificationsByPlayer(playerId);
        return ResponseEntity.ok(unreadNotifications);
    }

    // -------------------------------
    // MARCA UNA NOTIFICA LETTA/NON LETTA
    // -------------------------------
    @PostMapping("/read_notification")
    public ResponseEntity<String> readNotification(
            @RequestParam("notificationID") Long notificationID,
            @RequestParam("isRead") Boolean isRead) {

        if (notificationID == null || isRead == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request parameters");
        }

        if (isRead) {
            notificationService.markNotificationAsRead(notificationID);
        } else {
            notificationService.markNotificationAsNotRead(notificationID);
        }

        return ResponseEntity.ok("Notification status updated successfully");
    }

    // -------------------------------
    // ELIMINA UNA NOTIFICA FILTRANDOLA PER ID
    // -------------------------------
    @DeleteMapping("/remove_notification")
    public ResponseEntity<String> deleteNotification(@RequestParam("notificationID") Long notificationID) {
        if (notificationID == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Notification ID");
        }

        notificationService.deleteNotification(notificationID);
        return ResponseEntity.ok("Notification deleted successfully");
    }

    // -------------------------------
    // ELIMINA TUTTE LE NOTIFICHE DI UN UTENTE
    // -------------------------------
    @DeleteMapping("/clear_notifications")
    public ResponseEntity<String> clearNotifications(@RequestParam("playerId") Long playerId) {
        if (playerId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Player ID");
        }

        notificationService.clearNotificationsByPlayer(playerId);
        return ResponseEntity.ok("All notifications cleared successfully");
    }
}
