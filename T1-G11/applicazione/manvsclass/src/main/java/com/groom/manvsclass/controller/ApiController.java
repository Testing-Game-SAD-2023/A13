package com.groom.manvsclass.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.groom.manvsclass.model.PathRequest;
import com.groom.manvsclass.responses.ApiResponse;
import com.groom.manvsclass.service.ApiService;

@CrossOrigin
@Controller
public class ApiController {

    @Autowired
    private ApiService apiService;

    // Restituisce la lista dei nomi delle classi disponibili
    @GetMapping("classes")
    public ResponseEntity<ApiResponse> getClasses(@CookieValue(name = "jwt", required = false) String jwt) {
        return apiService.getClasses(jwt);
    }

    // Restituisce il path in cui si trova una specifica classe
    @GetMapping("classes/{className}")
    public ResponseEntity<ApiResponse> getClass(@PathVariable(value = "className") String className,
            @CookieValue(name = "jwt", required = false) String jwt) {

        System.out.println("Entro nel controller con: " + className);
        return apiService.getClass(className, jwt);
    }

    // Restituisce la lista di Robot associati ad una classe
    @GetMapping("classes/{className}/robots")
    public ResponseEntity<ApiResponse> getRobots(@PathVariable(value = "className") String className,
            @CookieValue(name = "jwt", required = false) String jwt) {
        return apiService.getRobots(className, jwt);
    }

    // Restituisce il path di uno specifico robot per una specifica classe
    @GetMapping("classes/{className}/{robotName}")
    public ResponseEntity<ApiResponse> getRobot(@PathVariable(value = "className") String className,
            @PathVariable(value = "robotName") String robotName,
            @CookieValue(name = "jwt", required = false) String jwt) {

        return apiService.getRobot(className, robotName, jwt);
    }

    // Salva un classe UT
    @PostMapping("classes/{className}")
    public ResponseEntity<ApiResponse> setClass(@PathVariable(value = "className") String className,
            @RequestParam(name = "classFile", required = false) MultipartFile classFile,
            @CookieValue(name = "jwt", required = false) String jwt) throws IOException {

        return apiService.setClass(className, classFile, jwt);
    }

    // Salva il test per una classe UT
    @PostMapping("classes/{className}/{robotName}")
    public ResponseEntity<ApiResponse> setRobot(@PathVariable(value = "className") String className,
            @PathVariable(value = "robotName") String robotName,
            @RequestParam(name = "robotFile", required = false) MultipartFile robotFile,
            @CookieValue(name = "jwt", required = false) String jwt) throws IOException {

        return apiService.setRobot(className, robotFile, jwt, robotName);
    }

    // Salva un classe UT
    @DeleteMapping("classes/{className}")
    public ResponseEntity<ApiResponse> deleteClass(@PathVariable(value = "className") String className,
            @CookieValue(name = "jwt", required = false) String jwt) throws IOException {

        return apiService.deleteClass(className, jwt);
    }

    // Salva il test per una classe UT
    @DeleteMapping("classes/{className}/{robotName}")
    public ResponseEntity<ApiResponse> deleteRobot(@PathVariable(value = "className") String className,
            @PathVariable(value = "robotName") String robotName,
            @CookieValue(name = "jwt", required = false) String jwt) throws IOException {

        return apiService.deleteRobot(className, jwt, robotName);
    }

    @GetMapping("fileSystem")
    public ResponseEntity<ApiResponse> getFileSystem(@RequestBody PathRequest pathRequest,
            @CookieValue(name = "jwt", required = false) String jwt) {

        return null;
    }
}