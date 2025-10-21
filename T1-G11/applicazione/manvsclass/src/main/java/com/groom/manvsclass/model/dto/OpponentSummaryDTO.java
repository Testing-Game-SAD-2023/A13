package com.groom.manvsclass.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

@Data
@AllArgsConstructor
public class OpponentSummaryDTO {
    private String classUT;
    private String opponentType;
    private OpponentDifficulty opponentDifficulty;
}
