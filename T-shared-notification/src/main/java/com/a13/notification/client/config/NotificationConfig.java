package com.a13.notification.client.config;

public class NotificationConfig {

    // Nome dell'exchange su cui pubblicare le notifiche
    // Deve coincidere con quello configurato nel Notification Service (T9)
    public static final String EXCHANGE = "notifications.exchange";

    // Routing key usata per creare una nuova notifica
    public static final String ROUTING_KEY = "notifications.create";

    // Evitiamo che la classe venga istanziata per sbaglio
    private NotificationConfig() {
    }
}
