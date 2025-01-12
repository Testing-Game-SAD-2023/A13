package com.groom.manvsclass.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Robot;
import com.groom.manvsclass.repository.ClassRepository;
import com.groom.manvsclass.responses.ApiResponse;

@Service
public class ApiService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private JwtService jwtService;

    @Value("${config.pathRobot}")
    private String configRobotPath;

    public ResponseEntity<ApiResponse> getClasses(String jwt) {

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        List<String> names = new ArrayList<>();

        List<ClassUT> classes = classRepository.findAll();

        if (classes.isEmpty()) {
            response.setMessage("Error, classes not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        for (ClassUT classUT : classes) {
            names.add(classUT.getName());
        }

        response.setData(names);
        response.setMessage("Classes found");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);

    }

    public ResponseEntity<ApiResponse> getClass(String className, String jwt) {

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        Query query = new Query();

        query.addCriteria(Criteria.where("name").is(className));

        ClassUT classUT = mongoTemplate.findOne(query, ClassUT.class);

        if (classUT == null) {
            response.setMessage("Error, class not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        response.setData(classUT.getcode_Uri());
        response.setMessage("Class found");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);

    }

    public ResponseEntity<ApiResponse> getRobots(String className, String jwt) {

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        List<String> robotsnames = new ArrayList<>();

        Query query = new Query();

        query.addCriteria(Criteria.where("name").is(className));

        ClassUT classUT = mongoTemplate.findOne(query, ClassUT.class);

        if (classUT == null) {
            response.setMessage("Error, class not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        List<String> robotNames = classUT.getRobotNames();

        if (robotNames.isEmpty()) {
            response.setMessage("Robots not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        response.setData(robotNames);
        response.setMessage("Robots found");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    public ResponseEntity<ApiResponse> getRobot(String className, String robotName, String jwt) {

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        Query query = new Query();

        query.addCriteria(Criteria.where("name").is(className));

        ClassUT classUT = mongoTemplate.findOne(query, ClassUT.class);

        if (classUT == null) {
            response.setMessage("Error, class not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        List<String> robotNames = classUT.getRobotNames();

        if (robotNames.contains(robotName)) {
            response.setData(classUT.getRobotPath(robotName));
            response.setMessage("Robot found");
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        response.setMessage("Error, robot not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);

    }

    // Sovrascrive una classe
    public ResponseEntity<ApiResponse> setClass(String className, MultipartFile classFile, String jwt)
            throws IOException {

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        Query query = new Query();

        query.addCriteria(Criteria.where("name").is(className));

        ClassUT classUT = mongoTemplate.findOne(query, ClassUT.class);

        if (classUT == null) {
            response.setMessage("Error, class not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        fileSystemService.deleteClass(className);

        Path path = fileSystemService.saveClass(className, classFile);

        classUT.setUri(path.toString());

        mongoTemplate.findAndReplace(query, classUT);
        response.setMessage("Class setted");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);

    }

    // Aggiungo o sovrascrive un robot se esso è disponibile nel file di
    // configurazione
    public ResponseEntity<ApiResponse> setRobot(String className, MultipartFile robotFile, String jwt,
            String robotName) throws IOException {

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        Query query = new Query();

        query.addCriteria(Criteria.where("name").is(className));

        ClassUT classUT = mongoTemplate.findOne(query, ClassUT.class);

        if (classUT == null) {
            response.setMessage("Error, class not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        List<String> robotsConfig = Files.readAllLines(Paths.get(configRobotPath));

        if (!robotsConfig.contains(robotName)) {
            response.setMessage("Robot not available.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }

        List<String> robotNames = classUT.getRobotNames();

        if (robotNames.contains(robotName)) {
            fileSystemService.deleteTest(className, robotName);
        }

        // Salva il nuovo test
        Path path = fileSystemService.saveTest(className, robotName, robotFile);

        if (!robotNames.contains(robotName)) {
            classUT.addRobot(new Robot(robotName, path.toString() + "/"));
        }

        mongoTemplate.findAndReplace(query, classUT);
        response.setMessage("Robot setted");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);

    }

    // Elmina l'intera classe dal DB e dal filesystem
    public ResponseEntity<ApiResponse> deleteClass(String className, String jwt) throws IOException {
        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        Query query = new Query();

        query.addCriteria(Criteria.where("name").is(className));

        ClassUT classUT = mongoTemplate.findOne(query, ClassUT.class);

        if (classUT == null) {
            response.setMessage("Error, class not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        fileSystemService.deleteAll(className);
        mongoTemplate.findAndRemove(query, ClassUT.class);
        response.setMessage("Class deleted");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);

    }

    // Elimina un solo Robot dal DB e dal Filesystem
    public ResponseEntity<ApiResponse> deleteRobot(String className, String jwt,
            String robotName) throws IOException {

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        Query query = new Query();

        query.addCriteria(Criteria.where("name").is(className));

        ClassUT classUT = mongoTemplate.findOne(query, ClassUT.class);

        if (classUT == null) {
            response.setMessage("Error, class not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        List<String> robotNames = classUT.getRobotNames();

        if (!robotNames.contains(robotName)) {
            response.setMessage("Error, robot not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        fileSystemService.deleteTest(className, robotName);

        int index = robotNames.indexOf(robotName);

        List<Robot> robots = classUT.getRobots();

        robots.remove(index);

        mongoTemplate.findAndReplace(query, classUT);
        response.setMessage("Robot deleted");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    public ResponseEntity<ApiResponse> setFileSystem(String pathRequest, MultipartFile file, String jwt)
            throws IOException {

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        Path path = Paths.get(pathRequest);

        if (!fileSystemService.validatePath(path) || !fileSystemService.existsPath(path)) {
            response.setMessage("Error, path not valid");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        if (file == null) {
            response.setMessage("Error, file not present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        if (!Files.isDirectory(path)) {

            fileSystemService.deleteDirectory(path);

            fileSystemService.saveFile(file, path.getParent());

        } else {

            if (file.getOriginalFilename().endsWith(".zip")) {
                fileSystemService.unzip(file, path);
            } else {
                fileSystemService.saveFile(file, path);
            }
        }

        response.setMessage("Setted path element");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    public ResponseEntity<ApiResponse> deleteFileSystem(String pathRequest, String jwt) throws IOException {

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        Path path = Paths.get(pathRequest);

        if (fileSystemService.validatePath(path) && fileSystemService.existsPath(path)) {

            fileSystemService.deleteDirectory(path);

            response.setMessage("Deleted path element");
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
        } else {

            response.setMessage("Error, path not valid");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }
    }

    public ResponseEntity<ApiResponse> lock(String pathRequest, String jwt) throws InterruptedException {

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        Path path = Paths.get(pathRequest);

        System.out.println("[TA] Entro in lock con path: " + path.toString());

        ReentrantLock lock = new ReentrantLock();

        Thread worker = new Thread(() -> {
            try {
                runThread(lock, path);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        worker.start();

        System.out.println("[TA] wait sul lock di comunicazione");

        lock.wait();

        System.out.println("[TA] invio risposta di lock");

        response.setMessage("Path locked");

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    public ResponseEntity<ApiResponse> unlock(String pathRequest, String jwt) {

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        Path path = Paths.get(pathRequest);

        System.out.println("[TC] Entro in unlock con path: " + path.toString());

        System.out.println("[TC] notify sul lock del path ");

        fileSystemService.notifyReadPath(path);

        System.out.println("[TC] Invio messaggio di riposta unlock");

        response.setMessage("Path unlocked");

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);

    }

    // lock gia è wait quando viene passato
    private void runThread(ReentrantLock lock, Path path) throws InterruptedException {

        if (lock == null) {
            return;
        }

        System.out.println("[TB] start");

        fileSystemService.lockReadPath(path);

        System.out.println("[TB] lock sul lock del path");

        try {
            while (!lock.hasQueuedThreads()) {
                Thread.currentThread().sleep(3000);
            }

            System.out.println("[TB] notify sul lock di comunicazione");

            lock.notify();

            System.out.println("[TB] wait sul lock del path");

            fileSystemService.waitReadPath(path);
        } finally {
            System.out.println("[TB] unlock sul lock del path");

            fileSystemService.unlockReadPath(path);
        }
    }
}