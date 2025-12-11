package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Level;
import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.model.dto.LevelDTO;
import com.groom.manvsclass.model.dto.ScalataDTO;
import com.groom.manvsclass.mapper.LevelMapper;
import com.groom.manvsclass.mapper.ScalataMapper;
import com.groom.manvsclass.model.repository.ScalataRepository;
import com.groom.manvsclass.service.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScalataService {

    private static final Logger logger = LoggerFactory.getLogger(ScalataService.class);

    private final ScalataRepository scalataRepository;
    private final ScalataMapper scalataMapper;
    private final LevelMapper levelMapper;

    public ScalataService(ScalataRepository scalataRepository,
                          ScalataMapper scalataMapper,
                          LevelMapper levelMapper) {
        this.scalataRepository = scalataRepository;
        this.scalataMapper = scalataMapper;
        this.levelMapper = levelMapper;
    }

    /**
     * Crea una nuova scalata.
     */
    public void createScalata(ScalataDTO scalataDTO) {
        logger.info("[CREATE] Received ScalataDTO: {}", scalataDTO);

        // Riferimento delle classi scelte
        Set<String> classUT = new HashSet<>();

        // Scalata con ugual nome già esistente
        if (scalataRepository.findById(scalataDTO.getName()).isPresent()) {
            throw new ScalataAlreadyExistsException("Scalata already exists");
        }

        // Scalata con meno di 2 livelli
        if (scalataDTO.getNumberOfLevels() < 2) {
            throw new ScalataLevelException("Scalata levels less than 2");
        }

        for (LevelDTO levelDTO : scalataDTO.getListOfLevels()) {
            // Scalata con livelli a tempo negativo o nullo
            if (levelDTO.getTempoMax() <= 0) {
                throw new NegativeTempoMaxException("tempoMax cannot be negative");
            }

            // Scalata con livelli con stessa classe UT
            if (classUT.contains(levelDTO.getOpponent().getClassUT())) {
                throw new LevelsSameClassException("levels must have different classUT");
            } else {
                classUT.add(levelDTO.getOpponent().getClassUT());
            }

        }

        Scalata scalata = scalataMapper.scalatafromScalataDTO(scalataDTO);
        scalata.setName(scalataDTO.getName().trim());
        scalataRepository.save(scalata);

        logger.info("[CREATE] Scalata '{}' saved successfully", scalata.getName());
    }

    /**
     * Restituisce tutte le scalate.
     */
    public List<ScalataDTO> getAll() {
        logger.info("[GET ALL] Retrieving all scalate");
        List<Scalata> scalate = scalataRepository.findAll();
        List<ScalataDTO> dtos = scalate.stream()
                .map(scalataMapper::scalatatoScalataDTO)
                .collect(Collectors.toList());
        logger.info("[GET ALL] Found {} scalate", dtos.size());
        return dtos;
    }

    /**
     * Recupera scalata per nome.
     */
    public ScalataDTO getScalataByName(String name) {
        logger.info("[GET] Retrieving scalata by name: {}", name);
        Scalata scalata = scalataRepository.findById(name)
                // se la Scalata non esiste
                .orElseThrow(() -> new ScalataNotFoundException("Scalata with name '" + name + "' not found"));
        return scalataMapper.scalatatoScalataDTO(scalata);
    }

    /**
     * Recupera un livello specifico di una scalata dato il nome della scalata
     * e l'indice (numero) del livello nella lista.
     */
    public LevelDTO getLevel(String name, int number) {
        logger.info("[GET LEVEL] Retrieving level at index {} for scalata: {}", number, name);
        Scalata scalata = scalataRepository.findById(name)
                // Se la Scalata non esiste, lancia l'eccezione
                .orElseThrow(() -> new ScalataNotFoundException("Scalata with name '" + name + "' not found"));

        Level level;
        try {
            level = scalata.getListOfLevels().get(number);
        } catch (IndexOutOfBoundsException e) {
            throw new LevelNotFoundException("Level '" + number + "' in Scalata with name '" + name + "' not found");
        }

        return levelMapper.leveltoLevelDTO(level);
    }

    /**
     * Recupera i livelli di una scalata per nome.
     */
    public List<LevelDTO> getLevels(String name) {
        logger.info("[GET LEVELS] Retrieving levels for scalata: {}", name);
        Scalata scalata = scalataRepository.findById(name)
                // se la Scalata non esiste
                .orElseThrow(() -> new ScalataNotFoundException("Scalata with name '" + name + "' not found"));
        return scalata.getListOfLevels().stream()
                .map(levelMapper::leveltoLevelDTO)
                .collect(Collectors.toList());
    }

    /**
     * Elimina una scalata per nome.
     */
    public ResponseEntity<String> deleteScalata(String name) {
        logger.info("[DELETE] Deleting scalata: {}", name);
        return scalataRepository.findById(name)
                .map(scalata -> {
                    scalataRepository.delete(scalata);
                    logger.info("[DELETE] Scalata '{}' deleted successfully", name);
                    return ResponseEntity.ok("Scalata deleted");
                })
                .orElseGet(() -> {
                    logger.warn("[DELETE] Scalata '{}' not found", name);
                    throw new ScalataNotFoundException("Scalata with name '" + name + "' not found");
                });
    }
}
