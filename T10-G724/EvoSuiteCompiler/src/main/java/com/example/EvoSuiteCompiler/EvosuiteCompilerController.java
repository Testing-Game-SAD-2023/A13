package com.example.EvoSuiteCompiler;

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
public class EvosuiteCompilerController {

    protected static final Logger logger = LoggerFactory.getLogger(CompilationService.class);

    @Value("${variabile.mvn}")
    private String mvn_path;

    @Autowired
    public EvosuiteCompilerController() {
    }

    /**
     * Endpoint per i test EvoSuite generati dai robot: gestisce file multipli e produce un report di copertura.
     *
     * @param testFilesDir   Path della directory contenente le classi di test generate dai robot
     * @param classUnderTest Classe sotto test.
     * @param robotType      Tipo di robot che ha generato i test.
     * @return               JSON con il percorso del report generato.
     * @throws IOException
     * @throws InterruptedException
     */
    @PostMapping(value = "/compile-evosuite", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> compileEvosuite(@RequestBody RobotRequestDTO request) throws IOException, InterruptedException {

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
            logger.error("[compile-evosuite] Errore nella richiesta.", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
