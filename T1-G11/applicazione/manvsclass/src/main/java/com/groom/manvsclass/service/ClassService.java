package com.groom.manvsclass.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Robot;
import com.groom.manvsclass.repository.ClassRepository;
import com.groom.manvsclass.responses.FileResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ClassService {

    private static final Logger logger = LoggerFactory.getLogger(ClassService.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ClassRepository classRepository;

    public ResponseEntity<FileResponse> saveAll(MultipartFile classFile, String modelJSON,
            Map<String, MultipartFile> tests, String jwt, HttpServletRequest request) throws IOException {

        logger.info("Saving...");

        FileResponse response = new FileResponse();

        // ! CHECK: Token validation
        if (!jwtService.isJwtValid(jwt)) {
            logger.warn("Invalid token.");
            response.setMessage("Error, invalid token.");
            return new ResponseEntity<FileResponse>(response, HttpStatus.BAD_REQUEST);
        }

        if (classFile == null) {
            logger.warn("File not present.");
            response.setMessage("Errore, richiesto il file.java della classe.");
            return new ResponseEntity<FileResponse>(response, HttpStatus.BAD_REQUEST);
        }

        /*
         * SETUP FILES
         */
        // Inserts the JSON Data into a new classUT
        ObjectMapper mapper = new ObjectMapper();
        ClassUT classUT = mapper.readValue(modelJSON, ClassUT.class);

        if (classUT.getName() == "") {
            logger.warn("Class name not present.");
            response.setMessage("Errore, richiesto il nome della classe.");
            return new ResponseEntity<FileResponse>(response, HttpStatus.BAD_REQUEST);
        }

        // Controllo classe duplicata
        logger.info("Checking if class already exists.");
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(classUT.getName()));

        if (mongoTemplate.findOne(query, ClassUT.class) != null) {
            logger.warn("Class already in MongoDB");
            response.setMessage("Errore, classe già presente");
            return new ResponseEntity<FileResponse>(response, HttpStatus.BAD_REQUEST);
        }

        if (tests.containsKey("file")) {
            // In the tests Map there is also a duplicate of classFile remove it
            logger.debug("Class.java inside the Map.");
            tests.remove("file");
        }

        String className = classUT.getName();

        Path path = fileSystemService.saveClass(className, classFile);
        classUT.setcode_Uri(path.toString());

        // Robot adding to ClassUT and RobotFolder creation
        List<Robot> robots = new ArrayList<Robot>();
        for (String robotName : tests.keySet()) {

            path = fileSystemService.saveTest(className, robotName, tests.get(robotName));
            robots.add(new Robot(robotName, path.toString() + "/"));

        }
        classUT.setRobots(robots);

        logger.info("Robots saved successfully");

        // ! TO DELETE IN FUTURE VERSIONS
        logger.debug("Starting legacy upload...");
        adminService.uploadTest(classFile, modelJSON, tests.get("Randoop"), tests.get("Evosuite"), jwt, request);
        logger.debug("Legacy upload ended.");

        // Saves in MongoDB
        logger.debug("Saving class into the database...");
        classRepository.save(classUT);
        System.out.println("Class saved into the database");

        // Building response
        response.setFileName(classFile.getOriginalFilename());
        response.setSize(classFile.getSize());

        logger.info("Class saved successfully in the application: {}", className);

        return new ResponseEntity<FileResponse>(response, HttpStatus.OK);
    }

    public ResponseEntity<String> deleteAll(String className, String jwt) throws IOException {

        logger.info("Deleting Class and its Tests...");

        // ! CHECK: Token validation
        if (!jwtService.isJwtValid(jwt)) {
            logger.warn("Invalid token.");
            return new ResponseEntity<String>("Error, invalid token.", HttpStatus.BAD_REQUEST);
        }

        // ! CHECK: Class existence
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(className));

        if (mongoTemplate.findOne(query, ClassUT.class) == null) {
            logger.warn("Class not in database");
            return new ResponseEntity<String>("Questa classe non esiste.", HttpStatus.BAD_REQUEST);
        }

        logger.debug("Deleting from FileSystem...");
        fileSystemService.deleteAll(className);

        // ! When removed, insert the delete from MongoDB logic
        logger.debug("Starting legacy deletion...");
        adminService.eliminaClasse(className, jwt);
        logger.debug("Legacy deletion completed.");

        logger.info("Class and its Tests have been successfully eliminated.");

        return new ResponseEntity<String>("Class and its Tests have been successfully eliminated.", HttpStatus.OK);
    }
}
