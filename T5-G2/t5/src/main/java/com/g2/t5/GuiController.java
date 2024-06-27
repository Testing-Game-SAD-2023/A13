package com.g2.t5;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.g2.Model.ClassUT;
import com.g2.Model.Game;
import com.g2.Model.Player;
import com.g2.Model.ScalataGiocata;                 // aggiunto
import com.g2.t5.MyData;                            // aggiunto

@CrossOrigin
@Controller
public class GuiController {

    private RestTemplate restTemplate;

    @Autowired
    public GuiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> getLevels(String className) {
        List<String> result = new ArrayList<String>();

        int i;
        for (i = 1; i < 11; i++) {
            try {
                restTemplate.getForEntity("http://t4-g18-app-1:3000/robots?testClassId=" + className
                        + "&type=randoop&difficulty=" + String.valueOf(i), Object.class);
            } catch (Exception e) {
                break;
            }

            result.add(String.valueOf(i));
        }

        for (int j = i; j - i + 1 < i; j++) {
            try { // aggiunto
                restTemplate.getForEntity("http://t4-g18-app-1:3000/robots?testClassId=" + className
                        + "&type=evosuite&difficulty=" + String.valueOf(j - i + 1), Object.class);
            } catch (Exception e) {
                break;
            }

            result.add(String.valueOf(j));
        }

        return result;
    }

    public List<ClassUT> getClasses() {
        ResponseEntity<List<ClassUT>> responseEntity = restTemplate.exchange("http://manvsclass-controller-1:8080/home",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<ClassUT>>() {
                });

        return responseEntity.getBody();
    }

    @GetMapping("/main")
    public String GUIController(Model model, @CookieValue(name = "jwt", required = false) String jwt) {

        System.out.println("GET /main, scelta della modalità di gioco");
        
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("jwt", jwt);

        Boolean isAuthenticated = restTemplate.postForObject("http://t23-g1-app-1:8080/validateToken", formData,
                Boolean.class);

        if (isAuthenticated == null || !isAuthenticated)
            return "redirect:/login";

        return "main";

    }

    @GetMapping("/gamemode")
    public String gamemodePage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        //MODIFICA (Stiamo controllando la validità dal token JWT)
        formData.add("jwt", jwt);

        System.out.println("GET /gamemode, scelta della classe e del robot da testare");
        Boolean isAuthenticated = restTemplate.postForObject("http://t23-g1-app-1:8080/validateToken", formData,
                Boolean.class);

        System.out.println("(/gamemode) Token del player valido?");
        if (isAuthenticated == null || !isAuthenticated)
            return "redirect:/login";

            System.out.println("(/gamemode) Token valido: "+ jwt);
        List<ClassUT> classes = getClasses();

        Map<Integer, String> hashMap = new HashMap<>();
        Map<Integer, List<MyData>> robotList = new HashMap<>();
        // Map<Integer, List<String>> evosuiteLevel = new HashMap<>();

        for (int i = 0; i < classes.size(); i++) {
            String valore = classes.get(i).getName();

            List<String> levels = getLevels(valore);
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
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("jwt", jwt);

        Boolean isAuthenticated = restTemplate.postForObject("http://t23-g1-app-1:8080/validateToken", formData,
                Boolean.class);

        if (isAuthenticated == null || !isAuthenticated)
            return "redirect:/login";
   
        return "report";
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
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("jwt", jwt);

        Boolean isAuthenticated = restTemplate.postForObject("http://t23-g1-app-1:8080/validateToken", formData,
                Boolean.class);

        if (isAuthenticated == null || !isAuthenticated)
            return "redirect:/login";
        // model.addAttribute("robot", valuerobot);
        // model.addAttribute("classe", valueclass);

        // model.addAttribute("gameIDj", globalID);

        return "editor";
    }

    @GetMapping("/editorAllenamento")
    public String editorAllenamentoPage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("jwt", jwt);

        Boolean isAuthenticated = restTemplate.postForObject("http://t23-g1-app-1:8080/validateToken", formData,
                Boolean.class);

        if (isAuthenticated == null || !isAuthenticated)
            return "redirect:/login";
        // model.addAttribute("robot", valuerobot);
        // model.addAttribute("classe", valueclass);

        // model.addAttribute("gameIDj", globalID);

        return "editorAllenamento";
    }

    //MODIFICA (22/05/2024) : Visualizzazione pagina dedicata alla selezione della "Scalata"
    @GetMapping("/gamemode_scalata")
	public String showScalatAndView(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        System.out.println("(GET /gamemode_scalata) get received");
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("jwt", jwt);

        Boolean isAuthenticated = restTemplate.postForObject("http://t23-g1-app-1:8080/validateToken", formData,
                Boolean.class);

        System.out.println("(GET /gamemode_scalata) Token del player valido?");
        if (isAuthenticated == null || !isAuthenticated) {

            System.out.println("(GET /gamemode_scalata) Token JWT non valido, il player verrà reindirizzato alla pagina di login.");
            return "redirect:/login";
        }
        else {

            System.out.println("(GET /gamemode_scalata) Token valido.");
            return "gamemode_scalata";
        }

            
	}

}
