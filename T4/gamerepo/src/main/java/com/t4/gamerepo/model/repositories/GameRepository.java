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

}

