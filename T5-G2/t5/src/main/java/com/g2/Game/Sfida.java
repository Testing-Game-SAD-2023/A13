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

package com.g2.Game;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.g2.Interfaces.ServiceManager;

public class Sfida extends GameLogic {

    private int currentTurn;
    private int userScore;
    private int robotScore;
    private int totalTurns = 10;
    private Boolean isGameEnd;
    private int gameCoverage;   //coverage di ogni partita. Può essere usata come winning condition per il passaggio al turno successivo.

    //Questa classe si specializza in una partita semplice basata sui turni, prende il nome di Sfida nella UI
    public Sfida(ServiceManager serviceManager, String PlayerID, String ClasseUT,
                                String type_robot, String difficulty, String gamemode) {
        super(serviceManager, PlayerID, ClasseUT, type_robot, difficulty, gamemode);
        currentTurn = 0;
    }

    @Override
    public void playTurn(int userScore, int robotScore, boolean isRoundEnd) {
        String Time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        currentTurn++;
        //CreateTurn(Time, userScore);
        this.isGameEnd = isRoundEnd;
        System.out.println("[GAME] Turn " + currentTurn + " played. User Score: " + userScore + ", Robot Score: " + robotScore);
        
        this.CreateTurn(Time, userScore);
        
        if(isGameEnd){
            this.EndRound(Time);
            //può essere sostituito con userScore
            this.EndGame(Time, userScore, gameCoverage > robotScore);
        }
    }

    @Override
    public Boolean isGameEnd() {
        return this.isGameEnd; 
    }

    @Override
    public int GetScore(int coverage) {
        // Se loc è 0, il punteggio è sempre 0
        if (coverage == 0) {
            return 0;
        }
        this.gameCoverage = coverage;
        // Calcolo della percentuale
        double locPerc = ((double) coverage) / 100;
        // Penalità crescente per ogni turno aggiuntivo
        double penaltyFactor = Math.pow(0.9, currentTurn);
        // Calcolo del punteggio
        double score = locPerc * 100 * penaltyFactor;
        return (int) Math.ceil(score);
    }


    //Aggiunta di un set per la condizione di game over

    public int GetCoverage(){
        return this.gameCoverage;
    }

}
