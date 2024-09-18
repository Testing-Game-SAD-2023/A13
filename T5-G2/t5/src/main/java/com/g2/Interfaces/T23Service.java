package com.g2.Interfaces;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class T23Service implements ServiceInterface{
    private final RestService restService;
    private static final String BASE_URL = "http://t23-g1-app-1:8080";

    public T23Service(RestTemplate restTemplate){
        this.restService = new RestService(restTemplate, BASE_URL);
    }

    @Override
    public Object handleRequest(String action, Object... params) {
        switch (action) {
            case "GetAuthenticated" -> {
                if (params.length != 1) {
                    throw new IllegalArgumentException("[HANDLEREQUEST] Per 'GetAuthenticated' è richiesto 1 parametro.");
                }
                if (!(params[0] instanceof String)) {
                    throw new IllegalArgumentException("[HANDLEREQUEST] Il parametro per 'GetAuthenticated' deve essere una stringa.");
                }
                return (boolean) GetAuthenticated((String) params[0]);
            }
            default -> throw new IllegalArgumentException("[HANDLEREQUEST] Azione non riconosciuta: " + action);
        }
        // Aggiungi altri casi per altre azioni
    }
    
    private Boolean GetAuthenticated(String jwt){
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
