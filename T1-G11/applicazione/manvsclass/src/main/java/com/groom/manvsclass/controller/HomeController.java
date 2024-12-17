/*MODIFICA (5/11/2024) - Refactoring task T1
 * HomeController ora si occupa solo del mapping dei servizi aggiunti.
 */

 package com.groom.manvsclass.controller;

 import java.io.IOException;
 import java.io.File;
 import java.time.Instant;
 import java.time.LocalDate;
 import java.time.format.DateTimeFormatter;
 import java.time.temporal.ChronoUnit;
 import java.util.*;
 import java.util.regex.Pattern;
 
 import javax.servlet.http.Cookie;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import org.springframework.http.ResponseEntity;
 import com.groom.manvsclass.model.ClassUT;
 import org.springframework.web.multipart.MultipartFile;
 import com.groom.manvsclass.model.filesystem.upload.FileUploadResponse;
 import com.groom.manvsclass.model.filesystem.upload.FileUploadUtil;
 import com.groom.manvsclass.model.filesystem.RobotUtil;
 import com.groom.manvsclass.model.filesystem.download.FileDownloadUtil;
 import com.groom.manvsclass.model.Admin;
 import com.groom.manvsclass.model.Challenge;
import com.groom.manvsclass.model.Achievement; 
 import com.groom.manvsclass.model.Statistic;
import com.groom.manvsclass.model.Team;
import com.groom.manvsclass.model.VictoryConditionType;
import com.groom.manvsclass.model.interaction; 
 import com.groom.manvsclass.model.Scalata; 
 
 import com.groom.manvsclass.service.AchievementService;
 import com.groom.manvsclass.service.AdminService;
import com.groom.manvsclass.service.ChallengeService;
import com.groom.manvsclass.service.ScalataService;
import com.groom.manvsclass.service.TeamService;
import com.groom.manvsclass.service.Util;
 
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.CookieValue;
 import org.springframework.web.bind.annotation.CrossOrigin;
 import org.springframework.web.bind.annotation.DeleteMapping;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.PathVariable;
 import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
 import org.springframework.web.bind.annotation.ResponseBody;
 import org.springframework.web.multipart.MultipartFile;
 import org.springframework.web.servlet.ModelAndView;
 import org.springframework.web.servlet.view.RedirectView;
 import org.springframework.http.HttpStatus;
 
 import javax.servlet.http.HttpServletRequest;
 
 import org.springframework.data.mongodb.core.query.Criteria;
 import org.springframework.data.mongodb.core.query.Update;
 import org.springframework.data.mongodb.core.query.Query;
 
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

     // MODIFICA (29/11/2024) - Inizializzazione dei servizi aggiunti
     @Autowired
    private TeamService teamService;

    // MODIFICA (10/12/2024) - Inizializzazione dei servizi aggiunti
    @Autowired
    private ChallengeService challengeService;
 

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
 
     @GetMapping("class")
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
     
        // INIZIO MODIFICA: Rotte per la gestione dei Team 29/11/2024
     
        //usata per visualizzare la select con tutta la lista di team. CI STA
    @GetMapping("/team_view")
    @ResponseBody
    public ResponseEntity<List<Team>> getAllTeams(@CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.getAllTeams(jwt);
    }  
    
    @GetMapping("/teams_view") //aggiunta rotta per visualizzare i team in formato html
    public ResponseEntity<String> getAllTeamsAsHtml(@RequestHeader("Authorization") String jwt) {
        String token = jwt.replace("Bearer ", ""); // Rimuove il prefisso Bearer
        return teamService.getAllTeamsAsHtml(token);
    }

     //Modifiche 06/12/2024
    @GetMapping("/students_list")//aggiunta rotta per visualizzare la lista degli studenti
    @ResponseBody
    public ResponseEntity<?> getStudentsList(@CookieValue(name = "jwt", required = false) String jwt) {
    return teamService.getStudentsList(jwt);
    }  

    @PostMapping("/team_create")//aggiunta rotta per creare un team
    @ResponseBody
    public ResponseEntity<Team> createTeam(@RequestBody Team team, @CookieValue(name = "jwt", required = false) String jwt) {
        return teamService.createTeam(team, jwt);
    }

    // Aggiungere un membro al team
     @PostMapping("/team_add_member")
    @ResponseBody
    public ResponseEntity<String> addMemberToTeam(
        @RequestBody Map<String, String> payload,
        @CookieValue(name = "jwt", required = false) String jwt) {
        String teamName = payload.get("teamName");
        String memberId = payload.get("memberId");
        return teamService.addMemberToTeam(teamName, memberId, jwt);
    }

    @PostMapping("/team_remove_member")//aggiunta rotta per rimuovere un membro dal team
        @ResponseBody
        public ResponseEntity<String> removeMemberFromTeam(@RequestBody Map<String, String> requestBody, @CookieValue(name = "jwt", required = false) String jwt){
            // Estrai i dati dalla richiesta
            String teamName = requestBody.get("teamName");
            String memberId = requestBody.get("memberId");
            // Verifica che i parametri siano presenti
            if (teamName == null || memberId == null) {
                return ResponseEntity.badRequest().body("Team name or member ID is missing.");
            }
            // Chiama il TeamService per rimuovere il membro
            return teamService.removeMemberFromTeam(teamName, memberId, jwt);
        }

    @PostMapping("/team_delete")
    @ResponseBody
    public ResponseEntity<String> deleteTeam(@RequestBody Map<String, String> requestBody, @CookieValue(name = "jwt", required = false) String jwt) {
    // Estrai i dati dalla richiesta
        String teamName = requestBody.get("teamName");
        // Verifica che il parametro sia presente
        if (teamName == null || teamName.isEmpty()) {
            return ResponseEntity.badRequest().body("Nome team mancante.");
        }
        // Chiama il TeamService per cancellare il team
        return teamService.deleteTeam(teamName, jwt);
    }  
    //Chiamata da bottone nella pagina home admin
        @GetMapping("/teams_show")
        @ResponseBody
        public ModelAndView showTeamManagementPage(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
        return adminService.showTeamManagementPage(request, jwt);
    }

   
    
    //FINE MODIFICA: Rotte per la gestione dei Team 29/11/2024


    

    //aggiunta rotte challenges (10/12/2024)
    
    /**
     * Crea una nuova challenge. 
     */
    @PostMapping("/challenge_create")
    public ResponseEntity<Challenge> createChallenge(
            @RequestBody Challenge challenge,
            @CookieValue(name = "jwt", required = false) String jwt) {
        return challengeService.createChallenge(challenge, jwt);
    }


        /**
     * Recupera una challenge tramite il nome. NON USATA
     
    @GetMapping("/challenges/ChallengesByName")
    public ResponseEntity<Challenge> getChallengeByName(
            @PathVariable String challengeName,
            @CookieValue(name = "jwt", required = false) String jwt) {
        return challengeService.getChallengeByName(challengeName, jwt);
    }

    /**
     * Recupera tutte le challenge associate a un team. //non usata
      
    @GetMapping("/team/team_Id")
    public ResponseEntity<List<Challenge>> getChallengesByTeam(
            @PathVariable String teamId,
            @CookieValue(name = "jwt", required = false) String jwt) {
        return challengeService.getChallengesByTeam(teamId, jwt);
    }

        /**
     * Aggiorna lo stato di una challenge. //non usata
     
    @PutMapping("/challenges/ChallengesByName/status")
    public ResponseEntity<String> updateChallengeStatus(
            @PathVariable String challengeName,
            @RequestBody String newStatus,
            @CookieValue(name = "jwt", required = false) String jwt) {
        return challengeService.updateChallengeStatus(challengeName, newStatus, jwt);
    }
    */

        /**
     * Elimina una challenge. //USATA
     */
    @PostMapping("/challenges_ChallengesByName")
    @ResponseBody
    public ResponseEntity<String> deleteChallenge(
        @RequestBody Map<String, String> payload,
        @CookieValue(name = "jwt", required = false) String jwt) {
    String challengeName = payload.get("challengeName");
    return challengeService.deleteChallenge(challengeName, jwt);
    }


        /**
     * Recupera le partite associate a un giocatore specifico. //Per ora non usata
     */
    @GetMapping("/players/{playerName}/games")
    public ResponseEntity<?> getPlayerGames(
        @PathVariable String playerName,
        @CookieValue(name = "jwt", required = false) String jwt) {
    return challengeService.getPlayerGames(playerName, jwt);
    }


        /**
     * Verifica se una challenge è completata. // Per ora non usata
     */
    @GetMapping("/challenges/{challenge_Id}/{playerName}")
    public ResponseEntity<Boolean> isChallengeCompleted(
            @PathVariable(value = "challenge_Id") String challengeId,
            @PathVariable(value = "playerName") String playerName,
            @CookieValue(name = "jwt", required = false) String jwt) {
        try {
            // Recupera la challenge dal servizio
            Challenge challenge = challengeService.getChallengeByName(challengeId, jwt).getBody();

            // Se la challenge non esiste, restituisci un 404
            if (challenge == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
            }

            // Verifica il completamento della challenge usando il nome del giocatore
            boolean completed = challengeService.isChallengeCompletedByMember(challenge, playerName, jwt);
            return ResponseEntity.ok(completed);
        } catch (IllegalArgumentException e) {
            // Risposta per victoryCondition non valida
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        } catch (UnsupportedOperationException e) {
            // Risposta per victoryConditionType non supportato
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(false);
        } catch (Exception e) {
            // Risposta per errori generali
            System.err.println("Errore durante la verifica della challenge: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }




    //route bottone della challenge //USATA
    @GetMapping("/challenges_show")
    @ResponseBody
    public ModelAndView showChallengeManagementPage(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
        return adminService.showChallengeManagementPage(request, jwt);
    }   


    @GetMapping("/challenge_view") //USATA
    public ResponseEntity<String> getAllChallengesAsHtml(@RequestHeader("Authorization") String jwt) {
        String token = jwt.replace("Bearer ", ""); // Rimuove il prefisso Bearer
        return challengeService.getAllChallengesAsHtml(token);
    }

    @GetMapping("/challenges_view") //USATA
    @ResponseBody
    public ResponseEntity<List<Challenge>> getAllChallenges(@CookieValue(name = "jwt", required = false) String jwt) {
        return challengeService.getAllChallenges(jwt);
    }

    // Route per ottenere la victoryConditionType //USATA
    @GetMapping("/victoryConditionTypes")
    @ResponseBody
    public ResponseEntity<VictoryConditionType[]> getVictoryConditionTypes() {
        return ResponseEntity.ok(VictoryConditionType.values());
    }

        /**
     * Endpoint per aggiornare lo stato delle challenge scadute.
     */
    @PostMapping("/challenges/update_expired")
    @ResponseBody
    public ResponseEntity<String> updateExpiredChallenges(
            @CookieValue(name = "jwt", required = false) String jwt) {
        return challengeService.updateExpiredChallenges(jwt);
    }


    
}
