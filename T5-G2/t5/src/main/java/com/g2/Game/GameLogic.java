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

public abstract class GameLogic {

    private final ServiceManager serviceManager;

    //IDs
    private int GameID;
    private int  RoundID;
    private String TurnID;
    private final String PlayerID;
    private String ClasseUT;
    private String type_robot;
    private String difficulty;
    private String gamemode;

    public GameLogic(ServiceManager serviceManager, String PlayerID, String ClasseUT,
            String type_robot, String difficulty, String gamemode) {
        this.serviceManager = serviceManager;
        this.PlayerID = PlayerID;
        this.ClasseUT = ClasseUT;
        this.type_robot = type_robot;
        this.difficulty = difficulty;
        this.gamemode = gamemode;
    }

    // Metodi che ogni gioco deve implementare
    /*
     * PlayTurn deve aggiornalo lo stato della partita ad ogni turno, il concetto di turno può esser gestito come si vuole
     */
    public abstract void playTurn(int userScore, int robotScore);
    /*
     * Si deve personalizzare la condizione di fine del gioco, in generale l'utente può sempre decretarne la fine tramite l'editor.
     */
    public abstract Boolean isGameEnd();
    /*
     * In base alla modalità va specificato come viene calcolato lo score, solo la COV viene fornita gli altri sono 
     * parametri interni alla classe di gioco.
     */
    public abstract int GetScore(int cov);

    /*
     * Realizzati partendo dal Service Manager per semplificare l'interfacciamento con il task T4 
     */
    protected void CreateGame() {
        String Time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        this.GameID = (int) serviceManager.handleRequest("T4", "CreateGame", Time, "difficulty", "name", this.gamemode, this.PlayerID);
        this.RoundID = (int) serviceManager.handleRequest("T4", "CreateRound", this.GameID, this.ClasseUT, Time);
    }

    protected void CreateTurn(String Time, int userScore) {
        //Apro un nuovo turno
        this.TurnID = (String) serviceManager.handleRequest("T4", "CreateTurn", this.PlayerID, this.RoundID, Time);
        //Chiudo il turno 
        serviceManager.handleRequest("T4", "EndTurn", userScore, Time, this.TurnID);
    }

    protected void EndRound(String Time) {
        this.serviceManager.handleRequest("T4", "EndRound", Time, this.RoundID);
    }

    protected void EndGame(String Time, int Score, Boolean isWinner){
        this.serviceManager.handleRequest("T4","EndGame", this.GameID, this.PlayerID, Time, Score, isWinner);
    }

    public int getGameID() {
        return this.GameID;
    }

    public void setGameID(int GameID) {
        this.GameID = GameID;
    }

    public int getRoundID() {
        return this.RoundID;
    }

    public void setRoundID(int RoundID) {
        this.RoundID = RoundID;
    }

    public String getTurnID() {
        return this.TurnID;
    }

    public void setTurnID(String TurnID) {
        this.TurnID = TurnID;
    }

    public String getType_robot() {
        return this.type_robot;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public String getClasseUT() {
        return this.ClasseUT;
    }

    public Boolean CheckGame(String type_robot, String difficulty, String underTestClassName){
        if( this.type_robot.equals(type_robot) && 
            this.difficulty.equals(difficulty) &&
            this.ClasseUT.equals(underTestClassName)){
                return true;
            }else{
                return false;
            }
    }

    public String getMode() {
        return gamemode;
    }

    public void setMode(String mode) {
        this.gamemode = gamemode;
    }


}
