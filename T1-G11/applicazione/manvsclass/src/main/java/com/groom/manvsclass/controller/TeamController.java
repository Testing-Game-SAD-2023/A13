package com.groom.manvsclass.controller;

import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.dto.TeamDTO;
import com.groom.manvsclass.dto.TeamModificationRequest;
import com.groom.manvsclass.service.TeamService;
import com.groom.manvsclass.service.JwtService;
import com.groom.manvsclass.security.JwtRequestContext;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;

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
    public ResponseEntity<?> createTeam(@Valid @RequestBody TeamDTO teamDTO) {

        String jwt = JwtRequestContext.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        teamService.createTeam(teamDTO, adminEmail);
        return ResponseEntity.ok("Team creato con successo.");

    }

    @GetMapping("/visualizzaTeams")
    public ResponseEntity<?> visualizzaTeams() {

        String jwt = JwtRequestContext.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        List<TeamDTO> teamDTOs = teamService.findAdminTeams(adminEmail);
        return ResponseEntity.ok(teamDTOs);
    }

    @GetMapping("/cercaTeam/{teamId}")
    public ResponseEntity<?> cercaTeam(@PathVariable("teamId") String teamId) {

        String jwt = JwtRequestContext.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        TeamDTO teamDTO =  teamService.findAdminTeam(teamId, adminEmail);
        return ResponseEntity.ok(teamDTO);
    }

    @DeleteMapping("/deleteTeam/{teamName}")
    public ResponseEntity<?> deleteTeam(@PathVariable String teamName) {

        String jwt = JwtRequestContext.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        teamService.deleteTeam(teamName, adminEmail);
        return ResponseEntity.ok("Team eliminato con successo.");
    }

    @PostMapping("/modificaNomeTeam")
    public ResponseEntity<?> modificaNomeTeam(@Valid @RequestBody TeamModificationRequest request) {

        String jwt = JwtRequestContext.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        teamService.modificaNomeTeam(request, adminEmail);
        return ResponseEntity.ok("Nome team modificato correttamente.");
    }

    @PutMapping("/aggiungiStudenti/{teamId}")
    public ResponseEntity<?> aggiungiStudenti(@PathVariable("teamId") String teamId, @RequestBody List<String> studentIds) {

        String jwt = JwtRequestContext.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        return teamService.addStudents(teamId, studentIds, adminEmail);
    }

    @GetMapping("/ottieniStudentiTeam/{idTeam}")
    public ResponseEntity<?> ottieniStudentiTeam(@PathVariable("idTeam") String idTeam) {

        String jwt = JwtRequestContext.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        return teamService.ottieniStudentiTeam(idTeam, adminEmail);
    }

    @PutMapping("/rimuoviStudenteTeam/{idTeam}")
    public ResponseEntity<?> rimuoviStudenteTeam(@PathVariable("idTeam") String idTeam, @RequestBody String idStudente) {

        String jwt = JwtRequestContext.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        return teamService.rimuoviStudenteTeam(idTeam, idStudente, adminEmail);
    }

    @GetMapping("/ottieniTeamByStudentId")
    public ResponseEntity<?> getTeamByStudentId(@RequestParam("studentId") String studentId) {

        Team studentTeam = teamService.getTeamByStudentId(studentId);
        return ResponseEntity.ok(studentTeam);
    }

    @GetMapping("/GetStudentTeam")
    public ResponseEntity<?> getStudentTeam(@RequestParam String studentId) {

        String jwt = JwtRequestContext.getJwtToken();
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        return teamService.getStudentTeam(studentId, adminEmail);
    }
}
