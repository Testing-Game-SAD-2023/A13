package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.service.ScalataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/scalata")
public class ScalataController {

    private final ScalataService scalataService;

    public ScalataController(ScalataService scalataService) {
        this.scalataService = scalataService;
    }

    @PostMapping("/configureScalata")
    public ResponseEntity<?> uploadScalata(@RequestBody Scalata scalata, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
        return scalataService.uploadScalata(scalata);
    }

    @GetMapping("/scalate_list")
    public ResponseEntity<?> listScalate() {
        return scalataService.listScalate();
    }

    @DeleteMapping("delete_scalata/{scalataName}")
    public ResponseEntity<?> deleteScalataByName(@PathVariable String scalataName, @CookieValue(name = "jwt", required = false) String jwt) {
        return scalataService.deleteScalataByName(scalataName);
    }

    @GetMapping("/retrieve_scalata/{scalataName}")
    public ResponseEntity<?> retrieveScalataByName(@PathVariable String scalataName) {
        return scalataService.retrieveScalataByName(scalataName);
    }

    /**
     * Recupera il livello i-esimo di una scalata.
     * Path: /scalata/{scalataName}/level/{currentLevel}
     * 
     * @param scalataName Nome della scalata (es: "Scalata Facile")
     * @param currentLevel Numero del livello (1-based: 1=primo, 2=secondo, etc.)
     * @return I dati del Level corrispondente
     */
    @GetMapping("/{scalataName}/level/{currentLevel}")
    public ResponseEntity<?> getLevelByPosition(
            @PathVariable String scalataName,
            @PathVariable int currentLevel) {
        return scalataService.getLevelByPosition(scalataName, currentLevel);
    }
}
