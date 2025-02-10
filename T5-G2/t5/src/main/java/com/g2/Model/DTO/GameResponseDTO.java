package com.g2.Model.DTO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class GameResponseDTO {
    private String outCompile;
    private String coverage;
    private int robotScore;
    private int userScore;
    private boolean gameOver;
    private CoverageDetails coverageDetails;

    // Costruttore e metodi di accesso

    public GameResponseDTO(String outCompile, String coverage, int robotScore, int userScore, boolean gameOver) {
        this.outCompile = outCompile;
        this.coverage = coverage;
        this.robotScore = robotScore;
        this.userScore = userScore;
        this.gameOver = gameOver;
        this.coverageDetails = new CoverageDetails();

        // Calcoliamo le coperture internamente al DTO
        coverageDetails.setLineCoverage(coverage);
        coverageDetails.setBranchCoverage(coverage);
        coverageDetails.setInstructionCoverage(coverage);
    }

    // Getters e setters
    public static class CoverageDetails {
        private Coverage line;
        private Coverage branch;
        private Coverage instruction;

        // Set di copertura per line, branch, instruction
        public void setLineCoverage(String coverageData) {
            this.line = getCoverage(coverageData, "LINE");
        }

        public void setBranchCoverage(String coverageData) {
            this.branch = getCoverage(coverageData, "BRANCH");
        }

        public void setInstructionCoverage(String coverageData) {
            this.instruction = getCoverage(coverageData, "INSTRUCTION");
        }

        private Coverage getCoverage(String cov, String coverageType) {
            try {
                Document doc = Jsoup.parse(cov, "", Parser.xmlParser());
                // Selezione dell'elemento counter in base al tipo di copertura
                Element counter = doc.selectFirst("report > counter[type=" + coverageType + "]");

                if (counter == null) {
                    throw new IllegalArgumentException("Elemento 'counter' di tipo '" + coverageType + "' non trovato nel documento XML.");
                }

                int covered = Integer.parseInt(counter.attr("covered"));
                int missed = Integer.parseInt(counter.attr("missed"));

                return new Coverage(covered, missed);
            } catch (NumberFormatException e) {
                //logger.error("[GAMECONTROLLER] getCoverage:", e);
                throw new IllegalArgumentException("Gli attributi 'covered' e 'missed' devono essere numeri interi validi.", e);
            } catch (Exception e) {
                //logger.error("[GAMECONTROLLER] getCoverage:", e);
                throw new RuntimeException("Errore durante l'elaborazione del documento XML.", e);
            }
        }

        public static class Coverage {
            private int covered;
            private int missed;

            public Coverage(int covered, int missed) {
                this.covered = covered;
                this.missed = missed;
            }

            public int getCovered() {
                return covered;
            }

            public void setCovered(int covered) {
                this.covered = covered;
            }

            public int getMissed() {
                return missed;
            }

            public void setMissed(int missed) {
                this.missed = missed;
            }
        }

        // Getters per le coperture
        public Coverage getLine() {
            return line;
        }

        public Coverage getBranch() {
            return branch;
        }

        public Coverage getInstruction() {
            return instruction;
        }
    }

    // Getters per i campi principali
    public String getOutCompile() {
        return outCompile;
    }

    public void setOutCompile(String outCompile) {
        this.outCompile = outCompile;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public int getRobotScore() {
        return robotScore;
    }

    public void setRobotScore(int robotScore) {
        this.robotScore = robotScore;
    }

    public int getUserScore() {
        return userScore;
    }

    public void setUserScore(int userScore) {
        this.userScore = userScore;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public CoverageDetails getCoverageDetails() {
        return coverageDetails;
    }

    public void setCoverageDetails(CoverageDetails coverageDetails) {
        this.coverageDetails = coverageDetails;
    }
}

