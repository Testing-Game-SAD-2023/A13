package com.g2.Components;

import java.util.HashMap;
import java.util.Map;

import com.g2.Interfaces.ServiceManager;

public class TurnBasedGameComponent extends PageComponent {

    // Definizione degli stati del gioco
    private enum GameState {
        START, PLAYING, END_TURN, GAME_OVER
    }

    private final ServiceManager serviceManager;
    private final int maxTurns;
    private int currentTurn;
    private int playerWins;
    private int botWins;
    private GameState currentState;

    // Costruttore
    public TurnBasedGameComponent(ServiceManager serviceManager, int maxTurns) {
        this.serviceManager = serviceManager;
        this.maxTurns = maxTurns;
        this.currentTurn = 0;
        this.playerWins = 0;
        this.botWins = 0;
        this.currentState = GameState.START; // Stato iniziale
    }

    // qui aggiustare con Model/game fatto dal t5 questo deve fornire solo le info per la pagina web non tutte quelle della classe
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

    // Metodo per processare un turno in base allo stato corrente
    // con l'override posso definire altri tipi di giochi 
    public void playTurn() {
        switch (currentState) {
            case START -> // Inizia il gioco, passa allo stato di gioco attivo
                currentState = GameState.PLAYING;

            case PLAYING -> {
                // Simula il round (turno completo di giocatore e bot)
                boolean playerWon = PlayTurn();
                if (playerWon) {
                    playerWins++;
                } else {
                    botWins++;
                }
                currentState = GameState.END_TURN;
            }

            case END_TURN -> {
                // Aggiorna il turno corrente e controlla se il gioco è finito
                currentTurn++;
                if (currentTurn >= maxTurns) {
                    currentState = GameState.GAME_OVER;
                } else {
                    currentState = GameState.PLAYING; // Continua il gioco
                }
            }

            case GAME_OVER -> {
            }
        }
        // Il gioco è finito, non fare nulla
    }

    // Metodo per determinare chi ha vinto la partita
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

    // Simula il risultato di un turno
    private boolean PlayTurn() {
        return Math.random() > 0.5;
    }

}