package com.t4.gamerepo.service;

import com.t4.gamerepo.model.Round;
import com.t4.gamerepo.model.Turn;
import com.t4.gamerepo.model.TurnScore;
import com.t4.gamerepo.model.repositories.RoundRepository;
import com.t4.gamerepo.service.exceptions.RoundAlreadyClosedException;
import com.t4.gamerepo.service.exceptions.TurnNotFoundException;
import org.springframework.stereotype.Service;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * Service che espone le funzionalità di CREATE, READ, UPDATE dei round di una partita.
 * <p>
 * Richiama {@link TurnService} per la gestione dei turni.
 * </p>
 */
@Service
public class RoundService {

    private final RoundRepository roundRepository;
    private final TurnService turnService;

    public RoundService(RoundRepository roundRepository, TurnService turnService) {
        this.roundRepository = roundRepository;
        this.turnService = turnService;
    }


    /**
     * Crea un nuovo round.
     *
     * @param roundNumber il numero del round
     * @param classUT     la classe sotto test
     * @param type        il tipo di avversario
     * @param difficulty  la difficoltà dell’avversario
     * @return il round creato e salvato
     */
    public Round createRound(int roundNumber, String classUT, String type, OpponentDifficulty difficulty) {
        Round round = new Round(roundNumber, classUT, type, difficulty);
        return roundRepository.save(round);
    }

    /**
     * Avvia un nuovo turno per un giocatore all'interno di un round specificato.
     *
     * @param round    il round corrente
     * @param playerId l'ID del giocatore
     * @return il turno creato e salvato
     * @throws RoundAlreadyClosedException se il round è già chiuso
     */
    public Turn startTurn(Round round, Long playerId, int turnNumber) {
        if (round.getClosedAt() != null)
            throw new RoundAlreadyClosedException("Round " + round.getRoundNumber() + " has already been closed");

        Turn turn = turnService.createTurn(playerId, turnNumber);
        round.addTurn(turn);
        roundRepository.save(round);

        return turn;
    }

    /**
     * Chiude un turno esistente e registra il punteggio del giocatore.
     *
     * @param currentRound il round corrente
     * @param turnNumber   il numero del turno da chiudere
     * @param playerId     l'ID del giocatore
     * @param turnScore    il punteggio del giocatore da registrare
     * @return il turno chiuso
     */
    public Turn closeTurn(Round currentRound, int turnNumber, Long playerId, TurnScore turnScore) {
        if (currentRound.getTurns().size() < turnNumber)
            throw new TurnNotFoundException("The turn " + turnNumber + " does not exist");
        return turnService.closeTurn(currentRound.getTurns().get(turnNumber - 1), playerId, turnScore);
    }

    /**
     * Chiude un round e registra il timestamp di chiusura.
     *
     * @param round il round da chiudere
     * @return il round chiuso e salvato
     * @throws RoundAlreadyClosedException se il round è già chiuso
     */
    public Round closeRound(Round round) {
        if (round.getClosedAt() != null)
            throw new RoundAlreadyClosedException("Round " + round.getRoundNumber() + " has already been closed");
        round.setClosedAt(Timestamp.from(Instant.now()));
        return roundRepository.save(round);
    }
}
