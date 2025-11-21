package com.groom.manvsclass.util.upload;

import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import java.io.File;

public class LevelExtractor {

    private LevelExtractor() {
        // Utility class
    }

    public static int extractLevelNumber(File levelFolder) {
        String folderName = levelFolder.toString();
        int length = folderName.length();
        String levelStr = folderName.substring(length - 7, length - 5);
        return Integer.parseInt(levelStr);
    }

    public static OpponentDifficulty extractDifficulty(File levelFolder) {
        int levelNumber = extractLevelNumber(levelFolder);
        return OpponentDifficulty.values()[levelNumber - 1];
    }
}
