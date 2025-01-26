package com.g2.Exercises;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class ExerciseRepository {
    @Autowired
    private MongoTemplate mongo;

    public List<Exercise> findAll(Integer teamId){
        Query query = new Query();
        if(teamId != null){
            query.addCriteria(Criteria.where("teamId").is(teamId));
        }
        return mongo.find(
            query,
            Exercise.class
        );

    }

    public Exercise findById(String exerciseId){
        return mongo.findById(
            exerciseId,
            Exercise.class
        );
    }

    public Exercise insert(Exercise exercise){
        return mongo.insert(exercise);
    }

    public void remove(Exercise exercise) throws Exception {
        if(mongo.remove(exercise).getDeletedCount() != 1){
            throw new Exception("Could not delete");
        }
    }

    public Exercise updateExercise(Exercise exercise) {
        return mongo.save(exercise);
    }
}
