package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.*;
import com.groom.manvsclass.util.TestUtils;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import testrobotchallenge.commons.models.opponent.OpponentDifficulty;

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
class InteractionRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InteractionRepository interactionRepository;

    // oggetti necessari per tutte le Interaction
    private Admin admin;
    private ClassUT classUT;

    // configurazione di comparison per Interaction
    //ignora i campi di relazione
    public static final RecursiveComparisonConfiguration INTERACTION_COMPARISON_CONFIG =
            RecursiveComparisonConfiguration.builder()
                    .withComparedFields(
                            "id",
                            "type",
                            "description",
                            "date"
                    )
                    .withEqualsForType(
                            (classUT1, classUT2) -> classUT1.getName().equals(classUT2.getName()),
                            ClassUT.class
                    )
                    .withEqualsForType(
                            (admin1, admin2) -> admin1.getEmail().equals(admin2.getEmail()),
                            Admin.class
                    )
                    .build();

    // assert personalizzati
    private final TestUtils<Interaction> interactionTestUtils = new TestUtils<>(INTERACTION_COMPARISON_CONFIG);

    //creazione interaction di base
    public static Interaction createBaseInteraction(Admin admin, ClassUT classUT) {

        Interaction interaction = new Interaction();
        interaction.setType(InteractionType.LIKE);
        interaction.setDescription("Descrizione base");
        interaction.setDate(LocalDate.now());
        interaction.setAdmin(admin);
        interaction.setClassUT(classUT);

        return interaction;
    }

    //creazione oggetti necessari in tutte le interaction
    @BeforeEach
    void setup() {

        // Admin
        admin = new Admin();
        admin.setEmail("admin@test.com");
        admin.setNome("NomeAdmin");
        admin.setCognome("CognomeAdmin");
        admin.setUsername("adminUser");
        entityManager.persist(admin);

        // ClassUT
        classUT = new ClassUT();
        classUT.setName("TestClass");
        classUT.setDate(LocalDate.now());
        classUT.setDifficulty(OpponentDifficulty.EASY);
        classUT.setDescription("Descrizione");
        classUT.setUri("uri");
        classUT.setCategories(List.of()); // nessuna categoria
        entityManager.persist(classUT);
    }

    @Test
    void testSaveInteraction() {

        //creazione interaction di base
        Interaction interaction = createBaseInteraction(admin, classUT);
        Interaction saved = interactionRepository.save(interaction);

        entityManager.flush();  // forza la scrittura sul database
        entityManager.clear();  // svuota la cache per forzare la lettura dal database

        //ricerca tramite l'id della interaction
        Optional<Interaction> foundOpt = interactionRepository.findById(saved.getId());
        assertThat(foundOpt).isPresent();

        //verifica presenza dell'interaction e corrispondenza tra trovata e salvata
        Interaction found = foundOpt.get();
        interactionTestUtils.assertEquals(saved, found);
    }

    @Test
    void testUpdateInteraction() {

        //creazione interaction di base
        Interaction interaction = createBaseInteraction(admin, classUT);
        Interaction saved = interactionRepository.save(interaction);

        entityManager.flush();

        saved.setType(InteractionType.REPORT);
        saved.setDescription("Descrizione aggiornata");
        Interaction updated = interactionRepository.save(saved);

        entityManager.flush();
        entityManager.clear();

        //ricerca interaction tramite id aggiornato
        Optional<Interaction> foundOpt = interactionRepository.findById(updated.getId());
        assertThat(foundOpt).isPresent();

        //verifica aggiornamento riuscito
        Interaction found = foundOpt.get();
        interactionTestUtils.assertEquals(updated, found);
    }

    @Test
    void testDeleteInteraction() {

        Interaction interaction = createBaseInteraction(admin, classUT);
        Interaction saved = interactionRepository.save(interaction);

        entityManager.flush();

        //delete dell'interaction tramite id e aggiornamento db
        interactionRepository.deleteById(saved.getId());
        entityManager.flush();
        entityManager.clear();

        //verifica interaction eliminata
        assertThat(interactionRepository.findById(saved.getId())).isEmpty();
    }


    @Test
    void testFindByType() {

        // creazione interazione 1 LIKE
        Interaction i1 = createBaseInteraction(admin, classUT);
        i1.setType(InteractionType.LIKE);
        interactionRepository.save(i1);

        // creazione interazione 2 REPORT
        Interaction i2 = createBaseInteraction(admin, classUT);
        i2.setType(InteractionType.REPORT);
        interactionRepository.save(i2);

        entityManager.flush();
        entityManager.clear();

        //filtro in base a interazione LIKE
        List<Interaction> results = interactionRepository.findByType(InteractionType.LIKE);

        //verifica che la lista contenga interazioni LIKE
        interactionTestUtils.assertListEquals(List.of(i1), results);
    }

    @Test
    void testCountByClassUTNameAndType() {

        // creazione interazione 1 LIKE
        Interaction i1 = createBaseInteraction(admin, classUT);
        i1.setType(InteractionType.LIKE);
        interactionRepository.save(i1);

        // creazione interazione 2 LIKE
        Interaction i2 = createBaseInteraction(admin, classUT);
        i2.setType(InteractionType.LIKE);
        interactionRepository.save(i2);

        // creazione interazione 3 REPORT
        Interaction i3 = createBaseInteraction(admin, classUT);
        i3.setType(InteractionType.REPORT);
        interactionRepository.save(i3);

        entityManager.flush();
        entityManager.clear();

        //conteggio di interazioni LIKE e REPORT sulla classe di test
        long countLike = interactionRepository.countByClassUT_NameAndType("TestClass", InteractionType.LIKE);
        long countReport = interactionRepository.countByClassUT_NameAndType("TestClass", InteractionType.REPORT);

        //verifica corrispondenza conteggio
        assertThat(countLike).isEqualTo(2);
        assertThat(countReport).isEqualTo(1);
    }


    @Test
    void testCountByClassUTNameAndType_NotFound() {
        //verifica inesistenza di interazioni di report nella classe di test
        long count = interactionRepository.countByClassUT_NameAndType("TestClass", InteractionType.REPORT);
        assertThat(count).isEqualTo(0);
    }
}
