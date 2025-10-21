package com.example.db_setup.security.jwt;

import com.example.db_setup.security.AuthenticationPropertiesConfig;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import testrobotchallenge.commons.models.user.Role;

import java.time.Instant;
import java.util.Date;

/**
 * Classe che espone le funzionalità di creazione, validazione ed estrazione dati del JSON Web Token (JWT), utilizzato
 * per l'autenticazione utente.
 */
@Component
@RequiredArgsConstructor
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private final AuthenticationPropertiesConfig authProperties;

    /**
     * Segreto impiegato nella generazione del JWT, passato tramite file .env
     */
    private final String keySecret = System.getenv("JWT_KEY_SECRET");

    /**
     * Genera un JWT (JSON Web Token) per l'utente autenticato e lo restituisce
     * sotto forma di {@link ResponseCookie}, pronto per essere inviato al client.
     * <p>
     * Il token contiene come payload l'email dell'utente, il suo ID interno e il ruolo associato,
     * ed è firmato con l'algoritmo HS256 usando la chiave segreta.
     * La durata del cookie è calcolata a partire dalle proprietà di configurazione del JWT.
     *
     * @param email  l'email dell'utente autenticato
     * @param userId l'ID dell'utente nel sistema (corrispondente all'ID nel database)
     * @param role   il ruolo dell'utente
     * @return un {@link ResponseCookie} contenente il JWT, con path "/" e durata settata
     */
    public ResponseCookie generateJwtCookie(String email, Long userId, Role role) {
        String jwt = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + authProperties.getJwtCookieExpirationMs()))
                .claim("userId", userId)
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS256, keySecret)
                .compact();

        return ResponseCookie.from(authProperties.getJwtCookieName(), jwt)
                .path("/")
                .maxAge(authProperties.getJwtCookieExpirationMs() / 1000 + 7200)
                .build();
    }

    /**
     * Genera un cookie HTTP vuoto per il JWT, utile per "pulire" o invalidare
     * il cookie sul client (ad esempio durante il logout).
     *
     * @return un {@link ResponseCookie} con nome del JWT, valore vuoto,
     * percorso "/" e durata settata a 0, pronto per essere inviato al client
     */
    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(authProperties.getJwtCookieName(), "").path("/").maxAge(0).build();
    }

    /**
     * Estrae l'email dell'utente a partire da un JWT valido.
     * <p>
     * Il metodo analizza il token JWT fornito, verifica la firma con la chiave segreta
     * e restituisce il soggetto del token, che corrisponde all'email dell'utente.
     *
     * @param authToken il JWT da cui estrarre l'email
     * @return l'email dell'utente contenuta nel token
     */
    public String getUserEmailFromJwtToken(String authToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(keySecret)
                .parseClaimsJws(authToken)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Estrae il ruolo dell'utente a partire da un JWT valido.
     * <p>
     * Il metodo analizza il token JWT fornito, verifica la firma con la chiave segreta
     * e restituisce il ruolo associato.
     *
     * @param authToken il JWT da cui estrarre l'email
     * @return il ruolo dell'utente contenuta nel token
     */
    public Role getUserRoleFromJwtToken(String authToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(keySecret)
                .parseClaimsJws(authToken)
                .getBody();

        return Role.valueOf(claims.get("role", String.class));
    }

    /**
     * Valida un token JWT verificandone la firma, il formato e la scadenza.
     * <p>
     * Il metodo prova a parsare il token usando la chiave segreta e restituisce
     * un oggetto {@link JwtValidationResult} contenente il risultato della validazione.
     * Vengono gestiti diversi casi di errore come token malformato, non supportato,
     * vuoto o scaduto.
     *
     * @param authToken il JWT da validare
     * @return un {@link JwtValidationResult} che indica se il token è valido o meno,
     * con eventuale codice di errore e messaggio descrittivo
     */
    public JwtValidationResult validateJwtToken(String authToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(keySecret)
                    .parseClaimsJws(authToken)
                    .getBody();

            if (Instant.now().isBefore(claims.getExpiration().toInstant())) {
                logger.trace("Jwt is valid");
                return new JwtValidationResult(true, null, null);
            } else {
                logger.error("Jwt is expired");
                return new JwtValidationResult(false, "EXPIRED", "JWT token is expired");
            }

        } catch (MalformedJwtException e) {
            logger.error("Jwt is malformed: {}", e.getMessage());
            return new JwtValidationResult(false, "MALFORMED", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Jwt is unsupported: {}", e.getMessage());
            return new JwtValidationResult(false, "UNSUPPORTED", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Jwt is empty: {}", e.getMessage());
            return new JwtValidationResult(false, "EMPTY", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Jwt is expired: {}", e.getMessage());
            return new JwtValidationResult(false, "EXPIRED", e.getMessage());
        } catch (Exception e) {
            logger.error("Jwt is invalid: {}", e.getMessage());
            return new JwtValidationResult(false, "INVALID", e.getMessage());
        }
    }
}
