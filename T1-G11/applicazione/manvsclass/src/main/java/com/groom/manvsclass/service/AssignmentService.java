//Modifica 08/12/2024: Creazione Service per Assignment
package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Assignment;
import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.repository.AssignmentRepository;
import com.groom.manvsclass.repository.AdminRepository;
import com.groom.manvsclass.repository.TeamRepository;
import com.groom.manvsclass.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private TeamRepository teamRepository;
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
                                            String teamName,
                                            @CookieValue(name = "jwt", required = false) String jwt) {
        System.out.println("Creazione dell'Assignment in corso...");

        // 1. Verifica il token JWT
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 2. Estrai l'Admin dal token JWT
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);
        if (adminEmail == null || adminEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile identificare l'Admin dal token JWT.");
        }

        // 3. Verifica i dati dell'Assignment
        if (assignment.getTitle() == null || assignment.getTitle().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Il titolo dell'Assignment è obbligatorio.");
        }
        if (assignment.getExpirationDate() == null || assignment.getExpirationDate().isBefore(LocalDate.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La data di scadenza deve essere una data futura.");
        }

        // 4. Recupera il Team dal repository
        Optional<Team> teamOpt = teamRepository.findByName(teamName);
        if (teamOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Il team " + teamName + " non è stato trovato.");
        }

        Team existingTeam = teamOpt.get();

        // 5. Verifica se l'Admin ha i permessi per questo Team
        if (existingTeam.getAdmin() == null || !existingTeam.getAdmin().getEmail().equals(adminEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per creare un Assignment per questo Team.");
        }

        // 6. Associa il team all'Assignment
        assignment.setTeam(existingTeam);

        // 8. Salva l'Assignment
        assignmentRepository.save(assignment);

        // 9. Invia notifica agli utenti del team
        List<String> studentIds = existingTeam.getStudentIds();
        List<Integer> integerList = studentIds.stream()
                .map(Integer::parseInt) // Converte ogni stringa in intero (possibili problemi)
                .collect(Collectors.toList());

        String Title = "Assignment";
        String Message = "Nuovo Assignment: " + assignment.getTitle();
        notificationService.sendNotificationsToUsers(integerList, Title, Message, "Team");

        //10. Invio email agli utenti del team
        //emailService.sendTeamNewAssignment(idsStudentiTeam, existingTeam, assignment, jwt);

        // 13. Restituisci la risposta di successo
        return ResponseEntity.status(HttpStatus.CREATED).body("Assignment creato con successo e associato al Team.");
    }

    //Modifica 08/12/2024: creazione funzioni visualizzaTeamAssignment,visualizzaAssignments e deleteAssignment
    // Funzione aggiornata per visualizzare gli Assignment di un Team
    public ResponseEntity<?> visualizzaTeamAssignment(String teamName, @CookieValue(name = "jwt", required = false) String jwt) {
        System.out.println("Recupero degli Assignment del Team in corso...");

        try {
            // 1. Verifica se il token JWT è valido
            if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
            }

            // 2. Estrai l'ID dell'Admin dal JWT
            String adminEmail = jwtService.getAdminEmailFromJwt(jwt);
            if (adminEmail == null || adminEmail.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile identificare l'Admin dal token JWT.");
            }

            // 3. Recupera il Team dal repository
            Optional<Team> teamOpt = teamRepository.findByName(teamName);
            if (teamOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team " + teamName + " non trovato.");
            }

            Team existingTeam = teamOpt.get();

            // 4. Verifica se l'Admin ha i permessi per visualizzare gli Assignment del Team
            if (existingTeam.getAdmin() == null || !existingTeam.getAdmin().getEmail().equals(adminEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per visualizzare gli assignment di questo team.");
            }

            // 5. Recupera i dettagli degli Assignment associati al Team
            List<Assignment> assignments = assignmentRepository.findByTeam_Id(existingTeam.getId());
            if (assignments == null || assignments.isEmpty()) {
                return ResponseEntity.ok("Nessun assignment trovato per il Team con nome " + teamName);
            }

            // 6. Restituisci gli Assignment trovati
            return ResponseEntity.ok(assignments);

        } catch (Exception e) {

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
            String adminEmail = jwtService.getAdminEmailFromJwt(jwt);
            if (adminEmail == null || adminEmail.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile identificare l'Admin dal token JWT.");
            }

            Optional<Admin> adminOpt = adminRepository.findById(adminEmail);
            if (!adminOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin non trovato.");
            }

            Admin teamAdmin = adminOpt.get();

            // 4. Recupera i team di quell'Admin
            List<Team> adminTeams = teamAdmin.getTeams();
            if (adminTeams == null || adminTeams.isEmpty()) {
                return ResponseEntity.ok("Non sei associato ad alcun team.");
            }

            // 5. Recupera gli ID dei team
            List<Long> teamIds = adminTeams.stream()
                    .map(Team::getId)
                    .collect(Collectors.toList());

            // 6. Recupera tutti gli assignment associati ai team
            List<Assignment> assignments = assignmentRepository.findAllByTeam_IdIn(teamIds);
            if (assignments == null || assignments.isEmpty()) {
                return ResponseEntity.ok("Non sono stati trovati assignment per i tuoi team.");
            }

            // 7. Restituisce gli assignment trovati
            return ResponseEntity.ok(assignments);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore durante il recupero degli assignment.");
        }
    }

    @Transactional
    public ResponseEntity<?> deleteAssignment(String assignmentTitle, @CookieValue(name = "jwt", required = false) String jwt) {
        System.out.println("Rimozione dell'Assignment in corso...");

        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 2. Estrai l'ID dell'Admin dal JWT
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);
        if (adminEmail == null || adminEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile identificare l'Admin dal token JWT.");
        }

        // 3. Recupera l'Assignment dal database
        Optional<Assignment> assignmentOpt = assignmentRepository.findByTitle(assignmentTitle);
        if (assignmentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assignment con titolo " + assignmentTitle + " non trovato.");
        }

        Assignment existingAssignment = assignmentOpt.get();

        // 4. Recupera l'ID del team dall'Assignment
        if (existingAssignment.getTeam() == null || existingAssignment.getTeam().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'Assignment non ha un Team associato valido.");
        }
        Long teamId = existingAssignment.getTeam().getId();

        // 5. Recupera il Team dal repository utilizzando l'ID del Team
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if (teamOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con ID " + teamId + " non trovato.");
        }

        Team existingTeam = teamOpt.get();

        // 6. Verifica se l'Admin ha i permessi per rimuovere l'Assignment del Team
        if (existingTeam.getAdmin() == null || !existingTeam.getAdmin().getEmail().equals(adminEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per rimuovere gli assignment di questo team.");
        }

        // 7. Rimuovi l'Assignment dal database
        assignmentRepository.deleteById(existingAssignment.getId());

        // 8. Restituisci la risposta di successo
        return ResponseEntity.status(HttpStatus.OK).body("Assignment rimosso con successo dal Team.");
    }


}

    