package com.t10.communication;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientRabbitConfig {

    // Coda di reply dedicata a t10
    public static final String REPLY_QUEUE = "notifications.reply.t10";

    // Valori usati per inviare verso T9 (devono coincidere con quelli di T9)
    public static final String NOTIFICATION_EXCHANGE = "notifications.exchange";
    public static final String ROUTING_KEY = "notifications.create";

    @Bean
    public Queue replyQueue() {
        // Coda durabile che riceverà le risposte del servizio T9
        return new Queue(REPLY_QUEUE, true);
    }

    // Converter JSON
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate che usa il converter JSON
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
