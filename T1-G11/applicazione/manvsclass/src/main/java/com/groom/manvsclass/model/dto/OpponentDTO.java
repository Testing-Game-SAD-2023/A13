package com.groom.manvsclass.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OpponentDTO {
    private String classUT;
    private String type;
    private OpponentDifficulty difficulty;
    private GameMode gameMode;
}
