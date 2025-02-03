package com.g2.Model;

import java.time.ZonedDateTime;

public class Assignment {
    private String idAssignment;
    private String teamId;
    private String nomeTeam;
    private String titolo;
    private String descrizione;
    private ZonedDateTime dataCreazione;
    private ZonedDateTime dataScadenza;

    // Getters e Setters
    public String getIdAssignment() {
        return idAssignment;
    }

    public void setIdAssignment(String idAssignment) {
        this.idAssignment = idAssignment;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getNomeTeam() {
        return nomeTeam;
    }

    public void setNomeTeam(String nomeTeam) {
        this.nomeTeam = nomeTeam;
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

    public ZonedDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(ZonedDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public ZonedDateTime getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(ZonedDateTime dataScadenza) {
        this.dataScadenza = dataScadenza;
    }
}
