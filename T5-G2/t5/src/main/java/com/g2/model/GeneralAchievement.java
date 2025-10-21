package com.g2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GeneralAchievement {
    @JsonProperty("player_id")
    private String playerId;
    @JsonProperty("global_achievements")
    private String[] achievements;

    @Override
    public String toString() {
        return "GlobalAchievement{" +
                "playerId='" + playerId + '\'' +
                ", achievements=" + Arrays.toString(achievements) +
                '}';
    }
}
