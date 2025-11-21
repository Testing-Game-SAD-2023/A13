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
import com.groom.manvsclass.service.exception.CoverageNotFoundException;
import com.groom.manvsclass.service.exception.OpponentNotFoundException;
import com.groom.manvsclass.service.exception.ScoreNotFoundException;
import com.groom.manvsclass.service.upload.ClassUTUploadService;
import com.groom.manvsclass.service.upload.FileStorageService;
import com.groom.manvsclass.service.upload.UploadOpponentService;
import com.groom.manvsclass.util.filesystem.download.FileDownloadUtil;
import com.groom.manvsclass.util.upload.FileUploadResponse;
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

        FileUploadResponse response = new FileUploadResponse();

        if (classUTFile == null || classUTFile.isEmpty()) {
            response.setErrorMessage("Errore: file della classe non ricevuto o vuoto.");
            return ResponseEntity.badRequest().body(response);
        }

        ClassUT classe = ClassUTDetailsDTO.parseFromJson(classUTDetails);
        String classUTFileName = StringUtils.cleanPath(Objects.requireNonNull(classUTFile.getOriginalFilename()));
        
        classUTUploadService.saveClassUTFile(classUTFileName, classe.getName(), classUTFile);
        uploadOpponentService.saveOpponentsFromZip(classUTFileName, classe.getName(), classUTFile, robotTestsZip);
        
        response.setFileName(classUTFileName);
        response.setSize(classUTFile.getSize());
        response.setDownloadUri("/downloadFile");

        classUTUploadService.persistClassUTMetadata(classe, classUTFileName);

        logger.info("Operazione completata con successo (uploadTest)");
        return ResponseEntity.ok(response);
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
