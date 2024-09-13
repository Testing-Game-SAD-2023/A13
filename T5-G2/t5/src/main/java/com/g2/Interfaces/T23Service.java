package com.g2.Interfaces;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class T23Service {
    private final RestService restService;
    private static final String BASE_URL = "http://t23-g1-app-1:8080";

    public T23Service(RestTemplate restTemplate){
        this.restService = new RestService(restTemplate, BASE_URL);
    }

    public Boolean GetAuthenticated(String jwt){
        // Verifica se il JWT è valido prima di fare la richiesta
        final String endpoint = "/validateToken";

        if (jwt == null || jwt.isEmpty()) {
            System.out.println("Token JWT non presente o vuoto.");
            return false;
        }

        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("jwt", jwt);
            Boolean isIsAuthenticated = restService.CallRestPost(endpoint, formData, null, Boolean.class);    
            return (isIsAuthenticated != null && isIsAuthenticated);
        } catch (Exception e) {
            // Gestisci eventuali errori durante la richiesta
            System.out.println("Errore durante l'autenticazione: " + e.getMessage());
            return false;
        }
    }

}
