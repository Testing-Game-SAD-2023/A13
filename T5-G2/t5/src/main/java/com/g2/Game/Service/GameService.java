/*
 *   Copyright (c) 2025 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.g2.Game.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.g2.Game.GameDTO.EndGameDTO.EndGameResponseDTO;
import com.g2.Game.GameFactory.params.GameParams;
import com.g2.Model.*;
import com.g2.Service.FileOperationService;
import com.g2.Service.AchievementService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g2.Game.GameFactory.GameRegistry;
import com.g2.Game.GameModes.Compile.CompileResult;
import com.g2.Game.GameModes.GameLogic;
import com.g2.Interfaces.ServiceManager;
import com.g2.Session.Exceptions.GameModeAlreadyExist;
import com.g2.Session.Exceptions.GameModeDontExist;
import com.g2.Session.Exceptions.SessionDontExist;
import com.g2.Session.SessionService;

@Service
public class GameService {

    private final ServiceManager serviceManager;
    private final GameRegistry gameRegistry;
    private final SessionService sessionService;
    private final AchievementService achievementService;
    private final FileOperationService fileOperationService;
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    @Autowired
    public GameService(ServiceManager serviceManager,
                        GameRegistry gameRegistry,
                        SessionService sessionService,
                        AchievementService achievementService,
                        FileOperationService fileOperationService) {
        this.serviceManager = serviceManager;
        this.gameRegistry = gameRegistry;
        this.sessionService = sessionService;
        this.achievementService = achievementService;
        this.fileOperationService = fileOperationService;
    }

    public GameLogic CreateGame(GameParams gameParams) throws GameModeAlreadyExist {
        String playerId = gameParams.getPlayerId();
        String mode = gameParams.getMode();
        try {
            /*
             * gameRegistry istanzia dinamicamente uno degli oggetti gameLogic (sfida, allenamento, scalata e ecc)
             * basta passargli il campo mode e dinamicamente se ne occupa lui
             */
            GameLogic gameLogic = gameRegistry.createGame(serviceManager, gameParams);
            logger.info("createGame: oggetto game creato con successo per playerId={}, mode={}.", playerId, mode);
            /*
             * Salvo il game in T4
             */
            gameLogic.CreateGame();
            logger.info("createGame: Inizio creazione partita per playerId={}, mode={}.", playerId, mode);
            /*
             * Creo nella sessione i dati di gioco
             */
            sessionService.SetGameMode(playerId, gameLogic);
            logger.info("createGame: sessione aggiornata con successo per playerId={}, mode={}.", playerId, mode);
            /*
             * Creo, se non esiste, lo UserGameProgress che tiene traccia della vittoria e degli obiettivi sbloccati
             * dell'utente per il GameRecord (mode, class, type_robot, difficulty)
             */
            UserGameProgress progress = (UserGameProgress) serviceManager.handleRequest("T4", "createUserGameProgress", Integer.parseInt(playerId), mode, gameParams.getUnderTestClassName(), gameParams.getType_robot(), gameParams.getDifficulty());
            logger.info("createGame: creato/recuperato con successo progress per playerId={}.", playerId);
            return gameLogic;
        } catch (SessionDontExist e) {
            logger.info("createGame: SessionDontExist per playerId={}, mode={}.", playerId, mode);
        } catch (Exception e) {
            logger.info("createGame: Exception per playerId={}, mode={}: {}", playerId, mode, e.getMessage());
        }
        return null;
    }

    public GameLogic GetGame(String mode, String playerId) throws GameModeDontExist {
        try {
            logger.info("getGame: Recupero partita per playerId={}, mode={}.", playerId, mode);
            GameLogic game = sessionService.getGameMode(playerId, mode);
            game.setServiceManager(serviceManager);
            logger.info("getGame: Partita recuperata con successo per playerId={} e modalità={}.", playerId, mode);
            return game;
        } catch ( SessionDontExist e) {
            throw new GameModeDontExist("Game don't exist ");
        }
    }

    public boolean destroyGame(String playerId, String mode) {
        try {
            sessionService.removeGameMode(playerId, mode, java.util.Optional.empty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean UpdateGame(String playerId, GameLogic game, GameParams updateParams, CompileResult userCompileResult, CompileResult robotCompileResult){
        game.updateState(updateParams, userCompileResult, robotCompileResult);
        return sessionService.updateGameMode(playerId, game);
    }

    public CompileResult handleCompile(GameLogic currentGame, String testingClassCode) {
        logger.info("handleCompile: Inizio compilazione per ClassName={}.", currentGame.getClasseUT());

        String underTestClassName = currentGame.getClasseUT();
        String testingClassName = "Test" + currentGame.getClasseUT();
        String testingClassFileName = testingClassName + ".java";
        String underTestClassFileName = underTestClassName + ".java";

        // Recupero il codice della classe under test
        String underTestClassCode = this.serviceManager.handleRequest("T1", "getClassUnderTest", String.class, underTestClassName);

        // Chiamata a T7 per calcolare jacoco coverage
        String responseT7Raw = this.serviceManager.handleRequest("T7", "CompileCoverage", String.class, testingClassFileName, testingClassCode, underTestClassFileName, underTestClassCode);
        JSONObject response_T7 = new JSONObject(responseT7Raw);

        // Chiamata a T8 per calcolare evosuite coverage solo se il codice è compilabile
        JSONObject response_T8 = new JSONObject();

        if (response_T7.optString("coverage", null) != null) {
            String responseT8Raw = this.serviceManager.handleRequest("T8", "evosuiteUserCoverage", String.class,
                    testingClassName, testingClassCode, underTestClassName, underTestClassCode, "");
            response_T8 = new JSONObject(responseT8Raw);
        }

        // Salvo in VolumeT0 testingClassCode, response_T8 (csv) e response_T7 (xml)
        String suffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        String userDir = String.format("/VolumeT0/FolderTree/StudentTest/Player%s/%s/%s/%s/Game%s/Round%s/Turn%s",
                currentGame.getPlayerID(), currentGame.getMode(), currentGame.getClasseUT(), suffix, currentGame.getGameID(), currentGame.getRoundID(), currentGame.getTurnID());
        String userCoverageDir = String.format("%s/coverage", userDir);
        String userSrcDir = String.format("%s/project/src/java/main", userDir);
        String userTestDir = String.format("%s/project/src/test/main", userDir);

        fileOperationService.createDirectory(userCoverageDir, userSrcDir, userTestDir);
        fileOperationService.writeTurn(underTestClassCode, underTestClassFileName, testingClassCode, testingClassFileName,
                response_T8.optString("statistics", ""), response_T7.optString("coverage", ""), userSrcDir, userTestDir, userCoverageDir);

        return new CompileResult(response_T8, response_T7);

    }

    public CompileResult handleCompileNew(GameLogic currentGame, String testingClassCode, String compileTypes) {
        logger.info("handleCompile: Inizio compilazione per ClassName={}.", currentGame.getClasseUT());

        String underTestClassName = currentGame.getClasseUT();
        String testingClassName = "Test" + currentGame.getClasseUT();
        String testingClassFileName = testingClassName + ".java";
        String underTestClassFileName = underTestClassName + ".java";

        // Recupero il codice della classe under test
        String underTestClassCode = this.serviceManager.handleRequest("T1", "getClassUnderTest", String.class, underTestClassName);

        // Preparo il percorso per salvare le compilazioni nel volume T0
        String suffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String userDir = String.format("/VolumeT0/FolderTree/StudentTest/Player%s/%s/%s/%s/Game%s/Round%s/Turn%s",
                currentGame.getPlayerID(), currentGame.getMode(), currentGame.getClasseUT(), suffix, currentGame.getGameID(), currentGame.getRoundID(), currentGame.getTurnID());
        String userCoverageDir = String.format("%s/coverage", userDir);
        String userSrcDir = String.format("%s/project/src/java/main", userDir);
        String userTestDir = String.format("%s/project/src/test/main", userDir);
        fileOperationService.createDirectory(userCoverageDir, userSrcDir, userTestDir);

        JSONObject response_T7 = new JSONObject();
        JSONObject response_T8 = new JSONObject();
        String responseT7Raw;
        switch (compileTypes) {
            case "T7":
                // Chiamata a T7 per calcolare jacoco coverage
                responseT7Raw = this.serviceManager.handleRequest("T7", "CompileCoverage", String.class, testingClassFileName, testingClassCode, underTestClassFileName, underTestClassCode);
                response_T7 = new JSONObject(responseT7Raw);

                fileOperationService.writeTurnNew(underTestClassCode, underTestClassFileName, testingClassCode, testingClassFileName,
                        response_T7.optString("coverage", ""), "coveragetot.xml", userSrcDir, userTestDir, userCoverageDir);

                return new CompileResult(response_T8, response_T7);
            case "T8":
                // Chiamata a T8 per calcolare evosuite coverage solo se il codice è compilabile
                if (currentGame.getUserCompileResult().getXML_coverage() != null) {
                    String responseT8Raw = this.serviceManager.handleRequest("T8", "evosuiteUserCoverage", String.class,
                            testingClassName, testingClassCode, underTestClassName, underTestClassCode, "");
                    response_T8 = new JSONObject(responseT8Raw);
                }

                fileOperationService.writeTurnNew(underTestClassCode, underTestClassFileName, testingClassCode, testingClassFileName,
                        response_T8.optString("statistics", ""), "statistics.csv", userSrcDir, userTestDir, userCoverageDir);

                return new CompileResult(currentGame.getUserCompileResult(), response_T8);
            default:
                responseT7Raw = this.serviceManager.handleRequest("T7", "CompileCoverage", String.class, testingClassFileName, testingClassCode, underTestClassFileName, underTestClassCode);
                response_T7 = new JSONObject(responseT7Raw);

                if (response_T7.optString("coverage", null) != null) {
                    String responseT8Raw = this.serviceManager.handleRequest("T8", "evosuiteUserCoverage", String.class,
                            testingClassName, testingClassCode, underTestClassName, underTestClassCode, "");
                    response_T8 = new JSONObject(responseT8Raw);
                }

                fileOperationService.writeTurn(underTestClassCode, underTestClassFileName, testingClassCode, testingClassFileName,
                        response_T8.optString("statistics", ""), response_T7.optString("coverage", ""), userSrcDir, userTestDir, userCoverageDir);

                return new CompileResult(response_T8, response_T7);
        }
    }

    /*
    *  Sfrutto T4 per avere i risultati dei robot
    */
    public CompileResult GetRobotCoverage(GameLogic currentGame) {
        try {
            logger.info("Richiesta Coverage robot per testClass={}, robotType={}, difficulty={}.",
                    currentGame.getClasseUT(),
                    currentGame.getTypeRobot(),
                    currentGame.getDifficulty()
            );
            return new CompileResult(currentGame, serviceManager,
                    currentGame.getClasseUT(),
                    currentGame.getTypeRobot(),
                    currentGame.getDifficulty()
            );
        } catch (Exception e) {
            logger.error("[GAMECONTROLLER] GetRobotCoverage:", e);
            return null;
        }
    }

    public void handleGameLogic(int userScore, int robotScore, GameLogic currentGame, GameParams updateParams, CompileResult userCompileResult, CompileResult robotCompileResult) {
        logger.info("handleGameLogic: Avvio logica di gioco per playerId={}.", currentGame.getPlayerID());
        currentGame.NextTurn(userScore, robotScore);
        UpdateGame(currentGame.getPlayerID(), currentGame, updateParams, userCompileResult, robotCompileResult);
    }

    public String[] handleGameModeAchievementsUnlocked(GameLogic currentGame, CompileResult user, CompileResult robot) {
        int playerId = Integer.parseInt(currentGame.getPlayerID());
        String gameMode = currentGame.getMode();
        String classUT = currentGame.getClasseUT();
        String robotType = currentGame.getTypeRobot();
        String difficulty = currentGame.getDifficulty();

        UserGameProgress currentGameProgress = (UserGameProgress) serviceManager.handleRequest("T4", "getUserGameProgress", playerId, gameMode, classUT, robotType, difficulty);
        logger.info("[achievementsUnlocked] Progresso sulla partita corrente: {}", currentGameProgress);

        logger.info("[achievementsUnlocked] Avvio verifica degli achievement sbloccati");
        Set<String> achievementUnlockedInTurn = achievementService.verifyGameModeAchievement(currentGame.gameModeAchievements(), user, robot);
        logger.info("[achievementsUnlocked] Achievement sbloccati nel turno: {}", achievementUnlockedInTurn);

        logger.info("[achievementsUnlocked] Avvio fetch degli achievement già sbloccati");
        String[] fetchedAchievements = currentGameProgress.getAchievements() == null ? new String[0] : currentGameProgress.getAchievements();
        logger.info("[achievementsUnlocked] Achievement già sbloccati per il match: {}", Arrays.toString(fetchedAchievements));

        Arrays.asList(fetchedAchievements).forEach(achievementUnlockedInTurn::remove);
        if (achievementUnlockedInTurn.isEmpty())
            return new String[0];

        logger.info("[achievementsUnlocked] Nuovi achievement sbloccati: {}", achievementUnlockedInTurn);
        logger.info("[achievementsUnlocked] Salvataggio dei nuovi achievement sbloccati.");
        String[] unlocked = achievementUnlockedInTurn.toArray(new String[0]);
        fetchedAchievements = ((UserGameProgress) serviceManager.handleRequest("T4", "updateUserGameProgressAchievements", playerId, gameMode, classUT, robotType, difficulty, unlocked)).getAchievements();
        logger.info("[achievementsUnlocked] Achievements aggiornati: {}", Arrays.toString(fetchedAchievements));

        return unlocked;
    }

    private String[] handleGlobalAchievementUnlocked(int playerId) {
        List<UserGameProgress> currentGameProgresses = (List<UserGameProgress>) serviceManager.handleRequest("T4", "getAllUserGameProgresses", playerId);
        logger.info("[globalAchievementsUnlocked] CurrentGameProgress trovai associati al player {}: {}", playerId, currentGameProgresses.size());

        GeneralAchievement generalAchievements = (GeneralAchievement) serviceManager.handleRequest("T4", "getGlobalAchievements", playerId);
        logger.info("[globalAchievementsUnlocked] Achievement globali già sbloccati dall'utente: {}", generalAchievements);

        List<AvailableRobot> robots = (List<AvailableRobot>) serviceManager.handleRequest("T4", "getAvailableRobots");
        logger.info("[globalAchievementsUnlocked] Robot disponibili estratti: {}", robots);

        logger.info("[globalAchievementsUnlocked] Avvio verifica di nuovi achievement globali sbloccati");
        Set<String> achievementUnlocked = achievementService.verifyNumberRobotBeaten(currentGameProgresses);
        achievementUnlocked.addAll(achievementService.verifyNumberAllRobotForClassBeaten(currentGameProgresses, robots));
        logger.info("[globalAchievementsUnlocked] Achievement sbloccati: {}", achievementUnlocked);

        logger.info("[globalAchievementsUnlocked] Avvio fetch degli achievement già sbloccati");
        String[] fetchedAchievements = generalAchievements.getAchievements() == null ? new String[0] : generalAchievements.getAchievements();
        logger.info("[globalAchievementsUnlocked] Achievement già sbloccati per il match: {}", Arrays.toString(fetchedAchievements));

        Arrays.asList(fetchedAchievements).forEach(achievementUnlocked::remove);
        if (achievementUnlocked.isEmpty())
            return new String[0];

        logger.info("[globalAchievementsUnlocked] Nuovi achievement sbloccati: {}", achievementUnlocked);
        logger.info("[globalAchievementsUnlocked] Salvataggio dei nuovi achievement sbloccati.");
        String[] unlocked = achievementUnlocked.toArray(new String[0]);
        fetchedAchievements = ((GeneralAchievement) serviceManager.handleRequest("T4", "updateGlobalAchievements", playerId, unlocked)).getAchievements();
        logger.info("[globalAchievementsUnlocked] Achievements aggiornati: {}", Arrays.toString(fetchedAchievements));

        return unlocked;
    }

    public int handleExperiencePoints(GameLogic currentGame) {
        int playerId = Integer.parseInt(currentGame.getPlayerID());
        String gameMode = currentGame.getMode();
        String classUT = currentGame.getClasseUT();
        String robotType = currentGame.getTypeRobot();
        String difficulty = currentGame.getDifficulty();

        UserGameProgress currentGameProgress = (UserGameProgress) serviceManager.handleRequest("T4", "getUserGameProgress", playerId, gameMode, classUT, robotType, difficulty);
        logger.info("[handleExperiencePoints] Progresso sulla partita corrente: {}", currentGameProgress);

        if (currentGameProgress.isWon()) {
            logger.info("[handleExperiencePoints] L'utente ha già battuto questo avversario, nessun punto esperienza verrà fornito");
            return 0;
        }

        logger.info("[handleExperiencePoints] L'utente ha già vinto questa sfida, nessun punto esperienza verrà fornito");
        currentGameProgress = (UserGameProgress) serviceManager.handleRequest("T4", "updateUserRecordForVictory",
                playerId, gameMode, classUT, robotType, difficulty);
        logger.info("[handleExperiencePoints] Aggiornamento match corrente come vinto: {}", currentGameProgress);
        Experience currentExp = (Experience) serviceManager.handleRequest("T4", "getUserExperiencePoints", playerId);
        Experience newExp = (Experience) serviceManager.handleRequest("T4", "updateUserExperiencePoints", playerId, Integer.parseInt(difficulty));
        logger.info("[handleExperiencePoints] Assegnamento di {} punti esperienza, l'utente passa da {} a {} punti esperienza.", difficulty, currentExp.getExperiencePoints(), newExp.getExperiencePoints());

        return Integer.parseInt(difficulty);
    }

    public EndGameResponseDTO handleGameEnd(GameLogic currentGame, boolean surrendered) {
        logger.info("handleGameEnd: Inizio operazioni di terminazione partita per playerId={}. Avvio aggiornamento progressi e notifiche.", currentGame.getPlayerID());
        /*
         * Verifico lo stato della compilazione che l'utente vuole consegnare (ultima compilazione)
         */
        if (currentGame.getUserCompileResult() == null ||
                !currentGame.getUserCompileResult().hasSuccess() ||
                surrendered ) {
            /*
             * Se l'utente non ha compilato, la compilazione ha generato errori o si è arreso:
             *  - Chiudo la partita in T4 e chiudo sessione
             *  - Invio la risposta di fallimento al frontend
             */
            EndGame(currentGame, 0);
            return new EndGameResponseDTO(0, 0, false, 0);
        } else if (!currentGame.isWinner()) {
            /*
             * Se l'utente ha perso la partita
             *  - Chiudo la partita in T4 e chiudo sessione
             *  - Invio la risposta al frontend
             */
            EndGame(currentGame, currentGame.GetScore(currentGame.getUserCompileResult()));
            return new EndGameResponseDTO(
                    currentGame.GetScore(currentGame.getRobotCompileResult()),
                    currentGame.GetScore(currentGame.getUserCompileResult()),
                    currentGame.isWinner(), 0);
        } else {
            /*
             * Se l'utente ha vinto la partita
             *  - Gestisco il calcolo e l'aggiornamento dei punti esperienza
             *  - Gestisco notifiche e trofei
             *  - Verifico gli achievement globali sbloccati
             *  - Chiudo la partita in T4 e chiudo sessione
             *  - Invio la risposta al frontend
             */
            int expGained = handleExperiencePoints(currentGame);
            EndGame(currentGame, currentGame.GetScore(currentGame.getUserCompileResult()));
            List<String> achievementsUnlocked = List.of(handleGlobalAchievementUnlocked(Integer.parseInt(currentGame.getPlayerID())));
            return new EndGameResponseDTO(
                    currentGame.GetScore(currentGame.getRobotCompileResult()),
                    currentGame.GetScore(currentGame.getUserCompileResult()),
                    currentGame.isWinner(), expGained, achievementsUnlocked);
        }
    }

    public void EndGame(GameLogic currentGame, int userscore) {
        logger.info("EndGame: Terminazione partita per playerId={}.", currentGame.getPlayerID());
        /*
        *       L'utente ha deciso di terminare la partita o 
        *       la modalità di gioco ha determinato il termine
        *       Salvo la partita 
        *       Distruggo la partita salvata in sessione  
        */
        currentGame.EndRound();
        currentGame.EndGame(userscore);
        destroyGame(currentGame.getPlayerID(), currentGame.getMode());
    }
}
