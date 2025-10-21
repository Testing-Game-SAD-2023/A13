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

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class CompilationServiceTest {

    //private CompilationService compilationService;
    private MockCompilationService mockCompilationService;
    private static final Logger logger = LoggerFactory.getLogger(CompilationServiceTest.class);
    //private AutoCloseable closeable;

    @Mock
    private Config mockConfig;

    @Value("${variabile.mvn}")
    private String mvn_path;

    private static final String BASE_PATH = ".\\src\\test\\resources\\classi_da_testare\\";

    private JSONObject loadTestFiles(String folderName, String testingClassName, String underTestClassName)
            throws Exception {
        // Carica i contenuti dei file e gestisci errori, restituendo un oggetto JSON
        // per la richiesta
        String folderNameT = Paths.get(folderName, testingClassName).toString();
        System.out.println("PATH: " + Paths.get(BASE_PATH, folderNameT, testingClassName + ".java").toString());
        String testingClassCode = readFileContent(Paths.get(BASE_PATH, folderNameT, testingClassName + ".java"));
        String underTestClassCode = readFileContent(Paths.get(BASE_PATH, folderNameT, underTestClassName + ".java"));
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

    @BeforeEach
    public void setUp() {
        try {
            JSONObject requestJson = loadTestFiles("t11", "SubjectParser", "TestSubjectParser");
            // Crea un'istanza di CompilationService con il Config simulato
            mockCompilationService = new MockCompilationService(requestJson.getString("testingClassName"),
                                                                requestJson.getString("testingClassCode"),
                                                                requestJson.getString("underTestClassName"),
                                                                requestJson.getString("underTestClassCode"),
                                                                mvn_path);
        } catch (IOException e) {
            logger.error("[IOException] [ERROR] ", e);
        } catch (Exception e) {
            logger.error("[Exception] [ERROR] ", e);
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        mockCompilationService.deleteCartelleTest();
    }


    /*
    *     test 11  - cartella con lo stesso nome
     */
    @Test
    public void CaseTest_11() throws IOException {
        CompilationService compilationServiceSpy = spy(mockCompilationService);
        //eccezione quando la directory esiste già
        IOException exception = assertThrows(IOException.class, () -> {
            compilationServiceSpy.createDirectoriesAndCopyPom();
        });
        assertTrue(exception.getMessage().contains("[createDirectoryIfNotExists] Errore esiste già la directory:"));
    }
}
