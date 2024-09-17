package com.g2.Components;

import java.util.HashMap;
import java.util.Map;

public class TurnBasedGameComponent extends PageComponent {

    private final ServiceManager serviceManager;
    private final int maxTurns;
    private int currentTurn;
    private int playerWins;
    private int botWins;

    // Costruttore
    public TurnBasedGameComponent(ServiceManager serviceManager, int maxTurns) {
        this.serviceManager = serviceManager;
        this.maxTurns = maxTurns;
        this.currentTurn = 0;
        this.playerWins = 0;
        this.botWins = 0;
    }

    @Override
    public Map<String, Object> getModel() {
        Map<String, Object> model = new HashMap<>();
        model.put("currentTurn", currentTurn);
        model.put("playerWins", playerWins);
        model.put("botWins", botWins);
        model.put("maxTurns", maxTurns);
        model.put("winner", determineWinner());

        return model;
    }

    // Metodo per processare un turno
    public void playTurn() {
        if (currentTurn < maxTurns) {
            currentTurn++;

            // Simula il risultato del turno (puoi sostituire con la logica del tuo gioco)
            boolean playerWon = simulateTurnResult();
            if (playerWon) {
                playerWins++;
            } else {
                botWins++;
            }
        }
    }

    // Metodo per determinare chi ha vinto la partita alla fine dei turni
    private String determineWinner() {
        if (currentTurn < maxTurns) {
            return "In corso";
        }
        if (playerWins > botWins) {
            return "Giocatore";
        } else if (botWins > playerWins) {
            return "Bot";
        } else {
            return "Pareggio";
        }
    }

    // Simula il risultato di un turno (logica del gioco)
    private boolean simulateTurnResult() {
        // Simula il risultato casuale di un turno, potrebbe essere migliorato con una logica più complessa
        return Math.random() > 0.5;
    }
}