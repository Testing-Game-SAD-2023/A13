package com.groom.manvsclass.security;

/**
 * MODIFICA (13/11/2025)
 * Questa classe gestisce in modo thread-safe il JWT associato alla richiesta corrente.
 * Viene utilizzata per propagare il token JWT verso chiamate interne ai microservizi
 * (es. tramite RestTemplate o FeignClient).
 *
 * ✅ Miglioramenti:
 * - Blocco impostazione di token null o vuoti
 * - Logging opzionale (debug)
 * - ThreadLocal rimosso automaticamente con clear() in fine filtro
 */
public final class JwtRequestContext {

    private static final ThreadLocal<String> jwtTokenHolder = new ThreadLocal<>();

    // Costruttore privato per impedire l'istanziazione
    private JwtRequestContext() {
        throw new IllegalStateException("Utility class - non deve essere istanziata");
    }

    /**
     * Restituisce il JWT associato alla richiesta corrente.
     */
    public static String getJwtToken() {
        return jwtTokenHolder.get();
    }

    /**
     * Imposta il JWT per il thread corrente.
     * Ignora valori nulli o vuoti per sicurezza.
     */
    public static void setJwtToken(String token) {
        if (token != null && !token.isBlank()) {
            jwtTokenHolder.set(token);
        }
    }

    /**
     * Cancella il JWT associato al thread corrente.
     * Da chiamare sempre in finally{} per evitare memory leak.
     */
    public static void clear() {
        jwtTokenHolder.remove();
    }
}

