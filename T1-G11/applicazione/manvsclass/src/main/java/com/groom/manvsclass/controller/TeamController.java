package com.groom.manvsclass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    @PostMapping("/deleteTeam")
    @ResponseBody
    public ResponseEntity<?> deleteTeam(@RequestBody String idTeam,@CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.deleteTeam(idTeam,jwt);
    }

    // Endpoint per modificare il nome di un team
    @PutMapping("/modificaTeam")
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
    public ResponseEntity<?> visualizzaTeam(@PathVariable("idTeam") String idTeam, @CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.visualizzaTeam(idTeam, jwt);
    }

}
