package com.groom.manvsclass.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.groom.manvsclass.api.ApiGatewayClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.dto.ClassUTDTO;
import com.groom.manvsclass.model.Category;
import com.groom.manvsclass.model.Operation;
import com.groom.manvsclass.model.OperationType;
import com.groom.manvsclass.model.Opponent;
import com.groom.manvsclass.service.ImageService;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import com.groom.manvsclass.repository.AdminRepository;
import com.groom.manvsclass.repository.ClassUTRepository;
import com.groom.manvsclass.repository.OperationRepository;
import com.groom.manvsclass.repository.OpponentRepository;

import com.groom.manvsclass.exception.CoverageNotFoundException;
import com.groom.manvsclass.exception.OpponentNotFoundException;
import com.groom.manvsclass.exception.ScoreNotFoundException;

import com.groom.manvsclass.util.filesystem.FileOperationUtil;
import com.groom.manvsclass.util.filesystem.download.FileDownloadUtil;
import com.groom.manvsclass.util.filesystem.upload.FileUploadResponse;
import com.groom.manvsclass.util.filesystem.upload.FileUploadUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;

import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.exception.ForbiddenException;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OpponentService {

    private static final Logger logger = LoggerFactory.getLogger(OpponentService.class);

    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private ClassUTRepository classUTRepository;
    @Autowired
    private UploadOpponentService uploadOpponentService;
    @Autowired
    private OpponentRepository opponentRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ApiGatewayClient apiGatewayClient;
    @Autowired
    private ImageService imageService;



    @Transactional
    public ResponseEntity<FileUploadResponse> uploadOpponent(
            MultipartFile classUTFile,
            String classUTDetails,
            MultipartFile robotTestsZip,
            String adminEmail) throws IOException {

        FileUploadResponse response = new FileUploadResponse();

        if (classUTFile == null || classUTFile.isEmpty()) {
            response.setErrorMessage("Errore: file della classe non ricevuto o vuoto.");
            return ResponseEntity.badRequest().body(response);
        }

        if (classUTDetails == null || classUTDetails.isBlank()) {
            response.setErrorMessage("Errore: dettagli della classe mancanti.");
            return ResponseEntity.badRequest().body(response);
        }

        ClassUTDTO classDTO;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            classDTO = mapper.readValue(classUTDetails, ClassUTDTO.class);
        } catch (Exception e) {
            response.setErrorMessage("Errore nel parsing dei dettagli della classe: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }


        ClassUT classUT = new ClassUT();
        classUT.setName(classDTO.getName());
        classUT.setDifficulty(
                OpponentDifficulty.valueOf(classDTO.getDifficulty().toUpperCase())
        );
        classUT.setDescription(classDTO.getDescription());
        classUT.setDate(classDTO.getDate());

        List<Category> categoryEntities = new ArrayList<>();

        if (classDTO.getCategory() != null) {
            for (String catName : classDTO.getCategory()) {
                if (catName != null && !catName.trim().isEmpty()) {
                    Category c = new Category();
                    c.setName(catName.trim());
                    categoryEntities.add(c);
                }
            }
        }

        classUT.setCategories(categoryEntities);

        String classUTFileName = StringUtils.cleanPath(
                Objects.requireNonNull(classUTFile.getOriginalFilename())
        );

        classUT.setUri(String.format("%s/%s/%s/%s",
                UploadOpponentService.VOLUME_T0_BASE_PATH,
                UploadOpponentService.UNMODIFIED_SRC,
                classUT.getName(),
                classUTFileName));

        classUTRepository.save(classUT);

        try {
            uploadOpponentService.saveOpponentsFromZip(
                    classUTFileName,
                    classUT.getName(),
                    classUTFile,
                    robotTestsZip
            );
        } catch (Exception e) {
            response.setErrorMessage("Errore durante l'elaborazione degli Opponents: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }


        try {
            FileUploadUtil.saveCLassFile(classUTFileName, classUT.getName(), classUTFile);
        } catch (Exception e) {
            response.setErrorMessage("Errore nel salvataggio del file della classe: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }

        response.setFileName(classUTFileName);
        response.setSize(classUTFile.getSize());
        response.setDownloadUri("/downloadFile/" + classUT.getName());

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<?> downloadClasse(String className) {

        logger.info("Request /downloadFile/{{}}", className);
        logger.debug("Inizio elaborazione downloadClasse per nome: {}", className);

        Optional<ClassUT> classUTOpt = classUTRepository.findById(className);
        if (!classUTOpt.isPresent()) {
            logger.warn("Nessuna classe con nome {} trovata", className);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nessuna classe con nome " + className + " trovata");
        }

        ClassUT classUT = classUTOpt.get();
        logger.debug("Classe trovata: {}", classUT);
        logger.debug("URI del file da scaricare: {}", classUT.getUri());

        try {
            logger.debug("Chiamata a FileDownloadUtil.downloadClassFile con URI: {}", classUT.getUri());
            ResponseEntity<?> fileResponse = FileDownloadUtil.downloadClassFile(classUT.getUri());
            logger.info("Download completato con successo per il file: {}", classUT.getUri());
            return fileResponse;
        } catch (Exception e) {
            logger.error("Errore durante il download del file: {}", classUT.getUri(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Impossibile scaricare la classe " + className);
        }
    }



    @Transactional
    public ResponseEntity<String> modificaClasse(String className, ClassUT newContent, String adminEmail, HttpServletRequest request) {

        Optional<ClassUT> classUTOpt = classUTRepository.findById(className);
        if (!classUTOpt.isPresent()) {
            return new ResponseEntity<>("Nessuna classe con nome " + className + " trovata", HttpStatus.NOT_FOUND);
        }

        Optional<Admin> adminOpt = adminRepository.findById(adminEmail);
        if (adminOpt.isEmpty()) {
            return new ResponseEntity<>("Admin non trovato", HttpStatus.NOT_FOUND);
        }

        ClassUT classUT = classUTOpt.get();
        classUT.setDate(newContent.getDate());
        classUT.setDifficulty(newContent.getDifficulty());
        classUT.setDescription(newContent.getDescription());
        classUT.setCategories(newContent.getCategories());

        classUTRepository.save(classUT);

        Operation updateOperation = new Operation();
        updateOperation.setAdmin(adminOpt.get());
        updateOperation.setClassUT(classUT);
        updateOperation.setType(OperationType.UPDATE);
        updateOperation.setDate(LocalDate.now());
        operationRepository.save(updateOperation);

        return new ResponseEntity<>("Aggiornamento eseguito correttamente.", HttpStatus.OK);
    }

        public void eliminaClasse(String className, String adminEmail) {

        Optional<ClassUT> classUTOpt = classUTRepository.findById(className);
        if(classUTOpt.isEmpty()) {
            throw new NotFoundException("Classe " + className + " non trovata");
        }

        ClassUT classToDelete = classUTOpt.get();

        Optional<Admin> adminOpt = adminRepository.findById(adminEmail);
        if(adminOpt.isEmpty()) {
            throw new NotFoundException("Admin " + adminEmail + " non trovato");
        }

        Operation deletionOperation = new Operation();
        deletionOperation.setAdmin(adminOpt.get());
        deletionOperation.setClassUT(classToDelete);
        deletionOperation.setType(OperationType.DELETE);
        deletionOperation.setDate(LocalDate.now());

        operationRepository.save(deletionOperation);

        // elimino gli opponent mantenuti da T23
        apiGatewayClient.callDeleteAllClassUTOpponents(className);

        // ELIMINAZIONI FILE IMG SUGGERIMENTI
        classToDelete.getSuggestions()
                .forEach(suggestion -> {
                    if(suggestion.getImage() != null) {
                        try {
                            imageService.deleteImage(suggestion.getImage());
                        } catch (IOException e) {
                            throw new RuntimeException("Errore nella cancellazione del file", e);
                        }
                    }
                });

        classUTRepository.delete(classToDelete);
        eliminaFile(className);

    }

    private void eliminaFile(String fileName) {
        File directory = new File(String.format("%s/%s", UploadOpponentService.VOLUME_T0_BASE_PATH, fileName));
        File directoryUnmodifiedSrc = new File(String.format("%s/%s/%s", UploadOpponentService.VOLUME_T0_BASE_PATH, UploadOpponentService.UNMODIFIED_SRC, fileName));

        System.out.println("name: " + fileName);
        if (directory.exists() && directory.isDirectory()) {
            try {
                FileOperationUtil.deleteDirectoryRecursively(directory.toPath());
                FileOperationUtil.deleteDirectoryRecursively(directoryUnmodifiedSrc.toPath());
                logger.info("Cartella eliminata con successo (/deleteFile/{fileName})");
            } catch (IOException e) {
                throw new RuntimeException("Impossibile eliminare la cartella.");
            }
        } else {
            throw new RuntimeException("Cartella non trovata.");
        }
    }


    public List<Opponent> getAllOpponents() {
        return opponentRepository.findAllOpponents();
    }

    public Opponent getOpponentData(String classUT, String opponentType, OpponentDifficulty opponentDifficulty) {
        Optional<Opponent> opponentOpt = opponentRepository.findOpponent(classUT, opponentType, opponentDifficulty);
        if (opponentOpt.isEmpty())
            throw new OpponentNotFoundException();

        return opponentOpt.get();
    }

    public EvosuiteScore getOpponentEvosuiteScore(String classUT, String opponentType, OpponentDifficulty opponentDifficulty) {
        Optional<EvosuiteScore> scoreOpt = opponentRepository.findEvosuiteScore(classUT,
                opponentType, opponentDifficulty);

        if (scoreOpt.isEmpty())
            throw new ScoreNotFoundException();

        return scoreOpt.get();
    }

    public JacocoScore getOpponentJacocoScore(String classUT, String opponentType, OpponentDifficulty opponentDifficulty) {
        Optional<JacocoScore> scoreOpt = opponentRepository.findJacocoScore(classUT,
                opponentType, opponentDifficulty);

        if (scoreOpt.isEmpty())
            throw new ScoreNotFoundException();

        return scoreOpt.get();
    }

    public String getOpponentCoverage(String classUT, String opponentType, OpponentDifficulty opponentDifficulty) {
        Optional<String> coverageOpt = opponentRepository.findCoverage(classUT,
                opponentType, opponentDifficulty);

        if(coverageOpt.isEmpty())
            throw new CoverageNotFoundException();

        return coverageOpt.get();
    }
}