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

    @JsonProperty("timeMaxPerLevel")
    private int timeMaxPerLevel;

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
                       String scalataName, int currentLevel, int totalLevels, int timeMaxPerLevel) {
        super(serviceManager, playerId, classUT, opponentType, difficulty, gamemode, testingClassCode, remainingTime);
        this.currentLevel = currentLevel;
        this.scalataName = scalataName;
        this.totalLevels = totalLevels;
        this.timeMaxPerLevel = timeMaxPerLevel;
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
        this.remainingTime = scalataParams.getRemainingTime();
        this.timeMaxPerLevel = scalataParams.getTimeMaxPerLevel();
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
        return currentLevel == totalLevels && isWinner();
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
     * Carica i dati del livello corrente da T1.
     * Chiama il microservizio T1 per ottenere className, tempoMax e opponentName
     * del livello corrente della scalata.
     */
    public void loadLevelData() {
        logger.info("[SCALATA] Caricamento dati livello {} della scalata '{}'", currentLevel, scalataName);
        
        // Chiama T1 per ottenere i dati del livello corrente
        Map<String, Object> levelData = (Map<String, Object>) getServiceManager().handleRequest(
                "T1", "getLevelByScalataAndPosition", scalataName, currentLevel);
        
        // Popola i dati del gioco con le informazioni del livello
        this.setClassUTName((String) levelData.get("className"));
        Integer tempoMax = (Integer) levelData.get("tempoMax");
        this.remainingTime = tempoMax != null ? tempoMax : 600;
        this.timeMaxPerLevel = this.remainingTime;
        this.setTypeRobot("EvoSuite");
        this.setDifficulty(OpponentDifficulty.EASY);

        logger.info("[SCALATA] Dati livello {} caricati: class={}, tempo={}", 
                   currentLevel, getClassUTName(), remainingTime);
    }

    /**
     *  Override di startGame per gestire la creazione del game in T4 con il nome della scalata.
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
     * Gestisce la chiusura di un livello nella modalità Scalata.
     * - Se il giocatore ha vinto: passa al livello successivo (carica dati da T1, crea nuovo round in T4)
     * - Se il giocatore ha perso: resetta il livello per permettere un nuovo tentativo
     */
    public void handleCloseLevel() {
        logger.info("[SCALATA] handleCloseLevel chiamato: isWinner={}, currentLevel={}/{}", 
                   isWinner(), currentLevel, totalLevels);
        
        if (isWinner()) {
            // ✅ HA VINTO IL LIVELLO: passa al livello successivo
            handleLevelWon();
        } else {
            // ❌ HA PERSO IL LIVELLO: resetta per permettere retry
            handleLevelLost();
        }
    }

    /**
     * Gestisce il caso di vittoria del livello corrente.
     * Incrementa il livello, carica i dati del prossimo livello da T1 e crea un nuovo round in T4.
     */
    private void handleLevelWon() {
        int oldLevel = currentLevel;
        currentLevel++; // Incrementa il livello
        
        logger.info("[SCALATA] Livello {} completato, passaggio al livello {}/{}", 
                   oldLevel, currentLevel, totalLevels);
        
        try {
            // Aggiorna currentLevel in T4
            getServiceManager().handleRequest("T4", "IncrementCurrentLevel", getGameID());
            logger.info("[SCALATA] CurrentLevel aggiornato in T4: gameID={}, newLevel={}", 
                       getGameID(), currentLevel);
            
            // Carica i dati del livello successivo da T1
            logger.info("[SCALATA] Caricamento dati livello {} della scalata '{}'", currentLevel, scalataName);
            
            Map<String, Object> nextLevelData = (Map<String, Object>) getServiceManager().handleRequest(
                    "T1", "getLevelByScalataAndPosition", scalataName, currentLevel);
            
            // Aggiorna i dati del gioco con il nuovo livello
            String nextClassName = (String) nextLevelData.get("className");
            Integer nextTempoMax = (Integer) nextLevelData.get("tempoMax");
            
            this.setClassUTName(nextClassName);
            this.remainingTime = nextTempoMax != null ? nextTempoMax : 600;
            this.timeMaxPerLevel = this.remainingTime;
            
            // Reset currentTurn a 0 per il nuovo livello (il primo Turn sarà 1)
            this.setCurrentTurn(0);
            
            logger.info("[SCALATA] Dati livello {} caricati: class={}, tempo={}, currentTurn resettato a 0", 
                       currentLevel, nextClassName, nextTempoMax);
            
            // Chiudi il round precedente PRIMA di crearne uno nuovo
            getServiceManager().handleRequest("T4", "EndRound", getGameID());
            logger.info("[SCALATA] Round precedente chiuso in T4");
            
            // Crea un nuovo round in T4 per il livello successivo
            this.startRound();
            logger.info("[SCALATA] Nuovo round creato in T4 per livello {}", currentLevel);
            
        } catch (Exception e) {
            logger.error("[SCALATA] Errore caricamento dati livello successivo: {}", e.getMessage(), e);
            throw new RuntimeException("Errore nel caricamento del livello successivo", e);
        }
    }

    /**
     * Gestisce il caso di sconfitta del livello corrente.
     * Resetta il tempo e il turno per permettere al giocatore di riprovare.
     */
    private void handleLevelLost() {
        logger.info("[SCALATA] Livello {} fallito, reset dati per nuovo tentativo", currentLevel);
        
        try {
            // Reset tempo al valore originale del livello
            this.remainingTime = this.timeMaxPerLevel;
            
            // Reset currentTurn a 0 (il primo Turn del nuovo tentativo sarà 1)
            this.setCurrentTurn(0);
            
            logger.info("[SCALATA] Tempo resettato a {} secondi, currentTurn resettato a 0 per riprovare livello {}", 
                       timeMaxPerLevel, currentLevel);
            
            // Incrementa round_number in T4 per tracciare il tentativo fallito
            getServiceManager().handleRequest("T4", "IncrementRoundAttempt", getGameID());
            logger.info("[SCALATA] Round attempt incrementato in T4 per game {}", getGameID());
            
        } catch (Exception e) {
            logger.error("[SCALATA] Errore reset dati livello corrente: {}", e.getMessage(), e);
            throw new RuntimeException("Errore nel reset del livello", e);
        }
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
            
        } else if (isScalataWon()) {
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
