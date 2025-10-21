package com.g2.game.service;

import com.g2.game.gameMode.Compile.CompileResult;
import com.g2.game.gameMode.GameLogic;
import com.g2.interfaces.ServiceManager;
import com.g2.model.OpponentSummary;
import com.g2.model.dto.GameProgressDTO;
import com.g2.model.dto.PlayerProgressDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service dedicato alla gestione delle statistiche del giocatore, quali la verifica e l’assegnazione degli achievement
 * e dei punti esperienza.
 */
@Service
public class PlayerStatService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerStatService.class);
    private final ServiceManager serviceManager;
    private final AchievementService achievementService;

    public PlayerStatService(ServiceManager serviceManager, AchievementService achievementService) {
        this.serviceManager = serviceManager;
        this.achievementService = achievementService;
    }


    /**
     * Verifica e sblocca gli achievement legati alla modalità di gioco corrente.
     * <p>
     * Recupera {@link GameProgressDTO} del giocatore rispetto all’avversario specifico e
     * utilizza l'{@link AchievementService} per determinare quali achievement
     * sono stati sbloccati nel turno. Gli achievement vengono poi salvati
     * tramite chiamata a T23.
     *
     * @param currentGame la partita corrente del giocatore.
     * @param user        il risultato di compilazione e coverage del giocatore.
     * @param opponent    il risultato di compilazione e coverage dell’avversario.
     * @return un {@link Set} rappresentante gli achievement non precedentemente sbloccati.
     */
    public Set<String> unlockGameModeAchievements(GameLogic currentGame, CompileResult user, CompileResult opponent) {
        long playerId = currentGame.getPlayerID();
        GameMode gameMode = currentGame.getGameMode();
        String classUT = currentGame.getClassUTName();
        String robotType = currentGame.getTypeRobot();
        OpponentDifficulty difficulty = currentGame.getDifficulty();

        GameProgressDTO currentGameProgress = (GameProgressDTO) serviceManager.handleRequest("T23", "getPlayerProgressAgainstOpponent", playerId, gameMode, classUT, robotType, difficulty);
        logger.info("[achievementsUnlocked] Progresso sulla partita corrente: {}", currentGameProgress);

        logger.info("[achievementsUnlocked] Avvio verifica degli achievement sbloccati nel turno");
        Set<String> unlockedAchievements = new HashSet<>(achievementService.verifyGameModeAchievement(currentGame.gameModeAchievements(), user, opponent));
        logger.info("[achievementsUnlocked] Avvio salvataggio degli achievement sbloccati");
        currentGameProgress = (GameProgressDTO) serviceManager.handleRequest("T23", "updatePlayerProgressAgainstOpponent", playerId, gameMode, classUT, robotType, difficulty, false, unlockedAchievements);

        logger.info("[achievementsUnlocked] Achievement sbloccati: {}", currentGameProgress.getAchievements());
        return currentGameProgress.getAchievements();
    }


    /**
     * Verifica e aggiorna gli achievement globali del giocatore.
     * <p>
     * Gli achievement globali si riferiscono a obiettivi cumulativi ottenuti ad esempio
     * giocando contro più avversari.
     * <p>
     * Il metodo interroga i servizi remoti per ottenere i {@link GameProgressDTO}
     * e l’elenco degli avversari, esegue le verifiche tramite {@link AchievementService}
     * e aggiorna gli achievement in T23.
     *
     * @param playerId l’identificativo del giocatore.
     * @return un {@link Set} contenente i nuovi achievement globali non precedentemente sbloccati.
     */
    public Set<String> unlockGlobalAchievements(long playerId) {
        logger.info("[globalAchievementsUnlocked] Avvio fetch PlayerProgress per playerId={}", playerId);
        PlayerProgressDTO status = (PlayerProgressDTO) serviceManager.handleRequest("T23", "getPlayerProgressAgainstAllOpponent", playerId);
        logger.info("[globalAchievementsUnlocked] PlayerProgress: {}", status);

        List<OpponentSummary> opponents = (List<OpponentSummary>) serviceManager.handleRequest("T1", "getOpponentsSummary");
        logger.info("[globalAchievementsUnlocked] Avversari disponibili estratti: {}", opponents);

        Set<String> achievementUnlocked = new HashSet<>();

        logger.info("[globalAchievementsUnlocked] Avvio verifica di nuovi achievement globali sbloccati");
        achievementUnlocked.addAll(achievementService.verifyNumberRobotBeaten(status.getGameProgressesDTO()));
        achievementUnlocked.addAll(achievementService.verifyNumberAllRobotForClassBeaten(status.getGameProgressesDTO(), opponents));
        logger.info("[globalAchievementsUnlocked] Salvataggio achievement sbloccati");

        achievementUnlocked = (Set<String>) serviceManager.handleRequest("T23", "updateGlobalAchievements", playerId, achievementUnlocked);

        logger.info("[globalAchievementsUnlocked] Nuovi achievement globali sbloccati: {}", achievementUnlocked);
        return achievementUnlocked;
    }


    /**
     * Assegna punti esperienza al giocatore in base alla difficoltà dell’avversario sconfitto.
     * <p>
     * Se il giocatore non ha ancora battuto l’avversario attuale, il metodo aggiorna
     * il {@link GameProgressDTO} della partita come “vinta”, calcola i punti esperienza (basati sulla
     * difficoltà dell'avversario) e li salva tramite T23.
     *
     * @param currentGame la partita corrente del giocatore.
     * @return il numero di punti esperienza guadagnati in questa partita (0 se già vinta in precedenza).
     */
    public int assignExperiencePoints(GameLogic currentGame) {
        long playerId = currentGame.getPlayerID();
        GameMode gameMode = currentGame.getGameMode();
        String classUT = currentGame.getClassUTName();
        String robotType = currentGame.getTypeRobot();
        OpponentDifficulty difficulty = currentGame.getDifficulty();

        GameProgressDTO currentGameProgress = (GameProgressDTO) serviceManager.handleRequest("T23", "getPlayerProgressAgainstOpponent", playerId, gameMode, classUT, robotType, difficulty);
        logger.info("[handleExperiencePoints] Progresso sulla partita corrente: {}", currentGameProgress);

        if (currentGameProgress.isWon()) {
            logger.info("[handleExperiencePoints] L'utente ha già battuto questo avversario, nessun punto esperienza verrà fornito");
            return 0;
        }

        logger.info("[handleExperiencePoints] L'utente non ha ancora battuto questo avversario");
        currentGameProgress = (GameProgressDTO) serviceManager.handleRequest("T23", "updatePlayerProgressAgainstOpponent",
                playerId, gameMode, classUT, robotType, difficulty, true, new HashSet<String>());
        logger.info("[handleExperiencePoints] Aggiornamento match corrente come vinto: {}", currentGameProgress);
        int expGained = difficulty.toInt();
        serviceManager.handleRequest("T23", "incrementUserExp", playerId, expGained);
        logger.info("[handleExperiencePoints] Assegnamento di {} punti esperienza", expGained);

        return expGained;
    }
}
