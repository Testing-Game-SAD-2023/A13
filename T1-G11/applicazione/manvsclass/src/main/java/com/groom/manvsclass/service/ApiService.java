package com.groom.manvsclass.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

    @Value("${filesystem.classesPath}")
    private String classesPath;

    @Value("${filesystem.sourceFolder}")
    private String sourceFolder;

    @Value("${filesystem.testsFolder}")
    private String testsFolder;

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

        List<Robot> robots = classUT.getRobots();

        for (Robot robot : robots) {
            robotsnames.add(robot.getRobotName());
        }

        if (robots.isEmpty()) {
            response.setMessage("Robots not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        response.setData(robotsnames);
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

        List<Robot> robots = classUT.getRobots();

        for (Robot robot : robots) {

            if (robot.getRobotName().equals(robotName)) {
                response.setData(robot.getRobotFile());
                response.setMessage("Robot found");
                return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
            }

        }

        response.setMessage("Error, robot not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);

    }

    // Aggiunge o sovrascrive una classe. Deve fare upload path nel DB e deve
    // ripulire la cartella prima di aggiungere
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

        fileSystemService.deleteDirectory(Paths.get(classesPath + className));

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

        List<Robot> robots = classUT.getRobots();

        List<String> robotsConfig = Files.readAllLines(Paths.get(configRobotPath));

        if (!robotsConfig.contains(robotName)) {
            response.setMessage("Robot not available.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } else {

            // Recupera ed elimina il documento dal DB temporaneamente
            // classUT = mongoTemplate.findAndRemove(query, ClassUT.class);

            // Pulisce la directory con i file del robot se esiste
            boolean find = false;
            int index = 0;
            for (Robot robot : robots) {

                if (robot.getRobotName().equals(robotName)) {
                    find = true;
                    index = robots.indexOf(robot);
                }
            }

            if (find) {
                fileSystemService.deleteDirectory(Paths.get(classesPath + className + "/" + testsFolder + robotName));
            }

            // Salva il nuovo test
            Path path = fileSystemService.saveTest(className, robotName, robotFile);

            // Aggiunge il robot alla classe solo se è un INSERT (nel caso di UPDATE cambio
            // solo il path)
            if (!find) {
                classUT.addRobot(new Robot(robotName, path.toString() + "/"));
            } else {
                Robot robot = robots.get(index);
                robot.setRobotFile(path.toString() + "/");
            }

            mongoTemplate.findAndReplace(query, classUT);
            response.setMessage("Robot setted");
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
        }

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

        List<Robot> robots = classUT.getRobots();
        boolean find = false;
        int index = 0;
        for (Robot robot : robots) {

            if (robot.getRobotName().equals(robotName)) {
                find = true;
                index = robots.indexOf(robot);
            }
        }

        if (!find) {
            response.setMessage("Error, robot not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        fileSystemService.deleteDirectory(Paths.get(classesPath + className + "/" + testsFolder + robotName));

        robots.remove(index);

        mongoTemplate.findAndReplace(query, classUT);
        response.setMessage("Robot deleted");
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }
}