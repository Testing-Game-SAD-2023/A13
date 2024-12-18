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

import javax.servlet.http.HttpServletRequest;


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
        if (teamRepository.existsByTeamName(team.getTeamName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Team already exists
        }
        searchRepository.addTeam(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(team);
    }
     /**
     * Prendi tutti i team. --> restituisce la lista: utile per la select
     */
    public ResponseEntity<List<Team>> getAllTeams(String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(teamRepository.findAll());
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
    
    //MODIFICA 06/12/2024
    public ResponseEntity<?> getStudentsList(String jwt) {
        String t2_3Url = "http://t23-g1-app-1:8080/students_list";
        List<User> allStudents;
    
        try {
            HttpEntity<Void> entity = jwtService.createJwtRequestEntity(jwt);
    
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
            return ResponseEntity.ok(allStudents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error connecting to T2-3");
        }
    }

    public ResponseEntity<String> getAllTeamsAsHtml(String jwt) {
        // Controlla se il JWT è valido
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Accesso non autorizzato");
        }
    
        // Recupera tutti i team dal repository
        List<Team> teams = teamRepository.findAll();
    
        // Genera il contenuto HTML per il corpo della tabella
        StringBuilder htmlBuilder = new StringBuilder();
        for (Team team : teams) {
            List<String> members = team.getMember();
            int numberOfMembers = members.size();
            String fullMembersList = String.join(", ", members);
    
            htmlBuilder.append("<tr>")
                .append("<td>").append(team.getTeamName()).append("</td>")
                .append("<td>").append(team.getDescription()).append("</td>")
                .append("<td>").append(team.getLeaderId()).append("</td>")
                .append("<td>")
                .append("<div class='member-tooltip' title='").append(fullMembersList).append("'>")
                .append(numberOfMembers).append(" membri")
                .append("</div>")
                .append("</td>")
                .append("</tr>");
        }
        return ResponseEntity.ok(htmlBuilder.toString());
    }
    
    
    
    public ResponseEntity<String> addMemberToTeam(String teamName, String memberId, String jwt) {
        // Verifica la validità del token JWT
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    
        // Recupera il team dal repository
        Team team = searchRepository.findTeamByName(teamName);
    
        // Verifica se il membro è già nel team
        if (team.getMember().contains(memberId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Membro già nel team: " + memberId);
        }
    
        // Aggiungi il membro al team
        try {
            searchRepository.addMemberToTeam(teamName, memberId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating the team with member " + memberId);
        }
    
        return ResponseEntity.ok("Studente aggiunto con successo!");
    }
    
    public ResponseEntity<String> removeMemberFromTeam(String teamName, String memberId, String jwt) {
        // Verifica la validità del token JWT
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        try {
             // Recupera il team dal repository
            Team team = searchRepository.findTeamByName(teamName);
            // Controlla se il membro è nel team
            if (!team.getMember().contains(memberId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Studente non presente nel team!");
            }
            // Rimuovi il membro dal team
            searchRepository.removeMemberFromTeam(teamName, memberId);
            return ResponseEntity.ok("Studente rimosso con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing member: " + e.getMessage());
        }
    }





    public ResponseEntity<String> deleteTeam(String teamName, String jwt) {
        // Verifica la validità del token JWT
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        try {
            // Elimina il team
            searchRepository.deleteTeam(teamName);
            return ResponseEntity.ok("Team cancellato con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nella cancellazione del team: " + e.getMessage());
        }
    }
    
    



}

