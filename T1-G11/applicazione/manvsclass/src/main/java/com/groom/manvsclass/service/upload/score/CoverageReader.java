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
            try {
                String content = Files.readString(evosuitePath);
                int[][] result = ExtractScore.fromEvosuite(content);
                if (result == null) {
                    throw new IOException(
                        "Il file di coverage EvoSuite '" + evosuitePath.getFileName() + "' è vuoto o in formato non valido. " +
                        "Verificare che il file statistics.csv contenga dati di coverage validi."
                    );
                }
                return result;
            } catch (IOException e) {
                throw new IOException(
                    "Errore durante la lettura del file di coverage EvoSuite: " + e.getMessage() + ". " +
                    "Il file potrebbe essere corrotto o in un formato non riconosciuto.",
                    e
                );
            }
        }
        return new int[8][2];
    }

    private static int[][] readJacocoIfExists(Path jacocoPath) throws IOException {
        if (Files.exists(jacocoPath)) {
            try {
                String content = Files.readString(jacocoPath);
                int[][] result = ExtractScore.fromJacoco(content);
                if (result == null) {
                    throw new IOException(
                        "Il file di coverage JaCoCo '" + jacocoPath.getFileName() + "' è vuoto o in formato non valido. " +
                        "Verificare che il file coveragetot.xml contenga dati XML di coverage validi."
                    );
                }
                return result;
            } catch (IOException e) {
                throw new IOException(
                    "Errore durante la lettura del file di coverage JaCoCo: " + e.getMessage() + ". " +
                    "Il file XML potrebbe essere corrotto o non conforme allo schema JaCoCo.",
                    e
                );
            }
        }
        return new int[3][2];
    }

    private static String readJacococXmlIfExists(Path jacocoPath) throws IOException {
        if (Files.exists(jacocoPath)) {
            try {
                String content = Files.readString(jacocoPath);
                if (content == null || content.trim().isEmpty()) {
                    throw new IOException(
                        "Il file di coverage JaCoCo XML è vuoto. " +
                        "Verificare che il file coveragetot.xml sia stato generato correttamente."
                    );
                }
                return content;
            } catch (IOException e) {
                throw new IOException(
                    "Impossibile leggere il file XML di coverage JaCoCo: " + e.getMessage(),
                    e
                );
            }
        }
        return null;
    }
}
