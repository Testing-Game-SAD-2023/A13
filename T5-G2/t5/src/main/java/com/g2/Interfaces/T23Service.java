package com.g2.Interfaces;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.g2.Model.User;

public class T23Service extends BaseService {

    private static final String BASE_URL = "http://t23-g1-app-1:8080";

    public T23Service(RestTemplate restTemplate) {
        super(restTemplate, BASE_URL);

        // Registrazione delle azioni
        registerAction("GetAuthenticated", new ServiceActionDefinition(
            params -> GetAuthenticated((String) params[0]),
            String.class
        ));

        registerAction("GetUsers", new ServiceActionDefinition(
            params ->  GetUsers()  //metodo senza parametri
        ));
    }

    // Metodo per l'autenticazione
    private Boolean GetAuthenticated(String jwt) {
        final String endpoint = "/validateToken";

        // Verifica se il JWT è valido prima di fare la richiesta
        if (jwt == null || jwt.isEmpty()) {
            System.out.println("[GETAUTHENTICATED] Token JWT non presente o vuoto.");
            return false;
        }

        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("jwt", jwt);

            // Chiamata POST utilizzando il metodo della classe base
            Boolean isAuthenticated = callRestPost(endpoint, formData, null, Boolean.class);
            return isAuthenticated != null && isAuthenticated;
        } catch (Exception e) {
            // Gestione degli errori durante la richiesta
            throw new IllegalArgumentException("[GETAUTHENTICATED] Errore durante l'autenticazione: " + e.getMessage());
        }
    }

    // Metodo per ottenere la lista degli utenti
    private List<User> GetUsers() {
        final String endpoint = "/students_list";
        try {
            // Chiamata GET utilizzando il metodo della classe base per ottenere una lista di utenti
            return callRestGET(endpoint, null, new ParameterizedTypeReference<List<User>>() {});
        } catch (RuntimeException e) {
            // Gestione degli errori durante la richiesta
            throw new IllegalArgumentException("[GETUSERS] Errore durante il recupero degli utenti: " + e.getMessage());
        }
    }
}
