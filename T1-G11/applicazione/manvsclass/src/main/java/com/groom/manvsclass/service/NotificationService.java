package com.groom.manvsclass.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NotificationService {

    String sendNotification(String email, Integer studentID, String title, String message, String type);

    List<CompletableFuture<String>> sendNotificationsToUsers(List<Integer> studentIDs, String title, String message, String type);
}
