package com.g2.util.achievement;

import com.g2.model.OpponentSummary;
import com.g2.model.dto.GameProgressDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Classe che definisce gli achievement basati sul numero di robot
 * battuti in relazione alle classi di test.
 * <p>
 * Ogni achievement è rappresentato da una funzione che, data la lista dei ({@link GameProgressDTO}) del giocatore e la
 * lista di {@link OpponentSummary} disponibili nel sistema, restituisce un booleano che indica se l'achievement è
 * stato sbloccato.
 * </p>
 */
public class NumberAllRobotForClassBeaten {

    private NumberAllRobotForClassBeaten() {
        throw new IllegalStateException("Classe utility che definisce il template e verifica di sblocco per achievement " +
                "di tipo \"Numero di Robot battuti per classe\"");
    }

    /**
     * Restituisce una mappa che associa il nome dell'achievement
     * alla funzione di validazione corrispondente.
     *
     * @return una mappa {@code Map<String, BiFunction<Map<String, List<GameProgressDTO>>, Map<String, List<OpponentSummary>>, Boolean>>}
     * contenente le regole di verifica degli achievement
     */
    public static Map<String, BiFunction<Map<String, List<GameProgressDTO>>, Map<String, List<OpponentSummary>>, Boolean>> getAchievementFunctions() {
        Map<String, BiFunction<Map<String, List<GameProgressDTO>>, Map<String, List<OpponentSummary>>, Boolean>> verifyBeaten = new HashMap<>();

        verifyBeaten.put("allBeatenOneClass", NumberAllRobotForClassBeaten::beatAllRobotForOneClassUT);
        verifyBeaten.put("allBeatenTwoClass", NumberAllRobotForClassBeaten::beatAllRobotForTwoClassUT);

        return verifyBeaten;
    }

    /**
     * Verifica se il giocatore ha battuto tutti i robot disponibili per almeno una classe UT.
     * <p>
     * La verifica viene effettuata confrontando, per ciascuna classe UT, il numero di avversari sconfitti
     * (derivato dal {@link GameProgressDTO}) con il numero totale di avversari disponibili per quella classe
     * (derivato da {@link OpponentSummary}).
     * </p>
     *
     * @param gameProgressesByClass  mappa che associa ciascuna classe UT alla lista di progressi di gioco del
     *                               giocatore (ossia i robot già battuti in quella classe)
     * @param availableRobotsByClass mappa che associa ciascuna classe UT alla lista completa di avversari
     *                               disponibili per quella classe
     * @return {@code true} se esiste almeno una classe UT in cui il numero di robot battuti dal giocatore coincide
     * con il numero totale di robot disponibili, {@code false} altrimenti
     */
    private static Boolean beatAllRobotForOneClassUT(
            Map<String, List<GameProgressDTO>> gameProgressesByClass,
            Map<String, List<OpponentSummary>> availableRobotsByClass) {

        for (String classUT : gameProgressesByClass.keySet()) {
            if (availableRobotsByClass.containsKey(classUT) && gameProgressesByClass.get(classUT).size() == availableRobotsByClass.get(classUT).size())
                return true;
        }
        return false;
    }

    /**
     * Verifica se il giocatore ha battuto tutti i robot disponibili per almeno due classi UT.
     * <p>
     * La verifica viene effettuata confrontando, per ciascuna classe UT, il numero di avversari sconfitti
     * (derivato dal {@link GameProgressDTO}) con il numero totale di avversari disponibili per quella classe
     * (derivato da {@link OpponentSummary}).
     * </p>
     *
     * @param gameProgressesByClass  mappa che associa ciascuna classe UT alla lista di progressi di gioco del
     *                               giocatore (ossia i robot già battuti in quella classe)
     * @param availableRobotsByClass mappa che associa ciascuna classe UT alla lista completa di avversari
     *                               disponibili per quella classe
     * @return {@code true} se esistono almeno due classe UT in cui il numero di robot battuti dal giocatore coincide
     * con il numero totale di robot disponibili, {@code false} altrimenti
     */
    private static Boolean beatAllRobotForTwoClassUT(Map<String, List<GameProgressDTO>> gameProgressesByClass,
                                                     Map<String, List<OpponentSummary>> availableRobotsByClass) {
        int beaten = 0;
        for (String classUT : gameProgressesByClass.keySet()) {
            if (availableRobotsByClass.containsKey(classUT) && gameProgressesByClass.get(classUT).size() == availableRobotsByClass.get(classUT).size())
                beaten++;
        }
        return beaten >= 2;
    }
}
