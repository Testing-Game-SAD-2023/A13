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

    private final String type_robot;
    private final String difficulty;

    public GameLogic(ServiceManager serviceManager, String PlayerID, String ClasseUT,
            String type_robot, String difficulty) {
        this.serviceManager = serviceManager;
        this.PlayerID = PlayerID;
        this.ClasseUT = ClasseUT;
        this.type_robot = type_robot;
        this.difficulty = difficulty;
    }

    // Metodi che ogni gioco deve implementare
    public abstract void playTurn(int userScore, int robotScore);

    public abstract Boolean isGameEnd();

    public abstract int GetScore(int cov);

    //Metodi base 
    protected void CreateGame(String Time) {
        this.GameID = (String) serviceManager.handleRequest("T4", "CreateGame", Time, "difficulty", "name", "description", this.PlayerID);
        this.RoundID = (String) serviceManager.handleRequest("T4", "CreateRound", this.GameID, ClasseUT, Time);
    }

    protected void CreateTurn(String Time, int userScore) {
        //Apro un nuovo turno
        this.TurnID = (String) serviceManager.handleRequest("T4", "CreateTurn", this.PlayerID, this.RoundID, Time);
        //Chiudo il turno 
        serviceManager.handleRequest("T4", "EndTurn", userScore, Time, this.TurnID);
    }

    protected void EndRound(String Time) {
        this.serviceManager.handleRequest("T4", "EndRound", Time, this.RoundID);
    }

    protected void EndGame(String Time, int Score, Boolean isWinner){
        this.serviceManager.handleRequest("T4","EndGame", this.GameID, this.PlayerID, Time, Score, isWinner);
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

    public String getType_robot() {
        return type_robot;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getClasseUT() {
        return ClasseUT;
    }

}
