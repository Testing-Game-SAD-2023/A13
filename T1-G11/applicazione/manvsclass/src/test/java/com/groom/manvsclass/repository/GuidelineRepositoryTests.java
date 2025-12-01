package com.groom.manvsclass.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.test.context.TestPropertySource;

import com.groom.manvsclass.model.Guideline;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.validation.ConstraintViolationException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
class GuidelineRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GuidelineRepository guidelineRepository;

    private Guideline createBaseGuideline() {

        Guideline guideline = new Guideline();
        guideline.setTitle("Suggerimento");
        guideline.setHint("Testo_Suggerimento");
        guideline.setDate(LocalDate.now());
        guideline.setImage(new byte[] {1,2,3,4,5});
        return guideline;
    }

    // TEST CREATE

    @Test
    void testCreateGuideline() {

        Guideline guideline = createBaseGuideline();
        Guideline savedGuideline = guidelineRepository.save(guideline);

        assertThat(savedGuideline).isNotNull();
        assertThat(savedGuideline.getId()).isNotNull();
        assertThat(savedGuideline.getTitle()).isEqualTo("Suggerimento");
        assertThat(savedGuideline.getHint()).isEqualTo("Testo_Suggerimento");
        assertThat(savedGuideline.getImage()).isEqualTo(new byte[]{1,2,3,4,5});
    }

    // TEST UPDATE

    @Test
    void testUpdateGuideline() {

        Guideline guideline = createBaseGuideline();
        guidelineRepository.save(guideline);
        entityManager.flush();

        guideline.setTitle("Titolo Aggiornato");
        guidelineRepository.save(guideline);
        entityManager.flush();
        entityManager.clear();

        Optional<Guideline> guidelineOpt = guidelineRepository.findById(guideline.getId());
        if(guidelineOpt.isEmpty()) {
            throw new RuntimeException("Not Found");
        }

        Guideline updatedGuideline = guidelineOpt.get();
        assertThat(updatedGuideline.getTitle()).isEqualTo("Titolo Aggiornato");
    }

    // TEST DELETE

    @Test
    void testDeleteGuideline() {

        Guideline guideline = createBaseGuideline();
        Guideline savedGuideline = guidelineRepository.save(guideline);
        entityManager.flush();

        guidelineRepository.deleteById(savedGuideline.getId());
        entityManager.flush();

        assertThat(guidelineRepository.findById(savedGuideline.getId())).isEmpty();
    }

    // TEST FIND

    @Test
    void testFindAllGuidelines_ReturnsList() {

        Guideline firstGuideline = createBaseGuideline();
        firstGuideline.setTitle("Suggerimento 1");
        guidelineRepository.save(firstGuideline);

        Guideline secondGuideline = createBaseGuideline();
        secondGuideline.setTitle("Suggerimento 2");
        guidelineRepository.save(secondGuideline);

        entityManager.flush();

        var results = guidelineRepository.findAllGuidelines();

        assertThat(results).hasSize(2);
        assertThat(results).extracting(Guideline::getTitle)
                .containsExactlyInAnyOrder("Suggerimento 1", "Suggerimento 2");
    }

    @Test
    void testFindAllGuidelines_ReturnsEmpty() {

        var results = guidelineRepository.findAllGuidelines();
        assertThat(results).isEmpty();
    }

    @Test
    void testFindByTitle() {

        Guideline guideline = createBaseGuideline();
        guidelineRepository.save(guideline);
        entityManager.flush();

        var result = guidelineRepository.findByTitle("Suggerimento");

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Suggerimento");
    }

    @Test
    void testFindByTitle_WrongTitle() {

        Guideline guideline = createBaseGuideline();
        guidelineRepository.save(guideline);
        entityManager.flush();

        var result = guidelineRepository.findByTitle("Titolo_Non_Esistente");

        assertThat(result).isEmpty();
    }

    @Test
    void testExistsByTitle_True() {

        Guideline guideline = createBaseGuideline();
        guidelineRepository.save(guideline);
        entityManager.flush();

        boolean exists = guidelineRepository.existsByTitle("Suggerimento");

        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByTitle_False() {

        Guideline guideline = createBaseGuideline();
        guidelineRepository.save(guideline);
        entityManager.flush();

        boolean exists = guidelineRepository.existsByTitle("Titolo_Non_Esistente");

        assertThat(exists).isFalse();
    }

    // TEST TITLE

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void testInvalidTitle(String invalidTitle) {

        Guideline guideline = createBaseGuideline();
        guideline.setTitle(invalidTitle);

        assertThrows(ConstraintViolationException.class, () -> {
            guidelineRepository.save(guideline);
            entityManager.flush();
        });
    }

    @Test
    void testTitleTooLong() {

        Guideline guideline = createBaseGuideline();
        guideline.setTitle("a".repeat(300));

        assertThrows(DataIntegrityViolationException.class, () -> {
            guidelineRepository.save(guideline);
            entityManager.flush();
        });
    }

    // TEST HINT

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void testInvalidHint(String invalidHint) {

        Guideline guideline = createBaseGuideline();
        guideline.setHint(invalidHint);

        assertThrows(ConstraintViolationException.class, () -> {
            guidelineRepository.save(guideline);
            entityManager.flush();
        });
    }

    @Test
    void testHintTooLong() {

        Guideline guideline = createBaseGuideline();
        guideline.setHint("a".repeat(300));

        assertThrows(DataIntegrityViolationException.class, () -> {
            guidelineRepository.save(guideline);
            entityManager.flush();
        });
    }

    // TEST DATE

    @Test
    void testNullDate() {

        Guideline guideline = createBaseGuideline();
        guideline.setDate(null);

        assertThrows(ConstraintViolationException.class, () -> {
            guidelineRepository.save(guideline);
            entityManager.flush();
        });
    }

    // TEST IMAGE

    @Test void testSaveNullImage() {

        Guideline guideline = createBaseGuideline();
        guideline.setImage(null);

        Guideline savedGuideline = guidelineRepository.save(guideline);
        entityManager.flush();
        assertThat(savedGuideline.getImage()).isNull();
    }

    @Test void testSaveLargeImage() {

        Guideline guideline = createBaseGuideline();
        guideline.setImage(new byte[1024*1024]); // 1 MB

        Guideline savedGuideline = guidelineRepository.save(guideline);
        entityManager.flush();
        assertThat(savedGuideline.getImage().length).isEqualTo(1024*1024);
    }

}