package com.groom.manvsclass.service;

import com.groom.manvsclass.api.ApiGatewayClient;
import com.groom.manvsclass.model.Opponent;
import com.groom.manvsclass.model.repository.OpponentRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import testrobotchallenge.commons.models.dto.score.EvosuiteCoverageDTO;
import testrobotchallenge.commons.models.dto.score.JacocoCoverageDTO;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import testrobotchallenge.commons.models.score.Coverage;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;
import testrobotchallenge.commons.util.ExtractScore;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UploadOpponentService {
    public static final String VOLUME_T0_BASE_PATH = "/VolumeT0/FolderTree/ClassUT/";
    public static final String UNMODIFIED_SRC = "unmodified_src";
    public static final String BASE_SRC_PATH = "src/main/java";
    public static final String BASE_TEST_PATH = "src/test/java";
    public static final String BASE_COVERAGE_PATH = "coverage";

    private static final String BASE_CODE_PATH = "project";
    private static final String JACOCO_COVERAGE_FILE = "coveragetot.xml";
    private static final String EVOSUITE_COVERAGE_FILE = "statistics.csv";

    // Inner class to hold path context
    private static class PathContext {
        Path toSrcPath;
        Path toTestPath;
        Path toCoveragePath;
        Path fromTestPath;
        Path fromCoveragePath;
    }
    private final Logger logger = LoggerFactory.getLogger(UploadOpponentService.class);

    private final FileStorageService fileStorageService;
    private final CoverageService coverageService;
    private final OpponentPersistenceService opponentPersistenceService;

    public UploadOpponentService(FileStorageService fileStorageService, CoverageService coverageService,
                                 OpponentPersistenceService opponentPersistenceService) {
        this.fileStorageService = fileStorageService;
        this.coverageService = coverageService;
        this.opponentPersistenceService = opponentPersistenceService;
    }

    /*
     *
     */
    public void saveOpponentsFromZip(String classUTFileName, String classUTName, MultipartFile classUTFile, MultipartFile robotTestsZip) throws IOException {
        Path operationTmpFolder = Paths.get(String.format("%s/%s/tmp", VOLUME_T0_BASE_PATH, classUTName));
        fileStorageService.saveFileInFileSystem("robot.zip", operationTmpFolder, robotTestsZip);
        fileStorageService.extractZipIn(operationTmpFolder);

        Path unmodifiedSrcCodePath = Paths.get(String.format("%s/%s/%s", VOLUME_T0_BASE_PATH, UNMODIFIED_SRC, classUTName));
        logger.info("Saving unmodified src in {}", unmodifiedSrcCodePath);
        fileStorageService.saveFileInFileSystem(classUTFileName, unmodifiedSrcCodePath, classUTFile);

        File robotGroupFolder = Objects.requireNonNull(operationTmpFolder.toFile().listFiles())[0];
        logger.info("Robot tests folder {}", robotGroupFolder);
        for (File robotFolder : Objects.requireNonNull(robotGroupFolder.listFiles())) {
            if (!robotFolder.isDirectory()) {
                logger.info("Ignoring file {} because it is not a directory", robotFolder);
                continue;
            }

            String robotType = robotFolder.getName();
            if (!robotType.endsWith("Test")) {
                logger.info("Ignoring directory {} because it does not follow the naming convention", robotFolder);
                continue;
            }
            robotType = robotType.substring(0, robotType.length() - 4);
            robotType = Character.toUpperCase(robotType.charAt(0)) + robotType.substring(1);

            logger.info("Robot folder {}", robotFolder);
            logger.info("Saving robot type {}", robotType);

            uploadNewOpponents(classUTFileName, classUTName, classUTFile, robotFolder.toPath(), robotType, Paths.get(VOLUME_T0_BASE_PATH));
        }

        fileStorageService.deleteDirectoryRecursively(operationTmpFolder);
    }

    private void uploadNewOpponents(String classUTFileName, String classUTName, MultipartFile classUTFile, Path operationTmpFolder, String robotType, Path volumeBasePath) throws IOException {
        for (File levelFolder : Objects.requireNonNull(operationTmpFolder.toFile().listFiles())) {
            try {
                processLevelFolder(classUTFileName, classUTName, classUTFile, robotType, volumeBasePath, levelFolder);
            } catch (IOException e) {
                logger.error("Error processing level folder {}: {}", levelFolder, e.getMessage(), e);
            }
        }

        fileStorageService.deleteDirectoryRecursively(operationTmpFolder);
    }

    private void processLevelFolder(String classUTFileName, String classUTName, MultipartFile classUTFile,
                                    String robotType, Path volumeBasePath, File levelFolder) throws IOException {
        if (!isValidLevelFolder(levelFolder)) {
            return;
        }

        String level = levelFolder.getName();
        logger.info("Saving level {}", level);

        PathContext paths = buildPathContext(classUTName, robotType, volumeBasePath, level, levelFolder);
        logPaths(paths);

        if (!isValidTestFolder(paths.fromTestPath)) {
            return;
        }

        String[][] splitPackageNames = saveSourceAndTestFiles(classUTFile, classUTFileName, classUTName,
                robotType, paths);
        logPackageNames(splitPackageNames);

        boolean[] coverageFound = fileStorageService.saveCoverageFilesInVolume(paths.fromCoveragePath,
                paths.toCoveragePath);
        logCoverageStatus(levelFolder.getName(), coverageFound, paths.toCoveragePath);

        generateCoverageIfNeeded(robotType, coverageFound, classUTName, splitPackageNames[0],
                paths, volumeBasePath);

        computeScoresAndPersist(classUTName, robotType, levelFolder, paths.toCoveragePath);
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

    private PathContext buildPathContext(String classUTName, String robotType, Path volumeBasePath,
                                         String level, File levelFolder) {
        PathContext ctx = new PathContext();

        ctx.toSrcPath = Paths.get(String.format("%s/%s/%s/%s/%s/%s",
                volumeBasePath, classUTName, robotType, BASE_CODE_PATH, level, BASE_SRC_PATH));
        ctx.toTestPath = Paths.get(String.format("%s/%s/%s/%s/%s/%s",
                volumeBasePath, classUTName, robotType, BASE_CODE_PATH, level, BASE_TEST_PATH));
        ctx.toCoveragePath = Paths.get(String.format("%s/%s/%s/%s/%s",
                volumeBasePath, classUTName, robotType, BASE_COVERAGE_PATH, level));

        if ("evosuite".equalsIgnoreCase(robotType)) {
            ctx.fromTestPath = Paths.get(String.format("%s/TestSourceCode/evosuite-tests", levelFolder));
            ctx.fromCoveragePath = Paths.get(String.format("%s/TestReport", levelFolder.getPath()));
        } else {
            ctx.fromTestPath = Paths.get(levelFolder.getPath());
            ctx.fromCoveragePath = Paths.get(levelFolder.getPath());
        }

        return ctx;
    }

    private void logPaths(PathContext paths) {
        logger.info("Save SRC path {}", paths.toSrcPath);
        logger.info("Save TESTS path {}", paths.toTestPath);
        logger.info("Save COVERAGE path {}", paths.toCoveragePath);
        logger.info("Robot TESTS path {}", paths.fromTestPath);
        logger.info("Robot COVERAGE path {}", paths.fromCoveragePath);
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
                                              String classUTName, String robotType, PathContext paths)
            throws IOException {
        String[][] splitPackageNames = fileStorageService.saveTestFilesInVolume(
                paths.fromTestPath, paths.toTestPath, classUTName, robotType);

        fileStorageService.saveSrcFileInVolume(classUTFile, paths.toSrcPath,
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
                                          PathContext paths, Path volumeBasePath) throws IOException {
        if ("evosuite".equalsIgnoreCase(robotType)) {
            logger.info("Generating Evosuite coverage for robot type {}", robotType);
            ensureEvoCoverageIfMissing(coverageFound[1], classUTName, srcPackageNameSplit,
                    paths.toSrcPath, paths.toTestPath,
                    paths.toCoveragePath, volumeBasePath);
        } else if ("randoop".equalsIgnoreCase(robotType)) {
            logger.info("Skipping Evosuite coverage generation for robot type {}", robotType);
            ensureJacocoCoverageIfMissing(coverageFound[0], classUTName,
                    paths.toSrcPath, paths.toTestPath,
                    paths.toCoveragePath, volumeBasePath);
        } else {
            logger.error("Unknown robot type {}, skipping coverage generation", robotType);
        }
    }

    private void ensureEvoCoverageIfMissing(boolean evosuiteFound, String classUTName, String[] srcPackageNameSplit,
                                            Path toSrcPath, Path toTestPath, Path toCoveragePath, Path volumeBasePath) throws IOException {
        if (evosuiteFound) return;

        Path tmpFolderToZip = Paths.get(String.format("%s/%s/tmp_zip", volumeBasePath, classUTName));

        Files.createDirectories(Paths.get(String.format("%s/%s", tmpFolderToZip, Paths.get(BASE_SRC_PATH))));
        fileStorageService.copyDirectoryRecursively(toSrcPath, Paths.get(String.format("%s/%s", tmpFolderToZip, Paths.get(BASE_SRC_PATH))));

        Files.createDirectories(Paths.get(String.format("%s/%s", tmpFolderToZip, Paths.get(BASE_TEST_PATH))));
        fileStorageService.copyDirectoryRecursively(toTestPath, Paths.get(String.format("%s/%s", tmpFolderToZip, Paths.get(BASE_TEST_PATH))));

        fileStorageService.zipDirectory(String.format("%s/src", tmpFolderToZip), String.format("%s/src.zip", tmpFolderToZip));
        File zip = new File(String.format("%s/src.zip", tmpFolderToZip));

        String srcPackage = "";
        if (srcPackageNameSplit != null)
            srcPackage = String.join(".", srcPackageNameSplit) + ".";

                if (!zip.exists()) {
                    logger.error("EvoSuite: zip was not created at {}", zip.getAbsolutePath());
                    fileStorageService.deleteDirectoryRecursively(tmpFolderToZip);
                } else {
                    logger.info("Calling Evosuite coverage generation for class {} with zip {} and srcPackage={}", classUTName, zip.getAbsolutePath(), srcPackage);
                    EvosuiteCoverageDTO coverageDTO = coverageService.generateMissingEvoSuiteCoverage(classUTName, srcPackage, zip);
                    fileStorageService.writeStringToFile(coverageDTO.getResultFileContent(), new File(toCoveragePath.toFile(), EVOSUITE_COVERAGE_FILE));
                }

        Files.delete(zip.toPath());
        fileStorageService.deleteDirectoryRecursively(tmpFolderToZip);
    }

    private void ensureJacocoCoverageIfMissing(boolean jacocoFound, String classUTName,
                                              Path toSrcPath, Path toTestPath, Path toCoveragePath, Path volumeBasePath) throws IOException {
        if (jacocoFound) return;

        Path tmpFolderToZip = Paths.get(String.format("%s/%s/tmp_zip", volumeBasePath, classUTName));

        Files.createDirectories(Paths.get(String.format("%s/%s", tmpFolderToZip, Paths.get(BASE_SRC_PATH))));
        fileStorageService.copyDirectoryRecursively(toSrcPath, Paths.get(String.format("%s/%s", tmpFolderToZip, Paths.get(BASE_SRC_PATH))));

        Files.createDirectories(Paths.get(String.format("%s/%s", tmpFolderToZip, Paths.get(BASE_TEST_PATH))));
        fileStorageService.copyDirectoryRecursively(toTestPath, Paths.get(String.format("%s/%s", tmpFolderToZip, Paths.get(BASE_TEST_PATH))));

        fileStorageService.zipDirectory(String.format("%s/src", tmpFolderToZip), String.format("%s/src.zip", tmpFolderToZip));
        File zip = new File(String.format("%s/src.zip", tmpFolderToZip));

                if (!zip.exists()) {
                    logger.error("Jacoco: zip was not created at {}", zip.getAbsolutePath());
                    fileStorageService.deleteDirectoryRecursively(tmpFolderToZip);
                } else {
                    logger.info("Calling Jacoco coverage generation for class {} with zip {}", classUTName, zip.getAbsolutePath());
                    JacocoCoverageDTO coverageDTO = coverageService.generateMissingJacocoCoverage(classUTName, zip);
                    fileStorageService.writeStringToFile(coverageDTO.getCoverage(), new File(toCoveragePath.toFile(), JACOCO_COVERAGE_FILE));
                }

                Files.delete(zip.toPath());
                fileStorageService.deleteDirectoryRecursively(tmpFolderToZip);
    }

    private void computeScoresAndPersist(String classUTName, String robotType, File levelFolder, Path toCoveragePath) throws IOException {
        Path evosuitePath = Paths.get(String.format("%s/%s", toCoveragePath, EVOSUITE_COVERAGE_FILE));
        Path jacocoPath = Paths.get(String.format("%s/%s", toCoveragePath, JACOCO_COVERAGE_FILE));

        int[][] evoSuiteStatistics = new int[8][2];
        int[][] jacocoStatistics = new int[3][2];
        String coverage = null;

        // Behavior by robot type: only warn for the expected coverage file.
        if ("evosuite".equalsIgnoreCase(robotType)) {
            if (Files.exists(evosuitePath)) {
                String evosuiteFileContent = Files.readString(evosuitePath);
                evoSuiteStatistics = ExtractScore.fromEvosuite(evosuiteFileContent);
                logger.info("Evosuite Coverage: {}", Arrays.deepToString(evoSuiteStatistics));
            } else {
                logger.warn("Evosuite coverage file not found at {}", evosuitePath);
                // evoSuiteStatistics remain zeroed
            }

            if (Files.exists(jacocoPath)) {
                coverage = Files.readString(jacocoPath);
                jacocoStatistics = ExtractScore.fromJacoco(coverage);
                logger.info("Jacoco Coverage (also present): {}", Arrays.deepToString(jacocoStatistics));
            } // Do not warn about Jacoco missing for Evosuite runs

        } else if ("randoop".equalsIgnoreCase(robotType)) {
            if (Files.exists(jacocoPath)) {
                coverage = Files.readString(jacocoPath);
                jacocoStatistics = ExtractScore.fromJacoco(coverage);
                logger.info("Jacoco Coverage: {}", Arrays.deepToString(jacocoStatistics));
            } else {
                logger.warn("Jacoco coverage file not found at {}", jacocoPath);
                // jacocoStatistics remain zeroed
            }

            if (Files.exists(evosuitePath)) {
                String evosuiteFileContent = Files.readString(evosuitePath);
                evoSuiteStatistics = ExtractScore.fromEvosuite(evosuiteFileContent);
                logger.info("Evosuite Coverage (also present): {}", Arrays.deepToString(evoSuiteStatistics));
            } // Do not warn about Evosuite missing for Randoop runs

        } else {
            // Unknown robot type: attempt both and warn if missing
            if (Files.exists(evosuitePath)) {
                String evosuiteFileContent = Files.readString(evosuitePath);
                evoSuiteStatistics = ExtractScore.fromEvosuite(evosuiteFileContent);
                logger.info("Evosuite Coverage: {}", Arrays.deepToString(evoSuiteStatistics));
            } else {
                logger.warn("Evosuite coverage file not found at {}", evosuitePath);
            }

            if (Files.exists(jacocoPath)) {
                coverage = Files.readString(jacocoPath);
                jacocoStatistics = ExtractScore.fromJacoco(coverage);
                logger.info("Jacoco Coverage: {}", Arrays.deepToString(jacocoStatistics));
            } else {
                logger.warn("Jacoco coverage file not found at {}", jacocoPath);
            }
        }

        int levelInt = Integer.parseInt(levelFolder.toString().substring(levelFolder.toString().length() - 7, levelFolder.toString().length() - 5));

        OpponentDifficulty difficulty = OpponentDifficulty.values()[levelInt - 1];

        JacocoScore jacocoScore = new JacocoScore();
        jacocoScore.setLineCoverage(new Coverage(jacocoStatistics[0][0], jacocoStatistics[0][1]));
        jacocoScore.setBranchCoverage(new Coverage(jacocoStatistics[1][0], jacocoStatistics[1][1]));
        jacocoScore.setInstructionCoverage(new Coverage(jacocoStatistics[2][0], jacocoStatistics[2][1]));

        EvosuiteScore evosuiteScore = new EvosuiteScore();
        evosuiteScore.setLineCoverage(new Coverage(evoSuiteStatistics[0][0], evoSuiteStatistics[0][1]));
        evosuiteScore.setBranchCoverage(new Coverage(evoSuiteStatistics[1][0], evoSuiteStatistics[1][1]));
        evosuiteScore.setExceptionCoverage(new Coverage(evoSuiteStatistics[2][0], evoSuiteStatistics[2][1]));
        evosuiteScore.setWeakMutationCoverage(new Coverage(evoSuiteStatistics[3][0], evoSuiteStatistics[3][1]));
        evosuiteScore.setOutputCoverage(new Coverage(evoSuiteStatistics[4][0], evoSuiteStatistics[4][1]));
        evosuiteScore.setMethodCoverage(new Coverage(evoSuiteStatistics[5][0], evoSuiteStatistics[5][1]));
        evosuiteScore.setMethodNoExceptionCoverage(new Coverage(evoSuiteStatistics[6][0], evoSuiteStatistics[6][1]));
        evosuiteScore.setCBranchCoverage(new Coverage(evoSuiteStatistics[7][0], evoSuiteStatistics[7][1]));

        Opponent opponent = new Opponent();
        opponent.setClassUT(classUTName);
        opponent.setOpponentType(robotType);
        opponent.setOpponentDifficulty(difficulty);
        opponent.setCoverage(coverage);
        opponent.setEvosuiteScore(evosuiteScore);
        opponent.setJacocoScore(jacocoScore);

        opponentPersistenceService.saveOpponentAndNotify(opponent);
    }

}