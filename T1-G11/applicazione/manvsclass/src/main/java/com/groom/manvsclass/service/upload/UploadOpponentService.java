package com.groom.manvsclass.service.upload;

import com.groom.manvsclass.service.CoverageService;
import com.groom.manvsclass.util.upload.OpponentPathResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import testrobotchallenge.commons.models.dto.score.EvosuiteCoverageDTO;
import testrobotchallenge.commons.models.dto.score.JacocoCoverageDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

import com.groom.manvsclass.service.exception.*;

import static com.groom.manvsclass.util.upload.OpponentPathResolver.*;

@Service
public class UploadOpponentService {
    public static final String VOLUME_T0_BASE_PATH = OpponentPathResolver.VOLUME_T0_BASE_PATH;
    public static final String UNMODIFIED_SRC = OpponentPathResolver.UNMODIFIED_SRC;
    public static final String BASE_SRC_PATH = OpponentPathResolver.BASE_SRC_PATH;
    public static final String BASE_TEST_PATH = OpponentPathResolver.BASE_TEST_PATH;
    public static final String BASE_COVERAGE_PATH = OpponentPathResolver.BASE_COVERAGE_PATH;

    private static final String JACOCO_COVERAGE_FILE = "coveragetot.xml";
    private static final String EVOSUITE_COVERAGE_FILE = "statistics.csv";
    private static final String ROBOT_TYPE_EVOSUITE = "evosuite";
    private static final String ROBOT_TYPE_RANDOOP = "randoop";
    
    private final Logger logger = LoggerFactory.getLogger(UploadOpponentService.class);

    private final FileStorageService fileStorageService;
    private final JavaSourceFileService javaSourceFileService;
    private final CoverageService coverageService;
    private final OpponentTransformationService opponentTransformationService;

    public UploadOpponentService(FileStorageService fileStorageService, 
                                 JavaSourceFileService javaSourceFileService,
                                 CoverageService coverageService,
                                 OpponentTransformationService opponentTransformationService) {
        this.fileStorageService = fileStorageService;
        this.javaSourceFileService = javaSourceFileService;
        this.coverageService = coverageService;
        this.opponentTransformationService = opponentTransformationService;
    }

    public void saveOpponentsFromZip(String classUTFileName, String classUTName, MultipartFile classUTFile, MultipartFile robotTestsZip) throws IOException {
        Path operationTmpFolder = getTempOperationFolder(classUTName);
        
        try {
            fileStorageService.saveFileInFileSystem("robot.zip", operationTmpFolder, robotTestsZip);
            fileStorageService.extractZipIn(operationTmpFolder);
        } catch (IOException e) {
            logger.error("Errore durante l'estrazione del file ZIP dei robot test: {}", e.getMessage(), e);
            throw new FileUploadException(
                "Errore nell'estrazione del file ZIP: " + e.getMessage() + 
                ". Verificare che il file sia un archivio ZIP valido e non corrotto.",
                "error.robot.zip.extract",
                e.getMessage()
            );
        }

        Path unmodifiedSrcCodePath = getUnmodifiedSrcPath(classUTName);
        logger.info("Saving unmodified src in {}", unmodifiedSrcCodePath);
        fileStorageService.saveFileInFileSystem(classUTFileName, unmodifiedSrcCodePath, classUTFile);

        File[] robotGroupFiles = operationTmpFolder.toFile().listFiles();
        if (robotGroupFiles == null || robotGroupFiles.length == 0) {
            throw new RobotProcessingException(
                "Il file ZIP è vuoto o non contiene cartelle di test robot. " +
                "Assicurarsi che il file ZIP contenga le cartelle EvoSuiteTest e/o RandoopTest con i livelli di test.",
                "error.robot.zip.empty"
            );
        }
        
        File robotGroupFolder = robotGroupFiles[0];
        logger.info("Robot tests folder {}", robotGroupFolder);
        
        try {
            processRobotFolders(classUTFileName, classUTName, classUTFile, robotGroupFolder);
        } catch (IOException e) {
            logger.error("Errore durante l'elaborazione delle cartelle robot: {}", e.getMessage(), e);
            throw new RobotProcessingException(
                "Errore nell'elaborazione dei test robot: " + e.getMessage() + 
                ". Verificare che i file di test siano file Java validi e correttamente formattati.",
                "error.robot.processing",
                e.getMessage()
            );
        } finally {
            fileStorageService.deleteDirectoryRecursively(operationTmpFolder);
        }
    }

    private void processRobotFolders(String classUTFileName, String classUTName, MultipartFile classUTFile, File robotGroupFolder) throws IOException {
        File[] robotFolders = robotGroupFolder.listFiles();
        if (robotFolders == null || robotFolders.length == 0) {
            throw new RobotProcessingException(
                "La cartella dei robot test è vuota o non accessibile. " +
                "Verificare che il file ZIP contenga cartelle di test valide (EvoSuiteTest, RandoopTest).",
                "error.robot.folder.empty"
            );
        }
        
        int validRobotFolders = 0;
        int totalTestFilesFound = 0;
        
        for (File robotFolder : robotFolders) {
            if (!isValidRobotFolder(robotFolder)) {
                continue;
            }

            validRobotFolders++;
            String robotType = extractRobotType(robotFolder.getName());
            logger.info("Robot folder {}", robotFolder);
            logger.info("Saving robot type {}", robotType);

            int testFilesInRobot = uploadNewOpponents(classUTFileName, classUTName, classUTFile, robotFolder.toPath(), robotType);
            totalTestFilesFound += testFilesInRobot;
        }
        
        if (validRobotFolders == 0) {
            throw new RobotProcessingException(
                "Il file ZIP non contiene cartelle di test robot valide. " +
                "Le cartelle devono essere nominate 'EvoSuiteTest' o 'RandoopTest' " +
                "(il nome deve terminare con 'Test').",
                "error.robot.folder.noValid"
            );
        }
        
        if (totalTestFilesFound == 0) {
            throw new RobotProcessingException(
                "Il file ZIP non contiene alcun file di test Java (.java). " +
                "Verificare che le cartelle dei livelli (01Level, 02Level, ecc.) contengano " +
                "file di test con estensione .java all'interno della sottocartella 'TestSourceCode'.",
                "error.robot.noTests"
            );
        }
    }

    private boolean isValidRobotFolder(File robotFolder) {
        if (!robotFolder.isDirectory()) {
            logger.info("Ignoring file {} because it is not a directory", robotFolder);
            return false;
        }

        String robotName = robotFolder.getName();
        if (!robotName.endsWith("Test")) {
            logger.info("Ignoring directory {} because it does not follow the naming convention", robotFolder);
            return false;
        }

        return true;
    }

    private String extractRobotType(String robotFolderName) {
        String robotType = robotFolderName.substring(0, robotFolderName.length() - 4);
        return Character.toUpperCase(robotType.charAt(0)) + robotType.substring(1);
    }

    private int uploadNewOpponents(String classUTFileName, String classUTName, MultipartFile classUTFile, Path operationTmpFolder, String robotType) throws IOException {
        File[] levelFolders = operationTmpFolder.toFile().listFiles();
        
        if (levelFolders == null || levelFolders.length == 0) {
            logger.warn("Nessuna cartella di livello trovata per il tipo robot: {}", robotType);
            return 0;
        }
        
        int processedLevels = 0;
        int totalTestFiles = 0;
        
        for (File levelFolder : levelFolders) {
            try {
                int testFilesInLevel = processLevelFolder(classUTFileName, classUTName, classUTFile, robotType, levelFolder);
                if (testFilesInLevel > 0) {
                    processedLevels++;
                    totalTestFiles += testFilesInLevel;
                }
            } catch (IOException e) {
                logger.error("Errore durante l'elaborazione della cartella di livello {}: {}", 
                    levelFolder.getName(), e.getMessage(), e);
                // Continua con gli altri livelli invece di fallire completamente
                throw new RobotProcessingException(
                    "Errore nell'elaborazione del livello '" + levelFolder.getName() + "': " + e.getMessage() + 
                    ". Verificare che il livello contenga file di test validi.",
                    "error.robot.level.processing",
                    levelFolder.getName(), e.getMessage()
                );
            }
        }

        if (processedLevels == 0) {
            throw new RobotProcessingException(
                String.format("Nessun livello di test valido trovato per '%s'. " +
                    "I livelli devono essere cartelle nominate nel formato '01Level', '02Level', ecc. " +
                    "e contenere file di test Java.", robotType),
                "error.robot.level.noValid",
                robotType
            );
        }

        fileStorageService.deleteDirectoryRecursively(operationTmpFolder);
        return totalTestFiles;
    }

    private int processLevelFolder(String classUTFileName, String classUTName, MultipartFile classUTFile,
                                    String robotType, File levelFolder) throws IOException {
        if (!isValidLevelFolder(levelFolder)) {
            return 0;
        }

        String level = levelFolder.getName();
        logger.info("Saving level {}", level);

        OpponentPaths opponentPaths = buildOpponentPaths(classUTName, robotType, level);
        RobotTestPaths robotPaths = buildRobotTestPaths(levelFolder.toPath(), robotType);

        logPaths(opponentPaths, robotPaths);

        if (!isValidTestFolder(robotPaths.testPath)) {
            return 0;
        }
        
        // Count test files in this level
        int testFileCount = countJavaFiles(robotPaths.testPath);

        String[][] splitPackageNames = saveSourceAndTestFiles(classUTFile, classUTFileName, classUTName,
                robotType, opponentPaths, robotPaths);
        logPackageNames(splitPackageNames);

        boolean[] coverageFound = javaSourceFileService.saveCoverageFilesInVolume(robotPaths.coveragePath,
                opponentPaths.coveragePath);
        logCoverageStatus(levelFolder.getName(), coverageFound, opponentPaths.coveragePath);

        generateCoverageIfNeeded(robotType, coverageFound, classUTName, splitPackageNames[0], opponentPaths);

        computeScoresAndPersist(classUTName, robotType, levelFolder, opponentPaths.coveragePath);
        
        return testFileCount;
    }

    private boolean isValidLevelFolder(File levelFolder) {
        if (!levelFolder.isDirectory()) {
            logger.trace("Ignoring file {} because it is not a directory", levelFolder.getName());
            return false;
        }

        if (!levelFolder.getName().matches("\\d{2,}Level")) {
            logger.trace("Ignoring folder {} because it is not a level", levelFolder.getName());
            return false;
        }

        return true;
    }

    private void logPaths(OpponentPaths opponentPaths, RobotTestPaths robotPaths) {
        logger.info("Save SRC path {}", opponentPaths.srcPath);
        logger.info("Save TESTS path {}", opponentPaths.testPath);
        logger.info("Save COVERAGE path {}", opponentPaths.coveragePath);
        logger.info("Robot TESTS path {}", robotPaths.testPath);
        logger.info("Robot COVERAGE path {}", robotPaths.coveragePath);
    }

    private boolean isValidTestFolder(Path fromTestPath) {
        if (!Files.exists(fromTestPath)) {
            logger.info("Skipping folder {} because it does not exist", fromTestPath);
            return false;
        }

        File[] files = fromTestPath.toFile().listFiles();
        if (files == null || files.length == 0) {
            logger.info("Skipping folder {} because it does not have any files", fromTestPath);
            return false;
        }

        if (Arrays.stream(files).noneMatch(file -> file.getName().endsWith(".java"))) {
            logger.info("Skipping folder {} because it does not contain any .java files", fromTestPath);
            return false;
        }

        return true;
    }
    
    private int countJavaFiles(Path testPath) {
        if (!Files.exists(testPath)) {
            return 0;
        }
        
        File[] files = testPath.toFile().listFiles();
        if (files == null) {
            return 0;
        }
        
        return (int) Arrays.stream(files)
                .filter(file -> file.getName().endsWith(".java"))
                .count();
    }

    private String[][] saveSourceAndTestFiles(MultipartFile classUTFile, String classUTFileName,
                                              String classUTName, String robotType,
                                              OpponentPaths opponentPaths,
                                              RobotTestPaths robotPaths)
            throws IOException {
        String[][] splitPackageNames = javaSourceFileService.saveTestFilesInVolume(
                robotPaths.testPath, opponentPaths.testPath, classUTName, robotType);

        javaSourceFileService.saveSrcFileInVolume(classUTFile, opponentPaths.srcPath,
                splitPackageNames[0], classUTFileName);

        return splitPackageNames;
    }

    private void logPackageNames(String[][] splitPackageNames) {
        logger.info("SRC package names split {}", Arrays.toString(splitPackageNames[0]));
        logger.info("TEST package names split {}", Arrays.toString(splitPackageNames[1]));
    }

    private void logCoverageStatus(String levelName, boolean[] coverageFound, Path toCoveragePath) {
        logger.info("Coverage flags for {} - jacoco: {}, evosuite: {}",
                levelName, coverageFound[0], coverageFound[1]);

        if (Files.exists(toCoveragePath)) {
            File[] covFiles = toCoveragePath.toFile().listFiles();
            if (covFiles != null) {
                for (File f : covFiles) {
                    logger.info("Coverage file present: {}", f.getName());
                }
            }
        } else {
            logger.info("Coverage path does not exist: {}", toCoveragePath);
        }
    }

    private void generateCoverageIfNeeded(String robotType, boolean[] coverageFound,
                                          String classUTName, String[] srcPackageNameSplit,
                                          OpponentPaths opponentPaths) throws IOException {
        if (ROBOT_TYPE_EVOSUITE.equalsIgnoreCase(robotType)) {
            logger.info("Generating Evosuite coverage for robot type {}", robotType);
            ensureEvoCoverageIfMissing(coverageFound[1], classUTName, srcPackageNameSplit,
                    opponentPaths.srcPath, opponentPaths.testPath, opponentPaths.coveragePath);
        } else if (ROBOT_TYPE_RANDOOP.equalsIgnoreCase(robotType)) {
            logger.info("Skipping Evosuite coverage generation for robot type {}", robotType);
            ensureJacocoCoverageIfMissing(coverageFound[0], classUTName,
                    opponentPaths.srcPath, opponentPaths.testPath, opponentPaths.coveragePath);
        } else {
            logger.error("Unknown robot type {}, skipping coverage generation", robotType);
        }
    }

    private void ensureEvoCoverageIfMissing(boolean evosuiteFound, String classUTName, String[] srcPackageNameSplit,
                                            Path toSrcPath, Path toTestPath, Path toCoveragePath) throws IOException {
        if (evosuiteFound) return;

        Path tmpFolderToZip = getTempZipFolder(classUTName);
        prepareZipDirectories(tmpFolderToZip, toSrcPath, toTestPath);
        
        File zip = zipSourceFiles(tmpFolderToZip);
        if (zip.exists()) {
            generateEvosuiteCoverage(classUTName, srcPackageNameSplit, toCoveragePath, zip);
        }

        cleanupTempFiles(zip, tmpFolderToZip);
    }

    private void ensureJacocoCoverageIfMissing(boolean jacocoFound, String classUTName,
                                              Path toSrcPath, Path toTestPath, Path toCoveragePath) throws IOException {
        if (jacocoFound) return;

        Path tmpFolderToZip = getTempZipFolder(classUTName);
        prepareZipDirectories(tmpFolderToZip, toSrcPath, toTestPath);
        
        File zip = zipSourceFiles(tmpFolderToZip);
        if (zip.exists()) {
            generateJacocoCoverage(classUTName, toCoveragePath, zip);
        }

        cleanupTempFiles(zip, tmpFolderToZip);
    }

    private void prepareZipDirectories(Path tmpFolderToZip, Path toSrcPath, Path toTestPath) throws IOException {
        Path srcDestination = tmpFolderToZip.resolve(BASE_SRC_PATH);
        Files.createDirectories(srcDestination);
        fileStorageService.copyDirectoryRecursively(toSrcPath, srcDestination);

        Path testDestination = tmpFolderToZip.resolve(BASE_TEST_PATH);
        Files.createDirectories(testDestination);
        fileStorageService.copyDirectoryRecursively(toTestPath, testDestination);
    }

    private File zipSourceFiles(Path tmpFolderToZip) throws IOException {
        String srcFolder = tmpFolderToZip + "/src";
        String zipFilePath = tmpFolderToZip + "/src.zip";
        fileStorageService.zipDirectory(srcFolder, zipFilePath);
        return new File(zipFilePath);
    }

    private void generateEvosuiteCoverage(String classUTName, String[] srcPackageNameSplit, 
                                          Path toCoveragePath, File zip) throws IOException {
        String srcPackage = (srcPackageNameSplit != null) 
            ? String.join(".", srcPackageNameSplit) + "." 
            : "";

        logger.info("Calling Evosuite coverage generation for class {} with zip {} and srcPackage={}", 
                classUTName, zip.getAbsolutePath(), srcPackage);
        
        try {
            EvosuiteCoverageDTO coverageDTO = coverageService.generateMissingEvoSuiteCoverage(classUTName, srcPackage, zip);
            fileStorageService.writeStringToFile(coverageDTO.getResultFileContent(), 
                    new File(toCoveragePath.toFile(), EVOSUITE_COVERAGE_FILE));
        } catch (Exception e) {
            logger.error("Errore durante la generazione della coverage EvoSuite per {}: {}", classUTName, e.getMessage(), e);
            throw new RobotProcessingException(
                "Errore durante la compilazione e generazione della coverage EvoSuite: " + e.getMessage() + 
                ". Verificare che i test EvoSuite siano compilabili e che la classe sotto test sia valida.",
                "error.robot.processing",
                e.getMessage()
            );
        }
    }

    private void generateJacocoCoverage(String classUTName, Path toCoveragePath, File zip) {
        logger.info("Calling Jacoco coverage generation for class {} with zip {}", classUTName, zip.getAbsolutePath());
        
        try {
            JacocoCoverageDTO coverageDTO = coverageService.generateMissingJacocoCoverage(classUTName, zip);
            fileStorageService.writeStringToFile(coverageDTO.getCoverage(), 
                    new File(toCoveragePath.toFile(), JACOCO_COVERAGE_FILE));
        } catch (Exception e) {
            logger.error("Errore durante la generazione della coverage JaCoCo per {}: {}", classUTName, e.getMessage(), e);
            throw new RobotProcessingException(
                "Errore durante la compilazione e generazione della coverage JaCoCo: " + e.getMessage() + 
                ". Verificare che i test Randoop siano compilabili, che la classe sotto test sia valida " +
                "e che non ci siano errori di compilazione.",
                "error.robot.processing",
                e.getMessage()
            );
        }
    }

    private void cleanupTempFiles(File zip, Path tmpFolderToZip) throws IOException {
        if (zip.exists()) {
            Files.delete(zip.toPath());
        }
        fileStorageService.deleteDirectoryRecursively(tmpFolderToZip);
    }

    private void computeScoresAndPersist(String classUTName, String robotType, File levelFolder, Path toCoveragePath) throws IOException {
        Path evosuitePath = toCoveragePath.resolve(EVOSUITE_COVERAGE_FILE);
        Path jacocoPath = toCoveragePath.resolve(JACOCO_COVERAGE_FILE);

        opponentTransformationService.createAndPersistOpponent(classUTName, robotType, levelFolder, evosuitePath, jacocoPath);
    }

    /**
     * Rollback all opponent files created during upload.
     * Deletes the entire class directory from the volume.
     * Silently handles cases where directories don't exist.
     */
    public void rollbackOpponentFiles(String classUTName) {
        try {
            Path classUTDirectory = Path.of(VOLUME_T0_BASE_PATH, classUTName);
            if (classUTDirectory.toFile().exists()) {
                fileStorageService.deleteDirectoryRecursively(classUTDirectory);
                logger.info("Rollback: deleted opponent files directory for {}", classUTName);
            } else {
                logger.debug("Rollback: opponent files directory does not exist for {}", classUTName);
            }

            Path unmodifiedSrcDirectory = getUnmodifiedSrcPath(classUTName);
            if (unmodifiedSrcDirectory.toFile().exists()) {
                fileStorageService.deleteDirectoryRecursively(unmodifiedSrcDirectory);
                logger.info("Rollback: deleted unmodified src directory for {}", classUTName);
            } else {
                logger.debug("Rollback: unmodified src directory does not exist for {}", classUTName);
            }
        } catch (Exception e) {
            logger.warn("Error during rollback of opponent files for {}: {}", classUTName, e.getMessage());
        }
    }

}