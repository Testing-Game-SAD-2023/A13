package com.groom.manvsclass.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.service.TeamService;
import com.groom.manvsclass.service.TeamModificationRequest;

@CrossOrigin
@Controller
public class TeamController {

    @Autowired
    private TeamService teamService;


    // Endpoint per aggiungere un nuovo team
    @PostMapping("/creaTeam")
    @ResponseBody
    public ResponseEntity<?> creaTeam(@RequestBody Team team,@CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.creaTeam(team,jwt);
    }
    
    // Endpoint per aggiungere un nuovo team
    @DeleteMapping("/deleteTeam")
    @ResponseBody
    public ResponseEntity<?> deleteTeam(@RequestBody String idTeam,@CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.deleteTeam(idTeam,jwt);
    }

    // Endpoint per modificare il nome di un team
    @PutMapping("/modificaNomeTeam")
    @ResponseBody
    public ResponseEntity<?> modificaNomeTeam(@RequestBody TeamModificationRequest request,@CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.modificaNomeTeam(request,jwt);
    }

    @GetMapping("/visualizzaTeams")
    @ResponseBody
    public ResponseEntity<?> visualizzaTeams(@CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.visualizzaTeams(jwt);
    }

    @GetMapping("/cercaTeam/{idTeam}")
    @ResponseBody
    public ResponseEntity<?> cercaTeam(@PathVariable("idTeam") String idTeam, @CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.cercaTeam(idTeam, jwt);
    }

    //Modifica 04/12/2024: aggiunta di una lista di idStudenti al team
    @PutMapping("/aggiungiStudenti/{idTeam}")
    @ResponseBody
    public ResponseEntity<?> aggiungiStudenti(@PathVariable("idTeam") String idTeam, @RequestBody List<String> idStudenti, @CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.aggiungiStudenti(idTeam, idStudenti, jwt);
    }
    
    @GetMapping("/ottieniStudentiTeam/{idTeam}")
    @ResponseBody
    public ResponseEntity<?> ottieniStudentiTeam(@PathVariable("idTeam") String idTeam, @CookieValue(name = "jwt", required = false) String jwt){
        return teamService.ottieniStudentiTeam(idTeam,jwt);
    }

    //Modifica 05/12/2024: aggiunta rimozione studente da un team
    @PutMapping("/rimuoviStudenteTeam/{idTeam}")
    @ResponseBody
    public ResponseEntity<?> rimuoviStudenteTeam(@PathVariable("idTeam") String idTeam,@RequestBody String idStudente,@CookieValue(name = "jwt", required = false) String jwt){
        return teamService.rimuoviStudenteTeam(idTeam,idStudente,jwt);
    }
   
}   

