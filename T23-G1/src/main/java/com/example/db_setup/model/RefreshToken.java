package com.example.db_setup.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import testrobotchallenge.commons.models.user.Role;

import javax.persistence.*;
import java.time.Instant;

/**
 * Entità rappresentante un refresh token salvato nel database.
 * <p>
 * I refresh token vengono utilizzati per generare nuovi JWT senza richiedere nuovamente le credenziali dell’utente
 * (senza dover passare per la relativa pagina di login). Ogni token è associato a un singolo utente,
 * che può essere un {@link Player} oppure un {@link Admin}.
 * <p>
 * La tabella corrispondente è {@code studentsrepo.refresh_tokens}.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "refresh_tokens", schema = "studentsrepo")
public class RefreshToken {

    /**
     * Identificatore univoco del token (chiave primaria).
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Valore del refresh token (UUID generato).
     */
    private String token;

    /**
     * Ruolo dell’utente associato al token (PLAYER o ADMIN).
     */
    private Role role;

    /**
     * Riferimento al giocatore proprietario del token, se il ruolo è {@link Role#PLAYER}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Player player;

    /**
     * Riferimento all’amministratore proprietario del token, se il ruolo è {@link Role#ADMIN}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Admin admin;

    /**
     * Data e ora di scadenza del token.
     */
    private Instant expiryDate;

    /**
     * Flag che indica se il token è stato revocato.
     */
    private boolean revoked = false;

    /**
     * Imposta l’utente come {@link Player} e rimuove l’eventuale riferimento a {@link Admin}.
     *
     * @param player il giocatore a cui associare il token
     */
    public void setPlayer(Player player) {
        this.player = player;
        this.admin = null;
    }

    /**
     * Imposta l’utente come {@link Admin} e rimuove l’eventuale riferimento a {@link Player}.
     *
     * @param admin l’amministratore a cui associare il token
     */
    public void setAdmin(Admin admin) {
        this.admin = admin;
        this.player = null;
    }
}

