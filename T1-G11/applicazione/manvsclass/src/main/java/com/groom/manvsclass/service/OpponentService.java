package com.groom.manvsclass.service;

import com.groom.manvsclass.model.entity.ClassUTEntity;
import com.groom.manvsclass.model.entity.OpponentEntity;
import com.groom.manvsclass.util.filesystem.upload.FileUploadResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface OpponentService {

    String VOLUME_T0_BASE_PATH = "/VolumeT0/FolderTree/ClassUT/";
    String UNMODIFIED_SRC = "unmodified_src";
    String BASE_SRC_PATH = "src/main/java";
    String BASE_TEST_PATH = "src/test/java";
    String BASE_COVERAGE_PATH = "coverage";

    String BASE_CODE_PATH = "project";
    String JACOCO_COVERAGE_FILE = "coveragetot.xml";
    String EVOSUITE_COVERAGE_FILE = "statistics.csv";



    void eliminaOpponent(String className);

    ResponseEntity<?> getNomiClassiUT(String jwt);

    ResponseEntity<FileUploadResponse> uploadOpponent(
            MultipartFile classUTFile,
            String classUTDetails,
            MultipartFile robotTestsZip) throws IOException;

    ResponseEntity<?> downloadClasse(@PathVariable("name") String name) throws Exception;

    List<ClassUTEntity> getAllClassUTs();

    List<ClassUTEntity> filterByDifficulty(String difficulty);

    List<ClassUTEntity> orderByDate();

    List<ClassUTEntity> orderByName();

    ResponseEntity<String> modificaClasse(String name, ClassUTEntity newContent, String jwt, HttpServletRequest request);

    ResponseEntity<?> eliminaClasse(String name);

    void eliminaFile(String fileName);

    List<OpponentEntity> getAllOpponents();

    OpponentEntity getOpponentData(String classUT, String opponentType, OpponentDifficulty opponentDifficulty);

    EvosuiteScore getOpponentEvosuiteScore(String classUT, String opponentType, OpponentDifficulty opponentDifficulty);

    JacocoScore getOpponentJacocoScore(String classUT, String opponentType, OpponentDifficulty opponentDifficulty);

    String getOpponentCoverage(String classUT, String opponentType, OpponentDifficulty opponentDifficulty);

}
