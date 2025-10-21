package com.g2.game.gameDTO.CreateSessionDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PartitaSingolaSessionDTO extends SessionDTO {
    @JsonProperty("remainingTime")
    private int remainingTime;
}
