package com.g2.game.gameFactory.params;

import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

public class PartitaSingolaParams extends GameParams {
    int remainingTime;

    public PartitaSingolaParams(Long playerId, String underTestClassName, String opponentType, OpponentDifficulty difficulty, GameMode mode, int remainingTime) {
        super(playerId, underTestClassName, opponentType, difficulty, mode);
        this.remainingTime = remainingTime;
    }

    public PartitaSingolaParams(Long playerId, GameMode gameMode, String classUTCode, String testClassCode, int remainingTime) {
        super(playerId, gameMode, classUTCode, testClassCode);
        this.remainingTime = remainingTime;
    }

    public PartitaSingolaParams(Long playerId, String underTestClassName, String classUTCode, String opponentType, OpponentDifficulty difficulty, GameMode mode,
                                String testingClassCode, int remainingTime) {
        super(playerId, underTestClassName, classUTCode, opponentType, difficulty, mode, testingClassCode);
        this.remainingTime = remainingTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    @Override
    public String toString() {
        return "PartitaSingolaParams{" +
                super.toString() +
                "remainingTime=" + remainingTime +
                '}';
    }
}
