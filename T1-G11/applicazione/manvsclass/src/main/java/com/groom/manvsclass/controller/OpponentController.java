package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Opponent;
import com.groom.manvsclass.model.dto.ErrorResponseDTO;
import com.groom.manvsclass.model.dto.OpponentSummaryDTO;
import com.groom.manvsclass.service.OpponentService;
import com.groom.manvsclass.util.upload.FileUploadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import testrobotchallenge.commons.mappers.EvosuiteScoreMapper;
import testrobotchallenge.commons.mappers.JacocoScoreMapper;
import testrobotchallenge.commons.models.dto.score.basic.EvosuiteScoreDTO;
import testrobotchallenge.commons.models.dto.score.basic.JacocoScoreDTO;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/opponents")
public class OpponentController {

    private static final Logger logger = LoggerFactory.getLogger(OpponentController.class);
    private final OpponentService opponentService;

    public OpponentController(OpponentService opponentService) {
        this.opponentService = opponentService;
    }

    @GetMapping("/elencoNomiClassiUT")
    public ResponseEntity<List<String>> getNomiClassiUT() {
        return opponentService.getNomiClassiUT();
    }


    @PostMapping("/update/{name}")
    public ResponseEntity<String> modificaClasse(@PathVariable String name, @RequestBody ClassUT newContent) {
        return opponentService.modificaClasse(name, newContent);
    }


    // OPPONENTS ENDPOINT
    @GetMapping("")
    public ResponseEntity<List<Opponent>> getAllOpponents() {
        return ResponseEntity.ok(opponentService.getAllOpponents());
    }

    @GetMapping("/classes/summary")
    public ResponseEntity<List<String>> getAllClassesAsSummary() {
        logger.info("[GET /classes/summary] Request received");
        List<ClassUT> classes = opponentService.getAllClassUTs();
        logger.info("[GET /classes/summary] Classes found: {}", classes);
        List<String> classesAsSummary = new ArrayList<>();
        for (ClassUT c : classes) {
            classesAsSummary.add(c.getName());
        }

        return ResponseEntity.ok(classesAsSummary);
    }

    @GetMapping("/summary")
    public ResponseEntity<List<OpponentSummaryDTO>> getAllOpponentsAsSummary() {
        logger.info("[GET /summary] Request received");
        List<Opponent> opponents = opponentService.getAllOpponents();
        logger.info("[GET /summary] Opponents found: {}", opponents);
        List<OpponentSummaryDTO> response = new ArrayList<>();
        for (Opponent opponent : opponents) {
            response.add(new OpponentSummaryDTO(opponent.getClassUT(),
                    opponent.getOpponentType(), opponent.getOpponentDifficulty()));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{classUT}/{opponentType}/{opponentDifficulty}/score")
    public ResponseEntity<Opponent> getOpponentData(@PathVariable("classUT") String classUT,
                                                    @PathVariable("opponentType") String type,
                                                    @PathVariable("opponentDifficulty") OpponentDifficulty difficulty) {
        return ResponseEntity.ok(opponentService.getOpponentData(classUT, type, difficulty));
    }

    @GetMapping("/{classUT}/{opponentType}/{opponentDifficulty}/score/evosuite")
    public ResponseEntity<EvosuiteScoreDTO> getOpponentEvosuiteScore(@PathVariable("classUT") String classUT,
                                                                     @PathVariable("opponentType") String type,
                                                                     @PathVariable("opponentDifficulty") OpponentDifficulty difficulty) {
        EvosuiteScore score = opponentService.getOpponentEvosuiteScore(classUT, type, difficulty);

        return ResponseEntity.ok(EvosuiteScoreMapper.toEvosuiteScoreDTO(score));
    }

    @GetMapping("/{classUT}/{opponentType}/{opponentDifficulty}/score/jacoco")
    public ResponseEntity<JacocoScoreDTO> getOpponentJacocoScore(@PathVariable("classUT") String classUT,
                                                                 @PathVariable("opponentType") String type,
                                                                 @PathVariable("opponentDifficulty") OpponentDifficulty difficulty) {
        JacocoScore score = opponentService.getOpponentJacocoScore(classUT, type, difficulty);
        return ResponseEntity.ok(JacocoScoreMapper.toJacocoScoreDTO(score));
    }

    @GetMapping("/{classUT}/{opponentType}/{opponentDifficulty}/coverage")
    public ResponseEntity<String> getOpponentCoverage(@PathVariable("classUT") String classUT,
                                                      @PathVariable("opponentType") String type,
                                                      @PathVariable("opponentDifficulty") OpponentDifficulty difficulty) {
        return ResponseEntity.ok(opponentService.getOpponentCoverage(classUT, type, difficulty));
    }


    @PostMapping("")
    @Operation(
            summary = "Upload class under test and robot tests",
            description = "Uploads a Java class file (ClassUT) along with its metadata and robot-generated test suites (EvoSuite/Randoop). " +
                    "The endpoint validates the class file structure, verifies class name consistency, processes robot test archives, " +
                    "and stores opponents with different difficulty levels. The robot tests zip must contain folders named 'EvoSuiteTest' " +
                    "and/or 'RandoopTest' with subdirectories for each difficulty level (e.g., 01Level, 02Level) containing test files and coverage reports."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully uploaded and processed class and robot tests. Returns metadata about the uploaded files including file names, URIs, and sizes.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FileUploadResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Validation errors occurred. Possible causes: " +
                            "empty or missing files, class name mismatch between form and source file, " +
                            "invalid Java syntax, missing package declaration, invalid ZIP structure, " +
                            "missing robot test directories (EvoSuiteTest/RandoopTest), or invalid test file format.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict - A class with the same name already exists in the system.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error - Unexpected error occurred during file processing, " +
                            "ZIP extraction, database operation, or robot test analysis.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    })
    public ResponseEntity<FileUploadResponse> uploadClassAndOpponents(
            @Parameter(
                    description = "Java source file (.java) containing the class under test. " +
                            "Must be a valid Java class with proper package declaration and syntax. " +
                            "The class name in the file must match the name provided in classUTDetails.",
                    required = true
            )
            @RequestParam("classUTFile") MultipartFile classUTFile,

            @Parameter(
                    description = "JSON string containing class metadata including: name, description, difficulty level, " +
                            "and visibility settings. Example: {\"name\":\"Calculator\",\"description\":\"A simple calculator class\",\"difficulty\":1,\"visible\":true}",
                    required = true
            )
            @RequestParam("classUTDetails") String classUTDetails,

            @Parameter(
                    description = "ZIP archive containing robot-generated test suites. Required structure: " +
                            "ZIP root must contain 'EvoSuiteTest' and/or 'RandoopTest' folders. " +
                            "Each folder should have level subdirectories (01Level, 02Level, etc.) with test files (.java) and coverage reports. " +
                            "Example structure: robotTests.zip/EvoSuiteTest/01Level/CalculatorTest.java",
                    required = true
            )
            @RequestParam("robotTestsZip") MultipartFile robotTestsZip
    ) throws IOException {
        return opponentService.uploadClassAndOpponents(classUTFile, classUTDetails, robotTestsZip);
    }


    @GetMapping("/downloadFile/{name}")
    public ResponseEntity<Object> downloadClasse(@PathVariable("name") String name) {
        // The service already handles exceptions and returns appropriate ResponseEntity
        // No need for try-catch here since we're not throwing to GlobalExceptionHandler
        return opponentService.downloadClasse(name);
    }

    @DeleteMapping("/{classUT}")
    public ResponseEntity<Object> deleteClassUT(@PathVariable("classUT") String classUT) {
        return opponentService.eliminaClasse(classUT);
    }
}
