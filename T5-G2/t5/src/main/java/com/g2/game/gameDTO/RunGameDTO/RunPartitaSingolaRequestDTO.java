package com.g2.game.gameDTO.RunGameDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RunPartitaSingolaRequestDTO extends RunGameRequestDTO {
    @NotNull
    @JsonProperty(value = "remainingTime", required = true)
    private int remainingTime;
}
