package com.g2.Components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.g2.Interfaces.ServiceManager;

public class ScalataGameComponent {

    private final ServiceManager serviceManager;
    private final int numPartite;
    private int currentGameIndex;
    private final List<TurnBasedGameComponent> partite = new ArrayList<>();
    private int playerWins;
    private int botWins;

    // Costruttore
    public ScalataGameComponent(ServiceManager serviceManager, int numPartite, int maxTurnsPerGame) {
        this.serviceManager = serviceManager;
        this.numPartite = numPartite;
        this.currentGameIndex = 0;
        this.playerWins = 0;
        this.botWins = 0;

        // Crea le partite con TurnBasedGameComponent
        for (int i = 0; i < numPartite; i++) {
            this.partite.add(new TurnBasedGameComponent(serviceManager, maxTurnsPerGame));
        }
    }

    // Metodo per giocare il turno corrente della partita attuale
    public void playTurn() {
        if (currentGameIndex < numPartite) {
            TurnBasedGameComponent currentGame = partite.get(currentGameIndex);
            currentGame.playTurn();

            // Se la partita corrente è finita, passa alla prossima
            if ((int) currentGame.getModel().get("currentTurn") == (int) currentGame.getModel().get("maxTurns")) {
                String winner = (String) currentGame.getModel().get("winner");
                if (winner.equals("Giocatore")) {
                    playerWins++;
                } else if (winner.equals("Bot")) {
                    botWins++;
                }
                currentGameIndex++;
            }
        }
    }

    // Metodo per determinare chi ha vinto la scalata
    public String determineOverallWinner() {
        if (currentGameIndex < numPartite) {
            return "Scalata in corso";
        }

        if (playerWins > botWins) {
            return "Giocatore ha vinto la scalata";
        } else if (botWins > playerWins) {
            return "Bot ha vinto la scalata";
        } else {
            return "Scalata terminata in pareggio";
        }
    }

    // Ottiene il modello per la visualizzazione nel template
    public Map<String, Object> getModel() {
        TurnBasedGameComponent currentGame = partite.get(currentGameIndex);
        Map<String, Object> model = currentGame.getModel();
        model.put("overallPlayerWins", playerWins);
        model.put("overallBotWins", botWins);
        model.put("currentGameIndex", currentGameIndex + 1); // 1-based index per la UI
        model.put("numPartite", numPartite);
        model.put("overallWinner", determineOverallWinner());
        return model;
    }
}
