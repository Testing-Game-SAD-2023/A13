package com.service.sse;

import com.model.dto.NotificationRestDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseConnectionManager {

    // Registro degli emitters per i singoli utenti connessi
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    // Iscrizione all'sse per garantire l'invio delle notifiche live
    public SseEmitter subscribe(Long userId) {

        // Timeout 1 minuto (da tarare)
        SseEmitter emitter = new SseEmitter(60000L);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError((e) -> emitters.remove(userId));

        emitters.put(userId, emitter);
        return emitter;
    }

    // Invio effettivo della notifica all'utente
    public void dispatch(Long userId, NotificationRestDTO notification) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("NOTIFICATION")
                        .data(notification));
            } catch (IOException e) {
                emitters.remove(userId);
            }
        }
    }
}