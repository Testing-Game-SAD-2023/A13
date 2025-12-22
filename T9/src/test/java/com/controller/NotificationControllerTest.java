package com.controller;

import com.model.dto.NotificationRestDTO;
import com.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    void testGetUserNotifications_ShouldReturnList() throws Exception {
        // 1. ARRANGE
        Long userId = 1L;
        NotificationRestDTO dto1 = new NotificationRestDTO(10L, userId, "INFO", "Title1", "Body1", false, Instant.now());
        NotificationRestDTO dto2 = new NotificationRestDTO(11L, userId, "WARN", "Title2", "Body2", true, Instant.now());

        List<NotificationRestDTO> mockList = Arrays.asList(dto1, dto2);

        // Simuliamo la risposta del service
        given(notificationService.getUserNotifications(userId, null, null))
                .willReturn(mockList);

        // 2. ACT & ASSERT
        // URL Corretto: /notifications/{userId}
        mockMvc.perform(get("/notifications/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Title1"))
                .andExpect(jsonPath("$[1].type").value("WARN"));
    }

    @Test
    void testMarkAsRead_ShouldReturnNoContent() throws Exception {
        // 1. ARRANGE
        Long userId = 1L;
        Long notificationId = 10L;

        // 2. ACT & ASSERT
        // URL Corretto: /notifications/{userId}/{notificationId}/read
        // NOTA: Il tuo controller restituisce 204 (No Content), non 200 (OK)
        mockMvc.perform(patch("/notifications/{userId}/{notificationId}/read", userId, notificationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Si aspetta 204

        // 3. VERIFY
        // Verifichiamo che il service sia stato chiamato con i parametri giusti
        verify(notificationService).markNotificationAsRead(userId, notificationId);
    }
}
