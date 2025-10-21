package com.t4.gamerepo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import testrobotchallenge.commons.models.opponent.GameMode;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entità che rappresenta una partita.
 * <p>
 * Traccia i giocatori partecipanti, lo stato del gioco, la modalità di gioco associata, i round giocati e il risultato
 * finale ottenuto da ciascun giocatore.
 * </p>
 */
@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Game {

    /**
     * Identificativo univoco della partita
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Lista degli ID dei giocatori partecipanti
     */
    @ElementCollection
    @CollectionTable(name = "players", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "player_id")
    private List<Long> players;

    /**
     * Stato corrente della partita
     */
    @Enumerated(EnumType.STRING)
    private GameStatus status;

    /**
     * Modalità di gioco scelta
     */
    @Enumerated(EnumType.STRING)
    private GameMode gameMode;

    /**
     * Mappa dei risultati dei giocatori.
     * La chiave è l'ID del giocatore, il valore è il risultato {@link PlayerResult}.
     */
    @ElementCollection
    @CollectionTable(name = "game_player_results", joinColumns = @JoinColumn(name = "game_id"))
    @MapKeyColumn(name = "player_id")
    private Map<Long, PlayerResult> playerResults = new HashMap<>();

    /**
     * Lista dei round della partita.
     * Cascade = ALL indica che le operazioni sul Game si propagano ai round.
     */
    // Con cascade = CascadeType.ALL delego la gestione nel db dei Round a Game, ovvero se salvo/aggiorno/elimino il
    // genitore lo stesso avviene anche per i figli
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "game_id")
    @OrderBy("roundNumber ASC")
    private List<Round> rounds = new ArrayList<>();

    /**
     * Timestamp di apertura della partita
     */
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Timestamp startedAt;

    /**
     * Timestamp di chiusura della partita
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp closedAt;

    public Game(GameMode gameMode, List<Long> players) {
        this.gameMode = gameMode;
        this.players = players;
    }

    /**
     * Aggiunge un nuovo round alla partita.
     *
     * @param newRound il nuovo round da aggiungere
     */
    public void addRound(Round newRound) {
        rounds.add(newRound);
    }

    /**
     * Restituisce l'ultimo round della partita.
     * <p>
     * Annotato con {@link JsonIgnore} per evitare problemi di serializzazione con Jackson
     * quando i round non sono ancora registrati.
     * </p>
     *
     * @return l'ultimo round o {@code null} se non esistono round
     */
    @JsonIgnore
    public Round getLastRound() {
        if (rounds == null || rounds.isEmpty()) {
            return null;
        }
        return rounds.get(rounds.size() - 1);
    }

}
