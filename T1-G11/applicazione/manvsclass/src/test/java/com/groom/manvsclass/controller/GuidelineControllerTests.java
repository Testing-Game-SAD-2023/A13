package com.groom.manvsclass.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.manvsclass.api.ApiGatewayClient;
import com.groom.manvsclass.dto.GuidelineDTO;
import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.service.SecurityService;
import com.groom.manvsclass.service.GuidelineService;
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

@WebMvcTest(GuidelineController.class)
@AutoConfigureMockMvc(addFilters = false)
class GuidelineControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GuidelineService guidelineService;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private ApiGatewayClient apiGatewayClient;

    // TEST UPLOAD (POST)

    @Test
    void uploadGuidelines_Success() throws Exception {

        when(securityService.getJwtToken()).thenReturn("valid_jwt_token");

        GuidelineDTO testDTO = new GuidelineDTO();
        testDTO.setTitle("Linea_Guida");
        testDTO.setHint("Testo_Linea_Guida");
        testDTO.setImage(null);

        mockMvc.perform(post("/opponents/guidelines/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonList(testDTO))))
                .andExpect(status().isOk())
                .andExpect(content().string("Linee guida caricate con successo."));

        verify(guidelineService).uploadGuidelines(any());
    }

    @Test
    void uploadGuidelines_Unauthorized() throws Exception {

        when(securityService.getJwtToken()).thenReturn(null);

        mockMvc.perform(post("/opponents/guidelines/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token JWT non valido o mancante."));

        verifyNoInteractions(guidelineService);
    }

    @Test
    void uploadGuidelines_NoTitle() throws Exception {

        when(securityService.getJwtToken()).thenReturn("valid_jwt_token");

        GuidelineDTO testDTO = new GuidelineDTO();
        testDTO.setTitle(null);
        testDTO.setHint("Testo_Linea_Guida");
        testDTO.setImage(null);

        mockMvc.perform(post("/opponents/guidelines/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonList(testDTO))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(guidelineService);
    }

    @Test
    void uploadGuidelines_NoHint() throws Exception {

        when(securityService.getJwtToken()).thenReturn("valid_jwt_token");

        GuidelineDTO testDTO = new GuidelineDTO();
        testDTO.setTitle("Linea_Guida");
        testDTO.setHint(null);
        testDTO.setImage(null);

        mockMvc.perform(post("/opponents/guidelines/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonList(testDTO))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(guidelineService);
    }

    // TEST VIEW (GET)

    @Test
    void viewGuidelines_Success() throws Exception {
        when(securityService.getJwtToken()).thenReturn("valid_token");

        GuidelineDTO testDTO = new GuidelineDTO();
        testDTO.setTitle("Linea_Guida");
        testDTO.setHint("Testo_Linea_Guida");
        testDTO.setImage(null);

        when(guidelineService.findGuidelines())
                .thenReturn(Collections.singletonList(testDTO));

        mockMvc.perform(get("/opponents/guidelines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Linea_Guida"));
    }

    @Test
    void viewGuidelines_Unauthorized() throws Exception {
        when(securityService.getJwtToken()).thenReturn(null);

        mockMvc.perform(get("/opponents/guidelines"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token JWT non valido o mancante."));
    }

    // TEST DELETE (HTTP DELETE)

    @Test
    void deleteGuideline_Success() throws Exception {

        when(securityService.getJwtToken()).thenReturn("valid_token");

        mockMvc.perform(delete("/opponents/guidelines/Linea_Guida"))
                .andExpect(status().isOk())
                .andExpect(content().string("Linea guida eliminata con successo."));

        verify(guidelineService).deleteGuideline("Linea_Guida");
    }

    @Test
    void deleteGuideline_Unauthorized() throws Exception {
        when(securityService.getJwtToken()).thenReturn(null);

        mockMvc.perform(delete("/opponents/guidelines/AnyTitle"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token JWT non valido o mancante."));

        verifyNoInteractions(guidelineService);
    }

    @Test
    void deleteGuideline_NotFound() throws Exception {

        when(securityService.getJwtToken()).thenReturn("valid_token");
        String errorMsg = "Linea_Guida non trovato";

        doThrow(new NotFoundException(errorMsg))
                .when(guidelineService).deleteGuideline(anyString());

        mockMvc.perform(delete("/opponents/guidelines/Linea_Guida"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMsg));

        verify(guidelineService).deleteGuideline("Linea_Guida");
    }
}