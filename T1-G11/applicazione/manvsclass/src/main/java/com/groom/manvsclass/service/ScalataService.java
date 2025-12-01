/*MODIFICA (5/11/2024) - Refactoring task T1
 * ScalataService ora si occupa di implementare i servizi relativi alla modalità scalata
 */
package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Level;
import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.model.repository.LevelRepository;
import com.groom.manvsclass.model.repository.ScalataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ScalataService {

    private static final Logger logger = LoggerFactory.getLogger(ScalataService.class);
    @Autowired
    private ScalataRepository scalata_repo;
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private JwtService jwtService;

     
    public ResponseEntity<?> uploadScalata(Scalata scalata) {
        /*
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("(POST /configureScalata) Attenzione, non sei loggato!");
        }*/
        Scalata new_scalata = new Scalata();
        new_scalata.setUsername(scalata.getUsername());
        new_scalata.setScalataName(scalata.getScalataName());
        new_scalata.setScalataDescription(scalata.getScalataDescription());
        new_scalata.setNumberOfLevels(scalata.getNumberOfLevels());
        new_scalata.setLevels(scalata.getLevels());

        scalata_repo.save(new_scalata);
        return ResponseEntity.ok().body(new_scalata);
    }

    public ResponseEntity<?> listScalate() {
        List<Scalata> scalate = scalata_repo.findAll();
        return new ResponseEntity<>(scalate, HttpStatus.OK);
    }

      public ResponseEntity<?> deleteScalataByName(String scalataName) {

        List<Scalata> scalata = scalata_repo.findByScalataNameContaining(scalataName);
        if (scalata.isEmpty()) {
            return new ResponseEntity<>("Scalata con nome: " + scalataName + " non trovata", HttpStatus.NOT_FOUND);
        } else {
            scalata_repo.delete(scalata.get(0));
            return new ResponseEntity<>("Scalata con nome: " + scalataName + " rimossa", HttpStatus.OK);
        }
    }

    /*  metodo che verifica jwt, per testing eliminato per evitare dipendenza da JwtService in ScalataService
    public ResponseEntity<?> deleteScalataByName(String scalataName, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("(DELETE /delete_scalata/{scalataName}) Attenzione, non sei loggato!");
        }

        List<Scalata> scalata = scalata_repo.findByScalataNameContaining(scalataName);
        if (scalata.isEmpty()) {
            return new ResponseEntity<>("Scalata con nome: " + scalataName + " non trovata", HttpStatus.NOT_FOUND);
        } else {
            scalata_repo.delete(scalata.get(0));
            return new ResponseEntity<>("Scalata con nome: " + scalataName + " rimossa", HttpStatus.OK);
        }
    }
    */
    public ResponseEntity<?> retrieveScalataByName(String scalataName) {
        List<Scalata> scalata = scalata_repo.findByScalataNameContaining(scalataName);
        if (scalata.isEmpty()) {
            return new ResponseEntity<>("Scalata with name: " + scalataName + " not found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(scalata, HttpStatus.OK);
        }
    }

    /**
     * Recupera il livello i-esimo di una scalata specifica.
     * 
     * @param scalataName Nome della scalata
     * @param currentLevel Posizione del livello (1-based: 1 = primo livello, 2 = secondo, etc.)
     * @return ResponseEntity con i dati del Level o errore
     */
    public ResponseEntity<?> getLevelByPosition(String scalataName, int currentLevel) {
        try {
            logger.info("Retrieving level {} for scalata: {}", currentLevel, scalataName);

            // 1. Trova la scalata
            List<Scalata> scalate = scalata_repo.findByScalataNameContaining(scalataName);
            if (scalate.isEmpty()) {
                logger.warn("Scalata '{}' not found", scalataName);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Scalata non trovata: " + scalataName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            Scalata scalata = scalate.get(0);
            
            // 2. Verifica che currentLevel sia valido
            if (currentLevel < 1 || currentLevel > scalata.getLevels().size()) {
                logger.warn("Invalid level {} for scalata '{}' (total levels: {})", 
                           currentLevel, scalataName, scalata.getLevels().size());
                Map<String, String> error = new HashMap<>();
                error.put("error", "Livello non valido. Scalata ha " + scalata.getLevels().size() + " livelli");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            // 3. Estrai l'ID del livello dall'array (currentLevel è 1-based)
            Integer levelId = scalata.getLevels().get(currentLevel - 1);
            logger.info("Level {} of scalata '{}' corresponds to levelId: {}", 
                       currentLevel, scalataName, levelId);

            // 4. Carica il Level dal repository
            Optional<Level> level = levelRepository.findById(levelId);
            if (level.isEmpty()) {
                logger.error("Level with ID {} not found (referenced by scalata '{}')", 
                            levelId, scalataName);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Dati livello corrotti - ID " + levelId + " non trovato");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }

            logger.info("Level found: {}", level.get());
            return ResponseEntity.ok(level.get());

        } catch (Exception e) {
            logger.error("Error retrieving level for scalata '{}': {}", scalataName, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore interno: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}