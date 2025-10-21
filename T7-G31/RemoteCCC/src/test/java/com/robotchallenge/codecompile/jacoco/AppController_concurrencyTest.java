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
package com.robotchallenge.codecompile.jacoco;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ActiveProfiles("test")
@WebMvcTest(AppController.class)
public class AppController_concurrencyTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompilationService compilationService;

    @Autowired
    private AppController appController;

    private static final String BASE_PATH = ".\\src\\test\\resources\\classi_da_testare\\";

    final static int numberOfThreads = 10;
    static ExecutorService executor;
    static List<Exception> exceptions;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(appController).build();
        executor = Executors.newFixedThreadPool(numberOfThreads);
        exceptions = Collections.synchronizedList(new ArrayList<>());
    }

    private JSONObject loadTestFiles(String folderName, String testingClassName, String underTestClassName)
            throws Exception {
        // Carica i contenuti dei file e gestisci errori, restituendo un oggetto JSON
        // per la richiesta
        System.out.println("PATH: " + Paths.get(BASE_PATH, folderName, testingClassName + ".java").toString());
        String testingClassCode = readFileContent(Paths.get(BASE_PATH, folderName, testingClassName + ".java"));
        String underTestClassCode = readFileContent(Paths.get(BASE_PATH, folderName, underTestClassName + ".java"));
        JSONObject requestJson = new JSONObject();
        requestJson.put("testingClassName", testingClassName + ".java");
        requestJson.put("testingClassCode", testingClassCode);
        requestJson.put("underTestClassName", underTestClassName + ".java");
        requestJson.put("underTestClassCode", underTestClassCode);
        // Stampa il timestamp corrente
        long timestamp = System.currentTimeMillis();
        System.out.println("Timestamp: " + timestamp + " - Thread: " + Thread.currentThread().getName());

        return requestJson;
    }

    private String readFileContent(Path path) throws IOException {
        // Lettura del file in una stringa, lancia un'eccezione in caso di errore
        if (!Files.exists(path)) {
            throw new IOException("Il file non esiste: " + path);
        }
        return Files.readString(path);
    }

    private ResultActions Mock_error_compile(JSONObject requestJson) throws Exception {
        // Metodo per eseguire la richiesta di compilazione e testing
        return mockMvc.perform(post("/compile-and-codecoverage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.outCompile").isNotEmpty()) // Verifica che outCompile sia una stringa non nulla
                .andExpect(jsonPath("$.coverage").doesNotExist()) // Verifica che coverage sia una stringa nulla
                .andExpect(jsonPath("$.error").value(true)); // Verifica che error sia true
    }

    private ResultActions Mock_correct_compile(JSONObject requestJson) throws Exception {
        // Metodo per eseguire la richiesta di compilazione e testing
        return mockMvc.perform(post("/compile-and-codecoverage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.outCompile").isNotEmpty()) // Verifica che outCompile sia una stringa non nulla
                .andExpect(jsonPath("$.coverage").isNotEmpty()) // Verifica che coverage sia una stringa nulla
                .andExpect(jsonPath("$.error").value(false)); // Verifica che error sia false
    }

    /*
     *  Mando in esecuzione 10 thread nello stesso istante, 
     *  tutti fanno lo stesso tipo di richiesta da cui mi aspetto esito positivo 
     */
    @Test
    public void testConcurrent_validRequests() throws Exception {
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    JSONObject requestJson = loadTestFiles("t0", "TestTimeStamp", "TimeStamp");
                    Mock_correct_compile(requestJson);
                } catch (Exception e) {
                    exceptions.add(e);
                } finally {
                    doneLatch.countDown();
                }
            }
            );
        }
        startLatch.countDown(); // Release all threads
        doneLatch.await(); // Wait for all threads to complete
        executor.shutdown(); // Shut down the executor
        assertTrue(exceptions.isEmpty());
    }

    /*
    *  Mando in esecuzione 10 thread nello stesso istante, 
    *  tutti fanno lo stesso tipo di richiesta da cui mi aspetto esito negativo 
     */
    @Test
    public void testConcurrent_invalidRequests() throws Exception {
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);
        // Crea e avvia i thread
        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await(); // Attendi che tutti i thread siano pronti
                    // Costruisci il JSON da inviare nella richiesta
                    JSONObject requestJson = loadTestFiles("t1", "TestFontinfo", "Fontinfo");
                    // Esegui la richiesta POST
                    Mock_error_compile(requestJson);
                } catch (Exception e) {
                    exceptions.add(e);
                } finally {
                    doneLatch.countDown(); // Segnala che questo thread ha finito
                }
            });
        }
        startLatch.countDown(); // Rilascia tutti i thread
        doneLatch.await(); // Aspetta che tutti i thread completino
        executor.shutdown(); // Chiudi l'executor
        assertTrue(exceptions.isEmpty());
    }

    /*
    *  Mando in esecuzione 10 thread nello stesso istante, 
    *  metà fanno richieste di tipo 1 con  esito positivo 
    *  l'altra metà fanno richieste di tipo 2 con esito negativo
     */
    @Test
    public void testConcurrentMixedRequests() throws Exception {
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);
        // Crea e avvia i thread
        for (int i = 0; i < numberOfThreads; i++) {
            final boolean isValidRequest = (i < 2); // I primi 10 thread faranno richieste valide
            executor.submit(() -> {
                try {
                    startLatch.await(); // Attendi che tutti i thread siano pronti
                    JSONObject requestJson;
                    if (isValidRequest) {
                        // Costruisci il JSON per la richiesta valida
                        requestJson = loadTestFiles("t0", "TestTimeStamp", "TimeStamp");
                        // Esegui la richiesta POST e verifica l'esito positivo
                        Mock_correct_compile(requestJson);
                    } else {
                        // Costruisci il JSON per la richiesta non valida
                        requestJson = loadTestFiles("t1", "TestFontinfo", "Fontinfo");
                        // Esegui la richiesta POST e verifica l'esito negativo
                        Mock_error_compile(requestJson);
                    }
                } catch (Exception e) {
                    exceptions.add(e);
                } finally {
                    doneLatch.countDown(); // Segnala che questo thread ha finito
                }
            });
        }
        startLatch.countDown(); // Rilascia tutti i thread
        doneLatch.await(); // Aspetta che tutti i thread completino
        executor.shutdown(); // Chiudi l'executor
        assertTrue(exceptions.isEmpty());
    }

    /*
     *  10 richieste con esito positivo ma ho una sleep che forza un timeout
     */
    @Test
    public void testTimeoutHandling() throws Exception {
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    // Simula un timeout
                    Thread.sleep(10000); // Ad esempio, dormire per 10 secondi
                    JSONObject requestJson = loadTestFiles("t0", "TestTimeStamp", "TimeStamp");
                    Mock_correct_compile(requestJson);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await();
        executor.shutdown();
        // Verifica che l'applicazione gestisca i timeout come previsto
        assertTrue(exceptions.isEmpty());
    }
}
