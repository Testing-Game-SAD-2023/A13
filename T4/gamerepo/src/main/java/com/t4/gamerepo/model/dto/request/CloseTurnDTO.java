package com.t4.gamerepo.model.dto.request;

import com.t4.gamerepo.model.dto.common.TurnScoreDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO usato per la chiusura (completamento) di un turno.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CloseTurnDTO {
    /**
     * ID del giocatore che ha giocato il turno.
     */
    private Long playerId;

    /**
     * Punteggio di copertura ottenuto dal giocatore nel turno
     */
    private TurnScoreDTO turnScoreDTO;
}
