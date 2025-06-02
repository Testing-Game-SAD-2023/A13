package com.g2.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileOperationService {

    private static final Logger logger = LoggerFactory.getLogger(FileOperationService.class);

    public void createDirectory(String...directories) {
        for (String directory : directories) {
            try {
                Files.createDirectories(Path.of(directory));
            } catch (IOException e) {
                logger.error("[createDirectory] Errore durante la creazione delle cartelle utente in VolumeT0: ", e);
                throw new RuntimeException("[createDirectory] Errore durante la creazione delle cartelle utente in VolumeT0: " + e);
            }
        }
    }

    public void writeTurn(String underTestClassCode, String underTestClassName,
                             String testingClassCode, String testingClassName,
                             String response_T8, String response_T7,
                             String userSrcDir, String userTestDir, String userCoverageDir) {
        try {
            Files.copy(new ByteArrayInputStream(underTestClassCode.getBytes()), Path.of(userSrcDir, underTestClassName), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new ByteArrayInputStream(testingClassCode.getBytes()), Paths.get(userTestDir, "Test" + testingClassName), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new ByteArrayInputStream(response_T8.getBytes()), Paths.get(userCoverageDir, "statistics.csv"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new ByteArrayInputStream(response_T7.getBytes()), Paths.get(userCoverageDir, "coveragetot.xml"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | NullPointerException e) {
            logger.error("[writeTurn] Errore durante la scrittura dei file in VolumeT0: ", e);
            throw new RuntimeException("[writeTurn] Errore durante la scrittura dei file in VolumeT0: " + e);
        }
    }

    public void writeTurnNew(String underTestClassCode, String underTestClassName,
                             String testingClassCode, String testingClassName,
                             String response, String filename,
                             String userSrcDir, String userTestDir, String userCoverageDir) {
        try {
            Files.copy(new ByteArrayInputStream(underTestClassCode.getBytes()), Path.of(userSrcDir, underTestClassName), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new ByteArrayInputStream(testingClassCode.getBytes()), Paths.get(userTestDir, "Test" + testingClassName), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new ByteArrayInputStream(response.getBytes()), Paths.get(userCoverageDir, filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | NullPointerException e) {
            logger.error("[writeTurn] Errore durante la scrittura dei file in VolumeT0: ", e);
            throw new RuntimeException("[writeTurn] Errore durante la scrittura dei file in VolumeT0: " + e);
        }
    }
}
