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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.g2.Interfaces.ServiceActionDefinition.InvalidParameterTypeException;
import com.g2.Interfaces.ServiceActionDefinition.MissingParametersException;
import com.g2.t5.T5Application;

@SpringBootTest(classes = T5Application.class)
public class BaseServiceTest {

    @Autowired
    private RestTemplate restTemplate;
    private BaseServiceImpl baseService;
    private MockRestServiceServer mockServer;
    private String Base_URL = "http://mock_url:123";


    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        baseService = new BaseServiceImpl(restTemplate);
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
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            baseService.handleRequest("testGetWithoutEndpoint");
        });
        assertEquals("Chiamata REST fallita: L'endpoint non può essere nullo o vuoto. (eseguita da: callRestGET)", exception.getMessage());
    }

    /*
    * T3 - GET endpoint errato che non esiste
    */
    @Test
    public void testGetNoExistEndpoint() {
        String expected_exception = "Chiamata REST fallita con stato 4xx: 404 NOT_FOUND (eseguita da: callRestGET)";
        String endpoint = Base_URL + "/nonEsisteEndpoint";
        mockServer.expect(requestTo(endpoint)).andRespond(withStatus(HttpStatus.NOT_FOUND));
        RestClientException exception = assertThrows(RestClientException.class, () -> {
            baseService.handleRequest("testGetSetEndpoint", "/nonEsisteEndpoint");
        });
        assertEquals(expected_exception, exception.getMessage());
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

    // eccezione su GET LIST con eccezione 
    @Test
    public void testGetListException() {
        String Expected_exception = "Chiamata REST fallita con stato 4xx: 400 BAD_REQUEST (eseguita da: callRestGET)";
        mockServer.expect(requestTo(Base_URL + "/resources"))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.BAD_REQUEST));
        RestClientException exception = assertThrows(RestClientException.class, () -> {
            baseService.handleRequest("testGetList");
        });
        assertEquals(Expected_exception, exception.getMessage());
    }

    @Test
    public void testGetListNullEndpoint() {
        String expected_exception = "Chiamata REST fallita: L'endpoint non può essere nullo o vuoto. (eseguita da: callRestGET)";
        // Chiamiamo il metodo testGetList
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            baseService.handleRequest("testGetListWithoutEndpoint");
        });
        assertEquals(expected_exception, exception.getMessage());
    }

    /*
     * T5 - eseguo una get ma il server mi risponde 400 
     */
    @Test
    public void testGetWithBadRequest() {
        String Expected_exception = "Chiamata REST fallita con stato 4xx: 400 BAD_REQUEST (eseguita da: callRestGET)";
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
     * T5 - eseguo una get ma il server mi risponde 500 
     */
    @Test
    public void testGetWithMultipleChoices() {
        String Expected_exception = "Chiamata REST fallita con stato 5xx: 500 INTERNAL_SERVER_ERROR (eseguita da: callRestGET)";
        mockServer.expect(requestTo(Base_URL + "/testGetNoParams"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR)); // Simula una risposta 5xx
    
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
     *  T4 - Eseguo una Post con un formdate nullo
     */
    @Test
    public void testPostWithNullFormData() {
        // Configura l'endpoint e la risposta attesa
        String expectedResponse = "formData non può essere nullo";  // Risposta simulata
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            baseService.handleRequest("testPostNullForm");
        });
        // Verifica la risposta
        assertEquals(expectedResponse, exception.getMessage());
    }

    /*
     *  Testo post con custom header con 200 
     */
    @Test
    public void testCreateResourceSuccess() {
        // Simula una risposta 200 OK dal server
        String endpoint = Base_URL + "/resources/create";
        mockServer.expect(requestTo(endpoint))
                  .andExpect(method(HttpMethod.POST))
                  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                  .andRespond(withSuccess("{\"result\":\"Resource created successfully\"}", MediaType.APPLICATION_JSON));
        // Esegui il metodo e verifica il risultato
        String response = baseService.createResource("ExampleName", "ExampleData");
        assertEquals("{\"result\":\"Resource created successfully\"}", response);
        // Verifica che tutte le richieste siano state soddisfatte
        mockServer.verify();
    }

    /*
     * Testo post con custom header con errore 400
     */
    @Test
    public void testCreateResourceFailure() {
        // Simula una risposta 400 Bad Request dal server
        String endpoint = Base_URL + "/resources/create";
        String Expected_exception = "Chiamata REST fallita con stato 4xx: 400 BAD_REQUEST (eseguita da: callRestPost)";
        mockServer.expect(requestTo(endpoint))
                  .andExpect(method(HttpMethod.POST))
                  .andRespond(withBadRequest());

        // Esegui il metodo e verifica che venga lanciata un'eccezione
        Exception exception = assertThrows(RuntimeException.class, () -> {
            baseService.createResource("ExampleName", "ExampleData");
        });
        assertEquals(Expected_exception, exception.getMessage());

        // Verifica che tutte le richieste siano state soddisfatte
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

    @Test
    public void testDeleteResource_Success() {
        String resourceId = "123";
        String endpoint = Base_URL + "/resources/" + resourceId;
        mockServer.expect(requestTo(endpoint))
                  .andExpect(method(HttpMethod.DELETE))
                  .andRespond(withSuccess("{\"message\":\"Success\"}", MediaType.APPLICATION_JSON)); // 204 No Content

        baseService.handleRequest("testDelete", resourceId); // Non dovrebbe lanciare eccezioni
        mockServer.verify();
    }

    @Test
    public void testDeleteResource_NotFound() {
        String resourceId = "123";
        String expected_exception = "Chiamata REST fallita con stato 4xx: 404 NOT_FOUND (eseguita da: callRestDelete)";
        mockServer.expect(requestTo(Base_URL + "/resources/" + resourceId))
                  .andExpect(method(HttpMethod.DELETE))
                  .andRespond(withStatus(HttpStatus.NOT_FOUND)); // 404 Not Found

        RestClientException exception = assertThrows(RestClientException.class, () -> {
            baseService.handleRequest("testDelete", resourceId); // Non dovrebbe lanciare eccezioni
        });
        assertEquals(expected_exception, exception.getMessage());
    }

    /*
     *   endpoint nullo
     */
    @Test
    public void testBuildUri_WithNullEndpoint_ShouldThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            baseService.buildUri_test(null, null);
        });
        assertEquals("L'endpoint non può essere nullo o vuoto.", exception.getMessage());
    }

    /*
     * endpoint vuoto 
     */
    @Test
    public void testBuildUri_WithEmptyEndpoint_ShouldThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            baseService.buildUri_test("", null);
        });
        assertEquals("L'endpoint non può essere nullo o vuoto.", exception.getMessage());
    }

    /*
     * endpoint con query ma chiave vuota
     */
    @Test
    public void testBuildUri_WithInvalidQueryParamKey_ShouldThrowIllegalArgumentException() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("", "value"); // Chiave vuota
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            baseService.buildUri_test("/validEndpoint", queryParams);
        });
        assertEquals("URL malformato: Le chiavi dei parametri non possono essere nulle o vuote.", exception.getMessage());

    }

    /*
     * endpoint con query ma valori vuoti
     */
    @Test
    public void testBuildUri_WithNullQueryParamValue_ShouldThrowIllegalArgumentException() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("key", null); // Valore nullo
        Exception exception =  assertThrows(IllegalArgumentException.class, () -> {
            baseService.buildUri_test("/validEndpoint", queryParams);
        });
        assertEquals("URL malformato: I valori dei parametri non possono essere nulli.", exception.getMessage());
    }

    /*
     *  endpoint con queryparam vuoto non deve generare eccezioni 
     */
    @Test
    public void testBuildUri_WithEmptyQueryParams_ShouldReturnValidUrl() {
        // Endpoint valido e query parameters vuoti
        String endpoint = "/validEndpoint";
        String expectedUrl = Base_URL + "/validEndpoint"; // Sostituisci con il tuo baseUrl

        // Invoca il metodo buildUri senza aspettarsi eccezioni
        String resultUrl = baseService.buildUri_test(endpoint, Collections.emptyMap());

        // Verifica che l'URL risultante sia quello atteso
        assertEquals(expectedUrl, resultUrl);
    }

    /*
     *  endpoint con queryparam
     */
    @Test
    public void testBuildUri_WithNullQueryParamKey_ShouldThrowIllegalArgumentException() {
        // Creazione di query parameters con una chiave vuota
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(null, "someValue"); // Chiave vuota

        // Passa un endpoint valido con una chiave di query parameter vuota
        Exception exception =  assertThrows(IllegalArgumentException.class, () -> {
            baseService.buildUri_test("/validEndpoint", queryParams);
        });
        assertEquals("URL malformato: Le chiavi dei parametri non possono essere nulle o vuote.", exception.getMessage());

    }

    /*
     *  Testo il comportamento atteso di convertToString
     */
    @Test
    public void testConvertToString(){
        String fileContent = "public class Calcolatrice {}";
        byte[] byteArray = fileContent.getBytes(StandardCharsets.UTF_8);
        String result = baseService.convertToString(byteArray);
        assertNotNull(result);
        assertEquals(fileContent, result); // Verifica che il risultato sia quello atteso
        mockServer.verify(); // Verifica che il server mock abbia ricevuto la richiesta
    }

    /*
     *  Testo che convertToString dia eccezione se gli dò un null
     */
    @Test
    public void testConvertToStringNullinput(){
        Exception e = assertThrows(IllegalArgumentException.class, ()->{
            baseService.convertToString(null);
        });
        assertEquals("L'array di byte non può essere nullo.", e.getMessage());
    }

    /*
     *  Testo che convertToString dia eccezione se gli dò un array empty
     */
    @Test
    public void testConvertToStringempthyinput(){
        Exception e = assertThrows(IllegalArgumentException.class, ()->{
            baseService.convertToString(new byte[0]);
        });
        assertEquals("L'array di byte non può essere nullo.", e.getMessage());
    }

    /*
     * Test23: testConvertToStringWithMalformedInput
     * Precondizioni: Provo a fornire un Byte Array che rappresenta un immagine piuttosto che una stringa. 
     * Azioni: Invocare il metodo convertToString con l'array di byte.
     * Post-condizioni: Verificare che venga sollevata un'eccezione
     * IllegalArgumentException
     * e che il messaggio dell'eccezione indichi un input malformato o caratteri non
     * mappabili.
     */
    @Test
    public void testConvertToStringWithMalformedInput() {
        // Esempio di un array di byte che simula il contenuto di un file JPEG
        // (Questi non sono byte reali di un'immagine, ma un esempio per illustrare il concetto)
        byte[] jpegmalformedByteArray = new byte[] {
            (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 
            (byte) 0x00, (byte) 0x10, (byte) 0x4A, (byte) 0x46, 
            (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0x01, 
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            // Altri byte dell'immagine JPEG...
            (byte) 0xFF, (byte) 0xD9 // Marker di fine immagine
        };

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            baseService.convertToString(jpegmalformedByteArray);
        });

        assertNotNull(exception, "Expected IllegalArgumentException to be thrown");
        assertEquals("Erorr conversione, Il byte array contiene byte non validi per UTF-8.", exception.getMessage());
    }

       /*
     * Test25: testRemoveBOMWithValidInput
     * Precondizioni: Una stringa con BOM è preparata per il test.
     * Azioni: Invocare il metodo removeBOM con la stringa contenente il BOM.
     * Post-condizioni: Verificare che il BOM sia rimosso correttamente dalla
     * stringa
     * e che il risultato corrisponda alla stringa attesa senza BOM.
     */
    @Test
    public void testRemoveBOMWithValidInput() {
        String inputWithBOM = "\uFEFFHello World!";
        String expectedOutput = "Hello World!";
        String result = baseService.removeBOM(inputWithBOM);
        assertNotNull(result);
        assertEquals(expectedOutput, result);
    }

    /*
     * Test26: testRemoveBOMWithoutBOM
     * Precondizioni: Una stringa senza BOM è preparata per il test.
     * Azioni: Invocare il metodo removeBOM con la stringa che non contiene BOM.
     * Post-condizioni: Verificare che la stringa di output sia la stessa della
     * stringa di input,
     * poiché non c'è nulla da rimuovere.
     */
    @Test
    public void testRemoveBOMWithoutBOM() {
        String inputWithoutBOM = "Hello World!";
        String result = baseService.removeBOM(inputWithoutBOM);
        assertNotNull(result);
        assertEquals(inputWithoutBOM, result);
    }

    /*
     * Test27: testRemoveBOMWithNullInput
     * Precondizioni: L'input è null.
     * Azioni: Invocare il metodo removeBOM con un input null.
     * Post-condizioni: Verificare che il risultato sia null, come ci si aspetta
     * quando l'input è null.
     */
    @Test
    public void testRemoveBOMWithNullInput() {
        String result = baseService.removeBOM(null);
        assertNull(result);
    }

    /*
     * Esecuzione con custom headers
     */
    @Test
    void testCallRestPostWithCustomHeaders() {
        // Setup
        String endpoint = Base_URL + "/api";
        MultiValueMap<String, String> formData =  new LinkedMultiValueMap<>();
        Map<String, String> queryParams = new HashMap<>();
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("X-Custom-Header", "CustomValue");
        Class<String> responseType = String.class;

        // Configura il mock server per aspettarsi una richiesta e restituire una risposta
        mockServer.expect(requestTo(endpoint))
                    .andExpect(method(HttpMethod.POST))
                    .andExpect(header("X-Custom-Header", "CustomValue"))
                    .andExpect(header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8"))
                    .andRespond(withSuccess("Success", MediaType.TEXT_PLAIN));

        // Call the method
        String result = baseService.callRestPost("/api", formData, queryParams, customHeaders, responseType);
        // Verifica che la risposta sia come ci si aspetta
        assertEquals("Success", result);
        // Verifica che tutte le richieste siano state soddisfatte
        mockServer.verify();
    }

    @Test
    void testTimeoutCall(){
        String expected_exception = "Chiamata REST fallita con stato 5xx: 504 GATEWAY_TIMEOUT (eseguita da: callRestGET)";
        String endpoint = Base_URL + "/testGetNoParams";
        // Configura il server mock per simulare un timeout
        mockServer.expect(requestTo(endpoint)).andRespond(withStatus(HttpStatus.GATEWAY_TIMEOUT));

        RestClientException exception = assertThrows(RestClientException.class, () -> {
            baseService.handleRequest("testGetNoParams"); // Non dovrebbe lanciare eccezioni
        });
        assertEquals(expected_exception, exception.getMessage());
    }
}