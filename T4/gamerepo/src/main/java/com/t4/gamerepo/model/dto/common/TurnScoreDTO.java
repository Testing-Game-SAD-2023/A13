package com.t4.gamerepo.model.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import testrobotchallenge.commons.models.dto.score.basic.EvosuiteScoreDTO;
import testrobotchallenge.commons.models.dto.score.basic.JacocoScoreDTO;

/**
 * DTO usato per descrivere il punteggio ottenuto dal giocatore in un turno.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class TurnScoreDTO {

    /**
     * Punteggio di EvoSuite ottenuto dal giocatore nel turno.
     */
    private EvosuiteScoreDTO evosuiteScoreDTO;

    /**
     * Punteggio di JaCoCo ottenuto dal giocatore nel turno.
     */
    private JacocoScoreDTO jacocoScoreDTO;
}
