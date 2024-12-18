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


@Component
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

    

    public void addTeam(Team team) {
        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("Team");
    
        // Converti la lista di membri in un formato compatibile con MongoDB
        List<String> members = team.getMember(); // Assumi che `getMember` restituisca una lista di stringhe
    
        Document teamDoc = new Document()
            .append("teamName", team.getTeamName()) // Usato teamName come identificatore
            .append("description", team.getDescription())
            .append("leaderId", team.getLeaderId())
            .append("member", members) // Inserisci direttamente l'array dei membri
            .append("creationDate", team.getCreationDate());
    
        collection.insertOne(teamDoc);
    }
    

    public void deleteTeam(String teamName) {
        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("Team");

        Bson filter = Filters.eq("teamName", teamName); // Filtra per teamName
        collection.deleteOne(filter);
    }
     //30/11/2024

      // Metodo per aggiungere un membro al team
    public void addMemberToTeam(String teamName, String memberId) {
        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("Team");

        // Filtra il documento del team
        Bson filter = Filters.eq("teamName", teamName);

        // Aggiungi il nuovo membro alla lista dei membri
        Bson update = new Document("$addToSet", new Document("member", memberId));

        // Aggiorna il documento del team
        collection.updateOne(filter, update);
    }

    // Metodo per rimuovere un membro dal team
    public void removeMemberFromTeam(String teamName, String memberId) {
        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("Team");

        // Filtra il documento del team
        Bson filter = Filters.eq("teamName", teamName);

        // Rimuovi il membro dalla lista dei membri
        Bson update = new Document("$pull", new Document("member", memberId));

        // Aggiorna il documento del team
        collection.updateOne(filter, update);
    }

}
    
    
    


