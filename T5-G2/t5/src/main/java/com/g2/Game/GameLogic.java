package com.g2.Game;

import com.g2.Interfaces.ServiceManager;

public abstract class GameLogic {
    private final ServiceManager serviceManager; 

    //IDs
    private String GameID;
    private String RoundID;
    private String TurnID;
    private final String PlayerID;
    private final String ClasseUT;

    public GameLogic(ServiceManager serviceManager, String PlayerID, String ClasseUT) {
        this.serviceManager = serviceManager;
        this.PlayerID = PlayerID;
        this.ClasseUT = ClasseUT;
    }

    // Metodi che ogni gioco deve implementare
    public abstract void playTurn(int userScore, int robotScore);
    public abstract Boolean isGameOver();

    //Metodi base 
    protected void CreateGame(String Time){
        this.GameID  = (String) serviceManager.handleRequest("T4", "CreateGame", Time, "difficulty", "name", "description", "username");
        this.RoundID = (String) serviceManager.handleRequest("T4", "CreateRound", this.GameID, ClasseUT, Time);
    }

    protected void CreateTurn(String Time, int userScore){
        //Apro un nuovo turno
        this.TurnID  = (String)serviceManager.handleRequest("T4", "CreateTurn", this.PlayerID, this.RoundID, Time);
        //Chiudo il turno 
        serviceManager.handleRequest("T4", "EndTurn", userScore, Time, this.TurnID);
    }

    protected void EndGame(){

    }

    protected void EndRound(String Time){
        this.serviceManager.handleRequest("T4", "EndRound", Time, this.RoundID);
    }

    public String getGameID() {
        return GameID;
    }

    public void setGameID(String GameID) {
        this.GameID = GameID;
    }

    public String getRoundID() {
        return RoundID;
    }

    public void setRoundID(String RoundID) {
        this.RoundID = RoundID;
    }

    public String getTurnID() {
        return TurnID;
    }

    public void setTurnID(String TurnID) {
        this.TurnID = TurnID;
    }
}
