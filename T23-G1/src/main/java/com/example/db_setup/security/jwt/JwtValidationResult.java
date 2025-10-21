package com.example.db_setup.security.jwt;

import lombok.Getter;
import lombok.Setter;

/**
 * Rappresenta il risultato della validazione di un token JWT associato a un utente
 * in una richiesta HTTP in ingresso.
 * <p>
 * Questa classe viene utilizzata internamente al microservizio per
 * indicare se un JWT è valido, o, in caso contrario, fornire informazioni sull'errore di validazione.
 * Per l'esposizione verso l'esterno (API), viene utilizzata la classe {@link testrobotchallenge.commons.models.dto.auth}.
 * </p>
 */
@Getter
@Setter
public class JwtValidationResult {

    /**
     * Indica se il JWT è valido o meno
     */
    private boolean valid;

    /**
     * Codice identificativo dell'errore, ad esempio "EXPIRED", "MALFORMED", "INVALID"
     */
    private String error;

    /**
     * Messaggio descrittivo dell'errore o informazioni aggiuntive
     */
    private String message;

    /**
     * Costruttore della classe.
     *
     * @param valid   true se il JWT è valido, false altrimenti
     * @param error   costante dell'errore, null se il JWT è valido
     * @param message messaggio associato all'errore, null se il JWT è valido
     */
    public JwtValidationResult(boolean valid, String error, String message) {
        this.valid = valid;
        this.error = error;
        this.message = message;
    }

}
