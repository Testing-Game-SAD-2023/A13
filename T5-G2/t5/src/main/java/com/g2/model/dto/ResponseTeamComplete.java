package com.g2.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.g2.model.Assignment;
import com.g2.model.Team;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseTeamComplete {

    @JsonProperty("assignments")
    private List<Assignment> assignments;

    @JsonProperty("team")
    private Team team;

    // Costruttore con argomenti per Jackson
    @JsonCreator
    public ResponseTeamComplete(
            @JsonProperty("assignments") List<Assignment> assignments,
            @JsonProperty("team") Team team) {
        this.assignments = assignments;
        this.team = team;
    }

    // Costruttore senza argomenti
    public ResponseTeamComplete() {
    }

    // Getters e Setters
    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
