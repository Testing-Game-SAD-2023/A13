package com.groom.manvsclass.service;

import com.groom.manvsclass.dto.SuggestionDTO;
import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.mapper.SuggestionMapper;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Suggestion;
import com.groom.manvsclass.model.SuggestionLevel;
import com.groom.manvsclass.repository.ClassUTRepository;
import com.groom.manvsclass.repository.SuggestionRepository;
import com.groom.manvsclass.util.TestUtils;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class SuggestionServiceTests {

    @Mock
    private ClassUTRepository classUTRepository;

    @Mock
    private SuggestionRepository suggestionRepository;

    @Mock
    private SuggestionMapper suggestionMapper;

    @Mock
    private ImageService imageService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private SuggestionService suggestionService;

    @Captor
    private ArgumentCaptor<List<Suggestion>> suggestionListCaptor;

    @Captor
    private ArgumentCaptor<Suggestion> suggestionCaptor;

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

    // configurazione di comparison per Suggestion
    public static final RecursiveComparisonConfiguration SUGGESTION_COMPARISON_CONFIG = RecursiveComparisonConfiguration.builder()
            .withComparedFields("id", "order", "hint", "date", "image", "level")
            .withEqualsForType(
                    (classUT1, classUT2) -> classUT1.getName().equals(classUT2.getName()),
                    ClassUT.class
            )
            .build();

    // assert personalizzati
    private final TestUtils<Suggestion> suggestionTestUtils = new TestUtils<>(SUGGESTION_COMPARISON_CONFIG);

    /**
     * Crea un suggerimento associato alla classe {@code classUT}.
     */
    public static Suggestion createBaseSuggestion(ClassUT classUT) {

        Suggestion suggestion = new Suggestion();
        suggestion.setClassUT(classUT);
        suggestion.setOrder(1);
        suggestion.setHint("Testo_Suggerimento");
        suggestion.setDate(LocalDate.now());
        suggestion.setLevel(SuggestionLevel.LOW);
        suggestion.setImage(null);

        return suggestion;
    }

    // TEST UPLOAD_SUGGESTIONS

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestions} con
     * dati in input nel formato valido e classe esistente nel database.
     */
    @Test
    public void uploadSuggestions_Correct() {

        // INPUT

        String className = "Calcolatrice";

        SuggestionDTO firstSuggestionDTO = createBaseSuggestionDTO();
        firstSuggestionDTO.setOrder(1);

        SuggestionDTO secondSuggestionDTO = createBaseSuggestionDTO();
        secondSuggestionDTO.setOrder(2);

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(firstSuggestionDTO, secondSuggestionDTO);

        // OUTPUT MAPPER

        Suggestion firstSuggestion = createBaseSuggestion(null);
        firstSuggestion.setOrder(1);

        Suggestion secondSuggestion = createBaseSuggestion(null);
        secondSuggestion.setOrder(2);

        List<Suggestion> suggestions = Arrays.asList(firstSuggestion, secondSuggestion);

        // MOCK MAPPER

        when(suggestionMapper.toEntityList(suggestionDTOs))
                .thenReturn(suggestions);

        // OUTPUT CLASS_UT REPOSITORY

        ClassUT mockClassUT = new ClassUT();
        mockClassUT.setName("Calcolatrice");

        // MOCK CLASS_UT REPOSITORY

        when(classUTRepository.findById(className))
                .thenReturn(Optional.of(mockClassUT));

        // MOCK DEL SUGGESTION REPOSITORY

        when(suggestionRepository.findAllByClassUT_Name(className))
                .thenReturn(List.of(firstSuggestion));

        when(suggestionRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ESECUZIONE TEST

        assertDoesNotThrow(() -> suggestionService.uploadSuggestions(className, suggestionDTOs));

        // VERIFICA CHIAMATA MAPPER

        verify(suggestionMapper, times(1)).toEntityList(suggestionDTOs);

        // VERIFICA CHIAMATA CLASS_UT REPOSITORY

        verify(classUTRepository, times(1)).findById(className);

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findAllByClassUT_Name(className);

        // CAPTURE DEGLI ARGOMENTI PASSATI A saveAll
        verify(suggestionRepository, times(1)).saveAll(suggestionListCaptor.capture());

        List<Suggestion> savedSuggestions = suggestionListCaptor.getValue();

        // verifica che i suggerimenti passati a saveAll corrispondono effettivamente a quelli da salvare
        suggestionTestUtils.assertListEquals(suggestions, savedSuggestions);
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

        Suggestion suggestion = createBaseSuggestion(null);

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

        // 	VERIFICA CHIAMATA CLASS_UT REPOSITORY

        verify(classUTRepository, times(1)).findById(className);

        // 	VERIFICA ASSENZA CHIAMATE SUGGESTION REPOSITORY

        verifyNoInteractions(suggestionRepository);
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestions} con
     * lista dei suggerimenti vuota.
     */
    @Test
    public void uploadSuggestions_EmptyList() {

        // INPUT

        String className = "Calcolatrice";

        List<SuggestionDTO> suggestionDTOs = new ArrayList<>();

        // ESECUZIONE TEST

        assertDoesNotThrow(() -> suggestionService.uploadSuggestions(className, suggestionDTOs));

        // VERIFICA ASSENZA CHIAMATE MAPPER

        verifyNoInteractions(suggestionMapper);

        // VERIFICA ASSENZA CHIAMATE CLASS_UT REPOSITORY

        verifyNoInteractions(classUTRepository);

        // VERIFICA ASSENZA CHIAMATE SUGGESTION REPOSITORY

        verifyNoInteractions(suggestionRepository);
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

        String className = "Calcolatrice";

        SuggestionDTO validSuggestionDTO = createBaseSuggestionDTO();
        validSuggestionDTO.setOrder(1);

        SuggestionDTO invalidSuggestionDTO = createBaseSuggestionDTO();
        invalidSuggestionDTO.setOrder(-1);

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(validSuggestionDTO, invalidSuggestionDTO);

        // OUTPUT MAPPER

        Suggestion validSuggestion = createBaseSuggestion(null);
        validSuggestion.setOrder(1);

        Suggestion invalidSuggestion = createBaseSuggestion(null);
        invalidSuggestion.setOrder(-1);

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

        when(suggestionRepository.findAllByClassUT_Name(eq(className)))
                .thenReturn(List.of());

        when(suggestionRepository.saveAll(anyList()))
                .thenThrow(new DataIntegrityViolationException("Null Value"));

        // ESECUZIONE TEST

        assertThrows(DataIntegrityViolationException.class, () -> suggestionService.uploadSuggestions(className, suggestionDTOs));

        // VERIFICA CHIAMATA MAPPER

        verify(suggestionMapper, times(1)).toEntityList(suggestionDTOs);

        // VERIFICA CHIAMATA CLASS_UT REPOSITORY

        verify(classUTRepository, times(1)).findById(className);

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findAllByClassUT_Name(className);

        verify(suggestionRepository, times(1)).saveAll(suggestionListCaptor.capture());

        List<Suggestion> savedSuggestions = suggestionListCaptor.getValue();

        // verifica che i suggerimenti passati a saveAll corrispondono effettivamente a quelli da salvare
        suggestionTestUtils.assertListEquals(suggestions, savedSuggestions);
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

        String className = "Calcolatrice";

        SuggestionDTO firstSuggestionDTO = createBaseSuggestionDTO();

        SuggestionDTO secondSuggestionDTO = createBaseSuggestionDTO();

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(firstSuggestionDTO, secondSuggestionDTO);

        // OUTPUT MAPPER

        Suggestion firstSuggestion = createBaseSuggestion(null);

        Suggestion secondSuggestion = createBaseSuggestion(null);

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

        when(suggestionRepository.findAllByClassUT_Name(eq(className)))
                .thenReturn(List.of(firstSuggestion));

        when(suggestionRepository.saveAll(anyList()))
                .thenThrow(new DataIntegrityViolationException("Same Order"));

        // ESECUZIONE TEST

        assertThrows(DataIntegrityViolationException.class, () -> suggestionService.uploadSuggestions(className, suggestionDTOs));

        // VERIFICA CHIAMATA MAPPER

        verify(suggestionMapper, times(1)).toEntityList(suggestionDTOs);

        // VERIFICA CHIAMATA CLASS_UT REPOSITORY

        verify(classUTRepository, times(1)).findById(className);

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findAllByClassUT_Name(className);

        verify(suggestionRepository, times(1)).saveAll(suggestionListCaptor.capture());

        List<Suggestion> savedSuggestions = suggestionListCaptor.getValue();

        // verifica che i suggerimenti passati a saveAll corrispondono effettivamente a quelli da salvare
        suggestionTestUtils.assertListEquals(suggestions, savedSuggestions);
    }

    // TEST FIND_SUGGESTIONS

    /**
     * Effettua un test del metodo {@link SuggestionService#findSuggestions} con
     * classe esistente nel database e suggerimenti associati presenti.
     */
    @Test
    public void findSuggestions_Correct() {

        // INPUT

        String className = "Calcolatrice";

        // MOCK CLASS_UT REPOSITORY

        when(classUTRepository.existsById(className))
                .thenReturn(true);

        // OUTPUT SUGGESTION REPOSITORY

        Suggestion suggestion = createBaseSuggestion(null);
        List<Suggestion> suggestions = Arrays.asList(suggestion);

        // MOCK SUGGESTION REPOSITORY

        when(suggestionRepository.findAllByClassUT_Name(className))
                .thenReturn(suggestions);

        // OUTPUT MAPPER

        SuggestionDTO suggestionDTO = createBaseSuggestionDTO();
        List<SuggestionDTO> suggestionDTOs = Arrays.asList(suggestionDTO);

        // MOCK MAPPER

        when(suggestionMapper.toDtoList(suggestions))
                .thenReturn(suggestionDTOs);

        // ESECUZIONE TEST

        List<SuggestionDTO> testResults = suggestionService.findSuggestions(className);

        // VERIFICA OUTPUT

        assertEquals(1, testResults.size());

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

        String className = "Calcolatrice";

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

    // TEST DELETE_SUGGESTION

    /**
     * Effettua un test del metodo {@link SuggestionService#deleteSuggestion} con
     * classe e suggerimento associato esistenti nel database.
     */
    @Test
    public void deleteSuggestion_Correct() {

        // INPUT

        String className = "Calcolatrice";

        int suggestionOrder = 1;

        // OUTPUT SUGGESTION REPOSITORY

        Suggestion suggestion = createBaseSuggestion(null);
        suggestion.setOrder(suggestionOrder);
        ClassUT classUT = new ClassUT();
        classUT.setName(className);
        suggestion.setClassUT(classUT);

        // MOCK SUGGESTION REPOSITORY

        when(suggestionRepository.findByClassUT_NameAndOrder(className, suggestionOrder))
                .thenReturn(Optional.of(suggestion));

        // ESECUZIONE TEST

        assertDoesNotThrow(() -> suggestionService.deleteSuggestion(className, suggestionOrder));

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findByClassUT_NameAndOrder(className, suggestionOrder);

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

        int suggestionOrder = 1;

        // ESECUZIONE TEST

        assertThrows(NotFoundException.class, () -> suggestionService.deleteSuggestion(className, suggestionOrder));

        // VERIFICA CHIAMATA SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findByClassUT_NameAndOrder(className, suggestionOrder);

        verify(suggestionRepository, times(0)).delete(any(Suggestion.class));
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#deleteSuggestion} con
     * suggerimento inesistente nel database.
     */
    @Test
    public void deleteSuggestion_SuggestionNotFound() {

        // INPUT

        String className = "Calcolatrice";

        int suggestionOrder = 2;

        // MOCK SUGGESTION REPOSITORY

        when(suggestionRepository.findByClassUT_NameAndOrder(className, suggestionOrder))
                .thenReturn(Optional.empty());

        // ESECUZIONE TEST

        assertThrows(NotFoundException.class, () -> suggestionService.deleteSuggestion(className, suggestionOrder));

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findByClassUT_NameAndOrder(className, suggestionOrder);

        verify(suggestionRepository, times(0)).delete(any(Suggestion.class));
    }

    // TEST UPLOAD_SUGGESTION_IMAGE

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestionImage} con
     * dati in input nel formato valido e suggerimento esistente nel database.
     */
    @Test
    public void uploadSuggestionsImage_Success_NoExistingImage() throws IOException {

        // INPUT

        String className = "Calcolatrice";
        String fileName = "immagine.png";

        // OUTPUT SUGGESTION REPOSITORY

        Suggestion suggestion = createBaseSuggestion(null);
        suggestion.setOrder(1);
        suggestion.setImage(null);  // immagine non presente

        // MOCK DEL MULTIPART FILE

        when(multipartFile.getOriginalFilename()).thenReturn(fileName);

        // MOCK DEL SUGGESTION REPOSITORY

        when(suggestionRepository.findByClassUT_NameAndOrder(className, suggestion.getOrder()))
                .thenReturn(Optional.of(suggestion));

        // ESECUZIONE TEST

        assertDoesNotThrow(() -> suggestionService.uploadSuggestionImage(className, suggestion.getOrder(), multipartFile));

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findByClassUT_NameAndOrder(className, suggestion.getOrder());

        // VERIFICA CHE L'IMMAGINE SIA STATA SALVATA CON IL NOME CORRETTO

        verify(imageService, times(1)).storeImage(multipartFile, "Calcolatrice_1.png");
        verify(imageService, times(0)).deleteImage(anyString());

        // CAPTURE DEGLI ARGOMENTI PASSATI A save (chiamato per fare l'upload del campo image)
        verify(suggestionRepository, times(1)).save(suggestionCaptor.capture());

        Suggestion updatedSuggestion = suggestionCaptor.getValue();

        // crea un nuovo suggerimento con i campi attesi
        // non utilizza "suggestion" perché corrisponde allo STESSO oggetto updatedSuggestion
        Suggestion expectedSuggestion = createBaseSuggestion(null);
        expectedSuggestion.setOrder(1);
        expectedSuggestion.setImage("Calcolatrice_1.png");  // modifica immagine per effettuare il confronto

        suggestionTestUtils.assertEquals(expectedSuggestion, updatedSuggestion);
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestionImage} con
     * dati in input nel formato valido e suggerimento esistente nel database, con immagine già presente.
     */
    @Test
    public void uploadSuggestionsImage_Success_WithExistingImage() throws IOException {

        // INPUT

        String className = "Calcolatrice";
        String oldFileName = "Calcolatrice_1.jpg";
        String newFileName = "immagine.png";

        // OUTPUT SUGGESTION REPOSITORY

        Suggestion suggestion = createBaseSuggestion(null);
        suggestion.setOrder(1);
        suggestion.setImage(oldFileName);  // immagine presente

        // MOCK DEL MULTIPART FILE

        when(multipartFile.getOriginalFilename()).thenReturn(newFileName);

        // MOCK DEL SUGGESTION REPOSITORY

        when(suggestionRepository.findByClassUT_NameAndOrder(className, suggestion.getOrder()))
                .thenReturn(Optional.of(suggestion));

        // ESECUZIONE TEST

        assertDoesNotThrow(() -> suggestionService.uploadSuggestionImage(className, suggestion.getOrder(), multipartFile));

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findByClassUT_NameAndOrder(className, suggestion.getOrder());

        // VERIFICA CHE L'IMMAGINE PRECEDENTE SIA STATA CANCELLATA
        verify(imageService, times(1)).deleteImage(oldFileName);

        // VERIFICA CHE L'IMMAGINE SIA STATA SALVATA CON IL NOME CORRETTO

        verify(imageService).storeImage(multipartFile, "Calcolatrice_1.png");

        // CAPTURE DEGLI ARGOMENTI PASSATI A save (chiamato per fare l'upload del campo image)
        verify(suggestionRepository, times(1)).save(suggestionCaptor.capture());

        Suggestion updatedSuggestion = suggestionCaptor.getValue();

        // crea un nuovo suggerimento con i campi attesi
        // non utilizza "suggestion" perché corrisponde allo STESSO oggetto updatedSuggestion
        Suggestion expectedSuggestion = createBaseSuggestion(null);
        expectedSuggestion.setOrder(1);
        expectedSuggestion.setImage("Calcolatrice_1.png");  // modifica immagine per effettuare il confronto

        suggestionTestUtils.assertEquals(expectedSuggestion, updatedSuggestion);
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestionImage} con
     * dati in input nel formato valido e suggerimento non esistente nel database.
     */
    @Test
    public void uploadSuggestionsImage_SuggestionNotFound() throws IOException {

        // INPUT

        String className = "Calcolatrice";
        int invalidOrder = 99;

        // MOCK DEL SUGGESTION REPOSITORY

        when(suggestionRepository.findByClassUT_NameAndOrder(className, invalidOrder))
                .thenReturn(Optional.empty());

        // ESECUZIONE TEST

        assertThrows(NotFoundException.class,
                () -> suggestionService.uploadSuggestionImage(className, invalidOrder, multipartFile)
        );

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findByClassUT_NameAndOrder(className, invalidOrder);

        // VERIFICA NO INTERACTIONS CON imageService E suggestionRepository.save()
        verifyNoInteractions(imageService);
        verifyNoMoreInteractions(suggestionRepository);
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestionImage} con
     * dati in input nel formato valido e suggerimento esistente ma errore in imageService.deleteImage.
     */
    @Test
    void testUploadSuggestionImage_ErrorDeletingOldImage() throws IOException {
        String className = "Calcolatrice";
        int order = 1;
        String oldFileName = "old_image.jpg";
        String newFilename = "immagine.jpg";

        Suggestion suggestion = createBaseSuggestion(null);
        suggestion.setImage(oldFileName);

        when(suggestionRepository.findByClassUT_NameAndOrder(className, order))
                .thenReturn(Optional.of(suggestion));

        // simula un errore nella cancellazione
        doThrow(new IOException("Delete failed"))
                .when(imageService).deleteImage(oldFileName);

        assertThrows(RuntimeException.class, () -> {
            suggestionService.uploadSuggestionImage(className, order, multipartFile);
        });

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findByClassUT_NameAndOrder(className, order);

        // VERIFICA CHE SIA STATA INVOCATA LA DELETE
        verify(imageService, times(1)).deleteImage(oldFileName);

        // verifica che storeImage e save NON siano stati chiamati
        verify(imageService, never()).storeImage(any(), any());
        verify(suggestionRepository, never()).save(any());
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestionImage} con
     * dati in input nel formato valido e suggerimento esistente ma errore in imageService.storeImage.
     */
    @Test
    void testUploadSuggestionImage_ErrorStoringImage() throws IOException {
        String className = "Calcolatrice";
        int order = 1;
        String fileName = "immagine.png";

        Suggestion suggestion = createBaseSuggestion(null);
        suggestion.setImage(null);

        when(multipartFile.getOriginalFilename()).thenReturn(fileName);
        when(suggestionRepository.findByClassUT_NameAndOrder(className, order))
                .thenReturn(Optional.of(suggestion));

        // simula un errore nel salvataggio
        doThrow(new IOException("Storage failed"))
                .when(imageService).storeImage(multipartFile, "Calcolatrice_1.png");

        assertThrows(RuntimeException.class, () -> {
            suggestionService.uploadSuggestionImage(className, order, multipartFile);
        });

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findByClassUT_NameAndOrder(className, order);

        // VERIFICA CHE NON SIA STATA INVOCATA LA DELETE
        verify(imageService, times(0)).deleteImage(anyString());

        // verifica che save NON sia stato chiamato
        verify(suggestionRepository, never()).save(any());
    }

    // TEST DELETE_SUGGESTION_IMAGE
    /**
     * Effettua un test del metodo {@link SuggestionService#deleteSuggestionImage} con
     * suggerimento e immagine presenti nel database.
     */
    @Test
    void testDeleteSuggestionImage_Success_WithImage() throws IOException {
        String className = "Calcolatrice";
        int order = 1;
        String imageName = "Calcolatrice_1.png";

        Suggestion suggestion = createBaseSuggestion(null);
        suggestion.setImage(imageName);  // ha un'immagine

        when(suggestionRepository.findByClassUT_NameAndOrder(className, order))
                .thenReturn(Optional.of(suggestion));

        suggestionService.deleteSuggestionImage(className, order);

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findByClassUT_NameAndOrder(className, order);

        // verifica che l'immagine sia stata cancellata
        verify(imageService, times(1)).deleteImage(imageName);

        // verifica che il suggestion sia stato salvato con image = null
        verify(suggestionRepository, times(1)).save(suggestionCaptor.capture());

        // verifica che sia stato invocato il save con un suggerimento uguale a suggestion ma con campo image null
        suggestion.setImage(null);
        suggestionTestUtils.assertEquals(suggestion, suggestionCaptor.getValue());
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#deleteSuggestionImage} con
     * suggerimento non presente nel database.
     */
    @Test
    void testDeleteSuggestionImage_NotFound() throws IOException {
        String className = "Calcolatrice";
        int invalidOrder = 99;

        when(suggestionRepository.findByClassUT_NameAndOrder(className, invalidOrder))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            suggestionService.deleteSuggestionImage(className, invalidOrder);
        });

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findByClassUT_NameAndOrder(className, invalidOrder);

        // Verifica che nessuna operazione sia stata fatta
        verify(imageService, never()).deleteImage(any());
        verify(suggestionRepository, never()).save(any());
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#deleteSuggestionImage} con
     * suggerimento e immagine presente nel database e errore durante la imageService.deleteImage.
     */
    @Test
    void testDeleteSuggestionImage_ErrorDeletingImage() throws IOException {
        String className = "Calcolatrice";
        int order = 1;
        String imageName = "Calcolatrice_1.png";

        Suggestion suggestion = createBaseSuggestion(null);
        suggestion.setImage(imageName); // immagine presente

        when(suggestionRepository.findByClassUT_NameAndOrder(className, order))
                .thenReturn(Optional.of(suggestion));

        // Simula un errore nella cancellazione
        doThrow(new IOException("File not found"))
                .when(imageService).deleteImage(imageName);

        assertThrows(RuntimeException.class, () -> {
            suggestionService.deleteSuggestionImage(className, order);
        });

        // VERIFICA CHIAMATE SUGGESTION REPOSITORY

        verify(suggestionRepository, times(1)).findByClassUT_NameAndOrder(className, order);

        // Verifica che save NON sia stato chiamato (a causa dell'errore)
        verify(suggestionRepository, never()).save(any());
    }

}