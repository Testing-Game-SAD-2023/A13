package com.g2.session;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisCleanupService implements DisposableBean {

    private final LettuceConnectionFactory lettuceConnectionFactory;

    @Autowired
    public RedisCleanupService(LettuceConnectionFactory lettuceConnectionFactory) {
        this.lettuceConnectionFactory = lettuceConnectionFactory;
    }

    @Override
    public void destroy() {
        try (RedisConnection connection = lettuceConnectionFactory.getConnection()) {
            // Verifica se la connessione è attiva con PING
            String pingResponse = connection.ping();
            if ("PONG".equalsIgnoreCase(pingResponse)) {
                System.out.println("Connessione a Redis attiva, procedo con la pulizia...");
                connection.serverCommands().flushDb();
                System.out.println("Tutte le sessioni sono state rimosse da Redis.");
            } else {
                System.err.println("Errore: Redis non risponde correttamente.");
            }
        } catch (Exception e) {
            System.err.println("Errore di pulizia sessioni: " + e.getMessage());
        }
    }
}
