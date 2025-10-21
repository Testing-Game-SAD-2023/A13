package com.t4.gamerepo.controller;


import com.t4.gamerepo.model.dto.request.*;
import com.t4.gamerepo.model.dto.response.GameDTO;
import com.t4.gamerepo.model.dto.response.RoundDTO;
import com.t4.gamerepo.model.dto.response.TurnDTO;
import com.t4.gamerepo.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import testrobotchallenge.commons.models.dto.api.ApiErrorBackend;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }


    @Operation(
            summary = "Get a game by its Id",
            description = "Returns a single Game object identified by its unique Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Game found and returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GameDTO.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Game not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorBackend.class))
            ),
    })
    @GetMapping("/{gameId}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable("gameId") Long gameId) {
        GameDTO responseBody = gameService.getGameById(gameId);
        return ResponseEntity.ok(responseBody);
    }


    @Operation(
            summary = "Get all games associated with a player by ID",
            description = "Returns a list of Game objects in which the specified player appears in the 'players' field"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of games returned successfully or empty if there are no games",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GameDTO.class))
                    )
            )
    })
    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<GameDTO>> getAllPlayerGames(
            @Parameter(name = "playerId", description = "Id of the player", required = true)
            @PathVariable("playerId") Long playerId) {
        List<GameDTO> games = gameService.getAllPlayerGames(playerId);
        return ResponseEntity.ok(games);
    }


    @Operation(
            summary = "Get all games",
            description = "Returns a list of all available games"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of games returned successfully or empty if there are no games",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GameDTO.class))
                    )
            )
    })
    @GetMapping("")
    public ResponseEntity<List<GameDTO>> getAllGames() {
        List<GameDTO> games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }


    @Operation(
            summary = "Create a new Game",
            description = "Returns the created Game object",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO containing details to create a new Game",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateGameDTO.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The created Game Object",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GameDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Duplicate player IDs found in the request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class)
                    )
            )
    })
    @PostMapping("")
    public ResponseEntity<GameDTO> createGame(@Validated @RequestBody CreateGameDTO createGameDTO) {
        GameDTO createdGame = gameService.createGame(createGameDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGame);
    }


    @Operation(
            summary = "Create a new Round for the specified Game",
            description = "Returns the created Round object",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO containing details to create a new Round",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateRoundDTO.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The created Round Object",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GameDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Game not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Game is already closed or the previous round is still open",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class)
                    )
            )
    })
    @PostMapping("/{gameId}/rounds")
    public ResponseEntity<RoundDTO> startRound(
            @Parameter(name = "gameId", description = "Id of the game", required = true)
            @PathVariable("gameId") Long gameId,
            @Validated @RequestBody CreateRoundDTO createRoundDTO) {
        RoundDTO newRound = gameService.startRound(gameId, createRoundDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRound);
    }


    @Operation(
            summary = "Create a new Turn for the last Round in the specified Game",
            description = "Returns the TurnDTO of the created Turn",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO containing details to create a new Turn",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateTurnDTO.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The TurnDTO of the created Turn",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TurnDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Game or Round not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Game or Round are already closed or the player is not in the game",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class)
                    )
            )
    })
    @PostMapping("/{gameId}/rounds/last/turns")
    public ResponseEntity<TurnDTO> startTurn(
            @Parameter(name = "gameId", description = "Id of the game", required = true)
            @PathVariable("gameId") Long gameId,
            @Validated @RequestBody CreateTurnDTO turnDTO) {
        TurnDTO newTurn = gameService.startTurn(gameId, turnDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(newTurn);
    }


    @Operation(
            summary = "Close the specified turn in the last turn of the specified Game",
            description = "Returns the updated Turn",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO containing the score of the player in the turn",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CloseTurnDTO.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The updated Turn",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TurnDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Game, Round or Turn not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TurnDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Game is already closed or the player is not in the game",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Game is already closed or the player is not in the game",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class)
                    )
            )
    })
    @PutMapping("/{gameId}/rounds/last/turns/{turnNumber}")
    public ResponseEntity<TurnDTO> closeTurn(
            @Parameter(name = "gameId", description = "Id of the game", required = true)
            @PathVariable("gameId") Long gameId,
            @Parameter(name = "turnNumber", description = "The number of the turn of the last round to close", required = true)
            @PathVariable("turnNumber") int turnNumber,
            @Validated @RequestBody CloseTurnDTO closeTurnDTO) {

        TurnDTO closedTurn = gameService.endTurn(gameId, turnNumber, closeTurnDTO);
        return ResponseEntity.ok(closedTurn);
    }


    @Operation(
            summary = "Close the last round in the specified Game",
            description = "Returns the updated Round"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The updated Round",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RoundDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Game or Round not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Game or Round already closed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class)
                    )
            )
    })
    @PutMapping("/{gameId}/rounds/last")
    public ResponseEntity<RoundDTO> closeRound(
            @Parameter(name = "gameId", description = "Id of the game", required = true)
            @PathVariable(value = "gameId") Long gameId) {
        RoundDTO closedRound = gameService.endRound(gameId);
        return ResponseEntity.ok(closedRound);
    }


    @Operation(
            summary = "Close a Game specified by its Id",
            description = "Returns the updated Game",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO containing the score of the players in the Game and if they have won",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CloseGameDTO.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The updated Game",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GameDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Game not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Game already closed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorBackend.class)
                    )
            )
    })
    @PutMapping("/{gameId}")
    public ResponseEntity<GameDTO> closeGame(
            @Parameter(name = "gameId", description = "Id of the game", required = true)
            @PathVariable(value = "gameId") Long gameId,
            @Validated @RequestBody CloseGameDTO closeGameDTO) {
        GameDTO closedGame = gameService.endGame(gameId, closeGameDTO);
        return ResponseEntity.ok(closedGame);
    }
}
