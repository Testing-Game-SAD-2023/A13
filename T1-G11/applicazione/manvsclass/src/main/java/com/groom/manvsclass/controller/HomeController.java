package com.groom.manvsclass.controller;


import java.io.IOException;
import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.groom.manvsclass.model.filesystem.upload.FileUploadResponse;
import com.groom.manvsclass.model.filesystem.upload.FileUploadUtil;
import com.groom.manvsclass.model.filesystem.RobotUtil;
import com.groom.manvsclass.model.filesystem.download.FileDownloadUtil;
import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.interaction;
import com.groom.manvsclass.model.Operation;
import com.groom.manvsclass.model.repository.AdminRepository;
import com.groom.manvsclass.model.repository.ClassRepository;
import com.groom.manvsclass.model.repository.InteractionRepository;
import com.groom.manvsclass.model.repository.OperationRepository;
import com.groom.manvsclass.model.repository.SearchRepositoryImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;

//MODIFICA(11/02/2024): Gestione sessione tramite JWT
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//MODIFICA (11/02/2024) : Controlli sul form registrazione
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//MODIFICA (12/02/2024) : Gestione autenticazione
import com.groom.manvsclass.controller.Authentication.AuthenticatedAdmin;
import com.groom.manvsclass.controller.Authentication.AuthenticatedAdminRepository;

//MODIFICA (13/02/2024) : Autenticazione token proveniente dai players
// import org.springframework.web.client.RestTemplate;
 //MODIFICA (15/02/2024) : Servizio di posta elettronica
 import javax.mail.MessagingException;

//FINE MODIFICA

@CrossOrigin
@Controller
public class HomeController {
	
	@Autowired
	ClassRepository repo;
	@Autowired
	AdminRepository arepo;
	@Autowired
	InteractionRepository repo_int;
	@Autowired
	OperationRepository orepo;
	
	@Autowired
    private MongoTemplate mongoTemplate; 
	// private final Admin userAdmin= new Admin("default","default","default","default");
	private final Admin userAdmin= new Admin("default","default","default","default","default");
	private final LocalDate today = LocalDate.now();
	private final SearchRepositoryImpl srepo;

	//MODIFICA (11/02/2024) : Controlli sul form registrazione
	@Autowired
    private PasswordEncoderAdmin myPasswordEncoder;

	//MODIFICA (12/02/2024) : Gestione autenticazione
	@Autowired
    private AuthenticatedAdminRepository authenticatedAdminRepository;

	//MODIFICA (13/02/2024) : Autenticazione token proveniente players
	// private RestTemplate restTemplate;

	//MODIFICA (15/02/2024) : Servizio di posta elettronica
	@Autowired
    private EmailService emailService;

	//MODIFICA (11/02/2024) : Regex, La psw deve contenere da 8 a 16 caratteri, di cui almeno un carattere minuscolo,
    //                                 maiuscolo, un numero ed un carattere speciale
	                               
    String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,16}$"; // maiuscola, minuscola, numero e chr. speciale
    Pattern p = Pattern.compile(regex);
	
	//FINE MODIFICA

	//MODIFICA (13/02/2024) : Autenticazione token proveniente players
	public HomeController(SearchRepositoryImpl srepo) {
	this.userAdmin.setUsername("default");
	this.srepo=srepo;
	}

	// public HomeController(SearchRepositoryImpl srepo, RestTemplate restTemplate) {
	// 	this.userAdmin.setUsername("default");
	// 	this.srepo=srepo;
	// 	this.restTemplate = restTemplate;
	// }

	@GetMapping("/home_adm")
	public ModelAndView showHomeAdmin(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
		if(isJwtValid(jwt)) return new ModelAndView("home_adm"); 
		return new ModelAndView("redirect:/loginAdmin"); 
	}
	
	// @GetMapping("/loginAdmin")
	// public String showLoginAdmin() {
	// 	return "login_admin";
	// }

	@GetMapping("/registraAdmin")
	public String showRegistraAdmin() {
		return "registraAdmin";
	}

	// @GetMapping("/modificaClasse")
	// public String showModificaClasse() {
	// 	return "modificaClasse";
	// }

	//MODIFICA (11/02/2024) : Gestione sessione tramite JWT
	@GetMapping("/modificaClasse")
	public ModelAndView showModificaClasse(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
		//Se il token è valido, allora l'admin ha la possibilità di modificare le classi...
		if(isJwtValid(jwt)) return new ModelAndView("modificaClasse");
		//altrimenti verrà indirizzato alla pagina di login
		return new ModelAndView("redirect:/loginAdmin"); 
	}

	@GetMapping("/uploadClasse")
	public ModelAndView showUploadClasse(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
		//Se il token è valido, allora l'admin ha la possibilità di caricare le classi...
		if(isJwtValid(jwt)) return new ModelAndView("uploadClasse");
		//altrimenti verrà indirizzato alla pagina di login
		return new ModelAndView("redirect:/loginAdmin"); 
	}

	@GetMapping("/uploadClasseAndTest")
	public ModelAndView showUploadClasseAndTest(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
		//Se il token è valido, allora l'admin ha la possibilità di caricare le classi ed i test...
		if(isJwtValid(jwt)) return new ModelAndView("uploadClasseAndTest");
		//altrimenti verrà indirizzato alla pagina di login
		return new ModelAndView("redirect:/loginAdmin"); 
	}

	@GetMapping("/reportClasse")
	public ModelAndView showReportClasse(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
		//Se il token è valido, allora l'admin ha la possibilità di visualizzare i report associati alle classi...
		if(isJwtValid(jwt)) return new ModelAndView("reportClasse");
		//altrimenti verrà indirizzato alla pagina di login
		return new ModelAndView("redirect:/loginAdmin"); 
	}

	@GetMapping("/Reports")
	public ModelAndView showReports(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
		//Se il token è valido, allora l'admin ha la possibilità di visualizzare i report...
		if(isJwtValid(jwt)) return new ModelAndView("Reports");
		//altrimenti verrà indirizzato alla pagina di login
		return new ModelAndView("redirect:/loginAdmin"); 
	}
	//FINE MODIFICA
	
	// @GetMapping("/uploadClasse")
	// public String showUploadClasse() {
	// 	return "uploadClasse";
	// }

	// @GetMapping("/uploadClasseAndTest")
	// public String showUploadClasseAndTest() {
	// 	return "uploadClasseAndTest";
	// }

	// @GetMapping("/reportClasse")
	// public String showReportClasse() {
	// 	return "reportClasse";
	// }

	// @GetMapping("/Reports")
	// public String showReports() {
	// 	return "Reports";
	// }

	@GetMapping("/interaction")
	@ResponseBody
	public	List<interaction>	elencaInt() {
		return repo_int.findAll();
	}
	
	@GetMapping("/findreport")
	@ResponseBody
	public	List<interaction> elencaReport() {
		return srepo.findReport();
	}
	
	//Solo x testing
	@GetMapping("/getLikes/{name}")
	@ResponseBody
	public long likes(@PathVariable String name) {
		long likes=srepo.getLikes(name);
		
		return likes;
	}
	
	@PostMapping("/newinteraction")
	@ResponseBody
	public interaction UploadInteraction(@RequestBody interaction interazione) {
		return repo_int.save(interazione);
	}



	public int API_id() {
	    Random random = new Random();
	    return random.nextInt(1000000 - 0 + 1) + 0;
	}
	
	public String API_email(int id_u) {
		
		String email = "prova."+id_u+"@email.com";
		return email;
	}
	
	@PostMapping("/newlike/{name}")
	@ResponseBody
	public String newLike(@PathVariable String name) {
	    interaction newInteraction = new interaction();
	    //Finta chiamata all'API utente
	    int id_u = API_id();
	    String email_u = API_email(id_u);
	    LocalDate currentDate = LocalDate.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String data = currentDate.format(formatter);
        
        newInteraction.setId_i(0);
	    newInteraction.setId(id_u);
	    newInteraction.setEmail(email_u);
	    newInteraction.setName(name);
	    newInteraction.setType(1);
	    newInteraction.setDate(data);
	    repo_int.save(newInteraction);

	    return "Nuova interazione di tipo 'like' inserita per la classe: " + name;
	}
	
	@PostMapping("/newReport/{name}")
	@ResponseBody
	public String newReport(@PathVariable String name, @RequestBody String commento ) {
	    interaction newInteraction = new interaction();
	    
	    //Finta chiamata all'API utente
	    int id_u = API_id();
	    
	    //Finta chiamata API email
	    String email_u = API_email(id_u);
	    
	    //Generazione data del giorno
	    LocalDate currentDate = LocalDate.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String data = currentDate.format(formatter);
        
        newInteraction.setId_i(0);
	    newInteraction.setId(id_u);
	    newInteraction.setEmail(email_u);
	    newInteraction.setName(name);
	    newInteraction.setType(0);
	    newInteraction.setDate(data);
	    newInteraction.setCommento(commento);
	    repo_int.save(newInteraction);

	    return "Nuova interazione di tipo 'report' inserita per la classe: " + name;
	}
	
	@PostMapping("/deleteint/{id_i}")
	@ResponseBody
	public interaction eliminaInteraction(@PathVariable int id_i) {
		Query query= new Query(); 
	   query.addCriteria(Criteria.where("id_i").is(id_i));
	   return mongoTemplate.findAndRemove(query, interaction.class);
	}
	
	
	// @GetMapping("/home")
	// @ResponseBody
	// public	List<ClassUT>	elencaClassi() {
	// 	return repo.findAll();
	// }

	//MODIFICA (11/02/2024) : Gestione flusso JWT

	@GetMapping("/home")
	@ResponseBody
	public ResponseEntity<List<ClassUT>> elencaClassi(@CookieValue(name = "jwt", required = false) String jwt) {
		// if (isJwtValid(jwt)) {
		//MODIFICA (13/02/2024) : Controllo validità token proveniente dai players
		// MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
		// formData.add("jwt", jwt);

		// Boolean isAuthenticated = restTemplate.postForObject("http://t23-g1-app-1:8080/validateToken", formData, Boolean.class);
		// System.out.println("isAuthenticated (home):"+ isAuthenticated);

		// if(isAuthenticated == null || !isAuthenticated) {
		// 	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Ritorna 401 Unauthorized
		// }
		//FINE MODIFICA (13/02/2024)
		
		//FUNZIONANTE
		System.out.println("(/home) visualizzazione delle classi di gioco");
		List<ClassUT> classi = repo.findAll();
		return ResponseEntity.ok().body(classi);
		//FUNZIONANTE

		// } else {
		// 	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Ritorna 401 Unauthorized
		// }
	}

	@GetMapping("/orderbydate")
	@ResponseBody
	public ResponseEntity<List<ClassUT>> ordinaClassi(@CookieValue(name = "jwt", required = false) String jwt) {

		if (isJwtValid(jwt)) {

			System.out.println("Token valido, può ordinare tutte le classi per data (orderbydate)");
			List<ClassUT> classiOrdinate = srepo.orderByDate();
        	return ResponseEntity.ok().body(classiOrdinate);
			
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Ritorna 401 Unauthorized
		}
	}

	@GetMapping("/orderbyname")
	@ResponseBody
	public ResponseEntity<List<ClassUT>> ordinaClassiNomi(@CookieValue(name = "jwt", required = false) String jwt) {

		if (isJwtValid(jwt)) {

			System.out.println("Token valido, può ordinare tutte le classi per nome (orderbyname)");
			List<ClassUT> classiOrdinateNome = srepo.orderByName();
        	return ResponseEntity.ok().body(classiOrdinateNome);
			
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Ritorna 401 Unauthorized
		}
	}
	
	@GetMapping("/Cfilterby/{category}")
	@ResponseBody
	public ResponseEntity<List<ClassUT>> filtraClassi(@PathVariable String category,
													  @CookieValue(name = "jwt", required = false) String jwt) {
		if (isJwtValid(jwt)) {

			System.out.println("Token valido, può filtrare le classi per categoria (Cfilterby/{category})");
			List<ClassUT> classiFiltrate = srepo.filterByCategory(category);
			return ResponseEntity.ok().body(classiFiltrate);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Ritorna 401 Unauthorized
		}
	}

	
	@GetMapping("/Cfilterby/{text}/{category}")
	@ResponseBody
	public	ResponseEntity<List<ClassUT>> filtraClassi(@PathVariable String text,
													   @PathVariable String category,
													   @CookieValue(name = "jwt", required = false) String jwt) {

		if (isJwtValid(jwt)) {

			System.out.println("Token valido, può ricercare e filtrare le classi per categoria (/Cfilterby/{text}/{category})");
			List<ClassUT> classiFiltrate = srepo.searchAndFilter(text,category);
			return ResponseEntity.ok().body(classiFiltrate);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Ritorna 401 Unauthorized
		}
	}
	
	@GetMapping("/Dfilterby/{difficulty}")
	@ResponseBody
	public ResponseEntity<List<ClassUT>> elencaClassiD(@PathVariable String difficulty,
													   @CookieValue(name = "jwt", required = false) String jwt) {

		if (isJwtValid(jwt)) {

			System.out.println("Token valido, può filtrare le classi per difficoltà (/Dfilterby/{difficulty}");
			List<ClassUT> classiFiltrate = srepo. filterByDifficulty(difficulty);
			return ResponseEntity.ok().body(classiFiltrate);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Ritorna 401 Unauthorized
		}
	}
	
	@GetMapping("/Dfilterby/{text}/{difficulty}")
	@ResponseBody
	public	ResponseEntity<List<ClassUT>>	elencaClassiD(@PathVariable String text,
														  @PathVariable String difficulty,
														  @CookieValue(name = "jwt", required = false) String jwt) {

		if (isJwtValid(jwt)) {

			System.out.println("Token valido, può ricercare e  filtrare le classi per difficoltà (/Dfilterby/{text}/{difficulty}");
			List<ClassUT> classiFiltrate = srepo.searchAndDFilter(text,difficulty);
			return ResponseEntity.ok().body(classiFiltrate);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Ritorna 401 Unauthorized
		}
	}
	
	@PostMapping("/insert")
	@ResponseBody
	public ResponseEntity<ClassUT> uploadClasse(@RequestBody ClassUT classe,
											    @CookieValue(name = "jwt", required = false) String jwt) {
		if (isJwtValid(jwt)) {

			System.out.println("Token valido, può inserire una nuova classe (/insert)");
			LocalDate currentDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String data = currentDate.format(formatter);
			Operation operation1 = new Operation((int)orepo.count(), userAdmin.getUsername(), classe.getName(), 0, data);
			orepo.save(operation1);
			ClassUT savedClasse = repo.save(classe);

			System.out.println("Inserimento classe avvenuto con successo (/insert)");
			return ResponseEntity.ok().body(savedClasse);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Ritorna 401 Unauthorized
		}
	}


	// @PostMapping("/insert")
	// @ResponseBody
	// public ClassUT UploadClasse(@RequestBody ClassUT classe) {
	// 	LocalDate currentDate = LocalDate.now();
	//     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    //     String data = currentDate.format(formatter);
    //     Operation operation1= new Operation((int)orepo.count(),userAdmin.getUsername(),classe.getName(),0,data);
    //     orepo.save(operation1);
	// 	return repo.save(classe);
	// }


	//FINE MODIFICA
	
	// @GetMapping("/orderbydate")
	// @ResponseBody
	// public List<ClassUT> ordinaClassi() {
	// 	return srepo.orderByDate();
	// }

	// @GetMapping("/orderbyname")
	// @ResponseBody
	// public List<ClassUT> ordinaClassiNomi() {
	// 	return srepo.orderByName();
	// }
	
	// @GetMapping("/Cfilterby/{category}")
	// @ResponseBody
	// public List<ClassUT> filtraClassi(@PathVariable String category) {
	// 	return srepo.filterByCategory(category);
	// }
	
	// @GetMapping("/Cfilterby/{text}/{category}")
	// @ResponseBody
	// public	List<ClassUT>	filtraClassi(@PathVariable String text,@PathVariable String category) {
	// 	return srepo.searchAndFilter(text,category);
	// }
	
	// @GetMapping("/Dfilterby/{difficulty}")
	// @ResponseBody
	// public List<ClassUT> elencaClassiD(@PathVariable String difficulty) {
	// 	return srepo. filterByDifficulty(difficulty);
	// }
	
	// @GetMapping("/Dfilterby/{text}/{difficulty}")
	// @ResponseBody
	// public	List<ClassUT>	elencaClassiD(@PathVariable String text,@PathVariable String difficulty) {
	// 	return srepo.searchAndDFilter(text,difficulty);
	// }
	

	// @PostMapping("/insert")
	// @ResponseBody
	// public ClassUT UploadClasse(@RequestBody ClassUT classe) {
	// 	LocalDate currentDate = LocalDate.now();
	//     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    //     String data = currentDate.format(formatter);
    //     Operation operation1= new Operation((int)orepo.count(),userAdmin.getUsername(),classe.getName(),0,data);
    //     orepo.save(operation1);
	// 	return repo.save(classe);
	// }

	/**
	 * @param classFile     file inviato come parte della richiesta multipart
	 *                      L'interfaccia MultipartFile fornisce i metodi per
	 *                      accedere ai dati del file che nel nostro caso sono i
	 *                      parametri descrittivi della classe ovvero difficoltà,
	 *                      data di caricamento ecc
	 * @param model 		stringa che rappresenta la struttura dati ClasseUT che 
	 * 						immagazzina le infromazioni sulla classe
	 * @throws IOException
	 */

	 //MODIFICA (20/02/2024) : Eliminazione della riga riguardante il caricamento dei relativi test generati dai Robot (ATTENZIONE)
	@PostMapping("/uploadFile")
	@ResponseBody
	//public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile classFile, @RequestParam("model") String model) throws IOException {
	public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile classFile,
														 @RequestParam("model") String model, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) throws IOException {

		//MODIFICA (11/02/2024) : Gestione flusso JWT
		if (isJwtValid(jwt)) {

			System.out.println("Token valido (uploadFile)");
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
			classe.setUri("Files-Upload/"+classe.getName()+"/"+fileName);
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
			return new ResponseEntity<>(response,HttpStatus.OK);
			
		}else {

			System.out.println("Token non valido (uploadFile)");
			FileUploadResponse response = new FileUploadResponse();
			response.setErrorMessage("Errore, il token non è valido");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

		}
		//FINE MODIFICA

		// //Legge i metadati della classe della parte "model" del body HTTP e li salva in un oggetto ClasseUT
		// ObjectMapper mapper = new ObjectMapper();
		// ClassUT classe = mapper.readValue(model, ClassUT.class);
		
		// //Salva il nome del file caricato
		// String fileName = StringUtils.cleanPath(classFile.getOriginalFilename());
		// long size = classFile.getSize();
		
		// //Salva la classe nel filesystem condiviso
		// FileUploadUtil.saveCLassFile(fileName, classe.getName(), classFile);
		
		// //Genera e salva i test nel filesystem condiviso
		// RobotUtil.generateAndSaveRobots(fileName, classe.getName(), classFile);
		
		// //Prepara la risposta per il front-end
		// FileUploadResponse response = new FileUploadResponse();
		// response.setFileName(fileName);
		// response.setSize(size);
		// response.setDownloadUri("/downloadFile");
		
		// //Setta data di caricamento e percorso di download 
		// classe.setUri("Files-Upload/"+classe.getName()+"/"+fileName);
		// classe.setDate(today.toString());

		// //Creazione dell'oggetto riguardante l'operazione appena fatta
		// LocalDate currentDate = LocalDate.now();
	    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // String data = currentDate.format(formatter);
        // Operation operation1= new Operation((int)orepo.count(),userAdmin.getUsername(),classe.getName(),0,data);

		// //Salva i dati sull'operazione fatta nel database
        // orepo.save(operation1);
		// //Salva i dati sullla classe nel database
		// repo.save(classe);
		// return new ResponseEntity<>(response,HttpStatus.OK);
	}

	/**
	 * @param classFile     file della classe inviato come parte della richiesta POST
	 *                      L'interfaccia MultipartFile fornisce i metodi per
	 *                      accedere ai dati del file che nel nostro caso sono i
	 *                      parametri descrittivi della classe ovvero difficoltà,
	 *                      data di caricamento ecc
	 * @param model 		stringa che rappresenta la struttura dati ClasseUT che 
	 * 						immagazzina le infromazioni sulla classe
	 * @param testFile      file dei test inviato come parte della richiesta POST
	 * @throws IOException
	 */
	@PostMapping("/uploadTest")
    @ResponseBody
	//public ResponseEntity<FileUploadResponse> uploadTest(@RequestParam("file") MultipartFile classFile, @RequestParam("model") String model, @RequestParam("test") MultipartFile testFile,
	
	//														@RequestParam("testEvo") MultipartFile testFileEvo) throws IOException {
        
        // //Legge i metadati della classe della parte "model" del body HTTP e li salva in un oggetto ClasseUT
        // ObjectMapper mapper = new ObjectMapper();
        // ClassUT classe = mapper.readValue(model, ClassUT.class);
        
        // //Salva il nome del file della classe caricato
        // String fileNameClass = StringUtils.cleanPath(classFile.getOriginalFilename());
        // long size = classFile.getSize();
        
        // //Salva la classe nel filesystem condiviso
        // FileUploadUtil.saveCLassFile(fileNameClass, classe.getName(), classFile);
        
        // //Salva i test nel filesystem condiviso
        // String fileNameTest = StringUtils.cleanPath(testFile.getOriginalFilename());
		// String fileNameTestEvo = StringUtils.cleanPath(testFileEvo.getOriginalFilename());
        // RobotUtil.saveRobots(fileNameClass, fileNameTest,fileNameTestEvo , classe.getName(), classFile ,testFile, testFileEvo);

        // FileUploadResponse response = new FileUploadResponse();
        // response.setFileName(fileNameClass);
        // response.setSize(size);
        // response.setDownloadUri("/downloadFile");

        // //Setta data di caricamento e percorso di download della classe
        // classe.setUri("Files-Upload/" + classe.getName() + "/" + fileNameClass);
        // classe.setDate(today.toString());
        
        // //Creazione dell'oggetto riguardante l'operazione appena fatta
        // LocalDate currentDate = LocalDate.now();
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // String data = currentDate.format(formatter);
        // Operation operation1 = new Operation((int) orepo.count(), userAdmin.getUsername(), classe.getName() + " con Robot", 0, data);

        // //Salva i dati sull'operazione fatta nel database
        // orepo.save(operation1);
        // //Salva i dati sulla classe nel database
        // repo.save(classe);
        // return new ResponseEntity<>(response, HttpStatus.OK);
	//}

	//MODIFICA (11/02/2024) : Gestione flusso JWT

    public ResponseEntity<FileUploadResponse> uploadTest(@RequestParam("file") MultipartFile classFile, 
														 @RequestParam("model") String model,
														 @RequestParam("test") MultipartFile testFile,
														 @RequestParam("testEvo") MultipartFile testFileEvo,
														 @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) throws IOException {

		//Controllo toke JWT
		System.out.println(("Il token JWT è valido (uploadTest)?"));
		if (isJwtValid(jwt)) {

			System.out.println("Token valido (uploadTest)");
			//Legge i metadati della classe della parte "model" del body HTTP e li salva in un oggetto ClasseUT
			ObjectMapper mapper = new ObjectMapper();
			ClassUT classe = mapper.readValue(model, ClassUT.class);
			
			//Salva il nome del file della classe caricato
			String fileNameClass = StringUtils.cleanPath(classFile.getOriginalFilename());
			long size = classFile.getSize();
			
			//Salva la classe nel filesystem condiviso
			System.out.println("Salvataggio di "+fileNameClass+"nel filestystem condiviso");
			FileUploadUtil.saveCLassFile(fileNameClass, classe.getName(), classFile);
			
			//Salva i test nel filesystem condiviso
			String fileNameTest = StringUtils.cleanPath(testFile.getOriginalFilename());
			String fileNameTestEvo = StringUtils.cleanPath(testFileEvo.getOriginalFilename());
			RobotUtil.saveRobots(fileNameClass, fileNameTest,fileNameTestEvo , classe.getName(), classFile ,testFile, testFileEvo);
	
			FileUploadResponse response = new FileUploadResponse();
			response.setFileName(fileNameClass);
			response.setSize(size);
			response.setDownloadUri("/downloadFile");
	
			//Setta data di caricamento e percorso di download della classe
			classe.setUri("Files-Upload/" + classe.getName() + "/" + fileNameClass);
			classe.setDate(today.toString());
			
			//Creazione dell'oggetto riguardante l'operazione appena fatta
			LocalDate currentDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String data = currentDate.format(formatter);
			Operation operation1 = new Operation((int) orepo.count(), userAdmin.getUsername(), classe.getName() + " con Robot", 0, data);
	
			//Salva i dati sull'operazione fatta nel database
			orepo.save(operation1);
			//Salva i dati sulla classe nel database
			repo.save(classe);
			System.out.println("Operazione completata con successo (uploadTest)");
			return new ResponseEntity<>(response, HttpStatus.OK);

		} else {

			System.out.println("Token non valido (uploadTest)");
			FileUploadResponse response = new FileUploadResponse();
			response.setErrorMessage("Errore, il token non è valido");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

		}
		
	 }

	 @PostMapping("/delete/{name}")
	 @ResponseBody
	 public ResponseEntity<?> eliminaClasse(@PathVariable String name, 
	 										@CookieValue(name = "jwt", required = false) String jwt) {
		 if (isJwtValid(jwt)) {

			System.out.println("Token valido, può rimuovere la classe selezionata (/delete/{name})");
			 Query query = new Query(); 
			 query.addCriteria(Criteria.where("name").is(name));
			 this.eliminaFile(name, jwt);							//Aggiunta parametro jwt
			 LocalDate currentDate = LocalDate.now();
			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			 String data = currentDate.format(formatter);
			 Operation operation1 = new Operation((int)orepo.count(), userAdmin.getUsername(), name, 2, data);
			 orepo.save(operation1);
			 ClassUT deletedClass = mongoTemplate.findAndRemove(query, ClassUT.class);
			 if (deletedClass != null) {

		         System.out.println("Rimozione avvenuta con successo (/delete/{name})");
				 return ResponseEntity.ok().body(deletedClass);

			 } else {
				 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe non trovata");
			 }
		 } else {
			 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido");
		 }
	 }
	 
	
	// @PostMapping("/delete/{name}")
	// @ResponseBody
	// public ClassUT eliminaClasse(@PathVariable String name) {
	// 	Query query= new Query(); 
	//    	query.addCriteria(Criteria.where("name").is(name));
	//    	this.eliminaFile(name);
	//    	LocalDate currentDate = LocalDate.now();
	//     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    //    	String data = currentDate.format(formatter);
    //    	Operation operation1= new Operation((int)orepo.count(),userAdmin.getUsername(),name,2,data);
    //    	orepo.save(operation1);
	//    	return mongoTemplate.findAndRemove(query, ClassUT.class);
	// }

	@PostMapping("/deleteFile/{fileName}")
	@ResponseBody
	public ResponseEntity<String> eliminaFile(@PathVariable String fileName,
											  @CookieValue(name = "jwt", required = false) String jwt) {
		if (isJwtValid(jwt)) {

			System.out.println("Token valido, può rimuovere il file selezionato (/deleteFile/{fileName})");
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

	// @PostMapping("/deleteFile/{fileName}")
	// @ResponseBody
	// public ResponseEntity<String> eliminaFile(@PathVariable String fileName) {
	// 	String folderPath = "Files-Upload/"+ fileName; 
	//   	File directoryRandoop = new File("/VolumeT9/app/FolderTree/" + fileName);
	// 	File directoryEvo = new File("/VolumeT8/FolderTreeEvo/" +  fileName);
	  
	// 	File folderToDelete = new File(folderPath);
	// 	if (folderToDelete.exists() && folderToDelete.isDirectory()) {
	// 		try {
	// 			FileUploadUtil.deleteDirectory(folderToDelete);
	// 			FileUploadUtil.deleteDirectory(directoryRandoop);
	// 			FileUploadUtil.deleteDirectory(directoryEvo);
	// 			return new ResponseEntity<>("Cartella eliminata con successo.", HttpStatus.OK);
	// 		} catch (IOException e) {
	// 			return new ResponseEntity<>("Impossibile eliminare la cartella.", HttpStatus.INTERNAL_SERVER_ERROR);
	// 		}
	// 	} else {
	// 		return new ResponseEntity<>("Cartella non trovata.", HttpStatus.NOT_FOUND);
	// 	}
	//  }
	    
	// @GetMapping("/home/{text}")
	// @ResponseBody
	// public ResponseEntity<List<ClassUT>> ricercaClasse(@PathVariable String text,
	// 												   @CookieValue(name = "jwt", required = false) String jwt) {
	// 	if (isJwtValid(jwt)) {

	// 		System.out.println("Token valido, può ricercare classi dalla /home (/home/{text})");
	// 		List<ClassUT> classiTrovate = srepo.findByText(text);
	// 		return ResponseEntity.ok().body(classiTrovate);
	// 	} else {
	// 		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Ritorna 401 Unauthorized
	// 	}
	// }

	// @GetMapping("/downloadFile/{name}")
	// @ResponseBody
	// public ResponseEntity<?> downloadClasse(@PathVariable("name") String name,
	// 									    @CookieValue(name = "jwt", required = false) String jwt) throws Exception {
	// 	if (isJwtValid(jwt)) {

	// 		System.out.println("Token valido (/downloadFile/{name})");
	// 		List<ClassUT> classe = srepo.findByText(name);
	// 		if (!classe.isEmpty()) {

	// 			System.out.println("Download classe prescelta (/downloadFile/{name})");
	// 			return FileDownloadUtil.downloadClassFile(classe.get(0).getcode_Uri());
	// 		} else {
	// 			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe non trovata");
	// 		}
	// 	} else {
	// 		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT non valido");
	// 	}
	// }

	//FINE MODIFICA

	
	@GetMapping("/home/{text}")
	@ResponseBody
	public	List<ClassUT>	ricercaClasse(@PathVariable String text) {
	return srepo.findByText(text);
	}

	@GetMapping("/test")
	@ResponseBody
	public String test() {
		return "test T1";
	}
	
	@GetMapping("/downloadFile/{name}")
	@ResponseBody
	public ResponseEntity<?> downloadClasse(@PathVariable("name") String name) throws Exception {
		System.out.println("test");
	 	List<ClassUT> classe= srepo.findByText(name);
		System.out.println("File download:");
		System.out.println(classe.get(0).getcode_Uri());
	 	return FileDownloadUtil.downloadClassFile(classe.get(0).getcode_Uri());
	}
	 

	// @PostMapping("/update/{name}")
	// @ResponseBody
	// public ResponseEntity<String> modificaClasse(@PathVariable String name, @RequestBody ClassUT newContent) {
	// 	Query query= new Query();
		
	// 	query.addCriteria(Criteria.where("name").is(name));
	// 	Update update = new Update().set("name", newContent.getName())
	// 			.set("date", newContent.getDate())
	// 			.set("difficulty", newContent.getDifficulty())
	// 			.set("description", newContent.getDescription())
	// 			.set("category", newContent.getCategory());
	// 	long modifiedCount = mongoTemplate.updateFirst(query, update, ClassUT.class).getModifiedCount();

	// 	if (modifiedCount > 0) {
	// 		LocalDate currentDate = LocalDate.now();
	// 		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	// 		String data = currentDate.format(formatter);
	// 		Operation operation1= new Operation((int)orepo.count(),userAdmin.getUsername(),newContent.getName(),1,data);
	// 		orepo.save(operation1);
	// 		return new ResponseEntity<>("Aggiornamento eseguito correttamente.", HttpStatus.OK);
	// 	} else {
	// 		return new ResponseEntity<>("Nessuna classe trovata o nessuna modifica effettuata.", HttpStatus.NOT_FOUND);
	// 	}
	// }

	//MODIFICA (11/02/2024) : Gestione flusso JWT

	@PostMapping("/update/{name}")
	@ResponseBody
	public ResponseEntity<String> modificaClasse(@PathVariable String name,
												 @RequestBody ClassUT newContent,
												 @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
		
		//Controllo token
		if (isJwtValid(jwt)) {

			System.out.println("Token valido, può aggiornare informazioni inerenti le classi (update/{name})");
			Query query= new Query();
		
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
				Operation operation1= new Operation((int)orepo.count(),userAdmin.getUsername(),newContent.getName(),1,data);
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

	@PostMapping("/registraAdmin")
	@ResponseBody
	public ResponseEntity<?> registraAdmin(@RequestBody Admin admin1,
										   @CookieValue(name = "jwt", required = false) String jwt) {
		if (isJwtValid(jwt)) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Already logged in");
		}

		// Se il token JWT non è valido, procedi con la registrazione dell'admin
		System.out.println("Token non valido, procedere con la registrazione");

		//Controlli
		//1: Possibilità di inserire più nomi separati da uno spazio
		if (admin1.getNome().length() >=2 && admin1.getNome().length() <= 30 && Pattern.matches("[a-zA-Z]+(\\s[a-zA-Z]+)*", admin1.getNome()) ) {

			this.userAdmin.setNome(admin1.getNome());
			System.out.println("Nome settato con successo");

		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome non valido");

		}

		//2: Possibilità di inserire più parole separate da uno spazio ed eventualmente da un apostrofo

		if ((admin1.getCognome().length() >= 2) && (admin1.getCognome().length() <= 30) && (Pattern.matches("[a-zA-Z]+(\\s?[a-zA-Z]+\\'?)*", admin1.getCognome()))) {
            
			this.userAdmin.setCognome(admin1.getCognome());
			System.out.println("Cognome settato con successo");

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cognome non valido");
        }

		//3: L'username deve rispettare necessariamente il seguente formato: "[username di lunghezza compresa tra 2 e 30 caratteri]_unina"
		if (admin1.getUsername().length() >=2 && admin1.getUsername().length() <= 30 && (Pattern.matches(".*_unina$", admin1.getUsername()))) {

			this.userAdmin.setUsername(admin1.getUsername());
			System.out.println("Username settato con successo");

		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username non valido, deve rispettare il seguente formato: [username di lunghezza compresa tra 2 e 30 caratteri]_unina");
		}

		//4: L'email deve essere necessariamente quella istituzionale e terminare: o con [nome]@studenti.unina.it oppure [nome]@unina.it
		if (Pattern.matches("^[a-zA-Z0-9._%+-]+@(?:studenti\\.)?unina\\.it$", admin1.getEmail())) {

			System.out.println("Formato email corretto (registraAdmin)");
			//Controllare che la mail non sia già presente nel DB
			Admin existingAdmin = arepo.findById(admin1.getEmail()).orElse(null);

			if (existingAdmin != null) {

				System.out.println("Mail già utilizzata (registraAdmin)");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin con questa mail già registrato");
			}
			System.out.println("Mail non ancora utilizzata (registraAdmin)");
			this.userAdmin.setEmail(admin1.getEmail());
			System.out.println("Email settata con successo");
		}else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email non valida, registrarsi con le credenziali istituzionali!");
		}

		//5: La password deve contenere almeno una lettera maiuscola, una minuscola, un numero ed un carattere speciale e deve essere lunga tra gli 8 e i 16 caratteri
		Matcher m = p.matcher(admin1.getPassword());

        if ((admin1.getPassword().length() >16) || (admin1.getPassword().length() < 8) || !(m.matches())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password non valida! La password deve contenere almeno una lettera maiuscola, una minuscola, un numero ed un carattere speciale e deve essere lunga tra gli 8 e i 16 caratteri");
        }

		//N.B: Aggiungere un ulteriore campo per poter gesitre il check_password
        //if (password.equals(check_password)) {
		String crypted = myPasswordEncoder.encoder().encode(admin1.getPassword());
		System.out.println("Password memorizzata e crittata con successo");
		this.userAdmin.setPassword(crypted);

		//Salvataggio admin nel db
		Admin savedAdmin = arepo.save(this.userAdmin);
		System.out.println("Registrazione avvenuta con successo");
		return ResponseEntity.ok().body(savedAdmin);
	}


	//FINE MODIFICA


	// @PostMapping("/registraAdmin")
	// @ResponseBody
	// public Admin registraAdmin(@RequestBody Admin admin1) {
	// 	//NB:Aggiungere setCognome
	// 	this.userAdmin.setUsername(admin1.getUsername());
	// 	this.userAdmin.setPassword(admin1.getPassword());
	// 	return arepo.save(admin1);
	// }

	@PostMapping("/loginAdmin")
	@ResponseBody
	//public String loginAdmin(@RequestBody Admin admin1, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request, HttpServletResponse response) {
	public ResponseEntity<String> loginAdmin(@RequestBody Admin admin1, @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request, HttpServletResponse response) {
		//NB: Bisognerebbe controllare in base all'email

		//Controllo token
		System.out.println("Token JWT valido?");
		if(isJwtValid(jwt)) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sei già loggato.");
		}

		//MODIFICA (11/02/2024)
		System.out.println("Token valido");

		//Controllare di aver compilato tutti i campi
		if (admin1.getUsername().isEmpty() && admin1.getPassword().isEmpty()) {

			System.out.println("Per favore, compila tutti i campi!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Per favore, compila tutti i campi!");
		}
		else if (admin1.getUsername().isEmpty()) {

			System.out.println("Per favore, compila questo campo [username]!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lo username è un campo obbligatorio!");
		}
		else if (admin1.getPassword().isEmpty()) {

			System.out.println("Per favore, compila questo campo [passwrod]!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La password è un campo obbligatorio!");
		}
		else {
				System.out.println("Campi compilati.");
				Admin admin = srepo.findAdminByUsername(admin1.getUsername());
				boolean passwordMatches = myPasswordEncoder.matches(admin1.getPassword(), admin.getPassword());

				if ((admin != null) && passwordMatches) { 
					//Se l'admin esiste (username trovata nel db) e le password crittate coincidono...
					System.out.println("Admin registrato (username trovato nel database)"); 	
					this.userAdmin.setUsername(admin.getUsername());
					this.userAdmin.setPassword(admin.getPassword());
		
					//MODIFICA (Gestione token JWT)
		
					//Flusso JWT
					System.out.println("Generazione token JWT...");
					String token = generateToken(admin1);

					//Generazione AuthenticatedAdmin
					// AuthenticatedAdmin authenticatedAdmin = new AuthenticatedAdmin(admin1, token);
					// authenticatedAdminRepository.save(authenticatedAdmin);
					// System.out.println("creazione AuthenticatedAdmin avvenuta con successo");

					Cookie jwtTokenCookie = new Cookie("jwt", token);
					jwtTokenCookie.setMaxAge(3600);
					response.addCookie(jwtTokenCookie);
					System.out.println("Cookie aggiunto alla risposta");
					return ResponseEntity.ok().body("Autenticazione avvenuta con successo");

				} else {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore nella fase di login, per favore ricontrolla di aver inserito tutti i dati corretti");
					}
		}
		
	}

	//MODIFICA (11/02): Gestione flusso JWT
	public static String generateToken(Admin admin) {
		Instant now = Instant.now();
		Instant expiration = now.plus(1, ChronoUnit.HOURS);

		String token = Jwts.builder()
				.setSubject(admin.getUsername())  // .setSubject() imposta il soggetto del token JWT; il soggetto di solito rappresenta l'identità a cui si applica il token
				.setIssuedAt(Date.from(now)) 	  // .setIssuedAt() imposta il timestamp di emissione del token
				.setExpiration(Date.from(expiration)) //.setExpiration() imposta il timestamp di scadenza del token
				.claim("admin_username", admin.getUsername()) //.claim() aggiunge una serie di informazioni aggiuntive
				.claim("role", "admin")
				.signWith(SignatureAlgorithm.HS256, "mySecretKeyAdmin") //.signWith() serve per firmare il token JWT utilizzando l'algoritmo di firma HMAC-SHA256 e una chiave segreta specificata
				.compact(); //.compact() serve a compattare il token JWT in una stringa valida che può essere facilmente trasferita tramite HTTP o memorizzata in altri luoghi di archiviazione come cookie

		return token;
	}

	public boolean isJwtValid(String jwt) {
		try {
			Claims c = Jwts.parser().setSigningKey("mySecretKeyAdmin").parseClaimsJws(jwt).getBody();

			if((new Date()).before(c.getExpiration())) {
				return true;
			}
		} catch(Exception e) {
			System.err.println(e);
		}

		return false;
	}

	@GetMapping("/loginAdmin")
	public ModelAndView showLoginForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
		if(isJwtValid(jwt)) return new ModelAndView("redirect:/home_adm"); 

		return new ModelAndView("login_admin");
	}

	// @GetMapping("/registraAdmin")
	// public ModelAndView showRegistrationForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
	// 	if(isJwtValid(jwt)) return new ModelAndView("redirect:/home_adm"); 

	// 	return new ModelAndView("registraAdmin");
	// }
	//FINE MODIFICA

	// @GetMapping("/admins/{username}")
	// @ResponseBody
	// public Admin getAdminByUsername(@PathVariable String username) {
	// 	return srepo.findAdminByUsername(username);
	// }

	@GetMapping("/admins/{username}")
	@ResponseBody
	public ResponseEntity<Admin> getAdminByUsername(@PathVariable String username,
												    @CookieValue(name = "jwt", required = false) String jwt) {
		if (isJwtValid(jwt)) {

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

	// @GetMapping("/player")
	// public String showplayer() {
	// 	return "player";
	// }

	//MODIFICA (11/02/2024) : Gestione flusso JWT

	@GetMapping("/player")
	public ModelAndView showplayer(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {

		if(isJwtValid(jwt)) return new ModelAndView("player");

        return new ModelAndView("login_admin");
	}

	@GetMapping("class")
	public ModelAndView showclass(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {

		if(isJwtValid(jwt)) return new ModelAndView("class");

        return new ModelAndView("login_admin");
	}

	//FINE MODIFICA

	// @GetMapping("class")
	// public String showclass() {
	// 	return "class";
	// }

	//MODIFICA (12/02/2024) : Logout amministratore
    @GetMapping("/logout_admin")
    public ModelAndView logoutAdmin(HttpServletResponse response) {

		System.out.println("GetMapping(\"/logout_admin\")");
        Cookie jwtTokenCookie = new Cookie("jwt", null);
		System.out.println("Logout (GET)");
        jwtTokenCookie.setMaxAge(0);
        response.addCookie(jwtTokenCookie);

        return new ModelAndView("login_admin"); 
    }

	//MODIFICA (15/02/2024) : Elenco di tutti gli amministratori
	// @GetMapping("/admins_list")
	// @ResponseBody 						// Questa annotazione indica a Spring di restituire direttamente il risultato come corpo della risposta HTTP
    // public List<Admin> getAllAdmins() {
    //     return arepo.findAll();
    // }
	//MODIFICA (1/3/2024) : Aggiunto controllo token jwt
	@GetMapping("/admins_list")
    @ResponseBody
    public Object getAllAdmins(@CookieValue(name = "jwt", required = false) String jwt) {
        System.out.println("GET /admins_list");
        if (isJwtValid(jwt)) {
            return arepo.findAll();
        } else {
            return new RedirectView("/loginAdmin"); // Reindirizza alla pagina di loginAdmin
        }
    }

	@GetMapping("/info")
	public ModelAndView showadmin(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {

		System.out.println("GET /info");
		if(isJwtValid(jwt)) return new ModelAndView("info");

        return new ModelAndView("login_admin");
	}

	@PostMapping("/password_change_admin")
	@ResponseBody
	public ResponseEntity<?> changePasswordAdmin (@RequestBody Admin admin1,
												  //@RequestParam("resetToken") String resetToken,
												  @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
		//Controllo validità token
		System.out.println("Token valido? (password_change_admin)");
		if(isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Già loggato");
        }
		System.out.println("Token valido, si può procedere al cambio password (password_change_admin)");
		
		//Definizione amministratore
		Admin admin = arepo.findById(admin1.getEmail()).orElse(null);
		System.out.println("La mail esiste? (password_change_admin)");

		if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email non trovata");
        }
		Admin admin_reset = srepo.findAdminByResetToken(admin1.getResetToken());
		System.out.println("Mail trovata nel database (password_change_admin)");
		System.out.println("admin: " + admin_reset);
		
		// System.out.println("admin1.getResetToken: " + admin1.getResetToken() + "\n");		//OK
		// System.out.println("admin.getResetToken: " + admin_reset.getResetToken() + "\n");	//OK

		System.out.println("Token di reset valido?");
		if(!admin_reset.getResetToken().equals(admin1.getResetToken())) {
			System.out.println("Token non valido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token di reset invalido!");
        }
		System.out.println("Token di reset valido! (password_change_admin)");
		System.out.println("Si può procedere al cambio password");

		//5: La password deve contenere almeno una lettera maiuscola, una minuscola, un numero ed un carattere speciale e deve essere lunga tra gli 8 e i 16 caratteri
		Matcher m = p.matcher(admin1.getPassword());

        if ((admin1.getPassword().length() >16) || (admin1.getPassword().length() < 8) || !(m.matches())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password non valida! La password deve contenere almeno una lettera maiuscola, una minuscola, un numero ed un carattere speciale e deve essere lunga tra gli 8 e i 16 caratteri");
        }
		String crypted = myPasswordEncoder.encoder().encode(admin1.getPassword());
		admin1.setPassword(crypted);
		admin.setPassword(admin1.getPassword());
		System.out.println("Password memorizzata e crittata con successo");

		admin1.setResetToken(null);
		admin.setResetToken(admin1.getResetToken());

		//Salvataggio admin nel db
		Admin savedAdmin = arepo.save(admin);
		System.out.println("Password cambiata con successo");
		return ResponseEntity.ok().body(savedAdmin);
											
	}
	@GetMapping("/password_change_admin")
    public ModelAndView showChangePswAdminForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
        
		System.out.println("GET /password_change_admin");
		if(isJwtValid(jwt)) return new ModelAndView("redirect:/login_admin"); 

        return new ModelAndView("password_change_admin");
    }
	@PostMapping("/password_reset_admin")
	@ResponseBody
	public ResponseEntity<?> resetPasswordAdmin (@RequestBody Admin admin1,
												 @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
		System.out.println("Token valido? (password_reset_admin)");
		if( isJwtValid(jwt)){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Già loggato");
		}
		System.out.println("Token non valido (password_reset_admin)");

		//Definizione amministratore
		//Controlliamo che esista nel repository un admin con la mail specificata
		Admin admin = arepo.findById(admin1.getEmail()).orElse(null);
		System.out.println("La mail esiste? (password_change_admin)");
		if (admin == null){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email non trovata");
		}
		
		//Generazione token di reset
		System.out.println("Generazione token di reset...");
		// String resetToken = generateToken(admin1);
		String resetToken = generateToken(admin);
		System.out.println("resetToken:" + resetToken);
		System.out.println("Token creato correttamente");
		admin.setResetToken(resetToken);
		

		//Salvataggio admin nel db
		Admin savedAdmin = arepo.save(admin);
		System.out.println("savedAdmin: " + savedAdmin);
		System.out.println("Amministratore salvato correttamente nel DB");
		try {
            emailService.sendPasswordResetEmail(savedAdmin.getEmail(), savedAdmin.getResetToken());
			// System.out.println("admin.getEmail"+ savedAdmin.getEmail()+"admin.getResetToken"+ savedAdmin.getResetToken());
			System.out.println("Congratulazioni! Controlla il tuo indirizzo di posta elettronica, ti è stato inviato un token di reset");
            return ResponseEntity.ok().body(savedAdmin);
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nell'invio del messaggio di posta");
        }
		
	}
	@GetMapping("/password_reset_admin")
    public ModelAndView showResetPswAdminForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {
        
		System.out.println("GET /password_reset_admin");
		if(isJwtValid(jwt)) return new ModelAndView("redirect:/login_admin"); 

        return new ModelAndView("password_reset_admin");
    }
	@GetMapping("/invite_admins")
	public ModelAndView showInviteAdmins(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {

		System.out.println("GET /invite_admins");
		if(isJwtValid(jwt)) return new ModelAndView("invite_admins");

        return new ModelAndView("login_admin");
	}
	@PostMapping("/invite_admins")
	@ResponseBody
	public ResponseEntity<?> inviteAdmins(@RequestBody Admin admin1,
										  @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
		System.out.println("Token valido? (invite_admins)");
		if( !isJwtValid(jwt)){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Attenzione, non sei loggato");
		}
		System.out.println("Token valido (invite_admins)"+"\n");
		
		//Controlliamo che non esista nel repository un admin con la mail specificata nell'invito
		Admin admin = arepo.findById(admin1.getEmail()).orElse(null);
		System.out.println("La mail esiste? (invite_admins)");
		if (admin != null){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email trovata, la persona che stai tentando di invitare è già un amministratore!");
		}
		//Creazione nuovo admin
		//Admin new_admin = new Admin("", "", "", admin1.getEmail(), "");
		Admin new_admin = new Admin("default", "default", "default", "default", "default");
		//this.userAdmin.setEmail(admin1.getEmail());
		new_admin.setEmail(admin1.getEmail());
		System.out.println("Nuovo admin creato correttamente");
		
		//Generazione token di invito per quella specifica mail
		System.out.println("Generazione token diinvito...");
		//String invitationToken = generateToken(this.userAdmin);
		String invitationToken = generateToken(new_admin);
		System.out.println("invitationToken:" + invitationToken);
		System.out.println("Token creato correttamente");
		//this.userAdmin.setInvitationToken(invitationToken);
		new_admin.setInvitationToken(invitationToken);
		//System.out.println(this.userAdmin);
		System.out.println(new_admin);
		

		//Salvataggio admin nel db
		//Admin savedAdmin = arepo.save(this.userAdmin);
		Admin savedAdmin = arepo.save(new_admin);
		System.out.println("savedAdmin: " + savedAdmin);
		System.out.println("Amministratore salvato correttamente nel DB");
		try {
            emailService.sendInvitationToken(savedAdmin.getEmail(), savedAdmin.getInvitationToken());
			// System.out.println("admin.getEmail"+ savedAdmin.getEmail()+"admin.getResetToken"+ savedAdmin.getResetToken());
			System.out.println("Mail inviata correttamente all'indirizzo specificato");
            //return ResponseEntity.ok().body(savedAdmin);
			return ResponseEntity.ok().body("Invitation token inviato correttamente all'indirizzo:"+ savedAdmin.getEmail());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nell'invio del messaggio di posta");
        }
		
	}
	@GetMapping("/login_with_invitation")
	public ModelAndView showLoginWithInvitationForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {

		System.out.println("GET /login_with_invitation");
		if(isJwtValid(jwt)) return new ModelAndView("login_admin");

        return new ModelAndView("login_with_invitation");
	}
	@PostMapping("/login_with_invitation")
	@ResponseBody
	public ResponseEntity<?> loginWithInvitation (@RequestBody Admin admin1,
												  @CookieValue(name = "jwt", required = false) String jwt, HttpServletRequest request) {
		//Controllo validità token
		System.out.println("Token valido? (login_with_invitation)");
		if(isJwtValid(jwt)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Attenzione, hai già un token valido!");
        }
		System.out.println("Token invalido, si può procedere alla registrazione tramite invito (login_with_invitation)");
		
		//Definizione amministratore
		Admin admin = arepo.findById(admin1.getEmail()).orElse(null);
		System.out.println("La mail esiste? (login_with_invitation)");

		if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email non trovata");
        }

		System.out.println("Mail trovata nel database (login_with_invitation)");
		Admin admin_invited = srepo.findAdminByInvitationToken(admin1.getInvitationToken());
		System.out.println("admin: " + admin_invited);
		
		// System.out.println("admin1.getInvitationToken: " + admin1.getInvitationToken() + "\n");				//OK
		// System.out.println("admin_invited.getInvitationToken: " + admin_invited.getResetToken() + "\n");		//OK

		System.out.println("Token di invio valido?");
		if(!admin_invited.getInvitationToken().equals(admin1.getInvitationToken())) {
			System.out.println("Token non valido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token di invito invalido!");
        }
		System.out.println("Token di invito valido! (login_with_invitation)");
		System.out.println("Si può procedere alla registrazione");

		//Salvataggio email
		admin.setEmail(admin1.getEmail());
		System.out.println("Mail correttamente salvata!");

		//Fase di registrazione
		//Controlli
		//1: Possibilità di inserire più nomi separati da uno spazio
		if (admin1.getNome().length() >=2 && admin1.getNome().length() <= 30 && Pattern.matches("[a-zA-Z]+(\\s[a-zA-Z]+)*", admin1.getNome()) ) {

			admin.setNome(admin1.getNome());
			System.out.println("Nome settato con successo");

		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome non valido");

		}

		//2: Possibilità di inserire più parole separate da uno spazio ed eventualmente da un apostrofo

		if ((admin1.getCognome().length() >= 2) && (admin1.getCognome().length() <= 30) && (Pattern.matches("[a-zA-Z]+(\\s?[a-zA-Z]+\\'?)*", admin1.getCognome()))) {
            
			admin.setCognome(admin1.getCognome());
			System.out.println("Cognome settato con successo");

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cognome non valido");
        }

		//3: L'username deve rispettare necessariamente il seguente formato: "[username di lunghezza compresa tra 2 e 30 caratteri]_invited"
		if (admin1.getUsername().length() >=2 && admin1.getUsername().length() <= 30 && (Pattern.matches(".*_invited$", admin1.getUsername()))) {

			admin.setUsername(admin1.getUsername());
			System.out.println("Username settato con successo");

		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username non valido, deve rispettare il seguente formato: [username di lunghezza compresa tra 2 e 30 caratteri]_invited");
		}

		//5: La password deve contenere almeno una lettera maiuscola, una minuscola, un numero ed un carattere speciale e deve essere lunga tra gli 8 e i 16 caratteri
		Matcher m = p.matcher(admin1.getPassword());

        if ((admin1.getPassword().length() >16) || (admin1.getPassword().length() < 8) || !(m.matches())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password non valida! La password deve contenere almeno una lettera maiuscola, una minuscola, un numero ed un carattere speciale e deve essere lunga tra gli 8 e i 16 caratteri");
        }
		String crypted = myPasswordEncoder.encoder().encode(admin1.getPassword());
		admin1.setPassword(crypted);
		admin.setPassword(admin1.getPassword());
		System.out.println("Password memorizzata e crittata con successo");

		admin1.setInvitationToken(null);
		admin.setInvitationToken(admin1.getInvitationToken());

		//Salvataggio admin nel db
		Admin savedAdmin = arepo.save(admin);
		System.out.println(savedAdmin);
		System.out.println("Registrazione avvenuta con successo!");
		return ResponseEntity.ok().body(savedAdmin);
											
	}

	//MODIFICA (17/04/2024) : Aggiunta pagina di configurazione della nuova modalità di gioco
	@GetMapping("/scalata")
	public ModelAndView showGamePageScalata(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {

		System.out.println("(GET /scalata) Token JWT valido?");
		if(isJwtValid(jwt)) return new ModelAndView("scalata");

		System.out.println("(GET /scalata) Token JWT invalido");
        return new ModelAndView("login_admin");
	}
	// //MODIFICA (18/02/2024) : Aggiunta menù
    // @GetMapping("/menu")
    // public ModelAndView showMenuForm(HttpServletRequest request, @CookieValue(name = "jwt", required = false) String jwt) {

    //     System.out.println("GET (/menu)");
    //     if(isJwtValid(jwt)) return new ModelAndView("redirect:/loginAdmin"); 

    //     return new ModelAndView("menu");
    // }
	//FINE MODIFICA

    // @PostMapping("/logout_admin")
	// @ResponseBody
    // public ResponseEntity<String> logoutAdmin(@RequestParam("authToken") String authToken, HttpServletResponse response) {
        
	// 	System.out.println("PostMapping(\"/logout_admin\")");
	// 	AuthenticatedAdmin authenticatedAdmin = authenticatedAdminRepository.findByAuthToken(authToken);
	// 	System.out.println("Admin autenticato?");
    //     if (authenticatedAdmin == null) {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin non autenticato");
    //     }

    //     Cookie jwtTokenCookie = new Cookie("jwt", null);
	// 	System.out.println("Logout (POST)");
    //     jwtTokenCookie.setMaxAge(0);
    //     response.addCookie(jwtTokenCookie);

    //     authenticatedAdminRepository.delete(authenticatedAdmin);
    //     return ResponseEntity.ok("Logout avvenuto con successo");
    // }

	//FINE MODIFICA


	
}
