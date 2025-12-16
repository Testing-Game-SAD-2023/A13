package com.g2.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.g2.model.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.RabbitConverterFuture;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class NotificationService extends BaseServiceBroker {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final ObjectMapper mapper;

    // --- Costanti per la comunicazione con il servizio di Notifiche via RabbitMQ ---
    public static final String EXCHANGE_NAME = "notificationExchange"; // Nome dell'exchange a cui inviare i messaggi
    public static final String GET_ROUTING_KEY = "routing.get"; // Routing key per richiedere le notifiche
    public static final String DELETE_ROUTING_KEY = "routing.delete"; // Routing key per eliminare una notifica
    public static final String CLEAR_ROUTING_KEY = "routing.clear"; // Routing key per pulire tutte le notifiche
    public static final String UPDATE_ROUTING_KEY = "routing.update"; // Routing key per aggiornare una notifica (es. segnarla come letta)
    public static final String NEW_ROUTING_KEY = "routing.new"; // Routing key per creare una nuova notifica

    public NotificationService(RabbitTemplate rabbitTemplate, AsyncRabbitTemplate asyncRabbitTemplate) {
        super(rabbitTemplate,asyncRabbitTemplate);
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        registerActions();
    }

    private void registerActions() {
        registerAction("newNotification", new ServiceActionDefinition(
                params -> newNotification((String) params[0], (String) params[1], (String) params[2]),
                String.class, String.class, String.class
        ));

        registerAction("getNotifications", new ServiceActionDefinition(
                params -> getNotifications((String) params[0], (Integer) params[1], (Integer) params[2]),
                String.class, Integer.class, Integer.class
        ));

        registerAction("updateNotification", new ServiceActionDefinition(
                params -> updateNotification((String) params[0], (String) params[1]),
                String.class, String.class
        ));

        registerAction("deleteNotification", new ServiceActionDefinition(
                params -> deleteNotification((String) params[0], (String) params[1]),
                String.class, String.class
        ));

        registerAction("clearNotifications", new ServiceActionDefinition(
                params -> clearNotifications((String) params[0]),
                String.class
        ));
    }
    /**
     * Invia una richiesta per creare una nuova notifica.
     * Questa è una comunicazione "fire-and-forget": inviamo il messaggio e non aspettiamo una risposta.
     *
     * @param userEmail L'email dell'utente a cui inviare la notifica.
     * @param title     Il titolo della notifica.
     * @param message   Il corpo del messaggio.
     * @return Una stringa di conferma.
     */
    private String newNotification(String userEmail, String title, String message) {
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("email", userEmail);
        notificationData.put("title", title);
        notificationData.put("message", message);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, NEW_ROUTING_KEY, notificationData);
        return "Notification sent via RabbitMQ";
    }
    /**
     * Recupera le notifiche per un utente in modo asincrono utilizzando il pattern request/reply.
     * 1. Invia un messaggio sulla coda 'notificationGet'.
     * 2. `AsyncRabbitTemplate` crea una coda di risposta temporanea e imposta le proprietà 'reply-to' e 'correlation-id' nel messaggio.
     * 3. Il servizio T23-G1 riceve il messaggio, processa la richiesta e invia la risposta alla coda specificata in 'reply-to'.
     * 4. Questo metodo attende la risposta (con un timeout) e la deserializza.
     *
     * @param userEmail L'email dell'utente.
     * @param page      Il numero di pagina da recuperare.
     * @param size      La dimensione della pagina.
     * @return Un oggetto NotificationResponse contenente le notifiche e le informazioni di paginazione.
     */
    public NotificationResponse getNotifications(String userEmail, int page, int size) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("email", userEmail);
        requestData.put("page", page);
        requestData.put("size", size);

        logger.info("Sending 'getNotifications' request to RabbitMQ for user: {}", userEmail);

        try {
            // Invia il messaggio e ottiene un "Future" che conterrà la risposta.
            RabbitConverterFuture<Object> future = asyncRabbitTemplate.convertSendAndReceive(EXCHANGE_NAME, GET_ROUTING_KEY, requestData);

            // Attende la risposta per un massimo di 60 secondi.
            Object response = future.get(60, TimeUnit.SECONDS);

            if (response instanceof Map) {
                // Se la risposta è una mappa (come ci aspettiamo), la convertiamo nel nostro DTO.
                return mapper.convertValue(response, NotificationResponse.class);
            } else if (response == null) {
                logger.warn("Received null response for getNotifications for user {}", userEmail);
                return new NotificationResponse();
            }

            logger.error("Unexpected response type for getNotifications: {}", response.getClass().getName());
            return new NotificationResponse();
        } catch (Exception e) {
            logger.error("Error getting notifications via RabbitMQ for user {}: {}", userEmail, e.getMessage());
            Thread.currentThread().interrupt(); // Ripristina lo stato di interruzione in caso di InterruptedException
            return new NotificationResponse();
        }
    }
    /**
     * Invia una richiesta per aggiornare una notifica (es. per segnarla come letta).
     * Comunicazione "fire-and-forget".
     *
     * @param userEmail      L'email dell'utente.
     * @param notificationID L'ID della notifica da aggiornare.
     * @return Una stringa di conferma.
     */
    public String updateNotification(String userEmail, String notificationID) {
        Map<String, String> updateData = new HashMap<>();
        updateData.put("email", userEmail);
        updateData.put("notificationID", notificationID);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, UPDATE_ROUTING_KEY, updateData);
        return "Update notification request sent via RabbitMQ";
    }
    /**
     * Invia una richiesta per eliminare una singola notifica.
     * Comunicazione "fire-and-forget".
     *
     * @param userEmail      L'email dell'utente.
     * @param notificationID L'ID della notifica da eliminare.
     * @return Una stringa di conferma.
     */
    public String deleteNotification(String userEmail, String notificationID) {
        Map<String, String> deleteData = new HashMap<>();
        deleteData.put("email", userEmail);
        deleteData.put("notificationID", notificationID);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, DELETE_ROUTING_KEY, deleteData);
        return "Delete notification request sent via RabbitMQ";
    }
    /**
     * Invia una richiesta per eliminare tutte le notifiche di un utente.
     * Comunicazione "fire-and-forget".
     *
     * @param userEmail L'email dell'utente.
     * @return Una stringa di conferma.
     */
    public String clearNotifications(String userEmail) {
        Map<String, String> clearData = new HashMap<>();
        clearData.put("email", userEmail);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, CLEAR_ROUTING_KEY, clearData);
        return "Clear notifications request sent via RabbitMQ";
    }
}
