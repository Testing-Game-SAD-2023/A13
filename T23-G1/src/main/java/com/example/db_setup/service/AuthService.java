package com.example.db_setup.service;

import com.example.db_setup.model.*;
import com.example.db_setup.model.repository.AdminRepository;
import com.example.db_setup.model.repository.PasswordResetTokenRepository;
import com.example.db_setup.model.repository.PlayerRepository;
import com.example.db_setup.model.repository.RefreshTokenRepository;
import com.example.db_setup.security.jwt.JwtProvider;
import com.example.db_setup.security.jwt.JwtValidationResult;
import com.example.db_setup.security.service.UserDetailsImpl;
import com.example.db_setup.service.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testrobotchallenge.commons.models.dto.auth.JwtValidationResponseDTO;
import testrobotchallenge.commons.models.user.Role;

import javax.mail.MessagingException;
import java.util.Locale;
import java.util.Optional;

import static testrobotchallenge.commons.models.user.Role.ADMIN;

/**
 * Service che espone tutte le funzionalità di autenticazione e gestione degli utenti,
 * sia amministratori (docenti) che giocatori (studenti).
 * <p>
 * Le principali responsabilità di questa classe includono:
 * <ul>
 *     <li>Registrazione di nuovi utenti;</li>
 *     <li>Gestione del login con generazione di JWT e refresh token;</li>
 *     <li>Validazione dei token JWT;</li>
 *     <li>Rigenerazione dei JWT tramite refresh token;</li>
 *     <li>Logout con invalidamento dei token;</li>
 *     <li>Gestione del reset della password tramite token dedicato.</li>
 * </ul>
 * </p>
 */
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager userAuthManager;
    private final AuthenticationManager adminAuthManager;
    private final PlayerRepository playerRepository;
    private final AdminRepository adminRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder encoder;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenService passwordResetTokenService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final PlayerService playerService;
    private final AdminService adminService;

    public AuthService(@Qualifier("playerAuthManager") AuthenticationManager userAuthManager, @Qualifier("adminAuthManager") AuthenticationManager adminAuthManager,
                       PlayerRepository playerRepository, AdminRepository adminRepository, JwtProvider jwtProvider, PasswordEncoder encoder, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository,
                       PasswordResetTokenService passwordResetTokenService, PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService, PlayerService playerService, AdminService adminService) {
        this.userAuthManager = userAuthManager;
        this.adminAuthManager = adminAuthManager;
        this.playerRepository = playerRepository;
        this.adminRepository = adminRepository;
        this.jwtProvider = jwtProvider;
        this.encoder = encoder;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordResetTokenService = passwordResetTokenService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
        this.playerService = playerService;
        this.adminService = adminService;
    }

    /**
     * Registra un nuovo giocatore (studente) nel sistema.
     *
     * @param name          nome del giocatore
     * @param surname       cognome del giocatore
     * @param email         email del giocatore (univoca nella tabella `players`)
     * @param password      password scelta
     * @param passwordCheck conferma della password
     * @param studies       informazioni sugli studi del giocatore
     * @return l'entità {@link Player} salvata
     * @throws PasswordMismatchException  se le password non coincidono
     * @throws UserAlreadyExistsException se esiste già un {@link Player} con la stessa email
     */
    @Transactional
    public Player registerPlayer(String name, String surname, String email,
                                 String password, String passwordCheck, Studies studies) {
        if (!password.equals(passwordCheck))
            throw new PasswordMismatchException("passwordCheck");

        if (playerRepository.findByUserProfileEmail(email).isPresent())
            throw new UserAlreadyExistsException("mail");

        return playerService.addNewPlayer(name, surname, email, encoder.encode(password), studies);
    }

    /**
     * Registra un nuovo amministratore (docente) nel sistema.
     *
     * @param name          nome dell'amministratore
     * @param surname       cognome dell'amministratore
     * @param email         email dell'amministratore (univoca nella tabella `Admins`)
     * @param password      password scelta
     * @param passwordCheck conferma della password
     * @return l'entita {@link Admin} creata
     * @throws PasswordMismatchException  se le password non coincidono
     * @throws UserAlreadyExistsException se esiste già un {@link Admin} con la stessa mail
     */
    public Admin registerAdmin(String name, String surname, String email,
                               String password, String passwordCheck) {
        if (!password.equals(passwordCheck))
            throw new PasswordMismatchException("passwordCheck");

        if (adminRepository.findByEmail(email).isPresent())
            throw new UserAlreadyExistsException("mail");

        return adminService.addNewAdmin(name, surname, email, encoder.encode(password));
    }

    /**
     * Effettua il login per un giocatore.
     * <p>
     * Se le credenziali sono corrette, genera un JWT e un refresh token associato,
     * entrambi restituiti come stringhe pronte per essere inviate come cookie HTTP.
     * </p>
     *
     * @param email    email del giocatore
     * @param password password del giocatore
     * @return array contenente il JWT e il refresh token in formato stringa
     * @throws UserNotFoundException se l'utente non esiste nel database
     */
    public String[] loginPlayer(String email, String password) {
        Authentication authentication = userAuthManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtProvider.generateJwtCookie(userDetails.getEmail(), userDetails.getId(), Role.PLAYER);
        Optional<Player> userOpt = playerRepository.findByUserProfileEmail((email));

        if (userOpt.isEmpty())
            throw new UserNotFoundException();

        Player player = userOpt.get();
        ResponseCookie refreshCookie = refreshTokenService.generateRefreshToken(player);

        return new String[]{jwtCookie.toString(), refreshCookie.toString()};
    }

    /**
     * Effettua il login per un amministratore.
     * <p>
     * Se le credenziali sono corrette, genera un JWT e un refresh token associato,
     * entrambi restituiti come stringhe pronte per essere inviate come cookie HTTP.
     * </p>
     *
     * @param email    email del giocatore
     * @param password password del giocatore
     * @return array contenente il JWT e il refresh token in formato stringa
     * @throws UserNotFoundException se l'utente non esiste nel database
     */
    public String[] loginAdmin(String email, String password) {
        Authentication authentication = adminAuthManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtProvider.generateJwtCookie(userDetails.getEmail(), userDetails.getId(), ADMIN);
        Optional<Admin> userOpt = adminRepository.findByEmail((email));

        if (userOpt.isEmpty())
            throw new UserNotFoundException();

        Admin admin = userOpt.get();
        ResponseCookie refreshCookie = refreshTokenService.generateRefreshToken(admin);

        return new String[]{jwtCookie.toString(), refreshCookie.toString()};
    }

    /**
     * Valida un token JWT, verificandone sia l'integrità che la coerenza con l'utente
     * presente nel sistema.
     *
     * @param jwtToken token JWT da validare
     * @return DTO contenente il risultato della validazione, con eventuale ruolo associato all'utente
     */
    public JwtValidationResponseDTO validateToken(String jwtToken) {
        // Valido il JWT
        JwtValidationResult result = jwtProvider.validateJwtToken(jwtToken);
        if (!result.isValid()) {
            return new JwtValidationResponseDTO(false, result.getError(), result.getMessage());
        }

        // Estraggo ruolo e mail embeddati
        Role role = jwtProvider.getUserRoleFromJwtToken(jwtToken);
        String email = jwtProvider.getUserEmailFromJwtToken(jwtToken);

        // Verifico che l'utente esista per il ruolo associato
        switch (role) {
            case PLAYER:
                if (playerService.getUserByEmail(email) == null)
                    return new JwtValidationResponseDTO(false, result.getError(), result.getMessage());
                break;
            case ADMIN:
                if (adminService.getAdminByEmail(email) == null)
                    return new JwtValidationResponseDTO(false, result.getError(), result.getMessage());
                break;
            default:
                return new JwtValidationResponseDTO(false, result.getError(), result.getMessage());
        }

        return new JwtValidationResponseDTO(true, role);
    }

    /**
     * Rigenera un nuovo JWT a partire da un refresh token valido.
     *
     * @param token refresh token da verificare
     * @return nuovo JWT serializzato come stringa per l'invio in cookie
     * @throws InvalidRefreshTokenException se il refresh token non è valido
     */
    public String refreshToken(String token) {
        // Verifico la validità del refresh token
        RefreshToken refreshToken = refreshTokenService.verifyToken(token);
        if (refreshToken == null)
            throw new InvalidRefreshTokenException();

        // Genero il nuovo JWT
        ResponseCookie jwtCookie;
        if (refreshToken.getRole().equals(ADMIN)) {
            jwtCookie = jwtProvider.generateJwtCookie(refreshToken.getAdmin().getEmail(), refreshToken.getAdmin().getId(), ADMIN);
        } else {
            jwtCookie = jwtProvider.generateJwtCookie(refreshToken.getPlayer().getEmail(), refreshToken.getPlayer().getID(), Role.PLAYER);
        }

        return jwtCookie.toString();
    }

    /**
     * Effettua il logout dell'utente, invalidando i token JWT e refresh token.
     *
     * <p>
     * Se il JWT è valido, vengono revocati tutti i refresh token dell'utente; se il JWT non è valido,
     * viene revocato solo il refresh token passato.
     * </p>
     *
     * @param jwtToken     il JWT dell'utente
     * @param refreshToken il refresh token dell'utente
     * @return array contenente i cookie vuoti
     */
    public String[] logout(String jwtToken, String refreshToken) {
        // Genero un JWT e un refresh token puliti per cancellare quelli nel browser
        ResponseCookie cleanJwt = jwtProvider.getCleanJwtCookie();
        ResponseCookie cleanRefresh = refreshTokenService.generateCleanRefreshToken();
        logger.info("[logout] Cleaned cookies generated: {}", cleanJwt);

        /* TODO: gestire il caso in cui uno o entrambi i token sono null */

        // Se il jwt inviato nella richiesta di logout è valido, estraggo l'utente e procedo a
        // invalidare tutti i suoi refresh token
        if (jwtProvider.validateJwtToken(jwtToken).isValid()) {
            String userEmail = jwtProvider.getUserEmailFromJwtToken(jwtToken);
            Role userRole = jwtProvider.getUserRoleFromJwtToken(jwtToken);

            if (userRole == Role.PLAYER) {
                playerRepository.findByUserProfileEmail(userEmail).ifPresent(refreshTokenService::invalidAllUserRefreshTokens);
            } else {
                adminRepository.findByEmail(userEmail).ifPresent(refreshTokenService::invalidAllAdminRefreshTokens);
            }

            logger.info("[logout] jwt is valid, revoked all refresh tokens for {} with role {}", userEmail, userRole);
        } else {
            // Invalido solo il refresh token fornito nella richiesta
            /* TODO: estrarre l'utente dal refresh token per ivalidare tutti i resfresh token associati */
            Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(refreshToken);
            if (refreshTokenOpt.isPresent()) {
                RefreshToken toRevoke = refreshTokenOpt.get();
                toRevoke.setRevoked(true);
                refreshTokenRepository.save(toRevoke);
            }

            logger.info("[logout] jwt isn't valid, can't extract user, revoked only refresh token in request cookies");
        }

        return new String[]{cleanJwt.toString(), cleanRefresh.toString()};
    }

    /**
     * Richiede il reset della password per l'utente identificato da email e ruolo forniti.
     * <p>
     * Genera un token di reset, lo salva a database e invia un'email con le istruzioni
     * per completare la procedura all'utente.
     * </p>
     *
     * @param email  indirizzo email dell'utente
     * @param role   ruolo dell'utente (PLAYER o ADMIN)
     * @param locale lingua per la localizzazione del messaggio email
     * @throws UserNotFoundException se l'utente non esiste
     * @throws MessagingException    se l'email non può essere inviata
     */
    public void requestResetPassword(String email, Role role, Locale locale) throws MessagingException {
        PasswordResetToken passwordResetToken;

        if (ADMIN.equals(role)) {
            // Genero il password reset token per la mail passata, lo associo al ruolo Admin e lo salvo nel database
            Optional<Admin> adminOpt = adminRepository.findByEmail(email);

            if (adminOpt.isEmpty()) {
                throw new UserNotFoundException();
            }

            Admin admin = adminOpt.get();
            passwordResetToken = passwordResetTokenService.generateResetToken(admin);
            passwordResetTokenRepository.save(passwordResetToken);
        } else {
            // Genero il password reset token per la mail passata, lo associo al ruolo Player e lo salvo nel database
            Optional<Player> userOpt = playerRepository.findByUserProfileEmail(email);

            if (userOpt.isEmpty()) {
                throw new UserNotFoundException();
            }

            Player player = userOpt.get();
            passwordResetToken = passwordResetTokenService.generateResetToken(player);
            passwordResetTokenRepository.save(passwordResetToken);
        }

        emailService.sendPasswordResetEmail(email, passwordResetToken.getToken(), locale);
    }

    /**
     * Esegue la procedura di reset password per un utente.
     * <p>
     * Verifica la correttezza del token di reset, la corrispondenza con email e ruolo,
     * e aggiorna la password nel database.
     * </p>
     *
     * @param email         email dell'utente
     * @param password      nuova password
     * @param passwordCheck conferma della nuova password
     * @param resetToken    token di reset ricevuto dall'utente
     * @param role          ruolo dell'utente
     * @throws PasswordMismatchException           se le password non coincidono
     * @throws PasswordResetTokenNotFoundException se il token non è valido
     * @throws IncompatibleRoleException           se il ruolo non è quello associato al reset token
     * @throws IncompatibleEmailException          se l'email non è quella associato al reset token
     */
    public void changePassword(String email, String password, String passwordCheck, String resetToken, Role role) {
        if (!password.equals(passwordCheck))
            throw new PasswordMismatchException("passwordCheck");

        PasswordResetToken passwordResetToken = passwordResetTokenService.verifyToken(resetToken);
        if (passwordResetToken == null) {
            throw new PasswordResetTokenNotFoundException("passwordResetToken");
        }

        if (!passwordResetToken.getRole().equals(role)) {
            logger.info("Invalid password reset token: role incorrect, expected role {}, found {}", role, passwordResetToken.getRole());

            throw new IncompatibleRoleException("resetToken");
        }

        // Verifico che il ruolo e la mail dell'utente che sta procedendo con il reset della password siano quelli per
        // cui è stato emesso il password reset token
        if (role.equals(ADMIN)) {
            Admin admin = passwordResetToken.getAdmin();

            if (!admin.getEmail().equals(email))
                throw new IncompatibleEmailException("resetToken");

            admin.setPassword(encoder.encode(password));
            adminRepository.save(admin);
        } else {
            Player player = passwordResetToken.getPlayer();

            if (!player.getUserProfile().getEmail().equals(email)) {
                throw new IncompatibleEmailException("resetToken");
            }

            player.setPassword(encoder.encode(password));
            playerRepository.save(player);
        }

        // Revoco il token e aggiorno la password
        passwordResetToken.setRevoked(true);
        passwordResetTokenRepository.save(passwordResetToken);
    }
}
