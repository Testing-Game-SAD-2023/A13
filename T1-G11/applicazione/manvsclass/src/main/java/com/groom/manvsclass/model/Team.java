package com.groom.manvsclass.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Team")
public class Team {
    @Id
    private String idTeam;
    private String name;
    private List<String> studenti; // Lista di ID o nomi degli studenti
    private List<String> assignments; // Lista di ID o nomi degli assignment

    // Costruttore
    public Team(String idTeam, String name) {
        this.idTeam = idTeam;
        this.name = name;
        this.studenti = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    // Getter e Setter
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

    public List<String> getStudenti() {
        return studenti;
    }

    public void setStudenti(List<String> studenti) {
        this.studenti = studenti;
    }

    public List<String> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<String> assignments) {
        this.assignments = assignments;
    }

    @Override
    public String toString() {
        return "Team{" +
                "idTeam='" + idTeam + '\'' +
                ", name='" + name + '\'' +
                ", studenti=" + studenti +
                ", assignments=" + assignments +
                '}';
    }
}
