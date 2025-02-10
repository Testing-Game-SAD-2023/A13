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
     
     private boolean youlose;
     private int tot_score;
     private int currentRound;
 

        public ScalataGame(ServiceManager serviceManager, String playerID, String classeUT,
                          String typeRobot, String difficulty, String mode) {

            super(serviceManager, playerID, classeUT, typeRobot, difficulty, mode); 
            youlose=false;
            tot_score=0;
            currentRound=0;
            }
            
 
     @Override
     public void playTurn(int userScore, int robotScore) {
        currentRound++;
         if (robotScore>userScore){
            youlose=true;
         }else{
            youlose=false;
         }
     }
 
     @Override
     public Boolean isGameEnd() {
         return youlose;
     }
 
     @Override
     public int GetScore(int coverage) {
         // Implementa la logica per calcolare il punteggio totale tra tutti i giochi
         tot_score=tot_score+coverage;
         return tot_score;
     }
 
     // Altri metodi necessari per gestire la logica del gioco
 }