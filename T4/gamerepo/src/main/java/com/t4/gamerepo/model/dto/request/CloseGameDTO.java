package com.t4.gamerepo.model.dto.request;

import com.t4.gamerepo.model.dto.common.PlayerResultDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * DTO usato per la chiusura (completamento) di una partita.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CloseGameDTO {

    /**
     * Mappa che associa l'ID di ciascun giocatore al relativo risultato.
     */
    private Map<Long, PlayerResultDTO> results;

    /**
     * Indica se il game si è concluso perché il giocatore la ha abbandonato
     */
    private boolean isGameSurrendered;
}
