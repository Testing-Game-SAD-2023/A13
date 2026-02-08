package com.example.db_setup.controller;

import com.example.db_setup.model.GameProgress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.db_setup.service.GameProgressService;
import com.example.db_setup.model.dto.gamification.GameProgressDTO;


import java.util.List;

@RestController
@RequestMapping("/games")
public class GameProgressController {

    @Autowired
    private GameProgressService gameProgressService;

    /*
     * Storico partite di un player
     */
    @GetMapping("/player/{playerId}")
    public ResponseEntity<?> getGameHistoryByPlayer(
            @PathVariable Long playerId
    ) {
        try {
            List<GameProgressDTO> history =
                    gameProgressService.getGameHistoryByPlayer(playerId);

            return ResponseEntity.ok(history);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore nel recupero dello storico partite");
        }
    }
}
