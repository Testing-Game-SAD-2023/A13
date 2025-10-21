package com.g2.model.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateGameProgressDTO {
    private boolean isWon;
    private Set<String> achievements;
}

