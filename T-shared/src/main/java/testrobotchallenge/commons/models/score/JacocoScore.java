package testrobotchallenge.commons.models.score;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Rappresenta i risultati delle metriche di copertura calcolate da JaCoCo per un test JUnit.
 * <p>
 * Ogni tipo di copertura (linee, branch, istruzioni) è rappresentata da un oggetto {@link Coverage},
 * che contiene il numero di elementi coperti e non coperti.
 * </p>
 *
 * <p>
 * Questa classe è pensata per uso interno del dominio (principalmente persistenza) e come valore incorporabile in altre entità,
 * non come DTO per comunicazione esterna.
 * </p>
 */
@Data
@NoArgsConstructor
@Embeddable
public class JacocoScore {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "covered", column = @Column(name = "jacoco_line_covered")),
            @AttributeOverride(name = "missed", column = @Column(name = "jacoco_line_missed"))
    })
    private Coverage lineCoverage;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "covered", column = @Column(name = "jacoco_branch_covered")),
            @AttributeOverride(name = "missed", column = @Column(name = "jacoco_branch_missed"))
    })
    private Coverage branchCoverage;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "covered", column = @Column(name = "jacoco_instruction_covered")),
            @AttributeOverride(name = "missed", column = @Column(name = "jacoco_instruction_missed"))
    })
    private Coverage instructionCoverage;
}