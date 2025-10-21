package com.robotchallenge.t8.controller;

import com.robotchallenge.t8.config.CustomExecutorConfiguration;
import com.robotchallenge.t8.dto.request.OpponentCoverageRequestDTO;
import com.robotchallenge.t8.dto.request.StudentCoverageRequestDTO;
import com.robotchallenge.t8.service.CoverageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import testrobotchallenge.commons.models.dto.api.ApiErrorBackend;
import testrobotchallenge.commons.models.dto.score.EvosuiteCoverageDTO;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
@CrossOrigin
public class CoverageController {

    private static final Logger logger = LoggerFactory.getLogger(CoverageController.class);
    private final CustomExecutorConfiguration.CustomExecutorService compileExecutor;
    private final CoverageService coverageService;

    public CoverageController(CustomExecutorConfiguration.CustomExecutorService compileExecutor,
                              CoverageService coverageService) {
        this.compileExecutor = compileExecutor;
        this.coverageService = coverageService;
    }

    /*
     * Le eccezioni lanciate da CoverageService sono gestite da advice.TaskExceptionHandler come ApiErrorBackend
     */

    @Operation(
            summary = "Calculate EvoSuite coverage for an opponent",
            description = "Uploads opponent coverage request data and a project file (ZIP/JAR) to calculate EvoSuite coverage."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Coverage successfully calculated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EvosuiteCoverageDTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Compilation or execution error (invalid test or source code)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error (I/O or unexpected interruption)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class))),
            @ApiResponse(
                    responseCode = "504",
                    description = "Task exceeded maximum execution time (timeout)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class)))
    })
    @PostMapping(
            value = "/coverage/opponent",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<EvosuiteCoverageDTO> calculateRobotEvosuiteCoverage(
            @Parameter(
                    description = "Opponent coverage request payload (JSON)",
                    required = true
            )
            @RequestPart("request") OpponentCoverageRequestDTO request,
            @Parameter(
                    description = "Maven Project file ZIP to analyze",
                    required = true,
                    content = @Content(mediaType = "application/zip",
                            schema = @Schema(type = "binary"))
            )
            @RequestPart("project") MultipartFile project
    ) {
        logger.info("[CoverageController] [POST /score/opponent] Ricevuta richiesta con body {} e MultiPartFile {}", request, project.getOriginalFilename());
        EvosuiteCoverageDTO responseBody = coverageService.calculateRobotCoverage(request, project);

        logger.info("[CoverageController] [POST /score/opponent] Risultato: {}", responseBody);
        logger.info("[CoverageController] [POST /score/opponent] OK 200");
        return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(responseBody);
    }


    @Operation(
            summary = "Calculate EvoSuite coverage for a player",
            description = "Receives a JSON request with player coverage data and returns the calculated EvoSuite coverage metrics."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Coverage successfully calculated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EvosuiteCoverageDTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Compilation or execution error (invalid test or source code)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class))),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests in queue, the system is temporarily overloaded",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error (I/O or unexpected interruption)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class))),
            @ApiResponse(
                    responseCode = "504",
                    description = "Task exceeded maximum execution time (timeout)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "JSON payload containing the player coverage request data",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StudentCoverageRequestDTO.class)
            )
    )
    @PostMapping(value = "/coverage/player", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EvosuiteCoverageDTO> calculateStudentEvosuiteCoverage(
            @RequestBody StudentCoverageRequestDTO request)
            throws InterruptedException, ExecutionException {
        logger.info("[CoverageController] [POST /coverage/player] Ricevuta richiesta");

        Callable<EvosuiteCoverageDTO> compilationTimedTask = () -> coverageService.calculatePlayerCoverage(request);

        Future<EvosuiteCoverageDTO> future = compileExecutor.submitTask(compilationTimedTask); // Se la coda è piena, viene lanciata una RejectedExecutionException

        EvosuiteCoverageDTO responseBody = future.get(); // può lanciare InterruptedException, TimeoutException o altre eccezioni generiche

        logger.info("[CoverageController] [POST /coverage/player] Risultato: {}", responseBody);
        logger.info("[CoverageController] [POST /coverage/player] OK 200");
        return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(responseBody);

    }

}
