package com.g2.Goals;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.BSONObject;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.Exercises.Exercise;
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
    
    
        if(isValid!=null){
            //questi contano nel filtraggio degli esercizi
            Criteria criteria;
            if(isValid){
                criteria = Criteria.where("startingTime").lt(Instant.now()).and("expiryTime").gt(Instant.now());
            }else{
                criteria = new Criteria().orOperator(
                    Criteria.where("expiryTime").lt(Instant.now()),
                    Criteria.where("startingTime").gt(Instant.now())
                );
            }
            if(assignmentId != null){
                criteria.and("_id").is(assignmentId);
            }

            //questi contano nel join
            String pipelineStageString="{ $lookup: { from: \"goals\", let: { exeId: \"$_id\" }, pipeline: [{ $match: { $expr: { $and:[{$eq: [\"$$exeId\", { $toObjectId: \"$assignmentId\" }] }";
            if(playerId!=null){
                pipelineStageString+=",{$eq:[\"$playerId\",\""+playerId+"\"]}";
            }
            if(completed != null){
                if(completed){
                    pipelineStageString+=",{$eq:[\"$completition\",100]} ";
                }else{
                    pipelineStageString+=",{$ne:[\"$completition\",100]} ";
                }
            }
            pipelineStageString+="] } }}], as: \"eGoals\" }}";
            
            System.out.println(pipelineStageString);


            AggregationResults<DBObject> result = mongo.aggregate(
                Aggregation.newAggregation(
                    List.of(
                        Aggregation.match(criteria),
                        Aggregation.stage(
                            pipelineStageString
                        ),
                        Aggregation.stage("{$unwind:\"$eGoals\"}"),
                        Aggregation.stage("{ $group: { _id: \"g\", key: { $push: \"$eGoals\" } } }")
    
                                        
                    )
                ),
                "exercises",
                DBObject.class
            );
            List<Goal> goals = new ArrayList<Goal>();
            
            
            final ObjectMapper mapper = new ObjectMapper();
            try{
                result.getRawResults().getList("results", Document.class).get(0).getList("key",Document.class)
                    .forEach(
                        (document)->{

                            String className = document.get("_class", String.class);
                            try{
                                goals.add((Goal)mapper.readValue(document.toJson(),Class.forName(className)));
                            }catch(Exception e){}
                        }
                    );
            }catch(IndexOutOfBoundsException emptyResult){}
            return goals;
            
        }else{

            
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
                    query.addCriteria(Criteria.where("completition").ne(100));
                }
            }
            return mongo.find(
                query,
                Goal.class
            );
            
        }
    }
        
    public void delete(Goal goal){
       
        mongo.remove(goal);
    
    }

    public void deleteManyByAssignmentId(String assignmentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("assignmentId").is(assignmentId));
        mongo.remove(query, Goal.class);
    }

    public Goal update(Goal goal) {
        
        return mongo.save(goal);
    }
}

//Bisognava spezzettare questo stage di aggregation:

//"{ $lookup: { from: \"goals\", let: { exeId: \"$_id\" }, pipeline: [{ $match: { $expr: { $and:[{$eq: [\"$$exeId\", { $toObjectId: \"$assignmentId\" }] } ] } }}], as: \"eGoals\" } }"
//"{ $lookup: { from: \"goals\", let: { exeId: \"$_id\" }, pipeline: [{ $match: { $expr: { $and:[{$eq: [\"$playerId\",\"anna.tatangelo@studenti.unina.it\"]},{$eq: [\"$$exeId\", { $toObjectId: \"$assignmentId\" }] } ] } }}], as: \"eGoals\" } }"
//"{ $lookup: { from: \"goals\", let: { exeId: \"$_id\" }, pipeline: [{ $match: { $expr: { $and:[{$eq: [\"$$exeId\", { $toObjectId: \"$assignmentId\" }] },{$eq:[\"$playerId\",\"anna.tatangelo@studenti.unina.it\"]} ] } }}], as: \"eGoals\" } }"
//"{ $lookup: { from: \"goals\", let: { exeId: \"$_id\" }, pipeline: [{ $match: { $expr: { $and:[{$eq: [\"$$exeId\", { $toObjectId: \"$assignmentId\" }] },{$eq:[\"$playerId\",\"anna.tatangelo@studenti.unina.it\"]},{$ne:[\"$completition\",100]} ] } }}], as: \"eGoals\" }}"
//                                                                                                                                                       |                                                           |                             |


//La query originale di mongodb:
//db.exercises.aggregate([
//        { $match: { $and: [{ startingTime: { $lt: ISODate() } }] } },
//        { $lookup: { from: "goals", let: { exeId: "$_id" }, pipeline: [{ $match: { $expr: { $and:[{$eq: ["$$exeId", { $toObjectId: "$assignmentId" }] },{$eq:["$playerId","anna.tatangelo@studenti.unina.it"]},{$eq:["$completition",100]} ] } }}], as: "eGoals" } },
//        {$unwind:"$eGoals"},
//        { $group: { _id: "g", key: { $push: "$eGoals" } } }
//    ]);