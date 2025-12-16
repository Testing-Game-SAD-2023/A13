package com.groom.manvsclass.api;

import com.groom.manvsclass.dto.OpponentDTO;
import com.groom.manvsclass.dto.RequestEvosuiteCoverageDTO;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import testrobotchallenge.commons.models.dto.auth.JwtValidationResponseDTO;
import testrobotchallenge.commons.models.dto.score.EvosuiteCoverageDTO;
import testrobotchallenge.commons.models.dto.score.JacocoCoverageDTO;
import testrobotchallenge.commons.models.opponent.GameMode;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import com.groom.manvsclass.security.JwtRequestContext;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class ApiGatewayClient {

    private final RestExchangeTemplateHelper exchangeHelper;
    private final Logger logger = LoggerFactory.getLogger(ApiGatewayClient.class);

    @Value("${API_GATEWAY_ENDPOINT:api-gateway_controller}")
    private String apiGatewayHost;
    @Value("${API_GATEWAY_PORT:8090}")
    private int apiGatewayPort;

    private String userServiceUrl;
    private String jacocoCoverageServiceUrl;
    private String evosuiteCoverageServiceUrl;

    public ApiGatewayClient(RestExchangeTemplateHelper exchangeHelper) {
        this.exchangeHelper = exchangeHelper;
    }

    @PostConstruct
    public void init() {
        String baseUrl = String.format("http://%s:%d", apiGatewayHost, apiGatewayPort);
        userServiceUrl = baseUrl + "/userService";
        jacocoCoverageServiceUrl = baseUrl + "/compile/jacoco";
        evosuiteCoverageServiceUrl = baseUrl + "/compile/evosuite";
        logger.info("API Gateway Base URL: {}", baseUrl);
    }

    public JwtValidationResponseDTO callValidateJwtToken(String jwtToken) {
        ResponseEntity<JwtValidationResponseDTO> response = exchangeHelper.exchange(
                userServiceUrl + "/auth/validateToken?jwt=" + jwtToken,
                null, HttpMethod.POST, null, null, JwtValidationResponseDTO.class);

        return (response == null) ? null : response.getBody();
    }

    public String callRefreshJwtToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "jwt-refresh=" + refreshToken);

        ResponseEntity<String> response = exchangeHelper.exchange(
                userServiceUrl + "/auth/refreshToken",
                null, HttpMethod.POST, headers, null, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
            if (cookies != null) {
                for (String cookie : cookies) {
                    if (cookie.startsWith("jwt=")) {
                        return cookie;
                    }
                }
            }
        }
        throw new RuntimeException("Invalid refresh token");
    }

    public void callAddNewOpponent(String className, GameMode gameMode, String opponentType, OpponentDifficulty opponentDifficulty) {
        OpponentDTO requestBody = new OpponentDTO();
        requestBody.setClassUT(className);
        requestBody.setGameMode(gameMode);
        requestBody.setType(opponentType);
        requestBody.setDifficulty(opponentDifficulty);

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) throw new RuntimeException("Auth token is missing from context");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "jwt=" + jwt);

        ResponseEntity<String> response = exchangeHelper.exchange(
                userServiceUrl + "/opponents",
                null, HttpMethod.POST, headers, requestBody, String.class);

        if (response.getStatusCode().isError())
            throw new RuntimeException("Error adding new opponent");
    }

    public void callDeleteAllClassUTOpponents(String className) {
        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) throw new RuntimeException("Auth token is missing from context");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "jwt=" + jwt);

        ResponseEntity<String> response = exchangeHelper.exchange(
                userServiceUrl + "/opponents/" + className,
                null, HttpMethod.DELETE, headers, null, String.class);

        if (response.getStatusCode().isError())
            throw new RuntimeException("Error deleting opponents");
    }

    public EvosuiteCoverageDTO callGenerateMissingEvoSuiteCoverage(String classUTName, String classUTPackage, File zip) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) throw new RuntimeException("Auth token is missing from context");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "jwt=" + jwt);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("request", new RequestEvosuiteCoverageDTO(classUTName, classUTPackage));
        builder.part("project", new FileSystemResource(zip));

        ResponseEntity<EvosuiteCoverageDTO> response = exchangeHelper.exchange(
                evosuiteCoverageServiceUrl + "/coverage/opponent",
                null, HttpMethod.POST, headers, builder.build(), EvosuiteCoverageDTO.class);

        if (response.getStatusCode().isError())
            throw new RuntimeException("Error generating evosuite coverage");

        return response.getBody();
    }

    public JacocoCoverageDTO callGenerateMissingJacocoCoverage(File zip) {

        String jwt = JwtRequestContext.getJwtToken();
        if (jwt == null) throw new RuntimeException("Auth token is missing from context");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "jwt=" + jwt);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> reqBody = new LinkedMultiValueMap<>();
        reqBody.add("project", new FileSystemResource(zip));

        ResponseEntity<JacocoCoverageDTO> response = exchangeHelper.exchange(
                jacocoCoverageServiceUrl + "/coverage/opponent",
                null, HttpMethod.POST, headers, reqBody, JacocoCoverageDTO.class);

        if (response.getStatusCode().isError())
            throw new RuntimeException("Error generating jacoco coverage");

        return response.getBody();
    }

    public HttpResponse callOttieniStudentiDettagli(List<String> studentiIds) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            JSONArray studentiArray = new JSONArray(studentiIds);
            StringEntity entity = new StringEntity(studentiArray.toString(), StandardCharsets.UTF_8);

            String jwt = JwtRequestContext.getJwtToken();
            HttpPost post = new HttpPost(userServiceUrl + "/student/studentsByIds");
            post.setHeader("Cookie", "jwt=" + jwt);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(entity);

            return httpClient.execute(post);
        }
    }

    public ResponseEntity<String> callSendNotification(MultiValueMap<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        RestTemplate rest = new RestTemplate();
        return rest.exchange(userServiceUrl + "/new_notification", HttpMethod.POST, entity, String.class);
    }
}

