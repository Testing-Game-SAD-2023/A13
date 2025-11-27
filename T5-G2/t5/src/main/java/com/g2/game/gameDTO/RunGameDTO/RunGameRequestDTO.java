package com.g2.game.gameDTO.RunGameDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import testrobotchallenge.commons.models.opponent.GameMode;

@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "mode", include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RunPartitaSingolaRequestDTO.class, name = "PartitaSingola"), // Se "mode" è "PartitaSingola", usa questa classe
        @JsonSubTypes.Type(value = RunGameRequestDTO.class, name = "Allenamento"),
        @JsonSubTypes.Type(value = RunScalataDTO.class, name = "ScalataGame"), // Se "mode" è "ScalataGame", usa questa classe
        @JsonSubTypes.Type(value = RunGameRequestDTO.class, name = "Sfida")
})
public class RunGameRequestDTO {

    @NotNull
    @JsonProperty(value = "classUTCode", required = true, defaultValue = "")
    String classUTCode;

    @NotNull(message = "classUTCode is required")
    @JsonProperty(value = "testingClassCode", required = false, defaultValue = "")
    String testClassCode;

    @NotNull(message = "playerId is required")
    @JsonProperty(value = "playerId", required = true)
    Long playerId;

    @NotNull(message = "mode is required")
    @JsonProperty(value = "mode", required = true)
    GameMode gameMode;
}
