package com.groom.manvsclass.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "Team")
public class Team {
    
    //private String id;
    @Id
    private String teamName;
    private String description;
    private String leaderId; // ID dell'amministratore o leader del team
    private List<String> member; // Lista dei membri del team
    private String creationDate; // Data di creazione del team
    
    public Team(String teamName, String description, String leaderId, List<String> member, String creationDate) {
        this.teamName = teamName;
        this.description = description;
        this.leaderId = leaderId;
        this.member = member;
        this.creationDate = creationDate;
    }

    /*  public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }  */

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public List<String> getMember() {
        return member;
    }

    public void setMember(List<String> member) {
        this.member = member;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
    
    
    @Override
    public String toString() {
        return "Team{" +
                " teamName='" + teamName + '\'' +
                ", description='" + description + '\'' +
                ", leaderId='" + leaderId + '\'' +
                ", member=" + member +
                ", creationDate='" + creationDate + '\'' +
                '}';
    }
}

