package com.groom.manvsclass.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.manvsclass.api.ApiGatewayClient;
import com.groom.manvsclass.dto.GuidelineDTO;
import com.groom.manvsclass.repository.AdminRepository;
import com.groom.manvsclass.service.GuidelineService;
import com.groom.manvsclass.service.JwtService;
import com.groom.manvsclass.util.TestUtils;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    // MOCK NECESSARI A CAUSA DI AUTH_TOKEN_FILTER
    @MockBean
    private ApiGatewayClient apiGatewayClient;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private AdminRepository adminRepository;

    @Captor
    private ArgumentCaptor<List<GuidelineDTO>> guidelineListCaptor;

    @Captor
    private ArgumentCaptor<MultipartFile> fileCaptor;

    // configurazione di comparison per GuidelineDTO -> configurazione di default: compara tutti i campi
    public static final RecursiveComparisonConfiguration GUIDELINE_DTO_COMPARISON_CONFIG = new RecursiveComparisonConfiguration();

    private final TestUtils<GuidelineDTO> guidelineDTOTestUtils = new TestUtils<>(GUIDELINE_DTO_COMPARISON_CONFIG);

    public static GuidelineDTO createBaseGuidelineDTO() {

        GuidelineDTO guidelineDTO = new GuidelineDTO();
        guidelineDTO.setOrder(1);
        guidelineDTO.setHint("Testo_Linea Guida");
        guidelineDTO.setImage(null);

        return guidelineDTO;
    }

    // TEST UPLOAD (POST)

    @Test
    void uploadGuidelines_Success() throws Exception {

        GuidelineDTO test1DTO = createBaseGuidelineDTO();
        test1DTO.setOrder(1);
        test1DTO.setHint("Testo_Linea_Guida_1");

        GuidelineDTO test2DTO = createBaseGuidelineDTO();
        test2DTO.setOrder(2);
        test2DTO.setHint("Testo_Linea_Guida_2");

        List<GuidelineDTO> guidelineDTOs = Arrays.asList(test1DTO, test2DTO);

        mockMvc.perform(post("/opponents/guidelines/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guidelineDTOs)))
                .andExpect(status().isOk())
                .andExpect(content().string("Linee guida caricate con successo."));

        verify(guidelineService).uploadGuidelines(guidelineListCaptor.capture());

        // verifica che sia stato chiamato guidelineService.uploadGuidelines proprio con gli argomenti attesi
        List<GuidelineDTO> uploadedGuidelines = guidelineListCaptor.getValue();
        guidelineDTOTestUtils.assertListEquals(guidelineDTOs, uploadedGuidelines);
    }

    @Test
    void uploadGuidelines_ValidationError_InvalidProgression() throws Exception {

        GuidelineDTO test1DTO = createBaseGuidelineDTO();
        test1DTO.setOrder(1);
        test1DTO.setHint("Testo_Linea_Guida_1");

        GuidelineDTO test2DTO = createBaseGuidelineDTO();
        test2DTO.setOrder(2);
        test2DTO.setHint("Testo_Linea_Guida_2");

        List<GuidelineDTO> guidelineDTOs = Arrays.asList(test2DTO, test1DTO);

        mockMvc.perform(post("/opponents/guidelines/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guidelineDTOs)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore di validazione")));

        verifyNoInteractions(guidelineService);
    }

    @Test
    void uploadGuidelines_InvalidJSON_GuidelineListMissing() throws Exception {

        mockMvc.perform(post("/opponents/guidelines/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore nel formato JSON")));

        verifyNoInteractions(guidelineService);
    }

    @Test
    void uploadGuidelines_InvalidJSON_InvalidFormat() throws Exception {

        String invalidBody = "\"order\" = \"1\"";
        mockMvc.perform(post("/opponents/guidelines/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore nel formato JSON")));

        verifyNoInteractions(guidelineService);
    }

    @Test
    void uploadGuidelines_JSONMissing() throws Exception {

        mockMvc.perform(post("/opponents/guidelines/upload"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore nel formato JSON")));

        verifyNoInteractions(guidelineService);
    }

    // TEST VIEW (GET)

    @Test
    void viewGuidelines_Success() throws Exception {

        GuidelineDTO test1DTO = createBaseGuidelineDTO();
        test1DTO.setOrder(1);
        test1DTO.setHint("Testo_Linea_Guida_1");

        GuidelineDTO test2DTO = createBaseGuidelineDTO();
        test1DTO.setOrder(2);
        test1DTO.setHint("Testo_Linea_Guida_2");

        List<GuidelineDTO> foundGuidelines = Arrays.asList(test1DTO, test2DTO);

        when(guidelineService.findGuidelines())
                .thenReturn(foundGuidelines);

        mockMvc.perform(get("/opponents/guidelines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].order").value(test1DTO.getOrder()))
                .andExpect(jsonPath("$.[0].hint").value(test1DTO.getHint()))
                .andExpect(jsonPath("$.[0].image").value(test1DTO.getImage()))
                .andExpect(jsonPath("$.[1].order").value(test2DTO.getOrder()))
                .andExpect(jsonPath("$.[1].hint").value(test2DTO.getHint()))
                .andExpect(jsonPath("$.[1].image").value(test2DTO.getImage()));

        verify(guidelineService, times(1)).findGuidelines();
    }

    // TEST DELETE (DELETE)

    @Test
    void deleteGuideline_Success() throws Exception {

        mockMvc.perform(delete("/opponents/guidelines/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Linea guida eliminata con successo."));

        verify(guidelineService).deleteGuideline(1);
    }

    @Test
    void deleteGuideline_ValidationError_InvalidOrder() throws Exception {

        mockMvc.perform(delete("/opponents/guidelines/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore di validazione")));

        verifyNoInteractions(guidelineService);
    }

    // TEST UPLOAD IMAGE (POST)
    @Test
    void updateGuidelineImage_Success() throws Exception {

        String imageName = "image.png";
        byte[] content = "contenuto_immagine".getBytes();

        // crea un file di immagine mockato
        MockMultipartFile image = new MockMultipartFile(
                "image",
                imageName,
                "image/png",
                content
        );

        // upload dell'immagine al linea guida con order 1
        mockMvc.perform(multipart("/opponents/guidelines/upload/1")
                        .file(image))
                .andExpect(status().isOk())
                .andExpect(content().string("Immagine caricata con successo."));

        // verifica che la chiamata a guidelineService sia con gli argomenti corretti (order 1 e immagine corretta)
        verify(guidelineService).uploadGuidelineImage(eq(1), fileCaptor.capture());

        // verifica che l'immagine data al service coincida con quella inviata nella richiesta HTTP
        MultipartFile savedImage = fileCaptor.getValue();
        assertEquals(imageName, savedImage.getOriginalFilename());
        assertArrayEquals(content, savedImage.getBytes());
    }

    @Test
    void updateGuidelineImage_ValidationError_InvalidOrder() throws Exception {

        String imageName = "image.png";
        byte[] content = "contenuto_immagine".getBytes();

        // crea un file di immagine mockato
        MockMultipartFile image = new MockMultipartFile(
                "image",
                imageName,
                "image/png",
                content
        );

        // upload dell'immagine la linea guida con order -1
        mockMvc.perform(multipart("/opponents/guidelines/upload/-1")
                        .file(image))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore di validazione")));

        verifyNoInteractions(guidelineService);
    }

    @Test
    void uploadGuidelineImage_HTTPError_NoImage() throws Exception {
        mockMvc.perform(multipart("/opponents/guidelines/upload/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Richiesta non valida")));

        verifyNoInteractions(guidelineService);
    }

    // TEST DELETE IMAGE (DELETE)

    @Test
    void deleteGuidelineImage_Success() throws Exception {
        mockMvc.perform(delete("/opponents/guidelines/image/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Immagine eliminata con successo."));

        verify(guidelineService).deleteGuidelineImage(1);
    }

    @Test
    void deleteGuidelineImage_ValidationError_InvalidOrder() throws Exception {

        mockMvc.perform(delete("/opponents/guidelines/image/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Errore di validazione")));

        verifyNoInteractions(guidelineService);
    }
}