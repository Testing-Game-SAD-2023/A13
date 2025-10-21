package com.g2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GameProgressDTO {
    private GameMode gameMode;
    private String classUT;
    private String type;
    private OpponentDifficulty difficulty;
    private boolean isWon;
    private Set<String> achievements;
}

