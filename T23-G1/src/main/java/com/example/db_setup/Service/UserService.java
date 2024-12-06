package com.example.db_setup.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.db_setup.OAuthUserGoogle;

import com.example.db_setup.User;
import com.example.db_setup.UserRepository;
import com.example.db_setup.Authentication.AuthenticatedUser;
import com.example.db_setup.Authentication.AuthenticatedUserRepository;

//import com.T8.social.temp.Authentication.AuthenticatedUser;
//import com.T8.social.temp.Authentication.AuthenticatedUserRepository;
//import com.T8.social.temp.User;
//import com.T8.social.temp.UserRepository;   

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// Questa classe è un servizio che gestisce le operazioni relative agli utenti
// è usato per creare un nuovo utente, recuperare un utente esistente e generare un token JWT per l'utente
@Service
public class UserService {
    @Autowired
    // Usa la dipendenza UserRepository per accedere ai dati dell'utente sul DB
    private UserRepository userRepository;
    // Stessa cosa di sopra
    @Autowired
    private AuthenticatedUserRepository authenticatedUserRepository;
    
    // Recupera dal DB l'utente con l'email specificata
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    
    
    // Crea un nuovo utente con i dettagli forniti da OAuthUserGoogle, recuperati dall'accesso OAuth2
    // e lo salva nel DB
    public User createUserFromOAuth(OAuthUserGoogle oauthUser) {
        User newUser = new User();
        newUser.setEmail(oauthUser.getEmail());
        newUser.setName(oauthUser.getName());
        newUser.setRegisteredWithGoogle(true);
        String[] nameParts = oauthUser.getName().split(" ");
        if (nameParts.length > 1) {
            newUser.setSurname(nameParts[nameParts.length - 1]);
        }
        // Set other user properties as needed
        
        return userRepository.save(newUser);
    }
    
    // Genera un token JWT per l'utente specificato e lo salva nel DB
    public String saveToken(User user) {
        // Genera un token JWT per l'utente
        String token = generateToken(user);

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user, token);

        authenticatedUserRepository.save(authenticatedUser);

        return token;
    }

    // Genera un token JWT per l'utente specificato, forse si deve cambiare
    public static String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiration = now.plus(1, ChronoUnit.HOURS);
        // usa per generare il token email, data di creazione, data di scadenza, ID utente e ruolo
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .claim("userId", user.getID())
                .claim("role", "user")
                .signWith(SignatureAlgorithm.HS256, "mySecretKey")
                .compact();

        return token;
    }
  
    //Modifica 04/12/2024
    public ResponseEntity<?> getStudentiTeam(List<String> idUtenti) {
        System.out.println("Inizio metodo getStudentiTeam. ID ricevuti: " + idUtenti);
    
            // Controlla se la lista di ID è vuota
        if (idUtenti == null || idUtenti.isEmpty()) {
            System.out.println("La lista degli ID è vuota.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lista degli ID vuota.");
        }
    
        try {
        // Converte gli ID in interi
            List<Integer> idIntegerList = idUtenti.stream()
                                                      .map(Integer::valueOf)
                                                      .toList();
    
                // Recupera gli utenti dal database
                List<User> utenti = userRepository.findAllById(idIntegerList);
    
                // Verifica se sono stati trovati utenti
                if (utenti == null || utenti.isEmpty()) {
                    System.out.println("Nessun utente trovato per gli ID forniti.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nessun utente trovato.");
                }
    
                System.out.println("Utenti trovati: " + utenti);
    
                // Restituisce la lista di utenti trovati
                return ResponseEntity.ok(utenti);
    
            } catch (NumberFormatException e) {
                System.out.println("Errore durante la conversione degli ID: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body("Formato degli ID non valido. Devono essere numeri interi.");
            } catch (Exception e) {
                System.out.println("Errore durante il recupero degli utenti: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body("Errore interno del server.");
            }
        }
        
    }
