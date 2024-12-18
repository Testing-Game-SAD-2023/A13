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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.g2.Model.User;
import java.util.HashMap;

//noi

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.HttpClientErrorException;

//fine noi

 import org.springframework.core.ParameterizedTypeReference;
 import org.springframework.stereotype.Service;
 import org.springframework.util.LinkedMultiValueMap;
 import org.springframework.util.MultiValueMap;
 import org.springframework.web.client.RestTemplate;
 
 @Service
 public class T23Service extends BaseService {
 
     private static final String BASE_URL = "http://t23-g1-app-1:8080";
     
    public T23Service(RestTemplate restTemplate) {
         super(restTemplate, BASE_URL);
 
         // Registrazione delle azioni
         registerAction("GetAuthenticated", new ServiceActionDefinition(
                 params -> GetAuthenticated((String) params[0]),
                 String.class
         ));
         //cami (02/12)
        registerAction("GetUserInfo", new ServiceActionDefinition(
        params -> getUserInfo((String) params[0])
        ));

         registerAction("GetUsers", new ServiceActionDefinition(
                 params -> GetUsers()
         ));
         registerAction("UpdateBiography", new ServiceActionDefinition(
                 params -> updateBiography((String) params[0], (String) params[1])
         ));
        
         registerAction("GetBiography", new ServiceActionDefinition(
         params -> getBiography((String) params[0]),
         String.class
         ));

         registerAction("getFriendlist", new ServiceActionDefinition(
                 params -> getFriendlist((String) params[0])
         ));

  
         registerAction("AddFriend", new ServiceActionDefinition(
                 params -> addFriend((String) params[0], (String) params[1])
         ));
        registerAction("removeFriend", new ServiceActionDefinition(
        params -> removeFriend((String) params[0], (Integer) params[1])
        ));


        
     }
    
    //cami
    // Metodo per aggiornare l'avatar
    public Boolean updateAvatar(Integer userId, String avatar) {
    final String endpoint = "/updateAvatar";
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("avatar", avatar); 

    try {
        // Chiamata al servizio T23
        return callRestPost(endpoint, formData, null, Boolean.class);
    } catch (Exception e) {
        System.err.println("Errore durante l'aggiornamento dell'avatar: " + e.getMessage());
        return false;
    }
    }

    // Metodo per recuperare l'avatar da T23
    public String getAvatar(String userId) {
    final String endpoint = "/getAvatar?userId=" + userId;
    try {
        return callRestGet(endpoint, null, String.class); // Ritorna direttamente il corpo
    } catch (Exception e) {
        System.err.println("Errore durante il recupero dell'avatar: " + e.getMessage());
        return null;
    }
    }



    // Metodo per recuperare la biografia
    public String getBiography(String userId) {
    final String endpoint = "/getBiography";

    // Creazione dei parametri della richiesta
    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    queryParams.add("userId", userId);

    // Chiamata all'endpoint usando callRestGET
    return callRestGet(endpoint, queryParams, String.class);
    }

    public <R> R callRestGet(String endpoint, MultiValueMap<String, String> queryParams, Class<R> responseType) {
    HttpHeaders httpHeaders = new HttpHeaders();

    // Creazione della richiesta senza corpo
    HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);

    // Costruzione dell'URL con i parametri di query
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(BASE_URL + endpoint);
    if (queryParams != null) {
        queryParams.forEach(uriBuilder::queryParam);
    }

    // Esecuzione della richiesta GET
    ResponseEntity<R> response = restTemplate.exchange(
        uriBuilder.toUriString(),
        HttpMethod.GET,
        requestEntity,
        responseType
    );

    // Restituzione del risultato
    return response.getBody();
    }

    // Nuovo overload (02/12)-cami
    public <R> R callRestGet(String endpoint, MultiValueMap<String, String> queryParams, ParameterizedTypeReference<R> responseType) {
    HttpHeaders httpHeaders = new HttpHeaders();

    // Creazione della richiesta senza corpo
    HttpEntity<Void> requestEntity = new HttpEntity<>(httpHeaders);

    // Costruzione dell'URL con i parametri di query
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(BASE_URL + endpoint);
    if (queryParams != null) {
        queryParams.forEach(uriBuilder::queryParam);
    }

    // Esecuzione della richiesta GET
    ResponseEntity<R> response = restTemplate.exchange(
        uriBuilder.toUriString(),
        HttpMethod.GET,
        requestEntity,
        responseType
    );

    // Restituzione del risultato
    return response.getBody();
    }

  
     // Metodo per l'autenticazione
     private Boolean GetAuthenticated(String jwt) {
         final String endpoint = "/validateToken";
         if (jwt.isEmpty()) {
             throw new IllegalArgumentException("[GETAUTHENTICATED] Errore, token nullo o vuoto");
         }
         MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
         formData.add("jwt", jwt);
         Boolean isAuthenticated = callRestPost(endpoint, formData, null, Boolean.class);
         return isAuthenticated != null && isAuthenticated;
     }
 
     // Metodo per ottenere la lista degli utenti
     private List<User> GetUsers() {
         final String endpoint = "/students_list";
         return callRestGET(endpoint, null, new ParameterizedTypeReference<List<User>>() {});
     }
    //cami
     // Metodo per aggiornare la biografia
     public Boolean updateBiography(String userId, String biography) {
         final String endpoint = "/updateBiography";
         MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
         formData.add("userId", userId);
         formData.add("biography", biography);
         return callRestPost(endpoint, formData, null, Boolean.class);
     }
    public <R> R callRestPost(String endpoint, MultiValueMap<String, String> payload, Map<String, String> headers, ParameterizedTypeReference<R> responseType) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    if (headers != null) {
        headers.forEach(httpHeaders::add);
    }

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(payload, httpHeaders);

    // Esegui la richiesta
    ResponseEntity<R> response = restTemplate.exchange(
        BASE_URL + endpoint,
        HttpMethod.POST,
        requestEntity,
        responseType
    );
    return response.getBody();
    }
    //GabMan 03/12
     // Metodo per ottenere la lista degli amici
    public List<Map<String, String>> getFriendlist(String userId) {
    final String endpoint = "/getFriendlist"; 

    // Parametri per la richiesta
    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    queryParams.add("userId", userId);

    try {
        // Effettua una richiesta GET per ottenere la lista amici
        System.out.println("Calling endpoint: " + BASE_URL + endpoint);
        return callRestGet(endpoint, queryParams, new ParameterizedTypeReference<List<Map<String, String>>>() {});
    } catch (Exception e) {
        System.err.println("Errore durante la chiamata a " + endpoint + ": " + e.getMessage());
        return new ArrayList<>(); // Ritorna una lista vuota come fallback
    }
    }


      
     // Metodo per aggiungere un amico

    public Map<String, String> searchFriend(String identifier) {
    final String endpoint = "/searchFriend";

    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    queryParams.add("identifier", identifier);

    try {
        return callRestGet(endpoint, queryParams, new ParameterizedTypeReference<Map<String, String>>() {});
    } catch (Exception e) {
        System.err.println("Errore durante la ricerca dell'amico: " + e.getMessage());
        return null;
    }
    }
    
    public String addFriend(String userId, String friendId) {
        final String endpoint = "/addFriend";
        MultiValueMap<String, String> payload = new LinkedMultiValueMap<>();
        payload.add("userId", userId);
        payload.add("friendId", friendId);

        try {
            return callRestPost(endpoint, payload, null, String.class);
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiunta dell'amico: " + e.getMessage());
            return null;
        }
    }

     public Boolean removeFriend(String jwt, Integer friendId) {
    final String endpoint = "/removeFriend";

    // Prepara i parametri per la richiesta
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("friendId", friendId.toString());

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + jwt);


    try {
        // Effettua la chiamata POST 
        return callRestPost(endpoint, formData, headers, Boolean.class);
    } catch (Exception e) {
        System.err.println("Errore durante la rimozione dell'amico: " + e.getMessage());
        return false;
    }
    }



    
    private String buildQueryString(MultiValueMap<String, String> queryParams) {
        return queryParams.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
            .reduce((param1, param2) -> param1 + "&" + param2)
            .orElse("");
    }
    


     //cami (02/12)
     // Metodo per ottenere informazioni dell'utente (name, surname, nickname)
    public Map<String, String> getUserInfo(String userId) {
    final String endpoint = "/getUserInfo";

    // Creazione dei parametri della richiesta
    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    queryParams.add("userId", userId);

    // Chiamata all'endpoint usando callRestGet
    return callRestGet(endpoint, queryParams, new ParameterizedTypeReference<Map<String, String>>() {});
    }

    //Gabman 09/12 : Metodo per aggiornare le informazioni dell'utente (name, surname, nickname)
    public Boolean updateUserInfo(String userId, String name, String surname, String nickname) {
        final String endpoint = "/updateUserInfo";

        // Creazione del payload per la richiesta POST
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("userId", userId); 
        formData.add("name", name);
        formData.add("surname", surname);
        formData.add("nickname", nickname);

        try {
            // Effettua la richiesta POST 
            return callRestPost(endpoint, formData, null, Boolean.class);
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento delle informazioni dell'utente: " + e.getMessage());
            return false;
        }
    }

    // GabMan 12/12 Caricamento Immagine in Base64
    public Boolean updateProfilePicture(Integer userId, String base64Image) {
        final String endpoint = "/updateProfilePicture";
        Map<String, String> payload = new HashMap<>();
        payload.put("profilePicture", base64Image);

        try {
            
            ResponseEntity<Boolean> response = restTemplate.postForEntity(
                endpoint,
                payload,
                Boolean.class
            );
            return response.getStatusCode() == HttpStatus.OK;
        } catch (RestClientException e) {
            System.err.println("Errore durante l'aggiornamento dell'immagine personalizzata: " + e.getMessage());
            return false;
        }
    }

        public String getImage(Integer userId) {
        final String endpoint = "/getImage";
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(endpoint, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return (String) response.getBody().get("imageUrl");
            }
            return null;
        } catch (RestClientException e) {
            System.err.println("Errore durante il recupero dell'immagine: " + e.getMessage());
            return null;
        }
    }













}

    



 