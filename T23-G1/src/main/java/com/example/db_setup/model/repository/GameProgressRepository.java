package com.example.db_setup.model.repository;

import com.example.db_setup.model.GameProgress;
import com.example.db_setup.model.Opponent;
import com.example.db_setup.model.PlayerProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import java.util.Optional;

public interface GameProgressRepository extends JpaRepository<GameProgress, Long> {
    @Query("""
                SELECT gp FROM GameProgress gp
                JOIN gp.opponent o
                JOIN gp.playerProgress pp
                WHERE pp.player.ID = :playerId
                  AND o.classUT = :classUT
                  AND o.gameMode = :gameMode
                  AND o.type = :type
                  AND o.difficulty = :difficulty
            """)
    Optional<GameProgress> findByPlayerAndOpponentParams(
            @Param("playerId") Long playerId,
            @Param("gameMode") GameMode gameMode,
            @Param("classUT") String classUT,
            @Param("type") String type,
            @Param("difficulty") OpponentDifficulty difficulty
    );

    Optional<GameProgress> getGameProgressByPlayerProgressAndOpponent(PlayerProgress playerProgress, Opponent opponent);
}
