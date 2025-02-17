package com.g2.Game.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g2.Game.GameDTO.GameResponseDTO;
import com.g2.Game.GameModes.Coverage.CompileResult;
import com.g2.Game.GameModes.GameLogic;
import com.g2.Game.Service.Exceptions.GameAlreadyExistsException;
import com.g2.Game.Service.Exceptions.GameDontExistsException;

@Service
public class GameServiceManager {

    private final GameService gameService;

    @Autowired
    public GameServiceManager(GameService gameService) {
        this.gameService = gameService;
    }

    public GameLogic CreateGameLogic(String playerId,
            String mode,
            String underTestClassName,
            String type_robot,
            String difficulty) throws GameAlreadyExistsException {
        return gameService.CreateGame(playerId, mode, underTestClassName, type_robot, difficulty);
    }

    protected GameLogic GetGameLogic(String playerId, String mode) throws GameDontExistsException {
        return gameService.GetGame(mode, playerId);
    }

    protected CompileResult compileGame(GameLogic game, String testingClassCode) {
        return gameService.handleCompile(game.getClasseUT(), testingClassCode);
    }

    public GameResponseDTO PlayGame(String playerId, String mode, String testingClassCode, Boolean isGameEnd) throws GameDontExistsException {
        /*
         * Recupero la sessioen di gioco 
         */
        GameLogic currentGame = GetGameLogic(playerId, mode);
        /*
         * Compilo il test dell'utente  
         */
        CompileResult compile = compileGame(currentGame, testingClassCode);
        if (compile == null) {
            throw new RuntimeException("compile is null");
        }
        /*
        *   getSuccess() mi dà l'esito della compilazione => se l'utente ha scritto un test senza errori 
         */
        if (compile.getSuccess()) {
            /*
            *  vado avanti col gioco 
            *  restituisce l'oggetto json che rispecchia lo stato del game
            *  l'utente può imporre la fine del gioco con isGameEnd
             */
            return gameService.handleGameLogic(compile, currentGame, isGameEnd);
        } else {
            /*
             * Restituisco un Json solo con info parziali 
             */
            return gameService.createResponseRun(compile, false, 0, 0, false);
        }
    }

}
