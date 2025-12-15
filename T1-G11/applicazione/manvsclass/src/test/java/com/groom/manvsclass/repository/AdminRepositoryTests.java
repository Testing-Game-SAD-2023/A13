package com.groom.manvsclass.repository;

import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.util.TestUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@EntityScan(basePackages = "com.groom.manvsclass")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL", // Simula MySQL
        "spring.jpa.show-sql=true",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
public class AdminRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdminRepository adminRepository;

    //config di comparison per Admin, di default confronta tutti i campi tramite equals ignorando i campi delle relazioni, non utili ai fini del test del repository
    public static final RecursiveComparisonConfiguration ADMIN_COMPARISON_CONFIG =
            RecursiveComparisonConfiguration.builder()
                    .withIgnoredFields(
                            "achievements",
                            "interactions",
                            "operations",
                            "scalate",
                            "teams"
                    )
                    .build();

    // assert personalizzati
    private final TestUtils<Admin> adminTestUtils = new TestUtils<>(ADMIN_COMPARISON_CONFIG);

    //Creazione Admin di base per l'utilizzo nei test
    public static Admin createBaseAdmin() {
        Admin admin = new Admin();
        admin.setEmail("admin@test.com");
        admin.setUsername("username");
        admin.setNome("nomeUser");
        admin.setCognome("cognomeUser");
        return admin;
    }

    @Test
    void testSaveAdmin(){
        Admin admin = createBaseAdmin();
        Admin savedAdmin = adminRepository.save(admin);

        entityManager.flush();  // forza la scrittura sul database
        entityManager.clear();  // svuota la cache per forzare la lettura dal database

        //find per verificare l'inserimento nel database
        Optional<Admin> adminOpt = adminRepository.findById(savedAdmin.getEmail());
        assertThat(adminOpt).isPresent();

        //legge l'admin e verifica che sia identico a quello salvato
        Admin foundAdmin = adminOpt.get();
        adminTestUtils.assertEquals(savedAdmin, foundAdmin);
    }

    @Test
    void testUpdateAdmin(){
        Admin admin = createBaseAdmin();
        Admin savedAdmin = adminRepository.save(admin);

        entityManager.flush();

        //update di cambio username
        savedAdmin.setUsername("userUpdated");

        Admin updatedAdmin = adminRepository.save(savedAdmin);
        entityManager.flush();
        entityManager.clear();

        //reload nel database
        Optional<Admin> adminOpt = adminRepository.findById(savedAdmin.getEmail());
        assertThat(adminOpt).isPresent();
        Admin foundAdmin = adminOpt.get();

        //confronto tra admin trovato e admin aggiornato
        adminTestUtils.assertEquals(updatedAdmin, foundAdmin);
    }

    @Test
    void testDeleteAdmin(){
        Admin admin = createBaseAdmin();

        Admin savedAdmin = adminRepository.save(admin);
        entityManager.flush();

        //delete dell'admin tramite il suo id
        adminRepository.deleteById(savedAdmin.getEmail());
        entityManager.flush();
        entityManager.clear();

        //ricerca dell'admin eliminato nel repository
        Optional<Admin> adminOpt = adminRepository.findById(savedAdmin.getEmail());
        //verifica dell'inesistenza nel database
        assertThat(adminOpt).isEmpty();
    }

    @Test
    void testFindAdminByUsername(){
        Admin admin = createBaseAdmin();
        Admin savedAdmin = adminRepository.save(admin);

        entityManager.flush();
        entityManager.clear();

        Optional<Admin> adminOpt = adminRepository.findByUsername(savedAdmin.getUsername());
        assertThat(adminOpt).isPresent();

        //admin trovato tramite username
        Admin foundAdmin = adminOpt.get();

        //confronto tra admin trovato e inserito
        adminTestUtils.assertEquals(savedAdmin, foundAdmin);
    }

    @Test
    void testFindAdminByUsername_NotFound(){
        Admin admin = createBaseAdmin();
        adminRepository.save(admin);

        entityManager.flush();
        entityManager.clear();

        //ricerca di un admin con username inesistente
        Optional<Admin> adminOpt = adminRepository.findByUsername("usernameInesistente");

        assertThat(adminOpt).isEmpty();
    }
//
//    @Test
//    void testFindAdminByResetToken(){
//        Admin admin = createBaseAdmin();
//        Admin savedAdmin = adminRepository.save(admin);
//
//        entityManager.flush();
//        entityManager.clear();
//
//        //ricerca tramite il reset token
//        Optional<Admin> adminOpt = adminRepository.findByResetToken(savedAdmin.getResetToken());
//        assertThat(adminOpt).isPresent();
//
//        Admin foundAdmin = adminOpt.get();
//
//        adminTestUtils.assertEquals(savedAdmin, foundAdmin);
//    }
//
//    @Test
//    void testFindAdminByResetToken_NotFound(){
//
//        Admin admin = createBaseAdmin();
//        adminRepository.save(admin);
//
//        entityManager.flush();
//        entityManager.clear();
//
//        //ricerca tramite un token che non esiste
//        Optional<Admin> adminOpt = adminRepository.findByResetToken("inesistente-token");
//
//        assertThat(adminOpt).isEmpty();
//    }
//
//    @Test
//    void testFindByInvitationToken(){
//        Admin admin = createBaseAdmin();
//        Admin savedAdmin = adminRepository.save(admin);
//
//        entityManager.flush();
//        entityManager.clear();
//
//        //ricerca tramite invitation token
//        Optional<Admin> adminOpt = adminRepository.findByInvitationToken(savedAdmin.getInvitationToken());
//        assertThat(adminOpt).isPresent();
//
//        Admin foundAdmin = adminOpt.get();
//
//        adminTestUtils.assertEquals(savedAdmin, foundAdmin);
//    }
//
//    @Test
//    void testFindByInvitationToken_NotFound(){
//        Admin admin = createBaseAdmin();
//        adminRepository.save(admin);
//
//        entityManager.flush();
//        entityManager.clear();
//
//        //ricerca tramite token di invito inesistente
//        Optional<Admin> adminOpt = adminRepository.findByInvitationToken("inesistente-token");
//        assertThat(adminOpt).isEmpty();
//    }


}
