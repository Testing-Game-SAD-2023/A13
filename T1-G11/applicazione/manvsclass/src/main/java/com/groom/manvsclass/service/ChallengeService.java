package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Challenge;
import com.groom.manvsclass.model.repository.ChallengeRepository;
import com.groom.manvsclass.model.repository.ChallengeSearchImpl;
import com.groom.manvsclass.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeSearchImpl searchRepository;

    @Autowired
    private JwtService jwtService;

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
}
