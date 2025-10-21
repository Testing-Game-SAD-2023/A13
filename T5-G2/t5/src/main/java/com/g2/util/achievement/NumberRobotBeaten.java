package com.g2.util.achievement;

import com.g2.model.dto.GameProgressDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Classe che definisce gli achievement globali legati al numero di robot battuti.
 * <p>
 * Ogni achievement è rappresentato da una funzione che, data la lista dei ({@link GameProgressDTO}) del giocatore,
 * restituisce un booleano che indica se l'achievement è stato sbloccato.
 * </p>
 */
public class NumberRobotBeaten {

    private NumberRobotBeaten() {
        throw new IllegalStateException("Classe utility che definisce gli achievement di tipo \"Numero di Robot battuti\"");
    }

    /**
     * Restituisce una mappa che associa il nome dell'achievement
     * alla funzione di validazione corrispondente.
     *
     * @return una mappa {@code Map<String, Function<List<GameProgressDTO>, Boolean>>}
     * contenente le regole di verifica degli achievement
     */
    public static Map<String, Function<List<GameProgressDTO>, Boolean>> getAchievementFunctions() {
        Map<String, Function<List<GameProgressDTO>, Boolean>> verifyBeaten = new HashMap<>();
        verifyBeaten.put("firstMatchWon", NumberRobotBeaten::firstMatchWon);
        verifyBeaten.put("thirdMatchWon", NumberRobotBeaten::thirdMatchWon);

        return verifyBeaten;
    }

    /**
     * Definisce la funzione che valuta se il giocatore ha vinto almeno una partita.
     *
     * @param gameProgresses la lista dei {@link GameProgressDTO del giocatore}
     * @return {@code true} se è stata vinta almeno una partita, {@code false} altrimenti
     */
    private static Boolean firstMatchWon(List<GameProgressDTO> gameProgresses) {
        return gameProgresses != null && gameProgresses.stream().anyMatch(GameProgressDTO::isWon);
    }

    /**
     * Definisce la funzione che valuta se il giocatore ha vinto almeno tre partite.
     *
     * @param gameProgresses la lista dei {@link GameProgressDTO del giocatore}
     * @return {@code true} se sono state vinte almeno tre partite, {@code false} altrimenti
     */
    private static Boolean thirdMatchWon(List<GameProgressDTO> gameProgresses) {
        return gameProgresses != null && gameProgresses.stream().filter(GameProgressDTO::isWon).count() >= 3;
    }
}
