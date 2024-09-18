package com.g2.Interfaces;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


public class RestService{

    private final String BASE_URL;
    private final RestTemplate restTemplate;

    public RestService(RestTemplate restTemplate, String BASE_URL){
        this.restTemplate = restTemplate;
        this.BASE_URL = BASE_URL;
    }

    //costruisce un uri con partendo dal base_url e endpoint, poi aggiunge eventuali parametri extra
    private String Build_uri(String endpoint, Map<String, String> queryParams){
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL).path(endpoint);
        // Aggiungi i parametri di query variabili
        if (queryParams != null && !queryParams.isEmpty()) {
            // Aggiungi i parametri di query variabili
            for (Map.Entry<String, String> param : queryParams.entrySet()) {
                builder.queryParam(param.getKey(), param.getValue());
            }
        }
        String url = builder.build().toUriString();
        System.out.println(url);
        return url;
    }

    public <R> R CallRestGET(String endpoint, Map<String, String> queryParams, Class<R> responseType) {
        try {
            // Validazione degli input
            if (endpoint == null || endpoint.isEmpty()) {
                throw new IllegalArgumentException("L'endpoint non può essere nullo o vuoto");
            }
            // Costruisci l'URL
            String url = Build_uri(endpoint, queryParams);
            
            // Esegui la chiamata GET
            ResponseEntity<R> response = restTemplate.getForEntity(url, responseType);
            
            // Verifica la risposta
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RestClientException("Chiamata GET fallita con stato: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Gestione delle eccezioni HTTP specifiche per errori client o server
            throw new RestClientException("Chiamata GET fallita con stato: " + e);
        } catch (RestClientException | IllegalArgumentException e) {
            // Gestione generica degli errori REST (es. timeout, connessione fallita)
            throw new RestClientException("Chiamata GET fallita con stato: " + e);
        }
    }
    //versione overloaded per gestire liste di classi
    public <R> List<R> CallRestGET(String endpoint, Map<String, String> queryParams, ParameterizedTypeReference<List<R>> responseType) {
        try {
            // Validazione degli input
            if (endpoint == null || endpoint.isEmpty()) {
                throw new IllegalArgumentException("L'endpoint non può essere nullo o vuoto");
            }
            if (responseType == null) {
                throw new IllegalArgumentException("Il tipo di risposta non può essere nullo");
            }

            // Costruisci l'URL
            String url = Build_uri(endpoint, queryParams);
            // Esegui la chiamata GET
            ResponseEntity<List<R>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

            // Verifica la risposta
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RestClientException("[CallRestGET] Chiamata GET fallita con stato: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Gestione delle eccezioni HTTP specifiche
            throw new RestClientException("[CallRestGET] Chiamata GET fallita con stato: " + e);
        } catch (RestClientException | IllegalArgumentException e) {
            // Gestione generica degli errori REST
            throw new RestClientException("[CallRestGET] Chiamata GET fallita con stato: " + e);
        }        
    }

    public <R> R CallRestPost(String endpoint, MultiValueMap<String, String> formData, Map<String, String> queryParams, Class<R> responseType) {
        try {
            // Validazione degli input
            if (endpoint == null || endpoint.isEmpty()) {
                throw new IllegalArgumentException("Endpoint non può essere nullo o vuoto");
            }
            if (formData == null) {
                throw new IllegalArgumentException("formData non può essere nullo");
            }

            // Costruisci l'URL
            String url = Build_uri(endpoint, queryParams);

            // Aggiungi eventuali headers se necessario (opzionale)
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

            // Esegui la chiamata POST
            ResponseEntity<R> response = restTemplate.postForEntity(url, requestEntity, responseType);
            
            // Verifica la risposta
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RestClientException("[CallRestPost] Chiamata POST fallita con stato: " + response.getStatusCode());
            }

        } catch (RestClientException | IllegalArgumentException e) {
            // Gestione degli errori REST (es. timeout, connessione fallita, ecc.)
            throw new RestClientException("[CallRestPost] Chiamata POST fallita con stato: " + e);
        }
    }
}