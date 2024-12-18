/*MODIFICA (5/11/2024) - Refactoring task T1
 * JwtService ora si occupa di implementare il controllo riguardo il token jwt.
 */
package com.groom.manvsclass.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import java.util.Date; 
import java.time.Instant;
import java.time.temporal.ChronoUnit;  

import com.groom.manvsclass.model.Admin;

@Service
public class JwtService {

    // Controllo validità del Token da parte di un Admin
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

    public static String generateToken(Admin admin) {
        Instant now = Instant.now();
        Instant expiration = now.plus(1, ChronoUnit.HOURS);

        return Jwts.builder()
                .setSubject(admin.getUsername()) // .setSubject() imposta il soggetto del token JWT; il soggetto di solito rappresenta l'identità a cui si applica il token
                .setIssuedAt(Date.from(now)) // .setIssuedAt() imposta il timestamp di emissione del token
                .setExpiration(Date.from(expiration)) //.setExpiration() imposta il timestamp di scadenza del token
                .claim("admin_username", admin.getUsername()) //.claim() aggiunge una serie di informazioni aggiuntive
                .claim("role", "admin")
                .signWith(SignatureAlgorithm.HS256, "mySecretKeyAdmin") //.signWith() serve per firmare il token JWT utilizzando l'algoritmo di firma HMAC-SHA256 e una chiave segreta specificata
                .compact(); //.compact() serve a compattare il token JWT in una stringa valida che può essere facilmente trasferita tramite HTTP o memorizzata in altri luoghi di archiviazione come cookie
    }

}