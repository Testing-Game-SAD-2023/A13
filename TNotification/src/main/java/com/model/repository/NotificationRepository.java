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


}
