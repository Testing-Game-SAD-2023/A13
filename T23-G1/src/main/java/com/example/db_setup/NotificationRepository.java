/*
 *   Copyright (c) 2025 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.example.db_setup;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.db_setup.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Trova tutte le notifiche di un utente
    List<Notification> findByPlayerID(int playerID);

    // Trova solo le notifiche non lette di un utente
    List<Notification> findByPlayerIDAndIsReadFalse(int playerID);

    // Trova le notifiche (lette o non lette) con filtro sullo stato
    List<Notification> findByPlayerIDAndIsRead(int playerID, boolean isRead);

    // Trova tutte le notifiche di un utente ordinate dalla più recente alla più vecchia
    List<Notification> findByPlayerIDOrderByTimestampDesc(int playerID);

    // Paginazione delle notifiche per un utente
    Page<Notification> findByPlayerID(int playerID, Pageable pageable);

    // Notifiche non lette per timestamp
    List<Notification> findByPlayerIDAndIsReadFalseOrderByTimestampDesc(int playerID);

    // Segna tutte le notifiche di un utente come lette in un'unica query
    @Transactional
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.playerID = :playerID")
    void markAllAsReadByPlayerID(int playerID);

    //Elimina tutte le notifiche
    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.playerID = :playerID")
    void deleteByPlayerID(int playerID);

    //Setta come letta una notifica
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :notificationID")
    void markAsRead(@Param("notificationID") Long notificationID);

    //Setta come non letta una notifica
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = false WHERE n.id = :notificationID")
    void markAsNotRead(@Param("notificationID") Long notificationID);

    // Trova tutte le notifiche di un certo tipo per un utente
    List<Notification> findByPlayerIDAndType(int playerID, String type);

    // Trova solo le notifiche di un certo tipo e non lette
    List<Notification> findByPlayerIDAndTypeAndIsReadFalse(int playerID, String type);

    // Paginazione delle notifiche per un utente filtrate per tipo
    Page<Notification> findByPlayerIDAndType(int playerID, String type, Pageable pageable);

    // Filtro per playerID, tipo di notifica e stato di lettura
    Page<Notification> findByPlayerIDAndTypeAndIsRead(int playerID, String type, boolean isRead, Pageable pageable);

    // Filtro per playerID e stato di lettura
    Page<Notification> findByPlayerIDAndIsRead(int playerID, boolean isRead, Pageable pageable);

    // Trova le notifiche per un utente che corrispondono a più tipi e stato di lettura
    Page<Notification> findByPlayerIDAndTypeInAndIsRead(int playerID, List<String> types, boolean isRead, Pageable pageable);

    // Trova le notifiche per un utente che corrispondono a più tipi
    Page<Notification> findByPlayerIDAndTypeIn(int playerID, List<String> types, Pageable pageable);
    
    
}
