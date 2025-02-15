package com.g2.Game.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g2.Game.GameModes.Coverage.CompileResult;
import com.g2.Game.GameFactory.GameRegistry;
import com.g2.Game.GameModes.GameLogic;
import com.g2.Game.Service.Exceptions.GameAlreadyExistsException;
import com.g2.Game.Service.Exceptions.GameDontExistsException;
import com.g2.Interfaces.ServiceManager;
import com.g2.Model.AchievementProgress;
import com.g2.Model.DTO.GameResponse;
import com.g2.Model.User;
import com.g2.Service.AchievementService;

@Service
public class GameService {

    private final ServiceManager serviceManager;
    private final GameRegistry gameRegistry;
    private final AchievementService achievementService;
    //Gestisco qui tutti i giochi aperti sostituire con la sessione;
    private final Map<String, GameLogic> activeGames;
    //Logger
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    @Autowired
    public GameService(
            ServiceManager serviceManager,
            GameRegistry gameRegistry,
            AchievementService achievementService
    ) {
        this.serviceManager = serviceManager;
        this.activeGames = new ConcurrentHashMap<>();
        this.gameRegistry = gameRegistry;
        this.achievementService = achievementService;
    }


    /*
    *  Sfrutto T4 per avere i risultati dei robot
     */
    private int GetRobotScore(String testClass, String robot_type, String difficulty) {
        logger.info("getRobotScore: Richiesta punteggio robot per testClass={}, robotType={}, difficulty={}.", testClass, robot_type, difficulty);
        try {
            String response_T4 = (String) serviceManager.handleRequest("T4", "GetRisultati",
                    testClass, robot_type, difficulty);

            JSONObject jsonObject = new JSONObject(response_T4);            
            //anche se scritto al plurale scores è un solo punteggio, cioè quello del robot
            return jsonObject.getInt("scores");
        } catch (JSONException e) {
            logger.error("[GAMECONTROLLER] GetRobotScore:", e);
            return 0;
        }
    }

    /*
     *      quando ci sarà la sessione non dovrò crearla, ma solo recuperarla 
     *      per ora ad ogni iterazione ho bisogno di fornire String underTestClassName, String type_robot, String difficulty
     *      con l'implementazione della sessione, qui non creo e quindi non ne ho bisogno 
     */
    public GameLogic CreateGame(String playerId, String mode,
            String underTestClassName,
            String type_robot,
            String difficulty) throws GameAlreadyExistsException {

        GameLogic gameLogic = activeGames.get(playerId);
        if (gameLogic == null) {
            //Creo la nuova partita
            gameLogic = gameRegistry.createGame(mode, serviceManager, playerId, underTestClassName, type_robot, difficulty);
            activeGames.put(playerId, gameLogic);
            gameLogic.CreateGame();
            logger.info("createGame: Inizio creazione partita per playerId={}, mode={}.", playerId, mode);
            return gameLogic;
        } else {
            logger.error("createGame: Esiste già una partita per il playerId={}.", playerId);
            throw new GameAlreadyExistsException("Esiste già una partita per il giocatore con ID: " + playerId);
        }
    }

    /*
    *     Recupero il Game 
     */
    public GameLogic GetGame(String mode, String playerId) throws GameDontExistsException {
        logger.info("getGame: Recupero partita per playerId={}, mode={}.", playerId, mode);
        GameLogic gameLogic = activeGames.get(playerId);
        if(gameLogic == null){
            logger.error("getGame: Nessuna partita trovata per playerId={}, mode={}.", playerId, mode);
            throw new GameDontExistsException("Non esiste un game per il giocatore con ID: " + playerId + "con modalità: " + mode);
        }
        logger.info("getGame: Partita recuperata con successo per playerId={}.", playerId);
        return gameLogic;
    }

    /*
    * Elimina un game
     */
    public Boolean destroyGame(String playerId) {
        activeGames.remove(playerId);
        logger.info("destroyGame: Distruzione partita per playerId={}.", playerId);
        return true;
    }

    public CompileResult handleCompile(String Classname, String testingClassCode) {
        logger.info("handleCompile: Inizio compilazione per className={}.", Classname);
        return new CompileResult(Classname, testingClassCode, this.serviceManager);
    }

    public GameResponse handleGameLogic(CompileResult compileResult, GameLogic currentGame, Boolean isGameEnd) {
        logger.info("handleGameLogic: Avvio logica di gioco per playerId={}.", currentGame.getPlayerID());
        /*
         *  Lo score è definito dalle performance del file XML del test 
         */
        int userscore = currentGame.GetScore(compileResult);
        int robotScore = GetRobotScore(currentGame.getClasseUT(), currentGame.getType_robot(), currentGame.getDifficulty());
        /*
         *  Avanzo nel gioco 
         */
        currentGame.NextTurn(userscore, robotScore);
        Boolean gameFinished = isGameEnd || currentGame.isGameEnd();
        logger.info("handleGameLogic: Stato partita (gameFinished={}) per playerId={}.", gameFinished, currentGame.getPlayerID());
        if (gameFinished) {
            logger.info("handleGameLogic: Partita terminata per playerId={}. Avvio aggiornamento progressi e notifiche.", currentGame.getPlayerID());
            updateProgressAndNotifications(currentGame.getPlayerID());
            EndGame(currentGame, userscore);
        }
        logger.info("handleGameLogic: Risposta creata per playerId={}.", currentGame.getPlayerID());
        return createResponseRun(compileResult, gameFinished, robotScore, userscore);
    }

    public void EndGame(GameLogic currentGame, int userscore) {
        logger.info("endGame: Terminazione partita per playerId={}.", currentGame.getPlayerID());
        //L'utente ha deciso di terminare o la modalità è arrivata al termine 
        //Salvo la partita  
        currentGame.EndRound();
        currentGame.EndGame(userscore);
        destroyGame(currentGame.getPlayerID());
    }


    /*
     * Utility che crea il DTO 
     */
    public GameResponse createResponseRun(CompileResult compileResult,
            Boolean gameFinished,
            int robotScore,
            int UserScore) {
        logger.info("createResponseRun: Creazione risposta per la partita (gameFinished={}, userScore={}, robotScore={}).", gameFinished, UserScore, robotScore);

        GameResponse response = new GameResponse();
        response.setOutCompile(compileResult.getCompileOutput());
        response.setCoverage(compileResult.getXML_coverage());
        response.setRobotScore(robotScore);
        response.setUserScore(UserScore);
        response.setGameOver(gameFinished);

        // Dettagli della copertura
        GameResponse.CoverageDetails coverageDetails = new GameResponse.CoverageDetails();

        // Aggiungi i dettagli della copertura (Line, Branch, Instruction)
        coverageDetails.setLine(new GameResponse.CoverageDetail(
                compileResult.getLineCoverage().getCovered(),
                compileResult.getLineCoverage().getMissed()
        ));
        coverageDetails.setBranch(new GameResponse.CoverageDetail(
                compileResult.getBranchCoverage().getCovered(),
                compileResult.getBranchCoverage().getMissed()
        ));
        coverageDetails.setInstruction(new GameResponse.CoverageDetail(
                compileResult.getInstructionCoverage().getCovered(),
                compileResult.getInstructionCoverage().getMissed()
        ));

        response.setCoverageDetails(coverageDetails);
        return response;
    }

    //Gestione Notifiche
    @SuppressWarnings("unchecked")
    private void updateProgressAndNotifications(String playerId) {
        List<User> users = (List<User>) serviceManager.handleRequest("T23", "GetUsers");
        Long userId = Long.valueOf(playerId);
        User user = users.stream().filter(u -> Objects.equals(u.getId(), userId)).findFirst().orElse(null);
        String email = user.getEmail();
        List<AchievementProgress> newAchievements = achievementService.updateProgressByPlayer(userId.intValue());
        achievementService.updateNotificationsForAchievements(email, newAchievements);
    }
}
