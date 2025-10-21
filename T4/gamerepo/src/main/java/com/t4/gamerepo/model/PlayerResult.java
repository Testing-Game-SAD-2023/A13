package com.t4.gamerepo.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entità {@link Embeddable} che rappresenta il risultato di un giocatore in una partita.
 * <p>
 * Contiene il punteggio ottenuto dal giocatore e lo stato di vittoria.
 * </p>
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class PlayerResult {
    /**
     * Punteggio finale ottenuto dal giocatore nella partita. Corrisponde al punteggio usato dal GameEngine T56 per
     * determinare se il giocatore ha vinto.
     */
    private int score;

    /**
     * Indica se il giocatore ha vinto la partita.
     */
    private boolean isWinner;
}
