package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.service.TeamModificationRequest;
import com.groom.manvsclass.service.TeamService;
import com.groom.manvsclass.service.JwtService;
import com.groom.manvsclass.security.JwtRequestContext;
import com.groom.manvsclass.exception.NotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class TeamController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/creaTeam")
    public ResponseEntity<?> createTeam(@RequestBody Team team) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        if (adminEmail == null || adminEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile identificare l'Admin dal token JWT.");
        }

        return teamService.createTeam(team, adminEmail);
    }

    @GetMapping("/visualizzaTeams")
    public ResponseEntity<?> visualizzaTeams() {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        return teamService.visualizzaTeams(jwt);
    }

    @GetMapping("/cercaTeam/{teamId}")
    public ResponseEntity<?> cercaTeam(@PathVariable("teamId") String teamId) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        return teamService.cercaTeam(teamId, jwt);
    }

    @DeleteMapping("/deleteTeam")
    public ResponseEntity<?> deleteTeam(@RequestBody String teamId) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        return teamService.deleteTeam(teamId, jwt);
    }

    @PutMapping("/modificaNomeTeam")
    public ResponseEntity<?> modificaNomeTeam(@RequestBody TeamModificationRequest request) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        return teamService.modificaNomeTeam(request, jwt);
    }

    @PutMapping("/aggiungiStudenti/{teamId}")
    public ResponseEntity<?> aggiungiStudenti(@PathVariable("teamId") String teamId, @RequestBody List<String> studentIds) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        return teamService.aggiungiStudenti(teamId, studentIds, jwt);
    }

    @GetMapping("/ottieniStudentiTeam/{idTeam}")
    public ResponseEntity<?> ottieniStudentiTeam(@PathVariable("idTeam") String idTeam, @CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.ottieniStudentiTeam(idTeam, jwt);
    }

    @PutMapping("/rimuoviStudenteTeam/{idTeam}")
    public ResponseEntity<?> rimuoviStudenteTeam(@PathVariable("idTeam") String idTeam, @RequestBody String idStudente, @CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.rimuoviStudenteTeam(idTeam, idStudente, jwt);
    }

    @GetMapping("/ottieniTeamByStudentId")
    public ResponseEntity<?> getTeamByStudentId(@RequestParam("studentId") String studentId) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        try {
            Team studentTeam = teamService.getTeamByStudentId(studentId);
            return ResponseEntity.ok(studentTeam);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore!" );
        }
    }

    @GetMapping("/GetStudentTeam")
    public ResponseEntity<?> getStudentTeam(@RequestParam String studentId) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        return teamService.getStudentTeam(studentId, jwt);
    }
}
