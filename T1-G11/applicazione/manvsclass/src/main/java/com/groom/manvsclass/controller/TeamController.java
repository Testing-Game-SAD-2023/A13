package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.dto.TeamUpdate;
import com.groom.manvsclass.model.entity.TeamEntity;
import com.groom.manvsclass.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    // Endpoint per aggiungere un nuovo team
    @PostMapping("/creaTeam")
    public ResponseEntity<?> creaTeam(@RequestBody TeamEntity teamEntity, @CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.creaTeam(teamEntity, jwt);
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
    public ResponseEntity<?> modificaNomeTeam(@RequestBody TeamUpdate request, @CookieValue(name = "jwt", required = false) String jwt) {
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
    public ResponseEntity<TeamEntity> getTeamByStudentId(@RequestParam("StudentId") String idStudente) {
        // Invoca il servizio per recuperare il team in base all'ID dello studente
        TeamEntity teamEntity = teamService.getTeamByStudentId(idStudente);
        // Se il team non viene trovato, restituisce un 404 Not Found
        if (teamEntity == null) {
            return ResponseEntity.notFound().build();
        }
        // Se il team viene trovato, restituisce un 200 OK con il team in formato JSON
        return ResponseEntity.ok(teamEntity);
    }

    @GetMapping("/GetStudentTeam")
    public ResponseEntity<?> getStudentTeam(@CookieValue(name = "jwt", required = false) String jwt, @RequestParam String studentId) {
        return teamService.GetStudentTeam(studentId, jwt);
    }
}
