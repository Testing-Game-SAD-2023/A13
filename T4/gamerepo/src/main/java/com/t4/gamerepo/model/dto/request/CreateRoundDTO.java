package com.t4.gamerepo.model.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

/**
 * DTO usato per la creazione di un nuovo round.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CreateRoundDTO {
    /**
     * Classe sotto test da testare nel round.
     */
    private String classUT;

    /**
     * Tipo di avversario da affrontare nel round.
     */
    private String type;

    /**
     * Difficoltà dell'avversario.
     */
    private OpponentDifficulty difficulty;

    /**
     * Numero del round da creare all'interno della partita.
     */
    private int roundNumber;
}
