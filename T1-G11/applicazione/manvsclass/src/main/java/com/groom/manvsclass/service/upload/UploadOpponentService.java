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
        fileStorageService.saveFileInFileSystem("robot.zip", operationTmpFolder, robotTestsZip);
        fileStorageService.extractZipIn(operationTmpFolder);

        Path unmodifiedSrcCodePath = getUnmodifiedSrcPath(classUTName);
        logger.info("Saving unmodified src in {}", unmodifiedSrcCodePath);
        fileStorageService.saveFileInFileSystem(classUTFileName, unmodifiedSrcCodePath, classUTFile);

        File robotGroupFolder = Objects.requireNonNull(operationTmpFolder.toFile().listFiles())[0];
        logger.info("Robot tests folder {}", robotGroupFolder);
        
        processRobotFolders(classUTFileName, classUTName, classUTFile, robotGroupFolder);
        fileStorageService.deleteDirectoryRecursively(operationTmpFolder);
    }

    private void processRobotFolders(String classUTFileName, String classUTName, MultipartFile classUTFile, File robotGroupFolder) throws IOException {
        for (File robotFolder : Objects.requireNonNull(robotGroupFolder.listFiles())) {
            if (!isValidRobotFolder(robotFolder)) {
                continue;
            }

            String robotType = extractRobotType(robotFolder.getName());
            logger.info("Robot folder {}", robotFolder);
            logger.info("Saving robot type {}", robotType);

            uploadNewOpponents(classUTFileName, classUTName, classUTFile, robotFolder.toPath(), robotType);
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

    private void uploadNewOpponents(String classUTFileName, String classUTName, MultipartFile classUTFile, Path operationTmpFolder, String robotType) throws IOException {
        for (File levelFolder : Objects.requireNonNull(operationTmpFolder.toFile().listFiles())) {
            try {
                processLevelFolder(classUTFileName, classUTName, classUTFile, robotType, levelFolder);
            } catch (IOException e) {
                logger.error("Error processing level folder {}: {}", levelFolder, e.getMessage(), e);
            }
        }

        fileStorageService.deleteDirectoryRecursively(operationTmpFolder);
    }

    private void processLevelFolder(String classUTFileName, String classUTName, MultipartFile classUTFile,
                                    String robotType, File levelFolder) throws IOException {
        if (!isValidLevelFolder(levelFolder)) {
            return;
        }

        String level = levelFolder.getName();
        logger.info("Saving level {}", level);

        OpponentPaths opponentPaths = buildOpponentPaths(classUTName, robotType, level);
        RobotTestPaths robotPaths = buildRobotTestPaths(levelFolder.toPath(), robotType);

        logPaths(opponentPaths, robotPaths);

        if (!isValidTestFolder(robotPaths.testPath)) {
            return;
        }

        String[][] splitPackageNames = saveSourceAndTestFiles(classUTFile, classUTFileName, classUTName,
                robotType, opponentPaths, robotPaths);
        logPackageNames(splitPackageNames);

        boolean[] coverageFound = javaSourceFileService.saveCoverageFilesInVolume(robotPaths.coveragePath,
                opponentPaths.coveragePath);
        logCoverageStatus(levelFolder.getName(), coverageFound, opponentPaths.coveragePath);

        generateCoverageIfNeeded(robotType, coverageFound, classUTName, splitPackageNames[0], opponentPaths);

        computeScoresAndPersist(classUTName, robotType, levelFolder, opponentPaths.coveragePath);
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
        
        EvosuiteCoverageDTO coverageDTO = coverageService.generateMissingEvoSuiteCoverage(classUTName, srcPackage, zip);
        fileStorageService.writeStringToFile(coverageDTO.getResultFileContent(), 
                new File(toCoveragePath.toFile(), EVOSUITE_COVERAGE_FILE));
    }

    private void generateJacocoCoverage(String classUTName, Path toCoveragePath, File zip) throws IOException {
        logger.info("Calling Jacoco coverage generation for class {} with zip {}", classUTName, zip.getAbsolutePath());
        
        JacocoCoverageDTO coverageDTO = coverageService.generateMissingJacocoCoverage(classUTName, zip);
        fileStorageService.writeStringToFile(coverageDTO.getCoverage(), 
                new File(toCoveragePath.toFile(), JACOCO_COVERAGE_FILE));
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

}