package com.groom.manvsclass.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "TeamManagement")
public class TeamAdmin {
    @Id
    private String id; // Identificativo univoco della relazione

    private String adminId; // Riferimento all'Admin (ID)
    private String teamId;  // Riferimento al Team (ID)
    private String teamName; //Nome della classe
    private String role; // Ruolo dell'Admin nel Team
    private boolean isActive; // Stato attuale della relazione

    // Costruttore
    public TeamAdmin(String adminId, String teamId, String teamName, String role, boolean isActive) {
        this.adminId = adminId;
        this.teamId = teamId;
        this.teamName = teamName;
        this.role = role;
        this.isActive = isActive;
    }

    // Getter e Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "TeamManagement{" +
                "id='" + id + '\'' +
                ", adminId='" + adminId + '\'' +
                ", teamId='" + teamId + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
