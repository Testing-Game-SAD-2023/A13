package testrobotchallenge.commons.models.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import testrobotchallenge.commons.models.user.Role;

/**
 * Rappresenta il risultato della validazione di un token JWT associato a un utente
 * restituito dal servizio di autenticazione (T23) al microservizio (T1, T56 o API Gateway) che lo ha invocato
 * tramite chiamata REST API.
 * <p>
 * Questa classe è un'estensione della classe {@link com.example.db_setup.security.jwt.JwtValidationResult} che aggiunge
 * il ruolo dell'utente all'interno del sistema, se l'autenticazione ha avuto successo.
 * </p>
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class JwtValidationResponseDTO {
    /** Indica se il JWT è valido o meno */
    private boolean valid;

    /** Ruolo dell'utente estratto dal JWT */
    private Role role;

    /** Codice identificativo dell'errore, ad esempio "EXPIRED", "MALFORMED", "INVALID" */
    private String error;

    /** Messaggio descrittivo dell'errore o informazioni aggiuntive */
    private String message;

    /**
     * Costruttore utilizzato se l'autenticazione ha avuto successo.
     *
     * @param valid settato a true
     * @param role  ruolo dell'utente estratto dal JWT valido
     */
    public JwtValidationResponseDTO(boolean valid, Role role) {
        this.valid = valid;
        this.role = role;
    }

    /**
     * Costruttore utilizzato se l'autenticazione non ha avuto successo.
     *
     * @param valid   settato a false
     * @param error   costante dell'errore, null se il JWT è valido
     * @param message messaggio associato all'errore, null se il JWT è valido
     */
    public JwtValidationResponseDTO(boolean valid, String error, String message) {
        this.valid = valid;
        this.error = error;
        this.message = message;
    }
}
