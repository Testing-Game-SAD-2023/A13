package com.example.db_setup.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import javax.persistence.*;

/**
 * Entità che rappresenta un avversario registrato nel sistema. Utilizzato per il tracking dei progressi del giocatore.
 * <p>
 * Ogni avversario è identificato in modo univoco dalla combinazione
 * di modalità di gioco, classe sotto test, tipo e difficoltà.
 * </p>
 */
@Table(
        name = "opponents",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"gameMode", "classUT", "type", "difficulty"}
        )
)
@Entity
@Data
@NoArgsConstructor
public class Opponent {
    /**
     * Identificatore univoco dell’avversario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Modalità di gioco associata all’avversario (PartitaSingola, Allenamento, ecc.).
     */
    @Enumerated(EnumType.STRING)
    private GameMode gameMode;

    /**
     * Nome della classe Java sotto test a cui è legato l’avversario.
     */
    private String classUT;

    /**
     * Tool di generazione dell’avversario (es. Evosuite, Test utente, ecc.).
     */
    private String type;

    /**
     * Livello di difficoltà dell’avversario.
     */
    @Enumerated(EnumType.STRING)
    private OpponentDifficulty difficulty;

    /**
     * Indica se l’avversario è attivo e disponibile per essere sfidato. Se un avversario viene eliminato dall'amministratore,
     * questo sarà disattivato e non rimosso, per mantenere coerenza nel tracking delle statistiche del giocatore.
     * <p>
     * Di default, l’avversario viene creato come attivo.
     * </p>
     */
    private boolean active = true;

    /**
     * Costruttore che crea un nuovo avversario specificando
     * modalità di gioco, classe sotto test, tipologia e difficoltà.
     *
     * @param gameMode   la modalità di gioco
     * @param classUT    la classe Java sotto test
     * @param type       la tipologia di avversario
     * @param difficulty la difficoltà dell’avversario
     */
    public Opponent(GameMode gameMode, String classUT, String type, OpponentDifficulty difficulty) {
        this.gameMode = gameMode;
        this.classUT = classUT;
        this.type = type;
        this.difficulty = difficulty;
    }
}
