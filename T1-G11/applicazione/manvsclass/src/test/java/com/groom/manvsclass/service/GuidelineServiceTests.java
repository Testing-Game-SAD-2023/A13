package com.groom.manvsclass.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.groom.manvsclass.dto.GuidelineDTO;
import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.mapper.GuidelineMapper;
import com.groom.manvsclass.model.Guideline;
import com.groom.manvsclass.repository.GuidelineRepository;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
public class GuidelineServiceTests {

    @Mock
    private GuidelineRepository guidelineRepository;

    @Mock
    private GuidelineMapper guidelineMapper;

    @InjectMocks
    private GuidelineService guidelineService;

    /* ======================== METODI UTILI ======================== */
    private GuidelineDTO createBaseGuidelineDTO() {

        GuidelineDTO guidelineDTO = new GuidelineDTO();
        guidelineDTO.setTitle("Guideline");
        guidelineDTO.setHint("Testo_Guideline");
        guidelineDTO.setImage(null);

        return guidelineDTO;
    }

    private Guideline createBaseGuideline() {

        Guideline guideline = new Guideline();
        guideline.setTitle("Guideline");
        guideline.setHint("Testo_Guideline");
        guideline.setImage(null);

        return guideline;
    }

    /* ======================== TEST UPLOAD_GUIDELINES ======================== */

    /**
     * Effettua un test del metodo {@link GuidelineService#uploadGuidelines} con
     * dati in input nel formato valido.
     */
    @Test
    public void uploadGuidelines_Correct() {

        // INPUT

        GuidelineDTO firstGuidelineDTO = createBaseGuidelineDTO();
        firstGuidelineDTO.setTitle("Guideline_1");

        GuidelineDTO secondGuidelineDTO = createBaseGuidelineDTO();
        secondGuidelineDTO.setTitle("Guideline_2");

        List<GuidelineDTO> guidelineDTOs = Arrays.asList(firstGuidelineDTO, secondGuidelineDTO);

        // OUTPUT MAPPER

        Guideline firstGuideline = createBaseGuideline();
        firstGuideline.setTitle("Guideline_1");

        Guideline secondGuideline = createBaseGuideline();
        secondGuideline.setTitle("Guideline_2");

        List<Guideline> guidelines = Arrays.asList(firstGuideline, secondGuideline);

        // MOCK MAPPER

        when(guidelineMapper.toEntityList(guidelineDTOs))
                .thenReturn(guidelines);

        // MOCK DEL GUIDELINE REPOSITORY

        when(guidelineRepository.existsByTitle(anyString()))
                .thenReturn(false);

        when(guidelineRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ESECUZIONE TEST

        assertDoesNotThrow(() -> guidelineService.uploadGuidelines(guidelineDTOs));

        // VERIFICA CHIAMATA MAPPER

        verify(guidelineMapper, times(1)).toEntityList(guidelineDTOs);

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).existsByTitle("Guideline_1");
        verify(guidelineRepository, times(1)).existsByTitle("Guideline_2");

        verify(guidelineRepository, times(1)).saveAll(anyList());
    }

    /**
     * Effettua un test del metodo {@link GuidelineService#uploadGuidelines} con
     * lista dei suggerimenti vuota.
     */
    @Test
    public void uploadGuidelines_EmptyList() {

        // INPUT

        List<GuidelineDTO> guidelineDTOs = new ArrayList<>();

        // OUTPUT MAPPER

        List<Guideline> guidelines = new ArrayList<>();

        // MOCK MAPPER

        when(guidelineMapper.toEntityList(guidelineDTOs))
                .thenReturn(guidelines);

        // ESECUZIONE TEST

        assertDoesNotThrow(() -> guidelineService.uploadGuidelines(guidelineDTOs));

        // VERIFICA CHIAMATA MAPPER

        verify(guidelineMapper, times(1)).toEntityList(guidelineDTOs);

        // VERIFICA ASSENZA CHIAMATE SUGGESTION REPOSITORY

        verify(guidelineRepository, times(0)).saveAll(anyList());
    }

    /**
     * Effettua un test del metodo {@link GuidelineService#uploadGuidelines} con
     * un suggerimento valido e un altro no.
     * Verifica che venga lanciata un'eccezione unchecked, in modo da scatenare il
     * rollback della transazione (vista l'annotazione @Transactional del service).
     */
    @Test
    public void uploadGuidelines_Atomic() {

        // INPUT

        GuidelineDTO validGuidelineDTO = createBaseGuidelineDTO();
        validGuidelineDTO.setTitle("Titolo_Valido");

        GuidelineDTO invalidGuidelineDTO = createBaseGuidelineDTO();
        invalidGuidelineDTO.setTitle(null);

        List<GuidelineDTO> guidelineDTOs = Arrays.asList(validGuidelineDTO, invalidGuidelineDTO);

        // OUTPUT MAPPER

        Guideline validGuideline = createBaseGuideline();
        validGuideline.setTitle("Titolo_Valido");

        Guideline invalidGuideline = createBaseGuideline();
        invalidGuideline.setTitle(null);

        List<Guideline> guidelines = Arrays.asList(validGuideline, invalidGuideline);

        // MOCK MAPPER

        when(guidelineMapper.toEntityList(guidelineDTOs))
                .thenReturn(guidelines);

        // MOCK GUIDELINE REPOSITORY

        when(guidelineRepository.existsByTitle(eq("Titolo_Valido")))
                .thenReturn(false);

        when(guidelineRepository.existsByTitle(isNull()))
                .thenThrow(new DataIntegrityViolationException("Title is NULL"));

        // ESECUZIONE TEST

        assertThrows(DataIntegrityViolationException.class, () -> guidelineService.uploadGuidelines(guidelineDTOs));

        // VERIFICA CHIAMATA MAPPER

        verify(guidelineMapper, times(1)).toEntityList(guidelineDTOs);

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(2)).existsByTitle(any());

        verify(guidelineRepository, times(0)).saveAll(anyList());
    }

    /**
     * Effettua un test del metodo {@link GuidelineService#uploadGuidelines} con
     * due linee guida identiche.
     * Verifica che venga lanciata un'eccezione unchecked, in modo da scatenare il
     * rollback della transazione (vista l'annotazione @Transactional del service).
     */
    @Test
    public void uploadGuidelines_DuplicatedGuidelines() {

        // INPUT

        GuidelineDTO firstGuidelineDTO = createBaseGuidelineDTO();

        GuidelineDTO secondGuidelineDTO = createBaseGuidelineDTO();

        List<GuidelineDTO> guidelineDTOs = Arrays.asList(firstGuidelineDTO, secondGuidelineDTO);

        // OUTPUT MAPPER

        Guideline firstGuideline = createBaseGuideline();

        Guideline secondGuideline = createBaseGuideline();

        List<Guideline> guidelines = Arrays.asList(firstGuideline, secondGuideline);

        // MOCK MAPPER

        when(guidelineMapper.toEntityList(guidelineDTOs))
                .thenReturn(guidelines);

        // MOCK GUIDELINE REPOSITORY

        when(guidelineRepository.existsByTitle(anyString()))
                .thenReturn(false);

        when(guidelineRepository.saveAll(anyList()))
                .thenThrow(new DataIntegrityViolationException("Guideline is Duplicated"));

        // ESECUZIONE TEST

        assertThrows(DataIntegrityViolationException.class, () -> guidelineService.uploadGuidelines(guidelineDTOs));

        // VERIFICA CHIAMATA MAPPER

        verify(guidelineMapper, times(1)).toEntityList(guidelineDTOs);

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(2)).existsByTitle(any());

        verify(guidelineRepository, times(1)).saveAll(anyList());
    }

    /* ========================= TEST FIND_GUIDELINES ========================= */

    /**
     * Effettua un test del metodo {@link GuidelineService#findGuidelines} con
     * guideline presenti nel database.
     */
    @Test
    public void findGuidelines_Correct() {

        // OUTPUT GUIDELINE REPOSITORY

        Guideline firstGuideline = createBaseGuideline();
        firstGuideline.setTitle("Guideline_1");

        Guideline secondGuideline = createBaseGuideline();
        secondGuideline.setTitle("Guideline_2");

        List<Guideline> guidelines = Arrays.asList(firstGuideline, secondGuideline);

        // MOCK GUIDELINE REPOSITORY

        when(guidelineRepository.findAllGuidelines())
                .thenReturn(guidelines);

        // OUTPUT MAPPER

        GuidelineDTO firstGuidelineDTO = createBaseGuidelineDTO();
        firstGuidelineDTO.setTitle("Guideline_1");

        GuidelineDTO secondGuidelineDTO = createBaseGuidelineDTO();
        secondGuidelineDTO.setTitle("Guideline_2");

        List<GuidelineDTO> guidelineDTOs = Arrays.asList(firstGuidelineDTO, secondGuidelineDTO);

        // MOCK MAPPER

        when(guidelineMapper.toDtoList(guidelines))
                .thenReturn(guidelineDTOs);

        // ESECUZIONE TEST

        List<GuidelineDTO> testResults = guidelineService.findGuidelines();

        // VERIFICA OUTPUT

        // verifica che l'output restituito dal service contenga esattamente i dto
        // restituiti dal mapper
        assertThat(testResults)
                .hasSize(guidelineDTOs.size())
                .containsExactlyInAnyOrderElementsOf(guidelineDTOs);

        // VERIFICA CHIAMATA GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findAllGuidelines();

        // VERIFICA CHIAMATA MAPPER
        verify(guidelineMapper, times(1)).toDtoList(guidelines);
    }

    /**
     * Effettua un test del metodo {@link GuidelineService#findGuidelines} con
     * guideline assenti nel database.
     */
    @Test
    public void findGuidelines_Correct_MissingGuidelines() {

        // OUTPUT GUIDELINE REPOSITORY

        List<Guideline> guidelines = new ArrayList<>();

        // MOCK GUIDELINE REPOSITORY

        when(guidelineRepository.findAllGuidelines())
                .thenReturn(guidelines);

        // OUTPUT MAPPER

        List<GuidelineDTO> guidelineDTOs = new ArrayList<>();

        // MOCK MAPPER

        when(guidelineMapper.toDtoList(guidelines))
                .thenReturn(guidelineDTOs);

        // ESECUZIONE TEST

        List<GuidelineDTO> testResults = guidelineService.findGuidelines();

        // VERIFICA OUTPUT

        assertEquals(0, testResults.size());

        // VERIFICA CHIAMATA GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findAllGuidelines();

        // VERIFICA CHIAMATA MAPPER
        verify(guidelineMapper, times(1)).toDtoList(guidelines);
    }

    /* ======================== TEST DELETE_GUIDELINES ======================== */

    /**
     * Effettua un test del metodo {@link GuidelineService#deleteGuideline} con
     * guideline esistente nel database.
     */
    @Test
    public void deleteGuideline_Correct() {

        // INPUT

        String guidelineTitle = "Guideline_Esistente";

        // OUTPUT SUGGESTION REPOSITORY

        Guideline guideline = createBaseGuideline();
        guideline.setTitle(guidelineTitle);

        // MOCK GUIDELINE REPOSITORY

        when(guidelineRepository.findByTitle(guidelineTitle))
                .thenReturn(Optional.of(guideline));

        // ESECUZIONE TEST

        assertDoesNotThrow(() -> guidelineService.deleteGuideline(guidelineTitle));

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findByTitle(guidelineTitle);

        verify(guidelineRepository, times(1)).delete(guideline);
    }

    /**
     * Effettua un test del metodo {@link GuidelineService#deleteGuideline} con
     * guideline non esistente nel database.
     */
    @Test
    public void deleteGuideline_GuidelineNotFound() {

        // INPUT

        String guidelineTitle = "Guideline_Non_Esistente";

        // MOCK GUIDELINE REPOSITORY

        when(guidelineRepository.findByTitle(guidelineTitle))
                .thenReturn(Optional.empty());

        // ESECUZIONE TEST

        assertThrows(NotFoundException.class, () -> guidelineService.deleteGuideline(guidelineTitle));

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findByTitle(guidelineTitle);

        verify(guidelineRepository, times(0)).delete(any(Guideline.class));
    }

}