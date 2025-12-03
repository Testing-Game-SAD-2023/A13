package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Suggestion;
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
class GuidelineRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GuidelineRepository guidelineRepository;

    // METODI UTILI
    private Guideline createBaseGuideline() {

        Guideline guideline = new Guideline();
        guideline.setTitle("Guideline");
        guideline.setHint("Testo_Guideline");
        guideline.setDate(LocalDate.now());
        guideline.setImage(new byte[] {1,2,3,4,5});
        return guideline;
    }

    private static boolean guidelineEquals(Guideline g1, Guideline g2) {
        if (g1 == g2) return true;
        if (g1 == null || g2 == null) return false;

        return g1.getId().equals(g2.getId()) &&
                g1.getTitle().equals(g2.getTitle()) &&
                g1.getHint().equals(g2.getHint()) &&
                g1.getDate().equals(g2.getDate()) &&
                g1.getBase64Image().equals(g2.getBase64Image());
    }

    private static void assertGuidelineEquals(Guideline expected, Guideline actual) {
        assertThat(guidelineEquals(expected, actual)).isTrue();
    }

    // TEST CREATE

    @Test
    void testCreateGuideline() {

        Guideline guideline = createBaseGuideline();
        Guideline savedGuideline = guidelineRepository.save(guideline);

        entityManager.flush();  // forza la scrittura sul database
        entityManager.clear();  // svuota la cache per forzare la lettura dal database

        // lo rilegge per verificare il corretto inserimento nel database
        Optional<Guideline> guidelineOpt = guidelineRepository.findById(savedGuideline.getId());
        assertThat(guidelineOpt).isPresent();

        Guideline receivedGuideline = guidelineOpt.get();
        assertGuidelineEquals(savedGuideline, receivedGuideline);
    }

    @Test
    void testSaveGuideline_DuplicatedTitle() {

        Guideline firstGuideline = createBaseGuideline();
        Guideline savedFirstGuideline = guidelineRepository.save(firstGuideline);

        entityManager.flush();

        Guideline secondGuideline = createBaseGuideline();
        assertThrows(DataIntegrityViolationException.class, () -> {
            guidelineRepository.save(secondGuideline);
            entityManager.flush();
        });

        entityManager.clear();

        // verifica la presenza del primo guideline
        Optional<Guideline> guidelineOpt = guidelineRepository.findById(savedFirstGuideline.getId());
        assertThat(guidelineOpt).isPresent();

        Guideline receivedGuideline = guidelineOpt.get();
        assertGuidelineEquals(savedFirstGuideline, receivedGuideline);
    }

    // TEST SAVE_ALL

    @Test
    void testSaveAllGuidelines() {

        Guideline firstGuideline = createBaseGuideline();
        firstGuideline.setTitle("Guideline_1");

        Guideline secondGuideline = createBaseGuideline();
        secondGuideline.setTitle("Guideline_2");

        List<Guideline> guidelines = Arrays.asList(firstGuideline, secondGuideline);

        // salva le guidelines
        List<Guideline> savedGuidelines = guidelineRepository.saveAll(guidelines);
        assertThat(savedGuidelines).hasSize(guidelines.size());

        entityManager.flush();
        entityManager.clear();

        // verifica la presenza delle guidelines nel database, effettuando una findById per ogni guideline
        Stream<Guideline> foundGuidelines =
                savedGuidelines
                        .stream()
                        .map(guideline -> {
                            Optional<Guideline> guidelineOpt = guidelineRepository.findById(guideline.getId());
                            assertThat(guidelineOpt).isPresent();
                            return guidelineOpt.get();
                        });

        Comparator<Guideline> guidelineComparator = (g1, g2) -> guidelineEquals(g1, g2) ? 0 : 1;

        assertThat(foundGuidelines)
                .hasSize(guidelines.size())
                .usingElementComparator(guidelineComparator)
                .containsExactlyInAnyOrderElementsOf(guidelines);
    }

    // TEST UPDATE

    @Test
    void testUpdateGuideline() {

        Guideline guideline = createBaseGuideline();
        Guideline savedGuideline = guidelineRepository.save(guideline);
        entityManager.flush();

        guideline.setTitle("Titolo Aggiornato");
        Guideline updatedGuideline = guidelineRepository.save(savedGuideline);
        entityManager.flush();
        entityManager.clear();

        Optional<Guideline> guidelineOpt = guidelineRepository.findById(guideline.getId());
        assertThat(guidelineOpt).isPresent();

        Guideline foundGuideline = guidelineOpt.get();
        assertGuidelineEquals(updatedGuideline, foundGuideline);
    }

    // TEST DELETE

    @Test
    void testDeleteGuideline() {

        Guideline guideline = createBaseGuideline();
        Guideline savedGuideline = guidelineRepository.save(guideline);
        entityManager.flush();

        guidelineRepository.deleteById(savedGuideline.getId());
        entityManager.flush();
        entityManager.clear();

        assertThat(guidelineRepository.findById(savedGuideline.getId())).isEmpty();
    }

    // TEST FIND

    @Test
    void testFindAllGuidelines_ReturnsList() {

        Guideline firstGuideline = createBaseGuideline();
        firstGuideline.setTitle("Guideline_1");
        Guideline savedFirstGuideline = guidelineRepository.save(firstGuideline);

        Guideline secondGuideline = createBaseGuideline();
        secondGuideline.setTitle("Guideline_2");
        Guideline savedSecondGuideline = guidelineRepository.save(secondGuideline);

        entityManager.flush();
        entityManager.clear();

        List<Guideline> results = guidelineRepository.findAllGuidelines();

        Comparator<Guideline> guidelineComparator = (g1, g2) -> guidelineEquals(g1, g2) ? 0 : 1;

        // verifica che i suggerimenti ottenuti siano corretti
        assertThat(results)
                .hasSize(2)
                .usingElementComparator(guidelineComparator)
                .containsExactlyInAnyOrder(savedFirstGuideline, savedSecondGuideline);
    }

    @Test
    void testFindAllGuidelines_ReturnsEmpty() {

        List<Guideline> results = guidelineRepository.findAllGuidelines();
        assertThat(results).isEmpty();
    }

    @Test
    void testFindByTitle() {

        Guideline guideline = createBaseGuideline();
        Guideline savedGuideline = guidelineRepository.save(guideline);
        entityManager.flush();
        entityManager.clear();

        Optional<Guideline> result = guidelineRepository.findByTitle(guideline.getTitle());

        assertThat(result).isPresent();
        assertGuidelineEquals(savedGuideline, result.get());
    }

    @Test
    void testFindByTitle_WrongTitle() {

        Guideline guideline = createBaseGuideline();
        guidelineRepository.save(guideline);
        entityManager.flush();
        entityManager.clear();

        Optional<Guideline> result = guidelineRepository.findByTitle("Titolo_Non_Esistente");

        assertThat(result).isEmpty();
    }

    @Test
    void testExistsByTitle_True() {

        Guideline guideline = createBaseGuideline();
        Guideline savedGuideline = guidelineRepository.save(guideline);
        entityManager.flush();
        entityManager.clear();

        boolean exists = guidelineRepository.existsByTitle(savedGuideline.getTitle());

        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByTitle_False() {

        Guideline guideline = createBaseGuideline();
        guidelineRepository.save(guideline);
        entityManager.flush();
        entityManager.clear();

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

        final int IMAGE_SIZE = 1024 * 1024;

        Guideline guideline = createBaseGuideline();
        guideline.setImage(new byte[IMAGE_SIZE]); // 1 MB

        Guideline savedGuideline = guidelineRepository.save(guideline);
        entityManager.flush();
        assertThat(savedGuideline.getImage().length).isEqualTo(IMAGE_SIZE);
    }

}