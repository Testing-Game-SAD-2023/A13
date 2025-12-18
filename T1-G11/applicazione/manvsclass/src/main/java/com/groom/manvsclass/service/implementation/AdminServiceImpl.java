package com.groom.manvsclass.service.implementation;

import com.groom.manvsclass.model.entity.AdminEntity;
import com.groom.manvsclass.model.entity.ClassUTEntity;
import com.groom.manvsclass.model.repository.AdminRepository;
import com.groom.manvsclass.model.repository.ClassUTRepository;
import com.groom.manvsclass.service.AdminService;
import com.groom.manvsclass.service.EmailService;
import com.groom.manvsclass.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final ClassUTRepository classUTRepository;
    private final JwtService jwtService;
    private final PasswordEncoder myPasswordEncoder;
    private final EmailService emailService;

    @Autowired
    public AdminServiceImpl(
            AdminRepository adminRepository,
            ClassUTRepository classUTRepository,
            JwtService jwtService,
            PasswordEncoder myPasswordEncoder,
            EmailService emailService) {
        this.adminRepository = adminRepository;
        this.classUTRepository = classUTRepository;
        this.jwtService = jwtService;
        this.myPasswordEncoder = myPasswordEncoder;
        this.emailService = emailService;
    }

    // --- METODO AGGIORNATO PER LA CREAZIONE AUTOMATICA ---
    @Override
    public ResponseEntity<AdminEntity> getAdminByUsername(String username, String jwt) {
        if (jwtService.isJwtValid(jwt)) {

            log.debug("Token valido, ricerca admin per username: {}", username);

            // 1. Cerchiamo l'admin per Username (non per ID, dato che l'ID sembra essere l'email)
            // Assicurati che AdminRepository abbia: AdminEntity findByUsername(String username);
            AdminEntity adminEntity = adminRepository.findByUsername(username);

            if (adminEntity != null) {
                log.debug("Admin trovato nel DB locale: {}", username);
                return ResponseEntity.ok().body(adminEntity);
            } else {
                // 2. SE NON ESISTE: Lo creiamo al volo (Lazy Creation / Sincronizzazione da T23)
                log.info("Admin '{}' non trovato nel DB locale. Creazione automatica.", username);

                AdminEntity newAdmin = new AdminEntity();
                newAdmin.setUsername(username);
                newAdmin.setEmail(username);

                newAdmin.setName("Nome");
                newAdmin.setSurname("Cognome");
                newAdmin.setPassword("password");

                // Salviamo nel DB locale
                adminRepository.saveAndFlush(newAdmin);
                log.info("Nuovo admin salvato correttamente.");

                return ResponseEntity.ok().body(newAdmin);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @Override
    public ResponseEntity<?> loginWithInvitation(AdminEntity adminEntity, String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Attenzione, hai già un token valido!");
        }

        AdminEntity adminEntityRetrieved = adminRepository.findById(adminEntity.getEmail()).orElse(null);
        if (adminEntityRetrieved == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email non trovata");
        }

        AdminEntity adminEntityInvited = adminRepository.findByInvitationToken(adminEntity.getInvitationToken());
        // Fix potenziale NullPointerException: controlliamo se invited è null prima di chiamare i metodi
        if (adminEntityInvited == null || !adminEntityInvited.getInvitationToken().equals(adminEntity.getInvitationToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token di invito invalido!");
        }

        adminEntityRetrieved.setEmail(adminEntity.getEmail());

        if (adminEntity.getName().length() >= 2 && adminEntity.getName().length() <= 30 && Pattern.matches("[a-zA-Z]+(\\s[a-zA-Z]+)*", adminEntity.getName())) {
            adminEntityRetrieved.setName(adminEntity.getName());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome non valido");
        }

        if (adminEntity.getSurname().length() >= 2 && adminEntity.getSurname().length() <= 30 && Pattern.matches("[a-zA-Z]+(\\s?[a-zA-Z]+\\'?)*", adminEntity.getSurname())) {
            adminEntityRetrieved.setSurname(adminEntity.getSurname());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cognome non valido");
        }

        if (adminEntity.getUsername().length() >= 2 && adminEntity.getUsername().length() <= 30 && Pattern.matches(".*_invited$", adminEntity.getUsername())) {
            adminEntityRetrieved.setUsername(adminEntity.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username non valido, deve rispettare il seguente formato: [username di lunghezza compresa tra 2 e 30 caratteri]_invited");
        }

        Matcher m = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$").matcher(adminEntity.getPassword());
        if (adminEntity.getPassword().length() > 16 || adminEntity.getPassword().length() < 8 || !m.matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password non valida! La password deve contenere almeno una lettera maiuscola, una minuscola, un numero ed un carattere speciale e deve essere lunga tra gli 8 e i 16 caratteri");
        }

        String crypted = myPasswordEncoder.encode(adminEntity.getPassword());
        adminEntity.setPassword(crypted);
        adminEntityRetrieved.setPassword(adminEntity.getPassword());

        adminEntity.setInvitationToken(null);
        adminEntityRetrieved.setInvitationToken(adminEntity.getInvitationToken());

        AdminEntity savedAdminEntity = adminRepository.save(adminEntityRetrieved);
        return ResponseEntity.ok().body(savedAdminEntity);
    }

    @Override
    public ResponseEntity<?> inviteAdmins(AdminEntity adminEntity, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Attenzione, non sei loggato");
        }

        //Controlliamo che non esista nel repository un admin con la mail specificata nell'invito
        AdminEntity adminEntityRetrieved = adminRepository.findById(adminEntity.getEmail()).orElse(null);
        if (adminEntityRetrieved != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email trovata, la persona che stai tentando di invitare è già un amministratore!");
        }

        AdminEntity newAdminEntity = new AdminEntity();
        newAdminEntity.setName("default");
        newAdminEntity.setSurname("default");
        newAdminEntity.setUsername("default");
        newAdminEntity.setEmail(adminEntity.getEmail());
        newAdminEntity.setPassword("default");

        String invitationToken = JwtService.generateToken(newAdminEntity);
        newAdminEntity.setInvitationToken(invitationToken);

        AdminEntity savedAdminEntity = adminRepository.save(newAdminEntity);
        try {
            emailService.sendInvitationToken(savedAdminEntity.getEmail(), savedAdminEntity.getInvitationToken());
            return ResponseEntity.ok().body("Invitation token inviato correttamente all'indirizzo:" + savedAdminEntity.getEmail());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nell'invio del messaggio di posta");
        }
    }

    @Override
    public ResponseEntity<List<ClassUTEntity>> filtraClassi(String text, String category, String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            List<ClassUTEntity> classiFiltrate = classUTRepository.searchAndFilter(text, category);
            return ResponseEntity.ok().body(classiFiltrate);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @Override
    public ResponseEntity<List<ClassUTEntity>> filtraClassi(String category, String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            List<ClassUTEntity> classiFiltrate = classUTRepository.findByCategory(category);
            return ResponseEntity.ok().body(classiFiltrate);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}