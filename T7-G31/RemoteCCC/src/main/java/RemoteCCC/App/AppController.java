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

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class AppController {

    protected static final Logger logger = LoggerFactory.getLogger(CompilationService.class);

    @Value("${variabile.mvn}")
    private String mvn_path;

    @Autowired
    public AppController() {
    }

    /**
     * REST endpoint for handling POST requests with JSON body containing two
     * Java files.
     *
     * @param request JSON request con i due file.
     * @return A JSON response con il risultato della console e il file di
     * coverage
     * @throws IOException
     * @throws InterruptedException
     */
    @PostMapping(value = "/compile-and-codecoverage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> compileAndTest(@RequestBody RequestDTO request) throws IOException, InterruptedException {
        try {
            CompilationService compilationService = new CompilationService(request.getTestingClassName(),
                        request.getTestingClassCode(),
                        request.getUnderTestClassName(),
                        request.getUnderTestClassCode(),
                        mvn_path);
            compilationService.compileAndTest();
            JSONObject result = new JSONObject();
            result.put("outCompile", compilationService.outputMaven);
            result.put("coverage", compilationService.Coverage);
            result.put("error", compilationService.Errors);
            return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(result.toString()); // Imposta l'intestazione Content-Type
        } catch (IOException | InterruptedException | JSONException e) {
            logger.error("[Compile-and-codecoverage]", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        }
    }

    /**
     * Endpoint per i test generati dai robot: gestisce file multipli e produce un report di copertura.
     *
     * @param testFiles     Array di file per i test generati.
     * @param classUnderTest Classe sotto test.
     * @param robotType      Tipo di robot che ha generato i test.
     * @return JSON con il percorso del report generato.
     * @throws IOException
     * @throws InterruptedException
     */
    @PostMapping(value = "/compile-and-analyze", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> compileAndAnalyze(@RequestBody RobotRequestDTO request) throws IOException, InterruptedException {

        try {
            CompilationService compilationService = new CompilationService(
                request.getTestFilesDir(), // Directory dei test generati
                request.getUnderTestClassName(), // Nome della classe sotto test
                request.getUnderTestClassCode(), // Codice sorgente della classe sotto test
                mvn_path // Path di Maven
            );

            compilationService.handleRequest(request.getRobotType());
            String coverageReport = compilationService.Coverage;

            // Prepara il risultato come JSON
            JSONObject result = new JSONObject();
            result.put("coverage", coverageReport); // Contenuto del file di copertura (jacoco.xml)
            result.put("error", compilationService.Errors);

            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(result.toString());

        } catch (IOException | InterruptedException | JSONException e) {
            // Gestione degli errori con log
            logger.error("[Compile-and-analyze] Errore nella richiesta.", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        }
}


    // Data Transfer Object
    /*
     *  gestisco il request body come una classe per semplicità 
     */
    protected static class RequestDTO {

        private String testingClassName;
        private String testingClassCode;
        private String underTestClassName;
        private String underTestClassCode;

        // Getters e setters...
        public String getTestingClassName() {
            return testingClassName;
        }

        public void setTestingClassName(String testingClassName) {
            this.testingClassName = testingClassName;
        }

        public String getTestingClassCode() {
            return testingClassCode;
        }

        public void setTestingClassCode(String testingClassCode) {
            this.testingClassCode = testingClassCode;
        }

        public String getUnderTestClassName() {
            return underTestClassName;
        }

        public void setUnderTestClassName(String underTestClassName) {
            this.underTestClassName = underTestClassName;
        }

        public String getUnderTestClassCode() {
            return underTestClassCode;
        }

        public void setUnderTestClassCode(String underTestClassCode) {
            this.underTestClassCode = underTestClassCode;
        }
    }

    
    // Data Transfer Object
    /*
     *  gestisco il request body come una classe per semplicità 
     */
    protected static class RobotRequestDTO {

        private String testFilesDir; // Path delle classi di test generate dai robot
        private String underTestClassName; // Nome della classe sotto test
        private String underTestClassCode; // Codice sorgente della classe sotto test
        private String robotType; // Tipo di robot
    
        // Getters e setters
        public String getTestFilesDir() {
            return testFilesDir;
        }
    
        public void setTestFilesDir(String testFilesDir) {
            this.testFilesDir = testFilesDir;
        }
    
        public String getUnderTestClassName() {
            return underTestClassName;
        }
    
        public void setUnderTestClassName(String underTestClassName) {
            this.underTestClassName = underTestClassName;
        }
    
        public String getUnderTestClassCode() {
            return underTestClassCode;
        }
    
        public void setUnderTestClassCode(String underTestClassCode) {
            this.underTestClassCode = underTestClassCode;
        }
    
        public String getRobotType() {
            return robotType;
        }
    
        public void setRobotType(String robotType) {
            this.robotType = robotType;
        }
    }

}
