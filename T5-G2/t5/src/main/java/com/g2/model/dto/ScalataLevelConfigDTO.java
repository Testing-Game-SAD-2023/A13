package com.g2.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

/**
 * DTO per la configurazione di un singolo livello della Scalata.
 * Questo DTO viene restituito da T1 e contiene tutte le informazioni
 * necessarie per avviare una partita per quel livello specifico.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScalataLevelConfigDTO {

    /**
     * Numero del livello (1, 2, 3, ...).
     */
    @JsonProperty("levelNumber")
    private int levelNumber;

    /**
     * Nome della classe da testare per questo livello.
     */
    @JsonProperty("className")
    private String className;

    /**
     * Tipo di robot avversario (es. "Randoop", "Evosuite").
     */
    @JsonProperty("typeRobot")
    private String typeRobot;

    /**
     * Difficoltà dell'avversario per questo livello.
     */
    @JsonProperty("difficulty")
    private OpponentDifficulty difficulty;

    /**
     * Tempo limite in secondi per completare il livello (opzionale).
     */
    @JsonProperty("timeLimit")
    private Integer timeLimit;

    /**
     * Descrizione del livello (opzionale).
     */
    @JsonProperty("description")
    private String description;
}
