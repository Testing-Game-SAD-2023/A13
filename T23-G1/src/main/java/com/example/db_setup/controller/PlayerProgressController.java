package com.example.db_setup.controller;

import com.example.db_setup.model.dto.business.ServiceResponse;
import com.example.db_setup.model.dto.exception.ApiErrorDTO;
import com.example.db_setup.model.dto.gamification.*;
import com.example.db_setup.service.PlayerProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import java.util.Set;

@RestController
@CrossOrigin
public class PlayerProgressController {

    private final PlayerProgressService playerProgressService;
    private final Logger logger = LoggerFactory.getLogger(PlayerProgressController.class);

    public PlayerProgressController(PlayerProgressService playerProgressService) {
        this.playerProgressService = playerProgressService;
    }

    @Operation(summary = "Get player progression by player ID",
            description = "Retrieve the overall progression and achievements of a player given their ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Player progression retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = PlayerProgressDTO.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Player or Player progress not found",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class)
                    )
            )
    })
    @GetMapping("/players/{playerId}/progression")
    public ResponseEntity<PlayerProgressDTO> getProgressByPlayerId(
            @Parameter(description = "ID of the player to retrieve progression for")
            @PathVariable("playerId") long playerId
    ) {
        logger.info("[GET /{}] Received request to retrieve PlayerProgress by playerId {}", playerId, playerId);
        PlayerProgressDTO playerProgress = playerProgressService.getProgressByPlayerId(playerId);
        logger.info("[GET /{}] Returned PlayerProgressDTO: {}", playerId, playerProgress);
        return ResponseEntity.ok(playerProgress);
    }


    @Operation(summary = "Get player progress against a specific opponent",
            description = "Retrieve the progress of a player against a specific opponent defined by game mode, class UT, type, and difficulty")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Game progress retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = GameProgressDTO.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Game progress not found",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class)
                    )
            )
    })
    @GetMapping("/players/{playerId}/progression/against/{gameMode}/{classUT}/{opponentType}/{opponentDifficulty}")
    public ResponseEntity<GameProgressDTO> getPlayerProgressAgainstOpponent(
            @Parameter(description = "ID of the player") @PathVariable("playerId") long playerId,
            @Parameter(description = "Game mode of the opponent") @PathVariable("gameMode") GameMode gameMode,
            @Parameter(description = "Class under test") @PathVariable("classUT") String classUT,
            @Parameter(description = "Opponent type") @PathVariable("opponentType") String type,
            @Parameter(description = "Opponent difficulty") @PathVariable("opponentDifficulty") OpponentDifficulty difficulty
    ) {
        GameProgressDTO gameProgress = playerProgressService.getPlayerGameProgressAgainstOpponent(playerId, gameMode, classUT, type, difficulty);
        return ResponseEntity.ok(gameProgress);
    }


    @Operation(summary = "Get player's total experience points",
            description = "Retrieve the current experience points of the player")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Experience points retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Integer.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Player or Player Progress not found",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class)
                    )
            )
    })
    @GetMapping("/players/{playerId}/progression/experience")
    public ResponseEntity<Integer> getPlayerExperience(
            @Parameter(description = "ID of the player") @PathVariable("playerId") long playerId) {
        return ResponseEntity.ok(playerProgressService.getPlayerExperience(playerId));
    }


    @Operation(summary = "Get global achievements of a player",
            description = "Retrieve all unlocked global achievements for a given player")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Global achievements retrieved successfully",
                    content = @Content(schema = @Schema(
                            implementation = Set.class
                    ))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Player or Player Progress not found",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class)
                    )
            )
    })
    @GetMapping("/players/{playerId}/progression/achievements/global")
    public ResponseEntity<Set<String>> getPlayerGlobalAchievements(
            @Parameter(description = "ID of the player") @PathVariable("playerId") long playerId
    ) {
        return ResponseEntity.ok(playerProgressService.getPlayerGlobalAchievements(playerId));
    }


    @Operation(summary = "Create player progress against an opponent",
            description = "Create a new game progress record for a player against a specific opponent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game progress already exists",
                    content = @Content(schema = @Schema(implementation = GameProgressDTO.class))),
            @ApiResponse(responseCode = "201", description = "Game progress created successfully",
                    content = @Content(schema = @Schema(implementation = GameProgressDTO.class))),
            @ApiResponse(responseCode = "404", description = "Player or opponent not found",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class)
                    )
            )
    })
    @PostMapping("/players/{playerId}/progression/against")
    public ResponseEntity<GameProgressDTO> createPlayerProgressAgainstOpponent(
            @PathVariable("playerId") long playerId,
            @Validated @RequestBody CreateGameProgressDTO dto
    ) {
        logger.info("[POST /players/{playerId}/progression/against] Received request");
        ServiceResponse<GameProgressDTO> response = playerProgressService.
                createPlayerGameProgressAgainstOpponent(playerId, dto.getGameMode(), dto.getClassUT(), dto.getType(), dto.getDifficulty());

        HttpStatus status = response.created() ? HttpStatus.CREATED : HttpStatus.OK;
        logger.info("[POST /players/{playerId}/progression/against] response {}", response.entity());
        return ResponseEntity.status(status).body(response.entity());
    }


    @Operation(summary = "Update player experience points",
            description = "Update the experience points of a specific player")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Experience updated successfully",
                    content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "404", description = "Player or Player Progress not found",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class)
                    )
            )
    })
    @PutMapping("/players/{playerId}/progression/experience")
    public ResponseEntity<Integer> updatePlayerExperience(
            @Parameter(description = "ID of the player") @PathVariable("playerId") long playerId,
            @Parameter(description = "Experience DTO containing updated points")
            @RequestBody @Validated ExperienceDTO expDTO) {
        return ResponseEntity.ok(playerProgressService.updatePlayerExperience(playerId, expDTO.getExperiencePoints()));
    }


    @Operation(summary = "Update global achievements of a player",
            description = "Update the set of unlocked global achievements for a player")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Global achievements updated successfully",
                    content = @Content(schema = @Schema(implementation = Set.class))
            ),
            @ApiResponse(responseCode = "404", description = "Player or Player Progress not found",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class)
                    )
            )
    })
    @PutMapping("/players/{playerId}/progression/achievements/global")
    public ResponseEntity<Set<String>> updatePlayerGlobalAchievements(
            @Parameter(description = "ID of the player") @PathVariable("playerId") long playerId,
            @Parameter(description = "Achievements DTO containing unlocked achievements")
            @RequestBody @Validated AchievementsDTO achievementsDTO) {
        logger.info("[PUT /{}/achievements/global] Received request with body {}", playerId, achievementsDTO);
        return ResponseEntity.ok(playerProgressService.updatePlayerGlobalAchievements(playerId, achievementsDTO.getUnlockedAchievements()));
    }


    @Operation(summary = "Update player progress against a specific opponent",
            description = "Update the result and achievements for a player against a specific opponent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game progress updated successfully",
                    content = @Content(schema = @Schema(implementation = UpdateGameProgressDTO.class))),
            @ApiResponse(responseCode = "404", description = "Player Progress not found",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorDTO.class)
                    )
            )
    })
    @PutMapping("/players/{playerId}/progression/against/{gameMode}/{classUT}/{opponentType}/{opponentDifficulty}")
    public ResponseEntity<UpdateGameProgressDTO> updatePlayerProgressAgainstOpponent(
            @Parameter(description = "ID of the player") @PathVariable("playerId") long playerId,
            @Parameter(description = "Game mode of the opponent") @PathVariable("gameMode") GameMode gameMode,
            @Parameter(description = "Class under test") @PathVariable("classUT") String classUT,
            @Parameter(description = "Opponent type") @PathVariable("opponentType") String type,
            @Parameter(description = "Opponent difficulty") @PathVariable("opponentDifficulty") OpponentDifficulty difficulty,
            @Parameter(description = "Updated game progress info") @RequestBody @Validated UpdateGameProgressDTO dto) {

        logger.info("[PUT /{}/{}/{}/{}/{}] Received request with body: {}", playerId, gameMode, classUT, type, difficulty, dto);
        UpdateGameProgressDTO result = playerProgressService.
                updatePlayerGameProgressAgainstOpponent(playerId, gameMode, classUT, type, difficulty, dto);
        return ResponseEntity.ok(result);
    }
}
