package com.g2.Model;

import com.commons.model.Gamemode;
import com.commons.model.Robot;

public class Ratio {

    private Integer playerID;
    private Gamemode gamemode;
    private Robot robot;
    private Float value;

    
    public Ratio(Integer playerID, Gamemode gamemode, Robot robot, float value) {
        this.playerID = playerID;
        this.gamemode = gamemode;
        this.robot = robot;
        this.value = value;
    }

    // Getter e Setter
    public Integer getPlayerID() {
        return playerID;
    }

    public void setPlayerID(Integer playerID) {
        this.playerID = playerID;
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

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }



    // Implementazione del metodo toString()
    @Override
    public String toString() {
        return "Ratio{" +
               "playerID=" + playerID +
               ", gamemode=" + gamemode +
               ", robot=" + robot +
               ", value=" + value +
               '}';
    }

}
