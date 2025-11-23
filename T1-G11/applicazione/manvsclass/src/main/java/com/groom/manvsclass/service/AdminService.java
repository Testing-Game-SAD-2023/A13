package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.model.ClassUT;

import com.groom.manvsclass.repository.AdminRepository;
import com.groom.manvsclass.repository.ClassUTRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.groom.manvsclass.exception.NotFoundException;
import com.groom.manvsclass.exception.ForbiddenException;

import jakarta.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Optional;

@Service
public class AdminService {

    private final LocalDate today = LocalDate.now();

    @Autowired
    private JwtService jwtService;
    @Autowired
    private ClassUTRepository classUTRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder myPasswordEncoder;

    public boolean existsAdminById(String adminEmail) {

        return adminRepository.existsById(adminEmail);
    }

    public Admin saveAdmin(Admin newAdmin) {

        return adminRepository.save(newAdmin);
    }


    public List<ClassUT> filtraClassi(String category) {

        List<ClassUT> filteredClasses = classUTRepository.findAllByCategoryName(category);
        if(filteredClasses == null || filteredClasses.isEmpty()) {

            throw new NotFoundException("Nessuna classe trovata per la categoria " + category);
        }

        return filteredClasses;
    }

    public List<ClassUT> filtraClassi(String text, String category) {

        List<ClassUT> filteredClasses = classUTRepository.searchAndFilterByCategory(text, category);
        if(filteredClasses == null || filteredClasses.isEmpty()) {

            throw new NotFoundException("Nessuna classe trovata per la categoria " + category + " contenente " + text);
        }

        return filteredClasses;
    }


    // METODO NON UTILIZZATO
    public ResponseEntity<?> inviteAdmins(Admin admin1, String jwt) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Attenzione, non sei loggato");
        }

        // Modifica 09/11/25
        //Controlliamo che non esista nel repository un admin con la mail specificata nell'invito
        Admin admin = adminRepository.findById(admin1.getEmail()).orElse(null);
        if (admin != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email trovata, la persona che stai tentando di invitare è già un amministratore!");
        }

        // NoArgsConstructor + Setter
        Admin newAdmin = new Admin();
        newAdmin.setEmail(admin1.getEmail());
        newAdmin.setNome("default");
        newAdmin.setCognome("default");
        newAdmin.setUsername("default");
        newAdmin.setPassword("default");

        String invitationToken = jwtService.generateToken(newAdmin);
        newAdmin.setInvitationToken(invitationToken);

        Admin savedAdmin = adminRepository.save(newAdmin);
        try {
            emailService.sendInvitationToken(savedAdmin.getEmail(), savedAdmin.getInvitationToken());
            return ResponseEntity.ok().body("Invitation token inviato correttamente all'indirizzo:" + savedAdmin.getEmail());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nell'invio del messaggio di posta");
        }
    }


    // METODO NON UTILIZZATO
    public ResponseEntity<?> loginWithInvitation(Admin admin1, String jwt) {

        if (jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Attenzione, hai già un token valido!");
        }

        Admin admin = adminRepository.findById(admin1.getEmail()).orElse(null);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email non trovata");
        }

        Optional<Admin> adminInvitedOpt = adminRepository.findByInvitationToken(admin1.getInvitationToken());
        if(adminInvitedOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token di invito non valido (utente non trovato)");
        }

        Admin adminInvited = adminInvitedOpt.get();

        if (!adminInvited.getInvitationToken().equals(admin1.getInvitationToken())) {
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cognome non valido!");
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

        Admin savedAdmin = adminRepository.save(admin);
        return ResponseEntity.ok().body(savedAdmin);
    }

    public Admin getAdminByUsername(String username) {

        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        if (adminOpt.isEmpty()) {
            throw new NotFoundException("Impossibile trovare Admin con username: " + username);
        }
        return adminOpt.get();

    }
}