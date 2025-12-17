package com.groom.manvsclass.service;

import com.groom.manvsclass.dto.GuidelineDTO;
import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.mapper.GuidelineMapper;
import com.groom.manvsclass.model.Guideline;
import com.groom.manvsclass.repository.GuidelineRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class GuidelineServiceTests {

    @Mock
    private GuidelineRepository guidelineRepository;

    @Mock
    private GuidelineMapper guidelineMapper;

    @Mock
    private ImageService imageService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private GuidelineService guidelineService;

    @Captor
    private ArgumentCaptor<List<Guideline>> guidelineListCaptor;

    @Captor
    private ArgumentCaptor<Guideline> guidelineCaptor;

    // configurazione di comparison per Guideline
    // configurazione di default: confronta tutti i parametri con equals
    public static final RecursiveComparisonConfiguration GUIDELINE_COMPARISON_CONFIG = new RecursiveComparisonConfiguration();

    // assert personalizzati
    private final TestUtils<Guideline> guidelineTestUtils = new TestUtils<>(GUIDELINE_COMPARISON_CONFIG);

    public static GuidelineDTO createBaseGuidelineDTO() {

        GuidelineDTO guidelineDTO = new GuidelineDTO();
        guidelineDTO.setOrder(1);
        guidelineDTO.setHint("Testo_Guideline");
        guidelineDTO.setImage(null);

        return guidelineDTO;
    }

    public static Guideline createBaseGuideline() {

        Guideline guideline = new Guideline();
        guideline.setOrder(1);
        guideline.setHint("Testo_Guideline");
        guideline.setDate(LocalDate.now());
        guideline.setImage(null);
        return guideline;
    }

    // TEST UPLOAD_GUIDELINES

    /**
     * Effettua un test del metodo {@link GuidelineService#uploadGuidelines} con
     * dati in input nel formato valido.
     */
    @Test
    public void uploadGuidelines_Correct() {

        // INPUT

        GuidelineDTO firstGuidelineDTO = createBaseGuidelineDTO();
        firstGuidelineDTO.setOrder(1);

        GuidelineDTO secondGuidelineDTO = createBaseGuidelineDTO();
        secondGuidelineDTO.setOrder(2);

        List<GuidelineDTO> guidelineDTOs = Arrays.asList(firstGuidelineDTO, secondGuidelineDTO);

        // OUTPUT MAPPER

        Guideline firstGuideline = createBaseGuideline();
        firstGuideline.setOrder(1);

        Guideline secondGuideline = createBaseGuideline();
        secondGuideline.setOrder(2);

        List<Guideline> guidelines = Arrays.asList(firstGuideline, secondGuideline);

        // MOCK MAPPER

        when(guidelineMapper.toEntityList(guidelineDTOs))
                .thenReturn(guidelines);

        // MOCK DEL GUIDELINE REPOSITORY

        when(guidelineRepository.findAllGuidelines())
                .thenReturn(List.of(firstGuideline));

        when(guidelineRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ESECUZIONE TEST

        assertDoesNotThrow(() -> guidelineService.uploadGuidelines(guidelineDTOs));

        // VERIFICA CHIAMATA MAPPER

        verify(guidelineMapper, times(1)).toEntityList(guidelineDTOs);

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findAllGuidelines();

        verify(guidelineRepository, times(1)).saveAll(guidelineListCaptor.capture());

        List<Guideline> savedGuidelines = guidelineListCaptor.getValue();

        // verifica che le linee guida passate a saveAll corrispondono effettivamente a quelle da salvare
        guidelineTestUtils.assertListEquals(guidelines, savedGuidelines);
    }

    /**
     * Effettua un test del metodo {@link GuidelineService#uploadGuidelines} con
     * lista dei suggerimenti vuota.
     */
    @Test
    public void uploadGuidelines_EmptyList() {

        // INPUT

        List<GuidelineDTO> guidelineDTOs = new ArrayList<>();

        // ESECUZIONE TEST

        assertDoesNotThrow(() -> guidelineService.uploadGuidelines(guidelineDTOs));

        // VERIFICA ASSENZA CHIAMATE MAPPER

        verifyNoInteractions(guidelineMapper);

        // VERIFICA ASSENZA CHIAMATE GUIDELINE REPOSITORY

        verifyNoInteractions(guidelineRepository);
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
        validGuidelineDTO.setOrder(1);

        GuidelineDTO invalidGuidelineDTO = createBaseGuidelineDTO();
        validGuidelineDTO.setOrder(-1);

        List<GuidelineDTO> guidelineDTOs = Arrays.asList(validGuidelineDTO, invalidGuidelineDTO);

        // OUTPUT MAPPER

        Guideline validGuideline = createBaseGuideline();
        validGuideline.setOrder(1);

        Guideline invalidGuideline = createBaseGuideline();
        invalidGuideline.setOrder(-1);

        List<Guideline> guidelines = Arrays.asList(validGuideline, invalidGuideline);

        // MOCK MAPPER

        when(guidelineMapper.toEntityList(guidelineDTOs))
                .thenReturn(guidelines);

        // MOCK GUIDELINE REPOSITORY

        when(guidelineRepository.findAllGuidelines())
                .thenReturn(List.of());

        when(guidelineRepository.saveAll(anyList()))
                .thenThrow(new DataIntegrityViolationException("Negative order"));

        // ESECUZIONE TEST

        assertThrows(DataIntegrityViolationException.class, () -> guidelineService.uploadGuidelines(guidelineDTOs));

        // VERIFICA CHIAMATA MAPPER

        verify(guidelineMapper, times(1)).toEntityList(guidelineDTOs);

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findAllGuidelines();

        verify(guidelineRepository, times(1)).saveAll(guidelineListCaptor.capture());

        List<Guideline> savedGuidelines = guidelineListCaptor.getValue();

        // verifica che le linee guida passate a saveAll corrispondono effettivamente a quelle da salvare
        guidelineTestUtils.assertListEquals(guidelines, savedGuidelines);
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

        when(guidelineRepository.findAllGuidelines())
                .thenReturn(List.of(firstGuideline));

        when(guidelineRepository.saveAll(anyList()))
                .thenThrow(new DataIntegrityViolationException("Same Order"));

        // ESECUZIONE TEST

        assertThrows(DataIntegrityViolationException.class, () -> guidelineService.uploadGuidelines(guidelineDTOs));

        // VERIFICA CHIAMATA MAPPER

        verify(guidelineMapper, times(1)).toEntityList(guidelineDTOs);

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findAllGuidelines();

        verify(guidelineRepository, times(1)).saveAll(guidelineListCaptor.capture());

        List<Guideline> savedGuidelines = guidelineListCaptor.getValue();

        // verifica che le linee guida passate a saveAll corrispondono effettivamente a quelle da salvare
        guidelineTestUtils.assertListEquals(guidelines, savedGuidelines);
    }

    // TEST FIND_GUIDELINES

    /**
     * Effettua un test del metodo {@link GuidelineService#findGuidelines} con
     * guideline presenti nel database.
     */
    @Test
    public void findGuidelines_Correct() {

        // OUTPUT GUIDELINE REPOSITORY

        Guideline guideline = createBaseGuideline();
        List<Guideline> guidelines = Arrays.asList(guideline);

        // MOCK GUIDELINE REPOSITORY

        when(guidelineRepository.findAllGuidelines())
                .thenReturn(guidelines);

        // OUTPUT MAPPER

        GuidelineDTO guidelineDTO = createBaseGuidelineDTO();
        List<GuidelineDTO> guidelineDTOs = Arrays.asList(guidelineDTO);

        // MOCK MAPPER

        when(guidelineMapper.toDtoList(guidelines))
                .thenReturn(guidelineDTOs);

        // ESECUZIONE TEST

        List<GuidelineDTO> testResults = guidelineService.findGuidelines();

        // VERIFICA OUTPUT

        assertEquals(1, testResults.size());

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

    // TEST DELETE_GUIDELINE

    /**
     * Effettua un test del metodo {@link GuidelineService#deleteGuideline} con
     * guideline esistente nel database.
     */
    @Test
    public void deleteGuideline_Correct() {

        // INPUT

        int guidelineOrder = 1;

        // OUTPUT GUIDELINE REPOSITORY

        Guideline guideline = createBaseGuideline();
        guideline.setOrder(guidelineOrder);

        // MOCK GUIDELINE REPOSITORY

        when(guidelineRepository.findByOrder(guidelineOrder))
                .thenReturn(Optional.of(guideline));

        // ESECUZIONE TEST

        assertDoesNotThrow(() -> guidelineService.deleteGuideline(guidelineOrder));

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findByOrder(guidelineOrder);

        verify(guidelineRepository, times(1)).delete(guideline);
    }

    /**
     * Effettua un test del metodo {@link GuidelineService#deleteGuideline} con
     * guideline non esistente nel database.
     */
    @Test
    public void deleteGuideline_GuidelineNotFound() {

        // INPUT

        int guidelineOrder = 2;

        // MOCK GUIDELINE REPOSITORY

        when(guidelineRepository.findByOrder(guidelineOrder))
                .thenReturn(Optional.empty());

        // ESECUZIONE TEST

        assertThrows(NotFoundException.class, () -> guidelineService.deleteGuideline(guidelineOrder));

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findByOrder(guidelineOrder);

        verify(guidelineRepository, times(0)).delete(any(Guideline.class));
    }

    // TEST UPLOAD_GUIDELINE_IMAGE

    /**
     * Effettua un test del metodo {@link GuidelineService#uploadGuidelineImage} con
     * dati in input nel formato valido e linea guida esistente nel database.
     */
    @Test
    public void uploadGuidelinesImage_Success_NoExistingImage() throws IOException {

        // INPUT

        String fileName = "immagine.png";

        // OUTPUT GUIDELINE REPOSITORY

        Guideline guideline = createBaseGuideline();
        guideline.setOrder(1);
        guideline.setImage(null);  // immagine non presente

        // MOCK DEL MULTIPART FILE

        when(multipartFile.getOriginalFilename()).thenReturn(fileName);

        // MOCK DEL GUIDELINE REPOSITORY

        when(guidelineRepository.findByOrder(guideline.getOrder()))
                .thenReturn(Optional.of(guideline));

        // ESECUZIONE TEST

        assertDoesNotThrow(() -> guidelineService.uploadGuidelineImage(guideline.getOrder(), multipartFile));

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findByOrder(guideline.getOrder());

        // VERIFICA CHE L'IMMAGINE SIA STATA SALVATA CON IL NOME CORRETTO

        verify(imageService).storeImage(multipartFile, "1.png");

        // CAPTURE DEGLI ARGOMENTI PASSATI A save (chiamato per fare l'upload del campo image)
        verify(guidelineRepository, times(1)).save(guidelineCaptor.capture());

        Guideline updatedGuideline = guidelineCaptor.getValue();

        // crea un nuovo guideline con i campi attesi
        // non utilizza "guideline" perché corrisponde allo STESSO oggetto updatedGuideline
        Guideline expectedGuideline = createBaseGuideline();
        expectedGuideline.setOrder(1);
        expectedGuideline.setImage("1.png");  // modifica immagine per effettuare il confronto

        guidelineTestUtils.assertEquals(expectedGuideline, updatedGuideline);
    }

    /**
     * Effettua un test del metodo {@link GuidelineService#uploadGuidelineImage} con
     * dati in input nel formato valido e suggerimento esistente nel database, con immagine già presente.
     */
    @Test
    public void uploadGuidelinesImage_Success_WithExistingImage() throws IOException {

        // INPUT

        String oldFileName = "1.jpg";
        String newFileName = "immagine.png";

        // OUTPUT GUIDELINE REPOSITORY

        Guideline guideline = createBaseGuideline();
        guideline.setOrder(1);
        guideline.setImage(oldFileName);  // immagine presente

        // MOCK DEL MULTIPART FILE

        when(multipartFile.getOriginalFilename()).thenReturn(newFileName);

        // MOCK DEL GUIDELINE REPOSITORY

        when(guidelineRepository.findByOrder(guideline.getOrder()))
                .thenReturn(Optional.of(guideline));

        // ESECUZIONE TEST

        assertDoesNotThrow(() -> guidelineService.uploadGuidelineImage(guideline.getOrder(), multipartFile));

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findByOrder(guideline.getOrder());

        // VERIFICA CHE L'IMMAGINE SIA STATA SALVATA CON IL NOME CORRETTO

        verify(imageService).storeImage(multipartFile, "1.png");

        // CAPTURE DEGLI ARGOMENTI PASSATI A save (chiamato per fare l'upload del campo image)
        verify(guidelineRepository, times(1)).save(guidelineCaptor.capture());

        Guideline updatedGuideline = guidelineCaptor.getValue();

        // crea un nuovo guideline con i campi attesi
        // non utilizza "guideline" perché corrisponde allo STESSO oggetto updatedGuideline
        Guideline expectedGuideline = createBaseGuideline();
        expectedGuideline.setOrder(1);
        expectedGuideline.setImage("1.png");  // modifica immagine per effettuare il confronto

        guidelineTestUtils.assertEquals(expectedGuideline, updatedGuideline);
    }

    /**
     * Effettua un test del metodo {@link GuidelineService#uploadGuidelineImage} con
     * dati in input nel formato valido e suggerimento non esistente nel database.
     */
    @Test
    public void uploadGuidelinesImage_GuidelineNotFound() throws IOException {

        // INPUT

        int invalidOrder = 99;

        // MOCK DEL GUIDELINE REPOSITORY

        when(guidelineRepository.findByOrder(invalidOrder))
                .thenReturn(Optional.empty());

        // ESECUZIONE TEST

        assertThrows(NotFoundException.class,
                () -> guidelineService.uploadGuidelineImage(invalidOrder, multipartFile)
        );

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findByOrder(invalidOrder);

        // VERIFICA NO INTERACTIONS CON imageService E guidelineRepository.save()
        verifyNoInteractions(imageService);
        verifyNoMoreInteractions(guidelineRepository);
    }

    /**
     * Effettua un test del metodo {@link GuidelineService#uploadGuidelineImage} con
     * dati in input nel formato valido e suggerimento esistente ma errore in imageService.deleteImage.
     */
    @Test
    void testUploadGuidelineImage_ErrorDeletingOldImage() throws IOException {
        int order = 1;
        String oldFileName = "old_image.jpg";
        String newFilename = "new_image.jpg";

        Guideline guideline = createBaseGuideline();
        guideline.setImage(oldFileName);

        when(guidelineRepository.findByOrder(order))
                .thenReturn(Optional.of(guideline));

        // simula un errore nella cancellazione
        doThrow(new IOException("Delete failed"))
                .when(imageService).deleteImage(oldFileName);

        assertThrows(RuntimeException.class, () -> {
            guidelineService.uploadGuidelineImage(order, multipartFile);
        });

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findByOrder(order);

        // VERIFICA CHE SIA STATA INVOCATA LA DELETE
        verify(imageService, times(1)).deleteImage(oldFileName);

        // verifica che storeImage e save NON siano stati chiamati
        verify(imageService, never()).storeImage(any(), any());
        verify(guidelineRepository, never()).save(any());
    }

    /**
     * Effettua un test del metodo {@link GuidelineService#uploadGuidelineImage} con
     * dati in input nel formato valido e suggerimento esistente ma errore in imageService.storeImage.
     */
    @Test
    void testUploadGuidelineImage_ErrorStoringImage() throws IOException {
        int order = 1;
        String fileName = "test.png";

        Guideline guideline = createBaseGuideline();
        guideline.setImage(null);

        when(multipartFile.getOriginalFilename()).thenReturn(fileName);
        when(guidelineRepository.findByOrder(order))
                .thenReturn(Optional.of(guideline));

        // simula un errore nel salvataggio
        doThrow(new IOException("Storage failed"))
                .when(imageService).storeImage(multipartFile, "1.png");

        assertThrows(RuntimeException.class, () -> {
            guidelineService.uploadGuidelineImage(order, multipartFile);
        });

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findByOrder(order);

        // VERIFICA CHE NON SIA STATA INVOCATA LA DELETE
        verify(imageService, times(0)).deleteImage(anyString());

        // verifica che save NON sia stato chiamato
        verify(guidelineRepository, never()).save(any());
    }

    // TEST DELETE_GUIDELINE_IMAGE
    /**
     * Effettua un test del metodo {@link GuidelineService#deleteGuidelineImage} con
     * suggerimento e immagine presenti nel database.
     */
    @Test
    void testDeleteGuidelineImage_Success_WithImage() throws IOException {
        int order = 1;
        String imageName = "Calcolatrice_1.png";

        Guideline guideline = createBaseGuideline();
        guideline.setImage(imageName);  // ha un'immagine

        when(guidelineRepository.findByOrder(order))
                .thenReturn(Optional.of(guideline));

        guidelineService.deleteGuidelineImage(order);

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findByOrder(order);

        // verifica che l'immagine sia stata cancellata
        verify(imageService, times(1)).deleteImage(imageName);

        // verifica che il guideline sia stato salvato con image = null
        verify(guidelineRepository, times(1)).save(guidelineCaptor.capture());

        // verifica che sia stato invocato il save con un suggerimento uguale a guideline ma con campo image null
        guideline.setImage(null);
        guidelineTestUtils.assertEquals(guideline, guidelineCaptor.getValue());
    }

    /**
     * Effettua un test del metodo {@link GuidelineService#deleteGuidelineImage} con
     * suggerimento non presente nel database.
     */
    @Test
    void testDeleteGuidelineImage_NotFound() throws IOException {
        String className = "Calcolatrice";
        int invalidOrder = 99;

        when(guidelineRepository.findByOrder(invalidOrder))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            guidelineService.deleteGuidelineImage(invalidOrder);
        });

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findByOrder(invalidOrder);

        // Verifica che nessuna operazione sia stata fatta
        verify(imageService, never()).deleteImage(any());
        verify(guidelineRepository, never()).save(any());
    }

    /**
     * Effettua un test del metodo {@link GuidelineService#deleteGuidelineImage} con
     * suggerimento e immagine presente nel database e errore durante la imageService.deleteImage.
     */
    @Test
    void testDeleteGuidelineImage_ErrorDeletingImage() throws IOException {
        String className = "Calcolatrice";
        int order = 1;
        String imageName = "Calcolatrice_1.png";

        Guideline guideline = createBaseGuideline();
        guideline.setImage(imageName); // immagine presente

        when(guidelineRepository.findByOrder(order))
                .thenReturn(Optional.of(guideline));

        // Simula un errore nella cancellazione
        doThrow(new IOException("File not found"))
                .when(imageService).deleteImage(imageName);

        assertThrows(RuntimeException.class, () -> {
            guidelineService.deleteGuidelineImage(order);
        });

        // VERIFICA CHIAMATE GUIDELINE REPOSITORY

        verify(guidelineRepository, times(1)).findByOrder(order);

        // Verifica che save NON sia stato chiamato (a causa dell'errore)
        verify(guidelineRepository, never()).save(any());
    }

}