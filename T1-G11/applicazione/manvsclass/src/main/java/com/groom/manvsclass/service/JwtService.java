/*MODIFICA (5/11/2024) - Refactoring task T1
 * JwtService ora si occupa di implementare il controllo riguardo il token jwt.
 */
package com.groom.manvsclass.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import java.util.Date; 
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;  

import com.groom.manvsclass.model.Admin;

@Service
public class JwtService {

    public boolean isJwtValid(String jwt) {
		try {
			Claims c = Jwts.parser().setSigningKey("mySecretKeyAdmin").parseClaimsJws(jwt).getBody();

			if((new Date()).before(c.getExpiration())) {
				return true;
			}
		} catch(Exception e) {
			System.err.println(e);
		}

		return false;
	}


    //MODIFICA 02/12/2024: modifica logica calcolo jwt con email.
    public static String generateToken(Admin admin) {
        Instant now = Instant.now();
        Instant expiration = now.plus(1, ChronoUnit.HOURS);

        return Jwts.builder()
                .setSubject(admin.getUsername()) // .setSubject() imposta il soggetto del token JWT; il soggetto di solito rappresenta l'identità a cui si applica il token
                .setIssuedAt(Date.from(now)) // .setIssuedAt() imposta il timestamp di emissione del token
                .setExpiration(Date.from(expiration)) //.setExpiration() imposta il timestamp di scadenza del token
                .claim("admin_email", admin.getEmail()) //.claim() aggiunge una serie di informazioni aggiuntive
                .signWith(SignatureAlgorithm.HS256, "mySecretKeyAdmin") //.signWith() serve per firmare il token JWT utilizzando l'algoritmo di firma HMAC-SHA256 e una chiave segreta specificata
                .compact(); //.compact() serve a compattare il token JWT in una stringa valida che può essere facilmente trasferita tramite HTTP o memorizzata in altri luoghi di archiviazione come cookie
    }

    // Estrae l'ID dell'admin dal JWT
    public String getAdminFromJwt(String jwt) {
        try {
            Claims claims = Jwts.parser()
                                .setSigningKey("mySecretKeyAdmin")
                                .parseClaimsJws(jwt)
                                .getBody();

            // Estrae l'ID dell'admin dalla claim
            return claims.get("admin_email", String.class); // Restituisce l'username dell'admin come stringa
        } catch (Exception e) {
            System.err.println("Errore nell'estrazione dell'email dell'admin: " + e);
            return null; // Ritorna null se non riesce a estrarre l'ID
        }
    }
}

    