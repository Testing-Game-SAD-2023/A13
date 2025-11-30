package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.Level;
import com.groom.manvsclass.service.LevelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller per la gestione dei livelli delle scalate
 */
@CrossOrigin
@RestController
@RequestMapping("/level")
public class LevelController {

    private final LevelService levelService;

    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }

    /**
     * Crea un nuovo livello
     * POST /adminService/level
     */
    @PostMapping
    public ResponseEntity<?> createLevel(@RequestBody Level level) {
        return levelService.createLevel(level);
    }

    /**
     * Recupera tutti i livelli di una scalata specifica
     * GET /level/scalata/{scalataName}
     */
    @GetMapping("/scalata/{scalataName}")
    public ResponseEntity<?> getLevelsByScalata(@PathVariable String scalataName) {
        return levelService.getLevelsByScalata(scalataName);
    }

    /**
     * Recupera un livello specifico per ID
     * GET /level/{idLevel}
     */
    @GetMapping("/{idLevel}")
    public ResponseEntity<?> getLevelById(@PathVariable int idLevel) {
        return levelService.getLevelById(idLevel);
    }

    /**
     * Aggiorna un livello esistente
     * PUT /level/{idLevel}
     */
    @PutMapping("/{idLevel}")
    public ResponseEntity<?> updateLevel(@PathVariable int idLevel, @RequestBody Level level) {
        return levelService.updateLevel(idLevel, level);
    }

    /**
     * Elimina un livello
     * DELETE /level/{idLevel}
     */
    @DeleteMapping("/{idLevel}")
    public ResponseEntity<?> deleteLevel(@PathVariable int idLevel) {
        return levelService.deleteLevel(idLevel);
    }

    /**
     * Recupera tutti i livelli (lista completa)
     * GET /level/all
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllLevels() {
        return levelService.getAllLevels();
    }
}
