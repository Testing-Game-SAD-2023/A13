package com.groom.manvsclass.model.repository;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import com.groom.manvsclass.model.Team;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;



public class TeamSearchImpl {

    @Autowired
    MongoClient client;
 
    @Autowired
    MongoConverter converter;

    // INIZIO MODIFICA 28/11/2024: Metodi per gestire Team B14

    public Team findTeamByName(String teamName) {
        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("Team");

        Bson filter = Filters.eq("teamName", teamName); // teamName è il campo identificativo
        Document result = collection.find(filter).first();

        if (result == null) {
            return null;
        }

        return converter.read(Team.class, result);
    }

    public List<Team> findTeamsByLeader(String leaderId) {
        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("Team");

        Bson filter = Filters.eq("leaderId", leaderId);
        List<Team> teams = new ArrayList<>();

        collection.find(filter).forEach(doc -> teams.add(converter.read(Team.class, doc)));
        return teams;
    }

    public void addTeam(Team team) {
        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("Team");

        Document teamDoc = new Document()
            .append("teamName", team.getTeamName()) // Usato teamName come identificatore
            .append("description", team.getDescription())
            .append("leaderId", team.getLeaderId())
            .append("member", team.getMember())
            .append("creationDate", team.getCreationDate());

        collection.insertOne(teamDoc);
    }

    public void updateTeam(Team team) {
        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("Team");

        Bson filter = Filters.eq("teamName", team.getTeamName()); // Filtra per teamName
        Document updatedDoc = new Document()
            .append("description", team.getDescription())
            .append("leaderId", team.getLeaderId());
            //.append("member", team.getMember());
            //.append("creationDate", team.getCreationDate());

        collection.updateOne(filter, new Document("$set", updatedDoc));
    }

    public void deleteTeam(String teamName) {
        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("Team");

        Bson filter = Filters.eq("teamName", teamName); // Filtra per teamName
        collection.deleteOne(filter);
    }
     //Fine Modifica

}
    
    
    


