package com.g2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class OpponentSummary {
    @JsonProperty("classUT")
    private String classUT;
    @JsonProperty("opponentType")
    private String type;
    @JsonProperty("opponentDifficulty")
    private OpponentDifficulty difficulty;
}
