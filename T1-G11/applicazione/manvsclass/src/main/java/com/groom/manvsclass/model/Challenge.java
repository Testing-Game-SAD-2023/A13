package com.groom.manvsclass.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "Challenge")
public class Challenge {
    @Id
    private String id;
    private String challengeName; // Nome della challenge
    private String description; // Descrizione della challenge
    private String teamId; // ID del team assegnato alla challenge
    private String creatorId; // ID dell'utente o amministratore che ha creato la challenge
    private String startDate; // Data di inizio della challenge
    private String endDate; // Data di fine della challenge
    private String status; // Stato della challenge (es. "In Progress", "Completed", "Pending")
    private VictoryConditionType victoryConditionType; // Tipo di condizione di vittoria
    private String victoryCondition; // Dettaglio della condizione di vittoria (es. numero di partite)

    public Challenge(String challengeName, String description, String teamId, String creatorId,
                     String startDate, String endDate, String status, VictoryConditionType victoryConditionType, String victoryCondition) {
        this.challengeName = challengeName;
        this.description = description;
        this.teamId = teamId;
        this.creatorId = creatorId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.victoryConditionType = victoryConditionType;
        this.victoryCondition = victoryCondition;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VictoryConditionType getVictoryConditionType() {
        return victoryConditionType;
    }

    public void setVictoryConditionType(VictoryConditionType victoryConditionType) {
        this.victoryConditionType = victoryConditionType;
    }

    public String getVictoryCondition() {
        return victoryCondition;
    }

    public void setVictoryCondition(String victoryCondition) {
        this.victoryCondition = victoryCondition;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "challengeName='" + challengeName + '\'' +
                ", description='" + description + '\'' +
                ", teamId='" + teamId + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", status='" + status + '\'' +
                ", victoryConditionType='" + victoryConditionType + '\'' +
                ", victoryCondition='" + victoryCondition + '\'' +
                '}';
    }
}
