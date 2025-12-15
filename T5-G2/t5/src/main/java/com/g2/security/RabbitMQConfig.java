package com.g2.security;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configurazione di RabbitMQ per il servizio T5-G2.
 * Questa classe definisce i bean necessari per la comunicazione asincrona
 * e per la corretta serializzazione/deserializzazione dei messaggi.
 */
@Configuration
public class RabbitMQConfig {

    // Nome dell'exchange, deve corrispondere a quello definito nel Broker
    public static final String EXCHANGE_NAME = "notificationExchange";

    /**
     * Definisce l'exchange. Deve essere dello stesso tipo (TopicExchange)
     * di quello dichiarato nel NotificationBroker per evitare conflitti.
     * @return Un'istanza di TopicExchange.
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    /**
     * Definisce il convertitore di messaggi per serializzare/deserializzare oggetti in JSON.
     * È essenziale che sia T5 che T23 usino lo stesso tipo di converter per evitare errori.
     * @return Un'istanza di Jackson2JsonMessageConverter.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Crea il bean per `AsyncRabbitTemplate`.
     * Questo template specializzato semplifica l'implementazione del pattern request/reply asincrono,
     * gestendo automaticamente la creazione di code di risposta temporanee e la correlazione dei messaggi.
     * @param rabbitTemplate Il RabbitTemplate standard da cui partire.
     * @return Un'istanza di AsyncRabbitTemplate.
     */
    @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate rabbitTemplate) {
        return new AsyncRabbitTemplate(rabbitTemplate);
    }
}
