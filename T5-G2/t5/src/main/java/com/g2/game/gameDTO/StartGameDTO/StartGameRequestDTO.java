package com.g2.game.gameDTO.StartGameDTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "mode", include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StartPartitaSingolaRequestDTO.class, name = "PartitaSingola"),// Se "mode" è "PartitaSingola", usa questa classe
        @JsonSubTypes.Type(value = StartGameRequestDTO.class, name = "Allenamento"),
        @JsonSubTypes.Type(value = StartGameRequestDTO.class, name = "ScalataGame"),
        @JsonSubTypes.Type(value = StartGameRequestDTO.class, name = "Sfida")
})
@Getter
@Setter
@NoArgsConstructor
@ToString
public class StartGameRequestDTO {

    @NotNull
    @JsonProperty("playerId")
    private Long playerId;

    @JsonProperty("typeRobot")
    @JsonAlias({"type_robot", "typeRobot"})
    private String typeRobot;

    @NotNull
    @JsonProperty("difficulty")
    private OpponentDifficulty difficulty;

    @NotNull
    @JsonProperty("mode")
    private GameMode gameMode;

    @JsonProperty("underTestClassName")
    @NotBlank(message = "underTestClassName is required")
    private String classUTName;

}
