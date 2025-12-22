package com.example.db_setup.model.dto.gamification;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class PlayerProgressDTO {
    private int experiencePoints;
    private Set<String> globalAchievements;
    private List<GameProgressDTO> gameProgressesDTO = new ArrayList<>();

    /*
    public PlayerProgressDTO(int experiencePoints, Set<String> globalAchievements, List<GameProgress> gameProgresses) {
        this.experiencePoints = experiencePoints;
        this.globalAchievements = globalAchievements;
        for (GameProgress gameProgress : gameProgresses) {
            Opponent opponent = gameProgress.getOpponent();
            this.gameProgressesDTO.add(new GameProgressDTO(opponent.getClassUT(), opponent.getGameMode(), opponent.getType(),
                    opponent.getDifficulty(), gameProgress.isWinner(), gameProgress.getAchievements()));
        }
    }

     */
}
