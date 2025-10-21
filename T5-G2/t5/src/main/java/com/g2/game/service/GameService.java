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

import com.g2.game.gameFactory.GameRegistry;
import com.g2.game.gameFactory.params.GameParams;
import com.g2.game.gameMode.Compile.CompileResult;
import com.g2.game.gameMode.GameLogic;
import com.g2.interfaces.ServiceManager;
import com.g2.model.dto.GameProgressDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testrobotchallenge.commons.models.dto.score.EvosuiteCoverageDTO;
import testrobotchallenge.commons.models.dto.score.JacocoCoverageDTO;
import testrobotchallenge.commons.models.opponent.GameMode;

/**
 * Service impiegato per la gestione della logica di gioco lato backend.
 * <p>
 * La classe incapsula tutte le operazioni necessarie per la creazione,
 * il recupero, la gestione e la chiusura delle partite. Si interfaccia con i microservizi
 * specializzati (T7, T8, T4) tramite il {@link ServiceManager}, delegando le operazioni
 * di compilazione, calcolo della coverage, aggiornamento del progresso e gestione delle
 * sessioni di gioco.
 */

@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    private final ServiceManager serviceManager;
    private final GameRegistry gameRegistry;

    @Autowired
    public GameService(ServiceManager serviceManager,
                       GameRegistry gameRegistry
    ) {
        this.serviceManager = serviceManager;
        this.gameRegistry = gameRegistry;
    }


    /**
     * Crea una nuova istanza di gioco in base ai parametri forniti. L'istanza viene creata nella sessione e in T4.
     * <p>
     * Utilizza il {@link GameRegistry} per istanziare dinamicamente l'oggetto {@link GameLogic}
     * corretto in base alla modalità di gioco ({@link GameMode}). L'oggetto creato gestirà internamente
     * la logica e lo stato della partita.
     *
     * @param gameParams i parametri di configurazione della partita
     * @return un'istanza di {@link GameLogic} inizializzata per la nuova partita.
     */
    public GameLogic createNewGame(GameParams gameParams) {
        Long playerId = gameParams.getPlayerId();
        GameMode mode = gameParams.getGameMode();

        /*
         * gameRegistry istanzia dinamicamente uno degli oggetti gameLogic (sfida, allenamento, scalata, ecc)
         * basta passargli il campo mode e dinamicamente se ne occupa lui
         */
        GameLogic gameLogic = gameRegistry.createGame(serviceManager, gameParams);
        logger.info("createGame: oggetto game creato con successo per playerId={}, mode={}.", playerId, mode);

        return gameLogic;
    }


    /**
     * Crea (se non esiste) o recupera il {@link GameProgressDTO} del giocatore nei confronti
     * dell'avversario specificato.
     * <p>
     * Il progresso tiene traccia della vittoria e degli achievement sbloccati dal giocatore contro l'avversario.
     * L'avversario è identificato dalla combinazione unica di:
     * <code>gameMode</code>, <code>classUT</code>, <code>opponentType</code> e <code>opponentDifficulty</code>.
     *
     * @param gameParams i parametri della partita necessari per identificare il progresso.
     * @return un {@link GameProgressDTO} rappresentante il progresso dell’utente contro l’avversario.
     */
    public GameProgressDTO createNewGameProgress(GameParams gameParams) {
        return (GameProgressDTO) serviceManager.handleRequest("T23", "createPlayerProgressAgainstOpponent",
                gameParams.getPlayerId(), gameParams.getGameMode(), gameParams.getClassUTName(), gameParams.getOpponentType(), gameParams.getOpponentDifficulty());
    }


    /**
     * Associa il {@link ServiceManager} corrente a un'istanza esistente di {@link GameLogic}.
     * <p>
     * Questo metodo viene utilizzato per reiniettare il {@link ServiceManager} nella {@link GameLogic} recuperata dalla
     * sessione, in quanto non serializzato.
     *
     * @param currentGame l'istanza di {@link GameLogic} a cui associare il {@link ServiceManager}.
     */

    public void addServiceManager(GameLogic currentGame) {
        currentGame.setServiceManager(serviceManager);
    }


    /**
     * Richiede la compilazione e il calcolo delle metriche di JaCoCo per il test fornito dal giocatore.
     * <p>
     * Il metodo invoca il microservizio T7.
     *
     * @param classUTFileName   il nome del file contenente la classe sotto test.
     * @param classUTCode       il codice sorgente della classe sotto test.
     * @param testClassFileName il nome del file della classe di test.
     * @param testClassCode     il codice sorgente della classe di test del giocatore.
     * @return un {@link JacocoCoverageDTO} contenente i risultati della compilazione e della copertura.
     */
    public JacocoCoverageDTO compilePlayerTest(String classUTFileName, String classUTCode, String testClassFileName, String testClassCode) {
        logger.info("Inizio compilazione per ClassName={}.", classUTFileName);

        return this.serviceManager.handleRequest("T7", "CompileCoverage", JacocoCoverageDTO.class,
                testClassFileName, testClassCode, classUTFileName, classUTCode);
    }


    /**
     * Richiede la compilazione e il calcolo delle metriche di EvoSuite per il test fornito dal giocatore.
     * <p>
     * Il metodo invoca il microservizio T8.
     *
     * @param classUTFileName   il nome del file contenente la classe sotto test.
     * @param classUTCode       il codice sorgente della classe sotto test.
     * @param testClassFileName il nome del file della classe di test.
     * @param testClassCode     il codice sorgente della classe di test del giocatore.
     * @return un {@link EvosuiteCoverageDTO} contenente i risultati della compilazione e della copertura.
     */
    public EvosuiteCoverageDTO computeEvosuiteCoverage(String classUTFileName, String classUTCode, String testClassFileName, String testClassCode) {
        logger.info("Inizio compilazione per ClassName={}.", classUTFileName);

        return this.serviceManager.handleRequest("T8", "evosuiteUserCoverage", EvosuiteCoverageDTO.class,
                testClassFileName, testClassCode, classUTFileName, classUTCode, "");
    }


    /**
     * Recupera dalla sessione o richiede a T4 la copertura dell'avversario per la classe corrente che il giocatore sta
     * affrontando.
     * <p>
     * Se la copertura dell'avversario è già disponibile in {@link GameLogic}, viene riutilizzata.
     * In caso contrario, ne viene calcolata una nuova tramite {@link CompileResult}.
     *
     * @param currentGame partita corrente.
     * @return un {@link CompileResult} contenente le informazioni di compilazione e copertura del robot.
     */
    public CompileResult getOpponentCoverage(GameLogic currentGame) {
        try {
            logger.info("Richiesta Coverage robot per testClass={}, robotType={}, difficulty={}.",
                    currentGame.getClassUTName(),
                    currentGame.getTypeRobot(),
                    currentGame.getDifficulty()
            );
            return currentGame.getRobotCompileResult() != null ?
                    currentGame.getRobotCompileResult() :
                    new CompileResult(currentGame, serviceManager,
                            currentGame.getClassUTName(),
                            currentGame.getTypeRobot(),
                            currentGame.getDifficulty()
                    );
        } catch (Exception e) {
            logger.error("[GAMECONTROLLER] GetRobotCoverage:", e);
            return null;
        }
    }


    /**
     * Mette in pausa la partita corrente, aggiornando la sessione corrente.
     * <p>
     * Questo metodo viene utilizzato, ad esempio, quando il giocatore lascia la partita senza averla terminata.
     *
     * @param userCompileResult  il risultato della compilazione del test del giocatore.
     * @param robotCompileResult il risultato della compilazione del test del robot.
     * @param currentGame        l'istanza della partita corrente.
     * @param updateParams       i parametri aggiornati da salvare nella sessione.
     */
    public void pauseTurn(CompileResult userCompileResult, CompileResult robotCompileResult, GameLogic currentGame, GameParams updateParams) {
        logger.info("handleGameLogic: Avvio logica di gioco per playerId={}.", currentGame.getPlayerID());
        currentGame.updateState(updateParams, userCompileResult, robotCompileResult);
    }


    /**
     * Apre, aggiorna e chiude un nuovo turno in T4 e nella logica interna. La gestione del numero di turni è affidata a
     * {@link GameLogic}
     *
     * @param userCompileResult  il risultato della compilazione del test del giocatore.
     * @param robotCompileResult il risultato raggiunto dall'avversario.
     * @param currentGame        l'istanza corrente della partita.
     * @param updateParams       i parametri aggiornati del turno in corso da salvare nella sessione.
     */
    public void closeTurn(CompileResult userCompileResult, CompileResult robotCompileResult, GameLogic currentGame, GameParams updateParams) {
        logger.info("handleGameLogic: Avvio logica di gioco per playerId={}.", currentGame.getPlayerID());
        currentGame.nextTurn(userCompileResult, robotCompileResult);
        currentGame.updateState(updateParams, userCompileResult, robotCompileResult);
    }


    /**
     * Chiude la partita per il giocatore specificato.
     * <p>
     * Questo metodo viene invocato sia in caso di termine naturale della partita che in caso di resa.
     * Esegue la chiusura del round, l'aggiornamento e la chiusura della partita su T4.
     * Rimuove infine la sessione associata.
     *
     * @param currentGame       l'istanza di {@link GameLogic} della partita da chiudere.
     * @param isGameSurrendered {@code true} se la chiusura è dovuta a resa del giocatore,
     *                          {@code false} se la partita è terminata normalmente.
     */
    public void closeGame(GameLogic currentGame, boolean isGameSurrendered) {
        logger.info("EndGame: Terminazione partita per playerId={}.", currentGame.getPlayerID());
        /*
         *       L'utente ha deciso di terminare la partita o
         *       la modalità di gioco ha determinato il termine
         *       Salvo la partita
         *       Distruggo la partita salvata in sessione
         */
        currentGame.endRound();
        logger.info("EndGame: Inizio processo chiusura partita per playerId={}.", currentGame.getPlayerID());
        logger.info("Current game = {}", currentGame);
        currentGame.endGame(isGameSurrendered);
        logger.info("EndGame: Partita rimossa con successo per playerId={}.", currentGame.getPlayerID());
    }
}
