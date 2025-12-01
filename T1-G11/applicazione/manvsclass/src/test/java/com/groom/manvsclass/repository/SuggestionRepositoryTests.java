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
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;

import java.util.Optional;

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

    private Suggestion createBaseSuggestion() {

        Suggestion suggestion = new Suggestion();
        suggestion.setClassUT(this.classUT);
        suggestion.setTitle("Suggerimento");
        suggestion.setHint("Testo_Suggerimento");
        suggestion.setDate(LocalDate.now());
        suggestion.setLevel(SuggestionLevel.LOW);
        suggestion.setImage(new byte[] {1,2,3,4,5});
        return suggestion;
    }

    // TEST CREATE

    @Test
    void testCreateSuggestion() {

        Suggestion suggestion = createBaseSuggestion();
        Suggestion savedSuggestion = suggestionRepository.save(suggestion);

        assertThat(savedSuggestion).isNotNull();
        assertThat(savedSuggestion.getId()).isNotNull();
        assertThat(savedSuggestion.getClassUT().getName()).isEqualTo("Calcolatrice");
        assertThat(savedSuggestion.getTitle()).isEqualTo("Suggerimento");
        assertThat(savedSuggestion.getHint()).isEqualTo("Testo_Suggerimento");
        assertThat(savedSuggestion.getLevel()).isEqualTo(SuggestionLevel.LOW);
        assertThat(savedSuggestion.getImage()).isEqualTo(new byte[]{1,2,3,4,5});
    }

    // TEST UPDATE

    @Test
    void testUpdateSuggestion() {

        Suggestion suggestion = createBaseSuggestion();
        suggestionRepository.save(suggestion);
        entityManager.flush();

        suggestion.setTitle("Titolo Aggiornato");
        suggestionRepository.save(suggestion);
        entityManager.flush();
        entityManager.clear();

        Optional<Suggestion> suggestionOpt = suggestionRepository.findById(suggestion.getId());
        if(suggestionOpt.isEmpty()) {
            throw new RuntimeException("Not Found");
        }

        Suggestion updatedSuggestion = suggestionOpt.get();
        assertThat(updatedSuggestion.getTitle()).isEqualTo("Titolo Aggiornato");
    }

    // TEST DELETE

    @Test
    void testDeleteSuggestion() {

        Suggestion suggestion = createBaseSuggestion();
        Suggestion savedSuggestion = suggestionRepository.save(suggestion);
        entityManager.flush();

        suggestionRepository.deleteById(savedSuggestion.getId());
        entityManager.flush();

        assertThat(suggestionRepository.findById(savedSuggestion.getId())).isEmpty();
    }

    // TEST FIND

    @Test
    void testFindAllByClassUT_Name_ReturnsList() {

        Suggestion firstSuggestion = createBaseSuggestion();
        firstSuggestion.setTitle("Suggerimento 1");
        suggestionRepository.save(firstSuggestion);

        Suggestion secondSuggestion = createBaseSuggestion();
        secondSuggestion.setTitle("Suggerimento 2");
        suggestionRepository.save(secondSuggestion);

        ClassUT otherClassUT = new ClassUT();
        otherClassUT.setName("Classe_Diversa");
        otherClassUT.setDate(LocalDate.now());
        otherClassUT.setDescription("Descrizione_Diversa");
        otherClassUT.setDifficulty(OpponentDifficulty.HARD);
        otherClassUT.setUri("/URI/DIVERSO");

        entityManager.persist(otherClassUT);

        Suggestion thirdSuggestion = createBaseSuggestion();
        thirdSuggestion.setClassUT(otherClassUT);
        thirdSuggestion.setTitle("Suggerimento_Classe_Diversa");
        suggestionRepository.save(thirdSuggestion);

        entityManager.flush();

        var results = suggestionRepository.findAllByClassUT_Name("Calcolatrice");

        assertThat(results).hasSize(2);
        assertThat(results).extracting(Suggestion::getTitle)
                .containsExactlyInAnyOrder("Suggerimento 1", "Suggerimento 2");
    }

    @Test
    void testFindAllByClassUT_Name_ReturnsEmpty() {

        var results = suggestionRepository.findAllByClassUT_Name("Classe_Inesistente");
        assertThat(results).isEmpty();
    }

    @Test
    void testFindByClassUT_NameAndTitle() {

        suggestionRepository.save(createBaseSuggestion());
        entityManager.flush();

        var result = suggestionRepository.findByClassUT_NameAndTitle("Calcolatrice", "Suggerimento");

        assertThat(result).isPresent();
        assertThat(result.get().getClassUT().getName()).isEqualTo("Calcolatrice");
        assertThat(result.get().getTitle()).isEqualTo("Suggerimento");
    }

    @Test
    void testFindByClassUT_NameAndTitle_NotFound_WrongTitle() {

        suggestionRepository.save(createBaseSuggestion());
        entityManager.flush();

        var result = suggestionRepository.findByClassUT_NameAndTitle("Calcolatrice", "Titolo_Inesistente");

        assertThat(result).isEmpty();
    }

    @Test
    void testFindByClassUT_NameAndTitle_NotFound_WrongClass() {

        suggestionRepository.save(createBaseSuggestion());
        entityManager.flush();

        var result = suggestionRepository.findByClassUT_NameAndTitle("Classe_Inesistente", "Suggerimento");

        assertThat(result).isEmpty();
    }

    @Test
    void testExistsByClassUT_NameAndTitle_True() {

        suggestionRepository.save(createBaseSuggestion());
        entityManager.flush();

        boolean exists = suggestionRepository.existsByClassUT_NameAndTitle("Calcolatrice", "Suggerimento");

        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByClassUT_NameAndTitle_False() {

        suggestionRepository.save(createBaseSuggestion());
        entityManager.flush();

        boolean exists = suggestionRepository.existsByClassUT_NameAndTitle("Calcolatrice", "Titolo_Inesistente");

        assertThat(exists).isFalse();
    }

    // TEST TITLE

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void testInvalidTitle(String invalidTitle) {

        Suggestion suggestion = createBaseSuggestion();
        suggestion.setTitle(invalidTitle);

        assertThrows(ConstraintViolationException.class, () -> {
            suggestionRepository.save(suggestion);
            entityManager.flush();
        });
    }

    @Test
    void testTitleTooLong() {

        Suggestion suggestion = createBaseSuggestion();
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

        Suggestion suggestion = createBaseSuggestion();
        suggestion.setHint(invalidHint);

        assertThrows(ConstraintViolationException.class, () -> {
            suggestionRepository.save(suggestion);
            entityManager.flush();
        });
    }

    @Test
    void testHintTooLong() {

        Suggestion suggestion = createBaseSuggestion();
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

        Suggestion suggestion = createBaseSuggestion();
        suggestion.setLevel(validLevel);

        Suggestion savedSuggestion = suggestionRepository.save(suggestion);
        entityManager.flush();

        assertThat(savedSuggestion.getLevel()).isEqualTo(validLevel);
    }

    @Test
    void testNullLevel() {

        Suggestion suggestion = createBaseSuggestion();
        suggestion.setLevel(null);

        assertThrows(ConstraintViolationException.class, () -> {
            suggestionRepository.save(suggestion);
            entityManager.flush();
        });
    }

    // TEST DATE

    @Test
    void testNullDate() {

        Suggestion suggestion = createBaseSuggestion();
        suggestion.setDate(null);

        assertThrows(ConstraintViolationException.class, () -> {
            suggestionRepository.save(suggestion);
            entityManager.flush();
        });
    }

    // TEST IMAGE

    @Test void testSaveNullImage() {

        Suggestion suggestion = createBaseSuggestion();
        suggestion.setImage(null);

        Suggestion savedSuggestion = suggestionRepository.save(suggestion);
        entityManager.flush();
        assertThat(savedSuggestion.getImage()).isNull();
    }

    @Test void testSaveLargeImage() {

        Suggestion suggestion = createBaseSuggestion();
        suggestion.setImage(new byte[1024*1024]); // 1 MB

        Suggestion savedSuggestion = suggestionRepository.save(suggestion);
        entityManager.flush();
        assertThat(savedSuggestion.getImage().length).isEqualTo(1024*1024);
    }

    // TEST FOREIGN KEY

    @Test
    void testNullClassUT() {

        Suggestion suggestion = createBaseSuggestion();
        suggestion.setClassUT(null);

        assertThrows(ConstraintViolationException.class, () -> {
            suggestionRepository.save(suggestion);
            entityManager.flush();
        });
    }

    @Test
    void testCascadeDelete() {

        Suggestion suggestion = createBaseSuggestion();
        suggestionRepository.save(suggestion);
        entityManager.flush();

        entityManager.refresh(this.classUT);

        entityManager.remove(this.classUT);
        entityManager.flush();

        assertThat(suggestionRepository.findById(suggestion.getId())).isEmpty();
    }

}