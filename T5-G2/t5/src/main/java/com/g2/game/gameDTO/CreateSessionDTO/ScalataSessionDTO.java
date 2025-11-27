package com.g2.game.gameDTO.CreateSessionDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScalataSessionDTO extends SessionDTO {

    /**
     * Nome della scalata configurata dall'amministratore.
     * Questo nome viene utilizzato per recuperare la configurazione completa da T1.
     */
    @JsonProperty("scalataName")
    private String scalataName;

    /**
     * Livello corrente dell'utente nella scalata.
     */
    @JsonProperty("currentLevel")
    private int currentLevel;

    /**
     * Numero totale di livelli nella scalata.
     */
    @JsonProperty("totalLevels")
    @NotNull(message = "totalLevels is required") 
    private int totalLevels;

    /**
     * Tempo rimanente per la sessione di gioco.
     */
    @JsonProperty("remainingTime")
    private int remainingTime;


}
