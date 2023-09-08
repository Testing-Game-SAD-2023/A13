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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g2.Model.ClassUT;
import com.g2.Model.Game;
import com.g2.Model.Player;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.json.JSONObject;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@Controller
public class GuiController {

    // Player p1 = Player.getInstance();
    // Game g = new Game();
    // long globalID;

    // String valueclass = "NULL";
    // String valuerobot = "NULL";
    // private Integer myClass = null;
    // private Integer myRobot = null;
    // private Map<Integer, String> hashMap = new HashMap<>();
    // private Map<Integer, String> hashMap2 = new HashMap<>();
    // private final FileController fileController;
    private RestTemplate restTemplate;

    @Autowired
    public GuiController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // @GetMapping("/login")
    // public String loginPage() {
    // return "login"; // Nome del template Thymeleaf per la pagina1.html
    // }

    public List<String> getLevels(String className) {
        List<String> result = new ArrayList<String>();

        for(int i = 1; i < 11; i++) {
            try {
                restTemplate.getForEntity("http://t4-g18-app-1:3000/robots?testClassId=" + className + "&type=randoop&difficulty="+String.valueOf(i), Object.class);
            } catch (Exception e) {
                break;
            }

            result.add(String.valueOf(i));
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
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("jwt", jwt);

        Boolean isAuthenticated = restTemplate.postForObject("http://t23-g1-app-1:8080/validateToken", formData, Boolean.class);

        if(isAuthenticated == null || !isAuthenticated) return "redirect:/login";

        // fileController.listFilesInFolder("/app/AUTName/AUTSourceCode");
        // int size = fileController.getClassSize();

        List<ClassUT> classes = getClasses();

        Map<Integer, String> hashMap = new HashMap<>();
        Map<Integer, List<String>> robotList = new HashMap<>();

        for (int i = 0; i < classes.size(); i++) {
            String valore = classes.get(i).getName();

            List<String> levels = getLevels(valore);
            System.out.println(levels);
            hashMap.put(i, valore);
            robotList.put(i, levels);
        }

        model.addAttribute("hashMap", hashMap);

        // hashMap2 = com.g2.Interfaces.t8.RobotList();

        model.addAttribute("hashMap2", robotList);
        return "main";
    }

    // @PostMapping("/sendVariable")
    // public ResponseEntity<String>
    // receiveVariableClasse(@RequestParam("myVariable") Integer myClassa,
    // @RequestParam("myVariable2") Integer myRobota) {
    // // Fai qualcosa con la variabile ricevuta
    // System.out.println("Variabile ricevuta: " + myClassa);
    // System.out.println("Variabile ricevuta: " + myRobota);
    // myClass = myClassa;
    // myRobot = myRobota;
    // // Restituisci una risposta al client (se necessario)
    // return ResponseEntity.ok("Dati ricevuti con successo");
    // }

    @GetMapping("/report")
    public String reportPage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("jwt", jwt);

        Boolean isAuthenticated = restTemplate.postForObject("http://t23-g1-app-1:8080/validateToken", formData, Boolean.class);

        if(isAuthenticated == null || !isAuthenticated) return "redirect:/login";
        // valueclass = hashMap.get(myClass);
        // valuerobot = hashMap2.get(myRobot);

        // System.out.println("IL VALORE DEL ROBOT " + valuerobot + " " + myRobot);
        // System.out.println("Il VALORE DELLA CLASSE " + valueclass + " " + myClass);
        // model.addAttribute("classe", valueclass);
        // model.addAttribute("robot", valuerobot);
        return "report";
    }

    // @PostMapping("/login-variabiles")
    // public ResponseEntity<String> receiveLoginData(@RequestParam("var1") String
    // username,
    // @RequestParam("var2") String password) {

    // System.out.println("username : " + username);
    // System.out.println("password : " + password);

    // p1.setUsername(username);
    // p1.setPassword(password);

    // // Salva i valori in una variabile o esegui altre operazioni necessarie
    // if (com.g2.Interfaces.t2_3.verifyLogin(username, password)) {
    // return ResponseEntity.ok("Dati ricevuti con successo");
    // }

    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è
    // verificato un errore interno");
    // }

    @PostMapping("/save-data")
    public ResponseEntity<String> saveGame(@RequestParam("playerId") int playerId, @RequestParam("robot") String robot,
            @RequestParam("classe") String classe, @RequestParam("difficulty") String difficulty, HttpServletRequest request) {

                if(!request.getHeader("X-UserID").equals(String.valueOf(playerId))) return ResponseEntity.badRequest().body("Unauthorized");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime oraCorrente = LocalTime.now();
                String oraFormattata = oraCorrente.format(formatter);

                GameDataWriter gameDataWriter = new GameDataWriter();
                // g.setGameId(gameDataWriter.getGameId());
                Game g = new Game(playerId, "descrizione", "nome", difficulty);
                // g.setPlayerId(pl);
                // g.setPlayerClass(classe);
                // g.setRobot(robot);
                g.setData_creazione(LocalDate.now());
                g.setOra_creazione(oraFormattata);
                g.setClasse(classe);
                // System.out.println(g.getUsername() + " " + g.getGameId());

                // globalID = g.getGameId();

                JSONObject ids = gameDataWriter.saveGame(g);

                if(ids == null) return ResponseEntity.badRequest().body("Bad Request");

                return ResponseEntity.ok(ids.toString());
    }

    // @PostMapping("/download")
    // public ResponseEntity<Resource> downloadFile(@RequestParam("elementId")
    // String elementId) {
    // // Effettua la logica necessaria per ottenere il nome del file
    // // a partire dall'elementId ricevuto, ad esempio, recuperandolo dal database
    // System.out.println("elementId : " + elementId);
    // String filename = elementId;
    // System.out.println("filename : " + filename);
    // String basePath = "/app/AUTName/AUTSourceCode/";
    // String filePath = basePath + filename + ".java";
    // System.out.println("filePath : " + filePath);
    // Resource fileResource = new FileSystemResource(filePath);

    // HttpHeaders headers = new HttpHeaders();
    // headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +
    // filename + ".java");
    // headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

    // return ResponseEntity.ok()
    // .headers(headers)
    // .body(fileResource);
    // }

    // @GetMapping("/change_password")
    // public String showChangePasswordPage() {
    // return "change_password";
    // }

    @GetMapping("/editor")
    public String editorPage(Model model, @CookieValue(name = "jwt", required = false) String jwt) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("jwt", jwt);

        Boolean isAuthenticated = restTemplate.postForObject("http://t23-g1-app-1:8080/validateToken", formData, Boolean.class);

        if(isAuthenticated == null || !isAuthenticated) return "redirect:/login";
        // model.addAttribute("robot", valuerobot);
        // model.addAttribute("classe", valueclass);

        // model.addAttribute("gameIDj", globalID);

        return "editor";
    }

}
