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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.g2.game.gameFactory.params.GameParams;
import com.g2.game.gameFactory.params.ScalataParams;
import com.g2.game.gameMode.Compile.CompileResult;
import com.g2.interfaces.ServiceManager;
import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

/**
 * ScalataGame rappresenta una modalità di gioco a livelli progressivi.
 * Estende TurnBasedGame, ereditando tutta la logica di calcolo punteggio, turni e achievement.
 * Ogni livello della scalata è gestito come una PartitaSingola.
 */
@Getter
@Setter
public class ScalataGame extends TurnBasedGame {

    @JsonIgnore
    private static final Logger logger = LoggerFactory.getLogger(ScalataGame.class);

    @JsonProperty("currentLevel")
    private int currentLevel;

    @JsonProperty("scalataName")
    private String scalataName;

    @JsonProperty("totalLevels")
    private int totalLevels;

    // ========================================
    // COSTRUTTORI
    // ========================================

    public ScalataGame() {
        super();
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
        super(serviceManager, playerId, classUT, opponentType, difficulty, gamemode, testingClassCode, remainingTime);
        this.currentLevel = currentLevel;
        this.scalataName = scalataName;
        this.totalLevels = totalLevels;
    }

    // ========================================
    // OVERRIDE METODI
    // ========================================

    @Override
    public void updateState(GameParams gameParams, CompileResult userCompileResult, CompileResult robotCompileResult) {
        if (!(gameParams instanceof ScalataParams))
            throw new IllegalArgumentException("Impossibile aggiornare la logica corrente, i parametri ricevuti non son istanza di ScalataParams");
        
        // Chiama il metodo della superclasse per aggiornare i campi comuni
        super.updateState(gameParams, userCompileResult, robotCompileResult);
        
        // Aggiorna i campi specifici della scalata
        ScalataParams scalataParams = (ScalataParams) gameParams;
        this.currentLevel = scalataParams.getCurrentLevel();
        this.scalataName = scalataParams.getScalataName();
        this.totalLevels = scalataParams.getTotalLevels();
    }

    /**
     * Verifica se il LIVELLO CORRENTE è terminato.
     * La scalata non ha un "fine" per turno, ogni livello si comporta come PartitaSingola.
     */
    @Override
    public Boolean isGameEnd() {
        return false; // Come PartitaSingola, il giocatore può fare quanti turni vuole
    }

    // ========================================
    // METODI SPECIFICI DELLA SCALATA
    // ========================================

    /**
     * Verifica se l'utente ha vinto la SCALATA COMPLETA.
     */
    public boolean isScalataWon() {
        return isWinner() && currentLevel >= totalLevels;
    }

    /** DA MODIFICARE
     * Achievement per la modalità Scalata.
     * Combina gli achievement di TurnBasedGame con achievement specifici della scalata.
     */
    @Override
    public Map<String, BiFunction<CompileResult, CompileResult, Boolean>> gameModeAchievements() {
        // Ottieni gli achievement di base da TurnBasedGame
        Map<String, BiFunction<CompileResult, CompileResult, Boolean>> achievements = 
            new HashMap<>(super.gameModeAchievements());
        
        // ========================================
        // ACHIEVEMENT DELLA SCALATA COMPLETA
        // ========================================
        
        // Achievement: Completa la scalata
        achievements.put("scalata_completed", (user, robot) -> isScalataWon());
        
        // Achievement: Completa un singolo livello
        achievements.put("scalata_level_cleared", (user, robot) -> isWinner());
        
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
    public void startGame() {
        // Chiama T4 con la nuova action che accetta 3 parametri (GameMode, playerId, scalataName)
        this.setGameID(getServiceManager().handleRequest("T4", "CreateGameWithName", Long.class, 
            getGameMode(), getPlayerID(), this.scalataName));
        
        logger.info("[SCALATA] Game created in T4 with ID={}, scalataName='{}'", 
                   getGameID(), scalataName);
    }

    /**
     *  Override di endGame per gestire la logica di fine scalata.
     *  Se la scalata non è ancora terminata, avvia il livello successivo.
     *  Altrimenti, conclude la scalata.
     */
    @Override
    public void endGame(boolean isGameSurrendered) {
        super.endGame(isGameSurrendered);
        
        // Se ha vinto il livello ma non la scalata completa, incrementa il livello
        if (isWinner() && !isScalataWon()) {
            currentLevel++;
            logger.info("[SCALATA] Livello {} completato! Prossimo livello: {}/{}", 
                       currentLevel - 1, currentLevel, totalLevels);
        } else if (isScalataWon()) {
            logger.info("[SCALATA] Scalata '{}' completata con successo! Tutti i {} livelli superati.", 
                       scalataName, totalLevels);
        }
    }
}
