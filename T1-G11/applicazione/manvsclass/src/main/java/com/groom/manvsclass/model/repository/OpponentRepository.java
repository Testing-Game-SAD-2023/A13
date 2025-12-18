package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.entity.OpponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpponentRepository extends JpaRepository<OpponentEntity, Long> {

    Optional<OpponentEntity> findByClassUt_NameAndOpponentTypeAndOpponentDifficulty(
            String classUtName,
            String opponentType,
            OpponentDifficulty opponentDifficulty
    );

    @Query("SELECT o.evosuiteScore FROM OpponentEntity o " +
            "WHERE o.classUt.name = :className " +
            "AND o.opponentType = :type " +
            "AND o.opponentDifficulty = :difficulty")
    Optional<EvosuiteScore> findEvosuiteScore(
            @Param("className") String className,
            @Param("type") String type,
            @Param("difficulty") OpponentDifficulty difficulty
    );

    @Query("SELECT o.jacocoScore FROM OpponentEntity o " +
            "WHERE o.classUt.name = :className " +
            "AND o.opponentType = :type " +
            "AND o.opponentDifficulty = :difficulty")
    Optional<JacocoScore> findJacocoScore(
            @Param("className") String className,
            @Param("type") String type,
            @Param("difficulty") OpponentDifficulty difficulty
    );

    @Query("SELECT o.coverage FROM OpponentEntity o " +
            "WHERE o.classUt.name = :className " +
            "AND o.opponentType = :type " +
            "AND o.opponentDifficulty = :difficulty")
    Optional<String> findCoverage(
            @Param("className") String className,
            @Param("type") String type,
            @Param("difficulty") OpponentDifficulty difficulty
    );

    List<OpponentEntity> findByClassUt_Name(String className);

    @Modifying
    @Query("DELETE FROM OpponentEntity o WHERE o.classUt.name = :className")
    void deleteByClassUtName(@Param("className") String className);
}

