package com.groom.manvsclass.model;

import com.commons.model.Gamemode;
import com.commons.model.Robot;
import com.commons.model.StatisticRole;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;

@Document(collection = "statistics")
public class Statistic {
    @Id
    @GeneratedValue
    private String id;

    private String name;

    @Enumerated(EnumType.STRING)
    private StatisticRole role;

    @Enumerated(EnumType.STRING)
    private Gamemode gamemode;

    @Enumerated(EnumType.STRING)
    private Robot robot;

    public Statistic() {

    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
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

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
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
                "Id=" + id +
                ", name='" + name + '\'' +
                ", gamemode='" + gamemode + '\'' +
                ", robot='" + robot + '\'' +
                '}';
    }
}
