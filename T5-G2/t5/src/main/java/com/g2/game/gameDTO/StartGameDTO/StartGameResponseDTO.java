package com.g2.game.gameDTO.StartGameDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StartGameResponseDTO {
    @JsonProperty("gameId")
    private long gameId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("classUTName")
    private String classUTName;

    public StartGameResponseDTO() {
        //costruttore vuoto per jackson
    }

    public StartGameResponseDTO(long gameId, String status, String classUTName) {
        this.gameId = gameId;
        this.status = status;
        this.classUTName = classUTName;
    }

    // Getters e Setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }
    public String getClassUTName() {
        return classUTName;
    }
    public void setClassUTName(String classUTName) {
        this.classUTName = classUTName;
    }
}