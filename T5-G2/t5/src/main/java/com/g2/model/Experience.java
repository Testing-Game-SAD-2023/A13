package com.g2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Experience {
    @JsonProperty("player_id")
    private int playerId;
    @JsonProperty("experience_points")
    private int experiencePoints;
}
