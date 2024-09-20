package com.g2.t5;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
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

import com.g2.Components.PageBuilder;
import com.g2.Components.TableComponent;
import com.g2.Components.TextComponent;
import com.g2.Interfaces.ServiceManager;
import com.g2.Model.Game;
import com.g2.Model.ScalataGiocata;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@Controller
public class GuiController {

    private final ServiceManager serviceManager;

    @Autowired
    public GuiController(RestTemplate restTemplate) {
        this.serviceManager = new ServiceManager(restTemplate);
    }

    @GetMapping("/debug")
    public String debug(Model model){
        
        List<String> items = List.of("Elemento 1", "Elemento 2", "Elemento 3", "Elemento 4");
        model.addAttribute("items", items); 
        
        return "debug";
    }

    @GetMapping("/main")
    public String GUIController(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        PageBuilder main = new PageBuilder(serviceManager, null);
        return main.handlePageRequest(model, "main", jwt);
    }

    @GetMapping("/gamemode")
    public String gamemodePage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        TextComponent Class_list  = new TextComponent(serviceManager, "T1", "getClasses", "classeUT");
        PageBuilder   gamemode    = new PageBuilder(serviceManager, Arrays.asList(Class_list));
        return gamemode.handlePageRequest(model, "gamemode", jwt);
    } 
        
    @GetMapping("/editor")
    public String editorPage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        TextComponent testo_CUT = new TextComponent(serviceManager, "T1", "getClassUnderTest", "classeUT", "Calcolatrice");
        PageBuilder   editor      = new PageBuilder(serviceManager, Arrays.asList(testo_CUT));
        return editor.handlePageRequest(model, "editor", jwt);
    }
    
    @GetMapping("/leaderboard")
    public String leaderboard(Model model, @CookieValue(name = "jwt", required = false) String jwt){
        TableComponent table       = new TableComponent(serviceManager, "listaPlayers");
        PageBuilder    leaderboard = new PageBuilder(serviceManager, Arrays.asList(table));
        return leaderboard.handlePageRequest(model, "leaderboard", jwt);
    }

    @GetMapping("/report")
    public String reportPage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        Boolean Auth = (Boolean) serviceManager.handleRequest("T23", "GetAuthenticated", jwt);
        if(Auth){
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

    @GetMapping("/leaderboardScalata")
    public String getLeaderboardScalata(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        Boolean Auth = (Boolean) serviceManager.handleRequest("T23", "GetAuthenticated", jwt);
        if(Auth){
            return "leaderboardScalata";
        }
        return "redirect:/login";
    }

    @GetMapping("/editorAllenamento")
    public String editorAllenamentoPage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        Boolean Auth = (Boolean) serviceManager.handleRequest("T23", "GetAuthenticated", jwt);
        if(Auth){
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
        Boolean Auth = (Boolean) serviceManager.handleRequest("T23", "GetAuthenticated", jwt);
        if(Auth){
            return "gamemode_scalata";
        }
        return "redirect:/login";
        
	}    
}