/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
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

package com.g2.game.gameMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.g2.game.gameFactory.params.GameParams;
import com.g2.game.gameFactory.params.ScalataParams;
import com.g2.game.gameMode.Compile.CompileResult;
import com.g2.interfaces.ServiceManager;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

public class ScalataGame extends GameLogic {

    private int currentLevel;
    private int remainingTime;
    private String scalataName;
    private int totalLevels;
    private PartitaSingola currentLevelGame;  // Delega la logica a PartitaSingola

    @JsonIgnore
    private static final Logger logger = LoggerFactory.getLogger(ScalataGame.class);

    public ScalataGame() {
        //Costruttore vuoto
    }

    public ScalataGame(ServiceManager serviceManager, Long playerId, String classUT,
                       String opponentType, OpponentDifficulty difficulty,
                       GameMode gamemode, String testingClassCode) {
        super(serviceManager, playerId, classUT, opponentType, difficulty, gamemode, testingClassCode);
    }

    public ScalataGame(ServiceManager serviceManager, Long playerId, String classUT,
                       String opponentType, OpponentDifficulty difficulty,
                       GameMode gamemode, String testingClassCode, int remainingTime,
                       String scalataName, int currentLevel, int totalLevels) {
        super(serviceManager, playerId, classUT, opponentType, difficulty, gamemode, testingClassCode);
        this.remainingTime = remainingTime;
        this.currentLevel = currentLevel;
        this.scalataName = scalataName;
        this.totalLevels = totalLevels;
    }

    @Override
    public void nextTurn(CompileResult userScore, CompileResult robotScore) {
        // Delega al gioco del livello corrente
        currentLevelGame.nextTurn(userScore, robotScore);
    }

    @Override
    public void updateState(GameParams gameParams, CompileResult userCompileResult, CompileResult robotCompileResult) {
        if (!(gameParams instanceof ScalataParams))
            throw new IllegalArgumentException("Impossibile aggiornare la logica corrente, i parametri ricevuti non son istanza di ScalataParams");
        super.updateState(gameParams, userCompileResult, robotCompileResult);
        this.remainingTime = ((ScalataParams) gameParams).getRemainingTime();
        this.currentLevel = ((ScalataParams) gameParams).getCurrentLevel();
        this.scalataName = ((ScalataParams) gameParams).getScalataName();
    }

    @Override
    public int getScore(CompileResult compileResult) {
        // Delega al gioco del livello corrente
        return currentLevelGame.getScore(compileResult);
    }

    /**
     * Verifica se il LIVELLO CORRENTE è terminato.
     */
    @Override
    public Boolean isGameEnd() {
        return currentLevelGame.isGameEnd();
    }

    /**
     * Verifica se l'utente ha vinto il LIVELLO CORRENTE.
     */
    @Override
    public boolean isWinner() {
        return currentLevelGame.isWinner();
    }

    /**
     * Verifica se l'utente ha vinto la SCALATA.
     */
    public boolean isScalataWon() {
        return isWinner() && currentLevel >= totalLevels;
    }

    /** DA SISTEMARE
     * Achievement per la modalità Scalata.
     * Combina gli achievement del livello corrente (da PartitaSingola)
     * con gli achievement specifici della scalata completa.
     */
    @Override
    public Map<String, BiFunction<CompileResult, CompileResult, Boolean>> gameModeAchievements() {
        Map<String, BiFunction<CompileResult, CompileResult, Boolean>> achievements = new HashMap<>();
        
        // ========================================
        // ACHIEVEMENT DEL LIVELLO CORRENTE
        // ========================================
        if (currentLevelGame != null) {
            Map<String, BiFunction<CompileResult, CompileResult, Boolean>> levelAchievements = 
                currentLevelGame.gameModeAchievements();
        }
        
        // ========================================
        // ACHIEVEMENT DELLA SCALATA COMPLETA
        // ========================================
        
        // Achievement: Completa la scalata
        achievements.put("scalata_completed", (user, robot) -> isScalataWon());

        /* SBAGLIATO, DA RIVEDERE
        // Achievement: Completa la scalata senza mai perdere contro il robot
        achievements.put("scalata_perfect", (user, robot) -> {
            // Verifica che in TUTTI i livelli user >= robot
            return isScalataWon() && currentLevelGame != null && isWinner();
        });
        */
        
        // Achievement: Completa un singolo livello
        achievements.put("scalata_level_cleared", (user, robot) -> 
            currentLevelGame != null && currentLevelGame.isWinner()
        );
        
        logger.info("[SCALATA] gameModeAchievements: {} achievement definiti per livello {} di {}", 
                   achievements.size(), currentLevel, totalLevels);
        
        return achievements;
    }

    /**
     *  Override di endGame per gestire la logica di fine scalata.
     *  Se la scalata non è ancora terminata, avvia il livello successivo.
     *  Altrimenti, conclude la scalata.
     */
    @Override
    public void endGame(boolean isGameSurrendered) {
        super.endGame(isGameSurrendered);
        if (!isScalataWon() && isWinner()) {
            currentLevel++;
        }
    }
}
