package com.t4.gamerepo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * Entità che rappresenta un turno giocato da un giocatore all’interno di un round.
 * <p>
 * Riporta:
 * <ul>
 *     <li>L'ID del giocatore associato al turno;</li>
 *     <li>Il numero progressivo del turno nel round;</li>
 *     <li>Il punteggio ottenuto dal giocatore nel turno;</li>
 *     <li>Il timestamp di avvio e chiusura del turno.</li>
 * </ul>
 * </p>
 */
@Entity
@Table(name = "turns")
@Getter
@Setter
@ToString
public class Turn {

    /**
     * Identificatore univoco del turno
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * ID del giocatore che ha giocato il turno
     */
    @Column(name = "player_id", nullable = false)
    private Long playerId;

    /**
     * Numero progressivo del turno all’interno del round
     */
    private int turnNumber;

    /**
     * Punteggio ottenuto dal giocatore nel turno
     */
    @Embedded
    private TurnScore score = null;

    /**
     * Timestamp di avvio del turno, settato automaticamente
     */
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Timestamp startedAt;

    /**
     * Timestamp di chiusura del turno, null se il turno è ancora aperto
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp closedAt = null;
}
