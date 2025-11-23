package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.service.TeamModificationRequest;
import com.groom.manvsclass.service.TeamService;
import com.groom.manvsclass.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class TeamController {

    private final TeamService teamService;

    @Autowired
    private JwtService jwtService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    // Endpoint per aggiungere un nuovo team
    @PostMapping("/creaTeam")
    public ResponseEntity<?> creaTeam(@RequestBody Team team, @CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.creaTeam(team, jwt);
    }

    @GetMapping("/visualizzaTeams")
    public ResponseEntity<?> visualizzaTeams(@CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.visualizzaTeams(jwt);
    }

    @GetMapping("/cercaTeam/{idTeam}")
    public ResponseEntity<?> cercaTeam(@PathVariable("idTeam") String idTeam, @CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.cercaTeam(idTeam, jwt);
    }

    // Endpoint per aggiungere un nuovo team
    @DeleteMapping("/deleteTeam")
    public ResponseEntity<?> deleteTeam(@RequestBody String idTeam, @CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.deleteTeam(idTeam, jwt);
    }

    // Endpoint per modificare il nome di un team
    @PutMapping("/modificaNomeTeam")
    public ResponseEntity<?> modificaNomeTeam(@RequestBody TeamModificationRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.modificaNomeTeam(request, jwt);
    }

    //Modifica 04/12/2024: aggiunta di una lista di idStudenti al team
    @PutMapping("/aggiungiStudenti/{idTeam}")
    public ResponseEntity<?> aggiungiStudenti(@PathVariable("idTeam") String idTeam, @RequestBody List<String> idStudenti, @CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.aggiungiStudenti(idTeam, idStudenti, jwt);
    }

    @GetMapping("/ottieniStudentiTeam/{idTeam}")
    public ResponseEntity<?> ottieniStudentiTeam(@PathVariable("idTeam") String idTeam, @CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.ottieniStudentiTeam(idTeam, jwt);
    }

    //Modifica 05/12/2024: aggiunta rimozione studente da un team
    @PutMapping("/rimuoviStudenteTeam/{idTeam}")
    public ResponseEntity<?> rimuoviStudenteTeam(@PathVariable("idTeam") String idTeam, @RequestBody String idStudente, @CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.rimuoviStudenteTeam(idTeam, idStudente, jwt);
    }

    /*
     * Queste chiamate sono accedibili a un utente se fa parte di quel team
     */
    @GetMapping("/ottieniTeamByStudentId")
    public ResponseEntity<?> getTeamByStudentId(
            @RequestParam("StudentId") String idStudente,
            @CookieValue(name = "jwt", required = false) String jwt) {


        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        Optional<Team> teamOpt = teamService.getTeamByStudentId(idStudente);
        if(teamOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team dello studente " + idStudente + " non trovato.");
        }

        Team studentTeam = teamOpt.get();
        return ResponseEntity.ok().body(studentTeam);
    }

    @GetMapping("/GetStudentTeam")
    public ResponseEntity<?> getStudentTeam(@RequestParam String studentId, @CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.getStudentTeam(studentId, jwt);
    }
}
