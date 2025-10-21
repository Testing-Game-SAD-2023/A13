package com.g2.game.gameMode.Compile;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CoverageResult {
    @JsonProperty("covered")
    private int covered;
    @JsonProperty("missed")
    private int missed;
    @JsonProperty("percentage")
    private double percentage;
    @JsonProperty("errorMessage")
    private String errorMessage;

    public CoverageResult() {
    }

    public CoverageResult(int covered, int missed) {
        this.covered = covered;
        this.missed = missed;
        this.percentage = calculatePercentage(covered, missed);
        this.errorMessage = null;
    }

    public CoverageResult(String errorMessage) {
        this.covered = 0;
        this.missed = 0;
        this.percentage = 0.0;
        this.errorMessage = errorMessage;
    }

    private double calculatePercentage(int covered, int missed) {
        if (covered + missed == 0) {
            return 0.0; // No coverage data available
        }
        return (double) covered / (covered + missed) * 100;
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

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "CoverageResult{" +
                "covered=" + covered +
                ", missed=" + missed +
                ", percentage=" + percentage +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
