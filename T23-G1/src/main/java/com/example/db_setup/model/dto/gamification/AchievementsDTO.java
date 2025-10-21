package com.example.db_setup.model.dto.gamification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class AchievementsDTO {
    Set<String> unlockedAchievements;
}
