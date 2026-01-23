package com.a13.notification.client.config;

import com.a13.notification.client.service.NotificationProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationAutoConfiguration {

    // Legge il valore dal file application.properties del servizio che usa la libreria
    @Value("${notification.reply.queue:#{null}}")
    private String replyQueue;

    // Converte tutto in JSON automaticamente
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Crea il producer per inviare notifiche
    @Bean
    public NotificationProducer notificationProducer(RabbitTemplate rabbitTemplate) {
        return new NotificationProducer(rabbitTemplate, replyQueue);
    }
}
