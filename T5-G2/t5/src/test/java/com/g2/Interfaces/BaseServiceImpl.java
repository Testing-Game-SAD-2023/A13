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

import org.json.JSONObject;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

// Annotazione @Service per Spring
@Profile("test")
@Service
public class BaseServiceImpl extends BaseService {

    private static String baseUrl = "http://mock_url:123";

    // Costruttore che richiama il costruttore della classe base
    public BaseServiceImpl(RestTemplate restTemplate) {
        super(restTemplate, baseUrl);
        // Registrazione delle azioni specifiche del servizio
        registerAction("testGetNoParams", new ServiceActionDefinition(
            params -> testGetNoParams()
        ));
        registerAction("testGetWithoutEndpoint", new ServiceActionDefinition(
            params -> testGetWithoutEndpoint()
        )); 
        registerAction("testGetSetEndpoint", new ServiceActionDefinition(
            params -> testGetSetEndpoint((String) params[0]), 
            String.class
        ));
        registerAction("TestGetParams", new ServiceActionDefinition(
            params -> TestGetParams((String) params[0]),
            String.class 
        ));
        registerAction("testGetList", new ServiceActionDefinition(
            params -> testGetList()
        ));
        registerAction("testPost", new ServiceActionDefinition(
            params -> testPost((String) params[0]), 
            String.class
        )); 
        registerAction("testPostNullForm", new ServiceActionDefinition(
            params -> testPostNullForm()
        ));
        registerAction("testPut", new ServiceActionDefinition(
            params -> testPut((String) params[0], (String) params[0]), 
            String.class, String.class
        ));
        registerAction("testDelete", new ServiceActionDefinition(
            params -> testDelete((String) params[0]), 
            String.class
        ));
        registerAction("createResource", new ServiceActionDefinition(
            params -> createResource((String) params[0], (String) params[1]), 
            String.class, String.class
        )); 
        registerAction("testGetListWithoutEndpoint", new ServiceActionDefinition(
            params -> testGetListWithoutEndpoint()
        )); 
    }


    /*
    * Rendo publico buildURI così è più semplice testarlo 
    */
    public String buildUri_test(String endpoint, Map<String, String> queryParams){
        return buildUri(endpoint, queryParams);
    }

    /**
     * Metodo per eseguire una chiamata GET senza parametri.
     * @return La risposta della chiamata GET come stringa
     */
    private Object testGetNoParams() {
        // Endpoint per la chiamata GET
        String endpoint = "/testGetNoParams";
        // Chiamata GET senza parametri
        String response = callRestGET(endpoint, null, String.class);
        return response;
    }

    /**
     * Metodo per eseguire una chiamata GET con parametri di query.
     * @param params Parametri richiesti dalla firma del metodo
     * @return La risposta della chiamata GET come stringa
     */
    private String TestGetParams(String Test_param) {
            String endpoint = "/TestGetParams";
            Map<String, String> formData = new HashMap<>();
            formData.put("Test_Query_Params", Test_param);  
            String response = callRestGET(endpoint, formData, String.class);
            return response;
    }

    private String testGetWithoutEndpoint(){
        String response = callRestGET(null, null, String.class);
        return response;
    }

    private String testGetSetEndpoint(String endpoint){
        String response = callRestGET(endpoint, null, String.class);
        return response;
    }

    public List<String> testGetList() {
            String endpoint = "/resources";
            // Chiamata GET per ottenere la lista di risorse
            return callRestGET(endpoint, null, new ParameterizedTypeReference<List<String>>() {});
    }

    public List<String> testGetListWithoutEndpoint() {
        return callRestGET(null, null, new ParameterizedTypeReference<List<String>>() {});
    }

    /**
     * Metodo per eseguire una chiamata POST con parametri.
     * @param params Parametri richiesti dalla firma del metodo (attesi come JSONObject)
     * @return La risposta della chiamata POST come stringa
     */
    private Object testPost(String text_params) {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("param1", text_params);
            // Endpoint per la chiamata POST
            String endpoint = "/example-post-endpoint";
            // Chiamata POST con il corpo JSON
            String response = callRestPost(endpoint, formData, null, String.class);
            return response;
    }

    private Object testPostNullForm() {
        MultiValueMap<String, String> formData = null;
        // Endpoint per la chiamata POST
        String endpoint = "/example-post-endpoint";
        // Chiamata POST con il corpo JSON
        String response = callRestPost(endpoint, formData, null, null, String.class);
        return response;
    }

    public String createResource(String resourceName, String resourceData) {
        // Creazione del JSONObject con i dati della risorsa
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", resourceName);
        jsonObject.put("data", resourceData);

        // Endpoint per la creazione della risorsa
        String endpoint = "/resources/create";

        // Header personalizzati (opzionale)
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("Authorization", "Bearer token_esempio");

        // Chiamata al metodo callRestPost
        return callRestPost(endpoint, jsonObject, null, customHeaders, String.class);
    }


    // Metodo per aggiornare una risorsa tramite una chiamata PUT
    public String testPut(String resourceId, String Text_param) {
            // Endpoint per la chiamata PUT
            String endpoint = "/resources/" + resourceId;
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("param1", Text_param);
            // Chiamata PUT per aggiornare la risorsa
            return callRestPut(endpoint, formData, null, String.class);
    }

    public Object testDelete(String resourceId) {
            // Endpoint per la chiamata DELETE
            String endpoint = "/resources/" + resourceId;
            // Chiamata DELETE per eliminare la risorsa
            callRestDelete(endpoint, null);
            return null;
    }
}
