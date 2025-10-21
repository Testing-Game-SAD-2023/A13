package testrobotchallenge.commons.mappers;

import org.mapstruct.Named;
import testrobotchallenge.commons.models.dto.score.basic.EvosuiteScoreDTO;
import testrobotchallenge.commons.models.score.EvosuiteScore;

public class EvosuiteScoreMapper {

    /**
     * Converte un oggetto interno {@link EvosuiteScore} in un {@link EvosuiteScoreDTO}.
     *
     * @param score     l'oggetto interno EvosuiteScore da convertire
     * @return un {@link EvosuiteScoreDTO} popolato oppure {@code null} se l'input è null
     */
    @Named("evosuiteScoreToEvosuiteScoreDTO")
    public static EvosuiteScoreDTO toEvosuiteScoreDTO(EvosuiteScore score) {
        if (score == null)
            return null;

        EvosuiteScoreDTO dto = new EvosuiteScoreDTO();
        dto.setLineCoverageDTO(CoverageMapper.toCoverageDTO(score.getLineCoverage()));
        dto.setBranchCoverageDTO(CoverageMapper.toCoverageDTO(score.getBranchCoverage()));
        dto.setExceptionCoverageDTO(CoverageMapper.toCoverageDTO(score.getExceptionCoverage()));
        dto.setWeakMutationCoverageDTO(CoverageMapper.toCoverageDTO(score.getWeakMutationCoverage()));
        dto.setOutputCoverageDTO(CoverageMapper.toCoverageDTO(score.getOutputCoverage()));
        dto.setMethodCoverageDTO(CoverageMapper.toCoverageDTO(score.getMethodCoverage()));
        dto.setMethodNoExceptionCoverageDTO(CoverageMapper.toCoverageDTO(score.getMethodNoExceptionCoverage()));
        dto.setCBranchCoverageDTO(CoverageMapper.toCoverageDTO(score.getCBranchCoverage()));

        return dto;
    }

    /**
     * Converte un {@link EvosuiteScoreDTO} in un oggetto interno {@link EvosuiteScore}.
     *
     * @param dto   il DTO da convertire
     * @return un oggetto {@link EvosuiteScore} popolato oppure {@code null} se l'input è null
     */
    @Named("evosuiteScoreDTOToEvosuiteScore")
    public static EvosuiteScore toEvosuiteScore(EvosuiteScoreDTO dto) {
        if (dto == null)
            return null;

        EvosuiteScore evosuite = new EvosuiteScore();
        evosuite.setLineCoverage(CoverageMapper.toCoverage(dto.getLineCoverageDTO()));
        evosuite.setBranchCoverage(CoverageMapper.toCoverage(dto.getBranchCoverageDTO()));
        evosuite.setExceptionCoverage(CoverageMapper.toCoverage(dto.getExceptionCoverageDTO()));
        evosuite.setWeakMutationCoverage(CoverageMapper.toCoverage(dto.getWeakMutationCoverageDTO()));
        evosuite.setOutputCoverage(CoverageMapper.toCoverage(dto.getOutputCoverageDTO()));
        evosuite.setMethodCoverage(CoverageMapper.toCoverage(dto.getMethodCoverageDTO()));
        evosuite.setMethodNoExceptionCoverage(CoverageMapper.toCoverage(dto.getMethodNoExceptionCoverageDTO()));
        evosuite.setCBranchCoverage(CoverageMapper.toCoverage(dto.getCBranchCoverageDTO()));

        return evosuite;
    }
}
