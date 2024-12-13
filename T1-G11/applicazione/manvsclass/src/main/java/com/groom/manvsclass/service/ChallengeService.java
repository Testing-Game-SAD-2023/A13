package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Challenge;
import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.model.VictoryConditionType;
import com.groom.manvsclass.model.repository.ChallengeRepository;
import com.groom.manvsclass.model.repository.ChallengeSearchImpl;
import com.groom.manvsclass.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map; // Importa Map
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List; // Se utilizzi anche List
import java.util.List;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeSearchImpl searchRepository;

    @Autowired
    private JwtService jwtService;

     @Autowired
    private RestTemplate restTemplate; // Per effettuare chiamate HTTP

    /**
     * Crea una nuova challenge.
     */
    public ResponseEntity<Challenge> createChallenge(Challenge challenge, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    
        if (challengeRepository.existsById(challenge.getChallengeName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // La challenge esiste già
        }
    
        // Aggiungi il tipo di condizione di vittoria e la condizione
        if (challenge.getVictoryConditionType() == null || challenge.getVictoryCondition() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Parametri mancanti
        }
    
        searchRepository.addChallenge(challenge);
        return ResponseEntity.status(HttpStatus.CREATED).body(challenge);
    }

    /**
     * Recupera una challenge tramite il nome.
     */
    public ResponseEntity<Challenge> getChallengeByName(String challengeName, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Challenge challenge = searchRepository.findChallengeByName(challengeName);
        if (challenge != null) {
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Recupera tutte le challenge associate a un team.
     */
    public ResponseEntity<List<Challenge>> getChallengesByTeam(String teamId, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Challenge> challenges = searchRepository.findChallengesByTeam(teamId);
        return ResponseEntity.ok(challenges);
    }

    /**
     * Aggiorna lo stato di una challenge.
     */
    public ResponseEntity<String> updateChallengeStatus(String challengeName, String newStatus, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        try {
            searchRepository.updateChallengeStatus(challengeName, newStatus);
            return ResponseEntity.ok("Challenge status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating challenge status: " + e.getMessage());
        }
    }

    /**
     * Elimina una challenge.
     */
    public ResponseEntity<String> deleteChallenge(String challengeName, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        try {
            searchRepository.deleteChallenge(challengeName);
            return ResponseEntity.ok("Challenge deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting challenge: " + e.getMessage());
        }
    }

     /**
     * Recupera le partite associate a un giocatore specifico.
     */
    public ResponseEntity<?> getPlayerGames(int playerId, String jwt) {
        try {
            // Prepara l'entity con l'header JWT
            HttpEntity<Void> entity = jwtService.createJwtRequestEntity(jwt);

            // Costruisce l'URL per la richiesta REST
            String url = "http://t4-g18-app-1:3000/games/player/" + playerId;

            // Esegue la chiamata REST
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            // Restituisce i dati se la chiamata ha successo
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Error retrieving games from T4");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error connecting to T4: " + e.getMessage());
        }
    }

    /**
 * Verifica il completamento della challenge.
 */
public boolean isChallengeCompleted(Challenge challenge, int playerId, String jwt) {
    LocalDateTime challengeStartDate = LocalDateTime.parse(challenge.getStartDate(), DateTimeFormatter.ISO_DATE_TIME);
    LocalDateTime challengeEndDate = LocalDateTime.parse(challenge.getEndDate(), DateTimeFormatter.ISO_DATE_TIME);
    String victoryCondition = challenge.getVictoryCondition();
    VictoryConditionType type = challenge.getVictoryConditionType();

    try {
        // Recupera le partite giocate dal giocatore
        ResponseEntity<List<Map<String, Object>>> response = (ResponseEntity<List<Map<String, Object>>>) getPlayerGames(playerId, jwt);
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            List<Map<String, Object>> games = response.getBody();

            // Filtra le partite per l'arco temporale della challenge
            List<Map<String, Object>> gamesInRange = games.stream()
                .filter(game -> {
                    String startedAtStr = (String) game.get("startedAt");
                    if (startedAtStr != null) {
                        LocalDateTime startedAt = LocalDateTime.parse(startedAtStr, DateTimeFormatter.ISO_DATE_TIME);
                        return !startedAt.isBefore(challengeStartDate) && !startedAt.isAfter(challengeEndDate);
                    }
                    return false;
                })
                .collect(Collectors.toList());

            // **MODIFICA: Validazione della Victory Condition**
            if (type == VictoryConditionType.GAMES_PLAYED) {
                try {
                    int requiredGames = Integer.parseInt(victoryCondition); // Validazione del numero
                    return gamesInRange.size() >= requiredGames;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("La victoryCondition deve essere un numero valido.");
                }

                
            }

            // **MODIFICA: Gestione di tipi non supportati**
            throw new UnsupportedOperationException("Tipo di condizione non supportato: " + type);
        }
    } catch (Exception e) {
        // **MODIFICA: Migliore gestione degli errori**
        System.err.println("Errore durante la verifica della challenge: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
}


//funzione di lista challenge:
 public ResponseEntity<String> getAllChallengesAsHtml(String jwt) {
        // Controlla se il JWT è valido
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Accesso non autorizzato");
        }
    
        // Recupera tutti i Challenge dal repository
        List<Challenge> challenges = challengeRepository.findAll();
    
        // Genera il contenuto HTML per il corpo della tabella
        StringBuilder htmlBuilder = new StringBuilder();
        for (Challenge challenge : challenges) {
            htmlBuilder.append("<tr>")
                .append("<td>").append(challenge.getChallengeName()).append("</td>")
                .append("<td>").append(challenge.getDescription()).append("</td>")
                .append("<td>").append(challenge.getTeamId()).append("</td>")
                .append("<td>").append(challenge.getStartDate()).append("</td>")
                .append("<td>").append(challenge.getEndDate()).append("</td>")
                .append("<td>").append(challenge.getVictoryCondition()).append("</td>")
                .append("</tr>");
        }
        return ResponseEntity.ok(htmlBuilder.toString());
    }




    
}
