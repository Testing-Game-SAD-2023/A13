package com.g2.Game.GameModes.Coverage;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.g2.Interfaces.ServiceManager;

public class CompileResult {

    /*
     * Campi 
     */
    private final String compileOutput;
    private final String XML_coverage;

    private CoverageResult LineCoverage = null;
    private CoverageResult BranchCoverage = null; 
    private CoverageResult InstructionCoverage = null;
    /*
     * Servizi usati 
     */
    private final ServiceManager serviceManager;
    private final CoverageService coverageService;

    // Logger per la classe
    private static final Logger logger = LoggerFactory.getLogger(CompileResult.class);

    // Costruttore con XML coverage
    public CompileResult(String XML_coverage, String compileOutput, CoverageService coverageService, ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
        this.coverageService = coverageService; // Salvo il servizio
        this.compileOutput = compileOutput;
        this.XML_coverage = XML_coverage;
        calculateCoverage(); // Calcola coverage
    }

    // Costruttore con richiesta a T1 e T7
    public CompileResult(String ClassName, String testingClassCode, ServiceManager serviceManager) {
        String testingClassName = "Test" + ClassName + ".java";
        String underTestClassName = ClassName + ".java";
        this.serviceManager = serviceManager;
        // Recupero il codice della classe under test
        String underTestClassCode = this.serviceManager.handleRequest("T1", "getClassUnderTest", String.class, ClassName);
        // Chiamata a T7 per calcolare coverage
        String response_T7 = this.serviceManager.handleRequest("T7", "CompileCoverage", String.class, testingClassName, testingClassCode, underTestClassName, underTestClassCode);
        // Estraggo i valori dalla risposta
        JSONObject responseObj = new JSONObject(response_T7);
        this.XML_coverage = responseObj.optString("coverage", null);
        this.compileOutput = responseObj.optString("outCompile", null);
        this.coverageService = new CoverageService();
        calculateCoverage(); // Calcolo coverage
    }

    private void calculateCoverage() {
        if (this.XML_coverage != null) {
            this.LineCoverage = coverageService.getCoverage(this.XML_coverage, "LINE");
            this.BranchCoverage = coverageService.getCoverage(this.XML_coverage, "BRANCH");
            this.InstructionCoverage = coverageService.getCoverage(this.XML_coverage, "INSTRUCTION");
        }else{
            logger.warn("XML coverage è nulla. Coverage results sarà nulla");
        }
    }


    public Boolean getSuccess(){
        //Se true Il test dell'utente è stato compilato => nessun errore di compilazione nel test
        return !(getXML_coverage() == null || getXML_coverage().isEmpty());
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
        return BranchCoverage;
    }

    public void setBranchCoverage(CoverageResult BranchCoverage) {
        this.BranchCoverage = BranchCoverage;
    }

    public CoverageResult getInstructionCoverage() {
        return InstructionCoverage;
    }

    public CoverageResult getLineCoverage() {
        return LineCoverage;
    }

    public void setLineCoverage(CoverageResult LineCoverage) {
        this.LineCoverage = LineCoverage;
    }

}
