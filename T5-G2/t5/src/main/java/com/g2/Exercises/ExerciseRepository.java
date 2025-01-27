package com.g2.Exercises;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class ExerciseRepository {
    @Autowired
    private MongoTemplate mongo;

    public List<Exercise> findAll(Integer teamId,Boolean isValid){
        Query query = new Query();
        if(teamId != null){
            query.addCriteria(Criteria.where("teamId").is(teamId));
        }
        if(isValid!=null){
            //questi contano nel filtraggio degli esercizi
            if(isValid){
                query.addCriteria(Criteria.where("startingTime").lt(Instant.now()).and("expiryTime").gt(Instant.now()));
            }else{
                query.addCriteria(new Criteria().orOperator(
                    Criteria.where("expiryTime").lt(Instant.now()),
                    Criteria.where("startingTime").gt(Instant.now())
                ));
            }
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

    public Exercise update(Exercise exercise) {
        return mongo.save(exercise);
    }
}
