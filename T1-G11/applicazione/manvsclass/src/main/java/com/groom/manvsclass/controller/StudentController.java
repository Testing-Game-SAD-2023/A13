package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.entity.AssignmentEntity;
import com.groom.manvsclass.model.entity.TeamEntity;
import com.groom.manvsclass.model.repository.AssignmentRepository;
import com.groom.manvsclass.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Qui ci sono le chiamate che può fare uno student per accedere a dati che gli riguardano 
@CrossOrigin
@RestController
public class StudentController {

    private final TeamService teamService;
    private final AssignmentRepository assignmentRepository;

    private final Logger logger = LoggerFactory.getLogger(StudentController.class);

    public StudentController(
            TeamService teamService,
            AssignmentRepository assignmentRepository) {
        this.teamService = teamService;
        this.assignmentRepository = assignmentRepository;
    }

    @GetMapping("/ottieniDettagliTeamCompleto")
    public ResponseEntity<?> ottieniDettagliTeamCompleto(@RequestParam("StudentId") String studentId, @CookieValue(name = "jwt", required = false) String jwt) {
        try {

            // 1. Verifica se l'utente ha un team 
            TeamEntity existingTeamEntity = teamService.getTeamByStudentId(studentId);
            if (existingTeamEntity == null) {
                //il team non esiste 
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("L'utente non è associato a un Team");
            }

            // 2. Recupera gli Assignment associati al Team
            List<AssignmentEntity> assignmentEntityList = assignmentRepository.findByTeam_IdTeam(existingTeamEntity.getIdTeam());
            if (assignmentEntityList == null || assignmentEntityList.isEmpty()) {
                assignmentEntityList = new ArrayList<>();
            }

            // 3. Crea la struttura di risposta
            Map<String, Object> response = new HashMap<>();
            response.put("team", existingTeamEntity);
            response.put("assignments", assignmentEntityList);

            // 4. Restituisci la risposta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Gestione degli errori
            logger.error("Errore durante il recupero delle informazioni del team: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore durante il recupero delle informazioni del team.");
        }
    }

}
