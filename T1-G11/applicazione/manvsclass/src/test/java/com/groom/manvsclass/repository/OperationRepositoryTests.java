package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.*;
import com.groom.manvsclass.util.TestUtils;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.test.context.TestPropertySource;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
class OperationRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OperationRepository operationRepository;

    private Admin admin;
    private ClassUT classUT;

    // configurazione di comparison per Operation
    //ignora i campi di relazione
    public static final RecursiveComparisonConfiguration OPERATION_COMPARISON_CONFIG =
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
    private final TestUtils<Operation> operationTestUtils = new TestUtils<>(OPERATION_COMPARISON_CONFIG);

    @BeforeEach
    void setup() {

        // Admin setup
        admin = new Admin();
        admin.setEmail("admin@test.com");
        entityManager.persist(admin);

        // ClassUT setup
        classUT = new ClassUT();
        classUT.setName("JavaBasics");
        classUT.setDate(LocalDate.now());
        classUT.setDifficulty(OpponentDifficulty.EASY);
        classUT.setUri("/path/class");
        classUT.setDescription("Test class");
        entityManager.persist(classUT);
    }

    private Operation createBaseOperation() {
        Operation op = new Operation();
        op.setType(OperationType.UPLOAD);
        op.setDate(LocalDate.now());
        op.setAdmin(admin);
        op.setClassUT(classUT);
        return op;
    }

    // TEST CREATE

    @Test
    void testCreateOperation() {

        Operation operation = createBaseOperation();
        Operation savedOperation = operationRepository.save(operation);

        entityManager.flush();  // forza la scrittura sul database
        entityManager.clear();  // svuota la cache per forzare la lettura dal database

        //ricerca tramite l'id
        Optional<Operation> foundOpt = operationRepository.findById(savedOperation.getId());
        assertThat(foundOpt).isPresent();

        operationTestUtils.assertEquals(operation, savedOperation);
    }

    // TEST UPDATE

    @Test
    void testUpdateOperation() {

        Operation operation = createBaseOperation();
        operationRepository.save(operation);
        entityManager.flush();

        operation.setType(OperationType.DELETE);
        operationRepository.save(operation);
        entityManager.flush();
        entityManager.clear();

        Optional<Operation> operationOpt = operationRepository.findById(operation.getId());
        assertThat(operationOpt).isPresent();

        Operation updatedOperation = operationOpt.get();
        operationTestUtils.assertEquals(operation, updatedOperation);
    }

    @Test
    void testUpdateOperationDate() {

        Operation operation = createBaseOperation();
        operationRepository.save(operation);
        entityManager.flush();

        LocalDate newDate = LocalDate.now().minusDays(5);
        operation.setDate(newDate);
        operationRepository.save(operation);
        entityManager.flush();
        entityManager.clear();

        Optional<Operation> updatedOpt = operationRepository.findById(operation.getId());
        assertThat(updatedOpt).isPresent();
        operationTestUtils.assertEquals(operation, updatedOpt.get());
    }

    // TEST DELETE

    @Test
    void testDeleteOperation() {

        Operation operation = createBaseOperation();
        Operation savedOperation = operationRepository.save(operation);
        entityManager.flush();

        operationRepository.deleteById(savedOperation.getId());
        entityManager.flush();
        entityManager.clear();

        assertThat(operationRepository.findById(savedOperation.getId())).isEmpty();
    }

    @Test
    void testDeleteOperationDoesNotDeleteAdminOrClassUT() {

        Operation operation = createBaseOperation();
        Operation savedOperation = operationRepository.save(operation);
        entityManager.flush();

        operationRepository.deleteById(savedOperation.getId());
        entityManager.flush();

        // Admin deve esistere ancora
        Admin adminFound = entityManager.find(Admin.class, admin.getEmail());
        assertThat(adminFound).isNotNull();

        // ClassUT deve esistere ancora
        ClassUT classFound = entityManager.find(ClassUT.class, classUT.getName());
        assertThat(classFound).isNotNull();
    }

    // TEST FIND

    @Test
    void testFindById() {

        Operation operation = createBaseOperation();
        Operation savedOperation = operationRepository.save(operation);
        entityManager.flush();

        Optional<Operation> opOpt = operationRepository.findById(savedOperation.getId());
        assertThat(opOpt).isPresent();
        operationTestUtils.assertEquals(opOpt.get(), savedOperation);
    }

    @Test
    void testFindByIdNotFound() {

        Optional<Operation> opOpt = operationRepository.findById(999L);
        assertThat(opOpt).isEmpty();
    }

    @Test
    void testFindAll() {

        Operation op1 = createBaseOperation();
        Operation op2 = createBaseOperation();
        op2.setType(OperationType.UPDATE);
        List<Operation> expected = List.of(op1, op2);

        operationRepository.save(op1);
        operationRepository.save(op2);
        entityManager.flush();
        entityManager.clear();

        List<Operation> found = operationRepository.findAll();

        operationTestUtils.assertListEquals(expected, found);
    }
}
