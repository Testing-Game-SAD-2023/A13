package com.example.db_setup.controller;

import com.example.db_setup.model.dto.gamification.OpponentDTO;
import com.example.db_setup.service.OpponentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/opponents")
public class OpponentController {

    private final OpponentService opponentService;
    private final Logger logger = LoggerFactory.getLogger(OpponentController.class);

    public OpponentController(OpponentService opponentService) {
        this.opponentService = opponentService;
    }

    @Operation(
            summary = "Add a new opponent to track player's achievements",
            description = "Add a new opponent with the provided game mode, classUT, type, and difficulty."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Opponent successfully created",
                    content = @Content(schema = @Schema(implementation = OpponentDTO.class)))
    })
    @PostMapping("")
    public ResponseEntity<OpponentDTO> addNewOpponent(@RequestBody @Validated OpponentDTO opponentDTO) {
        logger.info("[POST /] Received request to add new opponent with requestBody {}", opponentDTO);
        return ResponseEntity.ok(opponentService.addNewOpponent(opponentDTO.getGameMode(), opponentDTO.getClassUT(),
                opponentDTO.getType(), opponentDTO.getDifficulty()));
    }

    @Operation(
            summary = "Disables all opponents for a given classUT",
            description = "Disables every opponent associated with the specified classUT."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The number of successfully disabled opponents",
                    content = @Content(schema = @Schema(implementation = Integer.class))),
    })
    @DeleteMapping("/{classUT}")
    public ResponseEntity<Integer> deleteAllOpponentsForClassUT(@PathVariable("classUT") String classUT) {
        return ResponseEntity.ok(opponentService.deleteAllOpponentsForClassUT(classUT));
    }
}
