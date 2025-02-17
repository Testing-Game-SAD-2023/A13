package com.g2.Game.GameDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StartGameResponseDTO {
     @JsonProperty("gameId")
    private int gameId;
    @JsonProperty("status")
    private String status;

    public StartGameResponseDTO(){
        //costruttore vuot per jackson
    }

    public StartGameResponseDTO(int gameId, String status){
        this.gameId = gameId;
        this.status = status;
    }

    // Getters e Setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}