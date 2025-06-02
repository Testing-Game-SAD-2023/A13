/*
 *   Copyright (c) 2025 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.g2.Game.Service;

import com.g2.Game.GameDTO.EndGameDTO.EndGameResponseDTO;
import com.g2.Game.GameFactory.params.GameParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g2.Game.GameDTO.RunGameDTO.RunGameResponseDTO;
import com.g2.Game.GameModes.Compile.CompileResult;
import com.g2.Game.GameModes.GameLogic;

@Service
public class GameServiceManager {

    private static final Logger logger = LoggerFactory.getLogger(GameServiceManager.class);
    private final GameService gameService;

    @Autowired
    public GameServiceManager(GameService gameService) {
        this.gameService = gameService;
    }

    /*
    public GameLogic CreateGameLogic(String playerId,
            String mode,
            String underTestClassName,
            String type_robot,
            String difficulty) {
        return gameService.CreateGame(playerId, mode, underTestClassName, type_robot, difficulty);
    }

     */

    public GameLogic CreateGameLogic(GameParams params) {
        return gameService.CreateGame(params);
    }

    protected GameLogic GetGameLogic(String playerId, String mode){
        return gameService.GetGame(mode, playerId);
    }

    protected CompileResult compileGame(GameLogic game, String testingClassCode, String type) {
        // return gameService.handleCompile(game, testingClassCode);
        return gameService.handleCompileNew(game, testingClassCode, type);
    }

    public RunGameResponseDTO PlayGame(String playerId, String mode, GameParams updateParams, String compilingType) {
        String testingClassCode;

        logger.info("[PlayGame] Inizio esecuzione per playerId={} e mode={}", playerId, mode);
        /*
         * Recupero la sessione di gioco
         */
        GameLogic currentGame = GetGameLogic(playerId, mode);
        logger.info("[PlayGame] GameLogic recuperato: gameID={}", currentGame.getGameID());
        /*
         * Compilo il test dell'utente
         */

        if (compilingType.equals("T8")) {
            testingClassCode = currentGame.getTestingClassCode();
        } else {
            testingClassCode = updateParams.getTestingClassCode();
        }

        CompileResult Usercompile = compileGame(currentGame, testingClassCode, compilingType);
        if (Usercompile == null) {
            throw new RuntimeException("compile is null");
        }
        logger.info("[PlayGame] Esito compilazione: success={}", Usercompile.hasSuccess());
        /*
         *   getSuccess() mi dà l'esito della compilazione => se l'utente ha scritto un test senza errori
         */
        if (Usercompile.hasSuccess()) {
            /*
             * Recupero i dati del robot da T4
             */
            CompileResult RobotCompile = gameService.GetRobotCoverage(currentGame);
            /*
             *  Lo score è definito dalle performance del file XML del test
             */
            int userScore = currentGame.GetScore(Usercompile);
            int robotScore = currentGame.GetScore(RobotCompile);
            /*
             *  Vado avanti col gioco
             *  Verifico gli achievement sbloccati nella partita corrente
             *  Aggiorno la sessione corrente con i due CompileResult
             *  Restituisco l'oggetto json che rispecchia lo stato del game
             */
            gameService.handleGameLogic(userScore, robotScore, currentGame, updateParams, Usercompile, RobotCompile);
            String[] unlockedAchievements = gameService.handleGameModeAchievementsUnlocked(currentGame, Usercompile, RobotCompile);

            logger.info("[PlayGame]: Creazione risposta per la partita (canWin={}, userScore={}, robotScore={}).", currentGame.isWinner(), userScore, robotScore);
            return new RunGameResponseDTO(Usercompile, RobotCompile, currentGame.isWinner(), userScore, robotScore, unlockedAchievements);
        } else {
            /*
             * Restituisco un Json solo con info parziali
             */
            return new RunGameResponseDTO(
                    Usercompile,
                    null,
                    false,
                    0, 0,
                    new String[0]
            );
        }
    }

    public void LeaveGame(String playerId, String mode, GameParams updateParams) {
        GameLogic currentGame = GetGameLogic(playerId, mode);
        logger.info("[LeaveGame] GameLogic recuperato: gameID={}", currentGame.getGameID());

        gameService.UpdateGame(playerId, currentGame, updateParams, null, null);
        logger.info("[LeaveGame] GameLogic aggiornato: gameID={}", currentGame.getGameID());
    }

    public EndGameResponseDTO EndGame(String playerId, String mode, boolean surrendered) {
        logger.info("[EndGame] Inizio terminazione partita per playerId={} e mode={}", playerId, mode);
        /*
         * Recupero la sessione di gioco
         */
        GameLogic currentGame = GetGameLogic(playerId, mode);
        logger.info("[EndGame] GameLogic recuperato: gameID={}", currentGame.getGameID());
        /*
         * Eseguo le operazioni di end game
         */
        return gameService.handleGameEnd(currentGame, surrendered);
    }

}
