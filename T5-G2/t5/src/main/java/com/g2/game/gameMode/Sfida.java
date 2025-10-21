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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.g2.game.gameMode.Compile.CompileResult;
import com.g2.interfaces.ServiceManager;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Sfida extends GameLogic {

    @JsonProperty("currentTurn")
    private int currentTurn;
    @JsonProperty("userScore")
    private int userScore;
    @JsonProperty("robotScore")
    private int robotScore;

    public Sfida() {
        //Costruttore vuoto
    }

    //Questa classe si specializza in una partita semplice basata sui turni, prende il nome di Sfida nella UI
    public Sfida(ServiceManager serviceManager, Long PlayerID, String ClasseUT,
                 String type_robot, OpponentDifficulty difficulty, GameMode gamemode, String testingClassCode) {
        super(serviceManager, PlayerID, ClasseUT, type_robot, difficulty, gamemode, testingClassCode);
        currentTurn = 0;
    }

    @Override
    public void nextTurn(CompileResult userScore, CompileResult robotScore) {
        currentTurn++;
        this.robotScore = getScore(robotScore);
        startTurn();
        System.out.println("[GAME] Turn " + currentTurn + " played. User Score: " + userScore + ", Robot Score: " + robotScore);
    }

    @Override
    public Boolean isGameEnd() {
        return false; //il giocatore può fare quanti turni vuole quindi ritorno sempre false
    }

    @Override
    public boolean isWinner() {
        return userScore > robotScore;
    }

    @Override
    public int getScore(CompileResult compileResult) {
        // Se loc è 0, il punteggio è sempre 0
        int coverage = compileResult.getLineCoverage().getCovered();
        if (coverage == 0) {
            return 0;
        }
        // Calcolo della percentuale
        double locPerc = ((double) coverage) / 100;
        // Penalità crescente per ogni turno aggiuntivo
        double penaltyFactor = Math.pow(0.9, currentTurn);
        // Calcolo del punteggio
        double score = locPerc * 100 * penaltyFactor;
        this.userScore = (int) Math.ceil(score);
        return userScore;
    }
}
