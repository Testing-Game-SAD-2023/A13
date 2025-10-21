package com.g2.game.gameDTO.StartGameDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StartPartitaSingolaRequestDTO extends StartGameRequestDTO {
    @JsonProperty("remainingTime")
    @NotNull(message = "remainingTime is required")
    private int remainingTime;

}
