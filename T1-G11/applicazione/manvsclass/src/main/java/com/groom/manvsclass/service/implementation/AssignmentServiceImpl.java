package com.groom.manvsclass.service.implementation;

import com.groom.manvsclass.model.entity.AssignmentEntity;
import com.groom.manvsclass.model.entity.TeamAdminEntity;
import com.groom.manvsclass.model.entity.TeamEntity;
import com.groom.manvsclass.model.repository.AssignmentRepository;
import com.groom.manvsclass.model.repository.TeamAdminRepository;
import com.groom.manvsclass.model.repository.TeamRepository;
import com.groom.manvsclass.service.*;
import com.groom.manvsclass.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CookieValue;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamAdminRepository teamAdminRepository;


    //Modifica 07/12/2024 : creazione funzione per la creazione di un assignment
    @Override
    @Transactional
    public ResponseEntity<?> creaAssignment(AssignmentEntity assignmentEntity,
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
        if (assignmentEntity.getTitolo() == null || assignmentEntity.getTitolo().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Il titolo dell'Assignment è obbligatorio.");
        }
        if (assignmentEntity.getDataScadenza() == null || assignmentEntity.getDataScadenza().before(new Date())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La data di scadenza deve essere una data futura.");
        }

        // 4. Recupera il Team dal repository
        TeamEntity existingTeamEntity = teamRepository.findById(idTeam).orElse(null);
        if (existingTeamEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Il team con ID " + idTeam + " non è stato trovato.");
        }

        // 5. Verifica se l'Admin ha i permessi per questo Team
        TeamAdminEntity teamAdminEntity = teamAdminRepository.findByTeam_IdTeam(idTeam).orElse(null);
        if (teamAdminEntity == null || !teamAdminEntity.getAdmin().getEmail().equals(adminUsername) ||
                (!"Owner".equals(teamAdminEntity.getRole()) && !"Professor".equals(teamAdminEntity.getRole()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per creare un Assignment per questo Team.");
        }

        // 6. Aggiorna i dettagli dell'Assignment con i dati del Team

        TeamEntity teamEntity = teamRepository.findById(idTeam).orElse(null);

        assignmentEntity.setTeam(teamEntity); // Imposta l'ID del team

        // 7. Aggiungi un ID univoco al team (se non specificato)
        if (assignmentEntity.getIdAssignment() == null || assignmentEntity.getIdAssignment().isEmpty()) {
            assignmentEntity.setIdAssignment(Util.generateUniqueId());
        }

        // 8. Salva l'Assignment
        assignmentRepository.save(assignmentEntity);

        // 9. Invia notifica agli utenti del team
        List<String> idsStudentiTeam = existingTeamEntity.getIdStudenti();
        List<Integer> integerList = idsStudentiTeam.stream()
                .map(Integer::parseInt) // Converte ogni stringa in intero
                .collect(Collectors.toList());

        String Title = "Assignment";
        String Message = "Nuovo Assignment: " + assignmentEntity.getTitolo();
        notificationService.sendNotificationsToUsers(integerList, Title, Message, "Team");

        //10. Invio email agli utenti del team
        //emailService.sendTeamNewAssignment(idsStudentiTeam, existingTeam, assignment, jwt);

        // 13. Restituisci la risposta di successo
        return ResponseEntity.status(HttpStatus.CREATED).body("Assignment creato con successo e associato al Team.");
    }

    //Modifica 08/12/2024: creazione funzioni visualizzaTeamAssignment,visualizzaAssignments e deleteAssignment
    // Funzione aggiornata per visualizzare gli Assignment di un Team
    @Override
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
            TeamEntity existingTeamEntity = teamRepository.findById(idTeam).orElse(null);
            if (existingTeamEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con ID " + idTeam + " non trovato.");
            }

            // 4. Verifica se l'Admin ha i permessi per visualizzare gli Assignment del Team
            TeamAdminEntity teamAdminEntity = teamAdminRepository.findByTeam_IdTeam(idTeam).orElse(null);
            if (teamAdminEntity == null || !teamAdminEntity.getAdmin().getEmail().equals(adminUsername) ||
                    (!"Owner".equals(teamAdminEntity.getRole()) && !"Professor".equals(teamAdminEntity.getRole()))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per visualizzare gli assignment di questo team.");
            }

            // 5. Recupera i dettagli degli Assignment associati al Team
            List<AssignmentEntity> assignmentEntityList = assignmentRepository.findByTeam_IdTeam(idTeam);
            if (assignmentEntityList == null || assignmentEntityList.isEmpty()) {
                return ResponseEntity.ok("Nessun assignment trovato per il Team con ID " + idTeam);
            }

            // 6. Restituisci gli Assignment trovati
            return ResponseEntity.ok(assignmentEntityList);

        } catch (Exception e) {
            // Gestione di eventuali errori inaspettati
            System.err.println("Errore durante il recupero degli assignment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore durante il recupero degli assignment.");
        }
    }

    @Override
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
            List<TeamAdminEntity> teamAdminEntityAssociations = teamAdminRepository.findAllByAdmin_Email(adminUsername);
            if (teamAdminEntityAssociations == null || teamAdminEntityAssociations.isEmpty()) {
                return ResponseEntity.ok("Non sei associato ad alcun team.");
            }

            //Non sei associato ad alcun team.
            // 4. Recupera gli ID dei team associati
            List<String> teamIds = teamAdminEntityAssociations.stream()
                    .map(entity -> entity.getTeam().getIdTeam())
                    .collect(Collectors.toList());

            // 5. Recupera tutti gli assignment associati ai team
            List<AssignmentEntity> assignmentEntityList = assignmentRepository.findByTeam_IdTeamIn(teamIds);
            if (CollectionUtils.isEmpty(assignmentEntityList)) {
                return ResponseEntity.ok("Non sono stati trovati assignment per i tuoi team.");
            }

            // 6. Restituisce gli assignment trovati
            return ResponseEntity.ok(assignmentEntityList);

        } catch (Exception e) {
            // Gestione di eventuali errori inaspettati
            System.err.println("Errore durante il recupero degli assignment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore durante il recupero degli assignment.");
        }
    }

    @Override
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
        AssignmentEntity existingAssignmentEntity = assignmentRepository.findById(idAssignment).orElse(null);
        if (existingAssignmentEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assignment con ID " + idAssignment + " non trovato.");
        }

        // 4. Recupera l'ID del team dall'Assignment
        String idTeam = existingAssignmentEntity.getTeam().getIdTeam();
        if (idTeam == null || idTeam.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'Assignment non ha un Team associato.");
        }

        // 5. Recupera il Team dal repository utilizzando l'ID del Team
        TeamEntity existingTeamEntity = teamRepository.findById(idTeam).orElse(null);
        if (existingTeamEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con ID " + idTeam + " non trovato.");
        }

        // 6. Verifica se l'Admin ha i permessi per rimuovere l'Assignment del Team
        TeamAdminEntity teamAdminEntity = teamAdminRepository.findByTeam_IdTeam(idTeam).orElse(null);
        if (teamAdminEntity == null || !teamAdminEntity.getAdmin().getEmail().equals(adminUsername) ||
                (!"Owner".equals(teamAdminEntity.getRole()) && !"Professor".equals(teamAdminEntity.getRole()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per rimuovere gli assignment di questo team.");
        }

        // 7. Rimuovi l'Assignment dal database
        assignmentRepository.deleteById(idAssignment);

        // 8. Restituisci la risposta di successo
        return ResponseEntity.status(HttpStatus.OK).body("Assignment rimosso con successo dal Team.");
    }


}
