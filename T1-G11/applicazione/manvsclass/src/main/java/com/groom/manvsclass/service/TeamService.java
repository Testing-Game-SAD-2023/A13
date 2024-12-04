/*
 * Crea - Elimina - 30/11/2024
 */

package com.groom.manvsclass.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.model.TeamAdmin;
import com.groom.manvsclass.model.repository.TeamAdminRepository;
import com.groom.manvsclass.model.repository.TeamRepository;

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
        if (team.getName() == null || team.getName().isEmpty() || team.getName().length() < 3 || team.getName().length() > 30) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome del team non valido. Deve essere tra 3 e 30 caratteri.");
        }

        // 4. Controlla se esiste già un team con lo stesso nome
        if (teamRepository.existsByName(team.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Un team con questo nome esiste già.");
        }

        // 5. Aggiungi un ID univoco al team (se non specificato)
        if (team.getIdTeam() == null || team.getIdTeam().isEmpty()) {
            team.setIdTeam(generateUniqueId());
        }

        // 6. Salva il team nel database
        Team savedTeam = teamRepository.save(team);

        // 7. Crea una relazione tra Admin e Team
        TeamAdmin teamManagement = new TeamAdmin(
                adminUsername,                      // ID dell'Admin -- Ussername.
                savedTeam.getIdTeam(),        // ID del Team appena creato
                savedTeam.getName(),            //Nome Team
                "Owner",                      // Ruolo (può essere parametrizzato)
                true                          // Relazione attiva
        );

        // 8. Salva la relazione nel database
        teamAdminRepository.save(teamManagement);

        // 9. Restituisci una risposta con il team creato
        return ResponseEntity.ok().body(savedTeam);
    }

    // Metodo per generare un ID univoco (esempio con UUID)
    //Modifica 04/12/2024
    public static String generateUniqueId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    // Elimina un team dato il nome del team
    public ResponseEntity<?> deleteTeam(String idTeam, String jwt) {
        
        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }
        
        // 2. Estrai l'ID dell'admin dal JWT
        String adminUsername = jwtService.getAdminFromJwt(jwt);

        System.out.print("Id da eliminare: "+idTeam);

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

        // Restituisci una risposta di successo
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Team con ID '" + idTeam + "' eliminato con successo.");
    }

    // Modifica il nome di un team
    public ResponseEntity<?> modificaNomeTeam(TeamModificationRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
        String idTeam = request.getIdTeam();
        String newName = request.getNewName();

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

        // 5. Modifica il nome del team
        existingTeam.setName(newName);
        teamAdmin.setTeamName(newName);

        // 6. Salva il team aggiornato
        Team updatedTeam = teamRepository.save(existingTeam);
        TeamAdmin updatedTeamAdmin = teamAdminRepository.save(teamAdmin);
        // 7. Restituisci il team aggiornato
        return ResponseEntity.ok().body(updatedTeam);
    }

    // Metodo per visualizzare i team associati a un admin specifico
    public ResponseEntity<?> visualizzaTeams(String jwt) {
        try {
            // Estrae l'username dell'admin dal JWT
            String adminUsername = jwtService.getAdminFromJwt(jwt);

            if (adminUsername == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token non valido o scaduto.");
            }

            // Recupera i team associati a quell'admin
            List<TeamAdmin> teamAssociati = teamAdminRepository.findAllByAdminId(adminUsername);

            // Se non ci sono associazioni
            if (teamAssociati.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nessun team associato trovato.");
            }

            // Estrai gli ID dei team associati
            List<String> teamIds = teamAssociati.stream() //crea uno stream a partire dalla lista
                                            .map(TeamAdmin::getTeamId) //ogni oggetto TeamAdmin nello stream viene trasformato nel valore restituito da getTeamId 
                                            .collect(Collectors.toList()); //L'operazione collect terminale converte lo stream risultante in una lista.

            // Recupera tutti i team in un'unica query
            List<Team> teams = (List<Team>) teamRepository.findAllById(teamIds);

            // Se non ci sono team trovati (possibile mismatch)
            if (teams.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nessun team trovato per gli ID specificati.");
            }

            // Restituisce i team associati
            return ResponseEntity.ok(teams);

        } catch (Exception e) {
            // Log dell'eccezione
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel recupero dei team: " + e.getMessage());
        }
    }

    //Modifica 03/12/2024: Aggiunta della visualizzazione del singolo team
    public  ResponseEntity<?> visualizzaTeam(String idTeam, String jwt) {    

        // Verifica se il token JWT è presente
        // 1. Verifica se il token JWT è valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }
        
        // 3. Verifica se il team esiste
        Team existingTeam = teamRepository.findById(idTeam).orElse(null);
        if (existingTeam == null){
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

        // 5. Filtra gli studenti già presenti nel team
        List<String> nuoviStudenti = idStudenti.stream()
            .filter(idStudente -> !existingTeam.getStudenti().contains(idStudente))
            .collect(Collectors.toList()); // Modifica per utilizzare Collectors.toList()

        if (nuoviStudenti.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tutti gli studenti forniti sono già associati al team.");
        }

        // 6. Aggiungi gli studenti validi al team
        existingTeam.getStudenti().addAll(nuoviStudenti);

        // 7. Aggiorna il numero di studenti
        existingTeam.setNumStudenti(existingTeam.getStudenti().size());

        // 8. Salva il team aggiornato
        Team updatedTeam = teamRepository.save(existingTeam);

        // 9. Restituisci il team aggiornato come risposta
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
        return ResponseEntity.ok(studentService.ottieniStudentiDettagli(studentiIds,jwt));
    }

}


