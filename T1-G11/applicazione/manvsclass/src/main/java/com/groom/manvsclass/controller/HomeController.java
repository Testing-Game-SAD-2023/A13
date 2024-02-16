package com.groom.manvsclass.controller;


import java.io.IOException;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	private final Admin userAdmin= new Admin("default","default","default","default");
	private final LocalDate today = LocalDate.now();
	private final SearchRepositoryImpl srepo;
	
	public HomeController(SearchRepositoryImpl srepo)
	{
		this.userAdmin.setUsername("default");
		this.srepo=srepo;
	}

	@GetMapping("/home_adm")
	public String showHomeAdmin() {
		return "home_adm";
	}
	
	@GetMapping("/loginAdmin")
	public String showLoginAdmin() {
		return "login_admin";
	}

	@GetMapping("/registraAdmin")
	public String showRegistraAdmin() {
		return "registraAdmin";
	}

	@GetMapping("/modificaClasse")
	public String showModificaClasse() {
		return "modificaClasse";
	}
	
	@GetMapping("/uploadClasse")
	public String showUploadClasse() {
		return "uploadClasse";
	}

	@GetMapping("/uploadClasseAndTest")
	public String showUploadClasseAndTest() {
		return "uploadClasseAndTest";
	}

	@GetMapping("/reportClasse")
	public String showReportClasse() {
		return "reportClasse";
	}

	@GetMapping("/Reports")
	public String showReports() {
		return "Reports";
	}

	@GetMapping("/interaction")
	@ResponseBody
	public	List<interaction>	elencaInt()
	{
		return repo_int.findAll();
	}
	
	@GetMapping("/findreport")
	@ResponseBody
	public	List<interaction> elencaReport()
	{
		return srepo.findReport();
	}
	
	//Solo x testing
	@GetMapping("/getLikes/{name}")
	@ResponseBody
	public long likes(@PathVariable String name)
	{
		long likes=srepo.getLikes(name);
		
		return likes;
	}
	
	@PostMapping("/newinteraction")
	@ResponseBody
	public interaction UploadInteraction(@RequestBody interaction interazione)
	{
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
	
	
	@GetMapping("/home")
	@ResponseBody
	public	List<ClassUT>	elencaClassi()
	{
		return repo.findAll();
	}
	
	@GetMapping("/orderbydate")
	@ResponseBody
	public List<ClassUT> ordinaClassi()
	{
		return srepo.orderByDate();
	}

	@GetMapping("/orderbyname")
	@ResponseBody
	public List<ClassUT> ordinaClassiNomi()
	{
		return srepo.orderByName();
	}
	
	@GetMapping("/Cfilterby/{category}")
	@ResponseBody
	public List<ClassUT> filtraClassi(@PathVariable String category)
	{
		return srepo.filterByCategory(category);
	}
	
	@GetMapping("/Cfilterby/{text}/{category}")
	@ResponseBody
	public	List<ClassUT>	filtraClassi(@PathVariable String text,@PathVariable String category)
	{
		return srepo.searchAndFilter(text,category);
	}
	
	@GetMapping("/Dfilterby/{difficulty}")
	@ResponseBody
	public List<ClassUT> elencaClassiD(@PathVariable String difficulty)
	{
		return srepo. filterByDifficulty(difficulty);
	}
	
	@GetMapping("/Dfilterby/{text}/{difficulty}")
	@ResponseBody
	public	List<ClassUT>	elencaClassiD(@PathVariable String text,@PathVariable String difficulty)
	{
		return srepo.searchAndDFilter(text,difficulty);
	}
	

	@PostMapping("/insert")
	@ResponseBody
	public ClassUT UploadClasse(@RequestBody ClassUT classe)
	{
		LocalDate currentDate = LocalDate.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String data = currentDate.format(formatter);
        Operation operation1= new Operation((int)orepo.count(),userAdmin.getUsername(),classe.getName(),0,data);
        orepo.save(operation1);
		return repo.save(classe);
	}

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
	@PostMapping("/uploadFile")
	@ResponseBody
	public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile classFile, @RequestParam("model") String model) throws IOException
	{
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
		//Salva i dati sullla classe nel database
		repo.save(classe);
		return new ResponseEntity<>(response,HttpStatus.OK);
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
    public ResponseEntity<FileUploadResponse> uploadTest(@RequestParam("file") MultipartFile classFile, @RequestParam("model") String model, @RequestParam("test") MultipartFile testFile,
	
															@RequestParam("testEvo") MultipartFile testFileEvo) throws IOException {
        
        //Legge i metadati della classe della parte "model" del body HTTP e li salva in un oggetto ClasseUT
        ObjectMapper mapper = new ObjectMapper();
        ClassUT classe = mapper.readValue(model, ClassUT.class);
        
        //Salva il nome del file della classe caricato
        String fileNameClass = StringUtils.cleanPath(classFile.getOriginalFilename());
        long size = classFile.getSize();
        
        //Salva la classe nel filesystem condiviso
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
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@PostMapping("/delete/{name}")
	@ResponseBody
	public ClassUT eliminaClasse(@PathVariable String name) {
		Query query= new Query(); 
	   query.addCriteria(Criteria.where("name").is(name));
	   this.eliminaFile(name);
	   LocalDate currentDate = LocalDate.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
       String data = currentDate.format(formatter);
       Operation operation1= new Operation((int)orepo.count(),userAdmin.getUsername(),name,2,data);
       orepo.save(operation1);
	   return mongoTemplate.findAndRemove(query, ClassUT.class);
	}

	@PostMapping("/deleteFile/{fileName}")
	@ResponseBody
	public ResponseEntity<String> eliminaFile(@PathVariable String fileName) {
	  String folderPath = "Files-Upload/"+ fileName; 
	  File directoryRandoop = new File("/VolumeT9/app/FolderTree/" + fileName);
	File directoryEvo = new File("/VolumeT8/FolderTreeEvo/" +  fileName);
	  
	        File folderToDelete = new File(folderPath);
	        if (folderToDelete.exists() && folderToDelete.isDirectory()) {
	        	try {
	        		FileUploadUtil.deleteDirectory(folderToDelete);
	        		FileUploadUtil.deleteDirectory(directoryRandoop);
	        		FileUploadUtil.deleteDirectory(directoryEvo);
	                return new ResponseEntity<>("Cartella eliminata con successo.", HttpStatus.OK);
	            } catch (IOException e) {
	                return new ResponseEntity<>("Impossibile eliminare la cartella.", HttpStatus.INTERNAL_SERVER_ERROR);
	            }
	        } else {
	            return new ResponseEntity<>("Cartella non trovata.", HttpStatus.NOT_FOUND);
	        }
	 }
	    
	

	
	@GetMapping("/home/{text}")
	@ResponseBody
	public	List<ClassUT>	ricercaClasse(@PathVariable String text)
	{
		return srepo.findByText(text);
	}
	
	@GetMapping("/downloadFile/{name}")
	@ResponseBody
	    public ResponseEntity<?> downloadClasse(@PathVariable("name") String name) throws Exception {
		 	   List<ClassUT> classe= srepo.findByText(name);
		 	   
	           return FileDownloadUtil.downloadClassFile(classe.get(0).getcode_Uri());
	    }
	 

@PostMapping("/update/{name}")
@ResponseBody
public ResponseEntity<String> modificaClasse(@PathVariable String name, @RequestBody ClassUT newContent) {
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
	    }


@PostMapping("/registraAdmin")
@ResponseBody
public Admin registraAdmin(@RequestBody Admin admin1)
{
	this.userAdmin.setUsername(admin1.getUsername());
    this.userAdmin.setPassword(admin1.getPassword());
	return arepo.save(admin1);
}

@PostMapping("/loginAdmin")
@ResponseBody
public String loginAdmin(@RequestBody Admin admin1) {
    Admin admin = srepo.findAdminByUsername(admin1.getUsername());
    if (admin.getPassword().equals(admin1.getPassword())) {   	
        this.userAdmin.setUsername(admin.getUsername());
        this.userAdmin.setPassword(admin.getPassword());
        return "ok";
    } else {
    	return "utente non loggato";
    }
}

@GetMapping("/admins/{username}")
@ResponseBody
public Admin getAdminByUsername(@PathVariable String username) {
    return srepo.findAdminByUsername(username);
}

@GetMapping("/player")
	public String showplayer() {
		return "player";
	}

	@GetMapping("class")
	public String showclass() {
		return "class";
	}


	
}
