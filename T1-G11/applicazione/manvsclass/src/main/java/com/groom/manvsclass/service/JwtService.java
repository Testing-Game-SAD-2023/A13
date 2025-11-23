/*MODIFICA (5/11/2024) - Refactoring task T1
 * JwtService ora si occupa di implementare il controllo riguardo il token jwt.
 */
package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Admin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    //MODIFICA 02/12/2024: modifica logica calcolo jwt con email.
    public static String generateToken(Admin admin) {
        Instant now = Instant.now();
        Instant expiration = now.plus(1, ChronoUnit.HOURS);

        return Jwts.builder()
                .setSubject(admin.getEmail())
                .claim("nome", admin.getNome())
                .claim("cognome", admin.getCognome())
                .setIssuedAt(Date.from(now)) // .setIssuedAt() imposta il timestamp di emissione del token
                .setExpiration(Date.from(expiration)) //.setExpiration() imposta il timestamp di scadenza del token
                .signWith(SignatureAlgorithm.HS256, "mySecretKey") //.signWith() serve per firmare il token JWT utilizzando l'algoritmo di firma HMAC-SHA256 e una chiave segreta specificata
                .compact(); //.compact() serve a compattare il token JWT in una stringa valida che può essere facilmente trasferita tramite HTTP o memorizzata in altri luoghi di archiviazione come cookie
    }

    public boolean isJwtValid(String jwt) {
        try {
            Jwts.parser()
                    .setSigningKey("mySecretKey")
                    .parseClaimsJws(jwt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Estrae l'email dell'admin dal JWT
    public String getAdminEmailFromJwt(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("mySecretKey")
                    .parseClaimsJws(jwt)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            System.err.println("Errore nell'estrazione dell'email dell'admin: " + e);
            return null; // Ritorna null se non riesce a estrarre l'email
        }
    }

    public Admin getAdminFromJwt(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("mySecretKey")
                    .parseClaimsJws(jwt)
                    .getBody();

            Admin admin = new Admin();
            admin.setEmail(claims.getSubject());
            admin.setNome(claims.get("nome", String.class));
            admin.setCognome(claims.get("cognome", String.class));

            return admin;
        } catch (Exception e) {
            System.err.println("Errore nell'estrazione dell'admin dal token: " + e);
            return null;
        }
    }


}