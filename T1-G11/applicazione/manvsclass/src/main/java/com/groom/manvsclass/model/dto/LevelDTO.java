package com.groom.manvsclass.model.dto;

import com.groom.manvsclass.model.Opponent;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LevelDTO {

    private Opponent opponent;
    private int tempoMax;
}
