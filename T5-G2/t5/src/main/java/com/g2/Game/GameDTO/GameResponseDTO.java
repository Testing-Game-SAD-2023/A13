package com.g2.Game.GameDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameResponseDTO {

    @JsonProperty("outCompile")
    private String outCompile;

    @JsonProperty("coverage")
    private String coverage;

    @JsonProperty("robotScore")
    private int robotScore;

    @JsonProperty("userScore")
    private int userScore;

    @JsonProperty("GameOver")
    private boolean gameOver;

    @JsonProperty("coverageDetails")
    private CoverageDetails coverageDetails;

    @JsonProperty("isWinner")
    private Boolean isWinner;

    // Getters and setters
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

    public Boolean getIsWinner() {
        return isWinner;
    }

    public void setIsWinner(Boolean isWinner) {
        this.isWinner = isWinner;
    }

    // Classe interna che rappresenta la struttura di coverageDetails
    public static class CoverageDetails {

        @JsonProperty("line")
        private CoverageDetail line;

        @JsonProperty("branch")
        private CoverageDetail branch;

        @JsonProperty("instruction")
        private CoverageDetail instruction;

        // Getters and setters
        public CoverageDetail getLine() {
            return line;
        }

        public void setLine(CoverageDetail line) {
            this.line = line;
        }

        public CoverageDetail getBranch() {
            return branch;
        }

        public void setBranch(CoverageDetail branch) {
            this.branch = branch;
        }

        public CoverageDetail getInstruction() {
            return instruction;
        }

        public void setInstruction(CoverageDetail instruction) {
            this.instruction = instruction;
        }
    }

    // Classe interna che rappresenta un singolo oggetto di coverage (covered, missed)
    public static class CoverageDetail {

        @JsonProperty("covered")
        private int covered;

        @JsonProperty("missed")
        private int missed;

        public CoverageDetail(int covered2, int missed2) {
            this.covered = covered2;
            this.missed = missed2;
        }

        // Getters and setters
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
}
