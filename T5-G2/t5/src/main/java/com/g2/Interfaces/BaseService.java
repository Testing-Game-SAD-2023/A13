package com.g2.Interfaces;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
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

public abstract class BaseService implements ServiceInterface {

    protected final RestTemplate restTemplate;
    private final String baseUrl;
    protected final Map<String, ServiceActionDefinition> actions = new HashMap<>();

    // Costruttore della classe base
    protected BaseService(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    // Metodo per registrare un'azione
    protected void registerAction(String actionName, ServiceActionDefinition actionDefinition) {
        actions.put(actionName, actionDefinition);
    }

    @Override
    public Object handleRequest(String action, Object... params) {
        try {
            ServiceActionDefinition actionDefinition = actions.get(action);

            if (actionDefinition == null) {
                throw new IllegalArgumentException("[HANDLEREQUEST] Azione non riconosciuta: " + action);
            }

            // Esegui la funzione associata all'azione con i parametri validati
            return actionDefinition.execute(params);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Metodi per le chiamate REST 
    // Costruisce un URI partendo dal baseUrl e dall'endpoint, aggiungendo eventuali parametri extra
    private String buildUri(String endpoint, Map<String, String> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl).path(endpoint);
        if (queryParams != null && !queryParams.isEmpty()) {
            for (Map.Entry<String, String> param : queryParams.entrySet()) {
                builder.queryParam(param.getKey(), param.getValue());
            }
        }
        String url = builder.build().toUriString();
        System.out.println(url);
        return url;
    }

    // Metodo per chiamate GET che restituiscono un singolo oggetto
    protected <R> R callRestGET(String endpoint, Map<String, String> queryParams, Class<R> responseType) {
        try {
            if (endpoint == null || endpoint.isEmpty()) {
                throw new IllegalArgumentException("L'endpoint non può essere nullo o vuoto");
            }
            String url = buildUri(endpoint, queryParams);
            ResponseEntity<R> response = restTemplate.getForEntity(url, responseType);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RestClientException("Chiamata GET fallita con stato: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RestClientException("Chiamata GET fallita con stato: " + e);
        } catch (RestClientException | IllegalArgumentException e) {
            throw new RestClientException("Chiamata GET fallita con stato: " + e);
        }
    }

    // Metodo per chiamate GET che restituiscono una lista di oggetti
    protected <R> List<R> callRestGET(String endpoint, Map<String, String> queryParams, ParameterizedTypeReference<List<R>> responseType) {
        try {
            if (endpoint == null || endpoint.isEmpty()) {
                throw new IllegalArgumentException("L'endpoint non può essere nullo o vuoto");
            }
            if (responseType == null) {
                throw new IllegalArgumentException("Il tipo di risposta non può essere nullo");
            }
            String url = buildUri(endpoint, queryParams);
            ResponseEntity<List<R>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RestClientException("[CallRestGET] Chiamata GET fallita con stato: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RestClientException("[CallRestGET] Chiamata GET fallita con stato: " + e);
        } catch (RestClientException | IllegalArgumentException e) {
            //throw new RestClientException("[CallRestGET] Chiamata GET fallita con stato: " + e);
            return new ArrayList<R>();
        }
    }

    // Metodo per chiamate POST
    protected <R> R callRestPost(String endpoint, MultiValueMap<String, String> formData, Map<String, String> queryParams, Class<R> responseType) {
        try {
            if (endpoint == null || endpoint.isEmpty()) {
                throw new IllegalArgumentException("L'endpoint non può essere nullo o vuoto");
            }
            if (formData == null) {
                throw new IllegalArgumentException("formData non può essere nullo");
            }
            String url = buildUri(endpoint, queryParams);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
            ResponseEntity<R> response = restTemplate.postForEntity(url, requestEntity, responseType);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RestClientException("[CallRestPost] Chiamata POST fallita con stato: " + response.getStatusCode());
            }
        } catch (RestClientException | IllegalArgumentException e) {
            throw new RestClientException("[CallRestPost] Chiamata POST fallita con stato: " + e);
        }
    }

    // Metodo per chiamate PUT
    protected <R> R callRestPut(String endpoint, MultiValueMap<String, String> formData, Map<String, String> queryParams, Class<R> responseType) {
        try {
            if (endpoint == null || endpoint.isEmpty()) {
                throw new IllegalArgumentException("L'endpoint non può essere nullo o vuoto");
            }
            if (formData == null) {
                throw new IllegalArgumentException("formData non può essere nullo");
            }
            String url = buildUri(endpoint, queryParams);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
            ResponseEntity<R> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RestClientException("[CallRestPut] Chiamata PUT fallita con stato: " + response.getStatusCode());
            }
        } catch (RestClientException | IllegalArgumentException e) {
            throw new RestClientException("[CallRestPut] Chiamata PUT fallita con stato: " + e);
        }
    }

    // Metodo per chiamate DELETE
    protected void callRestDelete(String endpoint, Map<String, String> queryParams) {
        try {
            if (endpoint == null || endpoint.isEmpty()) {
                throw new IllegalArgumentException("L'endpoint non può essere nullo o vuoto");
            }
            String url = buildUri(endpoint, queryParams);
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RestClientException("[CallRestDelete] Chiamata DELETE fallita con stato: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RestClientException("[CallRestDelete] Chiamata DELETE fallita con stato: " + e);
        } catch (RestClientException | IllegalArgumentException e) {
            throw new RestClientException("[CallRestDelete] Chiamata DELETE fallita con stato: " + e);
        }
    }

    //Metodi di utilità 
    // Metodo di supporto per convertire il contenuto in stringa
    protected String convertToString(byte[] content) {
        if (content == null) {
            return null;
        }
        return new String(content, StandardCharsets.UTF_8);
    }

    // Metodo per rimuovere il BOM (Byte Order Mark) da una stringa
    protected String removeBOM(String str) {
        if (str != null && str.startsWith("\uFEFF")) {
            return str.substring(1);
        }
        return str;
    }
}
