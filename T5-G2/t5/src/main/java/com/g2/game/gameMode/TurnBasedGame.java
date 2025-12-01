/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.g2.game.gameMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.g2.game.gameMode.Compile.CompileResult;
import com.g2.interfaces.ServiceManager;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Classe base astratta per giochi basati sui turni.
 * Contiene la logica comune per calcolare punteggi, gestire turni e verificare achievement.
 * Estende GameLogic e aggiunge i campi userScore e robotScore.
 */
@Getter
@Setter
public abstract class TurnBasedGame extends GameLogic {

    @JsonIgnore
    private static final Logger logger = LoggerFactory.getLogger(TurnBasedGame.class);

    @JsonProperty("userScore")
    protected int userScore;

    @JsonProperty("robotScore")
    protected int robotScore;

    @JsonProperty("remainingTime")
    protected int remainingTime;

    // ========================================
    // COSTRUTTORI
    // ========================================

    public TurnBasedGame() {
        super();
    }

    public TurnBasedGame(ServiceManager serviceManager, Long playerId, String classUT,
                         String opponentType, OpponentDifficulty difficulty, 
                         GameMode gameMode, String testingClassCode) {
        super(serviceManager, playerId, classUT, opponentType, difficulty, gameMode, testingClassCode);
    }

    public TurnBasedGame(ServiceManager serviceManager, Long playerId, String classUT,
                         String opponentType, OpponentDifficulty difficulty, 
                         GameMode gameMode, String testingClassCode, int remainingTime) {
        super(serviceManager, playerId, classUT, opponentType, difficulty, gameMode, testingClassCode);
        this.remainingTime = remainingTime;
    }

    // ========================================
    // LOGICA COMUNE DEI TURNI
    // ========================================

    @Override
    public void nextTurn(CompileResult userCompileResult, CompileResult robotCompileResult) {
        this.robotScore = getScore(robotCompileResult);
        this.userScore = getScore(userCompileResult);
        startTurn();
        logger.info("Created turn {} for game {}", this.getCurrentTurn(), this.getGameID());
        endTurn(userCompileResult);
        logger.info("[GAME] Turn {} played. User Score: {} , Robot Score: {}", 
                   getCurrentTurn(), userScore, robotScore);
    }

    // ========================================
    // CALCOLO PUNTEGGIO COMUNE
    // ========================================

    @Override
    public int getScore(CompileResult compileResult) {
        // Se loc è 0, il punteggio è sempre 0
        if (compileResult == null)
            return 0;

        int coverage = compileResult.getInstructionCoverage().getCovered();
        if (coverage == 0) {
            return 0;
        }
        // Calcolo della percentuale
        int total = coverage + compileResult.getInstructionCoverage().getMissed();
        double locPerc = (double) coverage / (double) total;
        return (int) Math.ceil(locPerc * 100);
    }

    // ========================================
    // VERIFICA VITTORIA COMUNE
    // ========================================

    @Override
    public boolean isWinner() {
        return userScore > 0 && robotScore >= 0 && userScore >= robotScore;
    }

    // ========================================
    // ACHIEVEMENT COMUNI
    // ========================================

    @Override
    public Map<String, BiFunction<CompileResult, CompileResult, Boolean>> gameModeAchievements() {
        Map<String, BiFunction<CompileResult, CompileResult, Boolean>> verifyBeaten = new HashMap<>();
        verifyBeaten.put("instructions", this::beatOnJacocoInstructionCoverage);
        verifyBeaten.put("instructionsAndWeakMutation", (user, robot) ->
                beatOnJacocoInstructionCoverage(user, robot) && beatOnEvosuiteWeakMutationCoverage(user, robot));

        logger.info("gameModeAchievement: {}", verifyBeaten);
        return verifyBeaten;
    }

    // ========================================
    // METODI PROTETTI PER ACHIEVEMENT
    // ========================================

    protected Boolean beatOnJacocoInstructionCoverage(CompileResult user, CompileResult robot) {
        return user.getInstructionCoverage().getCovered() > robot.getInstructionCoverage().getCovered();
    }

    protected Boolean beatOnEvosuiteWeakMutationCoverage(CompileResult user, CompileResult robot) {
        return user.getEvosuiteWeakMutation().getCovered() > robot.getEvosuiteWeakMutation().getCovered();
    }
}
