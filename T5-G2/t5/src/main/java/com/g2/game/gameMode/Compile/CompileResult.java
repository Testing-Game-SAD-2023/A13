package com.g2.game.gameMode.Compile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.g2.game.gameMode.GameLogic;
import com.g2.interfaces.ServiceManager;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import testrobotchallenge.commons.models.dto.score.EvosuiteCoverageDTO;
import testrobotchallenge.commons.models.dto.score.JacocoCoverageDTO;
import testrobotchallenge.commons.models.dto.score.basic.CoverageDTO;
import testrobotchallenge.commons.models.dto.score.basic.EvosuiteScoreDTO;
import testrobotchallenge.commons.models.dto.score.basic.JacocoScoreDTO;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

@Getter
@Setter
@ToString
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
    @JsonProperty("xml_coverage")
    private String xmlCoverage;
    /*
     * Dettagli della coverage JaCoCo
     * I nomi sono stati portati in camelCase per problemi con
     * la deserializzazione di Jackson, che cerca le proprietà in
     * camelCase
     */
    @JsonProperty("jacoco_line")
    private CoverageResult lineCoverage = new CoverageResult(0, 0);

    @JsonProperty("jacoco_branch")
    private CoverageResult branchCoverage = new CoverageResult(0, 0);

    @JsonProperty("jacoco_instruction")
    private CoverageResult instructionCoverage = new CoverageResult(0, 0);

    @JsonProperty("evosuite_line")
    private CoverageResult evosuiteLine = new CoverageResult(0, 0);

    @JsonProperty("evosuite_branch")
    private CoverageResult evosuiteBranch = new CoverageResult(0, 0);

    @JsonProperty("evosuite_exception")
    private CoverageResult evosuiteException = new CoverageResult(0, 0);

    @JsonProperty("evosuite_weak_mutation")
    private CoverageResult evosuiteWeakMutation = new CoverageResult(0, 0);

    @JsonProperty("evosuite_output")
    private CoverageResult evosuiteOutput = new CoverageResult(0, 0);

    @JsonProperty("evosuite_method")
    private CoverageResult evosuiteMethod = new CoverageResult(0, 0);

    @JsonProperty("evosuite_method_no_exception")
    private CoverageResult evosuiteMethodNoException = new CoverageResult(0, 0);

    @JsonProperty("evosuite_cbranch")
    private CoverageResult evosuiteCBranch = new CoverageResult(0, 0);

    @JsonIgnore
    private ServiceManager serviceManager;

    @JsonIgnore
    private CoverageService coverageService;

    // Costruttore di base
    public CompileResult() {
    }

    public CompileResult(String xmlCoverage, String compileOutput, CoverageService coverageService, ServiceManager serviceManager) {
        this.xmlCoverage = xmlCoverage;
        this.compileOutput = compileOutput;
        this.coverageService = coverageService;
        this.serviceManager = serviceManager;
    }

    public CompileResult(JacocoCoverageDTO jacocoDTO, EvosuiteCoverageDTO evosuiteDTO) {
        this.xmlCoverage = jacocoDTO.getCoverage();
        this.compileOutput = jacocoDTO.getOutCompile();

        extractJacocoScore(jacocoDTO.getJacocoScoreDTO());
        extractEvosuiteScore(evosuiteDTO != null ? evosuiteDTO.getEvosuiteScoreDTO() : null);
    }

    public CompileResult(CompileResult base, EvosuiteCoverageDTO evosuiteDTO) {
        this.xmlCoverage = base.xmlCoverage;
        this.compileOutput = base.compileOutput;
        this.lineCoverage = base.lineCoverage;
        this.branchCoverage = base.branchCoverage;
        this.instructionCoverage = base.instructionCoverage;

        extractEvosuiteScore(evosuiteDTO != null ? evosuiteDTO.getEvosuiteScoreDTO() : null);
    }

    public CompileResult(GameLogic game, ServiceManager manager, String testClass, String type, OpponentDifficulty difficulty) {
        this.serviceManager = manager;
        this.coverageService = null;
        this.compileOutput = "Robot no console output";

        this.xmlCoverage = manager.handleRequest("T1", "getOpponentCoverage", String.class, testClass, type, difficulty);

        extractJacocoScore(manager.handleRequest("T1", "getOpponentJacocoScore", JacocoScoreDTO.class, testClass, type, difficulty));
        extractEvosuiteScore(manager.handleRequest("T1", "getOpponentEvosuiteScore", EvosuiteScoreDTO.class, testClass, type, difficulty));
    }

    private void extractJacocoScore(JacocoScoreDTO score) {
        if (score == null || score.getLineCoverageDTO() == null) return;

        this.lineCoverage = new CoverageResult(score.getLineCoverageDTO().getCovered(), score.getLineCoverageDTO().getMissed());
        this.branchCoverage = new CoverageResult(score.getBranchCoverageDTO().getCovered(), score.getBranchCoverageDTO().getMissed());
        this.instructionCoverage = new CoverageResult(score.getInstructionCoverageDTO().getCovered(), score.getInstructionCoverageDTO().getMissed());
    }

    private void extractEvosuiteScore(EvosuiteScoreDTO score) {
        this.evosuiteLine = coverageOrEmpty(score != null ? score.getLineCoverageDTO() : null);
        this.evosuiteBranch = coverageOrEmpty(score != null ? score.getBranchCoverageDTO() : null);
        this.evosuiteException = coverageOrEmpty(score != null ? score.getExceptionCoverageDTO() : null);
        this.evosuiteWeakMutation = coverageOrEmpty(score != null ? score.getWeakMutationCoverageDTO() : null);
        this.evosuiteOutput = coverageOrEmpty(score != null ? score.getOutputCoverageDTO() : null);
        this.evosuiteMethod = coverageOrEmpty(score != null ? score.getMethodCoverageDTO() : null);
        this.evosuiteMethodNoException = coverageOrEmpty(score != null ? score.getMethodNoExceptionCoverageDTO() : null);
        this.evosuiteCBranch = coverageOrEmpty(score != null ? score.getCBranchCoverageDTO() : null);
    }

    private CoverageResult coverageOrEmpty(CoverageDTO dto) {
        return dto == null ? new CoverageResult(0, 0) : new CoverageResult(dto.getCovered(), dto.getMissed());
    }

    /*
     * Jackson include automaticamente i metodi getter come proprietà JSON,
     * quindi il metodo getSuccess() viene serializzato come "success".
     * Durante la deserializzazione, che avviene durante il recupero della sessione,
     * Jackson non trova un campo corrispondente (`private Boolean success;`
     * o `setSuccess()`), causando l'errore "Unrecognized field 'success'".
     * Il metodo è stato quindi rinominato in `hasSuccess`
     */
    public boolean hasSuccess() {
        //Se true Il test dell'utente è stato compilato => nessun errore di compilazione nel test
        String xml = getXmlCoverage();
        return !(xml == null || xml.isEmpty());
    }
}
