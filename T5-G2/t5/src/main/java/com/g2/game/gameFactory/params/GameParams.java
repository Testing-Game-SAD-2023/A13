package com.g2.game.gameFactory.params;

import lombok.Getter;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

@Getter
public class GameParams {
    private final Long playerId;
    private final String classUTName;
    private final String classUTCode;
    private final String opponentType;
    private final OpponentDifficulty opponentDifficulty;
    private final GameMode gameMode;
    private final String testClassCode;

    // Costruttore che inizializza l'oggetto GameParams per creare una nuova GameLogic
    public GameParams(Long playerId, String classUTName, String opponentType, OpponentDifficulty opponentDifficulty, GameMode gameMode) {
        this.playerId = playerId;
        this.classUTName = classUTName;
        this.opponentType = opponentType;
        this.opponentDifficulty = opponentDifficulty;
        this.gameMode = gameMode;
        this.testClassCode = null;
        this.classUTCode = null;
    }

    // Costruttore che inizializza l'oggetto GameParams per aggiornare una GameLogic esistente
    public GameParams(long playerId, GameMode gameMode, String classUTCode, String testClassCode) {
        this.playerId = playerId;
        this.gameMode = gameMode;
        this.classUTCode = classUTCode;
        this.testClassCode = testClassCode;
        this.classUTName = null;
        this.opponentType = null;
        this.opponentDifficulty = null;
    }

    public GameParams(Long playerId, String classUTName, String classUTCode, String opponentType, OpponentDifficulty opponentDifficulty, GameMode gameMode, String testClassCode) {
        this.playerId = playerId;
        this.classUTName = classUTName;
        this.classUTCode = classUTCode;
        this.opponentType = opponentType;
        this.opponentDifficulty = opponentDifficulty;
        this.gameMode = gameMode;
        this.testClassCode = testClassCode;
    }

    @Override
    public String toString() {
        return "GameParams{" +
                "playerId='" + playerId + '\'' +
                ", underTestClassName='" + classUTName + '\'' +
                ", type_robot='" + opponentType + '\'' +
                ", difficulty='" + opponentDifficulty + '\'' +
                ", mode='" + gameMode + '\'' +
                ", testingClassCode='" + testClassCode + '\'' +
                '}';
    }
}
