package com.example.db_setup.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entità che rappresenta i progressi/statistiche di un giocatore contro uno specifico avversario.
 * <p>
 * Traccia se il giocatore (identificato tramite il suo {@link PlayerProgress}) ha mai battuto l'avversario
 * (identificato tramite l'oggetto {@link Opponent}) e gli eventuali obiettivi che ha sbloccato contro di esso.
 * </p>
 */
@Table(name = "game_progresses")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class GameProgress {
    /**
     * Identificativo univoco del GameProgress.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    /**
     * Identificativo del giocatore.
     */
    @ManyToOne
    @JoinColumn(name = "players_progresses_id")
    private PlayerProgress playerProgress;

    /**
     * Avversario contro cui il giocatore ha disputato la partita.
     */
    @ManyToOne
    @JoinColumn(name = "opponent_id")
    private Opponent opponent;

    /**
     * Indica se il giocatore ha battuto almeno una volta l'avversario.
     * Default: {@code false}.
     */
    private boolean isWinner = false;

    /**
     * Insieme di achievement sbloccati giocando contro l'avversario.
     */
    @ElementCollection
    private Set<String> achievements = new HashSet<>();

    /**
     * Costruttore che inizializza un nuovo GameProgress
     * associandolo al giocatore e all’avversario.
     *
     * @param playerProgress il {@link PlayerProgress} del giocatore
     * @param opponent       l'avversario affrontato
     */
    public GameProgress(PlayerProgress playerProgress, Opponent opponent) {
        this.playerProgress = playerProgress;
        this.opponent = opponent;
    }

    @Override
    public String toString() {
        return "GameProgress{" +
                "Id=" + Id +
                ", opponent=" + opponent +
                ", isWinner=" + isWinner +
                ", achievements=" + achievements +
                '}';
    }
}
