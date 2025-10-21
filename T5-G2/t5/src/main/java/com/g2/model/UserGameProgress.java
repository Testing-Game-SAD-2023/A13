package com.g2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@NoArgsConstructor
@Getter
@Setter
public class UserGameProgress {
    @JsonProperty("user_game_progress_id")
    private long userGameProgressId;
    @JsonProperty("player_id")
    private long playerId;
    @JsonProperty("game_mode")
    private String gameMode;
    @JsonProperty("class_ut")
    private String classUT;
    @JsonProperty("robot_type")
    private String robotType;
    @JsonProperty("difficulty")
    private String difficulty;
    @JsonProperty("has_won")
    private boolean won;
    @JsonProperty("achievements")
    private String[] achievements;

    @Override
    public String toString() {
        return "UserGameProgress{" +
                "userGameProgressId=" + userGameProgressId +
                ", playerId=" + playerId +
                ", gameMode='" + gameMode + '\'' +
                ", classUT='" + classUT + '\'' +
                ", robotType='" + robotType + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", won=" + won +
                ", achievements=" + Arrays.toString(achievements) +
                '}';
    }
}
