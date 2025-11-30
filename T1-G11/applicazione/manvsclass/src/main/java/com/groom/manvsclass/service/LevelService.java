package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Level;
import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.model.repository.LevelRepository;
import com.groom.manvsclass.model.repository.ScalataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LevelService {

    private static final Logger logger = LoggerFactory.getLogger(LevelService.class);

    private final LevelRepository levelRepository;
    private final ScalataRepository scalataRepository;

    public LevelService(LevelRepository levelRepository, ScalataRepository scalataRepository) {
        this.levelRepository = levelRepository;
        this.scalataRepository = scalataRepository;
    }

    /**
     * Crea un nuovo livello
     */
    public ResponseEntity<?> createLevel(Level level) {
        try {
            logger.info("Creating new level with ID: {} for scalata: {}", level.getIdLevel(), level.getScalataName());

            // Verifica che la scalata esista
            Optional<Scalata> scalata = scalataRepository.findById(level.getScalataName());
            if (scalata.isEmpty()) {
                logger.warn("Scalata not found: {}", level.getScalataName());
                Map<String, String> error = new HashMap<>();
                error.put("error", "Scalata non trovata: " + level.getScalataName());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            // Verifica che non esista già un livello con lo stesso ID
            Optional<Level> existingLevel = levelRepository.findById(level.getIdLevel());
            if (existingLevel.isPresent()) {
                logger.warn("Level with ID {} already exists", level.getIdLevel());
                Map<String, String> error = new HashMap<>();
                error.put("error", "Livello con ID " + level.getIdLevel() + " già esistente");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }

            // Salva il livello
            Level savedLevel = levelRepository.save(level);

            // Aggiorna la scalata aggiungendo l'ID del livello alla lista
            Scalata scalataObj = scalata.get();
            List<Integer> levels = scalataObj.getLevels();
            if (!levels.contains(level.getIdLevel())) {
                levels.add(level.getIdLevel());
                scalataObj.setLevels(levels);
                scalataObj.setNumberOfLevels(levels.size());
                scalataRepository.save(scalataObj);
                logger.info("Updated scalata {} with new level", scalataObj.getScalataName());
            }

            logger.info("Level created successfully: {}", savedLevel);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Livello creato con successo");
            response.put("level", savedLevel);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            logger.error("Error creating level: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nella creazione del livello: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Recupera tutti i livelli di una scalata
     */
    public ResponseEntity<?> getLevelsByScalata(String scalataName) {
        try {
            logger.info("Retrieving levels for scalata: {}", scalataName);

            // Verifica che la scalata esista
            Optional<Scalata> scalata = scalataRepository.findById(scalataName);
            if (scalata.isEmpty()) {
                logger.warn("Scalata not found: {}", scalataName);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Scalata non trovata: " + scalataName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            List<Level> levels = levelRepository.findByScalataName(scalataName);
            logger.info("Found {} levels for scalata {}", levels.size(), scalataName);

            return ResponseEntity.ok(levels);

        } catch (Exception e) {
            logger.error("Error retrieving levels for scalata {}: {}", scalataName, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nel recupero dei livelli: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Recupera un livello specifico per ID
     */
    public ResponseEntity<?> getLevelById(int idLevel) {
        try {
            logger.info("Retrieving level with ID: {}", idLevel);

            Optional<Level> level = levelRepository.findById(idLevel);
            if (level.isEmpty()) {
                logger.warn("Level with ID {} not found", idLevel);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Livello non trovato con ID: " + idLevel);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            logger.info("Level found: {}", level.get());
            return ResponseEntity.ok(level.get());

        } catch (Exception e) {
            logger.error("Error retrieving level {}: {}", idLevel, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nel recupero del livello: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Aggiorna un livello esistente
     */
    public ResponseEntity<?> updateLevel(int idLevel, Level updatedLevel) {
        try {
            logger.info("Updating level with ID: {}", idLevel);

            Optional<Level> existingLevel = levelRepository.findById(idLevel);
            if (existingLevel.isEmpty()) {
                logger.warn("Level with ID {} not found", idLevel);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Livello non trovato con ID: " + idLevel);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            // Mantieni l'ID originale
            updatedLevel.setIdLevel(idLevel);

            // Salva le modifiche
            Level savedLevel = levelRepository.save(updatedLevel);
            logger.info("Level updated successfully: {}", savedLevel);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Livello aggiornato con successo");
            response.put("level", savedLevel);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error updating level {}: {}", idLevel, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nell'aggiornamento del livello: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Elimina un livello
     */
    public ResponseEntity<?> deleteLevel(int idLevel) {
        try {
            logger.info("Deleting level with ID: {}", idLevel);

            Optional<Level> level = levelRepository.findById(idLevel);
            if (level.isEmpty()) {
                logger.warn("Level with ID {} not found", idLevel);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Livello non trovato con ID: " + idLevel);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            Level levelObj = level.get();
            String scalataName = levelObj.getScalataName();

            // Elimina il livello
            levelRepository.deleteById(idLevel);
            logger.info("Level {} deleted", idLevel);

            // Rimuovi l'ID del livello dalla scalata
            Optional<Scalata> scalata = scalataRepository.findById(scalataName);
            if (scalata.isPresent()) {
                Scalata scalataObj = scalata.get();
                List<Integer> levels = scalataObj.getLevels();
                levels.remove(Integer.valueOf(idLevel));
                scalataObj.setLevels(levels);
                scalataObj.setNumberOfLevels(levels.size());
                scalataRepository.save(scalataObj);
                logger.info("Updated scalata {} after level deletion", scalataName);
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", "Livello eliminato con successo");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error deleting level {}: {}", idLevel, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nell'eliminazione del livello: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Recupera tutti i livelli (lista completa)
     */
    public ResponseEntity<?> getAllLevels() {
        try {
            logger.info("Retrieving all levels");
            List<Level> levels = levelRepository.findAll();
            logger.info("Found {} levels in total", levels.size());
            return ResponseEntity.ok(levels);
        } catch (Exception e) {
            logger.error("Error retrieving all levels: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Errore nel recupero di tutti i livelli: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
