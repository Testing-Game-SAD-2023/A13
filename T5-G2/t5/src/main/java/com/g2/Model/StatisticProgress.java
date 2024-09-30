package com.g2.Model;

public class StatisticProgress {
    private Integer PlayerID;
    private int Statistic;
    private float Progress;

    public StatisticProgress(Integer playerID, int statistic, float progress) {
        PlayerID = playerID;
        Statistic = statistic;
        Progress = progress;
    }

    public StatisticProgress() {

    }

    public Integer getPlayerID() {
        return PlayerID;
    }

    public void setPlayerID(Integer playerID) {
        PlayerID = playerID;
    }

    public int getStatistic() {
        return Statistic;
    }

    public void setStatistic(int category) {
        Statistic = category;
    }

    public float getProgress() {
        return Progress;
    }

    public void setProgress(float progress) {
        Progress = progress;
    }

    @Override
    public String toString() {
        return "StatisticProgress{" +
                "PlayerID=" + PlayerID +
                ", Statistic=" + Statistic +
                ", Progress=" + Progress +
                '}';
    }
}
