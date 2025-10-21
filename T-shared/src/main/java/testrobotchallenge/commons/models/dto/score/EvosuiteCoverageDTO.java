package testrobotchallenge.commons.models.dto.score;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import testrobotchallenge.commons.models.dto.score.basic.EvosuiteScoreDTO;

/**
 * DTO che rappresenta i risultati completi di una sessione di analisi EvoSuite.
 * Contiene:
 * <ul>
 *   <li>Un oggetto {@link EvosuiteScoreDTO} con le metriche di copertura calcolate da EvoSuite;</li>
 *   <li>Il contenuto raw del file di output generato da EvoSuite in forma testuale.</li>
 * </ul>
 *
 * <p>Si tratta della versione estesa di {@link EvosuiteScoreDTO}, da utilizzare quando il client necessita
 * anche del contenuto raw del file di output.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class EvosuiteCoverageDTO {

    /**
     * Oggetto che contiene le metriche di copertura calcolate da EvoSuite in modo struttura
     * (istruzioni coperte/mancanti, linee, branch, ecc.).
     */
    private EvosuiteScoreDTO evosuiteScoreDTO;

    /**
     * Contenuto testuale che corrisponde al file csv generato da EvoSuite.
     */
    private String resultFileContent;
}
