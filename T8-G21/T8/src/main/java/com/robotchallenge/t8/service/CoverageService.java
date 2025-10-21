package com.robotchallenge.t8.service;

import com.robotchallenge.t8.dto.request.OpponentCoverageRequestDTO;
import com.robotchallenge.t8.dto.request.StudentCoverageRequestDTO;
import com.robotchallenge.t8.service.exception.ProcessingExceptionWrapper;
import com.robotchallenge.t8.util.BuildResponse;
import com.robotchallenge.t8.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import testrobotchallenge.commons.models.dto.score.EvosuiteCoverageDTO;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service responsabile del calcolo della coverage tramite EvoSuite per i test del giocatore e i test degli avversari.
 */
@Service
public class CoverageService {

    private static final Logger logger = LoggerFactory.getLogger(CoverageService.class);

    /*
     * Serie di costanti relative a Evosuite, definite per risolvere le issue di SonarQube riguardo le ripetizioni di stringhe
     */
    private static final String EVOSUITE_FOLDER = "evosuite";
    private static final String EVOSUITE_JAR = "evosuite-1.0.6.jar";
    private static final String EVOSUITE_RUNTIME_JAR = "evosuite-standalone-runtime-1.0.6.jar";
    private static final String EVOSUITE_POM = "pom2.xml";

    /**
     * Legge in modo asincrono lo stream di output o di errore di un processo,
     * stampando le linee sul logger e aggiornando un flag in caso di errori specifici.
     *
     * <p>
     * In particolare, se una riga contiene la stringa {@code "ERROR SearchStatistics"},
     * il flag {@code foundError} viene impostato a {@code true}.
     * </p>
     *
     * @param inputStream lo {@link InputStream} del processo da leggere (stdout o stderr).
     * @param streamType  una {@link String} che indica il tipo di stream (ad esempio "OUTPUT" o "ERROR"),
     *                    utile per distinguere i messaggi nel log.
     * @param foundError  un {@link AtomicBoolean} condiviso che viene impostato a {@code true}
     *                    se viene rilevato un errore nello stream.
     */
    private static void streamGobbler(InputStream inputStream, String streamType, AtomicBoolean foundError) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info("[{}] {}", streamType, line);
                if (line.contains("ERROR SearchStatistics")) {
                    foundError.set(true);
                }
            }
        } catch (IOException e) {
            logger.error("[streamGobbler] Errore: {}", e.getMessage());
        }
    }

    /**
     * Calcola la coverage dei test di un avversario con EvoSuite, a partire da un archivio ZIP
     * contenente il codice sorgente da analizzare.
     * <p>
     * Il metodo esegue i seguenti passi:
     * <ol>
     *   <li>Crea una cartella temporanea per salvare il progetto.</li>
     *   <li>Estrae l’archivio ZIP contenente il codice sorgente.</li>
     *   <li>Copia all’interno della cartella i file JAR di EvoSuite e il POM necessario.</li>
     *   <li>Esegue EvoSuite sulla classe sotto test specificata nella richiesta.</li>
     *   <li>Elimina le cartelle e i file temporanei creati durante l’elaborazione.</li>
     * </ol>
     *
     * @param request    un {@link OpponentCoverageRequestDTO} con le informazioni sulla classe sotto test
     *                   (package e nome della classe) richieste da Evosuite.
     * @param projectZip un {@link MultipartFile} che rappresenta l’archivio ZIP del progetto da analizzare.
     * @return una {@link String} contenente i risultati della coverage calcolata da EvoSuite.
     */
    public EvosuiteCoverageDTO calculateRobotCoverage(OpponentCoverageRequestDTO request, MultipartFile projectZip) {
        // Definisco le cartelle su cui lavorare
        String cwd = String.valueOf(Paths.get(".").toAbsolutePath().normalize());
        String targetDir = cwd + File.separator + projectZip.getName() + "-" + generateTimestamp();
        Path targetDirPath = Paths.get(targetDir);
        Path sourceDirPath = Paths.get(cwd, EVOSUITE_FOLDER);

        // Salvo lo zip contente il codice da testare e lo unzippo
        try {
            Files.createDirectories(targetDirPath);
            projectZip.transferTo(new File(targetDir + File.separator + projectZip.getOriginalFilename()));
            FileUtil.unzip(targetDir + File.separator + projectZip.getOriginalFilename(), new File(targetDir));
        } catch (IOException e) {
            throw new ProcessingExceptionWrapper("Impossibile scompattare zip", e);
        }

        // Copio evosuite e pom
        try {
            Files.copy(sourceDirPath.resolve(EVOSUITE_JAR), targetDirPath.resolve(EVOSUITE_JAR), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(sourceDirPath.resolve(EVOSUITE_RUNTIME_JAR), targetDirPath.resolve(EVOSUITE_RUNTIME_JAR), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(sourceDirPath.resolve(EVOSUITE_POM), targetDirPath.resolve("pom.xml"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ProcessingExceptionWrapper(
                    "Errore durante la creazione/copia nel file system locale del progetto Maven (src|test|evosuite|pom.xml)", e);
        }

        String result = calculateEvosuiteCoverage(targetDir, request.getClassUTPackage(), request.getClassUTName());

        try {
            FileUtil.deleteDirectoryRecursively(targetDirPath);
        } catch (IOException e) {
            throw new ProcessingExceptionWrapper("Errore durante l'eliminazione delle cartelle e file temporanei creati per la compilazione", e);
        }

        return BuildResponse.buildExtendedDTO(result);
    }

    /**
     * Calcola la coverage dei test di un giocatore con EvoSuite, a partire da un DTO
     * contenente il codice sorgente da analizzare in forma testuale.
     * <p>
     * Il metodo esegue i seguenti passi:
     * <ol>
     *   <li>Crea una cartella temporanea e la organizza secondo la struttura di u progetto Maven.</li>
     *   <li>Salva il codice del giocatore, ricevuto in forma testuale con un {@link StudentCoverageRequestDTO}, come file nella cartella temporanea.</li>
     *   <li>Copia all’interno della cartella i file JAR di EvoSuite e il POM necessario.</li>
     *   <li>Esegue EvoSuite sulla classe sotto test specificata nella richiesta.</li>
     *   <li>Elimina le cartelle e i file temporanei creati durante l’elaborazione.</li>
     * </ol>
     *
     * @param request un {@link StudentCoverageRequestDTO} con informazioni e codice riguardante i test del giocatore.
     * @return una {@link String} contenente i risultati della coverage calcolata da EvoSuite.
     */
    public EvosuiteCoverageDTO calculatePlayerCoverage(StudentCoverageRequestDTO request) {
        String classUTName = request.getClassUTName();
        String classUTCode = request.getClassUTCode();
        String testClassCode = request.getTestClassCode();
        String testClassName = request.getTestClassName();

        String currentCWD = Paths.get(".").toAbsolutePath().normalize().toString();
        logger.info("[calculateStudentCoverage] CWD: {}", currentCWD);

        String baseCwd = String.format("%s/%s", currentCWD, "EvoSuite_Coverage_" + generateTimestamp());
        String cwdSrc = String.format("%s/src/main/java", baseCwd);
        String cwdTest = String.format("%s/src/test/java", baseCwd);
        Path baseCwdPath = Path.of(baseCwd);
        try {
            Files.createDirectories(baseCwdPath);
            Files.createDirectories(Path.of(cwdSrc));
            Files.createDirectories(Path.of(cwdTest));
            Files.write(Path.of(cwdSrc, classUTName + ".java"), classUTCode.getBytes(), StandardOpenOption.CREATE);
            Files.write(Path.of(cwdTest, testClassName + ".java"), testClassCode.getBytes(), StandardOpenOption.CREATE);
            Files.copy(Paths.get(currentCWD, EVOSUITE_FOLDER, EVOSUITE_JAR), Paths.get(baseCwd, "evosuite-1.0.6.jar"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(currentCWD, EVOSUITE_FOLDER, EVOSUITE_RUNTIME_JAR), Paths.get(baseCwd, "evosuite-standalone-1.0.6.jar"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(currentCWD, EVOSUITE_FOLDER, EVOSUITE_POM), Paths.get(baseCwd, "pom.xml"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ProcessingExceptionWrapper(
                    "Errore durante la creazione/copia nel file system locale del progetto Maven (src|test|evosuite|pom.xml)", e);
        }

        String result = calculateEvosuiteCoverage(baseCwd, request.getClassUTPackage(), request.getClassUTName());

        try {
            FileUtil.deleteDirectoryRecursively(baseCwdPath);
            return BuildResponse.buildExtendedDTO(result);
        } catch (IOException e) {
            throw new ProcessingExceptionWrapper("Errore durante l'eliminazione delle cartelle e file temporanei creati per la compilazione", e);
        }
    }

    /**
     * Esegue Evosuite sul progetto Maven passato per determinare la copertura dei test. L'esecuzione di Evosuite è effettuata
     * richiamando un {@link ProcessBuilder}.
     *
     * <p>
     * Il metodo esegue i seguenti passi:
     * <ol>
     *   <li>Compila il progetto.</li>
     *   <li>Copia le dipendenze di Evosuite necessarie.</li>
     *   <li>Esegue Evosuite, in istanze indipendenti, per ogni criterio disponibile.</li>
     * </ol>
     *
     * @param workingDir     una {@link String} che rappresenta il percorso al progetto Maven inizializzato.
     * @param classUTPackage una {@link String} che rappresenta il nome del package della classe sotto test. Può essere vuoto se la classe non ha package.
     * @param classUTName    una {@link String} che rappresenta il nome della classe da testare.
     * @return una {@link String} contenente i risultati della coverage calcolata da EvoSuite,
     * oppure {@code null} se si è verificato un errore durante il calcolo di uno dei criteri
     * o nella lettura del file dei risultati della copertura.
     */
    private String calculateEvosuiteCoverage(String workingDir, String classUTPackage, String classUTName) {
        // Preparo evosuite per la coverage
        runCommand(workingDir, 15, "mvn", "clean", "install");
        runCommand(workingDir, 15, "mvn", "dependency:copy-dependencies");

        String projectCP = workingDir + "/target/classes:" + workingDir + "/target/test-classes";
        List<String> criteria = Arrays.asList("LINE", "BRANCH", "EXCEPTION", "WEAKMUTATION", "OUTPUT", "METHOD", "METHODNOEXCEPTION", "CBRANCH");

        // Eseguo evosuite con ogni criterio di coverage che mette a disposizione. La variabile `foundError` mi dice se l'esecuzione del criterio
        // è fallita. In quel caso termino, in quanto non ha senso continuare con gli altri e perdere tempo
        for (String criterion : criteria) {
            boolean foundError = runCommand(workingDir, 30, "/usr/lib/jvm/java-8-openjdk-amd64/bin/java", "-jar", workingDir + "/evosuite-1.0.6.jar",
                    "-measureCoverage", "-class", classUTPackage + classUTName,
                    "-projectCP", projectCP, "-Dcriterion=" + criterion);

            if (foundError) {
                logger.error("[calculateEvosuiteCoverage] Errore durante la verifica della copertura: Uno o più goal non sono stati trovati");
                return null;
            }
        }

        // Leggo i risultati della coverage dal file prodotto da evosuite
        Path coverageFilePath = Paths.get(workingDir, "evosuite-report", "statistics.csv");
        try (Stream<String> coverage = Files.lines(coverageFilePath)) {
            return coverage.collect(Collectors.joining("\n"));
        } catch (IOException e) {
            logger.error("[calculateEvosuiteCoverage] Errore durante la lettura di statistics.csv: ", e);
            return null;
        }
    }

    /**
     * Esegue un processo sull'OS tramite {@link ProcessBuilder}.
     *
     * <p>
     * Il metodo esegue i seguenti passi:
     * <ol>
     *   <li>Imposta l'ambiente di esecuzione (forzando l'uso di Java 8 per EvoSuite).</li>
     *   <li>Si sposta nella working directory specificata.</li>
     *   <li>Avvia il processo con il comando fornito.</li>
     *   <li>Gestisce i flussi di output ed error tramite thread separati.</li>
     *   <li>Attende il termine del processo fino al timeout specificato.</li>
     *   <li>Termina forzatamente il processo in caso di superamento del timeout.</li>
     * </ol>
     *
     * @param workingDir una {@link String} che specifica la directory di lavoro in cui eseguire il processo.
     * @param timer      un {@link Integer} che rappresenta il tempo massimo di esecuzione del processo, in minuti.
     * @param command    uno o più {@link String} che rappresentano il comando e i relativi argomenti da eseguire.
     * @return {@code true} se è stato rilevato un errore durante l’esecuzione del processo,
     * {@code false} altrimenti.
     */
    private boolean runCommand(String workingDir, Integer timer, String... command) {
        Process process = null;
        AtomicBoolean foundError = new AtomicBoolean(false);

        // L'executor è necessario per poter catturare i log del processo e settare foundError
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            ProcessBuilder processBuilder = new ProcessBuilder();

            // Forzo l'uso di Java 8 per Evosuite
            processBuilder.environment().put("JAVA_HOME", "/usr/lib/jvm/java-8-openjdk-amd64");
            processBuilder.environment().put("PATH", "/usr/lib/jvm/java-8-openjdk-amd64/bin:" + System.getenv("PATH"));

            // Mi sposto nella nuova working directory
            processBuilder.directory(new File(workingDir));
            processBuilder.redirectErrorStream(true);
            processBuilder.command(command);
            process = processBuilder.start();

            // Preparo il logging dell'esecuzione del processo
            Process finalProcess = process;
            executor.submit(() -> streamGobbler(finalProcess.getInputStream(), "OUTPUT", foundError));
            executor.submit(() -> streamGobbler(finalProcess.getErrorStream(), "ERROR", foundError));

            logger.info("[runCommand] Avviato timer {} per comando {}", timer, command);
            boolean finished = process.waitFor(timer, TimeUnit.MINUTES);

            // Chiudo l'executor
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);

            if (!finished) {
                process.destroyForcibly();
                logger.error("[runCommand] Timeout superato. Processo terminato forzatamente.");
                throw new ProcessingExceptionWrapper("Timeout superato. Processo terminato forzatamente.", new TimeoutException());
            }
        } catch (IOException e) {
            throw new ProcessingExceptionWrapper("Errore I/O: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ProcessingExceptionWrapper("Processo interrotto: " + e.getMessage(), e);
        } finally {
            if (process != null && process.isAlive())
                process.destroyForcibly();
        }

        return foundError.get();
    }

    /**
     * Genera una stringa univoca basata sul timestamp corrente e su un numero casuale di 4 cifre.
     *
     * <p>
     * L’implementazione è thread-safe grazie all’utilizzo di {@link LocalDateTime} e
     * {@link ThreadLocalRandom}. La stringa risultante è composta da:
     * <ul>
     *   <li>Un timestamp nel formato {@code yyyyMMddHHmmssSSS},</li>
     *   <li>Un numero casuale a 4 cifre.</li>
     * </ul>
     * </p>
     *
     * @return una {@link String} che rappresenta un identificatore univoco basato
     * su timestamp e numero casuale.
     */
    private String generateTimestamp() {
        //questa funzione è thread safe
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        // Genera 4 cifre random thread-safe
        int randomFourDigits = ThreadLocalRandom.current().nextInt(1000, 10000); // 1000 (incluso) e 10000 (escluso)
        // Concatena il timestamp e le cifre casuali
        return timestamp + randomFourDigits;
    }

}
