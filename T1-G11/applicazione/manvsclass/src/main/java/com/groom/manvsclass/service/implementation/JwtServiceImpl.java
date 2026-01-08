package com.groom.manvsclass.service.implementation;

import com.groom.manvsclass.model.entity.AdminEntity;
import com.groom.manvsclass.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    //MODIFICA 02/12/2024: modifica logica calcolo jwt con email.
//    public static String generateToken(AdminMongoDB adminMongoDB) {
//        Instant now = Instant.now();
//        Instant expiration = now.plus(1, ChronoUnit.HOURS);
    ///
    //        return Jwts.builder()
//                .setSubject(adminMongoDB.getUsername()) // .setSubject() imposta il soggetto del token JWT; il soggetto di solito rappresenta l'identità a cui si applica il token
//                .setIssuedAt(Date.from(now)) // .setIssuedAt() imposta il timestamp di emissione del token
//                .setExpiration(Date.from(expiration)) //.setExpiration() imposta il timestamp di scadenza del token
//                .claim("admin_email", adminMongoDB.getEmail()) //.claim() aggiunge una serie di informazioni aggiuntive
//                .signWith(SignatureAlgorithm.HS256, "mySecretKeyAdmin") //.signWith() serve per firmare il token JWT utilizzando l'algoritmo di firma HMAC-SHA256 e una chiave segreta specificata
//                .compact(); //.compact() serve a compattare il token JWT in una stringa valida che può essere facilmente trasferita tramite HTTP o memorizzata in altri luoghi di archiviazione come cookie
//    }

    public static String generateToken(AdminEntity adminEntity) {
        Instant now = Instant.now();
        Instant expiration = now.plus(1, ChronoUnit.HOURS);

        return Jwts.builder()
                .setSubject(adminEntity.getUsername()) // .setSubject() imposta il soggetto del token JWT; il soggetto di solito rappresenta l'identità a cui si applica il token
                .setIssuedAt(Date.from(now)) // .setIssuedAt() imposta il timestamp di emissione del token
                .setExpiration(Date.from(expiration)) //.setExpiration() imposta il timestamp di scadenza del token
                .claim("admin_email", adminEntity.getEmail()) //.claim() aggiunge una serie di informazioni aggiuntive
                .signWith(SignatureAlgorithm.HS256, "mySecretKeyAdmin") //.signWith() serve per firmare il token JWT utilizzando l'algoritmo di firma HMAC-SHA256 e una chiave segreta specificata
                .compact(); //.compact() serve a compattare il token JWT in una stringa valida che può essere facilmente trasferita tramite HTTP o memorizzata in altri luoghi di archiviazione come cookie
    }

//    public boolean isJwtValid(String jwt) {
//        return true;
//    }

    @Override
    public boolean isJwtValid(String jwt) {
        try {
            Jwts.parser()
                    .setSigningKey("mySecretKey")
                    .parseClaimsJws(jwt);
            return true;
        } catch (Exception e) {
            System.err.println("Token non valido: " + e.getMessage());
            return false;
        }
    }

    // Estrae l'username dell'admin dal JWT
    @Override
    public String getAdminFromJwt(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("mySecretKey")
                    .parseClaimsJws(jwt)
                    .getBody();

            System.out.println("JWT:" + claims);
            // Estrae l'ID dell'admin dalla claim
            return claims.getSubject();
        } catch (Exception e) {
            System.err.println("Errore nell'estrazione dell'username dell'admin: " + e);
            return null; // Ritorna null se non riesce a estrarre l'ID
        }
    }
}
