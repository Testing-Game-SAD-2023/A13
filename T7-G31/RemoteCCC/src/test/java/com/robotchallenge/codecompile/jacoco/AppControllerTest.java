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
import java.util.stream.Stream;

import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(AppController.class)
public class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AppController appController;
    private static final String BASE_PATH = ".\\src\\test\\resources\\classi_da_testare\\";

    /*
     * Carica i contenuti dei file, restituendo un oggetto JSON per la richiesta
     */
    private JSONObject loadTestFiles(String folderName, String testingClassName, String underTestClassName)
            throws Exception {
        System.out.println("PATH: " + Paths.get(BASE_PATH, folderName, testingClassName + ".java").toString());
        String testingClassCode = readFileContent(Paths.get(BASE_PATH, folderName, testingClassName + ".java"));
        String underTestClassCode = readFileContent(Paths.get(BASE_PATH, folderName, underTestClassName + ".java"));
        JSONObject requestJson = new JSONObject();
        requestJson.put("testingClassName", testingClassName + ".java");
        requestJson.put("testingClassCode", testingClassCode);
        requestJson.put("underTestClassName", underTestClassName + ".java");
        requestJson.put("underTestClassCode", underTestClassCode);
        System.out.println("[TESTING] RIchiesta generata");
        return requestJson;
    }

    private String readFileContent(Path path) throws IOException {
        // Lettura del file in una stringa, lancia un'eccezione in caso di errore
        if (!Files.exists(path)) {
            throw new IOException("Il file non esiste: " + path);
        }
        return Files.readString(path);
    }

    private ResultActions Mock_perform(JSONObject requestJson, Boolean expectedError, Boolean expectCoverage)
            throws Exception {
        // Metodo per eseguire la richiesta di compilazione e testing
        return mockMvc.perform(post("/compile-and-codecoverage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.outCompile").isNotEmpty()) // Verifica che outCompile sia una stringa non nulla
                .andExpect(jsonPath("$.error").value(expectedError)) // Verifica che error sia true
                .andExpect(expectCoverage
                        ? jsonPath("$.coverage").isNotEmpty()
                        : jsonPath("$.coverage").doesNotExist());

    }

    /*
     * METODI PRINCIPALI PER FARE TEST
     */
    @ParameterizedTest
    @MethodSource("testParameters")
    public void DoTest(String foldername, String ClassName, Boolean expectedError, Boolean expectCoverage)
            throws Exception {
        String TestingClassName = "Test" + ClassName;
        JSONObject requestJson = loadTestFiles(foldername, TestingClassName, ClassName);
        Mock_perform(requestJson, expectedError, expectCoverage);
    }

    /*
     * test 0 - senza errori
     * test 1 - manca costruttore nella classe
     * test 2 - tipo di ritorno errato
     * test 3 - Verifica se una firma di metodo errata nella classe di test genera
     * un errore di compilazione
     * test 4 - Controlla se una classe di test vuota non genera un errore di
     * compilazione
     * test 5 - Testa se la mancanza di un metodo privato previsto genera un errore
     * di compilazione
     * test 6 - Verifica se un'importazione non valida causa un errore di
     * compilazione
     * test 7 - Controlla se l'assegnazione di un tipo di dato errato a una
     * variabile genera un errore di compilazione
     * test 8 - Verifica se un nome del file non corrispondente genera un errore di
     * compilazione
     * test 9 - Verifica se un errore di formattazione del codice genera un errore
     * di compilazione
     * test 10 - Controlla se un nome della classe di test non formattato
     * correttamente genera un errore di compilazione
     * template 0 - Classe test senza errori
     * template 1 - Manca costruttore nella classe di test
     * template 2 - Verifica se il test della classe di test è errato, prevedendo un
     * valore non nullo
     * nonostante l'introduzione di dati incompleti, come la rimozione dell'email,
     * che ne causa
     * il fallimento.
     * template 3 - Controlla se il test all'interno della classe di test è errato
     * confrontando una variabile con un
     * valore sbagliato, il che provoca il fallimento del test
     * template 4 - Verifica se il test all'interno della classe di test è errato
     * poiché restituisce null, mentre ci
     * aspettiamo il risultato opposto
     * template 5- Verifica se il nome del file della classe di test non
     * corrispondente generando
     * un errore di compilazione
     */
    /*
     * Metodo di supporto per fornire i parametri
     */
    static Stream<Arguments> testParameters() {
        return Stream.of(
                /*
                 * DIR NAME | ClassName | Expected Error | Expected Coverage
                 */
                Arguments.of("t0", "TimeStamp", false, true), // Caso senza errori, con coverage
                Arguments.of("t1", "Fontinfo", true, false), // Errore atteso, nessuna coverage
                Arguments.of("t2", "HSLColor", true, false), // Errore atteso, nessuna coverage
                Arguments.of("t3", "OutputFormat", true, false), // Altri casi...
                Arguments.of("t4", "SubjectParser", false, true),
                Arguments.of("t5", "TimeStamp", true, false),
                Arguments.of("t6", "TimeStamp", true, false),
                Arguments.of("t7", "FTPFile", true, false),
                Arguments.of("t8", "FontInf", true, false),
                Arguments.of("t9", "HSLColor", true, false),
                Arguments.of("t10", "OutputFormat", true, false),
                Arguments.of("template0", "VCardBean", false, true),
                Arguments.of("template1", "VCardBean", true, false),
                Arguments.of("template2", "VCardBean", true, false),
                Arguments.of("template3", "VCardBean", true, false),
                Arguments.of("template4", "VCardBean", true, false),
                Arguments.of("template5", "VCardBean", true, false));
    }

}
