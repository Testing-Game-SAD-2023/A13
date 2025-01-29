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
import com.g2.Model.Notification;
import com.g2.Model.User;

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
                params -> GetUsers() //metodo senza parametri
        ));

        registerAction("GetUser", new ServiceActionDefinition(
            params -> GetUser((String) params[0]), 
            String.class
        ));

        registerAction("UpdateProfile", new ServiceActionDefinition(
                params -> UpdateProfile((String) params[0], (String) params[1], (String) params[2]),
                String.class, String.class, String.class
        ));

        registerAction("GetUserByEmail", new ServiceActionDefinition(
                params -> GetUserByEmail((String) params[0]),
                String.class
        ));

        registerAction("NewNotification", new ServiceActionDefinition(
                params -> NewNotification((String) params[0], (String) params[1], (String) params[2]),
                String.class, String.class, String.class
        ));

        registerAction("getNotifications", new ServiceActionDefinition(
                params -> getNotifications((String) params[0]),
                String.class
        ));

        registerAction("updateNotification", new ServiceActionDefinition(
                params -> updateNotification((String) params[0], (String) params[1]),
                String.class, String.class
        ));

        registerAction("deleteNotification", new ServiceActionDefinition(
                params -> deleteNotification((String) params[0], (String) params[1]),
                String.class, String.class
        ));

        registerAction("clearNotifications", new ServiceActionDefinition(
                params -> clearNotifications((String) params[0]),
                String.class
        ));

        registerAction("followUser", new ServiceActionDefinition(
                params -> followUser((Integer) params[0], (Integer) params[1]),
                Integer.class, Integer.class
        ));

        registerAction("getFollowers", new ServiceActionDefinition(
                params -> getFollowers((String) params[0]),
                String.class
        ));

        registerAction("getFollowing", new ServiceActionDefinition(
                params -> getFollowing((String) params[0]),
                String.class
        ));
    }

    // Metodo per l'autenticazione
    private Boolean GetAuthenticated(String jwt) {
        final String endpoint = "/validateToken";
        // Verifica se il JWT è valido prima di fare la richiesta
        if (jwt.isEmpty()) {
            throw new IllegalArgumentException("[GETAUTHENTICATED] Errore, token nullo o vuoto");
        }
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("jwt", jwt);
        // Chiamata POST utilizzando il metodo della classe base
        Boolean isAuthenticated = callRestPost(endpoint, formData, null, Boolean.class);
        return isAuthenticated != null && isAuthenticated;
    }

    // Metodo per ottenere la lista degli utenti
    private List<User> GetUsers() {
        final String endpoint = "/students_list";
        return callRestGET(endpoint, null, new ParameterizedTypeReference<List<User>>() {});
    }

    private User GetUser(String user_id){
        final String endpoint = "/students_list/" + user_id;
        return callRestGET(endpoint, null, User.class);
    }

    // Metodo per modificare il profilo di un utente
    private Boolean UpdateProfile(String userEmail, String bio, String imagePath) {
        final String endpoint = "/update_profile";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", userEmail);
        map.add("bio", bio);
        map.add("profilePicturePath", imagePath);
        return callRestPost(endpoint, map, null, Boolean.class);
    }

    private User GetUserByEmail(String userEmail) {
        final String endpoint = "/user_by_email";
        Map<String, String> queryParams = Map.of("email", userEmail);
        return callRestGET(endpoint, queryParams, User.class);
    }

    // Metodo per la creazione di una notifica
    private String NewNotification(String userEmail, String title, String message) {
        final String endpoint = "/new_notification";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", userEmail);
        map.add("title", title);
        map.add("message", message);
        return callRestPost(endpoint, map, null, String.class);
    }

    public List<Notification> getNotifications(String userEmail) {
        final String endpoint = "/notifications";
        Map<String, String> queryParams = Map.of("email", userEmail);
        // Effettua la chiamata GET e restituisce una lista di notifiche
        return callRestGET(endpoint, queryParams, new ParameterizedTypeReference<List<Notification>>() {});
    }

    public String updateNotification(String userEmail, String notificationID) {
        final String endpoint = "/update_notification";
        // Imposta i dati del form
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("email", userEmail);
        formData.add("id notifica", notificationID);
        // Effettua una chiamata POST per aggiornare lo stato della notifica
        return callRestPost(endpoint, formData, null, String.class);
    }

    // Metodo per eliminare una singola notifica
    public String deleteNotification(String userEmail, String notificationID) {
        final String endpoint = "/delete_notification";
        Map<String, String> queryParams = Map.of(
            "email", userEmail,
            "idnotifica", notificationID
        );
        return callRestDelete(endpoint, queryParams);
    }

    // Metodo per eliminare tutte le notifiche
    public String clearNotifications(String userEmail) {
        final String endpoint = "/clear_notifications";
        Map<String, String> queryParams = Map.of("email", userEmail);
        return callRestDelete(endpoint, queryParams);
    }

    /*
    *   Metodo per follow/unfollow di un utente
    *   il targetUserId + chi viene seguito 
    *   il authUserId è chi segue 
    */ 
    public String followUser(Integer targetUserId, Integer authUserId) {
        final String endpoint = "/add-follow";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("targetUserId", String.valueOf(targetUserId));
        map.add("authUserId", String.valueOf(authUserId));
        return callRestPost(endpoint, map, null, String.class);
    }

    public List<User> getFollowers(String userId) {
        final String endpoint = "/get-followers";
        Map<String, String> queryParams = Map.of("userId", userId);
        return callRestGET(endpoint, queryParams, new ParameterizedTypeReference<List<User>>() {});
    }

    public List<User> getFollowing(String userId) {
        final String endpoint = "/get-following";
        Map<String, String> queryParams = Map.of("userId", userId);
        return callRestGET(endpoint, queryParams, new ParameterizedTypeReference<List<User>>() {});
    }
}