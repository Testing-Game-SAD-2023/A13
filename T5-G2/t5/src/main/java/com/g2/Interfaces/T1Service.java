package com.g2.Interfaces;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            case "getClassUnderTest" -> {
                if(params.length !=1){
                    throw new IllegalArgumentException("[HANDLEREQUEST] Per 'getClasses' è richiesto 1 parametri.");
                }
                return getClassUnderTest((String)params[0]);
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
            throw new IllegalArgumentException("[HANDLEREQUEST] Erroe  getClasses" + e);
        }
    }

    private ClassUT getClassUnderTest(String nomeCUT){
        final String endpoint = "/downloadFile/";

        Map<String, String> params = new HashMap<>();
        params.put("name", nomeCUT);

        try {
            ClassUT result = restService.CallRestGET(
                endpoint,
                params, 
                ClassUT.class
            );
            return result;
        } catch (RuntimeException e) {
            // Gestisci eventuali errori durante la richiesta
            throw new IllegalArgumentException("[HANDLEREQUEST] Erroe  getClassUnderTest" + e);
        }
    }
}