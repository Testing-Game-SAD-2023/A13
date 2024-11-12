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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.web.client.RestTemplate;

import com.g2.t5.T5Application;

@SpringBootTest(classes = T5Application.class)
public class T4ServiceTest {

        @Autowired
        private RestTemplate restTemplate;
        private T4Service T4Service;
        private MockRestServiceServer mockServer;
        private final String Base_URL = "http://t4-g18-app-1:3000";

        @BeforeEach
        public void setUp() {
                // Crea un server mock e inizializza il T4Service
                mockServer = MockRestServiceServer.createServer(restTemplate);
                T4Service = new T4Service(restTemplate);
        }

        /*
         * Test1: testGetRisultati_ValidParameters
         * Precondizioni: Parametri di input validi per testare la chiamata all'endpoint
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
                String className = "TestClass";
                String robotType = "Evosuite";
                String difficulty = "Medium";
                String endpoint = "/robots";
                String expectedUrl = String.format("%s%s?difficulty=%s&testClassId=%s&type=%s", Base_URL, endpoint,
                                difficulty,
                                className, robotType);
                String mockResponse = "{\"result\": \"success\"}";

                mockServer.expect(requestTo(expectedUrl))
                                .andExpect(method(HttpMethod.GET))
                                .andExpect(queryParam("testClassId", className))
                                .andExpect(queryParam("type", robotType))
                                .andExpect(queryParam("difficulty", difficulty))
                                .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

                String result = (String) T4Service.handleRequest("GetRisultati", className, robotType, difficulty);

                assertNotNull(result);
                assertEquals(mockResponse, result);
                mockServer.verify();
        }

        /*
         * Test2: testGetRisultati_EmptyResponse
         * Precondizioni: Parametri validi, ma il server restituisce una risposta vuota.
         * Azioni: Invocare handleRequest con parametri corretti.
         * Post-condizioni:
         * - La risposta deve essere null o gestita adeguatamente.
         */
        @Test
        public void testGetRisultati_EmptyResponse() {
                String className = "TestClass";
                String robotType = "Evosuite";
                String difficulty = "Medium";
                String endpoint = "/robots";
                String expectedUrl = String.format("%s%s?difficulty=%s&testClassId=%s&type=%s", Base_URL, endpoint,
                                difficulty,
                                className, robotType);

                mockServer.expect(requestTo(expectedUrl))
                                .andExpect(method(HttpMethod.GET))
                                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

                String result = (String) T4Service.handleRequest("GetRisultati", className, robotType, difficulty);

                assertNull(result);
                mockServer.verify();
        }

        /*
         * Test3: testGetRisultati_ServerError
         * Precondizioni: Parametri validi, ma il server risponde con errore 500.
         * Azioni: Invocare handleRequest con parametri corretti.
         * Post-condizioni:
         * - Generare un'eccezione RuntimeException.
         */
        @Test
        public void testGetRisultati_ServerError() {
                String className = "TestClass";
                String robotType = "Evosuite";
                String difficulty = "Medium";
                String endpoint = "/robots";
                String expectedUrl = String.format("%s%s?difficulty=%s&testClassId=%s&type=%s", Base_URL, endpoint,
                                difficulty,
                                className, robotType);

                mockServer.expect(requestTo(expectedUrl))
                                .andExpect(method(HttpMethod.GET))
                                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

                assertThrows(RuntimeException.class, () -> {
                        T4Service.handleRequest("GetRisultati", className, robotType, difficulty);
                });

                mockServer.verify();
        }

        /*
         * Test4: testCreateGame_ValidGameCreation
         * Precondizioni: Parametri validi per creare un nuovo gioco.
         * Azioni: Invocare handleRequest con parametri per un nuovo gioco.
         * Post-condizioni:
         * - Verificare che l'ID del gioco creato corrisponda a quello mockato.
         */
        @Test
        public void testCreateGame_ValidGameCreation() {
                String mockResponse = "{\"id\": 12345}";
                mockServer.expect(requestTo(Base_URL + "/games"))
                                .andExpect(method(HttpMethod.POST))
                                .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

                int gameId = (int) T4Service.handleRequest("CreateGame", "2024-10-29T10:00:00Z", "Hard", "GameName",
                                "Description", "Username");
                assertEquals(12345, gameId);
                mockServer.verify();
        }

        /*
         * Test5: testCreateRound_ValidResponse
         * Precondizioni: Parametri validi per creare un nuovo round.
         * Azioni: Invocare handleRequest con parametri per un nuovo round.
         * Post-condizioni:
         * - Verificare che l'ID del round creato corrisponda a quello mockato.
         */
        @Test
        public void testCreateRound_ValidResponse() {
                String mockResponse = "{\"id\": 6789}";
                mockServer.expect(requestTo(Base_URL + "/rounds"))
                                .andExpect(method(HttpMethod.POST))
                                .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

                int roundId = (int) T4Service.handleRequest("CreateRound", 12345, "ClassName", "2024-10-29T10:00:00Z");
                assertEquals(6789, roundId);
                mockServer.verify();
        }

        /*
         * Test6: testEndRound_SuccessResponse
         * Precondizioni: ID del round valido per terminare il round.
         * Azioni: Invocare handleRequest con ID del round per terminarlo.
         * Post-condizioni:
         * - Verificare che la risposta dello stato sia "closed".
         */
        @Test
        public void testEndRound_SuccessResponse() {
                int roundId = 6789;
                String expectedUrl = String.format("%s/rounds/%d", Base_URL, roundId);
                String mockResponse = "{\"status\": \"closed\"}";

                mockServer.expect(requestTo(expectedUrl))
                                .andExpect(method(HttpMethod.PUT))
                                .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

                String result = (String) T4Service.handleRequest("EndRound", "2024-10-29T11:00:00Z", roundId);
                assertEquals(mockResponse, result);
                mockServer.verify();
        }

        /*
         * Test7: testCreateTurn_ValidResponse
         * Precondizioni: Parametri validi per creare un nuovo turno.
         * Azioni: Invocare handleRequest con parametri per un nuovo turno.
         * Post-condizioni:
         * - Verificare che la risposta del turno creato corrisponda a quella mockata.
         */
        @Test
        public void testCreateTurn_ValidResponse() {
                String mockResponse = "{\"turnId\": 3456}";
                mockServer.expect(requestTo(Base_URL + "/turns"))
                                .andExpect(method(HttpMethod.POST))
                                .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

                String result = (String) T4Service.handleRequest("CreateTurn", "PlayerId", 6789,
                                "2024-10-29T11:00:00Z");
                assertEquals(mockResponse, result);
                mockServer.verify();
        }

        /*
         * Test8: testEndTurn_SuccessResponse
         * Precondizioni: ID del turno valido per terminare il turno.
         * Azioni: Invocare handleRequest con ID del turno per terminarlo.
         * Post-condizioni:
         * - Verificare che lo stato sia "completed".
         */
        @Test
        public void testEndTurn_SuccessResponse() {
                int turnId = 3456;
                String expectedUrl = String.format("http://t4-g18-app-1:3000/turns/%d", turnId);
                String mockResponse = "{\"status\": \"completed\"}";

                mockServer.expect(requestTo(expectedUrl))
                                .andExpect(method(HttpMethod.PUT))
                                .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

                String result = (String) T4Service.handleRequest("EndTurn", "90", "2024-10-29T11:00:00Z", turnId);
                assertEquals(mockResponse, result);
                mockServer.verify();
        }

        /*
         * Test9: testCreateScalata_ValidResponse
         * Precondizioni: Parametri validi per creare una nuova scalata.
         * Azioni: Invocare handleRequest con parametri per una nuova scalata.
         * Post-condizioni:
         * - Verificare che l'ID della scalata creata corrisponda a quello mockato.
         */
        @Test
        public void testCreateScalata_ValidResponse() {
                String mockResponse = "{\"scalataId\": 7890}";
                mockServer.expect(requestTo("http://t4-g18-app-1:3000/turns"))
                                .andExpect(method(HttpMethod.POST))
                                .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

                String result = (String) T4Service.handleRequest("CreateScalata", "PlayerId", "ScalataName", "10:00:00",
                                "2024-10-29");
                assertEquals(mockResponse, result);
                mockServer.verify();
        }

        /*
         * Test10: testEndGame_ValidGameEnd
         * Precondizioni: ID di gioco valido e parametri corretti per chiudere il gioco.
         * Azioni: Invocare EndGame con parametri validi per chiudere il gioco.
         * Post-condizioni:
         * - Verificare che la risposta del gioco terminato corrisponda a quella
         * mockata.
         */
        @Test
        public void testEndGame_ValidGameEnd() {
                // Parametri di input del metodo
                int gameId = 12345;
                String username = "TestUser";
                String closedAt = "2024-10-29T12:00:00Z";
                int score = 100;
                Boolean isWinner = true;

                // URL di endpoint atteso
                String expectedUrl = String.format("%s/games/%d", Base_URL, gameId);

                // Mock della risposta attesa
                String mockResponse = "{\"status\": \"ended\", \"score\": 100, \"isWinner\": true}";

                // Configurazione del mock server
                mockServer.expect(requestTo(expectedUrl))
                                .andExpect(method(HttpMethod.POST))
                                .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

                // Chiamata al metodo sotto test

                // EndGame
                String response = (String) T4Service.handleRequest("EndGame", gameId, username, closedAt, score,
                                isWinner);

                // Verifica della risposta
                assertEquals(mockResponse, response);

                // Verifica delle aspettative del mock server
                mockServer.verify();
        }

        /*
         * Test11: testEndGame_ValidGameEnd_NotWinner
         * Precondizioni: ID di gioco valido, username, data di chiusura, punteggio e
         * flag isWinner impostato a false.
         * Azioni: Invocare EndGame con parametri validi per chiudere il gioco.
         * Post-condizioni: Verificare che la risposta sia quella mockata e che le
         * aspettative del mock server siano soddisfatte.
         */

        @Test
        public void testEndGame_ValidGameEnd_NotWinner() {
                // Parametri di input del metodo
                int gameId = 12345;
                String username = "TestUser";
                String closedAt = "2024-10-29T12:00:00Z";
                int score = 100;
                Boolean isWinner = false;

                // URL di endpoint atteso
                String expectedUrl = String.format("%s/games/%d", Base_URL, gameId);

                // Mock della risposta attesa
                String mockResponse = "{\"status\": \"ended\", \"score\": 100, \"isWinner\": false}";

                // Configurazione del mock server
                mockServer.expect(requestTo(expectedUrl))
                                .andExpect(method(HttpMethod.POST))
                                .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

                // Chiamata al metodo sotto test
                String response = (String) T4Service.handleRequest("EndGame", gameId, username, closedAt, score,
                                isWinner);

                // Verifica della risposta
                assertEquals(mockResponse, response);

                // Verifica delle aspettative del mock server
                mockServer.verify();
        }

}
