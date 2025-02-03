package com.groom.manvsclass.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.groom.manvsclass.model.Assignment;
import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.model.repository.AssignmentRepository;
import com.groom.manvsclass.service.TeamService;

//Qui ci sono le chiamate che può fare uno student per accedere a dati che gli riguardano 
@CrossOrigin
@Controller
public class StudentController {
    @Autowired
    private TeamService teamService;
    @Autowired
    private AssignmentRepository assignmentRepository;

    @GetMapping("/ottieniDettagliTeamCompleto")
    public ResponseEntity<?> ottieniDettagliTeamCompleto(String studentId, @CookieValue(name = "jwt", required = false) String jwt) {
        try {

            // 1. Verifica se l'utente ha un team 
            Team existingTeam = teamService.getTeamByStudentId(studentId).orElse(null);
            if(existingTeam == null){
                //il team non esiste 
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("L'utente non è associato a un Team");
            }

            // 2. Recupera gli Assignment associati al Team
            List<Assignment> assignments = assignmentRepository.findByTeamId(existingTeam.getIdTeam());
            if (assignments == null || assignments.isEmpty()) {
                assignments = new ArrayList<>();
            }

            // 3. Crea la struttura di risposta
            Map<String, Object> response = new HashMap<>();
            response.put("team", existingTeam);
            response.put("assignments", assignments);

            // 4. Restituisci la risposta
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Gestione degli errori
            System.err.println("Errore durante il recupero delle informazioni del team: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore durante il recupero delle informazioni del team.");
        }
    }

}
