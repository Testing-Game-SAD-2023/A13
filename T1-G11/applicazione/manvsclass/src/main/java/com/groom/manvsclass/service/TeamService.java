/*
 * Crea - Elimina - 30/11/2024
 */

package com.groom.manvsclass.service;

import java.util.List;

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

    /**
     * Crea un nuovo team e associa l'Admin.
     *
     * @param team Il team da creare.
     * @param jwt Il token JWT per identificare l'Admin.
     * @return Una risposta HTTP con il risultato dell'operazione.
     */
    public ResponseEntity<?> creaTeam(Team team, @CookieValue(name = "jwt", required = false) String jwt) {

        System.out.println("Creazione del team in corso...");

        // 1. Verifica che il token JWT sia valido
        if (jwt == null || jwt.isEmpty() || !jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido o mancante.");
        }

        // 2. Estrai l'ID dell'Admin dal token JWT -> Sarebbe l'email
        String adminID = jwtService.getAdminFromJwt(jwt);
        System.out.println("Admin email: "+adminID);
        if (adminID == null || adminID.isEmpty()) {
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
                adminID,                      // ID dell'Admin -- Email.
                savedTeam.getIdTeam(),        // ID del Team appena creato
                "Owner",                      // Ruolo (può essere parametrizzato)
                true                          // Relazione attiva
        );

        // 8. Salva la relazione nel database
        teamAdminRepository.save(teamManagement);

        // 9. Restituisci una risposta con il team creato
        return ResponseEntity.ok().body(savedTeam);
    }

    // Metodo per generare un ID univoco (esempio con UUID)
    private String generateUniqueId() {
        return java.util.UUID.randomUUID().toString();
    }

    
     // Elimina un team dato il nome del team
      //, @CookieValue(name = "jwt", required = false) String jwt
    public ResponseEntity<?> deleteTeam(String id) {
        // Verifica se esiste un team con l'ID fornito
        Team teamToDelete = teamRepository.findById(id).orElse(null); 
        if (teamToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con l'ID '" + id + "' non trovato.");
        }

        // Elimina il team
        teamRepository.delete(teamToDelete);

        // Restituisci una risposta di successo
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Team con ID '" + id + "' eliminato con successo.");
    }


    // Modifica il nome di un team
     //, @CookieValue(name = "jwt", required = false) String jwt
    public ResponseEntity<?> modificaNomeTeam(TeamModificationRequest request) {
        String idTeam = request.getIdTeam();
        String newName = request.getNewName();

        // Verifica se il team esiste
        Team existingTeam = teamRepository.findById(idTeam).orElse(null);
        if (existingTeam == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team con l'ID '" + idTeam + "' non trovato.");
        }

        // Modifica il nome del team
        existingTeam.setName(newName);

        // Salva il team aggiornato
        Team updatedTeam = teamRepository.save(existingTeam);

        // Restituisci il team aggiornato
        return ResponseEntity.ok().body(updatedTeam);
    }

    // Metodo per visualizzare tutti i team
    public ResponseEntity<?> visualizzaTeams() {
        // Recupera tutti i team dal database
        List<Team> teams = teamRepository.findAll();
        
        // Verifica se ci sono team nel database
        if (teams.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nessun team trovato.");
        }

        // Restituisci i team
        return ResponseEntity.ok().body(teams);
    }

}

