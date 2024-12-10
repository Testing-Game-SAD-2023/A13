package com.g2.Model;

public class PlayerDTO { 
    private long ID; 
    private String name; 
    private String surname; 
    private int points; 
    private int gamesWon; 
    private int rankByGames; 
    private int rankByPoints; 
 
     
 
    public int getRankByGames() { 
        return rankByGames; 
    } 
    public void setRankByGames(int rankByGames) { 
        this.rankByGames = rankByGames; 
    } 
    public int getRankByPoints() { 
        return rankByPoints; 
    } 
    public void setRankByPoints(int rankByPoints) { 
        this.rankByPoints = rankByPoints; 
    } 
    public long getID() { 
        return ID; 
    } 
    public void setID(long iD) { 
        ID = iD; 
    } 
 
    public void setName(String name) { 
        this.name = name; 
    } 
         
    public int getGamesWon() { 
        return gamesWon; 
    } 
    public void setGamesWon(int gamesWon) { 
        this.gamesWon = gamesWon; 
    } 
    public void setSurname(String surname) { 
        this.surname = surname; 
    } 
    public void setPoints(int points) { 
        this.points = points; 
    } 
    public String getName() { 
        return name; 
    } 
    public String getSurname() { 
        return surname; 
    } 
    public int getPoints() { 
        return points; 
    } 
 
     
 
    public PlayerDTO(long iD, int rankByGames, int rankByPoints) { 
        ID = iD; 
        this.rankByGames = rankByGames; 
        this.rankByPoints = rankByPoints; 
    } 
    public PlayerDTO(long iD, String name, String surname, int points, int gamesWon, int rankByGames, 
            int rankByPoints) { 
        ID = iD; 
        this.name = name; 
        this.surname = surname; 
        this.points = points; 
        this.gamesWon = gamesWon; 
        this.rankByGames = rankByGames; 
        this.rankByPoints = rankByPoints; 
    } 
    public PlayerDTO(long ID,String name, String surname, int points, int gamesWon) { 
        this.ID = ID; 
        this.name = name; 
        this.surname = surname; 
        this.points = points; 
        this.gamesWon = gamesWon; 
    } 
    public PlayerDTO(){ 
 
    } 
 
}
