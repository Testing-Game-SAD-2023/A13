package com.g2.Exercises;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JacksonException;

import jakarta.websocket.server.PathParam;


@RestController
public class GoalsRestController {
    @Autowired
    //private ExerciseRepository exerciseRepository;
    private GoalRepository goalRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;

    @PutMapping(value = "/goals", produces = MediaType.APPLICATION_JSON_VALUE)
    public void addExercise(@RequestBody String exercise){
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

    @GetMapping("/goals")
    public List<Goal> getExercise(
        @RequestParam(required = false) String playerId,
        @RequestParam(required = false) String assignmentId,
        @RequestParam(required = false) Boolean completed
    ){
        
        return goalRepository.findAll(playerId,assignmentId,completed);
    }

    @GetMapping("/exercise")
    public List<Exercise> getExercise(
        @RequestParam(required = false) Integer teamId
    ){
        return exerciseRepository.findAll(teamId);
    }

    @GetMapping("/exercise/{id}")
    public Exercise.WithGoals getExerciseWithGoals(
        @PathVariable String id
    ){
        Exercise exercise = exerciseRepository.find(id);
        Exercise.WithGoals retval = new Exercise.WithGoals(exercise);
        retval.setGoals(goalRepository.findAll(null, retval.id,null));
        return retval;
    }

    @PutMapping("/exercise")
    public void addExercise(@RequestBody Exercise exercise){
        String id = exerciseRepository.insert(exercise);
        Goal goal;
        //TODO move logic
        for(String generatingString : exercise.getGoalTypes()){
            for(String student : exercise.getStudents()){
                try{
                    goal = Goal.deserialize(generatingString);
                }catch(Exception ex){
                    ex.printStackTrace();
                    throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage()
                    );
                }
                goal.assignmentId=id;
                goal.playerId=student;
                goal.completition=0;
                goalRepository.insert(goal);
                
            }
        }
    }


    
    /*
     * aggiuntaMissione->T23
     * eliminazioneMissione
     * opzionale: modificaDescrizione
     * missioneConGoals(Id)
     * 
     */
    
}
