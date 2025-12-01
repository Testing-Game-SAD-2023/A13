package com.g2.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO per la risposta del progresso della Scalata.
 * Utilizzato per comunicare al frontend lo stato attuale della progressione.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScalataProgressDTO {

    /**
     * Nome della scalata.
     */
    @JsonProperty("scalataName")
    private String scalataName;

    /**
     * Ultimo livello completato con successo.
     * 0 = nessun livello completato, 1-N = livelli completati
     */
    @JsonProperty("lastCompletedLevel")
    private int lastCompletedLevel;

    /**
     * Numero totale di livelli nella scalata.
     */
    @JsonProperty("totalLevels")
    private int totalLevels;

    /**
     * Prossimo livello da giocare (lastCompletedLevel + 1).
     */
    @JsonProperty("nextLevel")
    private int nextLevel;

    /**
     * Indica se la scalata è stata completata interamente.
     */
    @JsonProperty("isCompleted")
    private boolean isCompleted;

    /**
     * Punteggio totale accumulato nei livelli completati. DA VEDERE SE SERVE
     */
    @JsonProperty("totalScore")
    private int totalScore;
}
