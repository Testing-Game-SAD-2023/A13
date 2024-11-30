package com.groom.manvsclass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.model.repository.TeamRepository;
import com.groom.manvsclass.model.repository.TeamSearchImpl;
import com.groom.manvsclass.service.JwtService;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamSearchImpl searchRepository;

    @Autowired
    private JwtService jwtService;

    /*
      Crea un nuovo Team.
     */
    public ResponseEntity<Team> createTeam(Team team, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (teamRepository.existsById(team.getTeamName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Team already exists
        }
        searchRepository.addTeam(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(team);
    }

    /*
       Cerca un Team con suo Nome.
     */
    public ResponseEntity<Team> getTeamByName(String teamName, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Team team = searchRepository.findTeamByName(teamName);
        if (team != null) {
            return ResponseEntity.ok(team);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Prendi tutti i team.
     */
    public ResponseEntity<List<Team>> getAllTeams(String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(teamRepository.findAll());
    }

    /**
     * Modifica un team esistente
     */
    public ResponseEntity<Team> updateTeam(String teamName, Team updatedTeam, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!teamRepository.existsById(teamName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Team not found
        }
        searchRepository.updateTeam(updatedTeam);
        return ResponseEntity.ok(updatedTeam);
    }

    /**
     * Cancella un team .
     */
    public ResponseEntity<String> deleteTeam(String teamName, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        if (!teamRepository.existsById(teamName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found");
        }
        searchRepository.deleteTeam(teamName);
        return ResponseEntity.ok("Team deleted successfully");
    }

    /**
     * Trova team basandoti sul leader ID
     */
    public ResponseEntity<List<Team>> findTeamsByLeader(String leaderId, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(searchRepository.findTeamsByLeader(leaderId));
    }

    /**
     * Mostra tutti i team in una View
     */
    public ModelAndView showAllTeams(String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return new ModelAndView("redirect:/login");
        }
        List<Team> teams = teamRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("team_list"); // Nome della vista HTML
        modelAndView.addObject("teams", teams); // Aggiungi i dati del modello alla vista
        return modelAndView;
    }

    /**
     * Mostra dettagli specifici di un team in una view
     */
    public ModelAndView showTeamDetails(String teamName, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return new ModelAndView("redirect:/login");
        }
        Team team = searchRepository.findTeamByName(teamName);
        if (team == null) {
            return new ModelAndView("error_page").addObject("message", "Team not found");
        }
        ModelAndView modelAndView = new ModelAndView("team_details");
        modelAndView.addObject("team", team);
        return modelAndView;
    }
}

