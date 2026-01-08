package com.groom.manvsclass.service.implementation;

import com.groom.manvsclass.api.ApiGatewayClient;
import com.groom.manvsclass.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final RestTemplate restTemplate;
    private final ApiGatewayClient apiGatewayClient;

    public NotificationServiceImpl(RestTemplate restTemplate, ApiGatewayClient apiGatewayClient) {
        this.restTemplate = restTemplate;
        this.apiGatewayClient = apiGatewayClient;
    }

    // Iniezione di RestTemplate tramite costruttore
    /*
    public NotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

     */

    @Override
    public String sendNotification(String email, Integer studentID, String title, String message, String type) {
        // Verifica che almeno uno dei due identificatori sia fornito
        if (email == null && studentID == null) {
            logger.warn("Tentativo di invio notifica senza email né studentID.");
            return "Errore: Devi fornire almeno un identificatore (email o studentID)";
        }

        // Creazione dei parametri della richiesta
        MultiValueMap<String, String> params = prepareParams(email, studentID, title, message, type);

        try {
            ResponseEntity<String> response = apiGatewayClient.callSendNotification(params);

            if (response.getStatusCode() == HttpStatus.OK) {
                return "Notifica inviata con successo!";
            } else {
                logger.error("Errore nell'invio della notifica: {}", response.getStatusCode());
                return "Errore nell'invio della notifica: " + response.getStatusCode();
            }
        } catch (HttpClientErrorException e) {
            logger.error("Errore HTTP durante l'invio della notifica: {}", e.getStatusCode());
            return "Errore HTTP durante l'invio della notifica: " + e.getStatusCode();
        } catch (RestClientException e) {
            logger.error("Errore di comunicazione con il server: {}", e.getMessage(), e);
            return "Errore di comunicazione con il server.";
        }
    }

    /**
     * Metodo helper per preparare i parametri della richiesta.
     */
    private MultiValueMap<String, String> prepareParams(String email, Integer studentID, String title, String message, String type) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        if (email != null) {
            params.add("email", email);
        }
        if (studentID != null) {
            params.add("studentID", String.valueOf(studentID));
        }
        params.add("title", title);
        params.add("message", message);
        params.add("type", (type != null) ? type : "info"); // Default "info"

        return params;
    }

    @Override
    public List<CompletableFuture<String>> sendNotificationsToUsers(List<Integer> studentIDs, String title, String message, String type) {
        return studentIDs.stream()
                .map(studentID -> CompletableFuture.supplyAsync(() -> sendNotification(null, studentID, title, message, type)))
                .collect(Collectors.toList());
    }

}
