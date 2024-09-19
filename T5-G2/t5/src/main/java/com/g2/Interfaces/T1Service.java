package com.g2.Interfaces;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import com.g2.Model.ClassUT;
import com.g2.Model.Player;

public class T1Service implements ServiceInterface{
    private final RestService restService;
    private static final String BASE_URL = "http://manvsclass-controller-1:8080";

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

    //non funziona, mistero della fede
    private String getClassUnderTest(String nomeCUT){
        //Stefano: i deficienti hanno definito un api senza parametri 
        //quindi devo costruire a mano un URL con questa struttura
        final String endpoint = "/api/downloadFile/" + nomeCUT;
        try {
            byte[] result = restService.CallRestGET(
                endpoint,
                null,
                byte[].class
            );

            String String_class = new String(result, StandardCharsets.UTF_8);
            System.out.println(String_class);
            return removeBOM(String_class);
        } catch (RuntimeException e) {
            // Gestisci eventuali errori durante la richiesta
            throw new IllegalArgumentException("[HANDLEREQUEST] Errore  getClassUnderTest" + e);
        }
    }

    //DUMMY
    private List<Player> getPlayer(){
        return null;
    }

    private String removeBOM(String str) {
        if (str != null && str.startsWith("\uFEFF")) {
            return str.substring(1);
        }
        return str;
    }

}