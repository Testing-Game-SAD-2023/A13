/*MODIFICA (5/11/2024) - Refactoring task T1
 * AdminService ora si occupa di implementare i servizi relativi all'Admin
 */
package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.repository.AdminRepository;
import com.groom.manvsclass.model.repository.ClassRepository;
import com.groom.manvsclass.model.repository.OperationRepository;
import com.groom.manvsclass.model.repository.SearchRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AdminService {

    @Autowired
    private final SearchRepositoryImpl srepo;
    private final Admin userAdmin = new Admin("default", "default", "default", "default", "default");
    private final LocalDate today = LocalDate.now();
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ClassRepository repo;
    @Autowired
    private OperationRepository orepo;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private AdminRepository arepo;
    //MODIFICA (15/02/2024) : Servizio di posta elettronica
    @Autowired
    private EmailService emailService;
    //MODIFICA (11/02/2024) : Controlli sul form registrazione
    @Autowired
    private PasswordEncoder myPasswordEncoder;

    public AdminService(SearchRepositoryImpl srepo) {
        this.userAdmin.setUsername("default");
        this.srepo = srepo;
    }


    public ResponseEntity<List<ClassUT>> filtraClassi(String category, String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            List<ClassUT> classiFiltrate = srepo.filterByCategory(category);
            return ResponseEntity.ok().body(classiFiltrate);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    public ResponseEntity<List<ClassUT>> filtraClassi(String text, String category, String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            List<ClassUT> classiFiltrate = srepo.searchAndFilter(text, category);
            return ResponseEntity.ok().body(classiFiltrate);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    public ResponseEntity<?> inviteAdmins(Admin admin1, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Attenzione, non sei loggato");
        }

        //Controlliamo che non esista nel repository un admin con la mail specificata nell'invito
        Admin admin = arepo.findById(admin1.getEmail()).orElse(null);
        if (admin != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email trovata, la persona che stai tentando di invitare è già un amministratore!");
        }

        Admin new_admin = new Admin("default", "default", "default", "default", "default");
        new_admin.setEmail(admin1.getEmail());

        String invitationToken = jwtService.generateToken(new_admin);
        new_admin.setInvitationToken(invitationToken);

        Admin savedAdmin = arepo.save(new_admin);
        try {
            emailService.sendInvitationToken(savedAdmin.getEmail(), savedAdmin.getInvitationToken());
            return ResponseEntity.ok().body("Invitation token inviato correttamente all'indirizzo:" + savedAdmin.getEmail());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nell'invio del messaggio di posta");
        }
    }

    public ResponseEntity<?> loginWithInvitation(Admin admin1, String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Attenzione, hai già un token valido!");
        }

        Admin admin = arepo.findById(admin1.getEmail()).orElse(null);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email non trovata");
        }

        Admin admin_invited = srepo.findAdminByInvitationToken(admin1.getInvitationToken());
        if (!admin_invited.getInvitationToken().equals(admin1.getInvitationToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token di invito invalido!");
        }

        admin.setEmail(admin1.getEmail());

        if (admin1.getNome().length() >= 2 && admin1.getNome().length() <= 30 && Pattern.matches("[a-zA-Z]+(\\s[a-zA-Z]+)*", admin1.getNome())) {
            admin.setNome(admin1.getNome());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome non valido");
        }

        if (admin1.getCognome().length() >= 2 && admin1.getCognome().length() <= 30 && Pattern.matches("[a-zA-Z]+(\\s?[a-zA-Z]+\\'?)*", admin1.getCognome())) {
            admin.setCognome(admin1.getCognome());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cognome non valido");
        }

        if (admin1.getUsername().length() >= 2 && admin1.getUsername().length() <= 30 && Pattern.matches(".*_invited$", admin1.getUsername())) {
            admin.setUsername(admin1.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username non valido, deve rispettare il seguente formato: [username di lunghezza compresa tra 2 e 30 caratteri]_invited");
        }

        Matcher m = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$").matcher(admin1.getPassword());
        if (admin1.getPassword().length() > 16 || admin1.getPassword().length() < 8 || !m.matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password non valida! La password deve contenere almeno una lettera maiuscola, una minuscola, un numero ed un carattere speciale e deve essere lunga tra gli 8 e i 16 caratteri");
        }

        String crypted = myPasswordEncoder.encode(admin1.getPassword());
        admin1.setPassword(crypted);
        admin.setPassword(admin1.getPassword());

        admin1.setInvitationToken(null);
        admin.setInvitationToken(admin1.getInvitationToken());

        Admin savedAdmin = arepo.save(admin);
        return ResponseEntity.ok().body(savedAdmin);
    }

    public ResponseEntity<Admin> getAdminByUsername(String username, String jwt) {
        if (jwtService.isJwtValid(jwt)) {

            System.out.println("Token valido, può ricercare admin per username (/admins/{username})");
            Admin admin = srepo.findAdminByUsername(username);
            if (admin != null) {

                System.out.println("Operazione avvenuta con successo (/admins/{username})");
                return ResponseEntity.ok().body(admin);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Ritorna 404 Not Found
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Ritorna 401 Unauthorized
        }
    }

}