package com.groom.manvsclass.model.repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;
import com.groom.manvsclass.model.VictoryConditionType;
import com.groom.manvsclass.model.Challenge;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@Component
public class ChallengeSearchImpl {

    @Autowired
    MongoClient client;

    @Autowired
    MongoConverter converter;

    public Challenge findChallengeByName(String challengeName) {
        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("Challenge");

        Bson filter = Filters.eq("challengeName", challengeName); // Nome della challenge come filtro
        Document result = collection.find(filter).first();

        if (result == null) {
            return null;
        }

        Challenge challenge = converter.read(Challenge.class, result);
        challenge.setVictoryConditionType(VictoryConditionType.valueOf(result.getString("victoryConditionType")));
        challenge.setVictoryCondition(result.getString("victoryCondition"));
        return challenge;
    }


    public void addChallenge(Challenge challenge) {
        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("Challenge");

        Document challengeDoc = new Document()
            .append("challengeName", challenge.getChallengeName())
            .append("description", challenge.getDescription())
            .append("teamId", challenge.getTeamId())
            .append("creatorId", challenge.getCreatorId())
            .append("startDate", challenge.getStartDate())
            .append("endDate", challenge.getEndDate())
            .append("status", challenge.getStatus())
            .append("victoryConditionType", challenge.getVictoryConditionType().toString())
            .append("victoryCondition", challenge.getVictoryCondition());

        collection.insertOne(challengeDoc);
    }
    

    public void deleteChallenge(String challengeName) {
        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("Challenge");

        Bson filter = Filters.eq("challengeName", challengeName); // Filtra per nome della challenge
        collection.deleteOne(filter);
    }
}
