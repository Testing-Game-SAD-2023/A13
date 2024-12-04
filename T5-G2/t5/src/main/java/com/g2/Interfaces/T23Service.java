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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hc.core5.http.HttpStatus;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

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

        registerAction("GetUserbyID", new ServiceActionDefinition(
            params -> GetUserbyID((Integer) params[0]),
            Integer.class
        ));

        registerAction("ModifyUser", new ServiceActionDefinition(
            params -> ModifyUser((User) params[0], (String) params[1]),
            User.class, String.class
        ));

        registerAction("SearchPlayer", new ServiceActionDefinition(
            params -> SearchPlayer((String) params[0]),
            String.class
        ));

        
        registerAction("AddFollow", new ServiceActionDefinition(
            params -> AddFollow((String) params[0], (String) params[1]),
            String.class, String.class
        ));

        
        registerAction("RmFollow", new ServiceActionDefinition(
            params -> RmFollow((String) params[0], (String) params[1]),
            String.class, String.class
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

    private User GetUserbyID(Integer user_id) {
        final String endpoint = "/students_list/" + user_id;
    
        return callRestGET(endpoint, null, User.class);
    }
    
    private String ModifyUser(User user_updated, String old_psw){
        final String endpoint = "/modifyUser";

        JSONObject requestBody = new JSONObject();
        requestBody.put("ID", user_updated.getId());          
        requestBody.put("name", user_updated.getName());
        requestBody.put("surname", user_updated.getSurname());
        requestBody.put("email", user_updated.getEmail());    
        requestBody.put("password", user_updated.getPassword());
        requestBody.put("biography", user_updated.getBiography());
        requestBody.put("following", user_updated.getFollowing()); 
        requestBody.put("followers", user_updated.getFollowers()); 
        requestBody.put("isRegisteredWithFacebook", user_updated.getisRegisteredWithFacebook());
        requestBody.put("isRegisteredWithGoogle", user_updated.getisRegisteredWithGoogle()); 
        requestBody.put("studies", user_updated.getStudies());
        requestBody.put("resetToken", user_updated.getResetToken());
        requestBody.put("missionToken", user_updated.getMissionToken());

        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("old_psw", old_psw);
     
        String response = callRestPut(endpoint, requestBody, queryParams, null, String.class);
        
        System.out.println(response);

        return response;

    }
    
    private User SearchPlayer(String key_search){
        final String endpoint = "/searchPlayer" ;

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("key_search", key_search);

        User player = callRestGET(endpoint, queryParams, User.class);  
        System.out.println(player);
        return player;
    }

    private String AddFollow(String userID_1, String userID_2){
        final String endpoint = "/addFollow";

        
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("userID_1", userID_1);
        formData.add("userID_2", userID_2);


        String msg = callRestPost(endpoint, formData , null, String.class);

        return msg;
    }

    private String RmFollow(String userID_1, String userID_2){
        final String endpoint = "/rmFollow";

        
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("userID_1", userID_1);
        formData.add("userID_2", userID_2);


        String msg = callRestPost(endpoint, formData , null, String.class);


        return msg;

    }


}