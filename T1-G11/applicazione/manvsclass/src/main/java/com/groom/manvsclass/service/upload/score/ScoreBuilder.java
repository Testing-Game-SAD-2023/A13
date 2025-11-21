package com.groom.manvsclass.service.upload.score;

import testrobotchallenge.commons.models.score.Coverage;
import testrobotchallenge.commons.models.score.EvosuiteScore;
import testrobotchallenge.commons.models.score.JacocoScore;

public class ScoreBuilder {

    private ScoreBuilder() {
        // Utility class
    }

    public static JacocoScore buildJacocoScore(int[][] statistics) {
        JacocoScore score = new JacocoScore();
        score.setLineCoverage(new Coverage(statistics[0][0], statistics[0][1]));
        score.setBranchCoverage(new Coverage(statistics[1][0], statistics[1][1]));
        score.setInstructionCoverage(new Coverage(statistics[2][0], statistics[2][1]));
        return score;
    }

    public static EvosuiteScore buildEvosuiteScore(int[][] statistics) {
        EvosuiteScore score = new EvosuiteScore();
        score.setLineCoverage(new Coverage(statistics[0][0], statistics[0][1]));
        score.setBranchCoverage(new Coverage(statistics[1][0], statistics[1][1]));
        score.setExceptionCoverage(new Coverage(statistics[2][0], statistics[2][1]));
        score.setWeakMutationCoverage(new Coverage(statistics[3][0], statistics[3][1]));
        score.setOutputCoverage(new Coverage(statistics[4][0], statistics[4][1]));
        score.setMethodCoverage(new Coverage(statistics[5][0], statistics[5][1]));
        score.setMethodNoExceptionCoverage(new Coverage(statistics[6][0], statistics[6][1]));
        score.setCBranchCoverage(new Coverage(statistics[7][0], statistics[7][1]));
        return score;
    }
}
