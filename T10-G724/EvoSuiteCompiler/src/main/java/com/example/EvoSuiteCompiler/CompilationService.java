package com.example.EvoSuiteCompiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLockInterruptionException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

//@Service
public class CompilationService {
    /*
     * Config gestisce i path delle directory e fornisce un id univoco 
     * serve solo per non tenere tutta la logica qua
     */
    protected final Config config;
    private final String underTestClassName;
    private final String underTestClassCode;
    /*
     *  Qui metto tutte le variabili che ottengo come output
     *  Attenzione outputMaven viene settato da compileExecuteCoverageWithMaven()
     *  mentre errors e Coverage in base al suo valore vengono settati da compileAndTest
     */
    public String outputMaven;
    public Boolean Errors;
    public String Coverage;
    /*
     *  Qui metto il path di maven in base al profilo, 
     *  in fase di testing ho bisogno di esplicitarlo rispetto a windows
     */
    @Value("${variabile.mvn}")
    private String mvn_path;

    // Logger
    protected static final Logger logger = LoggerFactory.getLogger(CompilationService.class);

    /* 
     * Costuttore specifico per la compilazione e la copertura delle
     * classi di test dei robot.
     */
    public CompilationService(String testingClassDir, String underTestClassName, String underTestClassCode, String mvn_path) {
        this.config = new Config();
        this.config.setRobotTestDir(testingClassDir); // Configura il path dei test generati dai robot
        this.underTestClassName = underTestClassName;
        this.underTestClassCode = underTestClassCode;
        this.outputMaven = null;
        this.mvn_path = mvn_path;
        logger.info("[CompilationService] Servizio creato per i test generati dai robot");
    }

    /*
     * Metodo per la gestione della richiesta di compilazione e copertura
     * delle classi di test generate dai robot Evosuite
     */
    public void handleRequest(String robotType) throws IOException, InterruptedException {

        createDirectoriesForRobotTests(robotType);
        logger.info("[handleRequest] Classi di test prese da directory: " + config.getRobotTestDir());

        // Copia le classi di test nella struttura Maven prevista
        copyTestFilesToMavenStructure(config.getRobotTestDir());

        // Salva la classe sotto test
        saveCodeToFileRobot(this.underTestClassName, this.underTestClassCode, config.getUnderTestClassPath());

        // Compila ed esegue i test
        boolean success = compileExecuteCoverageWithMaven();
        if (!success) {
            throw new RuntimeException("Compilazione o esecuzione dei test fallita.");
        }

        // Prelievo report di copertura Jacoco (jacoco.xml)
        this.Coverage = readFileToString(config.getCoverageFolderPath());
        this.Errors = false;
        logger.info("[Compilation Service] Compilazione terminata senza errori.");
        logger.info ("[Compilation Service] jacoco.xml: {}", this.Coverage);

        deleteTemporaryDirectories(config.getPathCompiler());
        logger.info("[CompilationService] File termporanei eliminati");
    }

    /* 
     * Metodo privato per copiare i file di test scritti dai robot in una 
     * struttura compatibile con Maven per consentire la compilazione dei
     * test e la valutazione della copertura del codice 
     */
    private void copyTestFilesToMavenStructure(String robotTestDirPath) throws IOException {
        // Directory contenente i file di test scritti dai robot (Volume T8/T9)
        File robotTestDir = new File(robotTestDirPath);
        // Directory src/test/java necessaria a Maven per compilare i test e valutare la coverage
        File mavenTestDir = new File(config.getTestingClassPath());

        // Verifica che la directory dei test dei robot esista
        if (!robotTestDir.exists() || !robotTestDir.isDirectory()) {
            throw new IOException("La directory dei test dei robot non esiste: " + robotTestDirPath);
        }

        // Copia tutti i file .java nella struttura Maven
        File[] testFiles = robotTestDir.listFiles((dir, name) -> name.endsWith(".java"));
        if (testFiles != null) {
            for (File testFile : testFiles) {
                File destination = new File(mavenTestDir, testFile.getName());
                Files.copy(testFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                logger.info("[Compilation Service] Copiato file di test: {}", testFile.getName());
            }
        } else {
            throw new IOException("Nessun file di test trovato nella directory: " + robotTestDirPath);
        }
}

    protected void createDirectoriesForRobotTests(String robotType) throws IOException {
        // Crea la directory principale per la compilazione
        createDirectoryIfNotExists(config.getPathCompiler());
        logger.info("[Compilation Service] Directory principale creata: {}", config.getPathCompiler());
    
        copyPomFile();

        logger.info("[Compilation Service] pom.xml copiato in: {}", config.getPathCompiler() + "pom.xml");
    
        // Verifica la directory dei test dei robot
        if (config.getRobotTestDir() != null) {
            File robotTestDir = new File(config.getRobotTestDir());
            if (!robotTestDir.exists() || !robotTestDir.isDirectory()) {
                throw new IOException("La directory dei test dei robot non esiste o non è valida: " + robotTestDir.getAbsolutePath());
            }
            logger.info("[Compilation Service] Directory dei test dei robot verificata: {}", config.getRobotTestDir());
        } else {
            throw new IllegalStateException("La directory dei test dei robot non è configurata.");
        }

        // Crea la directory per le classi di test generate dai robot
        createDirectoryIfNotExists(config.getTestingClassPath());
        logger.info("[CompilationService] Directory per le classi di test dei robot creata: {}", config.getTestingClassPath());
     
        // Crea la directory per la classe sotto test
        createDirectoryIfNotExists(config.getUnderTestClassPath());
        logger.info("[CompilationService] Directory per la classe sotto test creata: {}", config.getUnderTestClassPath());
    
        // Crea la directory per il report di copertura
        createDirectoryIfNotExists(config.getCoverageFolderPath());
        logger.info("[CompilationService] Directory per il report di copertura creata: {}", config.getCoverageFolderPath());
    }

    protected void createDirectoryIfNotExists(String path) throws IOException {
        File directory = new File(path);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("[createDirectoryIfNotExists] Impossibile creare la directory: " + path);
            }
        } else {
            throw new IOException("[createDirectoryIfNotExists] Errore esiste già la directory: " + path);
        }
    }

    private void copyPomFile() throws IOException {

        File pomFile = new File(config.getUsrPath() + config.getsep() + "testCompiler" + config.getsep() + "pom.xml");
        File destPomFile = new File(config.getPathCompiler() + "pom.xml");

        // Controlla se il file pom.xml esiste prima di tentare di copiarlo
        if (!pomFile.exists()) {
            throw new IOException("[Compilation Service] Il file pom.xml non esiste: " + pomFile.getAbsolutePath());
        } 
        /*
        *   Questa classe implementa un tipo di lock che distingue tra operazioni di lettura e scrittura, 
        *   consentendo a più thread di leggere simultaneamente, 
        *   ma limitando l'accesso esclusivo per le operazioni di scrittura.
        */
        final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        final ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        /*
        *    Col filechannel rende l'operazione atomica
        */
        FileChannel sourceChannel = FileChannel.open(pomFile.toPath(), StandardOpenOption.READ); 
        FileChannel destChannel   = FileChannel.open(destPomFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        readLock.lock();
        try {
            long size = sourceChannel.size();
            long position = 0;
            while (position < size) {
                position += sourceChannel.transferTo(position, size - position, destChannel);
            }
        } catch (OverlappingFileLockException e) {
            throw new FileConcurrencyException("[copyPomFile] FileLock concorrente sul file sorgente: " + e.getMessage(), e);
        } catch (FileLockInterruptionException e) {
            throw new FileConcurrencyException("[copyPomFile] L'acquisizione del lock è stata interrotta: " + e.getMessage(), e);
        } catch (NonWritableChannelException e) {
            throw new FileConcurrencyException("[copyPomFile] Canale di scrittura non valido per acquisire il lock: " + e.getMessage(), e);
        } catch (ClosedChannelException e) {
            throw new FileConcurrencyException("[copyPomFile] Il canale è stato chiuso prima di acquisire il lock: " + e.getMessage(), e);
        }finally {
            readLock.unlock();
        }
    }

    /* 
     * Aggiunge una dichiarazione del pacchetto al codice della classe da testare
     * per garantirne la visibilità alle classi di test generate dai robot.
     */
    private void saveCodeToFileRobot(String nameclass, String code, String path) throws IOException {
        // Controlla che il nome della classe e il percorso siano validi
        if (nameclass == null || nameclass.isEmpty()) {
            throw new IllegalArgumentException("[saveCodeToFile] Il nome della classe non può essere nullo o vuoto.");
        }
        if(!nameclass.endsWith(".java")){
            throw new IllegalArgumentException("[saveCodeToFile] Il nome della classe non ha l'estensione .java");
        }
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("[saveCodeToFile] Il percorso non può essere nullo o vuoto.");
        }

        // Aggiungi la dichiarazione del pacchetto al codice della classe da testare
        String newClassName = nameclass.replace(".java", "");
        String packageDeclaration = "package " + newClassName + "SourceCode;\n";
        code = packageDeclaration + code;

        // Creo il file di destinazione 
        File tempFile = new File(path + nameclass);
        // Imposta il file per la cancellazione all'uscita
        tempFile.deleteOnExit();
        // Utilizza FileChannel per scrivere in modo atomico
        try (FileChannel channel = FileChannel.open(tempFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            logger.info("[CompilationService] Ho creato il file " + path + nameclass);
            ByteBuffer buffer = StandardCharsets.UTF_8.encode(code);
            channel.write(buffer);
        } catch (IOException e) {
            throw new IOException("[saveCodeToFile] Errore durante la scrittura nel file: " + tempFile.getAbsolutePath(), e);
        }
    }

    private boolean compileExecuteCoverageWithMaven() throws RuntimeException{
        logger.error(mvn_path);
        ProcessBuilder processBuilder = new ProcessBuilder(mvn_path, "clean", "compile", "test");
        processBuilder.directory(new File(config.getPathCompiler()));
        StringBuilder output = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder();
        Process process = null;
        try {
            // Avvia il processo
            process = processBuilder.start();
            // Leggi l'output standard e l'errore in thread separati per evitare deadlock
            readProcessOutput(process, output, errorOutput);
            // Attendi la fine del processo con un timeout (es. 15 minuti)
            boolean finished = process.waitFor(15, TimeUnit.MINUTES);
            if (!finished) {
                process.destroyForcibly(); // Uccidi il processo se supera il timeout
                throw new RuntimeException("[compileExecuteCoverageWithMaven] Timeout superato. Il processo Maven è stato forzatamente interrotto.");
            }
            this.outputMaven += output.toString();
            // Verifica se il processo è terminato con successo
            logger.info("[Compilation Service]  compileExecuteCoverageWithMaven exitValue = {}",process.exitValue());
            return (process.exitValue()) == 0;
        } catch (IOException e) {
            logger.error("[Compilation Service] [MAVEN] {}", errorOutput);
            throw new RuntimeException("[compileExecuteCoverageWithMaven] Errore di I/O durante l'esecuzione del processo Maven: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error("[Compilation Service] [MAVEN] {}", errorOutput);
            Thread.currentThread().interrupt(); // Ripristina lo stato di interruzione
            throw new RuntimeException("[compileExecuteCoverageWithMaven] Processo Maven interrotto: " + e.getMessage(), e);
        } finally {
            // Assicurati che il processo sia terminato correttamente
            if (process != null && process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }

    private void readProcessOutput(Process process, StringBuilder output, StringBuilder errorOutput) throws IOException, InterruptedException {
        try (
             BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream())); 
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))
            ) {
            // Leggi l'output standard
            String line;
            while ((line = outputReader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
            // Leggi l'output di errore
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append(System.lineSeparator());
            }
        }
    }

    private void deleteTemporaryDirectories(String path) throws IOException {
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException e) {
                throw new IOException("[deleteTemporaryDirectories] Errore durante l'eliminazione della directory temporanea: " + dir.getAbsolutePath(), e);
            }
        } else {
            throw new IOException("[deleteTemporaryDirectories] La directory non esiste o non è valida: " + dir.getAbsolutePath());
        }
    }

    protected void deleteCartelleTest() throws IOException {
        deleteTemporaryDirectories(config.getPathCompiler());
    }

    private String readFileToString(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        return new String(bytes);
    }

    //mi serve per distingure le eccezioni sulla concorrenza 
    public class FileConcurrencyException extends IOException {

        public FileConcurrencyException(String message) {
            super(message);
        }

        public FileConcurrencyException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
