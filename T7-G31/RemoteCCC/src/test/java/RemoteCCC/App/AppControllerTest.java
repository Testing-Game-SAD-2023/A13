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


package RemoteCCC.App;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
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

@ActiveProfiles("test")
@WebMvcTest(AppController.class)
public class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompilationService compilationService;

    @Autowired
    private AppController appController;

    private static final String BASE_PATH = ".\\src\\test\\resources\\classi_da_testare\\";

    

    private JSONObject loadTestFiles(String folderName, String testingClassName, String underTestClassName)
            throws Exception {
        // Carica i contenuti dei file e gestisci errori, restituendo un oggetto JSON
        // per la richiesta
        System.out.println("PATH: "+Paths.get(BASE_PATH, folderName, testingClassName + ".java").toString());
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
    *  METODI PRINCIPALI PER FARE TEST
    */
    private void DoTest(String foldername, String ClassName, Boolean Esito) throws Exception{
        String TestingClassName = "Test" + ClassName;
        JSONObject requestJson = loadTestFiles(foldername, TestingClassName, ClassName);
        if(Esito){
            Mock_correct_compile(requestJson);
        }else{
            Mock_error_compile(requestJson);
        }

    }

    // Metodo che esegue i test per ogni sottocartella
    public void runTests(String rootFolder, Boolean Esito) {
        // Ottieni la cartella principale
        String rootFolderT = Paths.get(BASE_PATH,rootFolder).toString();
        File folder = new File(rootFolderT);

        // Verifica che la cartella esista e sia effettivamente una directory
        if (folder.exists() && folder.isDirectory()) {
            // Ottieni la lista di tutte le sottocartelle
            File[] subfolders = folder.listFiles(File::isDirectory);

            // Cicla attraverso tutte le sottocartelle
            if (subfolders != null) {
                for (File subfolder : subfolders) {
                        // Nome della sottocartella (che corrisponde al nome della classe)
                        String className = subfolder.getName();

                        try {
                            // Chiamata alla funzione DoTest
                            String rootFolderClass =  Paths.get(rootFolder,className).toString();
                            DoTest(rootFolderClass, className, Esito);
                        } catch (Exception e) {
                            System.err.println("Errore durante il test della classe: " + className);
                            e.printStackTrace();
                        }
                    }
            }
        } else {
            System.out.println("La cartella " + rootFolder + " non esiste o non e' una directory.");
        }
    }

    /*
    *     test 0 - senza errori
    */
    @Test
    public void CaseTest_0() throws Exception {
        runTests("t0", true);
    }
    
    /*
    *     test 1 - manca costruttore nella classe
    */
    /*
    @Test
    public void CaseTest_1() throws Exception {
        runTests("t1", false);
    }
    */

    /*
    *     test 2 - tipo di ritorno errato
    */
    @Test
    public void CaseTest_2() throws Exception {
        runTests("t2", false);
    }

    /*
    *     test 3 - Verifica se una firma di metodo errata nella classe di test genera un errore di compilazione
    */
    @Test
    public void CaseTest_3() throws Exception {
        runTests("t3", false);
    }

    /*
    *     test 4 - Controlla se una classe di test vuota non genera un errore di compilazione
    */
    @Test
    public void CaseTest_4() throws Exception {
        runTests("t4", true);
    }

    /*
    *     test 5 - Testa se la mancanza di un metodo privato previsto genera un errore di compilazione
    */
    @Test
    public void CaseTest_5() throws Exception {
        runTests("t5", false);
    }

    /*
    *     test 6 - Verifica se un'importazione non valida causa un errore di compilazione
    */
    @Test
    public void CaseTest_6() throws Exception {
        runTests("t6", false);
    }


    /*
    *     test 7 - Controlla se l'assegnazione di un tipo di dato errato a una variabile genera un errore di compilazione
    */
    @Test
    public void CaseTest_7() throws Exception {
        runTests("t7", false);
    }

    /*
    *     test 8 - Verifica se un nome del file non corrispondente genera un errore di compilazione
    */
    
    @Test
    public void CaseTest_8() throws Exception {
        runTests("t8", false);
    }

    /*
    *     test 9 - Verifica se un errore di formattazione del codice genera un errore di compilazione
    */
    @Test
    public void CaseTest_9() throws Exception {
        runTests("t9", false);
    }

    /*
    *     test 10 - Controlla se un nome della classe di test non formattato correttamente genera un errore di compilazione
    */
    @Test
    public void CaseTest_10() throws Exception {
        runTests("t10", false);
    }
}