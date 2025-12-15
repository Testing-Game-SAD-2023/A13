package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.util.TestUtils;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.model.Admin;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
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
class TeamRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeamRepository teamRepository;

    private Admin admin;

    // configurazione di comparison per Team
    //ignora i campi di relazione
    public static final RecursiveComparisonConfiguration TEAM_COMPARISON_CONFIG =
            RecursiveComparisonConfiguration.builder()
                    .withComparedFields(
                            "id",
                            "name",
                            "numStudents",
                            "creationDate"
                    )
                    .withEqualsForType(
                            (admin1, admin2) -> admin1.getEmail().equals(admin2.getEmail()),
                            Admin.class
                    )
                    .build();

    // assert personalizzati
    private final TestUtils<Team> teamTestUtils = new TestUtils<>(TEAM_COMPARISON_CONFIG);

    @BeforeEach
    void setup() {
        admin = new Admin();
        admin.setEmail("admin@test.com");
        admin.setNome("Giovanni");
        admin.setCognome("Napolitano");
        entityManager.persist(admin);
    }

    private Team createBaseTeam() {
        Team team = new Team();
        team.setName("Team1");
        team.setNumStudents(3);
        team.setCreationDate(LocalDate.now());
        team.setAdmin(admin);
        team.setStudentIds(new ArrayList<>(List.of("A", "B", "C")));
        return team;
    }

    // TEST CREATE

    @Test
    void testCreateTeam() {
        Team saved = teamRepository.save(createBaseTeam());
        entityManager.flush();
        entityManager.clear();

        Optional<Team> foundOpt = teamRepository.findById(saved.getId());
        assertThat(foundOpt).isPresent();
        Team found = foundOpt.get();
        teamTestUtils.assertEquals(saved, found);
        assertThat(found.getStudentIds()).containsExactlyElementsOf(saved.getStudentIds());
    }

    // TEST UPDATE

    @Test
    void testUpdateTeam() {
        Team team = createBaseTeam();
        teamRepository.save(team);
        entityManager.flush();

        team.setName("TeamUpdated");
        teamRepository.save(team);
        entityManager.flush();
        entityManager.clear();

        Optional<Team> updatedOpt = teamRepository.findById(team.getId());
        assertThat(updatedOpt).isPresent();
        Team updated = updatedOpt.get();
        teamTestUtils.assertEquals(team, updated);
        assertThat(updated.getStudentIds()).containsExactlyElementsOf(team.getStudentIds());
    }

    // TEST DELETE

    @Test
    void testDeleteTeam() {
        Team team = createBaseTeam();
        teamRepository.save(team);
        entityManager.flush();
        entityManager.clear();

        teamRepository.deleteById(team.getId());
        entityManager.flush();

        assertThat(teamRepository.findById(team.getId())).isEmpty();
    }


    // TEST EXISTS BY NAME

    @Test
    void testExistsByName() {
        Team team = createBaseTeam();
        teamRepository.save(team);
        entityManager.flush();
        entityManager.clear();

        boolean exists = teamRepository.existsByName("Team1");
        assertThat(exists).isTrue();

        boolean notExists = teamRepository.existsByName("UnknownTeam");
        assertThat(notExists).isFalse();
    }

    // TEST FIND BY ADMIN EMAIL

    @Test
    void testFindByAdminEmail() {
        Team team = createBaseTeam();
        teamRepository.save(team);
        entityManager.flush();
        entityManager.clear();

        List<Team> expected = List.of(team);

        List<Team> list = teamRepository.findByAdmin_Email("admin@test.com");
        teamTestUtils.assertListEquals(expected, list);
    }

    // TEST FIND BY NAME

    @Test
    void testFindByName() {
        Team team = createBaseTeam();
        teamRepository.save(team);
        entityManager.flush();

        Optional<Team> foundOpt = teamRepository.findByName("Team1");
        assertThat(foundOpt).isPresent();
        Team found = foundOpt.get();
        teamTestUtils.assertEquals(team, found);
        assertThat(found.getStudentIds()).containsExactlyElementsOf(team.getStudentIds());
    }


    // TEST FIND BY ADMIN EMAIL AND NAME

    @Test
    void testFindByAdminEmailAndName() {
        Team team = createBaseTeam();
        teamRepository.save(team);
        entityManager.flush();

        Optional<Team> foundOpt = teamRepository.findByAdmin_EmailAndName("admin@test.com", "Team1");
        assertThat(foundOpt).isPresent();
        Team found = foundOpt.get();
        teamTestUtils.assertEquals(team, found);
        assertThat(found.getStudentIds()).containsExactlyElementsOf(team.getStudentIds());
    }

    // TEST FIND BY STUDENT ID

    @Test
    void testFindByStudentId() {
        Team team = createBaseTeam();
        teamRepository.save(team);
        entityManager.flush();

        Optional<Team> foundOpt = teamRepository.findByStudentId("B");
        assertThat(foundOpt).isPresent();
        Team found = foundOpt.get();
        teamTestUtils.assertEquals(team, found);
        assertThat(found.getStudentIds()).containsExactlyElementsOf(team.getStudentIds());

        Optional<Team> notFound = teamRepository.findByStudentId("Z");
        assertThat(notFound).isEmpty();
    }
}
