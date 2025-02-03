package com.g2.Model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Team {
    
    private String idTeam;
    private String name;
    private Instant creationDate;
    //Questi sono gli ID
    private List<String> studenti;
    //Eventualmente posso avere una lista di oggetti User degli utenti 
    private List<User> UserList;
    private int numStudenti;
    private List<Assignment> assignments;

    // Costruttore
    public Team(String idTeam, String name, Instant creationDate, List<String> studenti, int numStudenti) {
        this.idTeam = idTeam;
        this.name = name;
        this.creationDate = creationDate;
        this.studenti = studenti;
        this.numStudenti = numStudenti;
        this.UserList = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    // Getters e Setters
    public String getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(String idTeam) {
        this.idTeam = idTeam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public List<String> getStudenti() {
        return studenti;
    }

    public void setStudenti(List<String> studenti) {
        this.studenti = studenti;
    }

    public int getNumStudenti() {
        return numStudenti;
    }

    public void setNumStudenti(int numStudenti) {
        this.numStudenti = numStudenti;
    }

    public List<User> getUserList() {
        return UserList;
    }

    public void setUserList(List<User> UserList) {
        this.UserList = UserList;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }
}
