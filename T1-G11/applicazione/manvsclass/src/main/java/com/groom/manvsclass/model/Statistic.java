package com.groom.manvsclass.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Document(collection = "statistics")
public class Statistic {
    @Id
    private int Id;

    private String name;

    @Enumerated(EnumType.STRING)
    private StatisticRole role;

    @Enumerated(EnumType.STRING)
    private Gamemode gamemode;

    private String robot;

    public Statistic() {

    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gamemode getGamemode() {
        return gamemode;
    }

    public void setGamemode(Gamemode gamemode) {
        this.gamemode = gamemode;
    }

    public String getRobot() {
        return robot;
    }

    public void setRobot(String robot) {
        this.robot = robot;
    }

    public StatisticRole getRole() {
        return role;
    }

    public void setRole(StatisticRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", gamemode='" + gamemode + '\'' +
                ", robot='" + robot + '\'' +
                '}';
    }
}
