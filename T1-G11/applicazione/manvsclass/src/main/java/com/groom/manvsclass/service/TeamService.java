/*
 * Crea - Elimina - 30/11/2024
 */

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
import org.springframework.web.bind.annotation.CookieValue;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamAdminRepository teamAdminRepository;

    @Autowired
    private JwtService jwtService;  // Servizio per la validazione del JWT

    @Autowired
    private StudentService studentService; //Servizio per mandare query al T23

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    //Metodo per creare un nuovo Team
    public ResponseEntity<?> creaTeam(Team team, @CookieValue(name = "jwt", required = false) String jwt) {

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
        if (team.getName() == null || team.getName().isEmpty() || team.getName().length() < 3 || team.getName().length() > 20) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome del team non valido. Deve essere tra 3 e 20 caratteri.");
        }

        // 4. Controlla se esiste già un team con lo stesso nome
        if (teamRepository.existsByName(team.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Un team con questo nome esiste già.");
        }

        // 5. Aggiungi un ID univoco al team (se non specificato)
        if (team.getIdTeam() == null || team.getIdTeam().isEmpty()) {
            team.setIdTeam(Util.generateUniqueId());
        }

        // 6. Salva il team nel database
        Team savedTeam = teamRepository.save(team);
        // 7. Crea una relazione tra Admin e Team
        TeamAdmin teamManagement = new TeamAdmin(
                adminUsername,                           // ID dell'Admin -- Ussername.
                savedTeam.getIdTeam(),                   // ID del Team appena creato
                savedTeam.getName(),                     //Nome Team
                "Owner",                            // Ruolo (può essere parametrizzato)
                true                            // Relazione attiva
        );

        // 8. Salva la relazione nel database
        teamAdminRepository.save(teamManagement);
        // 9. Restituisci una risposta con il team creato
        return ResponseEntity.ok().body(savedTeam);
    }

    // Elimina un team dato il nome del team
    public ResponseEntity<?> deleteTeam(String idTeam, String jwt) {

        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 2. Estrai l'ID dell'admin dal JWT
        String adminUsername = jwtService.getAdminFromJwt(jwt);

        System.out.print("Id da eliminare: " + idTeam);

        // 3. Verifica che il team esista
        Team teamToDelete = teamRepository.findById(idTeam).orElse(null);
        if (teamToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con l'ID '" + idTeam + "' non trovato.");
        }

        // 4. Verifica che l'admin sia effettivamente associato a questo team come "Owner"
        TeamAdmin teamAdmin = teamAdminRepository.findByTeamId(idTeam); //`findByTeamId` restituisca una sola associazione
        if (teamAdmin == null || !teamAdmin.getAdminId().equals(adminUsername) || !"Owner".equals(teamAdmin.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per eliminare questo team.");
        }

        // 5. Elimina il team
        teamRepository.delete(teamToDelete);

        // 6. Elimina l'associazione
        teamAdminRepository.delete(teamAdmin);

        // 7. Elimina gli Assignment associati al team
        List<Assignment> assignmentsToDelete = assignmentRepository.findByTeamId(idTeam);
        if (assignmentsToDelete != null && !assignmentsToDelete.isEmpty()) {
            assignmentRepository.deleteAll(assignmentsToDelete);
            System.out.println("Eliminati " + assignmentsToDelete.size() + " assignment associati al team.");
        }

        // Restituisci una risposta di successo
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Team con ID '" + idTeam + "' eliminato con successo.");
    }

    // Modifica il nome di un team
    public ResponseEntity<?> modificaNomeTeam(TeamModificationRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
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
        Team existingTeam = teamRepository.findById(idTeam).orElse(null);
        if (existingTeam == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con l'ID '" + idTeam + "' non trovato.");
        }

        // 4. Verifica che l'admin sia effettivamente associato a questo team come "Owner"
        TeamAdmin teamAdmin = teamAdminRepository.findByTeamId(idTeam); // Assumiamo che `findByTeamId` restituisca una sola associazione
        if (teamAdmin == null || !teamAdmin.getAdminId().equals(adminUsername) || !"Owner".equals(teamAdmin.getRole())) {
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
        existingTeam.setName(newName);

        // 8. Salva il team aggiornato
        teamRepository.save(existingTeam);

        // 9. Restituisci il team aggiornato
        return ResponseEntity.ok().body(existingTeam);
    }

    // Metodo per visualizzare i team associati a un admin specifico
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
            List<TeamAdmin> teamAssociations = teamAdminRepository.findAllByAdminId(adminUsername);
            if (teamAssociations == null || teamAssociations.isEmpty()) {
                return ResponseEntity.ok("Non sei associato ad alcun team.");
            }

            // 4. Estrai gli ID dei team associati
            List<String> teamIds = teamAssociations.stream()
                    .map(TeamAdmin::getTeamId)
                    .collect(Collectors.toList());

            // 5. Recupera tutti i team associati
            List<Team> teams = (List<Team>) teamRepository.findAllById(teamIds);
            if (teams == null || teams.isEmpty()) {
                return ResponseEntity.ok("Nessun team trovato per gli ID specificati.");
            }

            // 6. Restituisce i team trovati
            return ResponseEntity.ok(teams);

        } catch (Exception e) {
            // Gestione di eventuali errori inaspettati
            System.err.println("Errore durante il recupero dei team: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore durante il recupero dei team.");
        }
    }

    //Modifica 03/12/2024: Aggiunta della visualizzazione del singolo team
    public ResponseEntity<?> cercaTeam(String idTeam, String jwt) {

        // Verifica se il token JWT è presente
        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 3. Verifica se il team esiste
        Team existingTeam = teamRepository.findById(idTeam).orElse(null);
        if (existingTeam == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con l'ID '" + idTeam + "' non trovato.");
        }

        // Restituisce il team
        return ResponseEntity.ok().body(existingTeam);
    }

    //Modifica 03/12/2024: Aggiunta dell'aggiungiStudenti
    public ResponseEntity<?> aggiungiStudenti(String idTeam, List<String> idStudenti, String jwt) {
        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }
        // 2. Estrai l'ID dell'admin dal JWT
        String adminUsername = jwtService.getAdminFromJwt(jwt);
        // 3. Verifica se il team esiste
        Team existingTeam = teamRepository.findById(idTeam).orElse(null);
        if (existingTeam == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con l'ID '" + idTeam + "' non trovato.");
        }
        // 4. Verifica che l'admin sia effettivamente associato a questo team come "Owner"
        TeamAdmin teamAdmin = teamAdminRepository.findByTeamId(idTeam);
        if (teamAdmin == null || !teamAdmin.getAdminId().equals(adminUsername) || !"Owner".equals(teamAdmin.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per modificare questo team.");
        }
        //4.1 Verifica che non ho un array di id vuoto!
        if (idStudenti.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Non hai selezionato nessuno studente.");
        }
        // 5. Filtra gli studenti già presenti nel team
        List<String> nuoviStudenti = idStudenti.stream()
                .filter(idStudente -> !existingTeam.getStudenti().contains(idStudente))
                .collect(Collectors.toList());

        if (nuoviStudenti.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tutti gli studenti forniti sono già associati al team.");
        }

        // 6. Aggiungi gli studenti validi al team
        existingTeam.getStudenti().addAll(nuoviStudenti);
        // 7. Aggiorna il numero di studenti
        existingTeam.setNumStudenti(existingTeam.getStudenti().size());
        // 8. Salva il team aggiornato
        Team updatedTeam = teamRepository.save(existingTeam);
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
            emailService.sendTeamAdditionNotificationToStudents(emails, existingTeam.getName());
        } catch (MessagingException e) {
            System.out.println("Errore durante l'invio della email.");
        }

        // 12. notifica l'utente nella pagina web
        String Title = "Aggiunto al Team ";
        String message = "Ora fai parte di " + existingTeam.getName();
        for (String email : emails) {
            try {
                notificationService.sendNotification(email, null, Title, message, "Team");
            } catch (Exception e) {
                System.out.println("Errore durante l'invio della notifica.");
            }
        }

        // 10. Restituisci il team aggiornato come risposta
        return ResponseEntity.ok().body(updatedTeam);
    }

    //Modifica 04/12/2024: Aggiunta ottieniStudentiTeam
    public ResponseEntity<?> ottieniStudentiTeam(String idTeam, String jwt) {
        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 2. Estrai l'ID dell'admin dal JWT
        String adminUsername = jwtService.getAdminFromJwt(jwt);

        // 3. Verifica se il team esiste
        Team existingTeam = teamRepository.findById(idTeam).orElse(null);
        if (existingTeam == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con l'ID '" + idTeam + "' non trovato.");
        }

        // 4. Verifica che l'admin sia effettivamente associato a questo team come "Owner"
        //In futuro si potrebbe prevedere che anche altri professori possano vedere gli studenti di un team
        TeamAdmin teamAdmin = teamAdminRepository.findByTeamId(idTeam);
        if (teamAdmin == null || !teamAdmin.getAdminId().equals(adminUsername) || !"Owner".equals(teamAdmin.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per visualizzare gli studenti di questo team.");
        }

        // 5. Recupera la lista degli id degli studenti dei team
        List<String> studentiIds = existingTeam.getStudenti(); //Lista di id degli studenti
        if (studentiIds == null || studentiIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Non ci sono studenti associati a questo team.");
        }

        // 6. Invoca il servizio T23 per ottenere i dettagli degli utenti
        return ResponseEntity.ok(studentService.ottieniStudentiDettagli(studentiIds, jwt));
    }

    // Modifica 04/12/2024: Aggiunta rimuoviStudenteTeam
    public ResponseEntity<?> rimuoviStudenteTeam(String idTeam, String idStudente, String jwt) {

        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 2. Estrai l'ID dell'admin dal JWT
        String adminUsername = jwtService.getAdminFromJwt(jwt);

        // 3. Verifica se il team esiste
        Team existingTeam = teamRepository.findById(idTeam).orElse(null);
        if (existingTeam == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con l'ID '" + idTeam + "' non trovato.");
        }

        // 4. Verifica che l'admin sia effettivamente associato a questo team come "Owner"
        TeamAdmin teamAdmin = teamAdminRepository.findByTeamId(idTeam);
        if (teamAdmin == null || !teamAdmin.getAdminId().equals(adminUsername) || !"Owner".equals(teamAdmin.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per modificare questo team.");
        }

        // 5. Verifica se lo studente è effettivamente nel team
        if (!existingTeam.getStudenti().contains(idStudente)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Studente con ID '" + idStudente + "' non trovato nel team.");
        }

        // 6. Rimuovi lo studente dal team
        existingTeam.getStudenti().remove(idStudente);

        // 7. Aggiorna il numero di studenti
        existingTeam.setNumStudenti(existingTeam.getStudenti().size());

        // 8. Salva il team aggiornato
        Team updatedTeam = teamRepository.save(existingTeam);

        // 9. Restituisci il team aggiornato come risposta
        return ResponseEntity.ok().body(updatedTeam);
    }


    /**
     * Restituisce il team associato allo studente.
     *
     * @param idStudente l'identificativo dello studente
     * @return il team a cui lo studente appartiene
     */
    public Team getTeamByStudentId(String idStudente) {
        // Utilizzando il metodo di query derivata
        return teamRepository.findByIdStudenti(idStudente);
    }

    // Permetti a uno studente di vedere i componenti del proprio team 
    public ResponseEntity<?> GetStudentTeam(String studentId, String jwt) {
        // 1. Verifica se l'utente ha un team 
        Team existingTeam = getTeamByStudentId(studentId);
        if (existingTeam == null) {
            //il team non esiste 
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("L'utente non è associato a un Team");
        }
        // 2. Recupera la lista degli id degli studenti dei team
        List<String> studentiIds = existingTeam.getStudenti(); //Lista di id degli studenti
        if (studentiIds == null || studentiIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Non ci sono studenti associati a questo team.");
        }
        // 3. Invoca il servizio T23 per ottenere i dettagli degli utenti
        return ResponseEntity.ok(studentService.ottieniStudentiDettagli(studentiIds, jwt));
    }


}





