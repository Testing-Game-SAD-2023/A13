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

package com.g2.game.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.game.gameDTO.EndGameDTO.EndGameResponseDTO;
import com.g2.game.gameDTO.RunGameDTO.RunGameRequestDTO;
import com.g2.game.gameDTO.RunGameDTO.RunGameResponseDTO;
import com.g2.game.gameDTO.StartGameDTO.StartGameRequestDTO;
import com.g2.game.gameDTO.StartGameDTO.StartGameResponseDTO;
import com.g2.game.gameFactory.params.GameParams;
import com.g2.game.gameFactory.params.GameParamsFactory;
import com.g2.game.gameMode.Compile.CompileResult;
import com.g2.game.gameMode.GameLogic;
import com.g2.model.configuration.GameExecutionConfig;
import com.g2.model.dto.GameProgressDTO;
import com.g2.session.SessionService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import testrobotchallenge.commons.models.dto.score.EvosuiteCoverageDTO;
import testrobotchallenge.commons.models.dto.score.JacocoCoverageDTO;
import testrobotchallenge.commons.models.opponent.GameMode;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Orchestratore della logica di gioco e punto di accesso per il controller REST.
 * <p>
 * Questa coordina il flusso di esecuzione delle partite, gestendo le interazioni tra i diversi service impiegati per
 * realizzare il gameEngine.
 */

@Service
public class GameManager {

    private static final String TEST_CLASS_NAME_PREFIX = "Test";
    private static final Logger logger = LoggerFactory.getLogger(GameManager.class);
    private final GameService gameService;
    private final SessionService sessionService;
    private final PlayerStatService playerStatService;
    private final LogWriterService logWriterService;
    @Value("${config.turn.file}")
    private String turnExecutionConfigFile;
    private GameExecutionConfig config;

    public GameManager(GameService gameService, SessionService sessionService,
                       PlayerStatService playerStatService, LogWriterService logWriterService) {
        this.gameService = gameService;
        this.sessionService = sessionService;
        this.playerStatService = playerStatService;
        this.logWriterService = logWriterService;
    }

    @PostConstruct
    public void init() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("%s/%s".formatted(System.getProperty("user.dir"), turnExecutionConfigFile.replace("/", File.separator)));
            this.config = objectMapper.readValue(file, GameExecutionConfig.class);
        } catch (IOException e) {
            logger.info("[PostConstruct init] Error in loading game_config.json, using default values: {}", e.getMessage());
            this.config = new GameExecutionConfig(true);
        }
    }

    /**
     * Gestisce la creazione e l'avvio di una nuova partita.
     * <p>
     * Il metodo esegue le seguenti operazioni:
     * <ul>
     *     <li>Converte la richiesta {@link StartGameRequestDTO} in {@link GameParams} tramite la factory {@link GameParamsFactory};</li>
     *     <li>Crea una nuova partita tramite {@link GameService#createNewGame(GameParams)};</li>
     *     <li>Avvia la partita tramite {@link GameLogic#startGame()} e {@link GameLogic#startRound()};</li>
     *     <li>Registra la sessione di gioco in Redis tramite {@link SessionService#setGameMode(Long, GameLogic)}.</li>
     *     <li>Se non esiste, crea un {@link GameProgressDTO} tramite {@link GameService#createNewGameProgress(GameParams)}
     *          per tracciare vittorie e obiettivi sbloccati dell'utente.</li>
     * </ul>
     *
     * @param requestDTO DTO contenente i dati necessari per creare la nuova partita
     * @return {@link StartGameResponseDTO} contenente l'ID della partita appena creata
     * e lo stato della creazione ("created")
     */
    public StartGameResponseDTO handleStartNewGame(StartGameRequestDTO requestDTO) {
        // Converto il dto
        GameParams gameParams = GameParamsFactory.generateCreateParams(requestDTO);

        // Creo una nuova partita, modellata da GameLogic
        GameLogic gameLogic = gameService.createNewGame(gameParams);

        // Registra la creazione in T4
        gameLogic.startGame();
        gameLogic.startRound();
        logger.info("[START_GAME] Partita creata con successo. GameID={}, mode={}", gameLogic.getGameID(), gameLogic.getGameMode());

        // Creo la sessione di gioco su Redis
        sessionService.setGameMode(gameParams.getPlayerId(), gameLogic);
        logger.info("createGame: sessione aggiornata con successo per playerId={}, mode={}.",
                gameLogic.getPlayerID(), gameLogic.getGameMode());

        /*
         * Creo, se non esiste, il GameProgress che tiene traccia della vittoria e degli obiettivi sbloccati
         * dell'utente per l'avversario (mode, class, type_robot, difficulty)
         */
        GameProgressDTO progress = gameService.createNewGameProgress(gameParams);
        logger.info("createGame: creato/recuperato con successo progress {} per playerId={}.", progress, gameLogic.getPlayerID());

        return new StartGameResponseDTO(gameLogic.getGameID(), "created");
    }


    /**
     * Gestisce l'esecuzione di un singolo turno di gioco per un giocatore.
     * <p>
     * Il metodo esegue le seguenti operazioni:
     * <ul>
     *     <li>Converte il {@link RunGameRequestDTO} in {@link GameParams} tramite {@link GameParamsFactory}.</li>
     *     <li>Recupera la sessione di gioco corrente tramite {@link GameManager#handleGetCurrentGame(long, GameMode)}.</li>
     *     <li>Prepara i codici sorgente della classe da testare (ClassUT) e della classe di test del giocatore,
     *         utilizzando i dati aggiornati o quelli già presenti nella sessione.</li>
     *     <li>Prepara le directory sul VolumeT0 per salvare compilazioni, sorgenti, test e report di copertura.</li>
     *     <li>Richiede la compilazione e la valutazione del test del giocatore tramite {@link #handleCompileAndCoverage}.</li>
     *     <li>Registra i file della compilazione e dei report di copertura tramite {@link LogWriterService}.</li>
     *     <li>Recupera la copertura del codice dell'avversario tramite {@link GameService#getOpponentCoverage}.</li>
     *     <li>Chiude il turno aggiornando la partita e la sessione tramite {@link GameService#closeTurn} e {@link SessionService#updateGameMode}.</li>
     *     <li>Se la compilazione ha avuto successo:
     *         <ul>
     *             <li>Calcola i punteggi per giocatore e avversario.</li>
     *             <li>Verifica e sblocca eventuali achievement tramite {@link PlayerStatService#unlockGameModeAchievements}.</li>
     *             <li>Restituisce un {@link RunGameResponseDTO} completo con esiti e achievement sbloccati.</li>
     *         </ul>
     *     </li>
     *     <li>Se la compilazione fallisce, restituisce un {@link RunGameResponseDTO} parziale senza punteggi né achievement.</li>
     * </ul>
     *
     * @param dto       DTO contenente le informazioni del turno corrente, tra cui playerId, codice della classe da
     *                  testare, codice della classe di test e stato della partita.
     * @param isGameEnd booleano che indica se il turno è anche un submit finale della partita.
     * @return {@link RunGameResponseDTO}       contenente i risultati della compilazione del giocatore e dell'avversario,
     * lo stato di vittoria, i punteggi e gli achievement eventualmente sbloccati.
     */
    public RunGameResponseDTO handlePlayTurn(RunGameRequestDTO dto, boolean isGameEnd) {
        // Converto la richiesta nel modello di parametri previsti per la modalità di gioco
        GameParams updateParams = GameParamsFactory.generateUpdateParams(dto);
        logger.debug("[handlePlayTurn]: updateParams={}", updateParams);

        Long playerId = updateParams.getPlayerId();
        GameMode gameMode = updateParams.getGameMode();
        logger.info("[PlayGame] Inizio esecuzione per playerId={} e gameMode={}", playerId, gameMode);

        // Recupero la sessione corrente della partita
        GameLogic currentGame = handleGetCurrentGame(playerId, gameMode);
        logger.info("[PlayGame] GameLogic recuperato: gameID={}", currentGame.getGameID());

        // Preparo i dati per la richiesta di compilazione
        String classUTCode = updateParams.getClassUTCode().isEmpty() ?
                currentGame.getClassUTCode() :
                updateParams.getClassUTCode();

        String testClassCode =
                updateParams.getTestClassCode().isEmpty() ?
                        currentGame.getTestingClassCode() :
                        updateParams.getTestClassCode();

        String classUTName = currentGame.getClassUTName();
        String testClassName = TEST_CLASS_NAME_PREFIX + classUTName;

        String classUTFileName = classUTName + ".java";
        String testClassFileName = testClassName + ".java";

        // Richiedo la compilazione e la valutazione del test del giocatore
        Pair<JacocoCoverageDTO, EvosuiteCoverageDTO> coverage = handleCompileAndCoverage(
                classUTName, classUTFileName, classUTCode,
                testClassName, testClassFileName, testClassCode, isGameEnd);

        JacocoCoverageDTO responseT7 = coverage.getFirst();
        EvosuiteCoverageDTO responseT8 = coverage.getSecond();

        CompileResult playerCoverageResult = new CompileResult(responseT7, responseT8);

        logger.info("[PlayGame] Esito compilazione: success={}", playerCoverageResult.hasSuccess());
        logger.debug("[PlayGame] Esito EvoSuite: {}", playerCoverageResult.getEvosuiteLine());

        // Recupero la copertura dell'avversario scelto
        CompileResult opponentCoverageResult = gameService.getOpponentCoverage(currentGame);

        // Chiudo il turno e aggiorno la sessione
        gameService.closeTurn(playerCoverageResult, opponentCoverageResult, currentGame, updateParams);
        sessionService.updateGameMode(playerId, currentGame);

        // Preparo il percorso per salvare le compilazioni nel volume T0
        String userDir = String.format("/VolumeT0/FolderTree/StudentTest/Player%s/%s/%s/Game%s/Round%s/Turn%s",
                currentGame.getPlayerID(), currentGame.getGameMode(), currentGame.getClassUTName(),
                currentGame.getGameID(), currentGame.getCurrentRound(), currentGame.getCurrentTurn());
        String userCoverageDir = String.format("%s/coverage", userDir);
        String userSrcDir = String.format("%s/project/src/java/main", userDir);
        String userTestDir = String.format("%s/project/src/test/main", userDir);
        logWriterService.createDirectory(userCoverageDir, userSrcDir, userTestDir);

        // Loggo sul VolumeT0 la compilazione
        logWriterService.writeTurn(classUTCode, classUTFileName, testClassCode, testClassFileName,
                responseT8.getResultFileContent(), responseT7.getCoverage(),
                userSrcDir, userTestDir, userCoverageDir);

        // Se la compilazione ha avuto successo, calcolo i punteggi e verifico gli achievement
        if (playerCoverageResult.hasSuccess()) {
            // La modalità di gioco determina come calcolare il punteggio da confrontare per la vittoria
            int opponentScore = currentGame.getScore(opponentCoverageResult);
            int userScore = currentGame.getScore(playerCoverageResult);

            // Verifico gli achievement sbloccati nella partita corrente
            Set<String> unlockedAchievements = playerStatService.unlockGameModeAchievements(currentGame, playerCoverageResult, opponentCoverageResult);
            logger.info("[PlayGame]: Creazione risposta per la partita (canWin={}, userScore={}, opponentScore={}).", currentGame.isWinner(), userScore, opponentScore);
            logger.debug("EvoSuite restituito: {}", playerCoverageResult.getEvosuiteLine());
            return new RunGameResponseDTO(playerCoverageResult, opponentCoverageResult, currentGame.isWinner(), userScore, opponentScore, unlockedAchievements);
        } else {
            // La compilazione del test è fallita, restituisco solo un risultato parziale
            return new RunGameResponseDTO(
                    playerCoverageResult,
                    null,
                    false,
                    0, 0,
                    new HashSet<>()
            );
        }
    }


    /**
     * Gestisce l'abbandono temporaneo della partita da parte del giocatore.
     * <p>
     * Questo metodo viene invocato quando l'utente chiude il browser, la scheda, o comunque interrompe
     * la sessione di gioco senza terminare la partita. Il frontend invia un beacon, sotto forma di string raw,
     * con i dati disponibili nell'editor da salvare nella sessione Redis.
     * <p>
     * Le informazioni inviate includono:
     * <ul>
     *     <li>Il codice di test scritto dall'utente ({@code testingClassCode});</li>
     *     <li>Eventuali dati specifici della modalità di gioco (es. tempo rimanente, per la modalità "PartitaSingola").</li>
     * </ul>
     *
     * <p>Nello specifico:</p>
     * <ol>
     *     <li>Il metodo riceve la richiesta raw in formato JSON e la deserializza in un {@link RunGameRequestDTO}.</li>
     *     <li>Genera i parametri di aggiornamento tramite {@link GameParamsFactory#generateUpdateParams(RunGameRequestDTO)}.</li>
     *     <li>Recupera la partita corrente ({@link GameLogic}) per il giocatore e la modalità indicata.</li>
     *     <li>Invoca {@link GameService#pauseTurn(CompileResult, CompileResult, GameLogic, GameParams)}
     *          per salvare lo stato attuale della partita.</li>
     *     <li>Aggiorna la sessione su Redis tramite {@link SessionService#updateGameMode(Long, GameLogic)}.</li>
     * </ol>
     *
     * <p>
     *     Il fallimento nell'aggiornamento della sessione, ad esempio per errori di parsing della richiesta raw, non
     *     prevede attualmente notifica al frontend.
     * </p>
     *
     * @param rawRequest stringa JSON contenente la richiesta grezza di aggiornamento della sessione,
     *                   serializzata da {@link RunGameRequestDTO}.
     */
    public void handlePauseGame(String rawRequest) {
        // Provo a parsare la richiesta raw
        RunGameRequestDTO requestDTO;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            requestDTO = objectMapper.readValue(rawRequest, RunGameRequestDTO.class);
        } catch (JsonProcessingException e) {
            logger.error("Errore nel parsing della richiesta di aggiornamento della sessione", e);
            return;
        }

        // Converto la richiesta nel modello di parametri previsti per la modalità di gioco
        GameParams updateParams = GameParamsFactory.generateUpdateParams(requestDTO);
        Long playerId = requestDTO.getPlayerId();
        GameMode gameMode = requestDTO.getGameMode();
        logger.info("[POST /leave] Ricevuta richiesta salvataggio sessione per playerId={}", playerId);

        // Recupero la sessione corrente
        GameLogic currentGame = handleGetCurrentGame(playerId, gameMode);
        logger.info("[LeaveGame] GameLogic recuperato: gameID={}", currentGame.getGameID());

        // Aggiorno la GameLogic e la sessione
        gameService.pauseTurn(currentGame.getUserCompileResult(), currentGame.getRobotCompileResult(), currentGame, updateParams);
        sessionService.updateGameMode(playerId, currentGame);

        logger.info("[LeaveGame] GameLogic aggiornato: gameID={}", currentGame.getGameID());
    }


    /**
     * Gestisce la conclusione della partita in corso.
     * <p>
     * Il metodo esegue le seguenti operazioni:
     * <ul>
     *     <li>Converte il {@link RunGameRequestDTO} in {@link GameParams} tramite {@link GameParamsFactory}.</li>
     *     <li>Esegue l'ultimo turno di gioco se necessario, calcolando la copertura e sbloccando eventuali achievement
     *         tramite {@link #handlePlayTurn(RunGameRequestDTO, boolean)}.</li>
     *     <li>Recupera la sessione di gioco corrente ({@link GameLogic}) per il giocatore e la modalità specificata.</li>
     *     <li>Chiude la partita con {@link #handleCloseGame(GameLogic, boolean)} aggiornando lo stato interno.</li>
     *     <li>Determina la forma della risposta in base al risultato della partita:
     *         <ul>
     *             <li>Se la compilazione del test dell'utente non ha avuto successo,
     *                  restituisce un {@link EndGameResponseDTO} parziale.</li>
     *             <li>Se il giocatore ha perso, restituisce i punteggi ma senza esperienza aggiuntiva.</li>
     *             <li>Se il giocatore ha vinto, calcola i punti esperienza, sblocca eventuali achievement globali e restituisce
     *                 un {@link EndGameResponseDTO} completo con punteggi, stato di vittoria, punti esperienza e achievement sbloccati.</li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * @param requestDTO DTO contenente le informazioni necessarie per terminare la partita.
     * @return {@link EndGameResponseDTO}       contenente i punteggi finali della partita, lo stato di vittoria,
     * eventuali punti esperienza guadagnati e gli achievement sbloccati.
     */
    public EndGameResponseDTO handleEndGame(RunGameRequestDTO requestDTO) {
        GameParams updateParams = GameParamsFactory.generateUpdateParams(requestDTO);

        RunGameResponseDTO runGameResponse = new RunGameResponseDTO();
        Set<String> achievementsUnlocked = new HashSet<>();
        if (!updateParams.getTestClassCode().isEmpty() || config.isExecuteEvosuiteOnlyAtEndGame()) {
            runGameResponse = handlePlayTurn(requestDTO, true);
            achievementsUnlocked.addAll(runGameResponse.getUnlockedAchievements());
        }

        logger.info("[EndGame] Inizio terminazione partita per playerId={} e mode={}",
                updateParams.getPlayerId(), updateParams.getGameMode());

        // Recupero la sessione aggiornata
        GameLogic currentGame = handleGetCurrentGame(updateParams.getPlayerId(), updateParams.getGameMode());
        logger.info("[EndGame] GameLogic recuperato: gameID={}", currentGame.getGameID());

        handleCloseGame(currentGame, false);

        // Determino la forma della risposta in base allo stato di terminazione della partita ed eseguo le operazioni
        // aggiuntive in caso di vittoria
        if (currentGame.getUserCompileResult() == null ||
                !currentGame.getUserCompileResult().hasSuccess()
        ) {
            return new EndGameResponseDTO(0, 0, false, 0, runGameResponse);
        } else if (!currentGame.isWinner()) {

            return new EndGameResponseDTO(
                    currentGame.getScore(currentGame.getRobotCompileResult()),
                    currentGame.getScore(currentGame.getUserCompileResult()),
                    currentGame.isWinner(), 0, achievementsUnlocked, runGameResponse);
        } else {
            // Gestisco il calcolo e l'aggiornamento dei punti esperienza e degli achievement sbloccati
            int expGained = playerStatService.assignExperiencePoints(currentGame);
            achievementsUnlocked.addAll(playerStatService.unlockGlobalAchievements(currentGame.getPlayerID()));
            return new EndGameResponseDTO(
                    currentGame.getScore(currentGame.getRobotCompileResult()),
                    currentGame.getScore(currentGame.getUserCompileResult()),
                    currentGame.isWinner(), expGained, achievementsUnlocked, runGameResponse);
        }
    }


    /**
     * Gestisce la resa del giocatore di una partita in corso.
     * <p>
     * Il metodo recupera la sessione di gioco corrente per il giocatore e la modalità specificata
     * e chiude la partita segnandola come resa tramite {@link #handleCloseGame(GameLogic, boolean)}.
     *
     * @param playerId ID del giocatore che si arrende.
     * @param gameMode Modalità di gioco della partita da terminare.
     */
    public void handleSurrendGame(long playerId, GameMode gameMode) {
        // Recupero la sessione aggiornata
        GameLogic currentGame = handleGetCurrentGame(playerId, gameMode);
        logger.info("[EndGame] GameLogic recuperato: gameID={}", currentGame.getGameID());

        handleCloseGame(currentGame, true);
    }


    private GameLogic handleGetCurrentGame(long playerId, GameMode gameMode) {
        logger.info("getGame: Recupero partita per playerId={}, gameMode={}.", playerId, gameMode);
        GameLogic currentGame = sessionService.getGameMode(playerId, gameMode);
        gameService.addServiceManager(currentGame);
        logger.info("getGame: Partita recuperata con successo per playerId={} e modalità={}.", playerId, gameMode);
        return currentGame;
    }

    /**
     * Gestisce la compilazione e la valutazione della copertura del codice per il turno corrente.
     * <p>
     * Il metodo esegue due principali operazioni:
     * <ul>
     *     <li>Richiede la compilazione e valutazione della classe di test tramite il servizio T7
     *         (JaCoCo), ottenendo un {@link JacocoCoverageDTO}.</li>
     *     <li>Se la configurazione lo consente, oppure se il turno rappresenta la fine della partita,
     *         calcola anche la copertura tramite EvoSuite (servizio T8), ottenendo un {@link EvosuiteCoverageDTO}.</li>
     * </ul>
     * <p>
     *     La richiesta di copertura a EvoSuite viene effettuata solo se la compilazione in T7 ha avuto successo.
     * </p>
     *
     * @param classUTName       il nome della classe UT (es. "FTPFile")
     * @param classUTFileName   il nome del file sorgente della classe sotto test (es. "FTPFile.java").
     * @param classUTCode       il codice sorgente della classe sotto test.
     * @param testClassName     il nome della classe di test del giocatore.
     * @param testClassFileName il nome del file sorgente della classe di test del giocatore (es. "TestFTPFie.java").
     * @param testClassCode     il codice sorgente della classe di test del giocatore.
     * @param isGameEnd         il booleano che indica se il turno rappresenta la fine della partita (compilazione finale).
     *                          In tal caso viene sempre eseguita anche la copertura EvoSuite.
     * @return {@link Pair} contenente:
     * <ul>
     *     <li>Il {@link JacocoCoverageDTO} con il risultato della compilazione e della copertura JaCoCo.</li>
     *     <li>Il {@link EvosuiteCoverageDTO} con il risultato della copertura EvoSuite (eventualmente vuoto).</li>
     * </ul>
     */
    private Pair<JacocoCoverageDTO, EvosuiteCoverageDTO> handleCompileAndCoverage(
            String classUTName, String classUTFileName, String classUTCode,
            String testClassName, String testClassFileName, String testClassCode,
            boolean isGameEnd) {
        JacocoCoverageDTO responseT7;
        EvosuiteCoverageDTO responseT8 = new EvosuiteCoverageDTO();

        // Richiedo la compilazione e valutazione a T7, T7 determina il successo della compilazione
        responseT7 = gameService.compilePlayerTest(
                classUTFileName, classUTCode, testClassFileName, testClassCode);

        /*
         * A causa della lentezza/pesantezza del calcolo delle metriche con EvoSuite, e conveniente che T8 venga interrogato solo
         * quando necessario. Attualmente, le modalità di gioco disponibile che prevedono un concetto di vittoria ("PartitaSingola")
         * valutano quest'ultima solo sulle metriche restituite JaCoCo, mentre EvoSuite viene limitato allo sblocco di alcuni
         * achievement.
         * Di conseguenza, il sistema attualmente esegue EvoSuite solo al termine della partita, quando il giocatore
         * decide di consegnare il test scritto ed essere valutato definitivamente su quest'ultimo, piuttosto che durante
         * ogni turno. Questa "pipeline" è attualmente definita tramite:
         *  - il file di configurazione turn_execution_config.json con la proprietà "compute_evosuite_metrics_only_at_end_game"
         *    settata a true, per skippare EvoSuite durante l'esecuzione del turno
         *  - il parametro isGameEnd che a true forza l'esecuzione di EvoSuite, richiedendo le metriche complete per il
         *    termine del gioco
         *
         * In ogni caso, l'esecuzione di EvoSuite è dipendente dal successo di compilazione del test utente in T7 ->
         * non ha senso sprecare tempo e risorse se il test non compila
         */
        logger.debug("Evosuite eseguito solo a END GAME: {} ; è l'END GAME: {} ; T7 ha avuto successo: {}",
                config.isExecuteEvosuiteOnlyAtEndGame(), isGameEnd, responseT7.getCoverage() != null);
        if ((!config.isExecuteEvosuiteOnlyAtEndGame() || isGameEnd) && responseT7.getCoverage() != null)
            responseT8 = gameService.computeEvosuiteCoverage(
                    classUTName, classUTCode, testClassName, testClassCode);

        return Pair.of(responseT7, responseT8);
    }


    /**
     * Chiude la partita corrente e rimuove la sessione associata.
     * <p>
     * La partita viene chiusa nel servizio T4 e la sessione su Redis viene rimossa. Se {@code isGameSurrendered} è
     * {@code true}, la partita viene segnata come resa dal giocatore.
     *
     * @param currentGame       l'istanza di {@link GameLogic} della partita corrente.
     * @param isGameSurrendered {@code true} se la partita è stata chiusa per resa del giocatore,
     *                          {@code false} altrimenti.
     */
    private void handleCloseGame(GameLogic currentGame, boolean isGameSurrendered) {
        // Chiudo la partita i T4 e rimuovo la sessione
        gameService.closeGame(currentGame, isGameSurrendered);
        sessionService.removeGameMode(currentGame.getPlayerID(), currentGame.getGameMode(), java.util.Optional.empty());
    }

}
