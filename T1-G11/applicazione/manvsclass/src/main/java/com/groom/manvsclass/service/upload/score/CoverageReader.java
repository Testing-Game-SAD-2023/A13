package com.groom.manvsclass.service.upload.score;

import testrobotchallenge.commons.util.ExtractScore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CoverageReader {

    private CoverageReader() {
        // Utility class
    }

    public static class CoverageData {
        public final int[][] evosuiteStatistics;
        public final int[][] jacocoStatistics;
        public final String jacococCoverageXml;

        public CoverageData(int[][] evosuiteStatistics, int[][] jacocoStatistics, String jacococCoverageXml) {
            this.evosuiteStatistics = evosuiteStatistics;
            this.jacocoStatistics = jacocoStatistics;
            this.jacococCoverageXml = jacococCoverageXml;
        }
    }

    public static CoverageData readCoverageFiles(Path evosuitePath, Path jacocoPath) throws IOException {
        int[][] evosuiteStatistics = readEvosuiteIfExists(evosuitePath);
        int[][] jacocoStatistics = readJacocoIfExists(jacocoPath);
        String coverageXml = readJacococXmlIfExists(jacocoPath);

        return new CoverageData(evosuiteStatistics, jacocoStatistics, coverageXml);
    }

    private static int[][] readEvosuiteIfExists(Path evosuitePath) throws IOException {
        if (Files.exists(evosuitePath)) {
            String content = Files.readString(evosuitePath);
            return ExtractScore.fromEvosuite(content);
        }
        return new int[8][2];
    }

    private static int[][] readJacocoIfExists(Path jacocoPath) throws IOException {
        if (Files.exists(jacocoPath)) {
            String content = Files.readString(jacocoPath);
            return ExtractScore.fromJacoco(content);
        }
        return new int[3][2];
    }

    private static String readJacococXmlIfExists(Path jacocoPath) throws IOException {
        if (Files.exists(jacocoPath)) {
            return Files.readString(jacocoPath);
        }
        return null;
    }
}
