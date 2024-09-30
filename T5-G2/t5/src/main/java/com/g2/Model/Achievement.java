package com.g2.Model;

public class Achievement {
    private String ID;

    private String name;
    private String description;
    private int statistic;
    private float progressRequired;

    public Achievement() {

    }

    public Achievement(String ID, String name, String description, int statistic, float progressRequired) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.statistic = statistic;
        this.progressRequired = progressRequired;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatistic() {
        return statistic;
    }

    public void setStatistic(int category) {
        this.statistic = category;
    }

    public float getProgressRequired() {
        return progressRequired;
    }

    public void setProgressRequired(float progressRequired) {
        this.progressRequired = progressRequired;
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", statistic=" + statistic +
                ", progressRequired=" + progressRequired +
                '}';
    }
}
