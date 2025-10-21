package com.groom.manvsclass.service;

//E' giusto una classe per inviare correttamente la richiesta di modifica del nome
//Da eliminare -> inutile
public class TeamModificationRequest {
    private String idTeam;
    private String newName;

    // Getters and setters
    public String getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(String idTeam) {
        this.idTeam = idTeam;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
