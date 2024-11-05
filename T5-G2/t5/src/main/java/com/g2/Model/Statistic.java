package com.g2.Model;

import com.commons.model.Gamemode;
import com.commons.model.Robot;
import com.commons.model.StatisticRole;
import com.g2.Interfaces.IStatisticCalculator;
import com.g2.factory.StatisticCalculatorFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Statistic {
    private String Id;

    private String name;

    private StatisticRole role;
    private Gamemode gamemode;
    private Robot robot;

    public Statistic(String id, String name, Robot robot, Gamemode gamemode, StatisticRole role) {
        this.robot = robot;
        this.gamemode = gamemode;
        this.role = role;
        this.name = name;
        Id = id;
    }

    public Statistic() {

    }

    public void setId(String id) { Id = id; }

    public String getID() { return Id; }

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

    public float calculate(List<Game> gamesList) {
        return StatisticCalculatorFactory.getStatisticCalculator(role).calculate(gamesList);
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
