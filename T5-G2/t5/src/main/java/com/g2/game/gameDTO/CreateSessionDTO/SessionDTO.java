package com.g2.game.gameDTO.CreateSessionDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "mode", include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PartitaSingolaSessionDTO.class, name = "PartitaSingola"), // Se "mode" è "PartitaSingola", usa questa classe
        @JsonSubTypes.Type(value = SessionDTO.class, name = "Allenamento"),
        @JsonSubTypes.Type(value = SessionDTO.class, name = "ScalataGame"),
        @JsonSubTypes.Type(value = SessionDTO.class, name = "Sfida")
})
public class SessionDTO {

    @JsonProperty("mode")
    private GameMode mode;

    @JsonProperty("playerId")
    private Long playerId;

    @JsonProperty("underTestClassName")
    private String classUTName;

    @JsonProperty("classUTCode")
    private String classUTCode;

    @JsonProperty("type_robot")
    private String typeRobot;

    @JsonProperty("difficulty")
    private OpponentDifficulty difficulty;

    @JsonProperty("testingClassCode")
    private String testingClassCode;
}
