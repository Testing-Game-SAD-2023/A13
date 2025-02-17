package com.g2.Game.GameDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StartGameRequestDTO {
    @JsonProperty("playerId")
    private String playerId;
    @JsonProperty("typeRobot")
    private String typeRobot;
    @JsonProperty("difficulty")
    private String difficulty;
    @JsonProperty("mode")
    private String mode;
    @JsonProperty("underTestClassName")
    private String underTestClassName;

    // Getters e Setters

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getTypeRobot() {
        return typeRobot;
    }

    public void setTypeRobot(String typeRobot) {
        this.typeRobot = typeRobot;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUnderTestClassName() {
        return underTestClassName;
    }

    public void setUnderTestClassName(String underTestClassName) {
        this.underTestClassName = underTestClassName;
    }
}
