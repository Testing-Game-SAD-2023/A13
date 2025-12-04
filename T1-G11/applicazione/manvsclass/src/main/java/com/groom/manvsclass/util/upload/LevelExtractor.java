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
        
        if (length < 7) {
            throw new IllegalArgumentException(
                "Nome cartella livello non valido: '" + folderName + "'. " +
                "Il nome deve terminare con il formato 'XXLevel' (es: 01Level, 02Level)."
            );
        }
        
        try {
            String levelStr = folderName.substring(length - 7, length - 5);
            return Integer.parseInt(levelStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Impossibile estrarre il numero del livello dalla cartella '" + folderName + "'. " +
                "Il nome deve contenere un numero a due cifre seguito da 'Level' (es: 01Level, 02Level).",
                e
            );
        }
    }

    public static OpponentDifficulty extractDifficulty(File levelFolder) {
        int levelNumber = extractLevelNumber(levelFolder);
        
        if (levelNumber < 1 || levelNumber > OpponentDifficulty.values().length) {
            throw new IllegalArgumentException(
                "Numero di livello non valido: " + levelNumber + ". " +
                "I livelli devono essere compresi tra 1 e " + OpponentDifficulty.values().length + " " +
                "(corrispondenti alle difficoltà disponibili)."
            );
        }
        
        return OpponentDifficulty.values()[levelNumber - 1];
    }
}
