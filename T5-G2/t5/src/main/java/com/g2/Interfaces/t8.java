package com.g2.Interfaces;

import java.util.HashMap;
import java.util.Map;

public class t8 implements t8Interface{

    public static Map<Integer, String> RobotList() {
        Map<Integer, String> robot = new HashMap<>();

        robot.put(0, "Randoop lvl 1");
        robot.put(1, "Randoop lvl 2");
        robot.put(2, "Evo Suite lvl 1");
        robot.put(3, "Evo Suite lvl 2");

        return robot;
    }
}
