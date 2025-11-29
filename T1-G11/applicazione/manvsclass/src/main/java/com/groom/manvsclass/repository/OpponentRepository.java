package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Opponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;

import java.util.List;
import java.util.Optional;

public interface OpponentRepository extends JpaRepository<Opponent, Long> {

    @Query("SELECT o FROM Opponent o WHERE o.classUT.name = :className AND o.type = :type AND o.classUT.difficulty = :difficulty")
    Optional<Opponent> findOpponent(
            @Param("className") String className,
            @Param("type") String type,
            @Param("difficulty") OpponentDifficulty difficulty
    );

    @Query("SELECT o FROM Opponent o")
    List<Opponent> findAllOpponents();

    @Query("SELECT o.coverage FROM Opponent o WHERE o.classUT.name = :className AND o.type = :type AND o.classUT.difficulty = :difficulty")
    Optional<String> findCoverage(
            @Param("className") String className,
            @Param("type") String type,
            @Param("difficulty") OpponentDifficulty difficulty
    );

    @Query("SELECT o.jacocoScore FROM Opponent o WHERE o.classUT.name = :className AND o.type = :type AND o.classUT.difficulty = :difficulty")
    Optional<JacocoScore> findJacocoScore(
            @Param("className") String className,
            @Param("type") String type,
            @Param("difficulty") OpponentDifficulty difficulty
    );

    @Query("SELECT o.evosuiteScore FROM Opponent o WHERE o.classUT.name = :className AND o.type = :type AND o.classUT.difficulty = :difficulty")
    Optional<EvosuiteScore> findEvosuiteScore(
            @Param("className") String className,
            @Param("type") String type,
            @Param("difficulty") OpponentDifficulty difficulty
    );

}
