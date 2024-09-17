package com.g2.Interfaces;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import com.g2.Model.ClassUT;

public class T1Service implements ServiceInterface{
    private final RestService restService;
    private static final String BASE_URL = "http://manvsclass-controller-1:8080/";

    public T1Service(RestTemplate restTemplate){
        this.restService = new RestService(restTemplate, BASE_URL);
    }

    @Override
    public Object handleRequest(String action, Object... params) {
        switch (action) {
            case "getClasses" -> {
                if (params.length > 0) {
                    throw new IllegalArgumentException("[HANDLEREQUEST] Per 'getClasses' è richiesto 0 parametri.");
                }
                return getClasses();
            }
            default -> throw new IllegalArgumentException("[HANDLEREQUEST] Azione non riconosciuta: " + action);
        }
        // Aggiungi altri casi per altre azioni
    }

    //funziona !! Stefano
    private List<ClassUT> getClasses(){
        final String endpoint = "/home";
        try {
            List<ClassUT> result = restService.CallRestGET(
                endpoint,
                null, 
                new ParameterizedTypeReference<List<ClassUT>>() {}    
            );
            return result;
        } catch (RuntimeException e) {
            // Gestisci eventuali errori durante la richiesta
            System.out.println("Errore getClasses: " + e.getMessage());
            return null;
        }
    }
}