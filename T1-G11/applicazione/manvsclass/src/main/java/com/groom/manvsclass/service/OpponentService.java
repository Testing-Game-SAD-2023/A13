package com.groom.manvsclass.service;

import com.groom.manvsclass.api.ApiGatewayClient;
import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Operation;
import com.groom.manvsclass.model.Opponent;
import com.groom.manvsclass.model.dto.ClassUTDetailsDTO;
import com.groom.manvsclass.model.repository.ClassRepository;
import com.groom.manvsclass.model.repository.OperationRepository;
import com.groom.manvsclass.model.repository.OpponentRepository;
import com.groom.manvsclass.model.repository.SearchRepositoryImpl;
import com.groom.manvsclass.service.exception.*;
import com.groom.manvsclass.service.upload.ClassUTUploadService;
import com.groom.manvsclass.service.upload.FileStorageService;
import com.groom.manvsclass.service.upload.UploadOpponentService;
import com.groom.manvsclass.util.filesystem.download.FileDownloadUtil;
import com.groom.manvsclass.util.upload.FileUploadResponse;
import com.groom.manvsclass.util.upload.JavaMetadataExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.groom.manvsclass.util.upload.OpponentPathResolver.*;

@Service
public class OpponentService {
    private static final Logger logger = LoggerFactory.getLogger(OpponentService.class);
    private final OperationRepository operationRepository;
    private final ClassRepository classRepository;
    private final MongoTemplate mongoTemplate;
    private final SearchRepositoryImpl searchRepository;
    private final UploadOpponentService uploadOpponentService;
    private final OpponentRepository opponentRepository;
    private final ClassUTUploadService classUTUploadService;
    private final Admin userAdmin = new Admin("default", "default", "default", "default", "default");
    private final ApiGatewayClient apiGatewayClient;
    private final FileStorageService fileStorageService;

    public OpponentService(OperationRepository operationRepository,
                           ClassRepository classRepository,
                           MongoTemplate mongoTemplate,
                           SearchRepositoryImpl searchRepository,
                           UploadOpponentService uploadOpponentService,
                           OpponentRepository opponentRepository,
                           ApiGatewayClient apiGatewayClient,
                           FileStorageService fileStorageService,
                           ClassUTUploadService classUTUploadService) {
        this.operationRepository = operationRepository;
        this.classRepository = classRepository;
        this.mongoTemplate = mongoTemplate;
        this.searchRepository = searchRepository;
        this.uploadOpponentService = uploadOpponentService;
        this.opponentRepository = opponentRepository;
        this.apiGatewayClient = apiGatewayClient;
        this.fileStorageService = fileStorageService;
        this.classUTUploadService = classUTUploadService;
    }

    /*
     * Restituisce la lista di classi UT disponibili nel sistema
     */
    public ResponseEntity<List<String>> getNomiClassiUT() {
        // 2. Recupera tutte le ClassUT dal repository e restituisce solo i nomi
        List<String> classNames = classRepository.findAll()
                .stream()
                .map(ClassUT::getName) // Estrae solo i nomi
                .toList();

        // 3. Ritorna i nomi delle classi con lo status HTTP 200 (OK)
        return ResponseEntity.ok(classNames);
    }

    public ResponseEntity<FileUploadResponse> uploadClassAndOpponents(
            MultipartFile classUTFile,
            String classUTDetails,
            MultipartFile robotTestsZip) throws IOException {

        // Validate class file
        if (classUTFile == null || classUTFile.isEmpty()) {
            throw new FileUploadException(
                "File della classe mancante o vuoto. " +
                "Selezionare un file .java valido contenente la classe da testare. " +
                "Verificare che il file non sia danneggiato e abbia una dimensione maggiore di 0 byte."
            );
        }

        // Validate robot tests zip file
        if (robotTestsZip == null || robotTestsZip.isEmpty()) {
            throw new FileUploadException(
                "File ZIP dei test robot mancante o vuoto. " +
                "Selezionare un file ZIP valido contenente le cartelle EvoSuiteTest e/o RandoopTest. " +
                "Il file deve avere la seguente struttura: ZIP/[EvoSuiteTest|RandoopTest]/[01Level, 02Level, ...]/[test files e coverage]. " +
                "Verificare che il file non sia danneggiato e abbia una dimensione maggiore di 0 byte."
            );
        }

        ClassUT classe;
        try {
            classe = ClassUTDetailsDTO.parseFromJson(classUTDetails);
        } catch (IOException e) {
            throw new ClassValidationException(
                "Errore nella lettura dei dettagli della classe: " + e.getMessage(),
                e
            );
        }
        
        String classUTFileName = StringUtils.cleanPath(Objects.requireNonNull(classUTFile.getOriginalFilename()));
        String classUTName = classe.getName();

        //-------------------------------------------------------------------------------------------
        // Verifica che il nome della classe inserito dall'admin nel campo "Class Name" del form HTML
        // sia effettivamente il nome della classe così come dichiarato nel file .java.
        byte[] classFileBytes;
        try {
            classFileBytes = classUTFile.getBytes();
        } catch (IOException e) {
            throw new FileUploadException(
                "Impossibile leggere il contenuto del file .java: " + e.getMessage() + ". " +
                "Il file potrebbe essere danneggiato o non accessibile. Riprovare con un altro file.",
                e
            );
        }
        
        String classNameFromSourceFile = JavaMetadataExtractor.getClassNameFromJavaSourceFile(classFileBytes);

        if(classNameFromSourceFile == null) {
            throw new ClassValidationException(
                "Il file .java inviato non contiene una dichiarazione di classe Java valida. " +
                "Verificare che il file contenga una riga del tipo 'public class NomeClasse { ... }'. " +
                "Il file potrebbe essere corrotto, vuoto, o non essere un file Java valido."
            );
        }

        if(!classNameFromSourceFile.equalsIgnoreCase(classUTName)) {
            throw new ClassValidationException(
                String.format(
                    "Mancata corrispondenza tra nome classe nel file e nome inserito nel form:%n" +
                    "  • Nome nel file .java: '%s'%n" +
                    "  • Nome inserito nel form: '%s'%n%n" +
                    "Correggere il campo 'Class Name' nel form in modo che corrisponda esattamente al nome della classe " +
                    "dichiarato nel file .java (verificare maiuscole/minuscole).",
                    classNameFromSourceFile, classUTName
                )
            );
        }

		if(FormValidation.validateClassUT(classe) == false)
		{
			throw new ClassValidationException("Errore: la validazione del form non ha avuto successo.");
		}

        //-------------------------------------------------------------------------------------------

        try {
            // Save class file to filesystem
            classUTUploadService.saveClassUTFile(classUTFileName, classUTName, classUTFile);

            // Process and save opponents from ZIP
            uploadOpponentService.saveOpponentsFromZip(classUTFileName, classUTName, classUTFile, robotTestsZip);

            // Persist class metadata to database
            classUTUploadService.persistClassUTMetadata(classe, classUTFileName);

            // Success - build response
            FileUploadResponse response = new FileUploadResponse();
            response.setFileName(classUTFileName);
            response.setSize(classUTFile.getSize());
            response.setDownloadUri("/downloadFile");

            logger.info("Operazione completata con successo (uploadClassAndOpponents) - class: {}", classUTName);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Rollback everything - just try to delete everything that might exist
            logger.error("Error during upload of {}, initiating rollback. Error: {}", classUTName, e.getMessage(), e);
            performRollback(classUTName);

            // Re-throw the exception to be handled by GlobalExceptionHandler
            if (e instanceof FileUploadException ||
                e instanceof ClassValidationException ||
                e instanceof RobotProcessingException ||
                e instanceof ExternalServiceException) {
                throw e;
            } else if (e instanceof IOException ioException) {
                throw new FileUploadException(
                    "Errore I/O durante l'upload: " + ioException.getMessage() + 
                    ". Verificare i permessi dei file e lo spazio disponibile su disco.", ioException
                );
            } else {
                throw new FileUploadException(
                    "Errore imprevisto durante l'upload: " + e.getMessage() + 
                    ". Contattare l'amministratore se il problema persiste.", e
                );
            }
        }
    }

    /**
     * Performs rollback by attempting to delete everything that might have been created in case the upload fail.
     */
    private void performRollback(String classUTName) {
        logger.warn("Starting rollback for class: {}", classUTName);

        // Try to delete metadata from database
        try {
            classUTUploadService.rollbackClassUTMetadata(classUTName);
        } catch (Exception e) {
            logger.debug("Rollback: metadata cleanup - {}", e.getMessage());
        }

        // Try to delete opponent data from database
        try {
            opponentRepository.deleteByClassUT(classUTName);
        } catch (Exception e) {
            logger.debug("Rollback: opponent DB cleanup - {}", e.getMessage());
        }

        // Try to notify external service to delete opponents
        try {
            apiGatewayClient.callDeleteAllClassUTOpponents(classUTName);
        } catch (Exception e) {
            logger.debug("Rollback: external service cleanup - {}", e.getMessage());
        }

        // Try to delete all opponent files
        try {
            uploadOpponentService.rollbackOpponentFiles(classUTName);
        } catch (Exception e) {
            logger.debug("Rollback: opponent files cleanup - {}", e.getMessage());
        }

        // Try to delete class file
        try {
            classUTUploadService.rollbackClassUTFile(classUTName);
        } catch (Exception e) {
            logger.debug("Rollback: class file cleanup - {}", e.getMessage());
        }

        logger.info("Rollback completed for class: {}", classUTName);
    }


    public ResponseEntity<Object> downloadClasse(String name) {

        logger.info("/downloadFile (OpponentService) - name: {}", name);
        logger.debug("DownloadClasse invoked");
        try {
            List<ClassUT> classe = searchRepository.findByText(name);
            logger.info("File download: uri={}", classe.get(0).getUri());
            ResponseEntity<Resource> resourceResponse = FileDownloadUtil.downloadClassFile(classe.get(0).getUri());
            return ResponseEntity.status(resourceResponse.getStatusCode())
                    .headers(resourceResponse.getHeaders())
                    .body(resourceResponse.getBody());
        } catch (Exception e) {
            logger.error("Classe UT non trovata: name={}", name, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ClasseUT " + name + " non trovata");
        }
    }

    public List<ClassUT> getAllClassUTs() {
        return classRepository.findAll();
    }

    public List<ClassUT> filterByDifficulty(String difficulty) {
        return searchRepository.filterByDifficulty(difficulty);
    }

    public List<ClassUT> orderByDate() {
        return searchRepository.orderByDate();
    }

    public List<ClassUT> orderByName() {
        return searchRepository.orderByName();
    }

    public ResponseEntity<String> modificaClasse(String name, ClassUT newContent) {
        logger.debug("Token valido, aggiornamento informazioni classi (update/{})", name);
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        Update update = new Update().set("name", newContent.getName())
                .set("date", newContent.getDate())
                .set("difficulty", newContent.getDifficulty())
                .set("description", newContent.getDescription())
                .set("category", newContent.getCategory());
        long modifiedCount = mongoTemplate.updateFirst(query, update, ClassUT.class).getModifiedCount();

        if (modifiedCount > 0) {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String data = currentDate.format(formatter);
            Operation operation1 = new Operation((int) operationRepository.count(), userAdmin.getUsername(), newContent.getName(), 1, data);
            operationRepository.save(operation1);
            return new ResponseEntity<>("Aggiornamento eseguito correttamente.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Nessuna classe trovata o nessuna modifica effettuata.", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> eliminaClasse(String name) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("name").is(name));
            eliminaFile(name);
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String data = currentDate.format(formatter);
            Operation operation1 = new Operation((int) operationRepository.count(), "userAdmin", name, 2, data);
            operationRepository.save(operation1);
            ClassUT deletedClass = mongoTemplate.findAndRemove(query, ClassUT.class);

            Query query2 = new Query();
            query2.addCriteria(Criteria.where("classUT").is(name));
            mongoTemplate.findAndRemove(query, Opponent.class);

            apiGatewayClient.callDeleteAllClassUTOpponents(name);
            if (deletedClass != null) {
                return ResponseEntity.ok().body(deletedClass);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe non trovata");
            }
        } catch (RuntimeException e) {
            logger.error("Errore durante l'eliminazione della classe {}: {}", name, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante l'eliminazione: " + e.getMessage());
        }
    }

    public void eliminaFile(String fileName) {
        Path classUTDirectory = Path.of(VOLUME_T0_BASE_PATH, fileName);
        Path unmodifiedSrcDirectory = getUnmodifiedSrcPath(fileName);

        logger.debug("name: {}", fileName);
        if (classUTDirectory.toFile().exists() && classUTDirectory.toFile().isDirectory()) {
            try {
                fileStorageService.deleteDirectoryRecursively(classUTDirectory);
                fileStorageService.deleteDirectoryRecursively(unmodifiedSrcDirectory);
                logger.info("Cartella eliminata con successo (/deleteFile/{})", fileName);
            } catch (IOException e) {
                logger.error("Impossibile eliminare la cartella: {}", fileName, e);
                throw new RuntimeException("Impossibile eliminare la cartella.");
            }
        } else {
            logger.warn("Cartella non trovata: {}", fileName);
            throw new RuntimeException("Cartella non trovata.");
        }
    }


    public List<Opponent> getAllOpponents() {
        return opponentRepository.findAllOpponents();
    }

    public Opponent getOpponentData(String classUT, String opponentType, OpponentDifficulty opponentDifficulty) {
        Optional<Opponent> opponent = opponentRepository.findOpponent(classUT, opponentType, opponentDifficulty);
        if (opponent.isEmpty())
            throw new OpponentNotFoundException();

        return opponent.get();
    }

    public EvosuiteScore getOpponentEvosuiteScore(String classUT, String opponentType, OpponentDifficulty opponentDifficulty) {
        Optional<EvosuiteScore> score = opponentRepository.findEvosuiteScore(classUT,
                opponentType, opponentDifficulty);

        if (score.isEmpty())
            throw new ScoreNotFoundException();

        return score.get();
    }

    public JacocoScore getOpponentJacocoScore(String classUT, String opponentType, OpponentDifficulty opponentDifficulty) {
        Optional<JacocoScore> score = opponentRepository.findJacocoScore(classUT,
                opponentType, opponentDifficulty);

        if (score.isEmpty())
            throw new ScoreNotFoundException();

        return score.get();
    }

    public String getOpponentCoverage(String classUT, String opponentType, OpponentDifficulty opponentDifficulty) {
        Optional<String> coverage = opponentRepository.findCoverage(classUT,
                opponentType, opponentDifficulty);

        if (coverage.isEmpty())
            throw new CoverageNotFoundException();

        return coverage.get();
    }
}
