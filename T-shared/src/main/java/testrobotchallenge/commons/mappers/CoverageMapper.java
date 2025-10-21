package testrobotchallenge.commons.mappers;

import org.mapstruct.Named;
import testrobotchallenge.commons.models.dto.score.basic.CoverageDTO;
import testrobotchallenge.commons.models.score.Coverage;

public class CoverageMapper {

    /**
     * Converte un oggetto interno {@link Coverage} in un {@link CoverageDTO}.
     *
     * @param coverage  l'oggetto interno Coverage da convertire
     * @return un {@link CoverageDTO} popolato oppure {@code null} se l'input è null
     */
    public static CoverageDTO toCoverageDTO(Coverage coverage) {
        if (coverage == null) return null;
        CoverageDTO dto = new CoverageDTO();
        dto.setCovered(coverage.getCovered());
        dto.setMissed(coverage.getMissed());
        return dto;
    }

    /**
     * Converte un {@link CoverageDTO} in un oggetto interno {@link Coverage}.
     *
     * @param dto   il DTO da convertire
     * @return un oggetto {@link Coverage} popolato oppure {@code null} se l'input è null
     */
    @Named("coverageDTOToCoverage")
    public static Coverage toCoverage(CoverageDTO dto) {
        if (dto == null) return null;
        Coverage coverage = new Coverage();
        coverage.setCovered(dto.getCovered());
        coverage.setMissed(dto.getMissed());
        return coverage;
    }
}
