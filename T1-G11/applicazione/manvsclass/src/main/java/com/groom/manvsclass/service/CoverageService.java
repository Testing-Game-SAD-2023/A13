package com.groom.manvsclass.service;

import com.groom.manvsclass.api.ApiGatewayClient;
import org.springframework.stereotype.Service;
import testrobotchallenge.commons.models.dto.score.EvosuiteCoverageDTO;
import testrobotchallenge.commons.models.dto.score.JacocoCoverageDTO;

import java.io.File;

@Service
public class CoverageService {

    private final ApiGatewayClient apiGatewayClient;

    public CoverageService(ApiGatewayClient apiGatewayClient) {
        this.apiGatewayClient = apiGatewayClient;
    }

    public EvosuiteCoverageDTO generateMissingEvoSuiteCoverage(String classUTName, String srcPackage, File zip) {
        return apiGatewayClient.callGenerateMissingEvoSuiteCoverage(classUTName, srcPackage, zip);
    }

    public JacocoCoverageDTO generateMissingJacocoCoverage(String classUTName, File zip) {
        return apiGatewayClient.callGenerateMissingJacocoCoverage(classUTName, zip);
    }
}
