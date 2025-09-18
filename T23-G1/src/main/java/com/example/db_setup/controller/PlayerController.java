package com.example.db_setup.controller;

import com.example.db_setup.model.Player;
import com.example.db_setup.model.dto.gamification.PlayerDTO;
import com.example.db_setup.model.repository.PlayerRepository;
import com.example.db_setup.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerRepository playerRepository;
    private final Logger logger = LoggerFactory.getLogger(PlayerController.class);

    public PlayerController(PlayerService playerService, PlayerRepository playerRepository) {
        this.playerService = playerService;
        this.playerRepository = playerRepository;
    }

//    modified by GaetanoM
    @GetMapping("/players")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        logger.info("[GET /players] Received request");
        List<Player> players = playerRepository.findAll();
        List<PlayerDTO> playersDTO = players.stream().map(PlayerDTO::new).toList();
        logger.info("[GET /players] Players retrieved: {}", playersDTO);
        return ResponseEntity.ok(playersDTO);
    }

//    added by GaetanoM
//    @GetMapping("/players/{playerId}")
//    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable("playerId") long playerId) {
//        logger.info("[GET /players/{}] Received request", playerId);
//        Player player = playerService.getUserByID(playerId);
//        PlayerDTO playerDTO = new PlayerDTO(player);
//        logger.info("[GET /players/{}] Player retrieved: {}", playerId, playerDTO);
//        return ResponseEntity.ok(playerDTO);
//    }

    @PostMapping("/players/studentsByIds")
    public ResponseEntity<?> getStudentsByIds(@RequestBody List<String> idsStudenti){
        return playerService.getStudentsByIds(idsStudenti);
    }

    //Modifica 06/12/2024 Giuleppe: Aggiunta rotta
    @GetMapping("/players/studentByEmail/{emailStudente}")
    @ResponseBody
    public ResponseEntity<?> getStudentByEmail(@PathVariable("emailStudente") String emailStudent){
        return playerService.getStudentByEmail(emailStudent);
    }

    //Modifica 12/12/2024
    @GetMapping("/players/studentsByNameSurname")
    @ResponseBody
    public List<Map<String,Object>> getStudentsBySurnameAndName(@RequestBody Map<String, String> request){
        return playerService.getStudentsBySurnameAndName(request);
    }

    //Modifica 12/12/2024 Giuleppe: Aggiunta nuova rotta che verrà aggiunta per la ricerca degli studenti.
    @PostMapping("/players/searchStudents")
    @ResponseBody
    public List<Map<String,Object>> searchStudents(@RequestBody Map<String, String> request){
        return playerService.searchStudents(request);
    }

    @GetMapping("/players/students_list/{ID}")
    @ResponseBody
    public Player getStudent(@PathVariable String ID) {
        return playerService.getUserByID(Long.parseLong(ID));
    }
}
