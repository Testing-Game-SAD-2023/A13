package testrobotchallenge.commons.models.dto.score.basic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** DTO che rappresenta le metriche di copertura calcolate da JaCoCo per un test JUnit.
 * Contiene le coperture per linee, branch e istruzioni.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class JacocoScoreDTO {
    private CoverageDTO lineCoverageDTO;
    private CoverageDTO branchCoverageDTO;
    private CoverageDTO instructionCoverageDTO;
}
