package com.example.db_setup.service;

import com.example.db_setup.model.Admin;
import com.example.db_setup.model.PasswordResetToken;
import com.example.db_setup.model.Player;
import com.example.db_setup.model.repository.PasswordResetTokenRepository;
import com.example.db_setup.security.AuthenticationPropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import testrobotchallenge.commons.models.user.Role;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Service responsabile della gestione dei token di reset della password.
 * <p>
 * Questa classe si occupa di:
 * <ul>
 *     <li>Generare un nuovo password reset token per un utente;</li>
 *     <li>Invalidare eventuali token precedenti (rotazione), in modo che esista un solo token valido per utente;</li>
 *     <li>Verificare la validità di un token ricevuto;</li>
 *     <li>Revocare dei token non più validi.</li>
 * </ul>
 */
@Service
public class PasswordResetTokenService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetTokenService.class);
    private final AuthenticationPropertiesConfig authProperties;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetTokenService(AuthenticationPropertiesConfig authProperties, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.authProperties = authProperties;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    /**
     * Genera un nuovo password reset token per un {@link Player}.
     * <p>
     * Se l'utente ha già token precedenti, questi vengono revocati
     * tramite {@link #rotate(PasswordResetToken)}.
     *
     * @param player il giocatore per cui generare il token
     * @return il nuovo {@link PasswordResetToken} generato e salvato nel database
     */
    public PasswordResetToken generateResetToken(Player player) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setRole(Role.PLAYER);
        passwordResetToken.setPlayer(player);
        passwordResetToken.setToken(token);
        passwordResetToken.setExpiryDate(Instant.now().plusMillis(authProperties.getJwtRefreshCookieExpirationMs()));

        List<PasswordResetToken> oldPasswordResetTokens = passwordResetTokenRepository.findByPlayer(player);
        for (PasswordResetToken oldToken : oldPasswordResetTokens)
            this.rotate(oldToken);

        passwordResetTokenRepository.save(passwordResetToken);
        return passwordResetToken;
    }

    /**
     * Genera un nuovo password reset token per un {@link Admin}.
     * <p>
     * Se l'utente ha già token precedenti, questi vengono revocati
     * tramite {@link #rotate(PasswordResetToken)}.
     *
     * @param admin il giocatore per cui generare il token
     * @return il nuovo {@link PasswordResetToken} generato e salvato nel database
     */
    public PasswordResetToken generateResetToken(Admin admin) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setRole(Role.ADMIN);
        passwordResetToken.setAdmin(admin);
        passwordResetToken.setToken(token);
        passwordResetToken.setExpiryDate(Instant.now().plusMillis(authProperties.getJwtRefreshCookieExpirationMs()));

        List<PasswordResetToken> oldPasswordResetTokens = passwordResetTokenRepository.findByAdmin(admin);
        for (PasswordResetToken oldToken : oldPasswordResetTokens)
            this.rotate(oldToken);

        passwordResetTokenRepository.save(passwordResetToken);
        return passwordResetToken;
    }

    /**
     * Verifica la validità di un password reset token.
     * <p>
     * Un token è considerato valido se:
     * <ul>
     *     <li>Non è scaduto (data di scadenza successiva a {@link Instant#now()});</li>
     *     <li>Non è stato revocato.</li>
     * </ul>
     * Se il token non è valido, viene automaticamente revocato e non può più essere utilizzato.
     *
     * @param tokenValue il token da verificare
     * @return il {@link PasswordResetToken} se valido, altrimenti {@code null}
     */
    public PasswordResetToken verifyToken(String tokenValue) {
        return passwordResetTokenRepository.findByToken(tokenValue)
                .map(token -> {
                    logger.info("Password reset token found: {}", token);
                    if (token.getExpiryDate().isAfter(Instant.now()) && !token.isRevoked()) {
                        logger.info("Password reset token valid");
                        return token;
                    }
                    logger.info("Password reset token invalid: revoked {}, expired {}", token.getExpiryDate().isAfter(Instant.now()), !token.isRevoked());
                    token.setRevoked(true);
                    passwordResetTokenRepository.save(token);
                    return null;
                })
                .orElse(null);
    }

    /**
     * Revoca un password reset token rendendolo inutilizzabile.
     * <p>
     * Metodo usato internamente per invalidare i token precedenti di un utente
     * quando ne viene generato uno nuovo.
     *
     * @param oldToken il token da revocare
     * @return il token aggiornato e salvato come revocato
     */
    private PasswordResetToken rotate(PasswordResetToken oldToken) {
        oldToken.setRevoked(true);
        return passwordResetTokenRepository.save(oldToken);
    }

}
