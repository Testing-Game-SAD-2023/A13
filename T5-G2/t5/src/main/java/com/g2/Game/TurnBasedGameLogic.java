package com.g2.Game;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.g2.Interfaces.ServiceManager;

public class TurnBasedGameLogic extends GameLogic {

    private GameState currentState;
    private int currentTurn;
    private int userScore;
    private int robotScore;
    private int totalTurns = 10;

    public TurnBasedGameLogic(ServiceManager serviceManager, String PlayerID, String ClasseUT,
            String type_robot, String difficulty) {
        super(serviceManager, PlayerID, ClasseUT, type_robot, difficulty);
        this.currentTurn = 0;
        this.currentState = GameState.START; // Imposta lo stato iniziale
    }

    @Override
    public void playTurn(int userScore, int robotScore) {
        String Time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        switch (currentState) {
            case START -> {
                //Creo partita
                CreateGame(Time);
                currentState = GameState.PLAYING;
                System.out.println("Game Started! Good luck!");
            }
            case PLAYING -> {
                currentTurn++;
                CreateTurn(Time, userScore);
                System.out.println("Turn " + currentTurn + " played. User Score: " + userScore + ", Robot Score: " + robotScore);
                // Controlla se tutti i turni sono stati giocati
                if (currentTurn >= totalTurns) {
                    currentState = GameState.GAME_OVER;
                    System.out.println("Game Over! ");
                }
            }
            case GAME_OVER -> {
                if (robotScore > userScore) {
                    System.out.println("The game is already over, ROBOT win");
                } else {
                    System.out.println("The game is already over, USER win");
                }
                //qua devo mettere la chiusura del gioco e del round
                EndRound(Time);
                EndGame(Time, userScore, userScore>robotScore);
            }
            default -> {
                throw new IllegalStateException("Unexpected state: " + currentState);
            }
        }
    }

    @Override
    public Boolean isGameEnd() {
        return GameState.GAME_OVER == currentState;
    }

    @Override
    public int GetScore(int coverage) {
        // Se loc è 0, il punteggio è sempre 0
        if (coverage == 0) {
            return 0;
        }

        // Calcolo della percentuale della posizione
        double locPerc = ((double) coverage) / 100;
        // Penalità crescente per ogni turno aggiuntivo
        double penaltyFactor = Math.pow(0.9, currentTurn);
        // Calcolo del punteggio
        double score = locPerc * 100 * penaltyFactor;
        return (int) Math.ceil(score);
    }

}
