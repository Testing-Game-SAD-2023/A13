package testrobotchallenge.commons.models.dto.score;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import testrobotchallenge.commons.models.dto.score.basic.EvosuiteScoreDTO;
import testrobotchallenge.commons.models.dto.score.basic.JacocoScoreDTO;

/**
 * DTO che rappresenta il risultato completo della copertura JaCoCo per un test JUnit.
 * Contiene:
 * <ul>
 *   <li>Un oggetto {@link JacocoScoreDTO} con le metriche di copertura calcolate da JaCoCo (istruzioni coperte/mancanti, linee, branch, ecc.);</li>
 *   <li>Una stringa {@code outCompile} con il risultato della compilazione;</li>
 *   <li>Una stringa {@code coverage} con il contenuto del file XML di copertura generato da JaCoCo;</li>
 *   <li>Un booleano {@code errors} che indica se ci sono stati errori durante l'esecuzione del report.</li>
 * </ul>
 *
 * <p>Si tratta della versione estesa di {@link JacocoScoreDTO}, da utilizzare quando il client necessita
 * anche del contenuto raw del file di output e del risultato della compilazione.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class JacocoCoverageDTO {

    /**
     * Oggetto che contiene i risultati della copertura di JaCoCo in maniera struttura
     * (istruzioni coperte/mancanti, linee, branch, ecc.).
     */
    private JacocoScoreDTO jacocoScoreDTO;

    /**
     * Risultato della compilazione del test.
     */
    private String outCompile;

    /**
     * Risultato della copertura del test, corrisponde al file xml generato da JaCoCo.
     */
    private String coverage;

    /**
     * Indica se ci sono stati errori durante l'esecuzione del report.
     */
    private boolean errors;
}
