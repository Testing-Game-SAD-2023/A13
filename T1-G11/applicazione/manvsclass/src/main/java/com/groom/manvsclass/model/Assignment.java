package com.groom.manvsclass.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Assignment")
public class Assignment {
    @Id
    private String idAssignment;

    // Per la visualizzazione
    private String teamId;
    private String nomeTeam;

    private String titolo;
    private String descrizione;
    private Date dataCreazione;
    private Date dataScadenza;


    // Costruttore
    public Assignment(String titolo, String descrizione, Date dataScadenza) {
        this.idAssignment = null;
        this.teamId = null;
        this.nomeTeam = null;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataCreazione = new Date(); // Creazione all'istante corrente
        this.dataScadenza = dataScadenza;
    }

    // Getter e Setter
    public String getIdAssignment() {
        return idAssignment;
    }

    public void setIdAssignment(String idAssignment) {
        this.idAssignment = idAssignment;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setIdTeam(String idTeam) {
        this.teamId = idTeam;
    }

    public String getNomeTeam() {
        return nomeTeam;
    }

    public void setNomeTeam(String nomeTeam) {
        this.nomeTeam = nomeTeam;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "idAssignment='" + idAssignment + '\'' +
                ", idTeam='" + teamId + '\'' +
                ", nomeTeam='" + nomeTeam + '\'' +
                ", titolo='" + titolo + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", dataCreazione=" + dataCreazione +
                ", dataScadenza=" + dataScadenza +
                '}';
    }
}
