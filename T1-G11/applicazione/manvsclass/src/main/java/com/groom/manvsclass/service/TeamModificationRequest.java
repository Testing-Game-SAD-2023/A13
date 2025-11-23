package com.groom.manvsclass.service;

//E' giusto una classe per inviare correttamente la richiesta di modifica del nome
//Da eliminare -> inutile

public class TeamModificationRequest {

    private String teamOldName;
    private String teamNewName;

    public TeamModificationRequest() {
        teamOldName = "";
        teamNewName = "";
    }

    public TeamModificationRequest(String teamOldName, String teamNewName) {
        this.teamOldName = teamOldName;
        this.teamNewName = teamNewName;
    }

    public String getTeamOldName() {
        return teamOldName;
    }

    public void setTeamOldName(String teamOldName) {
        this.teamOldName = teamOldName;
    }

    public String getTeamNewName() {
        return teamNewName;
    }

    public void setTeamNewName(String teamNewName) {
        this.teamNewName = teamNewName;
    }
}
