package com.groom.manvsclass.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Level {

    @DBRef
    private Opponent opponent;
    private int tempoMax;

}
