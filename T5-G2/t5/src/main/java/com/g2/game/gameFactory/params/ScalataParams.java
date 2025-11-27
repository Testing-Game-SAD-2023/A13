package com.g2.game.gameFactory.params;

import lombok.Getter;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

/**
 * Classe di parametri specifica per la modalità Scalata.
 * Usa la COMPOSIZIONE includendo un PartitaSingolaParams che rappresenta
 * il livello corrente della scalata, aggiungendo informazioni specifiche
 * per il tracking della progressione (scalataName, currentLevel).
 */
@Getter
public class ScalataParams extends GameParams {

    private String scalataName;
    private int currentLevel;
    private int remainingTime;
    private int totalLevels;

    /**
     * Costruttore per creare una nuova partita Scalata.
     *
     * @param playerId ID del giocatore
     * @param gameMode Modalità di gioco (Scalata)
     * @param scalataName Nome della scalata
     * @param currentLevel Livello corrente
     * @param classUTName Nome della classe da testare
     * @param typeRobot Tipo di robot avversario
     * @param difficulty Difficoltà dell'avversario
     * @param remainingTime Tempo rimanente
     * @param totalLevels Numero totale di livelli nella scalata
     */
    public ScalataParams(Long playerId, String classUTName, String typeRobot,
                        OpponentDifficulty difficulty, GameMode gameMode, int remainingTime,
                        String scalataName, int currentLevel, int totalLevels) {
        super(playerId, classUTName, typeRobot, difficulty, gameMode);
        this.scalataName = scalataName;
        this.currentLevel = currentLevel;
        this.remainingTime = remainingTime;
        this.totalLevels = totalLevels;
    }

    /**
     * Costruttore per aggiornare una nuova partita Scalata.
     *
     * @param playerId ID del giocatore
     * @param gameMode Modalità di gioco (Scalata)
     * @param classUTCode Codice della classe da testare
     * @param testClassCode Codice del test del giocatore
     * @param currentLevel Livello corrente
     * @param remainingTime Tempo rimanente
     */
    public ScalataParams(Long playerId, GameMode gameMode, String classUTCode,
                        String testClassCode, String scalataName, int remainingTime,
                         int totalLevels, int currentLevel) {
        super(playerId, gameMode, classUTCode, testClassCode);
        this.scalataName = scalataName;
        this.currentLevel = currentLevel;
        this.remainingTime = remainingTime;
        this.totalLevels = totalLevels;
    }

    /**
     * Costruttore completo per creare o aggiornare una partita Scalata.
     *
     * @param playerId ID del giocatore
     * @param classUTName Nome della classe da testare
     * @param classUTCode Codice della classe da testare
     * @param typeRobot Tipo di robot avversario
     * @param difficulty Difficoltà dell'avversario
     * @param mode Modalità di gioco (Scalata)
     * @param testingClassCode Codice del test del giocatore
     * @param remainingTime Tempo rimanente
     * @param scalataName Nome della scalata
     * @param currentLevel Livello corrente
     */

    public ScalataParams(Long playerId, String classUTName, String classUTCode,
                         String typeRobot, OpponentDifficulty difficulty, GameMode mode,
                         String testingClassCode, String scalataName, int remainingTime,
                         int totalLevels, int currentLevel) {
        super(playerId, classUTName, classUTCode, typeRobot, difficulty, mode, testingClassCode);
        this.scalataName = scalataName;
        this.currentLevel = currentLevel;
        this.remainingTime = remainingTime;
        this.totalLevels = totalLevels;
    }


    @Override
    public String toString() {
        return "ScalataParams{" +
                super.toString() +
                ", scalataName='" + scalataName + '\'' +
                ", currentLevel=" + currentLevel + '\'' +
                ", remainingTime=" + remainingTime +
                '}';
    }
}