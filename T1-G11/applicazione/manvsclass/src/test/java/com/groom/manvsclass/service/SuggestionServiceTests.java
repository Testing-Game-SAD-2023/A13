package com.groom.manvsclass.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.groom.manvsclass.dto.SuggestionDTO;
import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.mapper.SuggestionMapper;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Suggestion;
import com.groom.manvsclass.model.SuggestionLevel;
import com.groom.manvsclass.repository.ClassUTRepository;
import com.groom.manvsclass.repository.SuggestionRepository;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class SuggestionServiceTests {


    @Mock
    private ClassUTRepository classUTRepository;
    @Mock
    private SuggestionRepository suggestionRepository;

    // prende il Mapper reale (assume già testato). In alternativa bisogna
    // utilizzare @Mock e configurarlo ad ogni test con i risultati mappati attesi
    @Spy
    private SuggestionMapper suggestionMapper = Mappers.getMapper(SuggestionMapper.class);

    @InjectMocks
    private SuggestionService suggestionService;

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestions} con dati in input nel formato valido e classe esistente nel database.
     */
    @Test
    public void uploadSuggestions_Corretto() {
        // prepara gli input (simulando input del Controller)
        String className = "ClasseEsistente";
        SuggestionDTO sugg1 = new SuggestionDTO();
        sugg1.setTitle("Suggestion1");
        sugg1.setHint("Hint1");
        sugg1.setImage(null);
        sugg1.setLevel(SuggestionLevel.LOW);

        SuggestionDTO sugg2 = new SuggestionDTO();
        sugg2.setTitle("Suggestion2");
        sugg2.setHint("Hint2");
        sugg2.setImage(null);
        sugg2.setLevel(SuggestionLevel.MEDIUM);

        SuggestionDTO sugg3 = new SuggestionDTO();
        sugg3.setTitle("Suggestion3");
        sugg3.setHint("Hint3");
        sugg3.setImage(null);
        sugg3.setLevel(SuggestionLevel.HIGH);

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(sugg1, sugg2, sugg3);

        List<Suggestion> savedSuggestions = suggestionMapper.toEntityList(suggestionDTOs);

        // crea l'oggetto Model che restituisce il Mock di ClassUTRepository (simulando l'esistenza della classe nel DB)
        ClassUT mockClassUT = new ClassUT();
        mockClassUT.setName(className);
        mockClassUT.setSuggestions(new ArrayList<>());

        // Configura il comportamento dei repository mock relativi ai metodi invocati da uploadSuggestions

        // - classUTRepository.findById
        when(classUTRepository.findById(className))
                .thenReturn(Optional.of(mockClassUT));

        // - suggestionRepository.existsByClassUT_NameAndTitle
        when(suggestionRepository.existsByClassUT_NameAndTitle(eq(className), anyString()))
                .thenReturn(false);

        // - suggestionRepository.saveAll

        when(suggestionRepository.saveAll(anyList()))
                .thenReturn(savedSuggestions);

        // chiama il metodo del Service e verifica che non lanci eccezioni
        assertDoesNotThrow(() -> suggestionService.uploadSuggestions(className, suggestionDTOs));

        // Verifica che findById sia stato chiamato una volta con il nome corretto
        verify(classUTRepository, times(1)).findById(className);

        // Verifica che existsByClassUT_NameAndTitle sia stato chiamato almeno per ogni titolo della lista
        for (SuggestionDTO dto : suggestionDTOs) {
            verify(suggestionRepository, times(1))
                    .existsByClassUT_NameAndTitle(className, dto.getTitle());
        }

        // Verifica che saveAll sia stato chiamato una volta con una lista di suggerimenti
        verify(suggestionRepository, times(1)).saveAll(anyList());
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestions} con dati in input con formato invalido (titolo null).
     */
    @Test
    public void uploadSuggestions_SenzaTitolo() {
        // Prepara gli input (simulando input del Controller)
        String className = "ClasseEsistente";
        SuggestionDTO sugg1 = new SuggestionDTO();
        sugg1.setHint("Hint1");
        sugg1.setImage(null);
        sugg1.setLevel(SuggestionLevel.LOW);

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(sugg1);

        // crea l'oggetto Model che restituisce il Mock di ClassUTRepository (simulando l'esistenza della classe nel DB)
        ClassUT mockClassUT = new ClassUT();
        mockClassUT.setName(className);
        mockClassUT.setSuggestions(new ArrayList<>());

        // Configura il comportamento dei repository mock relativi ai metodi invocati da uploadSuggestions

        // - classUTRepository.findById
        when(classUTRepository.findById(className))
                .thenReturn(Optional.of(mockClassUT));

        // chiama il metodo del Service e verifica che lanci l'eccezione attesa
        assertThrows(NullPointerException.class, () -> suggestionService.uploadSuggestions(className, suggestionDTOs));

        // Verifica che saveAll non sia mai stato chiamato
        verify(suggestionRepository, times(0)).saveAll(anyList());
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestions} con dati in input con formato invalido (hint null).
     */
    @Test
    public void uploadSuggestions_SenzaHint() {
        // Prepara gli input (simulando input del Controller)
        String className = "ClasseEsistente";
        SuggestionDTO sugg1 = new SuggestionDTO();
        sugg1.setTitle("Suggestion1");
        sugg1.setImage(null);
        sugg1.setLevel(SuggestionLevel.LOW);

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(sugg1);

        // crea l'oggetto Model che restituisce il Mock di ClassUTRepository (simulando l'esistenza della classe nel DB)
        ClassUT mockClassUT = new ClassUT();
        mockClassUT.setName(className);
        mockClassUT.setSuggestions(new ArrayList<>());

        // Configura il comportamento dei repository mock relativi ai metodi invocati da uploadSuggestions

        // - classUTRepository.findById
        when(classUTRepository.findById(className))
                .thenReturn(Optional.of(mockClassUT));

        // chiama il metodo del Service e verifica che lanci l'eccezione attesa
        assertThrows(NullPointerException.class, () -> suggestionService.uploadSuggestions(className, suggestionDTOs));

        // Verifica che saveAll non sia mai stato chiamato
        verify(suggestionRepository, times(0)).saveAll(anyList());
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestions} con dati in input con formato invalido (level null).
     */
    @Test
    public void uploadSuggestions_SenzaLevel() {
        // Prepara gli input (simulando input del Controller)
        String className = "ClasseEsistente";
        SuggestionDTO sugg1 = new SuggestionDTO();
        sugg1.setTitle("Suggestion1");
        sugg1.setHint("Hint1");
        sugg1.setImage(null);

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(sugg1);

        // crea l'oggetto Model che restituisce il Mock di ClassUTRepository (simulando l'esistenza della classe nel DB)
        ClassUT mockClassUT = new ClassUT();
        mockClassUT.setName(className);
        mockClassUT.setSuggestions(new ArrayList<>());

        // Configura il comportamento dei repository mock relativi ai metodi invocati da uploadSuggestions

        // - classUTRepository.findById
        when(classUTRepository.findById(className))
                .thenReturn(Optional.of(mockClassUT));

        // chiama il metodo del Service e verifica che lanci l'eccezione attesa
        assertThrows(NullPointerException.class, () -> suggestionService.uploadSuggestions(className, suggestionDTOs));

        // Verifica che saveAll non sia mai stato chiamato
        verify(suggestionRepository, times(0)).saveAll(anyList());
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestions} con dati in input nel formato valido e classe inesistente nel database.
     */
    @Test
    public void uploadSuggestions_ClasseInesistente() {
        // prepara gli input (simulando input del Controller)
        String className = "ClasseInesistente";
        SuggestionDTO sugg1 = new SuggestionDTO();
        sugg1.setTitle("Suggestion1");
        sugg1.setHint("Hint1");
        sugg1.setImage(null);
        sugg1.setLevel(SuggestionLevel.LOW);

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(sugg1);

        // Configura il comportamento dei repository mock relativi ai metodi invocati da uploadSuggestions

        // - classUTRepository.findById -> simula l'assenza della classe nel DB
        when(classUTRepository.findById(className))
                .thenReturn(Optional.empty());

        // chiama il metodo del Service e verifica che lanci l'eccezione attesa
        assertThrows(NotFoundException.class, () -> suggestionService.uploadSuggestions(className, suggestionDTOs));

        // Verifica che findById sia stato chiamato una volta con il nome corretto
        verify(classUTRepository, times(1)).findById(className);

        // Verifica che saveAll non sia mai stato chiamato
        verify(suggestionRepository, times(0)).saveAll(anyList());
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestions} con lista dei suggerimenti vuota.
     */
    @Test
    public void uploadSuggestions_NessunSuggerimento() {
        // prepara gli input (simulando input del Controller)
        String className = "ClasseEsistente";

        List<SuggestionDTO> suggestionDTOs = new ArrayList<>();

        // crea l'oggetto Model che restituisce il Mock di ClassUTRepository (simulando l'esistenza della classe nel DB)
        ClassUT mockClassUT = new ClassUT();
        mockClassUT.setName(className);
        mockClassUT.setSuggestions(new ArrayList<>());

        // Configura il comportamento dei repository mock relativi ai metodi invocati da uploadSuggestions

        // - classUTRepository.findById
        when(classUTRepository.findById(className))
                .thenReturn(Optional.of(mockClassUT));

        // chiama il metodo del Service e verifica che non modifichi il database
        suggestionService.uploadSuggestions(className, suggestionDTOs);

        // Verifica che findById sia stato chiamato una volta con il nome corretto
        verify(classUTRepository, times(1)).findById(className);

        // Verifica che saveAll non sia mai stato chiamato
        verify(suggestionRepository, times(0)).saveAll(anyList());
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestions} con un suggerimento valido e un altro no.
     * Verifica che nessuno dei due sia inserito.
     */
    @Test
    public void uploadSuggestions_Atomicita() {
        // prepara gli input (simulando input del Controller)
        String className = "ClasseEsistente";
        SuggestionDTO suggValido = new SuggestionDTO();
        suggValido.setTitle("Suggestion1");
        suggValido.setHint("Hint1");
        suggValido.setImage(null);
        suggValido.setLevel(SuggestionLevel.LOW);

        SuggestionDTO suggInvalido = new SuggestionDTO();
        suggInvalido.setTitle(null);
        suggInvalido.setHint("Hint1");
        suggInvalido.setImage(null);
        suggInvalido.setLevel(SuggestionLevel.LOW);

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(suggValido, suggInvalido);

        // crea l'oggetto Model che restituisce il Mock di ClassUTRepository (simulando l'esistenza della classe nel DB)
        ClassUT mockClassUT = new ClassUT();
        mockClassUT.setName(className);
        mockClassUT.setSuggestions(new ArrayList<>());

        // Configura il comportamento dei repository mock relativi ai metodi invocati da uploadSuggestions

        // - classUTRepository.findById
        when(classUTRepository.findById(className))
                .thenReturn(Optional.of(mockClassUT));

        // - suggestionRepository.existsByClassUT_NameAndTitle
        when(suggestionRepository.existsByClassUT_NameAndTitle(eq(className), anyString()))
                .thenReturn(false);

        // chiama il metodo del Service e verifica che lanci l'eccezione attesa
        assertThrows(NullPointerException.class, () -> suggestionService.uploadSuggestions(className, suggestionDTOs));

        // Verifica che findById sia stato chiamato una volta con il nome corretto
        verify(classUTRepository, times(1)).findById(className);

        // Verifica che saveAll non sia mai stato chiamato
        verify(suggestionRepository, times(0)).saveAll(anyList());
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#uploadSuggestions} con due suggerimenti identici.
     * Verifica che nessuno dei due sia inserito.
     */
    @Test
    public void uploadSuggestions_SuggerimentiIdentici() {
        // prepara gli input (simulando input del Controller)
        String className = "ClasseEsistente";
        SuggestionDTO sugg1 = new SuggestionDTO();
        sugg1.setTitle("Suggestion1");
        sugg1.setHint("Hint1");
        sugg1.setImage(null);
        sugg1.setLevel(SuggestionLevel.LOW);

        SuggestionDTO sugg2 = new SuggestionDTO();
        sugg2.setTitle("Suggestion2");
        sugg2.setHint("Hint1");
        sugg2.setImage(null);
        sugg2.setLevel(SuggestionLevel.LOW);

        List<SuggestionDTO> suggestionDTOs = Arrays.asList(sugg1, sugg2);

        List<Suggestion> savedSuggestions = suggestionMapper.toEntityList(suggestionDTOs);

        // crea l'oggetto Model che restituisce il Mock di ClassUTRepository (simulando l'esistenza della classe nel DB)
        ClassUT mockClassUT = new ClassUT();
        mockClassUT.setName(className);
        mockClassUT.setSuggestions(new ArrayList<>());

        // Configura il comportamento dei repository mock relativi ai metodi invocati da uploadSuggestions

        // - classUTRepository.findById
        when(classUTRepository.findById(className))
                .thenReturn(Optional.of(mockClassUT));

        // - suggestionRepository.existsByClassUT_NameAndTitle
        when(suggestionRepository.existsByClassUT_NameAndTitle(eq(className), anyString()))
                .thenReturn(false);

        // chiama il metodo del Service e verifica che lanci l'eccezione attesa
        assertThrows(IllegalArgumentException.class, () -> suggestionService.uploadSuggestions(className, suggestionDTOs));

        // Verifica che findById sia stato chiamato una volta con il nome corretto
        verify(classUTRepository, times(1)).findById(className);

        // Verifica che saveAll non sia mai stato chiamato
        verify(suggestionRepository, times(0)).saveAll(anyList());
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#findSuggestions} con dati in input nel formato valido, classe esistente nel database e suggerimenti presenti.
     */
    @Test
    public void findSuggestions_SuggerimentiPresenti() {
        // prepara gli input (simulando input del Controller)
        String className = "ClasseEsistente";
        Suggestion sugg1 = new Suggestion();
        sugg1.setTitle("Suggestion1");
        sugg1.setHint("Hint1");
        sugg1.setImage(null);
        sugg1.setLevel(SuggestionLevel.LOW);

        Suggestion sugg2 = new Suggestion();
        sugg2.setTitle("Suggestion2");
        sugg2.setHint("Hint2");
        sugg2.setImage(null);
        sugg2.setLevel(SuggestionLevel.MEDIUM);

        Suggestion sugg3 = new Suggestion();
        sugg3.setTitle("Suggestion3");
        sugg3.setHint("Hint3");
        sugg3.setImage(null);
        sugg3.setLevel(SuggestionLevel.HIGH);

        List<Suggestion> returnedSuggestions = Arrays.asList(sugg1, sugg2, sugg3);
        List<SuggestionDTO> expectedOutput = suggestionMapper.toDtoList(returnedSuggestions);

        // Configura il comportamento dei repository mock relativi ai metodi invocati da uploadSuggestions

        // - suggestionRepository.findAllByClassUT_Name
        when(suggestionRepository.findAllByClassUT_Name(className))
                .thenReturn(returnedSuggestions);

        // chiama il metodo del Service e verifica che non lanci eccezioni
        List<SuggestionDTO> actualOutput = assertDoesNotThrow(() -> suggestionService.findSuggestions(className));

        // verifica output atteso (NOTA: in questo caso si assume che anche l'ordine sia uguale, si potrebbe rilassare la condizione)
        assertEquals(expectedOutput.size(), actualOutput.size());
        for (int i = 0; i < actualOutput.size(); i++) {
            SuggestionDTO expected = expectedOutput.get(i);
            SuggestionDTO actual = actualOutput.get(i);
            assertEquals(expected, actual);
        }

        // Verifica che findAllByClassUT_Name sia stato chiamato una volta con il nome corretto
        verify(suggestionRepository, times(1)).findAllByClassUT_Name(className);
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#findSuggestions} con dati in input nel formato valido, classe esistente nel database e suggerimenti assenti.
     */
    @Test
    public void findSuggestions_SuggerimentiAssenti() {
        // prepara gli input (simulando input del Controller)
        String className = "ClasseSenzaSuggerimenti";
        List<Suggestion> returnedSuggestions = new ArrayList<>();

        // Configura il comportamento dei repository mock relativi ai metodi invocati da uploadSuggestions

        // - suggestionRepository.findAllByClassUT_Name
        when(suggestionRepository.findAllByClassUT_Name(className))
                .thenReturn(returnedSuggestions);

        // chiama il metodo del Service e verifica che non lanci eccezioni
        List<SuggestionDTO> output = assertDoesNotThrow(() -> suggestionService.findSuggestions(className));

        // verifica output vuoto
        assertEquals(true, output.isEmpty());
        // Verifica che findAllByClassUT_Name sia stato chiamato una volta con il nome corretto
        verify(suggestionRepository, times(1)).findAllByClassUT_Name(className);
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#findSuggestions} con dati in input nel formato valido e classe inesistente nel database.
     */
    @Test
    public void findSuggestions_ClasseInesistente() {
        // prepara gli input (simulando input del Controller)
        String className = "ClasseInesistente";
        List<Suggestion> returnedSuggestions = new ArrayList<>();

        // Configura il comportamento dei repository mock relativi ai metodi invocati da uploadSuggestions

        // - suggestionRepository.findAllByClassUT_Name
        when(suggestionRepository.findAllByClassUT_Name(className))
                .thenReturn(returnedSuggestions);

        // chiama il metodo del Service e verifica che non lanci eccezioni
        List<SuggestionDTO> output = assertDoesNotThrow(() -> suggestionService.findSuggestions(className));

        // verifica output vuoto
        assertEquals(true, output.isEmpty());
        // Verifica che findAllByClassUT_Name sia stato chiamato una volta con il nome corretto
        verify(suggestionRepository, times(1)).findAllByClassUT_Name(className);
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#deleteSuggestion} con dati in input nel formato valido e classe e suggerimento associato esistente nel database.
     */
    @Test
    public void deleteSuggestion_Corretto() {
        // prepara gli input (simulando input del Controller)
        String className = "ClasseEsistente";
        String suggestionTitle = "SuggerimentoEsistente";
        Suggestion suggestion = new Suggestion();
        suggestion.setTitle(suggestionTitle);
        suggestion.setHint("Hint1");
        suggestion.setImage(null);
        suggestion.setLevel(SuggestionLevel.LOW);

        // Configura il comportamento dei repository mock relativi ai metodi invocati da uploadSuggestions

        // - classUTRepository.existsById
        when(classUTRepository.existsById(className))
                .thenReturn(true);

        // - suggestionRepository.findByClassUT_NameAndTitle
        when(suggestionRepository.findByClassUT_NameAndTitle(className, suggestionTitle))
                .thenReturn(Optional.of(suggestion));

        // chiama il metodo del Service e verifica che non lanci eccezioni
        assertDoesNotThrow(() -> suggestionService.deleteSuggestion(className, suggestionTitle));

        // Verifica che existsById sia stato chiamato una volta con il nome corretto
        verify(classUTRepository, times(1)).existsById(className);

        // Verifica che findByClassUT_NameAndTitle sia stato chiamato una volta con nome classe e titolo suggerimento corretto
        verify(suggestionRepository, times(1)).findByClassUT_NameAndTitle(className, suggestionTitle);

        // Verifica che delete sia stato chiamato una volta con il suggerimento corretto
        verify(suggestionRepository, times(1)).delete(suggestion);
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#deleteSuggestion} con dati in input nel formato valido e classe inesistente nel database.
     */
    @Test
    public void deleteSuggestion_ClasseInesistente() {
        // prepara gli input (simulando input del Controller)
        String className = "ClasseInesistente";
        String suggestionTitle = "Suggerimento";
        Suggestion suggestion = new Suggestion();
        suggestion.setTitle(suggestionTitle);
        suggestion.setHint("Hint1");
        suggestion.setImage(null);
        suggestion.setLevel(SuggestionLevel.LOW);

        // Configura il comportamento dei repository mock relativi ai metodi invocati da uploadSuggestions

        // - classUTRepository.existsById
        when(classUTRepository.existsById(className))
                .thenReturn(false);

        // chiama il metodo del Service e verifica che lanci l'eccezione attesa
        assertThrows(NotFoundException.class, () -> suggestionService.deleteSuggestion(className, suggestionTitle));

        // Verifica che existsById sia stato chiamato una volta con il nome corretto
        verify(classUTRepository, times(1)).existsById(className);

        // Verifica che delete non sia mai stato chiamato
        verify(suggestionRepository, times(0)).delete(any(Suggestion.class));
    }

    /**
     * Effettua un test del metodo {@link SuggestionService#deleteSuggestion} con dati in input nel formato valido e suggerimento inesistente nel database.
     */
    @Test
    public void deleteSuggestion_SuggerimentoInesistente() {
        // prepara gli input (simulando input del Controller)
        String className = "ClasseEsistente";
        String suggestionTitle = "SuggerimentoInesistente";

        // Configura il comportamento dei repository mock relativi ai metodi invocati da uploadSuggestions

        // - classUTRepository.existsById
        when(classUTRepository.existsById(className))
                .thenReturn(true);

        // - suggestionRepository.findByClassUT_NameAndTitle
        when(suggestionRepository.findByClassUT_NameAndTitle(className, suggestionTitle))
                .thenReturn(Optional.empty());

        // chiama il metodo del Service e verifica che lanci l'eccezione attesa
        assertThrows(NotFoundException.class, () -> suggestionService.deleteSuggestion(className, suggestionTitle));

        // Verifica che existsById sia stato chiamato una volta con il nome corretto
        verify(classUTRepository, times(1)).existsById(className);

        // Verifica che findByClassUT_NameAndTitle sia stato chiamato una volta con nome classe e titolo suggerimento corretto
        verify(suggestionRepository, times(1)).findByClassUT_NameAndTitle(className, suggestionTitle);

        // Verifica che delete non sia mai stato chiamato
        verify(suggestionRepository, times(0)).delete(any(Suggestion.class));
    }


}

