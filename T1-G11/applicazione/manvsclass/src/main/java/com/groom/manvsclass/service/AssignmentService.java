//Modifica 08/12/2024: Creazione Service per Assignment
package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Assignment;
import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.model.TeamAdmin;
import com.groom.manvsclass.model.repository.AssignmentRepository;
import com.groom.manvsclass.model.repository.TeamAdminRepository;
import com.groom.manvsclass.model.repository.TeamRepository;
import com.groom.manvsclass.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private StudentService studentService;


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

        // 6. Aggiorna i dettagli dell'Assignment con i dati del Team
        assignment.setIdTeam(idTeam); // Imposta l'ID del team
        assignment.setNomeTeam(existingTeam.getName()); // Imposta il nome del team (assumendo che sia presente nella classe Team)

        // 7. Aggiungi un ID univoco al team (se non specificato)
        if (assignment.getIdAssignment() == null || assignment.getIdAssignment().isEmpty()) {
            assignment.setIdAssignment(Util.generateUniqueId());
        }

        // 8. Salva l'Assignment
        assignmentRepository.save(assignment);

        // 9. Invia notifica agli utenti del team
        List<String> idsStudentiTeam = existingTeam.getStudenti();
        List<Integer> integerList = idsStudentiTeam.stream()
                .map(Integer::parseInt) // Converte ogni stringa in intero
                .collect(Collectors.toList());

        String Title = "Assignment";
        String Message = "Nuovo Assignment: " + assignment.getTitolo();
        notificationService.sendNotificationsToUsers(integerList, Title, Message, "Team");

        //10. Invio email agli utenti del team
        //emailService.sendTeamNewAssignment(idsStudentiTeam, existingTeam, assignment, jwt);

        // 13. Restituisci la risposta di successo
        return ResponseEntity.status(HttpStatus.CREATED).body("Assignment creato con successo e associato al Team.");
    }

    //Modifica 08/12/2024: creazione funzioni visualizzaTeamAssignment,visualizzaAssignments e deleteAssignment
    // Funzione aggiornata per visualizzare gli Assignment di un Team
    public ResponseEntity<?> visualizzaTeamAssignment(String idTeam, @CookieValue(name = "jwt", required = false) String jwt) {
        System.out.println("Recupero degli Assignment del Team in corso...");

        try {
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

            // 5. Recupera i dettagli degli Assignment associati al Team
            List<Assignment> assignments = assignmentRepository.findByTeamId(idTeam);
            if (assignments == null || assignments.isEmpty()) {
                return ResponseEntity.ok("Nessun assignment trovato per il Team con ID " + idTeam);
            }

            // 6. Restituisci gli Assignment trovati
            return ResponseEntity.ok(assignments);

        } catch (Exception e) {
            // Gestione di eventuali errori inaspettati
            System.err.println("Errore durante il recupero degli assignment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore durante il recupero degli assignment.");
        }
    }

    public ResponseEntity<?> visualizzaAssignments(@CookieValue(name = "jwt", required = false) String jwt) {
        System.out.println("Recupero degli Assignment associati all'Admin in corso...");

        try {
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

            //Non sei associato ad alcun team.
            // 4. Recupera gli ID dei team associati
            List<String> teamIds = teamAdminAssociations.stream()
                    .map(TeamAdmin::getTeamId)
                    .collect(Collectors.toList());

            // 5. Recupera tutti gli assignment associati ai team
            List<Assignment> assignments = assignmentRepository.findAllByTeamIdIn(teamIds);
            if (assignments == null || assignments.isEmpty()) {
                return ResponseEntity.ok("Non sono stati trovati assignment per i tuoi team.");
            }

            // 6. Restituisce gli assignment trovati
            return ResponseEntity.ok(assignments);

        } catch (Exception e) {
            // Gestione di eventuali errori inaspettati
            System.err.println("Errore durante il recupero degli assignment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore durante il recupero degli assignment.");
        }
    }

    @Transactional
    public ResponseEntity<?> deleteAssignment(String idAssignment, String jwt) {
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

        // 3. Recupera l'Assignment dal database
        Assignment existingAssignment = assignmentRepository.findById(idAssignment).orElse(null);
        if (existingAssignment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assignment con ID " + idAssignment + " non trovato.");
        }

        // 4. Recupera l'ID del team dall'Assignment
        String idTeam = existingAssignment.getTeamId();
        if (idTeam == null || idTeam.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'Assignment non ha un Team associato.");
        }

        // 5. Recupera il Team dal repository utilizzando l'ID del Team
        Team existingTeam = teamRepository.findById(idTeam).orElse(null);
        if (existingTeam == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con ID " + idTeam + " non trovato.");
        }

        // 6. Verifica se l'Admin ha i permessi per rimuovere l'Assignment del Team
        TeamAdmin teamAdmin = teamAdminRepository.findByTeamId(idTeam);
        if (teamAdmin == null || !teamAdmin.getAdminId().equals(adminUsername) ||
                (!"Owner".equals(teamAdmin.getRole()) && !"Professor".equals(teamAdmin.getRole()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per rimuovere gli assignment di questo team.");
        }

        // 7. Rimuovi l'Assignment dal database
        assignmentRepository.deleteById(idAssignment);

        // 8. Restituisci la risposta di successo
        return ResponseEntity.status(HttpStatus.OK).body("Assignment rimosso con successo dal Team.");
    }


}

    