package com.robotchallenge.codecompile.jacoco.util;

import testrobotchallenge.commons.models.dto.score.basic.CoverageDTO;
import testrobotchallenge.commons.models.dto.score.basic.JacocoScoreDTO;
import testrobotchallenge.commons.models.dto.score.JacocoCoverageDTO;
import testrobotchallenge.commons.util.ExtractScore;

public class BuildResponse {

    // In assenza di un costruttore esplicito, SonarQube vuole un costruttore privato per evitare la generazione automatica
    // di quello vuoto
    private BuildResponse() {
        throw new IllegalStateException("Classe di utility per la costruzione della risposta");
    }

    public static JacocoScoreDTO buildDTO(String xmlContent) {
        int[][] scores = ExtractScore.fromJacoco(xmlContent);
        JacocoScoreDTO responseBody = new JacocoScoreDTO();

        responseBody.setLineCoverageDTO(new CoverageDTO(
                scores[0][0], scores[0][1]
        ));
        responseBody.setBranchCoverageDTO(new CoverageDTO(
                scores[1][0], scores[1][1]
        ));
        responseBody.setInstructionCoverageDTO(new CoverageDTO(
                scores[2][0], scores[2][1]
        ));

        return responseBody;
    }

    public static JacocoCoverageDTO buildExtendedDTO(String xmlContent, String outCompile, boolean errors) {
        JacocoCoverageDTO responseBody = new JacocoCoverageDTO();
        responseBody.setCoverage(xmlContent);
        responseBody.setErrors(errors);
        responseBody.setOutCompile(outCompile);
        responseBody.setJacocoScoreDTO(buildDTO(xmlContent));
        return responseBody;
    }
}
