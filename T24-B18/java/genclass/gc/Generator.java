package genclass.gc;


import java.io.File;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Generator {
/**Classe che utilizza un endpoint REST per generare una classe da un LLM e relativi test di Randoop ed Evosuite, compreso il class model  ENAC*/
	
private String generatedclassname;
/**
 * @return the generatedclassname
 */
public String getGeneratedclassname() {
	return generatedclassname;
}




private File generatedclassfile;
/**
 * @return the generatedclassfile
 */
public File getGeneratedclassfile() {
	return generatedclassfile;
}




private JSONObject jmodel;
    
    /**
 * @return the jmodel
 */
public JSONObject getJmodel() {
	return jmodel;
}



	public void computeJavaClass() throws Exception   
    {
        System.out.println("Computing...");

		/**Contatta il server, genera la classe e compila il model*/
		
		Utils.deleteDirectory(new File("./generated"));//pulizia file temporanei
		Utils.createTargetDirectory(new File("./generated").getCanonicalPath());
    	/**Contatta il server Ollama, ritorna una classe java generata dal LLM con delimitatori  ```java ed  ```.La salva nella cartella generated.   */
    String tmpclass = null;
    String response = contactOllamaServer("Generate a complete and correct java class from a thing");//server LLM con prompt
   	
    Pattern pattern = Pattern.compile("```java(.*?)```",Pattern.DOTALL);//estrae la classe dalla risposta
    
    Matcher matcher = pattern.matcher(response);
    
    while (matcher.find()) {
    	
    	    tmpclass=	matcher.group(1);
    	    
    	    
  }
    
    if(tmpclass==null) {
    	throw new Exception("Model error.");
    }else 
    {
    	
     generatedclassname=Utils.extractClassName(tmpclass);
    File file = new File("generated/"+generatedclassname+".java") ;
    generatedclassfile=file.getCanonicalFile();
    		
    
    
    
    FileWriter writer = new FileWriter(file);
    writer.write(tmpclass);
    writer.close();

    jmodel = new JSONObject();
    jmodel.put("name", generatedclassname);
    jmodel.put("date",LocalDate.now());
    jmodel.put("difficulty","Intermediate");
    jmodel.put("code_Uri",generatedclassfile.getCanonicalPath());
    jmodel.put("description","LLM Generated");
    JSONArray categoryArray = new JSONArray(); 
    categoryArray.put("LLM Generated"); 
    categoryArray.put("LLM Generated");
    categoryArray.put("LLM Generated");
    jmodel.put("Category",categoryArray);

    
    
    }
    
    }
    

    
    
    public void computeJunitTest() throws Exception
    {
    	/**Compila la classe(java 8),lancia randoop ed evosuite per i test,e ne zippa i test*/
    	//File randoopfile=new File(Main.class.getClassLoader().getResource("randoop-all-4.3.3.jar").getPath());
    	//File evofile=new File(Main.class.getClassLoader().getResource("evosuite-1.0.6.jar").getPath());
         File randoopfile=new File("randoop-all-4.3.3.jar");
         File evofile=new File("evosuite-1.0.6.jar");
   System.out.println(randoopfile);
   	    String commandjavac ="javac --release 8 "+generatedclassfile.getCanonicalPath();
     
     
     
     
     
     
     
     
     
     
     
     
     
          String commandevosuite=null;
          String commandrandoop=null;
     	  if(System.getProperty("os.name").toLowerCase().contains("win")) {
         	  commandrandoop = "java -cp "+generatedclassfile.getParentFile().getCanonicalPath()+";"+randoopfile.getCanonicalPath()+" randoop.main.Main gentests --testclass="+generatedclassname+" --time-limit=60";

     	   //commandevosuite=" export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre && export PATH=$JAVA_HOME/bin:$PATH && java -jar   "
        //  commandevosuite="powershell docker run  --rm -v "+generatedclassfile.getParentFile().getCanonicalPath()+":/evosuite evosuite/evosuite:latest -class "+generatedclassname+" -projectCP "+"/evosuite";
     	  }else
     	  {
         	   commandrandoop = "java -cp "+generatedclassfile.getParentFile().getCanonicalPath()+":"+randoopfile.getCanonicalPath()+" randoop.main.Main gentests --testclass="+generatedclassname+" --time-limit=60";

        	   commandevosuite=" export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre && export PATH=$JAVA_HOME/bin:$PATH && java -jar "+evofile.getCanonicalPath()+" -class "+generatedclassname+" -projectCP "+generatedclassfile.getParentFile().getCanonicalPath();
               System.out.println(commandevosuite);
          // commandevosuite="docker run  --rm -v "+generatedclassfile.getParentFile().getCanonicalPath()+":/evosuite evosuite/evosuite:latest -class "+generatedclassname+" -projectCP "+"/evosuite";

     	  }
     	  System.out.println(commandrandoop);

		execute(commandjavac);
		execute(commandrandoop);


		
	    Utils.createTargetDirectory(generatedclassfile.getParentFile().getCanonicalPath()+"/01Level");
         Utils.moveFilesToTargetDirectory(generatedclassfile.getParentFile().getParentFile().getCanonicalPath(), generatedclassfile.getParentFile().getCanonicalPath()+"/01Level","Regression");
         Utils.zipFiles(generatedclassfile.getParentFile().getCanonicalPath()+"/01Level", generatedclassfile.getParentFile().getCanonicalPath()+"/GenRandoopTests.zip","01Level/");

         Utils.deleteDirectory(new File(generatedclassfile.getParentFile().getCanonicalPath()+"/01Level"));

	     
    	  if(System.getProperty("os.name").toLowerCase().contains("win")) {
    			execute(commandevosuite);

    		  //
    	  }else
    	  {
    		  
    	  
         File script=Utils.createBashTempScript(commandevosuite);
         
         execute("sh "+script.getCanonicalPath());
         Utils.createTargetDirectory(generatedclassfile.getParentFile().getCanonicalPath()+"/01Level");
	     Utils.renameDirectory(generatedclassfile.getParentFile().getParentFile().getCanonicalPath()+"/evosuite-report", generatedclassfile.getParentFile().getCanonicalPath()+"/01Level/TestReport");
	     Utils.renameDirectory(generatedclassfile.getParentFile().getParentFile().getCanonicalPath()+"/evosuite-tests", generatedclassfile.getParentFile().getCanonicalPath()+"/01Level/TestSourceCode");
	     Utils.zipFiles(generatedclassfile.getParentFile().getCanonicalPath()+"/01Level", generatedclassfile.getParentFile().getCanonicalPath()+"/GenEvosuiteTests.zip","01Level/");
	     Utils.deleteDirectory(new File(generatedclassfile.getParentFile().getCanonicalPath()+"/01Level"));

    	  }
			
			

			
		System.out.println("Success.\nEnd.");
		
          
		

              
          

          

      
     } 
    
    private void execute(String command) throws Exception {
    	/**Esegue comando da shell */
    	System.out.println("exec :"+command);

    	   ProcessBuilder processBuilderjavac = new ProcessBuilder(command.split(" "));

           Process process = processBuilderjavac.start();

           // Leggi l'output del processo
           try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
               String line;
               while ((line = reader.readLine()) != null) {
                   System.out.println(line);
               }
               while ((line = errorReader.readLine()) != null) {
                   System.err.println(line);
               }
           }

           // Attendi che il processo termini
           int exitCode = process.waitFor();
           if(exitCode!=0)
           {
        	   throw new Exception("Child process error");
        	  
           }
           System.out.println("Process exit code: " + exitCode);
		
    }

    
    
    

    

 private String contactOllamaServer(String prompt) throws Exception {
	
	 /**Contatta il server Ollama ,passando i parametri*/
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = String.format("{\"model\": \"qwen2.5-coder:0.5b\", \"prompt\": \"%s\", \"stream\": false}", prompt);

        HttpRequest request = HttpRequest.newBuilder()
             //  .uri(new URI("http://localhost:11434/api/generate"))//era 8004
                .uri(new URI("http://host.docker.internal:11434/api/generate"))//per docker

                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            try (JsonReader jsonReader = Json.createReader(new StringReader(response.body()))) {
                JsonObject jsonResponse = jsonReader.readObject();
                return jsonResponse.getString("response"); 
            }
        } else {
            throw new RuntimeException("Failed to contact Ollama server: " + response.statusCode());
        }
    }

 

 
 
 }


