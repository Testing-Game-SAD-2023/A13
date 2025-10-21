package com.t4.gamerepo.model.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import testrobotchallenge.commons.models.opponent.GameMode;

import java.util.List;


/**
 * DTO usato per la l'avvio di una nuova partita.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CreateGameDTO {
    /**
     * Modalità di gioco giocata nella partita.
     */
    private GameMode gameMode;

    /**
     * Giocatori partecipanti alla partita.
     */
    private List<Long> players;
}
