package testrobotchallenge.commons.models.score;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Rappresenta i risultati delle metriche di copertura calcolate da EvoSuite.
 * <p>
 * Ogni tipo di copertura (linee, branch, metodi, ecc.) è rappresentata da un oggetto {@link Coverage}, che
 * contiene il numero di elementi coperti e non coperti.
 * </p>
 *
 * <p>
 * Questa classe è pensata per uso interno del dominio (principalmente persistenza) e come valore incorporabile in altre entità,
 * non come DTO per comunicazione esterna.
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class EvosuiteScore {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "covered", column = @Column(name = "evosuite_line_covered")),
            @AttributeOverride(name = "missed", column = @Column(name = "evosuite_line_missed"))
    })
    private Coverage lineCoverage;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "covered", column = @Column(name = "evosuite_branch_covered")),
            @AttributeOverride(name = "missed", column = @Column(name = "evosuite_branch_missed"))
    })
    private Coverage branchCoverage;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "covered", column = @Column(name = "evosuite_exception_covered")),
            @AttributeOverride(name = "missed", column = @Column(name = "evosuite_exception_missed"))
    })
    private Coverage exceptionCoverage;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "covered", column = @Column(name = "evosuite_weakMutation_covered")),
            @AttributeOverride(name = "missed", column = @Column(name = "evosuite_weakMutation_missed"))
    })
    private Coverage weakMutationCoverage;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "covered", column = @Column(name = "evosuite_output_covered")),
            @AttributeOverride(name = "missed", column = @Column(name = "evosuite_output_missed"))
    })
    private Coverage outputCoverage;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "covered", column = @Column(name = "evosuite_method_covered")),
            @AttributeOverride(name = "missed", column = @Column(name = "evosuite_method_missed"))
    })
    private Coverage methodCoverage;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "covered", column = @Column(name = "evosuite_methodNoException_covered")),
            @AttributeOverride(name = "missed", column = @Column(name = "evosuite_methodNoException_missed"))
    })
    private Coverage methodNoExceptionCoverage;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "covered", column = @Column(name = "evosuite_cBranch_covered")),
            @AttributeOverride(name = "missed", column = @Column(name = "evosuite_cBranch_missed"))
    })
    private Coverage cBranchCoverage;
}
