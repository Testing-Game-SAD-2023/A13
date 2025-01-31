package com.g2.Exercises;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.g2.Goals.Goal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Document(collection = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Exercise {
    @Id
    String id;

    @Builder.Default()
    List<String> goalTypes = new ArrayList<String>(0);

    Instant creationTime;
    @NonNull
    Instant expiryTime;
    //TODO: check null
    Instant startingTime;

    Integer teamId;
    @NonNull
    String description;

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class WithGoals extends Exercise{
    
        List<Goal> goals;

        public WithGoals(Exercise exercise){
            id = exercise.id;
            goalTypes = exercise.goalTypes;
            creationTime = exercise.creationTime;
            startingTime = exercise.startingTime;
            expiryTime = exercise.expiryTime;
            teamId = exercise.teamId;
            description = exercise.description;
        }
    }


    @Transient    
    List<Integer> students;
    @JsonIgnore
    List<Integer> getStudents(){
        return students;
    }
    


}
