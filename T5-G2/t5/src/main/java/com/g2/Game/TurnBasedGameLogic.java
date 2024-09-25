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

    public TurnBasedGameLogic(ServiceManager serviceManager, String PlayerID, String ClasseUT) {
        super(serviceManager, PlayerID, ClasseUT);
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
                if(robotScore > userScore){
                    System.out.println("The game is already over, ROBOT win");
                }else{
                    System.out.println("The game is already over, USER win");
                }
                //qua devo mettere la chiusura del gioco e del round
                EndRound(Time);
                EndGame(); 
            }
            default -> {
                throw new IllegalStateException("Unexpected state: " + currentState);
            }
        }
    }

    @Override
    public Boolean isGameOver() {
        return GameState.GAME_OVER == currentState;
    }

}
