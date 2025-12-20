package com.a13.notification.client.config;

import com.a13.notification.client.service.NotificationProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value; // Import necessario
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationAutoConfiguration {

    // Legge il valore dal file application.properties dell'app che usa la libreria
    @Value("${notification.reply.queue:#{null}}")
    private String replyQueue;

    // 1. Questo converte tutto in JSON automaticamente
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 2. Questo crea il tuo servizio per inviare notifiche
    @Bean
    public NotificationProducer notificationProducer(RabbitTemplate rabbitTemplate) {
        // Passiamo esplicitamente il valore letto al costruttore
        return new NotificationProducer(rabbitTemplate, replyQueue);
    }
}
