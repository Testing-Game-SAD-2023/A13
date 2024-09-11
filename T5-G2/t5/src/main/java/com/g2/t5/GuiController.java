package com.g2.t5;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.g2.Model.ClassUT;
import com.g2.Model.Game;
import com.g2.Model.ScalataGiocata;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@Controller
public class GuiController {

    private RestController restController;

    @Autowired
    public GuiController(RestTemplate restTemplate) {
        this.restController = new RestController(restTemplate);
    }

    @GetMapping("/main")
    public String GUIController(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        if(restController.IsAuthenticate(jwt)){
            return "main";
        }
        return "redirect:/login";
    }

    @GetMapping("/gamemode")
    public String gamemodePage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        if(!restController.IsAuthenticate(jwt)){
            return "redirect:/login";
        }

        List<ClassUT> classes = restController.getClasses();
        Map<Integer, String> hashMap = new HashMap<>();
        Map<Integer, List<MyData>> robotList = new HashMap<>();
        // Map<Integer, List<String>> evosuiteLevel = new HashMap<>();

        for (int i = 0; i < classes.size(); i++) {
            String valore = classes.get(i).getName();

            List<String> levels = restController.getLevels(valore);
            System.out.println(levels);

            List<String> evo = new ArrayList<>();               // aggiunto
            for (int j = 0; j < levels.size(); j++) {           // aggiunto
                if (j >= levels.size() / 2)
                    evo.add(j, levels.get(j - (levels.size() / 2)));
                else {
                    evo.add(j, levels.get(j + (levels.size() / 2)));
                }
            }
            System.out.println(evo);

            List<MyData> struttura = new ArrayList<>();

            for (int j = 0; j < levels.size(); j++) {
                MyData strutt = new MyData(levels.get(j), evo.get(j));
                struttura.add(j, strutt);
            }
            for (int j = 0; j < struttura.size(); j++)
                System.out.println(struttura.get(j).getList1());
            hashMap.put(i, valore);
            robotList.put(i, struttura);
            // evosuiteLevel.put(i, evo);
        }

        model.addAttribute("hashMap", hashMap);

        // hashMap2 = com.g2.Interfaces.t8.RobotList();

        model.addAttribute("hashMap2", robotList);

        // model.addAttribute("evRobot", evosuiteLevel); //aggiunto
        return "gamemode";

    }

    @GetMapping("/report")
    public String reportPage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        if(restController.IsAuthenticate(jwt)){
            return "report";
        }
        return "redirect:/login";
    }

    // TODO: Salvataggio della ScalataGiocata
    @PostMapping("/save-scalata")
    public ResponseEntity<String> saveScalata(@RequestParam("playerID") int playerID,
                                              @RequestParam("scalataName") String scalataName,
                                              HttpServletRequest request) {
        /*
         * Nella schermata /gamemode_scalata, il player non dovrà far altro che che selezionare una delle
         * "Scalate" presenti nella lista e dunque, le informazioni da elaborare saranno esclusivamente:
         * playerID
         * scalataName, dal quale è possibile risalire a tutte le informazioni relative quella specifica "Scalata"
         */

        /*
        * Verifica dell'autenticità del player controllando che l'header identificato dal: "X-UserID" sia lo stesso
        * associato all'utente identificato da "playerID"
        */
        if (!request.getHeader("X-UserID").equals(String.valueOf(playerID))) {

            System.out.println("(/save-scalata)[T5] Player non autorizzato.");
            return ResponseEntity.badRequest().body("Unauthorized");
        }
        else {

            // Player autorizzato.
            System.out.println("(/save-scalata)[T5] Player autorizzato.");

            // Recupero della data e dell'ora di inizio associata alla ScalataGiocata
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime currentHour = LocalTime.now();
            LocalDate currentDate = LocalDate.now();
            String fomattedHour = currentHour.format(formatter);
            System.out.println("(/save-scalata)[T5] Data ed ora di inizio recuperate con successo.");

            // Creazione di un oggetto scalataDataWriter
            ScalataDataWriter scalataDataWriter = new ScalataDataWriter();

            // Creazione di un oggetto ScalataGiocata
            ScalataGiocata scalataGiocata = new ScalataGiocata();

            // Inizializzazione dell'oggetto ScalataGiocata
            scalataGiocata.setPlayerID(playerID);
            scalataGiocata.setScalataName(scalataName);
            scalataGiocata.setCreationDate(currentDate);
            scalataGiocata.setCreationTime(fomattedHour);

            JSONObject ids = scalataDataWriter.saveScalata(scalataGiocata);
            System.out.println("(/save-scalata)[T5] Creazione dell'oggetto scalataDataWriter avvenuta con successo.");

            if (ids == null)
                return ResponseEntity.badRequest().body("Bad Request");
    
            return ResponseEntity.ok(ids.toString());
        }
    }

    @PostMapping("/save-data")
    public ResponseEntity<String> saveGame(@RequestParam("playerId") int playerId, @RequestParam("robot") String robot,
            @RequestParam("classe") String classe, @RequestParam("difficulty") String difficulty,
            @RequestParam("username") String username, @RequestParam("selectedScalata") Optional<Integer> selectedScalata, HttpServletRequest request) {

        if (!request.getHeader("X-UserID").equals(String.valueOf(playerId)))
            return ResponseEntity.badRequest().body("Unauthorized");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime oraCorrente = LocalTime.now();
        String oraFormattata = oraCorrente.format(formatter);

        GameDataWriter gameDataWriter = new GameDataWriter();
        // g.setGameId(gameDataWriter.getGameId());
        Game g = new Game(playerId, "descrizione", "nome", difficulty, username);
        // g.setPlayerId(pl);
        // g.setPlayerClass(classe);
        // g.setRobot(robot);
        g.setData_creazione(LocalDate.now());
        g.setOra_creazione(oraFormattata);
        g.setClasse(classe);
        g.setUsername(username);
        // System.out.println(g.getUsername() + " " + g.getGameId());

        System.out.println("ECCO LO USERNAME : " + username);       //in realtà stampa l'indirizzo e-mail del player...

        // globalID = g.getGameId();

        JSONObject ids = gameDataWriter.saveGame(g, username, selectedScalata);
        if (ids == null)
            return ResponseEntity.badRequest().body("Bad Request");

        return ResponseEntity.ok(ids.toString());
    }

    @GetMapping("/editor")
    public String editorPage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        if(restController.IsAuthenticate(jwt)){
            return "editor";
        }
        return "redirect:/login";
    }

    @GetMapping("/leaderboardScalata")
    public String getLeaderboardScalata(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        if(restController.IsAuthenticate(jwt)){
            return "leaderboardScalata";
        }
        return "redirect:/login";
    }

    @GetMapping("/editorAllenamento")
    public String editorAllenamentoPage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        if(restController.IsAuthenticate(jwt)){
            return "editorAllenamento";
        }
        return "redirect:/login";
    }

    //MODIFICA (22/05/2024) : Visualizzazione pagina dedicata alla selezione della "Scalata"
    @GetMapping("/gamemode_scalata")
	public String showScalatAndView(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        /*  stefano: vecchio codice t5
        System.out.println("(GET /gamemode_scalata) get received");
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("jwt", jwt);
        Boolean isIsAuthenticated = restTemplate.postForObject("http://t23-g1-app-1:8080/validateToken", formData,
                Boolean.class);

        System.out.println("(GET /gamemode_scalata) Token del player valido?");
        if (isIsAuthenticated == null || !isIsAuthenticated) {

            System.out.println("(GET /gamemode_scalata) Token JWT non valido, il player verrà reindirizzato alla pagina di login.");
            return "redirect:/login";
        }
        else {

            System.out.println("(GET /gamemode_scalata) Token valido.");
            return "gamemode_scalata";
        }
            */

        //mia versione di prova
        if(restController.IsAuthenticate(jwt)){
            return "gamemode_scalata";
        }
        return "redirect:/login";
	}

}
