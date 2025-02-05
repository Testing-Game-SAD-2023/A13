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
import java.util.ArrayList;
import java.util.List;

import com.g2.Interfaces.ServiceManager;

public class ScalataGame extends GameLogic {
    //enum statico che mantiene traccia del progresso della scalata
    static enum ScalataGamestatus {
        IN_PROGRESS,
        WIN,
        LOST
    }

    private ScalataGamestatus currentStatus;
    private final List<Sfida> games;
    private int id_scalata;
    private String scalata_name;
    private int currentRound;       //usata per contare il progresso della scalata
    private int currentRoundIndex;  //indice usato per la lettura della lista interna
    private int totalScore;
    private int gameCoverage;   //coverage di ogni partita usato per passare o meno al round successivo.

    public ScalataGame(ServiceManager serviceManager, String playerID,  String classeUT,
                        String scalata_name,List<String>classes, List<String> typesRobot, List<String> difficulties, String mode) {
        super(serviceManager, playerID, classeUT, typesRobot.get(0), difficulties.get(0), mode); 
        this.games = new ArrayList<>();
        this.currentRound = 1; // Inizia dal round 1
        this.currentRoundIndex = 0;
        this.totalScore = 0 ;
        this.currentStatus = ScalataGamestatus.IN_PROGRESS;
        this.scalata_name = scalata_name;

        for (int i = 0; i < classes.size(); i++) {
            String classe = classes.get(i);
            String typeRobot = typesRobot.get(i);
            String difficulty = difficulties.get(i);
            games.add(new Sfida(serviceManager, playerID, classe, typeRobot, difficulty, mode));
        }
    }

    @Override
    //con isGameEnd ci si riferisce al termine del round all'interno di una scalata, e non alla scalata stessa.
    public void playTurn(int userScore, int robotScore, boolean isRoundEnd) {

        String now = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        if (currentRoundIndex < games.size()) {
            Sfida currentGame = games.get(currentRoundIndex);
            currentGame.playTurn(userScore, robotScore,isRoundEnd);
            
            // Verifica se il gioco corrente è finito. Per gioco s'intende il round della scalata
            // Non so se per il passaggio al turno successivo serva il punteggio o la copertura. In tal caso, la scalata ha un sistema per recuperare entrambi
            if (currentGame.isGameEnd() && userScore >= robotScore) {
                currentGame.EndGame(now, robotScore, true);
                currentGame.EndRound(now);
                
                System.out.println("[SCALATAGAME][T5] Round " + currentRound + " completed.");
                
                currentRoundIndex++; // Passa al gioco successivo
                currentRound++;
                this.totalScore += userScore;
                //aggiornamento del prossimo turno
                
                    //adesso viene controllato se la scalata è terminata
                    if(currentRoundIndex >= games.size()){
                        System.out.println("[SCALATAGAME][T5] Scalata  completed.");
                        currentStatus = ScalataGamestatus.WIN;

                        this.CloseScalata(id_scalata, getRoundID(), true, totalScore);
                    }
                    else{
                        loadNextRound();
                        games.get(currentRoundIndex).CreateGame(id_scalata);
                        this.updateScalata(id_scalata, currentRound);
                    }

            }
            else if (currentGame.isGameEnd() && this.gameCoverage< robotScore) {
                this.currentStatus = ScalataGamestatus.LOST;
                currentGame.EndGame(now, robotScore, false);
                currentGame.EndRound(now);
                currentGame.EndTurn(getTurnID(), now, userScore);

                System.out.println("[SCALATAGAME][T5] Round " + currentRound + " lost.");
                

                //gestione scalata persa
                this.CloseScalata(id_scalata, getRoundID(), false, 0);

            }
            else {
                currentGame.CreateTurn(now, userScore);
                System.out.println("[SCALATAGAME][T5] Compilation command detected.");
                
            }
            
        } else {
            //Scalatagame non ha gestito correttamente gli indici
            
            System.out.println("[SCALATAGAME][T5] errore nella gestione degli indici");
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public Boolean isGameEnd() {
        if(currentStatus == ScalataGamestatus.IN_PROGRESS){
            return false;
        }
        else{
            return true;
        }

    }

    @Override
    public int GetScore(int coverage) {
        // restituisce il punteggio di un round, e incrementa il punteggio totale all'interno della scalata
        //Inoltre, salva all'interno della scalata la copertura della classe, in modo da confrontarla in seguito con il punteggio del robot.
        this.gameCoverage = coverage;
        int score = games.get(currentRoundIndex).GetScore(coverage);
        
        return  score;
    }

    @Override
    public String getClasseUT(){
        return games.get(currentRoundIndex).getClasseUT();
    }
    @Override
    public String getType_robot(){
        return games.get(currentRoundIndex).getType_robot();
    }
    @Override
    public String getDifficulty(){
        return games.get(currentRoundIndex).getDifficulty();
    }
    // Altri metodi necessari per gestire la logica del gioco

    @Override
    protected void CreateGame(){
        createScalata(getPlayerID(),this.scalata_name);
        games.get(currentRoundIndex).CreateGame(id_scalata);
    }

    @Override
    public Boolean CheckGame(String type_robot, String difficulty, String underTestClassName){
        System.out.println("[SCALATAGAME][CHECKGAME]");
        if( this.getType_robot().equals(type_robot) && 
            this.getDifficulty().equals(difficulty) &&
            this.getClasseUT().equals(underTestClassName)){
                return true;
            }else{
                return false;
            }
    }

    //funzione per ottenere lo stato interno della scalata
    public ScalataGamestatus getStatus(){
        return currentStatus;
    }

    public int GetTotalScore() {
        if (currentStatus == ScalataGamestatus.LOST) {
            return 0;
        }
        else{
            return this.totalScore;
        }
    }


    //Chiamate a T4

    public void createScalata(String player_id, String scalata_name){
        String creationDate = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE);
        String creationTime = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        String id_scalata_string = (String)serviceManager.handleRequest(
            "T4", 
            "CreateScalata",
             player_id,
             scalata_name,
             creationTime,
             creationDate);
        this.id_scalata = Integer.parseInt(id_scalata_string);
        System.out.println("scalata with ID: " + this.id_scalata);
    }

    public void updateScalata(int scalata_id, int round_id){
        String updateTime = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        String response = (String)serviceManager.handleRequest(
            "T4",
            "UpdateScalata",
            scalata_id,
            round_id,
            updateTime
            );
        //System.out.println(response);
    }
    public void CloseScalata(int scalata_id, int round_id, boolean is_win, int final_score){
        String closeTime = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        String response = (String)serviceManager.handleRequest(
            "T4",
            "CloseScalata",
            scalata_id,
            round_id,
            is_win,
            final_score,
            closeTime
            );
        System.out.println(response);
    }

    //Viene aggiornato il gameLogic al round successivo, in modo da far risultare il cambio di turno anche al sistema di gioco.
    private void loadNextRound(){

        this.setClasseUT(this.getClasseUT());
        this.setType_Robot(this.getType_robot());
        this.setDifficulty(this.getDifficulty());
    }



//funzione statica per processare i parametri sotto forma di JSON array che vengono passati come parametro nella chiamata
    public static void processScalataParameters (List<String>classes, List<String> typesRobot, List<String> difficulties){
        int rounds =  classes.size();
        
        String temp = "";
        for(int i = 0; i <rounds; i++){
            //processo classe
            temp = classes.get(i).replaceAll("[\\[\\]\"]", "");
            classes.set(i, temp);

            //processo robot
            temp = typesRobot.get(i).replaceAll("[\\[\\]\"]", "");
            typesRobot.set(i, temp);

            //processo difficolta
            temp = difficulties.get(i).replaceAll("[\\[\\]\"]", "");
           
            if (temp.equals("Beginner")) {
                difficulties.set(i, "1");
            } 

            else if (temp.equals("Intermediate")) {
                difficulties.set(i, "2");
            }

            else if (temp.equals("Advanced")) {
                difficulties.set(i, "3");
            }
            
            else {
                difficulties.set(i, "0");
            }
        }
        
    }

    //27GEN AGGIUNTE PER IL GAMECONTROLLER
    public int getId_scalata() {
        return this.id_scalata;
    }

    public void setId_scalata(int id_scalata) {
        this.id_scalata = id_scalata;
    }

    public int getCurrentRoundID() {
        return this.games.get(currentRoundIndex).getRoundID();
    }

    public int getCurrentGameID(){
        return this.games.get(currentRoundIndex).getGameID();
    }

    public String getCurrentTurnID(){
        return this.games.get(currentRoundIndex).getTurnID();
    }
}
