/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.g2.Interfaces;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.g2.Interfaces.BaseService_utils.HttpHeadersFactory;
import com.g2.Interfaces.BaseService_utils.UriBuilderHelper;

/*
 *  Questa è una classe base che implementa l'interfaccia ServiceInterface per il dispatcher ServiceManager
 *   e ti fornisce svariati metodi che mappano POST, GET, PUT E DELETE, in vari formati e header.
 *   Attenzione alcune chiamate sono state definite in modo molto rigido, potrebbero quindi non andar bene. 
 */
public abstract class BaseService implements ServiceInterface {

    protected RestTemplate restTemplate;
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
        ServiceActionDefinition actionDefinition = actions.get(action);
        if (actionDefinition == null) {
            throw new IllegalArgumentException("[HANDLEREQUEST] Azione non riconosciuta: " + action);
        }
        // Esegui la funzione associata all'azione con i parametri validati
        return actionDefinition.execute(params);
    }

    // Metodi per le chiamate REST
    // Costruisce un URI partendo dal baseUrl e dall'endpoint, aggiungendo eventuali
    // parametri extra
    protected String buildUri(String endpoint, Map<String, String> queryParams) {
        return UriBuilderHelper.buildUri(baseUrl, endpoint, queryParams);
    }

    protected HttpHeaders buildHeaders(Map<String, String> customHeaders, MediaType defaultContentType) {
        return HttpHeadersFactory.createHeaders(customHeaders, defaultContentType);
    }

    // Interfaccia funzionale per le chiamate REST serve per executeRestCall(String caller, RestCall<R> call)
    @FunctionalInterface
    protected interface RestCall<R> {

        R execute() throws RestClientException, RuntimeException;
    }

    /*
     * Mi serve per non ripetere i controlli sul try catch
     */
    private <R> R executeRestCall(String caller, RestCall<R> call) {
        try {
            return call.execute();
        } catch (HttpClientErrorException e) { // Gestisce gli errori 4xx
            throw new RestClientException("Chiamata REST fallita con stato 4xx: " + e.getStatusCode() + 
                                          " (eseguita da: " + caller + ")", e);
        } catch (HttpServerErrorException e) { // Gestisce gli errori 5xx
            throw new RestClientException("Chiamata REST fallita con stato 5xx: " + e.getStatusCode()
                    + " (eseguita da: " + caller + ")", e);
        } catch (RestClientException e) { // Altri tipi di errori di RestClient
            throw new RestClientException("Chiamata REST fallita: " + e.getMessage()
                    + " (eseguita da: " + caller + ")", e);
        } catch (IllegalArgumentException e) { // Errori dovuti a parametri non validi
            throw new RuntimeException("Chiamata REST fallita: " + e.getMessage()
                    + " (eseguita da: " + caller + ")", e);
        }
    }

    // Metodo per chiamate GET che restituiscono un singolo oggetto
    //FLAVIO 25GEN: AGGIUNTI FILE DI DEBUGGING
    protected <R> R callRestGET(String endpoint, Map<String, String> queryParams, Class<R> responseType) {
        return executeRestCall("callRestGET", () -> {
            String url = buildUri(endpoint, queryParams);
            ResponseEntity<R> response = restTemplate.getForEntity(url, responseType);
            System.out.println("CALLRESTGET: " + response.getBody()); 
            return response.getBody();
        });
    }

    // Metodo per chiamate GET che restituiscono una lista di oggetti
    protected <R> List<R> callRestGET(String endpoint, Map<String, String> queryParams, ParameterizedTypeReference<List<R>> responseType) {
        return executeRestCall("callRestGET", () -> {
            String url = buildUri(endpoint, queryParams);
            ResponseEntity<List<R>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            return response.getBody();
        });
    }

    // Metodo per chiamate POST senza specificare content type -> default
    // application/x-www-form-urlencoded
    protected <R> R callRestPost(String endpoint, MultiValueMap<String, String> formData, Map<String, String> queryParams, Class<R> responseType) {

        return callRestPost(endpoint, formData, queryParams, null, responseType);
    }

    // Metodo per chiamate POST con content type a application/x-www-form-urlencoded
    protected <R> R callRestPost(String endpoint, MultiValueMap<String, String> formData,
            Map<String, String> queryParams, Map<String, String> customHeaders,
            Class<R> responseType) {
        if (formData == null) {
            throw new IllegalArgumentException("formData non può essere nullo");
        }
        return executeRestCall("callRestPost", () -> {
            String url = UriBuilderHelper.buildUri(baseUrl, endpoint, queryParams);
            HttpHeaders headers = HttpHeadersFactory.createHeaders(customHeaders, MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
            ResponseEntity<R> response = restTemplate.postForEntity(url, requestEntity, responseType);
            return response.getBody();
        });
    }

    // metodo per chiamare POST con content type a application/json
    //FLAVIO 25GEN: AGGIUNTI FILE DI DEBUGGING
    protected <R> R callRestPost(String endpoint, JSONObject jsonObject,
            Map<String, String> queryParams, Map<String, String> customHeaders,
            Class<R> responseType) {
        if (jsonObject == null) {
            throw new IllegalArgumentException("Il body JSON non può essere nullo");
        }
        return executeRestCall("callRestPost", () -> {
            System.out.println("CALLRESTPOST - OBJECT: " + jsonObject); 
            System.out.println("CALLRESTPOST - ENDPOINT: " + endpoint); 
            String jsonBody = jsonObject.toString();
            System.out.println("CALLRESTPOST - jsonbody: " + jsonBody); 
            String url = buildUri(endpoint, queryParams);
            HttpHeaders headers = buildHeaders(customHeaders, MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            System.out.println("CALLRESTPOST - requestEntity: " + requestEntity); 
            ResponseEntity<R> response = restTemplate.postForEntity(url, requestEntity, responseType);
            System.out.println("CALLRESTPOST - response: " + response); 
            System.out.println("CALLRESTPOST response body: " + response.getBody()); 
            return response.getBody();
        });
    }

    // Metodo per chiamate PUT senza specificare content type -> default application/x-www-form-urlencoded
    protected <R> R callRestPut(String endpoint, MultiValueMap<String, String> formData,
            Map<String, String> queryParams, Class<R> responseType) {
        return callRestPut(endpoint, formData, queryParams, null, responseType);
    }

    //FLAVIO 26GEN: aggiunta per un put di un jsonObject
    protected <R> R callRestPut(String endpoint, JSONObject jsonObject, 
                                Map<String, String> queryParams, Class<R> responseType) {
        return callRestPut(endpoint, jsonObject, queryParams, null, responseType);
    }

    // Metodo per chiamate PUT con content type a application/x-www-form-urlencoded
    protected <R> R callRestPut(String endpoint, MultiValueMap<String, String> formData,
            Map<String, String> queryParams, Map<String, String> customHeaders,
            Class<R> responseType) {
        if (endpoint == null || endpoint.isEmpty()) {
            throw new IllegalArgumentException("L'endpoint non può essere nullo o vuoto");
        }
        if (formData == null) {
            throw new IllegalArgumentException("formData non può essere nullo");
        }

        return executeRestCall("callRestPut", () -> {
            String url = buildUri(endpoint, queryParams);
            HttpHeaders headers = buildHeaders(customHeaders, MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
            ResponseEntity<R> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType);
            return response.getBody();
        });
    }

    // metodo per chiamare PUT con content type a application/json
    protected <R> R callRestPut(String endpoint, JSONObject jsonObject,
            Map<String, String> queryParams, Map<String, String> customHeaders,
            Class<R> responseType) {
        if (endpoint == null || endpoint.isEmpty()) {
            throw new IllegalArgumentException("L'endpoint non può essere nullo o vuoto");
        }
        if (jsonObject == null) {
            throw new IllegalArgumentException("Il body JSON non può essere nullo");
        }

        return executeRestCall("callRestPut", () -> {
            String url = buildUri(endpoint, queryParams);
            String jsonBody = jsonObject.toString();

            HttpHeaders headers = buildHeaders(customHeaders, MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<R> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType);

            System.out.println("Request URL: " + url);
            System.out.println("Request Headers: " + headers);
            System.out.println("Request Body: " + jsonBody);

            return response.getBody();
        });
    }

    // Metodo per chiamate DELETE
    protected String callRestDelete(String endpoint, Map<String, String> queryParams) {
        return executeRestCall("callRestDelete", () -> {
            String url = buildUri(endpoint, queryParams);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
            return response.getBody();
        });
    }

    protected String convertToString(byte[] content) {
        if (content == null || content.length == 0) {
            throw new IllegalArgumentException("L'array di byte non può essere nullo.");
        }
        try {
            CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
            ByteBuffer byteBuffer = ByteBuffer.wrap(content);
            StringBuilder result = new StringBuilder();
            // Decodifica il buffer di byte
            while (byteBuffer.hasRemaining()) {
                // Aggiungi il carattere decodificato al risultato
                result.append(decoder.decode(byteBuffer));
            }
            // Completa la decodifica
            decoder.flush(CharBuffer.allocate(1));
            return result.toString();
        } catch (CharacterCodingException e) {
            throw new RuntimeException("Erorr conversione, Il byte array contiene byte non validi per UTF-8.");
        } catch (Exception e) {
            throw new RuntimeException("Errore imprevisto durante la conversione");
        }
    }

    // Metodo per rimuovere il BOM (Byte Order Mark) da una stringa
    protected String removeBOM(String str) {
        if (str != null && str.startsWith("\uFEFF")) {
            return str.substring(1);
        }
        return str;
    }
}
