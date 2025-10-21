package com.groom.manvsclass.service;

import com.groom.manvsclass.api.ApiGatewayClient;
import com.groom.manvsclass.model.Opponent;
import com.groom.manvsclass.model.repository.OpponentRepository;
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

    private final Logger logger = LoggerFactory.getLogger(UploadOpponentService.class);

    private final ApiGatewayClient apiGatewayClient;
    private final OpponentRepository opponentRepository;

    public UploadOpponentService(ApiGatewayClient apiGatewayClient, OpponentRepository opponentRepository) {
        this.apiGatewayClient = apiGatewayClient;
        this.opponentRepository = opponentRepository;
    }

    /*
     *
     */
    public void saveOpponentsFromZip(String classUTFileName, String classUTName, MultipartFile classUTFile, MultipartFile robotTestsZip) throws IOException {
        Path operationTmpFolder = Paths.get(String.format("%s/%s/tmp", VOLUME_T0_BASE_PATH, classUTName));
        FileOperationUtil.saveFileInFileSystem("robot.zip", operationTmpFolder, robotTestsZip);
        FileOperationUtil.extractZipIn(operationTmpFolder);

        Path unmodifiedSrcCodePath = Paths.get(String.format("%s/%s/%s", VOLUME_T0_BASE_PATH, UNMODIFIED_SRC, classUTName));
        logger.info("Saving unmodified src in {}", unmodifiedSrcCodePath);
        FileOperationUtil.saveFileInFileSystem(classUTFileName, unmodifiedSrcCodePath, classUTFile);

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

        FileOperationUtil.deleteDirectoryRecursively(operationTmpFolder);
    }

    private int[] getEmmaCoverageByCoverageType(String path, String coverageType) {
        try {
            File cov = new File(path);
            Document doc = Jsoup.parse(cov, null, "", Parser.xmlParser());

            // Seleziona solo il primo elemento che corrisponde al tipo di coverage richiesto
            Element stat = doc.selectFirst("coverage[type=\"" + coverageType + "\"]");

            if (stat == null) {
                throw new IllegalArgumentException("Nessuna riga trovata per il tipo di coverage: " + coverageType);
            }

            String value = stat.attr("value");
            Pattern pattern = Pattern.compile("\\((\\d+(?:\\.\\d+)?)/(\\d+(?:\\.\\d+)?)\\)"); //Patter per catturare interi e decimali
            Matcher matcher = pattern.matcher(value);

            if (!matcher.find()) {
                throw new IllegalArgumentException("Formato valore non valido: " + value);
            }

            int covered = (int) Double.parseDouble(matcher.group(1));
            int total = (int) (Double.parseDouble(matcher.group(2)) - Double.parseDouble(matcher.group(1)));

            return new int[]{covered, total};
        } catch (IOException e) {
            throw new RuntimeException("Errore nella lettura del file XML.", e);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Gli attributi 'covered' e 'total' devono essere numeri interi validi.", e);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'elaborazione del documento XML.", e);
        }
    }


    private String[] extractTestPackageNameFromCode(String code) {
        Pattern pattern = Pattern.compile("\\bpackage\\s*([a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*)\\s*;", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(code);

        if (matcher.find()) {
            String packageName = matcher.group(1).trim();
            return packageName.split("\\.");
        }

        return null; // Nessun package trovato
    }

    private String[] extractSrcPackageFromCode(String code, String className, String robotType) {
        Pattern pattern;
        Matcher matcher;

        switch (robotType) {
            case "Evosuite":
                // Costruisce il pattern per cercare il package della classe testata
                String regex = "org\\.evosuite\\.runtime\\.RuntimeSettings\\.className\\s*=\\s*\"([\\w.]+)\\." + className + "\"";
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(code);

                if (matcher.find()) {
                    String packageName = matcher.group(1); // Estrae solo il package senza la classe
                    return packageName.split("\\."); // Divide il package in array di stringhe
                }

                return null; // Nessun package trovato
            case "Randoop":
            default:
                pattern = Pattern.compile("\\bimport\\s+([a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*)\\." + className + "\\s*;");
                matcher = pattern.matcher(code);

                if (matcher.find()) {
                    String packageName = matcher.group(1);
                    return packageName.split("\\.");
                }

                return null; // Nessun package trovato
        }
    }

    private void modifyAndSaveSrcFile(String fileName, Path directory, MultipartFile originalFile, String edit) throws IOException {
        // Leggi il contenuto come stringa
        String content = new String(originalFile.getBytes());

        // Aggiungi il package all'inizio
        String modifiedContent = "package " + edit + ";\n" + content;

        // Salva il file modificato
        Path filePath = directory.resolve(fileName);
        File outputFile = filePath.toFile();
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(modifiedContent);
        }
    }


    private String[][] saveTestFilesInVolume(Path fromTestPath, Path toTestPath, String className, String robotType) throws IOException {
        String[] testPackageName = null;
        String[] srcPackageName = null;

        for (File src : Objects.requireNonNull(Objects.requireNonNull(fromTestPath.toFile()).listFiles())) {
            if (!src.getName().contains(".java"))
                continue;

            String content = Files.lines(src.toPath()).collect(Collectors.joining(System.lineSeparator()));

            testPackageName = extractTestPackageNameFromCode(content);
            if (srcPackageName == null) {
                srcPackageName = extractSrcPackageFromCode(content, className, robotType);
            }

            String testPackagePath = "";
            if (testPackageName != null) {
                testPackagePath = String.join("/", testPackageName);
            }

            Files.createDirectories(Paths.get(String.format("%s/%s", toTestPath, testPackagePath)).normalize());
            Files.copy(src.toPath(), Paths.get(String.format("%s/%s/%s", toTestPath, testPackagePath, src.getName())).normalize());
        }

        return new String[][]{srcPackageName, testPackageName};
    }

    private void saveSrcFileInVolume(MultipartFile src, Path srcPath, String[] srcPackageName, String srcFileName) throws IOException {
        String srcPackagePath = "";

        if (srcPackageName != null) {
            srcPackagePath = String.join("/", srcPackageName);
            String srcPackageCodeLine = String.join(".", srcPackagePath);
            Files.createDirectories(Paths.get(String.format("%s/%s", srcPath, srcPackagePath)));
            modifyAndSaveSrcFile(srcFileName, Paths.get(String.format("%s/%s", srcPath, srcPackagePath)), src, srcPackageCodeLine);
        } else {
            Files.createDirectories(Paths.get(String.format("%s/%s", srcPath, srcPackagePath)));
            FileOperationUtil.saveFileInFileSystem(srcFileName, Paths.get(String.format("%s/%s", srcPath, srcPackagePath)), src);
        }
    }

    private boolean[] saveCoverageFilesInVolume(Path searchIn, Path coveragePath) throws IOException, NullPointerException {
        boolean jacocoFound = false;
        boolean evosuiteFound = false;

        if (!Files.exists(searchIn)) {
            return new boolean[]{jacocoFound, evosuiteFound};
        }

        for (File coverageFile : Objects.requireNonNull(searchIn.toFile().listFiles())) {
            Files.createDirectories(Paths.get(String.format("%s", coveragePath)));

            if (coverageFile.getName().equals(JACOCO_COVERAGE_FILE)) {
                String coverage = Files.lines(coverageFile.toPath()).collect(Collectors.joining(System.lineSeparator()));
                if (coverage.contains("<coverage type=\"line, %\" value=")) {
                    continue;
                }

                Files.copy(coverageFile.toPath(), Paths.get(String.format("%s/%s", coveragePath, coverageFile.getName())));
                jacocoFound = true;
            }

            if (coverageFile.getName().equals(EVOSUITE_COVERAGE_FILE)) {
                Files.copy(coverageFile.toPath(), Paths.get(String.format("%s/%s", coveragePath, coverageFile.getName())));
                evosuiteFound = true;
            }
        }

        return new boolean[]{jacocoFound, evosuiteFound};
    }

    private void uploadNewOpponents(String classUTFileName, String classUTName, MultipartFile classUTFile, Path operationTmpFolder, String robotType, Path volumeBasePath) throws IOException {
        for (File levelFolder : Objects.requireNonNull(operationTmpFolder.toFile().listFiles())) {
            if (!levelFolder.isDirectory()) {
                logger.info("Ignoring file " + levelFolder.getName() + " because it is not a directory");
                continue;
            }

            if (!levelFolder.getName().matches("\\d{2,}Level")) {
                logger.info("Ignoring folder " + levelFolder.getName() + " because it is not a level");
            }

            String level = levelFolder.getName();

            logger.info("Saving level " + level);
            Path toSrcPath = Paths.get(String.format("%s/%s/%s/%s/%s/%s", volumeBasePath, classUTName, robotType, BASE_CODE_PATH, level, BASE_SRC_PATH));
            Path toTestPath = Paths.get(String.format("%s/%s/%s/%s/%s/%s", volumeBasePath, classUTName, robotType, BASE_CODE_PATH, level, BASE_TEST_PATH));
            Path toCoveragePath = Paths.get(String.format("%s/%s/%s/%s/%s", volumeBasePath, classUTName, robotType, BASE_COVERAGE_PATH, level));

            logger.info("Save SRC path " + toSrcPath);
            logger.info("Save TESTS path " + toTestPath);
            logger.info("Save COVERAGE path " + toCoveragePath);

            Path fromTestPath;
            Path fromCoveragePath;
            switch (robotType.toLowerCase()) {
                case "evosuite":
                    fromTestPath = Paths.get(String.format("%s/TestSourceCode/evosuite-tests", levelFolder));
                    fromCoveragePath = Paths.get(String.format("%s/%s", levelFolder.getPath(), "TestReport"));
                    break;
                default:
                    fromTestPath = Paths.get(String.format("%s", levelFolder));
                    fromCoveragePath = Paths.get(String.format("%s", levelFolder.getPath()));
                    break;
            }

            logger.info("Robot TESTS path " + fromTestPath);
            logger.info("Robot COVERAGE path " + fromCoveragePath);

            if (!Files.exists(fromTestPath)) {
                logger.info("Skipping folder " + fromTestPath + " because it does not exist");
                continue;
            }

            if (fromTestPath.toFile().listFiles().length == 0) {
                logger.info("Skipping folder " + fromTestPath + " because it does not have any files");
                continue;
            }

            if (Arrays.stream(fromTestPath.toFile().listFiles()).noneMatch(file -> file.getName().endsWith(".java"))) {
                logger.info("Skipping folder " + fromTestPath + " because it does not contain any .java files");
                continue;
            }

            String[][] splitPackageNames = saveTestFilesInVolume(fromTestPath, toTestPath, classUTName, robotType);
            String[] srcPackageNameSplit = splitPackageNames[0];
            saveSrcFileInVolume(classUTFile, toSrcPath, srcPackageNameSplit, classUTFileName);

            logger.info("SRC package names split " + Arrays.toString(srcPackageNameSplit));
            logger.info("TEST package names split " + Arrays.toString(splitPackageNames[1]));

            boolean[] coverageFound = saveCoverageFilesInVolume(fromCoveragePath, toCoveragePath);

            if (!coverageFound[1]) {
                Path tmpFolder_ToZip = Paths.get(String.format("%s/%s/tmp_zip", volumeBasePath, classUTName));

                Files.createDirectories(Paths.get(String.format("%s/%s", tmpFolder_ToZip, Paths.get(BASE_SRC_PATH))));
                FileOperationUtil.copyDirectoryRecursively(toSrcPath, Paths.get(String.format("%s/%s", tmpFolder_ToZip, Paths.get(BASE_SRC_PATH))));

                Files.createDirectories(Paths.get(String.format("%s/%s", tmpFolder_ToZip, Paths.get(BASE_TEST_PATH))));
                FileOperationUtil.copyDirectoryRecursively(toTestPath, Paths.get(String.format("%s/%s", tmpFolder_ToZip, Paths.get(BASE_TEST_PATH))));

                FileOperationUtil.zipDirectory(String.format("%s/src", tmpFolder_ToZip), String.format("%s/src.zip", tmpFolder_ToZip));
                File zip = new File(String.format("%s/src.zip", tmpFolder_ToZip));

                String srcPackage = "";
                if (srcPackageNameSplit != null)
                    srcPackage = String.join(".", srcPackageNameSplit) + ".";

                if (!zip.exists()) {
                    System.err.println("Errore: Il file ZIP non è stato creato correttamente.");
                    FileOperationUtil.deleteDirectoryRecursively(tmpFolder_ToZip);
                } else {
                    EvosuiteCoverageDTO coverageDTO = apiGatewayClient.callGenerateMissingEvoSuiteCoverage(classUTName, srcPackage, zip);
                    FileOperationUtil.writeStringToFile(coverageDTO.getResultFileContent(), new File(String.format("%s/%s", toCoveragePath, "statistics.csv")));
                }

                Files.delete(zip.toPath());
                FileOperationUtil.deleteDirectoryRecursively(tmpFolder_ToZip);
            }

            if (!coverageFound[0]) {
                Path tmpFolder_ToZip = Paths.get(String.format("%s/%s/tmp_zip", volumeBasePath, classUTName));

                Files.createDirectories(Paths.get(String.format("%s/%s", tmpFolder_ToZip, Paths.get(BASE_SRC_PATH))));
                FileOperationUtil.copyDirectoryRecursively(toSrcPath, Paths.get(String.format("%s/%s", tmpFolder_ToZip, Paths.get(BASE_SRC_PATH))));

                Files.createDirectories(Paths.get(String.format("%s/%s", tmpFolder_ToZip, Paths.get(BASE_TEST_PATH))));
                FileOperationUtil.copyDirectoryRecursively(toTestPath, Paths.get(String.format("%s/%s", tmpFolder_ToZip, Paths.get(BASE_TEST_PATH))));

                FileOperationUtil.zipDirectory(String.format("%s/src", tmpFolder_ToZip), String.format("%s/src.zip", tmpFolder_ToZip));
                File zip = new File(String.format("%s/src.zip", tmpFolder_ToZip));

                if (!zip.exists()) {
                    logger.error("Errore: Il file ZIP non è stato creato correttamente.");
                    FileOperationUtil.deleteDirectoryRecursively(tmpFolder_ToZip);
                } else {
                    JacocoCoverageDTO coverageDTO = apiGatewayClient.callGenerateMissingJacocoCoverage(zip);
                    FileOperationUtil.writeStringToFile(coverageDTO.getCoverage(), new File(String.format("%s/coveragetot.xml", toCoveragePath)));
                }

                Files.delete(zip.toPath());
                FileOperationUtil.deleteDirectoryRecursively(tmpFolder_ToZip);
            }

            String evosuiteFileContent = Files.readString(Paths.get(String.format("%s/statistics.csv", toCoveragePath)));
            int[][] evoSuiteStatistics = ExtractScore.fromEvosuite(evosuiteFileContent);
            logger.info("Evosuite Coverage: " + Arrays.deepToString(evoSuiteStatistics));

            String coverage = Files.readString(Paths.get(String.format("%s/coveragetot.xml", toCoveragePath)));
            int[][] jacocoStatistics = ExtractScore.fromJacoco(coverage);
            logger.info("Jacoco Coverage: " + Arrays.deepToString(jacocoStatistics));

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

            opponentRepository.saveOpponent(opponent);

            for (GameMode mode : GameMode.values()) {
                apiGatewayClient.callAddNewOpponent(classUTName, mode, robotType, difficulty);
            }

        }

        FileOperationUtil.deleteDirectoryRecursively(operationTmpFolder);
    }


}