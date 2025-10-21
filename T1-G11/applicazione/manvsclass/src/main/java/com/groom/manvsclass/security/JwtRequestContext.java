package com.groom.manvsclass.security;

/*
 * Questa classe ha il compito di memorizzare il JWT ricevuto nella richiesta dell'utente e
 * renderlo disponibile a tutte che comunicazione interne in uscita dal modulo, assicurandone quindi l'autenticazione
 */
public class JwtRequestContext {

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
