package com.groom.manvsclass.repository;

import com.groom.manvsclass.dto.SuggestionDTO;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Interaction;
import com.groom.manvsclass.model.Suggestion;
import com.groom.manvsclass.model.SuggestionLevel;
import com.groom.manvsclass.util.TestUtils;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@EntityScan(basePackages = "com.groom.manvsclass")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL", // Simula MySQL
        "spring.jpa.show-sql=true",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",

//        "spring.jpa.properties.hibernate.format_sql=true",
//        "logging.level.org.hibernate.SQL=DEBUG",
//        "logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE"
})
class SuggestionRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SuggestionRepository suggestionRepository;

    private ClassUT classUT;

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

    // TEST CREATE

    @Test
    void testSaveSuggestion() {

        Suggestion suggestion = createBaseSuggestion(this.classUT);
        Suggestion savedSuggestion = suggestionRepository.save(suggestion);

        entityManager.flush();  // forza la scrittura sul database
        entityManager.clear();  // svuota la cache per forzare la lettura dal database

        // per verificare il corretto inserimento nel database effettua una find
        Optional<Suggestion> suggestionOpt = suggestionRepository.findById(savedSuggestion.getId());
        assertThat(suggestionOpt).isPresent();

        Suggestion foundSuggestion = suggestionOpt.get();
        suggestionTestUtils.assertEquals(savedSuggestion, foundSuggestion);
    }

    @Test
    void testSaveAllSuggestions() {

        Suggestion firstSuggestion = createBaseSuggestion(this.classUT);

        Suggestion secondSuggestion = createBaseSuggestion(this.classUT);
        secondSuggestion.setOrder(2);

        List<Suggestion> suggestions = Arrays.asList(firstSuggestion, secondSuggestion);

        // salva i suggerimenti
        List<Suggestion> savedSuggestions = suggestionRepository.saveAll(suggestions);

        entityManager.flush();
        entityManager.clear();

        // verifica la presenza dei suggerimenti nel database, effettuando una findById per ogni suggerimento
        List<Suggestion> foundSuggestions = new ArrayList<>();
        for (Suggestion suggestion : savedSuggestions){
            Optional<Suggestion> suggestionOpt = suggestionRepository.findById(suggestion.getId());
            assertThat(suggestionOpt).isPresent();
            foundSuggestions.add(suggestionOpt.get());
        }

        // verifica che i suggerimenti ottenuti siano corretti
        suggestionTestUtils.assertListEquals(savedSuggestions, foundSuggestions);
    }

    // TEST UPDATE

    @Test
    void testUpdateSuggestion() {

        // salva un suggerimento
        Suggestion suggestion = createBaseSuggestion(this.classUT);
        Suggestion savedSuggestion = suggestionRepository.save(suggestion);

        entityManager.flush();

        // lo aggiorna
        savedSuggestion.setOrder(2);
        Suggestion updatedSuggestion = suggestionRepository.save(savedSuggestion);

        entityManager.flush();
        entityManager.clear();

        // lo rilegge per verificare l'aggiornamento corretto
        Optional<Suggestion> suggestionOpt = suggestionRepository.findById(updatedSuggestion.getId());
        assertThat(suggestionOpt).isPresent();

        Suggestion receivedSuggestion = suggestionOpt.get();
        suggestionTestUtils.assertEquals(updatedSuggestion, receivedSuggestion);
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

        Suggestion firstSuggestion = createBaseSuggestion(this.classUT);
        firstSuggestion.setOrder(1);
        Suggestion savedFirstSuggestion = suggestionRepository.save(firstSuggestion);

        Suggestion secondSuggestion = createBaseSuggestion(this.classUT);
        secondSuggestion.setOrder(2);
        Suggestion savedSecondSuggestion = suggestionRepository.save(secondSuggestion);

        ClassUT otherClassUT = new ClassUT();
        otherClassUT.setName("Classe_Diversa");
        otherClassUT.setDate(LocalDate.now());
        otherClassUT.setDescription("Descrizione_Diversa");
        otherClassUT.setDifficulty(OpponentDifficulty.HARD);
        otherClassUT.setUri("/URI/DIVERSO");

        entityManager.persist(otherClassUT);

        Suggestion thirdSuggestion = createBaseSuggestion(otherClassUT);
        thirdSuggestion.setOrder(1);
        suggestionRepository.save(thirdSuggestion);

        entityManager.flush();
        entityManager.clear();

        List<Suggestion> foundSuggestions = suggestionRepository.findAllByClassUT_Name(this.classUT.getName());

        List<Suggestion> expectedSuggestions = Arrays.asList(savedFirstSuggestion, savedSecondSuggestion);

        // verifica che i suggerimenti ottenuti siano corretti
        suggestionTestUtils.assertListEquals(expectedSuggestions, foundSuggestions);
    }

    @Test
    void testFindAllByClassUT_Name_ReturnsEmpty() {

        List<Suggestion> suggestions = suggestionRepository.findAllByClassUT_Name("Classe_Inesistente");
        assertThat(suggestions).isEmpty();
    }

    @Test
    void testFindByClassUT_NameAndOrder_Correct() {

        // salva un suggerimento associato a this.classUT
        Suggestion suggestion = createBaseSuggestion(this.classUT);
        Suggestion savedSuggestion = suggestionRepository.save(suggestion);

        entityManager.flush();
        entityManager.clear();

        // lo cerca per nome classe e order
        Optional<Suggestion> result = suggestionRepository.findByClassUT_NameAndOrder(this.classUT.getName(), savedSuggestion.getOrder());

        assertThat(result).isPresent();
        suggestionTestUtils.assertEquals(savedSuggestion, result.get());
    }

    @Test
    void testFindByClassUT_NameAndOrder_NotFound_OrderNotExists() {

        // salva un suggerimento associato a this.classUT
        Suggestion suggestion = createBaseSuggestion(this.classUT);
        suggestionRepository.save(suggestion);

        entityManager.flush();
        entityManager.clear();

        // cerca un suggerimento associato a this.classUT con un titolo di un suggerimento non esistente
        Optional<Suggestion> result = suggestionRepository.findByClassUT_NameAndOrder(this.classUT.getName(), 2);

        assertThat(result).isEmpty();
    }

//    @Test
//    void testFindByClassUT_NameAndOrder_NotFound_WrongOrder() {
//
//        // salva un suggerimento associato a this.classUT
//        Suggestion firstSuggestion = createBaseSuggestion(this.classUT);
//        Suggestion savedFirstSuggestion = suggestionRepository.save(firstSuggestion);
//
//        // salva un'altra classe UT
//        ClassUT otherClassUT = new ClassUT();
//        otherClassUT.setName("Classe_Diversa");
//        otherClassUT.setDate(LocalDate.now());
//        otherClassUT.setDescription("Descrizione_Diversa");
//        otherClassUT.setDifficulty(OpponentDifficulty.HARD);
//        otherClassUT.setUri("/URI/DIVERSO");
//
//        entityManager.persist(otherClassUT);
//
//        // salva un secondo suggerimento associato all'altra classe UT
//        Suggestion secondSuggestion = createBaseSuggestion(otherClassUT);
//        secondSuggestion.setOrder(1);
//        Suggestion savedSecondSuggestion = suggestionRepository.save(secondSuggestion);
//
//        entityManager.flush();
//        entityManager.clear();
//
//        // cerca ogni suggerimento con la classe sbagliata
//        Optional<Suggestion> result = suggestionRepository.findByClassUT_NameAndOrder(this.classUT.getName(), savedSecondSuggestion.getOrder());
//        assertThat(result).isEmpty();
//
//        Optional<Suggestion> result2 = suggestionRepository.findByClassUT_NameAndOrder(otherClassUT.getName(), savedFirstSuggestion.getOrder());
//        assertThat(result).isEmpty();
//
//    }

    @Test
    void testFindByClassUT_NameAndOrder_NotFound_WrongClass() {

        // salva un suggerimento associato a this.classUT
        Suggestion suggestion = createBaseSuggestion(this.classUT);
        Suggestion savedSuggestion = suggestionRepository.save(suggestion);

        entityManager.flush();
        entityManager.clear();

        // cerca il suggerimento con una classe non esistente
        Optional<Suggestion> result = suggestionRepository.findByClassUT_NameAndOrder("Classe_Non_Esistente", savedSuggestion.getOrder());

        assertThat(result).isEmpty();
    }

    // TEST ORDER

    void testInvalidOrder() {

        int invalidOrder = -1;

        Suggestion suggestion = createBaseSuggestion(this.classUT);
        suggestion.setOrder(invalidOrder);

        assertThrows(ConstraintViolationException.class, () -> {
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

        assertThrows(DataIntegrityViolationException.class, () -> {
            suggestionRepository.save(suggestion);
            entityManager.flush();
        });
    }

    // TEST IMAGE

    @Test void testSaveNullImage() {

        Suggestion suggestion = createBaseSuggestion(this.classUT);
        suggestion.setImage(null);

        Suggestion savedSuggestion = suggestionRepository.save(suggestion);
        entityManager.flush();
        assertThat(savedSuggestion.getImage()).isNull();
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
        suggestionRepository.save(suggestion);
        entityManager.flush();

        entityManager.refresh(this.classUT);

        entityManager.remove(this.classUT);
        entityManager.flush();

        assertThat(suggestionRepository.findById(suggestion.getId())).isEmpty();
    }

}