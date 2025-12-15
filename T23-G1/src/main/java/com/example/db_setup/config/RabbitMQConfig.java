package com.example.db_setup.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configurazione di RabbitMQ per il servizio T23-G1.
 * Questa classe definisce i bean necessari per la corretta (de)serializzazione dei messaggi.
 */
@Configuration
public class RabbitMQConfig {

    /**
     * Definisce il convertitore di messaggi per l'intera applicazione.
     * Utilizziamo un `Jackson2JsonMessageConverter` personalizzato per garantire
     * che i tipi di data e ora di Java 8 (come `LocalDateTime`) siano gestiti correttamente.
     *
     * @return Un'istanza del MessageConverter configurato.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        // Creiamo un ObjectMapper, il cuore di Jackson per la (de)serializzazione.
        ObjectMapper objectMapper = new ObjectMapper();

        // Registriamo il modulo JavaTimeModule. Questo modulo insegna a Jackson
        // come gestire i tipi di data/ora di Java 8 (es. LocalDateTime, LocalDate).
        // Senza questo, Jackson non sa come convertirli in JSON e lancia un'eccezione.
        objectMapper.registerModule(new JavaTimeModule());

        // Disabilitiamo la scrittura delle date come timestamp numerici (es. 1672531200.123456789).
        // In questo modo, le date verranno scritte in un formato stringa standard ISO-8601 (es. "2025-01-01T12:00:00"),
        // che è più leggibile e interoperabile.
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Creiamo il convertitore di messaggi di Spring AMQP passandogli il nostro ObjectMapper personalizzato.
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
