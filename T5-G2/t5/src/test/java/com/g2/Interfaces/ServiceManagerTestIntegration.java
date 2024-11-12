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

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import static org.springframework.test.web.client.ExpectedCount.once;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.web.client.RestTemplate;

import com.g2.t5.T5Application;

@SpringBootTest(classes = T5Application.class)
public class ServiceManagerTestIntegration {
    @Autowired
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private MockServiceManager serviceManager;
    private final String Base_URL_t1 = "http://manvsclass-controller-1:8080";
    private final String Base_URL_t4 = "http://t4-g18-app-1:3000";
    private final String Base_URL_t23 = "http://t23-g1-app-1:8080";

    /*
     * Metodo di inizializzazione per la registrazione dei servizi.
     * Precondizioni: RestTemplate non nullo.
     * Azioni: Registrazione del servizio ServiceManager.
     */
    @BeforeEach
    private void setUp_registrazione() {

        serviceManager = new MockServiceManager(restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);

    }

    /*
     * Test1 integrazione T1: testGetClassUnderTest
     * Precondizioni: Il servizio "T1Service" è registrato con la classe T1Service;
     * Azioni: Eseguire la richiesta per ottenere una classe (es. "Calcolatrice").
     * Post-condizioni: Verificare che la risposta ottenuta sia corretta e che la
     * richiesta al server mock sia stata eseguita correttamente.
     */
    @Test
    public void testGetClassUnderTest() {
        // pre-condizione
        serviceManager.registerService("T1Service", T1Service.class, restTemplate);
        // Nome della classe da recuperare
        String nomeCUT = "Calcolatrice";

        // Costruzione dell'endpoint e URL completo
        String endpoint = "/downloadFile/" + nomeCUT;
        String Base_URL_t = Base_URL_t1 + endpoint;

        // Contenuto del file simulato
        String fileContent = "public class Calcolatrice {}";
        byte[] byteArray = fileContent.getBytes(StandardCharsets.UTF_8);

        // Configurazione della simulazione della risposta dal mock server
        mockServer.expect(once(), requestTo(Base_URL_t))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(byteArray, MediaType.APPLICATION_OCTET_STREAM));

        // Esecuzione della richiesta tramite serviceManager
        String result = (String) serviceManager.handleRequest("T1Service", "getClassUnderTest", nomeCUT);

        // Verifica che il risultato non sia nullo
        assertNotNull(result);

        // Verifica che il risultato sia quello atteso
        assertEquals(fileContent, result);

        // Verifica che il mock server abbia ricevuto correttamente la richiesta
        mockServer.verify();
    }
    /*
     * Test2 integrazione T1: testGetClassesWithBadRequest
     * Precondizioni: Il servizio "T1Service" è registrato con la classe T1Service;
     * Il server mock è impostato per restituire un errore HTTP 400
     * (Bad Request) per una richiesta GET.
     * Azioni: Invocare il metodo getClasses per recuperare l'elenco delle classi.
     * Post-condizioni: Verificare che il servizio gestisca l'errore, lanciando
     * un'eccezione o restituendo un messaggio specifico.
     */

    @Test
    public void testGetClassesWithBadRequest() {

        serviceManager.registerService("T1Service", T1Service.class, restTemplate);
        String endpoint = Base_URL_t1 + "/home";

        // Configurazione del mock server per rispondere con un errore HTTP 400 (Bad
        // Request)
        mockServer.expect(once(), requestTo(endpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withBadRequest());

        // Variabile per catturare eccezioni o messaggi di errore
        String errorMessage = null;
        try {
            // Invoca il metodo getClasses e cattura il risultato o eventuali eccezioni
            serviceManager.handleRequest("T1Service", "getClasses");
        } catch (Exception e) {
            errorMessage = e.getMessage(); // Cattura il messaggio d'errore
        }

        // Verifica che il messaggio d'errore sia appropriato per un errore HTTP 400
        assertNotNull(errorMessage, "Expected an error message for HTTP 400 response");
        assertTrue(errorMessage.contains("Bad Request") || errorMessage.contains("400"),
                "Error message should indicate a Bad Request (400) error");

        // Verifica che tutte le aspettative del mock server siano state soddisfatte
        mockServer.verify();
    }

    /*
     * Test3 integrazione T4: testGetRisultati_ValidParameters
     * Precondizioni:Il servizio "T4Service" deve essere registrato;
     * Parametri di input validi per testare la chiamata all'endpoint
     * dei robot.
     * Azioni: Invocare handleRequest con la classe di test, il tipo di robot e la
     * difficoltà specificata.
     * Post-condizioni:
     * - Verificare che la risposta non sia nulla.
     * - Assicurarsi che la risposta corrisponda al contenuto mockato del server.
     * - Confermare che il server mock ha ricevuto la richiesta con i parametri
     * corretti.
     */
    @Test
    public void testGetRisultati_ValidParameters() {
        // precondizione: T4Service deve essere registrato
        serviceManager.registerService("T4Service", T4Service.class, restTemplate);
        String className = "TestClass";
        String robotType = "Evosuite";
        String difficulty = "Medium";
        String endpoint = "/robots";
        String expectedUrl = String.format("%s%s?difficulty=%s&testClassId=%s&type=%s", Base_URL_t4, endpoint,
                difficulty,
                className, robotType);
        String mockResponse = "{\"result\": \"success\"}";

        mockServer.expect(requestTo(expectedUrl))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("testClassId", className))
                .andExpect(queryParam("type", robotType))
                .andExpect(queryParam("difficulty", difficulty))
                .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        String result = (String) serviceManager.handleRequest("T4Service", "GetRisultati", className, robotType,
                difficulty);

        assertNotNull(result);
        assertEquals(mockResponse, result);
        mockServer.verify();
    }

    /*
     * Test4 integrazione T23: testGetAuthenticatedWithValidToken
     * Precondizioni:Il servizio "T23Service" deve essere registrato;
     * Simulazione di un token JWT valido.
     * Azioni: Configurare il mock server per restituire true per un token valido.
     * Post-condizioni: Ci si aspetta che il risultato sia true.
     */
    @Test
    public void testGetAuthenticatedWithValidToken() {
        // precondizione: T23Service deve essere registrato
        serviceManager.registerService("T23Service", T23Service.class, restTemplate);
        String validToken = "valid.jwt.token";
        mockServer.expect(requestTo(Base_URL_t23 + "/validateToken"))
                .andRespond(withSuccess("true", MediaType.APPLICATION_JSON));

        // Chiamata al metodo handleRequest con un token valido
        Boolean result = (Boolean) serviceManager.handleRequest("T23Service", "GetAuthenticated", validToken);

        // Verifica che il risultato sia true
        assertEquals(true, result);
    }

    /*
     * Test5 integrazione T23: testGetAuthenticatedWithInvalidToken
     * Precondizioni:Il servizio "T23Service" deve essere registrato;
     * Simulare una chiamata a validateToken con un token non valido.
     * Azioni: Chiamare handleRequest su T23Service con "GetAuthenticated" e un
     * token invalido.
     * Post-condizioni: Ci si aspetta che il risultato sia false, poiché il token
     * fornito
     * non è valido e il server risponde con un valore JSON "false".
     */
    @Test
    public void testGetAuthenticatedWithInvalidToken() {
        // precondizione: T23Service deve essere registrato
        serviceManager.registerService("T23Service", T23Service.class, restTemplate);
        // Chiamata al metodo handleRequest con un token null
        String invalidToken = "invalid.jwt.token";
        mockServer.expect(requestTo(Base_URL_t23 + "/validateToken"))
                .andRespond(withSuccess("false", MediaType.APPLICATION_JSON));
        Boolean result = (Boolean) serviceManager.handleRequest("T23Service", "GetAuthenticated", invalidToken);

        // Verifica che il risultato sia false
        assertEquals(false, result);
    }

}
