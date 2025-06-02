package com.g2.Service;

import com.g2.Game.GameModes.Compile.CompileResult;
import com.g2.Model.AvailableRobot;
import com.g2.Model.UserGameProgress;
import com.g2.util.AchievementDefinition.NumberAllRobotForClassBeaten;
import com.g2.util.AchievementDefinition.NumberRobotBeaten;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
public class AchievementService {

    private static final Logger logger = LoggerFactory.getLogger(AchievementService.class);

    public Set<String> verifyGameModeAchievement(
            Map<String, BiFunction<CompileResult, CompileResult, Boolean>> achievements,
            CompileResult user,
            CompileResult robot) {

        Set<String> unlocked = new HashSet<>();
        logger.info("[verifyAchievements]: {}", achievements);
        for (var entry : achievements.entrySet()) {
            if (entry.getValue().apply(user, robot)) {
                unlocked.add(entry.getKey());
            }
        }

        return unlocked;
    }

    public Set<String> verifyNumberRobotBeaten(List<UserGameProgress> userGameProgresses) {
        Map<String, Function<List<UserGameProgress>, Boolean>> achievements = NumberRobotBeaten.getAchievementFunctions();
        Set<String> unlocked = new HashSet<>();
        for (var entry : achievements.entrySet()) {
            if (entry.getValue().apply(userGameProgresses)) {
                unlocked.add(entry.getKey());
            }
        }

        return unlocked;
    }

    public Set<String> verifyNumberAllRobotForClassBeaten(
            List<UserGameProgress> userGameProgresses,
            List<AvailableRobot> robots
    ) {
        Map<String, BiFunction<Map<String, List<UserGameProgress>>, Map<String, List<AvailableRobot>>, Boolean>> achievements =
                NumberAllRobotForClassBeaten.getAchievementFunctions();
        Map<String, List<AvailableRobot>> availableRobotsByClass = new HashMap<>();
        Map<String, List<UserGameProgress>> userGameProgressesByClass = new HashMap<>();

        for (AvailableRobot robot : robots) {
            if (!availableRobotsByClass.containsKey(robot.getTestClassId())) {
                ArrayList<AvailableRobot> arrayList = new ArrayList<>();
                arrayList.add(robot);
                availableRobotsByClass.put(robot.getTestClassId(), arrayList);
            } else {
                availableRobotsByClass.get(robot.getTestClassId()).add(robot);
            }
        }

        for (UserGameProgress progress : userGameProgresses) {
            if (!userGameProgressesByClass.containsKey(progress.getClassUT())) {
                ArrayList<UserGameProgress> arrayList = new ArrayList<>();
                arrayList.add(progress);
                userGameProgressesByClass.put(progress.getClassUT(), arrayList);
            } else {
                userGameProgressesByClass.get(progress.getClassUT()).add(progress);
            }
        }

        Set<String> unlocked = new HashSet<>();
        for (var entry : achievements.entrySet()) {
            if (entry.getValue().apply(userGameProgressesByClass, availableRobotsByClass)) {
                unlocked.add(entry.getKey());
            }
        }

        return unlocked;
    }
}
