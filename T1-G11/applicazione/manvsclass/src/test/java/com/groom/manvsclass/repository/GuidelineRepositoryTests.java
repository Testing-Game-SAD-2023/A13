package com.groom.manvsclass.repository;

import com.groom.manvsclass.dto.GuidelineDTO;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Guideline;
import com.groom.manvsclass.util.TestUtils;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

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
class GuidelineRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GuidelineRepository guidelineRepository;

    // configurazione di comparison per Guideline
    // configurazione di default: confronta tutti i parametri con equals
    public static final RecursiveComparisonConfiguration GUIDELINE_COMPARISON_CONFIG = new RecursiveComparisonConfiguration();

    // assert personalizzati
    private final TestUtils<Guideline> guidelineTestUtils = new TestUtils<>(GUIDELINE_COMPARISON_CONFIG);

    public static Guideline createBaseGuideline() {

        Guideline guideline = new Guideline();
        guideline.setOrder(1);
        guideline.setHint("Testo_Guideline");
        guideline.setDate(LocalDate.now());
        guideline.setImage(null);
        return guideline;
    }

    // TEST CREATE

    @Test
    void testSaveGuideline() {

        Guideline guideline = createBaseGuideline();
        Guideline savedGuideline = guidelineRepository.save(guideline);

        entityManager.flush();  // forza la scrittura sul database
        entityManager.clear();  // svuota la cache per forzare la lettura dal database

        // per verificare il corretto inserimento nel database effettua una find
        Optional<Guideline> guidelineOpt = guidelineRepository.findById(savedGuideline.getId());
        assertThat(guidelineOpt).isPresent();

        Guideline foundGuideline = guidelineOpt.get();
        guidelineTestUtils.assertEquals(savedGuideline, foundGuideline);
    }

    @Test
    void testSaveAllGuidelines() {

        Guideline firstGuideline = createBaseGuideline();
        firstGuideline.setOrder(1);

        Guideline secondGuideline = createBaseGuideline();
        secondGuideline.setOrder(2);

        List<Guideline> guidelines = Arrays.asList(firstGuideline, secondGuideline);

        // salva le guidelines
        List<Guideline> savedGuidelines = guidelineRepository.saveAll(guidelines);
        assertThat(savedGuidelines).hasSize(guidelines.size());

        entityManager.flush();
        entityManager.clear();

        // verifica la presenza dei suggerimenti nel database, effettuando una findById per ogni suggerimento
        List<Guideline> foundGuidelines = new ArrayList<>();
        for (Guideline guideline : savedGuidelines) {
            Optional<Guideline> guidelineOpt = guidelineRepository.findById(guideline.getId());
            assertThat(guidelineOpt).isPresent();
            foundGuidelines.add(guidelineOpt.get());
        }

        // verifica che le linee guida ottenute siano corrette
        guidelineTestUtils.assertListEquals(savedGuidelines, foundGuidelines);
    }

    // TEST UPDATE

    @Test
    void testUpdateGuideline() {

        Guideline guideline = createBaseGuideline();
        Guideline savedGuideline = guidelineRepository.save(guideline);
        entityManager.flush();

        guideline.setOrder(2);
        Guideline updatedGuideline = guidelineRepository.save(savedGuideline);
        entityManager.flush();
        entityManager.clear();

        Optional<Guideline> guidelineOpt = guidelineRepository.findById(guideline.getId());
        assertThat(guidelineOpt).isPresent();

        Guideline foundGuideline = guidelineOpt.get();
        guidelineTestUtils.assertEquals(updatedGuideline, foundGuideline);
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
        firstGuideline.setOrder(1);
        Guideline savedFirstGuideline = guidelineRepository.save(firstGuideline);

        Guideline secondGuideline = createBaseGuideline();
        secondGuideline.setOrder(2);
        Guideline savedSecondGuideline = guidelineRepository.save(secondGuideline);

        entityManager.flush();
        entityManager.clear();

        List<Guideline> foundGuidelines = guidelineRepository.findAllGuidelines();

        List<Guideline> expectedGuidelines = Arrays.asList(savedFirstGuideline, savedSecondGuideline);

        // verifica che le linee guida ottenute siano corrette
        guidelineTestUtils.assertListEquals(expectedGuidelines, foundGuidelines);
    }

    @Test
    void testFindAllGuidelines_ReturnsEmpty() {

        List<Guideline> results = guidelineRepository.findAllGuidelines();
        assertThat(results).isEmpty();
    }

    @Test
    void testFindByOrder() {

        Guideline guideline = createBaseGuideline();
        Guideline savedGuideline = guidelineRepository.save(guideline);
        entityManager.flush();
        entityManager.clear();

        Optional<Guideline> result = guidelineRepository.findByOrder(guideline.getOrder());

        assertThat(result).isPresent();
        guidelineTestUtils.assertEquals(savedGuideline, result.get());
    }

    @Test
    void testFindByOrder_WrongTitle() {

        Guideline guideline = createBaseGuideline();
        guidelineRepository.save(guideline);
        entityManager.flush();
        entityManager.clear();

        Optional<Guideline> result = guidelineRepository.findByOrder(2);

        assertThat(result).isEmpty();
    }

    // TEST CODE_ID

    void testInvalidOrder() {

        int invalidOrder = -1;

        Guideline guideline = createBaseGuideline();
        guideline.setOrder(invalidOrder);

        assertThrows(ConstraintViolationException.class, () -> {
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

        assertThrows(DataIntegrityViolationException.class, () -> {
            guidelineRepository.save(guideline);
            entityManager.flush();
        });
    }

    // TEST IMAGE

    @Test
    void testSaveNullImage() {

        Guideline guideline = createBaseGuideline();
        guideline.setImage(null);

        Guideline savedGuideline = guidelineRepository.save(guideline);
        entityManager.flush();
        assertThat(savedGuideline.getImage()).isNull();
    }

}