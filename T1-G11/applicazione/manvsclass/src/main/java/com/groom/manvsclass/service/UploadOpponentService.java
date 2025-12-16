package com.groom.manvsclass.service;

import com.groom.manvsclass.api.ApiGatewayClient;
import com.groom.manvsclass.model.Opponent;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.repository.OpponentRepository;
import com.groom.manvsclass.repository.ClassUTRepository;
import com.groom.manvsclass.util.filesystem.FileOperationUtil;
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
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import testrobotchallenge.commons.models.score.Coverage;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;
import testrobotchallenge.commons.util.ExtractScore;
import org.springframework.beans.factory.annotation.Autowired;

import com.groom.manvsclass.exception.NotFoundException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private final Logger logger = LoggerFactory.getLogger(UploadOpponentService.class);

    @Autowired
    private ApiGatewayClient apiGatewayClient;
    @Autowired
    private OpponentRepository opponentRepository;
    @Autowired
    private ClassUTRepository classUTRepository;


    public void saveOpponentsFromZip(String classUTFileName, String classUTName, MultipartFile classUTFile, MultipartFile robotTestsZip) throws IOException {
        Path operationTmpFolder = Paths.get(VOLUME_T0_BASE_PATH).resolve(classUTName).resolve("tmp");
        logger.info("Starting saveOpponentsFromZip: class={}, tmpFolder={}", classUTName, operationTmpFolder.toAbsolutePath());

        try {
            FileOperationUtil.saveFileInFileSystem("robot.zip", operationTmpFolder, robotTestsZip);
            logger.debug("Saved robot zip in {}", operationTmpFolder.resolve("robot.zip").toAbsolutePath());

            FileOperationUtil.extractZipIn(operationTmpFolder);
            logger.debug("Extracted zip in {}. Listing contents: {}",
                    operationTmpFolder.toAbsolutePath(), listDirectory(operationTmpFolder));

            Path unmodifiedSrcCodePath = Paths.get(VOLUME_T0_BASE_PATH).resolve(UNMODIFIED_SRC).resolve(classUTName);
            logger.info("Saving unmodified src in {}", unmodifiedSrcCodePath.toAbsolutePath());
            FileOperationUtil.saveFileInFileSystem(classUTFileName, unmodifiedSrcCodePath, classUTFile);

            File[] rootFiles = operationTmpFolder.toFile().listFiles();
            if (rootFiles == null || rootFiles.length == 0) {
                logger.warn("No files found inside tmp folder {}", operationTmpFolder);
                return;
            }

            File robotGroupFolder = rootFiles[0];
            logger.info("Robot tests folder {}", robotGroupFolder.getAbsolutePath());

            File[] robotFolders = robotGroupFolder.listFiles();
            if (robotFolders == null) {
                logger.warn("Robot group folder {} is empty or inaccessible", robotGroupFolder.getAbsolutePath());
                return;
            }

            for (File robotFolder : robotFolders) {
                if (!robotFolder.isDirectory()) {
                    logger.info("Ignoring file {} because it is not a directory", robotFolder.getAbsolutePath());
                    continue;
                }

                String robotType = robotFolder.getName();
                if (!robotType.endsWith("Test")) {
                    logger.info("Ignoring directory {} because it does not follow the naming convention", robotFolder.getAbsolutePath());
                    continue;
                }
                robotType = robotType.substring(0, robotType.length() - 4);
                robotType = Character.toUpperCase(robotType.charAt(0)) + robotType.substring(1);

                logger.info("Robot folder {} (type={})", robotFolder.getAbsolutePath(), robotType);

                try {
                    uploadNewOpponents(classUTFileName, classUTName, classUTFile, robotFolder.toPath(), robotType, Paths.get(VOLUME_T0_BASE_PATH));
                } catch (NotFoundException e) {
                    logger.warn("NotFoundException while processing robot folder {}: {}", robotFolder.getAbsolutePath(), e.getMessage());
                    // continue with next robot folder
                } catch (Exception e) {
                    logger.error("Unexpected exception while processing robot folder {}", robotFolder.getAbsolutePath(), e);
                }
            }
        } finally {
            try {
                FileOperationUtil.deleteDirectoryRecursively(operationTmpFolder);
                logger.debug("Deleted tmp folder {}", operationTmpFolder.toAbsolutePath());
            } catch (Exception e) {
                logger.warn("Failed to delete tmp folder {}: {}", operationTmpFolder, e.getMessage());
            }
        }
    }

    private String listDirectory(Path dir) {
        try {
            File f = dir.toFile();
            String[] list = f.list();
            return (list == null) ? "[]" : Arrays.toString(list);
        } catch (Exception e) {
            return "(error listing)" + e.getMessage();
        }
    }

    private int[] getEmmaCoverageByCoverageType(String path, String coverageType) {
        logger.debug("getEmmaCoverageByCoverageType - parsing {} for type={}", path, coverageType);
        try {
            File cov = new File(path);
            if (!cov.exists()) {
                throw new IllegalArgumentException("Coverage file does not exist: " + path);
            }
            Document doc = Jsoup.parse(cov, null, "", Parser.xmlParser());

            Element stat = doc.selectFirst("coverage[type=\"" + coverageType + "\"]");

            if (stat == null) {
                throw new IllegalArgumentException("Nessuna riga trovata per il tipo di coverage: " + coverageType);
            }

            String value = stat.attr("value");
            logger.debug("Found coverage value attr='{}'", value);
            Pattern pattern = Pattern.compile("\\((\\d+(?:\\.\\d+)?)/(\\d+(?:\\.\\d+)?)\\)");
            Matcher matcher = pattern.matcher(value);

            if (!matcher.find()) {
                throw new IllegalArgumentException("Formato valore non valido: " + value);
            }

            int covered = (int) Math.round(Double.parseDouble(matcher.group(1)));
            int total = (int) Math.round(Double.parseDouble(matcher.group(2)));

            if (total < covered) {
                logger.warn("Parsed total < covered for {}: total={}, covered={} - will clamp total to covered", path, total, covered);
                total = covered;
            }

            logger.debug("Coverage parsed: covered={}, total={}", covered, total);
            return new int[]{covered, total};
        } catch (IOException e) {
            logger.error("Errore nella lettura del file XML {}", path, e);
            throw new RuntimeException("Errore nella lettura del file XML.", e);
        } catch (NumberFormatException e) {
            logger.error("NumberFormatException parsing coverage value in {}", path, e);
            throw new IllegalArgumentException("Gli attributi 'covered' e 'total' devono essere numeri interi validi.", e);
        } catch (Exception e) {
            logger.error("Errore durante l'elaborazione del documento XML {}", path, e);
            throw new RuntimeException("Errore durante l'elaborazione del documento XML.", e);
        }
    }


    private String[] extractTestPackageNameFromCode(String code) {
        Pattern pattern = Pattern.compile("\\bpackage\\s*([a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*)\\s*;", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(code);

        if (matcher.find()) {
            String packageName = matcher.group(1).trim();
            logger.debug("extractTestPackageNameFromCode -> {}", packageName);
            return packageName.split("\\.");
        }

        logger.debug("extractTestPackageNameFromCode -> none found");
        return null; // Nessun package trovato
    }

    private String[] extractSrcPackageFromCode(String code, String className, String robotType) {
        logger.debug("extractSrcPackageFromCode for class={} robotType={}", className, robotType);
        Pattern pattern;
        Matcher matcher;

        switch (robotType) {
            case "Evosuite":
                String regex = "org\\.evosuite\\.runtime\\.RuntimeSettings\\.className\\s*=\\s*\"([\\w.]+)\\." + className + "\"";
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(code);

                if (matcher.find()) {
                    String packageName = matcher.group(1);
                    logger.debug("Evosuite package extracted -> {}", packageName);
                    return packageName.split("\\.");
                }

                logger.debug("Evosuite package not found in code");
                return null;
            case "Randoop":
            default:
                pattern = Pattern.compile("\\bimport\\s+([a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*)\\." + className + "\\s*;");
                matcher = pattern.matcher(code);

                if (matcher.find()) {
                    String packageName = matcher.group(1);
                    logger.debug("Import package extracted -> {}", packageName);
                    return packageName.split("\\.");
                }

                logger.debug("Import/package not found in code");
                return null;
        }
    }

    private void modifyAndSaveSrcFile(String fileName, Path directory, MultipartFile originalFile, String edit) throws IOException {
        logger.debug("modifyAndSaveSrcFile - fileName={}, dir={}, package={}", fileName, directory.toAbsolutePath(), edit);
        String content = new String(originalFile.getBytes());
        String modifiedContent = "package " + edit + ";\n" + content;

        Files.createDirectories(directory);
        Path filePath = directory.resolve(fileName);
        File outputFile = filePath.toFile();
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(modifiedContent);
        }
        logger.info("Wrote modified src file {} ({} bytes)", filePath.toAbsolutePath(), Files.size(filePath));
    }


    private String[][] saveTestFilesInVolume(Path fromTestPath, Path toTestPath, String className, String robotType) throws IOException {
        logger.info("saveTestFilesInVolume from {} to {} (class={}, robot={})",
                fromTestPath.toAbsolutePath(), toTestPath.toAbsolutePath(), className, robotType);

        String[] testPackageName = null;
        String[] srcPackageName = null;

        File[] files = fromTestPath.toFile().listFiles();
        if (files == null) {
            logger.warn("No files found in fromTestPath {}", fromTestPath.toAbsolutePath());
            return new String[][]{null, null};
        }

        for (File src : files) {
            if (!src.getName().endsWith(".java"))
                continue;

            String content;
            try (Stream<String> lines = Files.lines(src.toPath())) {
                content = lines.collect(Collectors.joining(System.lineSeparator()));
            }

            testPackageName = extractTestPackageNameFromCode(content);
            if (srcPackageName == null) {
                srcPackageName = extractSrcPackageFromCode(content, className, robotType);
            }

            String testPackagePath = (testPackageName != null) ? String.join("/", testPackageName) : "";
            Path destDir = toTestPath.resolve(testPackagePath).normalize();
            Files.createDirectories(destDir);

            Path dest = destDir.resolve(src.getName());
            try {
                Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                logger.info("Copied test file {} -> {} ({} bytes)", src.getAbsolutePath(), dest.toAbsolutePath(), Files.size(dest));
            } catch (IOException e) {
                logger.error("Failed to copy test file {} to {}", src.getAbsolutePath(), dest.toAbsolutePath(), e);
                throw e;
            }
        }

        logger.debug("Finished saving tests. srcPackageName={}, testPackageName={}", Arrays.toString(srcPackageName), Arrays.toString(testPackageName));
        return new String[][]{srcPackageName, testPackageName};
    }

    private void saveSrcFileInVolume(MultipartFile src, Path srcPath, String[] srcPackageName, String srcFileName) throws IOException {
        String srcPackagePath = (srcPackageName != null) ? String.join("/", srcPackageName) : "";
        Path destDir = srcPath.resolve(srcPackagePath).normalize();
        Files.createDirectories(destDir);

        if (srcPackageName != null) {
            String srcPackageCodeLine = String.join(".", srcPackageName);
            modifyAndSaveSrcFile(srcFileName, destDir, src, srcPackageCodeLine);
        } else {
            FileOperationUtil.saveFileInFileSystem(srcFileName, destDir, src);
            logger.info("Saved src file {} in {}", srcFileName, destDir.toAbsolutePath());
        }
    }

    private boolean[] saveCoverageFilesInVolume(Path searchIn, Path coveragePath) throws IOException, NullPointerException {
        boolean jacocoFound = false;
        boolean evosuiteFound = false;

        logger.info("Searching coverage files in {} (will copy to {})", searchIn.toAbsolutePath(), coveragePath.toAbsolutePath());

        if (!Files.exists(searchIn)) {
            logger.warn("Coverage search directory does not exist: {}", searchIn.toAbsolutePath());
            return new boolean[]{jacocoFound, evosuiteFound};
        }

        File[] files = searchIn.toFile().listFiles();
        if (files == null) {
            logger.warn("No files in coverage search directory: {}", searchIn.toAbsolutePath());
            return new boolean[]{jacocoFound, evosuiteFound};
        }

        Files.createDirectories(coveragePath);

        for (File coverageFile : files) {
            Path dest = coveragePath.resolve(coverageFile.getName());
            try {
                if (coverageFile.getName().equals(JACOCO_COVERAGE_FILE)) {
                    String coverage = Files.lines(coverageFile.toPath()).collect(Collectors.joining(System.lineSeparator()));
                    if (coverage.contains("<coverage type=\"line, %\" value=")) {
                        logger.debug("Skipping jacoco file {} because it contains placeholder", coverageFile.getAbsolutePath());
                        continue;
                    }

                    Files.copy(coverageFile.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                    jacocoFound = true;
                    logger.info("Copied Jacoco coverage {} -> {}", coverageFile.getAbsolutePath(), dest.toAbsolutePath());
                }

                if (coverageFile.getName().equals(EVOSUITE_COVERAGE_FILE)) {
                    Files.copy(coverageFile.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                    evosuiteFound = true;
                    logger.info("Copied Evosuite coverage {} -> {}", coverageFile.getAbsolutePath(), dest.toAbsolutePath());
                }
            } catch (IOException e) {
                logger.error("Failed to copy coverage file {} to {}", coverageFile.getAbsolutePath(), dest.toAbsolutePath(), e);
                throw e;
            }
        }

        logger.debug("Coverage found flags: jacoco={}, evosuite={}", jacocoFound, evosuiteFound);
        return new boolean[]{jacocoFound, evosuiteFound};
    }

    private void uploadNewOpponents(
            String classUTFileName,
            String classUTName,
            MultipartFile classUTFile,
            Path operationTmpFolder,
            String robotType,
            Path volumeBasePath
    ) throws IOException {

        logger.info("uploadNewOpponents start: class={}, robotType={}, operationTmpFolder={}", classUTName, robotType, operationTmpFolder.toAbsolutePath());

        File[] levelFolders = operationTmpFolder.toFile().listFiles();
        if (levelFolders == null) {
            logger.warn("No level folders found in {}", operationTmpFolder.toAbsolutePath());
            return;
        }

        for (File levelFolder : levelFolders) {

            if (!levelFolder.isDirectory()) {
                logger.info("Ignoring file {} because it is not a directory", levelFolder.getName());
                continue;
            }

            String levelFolderName = levelFolder.getName();
            if (!levelFolderName.matches("\\d{2,}Level")) {
                logger.info("Ignoring folder {} because it is not a level", levelFolderName);
                continue;
            }

            Matcher matcher = Pattern.compile("^(\\d{2,})Level$").matcher(levelFolderName);
            if (!matcher.find()) {
                logger.warn("Could not parse level number from {}", levelFolderName);
                continue;
            }
            int levelInt = Integer.parseInt(matcher.group(1));

            logger.info("Saving level {} (num={})", levelFolderName, levelInt);

            Path toSrcPath = volumeBasePath.resolve(classUTName).resolve(robotType).resolve(BASE_CODE_PATH).resolve(levelFolderName).resolve(BASE_SRC_PATH).normalize();
            Path toTestPath = volumeBasePath.resolve(classUTName).resolve(robotType).resolve(BASE_CODE_PATH).resolve(levelFolderName).resolve(BASE_TEST_PATH).normalize();
            Path toCoveragePath = volumeBasePath.resolve(classUTName).resolve(robotType).resolve(BASE_COVERAGE_PATH).resolve(levelFolderName).normalize();

            logger.debug("Save SRC path {}", toSrcPath.toAbsolutePath());
            logger.debug("Save TESTS path {}", toTestPath.toAbsolutePath());
            logger.debug("Save COVERAGE path {}", toCoveragePath.toAbsolutePath());

            Path fromTestPath;
            Path fromCoveragePath;

            switch (robotType.toLowerCase()) {
                case "evosuite":
                    fromTestPath = levelFolder.toPath().resolve("TestSourceCode").resolve("evosuite-tests");
                    fromCoveragePath = levelFolder.toPath().resolve("TestReport");
                    break;

                default:
                    fromTestPath = levelFolder.toPath();
                    fromCoveragePath = levelFolder.toPath();
                    break;
            }

            logger.debug("Robot TESTS path {}", fromTestPath.toAbsolutePath());
            logger.debug("Robot COVERAGE path {}", fromCoveragePath.toAbsolutePath());

            if (!Files.exists(fromTestPath)) {
                logger.info("Skipping folder {} because it does not exist", fromTestPath.toAbsolutePath());
                continue;
            }

            File[] testFiles = fromTestPath.toFile().listFiles();
            if (testFiles == null || testFiles.length == 0) {
                logger.info("Skipping folder {} because it does not have any files", fromTestPath.toAbsolutePath());
                continue;
            }

            if (Arrays.stream(testFiles).noneMatch(f -> f.getName().endsWith(".java"))) {
                logger.info("Skipping folder {} because it does not contain any .java files", fromTestPath.toAbsolutePath());
                continue;
            }

            String[][] splitPackageNames = saveTestFilesInVolume(fromTestPath, toTestPath, classUTName, robotType);

            String[] srcPackageNameSplit = splitPackageNames[0];
            saveSrcFileInVolume(classUTFile, toSrcPath, srcPackageNameSplit, classUTFileName);

            logger.info("SRC package names split {}", Arrays.toString(srcPackageNameSplit));
            logger.info("TEST package names split {}", Arrays.toString(splitPackageNames[1]));

            boolean[] coverageFound = saveCoverageFilesInVolume(fromCoveragePath, toCoveragePath);

            if (!coverageFound[1]) {

                Path tmpFolder_ToZip = volumeBasePath.resolve(classUTName).resolve("tmp_zip");
                logger.debug("Preparing tmp zip folder {} for evosuite coverage generation", tmpFolder_ToZip.toAbsolutePath());

                Files.createDirectories(tmpFolder_ToZip.resolve("src"));
                FileOperationUtil.copyDirectoryRecursively(toSrcPath, tmpFolder_ToZip.resolve("src").resolve(BASE_SRC_PATH));

                Files.createDirectories(tmpFolder_ToZip.resolve("test"));
                FileOperationUtil.copyDirectoryRecursively(toTestPath, tmpFolder_ToZip.resolve("test").resolve(BASE_TEST_PATH));

                Path zipPath = tmpFolder_ToZip.resolve("src.zip");
                FileOperationUtil.zipDirectory(tmpFolder_ToZip.resolve("src").toString(), zipPath.toString());
                File zip = zipPath.toFile();

                String srcPackage = (srcPackageNameSplit == null) ? "" : String.join(".", srcPackageNameSplit) + ".";

                if (!zip.exists()) {
                    logger.error("Errore: Il file ZIP non è stato creato correttamente. tmpFolder={}", tmpFolder_ToZip.toAbsolutePath());
                    FileOperationUtil.deleteDirectoryRecursively(tmpFolder_ToZip);
                } else {
                    try {
                        logger.info("Calling API to generate missing Evosuite coverage for class={} zip={}", classUTName, zip.getAbsolutePath());
                        EvosuiteCoverageDTO coverageDTO = apiGatewayClient.callGenerateMissingEvoSuiteCoverage(classUTName, srcPackage, zip);
                        logger.debug("Evosuite API responded: resultContentSize={}",
                                coverageDTO == null ? null : (coverageDTO.getResultFileContent() == null ? 0 : coverageDTO.getResultFileContent().length()));

                        FileOperationUtil.writeStringToFile(
                                coverageDTO.getResultFileContent(),
                                new File(toCoveragePath.resolve(EVOSUITE_COVERAGE_FILE).toString())
                        );
                        logger.info("Wrote generated evosuite statistics to {}", toCoveragePath.resolve(EVOSUITE_COVERAGE_FILE).toAbsolutePath());
                    } catch (Exception e) {
                        logger.error("Evosuite coverage generation failed for class {}", classUTName, e);
                        FileOperationUtil.deleteDirectoryRecursively(tmpFolder_ToZip);
                        throw e;
                    }
                }

                try {
                    Files.deleteIfExists(zip.toPath());
                } catch (Exception e) {
                    logger.warn("Could not delete zip {}: {}", zip.getAbsolutePath(), e.getMessage());
                }
                FileOperationUtil.deleteDirectoryRecursively(tmpFolder_ToZip);
            }

            if (!coverageFound[0]) {

                Path tmpFolder_ToZip = volumeBasePath.resolve(classUTName).resolve("tmp_zip");
                logger.debug("Preparing tmp zip folder {} for jacoco coverage generation", tmpFolder_ToZip.toAbsolutePath());

                Files.createDirectories(tmpFolder_ToZip.resolve("src"));
                FileOperationUtil.copyDirectoryRecursively(toSrcPath, tmpFolder_ToZip.resolve("src").resolve(BASE_SRC_PATH));

                Files.createDirectories(tmpFolder_ToZip.resolve("test"));
                FileOperationUtil.copyDirectoryRecursively(toTestPath, tmpFolder_ToZip.resolve("test").resolve(BASE_TEST_PATH));

                Path zipPath = tmpFolder_ToZip.resolve("src.zip");
                FileOperationUtil.zipDirectory(tmpFolder_ToZip.resolve("src").toString(), zipPath.toString());
                File zip = zipPath.toFile();

                if (!zip.exists()) {
                    logger.error("Errore: Il file ZIP non è stato creato correttamente. tmpFolder={}", tmpFolder_ToZip.toAbsolutePath());
                    FileOperationUtil.deleteDirectoryRecursively(tmpFolder_ToZip);
                } else {
                    try {
                        logger.info("Calling API to generate missing Jacoco coverage zip={}", zip.getAbsolutePath());
                        JacocoCoverageDTO coverageDTO = apiGatewayClient.callGenerateMissingJacocoCoverage(zip);
                        logger.debug("Jacoco API responded: coverageSize={}",
                                coverageDTO == null ? null : (coverageDTO.getCoverage() == null ? 0 : coverageDTO.getCoverage().length()));

                        FileOperationUtil.writeStringToFile(
                                coverageDTO.getCoverage(),
                                new File(toCoveragePath.resolve(JACOCO_COVERAGE_FILE).toString())
                        );
                        logger.info("Wrote generated jacoco coverage to {}", toCoveragePath.resolve(JACOCO_COVERAGE_FILE).toAbsolutePath());
                    } catch (Exception e) {
                        logger.error("Jacoco coverage generation failed for class {}", classUTName, e);
                        FileOperationUtil.deleteDirectoryRecursively(tmpFolder_ToZip);
                        throw e;
                    }
                }

                try {
                    Files.deleteIfExists(zip.toPath());
                } catch (Exception e) {
                    logger.warn("Could not delete zip {}: {}", zip.getAbsolutePath(), e.getMessage());
                }
                FileOperationUtil.deleteDirectoryRecursively(tmpFolder_ToZip);
            }

            // Read and parse evosuite and jacoco coverage files
            Path evosuiteFile = toCoveragePath.resolve(EVOSUITE_COVERAGE_FILE);
            Path jacocoFile = toCoveragePath.resolve(JACOCO_COVERAGE_FILE);

            if (!Files.exists(evosuiteFile)) {
                logger.error("Missing evosuite coverage file: {}. Directory listing: {}", evosuiteFile.toAbsolutePath(), listDirectory(toCoveragePath));
                throw new IllegalStateException("Missing evosuite coverage file for " + levelFolderName);
            }
            if (!Files.exists(jacocoFile)) {
                logger.error("Missing jacoco coverage file: {}. Directory listing: {}", jacocoFile.toAbsolutePath(), listDirectory(toCoveragePath));
                throw new IllegalStateException("Missing jacoco coverage file for " + levelFolderName);
            }

            String evosuiteFileContent = Files.readString(evosuiteFile);
            int[][] evoSuiteStatistics = ExtractScore.fromEvosuite(evosuiteFileContent);
            logger.info("Evosuite Coverage: {}", Arrays.deepToString(evoSuiteStatistics));

            String coverage = Files.readString(jacocoFile);
            int[][] jacocoStatistics = ExtractScore.fromJacoco(coverage);
            logger.info("Jacoco Coverage: {}", Arrays.deepToString(jacocoStatistics));

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

            ClassUT classUT = classUTRepository.findById(classUTName)
                    .orElseThrow(() -> new NotFoundException("Classe " + classUTName + " non trovata."));

            Opponent opponent = new Opponent();
            opponent.setClassUT(classUT);
            opponent.setType(robotType);
            // opponent.setOpponentDifficulty(difficulty);
            opponent.setCoverage(coverage);
            opponent.setEvosuiteScore(evosuiteScore);
            opponent.setJacocoScore(jacocoScore);

            opponentRepository.save(opponent);
            logger.info("Saved Opponent: class={}, robot={}, level={}, id={}", classUTName, robotType, levelFolderName, opponent.getId());

            for (GameMode gameMode : GameMode.values()) {
                try {
                    logger.debug("Calling addNewOpponent for gameMode={} robot={} level={}", gameMode, robotType, levelFolderName);
                    apiGatewayClient.callAddNewOpponent(classUT.getName(), gameMode, robotType, difficulty);
                } catch (Exception e) {
                    logger.error("Failed to call addNewOpponent for gameMode={} robot={} level={}", gameMode, robotType, levelFolderName, e);
                }
            }
        }

        FileOperationUtil.deleteDirectoryRecursively(operationTmpFolder);
        logger.debug("Deleted operation tmp folder {}", operationTmpFolder.toAbsolutePath());
    }

}
