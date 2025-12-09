package com.groom.manvsclass.util.upload;

import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import java.io.File;

public class LevelExtractor {

    public static int extractLevelNumber(File levelFolder) {
        String folderName = levelFolder.getPath();
        
        if(folderName.matches("\\d{2}Level") == false) {
			throw new IllegalArgumentException(
				"Nome cartella livello non valido: " + folderName "."
				+ "Il nome della cartella deve seguire il formato XXLevel (es. 01 Level, 02Level)."
				);
		}
        
		return Integer.parseInt(folderName.substring(0,2));
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
