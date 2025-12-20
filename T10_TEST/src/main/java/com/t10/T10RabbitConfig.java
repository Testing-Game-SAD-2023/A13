package com.t10;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class T10RabbitConfig {

    // Deve coincidere con quello scritto nell'application.properties
    public static final String REPLY_QUEUE_NAME = "t10.reply.queue";

    @Bean
    public Queue t10ReplyQueue() {
        // true = durable (sopravvive al riavvio di RabbitMQ)
        return new Queue(REPLY_QUEUE_NAME, true);
    }
}
