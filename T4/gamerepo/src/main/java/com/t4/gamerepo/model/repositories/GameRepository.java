package com.t4.gamerepo.model.repositories;

import com.t4.gamerepo.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query(value = "SELECT * " +
            "FROM games AS g JOIN players AS p ON g.id = p.game_id " +
            "WHERE :playerId = p.player_id", nativeQuery = true)
    List<Game> findByPlayerId(@Param("playerId") Long playerId);

    /**
     * Trova una partita Scalata STARTED per un giocatore e nome scalata specifici
     * 
     * @param playerId ID del giocatore
     * @param gameName Nome della scalata
     * @return La partita in corso (STARTED) oppure null se non esiste
     */
    @Query(value = "SELECT g.* " +
            "FROM games AS g JOIN players AS p ON g.id = p.game_id " +
            "WHERE p.player_id = :playerId " +
            "AND g.game_name = :gameName " +
            "AND g.game_mode = 'Scalata' " +
            "AND g.status = 'STARTED' " +
            "LIMIT 1", nativeQuery = true)
    Game findScalataInProgress(@Param("playerId") Long playerId, @Param("gameName") String gameName);

    /**
     * Trova l'ultima partita Scalata del giocatore ordinata per data di inizio (la più recente).
     * Verifica lo status: se FINISHED ritorna null, altrimenti ritorna il game.
     * 
     * La logica è implementata nel service layer perché JPA non supporta condizioni
     * che restituiscono null basate su valori di colonna.
     * 
     * @param playerId ID del giocatore
     * @return L'ultima partita Scalata oppure null se non esiste o se è FINISHED
     */
    @Query(value = "SELECT g.* " +
            "FROM games AS g JOIN players AS p ON g.id = p.game_id " +
            "WHERE p.player_id = :playerId " +
            "AND g.game_mode = 'Scalata' " +
            "ORDER BY g.started_at DESC " +
            "LIMIT 1", nativeQuery = true)
    Game findLastScalataByPlayerId(@Param("playerId") Long playerId);
}

