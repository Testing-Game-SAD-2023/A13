package com.g2.Model.DTO;

import java.util.List;

import com.g2.Model.Assignment;
import com.g2.Model.Team;

public class ResponseTeamComplete {
    private List<Assignment> assignments;
    private Team team;

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
