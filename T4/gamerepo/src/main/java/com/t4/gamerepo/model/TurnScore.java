package com.t4.gamerepo.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;
import lombok.NoArgsConstructor;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;

/**
 * Entità {@link Embeddable} che rappresenta il punteggio del giocatore in un turno.
 * <p>
 * Contiene i punteggi restituiti al giocatore post compilazione dei test scritti.
 * Nello specifico riporta:
 * <ul>
 *     <li><b>JacocoScore</b>: punteggio della copertura restituita da JaCoCo (T7);</li>
 *     <li><b>EvosuiteScore</b>: punteggio della copertura restituito da EvoSuite (T8).</li>
 * </ul>
 * </p>
 */
@Data
@NoArgsConstructor
@Embeddable
public class TurnScore {

    /**
     * Punteggio restituito da Jacoco
     */
    @Embedded
    private JacocoScore jacocoScore;

    /**
     * Punteggio restituito da EvoSuite
     */
    @Embedded
    private EvosuiteScore evosuiteScore;
}
