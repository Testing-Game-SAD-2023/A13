package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Opponent;
import com.groom.manvsclass.model.dto.OpponentSummaryDTO;
import com.groom.manvsclass.service.OpponentService;
import com.groom.manvsclass.util.filesystem.upload.FileUploadResponse;
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

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> getNomiClassiUT(@CookieValue(name = "jwt", required = false) String jwt) {
        return opponentService.getNomiClassiUT(jwt);
    }


    @PostMapping("/update/{name}")
    public ResponseEntity<String> modificaClasse(@PathVariable String name, @RequestBody ClassUT newContent, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
        return opponentService.modificaClasse(name, newContent, jwt, request);
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
    public ResponseEntity<FileUploadResponse> uploadClassAndOpponents(
            @RequestParam("classUTFile") MultipartFile classUTFile,
            @RequestParam("classUTDetails") String classUTDetails,
            @RequestParam("robotTestsZip") MultipartFile robotTestsZip
    ) throws IOException {
        return opponentService.uploadOpponent(classUTFile, classUTDetails, robotTestsZip);
    }


    @GetMapping("/downloadFile/{name}")
    public ResponseEntity<?> downloadClasse(@PathVariable("name") String name) {
        try {
            return opponentService.downloadClasse(name);
        } catch (Exception e) {
            // Gestisci l'eccezione e ritorna una risposta appropriata
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel download della classe: " + e.getMessage());
        }
    }

    @DeleteMapping("/{classUT}")
    public ResponseEntity<?> deleteClassUT(@PathVariable("classUT") String classUT) {
        return opponentService.eliminaClasse(classUT);
    }
}
