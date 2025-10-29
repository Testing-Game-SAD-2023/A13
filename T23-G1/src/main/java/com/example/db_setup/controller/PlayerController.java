package com.example.db_setup.controller;

import com.example.db_setup.model.Player;
import com.example.db_setup.model.dto.gamification.PlayerDTO;
import com.example.db_setup.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class PlayerController {

    /*
     * Classe da analizzare e rifattorizzare
     */

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Operation(
            summary = "Get all players",
            description = "Returns a list of all registered players"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of player returned successfully or empty if there are no players",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PlayerDTO.class))
                    )
            )
    })
    @GetMapping("/players")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @PostMapping("/players/studentsByIds")
    public ResponseEntity<?> getStudentsByIds(@RequestBody List<String> idsStudenti) {
        return playerService.getStudentsByIds(idsStudenti);
    }

    //Modifica 06/12/2024 Giuleppe: Aggiunta rotta
    @GetMapping("/players/studentByEmail/{emailStudente}")
    @ResponseBody
    public ResponseEntity<?> getStudentByEmail(@PathVariable("emailStudente") String emailStudent) {
        return playerService.getStudentByEmail(emailStudent);
    }

    //Modifica 12/12/2024
    @GetMapping("/players/studentsByNameSurname")
    @ResponseBody
    public List<Map<String, Object>> getStudentsBySurnameAndName(@RequestBody Map<String, String> request) {
        return playerService.getStudentsBySurnameAndName(request);
    }

    //Modifica 12/12/2024 Giuleppe: Aggiunta nuova rotta che verrà aggiunta per la ricerca degli studenti.
    @PostMapping("/players/searchStudents")
    @ResponseBody
    public List<Map<String, Object>> searchStudents(@RequestBody Map<String, String> request) {
        return playerService.searchStudents(request);
    }


    @GetMapping("/players/students_list/{ID}")
    @ResponseBody
    public Player getStudent(@PathVariable String ID) {
        return playerService.getUserByID(Long.parseLong(ID));
    }
}
