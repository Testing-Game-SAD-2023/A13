package com.g2.Model;

public class StatisticProgress {
    private Integer PlayerID;
    private int Category;
    private float Progress;

    public StatisticProgress(Integer playerID, int category, float progress) {
        PlayerID = playerID;
        Category = category;
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

    public int getCategory() {
        return Category;
    }

    public void setCategory(int category) {
        Category = category;
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
                ", Category=" + Category +
                ", Progress=" + Progress +
                '}';
    }
}
