package com.g2.Model;


public class PlayerStats {
    private String email;
    private Long userId;
    private int statistic;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getStatistic() {
        return statistic;
    }

    public void setStatistic(int statistic) {
        this.statistic = statistic;
    }
 
}
