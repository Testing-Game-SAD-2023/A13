package com.g2.Game.GameModes.Compile;

import com.g2.Game.GameModes.GameLogic;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.g2.Interfaces.ServiceManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class CompileResult {
    /*
     * Istanza di default 
     */
    public static final CompileResult DEFAULT = new CompileResult(
            "", // XML_coverage vuoto
            "", // Compile output vuoto
            (CoverageService) null, // CoverageService nullo
            null // ServiceManager nullo
    );
    /*
     * Campi 
     */
    @JsonProperty("compileOutput")
    private String compileOutput;
    @JsonProperty("XML_coverage")
    private String XML_coverage;
    /*
     * Dettagli della coverage JaCoCo
     * I nomi sono stati portati in camelCase per problemi con
     * la deserializzazione di Jackson, che cerca le proprietà in
     * camelCase
     */
    @JsonProperty("jacoco_line")
    private CoverageResult lineCoverage = null;
    @JsonProperty("jacoco_branch")
    private CoverageResult branchCoverage = null;
    @JsonProperty("jacoco_instruction")
    private CoverageResult instructionCoverage = null;
    /*
     * Dettagli della coverage Evosuite
     */
    @JsonProperty("evosuite_line")
    private int evosuiteLine = 0;
    @JsonProperty("evosuite_branch")
    private int evosuiteBranch = 0;
    @JsonProperty("evosuite_exception")
    private int evosuiteException = 0;
    @JsonProperty("evosuite_weak_mutation")
    private int evosuiteWeakMutation = 0;
    @JsonProperty("evosuite_output")
    private int evosuiteOutput = 0;
    @JsonProperty("evosuite_method")
    private int evosuiteMethod = 0;
    @JsonProperty("evosuite_method_no_exception")
    private int evosuiteMethodNoException = 0;
    @JsonProperty("evosuite_cbranch")
    private int evosuiteCBranch = 0;
    /*
     * Servizi usati 
     */
    @JsonIgnore
    private ServiceManager serviceManager;
    @JsonIgnore
    private CoverageService coverageService;

    //Costruttore Vuoto
    public CompileResult(){
    }

    // Costruttore con XML coverage
    public CompileResult(String XML_coverage, String compileOutput, CoverageService coverageService, ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
        this.coverageService = coverageService;
        this.compileOutput = compileOutput;
        this.XML_coverage = XML_coverage;
        calculateCoverage(); // Calcola coverage
    }

    // Costruttore per l'utente
    public CompileResult(JSONObject response_T8, JSONObject response_T7) {
        // Estraggo i valori dalle risposte
        this.evosuiteLine = response_T8.optInt("evoSuiteLine", 0);
        this.evosuiteBranch = response_T8.optInt("evoSuiteBranch", 0);
        this.evosuiteException = response_T8.optInt("evoSuiteException", 0);
        this.evosuiteWeakMutation = response_T8.optInt("evoSuiteWeakMutation", 0);
        this.evosuiteOutput = response_T8.optInt("evoSuiteOutput", 0);
        this.evosuiteMethod = response_T8.optInt("evoSuiteMethod", 0);
        this.evosuiteMethodNoException = response_T8.optInt("evoSuiteMethodNoException", 0);
        this.evosuiteCBranch = response_T8.optInt("evoSuiteCBranch", 0);

        this.XML_coverage = response_T7.optString("coverage", null);
        this.compileOutput = response_T7.optString("outCompile", null);
        this.coverageService = new CoverageService();
        calculateCoverage(); // Calcolo coverage
    }

    // Costruttore (temporaneo) per elaborare la risposta di T8 solo al Submit dell'utente
    public CompileResult(CompileResult compileResult, JSONObject response_T8) {
        // Estraggo i valori dalle risposte
        this.evosuiteLine = response_T8.optInt("evoSuiteLine", 0);
        this.evosuiteBranch = response_T8.optInt("evoSuiteBranch", 0);
        this.evosuiteException = response_T8.optInt("evoSuiteException", 0);
        this.evosuiteWeakMutation = response_T8.optInt("evoSuiteWeakMutation", 0);
        this.evosuiteOutput = response_T8.optInt("evoSuiteOutput", 0);
        this.evosuiteMethod = response_T8.optInt("evoSuiteMethod", 0);
        this.evosuiteMethodNoException = response_T8.optInt("evoSuiteMethodNoException", 0);
        this.evosuiteCBranch = response_T8.optInt("evoSuiteCBranch", 0);

        this.XML_coverage = compileResult.XML_coverage;
        this.compileOutput = compileResult.compileOutput;
        this.lineCoverage = compileResult.lineCoverage;
        this.branchCoverage = compileResult.branchCoverage;
        this.instructionCoverage = compileResult.instructionCoverage;
    }

    // Costruttore che chiama T4 per i robot
    public CompileResult(GameLogic currentGame, ServiceManager serviceManager, String testClass, String robot_type, String difficulty) {
        this.serviceManager = serviceManager;
        String response_T4 = this.serviceManager.handleRequest("T4", "GetRisultati",
                String.class, testClass, robot_type, difficulty);

        JSONObject JsonResponseT4 = new JSONObject(response_T4);

        this.XML_coverage = JsonResponseT4.get("coverage").toString();
        this.compileOutput = "Robot no console output";
        this.coverageService = null;

        this.lineCoverage = new CoverageResult(
                JsonResponseT4.getInt("jacocoLineCovered"),
                JsonResponseT4.getInt("jacocoLineMissed")
        );

        this.branchCoverage = new CoverageResult(
                JsonResponseT4.getInt("jacocoBranchCovered"),
                JsonResponseT4.getInt("jacocoBranchMissed")
        );

        this.instructionCoverage = new CoverageResult(
                JsonResponseT4.getInt("jacocoInstructionCovered"),
                JsonResponseT4.getInt("jacocoInstructionMissed")
        );

        response_T4 = this.serviceManager.handleRequest("T4", "evosuiteRobotCoverage",
                String.class, testClass, robot_type, difficulty);

        JSONObject responseObj = new JSONObject(response_T4);
        this.evosuiteLine = responseObj.optInt("evoSuiteLine", 0);
        this.evosuiteBranch = responseObj.optInt("evoSuiteBranch", 0);
        this.evosuiteException = responseObj.optInt("evoSuiteException", 0);
        this.evosuiteWeakMutation = responseObj.optInt("evoSuiteWeakMutation", 0);
        this.evosuiteOutput = responseObj.optInt("evoSuiteOutput", 0);
        this.evosuiteMethod = responseObj.optInt("evoSuiteMethod", 0);
        this.evosuiteMethodNoException = responseObj.optInt("evoSuiteMethodNoException", 0);
        this.evosuiteCBranch = responseObj.optInt("evoSuiteCBranch", 0);
    }

    private void calculateCoverage() {
        if (this.XML_coverage != null && !this.XML_coverage.isEmpty()) {
            this.lineCoverage = coverageService.getCoverage(this.XML_coverage, "LINE");
            this.branchCoverage = coverageService.getCoverage(this.XML_coverage, "BRANCH");
            this.instructionCoverage = coverageService.getCoverage(this.XML_coverage, "INSTRUCTION");
        } else {
            this.lineCoverage = new CoverageResult(0, 0);
            this.branchCoverage = new CoverageResult(0, 0);
            this.instructionCoverage = new CoverageResult(0, 0);
        }
    }

    /*
     * Jackson include automaticamente i metodi getter come proprietà JSON,
     * quindi il metodo getSuccess() viene serializzato come "success".
     * Durante la deserializzazione, che avviene durante il recupero della sessione,
     * Jackson non trova un campo corrispondente (`private Boolean success;`
     * o `setSuccess()`), causando l'errore "Unrecognized field 'success'".
     * Il metodo è stato quindi rinominato in `hasSuccess`
     */
    public Boolean hasSuccess() {
        //Se true Il test dell'utente è stato compilato => nessun errore di compilazione nel test
        String XML= getXML_coverage();
        return !(XML == null || XML.isEmpty());
    }

    // Getter per il risultato della copertura
    public String getXML_coverage() {
        return XML_coverage;
    }

    // Getter per il risultato della compilazione
    public String getCompileOutput() {
        return compileOutput;
    }

    public CoverageResult getBranchCoverage() {
        return branchCoverage;
    }

    public void setBranchCoverage(CoverageResult BranchCoverage) {
        this.branchCoverage = BranchCoverage;
    }

    public CoverageResult getInstructionCoverage() {
        return instructionCoverage;
    }

    public CoverageResult getLineCoverage() {
        return lineCoverage;
    }

    public void setLineCoverage(CoverageResult LineCoverage) {
        this.lineCoverage = LineCoverage;
    }

    public void setCompileOutput(String compileOutput) {
        this.compileOutput = compileOutput;
    }

    public void setXML_coverage(String XML_coverage) {
        this.XML_coverage = XML_coverage;
    }

    public int getEvosuiteLine() {
        return evosuiteLine;
    }

    public int getEvosuiteBranch() {
        return evosuiteBranch;
    }

    public int getEvosuiteException() {
        return evosuiteException;
    }

    public int getEvosuiteWeakMutation() {
        return evosuiteWeakMutation;
    }

    public int getEvosuiteOutput() {
        return evosuiteOutput;
    }

    public int getEvosuiteMethod() {
        return evosuiteMethod;
    }

    public int getEvosuiteMethodNoException() {
        return evosuiteMethodNoException;
    }

    public int getEvosuiteCBranch() {
        return evosuiteCBranch;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public CoverageService getCoverageService() {
        return coverageService;
    }

    @Override
    public String toString() {
        return "CompileResult{" +
                "compileOutput='" + compileOutput + '\'' +
                ", XML_coverage='" + XML_coverage + '\'' +
                ", LineCoverage=" + lineCoverage +
                ", BranchCoverage=" + branchCoverage +
                ", InstructionCoverage=" + instructionCoverage +
                ", evosuiteLine=" + evosuiteLine +
                ", evosuiteBranch=" + evosuiteBranch +
                ", evosuiteException=" + evosuiteException +
                ", evosuiteWeakMutation=" + evosuiteWeakMutation +
                ", evosuiteOutput=" + evosuiteOutput +
                ", evosuiteMethod=" + evosuiteMethod +
                ", evosuiteMethodNoException=" + evosuiteMethodNoException +
                ", evosuiteCBranch=" + evosuiteCBranch +
                ", serviceManager=" + serviceManager +
                ", coverageService=" + coverageService +
                '}';
    }
}
