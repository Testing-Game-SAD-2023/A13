package com.example.db_setup.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entità che rappresenta i progressi/statistiche di un {@link Player}.
 * <p>
 * Tiene traccia dei seguenti dati:
 * <ul>
 *     <li>I punti esperienza accumulati dal giocatore;</li>
 *     <li>Gli achievement globali sbloccati;</li>
 *     <li>I progressi delle singole partite contro specifici {@link Opponent Opponent},
 *     modellati tramite {@link GameProgress}.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Ogni istanza è associata a un solo {@link Player} e può contenere più {@link GameProgress}.
 * </p>
 */
@Table(name = "players_progresses")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PlayerProgress {
    /**
     * Identificativo univoco del progresso del giocatore.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Riferimento al {@link Player} proprietario dei progressi.
     */
    @OneToOne
    private Player player;

    /**
     * Totale dei punti esperienza accumulati dal giocatore.
     */
    private int experiencePoints;

    /**
     * Insieme degli achievement globali sbloccati dal giocatore.
     */
    @ElementCollection
    private Set<String> globalAchievements;

    /**
     * Lista dei progressi contro singoli avversari.
     * Ogni voce rappresenta i risultati (vittoria/sconfitta) e gli achievement
     * relativi a uno specifico {@link Opponent}.
     */
    @OneToMany(mappedBy = "playerProgress", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameProgress> progresses = new ArrayList<>();

    /**
     * Costruttore che inizializza un nuovo progresso per il giocatore,
     * con punti esperienza a 0 e nessun achievement sbloccato.
     *
     * @param player il {@link Player} a cui associare i progressi
     */
    public PlayerProgress(Player player) {
        this.player = player;
        this.experiencePoints = 0;
        this.globalAchievements = new HashSet<>();
    }

    @Override
    public String toString() {
        return "PlayerProgress{" +
                "id=" + id +
                ", experiencePoints=" + experiencePoints +
                ", globalAchievements=" + globalAchievements +
                ", progresses=" + progresses +
                '}';
    }
}
