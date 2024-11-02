package com.g2.Interfaces;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
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

import com.g2.Interfaces.ServiceActionDefinition.InvalidParameterTypeException;
import com.g2.Interfaces.ServiceActionDefinition.MissingParametersException;
import com.g2.Model.ClassUT;
import com.g2.t5.T5Application;
import static org.springframework.test.web.client.ExpectedCount.once;

import org.springframework.http.HttpMethod;

import org.springframework.http.MediaType;

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

    /*
     * Test10: testGetClassesLargeResponse
     * Precondizioni: Il server mock è impostato per restituire una grande lista di
     * oggetti JSON.
     * Azioni: Invocare handleRequest per ottenere le classi.
     * Post-condizioni: Verificare che la lista restituita contenga il numero
     * previsto di elementi.
     */
    @Test
    public void testGetClassesLargeResponse() {
        String endpoint = Base_URL + "/home";
        String jsonResponse = "[" +
                "{\"name\":\"Class1\",\"date\":\"2024-10-30\",\"difficulty\":\"Beginner\",\"code_Uri\":\"Files/Class1.java\"},"
                +
                "{\"name\":\"Class2\",\"date\":\"2024-10-31\",\"difficulty\":\"Intermediate\",\"code_Uri\":\"Files/Class2.java\"},"
                +
                "{\"name\":\"Class3\",\"date\":\"2024-11-01\",\"difficulty\":\"Advanced\",\"code_Uri\":\"Files/Class3.java\"}"
                +
                "]";

        mockServer.expect(once(), requestTo(endpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        @SuppressWarnings("unchecked")
        List<ClassUT> resources = (List<ClassUT>) T1Service.handleRequest("getClasses");
        assertNotNull(resources);
        assertEquals(3, resources.size());
        assertEquals("Class3", resources.get(2).getName());

        mockServer.verify();
    }

    /*
     * Test11: testGetClassesWithPartialData
     * Precondizioni: Il server mock è impostato per restituire una lista di oggetti
     * JSON con alcuni campi mancanti.
     * Azioni: Invocare handleRequest per ottenere le classi.
     * Post-condizioni: Verificare che gli oggetti con campi mancanti siano gestiti
     * correttamente.
     */
    @Test
    public void testGetClassesWithPartialData() {
        String endpoint = Base_URL + "/home";
        String jsonResponse = "[" +
                "{\"name\":\"Class1\",\"date\":\"2024-10-30\",\"difficulty\":\"Beginner\"}," +
                "{\"name\":\"Class2\",\"code_Uri\":\"Files/Class2.java\"}," +
                "{\"name\":\"Class3\",\"date\":\"2024-11-01\"}" +
                "]";

        mockServer.expect(once(), requestTo(endpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        @SuppressWarnings("unchecked")
        List<ClassUT> resources = (List<ClassUT>) T1Service.handleRequest("getClasses");
        assertNotNull(resources);
        assertEquals(3, resources.size()); // Verifica che ci siano 3 elementi nella lista
        assertNull(resources.get(1).getDifficulty()); // Verifica che il campo mancante sia nullo
        assertNull(resources.get(2).getcode_Uri());

        mockServer.verify();
    }

    /*
     * Test12: testGetClassesWithLargeDataset
     * Precondizioni: Il server mock è impostato per restituire una lista di 1000
     * oggetti JSON.
     * Azioni: Invocare handleRequest per ottenere le classi.
     * Post-condizioni: Verificare che la lista restituita contenga 1000 elementi.
     */
    @Test
    public void testGetClassesWithLargeDataset() {
        String endpoint = Base_URL + "/home";
        StringBuilder jsonResponse = new StringBuilder("[");
        for (int i = 1; i <= 1000; i++) {
            jsonResponse.append("{\"name\":\"Class").append(i)
                    .append("\",\"date\":\"2024-10-").append(String.format("%02d", (i % 31) + 1))
                    .append("\",\"difficulty\":\"Beginner\",\"code_Uri\":\"Files/Class").append(i).append(".java\"},");
        }
        jsonResponse.deleteCharAt(jsonResponse.length() - 1).append("]");

        mockServer.expect(once(), requestTo(endpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse.toString(), MediaType.APPLICATION_JSON));

        @SuppressWarnings("unchecked")
        List<ClassUT> resources = (List<ClassUT>) T1Service.handleRequest("getClasses");
        assertNotNull(resources);
        assertEquals(1000, resources.size()); // Verifica che ci siano 1000 elementi nella lista
        assertEquals("Class1000", resources.get(999).getName()); // Verifica l'ultimo elemento

        mockServer.verify();
    }

    /*
     * Test13: testGetClassUnderTestWithWhitespace
     * Precondizioni: Nome della classe con spazi inutili.
     * Azioni: Invocare handleRequest per la classe specifica.
     * Post-condizioni: Verificare che il risultato corrisponda al contenuto atteso.
     */
    @Test
    public void testGetClassUnderTestWithWhitespace() {
        String nomeCUT = "    Calcolatrice   "; // Nome con spazi
        String endpoint = "/downloadFile/Calcolatrice";
        String Base_URL_t = Base_URL + endpoint;

        String fileContent = "public class Calcolatrice {}";
        byte[] byteArray = fileContent.getBytes(StandardCharsets.UTF_8);

        mockServer.expect(once(), requestTo(Base_URL_t))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(byteArray, MediaType.APPLICATION_OCTET_STREAM));

        String result = (String) T1Service.handleRequest("getClassUnderTest", nomeCUT.trim());
        assertNotNull(result);
        assertEquals(fileContent, result); // Verifica che il risultato sia quello atteso

        mockServer.verify();
    }

    /*
     * Test14: testGetListMultipleItems
     * Precondizioni: L'endpoint "/home" restituisce un JSON con due elementi.
     * Azioni: Invocare handleRequest per ottenere la lista delle classi.
     * Post-condizioni: Verificare che il risultato contenga due elementi con i dati
     * corretti.
     */
    @Test
    public void testGetListMultipleItems() {
        String endpoint = Base_URL + "/home";
        String jsonResponse = "[" +
                "{\"name\":\"Calcolatrice\",\"date\":\"2024-10-30\",\"difficulty\":\"Beginner\",\"code_Uri\":\"Files-Upload/Calcolatrice/Calcolatrice.java\",\"description\":\"\",\"category\":[\"\",\"\",\"\"]},"
                +
                "{\"name\":\"TestApp\",\"date\":\"2024-10-31\",\"difficulty\":\"Intermediate\",\"code_Uri\":\"Files-Upload/TestApp/TestApp.java\",\"description\":\"Test Application\",\"category\":[\"Example\",\"Test\",\"\"]}"
                +
                "]";

        mockServer.expect(once(), requestTo(endpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        @SuppressWarnings("unchecked")
        List<ClassUT> resources = (List<ClassUT>) T1Service.handleRequest("getClasses");
        assertNotNull(resources);
        assertEquals(2, resources.size()); // Verifica che ci siano due elementi nella lista

        // Dati attesi per i test
        String[][] expectedData = {
                { "2024-10-30", "Beginner", "Files-Upload/Calcolatrice/Calcolatrice.java", "3" },
                { "2024-10-31", "Intermediate", "Files-Upload/TestApp/TestApp.java", "3" }
        };

        // Verifica dei dati per ciascun elemento nella lista
        for (int i = 0; i < resources.size(); i++) {
            ClassUT resource = resources.get(i);
            assertEquals(expectedData[i][0], resource.getDate());
            assertEquals(expectedData[i][1], resource.getDifficulty());
            assertEquals(expectedData[i][2], resource.getcode_Uri());
            assertNotNull(resource.getCategory());
            assertEquals(Integer.parseInt(expectedData[i][3]), resource.getCategory().size());
        }

        mockServer.verify(); // Verifica che il server mock abbia ricevuto la richiesta
    }

    /*
     * Test15: testGetClassUnderTestWithLongName
     * Precondizioni: Nome della classe è molto lungo e il server mock è impostato
     * per restituire una risposta corretta.
     * Azioni: Invocare handleRequest con un nome di classe lungo.
     * Post-condizioni: Verificare che il risultato corrisponda al contenuto atteso
     * per un nome di classe lungo.
     */
    @Test
    public void testGetClassUnderTestWithLongName() {
        String nomeCUT = "CalcolatriceConUnNomeMoltoLungoPippooooooooo";
        String endpoint = "/downloadFile/" + nomeCUT;
        String Base_URL_t = Base_URL + endpoint;

        String fileContent = "public class CalcolatriceConUnNomeMoltoLungoPippooooooooo {}";
        byte[] byteArray = fileContent.getBytes(StandardCharsets.UTF_8);

        mockServer.expect(once(), requestTo(Base_URL_t))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(byteArray, MediaType.APPLICATION_OCTET_STREAM));

        String result = (String) T1Service.handleRequest("getClassUnderTest", nomeCUT);
        assertNotNull(result);
        assertEquals(fileContent, result);

        mockServer.verify();
    }

    /*
     * Test16: testMultipleConcurrentRequests
     * Precondizioni: Il server mock è impostato per restituire la risposta per
     * richieste concorrenti.
     * Azioni: Invocare getClassUnderTest in due thread separati.
     * Post-condizioni: Verificare che entrambi i risultati siano corretti e che il
     * server mock gestisca le richieste simultanee.
     */
    @Test
    public void testMultipleConcurrentRequests() throws InterruptedException {
        Runnable task = () -> {
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
            assertEquals(fileContent, result);
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        mockServer.verify();
    }

    /*
     * Test17: testGetClassesWithEmptyResponse
     * Precondizioni: Il server mock è impostato per restituire una risposta JSON
     * vuota per una richiesta GET.
     * Azioni: Invocare il metodo getClasses per recuperare l'elenco delle classi.
     * Post-condizioni: Verificare che il risultato sia null o vuoto.
     */
    @Test
    public void testGetClassesWithEmptyResponse() {
        String endpoint = Base_URL + "/home";

        // Configurazione del mock server per rispondere con una stringa vuota su
        // richiesta GET
        mockServer.expect(once(), requestTo(endpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

        // Invoca il metodo getClasses e verifica che il risultato sia null o una
        // stringa vuota
        String result = (String) T1Service.handleRequest("getClasses");

        // Assicura che il risultato sia null o vuoto come previsto
        assertTrue(result == null || result.isEmpty(), "Expected result to be null or empty for empty response");

        // Verifica che tutte le aspettative del mock server siano state soddisfatte
        mockServer.verify();
    }

    /*
     * Test18: testGetClassesWithBadRequest
     * Precondizioni: Il server mock è impostato per restituire un errore HTTP 400
     * (Bad Request) per una richiesta GET.
     * Azioni: Invocare il metodo getClasses per recuperare l'elenco delle classi.
     * Post-condizioni: Verificare che il servizio gestisca l'errore, lanciando
     * un'eccezione o restituendo un messaggio specifico.
     */
    @Test
    public void testGetClassesWithBadRequest() {
        String endpoint = Base_URL + "/home";

        // Configurazione del mock server per rispondere con un errore HTTP 400 (Bad
        // Request)
        mockServer.expect(once(), requestTo(endpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withBadRequest());

        // Variabile per catturare eccezioni o messaggi di errore
        String errorMessage = null;
        try {
            // Invoca il metodo getClasses e cattura il risultato o eventuali eccezioni
            T1Service.handleRequest("getClasses");
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
     * Test19: testGetClassesWithInternalServerError
     * Precondizioni: Il server mock è impostato per restituire un errore HTTP 500
     * (Internal Server Error) per una richiesta GET.
     * Azioni: Invocare il metodo getClasses per recuperare l'elenco delle classi.
     * Post-condizioni: Verificare che il servizio gestisca l'errore, lanciando
     * un'eccezione o restituendo un messaggio specifico.
     */
    @Test
    public void testGetClassesWithInternalServerError() {
        String endpoint = Base_URL + "/home";
        mockServer.expect(once(), requestTo(endpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        String errorMessage = null;
        try {
            T1Service.handleRequest("getClasses");
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }

        assertNotNull(errorMessage, "Expected an error message for HTTP 500 response");
        assertTrue(errorMessage.contains("Internal Server Error") || errorMessage.contains("500"),
                "Error message should indicate an Internal Server Error (500)");

        mockServer.verify();
    }

    /*
     * Test20: testGetClassUnderTestWithNullNomeCUT
     * Precondizioni: Il metodo handleRequest è chiamato con un valore nullo per
     * nomeCUT, il che dovrebbe attivare la logica di gestione degli errori.
     * Azioni: Invocare il metodo handleRequest passando Base_URL e null
     * come parametri.
     * Post-condizioni: Verificare che venga sollevata un'eccezione di tipo
     * IllegalArgumentException e che il messaggio dell'eccezione contenga
     * l'informazione relativa al fatto che nomeCUT non può essere nullo.
     */

    @Test
    public void testGetClassUnderTestWithNullNomeCUT() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            T1Service.handleRequest(Base_URL, null);
        });

        assertEquals("[HANDLEREQUEST] Azione non riconosciuta: http://manvsclass-controller-1:8080",
                exception.getMessage());
    }

}
