package com.g2.Game.GameModes;

import com.g2.Game.GameModes.Coverage.CompileResult;
import com.g2.Interfaces.ServiceManager;

public class Allenamento extends GameLogic{

    private int currentTurn;
    private int userScore;
    private int robotScore;
    
    public Allenamento(ServiceManager serviceManager, String PlayerID, String ClasseUT,
            String type_robot, String difficulty, String gamemode) {
        super(serviceManager, PlayerID, ClasseUT, type_robot, difficulty, gamemode);
        currentTurn = 0;
    }

    @Override
    public void NextTurn(int userScore, int robotScore) {
        currentTurn++;
        this.robotScore = robotScore;
        System.out.println("[GAME] Turn " + currentTurn + " played. User Score: " + userScore + ", Robot Score: " + robotScore);
    }

    @Override
    public Boolean isGameEnd() {
        return false; //il giocatore può fare quanti turni vuole quindi ritorno sempre false
    }

    @Override
    public Boolean isWinner() {
        return userScore > robotScore;
    }

    @Override
    public void CreateGame(){
        // Il metodo è intenzionalmente vuoto: L'allenamento non deve creare 
    }
    
    @Override
    public void EndGame(int Score) {
        // Il metodo è intenzionalmente vuoto: L'allenamento non deve creare 
    }

    @Override
    public int GetScore(CompileResult compileResult) {
        // Se loc è 0, il punteggio è sempre 0
        int coverage = compileResult.getLineCoverage().getCovered();
        if (coverage == 0) {
            return 0;
        }
        // Calcolo della percentuale
        double locPerc = ((double) coverage) / 100;
        // Penalità crescente per ogni turno aggiuntivo
        double penaltyFactor = Math.pow(0.9, currentTurn);
        // Calcolo del punteggio
        double score = locPerc * 100 * penaltyFactor;
        this.userScore = (int) Math.ceil(score);
        return userScore;
    }
}
