package com.g2.Model;

import java.time.*;
import java.util.List;

public class ScalataGiocata {

    private int playerID;               // Reference to the player ID
    private int ID;                     // ID of the ScalataGiocata
    private String scalataName;         // Reference to the name of the Scalata played

    private List<Game> games;           // List of games of the ScalataGiocata

    private LocalDate creationDate;     // Date of creation of the ScalataGiocata
    private String creationTime;        // Time of creation of the ScalataGiocata
    private String startingTime;        // Time of starting of the ScalataGiocata
    private String endingTime;          // Time of ending of the ScalataGiocata
    private String updateTime;          // Time of update of the ScalataGiocata

    private float finalScore;           // Score of the ScalataGiocata
    private boolean isFinished;         // Boolean to check if the ScalataGiocata is finished

    // Void constructor
    public ScalataGiocata() {

        System.out.println("ScalataGiocata created.");

    }

    // Constructor
    public ScalataGiocata(int playerID, int ID, String scalataName, List<Game> games, LocalDate creationDate, String creationTime, String startingTime, String endingTime, String updateTime, float finalScore, boolean isFinished) {

        this.playerID = playerID;
        this.ID = ID;
        this.scalataName = scalataName;
        this.games = games;
        this.creationDate = creationDate;
        this.creationTime = creationTime;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.updateTime = updateTime;
        this.finalScore = finalScore;
        this.isFinished = isFinished;
    }

    // Getters
    public int getPlayerID() {
        return playerID;
    }
    public int getID() {
        return ID;
    }
    public String getScalataName() {
        return scalataName;
    }
    public List<Game> getGames() {
        return games;
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }
    public String getCreationTime() {
        return creationTime;
    }
    public String getStartingTime() {
        return startingTime;
    }
    public String getEndingTime() {
        return endingTime;
    }
    public String getUpdateTime() {
        return updateTime;
    }
    public float getFinalScore() {
        return finalScore;
    }
    public boolean getIsFinished() {
        return isFinished;
    }

    // Setters
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public void setScalataName(String scalataName) {
        this.scalataName = scalataName;
    }
    public void setGames(List<Game> games) {
        this.games = games;
    }
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }
    public void setEndingTime(String endingTime) {
        this.endingTime = endingTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public void setFinalScore(float finalScore) {
        this.finalScore = finalScore;
    }
    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    //Overriding the toString() method
    @Override
    public String toString() {
        return "ScalataGiocata [" +
                "playerID=" + playerID + ", " +
                "ID=" + ID + ", " +
                "scalataName=" + scalataName + ", " +
                "games=" + games + ", " +
                "creationDate=" + creationDate + ", " +
                "creationTime=" + creationTime + ", " +
                "startingTime=" + startingTime + ", " +
                "endingTime=" + endingTime + ", " +
                "updateTime=" + updateTime + ", " +
                "finalScore=" + finalScore + ", " +
                "isFinished=" + isFinished +
                "]";
    }

}
