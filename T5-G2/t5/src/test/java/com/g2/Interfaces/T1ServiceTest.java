package com.g2.Interfaces;

import java.nio.charset.StandardCharsets;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.g2.Interfaces.ServiceActionDefinition.InvalidParameterTypeException;
import com.g2.Interfaces.ServiceActionDefinition.MissingParametersException;
import com.g2.Model.ClassUT;
import com.g2.t5.T5Application;
import static org.springframework.test.web.client.ExpectedCount.once;

@SpringBootTest(classes = T5Application.class)
public class T1ServiceTest {
    @Autowired
    private RestTemplate restTemplate;
    private T1Service T1Service;
    private MockRestServiceServer mockServer;
    private final String Base_URL = "http://manvsclass-controller-1:8080";

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        T1Service = new T1Service(restTemplate);

    }

    /*
     * T0_A - Eseguo una chiamata che non è stata registrata
     */
    @Test
    public void testGetWrongNumberParameters() {
        String expected_exception = "[ServiceActionDefinition] Numero di parametri errato: atteso 1, ricevuto 0";
        MissingParametersException exception = assertThrows(MissingParametersException.class, () -> {
            // baseService.handleRequest("testGetNoParams", "ErrorParam");
            T1Service.handleRequest("getClassUnderTest", null);
        });
        assertEquals(expected_exception, exception.getMessage());
    }

    /*
     * T4 - Eseguo una Get che mi deve dare una lista
     */
    @Test
    public void testGetList() {
        String endpoint = Base_URL + "/home";
        // Configuriamo il server mock per restituire una lista di oggetti JSON
        String jsonResponse = "[{\"name\":\"Calcolatrice\",\"date\":\"2024-10-30\",\"difficulty\":\"Beginner\",\"code_Uri\":\"Files-Upload/Calcolatrice/Calcolatrice.java\",\"description\":\"\",\"category\":[\"\",\"\",\"\"]}]";
        mockServer.expect(once(), requestTo(endpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // Chiamata al metodo che effettua la richiesta GET
        @SuppressWarnings("unchecked")
        List<ClassUT> resources = (List<ClassUT>) T1Service.handleRequest("getClasses");
        // Verifiche sulla lista restituita
        assertNotNull(resources);
        assertEquals(1, resources.size()); // Verifica che ci sia un solo elemento nella lista
        // Verifica delle proprietà del primo oggetto
        ClassUT firstResource = resources.get(0);
        assertEquals("2024-10-30", firstResource.getDate());
        assertEquals("Beginner", firstResource.getDifficulty());
        assertEquals("Files-Upload/Calcolatrice/Calcolatrice.java", firstResource.getcode_Uri());
        assertNotNull(firstResource.getCategory());
        assertEquals(3, firstResource.getCategory().size());

        // Verifica che il server mock abbia ricevuto la richiesta
        mockServer.verify();

    }

    @Test
    public void testGetClassUnderTest() {
        String nomeCUT = "Calcolatrice";
        String endpoint = "/downloadFile/" + nomeCUT;
        String Base_URL_t = Base_URL + endpoint;

        // Simuliamo una risposta in formato byte
        String fileContent = "public class Calcolatrice {}";
        byte[] byteArray = fileContent.getBytes(StandardCharsets.UTF_8);

        mockServer.expect(once(), requestTo(Base_URL_t))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(byteArray, MediaType.APPLICATION_OCTET_STREAM));

        // Chiamiamo il metodo da testare
        String result = (String) T1Service.handleRequest("getClassUnderTest", nomeCUT);

        // Verifiche
        assertNotNull(result);
        assertEquals(fileContent, result); // Verifica che il risultato sia quello atteso

        // Verifica che il server mock abbia ricevuto la richiesta
        mockServer.verify();
    }

    @Test
    public void testGetClassUnderTestEmptyResponse() {
        String nomeCUT = "Calcolatrice";
        String endpoint = "/downloadFile/" + nomeCUT;
        String Base_URL_t = Base_URL + endpoint;

        // Simuliamo una risposta vuota
        byte[] byteArray = new byte[0]; // Array vuoto

        mockServer.expect(once(), requestTo(Base_URL_t))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(byteArray, MediaType.APPLICATION_OCTET_STREAM));

        // Chiamiamo il metodo da testare
        String result = (String) T1Service.handleRequest("getClassUnderTest", nomeCUT);

        // Verifiche
        assertNull(result);

        // Verifica che il server mock abbia ricevuto la richiesta
        mockServer.verify();
    }

}