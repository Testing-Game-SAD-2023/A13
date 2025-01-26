package com.g2.Exercises;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.DBObject;


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
        Boolean completed,
        Boolean isValid
    ) {
        Query query = new Query();
        if(isValid!=null){
            AggregationResults<DBObject> cane = mongo.aggregate(
                Aggregation.newAggregation(
                    List.of(
                        Aggregation.match(
                            Criteria.where("startingTime").lt(Instant.now())
                        ),    
                        Aggregation.stage(
"{ $lookup: { from: \"goals\", let: { exeId: \"$_id\" }, pipeline: [{ $match: { $expr: { $eq: [\"$$exeId\", { $toObjectId: \"$assignmentId\" }] } } }], as: \"eGoals\" } }"
                        ),
                        Aggregation.stage("{$unwind:\"$eGoals\"}"),
                        Aggregation.stage("{ $group: { _id: \"cane\", key: { $push: \"$eGoals\" } } }")
    
                                        
                    )
                ),
                "exercises",
                DBObject.class
            );
            System.out.println(cane.getRawResults().toJson());
        }
    

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

    public void delete(Goal goal){
       
        mongo.remove(goal);
    
    }

    public void deleteManyByAssignmentId(String assignmentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("assignmentId").is(assignmentId));
        mongo.remove(query, Goal.class);
    }
}
