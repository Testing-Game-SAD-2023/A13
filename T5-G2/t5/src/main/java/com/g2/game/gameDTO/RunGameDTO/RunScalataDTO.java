package com.g2.game.gameDTO.RunGameDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO per l'esecuzione di un turno nella modalità Scalata.
 * Estende RunGameRequestDTO aggiungendo i campi specifici per questa modalità.
 * Ogni campo ereditato corrisponde alle informazioni relative al livello corrente
 * della scalata giocata dall'utente. Tali informazioni sono recuperate da T1 in fase di
 * avvio della scalata.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class RunScalataDTO extends RunGameRequestDTO {
    
    /**
     * Nome della scalata configurata dall'amministratore.
     */
    @JsonProperty("scalataName")
    @NotBlank(message = "scalataName is required")
    private String scalataName;

    /**
     * Livello corrente della scalata.
     */
    @JsonProperty("currentLevel")
    @NotNull(message = "currentLevel is required")
    private int currentLevel;

    /**
     * Numero totale di livelli nella scalata.
     */
    @JsonProperty("totalLevels")
    @NotNull(message = "totalLevels is required") 
    private int totalLevels;

    /**
     * Tempo rimanente per completare il livello corrente.
     */
    @JsonProperty("remainingTime")
    @NotNull(message = "remainingTime is required")
    private int remainingTime;
}
