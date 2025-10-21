package com.g2.model;

import com.fasterxml.jackson.annotation.*;

import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Team {

    @JsonProperty("idTeam")
    private String idTeam;

    @JsonProperty("name")
    private String name;

    @JsonProperty("creationDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Instant creationDate;

    @JsonProperty("studenti")
    private List<String> studenti;

    @JsonProperty("numStudenti")
    private int numStudenti;

    // Costruttore con argomenti per Jackson
    @JsonCreator
    public Team(
            @JsonProperty("idTeam") String idTeam,
            @JsonProperty("name") String name,
            @JsonProperty("creationDate") Instant creationDate,
            @JsonProperty("studenti") List<String> studenti,
            @JsonProperty("numStudenti") int numStudenti) {
        this.idTeam = idTeam;
        this.name = name;
        this.creationDate = creationDate;
        this.studenti = studenti;
        this.numStudenti = numStudenti;
    }

    // Costruttore senza argomenti
    public Team() {
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
}
