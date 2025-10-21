package com.t4.gamerepo.model.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO che rappresenta il risultato di un singolo giocatore.
 * Include informazioni sul punteggio finale ottenuto giocatore e se è il vincitore.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PlayerResultDTO {
    /**
     * Indica se il giocatore ha vinto la partita
     */
    private boolean isWinner;

    /**
     * Punteggio finale ottenuto dal giocatore nella partita. Corrisponde al punteggio usato dal GameEngine T56 per
     * determinare se il giocatore ha vinto.
     */
    private int score;
}
