package com.g2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameConfigData {
    @JsonProperty("max_level")
    private int maxLevel;
    @JsonProperty("exp_per_level")
    private int expPerLevel;
    @JsonProperty("starting_level")
    private int startingLevel;
}
