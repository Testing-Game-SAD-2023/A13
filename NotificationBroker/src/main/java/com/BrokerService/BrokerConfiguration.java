package com.BrokerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Configurazione centrale per RabbitMQ.
 * Questa classe definisce l'infrastruttura di base per la comunicazione tramite messaggi:
 * - Exchange: il punto di smistamento dei messaggi.
 * - Code (Queues): le "caselle di posta" dove i messaggi vengono accumulati.
 * - Binding: le regole che collegano l'exchange alle code.
 * - Message Converter: il componente che serializza/deserializza gli oggetti Java in JSON.
 */
@Configuration
public class BrokerConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BrokerConfiguration.class);

    // --- Nomi e Proprietà dell'Exchange ---
    public static final String EXCHANGE_NAME = "notificationExchange"; // Nome dell'exchange principale
    public static final boolean DURABLE = true; // Rende l'exchange e le code persistenti ai riavvii del broker
    public static final boolean AUTO_DESTROY = false; // Impedisce che l'exchange venga distrutto quando non è più in uso

    // --- Nomi delle Code (Queues) ---
    // Ogni coda è dedicata a un tipo specifico di operazione sulle notifiche.
    public static final String GET_QUEUE_NAME = "notificationGet";       // Coda per le richieste di recupero notifiche
    public static final String DELETE_QUEUE_NAME = "notificationDelete";   // Coda per le richieste di eliminazione
    public static final String CLEAR_QUEUE_NAME = "notificationClear";     // Coda per pulire tutte le notifiche
    public static final String UPDATE_QUEUE_NAME = "notificationUpdate";   // Coda per aggiornare una notifica
    public static final String NEW_QUEUE_NAME = "notificationNew";         // Coda per creare nuove notifiche

    // --- Routing Keys ---
    // Le routing key sono usate dall'exchange (di tipo Topic) per decidere a quale coda inoltrare un messaggio.
    public static final String GET_ROUTING_KEY = "routing.get";
    public static final String DELETE_ROUTING_KEY = "routing.delete";
    public static final String CLEAR_ROUTING_KEY = "routing.clear";
    public static final String UPDATE_ROUTING_KEY = "routing.update";
    public static final String NEW_ROUTING_KEY = "routing.new";

    /**
     * Definisce l'exchange principale come un TopicExchange.
     * Un TopicExchange permette un routing flessibile basato su pattern matching delle routing key.
     * @return Un'istanza di TopicExchange.
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME, DURABLE, AUTO_DESTROY);
    }

    /**
     * Definisce il convertitore di messaggi.
     * Utilizziamo Jackson2JsonMessageConverter per convertire automaticamente gli oggetti Java in JSON e viceversa.
     * Questo è fondamentale per evitare errori di conversione tra i servizi.
     * @return Un'istanza del MessageConverter.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Definisce il RabbitAdmin, un componente di Spring AMQP che gestisce la dichiarazione
     * automatica di exchange, code e binding all'avvio dell'applicazione.
     * @param connectionFactory La factory per creare connessioni al broker RabbitMQ.
     * @return Un'istanza di RabbitAdmin.
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * Dichiara tutte le code e i relativi binding all'exchange.
     * Questo metodo crea le code se non esistono e le collega all'exchange
     * tramite le routing key specificate.
     * @param exchange L'exchange principale a cui collegare le code.
     * @return Un oggetto Declarables che contiene tutte le definizioni di code e binding.
     */
    @Bean
    public Declarables topicBindings(TopicExchange exchange) {
        // Definizione di tutte le code, specificando che devono essere durevoli.
        Queue getQueue = new Queue(GET_QUEUE_NAME, DURABLE);
        Queue deleteQueue = new Queue(DELETE_QUEUE_NAME, DURABLE);
        Queue newQueue = new Queue(NEW_QUEUE_NAME, DURABLE);
        Queue clearQueue = new Queue(CLEAR_QUEUE_NAME, DURABLE);
        Queue updateQueue = new Queue(UPDATE_QUEUE_NAME, DURABLE);

        // Ritorna un oggetto che contiene tutte le dichiarazioni.
        // Spring AMQP si occuperà di crearle sul broker.
        return new Declarables(
                getQueue, deleteQueue, newQueue, clearQueue, updateQueue,

                // --- Definizione dei Binding ---
                // Collega ogni coda all'exchange usando la sua routing key.
                BindingBuilder.bind(getQueue).to(exchange).with(GET_ROUTING_KEY),
                BindingBuilder.bind(newQueue).to(exchange).with(NEW_ROUTING_KEY),
                BindingBuilder.bind(deleteQueue).to(exchange).with(DELETE_ROUTING_KEY),
                BindingBuilder.bind(updateQueue).to(exchange).with(UPDATE_ROUTING_KEY),
                BindingBuilder.bind(clearQueue).to(exchange).with(CLEAR_ROUTING_KEY)
        );
    }

}
