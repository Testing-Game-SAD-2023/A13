package com.g2.Exercises.Goals;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.g2.Exercises.Goal;
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
public class ClassScoreGoal extends Goal {

    Integer expectedScore;
    @NonNull
    String className;

    public void match(Map<String, String> userData, GameLogic gameLogic){
        
    }
}
