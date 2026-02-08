package com.example.db_setup.model.dto.gamification;

import com.example.db_setup.model.GameProgress;
import com.example.db_setup.model.Opponent;
import com.example.db_setup.model.PlayerProgress;
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
}
