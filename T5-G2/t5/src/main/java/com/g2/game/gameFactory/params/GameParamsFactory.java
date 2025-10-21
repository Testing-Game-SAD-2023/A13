package com.g2.game.gameFactory.params;

import com.g2.game.gameDTO.CreateSessionDTO.PartitaSingolaSessionDTO;
import com.g2.game.gameDTO.CreateSessionDTO.SessionDTO;
import com.g2.game.gameDTO.RunGameDTO.RunGameRequestDTO;
import com.g2.game.gameDTO.RunGameDTO.RunPartitaSingolaRequestDTO;
import com.g2.game.gameDTO.StartGameDTO.StartGameRequestDTO;
import com.g2.game.gameDTO.StartGameDTO.StartPartitaSingolaRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameParamsFactory {

    private static final Logger logger = LoggerFactory.getLogger(GameParamsFactory.class);

    // Inizializza un oggetto GameParams per creare una nuova GameLogic (POST /StartGame)
    public static GameParams generateCreateParams(StartGameRequestDTO gameRequest) {
        switch (gameRequest.getGameMode()) {
            case PartitaSingola:
                try {
                    StartPartitaSingolaRequestDTO request = (StartPartitaSingolaRequestDTO) gameRequest;
                    return new PartitaSingolaParams(request.getPlayerId(), request.getClassUTName(),
                            request.getTypeRobot(), request.getDifficulty(), request.getGameMode(), request.getRemainingTime());
                } catch (ClassCastException e) {
                    throw new RuntimeException();
                }
            default:
                return new GameParams(gameRequest.getPlayerId(), gameRequest.getClassUTName(),
                        gameRequest.getTypeRobot(), gameRequest.getDifficulty(), gameRequest.getGameMode());
        }
    }

    // Inizializza un oggetto GameParams per creare una aggiornare una GameLogic esistente (POST /run)
    public static GameParams generateUpdateParams(RunGameRequestDTO runGameRequest) {
        switch (runGameRequest.getGameMode()) {
            case PartitaSingola:
                try {
                    RunPartitaSingolaRequestDTO request = (RunPartitaSingolaRequestDTO) runGameRequest;
                    logger.debug("Update partitaSingola request: {}", request);
                    return new PartitaSingolaParams(request.getPlayerId(), request.getGameMode(),
                            request.getClassUTCode(), request.getTestClassCode(), request.getRemainingTime());
                } catch (ClassCastException e) {
                    throw new RuntimeException();
                }
            default:
                return new GameParams(runGameRequest.getPlayerId(), runGameRequest.getGameMode(),
                        runGameRequest.getClassUTCode(), runGameRequest.getTestClassCode());
        }
    }


    public static GameParams generateCreateParams(SessionDTO gameRequest) {
        switch (gameRequest.getMode()) {
            case PartitaSingola:
                try {
                    PartitaSingolaSessionDTO request = (PartitaSingolaSessionDTO) gameRequest;
                    return new PartitaSingolaParams(request.getPlayerId(), request.getClassUTName(), request.getClassUTCode(),
                            request.getTypeRobot(), request.getDifficulty(), request.getMode(), request.getTestingClassCode(),
                            request.getRemainingTime());
                } catch (ClassCastException e) {
                    throw new RuntimeException();
                }
            default:
                return new GameParams(gameRequest.getPlayerId(), gameRequest.getClassUTName(), gameRequest.getClassUTCode(),
                        gameRequest.getTypeRobot(), gameRequest.getDifficulty(), gameRequest.getMode(), gameRequest.getTestingClassCode());
        }
    }
}
