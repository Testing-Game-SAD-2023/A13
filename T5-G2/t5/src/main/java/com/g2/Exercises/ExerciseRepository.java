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

    public Exercise find(String exerciseId){
        return mongo.findById(
            exerciseId,
            Exercise.class
        );
    }

    public String insert(Exercise exercise){
        return mongo.insert(exercise).id;
    }
}
