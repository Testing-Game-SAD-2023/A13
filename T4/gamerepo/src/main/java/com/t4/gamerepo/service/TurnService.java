package com.t4.gamerepo.service;

import com.t4.gamerepo.model.Turn;
import com.t4.gamerepo.model.TurnScore;
import com.t4.gamerepo.model.repositories.TurnRepository;
import com.t4.gamerepo.service.exceptions.NotPlayerTurnException;
import com.t4.gamerepo.service.exceptions.TurnAlreadyClosedException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;


/**
 * Service che espone le operazioni di CREATE, READE, UPDATE per i turni di un round.
 */
@Service
public class TurnService {

    private final TurnRepository turnRepository;

    public TurnService(TurnRepository turnRepository) {
        this.turnRepository = turnRepository;
    }

    /**
     * Crea un nuovo turno per un giocatore.
     *
     * @param playerId   l'ID del giocatore a cui è associato il turno
     * @param turnNumber il numero del turno all'interno del round
     * @return il turno creato e salvato
     */
    public Turn createTurn(Long playerId, int turnNumber) {
        Turn turn = new Turn();
        turn.setPlayerId(playerId);
        turn.setTurnNumber(turnNumber);
        turnRepository.save(turn);

        return turn;
    }

    /**
     * Chiude un turno esistente, registrando il punteggio del giocatore e il timestamp di chiusura.
     *
     * @param turn      il turno da chiudere
     * @param playerId  l'ID del giocatore che chiude il turno
     * @param turnScore il punteggio ottenuto dal giocatore
     * @return il turno chiuso e salvato
     * @throws NotPlayerTurnException     se il turno non appartiene al giocatore indicato
     * @throws TurnAlreadyClosedException se il turno è già chiuso
     */
    public Turn closeTurn(Turn turn, Long playerId, TurnScore turnScore) {
        if (!turn.getPlayerId().equals(playerId))
            throw new NotPlayerTurnException("Turn " + turn.getTurnNumber() + " is not associated with player " + playerId);

        if (turn.getClosedAt() != null)
            throw new TurnAlreadyClosedException("Turn " + turn.getTurnNumber() + " is already closed at " + turn.getClosedAt());

        turn.setClosedAt(Timestamp.from(Instant.now()));
        turn.setScore(turnScore);
        return turnRepository.save(turn);
    }

}
