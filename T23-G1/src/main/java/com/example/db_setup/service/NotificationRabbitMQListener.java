package com.example.db_setup.service;

import com.example.db_setup.model.Notification;
import com.example.db_setup.model.Player;
import com.example.db_setup.model.UserProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Componente che si mette in ascolto sulle code RabbitMQ per gestire le operazioni relative alle notifiche.
 * Ogni metodo annotato con @RabbitListener è un "consumer" che processa i messaggi da una coda specifica.
 */
@Component
public class NotificationRabbitMQListener {

    private static final Logger logger = LoggerFactory.getLogger(NotificationRabbitMQListener.class);

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper; // Per convertire oggetti in JSON e viceversa

    // Costanti per RabbitMQ (copiate da BrokerConfiguration.java)
    public static final String EXCHANGE_NAME = "notificationExchange";
    public static final String GET_QUEUE_NAME = "notificationGet";
    public static final String DELETE_QUEUE_NAME = "notificationDelete";
    public static final String CLEAR_QUEUE_NAME = "notificationClear";
    public static final String UPDATE_QUEUE_NAME = "notificationUpdate";
    public static final String NEW_QUEUE_NAME = "notificationNew";
    public static final String RESPONSE_ROUTING_KEY = "routing.notifications.response"; // Routing key per le risposte

    /**
     * Gestisce i messaggi in arrivo sulla coda 'notificationNew'.
     * Riceve i dati per una nuova notifica, trova il profilo utente e salva la notifica nel database.
     * Questo è un esempio di comunicazione "fire-and-forget".
     *
     * @param notificationData Una mappa contenente i dati della notifica (email, titolo, messaggio, tipo).
     */
    @RabbitListener(queues = NEW_QUEUE_NAME)
    public void handleNewNotification(Map<String, String> notificationData) {
        String email = notificationData.get("email");
        String title = notificationData.get("title");
        String message = notificationData.get("message");
        String type = notificationData.getOrDefault("type", "info");

        Player profile = playerService.getUserByEmail(email);

        if (profile == null) {
            logger.error("Profilo non trovato per l'email: {}", email);
            return;
        }

        notificationService.saveNotification(profile.getID(), title, message, type);
        logger.info("Nuova notifica salvata per l'utente {}: {}", email, title);
    }

    /**
     * Gestisce le richieste di recupero notifiche dalla coda 'notificationGet'.
     * Implementa il lato "server" del pattern request/reply asincrono.
     *
     * @param requestData Il payload del messaggio, contenente i parametri della richiesta (email, pagina, dimensione).
     * @param message     Il messaggio AMQP originale, da cui estrarre 'correlationId' e 'replyTo'.
     */
    @RabbitListener(queues = GET_QUEUE_NAME)
    public void handleGetNotifications(Map<String, Object> requestData, org.springframework.amqp.core.Message message) {
        // Estrae le proprietà necessarie per la risposta asincrona.
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();

        if (correlationId == null || replyTo == null) {
            logger.error("Missing correlationId or replyTo in message for getNotifications");
            return;
        }

        String email = (String) requestData.get("email");
        int page = (Integer) requestData.get("page");
        int size = (Integer) requestData.get("size");
        UserProfile profile;

        try {
            profile = playerService.findProfileByEmail(email);
            sendReply(new HashMap<>(), correlationId, replyTo); // Invia una risposta vuota per notificare il client.
        }catch(IllegalArgumentException e) {
            logger.error("Profilo non trovato per l'email: {}", email);
            return;
        }

        // Recupera le notifiche dal database con paginazione.
        Page<Notification> notificationsPage = notificationService.getNotificationsByPlayer(
                profile.getPlayer().getID(), page, size);

        // Prepara la mappa di risposta. La struttura deve corrispondere a quella attesa dal client (NotificationResponse in T5).
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("content", notificationsPage.getContent());
        responseMap.put("totalPages", notificationsPage.getTotalPages());
        responseMap.put("totalElements", notificationsPage.getTotalElements());
        responseMap.put("number", notificationsPage.getNumber());
        responseMap.put("size", notificationsPage.getSize());

        // Invia la risposta.
        sendReply(responseMap, correlationId, replyTo);
        logger.info("Risposta per le notifiche inviata per l'utente {} con correlationId {}", email, correlationId);
    }

    /**
     * Gestisce le richieste di aggiornamento dalla coda 'notificationUpdate'.
     *
     * @param updateData Dati per l'aggiornamento (email, notificationID).
     */
    @RabbitListener(queues = UPDATE_QUEUE_NAME)
    public void handleUpdateNotification(Map<String, String> updateData) {
        String email = updateData.get("email");
        String notificationID = updateData.get("notificationID");
        Boolean isRead = true; // Logica semplificata: assume che l'update sia per marcare come letto.
        UserProfile profile;

        try {
            profile = playerService.findProfileByEmail(email);
        }catch(IllegalArgumentException e) {
            logger.error("Profilo non trovato per l'email: {}", email);
            return;
        }

        try {
            Long notifID = Long.parseLong(notificationID);
            if (isRead) {
                notificationService.markNotificationAsRead(notifID);
            } else {
                notificationService.markNotificationAsNotRead(notifID);
            }
            logger.info("Notifica {} aggiornata per l'utente {}", notificationID, email);
        } catch (NumberFormatException e) {
            logger.error("Invalid Notification ID: {}", notificationID);
        }
    }

    /**
     * Gestisce le richieste di eliminazione dalla coda 'notificationDelete'.
     *
     * @param deleteData Dati per l'eliminazione (email, notificationID).
     */
    @RabbitListener(queues = DELETE_QUEUE_NAME)
    public void handleDeleteNotification(Map<String, String> deleteData) {
        String email = deleteData.get("email");
        String notificationID = deleteData.get("notificationID");
        UserProfile profile;

        try {
            profile = playerService.findProfileByEmail(email);
        }catch(IllegalArgumentException e) {
            logger.error("Profilo non trovato per l'email: {}", email);
            return;
        }

        try {
            Long notifID = Long.parseLong(notificationID);
            long playerID = profile.getPlayer().getID(); // Ottengo l'ID del player

            // Usa un metodo sicuro che verifica anche l'ID del giocatore per evitare eliminazioni incrociate.
            notificationService.deleteNotificationByIdAndPlayerId(notifID, playerID);

            logger.info("Richiesta di eliminazione per notifica {} per l'utente {}", notificationID, email);
        } catch (NumberFormatException e) {
            logger.error("Invalid Notification ID: {}", notificationID);
        }
    }

    /**
     * Gestisce le richieste di pulizia totale dalla coda 'notificationClear'.
     *
     * @param clearData Dati per la pulizia (email).
     */
    @RabbitListener(queues = CLEAR_QUEUE_NAME)
    public void handleClearNotifications(Map<String, String> clearData) {
        String email = clearData.get("email");
        UserProfile profile;

        try {
            profile = playerService.findProfileByEmail(email);
        }catch(IllegalArgumentException e) {
            logger.error("Profilo non trovato per l'email: {}", email);
            return;
        }

        long playerID = profile.getPlayer().getID();
        notificationService.clearNotificationsByPlayer(playerID);
        logger.info("Tutte le notifiche eliminate per l'utente {}", email);
    }

    /**
     * Metodo helper per inviare una risposta al client nel pattern request/reply.
     *
     * @param payload       L'oggetto da inviare come risposta (verrà convertito in JSON).
     * @param correlationId L'ID di correlazione del messaggio originale, per permettere al client di abbinare la risposta.
     * @param replyTo       La coda di destinazione della risposta.
     */
    private void sendReply(Object payload, String correlationId, String replyTo) {
        // Invia la risposta direttamente alla coda specificata in 'replyTo',
        // usando l'exchange di default (vuoto) e la 'replyTo' come routing key.
        rabbitTemplate.convertAndSend("", replyTo, payload, message -> {
            message.getMessageProperties().setCorrelationId(correlationId);
            return message;
        });
    }
}