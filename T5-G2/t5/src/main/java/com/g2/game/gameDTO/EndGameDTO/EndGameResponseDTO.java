package com.g2.game.gameDTO.EndGameDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.g2.game.gameDTO.RunGameDTO.RunGameResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class EndGameResponseDTO {

    @JsonProperty("achievementsUnlocked")
    Set<String> achievementsUnlocked;
    @JsonProperty("runGameResponse")
    RunGameResponseDTO runGameResponse;
    @JsonProperty("robotScore")
    private int robotScore;
    @JsonProperty("userScore")
    private int userScore;
    @JsonProperty("isWinner")
    private Boolean isWinner;
    @JsonProperty("expGained")
    private int expGained;

    public EndGameResponseDTO(int robotScore, int userScore, Boolean isWinner, int expGained,
                              RunGameResponseDTO runGameResponse) {
        this.robotScore = robotScore;
        this.userScore = userScore;
        this.isWinner = isWinner;
        this.expGained = expGained;
        this.achievementsUnlocked = new HashSet<>();
        this.runGameResponse = runGameResponse;
    }

    public EndGameResponseDTO(int robotScore, int userScore, Boolean isWinner, int expGained, Set<String> achievementsUnlocked,
                              RunGameResponseDTO runGameResponse) {
        this.robotScore = robotScore;
        this.userScore = userScore;
        this.isWinner = isWinner;
        this.expGained = expGained;
        this.achievementsUnlocked = achievementsUnlocked;
        this.runGameResponse = runGameResponse;
    }


    @Override
    public String toString() {
        return "EndGameResponseDTO{" +
                "robotScore=" + robotScore +
                ", userScore=" + userScore +
                ", isWinner=" + isWinner +
                ", expGained=" + expGained +
                ", achievementsUnlocked=" + achievementsUnlocked +
                '}';
    }
}
