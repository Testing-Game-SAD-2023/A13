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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List; // Se utilizzi anche List
import java.util.List;
import java.util.Collections;


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
    public ResponseEntity<List<Map<String, Object>>> getPlayerGames(String playerName, String jwt) {
        try {
            // Prepara l'entity con l'header JWT
            HttpEntity<Void> entity = jwtService.createJwtRequestEntity(jwt);
    
            // Costruisce l'URL per recuperare tutte le partite
            String url = "http://t4-g18-app-1:3000/games";
    
            // Recupera tutte le partite
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
    
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // Filtra le partite per il giocatore specifico
                List<Map<String, Object>> filteredGames = response.getBody().stream()
                    .filter(game -> {
                        List<Map<String, Object>> players = (List<Map<String, Object>>) game.get("players");
                        return players.stream()
                                      .anyMatch(player -> playerName.equals(player.get("accountId")));
                    })
                    .collect(Collectors.toList());
    
                return ResponseEntity.ok(filteredGames);
            }
    
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            System.err.println("Errore durante il recupero delle partite: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Collections.emptyList());
        }
    }
    
    

    /**
 * Verifica il completamento della challenge.
 */
    public boolean isChallengeCompletedByMember(Challenge challenge, String playerName, String jwt) {
        LocalDate challengeStartDate = LocalDate.parse(challenge.getStartDate(), DateTimeFormatter.ISO_DATE);
        LocalDate challengeEndDate = LocalDate.parse(challenge.getEndDate(), DateTimeFormatter.ISO_DATE);
        String victoryCondition = challenge.getVictoryCondition();
        VictoryConditionType type = challenge.getVictoryConditionType();

        try {
            // Recupera le partite filtrate per il giocatore specifico
            ResponseEntity<List<Map<String, Object>>> response = getPlayerGames(playerName, jwt);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> gamesInRange = response.getBody().stream()
                    .filter(game -> {
                        String startedAtStr = (String) game.get("startedAt");
                        if (startedAtStr != null) {
                            LocalDate startedAt = LocalDate.parse(startedAtStr, DateTimeFormatter.ISO_DATE);
                            return !startedAt.isBefore(challengeStartDate) && !startedAt.isAfter(challengeEndDate);
                        }
                        return false;
                    })
                    .collect(Collectors.toList());

                // Verifica la Victory Condition
                if (type == VictoryConditionType.GAMES_PLAYED) {
                    int requiredGames = Integer.parseInt(victoryCondition);
                    return gamesInRange.size() >= requiredGames;
                }

                throw new UnsupportedOperationException("Tipo di condizione non supportato: " + type);
            }
        } catch (Exception e) {
            System.err.println("Errore durante la verifica della challenge: " + e.getMessage());
        }

    return false;
}



    /**
     * Verifica se tutti i membri del team hanno completato la challenge.
     */
    public boolean isChallengeCompletedByTeam(Challenge challenge, Team team, String jwt) {
        List<String> teamMembers = team.getMember(); // Recupera la lista dei nomi dei membri (String)

        if (teamMembers == null || teamMembers.isEmpty()) {
            throw new IllegalArgumentException("Il team non ha membri.");
        }

        for (String memberName : teamMembers) {
            try {
                // Verifica se il membro (identificato dal nome) ha completato la challenge
                boolean isMemberCompleted = isChallengeCompletedByMember(challenge, memberName, jwt);

                if (!isMemberCompleted) {
                    System.out.println("Il membro " + memberName + " non ha completato la challenge.");
                    return false; // Restituisce false appena trova un membro che non ha completato
                }
            } catch (Exception e) {
                System.err.println("Errore durante la verifica della challenge per il membro: " + memberName);
                e.printStackTrace();
                throw new RuntimeException("Errore nella verifica della challenge per il membro: " + memberName);
            }
        }

        return true; // Restituisce true se tutti i membri hanno completato la challenge
    }





//funzione di lista challenge:
    public ResponseEntity<String> getAllChallengesAsHtml(String jwt) {
        // Controlla se il JWT è valido
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Accesso non autorizzato");
        }
    
        // Recupera tutti i Challenge dal repository
        List<Challenge> challenges = challengeRepository.findAll();
        String statusStyle;

        // Genera il contenuto HTML per il corpo della tabella
        StringBuilder htmlBuilder = new StringBuilder();
        for (Challenge challenge : challenges) {
            if ("scaduta".equalsIgnoreCase(challenge.getStatus())) {
                statusStyle = " style='color:red;'";
            } else if ("in corso".equalsIgnoreCase(challenge.getStatus())) {
                statusStyle = " style='color:green;'";
            } else if ("in attesa".equalsIgnoreCase(challenge.getStatus())) {
                statusStyle = " style='color:#DAA520;'";
            } else {
                statusStyle = ""; // Nessuno stile di default
            }

            htmlBuilder.append("<tr>")
            .append("<td>").append(challenge.getChallengeName()).append("</td>")
            .append("<td>").append(challenge.getDescription()).append("</td>")
            .append("<td>").append(challenge.getTeamId()).append("</td>")
            .append("<td>").append(challenge.getStartDate()).append("</td>")
            .append("<td>").append(challenge.getEndDate()).append("</td>")
            .append("<td").append(statusStyle).append(">")
            .append(challenge.getStatus()).append("</td>")
            .append("</tr>");
        }
        return ResponseEntity.ok(htmlBuilder.toString());
    }

    public ResponseEntity<List<Challenge>> getAllChallenges(String jwt) {
        // Verifica la validità del token JWT
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Token non valido, restituisce 401 Unauthorized
        }
    
        // Recupera tutte le challenge dal repository e restituisce 200 OK con la lista
        return ResponseEntity.ok(challengeRepository.findAll());
    }
    
    
    public ResponseEntity<String> updateExpiredChallenges(String jwt) {
        // Verifica la validità del token JWT
    if (!jwtService.isJwtValid(jwt)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body("Accesso non autorizzato: JWT non valido o assente.");
    }

    LocalDate today = LocalDate.now(); // Ottieni la data di oggi

    // Recupera tutte le challenge dal database
    List<Challenge> challenges = challengeRepository.findAll();

    int countExpired = 0; // Contatore delle challenge scadute aggiornate
    int countInProgress = 0; // Contatore delle challenge in corso aggiornate

    for (Challenge challenge : challenges) {
        LocalDate endDate = LocalDate.parse(challenge.getEndDate(), DateTimeFormatter.ISO_DATE);
        LocalDate startDate = LocalDate.parse(challenge.getStartDate(), DateTimeFormatter.ISO_DATE);

        // Controlla se la challenge è scaduta
        if (today.isAfter(endDate) && !"scaduta".equals(challenge.getStatus())) {
            challenge.setStatus("scaduta");
            challengeRepository.save(challenge);
            countExpired++;
        } 
        // Controlla se la challenge inizia oggi
        if (today.isEqual(startDate) && "in attesa".equals(challenge.getStatus())) {
            challenge.setStatus("in corso");
            challengeRepository.save(challenge);
            countInProgress++;
        }
    }

    return ResponseEntity.ok("Aggiornate " + countExpired + " challenge a stato 'Scaduta' e "
                             + countInProgress + " challenge a stato 'in corso'.");
    }
    

    
}
