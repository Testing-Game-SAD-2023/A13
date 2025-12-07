package com.t10;

import com.t10.NotificationProducer;
import com.t10.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AchievementTestRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AchievementTestRunner.class);

    private final NotificationProducer notificationProducer;

    public AchievementTestRunner(NotificationProducer notificationProducer) {
        this.notificationProducer = notificationProducer;
    }

    @Override
    public void run(String... args) {
        // Simula "Achievement sbloccato"
        Long userId = 42L;
        String type = "ACHIEVEMENT_UNLOCKED";
        String title = "Achievement sbloccato!";
        String body = "Complimenti, hai sbloccato l'obiettivo 'Primo test completato'.";

        NotificationDTO dto = new NotificationDTO();
        dto.setUserId(userId);
        dto.setType(type);
        dto.setTitle(title);
        dto.setBody(body);

        // Stampa i campi che inserirà nel DTO
        log.info("T10 - Creo DTO notifica:");
        log.info("  userId={} | type={} | title={} | body={}",
                dto.getUserId(), dto.getType(), dto.getTitle(), dto.getBody());

        // Invio tramite RabbitMQ al servizio T9
        notificationProducer.sendCreateNotification(dto);

        log.info("T10 - DTO inviato, in attesa della risposta sulla coda {}",
                com.t10.ClientRabbitConfig.REPLY_QUEUE);
    }
}
