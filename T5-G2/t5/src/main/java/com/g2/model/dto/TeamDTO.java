package com.g2.model.dto;

public class TeamDTO {
    private String teamName;
    private String teamImagePath;

    public TeamDTO() {}

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamImagePath() {
        return teamImagePath;
    }

    public void setTeamImagePath(String teamImagePath) {
        this.teamImagePath = teamImagePath;
    }
}
