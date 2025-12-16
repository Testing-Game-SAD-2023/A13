package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Opponent;
import com.groom.manvsclass.dto.OpponentSummaryDTO;
import com.groom.manvsclass.service.ClassUTService;
import com.groom.manvsclass.service.OpponentService;
import com.groom.manvsclass.service.JwtService;
import com.groom.manvsclass.service.AdminService;
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
import org.springframework.beans.factory.annotation.Autowired;

import com.groom.manvsclass.security.JwtRequestContext;
import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.exception.ForbiddenException;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class OpponentController {

    private static final Logger logger = LoggerFactory.getLogger(OpponentController.class);

    @Autowired
    private JwtService jwtService;
    @Autowired
    private OpponentService opponentService;
    @Autowired
    private ClassUTService classUTService;
    @Autowired
    private AdminService adminService;

    @GetMapping("/opponents/elencoNomiClassiUT")
    public ResponseEntity<?> getNomiClassiUT() {

        return ResponseEntity.ok(classUTService.getClassUTNames());
    }

    @PostMapping("/opponents/update/{name}")
    public ResponseEntity<String> modificaClasse(@PathVariable String name, @RequestBody ClassUT newContent, HttpServletRequest request) {

        String jwt = JwtRequestContext.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        return opponentService.modificaClasse(name, newContent, adminEmail, request);
    }

    @GetMapping("/opponents")
    public ResponseEntity<List<Opponent>> getAllOpponents() {

        return ResponseEntity.ok(opponentService.getAllOpponents());
    }

    @GetMapping("/opponents/classes/summary")
    public ResponseEntity<List<String>> getAllClassesAsSummary() {

        logger.info("[GET /classes/summary] Request received");
        List<ClassUT> classes = classUTService.getClassUTs();
        logger.info("[GET /classes/summary] Classes found: {}", classes);
        List<String> classesAsSummary = new ArrayList<>();
        for (ClassUT c : classes) {
            classesAsSummary.add(c.getName());
        }

        return ResponseEntity.ok(classesAsSummary);
    }

    @GetMapping("/opponents/summary")
    public ResponseEntity<List<OpponentSummaryDTO>> getAllOpponentsAsSummary() {

        logger.info("[GET /summary] Request received");
        List<Opponent> opponents = opponentService.getAllOpponents();
        logger.info("[GET /summary] Opponents found: {}", opponents);
        List<OpponentSummaryDTO> response = new ArrayList<>();
        for (Opponent opponent : opponents) {
            response.add(new OpponentSummaryDTO(opponent.getClassUT().getName(),
                    opponent.getType(), opponent.getClassUT().getDifficulty()));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/opponents/{classUT}/{opponentType}/{opponentDifficulty}/score")
    public ResponseEntity<Opponent> getOpponentData(@PathVariable("classUT") String classUT,
                                                    @PathVariable("opponentType") String type,
                                                    @PathVariable("opponentDifficulty") OpponentDifficulty difficulty) {
        return ResponseEntity.ok(opponentService.getOpponentData(classUT, type, difficulty));
    }

    @GetMapping("/opponents/{classUT}/{opponentType}/{opponentDifficulty}/score/evosuite")
    public ResponseEntity<EvosuiteScoreDTO> getOpponentEvosuiteScore(@PathVariable("classUT") String classUT,
                                                                     @PathVariable("opponentType") String type,
                                                                     @PathVariable("opponentDifficulty") OpponentDifficulty difficulty) {
        EvosuiteScore score = opponentService.getOpponentEvosuiteScore(classUT, type, difficulty);

        return ResponseEntity.ok(EvosuiteScoreMapper.toEvosuiteScoreDTO(score));
    }

    @GetMapping("/opponents/{classUT}/{opponentType}/{opponentDifficulty}/score/jacoco")
    public ResponseEntity<JacocoScoreDTO> getOpponentJacocoScore(@PathVariable("classUT") String classUT,
                                                                 @PathVariable("opponentType") String type,
                                                                 @PathVariable("opponentDifficulty") OpponentDifficulty difficulty) {
        JacocoScore score = opponentService.getOpponentJacocoScore(classUT, type, difficulty);
        return ResponseEntity.ok(JacocoScoreMapper.toJacocoScoreDTO(score));
    }

    @GetMapping("/opponents/{classUT}/{opponentType}/{opponentDifficulty}/coverage")
    public ResponseEntity<String> getOpponentCoverage(@PathVariable("classUT") String classUT,
                                                      @PathVariable("opponentType") String type,
                                                      @PathVariable("opponentDifficulty") OpponentDifficulty difficulty) {
        return ResponseEntity.ok(opponentService.getOpponentCoverage(classUT, type, difficulty));
    }


    @PostMapping("/opponents")
    public ResponseEntity<?> uploadClassAndOpponents(
            @RequestParam("classUTFile") MultipartFile classUTFile,
            @RequestParam("classUTDetails") String classUTDetails,
            @RequestParam("robotTestsZip") MultipartFile robotTestsZip
    ) throws IOException {

        String jwt = JwtRequestContext.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        return opponentService.uploadOpponent(classUTFile, classUTDetails, robotTestsZip, adminEmail);
    }


    @GetMapping("/opponents/download/{className}")
    public ResponseEntity<?> downloadClasse(@PathVariable String className) {

        return opponentService.downloadClasse(className);
    }

    @DeleteMapping("/opponents/{classUT}")
    public ResponseEntity<?> deleteClassUT(@PathVariable("classUT") String classUT) {

        String jwt = JwtRequestContext.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        opponentService.eliminaClasse(classUT, adminEmail);
        return ResponseEntity.ok("Classe eliminata con successo.");
    }

}

