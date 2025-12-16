package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.model.Operation;
import com.groom.manvsclass.util.TestUtils;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.test.context.TestPropertySource;

import com.groom.manvsclass.model.Opponent;
import com.groom.manvsclass.model.ClassUT;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;
import testrobotchallenge.commons.models.score.JacocoScore;
import testrobotchallenge.commons.models.score.Coverage;
import testrobotchallenge.commons.models.score.EvosuiteScore;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EntityScan(basePackages = "com.groom.manvsclass")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL",
        "spring.jpa.show-sql=true",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class OpponentRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OpponentRepository opponentRepository;

    private ClassUT classUT;

    // configurazione di comparison per Opponent
    //ignora i campi di relazione
    public static final RecursiveComparisonConfiguration OPPONENT_COMPARISON_CONFIG =
            RecursiveComparisonConfiguration.builder()
                    .withComparedFields(
                            "id",
                            "date",
                            "type",
                            "coverage",
                            "jacocoScore",
                            "evosuiteScore"
                    )
                    .withEqualsForType(
                            (classUT1, classUT2) -> classUT1.getName().equals(classUT2.getName()),
                            ClassUT.class
                    )
                    .withEqualsForType(
                            (coverage1, coverage2) -> coverage1.getCovered() == coverage2.getCovered() && coverage1.getMissed() == coverage2.getMissed(),
                            Coverage.class
                    )
                    .build();

    // assert personalizzati
    private final TestUtils<Opponent> opponentTestUtils = new TestUtils<>(OPPONENT_COMPARISON_CONFIG);

    @BeforeEach
    void setup() {
        // Setup ClassUT
        classUT = new ClassUT();
        classUT.setName("TestClass");
        classUT.setDate(LocalDate.now());
        classUT.setDifficulty(OpponentDifficulty.EASY);
        classUT.setUri("/path/classe");
        classUT.setDescription("Test class");
        entityManager.persist(classUT);
    }

    private Opponent createBaseOpponent() {
        Opponent opponent = new Opponent();
        opponent.setDate(LocalDate.now());
        opponent.setType("SIMPLE");
        opponent.setCoverage("coverage_data");
        opponent.setClassUT(classUT);

        // JACOCO
        JacocoScore jacoco = new JacocoScore();
        jacoco.setLineCoverage(new Coverage(50, 10));
        jacoco.setBranchCoverage(new Coverage(30, 5));
        jacoco.setInstructionCoverage(new Coverage(80, 20));
        opponent.setJacocoScore(jacoco);

        // EVOSUITE
        EvosuiteScore evo = new EvosuiteScore();
        evo.setLineCoverage(new Coverage(90, 10));
        evo.setBranchCoverage(new Coverage(70, 30));
        evo.setExceptionCoverage(new Coverage(40, 10));
        evo.setWeakMutationCoverage(new Coverage(60, 40));
        evo.setOutputCoverage(new Coverage(50, 50));
        evo.setMethodCoverage(new Coverage(55, 45));
        evo.setMethodNoExceptionCoverage(new Coverage(65, 35));
        evo.setCBranchCoverage(new Coverage(75, 25));
        opponent.setEvosuiteScore(evo);

        return opponent;
    }


    // TEST CREATE

    @Test
    void testCreateOpponent() {
        Opponent opponent = createBaseOpponent();
        Opponent saved = opponentRepository.save(opponent);

        entityManager.flush();
        entityManager.clear();

        Optional<Opponent> foundOpt = opponentRepository.findById(saved.getId());
        assertThat(foundOpt).isPresent();
        opponentTestUtils.assertEquals(saved, foundOpt.get());
    }

    // TEST UPDATE

    @Test
    void testUpdateOpponentType() {
        Opponent opponent = createBaseOpponent();
        opponentRepository.save(opponent);
        entityManager.flush();

        opponent.setType("ADVANCED");
        opponentRepository.save(opponent);
        entityManager.flush();
        entityManager.clear();

        Optional<Opponent> updatedOpt = opponentRepository.findById(opponent.getId());
        assertThat(updatedOpt).isPresent();
        opponentTestUtils.assertEquals(opponent, updatedOpt.get());
    }

    // TEST DELETE

    @Test
    void testDeleteOpponent() {
        Opponent opponent = createBaseOpponent();
        Opponent saved = opponentRepository.save(opponent);
        entityManager.flush();
        entityManager.clear();

        opponentRepository.deleteById(saved.getId());
        entityManager.flush();

        assertThat(opponentRepository.findById(saved.getId())).isEmpty();
    }

    // TEST FIND BY ID

    @Test
    void testFindById() {
        Opponent opponent = createBaseOpponent();
        Opponent saved = opponentRepository.save(opponent);
        entityManager.flush();
        entityManager.clear();

        Optional<Opponent> found = opponentRepository.findById(saved.getId());
        assertThat(found).isPresent();
        opponentTestUtils.assertEquals(saved, found.get());
    }

    // TEST FIND ALL

    @Test
    void testFindAllOpponents() {
        Opponent o1 = opponentRepository.save(createBaseOpponent());
        Opponent o2 = opponentRepository.save(createBaseOpponent());
        entityManager.flush();
        entityManager.clear();

        List<Opponent> expected = List.of(o1, o2);

        List<Opponent> found = opponentRepository.findAllOpponents();
        opponentTestUtils.assertListEquals(expected, found);
    }

    // TEST FIND OPPONENT CUSTOM

    @Test
    void testFindOpponent() {
        Opponent opponent = createBaseOpponent();
        opponentRepository.save(opponent);
        entityManager.flush();
        entityManager.clear();

        Optional<Opponent> found = opponentRepository.findOpponent(
                opponent.getClassUT().getName(),
                opponent.getType(),
                opponent.getClassUT().getDifficulty()
        );

        assertThat(found).isPresent();
        opponentTestUtils.assertEquals(opponent, found.get());
    }

    @Test
    void testFindCoverage() {
        Opponent opponent = createBaseOpponent();
        opponentRepository.save(opponent);
        entityManager.flush();

        Optional<String> coverage = opponentRepository.findCoverage(
                opponent.getClassUT().getName(),
                opponent.getType(),
                opponent.getClassUT().getDifficulty()
        );

        assertThat(coverage).isPresent();
        assertThat(coverage.get()).isEqualTo(opponent.getCoverage());
    }

    @Test
    void testFindJacocoScore() {
        Opponent opponent = createBaseOpponent();
        opponentRepository.save(opponent);
        entityManager.flush();

        Optional<JacocoScore> score = opponentRepository.findJacocoScore(
                opponent.getClassUT().getName(),
                opponent.getType(),
                opponent.getClassUT().getDifficulty()
        );

        assertThat(score).isPresent();
        assertThat(score.get().getInstructionCoverage().getCovered()).isEqualTo(opponent.getJacocoScore().getInstructionCoverage().getCovered());
    }

    @Test
    void testFindEvosuiteScore() {
        Opponent opponent = createBaseOpponent();
        opponentRepository.save(opponent);
        entityManager.flush();

        Optional<EvosuiteScore> score = opponentRepository.findEvosuiteScore(
                opponent.getClassUT().getName(),
                opponent.getType(),
                opponent.getClassUT().getDifficulty()
        );

        assertThat(score).isPresent();
        assertThat(score.get().getLineCoverage().getCovered()).isEqualTo(opponent.getEvosuiteScore().getLineCoverage().getCovered());
    }
}
