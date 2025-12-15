package com.BrokerService;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe principale che avvia l'applicazione Spring Boot per il NotificationBroker.
 * Questo servizio ha il solo scopo di definire e creare l'infrastruttura RabbitMQ
 * (exchange, code, binding) che verrà utilizzata dagli altri microservizi.
 */
@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    /**
     * Metodo main, punto di ingresso dell'applicazione.
     * @param args Argomenti passati dalla riga di comando (non utilizzati in questo caso).
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Questo bean di tipo CommandLineRunner risolve un potenziale problema di ordine di avvio.
     * A volte, Spring potrebbe non creare le code e gli exchange abbastanza velocemente
     * prima che altri servizi provino a connettersi.
     *
     * Iniettando `RabbitAdmin` qui, forziamo la sua inizializzazione precoce.
     * La chiamata a `rabbitAdmin.initialize()` garantisce che tutte le dichiarazioni
     * di exchange, code e binding definite in `BrokerConfiguration` vengano eseguite
     * esplicitamente all'avvio dell'applicazione.
     *
     * @param rabbitAdmin Il bean di amministrazione di RabbitMQ, gestito da Spring.
     * @return Un'azione da eseguire all'avvio.
     */
    @Bean
    public CommandLineRunner forceAmqpAdminInitialization(RabbitAdmin rabbitAdmin) {
        return args -> {
            log.info("AmqpAdmin initialized. RabbitMQ topology declaration will now proceed.");
            // Inizializza esplicitamente RabbitAdmin per dichiarare code ed exchange.
            rabbitAdmin.initialize();
        };
    }
}
