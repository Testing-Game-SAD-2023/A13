package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Assignment;
import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.dto.TeamModificationRequest;
import com.groom.manvsclass.dto.TeamDTO;
import com.groom.manvsclass.mapper.TeamMapper;
import com.groom.manvsclass.service.JwtService;

import com.groom.manvsclass.repository.AssignmentRepository;
import com.groom.manvsclass.repository.AdminRepository;
import com.groom.manvsclass.repository.TeamRepository;

import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.exception.DuplicatedEntryException;
import com.groom.manvsclass.exception.ForbiddenException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StudentService studentService; //Servizio per mandare query al T23

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TeamMapper teamMapper;

    @Transactional
    public void createTeam(TeamDTO teamDTO, String adminEmail) {

        if (teamRepository.existsByName(teamDTO.getName())) {
            throw new DuplicatedEntryException("Team " + teamDTO.getName() + " già esistente.");
        }

        Team teamToSave = teamMapper.toEntity(teamDTO);

        Optional<Admin> adminOpt = adminRepository.findById(adminEmail);
        if (adminOpt.isEmpty()) {
            throw new NotFoundException("Admin " + adminEmail + " non trovato.");
        }

        teamToSave.setAdmin(adminOpt.get());

        teamRepository.save(teamToSave);
    }

    public void deleteTeam(String teamName, String adminEmail) {

        Optional<Team> teamOpt = teamRepository.findByName(teamName);
        if (teamOpt.isEmpty()) {
            throw new NotFoundException("Team " + teamName + " non trovato.");
        }

        Team teamToDelete = teamOpt.get();

        if (!teamToDelete.getAdmin().getEmail().equals(adminEmail)) {
            throw new ForbiddenException("L'admin " + adminEmail + " non ha i permessi per eliminare il team.");
        }

        teamRepository.delete(teamToDelete);
    }

    public void modificaNomeTeam(TeamModificationRequest request, String adminEmail) {

        String newName = request.getNewName();
        String oldName = request.getOldName();

        Optional<Team> teamOpt = teamRepository.findByName(oldName);
        if (teamOpt.isEmpty()) {
            throw new NotFoundException("Team " + oldName + "' non trovato.");
        }

        Team existingTeam = teamOpt.get();

        if (!existingTeam.getAdmin().getEmail().equals(adminEmail)) {
            throw new ForbiddenException("Non hai i permessi per modificare questo team.");
        }

        if (teamRepository.existsByName(newName)) {
            throw new DuplicatedEntryException("Esiste già un team con il nome '" + newName + "'");
        }

        existingTeam.setName(newName);

        teamRepository.save(existingTeam);

    }

    public List<TeamDTO> findAdminTeams(String adminEmail) {

        List<Team> teams = teamRepository.findByAdmin_Email(adminEmail);
        if (teams.isEmpty()) {
            throw new NotFoundException("Nessun team trovato per l'Admin " + adminEmail);
        }

        return teamMapper.toDtoList(teams);
    }

    public TeamDTO findAdminTeam(String teamName, String adminEmail) {

        Optional<Team> teamOpt = teamRepository.findByAdmin_EmailAndName(adminEmail, teamName);
        if (teamOpt.isEmpty()) {
            throw new NotFoundException("Team " + teamName + " non trovato per l'admin " + adminEmail);
        }

        TeamDTO teamDTO = teamMapper.toDto(teamOpt.get());

        return teamDTO;
    }

    public ResponseEntity<?> addStudents(String teamName, List<String> studentIds, String adminEmail) {

        Optional<Team> teamOpt = teamRepository.findByName(teamName);
        if (teamOpt.isEmpty()) {
            throw new NotFoundException("Team " + teamName + " non trovato.");
        }

        Team team = teamOpt.get();

        if (team.getAdmin().getEmail().equals(adminEmail)) {
            throw new ForbiddenException("Non hai i permessi per modificare questo team.");
        }
        //4.1 Verifica che non ho un array di id vuoto!
        if (studentIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Non hai selezionato nessuno studente.");
        }
        // 5. Filtra gli studenti già presenti nel team
        List<String> newStudents = studentIds.stream()
                .filter(studentId -> !team.getStudentIds().contains(studentIds))
                .collect(Collectors.toList());

        if (newStudents.isEmpty()) {
            throw new DuplicatedEntryException("Tutti gli studenti forniti sono già associati al team.");
        }

        // 6. Aggiungi gli studenti validi al team
        team.getStudentIds().addAll(newStudents);
        // 7. Aggiorna il numero di studenti
        team.setNumStudents(team.getStudentIds().size());
        // 8. Salva il team aggiornato
        Team updatedTeam = teamRepository.save(team);
        // 9. Recupero dettagli degli studenti per inviare le email.
        ResponseEntity<?> dettagliStudentiResponse = studentService.ottieniStudentiDettagli(newStudents);
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
            emailService.sendTeamAdditionNotificationToStudents(emails, updatedTeam.getName());
        } catch (MessagingException e) {
            System.out.println("Errore durante l'invio della email.");
        }

        // 12. notifica l'utente nella pagina web
        String Title = "Aggiunto al Team ";
        String message = "Ora fai parte di " + updatedTeam.getName();
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
        if (existingTeam.getAdmin() == null || !existingTeam.getAdmin().getEmail().equals(adminEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non hai i permessi per visualizzare gli studenti di questo team.");
        }

        // 5. Recupera la lista degli id degli studenti dei team
        List<String> studentIds = existingTeam.getStudentIds(); //Lista di id degli studenti
        if (studentIds == null || studentIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Non ci sono studenti associati a questo team.");
        }

        // 6. Invoca il servizio T23 per ottenere i dettagli degli utenti
        return ResponseEntity.ok(studentService.ottieniStudentiDettagli(studentIds));
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
        if (existingTeam.getAdmin() == null || !existingTeam.getAdmin().getEmail().equals(adminEmail)) {
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

    public Team getTeamByStudentId(String studentId) {

        Optional<Team> teamOpt = teamRepository.findByStudentId(studentId);
        if (teamOpt.isEmpty()) {
            throw new NotFoundException("L'utente non è associato a un Team");
        }

        return teamOpt.get();
    }


    public ResponseEntity<?> getStudentTeam(String studentId, String jwt) {

        Team studentTeam = new Team();

        try {

            studentTeam = getTeamByStudentId(studentId);

        }   catch (NotFoundException e) {

            throw e;
        }

        List<String> studentIds = studentTeam.getStudentIds();

        if (studentIds == null || studentIds.isEmpty()) {

            throw new NotFoundException("Non ci sono studenti associati a questo team.");
        }
        // 3. Invoca il servizio T23 per ottenere i dettagli degli utenti
        return ResponseEntity.ok(studentService.ottieniStudentiDettagli(studentIds));
    }

}





