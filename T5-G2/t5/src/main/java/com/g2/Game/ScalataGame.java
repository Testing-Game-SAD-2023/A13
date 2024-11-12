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
    private final List<Sfida> games;
    private int currentRound;
    private int currentGameIndex;

    public ScalataGame(ServiceManager serviceManager, String playerID, String classeUT,
                       List<String> typesRobot, List<String> difficulties, String mode) {
        super(serviceManager, playerID, classeUT, typesRobot.get(0), difficulties.get(0), mode); 
        this.games = new ArrayList<>();
        this.currentRound = 1; // Inizia dal round 1
        this.currentGameIndex = 0; // Indice del gioco corrente

        for (int i = 0; i < typesRobot.size(); i++) {
            String typeRobot = typesRobot.get(i);
            String difficulty = difficulties.get(i);
            games.add(new Sfida(serviceManager, playerID, classeUT, typeRobot, difficulty, mode));
        }
    }

    @Override
    public void playTurn(int userScore, int robotScore) {
        if (currentGameIndex < games.size()) {
            Sfida currentGame = games.get(currentGameIndex);
            currentGame.playTurn(userScore, robotScore);

            // Verifica se il gioco corrente è finito
            if (currentGame.isGameEnd()) {
                System.out.println("Round " + currentRound + " completed.");
                currentGameIndex++; // Passa al gioco successivo
                currentRound++; // Incrementa il contatore dei round
            }
        } else {
            System.out.println("All games have been played.");
        }
    }

    @Override
    public Boolean isGameEnd() {
        return currentGameIndex >= games.size();
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

    // Altri metodi necessari per gestire la logica del gioco
}
