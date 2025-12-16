package com.example.t9profileservice.llm;

import com.example.t9profileservice.dto.PlayerBioData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Primary
public class GroqLlmClient implements LlmClient {

    private static final Logger logger = LoggerFactory.getLogger(GroqLlmClient.class);

    @Value("${llm.api-url}")
    private String apiUrl;

    @Value("${llm.api-key}")
    private String apiKey;

    @Value("${llm.model}")
    private String model;

    @Autowired
    ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public String generateBio(PlayerBioData data) {
        try {
            String prompt = buildPrompt(data);

            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);

            Map<String, Object> body = new HashMap<>();
            body.put("model", model);
            body.put("messages", List.of(message));
            body.put("temperature", 0.7);

            String requestBody = objectMapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                JsonNode root = objectMapper.readTree(response.body());
                JsonNode choices = root.path("choices");
                if (choices.isArray() && choices.size() > 0) {
                    JsonNode contentNode = choices.get(0).path("message").path("content");
                    if (!contentNode.isMissingNode() && !contentNode.asText().isBlank()) {
                        String bio = contentNode.asText().trim();
                        if (bio.length() > 250) {
                            bio = bio.substring(0, 247) + "...";
                        }
                        return bio;
                    }
                }
                logger.warn("Groq response did not contain expected content field");
            } else {
                logger.warn("Groq API returned non-success status {}: {}", response.statusCode(), response.body());
            }

        } catch (Exception e) {
            logger.warn("Failed to generate bio from Groq: {}", e.getMessage());
        }

        return "Giocatore attivo su T4, amante delle sfide. Profilo in aggiornamento.";
    }

    private String buildPrompt(PlayerBioData data) {
        String name = data.name() != null ? data.name() : "";
        String surname = data.surname() != null ? data.surname() : "";
        String nickname = data.nickname() != null ? data.nickname() : "";
        Integer matchesPlayed = data.matchesPlayed() != null ? data.matchesPlayed() : 0;
        Integer matchesWon = data.matchesWon() != null ? data.matchesWon() : 0;
        String lastMatchAt = "";
        if (data.lastMatchAt() != null) {
            try {
                lastMatchAt = data.lastMatchAt().format(DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Exception ignored) {
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Genera una biografia breve (max 250 caratteri) in italiano, tono amichevole da gamer. ");
        sb.append("Usa le seguenti informazioni: ");
        if (!name.isBlank() || !surname.isBlank()) {
            sb.append("nome: ").append(name).append(" ").append(surname).append(". ");
        }
        if (!nickname.isBlank()) {
            sb.append("nickname: ").append(nickname).append(". ");
        }
        sb.append("Partite giocate: ").append(matchesPlayed).append(", partite vinte: ").append(matchesWon).append(". ");
        if (!lastMatchAt.isBlank()) {
            sb.append("Ultima partita: ").append(lastMatchAt).append(". ");
        }
        sb.append("Mantieni un tono amichevole e da gamer, breve e incisivo.");

        return sb.toString();
    }
}
