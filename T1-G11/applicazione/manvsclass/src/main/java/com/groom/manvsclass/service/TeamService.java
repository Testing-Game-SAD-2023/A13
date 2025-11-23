/*
 * Crea - Elimina - 30/11/2024
 */

package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Assignment;
import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.model.Team;

import com.groom.manvsclass.repository.AssignmentRepository;
import com.groom.manvsclass.repository.AdminRepository;
import com.groom.manvsclass.repository.TeamRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

import jakarta.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private AdminRepository adminRepository;

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

        // 2. Estrai l'email dell'Admin dal token JWT
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        if (adminEmail == null || adminEmail.isEmpty()) {
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

        Optional<Admin> adminOpt = adminRepository.findById(adminEmail);
        if (adminOpt.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin non trovato.");
        }

        Admin teamAdmin = adminOpt.get();
        team.setAdmin(teamAdmin);
        team.setAdminRole("Owner");

        // 6. Salva il team nel database
        Team savedTeam = teamRepository.save(team);

        // 9. Restituisci una risposta con il team creato
        return ResponseEntity.ok().body(savedTeam);
    }

    // Elimina un team dato il nome del team
    public ResponseEntity<?> deleteTeam(String teamName, String jwt) {

        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 2. Estrai l'ID dell'admin dal JWT
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);
	if (adminEmail == null || adminEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile identificare l'admin dal token JWT");
        }
        System.out.print("Team da eliminare: " + teamName);

        // 3. Verifica che il team esista
        Optional<Team> teamToDeleteOpt = teamRepository.findByName(teamName);
        if (teamToDeleteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team " + teamName + " non trovato.");
        }
        Team teamToDelete = teamToDeleteOpt.get();

        // 4. Verifica che l'admin sia effettivamente associato a questo team come "Owner"
        if (teamToDelete.getAdmin() == null || !teamToDelete.getAdmin().getEmail().equals(adminEmail) || !"Owner".equals(teamToDelete.getAdminRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per eliminare questo team.");
        }

        teamRepository.delete(teamToDelete);

        // Restituisci una risposta di successo
        return ResponseEntity.status(HttpStatus.OK).body("Team " + teamName + " eliminato con successo.");
    }

    // Modifica il nome di un team
    public ResponseEntity<?> modificaNomeTeam(TeamModificationRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
        String oldName = request.getTeamOldName();
        String newName = request.getTeamNewName();

        System.out.println("Team: " + oldName + " newName: " + newName);

        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 2. Estrai l'ID dell'admin dal JWT
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        // 3. Verifica se il team esiste
        Optional<Team> teamOpt = teamRepository.findByName(oldName);
        if (teamOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team " + oldName + "' non trovato.");
        }

        Team existingTeam = teamOpt.get();

        // 4. Verifica che l'admin sia effettivamente associato a questo team come "Owner"
        if (existingTeam.getAdmin() == null || !existingTeam.getAdmin().getEmail().equals(adminEmail) || !"Owner".equals(existingTeam.getAdminRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per modificare questo team.");
        }

        // 5. Verifica il nuovo nome del team

        //Modifica con nome nullo
        if (newName == null || newName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome team obbligatorio");
        }

        //Modifica con nome troppo lungo (massimo 20 caratteri)
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
            String adminEmail = jwtService.getAdminEmailFromJwt(jwt);
            if (adminEmail == null || adminEmail.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile identificare l'Admin dal token JWT.");
            }

            // 3
            List<Team> teams = teamRepository.findByAdmin_Email(adminEmail);
            if (teams == null || teams.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nessun team trovato per l'Admin specificato.");
            }

            // 4. Restituisce i team trovati
            return ResponseEntity.ok(teams);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore durante il recupero dei team.");
        }
    }

    //Modifica 03/12/2024: Aggiunta della visualizzazione del singolo team
    public ResponseEntity<?> cercaTeam(String teamName, String jwt) {

        // Verifica se il token JWT è presente
        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 3. Verifica se il team esiste
        Optional<Team> teamOpt = teamRepository.findByName(teamName);
        if (teamOpt == null || teamOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team " + teamName + " non trovato.");
        }

        Team team = teamOpt.get();
        // Restituisce il team
        return ResponseEntity.ok().body(team);
    }

    //Modifica 03/12/2024: Aggiunta dell'aggiungiStudenti
    public ResponseEntity<?> aggiungiStudenti(String teamName, List<String> studentIds, String jwt) {
        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }
        // 2. Estrai l'ID dell'admin dal JWT
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);
        // 3. Verifica se il team esiste
        Optional<Team> teamOpt = teamRepository.findByName(teamName);
        if (teamOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team " + teamName + " non trovato.");
        }

        Team existingTeam = teamOpt.get();

        // 4. Verifica che l'admin sia effettivamente associato a questo team come "Owner"
        if (existingTeam.getAdmin() == null || !existingTeam.getAdmin().getEmail().equals(adminEmail) || !"Owner".equals(existingTeam.getAdminRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per modificare questo team.");
        }
        //4.1 Verifica che non ho un array di id vuoto!
        if (studentIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Non hai selezionato nessuno studente.");
        }
        // 5. Filtra gli studenti già presenti nel team
        List<String> newStudents = studentIds.stream()
                .filter(studentId -> !existingTeam.getStudentIds().contains(studentIds))
                .collect(Collectors.toList());

        if (newStudents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tutti gli studenti forniti sono già associati al team.");
        }

        // 6. Aggiungi gli studenti validi al team
        existingTeam.getStudentIds().addAll(newStudents);
        // 7. Aggiorna il numero di studenti
        existingTeam.setNumStudents(existingTeam.getStudentIds().size());
        // 8. Salva il team aggiornato
        Team updatedTeam = teamRepository.save(existingTeam);
        // 9. Recupero dettagli degli studenti per inviare le email.
        ResponseEntity<?> dettagliStudentiResponse = studentService.ottieniStudentiDettagli(newStudents, jwt);
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
    public ResponseEntity<?> ottieniStudentiTeam(String teamName, String jwt) {
        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 2. Estrai l'ID dell'admin dal JWT
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        // 3. Verifica se il team esiste
        Optional<Team> teamOpt = teamRepository.findByName(teamName);
        if (teamOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team " + teamName + " non trovato.");
        }

        Team existingTeam = teamOpt.get();

        // 4. Verifica che l'admin sia effettivamente associato a questo team come "Owner"
        //In futuro si potrebbe prevedere che anche altri professori possano vedere gli studenti di un team
        if (existingTeam.getAdmin() == null || !existingTeam.getAdmin().getEmail().equals(adminEmail) || !"Owner".equals(existingTeam.getAdminRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per visualizzare gli studenti di questo team.");
        }

        // 5. Recupera la lista degli id degli studenti dei team
        List<String> studentIds = existingTeam.getStudentIds(); //Lista di id degli studenti
        if (studentIds == null || studentIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Non ci sono studenti associati a questo team.");
        }

        // 6. Invoca il servizio T23 per ottenere i dettagli degli utenti
        return ResponseEntity.ok(studentService.ottieniStudentiDettagli(studentIds, jwt));
    }

    // Modifica 04/12/2024: Aggiunta rimuoviStudenteTeam
    public ResponseEntity<?> rimuoviStudenteTeam(String teamName, String studentId, String jwt) {

        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 2. Estrai l'ID dell'admin dal JWT
        String adminEmail = jwtService.getAdminEmailFromJwt(jwt);

        // 3. Verifica se il team esiste
        Optional<Team> teamOpt = teamRepository.findByName(teamName);
        if (teamOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team " + teamName + " non trovato.");
        }

        Team existingTeam = teamOpt.get();

        // 4. Verifica che l'admin sia effettivamente associato a questo team come "Owner"
        if (existingTeam.getAdmin() == null || !existingTeam.getAdmin().getEmail().equals(adminEmail) || !"Owner".equals(existingTeam.getAdminRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per modificare questo team.");
        }

        // 5. Verifica se lo studente è effettivamente nel team
        if (!existingTeam.getStudentIds().contains(studentId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Studente con ID '" + studentId + "' non trovato nel team.");
        }

        // 6. Rimuovi lo studente dal team
        existingTeam.getStudentIds().remove(studentId);

        // 7. Aggiorna il numero di studenti
        existingTeam.setNumStudents(existingTeam.getStudentIds().size());

        // 8. Salva il team aggiornato
        Team updatedTeam = teamRepository.save(existingTeam);

        // 9. Restituisci il team aggiornato come risposta
        return ResponseEntity.ok().body(updatedTeam);
    }

    public Optional<Team> getTeamByStudentId(String studentId) {

        return teamRepository.findByStudentId(studentId);
    }

    // Permetti a uno studente di vedere i componenti del proprio team 
    public ResponseEntity<?> getStudentTeam(String studentId, String jwt) {
        // 1. Verifica se l'utente ha un team 
        Optional<Team> teamOpt = getTeamByStudentId(studentId);
        if (teamOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utente non è associato a un Team");
        }

        Team existingTeam = teamOpt.get();

        // 2. Recupera la lista degli id degli studenti dei team
        List<String> studentIds = existingTeam.getStudentIds(); // Lista di id degli studenti
        if (studentIds == null || studentIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Non ci sono studenti associati a questo team.");
        }
        // 3. Invoca il servizio T23 per ottenere i dettagli degli utenti
        return ResponseEntity.ok(studentService.ottieniStudentiDettagli(studentIds, jwt));
    }

}





