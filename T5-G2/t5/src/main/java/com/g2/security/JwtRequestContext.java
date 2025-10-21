package com.g2.security;

/**
 * Classe di utility che ha il compito di memorizzare il JWT validato ricevuto in una richiesta e mantenerlo per tutto il
 * tempo di vita della stessa, fornendolo ai componenti che lo richiedono.
 */
public class JwtRequestContext {
    /*
     * Questa classe è necessaria per l'autenticazione delle chiamate API interne ai microservizi
     */

    private static final ThreadLocal<String> jwtTokenHolder = new ThreadLocal<>();

    private JwtRequestContext() {
        throw new IllegalStateException("Classe utility che memorizza il JWT ricevuto dall'utente");
    }

    public static String getJwtToken() {
        return jwtTokenHolder.get();
    }

    public static void setJwtToken(String token) {
        jwtTokenHolder.set(token);
    }

    public static void clear() {
        jwtTokenHolder.remove();
    }
}
