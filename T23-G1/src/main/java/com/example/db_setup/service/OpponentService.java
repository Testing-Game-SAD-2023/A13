package com.example.db_setup.service;

import com.example.db_setup.mapper.MapperFacade;
import com.example.db_setup.model.Opponent;
import com.example.db_setup.model.dto.gamification.OpponentDTO;
import com.example.db_setup.model.repository.OpponentRepository;
import com.example.db_setup.service.exception.OpponentNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import java.util.List;
import java.util.Optional;

/**
 * Servizio che gestisce la logica di business legata agli {@link Opponent}.
 * Fornisce operazioni per:
 * <ul>
 *     <li>Recuperare un avversario specifico dato un insieme di parametri (modalità di gioco, classe UT, tipo, difficoltà).</li>
 *     <li>Aggiungere un nuovo avversario o riattivarne uno esistente.</li>
 *     <li>Disattivare tutti gli avversari associati a una determinata classe UT.</li>
 * </ul>
 * <p>
 * La classe è necessaria alla gestione degli achievement del giocatore.
 */
@Service
public class OpponentService {

    private final OpponentRepository opponentRepository;
    private final MapperFacade mapperFacade;

    public OpponentService(OpponentRepository opponentRepository, MapperFacade mapperFacade) {
        this.opponentRepository = opponentRepository;
        this.mapperFacade = mapperFacade;
    }

    /**
     * Recupera un avversario in base ai parametri forniti.
     *
     * @param gameMode   modalità di gioco ({@link GameMode})
     * @param classUT    nome della classe sotto test
     * @param type       tipo di avversario ({@link String})
     * @param difficulty difficoltà dell’avversario ({@link OpponentDifficulty})
     * @return l'oggetto {@link Opponent} trovato
     * @throws OpponentNotFoundException se non esiste un avversario con le caratteristiche specificate
     */
    public Opponent getOpponent(GameMode gameMode, String classUT, String type, OpponentDifficulty difficulty) {
        Optional<Opponent> opponent = opponentRepository.findOpponentByGameModeAndClassUTAndTypeAndDifficulty(gameMode, classUT, type, difficulty);
        if (opponent.isEmpty())
            throw new OpponentNotFoundException();

        return opponent.get();
    }

    /**
     * Aggiunge un nuovo avversario oppure riattiva un avversario esistente disattivato (eliminato).
     * <p>
     * Se non esiste un avversario con i parametri forniti, viene creato e salvato nel repository.
     * Se esiste già, viene semplicemente riattivato impostando {@code active=true}.
     * </p>
     *
     * @param gameMode   la modalità di gioco ({@link GameMode})
     * @param classUT    il nome della classe sotto test
     * @param type       il tipo di avversario ({@link String})
     * @param difficulty la difficoltà dell’avversario ({@link OpponentDifficulty})
     * @return il DTO dell'{@link Opponent} creato o riattivato
     */
    public OpponentDTO addNewOpponent(GameMode gameMode, String classUT, String type, OpponentDifficulty difficulty) {
        Optional<Opponent> existing = opponentRepository.findOpponentByGameModeAndClassUTAndTypeAndDifficulty(
                gameMode, classUT, type, difficulty
        );

        if (existing.isEmpty()) {
            return mapperFacade.toDTO(opponentRepository.save(new Opponent(gameMode, classUT, type, difficulty)));
        } else {
            Opponent opponent = existing.get();
            opponent.setActive(true);
            return mapperFacade.toDTO(opponentRepository.save(opponent));
        }
    }

    /**
     * Disattiva tutti gli avversari associati a una determinata classe UT.
     * <p>
     * Imposta il campo {@code active=false} per ogni avversario trovato.
     * L'operazione è eseguita all'interno di una transazione.
     * </p>
     *
     * @param classUT il nome della classe UT per la quale disattivare gli avversari
     * @return il numero di avversari disattivati
     */
    @Transactional
    public int deleteAllOpponentsForClassUT(String classUT) {
        List<Opponent> opponents = opponentRepository.findAllByClassUT(classUT);
        for (Opponent opponent : opponents) {
            opponent.setActive(false);
            opponentRepository.save(opponent);
        }
        return opponents.size();
    }
}
