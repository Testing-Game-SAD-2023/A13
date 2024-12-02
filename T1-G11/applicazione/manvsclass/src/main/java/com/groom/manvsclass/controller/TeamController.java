package com.groom.manvsclass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.service.TeamService;
import com.groom.manvsclass.service.JwtService;
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
        System.out.println(jwt);
        return teamService.creaTeam(team,jwt);
    }

    // Endpoint per aggiungere un nuovo team
    @PostMapping("/deleteTeam")
    @ResponseBody
    //@CookieValue(name = "jwt", required = false) String jwt
    public ResponseEntity<?> deleteTeam(@RequestBody String idTeam) {
        System.out.println("TeamController.");
        return teamService.deleteTeam(idTeam);
    }

    // Endpoint per modificare il nome di un team
    @PutMapping("/modificaTeam")
    @ResponseBody
    public ResponseEntity<?> modificaNomeTeam(@RequestBody TeamModificationRequest request) {
        return teamService.modificaNomeTeam(request);
    }

    @GetMapping("/visualizzaTeams")
    @ResponseBody
    public ResponseEntity<?> visualizzaTeams() {
        return teamService.visualizzaTeams();
    }

}
