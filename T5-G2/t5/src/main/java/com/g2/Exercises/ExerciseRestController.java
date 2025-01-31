package com.g2.Exercises;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.g2.Goals.Goal;
import com.g2.Goals.GoalRepository;


@RestController
public class ExerciseRestController {
    @Autowired
    //private ExerciseRepository exerciseRepository;
    private GoalRepository goalRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;

    @PutMapping(value = "/goal", produces = MediaType.APPLICATION_JSON_VALUE)
    public void addGoal(@RequestBody String exercise){
        String message;
        try{
            goalRepository.insert(Goal.deserialize(exercise));
            return;
        }catch(JacksonException serializationException){
            serializationException.printStackTrace();
            message = serializationException.getMessage();
            
        }catch(IllegalArgumentException serializationException){
            message = serializationException.getMessage();
            serializationException.printStackTrace();
        }
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            message
        );
    }

    @GetMapping("/goal")
    public List<Goal> getGoals(
        @RequestParam(required = false) Integer playerId,
        @RequestParam(required = false) String assignmentId,
        @RequestParam(required = false) Boolean isCompleted,
        @RequestParam(required = false) Boolean isValid
    ){
        
        return goalRepository.findAll(playerId,assignmentId,isCompleted,isValid);
    }

    @GetMapping("/exercise")
    public List<Exercise> getExercise(
        @RequestParam(required = false) Integer teamId,
        @RequestParam(required = false) Boolean isValid
    ){
        return exerciseRepository.findAll(teamId,isValid);
    }

    @GetMapping("/exercise/{id}")
    public Exercise.WithGoals getExerciseWithGoals(
        @PathVariable String id
    ){
        Exercise exercise = exerciseRepository.findById(id);
        Exercise.WithGoals retval = new Exercise.WithGoals(exercise);
        retval.setGoals(goalRepository.findAll(null, retval.id,null,null));
        return retval;
    }

    @PutMapping("/exercise")
    public Exercise addExercise(@RequestBody Exercise exercise){
        if(exercise.getCreationTime()==null){
            exercise.setCreationTime(Instant.now());
        }
        if(exercise.getStartingTime()==null){
            exercise.setStartingTime(exercise.getCreationTime());
        }
        exercise = exerciseRepository.insert(exercise);
        try{
            ExerciseModelManager.generateGoalsForExercise(exercise, goalRepository);
        }catch(Exception ex){
            ex.printStackTrace();
            try{
                exerciseRepository.remove(exercise);
            }catch(Exception t){}

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
            );
        }
        return exercise;
        
    }

    @DeleteMapping("/exercise/{id}")
    public Exercise deleteExercise(
        @PathVariable String id
    ){
        Exercise exercise = exerciseRepository.findById(id);
        if(exercise==null){
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND
            );
        }
        try{
            ExerciseModelManager.removeGoalsForExercise(exercise, goalRepository);
            exerciseRepository.remove(exercise);
            return exercise;
        }catch(Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
            );
        }
    }

    @PostMapping(value = "/exercise/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Exercise updateExercise(@RequestBody String exerciseString, @PathVariable String id){
        Exercise exercise = exerciseRepository.findById(id);
        if(exercise==null){
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND
            );
        }

        
        try{
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            final JsonNode node = mapper.readTree(exerciseString);

            if(node.has("students")){
                JsonNode students = node.get("students");
                exercise.students = new ArrayList<Integer>();
                for(JsonNode student : students){
                    exercise.students.add(student.asInt());
                }
                ExerciseModelManager.updateGoalsForExercise(exercise, goalRepository);
            }
            if(node.has("description")){
                exercise.description = node.get("description").asText();
            }
            if(node.has("expiryTime")){
                exercise.expiryTime = Instant.parse(node.get("expiryTime").asText());
            }
        }catch(JacksonException e){
            e.printStackTrace();
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
            );
        }

        return exerciseRepository.update(exercise);
    }
    
    /*
     * aggiuntaMissione->T23
     * missioneConGoals(Id)
     * 
     */
    
}
