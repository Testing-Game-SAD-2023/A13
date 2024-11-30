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

 import java.util.List;
 import java.util.Map;
 
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
         registerAction("GetUsers", new ServiceActionDefinition(
                 params -> GetUsers()
         ));
         registerAction("UpdateBiography", new ServiceActionDefinition(
                 params -> updateBiography((String) params[0], (String) params[1])
         ));
         registerAction("GetFriends", new ServiceActionDefinition(
                 params -> getFriends((String) params[0])
         ));
         registerAction("AddFriend", new ServiceActionDefinition(
                 params -> addFriend((String) params[0], (String) params[1])
         ));
         registerAction("RemoveFriend", new ServiceActionDefinition(
                 params -> removeFriend((String) params[0], (String) params[1])
         ));
     }
 
     // Metodo per l'autenticazione
     private Boolean GetAuthenticated(String jwt) {
         final String endpoint = "/validateToken";
         if (jwt.isEmpty()) {
             throw new IllegalArgumentException("[GETAUTHENTICATED] Errore, token nullo o vuoto");
         }
         MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
         formData.add("jwt", jwt);
         return callRestPost(endpoint, formData, null, Boolean.class);
     }
 
     // Metodo per ottenere la lista degli utenti
     private List<User> GetUsers() {
         final String endpoint = "/students_list";
         return callRestGET(endpoint, null, new ParameterizedTypeReference<List<User>>() {});
     }
 
     // Metodo per aggiornare la biografia
     public Boolean updateBiography(String userId, String biography) {
         final String endpoint = "/updateBiography";
         MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
         formData.add("userId", userId);
         formData.add("biography", biography);
         return callRestPost(endpoint, formData, null, Boolean.class);
     }
 
     // Metodo per ottenere la lista degli amici
     public List<Map<String, String>> getFriends(String userId) {
         final String endpoint = "/getFriends";
         MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
         formData.add("userId", userId);
         return callRestPost(endpoint, formData, null, new ParameterizedTypeReference<List<Map<String, String>>>() {});
     }
 
     // Metodo per aggiungere un amico
     public Boolean addFriend(String userId, String friendId) {
         final String endpoint = "/addFriend";
         MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
         formData.add("userId", userId);
         formData.add("friendId", friendId);
         return callRestPost(endpoint, formData, null, Boolean.class);
     }
 
     // Metodo per rimuovere un amico
     public Boolean removeFriend(String userId, String friendId) {
         final String endpoint = "/removeFriend";
         MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
         formData.add("userId", userId);
         formData.add("friendId", friendId);
         return callRestPost(endpoint, formData, null, Boolean.class);
     }
 }
 