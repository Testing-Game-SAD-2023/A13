package com.g2.Model;

public class PlayerStats {
    private String email;
    private Long playerId;
    private int stats;

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public Long getPlayerId(){
        return playerId;
    }

    public void setPlayerId(Long playerId){
        this.playerId=playerId;
    }

    public int getStats(){
        return stats;
    }

    public void setStats(int stats){
        this.stats = stats;
    }
}
