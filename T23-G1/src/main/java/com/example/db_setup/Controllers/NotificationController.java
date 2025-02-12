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
package com.example.db_setup.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.db_setup.Service.NotificationService;
import com.example.db_setup.Service.UserService;
import com.example.db_setup.model.Notification;
import com.example.db_setup.model.User;
import com.example.db_setup.model.UserProfile;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @PostMapping("/new_notification")
    public ResponseEntity<String> updateNotifications(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "studentID", required = false) Integer studentID,
            @RequestParam("title") String title,
            @RequestParam("message") String message,
            @RequestParam(value = "type", required = false, defaultValue = "info") String type) {
    
        // Verifica che almeno uno dei due parametri sia fornito
        if (email == null && studentID == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Devi fornire almeno un identificatore: email o studentID");
        }
    
        User profile = null;
    
        // Se è fornita l'email, cerca per email
        if (email != null) {
            profile = userService.getUserByEmail(email);
        } 
        // Se non è stata trovata con email o se l'email non è fornita, cerca per studentID
        if (profile == null && studentID != null) {
            profile = userService.getUserByID(studentID);
        }
    
        // Se non troviamo il profilo, restituiamo errore
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profilo non trovato");
        }
    
        // Salva la notifica
        notificationService.saveNotification(profile.getID(), title, message, type);
        return ResponseEntity.ok("Notifica inviata con successo");
    }
    

    //Ottieni tutte le notifiche 
    @GetMapping("/get_notifications")
    public ResponseEntity<Page<Notification>> getNotifications(
            @RequestParam("email") String email,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "type", required = false) List<String> types, // Lista diretta
            @RequestParam(value = "isRead", required = false) Boolean isRead) {

        UserProfile profile = userService.findProfileByEmail(email);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Se il client non passa "type", la lista risulterà null
        if (types != null && !types.isEmpty()) {
            if (isRead != null) {
                Page<Notification> notifications = notificationService.getNotificationsByPlayerAndTypesAndIsRead(
                        profile.getUser().getID(), types, isRead, page, size);
                return ResponseEntity.ok(notifications);
            } else {
                Page<Notification> notifications = notificationService.getNotificationsByPlayerAndTypes(
                        profile.getUser().getID(), types, page, size);
                return ResponseEntity.ok(notifications);
            }
        } else if (isRead != null) {
            Page<Notification> notifications = notificationService.getNotificationsByPlayerAndReadStatus(
                    profile.getUser().getID(), isRead, page, size);
            return ResponseEntity.ok(notifications);
        } else {
            Page<Notification> notifications = notificationService.getNotificationsByPlayer(
                    profile.getUser().getID(), page, size);
            return ResponseEntity.ok(notifications);
        }
    }

    //Ottieni solo le notifiche non lette
    @GetMapping("/read_notifications")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@RequestParam("userId") String userID) {
        Integer userID_int = Integer.parseInt(userID);
        User profile = userService.getUserByID(userID_int);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // Puoi aggiungere un messaggio di errore più chiaro
        }
        List<Notification> unreadNotifications = notificationService.getUnreadNotificationsByPlayer(userID_int);
        return ResponseEntity.ok(unreadNotifications);
    }

    //Marca una singola notifica come letta/non letta
    @PostMapping("/read_notification")
    public ResponseEntity<String> Read_Notification(@RequestParam("email") String email,
            @RequestParam("notificationID") String notificationID,
            @RequestParam("isRead") Boolean isRead) {
        UserProfile profile = userService.findProfileByEmail(email);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("[T23 Controller] UserProfile not found");
        }

        try {
            Long notifID = Long.parseLong(notificationID);
            if (isRead) {
                notificationService.markNotificationAsRead(notifID);
            } else {
                notificationService.markNotificationAsNotRead(notifID);
            }
            return ResponseEntity.ok("Notification marked as read successfully");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("[T23 Controller] Invalid Notification ID");
        }
    }

    @DeleteMapping("/remove_notification")
    public ResponseEntity<String> deleteNotification(@RequestParam("email") String email,
            @RequestParam("notificationID") String notificationID) {
        UserProfile profile = userService.findProfileByEmail(email);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("[T23 Controller] UserProfile not found");
        }
        try {
            Long notifID = Long.parseLong(notificationID);
            notificationService.deleteNotification(notifID);
            return ResponseEntity.ok("Notification deleted successfully");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("[T23 Controller] Invalid Notification ID");
        }
    }

    @DeleteMapping("/clear_notifications")
    public ResponseEntity<String> clearNotifications(@RequestParam("email") String email) {
        UserProfile profile = userService.findProfileByEmail(email);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("[T23 Controller] UserProfile not found");
        }
        int playerID = profile.getUser().getID();
        notificationService.clearNotificationsByPlayer(playerID);
        return ResponseEntity.ok("All notifications cleared successfully");
    }

}
