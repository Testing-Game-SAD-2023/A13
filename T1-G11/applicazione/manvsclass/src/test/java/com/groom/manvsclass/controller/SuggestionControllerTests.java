package com.groom.manvsclass.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.manvsclass.api.ApiGatewayClient;
import com.groom.manvsclass.dto.ClassUTSuggestionDTO;
import com.groom.manvsclass.dto.SuggestionDTO;
import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.model.SuggestionLevel;
import com.groom.manvsclass.repository.AdminRepository;
import com.groom.manvsclass.service.JwtService;
import com.groom.manvsclass.service.SuggestionService;
import com.groom.manvsclass.util.TestUtils;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    // MOCK NECESSARI A CAUSA DI AUTH_TOKEN_FILTER
    @MockBean
    private ApiGatewayClient apiGatewayClient;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private AdminRepository adminRepository;

    @Captor
    private ArgumentCaptor<List<SuggestionDTO>> suggestionListCaptor;

    @Captor
    private ArgumentCaptor<MultipartFile> fileCaptor;

    // configurazione di comparison per SuggestionDTO -> configurazione di default: compara tutti i campi
    public static final RecursiveComparisonConfiguration SUGGESTION_DTO_COMPARISON_CONFIG = new RecursiveComparisonConfiguration();

    private final TestUtils<SuggestionDTO> suggestionDTOTestUtils = new TestUtils<>(SUGGESTION_DTO_COMPARISON_CONFIG);

    public static SuggestionDTO createBaseSuggestionDTO() {

        SuggestionDTO suggestionDTO = new SuggestionDTO();
        suggestionDTO.setOrder(1);
        suggestionDTO.setHint("Testo_Suggerimento");
        suggestionDTO.setLevel(SuggestionLevel.LOW);
        suggestionDTO.setImage(null);

        return suggestionDTO;
    }

    // TEST UPLOAD (POST)

    @Test
    void uploadSuggestions_Success() throws Exception {

        SuggestionDTO test1DTO = createBaseSuggestionDTO();
        test1DTO.setOrder(1);
        test1DTO.setHint("Testo_Suggerimento_1");

        SuggestionDTO test2DTO = createBaseSuggestionDTO();
        test2DTO.setOrder(2);
        test2DTO.setHint("Testo_Suggerimento_2");

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(test1DTO, test2DTO);

        ClassUTSuggestionDTO ClassUTSuggestionDTO = new ClassUTSuggestionDTO();
        ClassUTSuggestionDTO.setClassName("Classe");
        ClassUTSuggestionDTO.setSuggestions(suggestionDTOs);

        mockMvc.perform(post("/opponents/suggestions/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ClassUTSuggestionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Suggerimenti caricati con successo."));

        verify(suggestionService).uploadSuggestions(eq("Classe"), suggestionListCaptor.capture());

        // verifica che sia stato chiamato suggestionService.uploadSuggestions proprio con gli argomenti attesi
        List<SuggestionDTO> uploadedSuggestions = suggestionListCaptor.getValue();
        suggestionDTOTestUtils.assertListEquals(suggestionDTOs, uploadedSuggestions);
    }

    @Test
    void uploadSuggestions_ServiceError_ClassNotFound() throws Exception {

        SuggestionDTO test1DTO = createBaseSuggestionDTO();
        test1DTO.setOrder(1);
        test1DTO.setHint("Testo_Suggerimento_1");

        SuggestionDTO test2DTO = createBaseSuggestionDTO();
        test2DTO.setOrder(2);
        test2DTO.setHint("Testo_Suggerimento_2");

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(test1DTO, test2DTO);

        ClassUTSuggestionDTO ClassUTSuggestionDTO = new ClassUTSuggestionDTO();
        ClassUTSuggestionDTO.setClassName("ClasseInesistente");
        ClassUTSuggestionDTO.setSuggestions(suggestionDTOs);

        doThrow(NotFoundException.class)
                .when(suggestionService).uploadSuggestions(eq("ClasseInesistente"), any());

        mockMvc.perform(post("/opponents/suggestions/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ClassUTSuggestionDTO)))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void uploadSuggestions_ValidationError_InvalidClassName(String invalidClassName) throws Exception {

        SuggestionDTO test1DTO = createBaseSuggestionDTO();
        test1DTO.setOrder(1);
        test1DTO.setHint("Testo_Suggerimento_1");

        SuggestionDTO test2DTO = createBaseSuggestionDTO();
        test2DTO.setOrder(2);
        test2DTO.setHint("Testo_Suggerimento_2");

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(test2DTO, test1DTO);

        ClassUTSuggestionDTO ClassUTSuggestionDTO = new ClassUTSuggestionDTO();
        ClassUTSuggestionDTO.setClassName(invalidClassName);
        ClassUTSuggestionDTO.setSuggestions(suggestionDTOs);

        mockMvc.perform(post("/opponents/suggestions/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ClassUTSuggestionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore di validazione")));

        verifyNoInteractions(suggestionService);
    }

    @Test
    void uploadSuggestions_ValidationError_InvalidProgression() throws Exception {

        SuggestionDTO test1DTO = createBaseSuggestionDTO();
        test1DTO.setOrder(1);
        test1DTO.setHint("Testo_Suggerimento_1");

        SuggestionDTO test2DTO = createBaseSuggestionDTO();
        test2DTO.setOrder(2);
        test2DTO.setHint("Testo_Suggerimento_2");

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(test2DTO, test1DTO);

        ClassUTSuggestionDTO ClassUTSuggestionDTO = new ClassUTSuggestionDTO();
        ClassUTSuggestionDTO.setClassName("Classe");
        ClassUTSuggestionDTO.setSuggestions(suggestionDTOs);

        mockMvc.perform(post("/opponents/suggestions/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ClassUTSuggestionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore di validazione")));

        verifyNoInteractions(suggestionService);
    }

    @Test
    void uploadSuggestions_InvalidJSON_ClassNameMissing() throws Exception {

        SuggestionDTO test1DTO = createBaseSuggestionDTO();
        test1DTO.setOrder(1);
        test1DTO.setHint("Testo_Suggerimento_1");

        SuggestionDTO test2DTO = createBaseSuggestionDTO();
        test2DTO.setOrder(2);
        test2DTO.setHint("Testo_Suggerimento_2");

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(test1DTO, test2DTO);

        Map<String, Object> invalidJson = new HashMap<>();
        invalidJson.put("suggestions", suggestionDTOs);

        mockMvc.perform(post("/opponents/suggestions/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidJson)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore di validazione")));

        verifyNoInteractions(suggestionService);
    }

    @Test
    void uploadSuggestions_InvalidJSON_InvalidFormat() throws Exception {

        String invalidBody = "\"className\" = \"Calcolatrice\"";
        mockMvc.perform(post("/opponents/suggestions/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore nel formato JSON")));

        verifyNoInteractions(suggestionService);
    }

    @Test
    void uploadSuggestions_JSONMissing() throws Exception {

        mockMvc.perform(post("/opponents/suggestions/upload"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore nel formato JSON")));

        verifyNoInteractions(suggestionService);
    }

    // TEST VIEW (GET)

    @Test
    void viewSuggestions_Success() throws Exception {

        SuggestionDTO test1DTO = createBaseSuggestionDTO();
        test1DTO.setOrder(1);
        test1DTO.setHint("Testo_Suggerimento_1");

        SuggestionDTO test2DTO = createBaseSuggestionDTO();
        test1DTO.setOrder(2);
        test1DTO.setHint("Testo_Suggerimento_2");

        List<SuggestionDTO> foundSuggestions = Arrays.asList(test1DTO, test2DTO);

        when(suggestionService.findSuggestions("Calcolatrice"))
                .thenReturn(foundSuggestions);

        mockMvc.perform(get("/opponents/suggestions/Calcolatrice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.className").value("Calcolatrice"))
                .andExpect(jsonPath("$.suggestions[0].order").value(test1DTO.getOrder()))
                .andExpect(jsonPath("$.suggestions[0].hint").value(test1DTO.getHint()))
                .andExpect(jsonPath("$.suggestions[0].level").value(test1DTO.getLevel().toString()))
                .andExpect(jsonPath("$.suggestions[0].image").value(test1DTO.getImage()))
                .andExpect(jsonPath("$.suggestions[1].order").value(test2DTO.getOrder()))
                .andExpect(jsonPath("$.suggestions[1].hint").value(test2DTO.getHint()))
                .andExpect(jsonPath("$.suggestions[1].level").value(test2DTO.getLevel().toString()))
                .andExpect(jsonPath("$.suggestions[1].image").value(test2DTO.getImage()));

        verify(suggestionService, times(1)).findSuggestions("Calcolatrice");
    }

    @Test
    void viewSuggestions_ServiceError_ClassNotFound() throws Exception {
        when(suggestionService.findSuggestions("ClasseInesistente"))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/opponents/suggestions/ClasseInesistente"))
                .andExpect(status().isNotFound());

        verify(suggestionService, times(1)).findSuggestions("ClasseInesistente");
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"   "})
    void viewSuggestions_ValidationError_InvalidClassName(String invalidClassName) throws Exception {
        when(suggestionService.findSuggestions(invalidClassName))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/opponents/suggestions/" + invalidClassName))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(suggestionService);
    }

    // TEST DELETE (DELETE)

    @Test
    void deleteSuggestion_Success() throws Exception {

        mockMvc.perform(delete("/opponents/suggestions/Calcolatrice/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Suggerimento eliminato con successo."));

        verify(suggestionService).deleteSuggestion("Calcolatrice", 1);
    }

    @Test
    void deleteSuggestion_ServiceError_ClassNotFound() throws Exception {

        doThrow(NotFoundException.class)
                .when(suggestionService).deleteSuggestion(anyString(), anyInt());

        mockMvc.perform(delete("/opponents/suggestions/ClasseInesistente/1"))
                .andExpect(status().isNotFound());

        verify(suggestionService).deleteSuggestion("ClasseInesistente", 1);
    }

    @Test
    void deleteSuggestion_ValidationError_InvalidClassName() throws Exception {
        String invalidClassName = "   ";

        mockMvc.perform(delete("/opponents/suggestions/" + invalidClassName + "/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore di validazione")));

        verifyNoInteractions(suggestionService);
    }

    @Test
    void deleteSuggestion_ValidationError_InvalidOrder() throws Exception {

        mockMvc.perform(delete("/opponents/suggestions/Calcolatrice/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore di validazione")));

        verifyNoInteractions(suggestionService);
    }

    // TEST UPLOAD IMAGE (POST)
    @Test
    void updateSuggestionImage_Success() throws Exception {

        String imageName = "image.png";
        byte[] content = "contenuto_immagine".getBytes();

        // crea un file di immagine mockato
        MockMultipartFile image = new MockMultipartFile(
                "image",
                imageName,
                "image/png",
                content
        );

        // upload dell'immagine al suggerimento con order 1 della classe Calcolatrice
        mockMvc.perform(multipart("/opponents/suggestions/upload/Calcolatrice/1")
                        .file(image))
                .andExpect(status().isOk())
                .andExpect(content().string("Immagine caricata con successo."));

        // verifica che la chiamata a suggestionService sia con gli argomenti corretti (classe Calcolatrice, order 1 e immagine corretta)
        verify(suggestionService).uploadSuggestionImage(eq("Calcolatrice"), eq(1), fileCaptor.capture());

        // verifica che l'immagine data al service coincida con quella inviata nella richiesta HTTP
        MultipartFile savedImage = fileCaptor.getValue();
        assertEquals(imageName, savedImage.getOriginalFilename());
        assertArrayEquals(content, savedImage.getBytes());
    }

    @Test
    void updateSuggestionImage_ServiceError_ClassNotFound() throws Exception {

        String imageName = "image.png";
        byte[] content = "contenuto_immagine".getBytes();

        // crea un file di immagine mockato
        MockMultipartFile image = new MockMultipartFile(
                "image",
                imageName,
                "image/png",
                content
        );

        doThrow(NotFoundException.class)
                .when(suggestionService).uploadSuggestionImage(eq("ClasseInesistente"), anyInt(), any(MultipartFile.class));

        // upload dell'immagine al suggerimento con order 1 della classe ClasseInesistente
        mockMvc.perform(multipart("/opponents/suggestions/upload/ClasseInesistente/1")
                        .file(image))
                .andExpect(status().isNotFound());

        // verifica che la chiamata a suggestionService sia con gli argomenti corretti (classe ClasseInesistente, order 1 e immagine corretta)
        verify(suggestionService).uploadSuggestionImage(eq("ClasseInesistente"), eq(1), fileCaptor.capture());

        // verifica che l'immagine data al service coincida con quella inviata nella richiesta HTTP
        MultipartFile savedImage = fileCaptor.getValue();
        assertEquals(imageName, savedImage.getOriginalFilename());
        assertArrayEquals(content, savedImage.getBytes());
    }

    @Test
    void updateSuggestionImage_ValidationError_InvalidClassName() throws Exception {

        String invalidClassName = "   ";

        String imageName = "image.png";
        byte[] content = "contenuto_immagine".getBytes();

        // crea un file di immagine mockato
        MockMultipartFile image = new MockMultipartFile(
                "image",
                imageName,
                "image/png",
                content
        );

        // upload dell'immagine al suggerimento con order 1 della classe invalidClassName
        mockMvc.perform(multipart("/opponents/suggestions/upload/" + invalidClassName + "/1")
                        .file(image))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore di validazione")));

        verifyNoInteractions(suggestionService);
    }

    @Test
    void updateSuggestionImage_ValidationError_InvalidOrder() throws Exception {

        String imageName = "image.png";
        byte[] content = "contenuto_immagine".getBytes();

        // crea un file di immagine mockato
        MockMultipartFile image = new MockMultipartFile(
                "image",
                imageName,
                "image/png",
                content
        );

        // upload dell'immagine al suggerimento con order -1 della classe Calcolatrice
        mockMvc.perform(multipart("/opponents/suggestions/upload/Calcolatrice/-1")
                        .file(image))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore di validazione")));

        verifyNoInteractions(suggestionService);
    }

    @Test
    void uploadSuggestionImage_HTTPError_NoImage() throws Exception {
        mockMvc.perform(multipart("/opponents/suggestions/upload/Calcolatrice/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Richiesta non valida")));

        verifyNoInteractions(suggestionService);
    }

    // TEST DELETE IMAGE (DELETE)

    @Test
    void deleteSuggestionImage_Success() throws Exception {
        mockMvc.perform(delete("/opponents/suggestions/image/Calcolatrice/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Immagine eliminata con successo."));

        verify(suggestionService).deleteSuggestionImage("Calcolatrice", 1);
    }

    @Test
    void deleteSuggestionImage_ServiceError_ClassNotFound() throws Exception {

        doThrow(NotFoundException.class)
                .when(suggestionService).deleteSuggestionImage(anyString(), anyInt());

        mockMvc.perform(delete("/opponents/suggestions/image/ClasseInesistente/1"))
                .andExpect(status().isNotFound());

        verify(suggestionService).deleteSuggestionImage("ClasseInesistente", 1);
    }

    @Test
    void deleteSuggestionImage_ValidationError_InvalidClassName() throws Exception {
        String invalidClassName = "   ";

        mockMvc.perform(delete("/opponents/suggestions/image/" + invalidClassName + "/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore di validazione")));

        verifyNoInteractions(suggestionService);
    }

    @Test
    void deleteSuggestionImage_ValidationError_InvalidOrder() throws Exception {

        mockMvc.perform(delete("/opponents/suggestions/image/Calcolatrice/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore di validazione")));

        verifyNoInteractions(suggestionService);
    }
}