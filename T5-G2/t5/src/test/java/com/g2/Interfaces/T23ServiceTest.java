package com.g2.Interfaces;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
// e altri import necessari
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.Interfaces.ServiceActionDefinition.InvalidParameterTypeException;
import com.g2.Interfaces.ServiceActionDefinition.MissingParametersException;
import com.g2.Model.ClassUT;
import com.g2.t5.T5Application;
import static org.springframework.test.web.client.ExpectedCount.once;
import com.g2.Model.User;

import org.springframework.http.HttpMethod;

import org.springframework.http.MediaType;

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
    * Test3: testGetAuthenticatedWithNullToken
    * Precondizioni: Testare il metodo handleRequest con un token null.
    * Azioni: Invocare handleRequest su T23Service con "GetAuthenticated" e un token null.
    * Post-condizioni: Ci si aspetta che il risultato sia false e venga stampato un messaggio di log.
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
     * Post-condizioni: Ci si aspetta di ricevere una lista di utenti correttamente popolata.
     */

     @Test
     public void testGetUsersSuccess() {
         // Dati di test: Lista di utenti fittizi
         List<User> mockUsers = new ArrayList<>();
         mockUsers.add(new User(1L, "John", "Doe", "john.doe@example.com", "password123", false, "Computer Science", null));
         mockUsers.add(new User(2L, "Jane", "Smith", "jane.smith@example.com", "password456", true, "Mathematics", null));
 
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
 
         // Verifica che il risultato corrisponda ai dati mock
         assertNotNull(result);
         assertEquals(2, result.size());
         assertEquals("John", result.get(0).getName());
         assertEquals("Jane", result.get(1).getName());
     }

    /*
     * Test5: testGetUsersThrowsException
     * Precondizioni: Simulare un errore durante la chiamata a callRestGET
     * Azioni: Chiamare handleRequest su T23Service con "GetUsers".
     * Post-condizioni: Ci si aspetta un'IllegalArgumentException con il messaggio appropriato.
     */
       
     @Test
    public void testGetUsersThrowsException() {

    String expectedExceptionMessage = "[GETUSERS] Errore durante il recupero degli utenti: [CallRestGET] Chiamata GET fallita con stato: org.springframework.web.client.HttpClientErrorException$BadRequest: 400 Bad Request: [no body]";

    // Configurazione del server mock per rispondere con un errore
    mockServer.expect(requestTo(Base_URL + "/students_list"))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withStatus(HttpStatus.BAD_REQUEST));

    // Verifica che l'eccezione venga lanciata
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        T23Service.handleRequest("GetUsers");
    });

    // Verifica che il messaggio dell'eccezione sia corretto
    assertEquals(expectedExceptionMessage, exception.getMessage());
}









}