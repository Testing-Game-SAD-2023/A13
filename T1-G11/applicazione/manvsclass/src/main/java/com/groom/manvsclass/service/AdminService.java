/*MODIFICA (5/11/2024) - Refactoring task T1
 * AdminService ora si occupa di implementare i servizi relativi all'Admin
 */
package com.groom.manvsclass.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.groom.manvsclass.controller.EmailService;
import com.groom.manvsclass.model.Achievement;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.Operation;
import com.groom.manvsclass.model.Statistic;
import com.groom.manvsclass.model.filesystem.RobotUtil;
import com.groom.manvsclass.model.filesystem.upload.FileUploadResponse;
import com.groom.manvsclass.model.filesystem.upload.FileUploadUtil;
import com.groom.manvsclass.model.repository.*;
import com.groom.manvsclass.service.*;
import com.commons.model.Gamemode;
import com.commons.model.Robot;
import com.commons.model.StatisticRole;


//MODIFICA (14/05/2024) : Importazione delle classi Scalata e ScalataRepository
import com.fasterxml.jackson.databind.ObjectMapper;

import com.groom.manvsclass.service.JwtService;

//MODIFICA (12/02/2024) : Gestione autenticazione
import com.groom.manvsclass.controller.Authentication.AuthenticatedAdminRepository;
import com.groom.manvsclass.model.Admin;

import com.groom.manvsclass.service.AchievementService;
import com.groom.manvsclass.service.ScalataService;
import com.groom.manvsclass.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.PathVariable;
import io.jsonwebtoken.Claims;

//MODIFICA(11/02/2024): Gestione sessione tramite JWT
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.groom.manvsclass.model.filesystem.download.FileDownloadUtil;
import org.springframework.util.StringUtils;

//MODIFICA (13/02/2024) : Autenticazione token proveniente dai players

 //MODIFICA (15/02/2024) : Servizio di posta elettronica
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

//MODIFICA (11/02/2024) : Controlli sul form registrazione
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AdminService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private final SearchRepositoryImpl srepo;

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

    private final Admin userAdmin= new Admin("default","default","default","default","default");

    //MODIFICA (18/09/2024) : Inizializzazione del repository per gli Achievement
    @Autowired
    private AchievementRepository achievementRepository;

    //MODIFICA (07/10/2024) : Inizializzazione del repository per le Statistiche
    @Autowired
    private StatisticRepository statisticRepository;

    
    private final LocalDate today = LocalDate.now();

    public AdminService(SearchRepositoryImpl srepo) {
	    this.userAdmin.setUsername("default");
	    this.srepo=srepo;
	}

    public ModelAndView showHomeAdmin(HttpServletRequest request, String jwt) {
        if (jwtService.isJwtValid(jwt)) return new ModelAndView("home_adm");
        return new ModelAndView("redirect:/loginAdmin");
    }

    public String showRegistraAdmin() {
        return "registraAdmin";
    }

    public ModelAndView showModificaClasse(HttpServletRequest request, String jwt) {
        if (jwtService.isJwtValid(jwt)) return new ModelAndView("modificaClasse");
        return new ModelAndView("redirect:/loginAdmin");
    }

    public ModelAndView showUploadClasse(HttpServletRequest request, String jwt) {
        if (jwtService.isJwtValid(jwt)) return new ModelAndView("uploadClasse");
        return new ModelAndView("redirect:/loginAdmin");
    }

    public ModelAndView showUploadClasseAndTest(HttpServletRequest request, String jwt) {
        if (jwtService.isJwtValid(jwt)) return new ModelAndView("uploadClasseAndTest");
        return new ModelAndView("redirect:/loginAdmin");
    }

    public ModelAndView showReportClasse(HttpServletRequest request, String jwt) {
        if (jwtService.isJwtValid(jwt)) return new ModelAndView("reportClasse");
        return new ModelAndView("redirect:/loginAdmin");
    }

    public ModelAndView showReports(HttpServletRequest request, String jwt) {
        if (jwtService.isJwtValid(jwt)) return new ModelAndView("Reports");
        return new ModelAndView("redirect:/loginAdmin");
    }

    public ResponseEntity<List<ClassUT>> ordinaClassi(String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            List<ClassUT> classiOrdinate = srepo.orderByDate();
            return ResponseEntity.ok().body(classiOrdinate);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    public ResponseEntity<List<ClassUT>> ordinaClassiNomi(String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            List<ClassUT> classiOrdinateNome = srepo.orderByName();
            return ResponseEntity.ok().body(classiOrdinateNome);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
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

    public ResponseEntity<List<ClassUT>> elencaClassiD(String difficulty, String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            List<ClassUT> classiFiltrate = srepo.filterByDifficulty(difficulty);
            return ResponseEntity.ok().body(classiFiltrate);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    public ResponseEntity<List<ClassUT>> elencaClassiD(String text, String difficulty, String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            List<ClassUT> classiFiltrate = srepo.searchAndDFilter(text, difficulty);
            return ResponseEntity.ok().body(classiFiltrate);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    public ResponseEntity<ClassUT> uploadClasse(ClassUT classe, String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            System.out.println("Token valido, può inserire una nuova classe (/insert)");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String data = currentDate.format(formatter);
            Operation operation1 = new Operation((int) orepo.count(), userAdmin.getUsername(), classe.getName(), 0, data);
            orepo.save(operation1);
            ClassUT savedClasse = repo.save(classe);

            System.out.println("Inserimento classe avvenuto con successo (/insert)");
            return ResponseEntity.ok().body(savedClasse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    public ResponseEntity<FileUploadResponse> uploadFile(MultipartFile classFile, String model, String jwt, HttpServletRequest request) throws IOException {
        if (jwtService.isJwtValid(jwt)) {
            //Legge i metadati della classe della parte "model" del body HTTP e li salva in un oggetto ClasseUT
            ObjectMapper mapper = new ObjectMapper();
            ClassUT classe = mapper.readValue(model, ClassUT.class);

            //Salva il nome del file caricato
            String fileName = StringUtils.cleanPath(classFile.getOriginalFilename());
            long size = classFile.getSize();
            
            //Salva la classe nel filesystem condiviso
            FileUploadUtil.saveCLassFile(fileName, classe.getName(), classFile);
            //Genera e salva i test nel filesystem condiviso
            RobotUtil.generateAndSaveRobots(fileName, classe.getName(), classFile);
            
            //Prepara la risposta per il front-end
            FileUploadResponse response = new FileUploadResponse();
            response.setFileName(fileName);
            response.setSize(size);
            response.setDownloadUri("/downloadFile");
            
            //Setta data di caricamento e percorso di download
            classe.setUri("Files-Upload/" + classe.getName() + "/" + fileName);
            classe.setDate(today.toString());

            //Creazione dell'oggetto riguardante l'operazione appena fatta
            LocalDate currentDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String data = currentDate.format(formatter);
			Operation operation1= new Operation((int)orepo.count(),userAdmin.getUsername(),classe.getName(),0,data);
            
            //Salva i dati sull'operazione fatta nel database
            orepo.save(operation1);
            //Salva i dati sulla classe nel database
            repo.save(classe);
            System.out.println("Operazione completata con successo (uploadFile)");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            FileUploadResponse response = new FileUploadResponse();
            response.setErrorMessage("Errore, il token non è valido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<FileUploadResponse> uploadTest(MultipartFile classFile, String model, MultipartFile testFile, MultipartFile testFileEvo, String jwt, HttpServletRequest request) throws IOException {
        if (jwtService.isJwtValid(jwt)) {
            System.out.println("Token valido (uploadTest)");
            ObjectMapper mapper = new ObjectMapper();
            ClassUT classe = mapper.readValue(model, ClassUT.class);

            String fileNameClass = StringUtils.cleanPath(classFile.getOriginalFilename());
			long size = classFile.getSize();

           System.out.println("Salvataggio di "+fileNameClass+"nel filestystem condiviso");
			FileUploadUtil.saveCLassFile(fileNameClass, classe.getName(), classFile);

            String fileNameTest = StringUtils.cleanPath(testFile.getOriginalFilename());
			String fileNameTestEvo = StringUtils.cleanPath(testFileEvo.getOriginalFilename());
			RobotUtil.saveRobots(fileNameClass, fileNameTest,fileNameTestEvo , classe.getName(), classFile ,testFile, testFileEvo);

            FileUploadResponse response = new FileUploadResponse();
            response.setFileName(fileNameClass);
            response.setSize(size);
            response.setDownloadUri("/downloadFile");

            classe.setUri("Files-Upload/" + classe.getName() + "/" + fileNameClass);
            classe.setDate(today.toString());

            LocalDate currentDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String data = currentDate.format(formatter);
			Operation operation1 = new Operation((int) orepo.count(), userAdmin.getUsername(), classe.getName() + " con Robot", 0, data);
            
            orepo.save(operation1);
            repo.save(classe);
            System.out.println("Operazione completata con successo (uploadTest)");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            FileUploadResponse response = new FileUploadResponse();
            response.setErrorMessage("Errore, il token non è valido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<?> eliminaClasse(String name, String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            Query query = new Query();
            query.addCriteria(Criteria.where("name").is(name));
            eliminaFile(name, jwt);
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String data = currentDate.format(formatter);
            Operation operation1 = new Operation((int) orepo.count(), "userAdmin", name, 2, data);
            orepo.save(operation1);
            ClassUT deletedClass = mongoTemplate.findAndRemove(query, ClassUT.class);
            if (deletedClass != null) {
                return ResponseEntity.ok().body(deletedClass);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe non trovata");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido");
        }
    }

    public ResponseEntity<String> eliminaFile(String fileName, String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            String folderPath = "Files-Upload/" + fileName;
            File directoryRandoop = new File("/VolumeT9/app/FolderTree/" + fileName);
            File directoryEvo = new File("/VolumeT8/FolderTreeEvo/" + fileName);
            File folderToDelete = new File(folderPath);
            if (folderToDelete.exists() && folderToDelete.isDirectory()) {
                try {
                    FileUploadUtil.deleteDirectory(folderToDelete);
                    FileUploadUtil.deleteDirectory(directoryRandoop);
                    FileUploadUtil.deleteDirectory(directoryEvo);
                    return new ResponseEntity<>("Cartella eliminata con successo (/deleteFile/{fileName})", HttpStatus.OK);
                } catch (IOException e) {
                    return new ResponseEntity<>("Impossibile eliminare la cartella.", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("Cartella non trovata.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Token JWT non valido.", HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<String> modificaClasse(String name, ClassUT newContent, String jwt, HttpServletRequest request) {
        if (jwtService.isJwtValid(jwt)) {
            System.out.println("Token valido, può aggiornare informazioni inerenti le classi (update/{name})");
            Query query = new Query();
            query.addCriteria(Criteria.where("name").is(name));
            Update update = new Update().set("name", newContent.getName())
                    .set("date", newContent.getDate())
                    .set("difficulty", newContent.getDifficulty())
                    .set("description", newContent.getDescription())
                    .set("category", newContent.getCategory());
            long modifiedCount = mongoTemplate.updateFirst(query, update, ClassUT.class).getModifiedCount();

            if (modifiedCount > 0) {
                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String data = currentDate.format(formatter);
                Operation operation1 = new Operation((int) orepo.count(), userAdmin.getUsername(), newContent.getName(), 1, data);
                orepo.save(operation1);
                return new ResponseEntity<>("Aggiornamento eseguito correttamente.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Nessuna classe trovata o nessuna modifica effettuata.", HttpStatus.NOT_FOUND);
            }
        } else {
            System.out.println("Token non valido ((update/{name}))");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore nel completamente dell'operazione");
        }
    }

    public ResponseEntity<?> registraAdmin(Admin admin1, String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Already logged in");
        }

        //Controlli
		//1: Possibilità di inserire più nomi separati da uno spazio
        if (admin1.getNome().length() >= 2 && admin1.getNome().length() <= 30 && Pattern.matches("[a-zA-Z]+(\\s[a-zA-Z]+)*", admin1.getNome())) {
            this.userAdmin.setNome(admin1.getNome());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome non valido");
        }

        //2: Possibilità di inserire più parole separate da uno spazio ed eventualmente da un apostrofo
        if (admin1.getCognome().length() >= 2 && admin1.getCognome().length() <= 30 && Pattern.matches("[a-zA-Z]+(\\s?[a-zA-Z]+\\'?)*", admin1.getCognome())) {
            this.userAdmin.setCognome(admin1.getCognome());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cognome non valido");
        }

        //3: L'username deve rispettare necessariamente il seguente formato: "[username di lunghezza compresa tra 2 e 30 caratteri]_unina"
        if (admin1.getUsername().length() >= 2 && admin1.getUsername().length() <= 30 && Pattern.matches(".*_unina$", admin1.getUsername())) {
            this.userAdmin.setUsername(admin1.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username non valido, deve rispettare il seguente formato: [username di lunghezza compresa tra 2 e 30 caratteri]_unina");
        }

        //4: L'email deve essere necessariamente quella istituzionale e terminare: o con [nome]@studenti.unina.it oppure [nome]@unina.it
        if (Pattern.matches("^[a-zA-Z0-9._%+-]+@(?:studenti\\.)?unina\\.it$", admin1.getEmail())) {
            Admin existingAdmin = arepo.findById(admin1.getEmail()).orElse(null);
            if (existingAdmin != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin con questa mail già registrato");
            }
            this.userAdmin.setEmail(admin1.getEmail());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email non valida, registrarsi con le credenziali istituzionali!");
        }

        //5: La password deve contenere almeno una lettera maiuscola, una minuscola, un numero ed un carattere speciale e deve essere lunga tra gli 8 e i 16 caratteri
        Matcher m = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$").matcher(admin1.getPassword());
        if (admin1.getPassword().length() > 16 || admin1.getPassword().length() < 8 || !m.matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password non valida! La password deve contenere almeno una lettera maiuscola, una minuscola, un numero ed un carattere speciale e deve essere lunga tra gli 8 e i 16 caratteri");
        }

        String crypted = myPasswordEncoder.encode(admin1.getPassword());
        this.userAdmin.setPassword(crypted);

        Admin savedAdmin = arepo.save(this.userAdmin);
        return ResponseEntity.ok().body(savedAdmin);
    }

    // admin altro

    public Object getAllAdmins(String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            return arepo.findAll();
        } else {
            return new RedirectView("/loginAdmin");
        }
    }

    public ModelAndView showAdmin(HttpServletRequest request, String jwt) {
        if (jwtService.isJwtValid(jwt)) return new ModelAndView("info");
        return new ModelAndView("login_admin");
    }

    public ResponseEntity<?> changePasswordAdmin(Admin admin1, String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Già loggato");
        }

        Admin admin = arepo.findById(admin1.getEmail()).orElse(null);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email non trovata");
        }

        Admin admin_reset = srepo.findAdminByResetToken(admin1.getResetToken());
        if (!admin_reset.getResetToken().equals(admin1.getResetToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token di reset invalido!");
        }

        Matcher m = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$").matcher(admin1.getPassword());
        if (admin1.getPassword().length() > 16 || admin1.getPassword().length() < 8 || !m.matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password non valida! La password deve contenere almeno una lettera maiuscola, una minuscola, un numero ed un carattere speciale e deve essere lunga tra gli 8 e i 16 caratteri");
        }

        String crypted = myPasswordEncoder.encode(admin1.getPassword());
        admin1.setPassword(crypted);
        admin.setPassword(admin1.getPassword());
        admin1.setResetToken(null);
        admin.setResetToken(admin1.getResetToken());

        Admin savedAdmin = arepo.save(admin);
        return ResponseEntity.ok().body(savedAdmin);
    }

    public ModelAndView showChangePswAdminForm(HttpServletRequest request, String jwt) {
        if (jwtService.isJwtValid(jwt)) return new ModelAndView("redirect:/login_admin");
        return new ModelAndView("password_change_admin");
    }

    public ResponseEntity<?> resetPasswordAdmin(Admin admin1, String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Già loggato");
        }

        Admin admin = arepo.findById(admin1.getEmail()).orElse(null);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email non trovata");
        }

        String resetToken = jwtService.generateToken(admin);
        admin.setResetToken(resetToken);

        Admin savedAdmin = arepo.save(admin);
        try {
            emailService.sendPasswordResetEmail(savedAdmin.getEmail(), savedAdmin.getResetToken());
            return ResponseEntity.ok().body(savedAdmin);
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nell'invio del messaggio di posta");
        }
    }

    public ModelAndView showResetPswAdminForm(HttpServletRequest request, String jwt) {
        if (jwtService.isJwtValid(jwt)) return new ModelAndView("redirect:/login_admin");
        return new ModelAndView("password_reset_admin");
    }

    public ModelAndView showInviteAdmins(HttpServletRequest request, String jwt) {
        if (jwtService.isJwtValid(jwt)) return new ModelAndView("invite_admins");
        return new ModelAndView("login_admin");
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

    public ModelAndView showLoginWithInvitationForm(HttpServletRequest request, String jwt) {
        if (jwtService.isJwtValid(jwt)) return new ModelAndView("login_admin");
        return new ModelAndView("login_with_invitation");
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

    public ModelAndView showAchievementsPage(HttpServletRequest request, String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            ModelAndView model = new ModelAndView("achievements");

            List<Gamemode> allGamemodes = Arrays.asList(Gamemode.values());
            List<StatisticRole> allRoles = Arrays.asList(StatisticRole.values());
            List<Robot> allRobots = Arrays.asList(Robot.values());

            List<Statistic> allStatistics = statisticRepository.findAll();

            model.addObject("gamemodesList", allGamemodes);
            model.addObject("rolesList", allRoles);
            model.addObject("robotsList", allRobots);
            model.addObject("statisticsList", allStatistics);

            return model;
        }

        return new ModelAndView("login_admin");
    }

    public ResponseEntity<?> listAchievements() {
        List<Achievement> achievements = achievementRepository.findAll();
        return new ResponseEntity<>(achievements, HttpStatus.OK);
    }

    public Object createAchievement(Achievement achievement, String jwt, HttpServletRequest request) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("(POST /createAchivement) Attenzione, non sei loggato!");
        }

        achievementRepository.save(achievement);
        return showAchievementsPage(request, jwt);
    }

    public ResponseEntity<?> listStatistics() {
        List<Statistic> statistics = statisticRepository.findAll();
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    public Object createStatistic(Statistic statistic, String jwt, HttpServletRequest request) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("(POST /createStatistic) Attenzione, non sei loggato!");
        }

        statisticRepository.save(statistic);
        return showAchievementsPage(request, jwt);
    }

    public Object deleteStatistic(String Id, String jwt, HttpServletRequest request) {
        if (!jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("(POST /deleteStatistic) Attenzione, non sei loggato!");
        }

        statisticRepository.deleteById(Id);
        return new ModelAndView("achievements");
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

    public ModelAndView showPlayer(HttpServletRequest request, String jwt) {
        if(jwtService.isJwtValid(jwt)) return new ModelAndView("player");

        return new ModelAndView("login_admin");
    }

    public ModelAndView showClass(HttpServletRequest request, String jwt) {
        if(jwtService.isJwtValid(jwt)) return new ModelAndView("class");

        return new ModelAndView("login_admin");
    }

    public ResponseEntity<String> loginAdmin(Admin admin1, String jwt, HttpServletResponse response) {
        if (jwtService.isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sei già loggato.");
        }

        if (admin1.getUsername().isEmpty() || admin1.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Per favore, compila tutti i campi!");
        }

        Admin admin = srepo.findAdminByUsername(admin1.getUsername());

        // (MODIFICA 14/05/2024) Check se admin esiste già
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore nella fase di login, admin non trovato.");
        }

        boolean passwordMatches = myPasswordEncoder.matches(admin1.getPassword(), admin.getPassword());

        if (passwordMatches) {
            String token = jwtService.generateToken(admin);

            Cookie jwtTokenCookie = new Cookie("jwt", token);
            jwtTokenCookie.setMaxAge(3600);
            response.addCookie(jwtTokenCookie);
            
            return ResponseEntity.ok("Autenticazione avvenuta con successo");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore nella fase di login, dati non corretti.");
        }
    }

    public ResponseEntity<?> downloadClasse(@PathVariable("name") String name) throws Exception {

        System.out.println("/downloadFile/{name} (HomeController) - name: "+ name);
		System.out.println("test");
		try{
			List<ClassUT> classe= srepo.findByText(name);
			System.out.println("File download:");
			System.out.println(classe.get(0).getcode_Uri());
			ResponseEntity file =  FileDownloadUtil.downloadClassFile(classe.get(0).getcode_Uri());
			return file;
		}
		catch(Exception e){
			System.out.println("Eccezione------------");
			return new ResponseEntity<>("Cartella non trovata.", HttpStatus.NOT_FOUND);
		}
    }

    public    List<ClassUT>    ricercaClasse(@PathVariable String text) {
        return srepo.findByText(text);
    }

    public ModelAndView getLoginForm(String jwt) {
        if (jwtService.isJwtValid(jwt)) {
            return new ModelAndView("redirect:/home_adm");
        } else {
            return new ModelAndView("login_admin");
        }
    }

    public void logout(HttpServletResponse response) {
        // Crea un cookie JWT nullo per invalidare il token
        Cookie jwtTokenCookie = new Cookie("jwt", null);
        jwtTokenCookie.setMaxAge(0); // Imposta l'età massima a 0 per eliminarlo
        response.addCookie(jwtTokenCookie);
        
        // Logica aggiuntiva, se necessaria (es. invalidazione sessione)
        System.out.println("Logout eseguito.");
    }

    public String test() {
        return "test T1";
    }

    public List<ClassUT> elencaClassi() {
        return repo.findAll();
    }


    /*Modifica 29/11/2024: Creazione metodi per reindirizzare alle view Teams e Assignments*/
    public ModelAndView showGestioneTeams(HttpServletRequest request, String jwt) {
        if (jwtService.isJwtValid(jwt)) {return new ModelAndView("/gestione_teams");}
        
        return new ModelAndView("redirect:/loginAdmin");
    }

    public ModelAndView showGestioneAssignments(HttpServletRequest request, String jwt) {
        if (jwtService.isJwtValid(jwt)) return new ModelAndView("/gestione_assignments");
        return new ModelAndView("redirect:/loginAdmin");
    }


}