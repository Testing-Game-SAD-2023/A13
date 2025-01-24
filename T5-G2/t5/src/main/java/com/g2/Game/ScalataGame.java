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

import java.util.ArrayList;
import java.util.List;

import com.commons.model.Gamemode;
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
    private int currentRound;       //usata per contare il progresso della scalata
    private int currentRoundIndex;  //indice usato per la lettura della lista interna

    public ScalataGame(ServiceManager serviceManager, String playerID, String classeUT,
                       List<String>classes, List<String> typesRobot, List<String> difficulties, String mode) {
        super(serviceManager, playerID, classeUT, typesRobot.get(0), difficulties.get(0), mode); 
        this.games = new ArrayList<>();
        this.currentRound = 1; // Inizia dal round 1
        this.currentRoundIndex = 0;
        this.currentStatus = ScalataGamestatus.IN_PROGRESS;

        for (int i = 0; i < classes.size(); i++) {
            String classe = classes.get(i);
            String typeRobot = typesRobot.get(i);
            String difficulty = difficulties.get(i);
            games.add(new Sfida(serviceManager, playerID, classe, typeRobot, difficulty, mode));
        }
    }

    @Override
    //con isGameEnd ci si riferisce al termine del round all'interno di una scalata, e non alla scalata stessa.
    public void playTurn(int userScore, int robotScore, boolean isGameEnd) {

        if (currentRoundIndex < games.size()) {
            Sfida currentGame = games.get(currentRoundIndex);
            currentGame.playTurn(userScore, robotScore,isGameEnd);
            
            // Verifica se il gioco corrente è finito. Per gioco s'intende il round della scalata
            if (currentGame.isGameEnd() && userScore >= robotScore) {
                System.out.println("[SCALATAGAME][T5] Round " + currentRound + " completed.");
                    currentRoundIndex++; // Passa al gioco successivo
                    currentRound++;

                    //adesso viene controllato se la scalata è terminata
                    if(isGameEnd()){
                        System.out.println("[SCALATAGAME][T5] Scalata  completed.");
                        currentStatus = ScalataGamestatus.WIN;
                    }

            }
            else if (currentGame.isGameEnd() && userScore< robotScore) {
                System.out.println("[SCALATAGAME][T5] Round " + currentRound + " lost.");
                currentStatus = ScalataGamestatus.LOST;
            }
            else {
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
        return currentRoundIndex >= games.size();
    }

    @Override
    public int GetScore(int coverage) {
        // Implementa la logica per calcolare il punteggio totale tra tutti i giochi
        int totalScore = 0;
        for (Sfida game : games) {
            totalScore += game.GetScore(coverage); // Calcola il punteggio per ogni gioco
        }
        return totalScore;
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

    //funzione per ottenere lo stato interno della scalata
    public ScalataGamestatus getStatus(){
        return currentStatus;
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
}
