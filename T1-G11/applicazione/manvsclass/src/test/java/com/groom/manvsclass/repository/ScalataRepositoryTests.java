package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Opponent;
import com.groom.manvsclass.util.TestUtils;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.test.context.TestPropertySource;

import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.model.Admin;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import testrobotchallenge.commons.models.score.Coverage;

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
class ScalataRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ScalataRepository scalataRepository;

    private Admin admin;

    // configurazione di comparison per Scalata
    //ignora i campi di relazione
    public static final RecursiveComparisonConfiguration SCALATA_COMPARISON_CONFIG =
            RecursiveComparisonConfiguration.builder()
                    .withComparedFields(
                            "name",
                            "numLevels",
                            "description",
                            "date"
                    )
                    .withEqualsForType(
                            (admin1, admin2) -> admin1.getEmail().equals(admin2.getEmail()),
                            Admin.class
                    )
                    .build();

    // assert personalizzati
    private final TestUtils<Scalata> scalataTestUtils = new TestUtils<>(SCALATA_COMPARISON_CONFIG);

    @BeforeEach
    void setup() {
        admin = new Admin();
        admin.setEmail("admin@test.com");
        entityManager.persist(admin);
    }

    private Scalata createBaseScalata() {
        Scalata scalata = new Scalata();
        scalata.setName("Scalata1");
        scalata.setNumLevels(5);
        scalata.setDescription("Descrizione Scalata");
        scalata.setDate(LocalDate.now());
        scalata.setAdmin(admin);
        return scalata;
    }


    // TEST CREATE

    @Test
    void testCreateScalata() {
        Scalata saved = scalataRepository.save(createBaseScalata());
        entityManager.flush();
        entityManager.clear();

        Optional<Scalata> foundOpt = scalataRepository.findById(saved.getName());
        assertThat(foundOpt).isPresent();
        scalataTestUtils.assertEquals(saved, foundOpt.get());
    }

    // TEST UPDATE

    @Test
    void testUpdateScalata() {
        Scalata scalata = createBaseScalata();
        scalataRepository.save(scalata);
        entityManager.flush();

        scalata.setDescription("Descrizione Aggiornata");
        scalata.setNumLevels(10);
        scalataRepository.save(scalata);
        entityManager.flush();
        entityManager.clear();

        Optional<Scalata> updatedOpt = scalataRepository.findById(scalata.getName());
        assertThat(updatedOpt).isPresent();
        scalataTestUtils.assertEquals(scalata, updatedOpt.get());
    }

    // TEST DELETE

    @Test
    void testDeleteScalata() {
        Scalata scalata = createBaseScalata();
        scalataRepository.save(scalata);
        entityManager.flush();

        scalataRepository.deleteById(scalata.getName());
        entityManager.flush();

        assertThat(scalataRepository.findById(scalata.getName())).isEmpty();
    }

    // TEST FIND BY ADMIN EMAIL

    @Test
    void testFindByAdminEmail() {
        Scalata scalata = createBaseScalata();
        scalataRepository.save(scalata);
        entityManager.flush();

        List<Scalata> list = scalataRepository.findByAdmin_Email("admin@test.com");

        scalataTestUtils.assertListEquals(List.of(scalata), list);
    }

    // TEST FIND BY NUM LEVELS

    @Test
    void testFindByNumLevels() {
        Scalata scalata1 = createBaseScalata();
        Scalata scalata2 = createBaseScalata();
        scalata2.setName("Scalata2");
        scalata2.setNumLevels(10);
        scalataRepository.save(scalata1);
        scalataRepository.save(scalata2);
        entityManager.flush();

        List<Scalata> list5 = scalataRepository.findByNumLevels(5);
        List<Scalata> list10 = scalataRepository.findByNumLevels(10);

        scalataTestUtils.assertListEquals(List.of(scalata1), list5);
        scalataTestUtils.assertListEquals(List.of(scalata2), list10);
    }
}
