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

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
}