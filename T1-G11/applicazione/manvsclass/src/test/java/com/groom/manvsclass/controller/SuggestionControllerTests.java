package com.groom.manvsclass.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.manvsclass.api.ApiGatewayClient;
import com.groom.manvsclass.dto.SuggestionDTO;
import com.groom.manvsclass.model.SuggestionLevel;
import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.service.SecurityService;
import com.groom.manvsclass.service.SuggestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SuggestionController.class)
@AutoConfigureMockMvc(addFilters = false)
class SuggestionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SuggestionService suggestionService;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private ApiGatewayClient apiGatewayClient;

    // TEST UPLOAD (POST)

    @Test
    void uploadSuggestions_Success() throws Exception {

        when(securityService.getJwtToken()).thenReturn("valid_jwt_token");

        SuggestionDTO testDTO = new SuggestionDTO();
        testDTO.setTitle("Suggerimento");
        testDTO.setHint("Testo_Suggerimento");
        testDTO.setImage(null);
        testDTO.setLevel(SuggestionLevel.LOW);

        mockMvc.perform(post("/opponents/suggestions/upload/Calcolatrice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonList(testDTO))))
                .andExpect(status().isOk())
                .andExpect(content().string("Suggerimenti caricati con successo."));

        verify(suggestionService).uploadSuggestions(eq("Calcolatrice"), any());
    }

    @Test
    void uploadSuggestions_Unauthorized() throws Exception {

        when(securityService.getJwtToken()).thenReturn(null);

        mockMvc.perform(post("/opponents/suggestions/upload/Calcolatrice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token JWT non valido o mancante."));

        verifyNoInteractions(suggestionService);
    }

    @Test
    void uploadSuggestions_ClassNotFound() throws Exception {

        when(securityService.getJwtToken()).thenReturn("valid_token");
        String errorMsg = "Classe non trovata";

        doThrow(new NotFoundException(errorMsg))
                .when(suggestionService).uploadSuggestions(eq("ClasseInesistente"), any());

        mockMvc.perform(post("/opponents/suggestions/upload/ClasseInesistente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMsg));
    }

    // TEST VIEW (GET)

    @Test
    void viewSuggestions_Success() throws Exception {
        when(securityService.getJwtToken()).thenReturn("valid_token");

        SuggestionDTO testDTO = new SuggestionDTO();
        testDTO.setTitle("Suggerimento");
        testDTO.setHint("Testo_Suggerimento");
        testDTO.setImage(null);
        testDTO.setLevel(SuggestionLevel.LOW);

        when(suggestionService.findSuggestions("Calcolatrice"))
                .thenReturn(Collections.singletonList(testDTO));

        mockMvc.perform(get("/opponents/suggestions/Calcolatrice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Suggerimento"));
    }

    @Test
    void viewSuggestions_Unauthorized() throws Exception {
        when(securityService.getJwtToken()).thenReturn(null);

        mockMvc.perform(get("/opponents/suggestions/Calcolatrice"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token JWT non valido o mancante."));
    }

    // TEST DELETE (DELETE)

    @Test
    void deleteSuggestion_Success() throws Exception {
        when(securityService.getJwtToken()).thenReturn("valid_token");

        mockMvc.perform(delete("/opponents/suggestions/Calcolatrice/suggestion")
                        .param("suggestionTitle", "MioSuggerimento"))
                .andExpect(status().isOk())
                .andExpect(content().string("Suggerimento eliminato con successo."));

        verify(suggestionService).deleteSuggestion("Calcolatrice", "MioSuggerimento");
    }

    @Test
    void deleteSuggestion_Unauthorized() throws Exception {
        when(securityService.getJwtToken()).thenReturn(null);

        mockMvc.perform(delete("/opponents/suggestions/Calcolatrice/suggestion")
                        .param("suggestionTitle", "Any"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token JWT non valido o mancante."));

        verifyNoInteractions(suggestionService);
    }

    @Test
    void deleteSuggestion_NotFound() throws Exception {
        when(securityService.getJwtToken()).thenReturn("valid_token");
        String errorMsg = "Suggerimento non trovato";

        doThrow(new NotFoundException(errorMsg))
                .when(suggestionService).deleteSuggestion(anyString(), anyString());

        mockMvc.perform(delete("/opponents/suggestions/Calcolatrice/suggestion")
                        .param("suggestionTitle", "NonEsiste"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMsg));
    }
}