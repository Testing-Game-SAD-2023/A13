package com.groom.manvsclass.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

    public ResponseEntity<ApiResponse> getClasses(String jwt) {

        logger.info("Received request to retrieve all classes.");

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * logger.warn("Invalid JWT provided for the request. JWT: {}", jwt);
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        List<String> names = new ArrayList<>();

        List<ClassUT> classes = classRepository.findAll();

        if (classes.isEmpty()) {
            logger.warn("No classes found in the database.");
            response.setMessage("Error, classes not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        for (ClassUT classUT : classes) {
            names.add(classUT.getName());
        }

        logger.info("Successfully retrieved {} classes.", names.size());
        response.setData(names);
        response.setMessage("Classes found");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);

    }

    public ResponseEntity<ApiResponse> getClass(String className, String jwt) {

        logger.info("Received request to retrieve class with name: {}", className);

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * logger.warn("Invalid JWT provided for the request. JWT: {}", jwt);
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
            logger.warn("Class not found for name: {}", className);
            response.setMessage("Error, class not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        logger.info("Successfully retrieved class with name: {}", className);
        response.setData(classUT.getcode_Uri());
        response.setMessage("Class found");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);

    }

    public ResponseEntity<ApiResponse> getRobots(String className, String jwt) {

        logger.info("Received request to retrieve robots for class: {}", className);

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * logger.warn("Invalid JWT provided for the request. JWT: {}", jwt);
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
            logger.warn("Class not found for name: {}", className);
            response.setMessage("Error, class not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        List<String> robotNames = classUT.getRobotNames();

        if (robotNames.isEmpty()) {
            logger.warn("No robots found for class: {}", className);
            response.setMessage("Robots not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        logger.info("Successfully retrieved {} robots for class: {}", robotNames.size(), className);
        response.setData(robotNames);
        response.setMessage("Robots found");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    public ResponseEntity<ApiResponse> getRobot(String className, String robotName, String jwt) {

        logger.info("Received request to retrieve robot '{}' for class: {}", robotName, className);

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * logger.warn("Invalid JWT provided for the request. JWT: {}", jwt);
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
            logger.warn("Class not found for name: {}", className);
            response.setMessage("Error, class not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        List<String> robotNames = classUT.getRobotNames();

        if (robotNames.contains(robotName)) {
            logger.info("Robot '{}' found for class '{}'. Path: {}", robotName, className,
                    classUT.getRobotPath(robotName));
            response.setData(classUT.getRobotPath(robotName));
            response.setMessage("Robot found");
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        logger.warn("Robot '{}' not found for class: {}", robotName, className);
        response.setMessage("Error, robot not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);

    }

    public ResponseEntity<ApiResponse> setClass(String className, MultipartFile classFile, String jwt)
            throws IOException {

        logger.info("Received request to set class '{}'.", className);

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * logger.warn("Invalid JWT provided for the request. JWT: {}", jwt);
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
            logger.warn("Class '{}' not found in the database.", className);
            response.setMessage("Error, class not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        logger.info("Deleting existing files for class '{}'.", className);
        fileSystemService.deleteClass(className);

        logger.info("Saving new class file for class '{}'.", className);
        Path path = fileSystemService.saveClass(className, classFile);

        logger.info("Updating database entry for class '{}'.", className);
        classUT.setUri(path.toString());

        mongoTemplate.findAndReplace(query, classUT);
        logger.info("Successfully updated class '{}'. File path: {}", className, path.toString());
        response.setMessage("Class setted");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);

    }

    public ResponseEntity<ApiResponse> setRobot(String className, MultipartFile robotFile, String jwt,
            String robotName) throws IOException {

        logger.info("Received request to set robot '{}' for class '{}'.", robotName, className);

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * logger.warn("Invalid JWT provided for the request. JWT: {}", jwt);
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
            logger.warn("Class '{}' not found in the database.", className);
            response.setMessage("Error, class not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        logger.info("Reading robot configurations from '{}'.", configRobotPath);
        List<String> robotsConfig = Files.readAllLines(Paths.get(configRobotPath));

        if (!robotsConfig.contains(robotName)) {
            logger.warn("Robot '{}' is not available according to configuration.", robotName);
            response.setMessage("Robot not available.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }

        List<String> robotNames = classUT.getRobotNames();

        if (robotNames.contains(robotName)) {
            logger.info("Robot '{}' already exists for class '{}'. Deleting existing test files.", robotName,
                    className);
            fileSystemService.deleteTest(className, robotName);
        }

        // Salva il nuovo test
        logger.info("Saving new robot '{}' test file for class '{}'.", robotName, className);
        Path path = fileSystemService.saveTest(className, robotName, robotFile);

        if (!robotNames.contains(robotName)) {
            logger.info("Adding robot '{}' to class '{}'.", robotName, className);
            classUT.addRobot(new Robot(robotName, path.toString() + "/"));
        }

        logger.info("Updating database entry for class '{}'.", className);
        mongoTemplate.findAndReplace(query, classUT);

        logger.info("Successfully set robot '{}' for class '{}'.", robotName, className);
        response.setMessage("Robot setted");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);

    }

    public ResponseEntity<ApiResponse> deleteClass(String className, String jwt) throws IOException {

        logger.info("Received request to delete class '{}'.", className);

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * logger.warn("Invalid JWT provided for the request. JWT: {}", jwt);
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
            logger.warn("Class '{}' not found in the database.", className);
            response.setMessage("Error, class not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }
        logger.info("Deleting all files associated with class '{}'.", className);
        fileSystemService.deleteAll(className);

        logger.info("Removing class '{}' from the database.", className);
        mongoTemplate.findAndRemove(query, ClassUT.class);

        logger.info("Successfully deleted class '{}'.", className);
        response.setMessage("Class deleted");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);

    }

    public ResponseEntity<ApiResponse> deleteRobot(String className, String jwt,
            String robotName) throws IOException {

        logger.info("Received request to delete robot '{}' from class '{}'.", robotName, className);

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * logger.warn("Invalid JWT provided for the request. JWT: {}", jwt);
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
            logger.warn("Class '{}' not found in the database.", className);
            response.setMessage("Error, class not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        List<String> robotNames = classUT.getRobotNames();

        if (!robotNames.contains(robotName)) {
            logger.warn("Robot '{}' not found in class '{}'.", robotName, className);
            response.setMessage("Error, robot not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        logger.info("Deleting files associated with robot '{}' from class '{}'.", robotName, className);
        fileSystemService.deleteTest(className, robotName);

        int index = robotNames.indexOf(robotName);

        List<Robot> robots = classUT.getRobots();

        robots.remove(index);

        logger.info("Updating class '{}' in the database after robot deletion.", className);
        mongoTemplate.findAndReplace(query, classUT);

        logger.info("Successfully deleted robot '{}' from class '{}'.", robotName, className);
        response.setMessage("Robot deleted");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    public ResponseEntity<ApiResponse> setFileSystem(String pathRequest, MultipartFile file, String jwt)
            throws IOException {

        logger.info("Received request to set file system at path '{}'.", pathRequest);
        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * logger.warn("Invalid JWT provided for the request. JWT: {}", jwt);
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        Path path = Paths.get(pathRequest);

        if (!fileSystemService.validatePath(path) || !fileSystemService.existsPath(path)) {
            logger.warn("Invalid path provided for the request. Path: {}", pathRequest);
            response.setMessage("Error, path not valid");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        if (file == null) {
            logger.warn("No file provided for the request. Path: {}", pathRequest);
            response.setMessage("Error, file not present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        if (!Files.isDirectory(path)) {
            logger.info("Path '{}' is not a directory. Deleting directory and saving file.", pathRequest);
            fileSystemService.deleteDirectory(path);

            logger.info("Saving file '{}' to parent directory '{}'.", file.getOriginalFilename(), path.getParent());
            fileSystemService.saveFile(file, path.getParent());

        } else {

            if (file.getOriginalFilename().endsWith(".zip")) {
                logger.info("Unzipping file '{}' to path '{}'.", file.getOriginalFilename(), pathRequest);
                fileSystemService.unzip(file, path);
            } else {
                logger.info("Saving file '{}' to path '{}'.", file.getOriginalFilename(), pathRequest);
                fileSystemService.saveFile(file, path);
            }
        }

        logger.info("Successfully set file system for path '{}'.", pathRequest);
        response.setMessage("Path element setted");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    public ResponseEntity<ApiResponse> deleteFileSystem(String pathRequest, String jwt) throws IOException {

        logger.info("Received request to delete file system at path '{}'.", pathRequest);

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * logger.warn("Invalid JWT provided for the request. JWT: {}", jwt);
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        Path path = Paths.get(pathRequest);

        if (fileSystemService.validatePath(path) && fileSystemService.existsPath(path)) {

            logger.info("Path '{}' is valid and exists. Proceeding with deletion.", pathRequest);

            fileSystemService.deleteDirectory(path);

            logger.info("Successfully deleted file system at path '{}'.", pathRequest);
            response.setMessage("Path element deleted");
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
        } else {
            logger.warn("Invalid or non-existent path provided: '{}'.", pathRequest);
            response.setMessage("Error, path not valid");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }
    }

    public ResponseEntity<ApiResponse> lock(String pathRequest, String jwt) throws InterruptedException {

        logger.info("Received request to lock path '{}'.", pathRequest);

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * logger.warn("Invalid JWT provided for the request. JWT: {}", jwt);
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        Path path = Paths.get(pathRequest);

        if (!fileSystemService.validatePath(path) || !fileSystemService.existsPath(path)) {
            logger.warn("Invalid or non-existent path provided: '{}'.", pathRequest);
            response.setMessage("Error, path not valid");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        logger.info("Locking process initiated for path: '{}'.", pathRequest);

        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        AtomicBoolean isReady = new AtomicBoolean(false);

        Thread worker = new Thread(() -> {
            try {
                logger.info("Worker thread '{}' started for path '{}'.", Thread.currentThread().getName(), pathRequest);
                runThread(lock, condition, isReady, path);
            } catch (InterruptedException e) {
                logger.error("Worker thread '{}' interrupted for path '{}': {}", Thread.currentThread().getName(),
                        pathRequest, e.getMessage());
                e.printStackTrace();
            }
        });

        worker.start();

        boolean signalled = false;
        lock.lock();
        try {
            while (!isReady.get()) {
                logger.info("Worker thread '{}' waited on comunication lock for path '{}'.",
                        Thread.currentThread().getName(), pathRequest);
                signalled = condition.await(20, TimeUnit.SECONDS);
            }
        } finally {
            lock.unlock();
        }

        if (!signalled) {
            logger.warn("Timeout occurred while waiting for lock on path '{}'. Thread '{}'.", pathRequest,
                    Thread.currentThread().getName());
            response.setMessage("TIMEOUT");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        logger.info("Successfully locked path '{}'. Sending response from thread '{}'.", pathRequest,
                Thread.currentThread().getName());
        response.setMessage("Path locked");

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    public ResponseEntity<ApiResponse> unlock(String pathRequest, String jwt) throws InterruptedException {

        logger.info("Received request to unlock path '{}'.", pathRequest);

        ApiResponse response = new ApiResponse();

        /*
         * if (!jwtService.isJwtValid(jwt)) {
         * logger.warn("Invalid JWT provided for the request. JWT: {}", jwt);
         * response.setMessage("Error, token not valid");
         * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.
         * APPLICATION_JSON)
         * .body(response);
         * }
         */

        Path path = Paths.get(pathRequest);

        if (!fileSystemService.validatePath(path) || !fileSystemService.existsPath(path)) {
            logger.warn("Invalid or non-existent path provided: '{}'.", pathRequest);
            response.setMessage("Error, path not valid");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        logger.info("Unlock process initiated for path '{}'.", pathRequest);

        logger.info("Thread '{}' entering unlock process for path '{}'.", Thread.currentThread().getName(),
                pathRequest);

        logger.info("Notifying lock release on path '{}'.", pathRequest);

        fileSystemService.notify(path);

        logger.info("Successfully unlocked path '{}'. Sending response from thread '{}'.", pathRequest,
                Thread.currentThread().getName());

        response.setMessage("Path unlocked");

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);

    }

    private void runThread(ReentrantLock lock, Condition condition, AtomicBoolean isReady, Path path)
            throws InterruptedException {

        if (lock == null) {
            return;
        }

        System.out.println("[TB] start");

        fileSystemService.readLock(path);

        System.out.println("[TB] lock sul lock del path");

        lock.lock();
        try {
            isReady.set(true);
            System.out.println("[TB] notify sul lock di comunicazione");
            condition.signal();
        } finally {
            lock.unlock();
        }

        System.out.println("[TB] wait sul lock del path");

        fileSystemService.wait(path);

        System.out.println("[TB] unlock sul lock del path");

        fileSystemService.readUnlock(path);
    }
}