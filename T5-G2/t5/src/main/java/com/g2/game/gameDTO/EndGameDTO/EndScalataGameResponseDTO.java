package com.g2.game.gameDTO.EndGameDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.g2.game.gameDTO.RunGameDTO.RunGameResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * DTO specifico per la risposta di EndGame nella modalità Scalata.
 * Estende EndGameResponseDTO aggiungendo informazioni specifiche per la gestione multilivello.
 */
@Setter
@Getter
public class EndScalataGameResponseDTO extends EndGameResponseDTO {

    /**
     * Livello corrente dopo la fine del turno.
     * Se il giocatore ha vinto, questo sarà incrementato al prossimo livello.
     */
    @JsonProperty("currentLevel")
    private int currentLevel;

    /**
     * Numero totale di livelli nella scalata.
     */
    @JsonProperty("totalLevels")
    private int totalLevels;

    /**
     * Nome della scalata in corso.
     */
    @JsonProperty("scalataName")
    private String scalataName;

    /**
     * Indica se la scalata è stata completata (tutti i livelli superati).
     * - true: Scalata completata con successo
     * - false: Ci sono ancora livelli da completare oppure il giocatore ha perso
     */
    @JsonProperty("isScalataWon")
    private boolean isScalataWon;

    /**
     * Costruttore principale per EndScalataGameResponseDTO.
     *
     * @param robotScore           Punteggio del robot
     * @param userScore            Punteggio dell'utente
     * @param isWinner             Se il giocatore ha vinto il LIVELLO corrente
     * @param expGained            Esperienza guadagnata
     * @param achievementsUnlocked Achievement sbloccati
     * @param runGameResponse      Risposta dell'esecuzione del turno
     * @param currentLevel         Livello corrente dopo EndGame
     * @param totalLevels          Numero totale di livelli
     * @param scalataName          Nome della scalata
     * @param isScalataWon         Se la scalata è stata completata
     */
    public EndScalataGameResponseDTO(int robotScore, int userScore, Boolean isWinner, int expGained,
                                     Set<String> achievementsUnlocked, RunGameResponseDTO runGameResponse,
                                     int currentLevel, int totalLevels, String scalataName, boolean isScalataWon) {
        super(robotScore, userScore, isWinner, expGained, achievementsUnlocked, runGameResponse);
        this.currentLevel = currentLevel;
        this.totalLevels = totalLevels;
        this.scalataName = scalataName;
        this.isScalataWon = isScalataWon;
    }

    @Override
    public String toString() {
        return "EndScalataGameResponseDTO{" +
                "robotScore=" + getRobotScore() +
                ", userScore=" + getUserScore() +
                ", isWinner=" + getIsWinner() +
                ", expGained=" + getExpGained() +
                ", achievementsUnlocked=" + getAchievementsUnlocked() +
                ", currentLevel=" + currentLevel +
                ", totalLevels=" + totalLevels +
                ", scalataName='" + scalataName + '\'' +
                ", isScalataWon=" + isScalataWon +
                '}';
    }
}
