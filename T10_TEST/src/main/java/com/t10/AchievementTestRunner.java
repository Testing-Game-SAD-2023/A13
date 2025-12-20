package com.t10;

import com.a13.notification.client.dto.NotificationDTO;       // Import dalla Libreria
import com.a13.notification.client.service.NotificationProducer; // Import dalla Libreria
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AchievementTestRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AchievementTestRunner.class);

    private final NotificationProducer notificationProducer;

    // Iniettiamo il Producer fornito dalla libreria condivisa
    public AchievementTestRunner(NotificationProducer notificationProducer) {
        this.notificationProducer = notificationProducer;
    }

    @Override
    public void run(String... args) {
        log.info("=== T10 TEST RUNNER AVVIATO ===");

        // Simula "Achievement sbloccato"
        Long userId = 42L;
        String type = "ACHIEVEMENT_UNLOCKED";
        String title = "Achievement sbloccato!";
        String body = "Complimenti, hai sbloccato l'obiettivo 'Primo test completato'.";

        // Creiamo il DTO usando la classe della libreria
        NotificationDTO dto = new NotificationDTO();
        dto.setUserId(userId);
        dto.setType(type);
        dto.setTitle(title);
        dto.setBody(body);

        // NOTA: Non serve più settare manualmente la replyTo qui.
        // Il NotificationProducer della libreria leggerà 'notification.reply.queue'
        // dalle proprietà e la imposterà da solo.

        log.info("T10 - Invio notifica per User {}: '{}'", userId, title);

        // Invio tramite la libreria
        notificationProducer.sendCreateNotification(dto);

        log.info("T10 - Notifica inviata al broker (Fire-and-Forget). Lavoro terminato.");
    }
}
