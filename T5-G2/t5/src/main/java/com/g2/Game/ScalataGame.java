package com.g2.Game;

import java.util.ArrayList;
import java.util.List;

import com.g2.Interfaces.ServiceManager;

public class ScalataGame extends GameLogic {
    private final List<TurnBasedGameLogic> games;
    private int currentRound;
    private int currentGameIndex;

    public ScalataGame(ServiceManager serviceManager, String playerID, String classeUT,
                       List<String> typesRobot, List<String> difficulties) {
        super(serviceManager, playerID, classeUT, typesRobot.get(0), difficulties.get(0)); 
        this.games = new ArrayList<>();
        this.currentRound = 1; // Inizia dal round 1
        this.currentGameIndex = 0; // Indice del gioco corrente

        for (int i = 0; i < typesRobot.size(); i++) {
            String typeRobot = typesRobot.get(i);
            String difficulty = difficulties.get(i);
            games.add(new TurnBasedGameLogic(serviceManager, playerID, classeUT, typeRobot, difficulty));
        }
    }

    @Override
    public void playTurn(int userScore, int robotScore) {
        if (currentGameIndex < games.size()) {
            TurnBasedGameLogic currentGame = games.get(currentGameIndex);
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
        for (TurnBasedGameLogic game : games) {
            totalScore += game.GetScore(coverage); // Calcola il punteggio per ogni gioco
        }
        return totalScore;
    }

    // Altri metodi necessari per gestire la logica del gioco
}
