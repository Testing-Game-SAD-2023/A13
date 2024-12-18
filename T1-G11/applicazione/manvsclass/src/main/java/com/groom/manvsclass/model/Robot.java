package com.groom.manvsclass.model;

public class Robot {

    private String robotName;
    private String robotFile;

    public Robot(String robotName, String robotFile) {
        this.robotName = robotName;
        this.robotFile = robotFile;
    }

    public Robot() {

    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getRobotFile() {
        return robotFile;
    }

    public void setRobotFile(String robotFile) {
        this.robotFile = robotFile;
    }

    @Override
    public String toString() {
        return "Robot [robotName=" + robotName + ", robotFile=" + robotFile + "]";
    }
}