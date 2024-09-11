package com.g2.t5;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.g2.Model.ClassUT;

@CrossOrigin
@Controller
public class RestController {
    private final RestTemplate restTemplate;
    
    //URL chiamate
    private final String url_IsAuthenticate = "http://t23-g1-app-1:8080/validateToken";
    private final String url_robots         = "http://t4-g18-app-1:3000/robots";
    private final String url_classes        = "http://manvsclass-controller-1:8080/orderbyname";

    @Autowired
    public RestController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //True se autenticato 
    public Boolean IsAuthenticate(String jwt) {
        // Verifica se il JWT è valido prima di fare la richiesta
        if (jwt == null || jwt.isEmpty()) {
            System.out.println("Token JWT non presente o vuoto.");
            return false;
        }

        // Crea il formData con il JWT
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("jwt", jwt);

        try {
            Boolean isIsAuthenticated = restTemplate.postForObject(url_IsAuthenticate, formData, Boolean.class);            
            return (isIsAuthenticated != null && isIsAuthenticated);
        } catch (Exception e) {
            // Gestisci eventuali errori durante la richiesta
            System.out.println("Errore durante l'autenticazione: " + e.getMessage());
            return false;
        }
    }


    public List<String> getLevels(String className) {
        List<String> result = new ArrayList<>();
        List<String> robot_type =  List.of("randoop", "evosuite");

        for (int i = 0; i < 11; i++){
            for(String robot_string: robot_type){
                URI uri = UriComponentsBuilder.fromHttpUrl(url_robots)
                .queryParam("testClassId", className)
                .queryParam("type", robot_string)
                .queryParam("difficulty", i)
                .build()
                .toUri();

                try{
                    // Stefano: Ancora non mi è chiaro perchè fanno così, 
                    // credo che non esista una vera e propria REST API per avere i robots
                    ResponseEntity<Object> response = restTemplate.getForEntity(uri, Object.class);
                    result.add(String.valueOf(i));
                }catch(Exception e){
                    break;
                }
            }  
        }
        return result;
    }
   
    //Stefano: Da Testare
    public List<ClassUT> getClasses() {
        try {
            ResponseEntity<List<ClassUT>> responseEntity = restTemplate.exchange(
                url_classes,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ClassUT>>() {}
            );

            // Verifica che la risposta non sia null e restituisci il corpo
            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                return responseEntity.getBody();
            } else {
                // Gestisci i casi di risposta non riuscita
                throw new RuntimeException("Errore getClasses " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            // Gestisci le eccezioni
            throw new RuntimeException("Errore durante la richiesta: " + e.getMessage(), e);
        }
    }

}
