package com.communication;

import com.a13.notification.client.config.NotificationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConfig.class);

    // Utilizzo delle costanti della libreria condivisa
    public static final String NOTIFICATION_EXCHANGE = NotificationConfig.EXCHANGE;
    public static final String ROUTING_KEY = NotificationConfig.ROUTING_KEY;

    // Nome della coda fisica del servizio di notifica
    public static final String NOTIFICATION_QUEUE = "notifications.create.queue";

    @PostConstruct
    public void init() {
        log.info("=== RabbitMQConfig CARICATO ===");
        log.info("Exchange: {}", NOTIFICATION_EXCHANGE);
        log.info("Queue: {}", NOTIFICATION_QUEUE);
        log.info("Routing Key: {}", ROUTING_KEY);
    }

    @Bean
    public Queue notificationQueue() {
        log.info("Crezione della Queue: {}", NOTIFICATION_QUEUE);
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public TopicExchange notificationExchange() {
        log.info("Creazione dell'Exchange: {}", NOTIFICATION_EXCHANGE);
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Binding notificationBinding() {
        log.info("Crezione Binding tra Queue e Exchange");
        return BindingBuilder
                .bind(notificationQueue())
                .to(notificationExchange())
                .with(ROUTING_KEY);
    }


    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);

        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("*");

        converter.setClassMapper(classMapper);
        return converter;
    }

    /**
     * Configurazione della Factory dei listener per l'utilizzo del convrtitore custom piuttosto che quello
     * standard
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                               Jackson2JsonMessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter); // Forza l'uso del JSON
        return factory;
    }

    // RabbitTemplate per inviare messaggi (es. risposte) usando JSON
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
