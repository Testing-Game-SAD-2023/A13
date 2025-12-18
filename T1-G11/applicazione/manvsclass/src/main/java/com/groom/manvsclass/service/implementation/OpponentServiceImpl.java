package com.groom.manvsclass.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.manvsclass.api.ApiGatewayClient;
import com.groom.manvsclass.model.entity.AdminEntity;
import com.groom.manvsclass.model.entity.ClassUTEntity;
import com.groom.manvsclass.model.entity.OperationEntity;
import com.groom.manvsclass.model.entity.OpponentEntity;
import com.groom.manvsclass.model.repository.*;
import com.groom.manvsclass.service.ClassUTService;
import com.groom.manvsclass.service.OpponentService;
import com.groom.manvsclass.service.exception.CoverageNotFoundException;
import com.groom.manvsclass.service.exception.OpponentNotFoundException;
import com.groom.manvsclass.service.exception.ScoreNotFoundException;
import com.groom.manvsclass.util.filesystem.FileOperationUtil;
import com.groom.manvsclass.util.filesystem.download.FileDownloadUtil;
import com.groom.manvsclass.util.filesystem.upload.FileUploadResponse;
import com.groom.manvsclass.util.filesystem.upload.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import testrobotchallenge.commons.models.dto.score.EvosuiteCoverageDTO;
import testrobotchallenge.commons.models.dto.score.JacocoCoverageDTO;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import testrobotchallenge.commons.models.score.Coverage;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;
import testrobotchallenge.commons.util.ExtractScore;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OpponentServiceImpl implements OpponentService {

//    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(String.valueOf(OpponentServiceImpl.class));

    @Autowired
    private OpponentRepository opponentRepository;

    private AdminEntity userAdminEntity = new AdminEntity();

    @Autowired
    private ApiGatewayClient apiGatewayClient;

    @Autowired
    private ClassUTRepository classUTRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ClassUTService classUTService;

    @Autowired
    private OpponentService opponentService;

    @Autowired
    private HintRepository hintRepository;

    /*
     * Restituisce la lista di classi UT disponibili nel sistema
     */
    @Override
    public ResponseEntity<?> getNomiClassiUT(String jwt) {
        // 2. Recupera tutte le ClassUT dal repository e restituisce solo i nomi
        List<String> classNames = classUTRepository.findAll()
                .stream()
                .map(ClassUTEntity::getName) // Estrae solo i nomi
                .collect(Collectors.toList());

        // 3. Ritorna i nomi delle classi con lo status HTTP 200 (OK)
        return ResponseEntity.ok(classNames);
    }

    @Override
    public ResponseEntity<FileUploadResponse> uploadOpponent(
            MultipartFile classUTFile,
            String classUTDetails,
            MultipartFile robotTestsZip) throws IOException {

        FileUploadResponse response = new FileUploadResponse();

        // Verifica che il file della classe sia stato ricevuto
        if (classUTFile == null || classUTFile.isEmpty()) {
            response.setErrorMessage("Errore: file della classe non ricevuto o vuoto.");
            return ResponseEntity.badRequest().body(response);
        }

        // Parsing dei dettagli della classe
        ObjectMapper mapper = new ObjectMapper();
        // Aggiungi il modulo JavaTimeModule se hai errori sulle date qui, come visto prima
        // mapper.registerModule(new JavaTimeModule());
        ClassUTEntity classe = mapper.readValue(classUTDetails, ClassUTEntity.class);

        // Nome del file e dimensione
        String classUTFileName = StringUtils.cleanPath(Objects.requireNonNull(classUTFile.getOriginalFilename()));
        long size = classUTFile.getSize();

        System.out.println("Salvataggio di " + classUTFileName + " nel filesystem condiviso");

        // --- MODIFICA INIZIA QUI ---

        // 1. Prima salviamo il file fisico
        FileUploadUtil.saveCLassFile(classUTFileName, classe.getName(), classUTFile);

        // 2. Prepariamo i dati dell'entità
        response.setFileName(classUTFileName);
        response.setSize(size);
        response.setDownloadUri("/downloadFile");

        classe.setUri(String.format("%s/%s/%s/%s",
                VOLUME_T0_BASE_PATH,
                UNMODIFIED_SRC,
                classe.getName(),
                classUTFileName));

        classe.setDate(LocalDate.now());

        // 3. SALVIAMO SU DB ORA (Prima di processare lo zip!)
        // Usiamo saveAndFlush per garantire che findById lo trovi subito dopo
        classUTRepository.saveAndFlush(classe);

        // 4. ORA possiamo chiamare il metodo che cercherà questa classe nel DB
        saveOpponentsFromZip(classUTFileName, classe.getName(), classUTFile, robotTestsZip);

        // --- MODIFICA FINISCE QUI ---

        System.out.println("Operazione completata con successo (uploadTest)");

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> downloadClasse(@PathVariable("name") String name) throws Exception {

        System.out.println("/downloadFile/{name} (HomeController) - name: " + name);
        System.out.println("test");
        try {
            List<ClassUTEntity> classe = classUTRepository.findByNameLike(name);
            System.out.println("File download:");
            System.out.println(classe.get(0).getUri());
            ResponseEntity file = FileDownloadUtil.downloadClassFile(classe.get(0).getUri());
            return file;
        } catch (Exception e) {
            System.out.println("Classe UT non trovata");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ClasseUT " + name + " non trovata");
        }
    }

    @Override
    public List<ClassUTEntity> getAllClassUTs() {
        return classUTRepository.findAll();
    }

    @Override
    public List<ClassUTEntity> filterByDifficulty(String difficulty) {
        return classUTRepository.findByDifficulty(difficulty);
    }

    @Override
    public List<ClassUTEntity> orderByDate() {
        return classUTRepository.findAllByOrderByDateAsc();
    }

    @Override
    public List<ClassUTEntity> orderByName() {
        return classUTRepository.findAllByOrderByNameAsc();
    }

    @Override
    public ResponseEntity<String> modificaClasse(String name, ClassUTEntity newContent, String jwt, HttpServletRequest request) {
        System.out.println("Token valido, può aggiornare informazioni inerenti le classi (update/{name})");

        ClassUTEntity classUTEntity = new ClassUTEntity();
        classUTEntity.setName(newContent.getName());
        classUTEntity.setDifficulty(newContent.getDifficulty());
        classUTEntity.setDescription(newContent.getDescription());
        classUTEntity.setDate(newContent.getDate());
        classUTEntity.setCategory(newContent.getCategory());

        long modifiedCount = classUTService.updateClassUT(name, classUTEntity);

        if (modifiedCount > 0) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String data = currentDate.format(formatter);
            userAdminEntity.setName("default");
            userAdminEntity.setUsername("default");
            userAdminEntity.setPassword("default");
            userAdminEntity.setSurname("default");
            OperationEntity operationEntity1 = new OperationEntity((int) operationRepository.count(), userAdminEntity, newContent.getName(), 1, data);
            operationRepository.save(operationEntity1);
            return new ResponseEntity<>("Aggiornamento eseguito correttamente.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Nessuna classe trovata o nessuna modifica effettuata.", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> eliminaClasse(String name) {

        // 1. Pulizia file su disco
        eliminaFile(name);

        // 2. Controllo/Creazione Admin
        AdminEntity adminEntity = adminRepository.findByUsername("userAdmin");
        if (adminEntity == null) {
            // ... (codice creazione admin default come prima) ...
            adminEntity = new AdminEntity();
            adminEntity.setEmail("admin@default.com");
            adminEntity.setUsername("userAdmin");
            adminEntity.setPassword("default");
            adminEntity.setName("System");
            adminEntity.setSurname("Admin");
            adminRepository.saveAndFlush(adminEntity);
        }

        ClassUTEntity classUTEntity = classUTRepository.findById(name).orElse(null);

        if (classUTEntity != null) {

            hintRepository.deleteByClassUt_Name(name);

            // 2. Elimina Opponent (Avversari) - QUELLO CHE MANCAVA
            // Usiamo il repository locale per pulire la tabella SQL 'opponent'
            opponentRepository.deleteByClassUtName(name);

            // Chiamata al microservizio esterno (per pulire l'altro lato, se esiste)
            try {
                apiGatewayClient.callDeleteAllClassUTOpponents(name);
            } catch (Exception e) {
                log.warn("Errore durante la chiamata al gateway per eliminare opponenti remoti: " + e.getMessage());
            }

            // 3. Pulisci Operation (se necessario, in base alla tua modifica precedente)
            // Se hai tolto la FK, questo passaggio è opzionale ma male non fa
            // operationRepository.deleteByClassName(name);

            // === CRUCIALE: FLUSH ===
            // Diciamo al database: "Esegui ORA queste cancellazioni, non aspettare!"
            hintRepository.flush();
            opponentRepository.flush();
            // operationRepository.flush();

            // === FASE DI LOG (Facoltativa, senza FK) ===
            try {
                // Assicurati che OperationEntity accetti la STRINGA 'name' nel costruttore
                OperationEntity logDelete = new OperationEntity(
                        (int) operationRepository.count() + 1,
                        adminEntity,
                        name, // Passiamo solo la stringa
                        2,
                        LocalDate.now().toString()
                );
                operationRepository.saveAndFlush(logDelete);
            } catch (Exception e) {
                log.warn("Log saltato: " + e.getMessage());
            }

            // === FASE FINALE: ELIMINAZIONE CLASSE ===
            // Ora la tabella 'opponent' è vuota per questa classe, quindi il vincolo FK non scatterà.
            ClassUTEntity deletedClass = classUTService.deleteClassUT(name);

            return ResponseEntity.ok("Classe eliminata con successo");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe non trovata nel DB");
        }
    }

    @Override
    public void eliminaFile(String fileName) {
        // Costruiamo i percorsi
        File directory = new File(String.format("%s/%s", VOLUME_T0_BASE_PATH, fileName));
        File directoryUnmodifiedSrc = new File(String.format("%s/%s/%s", VOLUME_T0_BASE_PATH, UNMODIFIED_SRC, fileName));

        log.info("Tentativo eliminazione cartella: {}", directory.getAbsolutePath());

        // Tentativo 1: Eliminazione cartella principale
        if (directory.exists() && directory.isDirectory()) {
            try {
                FileOperationUtil.deleteDirectoryRecursively(directory.toPath());
                log.info("Cartella eliminata: {}", directory.getName());
            } catch (IOException e) {
                log.error("Errore eliminazione cartella {}", directory.getName(), e);
                // Non lanciamo eccezione bloccante, proviamo a continuare
            }
        } else {
            log.warn("Attenzione: La cartella {} non esiste fisicamente su disco, procedo comunque con l'eliminazione dal DB.", directory.getAbsolutePath());
        }

        // Tentativo 2: Eliminazione src non modificato
        if (directoryUnmodifiedSrc.exists() && directoryUnmodifiedSrc.isDirectory()) {
            try {
                FileOperationUtil.deleteDirectoryRecursively(directoryUnmodifiedSrc.toPath());
            } catch (IOException e) {
                log.error("Errore eliminazione src unmodified {}", directoryUnmodifiedSrc.getName(), e);
            }
        }
    }

    @Override
    public List<OpponentEntity> getAllOpponents() {
        return opponentRepository.findAll();
    }

    @Override
    public OpponentEntity getOpponentData(String classUT, String opponentType, OpponentDifficulty opponentDifficulty) {
        Optional<OpponentEntity> opponent = opponentRepository.findByClassUt_NameAndOpponentTypeAndOpponentDifficulty(classUT, opponentType, opponentDifficulty);
        if (opponent.isEmpty())
            throw new OpponentNotFoundException();

        return opponent.get();
    }

    @Override
    public EvosuiteScore getOpponentEvosuiteScore(String classUT, String opponentType, OpponentDifficulty opponentDifficulty) {
        Optional<EvosuiteScore> score = opponentRepository.findEvosuiteScore(classUT,
                opponentType, opponentDifficulty);

        if (score.isEmpty())
            throw new ScoreNotFoundException();

        return score.get();
    }

    @Override
    public JacocoScore getOpponentJacocoScore(String classUT, String opponentType, OpponentDifficulty opponentDifficulty) {
        Optional<JacocoScore> score = opponentRepository.findJacocoScore(classUT,
                opponentType, opponentDifficulty);

        if (score.isEmpty())
            throw new ScoreNotFoundException();

        return score.get();
    }

    @Override
    public String getOpponentCoverage(String classUT, String opponentType, OpponentDifficulty opponentDifficulty) {
        Optional<String> coverage = opponentRepository.findCoverage(classUT,
                opponentType, opponentDifficulty);

        if (coverage.isEmpty())
            throw new CoverageNotFoundException();

        return coverage.get();
    }


    @Override
    public void eliminaOpponent(String className) {
        List<OpponentEntity> opponents = opponentRepository.findByClassUt_Name(className);

        if (!CollectionUtils.isEmpty(opponents)) {
            OpponentEntity toRemove = opponents.get(0);
            opponentRepository.delete(toRemove);

        }
    }



    public void saveOpponentsFromZip(String classUTFileName, String classUTName, MultipartFile classUTFile, MultipartFile robotTestsZip) throws IOException {
        Path operationTmpFolder = Paths.get(String.format("%s/%s/tmp", VOLUME_T0_BASE_PATH, classUTName));
        FileOperationUtil.saveFileInFileSystem("robot.zip", operationTmpFolder, robotTestsZip);
        FileOperationUtil.extractZipIn(operationTmpFolder);

        Path unmodifiedSrcCodePath = Paths.get(String.format("%s/%s/%s", VOLUME_T0_BASE_PATH, UNMODIFIED_SRC, classUTName));
        log.info("Saving unmodified src in {}", unmodifiedSrcCodePath);
        FileOperationUtil.saveFileInFileSystem(classUTFileName, unmodifiedSrcCodePath, classUTFile);

        File robotGroupFolder = Objects.requireNonNull(operationTmpFolder.toFile().listFiles())[0];
        log.info("Robot tests folder {}", robotGroupFolder);
        for (File robotFolder : Objects.requireNonNull(robotGroupFolder.listFiles())) {
            if (!robotFolder.isDirectory()) {
                log.info("Ignoring file {} because it is not a directory", robotFolder);
                continue;
            }

            String robotType = robotFolder.getName();
            if (!robotType.endsWith("Test")) {
                log.info("Ignoring directory {} because it does not follow the naming convention", robotFolder);
                continue;
            }
            robotType = robotType.substring(0, robotType.length() - 4);
            robotType = Character.toUpperCase(robotType.charAt(0)) + robotType.substring(1);

            log.info("Robot folder {}", robotFolder);
            log.info("Saving robot type {}", robotType);

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
            Files.copy(src.toPath(),
                    Paths.get(String.format("%s/%s/%s", toTestPath, testPackagePath, src.getName())), // O come stavi costruendo il path
                    StandardCopyOption.REPLACE_EXISTING);
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

                Files.copy(coverageFile.toPath(), Paths.get(String.format("%s/%s", coveragePath, coverageFile.getName())), StandardCopyOption.REPLACE_EXISTING);
                jacocoFound = true;
            }

            if (coverageFile.getName().equals(EVOSUITE_COVERAGE_FILE)) {
                Files.copy(coverageFile.toPath(), Paths.get(String.format("%s/%s", coveragePath, coverageFile.getName())), StandardCopyOption.REPLACE_EXISTING);
                evosuiteFound = true;
            }
        }

        return new boolean[]{jacocoFound, evosuiteFound};
    }

    private void uploadNewOpponents(String classUTFileName, String classUTName, MultipartFile classUTFile, Path operationTmpFolder, String robotType, Path volumeBasePath) throws IOException {
        for (File levelFolder : Objects.requireNonNull(operationTmpFolder.toFile().listFiles())) {
            if (!levelFolder.isDirectory()) {
                log.info("Ignoring file " + levelFolder.getName() + " because it is not a directory");
                continue;
            }

            if (!levelFolder.getName().matches("\\d{2,}Level")) {
                log.info("Ignoring folder " + levelFolder.getName() + " because it is not a level");
            }

            String level = levelFolder.getName();

            log.info("Saving level " + level);
            Path toSrcPath = Paths.get(String.format("%s/%s/%s/%s/%s/%s", volumeBasePath, classUTName, robotType, BASE_CODE_PATH, level, BASE_SRC_PATH));
            Path toTestPath = Paths.get(String.format("%s/%s/%s/%s/%s/%s", volumeBasePath, classUTName, robotType, BASE_CODE_PATH, level, BASE_TEST_PATH));
            Path toCoveragePath = Paths.get(String.format("%s/%s/%s/%s/%s", volumeBasePath, classUTName, robotType, BASE_COVERAGE_PATH, level));

            log.info("Save SRC path " + toSrcPath);
            log.info("Save TESTS path " + toTestPath);
            log.info("Save COVERAGE path " + toCoveragePath);

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

            log.info("Robot TESTS path " + fromTestPath);
            log.info("Robot COVERAGE path " + fromCoveragePath);

            if (!Files.exists(fromTestPath)) {
                log.info("Skipping folder " + fromTestPath + " because it does not exist");
                continue;
            }

            if (fromTestPath.toFile().listFiles().length == 0) {
                log.info("Skipping folder " + fromTestPath + " because it does not have any files");
                continue;
            }

            if (Arrays.stream(fromTestPath.toFile().listFiles()).noneMatch(file -> file.getName().endsWith(".java"))) {
                log.info("Skipping folder " + fromTestPath + " because it does not contain any .java files");
                continue;
            }

            String[][] splitPackageNames = saveTestFilesInVolume(fromTestPath, toTestPath, classUTName, robotType);
            String[] srcPackageNameSplit = splitPackageNames[0];
            saveSrcFileInVolume(classUTFile, toSrcPath, srcPackageNameSplit, classUTFileName);

            log.info("SRC package names split " + Arrays.toString(srcPackageNameSplit));
            log.info("TEST package names split " + Arrays.toString(splitPackageNames[1]));

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
                    log.error("Errore: Il file ZIP non è stato creato correttamente.");
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
            log.info("Evosuite Coverage: " + Arrays.deepToString(evoSuiteStatistics));

            String coverage = Files.readString(Paths.get(String.format("%s/coveragetot.xml", toCoveragePath)));
            int[][] jacocoStatistics = ExtractScore.fromJacoco(coverage);
            log.info("Jacoco Coverage: " + Arrays.deepToString(jacocoStatistics));

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

            ClassUTEntity classUTEntity = classUTRepository.findById(classUTName).orElse(null);

            if (classUTEntity == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Class UT not found");
            }

            OpponentEntity opponentEntity = new OpponentEntity();
            opponentEntity.setClassUt(classUTEntity);
            opponentEntity.setOpponentType(robotType);
            opponentEntity.setOpponentDifficulty(difficulty);
            opponentEntity.setCoverage(coverage);
            opponentEntity.setEvosuiteScore(evosuiteScore);
            opponentEntity.setJacocoScore(jacocoScore);

            opponentRepository.saveAndFlush(opponentEntity);

            for (GameMode mode : GameMode.values()) {
                apiGatewayClient.callAddNewOpponent(classUTName, mode, robotType, difficulty);
            }

        }

        FileOperationUtil.deleteDirectoryRecursively(operationTmpFolder);
    }

}
