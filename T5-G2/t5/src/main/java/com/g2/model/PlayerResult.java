package com.g2.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlayerResult {
    private boolean isWinner;
    private int score;
}
