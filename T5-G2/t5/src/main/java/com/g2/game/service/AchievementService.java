package com.g2.game.service;

import com.g2.game.gameMode.Compile.CompileResult;
import com.g2.model.OpponentSummary;
import com.g2.model.dto.GameProgressDTO;
import com.g2.util.achievement.NumberAllRobotForClassBeaten;
import com.g2.util.achievement.NumberRobotBeaten;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Service che espone la logica di verifica degli achievement del sistema di gioco. Ongi metodo offerto si occupa di valutare
 * un tipo di achievement specifico
 */
@Service
public class AchievementService {

    private static final Logger logger = LoggerFactory.getLogger(AchievementService.class);

    /**
     * Verifica gli achievement legati alla modalità di gioco, confrontando il risultato di copertura {@link CompileResult},
     * dell'utente e dell'avversario. Questi achievement sono definiti all'interno della modalità di gioco stessa.
     *
     * @param achievements la mappa che associa il nome dell'achievement alla funzione di verifica
     * @param user         il risultato di compilazione dell'utente
     * @param robot        il risultato di compilazione del robot
     * @return il {@link Set} di nomi degli achievement sbloccati
     */
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

    /**
     * Verifica gli achievement basati sul numero di avversari sconfitti complessivamente, definiti in {@link NumberRobotBeaten}
     *
     * @param gameProgresses la lista dei progressi di gioco dell'utente
     * @return il {@link Set} di nomi degli achievement sbloccati
     */
    public Set<String> verifyNumberRobotBeaten(List<GameProgressDTO> gameProgresses) {
        Map<String, Function<List<GameProgressDTO>, Boolean>> achievements = NumberRobotBeaten.getAchievementFunctions();
        Set<String> unlocked = new HashSet<>();
        for (var entry : achievements.entrySet()) {
            if (entry.getValue().apply(gameProgresses)) {
                unlocked.add(entry.getKey());
            }
        }

        return unlocked;
    }

    /**
     * Verifica gli achievement basati sull'aver sconfitto tutti gli avversari disponibili per una o più classi UT.
     *
     * @param gameProgress la lista dei progressi di gioco dell'utente
     * @param robots       la lista degli avversari disponibili nel sistema
     * @return il {@link Set} di nomi degli achievement sbloccati
     */
    public Set<String> verifyNumberAllRobotForClassBeaten(
            List<GameProgressDTO> gameProgress,
            List<OpponentSummary> robots
    ) {
        Map<String, BiFunction<Map<String, List<GameProgressDTO>>, Map<String, List<OpponentSummary>>, Boolean>> achievements =
                NumberAllRobotForClassBeaten.getAchievementFunctions();
        Map<String, List<OpponentSummary>> availableRobotsByClass = new HashMap<>();
        Map<String, List<GameProgressDTO>> gameProgressesByClass = new HashMap<>();

        for (OpponentSummary robot : robots) {
            if (!availableRobotsByClass.containsKey(robot.getClassUT())) {
                ArrayList<OpponentSummary> arrayList = new ArrayList<>();
                arrayList.add(robot);
                availableRobotsByClass.put(robot.getClassUT(), arrayList);
            } else {
                availableRobotsByClass.get(robot.getClassUT()).add(robot);
            }
        }

        for (GameProgressDTO progress : gameProgress) {
            if (!gameProgressesByClass.containsKey(progress.getClassUT())) {
                ArrayList<GameProgressDTO> arrayList = new ArrayList<>();
                arrayList.add(progress);
                gameProgressesByClass.put(progress.getClassUT(), arrayList);
            } else {
                gameProgressesByClass.get(progress.getClassUT()).add(progress);
            }
        }

        Set<String> unlocked = new HashSet<>();
        for (var entry : achievements.entrySet()) {
            if (entry.getValue().apply(gameProgressesByClass, availableRobotsByClass)) {
                unlocked.add(entry.getKey());
            }
        }

        return unlocked;
    }
}
