package com.groom.manvsclass.service.implementation;

import com.groom.manvsclass.model.dto.TeamUpdate;
import com.groom.manvsclass.model.entity.AdminEntity;
import com.groom.manvsclass.model.entity.AssignmentEntity;
import com.groom.manvsclass.model.entity.TeamAdminEntity;
import com.groom.manvsclass.model.entity.TeamEntity;
import com.groom.manvsclass.model.repository.AdminRepository;
import com.groom.manvsclass.model.repository.AssignmentRepository;
import com.groom.manvsclass.model.repository.TeamAdminRepository;
import com.groom.manvsclass.model.repository.TeamRepository;
import com.groom.manvsclass.service.*;
import com.groom.manvsclass.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.client.HttpClientErrorException;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private JwtService jwtService;  // Servizio per la validazione del JWT

    @Autowired
    private StudentService studentService; //Servizio per mandare query al T23

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamAdminRepository teamAdminRepository;

    @Autowired
    private AdminRepository adminRepository;

    //Metodo per creare un nuovo Team
    @Override
    public ResponseEntity<?> creaTeam(TeamEntity teamEntity, @CookieValue(name = "jwt", required = false) String jwt) {

        System.out.println("Creazione del team in corso...");

        // 1. Verifica che il token JWT sia valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 2. Estrai l'username dell'Admin dal token JWT
        String adminUsername = jwtService.getAdminFromJwt(jwt);

        if (adminUsername == null || adminUsername.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile identificare l'Admin dal token JWT.");
        }

        // 3. Controlla se il nome del team è valido
        if (teamEntity.getName() == null || teamEntity.getName().isEmpty() || teamEntity.getName().length() < 3 || teamEntity.getName().length() > 20) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome del team non valido. Deve essere tra 3 e 20 caratteri.");
        }

        // 4. Controlla se esiste già un team con lo stesso nome
        if (teamRepository.existsByName(teamEntity.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Un team con questo nome esiste già.");
        }

        // 5. Aggiungi un ID univoco al team (se non specificato)
        if (teamEntity.getIdTeam() == null || teamEntity.getIdTeam().isEmpty()) {
            teamEntity.setIdTeam(Util.generateUniqueId());
        }

        // 6. Salva il team nel database
        TeamEntity savedTeamEntity = teamRepository.save(teamEntity);
        // 7. Crea una relazione tra Admin e Team

        TeamAdminEntity teamManagement = new TeamAdminEntity();

        AdminEntity admin = adminRepository.findByUsername(adminUsername);

        if (admin == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Admin not found");
        }

        teamManagement.setAdmin(admin);
        teamManagement.setTeam(savedTeamEntity);
        teamManagement.setRole("Owner");
        teamManagement.setActive(true);

        // 8. Salva la relazione nel database
        teamAdminRepository.save(teamManagement);
        // 9. Restituisci una risposta con il team creato
        return ResponseEntity.ok().body(savedTeamEntity);
    }

    // Elimina un team dato il nome del team
    @Override
    public ResponseEntity<?> deleteTeam(String idTeam, String jwt) {

        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 2. Estrai l'ID dell'admin dal JWT
        String adminUsername = jwtService.getAdminFromJwt(jwt);

        System.out.print("Id da eliminare: " + idTeam);

        // 3. Verifica che il team esista
        TeamEntity teamEntityToDelete = teamRepository.findById(idTeam).orElse(null);
        if (teamEntityToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con l'ID '" + idTeam + "' non trovato.");
        }

        // 4. Verifica che l'admin sia effettivamente associato a questo team come "Owner"
        TeamAdminEntity teamAdminEntity = teamAdminRepository.findByTeam_IdTeam(idTeam).orElse(null); //`findByTeamId` restituisca una sola associazione
        if (teamAdminEntity == null || !teamAdminEntity.getAdmin().getEmail().equals(adminUsername) || !"Owner".equals(teamAdminEntity.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per eliminare questo team.");
        }

        // 5. Elimina il team
        teamRepository.delete(teamEntityToDelete);

        // 6. Elimina l'associazione
        teamAdminRepository.delete(teamAdminEntity);

        // 7. Elimina gli Assignment associati al team
        List<AssignmentEntity> assignmentsToDelete = assignmentRepository.findByTeam_IdTeam(idTeam);
        if (assignmentsToDelete != null && !assignmentsToDelete.isEmpty()) {
            assignmentRepository.deleteAll(assignmentsToDelete);
            System.out.println("Eliminati " + assignmentsToDelete.size() + " assignment associati al team.");
        }

        // Restituisci una risposta di successo
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Team con ID '" + idTeam + "' eliminato con successo.");
    }

    // Modifica il nome di un team
    @Override
    public ResponseEntity<?> modificaNomeTeam(TeamUpdate request, @CookieValue(name = "jwt", required = false) String jwt) {
        String idTeam = request.getIdTeam();
        String newName = request.getNewName();

        System.out.println("IdTeam: " + idTeam + " newName: " + newName);

        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 2. Estrai l'ID dell'admin dal JWT
        String adminUsername = jwtService.getAdminFromJwt(jwt);

        // 3. Verifica se il team esiste
        TeamEntity existingTeamEntity = teamRepository.findById(idTeam).orElse(null);
        if (existingTeamEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con l'ID '" + idTeam + "' non trovato.");
        }

        // 4. Verifica che l'admin sia effettivamente associato a questo team come "Owner"
        TeamAdminEntity teamAdminEntity = teamAdminRepository.findByTeam_IdTeam(idTeam).orElse(null); // Assumiamo che `findByTeamId` restituisca una sola associazione
        if (teamAdminEntity == null || !teamAdminEntity.getAdmin().getEmail().equals(adminUsername) || !"Owner".equals(teamAdminEntity.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per modificare questo team.");
        }

        // 5. Verifica il nuovo nome del team

        //Modifica con nome nullo
        if (newName == null || newName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome team obbligatorio");
        }

        //Modifica con nome troppo lungo (massimo 255 caratteri)
        if (newName.length() > 20) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome team troppo lungo");
        }


        // 6. Verifica se il nuovo nome è già utilizzato da un altro team
        if (teamRepository.existsByName(newName)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Esiste già un team con il nome '" + newName + "'");
        }

        // 7. Modifica il nome del team
        existingTeamEntity.setName(newName);

        // 8. Salva il team aggiornato
        teamRepository.save(existingTeamEntity);

        // 9. Restituisci il team aggiornato
        return ResponseEntity.ok().body(existingTeamEntity);
    }

    // Metodo per visualizzare i team associati a un admin specifico
    @Override
    public ResponseEntity<?> visualizzaTeams(@CookieValue(name = "jwt", required = false) String jwt) {
        System.out.println("Recupero dei team associati all'Admin in corso...");

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
            List<TeamAdminEntity> teamAssociations = teamAdminRepository.findAllByAdmin_Email(adminUsername);
            if (teamAssociations == null || teamAssociations.isEmpty()) {
                return ResponseEntity.ok("Non sei associato ad alcun team.");
            }

            // 4. Estrai gli ID dei team associati
            List<String> teamIds = teamAssociations.stream()
                    .map(assoc -> assoc.getTeam().getIdTeam())
                    .collect(Collectors.toList());

            // 5. Recupera tutti i team associati
            List<TeamEntity> teamEntityList = (List<TeamEntity>) teamRepository.findAllById(teamIds);
            if (CollectionUtils.isEmpty(teamEntityList)) {
                return ResponseEntity.ok("Nessun team trovato per gli ID specificati.");
            }

            // 6. Restituisce i team trovati
            return ResponseEntity.ok(teamEntityList);

        } catch (Exception e) {
            // Gestione di eventuali errori inaspettati
            System.err.println("Errore durante il recupero dei team: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore durante il recupero dei team.");
        }
    }

    //Modifica 03/12/2024: Aggiunta della visualizzazione del singolo team
    @Override
    public ResponseEntity<?> cercaTeam(String idTeam, String jwt) {

        // Verifica se il token JWT è presente
        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 3. Verifica se il team esiste
        TeamEntity existingTeamEntity = teamRepository.findById(idTeam).orElse(null);
        if (existingTeamEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con l'ID '" + idTeam + "' non trovato.");
        }

        // Restituisce il team
        return ResponseEntity.ok().body(existingTeamEntity);
    }

    //Modifica 03/12/2024: Aggiunta dell'aggiungiStudenti
    @Override
    public ResponseEntity<?> aggiungiStudenti(String idTeam, List<String> idStudenti, String jwt) {
        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }
        // 2. Estrai l'ID dell'admin dal JWT
        String adminUsername = jwtService.getAdminFromJwt(jwt);
        // 3. Verifica se il team esiste
        TeamEntity existingTeamEntity = teamRepository.findById(idTeam).orElse(null);
        if (existingTeamEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con l'ID '" + idTeam + "' non trovato.");
        }
        // 4. Verifica che l'admin sia effettivamente associato a questo team come "Owner"
        TeamAdminEntity teamAdminEntity = teamAdminRepository.findByTeam_IdTeam(idTeam).orElse(null);
        if (teamAdminEntity == null || !teamAdminEntity.getAdmin().getEmail().equals(adminUsername) || !"Owner".equals(teamAdminEntity.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per modificare questo team.");
        }
        //4.1 Verifica che non ho un array di id vuoto!
        if (idStudenti.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Non hai selezionato nessuno studente.");
        }
        // 5. Filtra gli studenti già presenti nel team
        List<String> nuoviStudenti = idStudenti.stream()
                .filter(idStudente -> !existingTeamEntity.getIdStudenti().contains(idStudente))
                .collect(Collectors.toList());

        if (nuoviStudenti.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tutti gli studenti forniti sono già associati al team.");
        }

        // 6. Aggiungi gli studenti validi al team
        existingTeamEntity.getIdStudenti().addAll(nuoviStudenti);
        // 7. Aggiorna il numero di studenti
        existingTeamEntity.setNumeroStudenti(existingTeamEntity.getIdStudenti().size());
        // 8. Salva il team aggiornato
        TeamEntity updatedTeamEntity = teamRepository.save(existingTeamEntity);
        // 9. Recupero dettagli degli studenti per inviare le email.
        ResponseEntity<?> dettagliStudentiResponse = studentService.ottieniStudentiDettagli(nuoviStudenti, jwt);
        if (!HttpStatus.OK.equals(dettagliStudentiResponse.getStatusCode())) {
            return ResponseEntity.status(dettagliStudentiResponse.getStatusCode())
                    .body("Errore nel recupero delle informazioni sugli studenti: " + dettagliStudentiResponse.getBody());
        }

        // 10. Recupera i dettagli degli studenti
        List<Map<String, Object>> studentiDettagli = (List<Map<String, Object>>) dettagliStudentiResponse.getBody();
        List<String> emails = studentiDettagli.stream()
                .map(student -> (String) student.get("email"))
                .collect(Collectors.toList());

        // 11. Invia email di notifica agli studenti aggiunti

        try {
            emailService.sendTeamAdditionNotificationToStudents(emails, existingTeamEntity.getName());
        } catch (MessagingException e) {
            System.out.println("Errore durante l'invio della email.");
        }

        // 12. notifica l'utente nella pagina web
        String Title = "Aggiunto al Team ";
        String message = "Ora fai parte di " + existingTeamEntity.getName();
        for (String email : emails) {
            try {
                notificationService.sendNotification(email, null, Title, message, "Team");
            } catch (Exception e) {
                System.out.println("Errore durante l'invio della notifica.");
            }
        }

        // 10. Restituisci il team aggiornato come risposta
        return ResponseEntity.ok().body(updatedTeamEntity);
    }

    //Modifica 04/12/2024: Aggiunta ottieniStudentiTeam
    @Override
    public ResponseEntity<?> ottieniStudentiTeam(String idTeam, String jwt) {
        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 2. Estrai l'ID dell'admin dal JWT
        String adminUsername = jwtService.getAdminFromJwt(jwt);

        // 3. Verifica se il team esiste
        TeamEntity existingTeamEntity = teamRepository.findById(idTeam).orElse(null);
        if (existingTeamEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con l'ID '" + idTeam + "' non trovato.");
        }

        // 4. Verifica che l'admin sia effettivamente associato a questo team come "Owner"
        //In futuro si potrebbe prevedere che anche altri professori possano vedere gli studenti di un team
        TeamAdminEntity teamAdminEntity = teamAdminRepository.findByTeam_IdTeam(idTeam).orElse(null);
        if (teamAdminEntity == null || !teamAdminEntity.getAdmin().getEmail().equals(adminUsername) || !"Owner".equals(teamAdminEntity.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per visualizzare gli studenti di questo team.");
        }

        // 5. Recupera la lista degli id degli studenti dei team
        List<String> studentiIds = existingTeamEntity.getIdStudenti(); //Lista di id degli studenti
        if (studentiIds == null || studentiIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Non ci sono studenti associati a questo team.");
        }

        // 6. Invoca il servizio T23 per ottenere i dettagli degli utenti
        return ResponseEntity.ok(studentService.ottieniStudentiDettagli(studentiIds, jwt));
    }

    // Modifica 04/12/2024: Aggiunta rimuoviStudenteTeam
    @Override
    public ResponseEntity<?> rimuoviStudenteTeam(String idTeam, String idStudente, String jwt) {

        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 2. Estrai l'ID dell'admin dal JWT
        String adminUsername = jwtService.getAdminFromJwt(jwt);

        // 3. Verifica se il team esiste
        TeamEntity existingTeamEntity = teamRepository.findById(idTeam).orElse(null);
        if (existingTeamEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con l'ID '" + idTeam + "' non trovato.");
        }

        // 4. Verifica che l'admin sia effettivamente associato a questo team come "Owner"
        TeamAdminEntity teamAdminEntity = teamAdminRepository.findByTeam_IdTeam(idTeam).orElse(null);
        if (teamAdminEntity == null || !teamAdminEntity.getAdmin().getEmail().equals(adminUsername) || !"Owner".equals(teamAdminEntity.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per modificare questo team.");
        }

        // 5. Verifica se lo studente è effettivamente nel team
        if (!existingTeamEntity.getIdStudenti().contains(idStudente)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Studente con ID '" + idStudente + "' non trovato nel team.");
        }

        // 6. Rimuovi lo studente dal team
        existingTeamEntity.getIdStudenti().remove(idStudente);

        // 7. Aggiorna il numero di studenti
        existingTeamEntity.setNumeroStudenti(existingTeamEntity.getIdStudenti().size());

        // 8. Salva il team aggiornato
        TeamEntity updatedTeamEntity = teamRepository.save(existingTeamEntity);

        // 9. Restituisci il team aggiornato come risposta
        return ResponseEntity.ok().body(updatedTeamEntity);
    }


    /**
     * Restituisce il team associato allo studente.
     *
     * @param idStudente l'identificativo dello studente
     * @return il team a cui lo studente appartiene
     */
    @Override
    public TeamEntity getTeamByStudentId(String idStudente) {
        // Utilizzando il metodo di query derivata
        return teamRepository.findByIdStudenti(idStudente);
    }

    // Permetti a uno studente di vedere i componenti del proprio team
    @Override
    public ResponseEntity<?> GetStudentTeam(String studentId, String jwt) {
        // 1. Verifica se l'utente ha un team
        TeamEntity existingTeamEntity = getTeamByStudentId(studentId);
        if (existingTeamEntity == null) {
            //il team non esiste
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("L'utente non è associato a un Team");
        }
        // 2. Recupera la lista degli id degli studenti dei team
        List<String> studentiIds = existingTeamEntity.getIdStudenti(); //Lista di id degli studenti
        if (studentiIds == null || studentiIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Non ci sono studenti associati a questo team.");
        }
        // 3. Invoca il servizio T23 per ottenere i dettagli degli utenti
        return ResponseEntity.ok(studentService.ottieniStudentiDettagli(studentiIds, jwt));
    }
}
