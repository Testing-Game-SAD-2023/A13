package com.g2.Interfaces;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.boot.test.context.SpringBootTest;
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
        // Precondizione: Creare un server mock e inizializzare il T1Service
        mockServer = MockRestServiceServer.createServer(restTemplate);
        T1Service = new T1Service(restTemplate);
    }

    /*
     * Test1: testGetWrongNumberParameters
     * Precondizioni: Una chiamata a handleRequest con un parametro mancante (null).
     * Azioni: Invocare handleRequest su T1Service con "getClassUnderTest" e null.
     * Post-condizioni: Ci si aspetta una MissingParametersException con il
     * messaggio appropriato.
     */
    @Test
    public void testGetWrongNumberParameters() {
        String expected_exception = "[ServiceActionDefinition] Numero di parametri errato: atteso 1, ricevuto 0";
        MissingParametersException exception = assertThrows(MissingParametersException.class, () -> {
            T1Service.handleRequest("getClassUnderTest", null);
        });
        assertEquals(expected_exception, exception.getMessage());
    }

    /*
     * Test2: testGetList
     * Precondizioni: Il server mock è impostato per restituire una lista di oggetti
     * JSON quando richiesto.
     * Azioni: Invocare handleRequest per ottenere le classi.
     * Post-condizioni: Verificare che la lista restituita non sia nulla, contenga
     * il numero previsto di elementi,
     * e che le proprietà del primo oggetto corrispondano ai valori attesi.
     */
    @Test
    public void testGetList() {
        String endpoint = Base_URL + "/home";
        String jsonResponse = "[{\"name\":\"Calcolatrice\",\"date\":\"2024-10-30\",\"difficulty\":\"Beginner\",\"code_Uri\":\"Files-Upload/Calcolatrice/Calcolatrice.java\",\"description\":\"\",\"category\":[\"\",\"\",\"\"]}]";
        mockServer.expect(once(), requestTo(endpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        @SuppressWarnings("unchecked")
        List<ClassUT> resources = (List<ClassUT>) T1Service.handleRequest("getClasses");
        assertNotNull(resources);
        assertEquals(1, resources.size()); // Verifica che ci sia un solo elemento nella lista
        ClassUT firstResource = resources.get(0);
        assertEquals("2024-10-30", firstResource.getDate());
        assertEquals("Beginner", firstResource.getDifficulty());
        assertEquals("Files-Upload/Calcolatrice/Calcolatrice.java", firstResource.getcode_Uri());
        assertNotNull(firstResource.getCategory());
        assertEquals(3, firstResource.getCategory().size());

        mockServer.verify(); // Verifica che il server mock abbia ricevuto la richiesta
    }

    /*
     * Test3: testGetClassUnderTest
     * Precondizioni: Il server mock è impostato per restituire un contenuto byte
     * specifico per la classe richiesta.
     * Azioni: Invocare handleRequest per la classe specifica.
     * Post-condizioni: Verificare che il risultato corrisponda al contenuto atteso.
     */
    @Test
    public void testGetClassUnderTest() {
        String nomeCUT = "Calcolatrice";
        String endpoint = "/downloadFile/" + nomeCUT;
        String Base_URL_t = Base_URL + endpoint;

        String fileContent = "public class Calcolatrice {}";
        byte[] byteArray = fileContent.getBytes(StandardCharsets.UTF_8);

        mockServer.expect(once(), requestTo(Base_URL_t))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(byteArray, MediaType.APPLICATION_OCTET_STREAM));

        String result = (String) T1Service.handleRequest("getClassUnderTest", nomeCUT);
        assertNotNull(result);
        assertEquals(fileContent, result); // Verifica che il risultato sia quello atteso

        mockServer.verify(); // Verifica che il server mock abbia ricevuto la richiesta
    }

    /*
     * Test4: testGetClassUnderTestEmptyResponse
     * Precondizioni: Il server mock è impostato per restituire una risposta vuota
     * per la classe richiesta.
     * Azioni: Invocare handleRequest per la classe specifica.
     * Post-condizioni: Verificare che il risultato sia nullo a causa della risposta
     * vuota.
     */
    @Test
    public void testGetClassUnderTestEmptyResponse() {
        String nomeCUT = "Calcolatrice";
        String endpoint = "/downloadFile/" + nomeCUT;
        String Base_URL_t = Base_URL + endpoint;

        byte[] byteArray = new byte[0]; // Array vuoto

        mockServer.expect(once(), requestTo(Base_URL_t))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(byteArray, MediaType.APPLICATION_OCTET_STREAM));

        String result = (String) T1Service.handleRequest("getClassUnderTest", nomeCUT);
        assertNull(result); // Ci si aspetta un risultato nullo per la risposta vuota

        mockServer.verify(); // Verifica che il server mock abbia ricevuto la richiesta
    }

    /*
     * Test5: testGetClassUnderTestWithBOM
     * Precondizioni: Il server mock è impostato per restituire una risposta byte
     * con BOM.
     * Azioni: Invocare handleRequest per la classe specifica.
     * Post-condizioni: Verificare che il BOM venga rimosso dal risultato.
     */
    @Test
    public void testGetClassUnderTestWithBOM() {
        String nomeCUT = "Calcolatrice";
        String endpoint = "/downloadFile/" + nomeCUT;
        String Base_URL_t = Base_URL + endpoint;

        String fileContentWithBOM = "\uFEFFpublic class Calcolatrice {}";
        byte[] byteArray = fileContentWithBOM.getBytes(StandardCharsets.UTF_8);

        mockServer.expect(once(), requestTo(Base_URL_t))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(byteArray, MediaType.APPLICATION_OCTET_STREAM));

        String result = (String) T1Service.handleRequest("getClassUnderTest", nomeCUT);
        assertNotNull(result);
        assertEquals("public class Calcolatrice {}", result); // Verifica che il BOM sia rimosso

        mockServer.verify(); // Verifica che il server mock abbia ricevuto la richiesta
    }

    /*
     * Test6: testGetClassUnderTestWithSpecialCharacters
     * Precondizioni: Nome della classe contenente caratteri speciali.
     * Azioni: Invocare handleRequest per la classe specifica.
     * Post-condizioni: Verificare che il risultato corrisponda al contenuto atteso.
     */
    @Test
    public void testGetClassUnderTestWithSpecialCharacters() {
        String nomeCUT = "Calcolatrice@123"; // Nome con caratteri speciali
        String endpoint = "/downloadFile/" + nomeCUT;
        String Base_URL_t = Base_URL + endpoint;

        String fileContent = "public class Calcolatrice@123 {}";
        byte[] byteArray = fileContent.getBytes(StandardCharsets.UTF_8);

        mockServer.expect(once(), requestTo(Base_URL_t))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(byteArray, MediaType.APPLICATION_OCTET_STREAM));

        String result = (String) T1Service.handleRequest("getClassUnderTest", nomeCUT);
        assertNotNull(result);
        assertEquals(fileContent, result); // Verifica che il risultato sia quello atteso

        mockServer.verify(); // Verifica che il server mock abbia ricevuto la richiesta
    }

    /*
     * Test7: testGetClassUnderTestInvalidName
     * Precondizioni: Nome della classe non valido (es. vuoto o nullo).
     * Azioni: Invocare handleRequest con un nome di classe non valido.
     * Post-condizioni: Ci si aspetta una InvalidParameterTypeException.
     */
    @Test
    public void testGetClassUnderTestInvalidName() {
        String nomeCUT = null; // Nome vuoto
        InvalidParameterTypeException exception = assertThrows(InvalidParameterTypeException.class, () -> {
            T1Service.handleRequest("getClassUnderTest", nomeCUT);
        });

        // Assicurati che il messaggio dell'eccezione corrisponda a quello atteso
        assertEquals("[ServiceActionDefinition] Parametro 1 non è del tipo corretto: atteso String, ricevuto null",
                exception.getMessage());
    }

    /*
     * Test8: testGetListEmptyResponse
     * Precondizioni: Il server mock è impostato per restituire una lista vuota.
     * Azioni: Invocare handleRequest per ottenere la lista.
     * Post-condizioni: Verificare che la lista restituita sia vuota.
     */
    @Test
    public void testGetListEmptyResponse() {
        String endpoint = Base_URL + "/home";
        String jsonResponse = "[]"; // Risposta JSON vuota
        mockServer.expect(once(), requestTo(endpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        @SuppressWarnings("unchecked")
        List<ClassUT> resources = (List<ClassUT>) T1Service.handleRequest("getClasses");
        assertNotNull(resources);
        assertEquals(0, resources.size()); // Verifica che la lista sia vuota

        mockServer.verify(); // Verifica che il server mock abbia ricevuto la richiesta
    }

    /*
     * @Test
     * public void testGetClassUnderTestNonConvertibleByteArray() {
     * String nomeCUT = "Calcolatrice";
     * String endpoint = "/downloadFile/" + nomeCUT;
     * String Base_URL_t = Base_URL + endpoint;
     * 
     * // Simuliamo una risposta con un contenuto non convertibile in stringa
     * byte[] invalidByteArray = new byte[] { (byte) 0xFF, (byte) 0xFE, (byte) 0xFD
     * }; // Dati binari non validi
     * mockServer.expect(once(), requestTo(Base_URL_t))
     * .andExpect(method(HttpMethod.GET))
     * .andRespond(withSuccess(invalidByteArray,
     * MediaType.APPLICATION_OCTET_STREAM)); // Restituiamo un
     * // contenuto di tipo
     * // octet-stream
     * 
     * // Verifica che venga lanciata l'eccezione attesa quando si tenta di
     * convertire
     * // i dati in stringa
     * IllegalArgumentException exception =
     * assertThrows(IllegalArgumentException.class, () -> {
     * T1Service.handleRequest("getClassUnderTest", nomeCUT);
     * });
     * 
     * // Assicurati che il messaggio dell'eccezione corrisponda a quello atteso
     * assertNotNull(exception);
     * assertEquals("Input malformato o contiene caratteri non mappabili",
     * exception.getMessage());
     * }
     */
}