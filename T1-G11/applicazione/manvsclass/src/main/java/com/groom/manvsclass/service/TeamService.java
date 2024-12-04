package com.groom.manvsclass.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.model.repository.TeamRepository;
import com.groom.manvsclass.model.repository.TeamSearchImpl;
import com.groom.manvsclass.service.JwtService;
import com.groom.manvsclass.model.User;
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

    @Autowired
    private RestTemplate restTemplate; // Per effettuare chiamate HTTP

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

    
    public ResponseEntity<String> addMemberToTeam(String teamName, List<String> selectedMemberIds, String jwt) {
        // Verifica la validità del token JWT
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    
        // Chiamata al container T2-3 per ottenere tutti gli studenti
        String t2_3Url = "http://t23-g1-app-1:8080/students_list"; // URL per ottenere tutti gli studenti
        List<User> allStudents;
    
        try {
            // Crea l'header con il JWT usando il metodo di JwtService
            HttpEntity<Void> entity = jwtService.createJwtRequestEntity(jwt);  // Qui non è necessario fare altro, è già un HttpEntity
    
            // Esegui la chiamata GET per ottenere gli studenti
            ResponseEntity<List<User>> response = restTemplate.exchange(
                t2_3Url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<User>>() {}
            );
    
            if (response.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error retrieving students from T2-3");
            }
    
            allStudents = response.getBody();
    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error connecting to T2-3");
        }
    
        // Visualizza tutti gli studenti e aggiungi i selezionati al team in MongoDB (T1)
        for (String memberId : selectedMemberIds) {
            // Verifica che il membro selezionato esista tra gli studenti di T2-3
            boolean memberFound = allStudents.stream()
                .anyMatch(student -> student.getID().equals(Integer.parseInt(memberId)));
    
            if (!memberFound) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found in T2-3: " + memberId);
            }
    
            // Aggiungi il membro al team in MongoDB (T1)
            try {
                searchRepository.addMemberToTeam(teamName, memberId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating the team with member " + memberId);
            }
        }
    
        return ResponseEntity.ok("Members added successfully");
    }
    





    // Rimuove un membro dal team
    public ResponseEntity<String> removeMemberFromTeam(String teamName, String memberId, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        searchRepository.removeMemberFromTeam(teamName, memberId);
        return ResponseEntity.ok("Member removed successfully");
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

