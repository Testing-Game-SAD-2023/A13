package com.example.db_setup.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import testrobotchallenge.commons.models.user.Role;

import javax.persistence.*;
import java.time.Instant;

/**
 * Entità che rappresenta un token di reset password generato dal sistema.
 * <p>
 * Il token è associato a un utente ({@link Player} o {@link Admin})
 * e ha una data di scadenza oltre la quale non è più valido.
 * Viene contrassegnato come revocato in caso di utilizzo o invalidamento.
 * </p>
 */
@Table(name = "password_reset_tokens", schema = "studentsrepo")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PasswordResetToken {
    /**
     * Identificatore univoco del token.
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * Valore univoco del token.
     */
    private String token;

    /**
     * Ruolo dell’utente a cui è associato il token
     * ({@link Role#PLAYER} o {@link Role#ADMIN}).
     */
    private Role role;

    /**
     * Giocatore a cui è legato il token, se emesso per questo ruolo.
     * <p>
     * Se il token è associato a un giocatore, il campo {@link #admin} sarà null.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Player player;

    /**
     * Amministratore a cui è legato il token, se emesso per questo ruolo.
     * <p>
     * Se il token è associato a un amministratore, il campo {@link #player} sarà null.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Admin admin;

    /**
     * Data e ora di scadenza del token.
     */
    private Instant expiryDate;

    /**
     * Indica se il token è stato revocato (non più utilizzabile).
     */
    private boolean revoked;

    /**
     * Associa il token a un giocatore e rimuove l’eventuale associazione con un admin.
     *
     * @param player il giocatore a cui associare il token
     */
    public void setPlayer(Player player) {
        this.player = player;
        this.admin = null;
    }

    /**
     * Associa il token a un amministratore e rimuove l’eventuale associazione con un giocatore.
     *
     * @param admin l’amministratore a cui associare il token
     */
    public void setAdmin(Admin admin) {
        this.admin = admin;
        this.player = null;
    }
}
