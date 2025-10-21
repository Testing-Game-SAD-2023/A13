package com.g2.game.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Service che espone le operazioni necessarie al logging sul VolumeT0 del turno del giocatore. Per ogni turno,
 * corrispondente a una compilazione del giocatore, viene creata una directory apposita e al suo interno salvato il test
 * scritto dall'utente, strutturato come progetto Maven, accompagnato dal risultato della compilazione del test
 * (restituito da T7) e dalle eventuali metriche di copertura restituite dal JaCoCo ed EvoSuite.
 */
@Service
public class LogWriterService {

    private static final String EVOSUITE_COVERAGE_FILE = "statistics.csv";
    private static final String JACOCO_COVERAGE_FILE = "coveragetot.xml";
    private static final Logger logger = LoggerFactory.getLogger(LogWriterService.class);

    /**
     * Crea una o più directory se non esistono già.
     *
     * @param directories l'array di path di directory da creare
     * @throws RuntimeException se non è possibile creare una delle directory
     */
    public void createDirectory(String... directories) {
        for (String directory : directories) {
            try {
                Files.createDirectories(Path.of(directory));
            } catch (IOException e) {
                logger.error("[createDirectory] Errore durante la creazione delle cartelle utente in VolumeT0: ", e);
            }
        }
    }

    /**
     * Scrive sul file il log del turno. Da usare quando è disponibile sia l'output di T7 che di T8.
     *
     * @param underTestClassCode il codice sorgente della classe UT
     * @param underTestClassName il nome della classe UT
     * @param testingClassCode   il codice sorgente del test scritto dal giocatore
     * @param testingClassName   il nome della classe di test (senza prefisso "Test")
     * @param responseT8         l'output di copertura del test utente prodotto dal T8 (coverage EvoSuite)
     * @param responseT7         l'output di copertura del test utente prodotto da T7 (coverage JaCoCo)
     * @param userSrcDir         la directory dove salvare la classe UT
     * @param userTestDir        la directory dove salvare il test dell'utente
     * @param userCoverageDir    la directory dove salvare i risultati della copertura restituita da JaCoCo ed EvoSuite
     * @throws RuntimeException se si verifica un errore di I/O o se i parametri sono {@code null}
     */
    public void writeTurn(String underTestClassCode, String underTestClassName,
                          String testingClassCode, String testingClassName,
                          String responseT8, String responseT7,
                          String userSrcDir, String userTestDir, String userCoverageDir) {
        try {
            Files.copy(new ByteArrayInputStream(underTestClassCode.getBytes()), Path.of(userSrcDir, underTestClassName), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new ByteArrayInputStream(testingClassCode.getBytes()), Paths.get(userTestDir, "Test" + testingClassName), StandardCopyOption.REPLACE_EXISTING);
            if (responseT7 != null)
                Files.copy(new ByteArrayInputStream(responseT7.getBytes()), Paths.get(userCoverageDir, JACOCO_COVERAGE_FILE), StandardCopyOption.REPLACE_EXISTING);
            if (responseT8 != null)
                Files.copy(new ByteArrayInputStream(responseT8.getBytes()), Paths.get(userCoverageDir, EVOSUITE_COVERAGE_FILE), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | NullPointerException e) {
            logger.error("[writeTurn] Errore durante la scrittura dei file in VolumeT0: ", e);
        }
    }
}
