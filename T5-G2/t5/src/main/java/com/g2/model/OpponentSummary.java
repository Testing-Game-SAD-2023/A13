package com.g2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OpponentSummary {
    @JsonProperty("classUT")
    private String classUT;
    @JsonProperty("opponentType")
    private String type;
    @JsonProperty("opponentDifficulty")
    private OpponentDifficulty difficulty;
}
