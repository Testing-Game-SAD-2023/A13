package com.g2.Exercises;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;


public  class GoalRepository{
    @Autowired
    private MongoTemplate mongo;

    public void insert(Goal goal){
        try{
            mongo.insert(goal, "goals");
        }catch(Throwable t){
            t.printStackTrace();
        }
    }

    public List<Goal> findAllByFilter(Map<String,String> filter){

        return mongo.findAll(
          Goal.class  
        );
    }

    public List<Goal> findAll(
        String playerId,
        String assignmentId,
        Boolean completed
    ) {
        Query query = new Query();
        if(playerId != null){
            query.addCriteria(Criteria.where("playerId").is(playerId));
        }
        if(assignmentId != null){
            query.addCriteria(Criteria.where("assignmentId").is(assignmentId));
        }
        if(completed != null){
            if(completed){
                query.addCriteria(Criteria.where("completition").is(100));
            }else{
                query.addCriteria(Criteria.where("completition").is(100).not());
            }
        }
        return mongo.find(
            query,
            Goal.class
        );
    }
}
