package com.g2.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class Player {
    @JsonProperty("id")
    private int ID;

    @JsonProperty("accountID")
    private int AccountID;

    @JsonProperty("createdAt")
    private LocalDate data_creazione;

    @JsonProperty("updatedAt")
    private LocalDate data_update;

    @JsonProperty("points")
    private int Points;

    @JsonProperty("gamesWon")
    private int GamesWon;

    public Player() {}

    public Player(int iD, int accountID, int points, int gamesWon) {
        this.ID = iD;
        this.AccountID = accountID;
        this.Points = points;
        this.GamesWon= gamesWon;
    }

    public Player(int iD, int accountID, LocalDate data_creazione, LocalDate data_update, int points,int gamesWon) {
        this.ID = iD;
        this.AccountID = accountID;
        this.data_creazione = data_creazione;
        this.data_update = data_update;
        this.Points= points;
        this.GamesWon= gamesWon;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public int getAccountID() {
        return AccountID;
    }

    public void setAccountID(int accountID) {
        AccountID = accountID;
    }

    public LocalDate getData_creazione() {
        return data_creazione;
    }

    public void setData_creazione(LocalDate data_creazione) {
        this.data_creazione = data_creazione;
    }

    public LocalDate getData_update() {
        return data_update;
    }

    public void setData_update(LocalDate data_update) {
        this.data_update = data_update;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }

    public int getGamesWon() {
        return GamesWon;
    }

    public void setGamesWon(int gamesWon) {
        GamesWon = gamesWon;
    }
    

}
