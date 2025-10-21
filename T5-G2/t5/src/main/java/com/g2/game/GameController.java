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
package com.g2.game;

import com.g2.game.gameDTO.EndGameDTO.EndGameResponseDTO;
import com.g2.game.gameDTO.RunGameDTO.RunGameRequestDTO;
import com.g2.game.gameDTO.RunGameDTO.RunGameResponseDTO;
import com.g2.game.gameDTO.StartGameDTO.StartGameRequestDTO;
import com.g2.game.gameDTO.StartGameDTO.StartGameResponseDTO;
import com.g2.game.service.GameManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testrobotchallenge.commons.models.dto.api.ApiErrorBackend;
import testrobotchallenge.commons.models.opponent.GameMode;

import java.util.Map;

//Qui introduco tutte le chiamate REST per la logica di gioco/editor
@CrossOrigin
@RestController
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final GameManager gameManager;

    @Autowired
    public GameController(GameManager gameManager) {
        this.gameManager = gameManager;
    }


    @Operation(
            summary = "Start a new game",
            description = """
                    Verify that a game with the same game mode doesn't exist already, them creates and starts a new game
                    for the given player and configuration parameters."""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Game successfully created and started",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StartGameResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "A game with the same game mode for the same player already exists and is still opened",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorBackend.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "The request is invalid or is incomplete",
                    content = @Content(schema = @Schema(implementation = Map.class))
            )
    })
    @PostMapping("/StartGame")
    public ResponseEntity<StartGameResponseDTO> startGame(
            @Parameter(description = "Request containing the player ID, opponent information, game mode, and additional game details.",
                    required = true)
            @Valid @RequestBody StartGameRequestDTO requestDTO
    ) {
        logger.info("[START_GAME] Richiesta ricevuta per avviare un nuovo gioco");

        // Log dei parametri inviati
        logger.info("[START_GAME] Dati ricevuti: {}", requestDTO);

        StartGameResponseDTO response = gameManager.handleStartNewGame(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(
            summary = "Start, execute and close a turn",
            description = """
                    Main entry point of the game engine.
                    This endpoint executes a single turn within an ongoing game.
                    The player’s test code is compiled and evaluated, the session state is updated, and achievements are checked and unlocked if applicable.
                    The endpoint returns the evaluation results to the player.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = """
                            The request is successfully processed. Returns the evaluation (aka the turn result) to the player.
                            ATTENTION: A failure in the player's test compilation or evaluation (e.g., the test doesn't compile
                            for code errors) is still considered a successful handling of the request.
                            """,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RunGameResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The game mode does not exist. The player attempted to play without starting a game first.",
                    content = @Content(schema = @Schema(implementation = ApiErrorBackend.class))
            )
    })
    @PostMapping(value = "/run", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RunGameResponseDTO> runGame(
            @Parameter(
                    description = "Request containing game information and player's test code.",
                    required = true
            )
            @Valid @RequestBody RunGameRequestDTO requestDTO) {
        RunGameResponseDTO response = gameManager.handlePlayTurn(requestDTO, false);

        return ResponseEntity.ok().body(response);
    }


    @Operation(
            summary = "Leave or pause the current game session",
            description = """
                    Handles the user's decision to leave an ongoing game (e.g., closing the browser or tab).
                    The endpoint saves the currently available common and specific game data.
                    
                    Common data for all game modes:
                    - `testingClassCode`: the test code available in that moment in the player editor
                    
                    Specific data for single-player games:
                    - `remainingTime`: the time left before the match ends.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Game state successfully saved or paused."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The game mode does not exist. The player attempted to pause a game without an active game session.",
                    content = @Content(schema = @Schema(implementation = ApiErrorBackend.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The game mode does not exist. The player attempted to pause a game without an active game session.",
                    content = @Content(schema = @Schema(implementation = ApiErrorBackend.class))
            )
    })
    @PostMapping(value = "/leave", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<RunGameResponseDTO> pauseGame(@RequestBody String rawRequest) {
        logger.info("/leave request: {}", rawRequest);

        gameManager.handlePauseGame(rawRequest);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "End the game",
            description = """
                    Handles the termination of an ongoing game.
                    The endpoint finalizes the game, evaluates the player's last turn, updates the session state,
                    checks for unlocked achievements, and returns the final results.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Game successfully ended. Returns the final game results.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EndGameResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The game mode does not exist. The player attempted to end a game without starting one.",
                    content = @Content(schema = @Schema(implementation = ApiErrorBackend.class))
            )
    })
    @PostMapping(value = "/EndGame", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EndGameResponseDTO> endGame(
            @Parameter(
                    description = "Request containing player ID, opponent info, game mode, and current game state.",
                    required = true
            )
            @Valid @RequestBody RunGameRequestDTO request
    ) {
        logger.info("[POST /EndGame] Ricevuta richiesta terminazione partita per playerId={}", request.getPlayerId());
        EndGameResponseDTO response = gameManager.handleEndGame(request);
        logger.info("[POST /EndGame] Invio risposta per playerId={}", request.getPlayerId());
        return ResponseEntity.ok().body(response);
    }


    @Operation(
            summary = "Surrender an ongoing game",
            description = """
                    Allows the player to surrender the current game session.
                    The game is finalized as surrendered and closed accordingly.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Game successfully surrendered and closed.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "The game mode does not exist. The player attempted to surrender a game that was not started.",
                    content = @Content(schema = @Schema(implementation = ApiErrorBackend.class))
            )
    })
    @DeleteMapping(value = "/SurrenderGame/{playerId}")
    public ResponseEntity<EndGameResponseDTO> surrendGame(
            @Parameter(description = "ID of the player surrendering the game", required = true)
            @PathVariable Long playerId,
            @Parameter(description = "Game mode of the ongoing game", required = true)
            @RequestParam GameMode mode
    ) {
        gameManager.handleSurrendGame(playerId, mode);
        logger.info("[DELETE /SurrenderGame] Invio risposta per playerId={}", playerId);
        return ResponseEntity.ok().build();
    }
}
