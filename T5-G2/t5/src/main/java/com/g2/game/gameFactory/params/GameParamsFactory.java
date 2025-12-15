package com.g2.game.gameFactory.params;

import com.g2.game.gameDTO.CreateSessionDTO.PartitaSingolaSessionDTO;
import com.g2.game.gameDTO.CreateSessionDTO.ScalataSessionDTO;
import com.g2.game.gameDTO.CreateSessionDTO.SessionDTO;
import com.g2.game.gameDTO.RunGameDTO.RunGameRequestDTO;
import com.g2.game.gameDTO.RunGameDTO.RunPartitaSingolaRequestDTO;
import com.g2.game.gameDTO.RunGameDTO.RunScalataDTO;
import com.g2.game.gameDTO.StartGameDTO.StartGameRequestDTO;
import com.g2.game.gameDTO.StartGameDTO.StartPartitaSingolaRequestDTO;
import com.g2.game.gameDTO.StartGameDTO.StartScalataRequestDTO;

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
                    return new PartitaSingolaParams(
                            request.getPlayerId(), 
                            request.getClassUTName(),
                            request.getTypeRobot(), 
                            request.getDifficulty(), 
                            request.getGameMode(), 
                            request.getRemainingTime()
                    );
                } catch (ClassCastException e) {
                    logger.error("Failed to cast StartGameRequestDTO to StartPartitaSingolaRequestDTO", e);
                    throw new RuntimeException("Invalid request type for PartitaSingola mode", e);
                }
            case Scalata:
                try {
                    StartScalataRequestDTO scalataRequest = (StartScalataRequestDTO) gameRequest;
                    logger.debug("Create scalata request: {}", scalataRequest);

                    return new ScalataParams(
                            scalataRequest.getPlayerId(),
                            scalataRequest.getClassUTName(),
                            scalataRequest.getTypeRobot(),
                            scalataRequest.getDifficulty(),
                            scalataRequest.getGameMode(),
                            scalataRequest.getRemainingTime(),
                            scalataRequest.getScalataName(),
                            scalataRequest.getCurrentLevel(),
                            scalataRequest.getTotalLevels(),
                            scalataRequest.getTimeMaxPerLevel()
                    );
                } catch (ClassCastException e) {
                    logger.error("Failed to cast StartGameRequestDTO to StartScalataRequestDTO", e);
                    throw new RuntimeException("Invalid request type for Scalata mode", e);
                }
            case Allenamento:
                try {
                    return new GameParams(
                            gameRequest.getPlayerId(),
                            gameRequest.getClassUTName(),
                            gameRequest.getTypeRobot(),
                            gameRequest.getDifficulty(),
                            gameRequest.getGameMode()
                    );

                } catch (ClassCastException e) {
                    logger.error("Failed to cast StartGameRequestDTO to StartAllenamentoRequestDTO", e);
                    throw new RuntimeException("Invalid request type for Allenamento mode", e);
                }
            default:
                logger.warn("Unhandled game mode: {}, using base GameParams", gameRequest.getGameMode());
                throw new UnsupportedOperationException("Game mode " + gameRequest.getGameMode() + " not supported for create");
        }
    }

    // Inizializza un oggetto GameParams per aggiornare una GameLogic esistente (POST /run)
    public static GameParams generateUpdateParams(RunGameRequestDTO runGameRequest) {
        switch (runGameRequest.getGameMode()) {
            case PartitaSingola:
                try {
                    RunPartitaSingolaRequestDTO request = (RunPartitaSingolaRequestDTO) runGameRequest;
                    logger.debug("Update partitaSingola request: {}", request);
                    return new PartitaSingolaParams(
                            request.getPlayerId(), 
                            request.getGameMode(),
                            request.getClassUTCode(), 
                            request.getTestClassCode(), 
                            request.getRemainingTime()
                    );
                } catch (ClassCastException e) {
                    logger.error("Failed to cast RunGameRequestDTO to RunPartitaSingolaRequestDTO", e);
                    throw new RuntimeException("Invalid request type for PartitaSingola mode", e);
                }
            case Scalata:
                try {
                    RunScalataDTO scalataRequest = (RunScalataDTO) runGameRequest;
                    logger.debug("Update scalata request: {}", scalataRequest);
                    
                    return new ScalataParams(
                            scalataRequest.getPlayerId(),
                            scalataRequest.getGameMode(),
                            scalataRequest.getClassUTCode(),
                            scalataRequest.getTestClassCode(),
                            scalataRequest.getScalataName(),
                            scalataRequest.getRemainingTime(),
                            scalataRequest.getTotalLevels(),
                            scalataRequest.getCurrentLevel(),
                            scalataRequest.getTimeMaxPerLevel()
                    );
                } catch (ClassCastException e) {
                    logger.error("Failed to cast RunGameRequestDTO to RunScalataDTO", e);
                    throw new RuntimeException("Invalid request type for Scalata mode", e);
                }
            case Allenamento:
                try {
                    return new GameParams(
                            runGameRequest.getPlayerId(),
                            runGameRequest.getGameMode(),
                            runGameRequest.getClassUTCode(),
                            runGameRequest.getTestClassCode()
                    );

                } catch (ClassCastException e) {
                    logger.error("Failed to cast RunGameRequestDTO to RunAllenamentoRequestDTO", e);
                    throw new RuntimeException("Invalid request type for Allenamento mode", e);
                }
            default:
                logger.warn("Unhandled game mode: {}, using base GameParams", runGameRequest.getGameMode());
                throw new UnsupportedOperationException("Game mode " + runGameRequest.getGameMode() + " not supported for update");
        }
    }


    public static GameParams generateCreateParams(SessionDTO gameRequest) {
        switch (gameRequest.getMode()) {
            case PartitaSingola:
                try {
                    PartitaSingolaSessionDTO request = (PartitaSingolaSessionDTO) gameRequest;
                    return new PartitaSingolaParams(
                            request.getPlayerId(),
                            request.getClassUTName(),
                            request.getClassUTCode(),
                            request.getTypeRobot(),
                            request.getDifficulty(),
                            request.getMode(),
                            request.getTestingClassCode(),
                            request.getRemainingTime());
                } catch (ClassCastException e) {
                    logger.error("Failed to cast SessionDTO to PartitaSingolaSessionDTO", e);
                    throw new RuntimeException("Invalid request type for Partita Singola mode", e);
                }
            case Scalata:
                try {
                    ScalataSessionDTO request = (ScalataSessionDTO) gameRequest;
                    return new ScalataParams(
                            request.getPlayerId(),
                            request.getClassUTName(),
                            request.getClassUTCode(),
                            request.getTypeRobot(),
                            request.getDifficulty(),
                            request.getMode(),
                            request.getTestingClassCode(),
                            request.getScalataName(),
                            request.getRemainingTime(),
                            request.getTotalLevels(),
                            request.getCurrentLevel(),
                            request.getTimeMaxPerLevel()
                    );
                } catch (ClassCastException e) {
                    logger.error("Failed to cast SessionDTO to ScalataSessionDTO", e);
                    throw new RuntimeException("Invalid request type for Scalata mode", e);
                }
            default:
                logger.warn("Unhandled game mode: {}, using base GameParams", gameRequest.getMode());
                throw new UnsupportedOperationException("Game mode " + gameRequest.getMode() + " not supported for update");

        }
    }
}
