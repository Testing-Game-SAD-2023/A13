package com.g2.game.gameDTO.StartGameDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO specifico per la modalità Scalata.
 * Estende StartGameRequestDTO aggiungendo i campi specifici per questa modalità.
 * Ogni campo ereditato corrispondono alle informazioni relative al livello corrente
 * della scalata giocata dall'utente. Tali informazioni sono recuperate da T1 in fase di
 * avvio della scalata.
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StartScalataRequestDTO extends StartGameRequestDTO {

    /**
     * Nome della scalata configurata dall'amministratore.
     * Questo nome viene utilizzato per recuperare la configurazione completa da T1.
     */
    @JsonProperty("scalataName")
    @NotBlank(message = "scalataName is required")
    private String scalataName;

    /**
     * Livello corrente dell'utente nella scalata.
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

    /**
     * Tempo massimo consentito per completare ogni livello della scalata.
     */
    @JsonProperty("timeMaxPerLevel")
    @NotNull(message = "timeMaxPerLevel is required")
    private int timeMaxPerLevel;

}