package com.example.db_setup.service;

import com.example.db_setup.model.Admin;
import com.example.db_setup.model.Player;
import com.example.db_setup.model.RefreshToken;
import com.example.db_setup.model.repository.RefreshTokenRepository;
import com.example.db_setup.security.AuthenticationPropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service responsabile della gestione dei Refresh Token.
 * Si occupa della creazione, rotazione, validazione e invalidazione
 * dei refresh token sia per utenti di tipo {@link Player} che {@link Admin}.
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationPropertiesConfig authProperties;

    /**
     * Genera un nuovo refresh token per un {@link Player}.
     * Eventuali token precedenti dello stesso utente vengono marcati come revocati.
     *
     * @param player il giocatore a cui associare il refresh token
     * @return il {@link ResponseCookie} contenente il nuovo refresh token
     */
    public ResponseCookie generateRefreshToken(Player player) {
        return generateTokenForUser(null, player);
    }

    /**
     * Genera un nuovo refresh token per un {@link Admin}.
     * Eventuali token precedenti dello stesso amministratore vengono marcati come revocati.
     *
     * @param admin l’amministratore a cui associare il refresh token
     * @return il {@link ResponseCookie} contenente il nuovo refresh token
     */
    public ResponseCookie generateRefreshToken(Admin admin) {
        return generateTokenForUser(admin, null);
    }

    /**
     * Metodo base per la creazione di un refresh token.
     * Supporta sia utenti di tipo {@link Admin} che {@link Player}.
     * Invalida eventuali token precedenti e salva quello nuovo nel database.
     *
     * @param admin  l’amministratore (se presente, altrimenti {@code null})
     * @param player il giocatore (se presente, altrimenti {@code null})
     * @return il {@link ResponseCookie} contenente il nuovo refresh token
     */
    private ResponseCookie generateTokenForUser(Admin admin, Player player) {
        // Costruisco il refresh token
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setAdmin(admin);
        refreshToken.setPlayer(player);
        refreshToken.setExpiryDate(Instant.now().plusMillis(authProperties.getJwtRefreshCookieExpirationMs()));

        // Invalido i refresh token precedenti dell'utente
        List<RefreshToken> oldUserRefreshTokens = admin != null ?
                refreshTokenRepository.findByAdmin(admin) :
                refreshTokenRepository.findByPlayer(player);

        for (RefreshToken oldRefreshToken : oldUserRefreshTokens)
            this.rotate(oldRefreshToken);

        // Salvo nel db il token e lo restituisco
        refreshTokenRepository.save(refreshToken);
        return ResponseCookie.from(authProperties.getJwtRefreshCookieName(), refreshToken.getToken()).path("/").maxAge(refreshToken.getExpiryDate().getEpochSecond()).build();

    }

    /**
     * Genera un cookie "pulito" che rimuove il refresh token dal client.
     *
     * @return cookie con valore vuoto e {@code maxAge=0}
     */
    public ResponseCookie generateCleanRefreshToken() {
        return ResponseCookie.from(authProperties.getJwtRefreshCookieName(), "").path("/").maxAge(0).build();
    }

    /**
     * Verifica se un refresh token è valido e non scaduto.
     * Se il token è scaduto, viene marcato come revocato.
     *
     * @param refreshToken il token da verificare
     * @return il {@link RefreshToken} valido se presente e non scaduto, altrimenti {@code null}
     */
    public RefreshToken verifyToken(String refreshToken) {
        Optional<RefreshToken> savedToken = refreshTokenRepository.findByToken(refreshToken);
        if (savedToken.isPresent()) {
            if (savedToken.get().getExpiryDate().isAfter(Instant.now())) {
                return savedToken.get();
            } else {
                RefreshToken token = savedToken.get();
                token.setRevoked(true);
                refreshTokenRepository.save(token);
            }
        }

        return null;
    }

    /**
     * Invalida tutti i refresh token di un determinato {@link Player}.
     *
     * @param player il giocatore i cui token devono essere invalidati
     */
    public void invalidAllUserRefreshTokens(Player player) {
        refreshTokenRepository.findByPlayer(player).forEach(this::rotate);
    }

    /**
     * Invalida tutti i refresh token di un determinato {@link Admin}.
     *
     * @param admin l’amministratore i cui token devono essere invalidati
     */
    public void invalidAllAdminRefreshTokens(Admin admin) {
        refreshTokenRepository.findByAdmin(admin).forEach(this::rotate);
    }

    /**
     * Marca un refresh token come revocato e lo salva a database.
     *
     * @param oldRefreshToken il token da revocare
     * @return il {@link RefreshToken} aggiornato
     */
    private RefreshToken rotate(RefreshToken oldRefreshToken) {
        oldRefreshToken.setRevoked(true);
        return refreshTokenRepository.save(oldRefreshToken);
    }
}
