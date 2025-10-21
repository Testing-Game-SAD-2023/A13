package com.groom.manvsclass.model.repository;

import com.groom.manvsclass.model.Opponent;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;

import java.util.List;
import java.util.Optional;

public interface OpponentRepository {
    void saveOpponent(Opponent opponent);

    List<Opponent> findAllOpponents();

    Optional<Opponent> findOpponent(String classUT, String type, OpponentDifficulty difficulty);

    Optional<EvosuiteScore> findEvosuiteScore(String classUT, String type, OpponentDifficulty difficulty);

    Optional<JacocoScore> findJacocoScore(String classUT, String type, OpponentDifficulty difficulty);

    Optional<String> findCoverage(String classUT, String type, OpponentDifficulty difficulty);
}
