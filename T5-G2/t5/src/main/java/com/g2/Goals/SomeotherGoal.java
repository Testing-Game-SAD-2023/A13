package com.g2.Goals;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.g2.Game.GameLogic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Document(collection = "goals")
@Data
@Jacksonized

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SomeotherGoal extends Goal {

    Integer expectedScore;
    @NonNull
    Integer exampleInteger;

    public boolean match(Map<String, String> userData, GameLogic gameLogic, int robotScore, int playerScore){
        return false;
    }
}