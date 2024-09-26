package com.example.db_setup.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
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
}
