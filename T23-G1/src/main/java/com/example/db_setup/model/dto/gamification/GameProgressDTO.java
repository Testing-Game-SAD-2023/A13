package com.example.db_setup.model.dto.gamification;

import lombok.*;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GameProgressDTO {
    private String classUT;
    private GameMode gameMode;
    private String type;
    private OpponentDifficulty difficulty;
    private boolean isWon;
    private Set<String> achievements;
}
