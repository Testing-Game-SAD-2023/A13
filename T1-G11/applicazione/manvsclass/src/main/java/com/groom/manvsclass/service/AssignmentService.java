//Modifica 08/12/2024: Creazione Service per Assignment
package com.groom.manvsclass.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;

import com.groom.manvsclass.model.Assignment;
import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.model.TeamAdmin;
import com.groom.manvsclass.model.repository.AssignmentRepository;
import com.groom.manvsclass.model.repository.TeamAdminRepository;
import com.groom.manvsclass.model.repository.TeamRepository;

@Service
public class AssignmentService {
    

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamAdminRepository teamAdminRepository;

    @Autowired
    private JwtService jwtService;  // Servizio per la validazione del JWT

    @Autowired
    private AssignmentRepository assignmentRepository;

    //Modifica 07/12/2024 : creazione funzione per la creazione di un assignment
    @Transactional
    public ResponseEntity<?> creaAssignment(Assignment assignment, 
                                            String idTeam, 
                                            @CookieValue(name = "jwt", required = false) String jwt) {
        System.out.println("Creazione dell'Assignment in corso...");

        // 1. Verifica il token JWT
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 2. Estrai l'Admin dal token JWT
        String adminUsername = jwtService.getAdminFromJwt(jwt);
        if (adminUsername == null || adminUsername.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile identificare l'Admin dal token JWT.");
        }

        // 3. Verifica i dati dell'Assignment
        if (assignment.getTitolo() == null || assignment.getTitolo().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Il titolo dell'Assignment è obbligatorio.");
        }
        if (assignment.getDataScadenza() == null || assignment.getDataScadenza().before(new Date())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La data di scadenza deve essere una data futura.");
        }

        // 4. Recupera il Team dal repository
        Team existingTeam = teamRepository.findById(idTeam).orElse(null);
        if (existingTeam == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Il team con ID " + idTeam + " non è stato trovato.");
        }

        // 5. Verifica se l'Admin ha i permessi per questo Team
        TeamAdmin teamAdmin = teamAdminRepository.findByTeamId(idTeam);
        if (teamAdmin == null || !teamAdmin.getAdminId().equals(adminUsername) || 
            (!"Owner".equals(teamAdmin.getRole()) && !"Professor".equals(teamAdmin.getRole()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per creare un Assignment per questo Team.");
        }

        // 6. Genera un ID univoco per l'Assignment e salvalo
        assignmentRepository.save(assignment);

        // 7. Aggiorna il Team con il nuovo Assignment
        if (existingTeam.getAssignments() == null) {
            existingTeam.setAssignments(new ArrayList<>());
        }
        existingTeam.getAssignments().add(assignment.getIdAssignment());
        teamRepository.save(existingTeam);



        //InviaNotifica agli utenti del team.
        List<String> idsStudentiTeam = existingTeam.getStudenti();
        inviaNotificaStudenti(idsStudentiTeam);

        // 8. Restituisci la risposta di successo
        return ResponseEntity.status(HttpStatus.CREATED).body("Assignment creato con successo e associato al Team.");
    }

    //Inviare notifiche agli studenti del team - Merge da fare.
    public void inviaNotificaStudenti(List<String> idStudenti){

    }

    //Modifica 08/12/2024: creazione funzioni visualizzaTeamAssignment,visualizzaAssignments e deleteAssignment
    public ResponseEntity<?> visualizzaTeamAssignment(String idTeam, @CookieValue(name = "jwt", required = false) String jwt) {
        System.out.println("Recupero degli Assignment del Team in corso...");
    
        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }
    
        // 2. Estrai l'ID dell'Admin dal JWT
        String adminUsername = jwtService.getAdminFromJwt(jwt);
        if (adminUsername == null || adminUsername.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile identificare l'Admin dal token JWT.");
        }
    
        // 3. Recupera il Team dal repository utilizzando l'idTeam
        Team existingTeam = teamRepository.findById(idTeam).orElse(null);
        if (existingTeam == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con ID " + idTeam + " non trovato.");
        }
    
        // 4. Verifica se l'Admin ha i permessi per visualizzare gli Assignment del Team
        TeamAdmin teamAdmin = teamAdminRepository.findByTeamId(idTeam);
        if (teamAdmin == null || !teamAdmin.getAdminId().equals(adminUsername) || 
            (!"Owner".equals(teamAdmin.getRole()) && !"Professor".equals(teamAdmin.getRole()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per visualizzare gli assignment di questo team.");
        }
    
        // 5. Recupera gli Assignment associati al Team
        List<String> assignmentIds = existingTeam.getAssignments();
        if (assignmentIds == null || assignmentIds.isEmpty()) {
            return ResponseEntity.ok("Nessun assignment associato al Team.");
        }
    
        // 6. Recupera i dettagli degli Assignment dal repository
        List<Assignment> assignments = (List<Assignment>) assignmentRepository.findAllById(assignmentIds);
    
        // 7. Restituisci gli Assignment trovati
        return ResponseEntity.ok(assignments);
    }
    
    public ResponseEntity<?> visualizzaAssignments(@CookieValue(name = "jwt", required = false) String jwt) {
        System.out.println("Recupero degli Assignment associati all'Admin in corso...");
    
        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }
    
        // 2. Estrai l'ID dell'Admin dal JWT
        String adminUsername = jwtService.getAdminFromJwt(jwt);
        if (adminUsername == null || adminUsername.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile identificare l'Admin dal token JWT.");
        }
    
        // 3. Recupera tutti i team associati all'Admin
        List<TeamAdmin> teamAdminAssociations = teamAdminRepository.findAllByAdminId(adminUsername);
        if (teamAdminAssociations == null || teamAdminAssociations.isEmpty()) {
            return ResponseEntity.ok("Non sei associato ad alcun team.");
        }
    
        // 4. Recupera gli ID dei team associati
        List<String> teamIds = teamAdminAssociations.stream()
                .map(TeamAdmin::getTeamId)
                .collect(Collectors.toList());
    
        // 5. Recupera tutti i team dal repository utilizzando gli ID
        List<Team> teams = (List<Team>) teamRepository.findAllById(teamIds);
        if (teams == null || teams.isEmpty()) {
            return ResponseEntity.ok("Non sono stati trovati team associati.");
        }
    
        // 6. Recupera gli ID degli assignment di tutti i team
        List<String> assignmentIds = new ArrayList<>();
        for (Team team : teams) {
            if (team.getAssignments() != null) {
                assignmentIds.addAll(team.getAssignments());
            }
        }
    
        if (assignmentIds.isEmpty()) {
            return ResponseEntity.ok("Non ci sono assignment associati ai tuoi team.");
        }
    
        // 7. Recupera i dettagli degli assignment dal repository
        List<Assignment> assignments = (List<Assignment>) assignmentRepository.findAllById(assignmentIds);
    
        // 8. Restituisce gli assignment trovati
        return ResponseEntity.ok(assignments);
    }

    @Transactional
    public ResponseEntity<?> deleteAssignment(String idTeam, String idAssignment, String jwt) {
    System.out.println("Rimozione dell'Assignment in corso...");

    // 1. Verifica se il token JWT è valido
    if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
    }

    // 2. Estrai l'ID dell'Admin dal JWT
    String adminUsername = jwtService.getAdminFromJwt(jwt);
    if (adminUsername == null || adminUsername.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile identificare l'Admin dal token JWT.");
    }

    // 3. Recupera il Team dal repository utilizzando l'idTeam
    Team existingTeam = teamRepository.findById(idTeam).orElse(null);
    if (existingTeam == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con ID " + idTeam + " non trovato.");
    }

    // 4. Verifica se l'Admin ha i permessi per rimuovere gli Assignment del Team
    TeamAdmin teamAdmin = teamAdminRepository.findByTeamId(idTeam);
    if (teamAdmin == null || !teamAdmin.getAdminId().equals(adminUsername) || 
        (!"Owner".equals(teamAdmin.getRole()) && !"Professor".equals(teamAdmin.getRole()))) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per rimuovere gli assignment di questo team.");
    }

    // 5. Verifica se l'Assignment esiste tra quelli associati al Team
    List<String> assignmentIds = existingTeam.getAssignments();
    if (assignmentIds == null || !assignmentIds.contains(idAssignment)) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assignment con ID " + idAssignment + " non trovato nel team.");
    }

    // 6. Rimuovi l'Assignment dal Team
    assignmentIds.remove(idAssignment);
    existingTeam.setAssignments(assignmentIds);
    teamRepository.save(existingTeam);

    // 7. Rimuovi l'Assignment dal database
    assignmentRepository.deleteById(idAssignment);

    // 8. Restituisci la risposta di successo
    return ResponseEntity.status(HttpStatus.OK).body("Assignment rimosso con successo dal Team.");
}
  
}

    