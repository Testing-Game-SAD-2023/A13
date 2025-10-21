package com.g2.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class PlayerProgressDTO {
    private int experiencePoints;
    private Set<String> globalAchievements;
    private List<GameProgressDTO> gameProgressesDTO;
}
