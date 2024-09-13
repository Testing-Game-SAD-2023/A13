package com.g2.Interfaces;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import com.g2.Model.ClassUT;

public class T1Service {
    private final RestService restService;
    private static final String BASE_URL = "http://manvsclass-controller-1:8080/";

    public T1Service(RestTemplate restTemplate){
        this.restService = new RestService(restTemplate, BASE_URL);
    }

    public List<ClassUT> getClasses(){
        final String endpoint = "/orderbyname";
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