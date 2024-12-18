package com.g2.Model;

public class PlayerStats {
    private String email;
    private Long userId;
    private int rank;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "PlayerStats{" +
                "email='" + email + '\'' +
                ", userId=" + userId +
                ", statistic=" + statistic +
                '}';
    }
 
}
