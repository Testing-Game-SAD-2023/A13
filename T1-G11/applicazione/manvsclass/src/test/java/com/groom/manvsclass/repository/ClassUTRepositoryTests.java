package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Category;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.util.TestUtils;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;

@DataJpaTest
@EntityScan(basePackages = "com.groom.manvsclass")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL", // Simula MySQL
        "spring.jpa.show-sql=true",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
public class ClassUTRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClassUTRepository classUTRepository;

    // configurazione di comparison per ClassUT
    // configurazione di default: confronta tutti i parametri con equals
    //ignorando le liste nelle relazioni a cascata
    public static final RecursiveComparisonConfiguration CLASS_UT_COMPARISON_CONFIG =
            RecursiveComparisonConfiguration.builder()
                    .withIgnoredFields(
                            "categories",
                            "interactions",
                            "operations",
                            "suggestions",
                            "opponents",
                            "classUTScalata"
                    )
                    .build();

    // assert personalizzati
    private final TestUtils<ClassUT> classUTTestUtils = new TestUtils<>(CLASS_UT_COMPARISON_CONFIG);

    //Creazione di una categoria di base
    public static Category createBaseCategory() {
        Category category = new Category();
        category.setName("BaseCategory");
        return category;
    }

    //Creazione classeUT di base senza categoria
    public static ClassUT createBaseClassUT() {
        ClassUT classUT = new ClassUT();
        classUT.setName("TestClass");
        classUT.setDate(LocalDate.now());
        classUT.setDifficulty(OpponentDifficulty.EASY);
        classUT.setUri("/path/classe");
        classUT.setDescription("Descrizione di test");
        classUT.setCategories(List.of());  // opzionale
        return classUT;
    }

    //Creazione classeUT di base con categoria
    public static ClassUT createBaseClassUTWithCategory(Category category) {
        ClassUT classUT = createBaseClassUT();
        classUT.setCategories(List.of(category));
        return classUT;
    }

    @Test
    void testSaveClassUT() {

        //create della classe base UT senza categoria
        ClassUT classUT = createBaseClassUT();

        //aggiungo la classe
        ClassUT savedClassUT = classUTRepository.save(classUT);

        entityManager.flush();  // forza la scrittura sul database
        entityManager.clear();  // svuota la cache per forzare la lettura dal database

        //ricerca della classeUT tramite id
        Optional<ClassUT> classUTOpt = classUTRepository.findById(savedClassUT.getName());
        //verifica dell'esistenza della classeUT
        assertThat(classUTOpt).isPresent();

        //verifica che classeUT trovata corrisponda a quella aggiunta
        ClassUT foundClassUT = classUTOpt.get();
        classUTTestUtils.assertEquals(savedClassUT, foundClassUT);
    }

    @Test
    void testSaveClassUTWithCategory() {

        // crea categoria
        Category category = createBaseCategory();
        entityManager.persist(category);

        // crea classUT con category
        ClassUT classUT = createBaseClassUTWithCategory(category);

        // salva la classUT
        ClassUT savedClassUT = classUTRepository.save(classUT);

        entityManager.flush();
        entityManager.clear();

        // ricarica dal DB
        Optional<ClassUT> classUTOpt = classUTRepository.findById(savedClassUT.getName());
        assertThat(classUTOpt).isPresent();

        ClassUT foundClassUT = classUTOpt.get();

        //prelievo lista delle categorie dalla classeUt
        //verifica della lista non nulla
        //verifica del numero di categorie pari a uno e verifica del nome della categoria
        assertThat(foundClassUT.getCategories())
                .isNotNull()
                .asInstanceOf(LIST)
                .hasSize(1)
                .extracting("name")
                .containsExactly(category.getName());
    }

    @Test
    void testUpdateClassUT() {
        ClassUT classUT = createBaseClassUT();

        ClassUT savedClassUT = classUTRepository.save(classUT);
        entityManager.flush();

        // aggiornamento campi di classUT
        savedClassUT.setDescription("Descrizione aggiornata");
        savedClassUT.setUri("http://updated-url.com");
        savedClassUT.setDifficulty(OpponentDifficulty.HARD);
        savedClassUT.setDate(LocalDate.now().plusDays(2));

        // update
        ClassUT updatedClassUT = classUTRepository.save(savedClassUT);
        entityManager.flush();
        entityManager.clear();

        Optional<ClassUT> classUTOpt = classUTRepository.findById(updatedClassUT.getName());
        assertThat(classUTOpt).isPresent();

        ClassUT foundClassUT = classUTOpt.get();

        // verifica che la classe aggiornata corrisponda a quella trovata
        classUTTestUtils.assertEquals(updatedClassUT, foundClassUT);
    }

    @Test
    void testDeleteClassUT() {

        ClassUT classUT = createBaseClassUT();

        ClassUT savedClassUT = classUTRepository.save(classUT);
        entityManager.flush();

        // delete della classUT tramite il suo nome
        classUTRepository.deleteById(savedClassUT.getName());
        entityManager.flush();
        entityManager.clear();

        // verifica della corretta rimozione della classUT
        Optional<ClassUT> classUTOpt = classUTRepository.findById(savedClassUT.getName());
        assertThat(classUTOpt).isEmpty();
    }

    @Test
    void testFindByNameOrDescriptionIgnoreCase() {

        // creazione prima classUT con match sul nome
        ClassUT classUT1 = createBaseClassUT();
        classUT1.setName("ProgettoBase");
        classUT1.setDescription("descrizione INUTILE");
        classUTRepository.save(classUT1);

        // creazione seconda con match sulla descrizione
        ClassUT classUT2 = createBaseClassUT();
        classUT2.setName("nomeInutile");
        classUT2.setDescription("descrizioneTest UTILE");
        classUTRepository.save(classUT2);

        // creazione classUT senza match
        ClassUT classUT3 = createBaseClassUT();
        classUT3.setName("ZeroMatch");
        classUT3.setDescription("Nessun riferimento utile");
        classUTRepository.save(classUT3);

        entityManager.flush();
        entityManager.clear();

        // ricerca ignor case su nome o descrizione
        List<ClassUT> results = classUTRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("BASE", "TEST");

        List<ClassUT> expectedResults = List.of(classUT1, classUT2);
        // deve restituire classUT1 e classUT2
        classUTTestUtils.assertListEquals(expectedResults, results);
    }

    @Test
    void testFilterByDifficulty() {

        // creazione ClassUT1 per match su difficoltà EASY
        ClassUT classUT1 = createBaseClassUT();
        classUT1.setName("EasyClass");
        classUT1.setDifficulty(OpponentDifficulty.EASY);
        classUTRepository.save(classUT1);

        // creazione classUT2 per non match su difficoltà HARD
        ClassUT classUT2 = createBaseClassUT();
        classUT2.setName("HardClass");
        classUT2.setDifficulty(OpponentDifficulty.HARD);
        classUTRepository.save(classUT2);

        entityManager.flush();
        entityManager.clear();

        // filtraggio per difficoltà
        List<ClassUT> results = classUTRepository.filterByDifficulty(OpponentDifficulty.EASY);

        //verifica che il risultato sia effettivamente solo classUT1
        classUTTestUtils.assertListEquals(List.of(classUT1), results);
    }

    @Test
    void testFindAllByOrderByDateAsc() {

        // creazione classUT1 con data vecchia
        ClassUT c1 = createBaseClassUT();
        c1.setName("OldClass");
        c1.setDate(LocalDate.of(2023, 1, 10));
        classUTRepository.save(c1);

        // creazione classUT con data recente
        ClassUT c2 = createBaseClassUT();
        c2.setName("NewClass");
        c2.setDate(LocalDate.of(2024, 5, 20));
        classUTRepository.save(c2);

        entityManager.flush();
        entityManager.clear();

        // lista ordinata in base alla data
        List<ClassUT> results = classUTRepository.findAllByOrderByDateAsc();

        List<ClassUT> expectedResults = List.of(c1, c2);
        //verifica che la seconda classUT venga considerata più recente
        classUTTestUtils.assertListSameOrderEquals(expectedResults, results);
    }

    @Test
    void testFindAllByOrderByNameAsc() {

        // classUT1 con iniziale del nome Z
        ClassUT c1 = createBaseClassUT();
        c1.setName("ZClass");
        classUTRepository.save(c1);

        // classUT2 con iniziale del nome A
        ClassUT c2 = createBaseClassUT();
        c2.setName("AClass");
        classUTRepository.save(c2);

        entityManager.flush();
        entityManager.clear();

        // lista ordinata in base al nome
        List<ClassUT> results = classUTRepository.findAllByOrderByNameAsc();

        List<ClassUT> expectedResults = List.of(c2, c1);
        // verifica che classUT2 venga prima in ordine alfabetico rispetto a classUT1
        classUTTestUtils.assertListSameOrderEquals(expectedResults, results);
    }

    @Test
    void testFindByNameContainingIgnoreCaseAndDifficulty() {

        // classUT1 con match completo
        ClassUT c1 = createBaseClassUT();
        c1.setName("ProgettoAvanzato");
        c1.setDifficulty(OpponentDifficulty.EASY);
        classUTRepository.save(c1);

        // classUT2 con match su difficoltà ma non su name
        ClassUT c2 = createBaseClassUT();
        c2.setName("CompitoFinale");
        c2.setDifficulty(OpponentDifficulty.EASY);
        classUTRepository.save(c2);

        // classUT3 con match su name ma nons su difficoltà
        ClassUT c3 = createBaseClassUT();
        c3.setName("ProgettoBase");
        c3.setDifficulty(OpponentDifficulty.HARD);
        classUTRepository.save(c3);

        entityManager.flush();
        entityManager.clear();

        // lista contenente classi con nome contenente PROG e difficoltà facile
        List<ClassUT> results =
                classUTRepository.findByNameContainingIgnoreCaseAndDifficulty("PROG", OpponentDifficulty.EASY);

        //verifica che la lista contenga solo classUT1
        classUTTestUtils.assertListEquals(List.of(c1), results);
    }

    @Test
    void testFindAllByCategoryName() {

        // classUT1 con categoria A
        Category categoryA = createBaseCategory();
        categoryA.setName("CategoriaA");
        entityManager.persist(categoryA);

        // classUT2 con categoria  B
        Category categoryB = createBaseCategory();
        categoryB.setName("CategoriaB");
        entityManager.persist(categoryB);

        // associazione categoria A a classUT1
        ClassUT c1 = createBaseClassUT();
        c1.setName("ClassA");
        c1.setCategories(List.of(categoryA));
        classUTRepository.save(c1);

        // associazione a classUT2 categoria B
        ClassUT c2 = createBaseClassUT();
        c2.setName("ClassB");
        c2.setCategories(List.of(categoryB));
        classUTRepository.save(c2);

        entityManager.flush();
        entityManager.clear();

        // lista contenente classUT con categoria A
        List<ClassUT> results = classUTRepository.findAllByCategoryName("CategoriaA");

        //verifica che la lista contenga solo classUT1
        classUTTestUtils.assertListEquals(List.of(c1), results);
    }

    @Test
    void testSearchAndFilterByCategory() {

        // classUT1 con categoria A
        Category catA = createBaseCategory();
        catA.setName("CategoriaA");
        entityManager.persist(catA);

        Category catB = createBaseCategory();
        catB.setName("CategoriaB");
        entityManager.persist(catB);

        // classUT1 deve fare match per categoria e nome
        ClassUT c1 = createBaseClassUT();
        c1.setName("ProgettoSpeciale");
        c1.setDescription("Descrizione irrilevante");
        c1.setCategories(List.of(catA));
        classUTRepository.save(c1);

        // match con categoria ma non con nome
        ClassUT c2 = createBaseClassUT();
        c2.setName("ProgettoTest");
        c2.setDescription("Ancora testo");
        c2.setCategories(List.of(catB));  // categoria sbagliata
        classUTRepository.save(c2);

        // match con categoria ma non la descrizione
        ClassUT c3 = createBaseClassUT();
        c3.setName("ClasseAlternativa");
        c3.setDescription("Nessun riferimento utile");
        c3.setCategories(List.of(catA)); // categoria giusta
        classUTRepository.save(c3);

        entityManager.flush();
        entityManager.clear();

        // lista con text pari a Prog e categoria A
        List<ClassUT> results = classUTRepository.searchAndFilterByCategory("Prog", "CategoriaA");

        classUTTestUtils.assertListEquals(List.of(c1), results);
    }

    @Test
    void testOrderByDate() {

        // classUT1 con data vecchia
        ClassUT c1 = createBaseClassUT();
        c1.setName("OldClass");
        c1.setDate(LocalDate.of(2023, 2, 15));
        classUTRepository.save(c1);

        // ClassUT2 con data recente
        ClassUT c2 = createBaseClassUT();
        c2.setName("NewClass");
        c2.setDate(LocalDate.of(2024, 1, 10));
        classUTRepository.save(c2);

        entityManager.flush();
        entityManager.clear();

        // lista ordinata in base alla data
        List<ClassUT> results = classUTRepository.orderByDate();

        //verifica che la lista sia correttamente ordinata
        classUTTestUtils.assertListSameOrderEquals(List.of(c1, c2), results);
    }

    @Test
    void testOrderByName() {

        // classUT1 con iniziale nome Z
        ClassUT c1 = createBaseClassUT();
        c1.setName("ZClass");
        classUTRepository.save(c1);

        // ClassUT2 con iniziale nome A
        ClassUT c2 = createBaseClassUT();
        c2.setName("AClass");
        classUTRepository.save(c2);

        entityManager.flush();
        entityManager.clear();

        //lista ordinata tramite iniziale del nome
        List<ClassUT> results = classUTRepository.orderByName();

        //verifica che la lista sia effettivamente ordinata
        classUTTestUtils.assertListSameOrderEquals(List.of(c2, c1), results);
    }

}
