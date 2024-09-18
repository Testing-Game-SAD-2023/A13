package com.g2.Model;

public class CategoryProgress {
    public int PlayerID;
    public int Category;
    public float Progress;

    public CategoryProgress(int playerID, int category, float progress) {
        PlayerID = playerID;
        Category = category;
        Progress = progress;
    }

    public CategoryProgress() {

    }

    public int getPlayerID() {
        return PlayerID;
    }

    public void setPlayerID(int playerID) {
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
        return "CategoryProgress{" +
                "PlayerID=" + PlayerID +
                ", Category=" + Category +
                ", Progress=" + Progress +
                '}';
    }
}
