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

import com.example.db_setup.Notification;
import com.example.db_setup.Service.NotificationService;
import com.example.db_setup.Service.UserService;
import com.example.db_setup.User;
import com.example.db_setup.UserProfile;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @PostMapping("/new_notification")
    public ResponseEntity<String> updateNotifications(@RequestParam("email") String email,
            @RequestParam("title") String title,
            @RequestParam("message") String message) {
        UserProfile profile = userService.findProfileByEmail(email);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Profile not found");
        }
        notificationService.saveNotification(profile.getUser().getID(), title, message);
        return ResponseEntity.ok("Profile notifications updated successfully");
    }

    @GetMapping("/notifications")
    public ResponseEntity<Page<Notification>> getNotifications(
            @RequestParam("email") String email, 
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        UserProfile profile = userService.findProfileByEmail(email);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Page<Notification> notifications = notificationService.getNotificationsByPlayer(profile.getUser().getID(), page, size);
        return ResponseEntity.ok(notifications);
    }

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

    //Marca una singola notifica come letta
    @PostMapping("/update_notification")
    public ResponseEntity<String> updateNotification(@RequestParam("email") String email, 
                                                     @RequestParam("notificationID") String notificationID) {
        UserProfile profile = userService.findProfileByEmail(email);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("[T23 Controller] UserProfile not found");
        }
    
        try {
            Long notifID = Long.parseLong(notificationID);
            notificationService.markNotificationAsRead(notifID);
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
