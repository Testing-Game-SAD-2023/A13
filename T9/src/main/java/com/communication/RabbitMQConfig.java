package com.communication;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class RabbitMQConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConfig.class);

    public static final String NOTIFICATION_EXCHANGE = "notifications.exchange";
    public static final String NOTIFICATION_QUEUE = "notifications.create.queue";
    public static final String ROUTING_KEY = "notifications.create";

    @PostConstruct
    public void init() {
        log.info("=== RabbitMQConfig CARICATO ===");
        log.info("Exchange: {}", NOTIFICATION_EXCHANGE);
        log.info("Queue: {}", NOTIFICATION_QUEUE);
        log.info("Routing Key: {}", ROUTING_KEY);
    }

    @Bean
    public Queue notificationQueue() {
        log.info("Creando Queue: {}", NOTIFICATION_QUEUE);
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public TopicExchange notificationExchange() {
        log.info("Creando Exchange: {}", NOTIFICATION_EXCHANGE);
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Binding notificationBinding() {
        log.info("Creando Binding tra Queue e Exchange");
        return BindingBuilder
                .bind(notificationQueue())
                .to(notificationExchange())
                .with(ROUTING_KEY);
    }
}