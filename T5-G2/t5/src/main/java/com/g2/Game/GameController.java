/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
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
package com.g2.Game;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.Game.GameDTO.EndGameDTO.EndGameResponseDTO;
import com.g2.Game.GameDTO.RunGameDTO.RunGameRequestDTO;
import com.g2.Game.GameFactory.params.GameParams;
import com.g2.Game.GameFactory.params.GameParamsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.g2.Game.GameDTO.RunGameDTO.RunGameResponseDTO;
import com.g2.Game.GameDTO.StartGameDTO.StartGameRequestDTO;
import com.g2.Game.GameDTO.StartGameDTO.StartGameResponseDTO;
import com.g2.Game.GameModes.GameLogic;
import com.g2.Game.Service.GameServiceManager;
import com.g2.Session.Exceptions.GameModeAlreadyExist;
import com.g2.Session.Exceptions.GameModeDontExist;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

//Qui introduco tutte le chiamate REST per la logica di gioco/editor
@CrossOrigin
@RestController
public class GameController {
    /*
     * Interfaccia per gestire gli endpoint
     */
    private final GameServiceManager gameServiceManager;
    /*
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    public GameController(GameServiceManager gameServiceManager) {
        this.gameServiceManager = gameServiceManager;
    }

    /*
     *  Chiamata che controllo se la partita quindi esisteva già o meno
     *  se non esiste instanzia un nuovo gioco 
     */
    @PostMapping("/StartGame")
    public ResponseEntity<StartGameResponseDTO> startGame(
            @RequestBody(required = false) StartGameRequestDTO request,
            @CookieValue(name = "jwt", required = false) String jwt) {

        logger.info("[START_GAME] Richiesta ricevuta per avviare il gioco");

        if (jwt == null || jwt.isEmpty()) {
            logger.error("[START_GAME] Nessun JWT trovato nella richiesta.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StartGameResponseDTO(-1, "JWT missing"));
        }

        if (request == null) {
            logger.error("[START_GAME] Il body della richiesta è NULL!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StartGameResponseDTO(-1, "Request body is missing"));
        }

        // Log dei parametri inviati
        logger.info("[START_GAME] Dati ricevuti: playerId={}, typeRobot={}, difficulty={}, mode={}, underTestClassName={}",
                request.getPlayerId(), request.getTypeRobot(), request.getDifficulty(),
                request.getMode(), request.getUnderTestClassName());

        try {
            GameParams gameParams = GameParamsFactory.createGameParams(request);
            GameLogic game = gameServiceManager.CreateGameLogic(gameParams);

            logger.info("[START_GAME] Partita creata con successo. GameID={}, mode={}", game.getGameID(), game.getMode());
            return ResponseEntity.ok(
                    new StartGameResponseDTO(game.getGameID(),
                            "created")
            );
        } catch (GameModeAlreadyExist e) {
            logger.error("[GAMECONTROLLER][StartGame] " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new StartGameResponseDTO(-1, "GameAlreadyExistsException"));
        }
        /*
        catch (Exception e) {
            logger.error("[GAMECONTROLLER][StartGame] Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StartGameResponseDTO(-1, "Internal Server Error"));
        }

         */
    }

    /*
     * Handler eccezione campi non validi 
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        /*
         * Ottengo gli errori per ogni binding di un json e popolo la mappa così posso poi inviarla 
         */
        ex.getBindingResult().getFieldErrors().forEach(error
                -> errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /*
     * Handler eccezione runtime
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();

        // Recupera il messaggio dell'errore
        errorResponse.put("error", ex.getMessage());

        // Recupera la riga più importante dello stacktrace
        StackTraceElement relevantStackTrace = ex.getStackTrace()[0];
        errorResponse.put("cause", relevantStackTrace.toString());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Map<String, String>> handleRestClientException(RestClientException ex) {
        Map<String, String> errorResponse = new HashMap<>();

        errorResponse.put("error", ex.getMessage());

        StackTraceElement relevantStackTrace = ex.getStackTrace()[0];
        errorResponse.put("cause", relevantStackTrace.toString());

        int httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

        if (ex.getCause() instanceof HttpStatusCodeException cause) {
            httpStatusCode = cause.getStatusCode().value();
        }

        return ResponseEntity.status(httpStatusCode).body(errorResponse);
    }


    /*
     *  Chiamata principale del game engine, l'utente ogni volta può comunicare la sua richiesta di
     *  calcolare la coverage/compilazione, il campo isGameEnd è da utilizzato per indicare se è anche un submit e
     *  quindi vuole terminare la partita ed ottenere i risultati del robot
     */
    @PostMapping(value = "/run", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RunGameResponseDTO> Runner(@RequestBody RunGameRequestDTO request) {
        try {
            GameParams updateGameParams = GameParamsFactory.updateGameParams(request);
            String playerId = request.getPlayerId();
            String mode = request.getMode();
            RunGameResponseDTO response = gameServiceManager.PlayGame(playerId, mode, updateGameParams, "T7");
            return ResponseEntity.ok().body(response);
        } catch (GameModeDontExist e) {
            /*
             * Il player non ha impostato una partita prima di arrivare all'editor
             */
            logger.error("[GAMECONTROLLER][run] " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /*
     *  Chiamata che gestisce l'abbandono della partita in corso da parte dell'utente (es: chiusura browser/scheda, ...),
     *  salva i dati generici e specifici della partita disponibili in quel momento.
     *
     *  Dati generici: testingClassCode (codice di test scritto dall'utente)
     *  Dati specifici:
     *      - PartitaSingola: remainingTime (tempo rimanente alla conclusione della partita)
     */
    @PostMapping(value = "/leave", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<RunGameResponseDTO> UpdateSession(@RequestBody String rawRequest) {
        try {
            logger.info("/leave request: {}", rawRequest);
            ObjectMapper objectMapper = new ObjectMapper();
            RunGameRequestDTO request = objectMapper.readValue(rawRequest, RunGameRequestDTO.class);

            GameParams updateGameParams = GameParamsFactory.updateGameParams(request);
            String playerId = request.getPlayerId();
            String mode = request.getMode();
            logger.info("[POST /leave] Ricevuta richiesta salvataggio sessione per playerId={}", playerId);
            gameServiceManager.LeaveGame(playerId, mode, updateGameParams);
            return ResponseEntity.ok().body(null);
        } catch (GameModeDontExist e) {
            /*
             * Il player non ha impostato una partita prima di arrivare all'editor
             */
            logger.error("[GAMECONTROLLER][leave] " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value="/EndGame", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EndGameResponseDTO> EndGame(@RequestBody RunGameRequestDTO request) {
        try {
            GameParams updateGameParams = GameParamsFactory.updateGameParams(request);
            String playerId = request.getPlayerId();
            String mode = request.getMode();

            if (!request.getTestingClassCode().isEmpty())
                gameServiceManager.PlayGame(playerId, mode, updateGameParams, "All");

            logger.info("[POST /EndGame] Ricevuta richiesta terminazione partita per playerId={}", playerId);
            EndGameResponseDTO response = gameServiceManager.EndGame(playerId, mode, false);
            logger.info("[POST /EndGame] Invio risposta per playerId={}", playerId);
            return ResponseEntity.ok().body(response);
        } catch (GameModeDontExist e) {
            /*
             * Il player non ha impostato una partita prima di arrivare all'editor
             */
            logger.error("[GAMECONTROLLER][EndGame] " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping(value="/CompileEvosuite", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RunGameResponseDTO> CompileEvosuiteBeforeEnding(@RequestBody RunGameRequestDTO request) {
        try {
            GameParams updateGameParams = GameParamsFactory.updateGameParams(request);
            String playerId = request.getPlayerId();
            String mode = request.getMode();
            RunGameResponseDTO response = gameServiceManager.PlayGame(playerId, mode, updateGameParams, "T8");
            return ResponseEntity.ok().body(response);
        } catch (GameModeDontExist e) {
            /*
             * Il player non ha impostato una partita prima di arrivare all'editor
             */
            logger.error("[GAMECONTROLLER][run] " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping(value = "/SurrenderGame/{playerId}")
    public ResponseEntity<EndGameResponseDTO> SurrenderGame(@PathVariable String playerId, @RequestParam String mode) {
        try {
            EndGameResponseDTO response = gameServiceManager.EndGame(playerId, mode, true);
            logger.error("[DELETE /SurrenderGame] Invio risposta per playerId={}: {} ", playerId, response);
            return ResponseEntity.ok().body(response);
        } catch (GameModeDontExist e) {
            /*
             * Il player non ha impostato una partita prima di arrivare all'editor
             */
            logger.error("[GAMECONTROLLER][AbandonGame] " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
