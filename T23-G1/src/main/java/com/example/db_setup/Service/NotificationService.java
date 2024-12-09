package com.example.db_setup.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.db_setup.Notification;
import com.example.db_setup.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;


    public void saveNotification(int playerID, String titolo, String message) {
        Notification notification = new Notification(playerID, titolo, message, LocalDateTime.now(), false);
        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByPlayer(int playerID) {
        return notificationRepository.findByPlayerID(playerID);
    }

    public void markNotificationAsRead(Long notificationID) {
        Optional<Notification> optionalNotification = notificationRepository.findById(notificationID);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setIsRead(true);
            notificationRepository.save(notification);
        } else {
            throw new RuntimeException("Notifica con ID " + notificationID + " non trovata.");
        }
    }

    public void deleteNotification(Long notificationID) {
        if (notificationRepository.existsById(notificationID)) {
            notificationRepository.deleteById(notificationID);
        } else {
            throw new RuntimeException("Notifica con ID " + notificationID + " non trovata.");
        }
    }

    public void clearNotificationsByPlayer(int playerID) {
        List<Notification> notifications = notificationRepository.findByPlayerID(playerID);
        notificationRepository.deleteAll(notifications);
    }
}

