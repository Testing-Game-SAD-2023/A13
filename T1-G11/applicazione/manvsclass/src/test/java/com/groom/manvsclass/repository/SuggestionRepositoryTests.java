package com.groom.manvsclass.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.test.context.TestPropertySource;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Suggestion;
import com.groom.manvsclass.model.SuggestionLevel;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDate;

import org.springframework.dao.DataIntegrityViolationException;
import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@DataJpaTest
@EntityScan(basePackages = "com.groom.manvsclass")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL", // Simula MySQL
        "spring.jpa.show-sql=true",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class SuggestionRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SuggestionRepository suggestionRepository;

    private ClassUT classUT;

    // METODI UTILI

    private static boolean suggestionEquals(Suggestion s1, Suggestion s2) {
        if (s1 == s2) return true;
        if (s1 == null || s2 == null) return false;

        return s1.getId().equals(s2.getId()) &&
                s1.getTitle().equals(s2.getTitle()) &&
                s1.getHint().equals(s2.getHint()) &&
                s1.getDate().equals(s2.getDate()) &&
                s1.getBase64Image().equals(s2.getBase64Image()) &&
                s1.getLevel().equals(s2.getLevel()) &&
                s1.getClassUT().getName().equals(s2.getClassUT().getName());
    }

    private static void assertSuggestionEquals(Suggestion expected, Suggestion actual) {
        assertThat(suggestionEquals(expected, actual)).isTrue();
    }

    @BeforeEach
    void classUTSetup() {

        this.classUT = new ClassUT();
        classUT.setName("Calcolatrice");
        classUT.setDate(LocalDate.now());
        classUT.setDescription("Descrizione");
        classUT.setDifficulty(OpponentDifficulty.EASY);
        classUT.setUri("/URI");

        entityManager.persist(classUT);
    }

    /**
     * Crea un suggerimento associato alla classe {@code classUT}.
     */
    private static Suggestion createBaseSuggestion(ClassUT classUT) {

        Suggestion suggestion = new Suggestion();
        suggestion.setClassUT(classUT);
        suggestion.setTitle("Suggerimento");
        suggestion.setHint("Testo_Suggerimento");
        suggestion.setDate(LocalDate.now());
        suggestion.setLevel(SuggestionLevel.LOW);
        suggestion.setImage(new byte[]{1, 2, 3, 4, 5});

        return suggestion;
    }

    // TEST SAVE

    @Test
    void testSaveSuggestion() {

        Suggestion suggestion = createBaseSuggestion(this.classUT);
        Suggestion savedSuggestion = suggestionRepository.save(suggestion);

        entityManager.flush();  // forza la scrittura sul database
        entityManager.clear();  // svuota la cache per forzare la lettura dal database

        // lo rilegge per verificare il corretto inserimento nel database
        Optional<Suggestion> suggestionOpt = suggestionRepository.findById(savedSuggestion.getId());
        assertThat(suggestionOpt).isPresent();

        Suggestion receivedSuggestion = suggestionOpt.get();
        assertSuggestionEquals(savedSuggestion, receivedSuggestion);
    }

    @Test
    void testSaveSuggestion_DuplicatedTitle() {

        Suggestion firstSuggestion = createBaseSuggestion(this.classUT);
        Suggestion savedFirstSuggestion = suggestionRepository.save(firstSuggestion);

        entityManager.flush();

        Suggestion secondSuggestion = createBaseSuggestion(this.classUT);
        assertThrows(DataIntegrityViolationException.class, () -> {
            suggestionRepository.save(secondSuggestion);
            entityManager.flush();
        });

        entityManager.clear();

        // verifica la presenza del primo suggerimento
        Optional<Suggestion> suggestionOpt = suggestionRepository.findById(savedFirstSuggestion.getId());
        assertThat(suggestionOpt).isPresent();

        Suggestion receivedSuggestion = suggestionOpt.get();
        assertSuggestionEquals(savedFirstSuggestion, receivedSuggestion);
    }

    // TEST SAVE_ALL

    @Test
    void testSaveAllSuggestions() {

        Suggestion firstSuggestion = createBaseSuggestion(this.classUT);
        firstSuggestion.setTitle("Suggerimento_1");

        Suggestion secondSuggestion = createBaseSuggestion(this.classUT);
        secondSuggestion.setTitle("Suggerimento_2");

        List<Suggestion> suggestions = Arrays.asList(firstSuggestion, secondSuggestion);

        // salva i suggerimenti
        List<Suggestion> savedSuggestions = suggestionRepository.saveAll(suggestions);

        entityManager.flush();
        entityManager.clear();

        // verifica la presenza dei suggerimenti nel database, effettuando una findById per ogni suggerimento
        Stream<Suggestion> foundSuggestions =
                savedSuggestions
                        .stream()
                        .map(suggestion -> {
                            Optional<Suggestion> suggestionOpt = suggestionRepository.findById(suggestion.getId());
                            assertThat(suggestionOpt).isPresent();
                            return suggestionOpt.get();
                        });

        Comparator<Suggestion> suggestionComparator = (s1, s2) -> suggestionEquals(s1, s2) ? 0 : 1;

        assertThat(foundSuggestions)
                .hasSize(savedSuggestions.size())
                .usingElementComparator(suggestionComparator)
                .containsExactlyInAnyOrderElementsOf(savedSuggestions);
    }

    // TEST UPDATE

    @Test
    void testUpdateSuggestion() {

        // salva un suggerimento
        Suggestion suggestion = createBaseSuggestion(this.classUT);
        Suggestion savedSuggestion = suggestionRepository.save(suggestion);

        entityManager.flush();

        // lo aggiorna
        savedSuggestion.setTitle("Titolo Aggiornato");
        Suggestion updatedSuggestion = suggestionRepository.save(savedSuggestion);

        entityManager.flush();
        entityManager.clear();

        // lo rilegge per verificare l'aggiornamento corretto
        Optional<Suggestion> suggestionOpt = suggestionRepository.findById(updatedSuggestion.getId());
        assertThat(suggestionOpt).isPresent();

        Suggestion receivedSuggestion = suggestionOpt.get();
        assertSuggestionEquals(updatedSuggestion, receivedSuggestion);
    }

    // TEST DELETE

    @Test
    void testDeleteSuggestion() {

        // salva il suggerimento
        Suggestion suggestion = createBaseSuggestion(this.classUT);
        Suggestion savedSuggestion = suggestionRepository.save(suggestion);

        entityManager.flush();

        // lo cancella
        suggestionRepository.deleteById(savedSuggestion.getId());

        entityManager.flush();
        entityManager.clear();

        // verifica che non sia più presente
        assertThat(suggestionRepository.findById(savedSuggestion.getId())).isEmpty();
    }

    // TEST FIND

    @Test
    void testFindAllByClassUT_Name_ReturnsList() {

        // salva due suggerimenti associati a this.classUT
        Suggestion firstSuggestion = createBaseSuggestion(this.classUT);
        firstSuggestion.setTitle("Suggerimento_1");
        Suggestion savedFirstSuggestion = suggestionRepository.save(firstSuggestion);

        Suggestion secondSuggestion = createBaseSuggestion(this.classUT);
        secondSuggestion.setTitle("Suggerimento_2");
        Suggestion savedSecondSuggestion = suggestionRepository.save(secondSuggestion);

        // salva un'altra classe UT
        ClassUT otherClassUT = new ClassUT();
        otherClassUT.setName("Classe_Diversa");
        otherClassUT.setDate(LocalDate.now());
        otherClassUT.setDescription("Descrizione_Diversa");
        otherClassUT.setDifficulty(OpponentDifficulty.HARD);
        otherClassUT.setUri("/URI/DIVERSO");

        entityManager.persist(otherClassUT);

        // salva un terzo suggerimento associato all'altra classe UT
        Suggestion thirdSuggestion = createBaseSuggestion(otherClassUT);
        thirdSuggestion.setTitle("Suggerimento_Classe_Diversa");
        suggestionRepository.save(thirdSuggestion);

        entityManager.flush();
        entityManager.clear();

        List<Suggestion> results = suggestionRepository.findAllByClassUT_Name(this.classUT.getName());

        Comparator<Suggestion> suggestionComparator = (s1, s2) -> suggestionEquals(s1, s2) ? 0 : 1;

        // verifica che i suggerimenti ottenuti siano corretti
        assertThat(results)
                .hasSize(2)
                .usingElementComparator(suggestionComparator)
                .containsExactlyInAnyOrder(savedFirstSuggestion, savedSecondSuggestion);
    }

    @Test
    void testFindAllByClassUT_Name_ReturnsEmpty() {

        List<Suggestion> results = suggestionRepository.findAllByClassUT_Name("Classe_Inesistente");
        assertThat(results).isEmpty();
    }

    @Test
    void testFindByClassUT_NameAndTitle() {

        // salva un suggerimento associato a this.classUT
        Suggestion suggestion = createBaseSuggestion(this.classUT);
        Suggestion savedSuggestion = suggestionRepository.save(suggestion);

        entityManager.flush();
        entityManager.clear();

        // lo cerca per nome classe e titolo
        Optional<Suggestion> result = suggestionRepository.findByClassUT_NameAndTitle(this.classUT.getName(), savedSuggestion.getTitle());

        assertThat(result).isPresent();
        assertSuggestionEquals(savedSuggestion, result.get());
    }

    @Test
    void testFindByClassUT_NameAndTitle_NotFound_TitleNotExists() {

        // salva un suggerimento associato a this.classUT
        Suggestion suggestion = createBaseSuggestion(this.classUT);
        suggestionRepository.save(suggestion);

        entityManager.flush();
        entityManager.clear();

        // cerca un suggerimento associato a this.classUT con un titolo di un suggerimento non esistente
        Optional<Suggestion> result = suggestionRepository.findByClassUT_NameAndTitle(this.classUT.getName(), "Titolo_Inesistente");

        assertThat(result).isEmpty();
    }

    @Test
    void testFindByClassUT_NameAndTitle_NotFound_WrongTitle() {

        // salva un suggerimento associato a this.classUT
        Suggestion firstSuggestion = createBaseSuggestion(this.classUT);
        Suggestion savedFirstSuggestion = suggestionRepository.save(firstSuggestion);

        // salva un'altra classe UT
        ClassUT otherClassUT = new ClassUT();
        otherClassUT.setName("Classe_Diversa");
        otherClassUT.setDate(LocalDate.now());
        otherClassUT.setDescription("Descrizione_Diversa");
        otherClassUT.setDifficulty(OpponentDifficulty.HARD);
        otherClassUT.setUri("/URI/DIVERSO");

        entityManager.persist(otherClassUT);

        // salva un secondo suggerimento associato all'altra classe UT
        Suggestion secondSuggestion = createBaseSuggestion(otherClassUT);
        secondSuggestion.setTitle("Suggerimento_Classe_Diversa");
        Suggestion savedSecondSuggestion = suggestionRepository.save(secondSuggestion);

        entityManager.flush();
        entityManager.clear();

        // cerca ogni suggerimento con la classe sbagliata
        Optional<Suggestion> result = suggestionRepository.findByClassUT_NameAndTitle(this.classUT.getName(), savedSecondSuggestion.getTitle());
        assertThat(result).isEmpty();

        Optional<Suggestion> result2 = suggestionRepository.findByClassUT_NameAndTitle(otherClassUT.getName(), savedFirstSuggestion.getTitle());
        assertThat(result).isEmpty();

    }

    @Test
    void testFindByClassUT_NameAndTitle_NotFound_WrongClass() {

        // salva un suggerimento associato a this.classUT
        Suggestion suggestion = createBaseSuggestion(this.classUT);
        Suggestion savedSuggestion = suggestionRepository.save(suggestion);

        entityManager.flush();
        entityManager.clear();

        // cerca il suggerimento con una classe non esistente
        Optional<Suggestion> result = suggestionRepository.findByClassUT_NameAndTitle("Classe_Non_Esistente", savedSuggestion.getTitle());

        assertThat(result).isEmpty();
    }

    @Test
    void testExistsByClassUT_NameAndTitle_True() {

        // salva un suggerimento associato a this.classUT
        Suggestion suggestion = createBaseSuggestion(this.classUT);
        Suggestion savedSuggestion = suggestionRepository.save(suggestion);

        entityManager.flush();
        entityManager.clear();

        boolean exists = suggestionRepository.existsByClassUT_NameAndTitle(this.classUT.getName(), savedSuggestion.getTitle());

        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByClassUT_NameAndTitle_False() {

        // salva un suggerimento associato a this.classUT
        Suggestion suggestion = createBaseSuggestion(this.classUT);
        suggestionRepository.save(suggestion);

        entityManager.flush();
        entityManager.clear();

        boolean exists = suggestionRepository.existsByClassUT_NameAndTitle(this.classUT.getName(), "Titolo_Inesistente");

        assertThat(exists).isFalse();
    }

    // TEST TITLE

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void testInvalidTitle(String invalidTitle) {

        Suggestion suggestion = createBaseSuggestion(this.classUT);
        suggestion.setTitle(invalidTitle);

        assertThrows(ConstraintViolationException.class, () -> {
            suggestionRepository.save(suggestion);
            entityManager.flush();
        });
    }

    @Test
    void testTitleTooLong() {

        Suggestion suggestion = createBaseSuggestion(this.classUT);
        suggestion.setTitle("a".repeat(300));

        assertThrows(DataIntegrityViolationException.class, () -> {
            suggestionRepository.save(suggestion);
            entityManager.flush();
        });
    }

    // TEST HINT

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void testInvalidHint(String invalidHint) {

        Suggestion suggestion = createBaseSuggestion(this.classUT);
        suggestion.setHint(invalidHint);

        assertThrows(ConstraintViolationException.class, () -> {
            suggestionRepository.save(suggestion);
            entityManager.flush();
        });
    }

    @Test
    void testHintTooLong() {

        Suggestion suggestion = createBaseSuggestion(this.classUT);
        suggestion.setHint("a".repeat(300));

        assertThrows(DataIntegrityViolationException.class, () -> {
            suggestionRepository.save(suggestion);
            entityManager.flush();
        });
    }

    // TEST LEVEL

    @ParameterizedTest
    @EnumSource(SuggestionLevel.class)
    void testValidLevels(SuggestionLevel validLevel) {

        Suggestion suggestion = createBaseSuggestion(this.classUT);
        suggestion.setLevel(validLevel);

        Suggestion savedSuggestion = suggestionRepository.save(suggestion);
        entityManager.flush();

        assertThat(savedSuggestion.getLevel()).isEqualTo(validLevel);
    }

    @Test
    void testNullLevel() {

        Suggestion suggestion = createBaseSuggestion(this.classUT);
        suggestion.setLevel(null);

        assertThrows(ConstraintViolationException.class, () -> {
            suggestionRepository.save(suggestion);
            entityManager.flush();
        });
    }

    // TEST DATE

    @Test
    void testNullDate() {

        Suggestion suggestion = createBaseSuggestion(this.classUT);
        suggestion.setDate(null);

        assertThrows(ConstraintViolationException.class, () -> {
            suggestionRepository.save(suggestion);
            entityManager.flush();
        });
    }

    // TEST IMAGE

    @Test
    void testSaveNullImage() {

        Suggestion suggestion = createBaseSuggestion(this.classUT);
        suggestion.setImage(null);

        Suggestion savedSuggestion = suggestionRepository.save(suggestion);
        entityManager.flush();
        assertThat(savedSuggestion.getImage()).isNull();
    }

    @Test
    void testSaveLargeImage() {

        final int IMAGE_SIZE = 1024 * 1024;

        Suggestion suggestion = createBaseSuggestion(this.classUT);
        suggestion.setImage(new byte[IMAGE_SIZE]); // 1 MB

        Suggestion savedSuggestion = suggestionRepository.save(suggestion);
        entityManager.flush();
        assertThat(savedSuggestion.getImage().length).isEqualTo(IMAGE_SIZE);
    }

    // TEST FOREIGN KEY

    @Test
    void testNullClassUT() {

        Suggestion suggestion = createBaseSuggestion(this.classUT);
        suggestion.setClassUT(null);

        assertThrows(ConstraintViolationException.class, () -> {
            suggestionRepository.save(suggestion);
            entityManager.flush();
        });
    }

    @Test
    void testCascadeDelete() {

        Suggestion suggestion = createBaseSuggestion(this.classUT);
        Suggestion savedSuggestion = suggestionRepository.save(suggestion);
        entityManager.flush();

        entityManager.refresh(this.classUT);

        entityManager.remove(this.classUT);
        entityManager.flush();

        Optional<Suggestion> suggestionOpt = suggestionRepository.findById(savedSuggestion.getId());
        assertThat(suggestionOpt).isEmpty();
    }

}