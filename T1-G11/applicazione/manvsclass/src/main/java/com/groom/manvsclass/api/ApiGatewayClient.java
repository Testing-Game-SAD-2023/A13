package com.groom.manvsclass.api;

import com.groom.manvsclass.model.dto.OpponentDTO;
import com.groom.manvsclass.model.dto.RequestEvosuiteCoverageDTO;
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

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * ApiGatewayClient contiene tutte le chiamate API di T1 verso gli altri microservizi del sistema.
 */
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

    /**
     * Valida il token JWT ricevuto nell'header della richiesta utente contattando T23
     */
    public JwtValidationResponseDTO callValidateJwtToken(String jwtToken) {
        ResponseEntity<JwtValidationResponseDTO> response = exchangeHelper.exchange(userServiceUrl + "/auth/validateToken?jwt=" + jwtToken,
                null, HttpMethod.POST, null, null, JwtValidationResponseDTO.class);
        if (response == null || response.getBody() == null)
            return null;

        return response.getBody();
    }

    /**
     * Richiede un nuovo jwt token usando il refresh token presente nell'header della richiesta utente contattando T23.
     */
    public String callRefreshJwtToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "jwt-refresh=" + refreshToken);

        ResponseEntity<String> response = exchangeHelper.exchange(userServiceUrl + "/auth/refreshToken",
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

    public void callAddNewOpponent(String classUT, GameMode gameMode, String type, OpponentDifficulty difficulty) {
        OpponentDTO requestBody = new OpponentDTO();
        requestBody.setClassUT(classUT);
        requestBody.setGameMode(gameMode);
        requestBody.setType(type);
        requestBody.setDifficulty(difficulty);

        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<String> response = exchangeHelper.exchange(userServiceUrl + "/opponents",
                null, HttpMethod.POST, headers, requestBody, String.class);

        if (response.getStatusCode().isError())
            throw new RuntimeException("Error adding new opponent");

    }

    public void callDeleteAllClassUTOpponents(String classUT) {
        ResponseEntity<String> response = exchangeHelper.exchange(userServiceUrl + "/opponents/" + classUT,
                null, HttpMethod.DELETE, null, null, String.class);

        if (response.getStatusCode().isError())
            throw new RuntimeException("Error deleting opponents");

    }

    public EvosuiteCoverageDTO callGenerateMissingEvoSuiteCoverage(String classUTName, String classUTPackageName, File zip) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("request", new RequestEvosuiteCoverageDTO(classUTName, classUTPackageName));
        builder.part("project", new FileSystemResource(zip));

        MultiValueMap<String, HttpEntity<?>> requestBody = builder.build();

        ResponseEntity<EvosuiteCoverageDTO> response = exchangeHelper.exchange(evosuiteCoverageServiceUrl + "/coverage/opponent",
                null, HttpMethod.POST, null, requestBody, EvosuiteCoverageDTO.class);

        if (response.getStatusCode().isError())
            throw new RuntimeException("Error generating evosuite coverage");

        EvosuiteCoverageDTO responseBody = response.getBody();
        logger.info("responseBody: {}", responseBody);

        return responseBody;
    }

    public JacocoCoverageDTO callGenerateMissingJacocoCoverage(File zip) {
        FileSystemResource fileResource = new FileSystemResource(zip);

        MultiValueMap<String, Object> reqBody = new LinkedMultiValueMap<>();
        reqBody.add("project", fileResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ResponseEntity<JacocoCoverageDTO> response = exchangeHelper.exchange(jacocoCoverageServiceUrl + "/coverage/opponent",
                null, HttpMethod.POST, headers, reqBody, JacocoCoverageDTO.class);

        if (response.getStatusCode().isError())
            throw new RuntimeException("Error generating jacoco coverage");

        JacocoCoverageDTO responseBody = response.getBody();
        logger.info("responseBody: {}", responseBody);
        return responseBody;
    }

    public HttpResponse callOttieniStudentiDettagli(List<String> studentiIds, String jwt) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            // 2. Prepara il corpo JSON
            logger.info("Preparazione del corpo JSON...");
            JSONArray studentiArray = new JSONArray(studentiIds); // Crea un array JSON direttamente
            StringEntity entity = new StringEntity(studentiArray.toString(), StandardCharsets.UTF_8); // Corpo JSON come array
            logger.info("Corpo JSON preparato: {}", studentiArray);

            // 3. Configura la richiesta HTTP POST
            logger.info("Configurazione della richiesta HTTP POST...");

            HttpPost httpPost = new HttpPost(userServiceUrl + "/student/studentsByIds");

            httpPost.setHeader("Authorization", "Bearer " + jwt);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(entity);

            // 4. Esegui la richiesta
            logger.info("Esecuzione della richiesta...");

            return httpClient.execute(httpPost);
        }
    }

    public ResponseEntity<String> callSendNotification(MultiValueMap<String, String> params) {
        // Headers della richiesta
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Crea la richiesta HTTP
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(userServiceUrl + "/new_notification", HttpMethod.POST, entity, String.class);
    }

}
