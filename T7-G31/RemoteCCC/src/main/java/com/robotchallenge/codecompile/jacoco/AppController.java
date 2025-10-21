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
import java.util.concurrent.*;

import com.robotchallenge.codecompile.jacoco.config.CustomExecutorConfiguration;
import com.robotchallenge.codecompile.jacoco.util.BuildResponse;
import lombok.Getter;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import testrobotchallenge.commons.models.dto.score.JacocoCoverageDTO;

@CrossOrigin
@RestController
public class AppController {

    protected static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Value("${variabile.mvn}")
    private String mvnPath;

    private final CustomExecutorConfiguration.CustomExecutorService compileExecutor;

    public AppController(CustomExecutorConfiguration.CustomExecutorService compileExecutor) {
        this.compileExecutor = compileExecutor;
    }

    /**
     * REST endpoint for handling POST requests with JSON body containing two
     * Java files.
     *
     * @param request JSON request con i due file.
     * @return A JSON response con il risultato della console e il file di
     * coverage
     */
    @PostMapping(value = "/coverage/player", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> compileAndTest(@RequestBody RequestDTO request) {
        Callable<JacocoCoverageDTO> compilationTimedTask = () -> {
            CompilationService compilationService = new CompilationService(
                    request.getTestingClassName(),
                    request.getTestingClassCode(),
                    request.getUnderTestClassName(),
                    request.getUnderTestClassCode(),
                    mvnPath
            );

            compilationService.compileAndTest();

            return BuildResponse.buildExtendedDTO(
                    compilationService.getCoverage(), compilationService.getOutputMaven(), compilationService.getErrors());
        };

        Future<JacocoCoverageDTO> future;
        try {
            future = compileExecutor.submitTask(compilationTimedTask);
        } catch (RejectedExecutionException e) {
            logger.warn("[CoverageController] Task rifiutato: sistema sovraccarico: {}", e.getStackTrace()[0]);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Il sistema è temporaneamente sovraccarico. Riprova più tardi.");
        }

        try {
            JacocoCoverageDTO result = future.get();
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("[CoverageController] Il processo è stato interrotto: {}", e.getStackTrace()[0]);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Il processo è stato interrotto.");
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof TimeoutException) {
                logger.warn("[CoverageController] Timeout: il task ha impiegato troppo tempo: {}", e.getStackTrace()[0]);
                return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                        .body("Il task ha superato il tempo massimo disponibile.");
            } else {
                logger.error("[CoverageController] Errore interno durante l'esecuzione: ", e);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Errore durante la compilazione o l'esecuzione.");
            }
        }
    }

    /**
     * REST endpoint for handling POST requests with JSON body containing two
     * Java files.
     *
     * @param projectCode JSON request con i due file.
     * @return A JSON response con il risultato della console e il file di
     * coverage
     * @throws IOException
     * @throws InterruptedException
     */
    @PostMapping(value = "/coverage/opponent", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> evoSuitRobotCoverage(@RequestParam(name = "project") MultipartFile projectCode) throws IOException, InterruptedException {
        try {
            logger.info("[POST /coverage/opponent] Ricevuta richiesta con project {}", projectCode.getOriginalFilename());

            CompilationService compilationService = new CompilationService(mvnPath);
            compilationService.compileAndTestEvoSuiteTests(projectCode);

            JacocoCoverageDTO responseBody = BuildResponse.buildExtendedDTO(
                    compilationService.getCoverage(), compilationService.getOutputMaven(), compilationService.getErrors());
            return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(responseBody); // Imposta l'intestazione Content-Type
        } catch (JSONException e) {
            logger.error("[Compile-and-codecoverage]", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        }
    }

    // Data Transfer Object
    /*
     *  gestisco il request body come una classe per semplicità 
     */
    @Getter
    protected static class RequestDTO {
        private String testingClassName;
        private String testingClassCode;
        private String underTestClassName;
        private String underTestClassCode;
    }

}
