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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.g2.Interfaces.ServiceActionDefinition.InvalidParameterTypeException;
import com.g2.Interfaces.ServiceActionDefinition.MissingParametersException;
import com.g2.t5.T5Application;

@SpringBootTest(classes = T5Application.class)
public class BaseServiceTest {

    @Autowired
    private RestTemplate restTemplate;
    private BaseServiceImpl baseService;
    private MockRestServiceServer mockServer;
    private final String Base_URL = "http://mock_url:123";

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        baseService = new BaseServiceImpl(restTemplate, Base_URL);
    }


    /*
    *  T0_A  - Eseguo una chiamata che non è stata registrata 
    */
    @Test
    public void testNoRegistred(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            baseService.handleRequest("noExistMethod");
        });
        assertEquals("[HANDLEREQUEST] Azione non riconosciuta: noExistMethod", exception.getMessage());
    }

    /*
    *  T0_B - Eseguo una chiamata registrata ma sbaglio il numero di parametri 
    */
    @Test
    public void testGetWrongNumberParameters() {
        String expected_exception = "[ServiceActionDefinition] Numero di parametri errato: atteso 0, ricevuto 1";
        MissingParametersException exception = assertThrows(MissingParametersException.class, () -> {
            baseService.handleRequest("testGetNoParams", "ErrorParam");
        });     
        assertEquals(expected_exception, exception.getMessage());
    }

    /*
    * T0_C - Eseguo una chiamata registrata ma dò un tipo sbagliato di parametro  
    */
    @Test
    public void testGetWrongTypeParameters() {
        String expected_exception = "[ServiceActionDefinition] Parametro 1 non è del tipo corretto: atteso String, ricevuto Integer";
        InvalidParameterTypeException exception = assertThrows(InvalidParameterTypeException.class, () -> {
            baseService.handleRequest("TestGetParams", (Integer) 12);
        });     
        assertEquals(expected_exception, exception.getMessage());
    }
    /*
    *    T1 - Eseguo una GET senza parametri  
    */
    @Test
    public void testGetWithoutParameters() {
        mockServer.expect(requestTo(Base_URL + "/testGetNoParams" ))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"message\":\"Success\"}", MediaType.APPLICATION_JSON));

        String response = (String) baseService.handleRequest("testGetNoParams");
        assertEquals("{\"message\":\"Success\"}", response);
        mockServer.verify();
    }

    /*
    *  T2 - Eseguo una Get con parametri  
    */
    @Test
    public void testGetWithParameters() {
        // Definisci il parametro di test
        String testParam = "123";
        // Configura l'endpoint e la risposta attesa
        String expectedResponse = "Success";  // Risposta simulata
        String endpoint = Base_URL + "/TestGetParams?Test_Query_Params=123";  // Costruisci l'endpoint atteso

        // Aspettati una chiamata GET e restituisci una risposta simulata
        mockServer.expect(requestTo(endpoint))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(expectedResponse, MediaType.TEXT_PLAIN));

        // Chiama il metodo da testare
        String response = (String) baseService.handleRequest("TestGetParams", testParam);

        // Verifica la risposta
        assertEquals(expectedResponse, response);
        // Verifica che il server simulato abbia ricevuto la richiesta
        mockServer.verify();
    }
    
    /*
    *  T3 - Eseguo una Get omettendo l'endpoint  
    */
    @Test
    public void testGetWithoutEndpoint() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            baseService.handleRequest("testGetWithoutEndpoint");
        });
        assertEquals("L'endpoint non può essere nullo o vuoto", exception.getMessage());
    }


    /*
    *  T4 - Eseguo una Get che mi deve dare una lista 
    */
    @Test
    public void testGetList() {
        String endpoint = Base_URL + "/resources";
        // Configuriamo il server mock
        mockServer.expect(requestTo(endpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[\"resource1\", \"resource2\", \"resource3\"]", MediaType.APPLICATION_JSON));

        // Chiamiamo il metodo testGetList
        @SuppressWarnings("unchecked")
        List<String> resources = (List<String>) baseService.handleRequest("testGetList");

        // Verifichiamo che la lista restituita sia corretta
        assertNotNull(resources);
        assertEquals(3, resources.size());
        assertEquals("resource1", resources.get(0));
        assertEquals("resource2", resources.get(1));
        assertEquals("resource3", resources.get(2));

        // Verifica che il server mock abbia ricevuto la richiesta
        mockServer.verify();
    }


    /*
     * T5 - eseguo una get ma il server mi risponde 400 
     */
    @Test
    public void testGetWithBadRequest() {
        String Expected_exception = "Chiamata GET fallita con stato: org.springframework.web.client.HttpClientErrorException$BadRequest: 400 Bad Request: [no body]";
        mockServer.expect(requestTo(Base_URL + "/testGetNoParams"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST)); // Simula una risposta 400
    
        // Ci aspettiamo che venga sollevata un'eccezione
        RestClientException exception = assertThrows(RestClientException.class, () -> {
            baseService.handleRequest("testGetNoParams");
        });
    
        assertEquals(Expected_exception, exception.getMessage());
    }

    /*
     *  T4 - Eseguo una Post con un parametro in formData
     */
    @Test
    public void testPostWithFormData() {
        // Definisci il parametro di test
        String testParam = "Hello, World!";
        // Configura l'endpoint e la risposta attesa
        String expectedResponse = "Success";  // Risposta simulata
        String endpoint = Base_URL + "/example-post-endpoint";  // Endpoint atteso

        // Configura il MultiValueMap
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("param1", testParam);

        // Aspettati una chiamata POST e restituisci una risposta simulata
        mockServer.expect(requestTo(endpoint))
                    .andExpect(method(HttpMethod.POST))
                    .andExpect(content().formData(formData)) // Controlla il tipo di contenuto
                    .andRespond(withSuccess(expectedResponse, MediaType.TEXT_PLAIN));

        // Chiama il metodo da testare
        String response = (String) baseService.handleRequest("testPost", testParam);
        // Verifica la risposta
        assertEquals(expectedResponse, response);
        // Verifica che il server simulato abbia ricevuto la richiesta
        mockServer.verify();
    }

    /*
    *  T5 - Eseguo una put
    */
    @Test
    public void testPut(){
        String resourceId = "1";
        String endpoint = Base_URL + "/resources/" + resourceId;
        String textParam = "value1";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("param1", textParam);

        // Configura le aspettative per la chiamata PUT
        mockServer.expect(requestTo(endpoint))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().formData(formData))
                .andRespond(withSuccess("Success", MediaType.TEXT_PLAIN));

        // Chiama il metodo testPut e verifica la risposta
        String response = baseService.testPut(resourceId, textParam);
        assertEquals("Success", response);

        // Verifica che il server mock abbia ricevuto la richiesta
        mockServer.verify();
    }
}