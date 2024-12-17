/*MODIFICA (5/11/2024) - Refactoring task T1
 * HomeController ora si occupa solo del mapping dei servizi aggiunti.
 */

 package com.groom.manvsclass.controller;

 import java.io.IOException;
 import java.util.*;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.springframework.http.ResponseEntity;
 import com.groom.manvsclass.model.ClassUT;
 import org.springframework.web.multipart.MultipartFile;
 import com.groom.manvsclass.model.filesystem.upload.FileUploadResponse;
 import com.groom.manvsclass.model.Admin; 
 import com.groom.manvsclass.model.Achievement; 
 import com.groom.manvsclass.model.Statistic; 
 import com.groom.manvsclass.model.interaction; 
 import com.groom.manvsclass.model.Scalata; 
 
 import com.groom.manvsclass.service.AchievementService;
 import com.groom.manvsclass.service.AdminService;
 import com.groom.manvsclass.service.ScalataService;
 import com.groom.manvsclass.service.Util;
 
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.CookieValue;
 import org.springframework.web.bind.annotation.CrossOrigin;
 import org.springframework.web.bind.annotation.DeleteMapping;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.PathVariable;
 import org.springframework.web.bind.annotation.PostMapping;
 import org.springframework.web.bind.annotation.RequestBody;
 import org.springframework.web.bind.annotation.RequestParam;
 import org.springframework.web.bind.annotation.ResponseBody;
 import org.springframework.web.servlet.ModelAndView;
 import org.springframework.http.HttpStatus;

 
 @CrossOrigin
 @Controller
 public class HomeController {
 
     // MODIFICA (5/11/2024) - Inizializzazione dei servizi aggiunti
     @Autowired
     private AdminService adminService;
 
     // MODIFICA (5/11/2024) - Inizializzazione dei servizi aggiunti
     @Autowired
     private ScalataService scalataService;
 
     // MODIFICA (5/11/2024) - Inizializzazione dei servizi aggiunti
     @Autowired
     private AchievementService achievementService;
 
     // MODIFICA (5/11/2024) - Inizializzazione dei servizi aggiunti
     @Autowired
     private Util utilsService;
 
     @GetMapping("/home_adm")
     public ModelAndView showHomeAdmin(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.showHomeAdmin(request, jwt);
     }
 
     @GetMapping("/registraAdmin")
     public String showRegistraAdmin() {
         return adminService.showRegistraAdmin();
     }
 
     //MODIFICA (11/02/2024) : Gestione sessione tramite JWT
     @GetMapping("/modificaClasse")
     public ModelAndView showModificaClasse(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.showModificaClasse(request, jwt);
     }
 
     @GetMapping("/uploadClasse")
     public ModelAndView showUploadClasse(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.showUploadClasse(request, jwt);
     }
 
     @GetMapping("/uploadClasseAndTest")
     public ModelAndView showUploadClasseAndTest(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.showUploadClasseAndTest(request, jwt);
     }
 
     @GetMapping("/reportClasse")
     public ModelAndView showReportClasse(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.showReportClasse(request, jwt);
     }
 
     @GetMapping("/Reports")
     public ModelAndView showReports(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.showReports(request, jwt);
     }
 
     @GetMapping("/orderbydate")
     @ResponseBody
     public ResponseEntity<List<ClassUT>> ordinaClassi(@CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.ordinaClassi(jwt);
     }
 
     @GetMapping("/orderbyname")
     @ResponseBody
     public ResponseEntity<List<ClassUT>> ordinaClassiNomi(@CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.ordinaClassiNomi(jwt);
     }

    /* MODIFICA 29/11/2024: Aggiunta EndPoint: /teams */

    @GetMapping("/teams")
    @ResponseBody
    public ModelAndView showGestioneTeams(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt){
        return adminService.showGestioneTeams(request,jwt);
    }
    
    @GetMapping("/visualizzaTeam/{idTeam}")
    @ResponseBody
    public ModelAndView showTeamSpecifico(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt){
        return adminService.showTeamSpecifico(request,jwt);
    }

    /* Modifica 04/12/2024: Aggiunta endpoint getUsernameAdmin */
    //Serve al front-end
    @GetMapping("/usernameAdmin")
    @ResponseBody
    public String getUsernameAdmin(@CookieValue(name = "jwt", required = false) String jwt){
        return adminService.getUsernameAdmin(jwt);
    }

     //Solo x testing
     @GetMapping("/getLikes/{name}")
     @ResponseBody
     public ResponseEntity<Long> likes(@PathVariable String name) {
         long likesCount = utilsService.likes(name);
         return ResponseEntity.ok(likesCount);
     }
 
     @PostMapping("/newinteraction") 
     @ResponseBody
     public ResponseEntity<interaction> uploadInteraction(@RequestBody interaction interazione) {
         interaction savedInteraction = utilsService.uploadInteraction(interazione); 
         return ResponseEntity.ok(savedInteraction); 
     }
 
     @GetMapping("/Cfilterby/{category}")
     @ResponseBody
     public ResponseEntity<List<ClassUT>> filtraClassi(@PathVariable String category, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.filtraClassi(category, jwt);
     }
 
     @GetMapping("/Cfilterby/{text}/{category}")
     @ResponseBody
     public ResponseEntity<List<ClassUT>> filtraClassi(@PathVariable String text, @PathVariable String category, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.filtraClassi(text, category, jwt);
     }
 
     @GetMapping("/Dfilterby/{difficulty}")
     @ResponseBody
     public ResponseEntity<List<ClassUT>> elencaClassiD(@PathVariable String difficulty, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.elencaClassiD(difficulty, jwt);
     }
 
     @GetMapping("/Dfilterby/{text}/{difficulty}")
     @ResponseBody
     public ResponseEntity<List<ClassUT>> elencaClassiD(@PathVariable String text, @PathVariable String difficulty, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.elencaClassiD(text, difficulty, jwt);
     }
 
     @PostMapping("/insert")
     @ResponseBody
     public ResponseEntity<ClassUT> uploadClasse(@RequestBody ClassUT classe, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.uploadClasse(classe, jwt);
     }
 
     //MODIFICA (20/02/2024) : Eliminazione della riga riguardante il caricamento dei relativi test generati dai Robot (ATTENZIONE)
     @PostMapping("/uploadFile")
     @ResponseBody
     public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile classFile, @RequestParam("model") String model, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) throws IOException {
         return adminService.uploadFile(classFile, model, jwt, request);
     }
 
     @PostMapping("/uploadTest")
     @ResponseBody
     public ResponseEntity<FileUploadResponse> uploadTest(@RequestParam("file") MultipartFile classFile, @RequestParam("model") String model, @RequestParam("test") MultipartFile testFile, @RequestParam("testEvo") MultipartFile testFileEvo, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) throws IOException {
         return adminService.uploadTest(classFile, model, testFile, testFileEvo, jwt, request);
     }
 
     @PostMapping("/delete/{name}")
     @ResponseBody
     public ResponseEntity<?> eliminaClasse(@PathVariable String name, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.eliminaClasse(name, jwt);
     }
 
     @PostMapping("/deleteFile/{fileName}")
     @ResponseBody
     public ResponseEntity<String> eliminaFile(@PathVariable String fileName, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.eliminaFile(fileName, jwt);
     }
     
     //MODIFICA (15/02/2024) : Elenco di tutti gli amministratori
     @GetMapping("/admins_list")
     @ResponseBody
     public Object getAllAdmins(@CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.getAllAdmins(jwt);
     }
     
     @GetMapping("/info")
     public ModelAndView showAdmin(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.showAdmin(request, jwt);
     }
     
     @PostMapping("/password_change_admin")
     @ResponseBody
     public ResponseEntity<?> changePasswordAdmin(@RequestBody Admin admin1, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
         return adminService.changePasswordAdmin(admin1, jwt);
     }
     
     @GetMapping("/password_change_admin")
     public ModelAndView showChangePswAdminForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.showChangePswAdminForm(request, jwt);
     }
     
     @PostMapping("/password_reset_admin")
     @ResponseBody
     public ResponseEntity<?> resetPasswordAdmin(@RequestBody Admin admin1, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
         return adminService.resetPasswordAdmin(admin1, jwt);
     }
     
     @GetMapping("/password_reset_admin")
     public ModelAndView showResetPswAdminForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.showResetPswAdminForm(request, jwt);
     }
     
     @GetMapping("/invite_admins")
     @ResponseBody
     public ModelAndView showInviteAdmins(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.showInviteAdmins(request, jwt);
     }
     
     @PostMapping("/invite_admins")
     @ResponseBody
     public ResponseEntity<?> inviteAdmins(@RequestBody Admin admin1, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
         return adminService.inviteAdmins(admin1, jwt);
     }
     
     @GetMapping("/login_with_invitation")
     @ResponseBody
     public ModelAndView showLoginWithInvitationForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.showLoginWithInvitationForm(request, jwt);
     }
     
     @PostMapping("/login_with_invitation")
     @ResponseBody
     public ResponseEntity<?> loginWithInvitation(@RequestBody Admin admin1, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
         return adminService.loginWithInvitation(admin1, jwt);
     }
 
     //MODIFICA (17/04/2024) : Aggiunta pagina di configurazione della nuova modalità di gioco
     @GetMapping("/scalata")
     public ModelAndView showGamePageScalata(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
         return scalataService.showGamePageScalata(request, jwt);
     }
 
     //MODIFICA (14/05/2024) : Creazione della propria "Scalata"
     @PostMapping("/configureScalata")
     public ResponseEntity<?> uploadScalata(@RequestBody Scalata scalata, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
         return scalataService.uploadScalata(scalata, jwt);
     }
 
     //MODIFICA (15/05/2024) : Recupero di tutte le "Scalate" memorizzate nel sistema
     @GetMapping("/scalate_list")
     @ResponseBody
     public ResponseEntity<?> listScalate() {
         return scalataService.listScalate();
     }
 
     //MODIFICA (16/05/2024) : Rimozione di una specifica "Scalata" memorizzata nel sistema
     @DeleteMapping("delete_scalata/{scalataName}")
     @ResponseBody
     public ResponseEntity<?> deleteScalataByName(@PathVariable String scalataName, @CookieValue(name = "jwt", required = false) String jwt) {
         return scalataService.deleteScalataByName(scalataName, jwt);
     }
 
     //MODIFICA (15/05/2024) : Recupero di una specifica "Scalata" memorizzata nel sistema
     @GetMapping("/retrieve_scalata/{scalataName}")
     @ResponseBody
     public ResponseEntity<?> retrieveScalataByName(@PathVariable String scalataName) {
         return scalataService.retrieveScalataByName(scalataName);
     }
 
     //MODIFICA (18/09/2024) : Aggiunta pagina di configurazione degli achievement
     @GetMapping("/achievements")
     public ModelAndView showAchievementsPage(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
         return achievementService.showAchievementsPage(request, jwt);
     }
 
     //MODIFICA (18/09/2024) : Aggiunta API di get per la lista degli achievement
     @GetMapping("/achievements/list")
     @ResponseBody
     public ResponseEntity<?> listAchievements() {
         return achievementService.listAchievements();
     }
 
     @PostMapping("/achievements")
     public Object createAchievement(Achievement achievement, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
         return achievementService.createAchievement(achievement, jwt, request);
     }
 
     //MODIFICA (07/10/2024) : Aggiunta API di get per la lista delle statistiche
     @GetMapping("/statistics/list")
     @ResponseBody
     public ResponseEntity<?> listStatistics() {
         return achievementService.listStatistics();
     }
 
     @PostMapping("/statistics")
     public Object createStatistic(Statistic statistic, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
         return achievementService.createStatistic(statistic, jwt, request);
     }
 
     @DeleteMapping("/statistics/{Id}")
     public Object deleteStatistic(@PathVariable("Id") String Id, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
         return achievementService.deleteStatistic(Id, jwt, request);
     }
 
     //MODIFICA (11/02/2024) : Gestione flusso JWT
     @PostMapping("/update/{name}")
     @ResponseBody
     public ResponseEntity<String> modificaClasse(@PathVariable String name, @RequestBody ClassUT newContent, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
         return adminService.modificaClasse(name, newContent, jwt, request);
     }
 
     @PostMapping("/registraAdmin")
     @ResponseBody
     public ResponseEntity<?> registraAdmin(@RequestBody Admin admin1, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.registraAdmin(admin1, jwt);
     }
 
     @GetMapping("/admins/{username}")
     @ResponseBody
     public ResponseEntity<Admin> getAdminByUsername(@PathVariable String username, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.getAdminByUsername(username, jwt);
     }
 
     //MODIFICA (11/02/2024) : Gestione flusso JWT
     @GetMapping("/player")
     public ModelAndView showPlayer(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.showPlayer(request, jwt);
     }
 
     @GetMapping("/class")
     public ModelAndView showClass(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.showClass(request, jwt);
     }
 
     @PostMapping("/loginAdmin")
     @ResponseBody
     public ResponseEntity<String> loginAdmin(@RequestBody Admin admin1, @CookieValue(name = "jwt", required = false) String jwt, HttpServletResponse response) {
         return adminService.loginAdmin(admin1, jwt, response);
     }
 
     @GetMapping("/loginAdmin")
     public ModelAndView showLoginForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
         return adminService.getLoginForm(jwt);
     }
 
     //MODIFICA (11/02/2024) : Gestione flusso JWT
     @GetMapping("/home")
     @ResponseBody
     public ResponseEntity<List<ClassUT>> elencaClassi(@CookieValue(name = "jwt", required = false) String jwt) {
         System.out.println("(/home) visualizzazione delle classi di gioco");
         List<ClassUT> classi = adminService.elencaClassi();
         return ResponseEntity.ok().body(classi);
     }
 
     @GetMapping("/downloadFile/{name}")
     @ResponseBody
     public ResponseEntity<?> downloadClasse(@PathVariable("name") String name) {
          try {
             return adminService.downloadClasse(name);
         } catch (Exception e) {
             // Gestisci l'eccezione e ritorna una risposta appropriata
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body("Errore nel download della classe: " + e.getMessage());
         }
     }
 
     @GetMapping("/home/{text}")
     @ResponseBody
     public List<ClassUT> ricercaClasse(@PathVariable String text) {
         return adminService.ricercaClasse(text);
     }
 
     //MODIFICA (12/02/2024) : Logout amministratore
     @GetMapping("/logout_admin")
     public ModelAndView logoutAdmin(HttpServletResponse response) {
         // Chiama il servizio per gestire il logout
         adminService.logout(response);
         
         // Redirect alla pagina di login
         return new ModelAndView("login_admin");
     }
 
     @GetMapping("/test")
     @ResponseBody
     public String test() {
         return adminService.test(); 
     }
 
     @GetMapping("/interaction")
     @ResponseBody
     public List<interaction> elencaInt() {
         return utilsService.elencaInt();
     }
 
     @GetMapping("/findReport")
     @ResponseBody
     public List<interaction> elencaReport() {
         return utilsService.elencaReport();
     }
 
 
     @PostMapping("/newLike/{name}")
     @ResponseBody
     public String newLike(@PathVariable String name) {
         return utilsService.newLike(name);
     }
 
     @PostMapping("/newReport/{name}")
     @ResponseBody
     public String newReport(@PathVariable String name, @RequestBody String commento) {
         return utilsService.newReport(name, commento);
     }
 
     @PostMapping("/deleteint/{id_i}")
     @ResponseBody
     public interaction eliminaInteraction(@PathVariable int id_i) {
         return utilsService.eliminaInteraction(id_i);
     }
     

 
    
 }



