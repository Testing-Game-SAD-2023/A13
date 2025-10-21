package com.g2.model;

import com.fasterxml.jackson.annotation.*;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Assignment {

    @JsonProperty("idAssignment")
    private String idAssignment;

    @JsonProperty("teamId")
    private String teamId;

    @JsonProperty("nomeTeam")
    private String nomeTeam;

    @JsonProperty("titolo")
    private String titolo;

    @JsonProperty("descrizione")
    private String descrizione;

    @JsonProperty("dataCreazione")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Instant dataCreazione;

    @JsonProperty("dataScadenza")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Instant dataScadenza;

    // Costruttore senza argomenti
    public Assignment() {
    }

    // Costruttore con argomenti per Jackson
    @JsonCreator
    public Assignment(
            @JsonProperty("idAssignment") String idAssignment,
            @JsonProperty("teamId") String teamId,
            @JsonProperty("nomeTeam") String nomeTeam,
            @JsonProperty("titolo") String titolo,
            @JsonProperty("descrizione") String descrizione,
            @JsonProperty("dataCreazione") Instant dataCreazione,
            @JsonProperty("dataScadenza") Instant dataScadenza) {
        this.idAssignment = idAssignment;
        this.teamId = teamId;
        this.nomeTeam = nomeTeam;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataCreazione = dataCreazione;
        this.dataScadenza = dataScadenza;
    }

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

    public Instant getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Instant dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Instant getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Instant dataScadenza) {
        this.dataScadenza = dataScadenza;
    }
}
