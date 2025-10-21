package testrobotchallenge.commons.mappers;

import org.mapstruct.Named;
import testrobotchallenge.commons.models.dto.score.basic.JacocoScoreDTO;
import testrobotchallenge.commons.models.score.JacocoScore;

public class JacocoScoreMapper {

    private JacocoScoreMapper() {
        throw new IllegalStateException("Classe di mapping che converte le metriche di " +
                "JaCoCo nei relativi DTO e viceversa");
    }

    /**
     * Converte un oggetto interno {@link JacocoScore} in un {@link JacocoScoreDTO}.
     *
     * @param score     l'oggetto interno JacocoScore da convertire
     * @return un {@link JacocoScoreDTO} popolato oppure {@code null} se l'input è null
     */
    @Named("jacocoScoreToJacocoScoreDTO")
    public static JacocoScoreDTO toJacocoScoreDTO(JacocoScore score) {
        if (score == null)
            return null;

        JacocoScoreDTO dto = new JacocoScoreDTO();
        dto.setLineCoverageDTO(CoverageMapper.toCoverageDTO(score.getLineCoverage()));
        dto.setBranchCoverageDTO(CoverageMapper.toCoverageDTO(score.getBranchCoverage()));
        dto.setInstructionCoverageDTO(CoverageMapper.toCoverageDTO(score.getInstructionCoverage()));

        return dto;
    }

    /**
     * Converte un {@link JacocoScoreDTO} in un oggetto interno {@link JacocoScore}.
     *
     * @param dto   il DTO da convertire
     * @return un oggetto {@link JacocoScore} popolato oppure {@code null} se l'input è null
     */
    @Named("jacocoScoreDTOToJacocoScore")
    public static JacocoScore toJacocoScore(JacocoScoreDTO dto) {
        if (dto == null)
            return null;

        JacocoScore jacoco = new JacocoScore();
        jacoco.setLineCoverage(CoverageMapper.toCoverage(dto.getLineCoverageDTO()));
        jacoco.setBranchCoverage(CoverageMapper.toCoverage(dto.getBranchCoverageDTO()));
        jacoco.setInstructionCoverage(CoverageMapper.toCoverage(dto.getInstructionCoverageDTO()));

        return jacoco;
    }

}
