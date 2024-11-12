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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.Model.User;
import com.g2.t5.T5Application;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;

@SpringBootTest(classes = T5Application.class)
class T23ServiceTest {

    @Autowired
    private RestTemplate restTemplate;
    private T23Service T23Service;
    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper;
    private final String Base_URL = "http://t23-g1-app-1:8080";

    @BeforeEach
    public void setUp() {
        // Precondizione: Creare un server mock e inizializzare il T1Service
        mockServer = MockRestServiceServer.createServer(restTemplate);
        T23Service = new T23Service(restTemplate);
        objectMapper = new ObjectMapper();
    }

    /*
     * Test1: testMissingToken
     * Precondizioni: Una chiamata a handleRequest con token mancante
     * Azioni: Invocare handleRequest su T23Service con "getAuthenticated" e null.
     * Post-condizioni: Ci si aspetta una MissingParametersException con il
     * messaggio appropriato.
     */
    @Test
    public void testMissingToken() {
        String expected_exception = "[GETAUTHENTICATED] Errore, token nullo o vuoto";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            T23Service.handleRequest("GetAuthenticated", "");
        });
        assertEquals(expected_exception, exception.getMessage());
    }

    /*
     * Test2: testGetAuthenticatedWithValidToken
     * Precondizioni: Simulazione di un token JWT valido.
     * Azioni: Configurare il mock server per restituire true per un token valido.
     * Post-condizioni: Ci si aspetta che il risultato sia true.
     */
    @Test
    public void testGetAuthenticatedWithValidToken() {
        String validToken = "valid.jwt.token";
        mockServer.expect(requestTo(Base_URL + "/validateToken"))
                .andRespond(withSuccess("true", MediaType.APPLICATION_JSON));

        // Chiamata al metodo handleRequest con un token valido
        Boolean result = (Boolean) T23Service.handleRequest("GetAuthenticated", validToken);

        // Verifica che il risultato sia true
        assertEquals(true, result);
    }

    /*
     * Test3: testGetAuthenticatedWithInvalidToken
     * Precondizioni: Simulare una chiamata a validateToken con un token non valido.
     * Azioni: Chiamare handleRequest su T23Service con "GetAuthenticated" e un
     * token invalido.
     * Post-condizioni: Ci si aspetta che il risultato sia false, poiché il token
     * fornito
     * non è valido e il server risponde con un valore JSON "false".
     */
    @Test
    public void testGetAuthenticatedWithInvalidToken() {
        // Chiamata al metodo handleRequest con un token null
        String invalidToken = "invalid.jwt.token";
        mockServer.expect(requestTo(Base_URL + "/validateToken"))
                .andRespond(withSuccess("false", MediaType.APPLICATION_JSON));
        Boolean result = (Boolean) T23Service.handleRequest("GetAuthenticated", invalidToken);

        // Verifica che il risultato sia false
        assertEquals(false, result);
    }

    /*
     * Test4: testGetUsersSuccess
     * Precondizioni: Il server mock deve restituire una lista di utenti.
     * Azioni: Invocare handleRequest su T23Service con "GetUsers".
     * Post-condizioni: Ci si aspetta di ricevere una lista di utenti correttamente
     * popolata.
     */
    @Test
    public void testGetUsersSuccess() {
        // Dati di test: Lista di utenti fittizi
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(
                new User(1L, "John", "Doe", "john.doe@example.com", "password123", false, "Computer Science", null));
        mockUsers
                .add(new User(2L, "Jane", "Smith", "jane.smith@example.com", "password456", true, "Mathematics", null));

        // Configurazione del server mock per rispondere con i dati fittizi
        try {
            mockServer.expect(requestTo(Base_URL + "/students_list"))
                    .andRespond(withSuccess(objectMapper.writeValueAsString(mockUsers), MediaType.APPLICATION_JSON));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Chiamata al metodo handleRequest
        @SuppressWarnings("unchecked")
        List<User> result = (List<User>) T23Service.handleRequest("GetUsers");

        // Verifica dei campi del primo utente
        User user1 = result.get(0);
        assertEquals(1L, user1.getId());
        assertEquals("John", user1.getName());
        assertEquals("Doe", user1.getSurname());
        assertEquals("john.doe@example.com", user1.getEmail());
        assertEquals("password123", user1.getPassword());
        assertEquals(false, user1.isRegisteredWithFacebook());
        assertEquals("Computer Science", user1.getStudies());

        // Verifica dei campi del secondo utente
        User user2 = result.get(1);
        assertEquals(2L, user2.getId());
        assertEquals("Jane", user2.getName());
        assertEquals("Smith", user2.getSurname());
        assertEquals("jane.smith@example.com", user2.getEmail());
        assertEquals("password456", user2.getPassword());
        assertEquals(true, user2.isRegisteredWithFacebook());
        assertEquals("Mathematics", user2.getStudies());
    }

    /*
     * Test5: testGetUsersThrowsException
     * Precondizioni: Simulare un errore durante la chiamata a callRestGET
     * Azioni: Chiamare handleRequest su T23Service con "GetUsers".
     * Post-condizioni: Ci si aspetta un'IllegalArgumentException con il messaggio
     * appropriato.
     */
    @Test
    public void testGetUsersThrowsException() {
        String expectedExceptionMessage = "Chiamata REST fallita con stato 4xx: 400 BAD_REQUEST (eseguita da: callRestGET)";
        // Configurazione del server mock per rispondere con un errore
        mockServer.expect(requestTo(Base_URL + "/students_list"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        // Verifica che l'eccezione venga lanciata
        RestClientException exception = assertThrows(RestClientException.class, () -> {
            T23Service.handleRequest("GetUsers");
        });

        // Verifica che il messaggio dell'eccezione sia corretto
        assertEquals(expectedExceptionMessage, exception.getMessage());
    }

    /*
     * Test6: testGetAuthenticatedWithNullToken
     * Precondizioni: Simulare una chiamata a validateToken con un token null.
     * Azioni: Chiamare handleRequest su T23Service con "GetAuthenticated" e un
     * token non valido.
     * Post-condizioni: Ci si aspetta che il risultato sia false, poiché il token
     * non è valido
     * e il server risponde con un codice di stato 204 No Content, che indica che
     * non ci sono contenuti.
     */
    @Test
    public void testGetAuthenticatedWithNullToken() {
        // Chiamata al metodo handleRequest con un token null
        String invalidToken = "invalid.jwt.token";
        mockServer.expect(requestTo(Base_URL + "/validateToken"))
                .andRespond(withNoContent());
        Boolean result = (Boolean) T23Service.handleRequest("GetAuthenticated", invalidToken);
        // Verifica che il risultato sia false
        assertEquals(false, result);
    }
}
