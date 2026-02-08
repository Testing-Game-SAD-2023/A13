package com.example.db_setup.service;

import com.example.db_setup.model.GameProgress;
import com.example.db_setup.model.dto.gamification.GameProgressDTO;
import com.example.db_setup.model.repository.GameProgressRepository;
import com.example.db_setup.mapper.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class GameProgressService {

    private static final Logger logger =
            LoggerFactory.getLogger(GameProgressService.class);

    private final GameProgressRepository gameProgressRepository;
    private final MapperFacade mapperFacade;

    public GameProgressService(GameProgressRepository gameProgressRepository, MapperFacade mapperFacade) {
        this.gameProgressRepository = gameProgressRepository;
        this.mapperFacade = mapperFacade;
    }

    /**
     * Recupera lo storico completo delle partite di un player
     */
    public List<GameProgressDTO> getGameHistoryByPlayer(Long playerId) {
        logger.info("Recupero storico partite per playerId={}", playerId);

        if (playerId == null) {
            throw new IllegalArgumentException("playerId nullo");
        }

        List<GameProgress> entities = gameProgressRepository.findGameProgressByPlayer(playerId);
        logger.info("Entità recuperate: {}", entities);

        List<GameProgressDTO> results = entities.stream()
                .map(mapperFacade::toDTO)
                .toList();

        logger.info("Results DTO: {}", results);

        return results;
    }

}
