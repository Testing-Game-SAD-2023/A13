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
        
        // IMPORTANTE: Aggiorna remainingTime (come fa PartitaSingola)
        this.remainingTime = scalataParams.getRemainingTime();
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
     *  - Se vince il livello ma non la scalata: passa al livello successivo (NON chiude il game)
     *  - Se perde il livello: permette di riprovare (NON chiude il game, NON incrementa livello)
     *  - Se abbandona o completa tutta la scalata: chiude definitivamente il game
     */
    @Override
    public void endGame(boolean isGameSurrendered) {
        logger.info("[SCALATA] ========== endGame() chiamato ==========");
        logger.info("[SCALATA] isGameSurrendered={}, isWinner={}, isScalataWon={}, currentLevel={}/{}, gameID={}", 
                   isGameSurrendered, isWinner(), isScalataWon(), currentLevel, totalLevels, getGameID());
        
        if (isGameSurrendered) {
            // ❌ ABBANDONATA → chiudi tutto
            super.endGame(true);
            logger.info("[SCALATA] Scalata '{}' abbandonata al livello {}/{}.", 
                       scalataName, currentLevel, totalLevels);
            
        } else if (isWinner() && isScalataWon()) {
            // ✅ COMPLETATA → chiudi tutto
            super.endGame(false);
            logger.info("[SCALATA] Scalata '{}' completata con successo! Tutti i {} livelli superati.", 
                       scalataName, totalLevels);
            
        } else if (isWinner()) {
            // ✅ VINTO IL LIVELLO → passa al successivo
            currentLevel++;
            logger.info("[SCALATA] Livello {} completato! Prossimo livello: {}/{}", 
                       currentLevel - 1, currentLevel, totalLevels);
            
            // Aggiorna currentLevel in T4 senza chiudere il game
            long gameId = getGameID();
            if (gameId > 0) {
                try {
                    getServiceManager().handleRequest("T4", "IncrementCurrentLevel", gameId);
                    logger.info("[SCALATA] CurrentLevel aggiornato in T4: gameID={}, newLevel={}", 
                               gameId, currentLevel);
                } catch (Exception e) {
                    logger.error("[SCALATA] Errore aggiornamento currentLevel in T4: {}", e.getMessage(), e);
                }
            } else {
                logger.warn("[SCALATA] GameID non valido ({}), impossibile aggiornare T4", gameId);
            }
            
            // NON chiamiamo super.endGame() perché la scalata continua!
            
        } else {
            // ❌ PERSO IL LIVELLO → permetti retry
            logger.info("[SCALATA] Livello {} fallito, giocatore può riprovare. currentLevel={}/{}", 
                       currentLevel, currentLevel, totalLevels);
            
            // TODO: Incrementare round_number in T4 per tracciare i tentativi
            // Quando implementerai la rotta in T4, decommenta:
            /*
            long gameId = getGameID();
            if (gameId > 0) {
                try {
                    getServiceManager().handleRequest("T4", "IncrementRoundAttempt", gameId);
                    logger.info("[SCALATA] round_number incrementato in T4 per gameID={}", gameId);
                } catch (Exception e) {
                    logger.error("[SCALATA] Errore incremento round_number in T4: {}", e.getMessage(), e);
                }
            }
            */
            
            // NON chiamiamo super.endGame() → la sessione rimane attiva
            // Il giocatore può riprovare lo stesso livello
        }
    }
}
