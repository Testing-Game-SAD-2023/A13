package com.groom.manvsclass.repository;

import com.groom.manvsclass.util.TestUtils;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.test.context.TestPropertySource;

import com.groom.manvsclass.model.Assignment;
import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.model.Admin;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Optional;

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
class AssignmentRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AssignmentRepository assignmentRepository;

    private Team team;

    // configurazione di comparison per Assignment
    // configurazione di default: confronta tutti i parametri con equals
    //ignora i campi delle relazioni
    public static final RecursiveComparisonConfiguration ASSIGNMENT_COMPARISON_CONFIG =
            RecursiveComparisonConfiguration.builder()
                    .withComparedFields(
                            "id",
                            "title",
                            "description",
                            "creationDate",
                            "expirationDate"
                    )
                    .withEqualsForType(
                            (team1, team2) -> team1.getId().equals(team2.getId()),
                            Team.class
                    )
                    .build();

    // assert personalizzati
    private final TestUtils<Assignment> assignmentTestUtils = new TestUtils<>(ASSIGNMENT_COMPARISON_CONFIG);

    //creazione Assignment base
    public static Assignment createBaseAssignment(Team team) {
        Assignment a = new Assignment();
        a.setTitle("Titolo Assegnazione");
        a.setDescription("Descrizione base");
        a.setCreationDate(LocalDate.now());
        a.setExpirationDate(LocalDate.now().plusDays(3));
        a.setTeam(team);
        return a;
    }

    @BeforeEach
    void teamSetup() {

        Admin admin = new Admin();
        admin.setEmail("testAdmin@gmail.com");
        entityManager.persist(admin);

        this.team = new Team();
        team.setName("Team");
        team.setNumStudents(3);
        team.setCreationDate(LocalDate.now());
        team.setAdmin(admin);
        team.setStudentIds(List.of("A", "B", "C"));

        entityManager.persist(team);
    }

    //TEST CREATE

    @Test
    void testSaveAssignment() {

        Assignment assignment = createBaseAssignment(team);
        Assignment savedAssignment = assignmentRepository.save(assignment);

        entityManager.flush();  // forza la scrittura sul database
        entityManager.clear();  // svuota la cache per forzare la lettura dal database

        // per verificare il corretto inserimento nel database effettua una find
        Optional<Assignment> assignmentOpt = assignmentRepository.findById(savedAssignment.getId());
        assertThat(assignmentOpt).isPresent();

        Assignment foundAssignment = assignmentOpt.get();
        assignmentTestUtils.assertEquals(savedAssignment, foundAssignment);
    }

    // TEST UPDATE

    @Test
    void testUpdateAssignment() {

        Assignment assignment = createBaseAssignment(team);
        Assignment savedAssignment = assignmentRepository.save(assignment);
        entityManager.flush();

        savedAssignment.setTitle("Titolo_Aggiornato");
        savedAssignment.setDescription("Descrizione aggiunta");

        Assignment updatedAssignment = assignmentRepository.save(savedAssignment);
        entityManager.flush();
        entityManager.clear();

        Optional<Assignment> assignmentOpt = assignmentRepository.findById(assignment.getId());
        assertThat(assignmentOpt).isPresent();

        Assignment foundAssignment = assignmentOpt.get();
        assignmentTestUtils.assertEquals(updatedAssignment, foundAssignment);
    }

    // TEST DELETE

    @Test
    void testDeleteAssignment() {

        Assignment assignment = createBaseAssignment(team);
        Assignment savedAssignment = assignmentRepository.save(assignment);
        entityManager.flush();

        assignmentRepository.deleteById(savedAssignment.getId());
        entityManager.flush();
        entityManager.clear();

        assertThat(assignmentRepository.findById(savedAssignment.getId())).isEmpty();
    }

    //TEST FINDBYTEAMID
    @Test
    void testFindByTeamId() {

        //creazione di due assignment da ricercare tramite id del team
        Assignment a1 = assignmentRepository.save(createBaseAssignment(team));
        Assignment a2 = assignmentRepository.save(createBaseAssignment(team));
        List<Assignment> expected = List.of(a1, a2);

        entityManager.flush();
        entityManager.clear();

        List<Assignment> results = assignmentRepository.findByTeam_Id(team.getId());

        //verifica che la lista ottenuta contenga effettivamente i due assignment creati
        assignmentTestUtils.assertListEquals(results, expected);
    }

    @Test
    void testFindByTeamId_Empty() {

        List<Assignment> results = assignmentRepository.findByTeam_Id(999L);

        assertThat(results).isEmpty();
    }

    //TEST FINDALLBYTEAMIDIN
    @Test
    void testFindAllByTeamIdIn() {

        //creazione secondo team per avere assegnazione appartenente a più team
        Team otherTeam = new Team();
        otherTeam.setName("AltroTeam");
        otherTeam.setNumStudents(2);
        otherTeam.setCreationDate(LocalDate.now());
        otherTeam.setAdmin(team.getAdmin());
        otherTeam.setStudentIds(List.of("X", "Y"));
        entityManager.persist(otherTeam);

        //creazione assignment
        Assignment a1 = assignmentRepository.save(createBaseAssignment(team));

        Assignment a2 = createBaseAssignment(otherTeam);
        assignmentRepository.save(a2);

        entityManager.flush();
        entityManager.clear();

        List<Assignment> results =
                assignmentRepository.findAllByTeam_IdIn(List.of(team.getId(), otherTeam.getId()));

        List<Assignment> expectedResults = List.of(a1, a2);
        //verifica che la lista contenga esattamente i due team creati
        assignmentTestUtils.assertListEquals(expectedResults, results);
    }

    //
    @Test
    void testFindAllByTeamIdIn_Empty() {

        List<Assignment> results =
                assignmentRepository.findAllByTeam_IdIn(List.of(111L, 222L));

        assertThat(results).isEmpty();
    }

    //TEST FINDALLBYTEAMNAME
    @Test
    void testFindAllByTeamName() {

        Assignment a1 = assignmentRepository.save(createBaseAssignment(team));
        Assignment a2 = assignmentRepository.save(createBaseAssignment(team));

        entityManager.flush();
        entityManager.clear();

        List<Assignment> results =
                assignmentRepository.findAllByTeam_Name(team.getName());

        List<Assignment> expectedResults = List.of(a1, a2);
        //verifica che la lista contenga esattamente i due team creati
        assignmentTestUtils.assertListEquals(expectedResults, results);
    }

    @Test
    void testFindAllByTeamName_Empty() {

        List<Assignment> results =
                assignmentRepository.findAllByTeam_Name("None");

        assertThat(results).isEmpty();
    }

    //TEST FINDBYTITLE
    @Test
    void testFindByTitle() {

        Assignment a = createBaseAssignment(team);
        a.setTitle("TitoloSpeciale");
        Assignment saved = assignmentRepository.save(a);

        entityManager.flush();
        entityManager.clear();

        Optional<Assignment> result =
                assignmentRepository.findByTitle("TitoloSpeciale");

        assertThat(result).isPresent();
        assignmentTestUtils.assertEquals(saved, result.get());
    }

    @Test
    void testFindByTitle_Empty() {

        Optional<Assignment> result =
                assignmentRepository.findByTitle("TitoloCheNonEsiste");

        assertThat(result).isEmpty();
    }
}
