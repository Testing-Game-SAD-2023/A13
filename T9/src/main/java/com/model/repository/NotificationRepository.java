package com.model.repository;

import com.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Recupero tutte le notifiche di un utente
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Recupero notifiche non ancora lette per un utente
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);

    // Recupero notifiche in base al tipo (eventualmente filtrate per utente)
    List<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, String type);

    // Segna una singola notifica come letta
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :notificationId")
    int markAsRead(Long notificationId);

    // Segna tutte le notifiche di un utente come lette
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.userId = :userId AND n.isRead = false")
    int markAllAsReadByUserId(Long userId);
}
