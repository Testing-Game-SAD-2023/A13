package com.g2.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "LeaderboardStats")
public class Player {
    @Id
    private String mail;
    private int matches;
    private int wins;
    private int totalScore;

    public Player(String mail, int matches, int wins, int totalScore) {
        this.mail = mail;
        this.matches = matches;
        this.wins = wins;
        this.totalScore = totalScore;
    }

    // Getter e setter
    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getMatches() {
        return this.matches;
    }

    public void setMatches(int matches) { 
        this.matches = matches;
    }

    public int getWins() { 
        return this.wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getTotalScore() {
        return this.totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public String toString() {
        return "Player{" +
                " mail='" + mail + 
                ", matches=" + matches +
                ", wins=" + wins +
                ", totalscore=" + totalScore +
                '}';
    }
}
