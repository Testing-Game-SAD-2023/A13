package com.example.db_setup.model.dto.gamification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

@Getter
@Setter
@NoArgsConstructor
public class CreateGameProgressDTO {
    private String classUT;
    private GameMode gameMode;
    private String type;
    private OpponentDifficulty difficulty;
}
