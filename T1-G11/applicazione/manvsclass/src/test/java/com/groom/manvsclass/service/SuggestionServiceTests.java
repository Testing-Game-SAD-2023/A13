package com.groom.manvsclass.service;

import com.groom.manvsclass.dto.SuggestionDTO;
import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.mapper.SuggestionMapper;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Suggestion;
import com.groom.manvsclass.model.SuggestionLevel;
import com.groom.manvsclass.repository.ClassUTRepository;
import com.groom.manvsclass.repository.SuggestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class SuggestionServiceTests {

    @Mock
    private ClassUTRepository classUTRepository;

    @Mock
    private SuggestionRepository suggestionRepository;

    @Mock
    private SuggestionMapper suggestionMapper;

    @InjectMocks
    private SuggestionService suggestionService;

    /* ======================== METODI UTILI ======================== */
    private SuggestionDTO createBaseSuggestionDTO() {

        SuggestionDTO suggestionDTO = new SuggestionDTO();
        suggestionDTO.setTitle("Suggerimento");
        suggestionDTO.setHint("Testo_Suggerimento");
        suggestionDTO.setImage(null);
        suggestionDTO.setLevel(SuggestionLevel.LOW);

        return suggestionDTO;
    }

    private Suggestion createBaseSuggestion() {

        Suggestion suggestion = new Suggestion();
        suggestion.setTitle("Suggerimento");
        suggestion.setHint("Testo_Suggerimento");
        suggestion.setImage(null);
        suggestion.setLevel(SuggestionLevel.LOW);

        return suggestion;
    }

    /* ======================== TEST UPLOAD_SUGGESTIONS ======================== */

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestions} con
     * dati in input nel formato valido e classe esistente nel database.
     */
    @Test
    public void uploadSuggestions_Correct() {

        // INPUT

        String className = "Classe_Esistente";

        SuggestionDTO firstSuggestionDTO = createBaseSuggestionDTO();
        firstSuggestionDTO.setTitle("Suggerimento_1");

        SuggestionDTO secondSuggestionDTO = createBaseSuggestionDTO();
        secondSuggestionDTO.setTitle("Suggerimento_2");

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(firstSuggestionDTO, secondSuggestionDTO);

        // OUTPUT MAPPER

        Suggestion firstSuggestion = createBaseSuggestion();
        firstSuggestion.setTitle("Suggerimento_1");

        Suggestion secondSuggestion = createBaseSuggestion();
        secondSuggestion.setTitle("Suggerimento_2");

        List<Suggestion> suggestions = Arrays.asList(firstSuggestion, secondSuggestion);

        // MOCK MAPPER

        when(suggestionMapper.toEntityList(suggestionDTOs))
                .thenReturn(suggestions);

        // OUTPUT CLASS_UT REPOSITORY

        ClassUT mockClassUT = new ClassUT();
        mockClassUT.setName("Classe_Esistente");

        // MOCK CLASS_UT REPOSITORY

        when(classUTRepository.findById(className))
                .thenReturn(Optional.of(mockClassUT));

        // MOCK DEL SUGGESTION REPOSITORY

        when(suggestionRepository.existsByClassUT_NameAndTitle(eq(className), anyString()))
                .thenReturn(false);

        when(suggestionRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ESECUZIONE TEST

        assertDoesNotThrow(() -> suggestionService.uploadSuggestions(className, suggestionDTOs));

        // VERIFICA CHIAMATA MAPPER

        verify(suggestionMapper, times(1)).toEntityList(suggestionDTOs);

        // VERIFICA CHIAMATA CLASS_UT REPOSITORY

        verify(classUTRepository, times(1)).findById(className);

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).existsByClassUT_NameAndTitle(className, "Suggerimento_1");
        verify(suggestionRepository, times(1)).existsByClassUT_NameAndTitle(className, "Suggerimento_2");

        verify(suggestionRepository, times(1)).saveAll(anyList());
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestions} con
     * dati in input nel formato valido e classe inesistente nel database.
     */
    @Test
    public void uploadSuggestions_ClassNotFound() {

        // INPUT

        String className = "Classe_Non_Esistente";

        SuggestionDTO suggestionDTO = createBaseSuggestionDTO();

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(suggestionDTO);

        // OUTPUT MAPPER

        Suggestion suggestion = createBaseSuggestion();

        List<Suggestion> suggestions = Arrays.asList(suggestion);

        // MOCK MAPPER

        when(suggestionMapper.toEntityList(suggestionDTOs))
                .thenReturn(suggestions);

        // MOCK CLASS_UT REPOSITORY

        when(classUTRepository.findById(className))
                .thenReturn(Optional.empty());

        // ESECUIONE TEST

        assertThrows(NotFoundException.class, () -> suggestionService.uploadSuggestions(className, suggestionDTOs));

        // VERIFICA CHIAMATA MAPPER

        verify(suggestionMapper, times(1)).toEntityList(suggestionDTOs);

        // VERIFICA CHIAMATA CLASS_UT REPOSITORY

        verify(classUTRepository, times(1)).findById(className);

        // VERIFICA ASSENZA CHIAMATE SUGGESTION REPOSITORY

        verifyNoInteractions(suggestionRepository);
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestions} con
     * lista dei suggerimenti vuota.
     */
    @Test
    public void uploadSuggestions_EmptyList() {

        // INPUT

        String className = "Classe_Esistente";

        List<SuggestionDTO> suggestionDTOs = new ArrayList<>();

        // OUTPUT MAPPER

        List<Suggestion> suggestions = new ArrayList<>();

        // MOCK MAPPER

        when(suggestionMapper.toEntityList(suggestionDTOs))
                .thenReturn(suggestions);

        // OUTPUT CLASS_UT REPOSITORY

        ClassUT mockClassUT = new ClassUT();
        mockClassUT.setName(className);

        // MOCK CLASS_UT REPOSITORY

        when(classUTRepository.findById(className))
                .thenReturn(Optional.of(mockClassUT));

        // ESECUZIONE TEST

        assertDoesNotThrow(() -> suggestionService.uploadSuggestions(className, suggestionDTOs));

        // VERIFICA CHIAMATA MAPPER

        verify(suggestionMapper, times(1)).toEntityList(suggestionDTOs);

        // VERIFICA CHIAMATA CLASS_UT REPOSITORY

        verify(classUTRepository, times(1)).findById(className);

        // VERIFICA ASSENZA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(0)).saveAll(anyList());
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestions} con
     * un suggerimento valido e un altro no.
     * Verifica che venga lanciata un'eccezione unchecked, in modo da scatenare il
     * rollback della transazione (vista l'annotazione @Transactional del service).
     */
    @Test
    public void uploadSuggestions_Atomic() {

        // INPUT

        String className = "Classe_Esistente";

        SuggestionDTO validSuggestionDTO = createBaseSuggestionDTO();
        validSuggestionDTO.setTitle("Titolo_Valido");

        SuggestionDTO invalidSuggestionDTO = createBaseSuggestionDTO();
        invalidSuggestionDTO.setTitle(null);

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(validSuggestionDTO, invalidSuggestionDTO);

        // OUTPUT MAPPER

        Suggestion validSuggestion = createBaseSuggestion();
        validSuggestion.setTitle("Titolo_Valido");

        Suggestion invalidSuggestion = createBaseSuggestion();
        invalidSuggestion.setTitle(null);

        List<Suggestion> suggestions = Arrays.asList(validSuggestion, invalidSuggestion);

        // MOCK MAPPER

        when(suggestionMapper.toEntityList(suggestionDTOs))
                .thenReturn(suggestions);

        // OUTPUT CLASS_UT REPOSITORY

        ClassUT mockClassUT = new ClassUT();
        mockClassUT.setName(className);

        // MOCK CLASS_UT REPOSITORY

        when(classUTRepository.findById(className))
                .thenReturn(Optional.of(mockClassUT));

        // MOCK SUGGESTION REPOSITORY

        when(suggestionRepository.existsByClassUT_NameAndTitle(eq(className), eq("Titolo_Valido")))
                .thenReturn(false);

        when(suggestionRepository.existsByClassUT_NameAndTitle(eq(className), isNull()))
                .thenThrow(new DataIntegrityViolationException("Title is NULL"));

        // ESECUZIONE TEST

        assertThrows(DataIntegrityViolationException.class,
                () -> suggestionService.uploadSuggestions(className, suggestionDTOs));

        // VERIFICA CHIAMATA MAPPER

        verify(suggestionMapper, times(1)).toEntityList(suggestionDTOs);

        // VERIFICA CHIAMATA CLASS_UT REPOSITORY

        verify(classUTRepository, times(1)).findById(className);

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(2)).existsByClassUT_NameAndTitle(eq(className), any());

        verify(suggestionRepository, times(0)).saveAll(anyList());
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestions} con
     * due suggerimenti identici.
     * Verifica che venga lanciata un'eccezione unchecked, in modo da scatenare il
     * rollback della transazione (vista l'annotazione @Transactional del service).
     */
    @Test
    public void uploadSuggestions_DuplicatedSuggestions() {

        // INPUT

        String className = "Classe_Esistente";

        SuggestionDTO firstSuggestionDTO = createBaseSuggestionDTO();

        SuggestionDTO secondSuggestionDTO = createBaseSuggestionDTO();

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(firstSuggestionDTO, secondSuggestionDTO);

        // OUTPUT MAPPER

        Suggestion firstSuggestion = createBaseSuggestion();

        Suggestion secondSuggestion = createBaseSuggestion();

        List<Suggestion> suggestions = Arrays.asList(firstSuggestion, secondSuggestion);

        // MOCK MAPPER

        when(suggestionMapper.toEntityList(suggestionDTOs))
                .thenReturn(suggestions);

        // OUTPUT CLASS_UT REPOSITORY

        ClassUT mockClassUT = new ClassUT();
        mockClassUT.setName(className);

        // MOCK CLASS_UT REPOSITORY

        when(classUTRepository.findById(className))
                .thenReturn(Optional.of(mockClassUT));

        // MOCK SUGGESTION REPOSITORY

        when(suggestionRepository.existsByClassUT_NameAndTitle(eq(className), anyString()))
                .thenReturn(false);

        when(suggestionRepository.saveAll(anyList()))
                .thenThrow(new DataIntegrityViolationException("Suggestion is Duplicated"));

        // ESECUZIONE TEST

        assertThrows(DataIntegrityViolationException.class,
                () -> suggestionService.uploadSuggestions(className, suggestionDTOs));

        // VERIFICA CHIAMATA MAPPER

        verify(suggestionMapper, times(1)).toEntityList(suggestionDTOs);

        // VERIFICA CHIAMATA CLASS_UT REPOSITORY

        verify(classUTRepository, times(1)).findById(className);

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(2)).existsByClassUT_NameAndTitle(eq(className), any());

        verify(suggestionRepository, times(1)).saveAll(anyList());
    }

    /* ========================= TEST FIND_SUGGESTIONS ========================= */

    /**
     * Effettua un test del metodo {@link SuggestionService#findSuggestions} con
     * classe esistente nel database e suggerimenti associati presenti.
     */
    @Test
    public void findSuggestions_Correct() {

        // INPUT

        String className = "Classe_Esistente";

        // MOCK CLASS_UT REPOSITORY

        when(classUTRepository.existsById(className))
                .thenReturn(true);

        // OUTPUT SUGGESTION REPOSITORY

        Suggestion firstSuggestion = createBaseSuggestion();
        firstSuggestion.setTitle("Suggerimento_1");

        Suggestion secondSuggestion = createBaseSuggestion();
        secondSuggestion.setTitle("Suggerimento_2");

        List<Suggestion> suggestions = Arrays.asList(firstSuggestion, secondSuggestion);

        // MOCK SUGGESTION REPOSITORY

        when(suggestionRepository.findAllByClassUT_Name(className))
                .thenReturn(suggestions);

        // OUTPUT MAPPER

        SuggestionDTO firstSuggestionDTO = createBaseSuggestionDTO();
        firstSuggestionDTO.setTitle("Suggerimento_1");

        SuggestionDTO secondSuggestionDTO = createBaseSuggestionDTO();
        secondSuggestionDTO.setTitle("Suggerimento_2");

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(firstSuggestionDTO, secondSuggestionDTO);

        // MOCK MAPPER

        when(suggestionMapper.toDtoList(suggestions))
                .thenReturn(suggestionDTOs);

        // ESECUZIONE TEST

        List<SuggestionDTO> testResults = suggestionService.findSuggestions(className);

        // VERIFICA OUTPUT

        // verifica che l'output restituito dal service contenga esattamente i dto
        // restituiti dal mapper
        assertThat(testResults)
                .hasSize(suggestionDTOs.size())
                .containsExactlyInAnyOrderElementsOf(suggestionDTOs);

        // VERIFICA CHIAMATA CLASS_UT REPOSITORY

        verify(classUTRepository, times(1)).existsById(className);

        // VERIFICA CHIAMATA SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findAllByClassUT_Name(className);

        // VERIFICA CHIAMATA MAPPER
        verify(suggestionMapper, times(1)).toDtoList(suggestions);
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#findSuggestions} con
     * classe esistente nel database e nessun suggerimento associato.
     */
    @Test
    public void findSuggestions_Correct_MissingSuggestions() {

        // INPUT

        String className = "Classe_Esistente";

        // MOCK CLASS_UT REPOSITORY

        when(classUTRepository.existsById(className))
                .thenReturn(true);

        // OUTPUT SUGGESTION REPOSITORY

        List<Suggestion> suggestions = new ArrayList<>();

        // MOCK SUGGESTION REPOSITORY

        when(suggestionRepository.findAllByClassUT_Name(className))
                .thenReturn(suggestions);

        // OUTPUT MAPPER

        List<SuggestionDTO> suggestionDTOs = new ArrayList<>();

        // MOCK MAPPER

        when(suggestionMapper.toDtoList(suggestions))
                .thenReturn(suggestionDTOs);

        // ESECUZIONE TEST

        List<SuggestionDTO> testResults = suggestionService.findSuggestions(className);

        // VERIFICA OUTPUT

        assertEquals(0, testResults.size());

        // VERIFICA CHIAMATA CLASS_UT REPOSITORY

        verify(classUTRepository, times(1)).existsById(className);

        // VERIFICA CHIAMATA SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findAllByClassUT_Name(className);

        // VERIFICA CHIAMATA MAPPER
        verify(suggestionMapper, times(1)).toDtoList(suggestions);
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#findSuggestions} con
     * classe inesistente nel database.
     */
    @Test
    public void findSuggestions_ClassNotFound() {

        // INPUT

        String className = "Classe_Non_Esistente";

        // MOCK CLASS_UT REPOSITORY

        when(classUTRepository.existsById(className))
                .thenReturn(false);

        // ESECUZIONE TEST

        assertThrows(NotFoundException.class, () -> suggestionService.findSuggestions(className));

        // VERIFICA CHIAMATA CLASS_UT REPOSITORY

        verify(classUTRepository, times(1)).existsById(className);

        // VERIFICA ASSENZA CHIAMATE SUGGESTION REPOSITORY

        verifyNoInteractions(suggestionRepository);
    }

    /* ======================== TEST DELETE_SUGGESTIONS ======================== */

    /**
     * Effettua un test del metodo {@link SuggestionService#deleteSuggestion} con
     * classe e suggerimento associato esistenti nel database.
     */
    @Test
    public void deleteSuggestion_Correct() {

        // INPUT

        String className = "Classe_Esistente";

        String suggestionTitle = "Suggerimento_Esistente";

        // MOCK CLASS_UT REPOSITORY

        when(classUTRepository.existsById(className))
                .thenReturn(true);

        // OUTPUT SUGGESTION REPOSITORY

        Suggestion suggestion = createBaseSuggestion();
        suggestion.setTitle(suggestionTitle);
        ClassUT classUT = new ClassUT();
        classUT.setName(className);
        suggestion.setClassUT(classUT);

        // MOCK SUGGESTION REPOSITORY

        when(suggestionRepository.findByClassUT_NameAndTitle(className, suggestionTitle))
                .thenReturn(Optional.of(suggestion));

        // ESECUZIONE TEST

        assertDoesNotThrow(() -> suggestionService.deleteSuggestion(className, suggestionTitle));

        // VERIFICA CHIAMATA SUGGESTION REPOSITORY

        verify(classUTRepository, times(1)).existsById(className);

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findByClassUT_NameAndTitle(className, suggestionTitle);

        verify(suggestionRepository, times(1)).delete(suggestion);
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#deleteSuggestion} con
     * classe inesistente nel database.
     */
    @Test
    public void deleteSuggestion_ClassNotFound() {

        // INPUT

        String className = "Classe_Non_Esistente";

        String suggestionTitle = "Suggerimento_Esistente";

        // MOCK CLASS_UT REPOSITORY

        when(classUTRepository.existsById(className))
                .thenReturn(false);

        // ESECUZIONE TEST

        assertThrows(NotFoundException.class, () -> suggestionService.deleteSuggestion(className, suggestionTitle));

        // VERIFICA CHIAMATA CLASS_UT REPOSITORY

        verify(classUTRepository, times(1)).existsById(className);

        // VERIFICA ASSENZA CHIAMATE SUGGESTION REPOSITORY

        verifyNoInteractions(suggestionRepository);
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#deleteSuggestion} con
     * suggerimento inesistente nel database.
     */
    @Test
    public void deleteSuggestion_SuggestionNotFound() {

        // INPUT

        String className = "Classe_Esistente";

        String suggestionTitle = "Suggerimento_Non_Esistente";

        // MOCK CLASS_UT REPOSITORY

        when(classUTRepository.existsById(className))
                .thenReturn(true);

        // MOCK SUGGESTION REPOSITORY

        when(suggestionRepository.findByClassUT_NameAndTitle(className, suggestionTitle))
                .thenReturn(Optional.empty());

        // ESECUZIONE TEST

        assertThrows(NotFoundException.class, () -> suggestionService.deleteSuggestion(className, suggestionTitle));

        // VERIFICA CHIAMATA CLASS_UT REPOSITORY

        verify(classUTRepository, times(1)).existsById(className);

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findByClassUT_NameAndTitle(className, suggestionTitle);

        verify(suggestionRepository, times(0)).delete(any(Suggestion.class));
    }

}
