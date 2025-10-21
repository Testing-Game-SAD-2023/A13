package com.groom.manvsclass.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "Team")
public class Team {
    @Id
    private String idTeam;
    private String name;
    private Date creationDate; //Data di creazione del team
    private int numeroStudenti;
    private List<String> idStudenti; // Lista di ID o nomi degli studenti

    // Costruttore
    public Team(String idTeam, String name) {
        this.idTeam = idTeam;
        this.name = name;
        this.numeroStudenti = 0; //Default
        this.creationDate = new Date(); //Data attuale
        this.idStudenti = new ArrayList<>();
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

    public void setCreazioneDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public int getNumStudenti() {
        return this.numeroStudenti;
    }

    public void setNumStudenti(int numeroStudenti) {
        this.numeroStudenti = numeroStudenti;
    }

    public List<String> getStudenti() {
        return this.idStudenti;
    }

    public void setStudenti(List<String> studenti) {
        this.idStudenti = studenti;
    }

    @Override
    public String toString() {
        return "Team{" +
                "idTeam='" + idTeam + '\'' +
                ", name='" + name + '\'' +
                ", dataCreazione='" + creationDate + '\'' +
                ", studenti=" + idStudenti
                + "}";
    }
}
