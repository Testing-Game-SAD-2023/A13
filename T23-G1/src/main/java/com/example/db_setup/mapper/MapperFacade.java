package com.example.db_setup.mapper;

import com.example.db_setup.model.GameProgress;
import com.example.db_setup.model.Opponent;
import com.example.db_setup.model.PlayerProgress;
import com.example.db_setup.model.dto.gamification.GameProgressDTO;
import com.example.db_setup.model.dto.gamification.OpponentDTO;
import com.example.db_setup.model.dto.gamification.PlayerProgressDTO;
import org.springframework.stereotype.Service;

/**
 * Classe facade per la gestione centralizzata dei mapper tra entità e DTO del sistema.
 * <p>
 * Questa classe fornisce metodi statici di utilità per convertire tra entità e
 * oggetti DTO utilizzando i mapper specifici generati da MapStruct.
 * </p>
 *
 * <p>
 * I mapper interni gestiti sono:
 * <ul>
 *   <li>{@link OpponentMapper} per le entità {@link Opponent}</li>
 *   <li>{@link PlayerProgressMapper} per le entità {@link PlayerProgress}</li>
 *   <li>{@link GameProgressMapper} per le entità {@link GameProgress}</li>
 * </ul>
 * </p>
 */
@Service
public class MapperFacade {
    private final OpponentMapper opponentMapper;
    private final PlayerProgressMapper playerProgressMapper;
    private final GameProgressMapper gameProgressMapper;

    public MapperFacade(OpponentMapper opponentMapper,
                        PlayerProgressMapper playerProgressMapper,
                        GameProgressMapper gameProgressMapper) {
        this.opponentMapper = opponentMapper;
        this.playerProgressMapper = playerProgressMapper;
        this.gameProgressMapper = gameProgressMapper;
    }

    public OpponentDTO toDTO(Opponent opponent) {
        return opponentMapper.opponentToOpponentDTO(opponent);
    }

    public PlayerProgressDTO toDTO(PlayerProgress playerProgress) {
        return playerProgressMapper.playerProgressToPlayerProgressDTO(playerProgress);
    }

    public GameProgressDTO toDTO(GameProgress gameProgress) {
        return gameProgressMapper.gameProgressToGameProgressDTO(gameProgress);
    }
}
