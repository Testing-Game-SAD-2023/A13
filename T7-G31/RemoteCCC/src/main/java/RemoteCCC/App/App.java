package RemoteCCC.App;


import RemoteCCC.Config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.File;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ServletComponentScan
@RestController
public class App {

    public static void main(String[] args) {
        System.out.println("Starting RemoteCCC application...(MAIN)");
		SpringApplication.run(App.class, args);

        //Get the current user directory in a Java application
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("user.dir: " + currentDirectory);

    }
    /**
     * REST endpoint for handling POST requests with JSON body containing two Java files.
     * Compiles the two files, runs the test file on the compiled first file, and measures test coverage with Jacoco.
     * @param request The JSON request containing the two Java files.
     * @return A JSON response containing the test coverage results.
     * @throws IOException If there is an I/O error reading or writing files.
     * @throws InterruptedException
     */

    @PostMapping(value = "/compile-and-codecoverage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseDTO compileAndTest(@RequestBody RequestDTO request) throws IOException, InterruptedException {

        //Call constructor to generate a new timestamp
        Config config = new Config();

        createDirectoriesAndCopyPom(config);

        String testingClassName   = request.getTestingClassName();
        String testingClassCode   = request.getTestingClassCode();

        String underTestClassName = request.getUnderTestClassName();
        String underTestClassCode = request.getUnderTestClassCode();
        
        // Salvataggio dei due file su disco: occorre specificare il nome della classe, per la corretta compilazione
        saveCodeToFile(testingClassName, testingClassCode, config.retrieveTestingClassPath(), config);
        saveCodeToFile(underTestClassName, underTestClassCode, config.retrieveUnderTestClassPath(), config);

        //Aggiunge la dichiarazione del package ai file java ricevuti.
        //addPackageDeclaration(firstFilePath, secondFilePath);

        //Output di ritorno del comando maven.
        String []output_maven={""};

        ResponseDTO response = new ResponseDTO();

        if(compileExecuteCovarageWithMaven(output_maven, config)){
            String retXmlJacoco = readFileToString(config.retrieveCoverageFolder());//zipSiteFolderToJSON(Config.getzipSiteFolderJSON()).toString();
            response.setError(false);
            response.setoutCompile(output_maven[0]);
            response.setCoverage(retXmlJacoco);

        }else
        {
            response.setError(true);
            response.setoutCompile(output_maven[0]);
            response.setCoverage(null);            
        }
        deleteFile(underTestClassName, testingClassName, config);
        deleteTemporaryDirectories(config);
        Config.setTimestamp();
        System.out.println("Timestamp setted to null");
        return response;
    }


 
    private static String readFileToString(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String contents = new String(bytes);
        return contents;
    }


    /*/**
     * Metodo per dichiarare i package all'interno dei file ".java".
     * @param file1path Path del primo file.
     * @param file2Path Path del secondo file.
     * @throws IOException Se ci sono errori I/O di lettura o scrittura su file.
     * @throws InterruptedException
     */
    /* 
    private static void addPackageDeclaration(Path file1Path, Path file2Path) throws IOException {
        String packageDeclaration = Config.getpackageDeclaretion();
    
        String file1Content = Files.readString(file1Path, StandardCharsets.UTF_8);
        String file2Content = Files.readString(file2Path, StandardCharsets.UTF_8);
    
        file1Content = packageDeclaration + file1Content;
        file2Content = packageDeclaration + file2Content;
    
        Files.write(file1Path, file1Content.getBytes(StandardCharsets.UTF_8));
        Files.write(file2Path, file2Content.getBytes(StandardCharsets.UTF_8));
    }

    */
    
    private void deleteFile(String underTestClassName, String testingClassName, Config config)throws IOException
    {
        File file1 = new File(config.retrieveUnderTestClassPath()+underTestClassName);
        file1.delete();
        File file2 = new File(config.retrieveTestingClassPath() + testingClassName);
        file2.delete();
    }
   

    //esegue compilazione con maven per ritornare eventuali errori utente
    private static boolean compileExecuteCovarageWithMaven(String []ret, Config config) throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command("mvn", "clean", "compile", "test");
        processBuilder.directory(new File(config.retrievePathCompiler()));
    
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null)
            ret[0] += line += "\n";
        int exitCode = process.waitFor();
       
        // Legge il contenuto del buffer del terminale
        // InputStream inputStream = process.getInputStream();
        // byte[] buffer = new byte[inputStream.available()];
        // inputStream.read(buffer);
        // ret[0] = new String(buffer, StandardCharsets.UTF_8);
        if (exitCode == 0) {
            System.out.println("Maven clean compile executed successfully.");
            return true;
        } else {
            System.out.println("Error executing Maven clean compile.");
            return false;
        }

    }

    private void createDirectoriesAndCopyPom(Config config) {
        
        // Creation of the parent directories if do not exist
        System.out.println("Creating directories and copying pom.xml file...");
        File pathCompilerDir = new File(config.retrievePathCompiler());
        if (!pathCompilerDir.exists()) {
            pathCompilerDir.mkdirs(); 
            System.out.println("(App.java) pathCompilerDir created"+ pathCompilerDir);

            /*Copy the pom.xml file to the new directory with the timestamp dynamically generated

                pom.xml relative path: T7-G31/RemoteCCC/ClientProject/pom.xml
            */
            File pomFile = new File(config.getUsrPath() + config.getsep() + "ClientProject" + config.getsep() + "pom.xml");
            File destPomFile = new File(config.retrievePathCompiler() + "pom.xml");
            try {
                Files.copy(pomFile.toPath(), destPomFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("(App.java) pom.xml copied to pathCompilerDir successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File testingClassPathDir = new File(config.retrieveTestingClassPath());
        if (!testingClassPathDir.exists()) {
            testingClassPathDir.mkdirs(); 
            System.out.println("(App.java) testingClassPathDir created"+ testingClassPathDir);
        }
        File underTestClassPathDir = new File(config.retrieveUnderTestClassPath());
        if (!underTestClassPathDir.exists()) {
            underTestClassPathDir.mkdirs(); 
            System.out.println("(App.java) underTestClassPathDir created"+ underTestClassPathDir);
        }
        File coverageFolderDir = new File(config.retrieveCoverageFolder());
        if (!coverageFolderDir.exists()) {
            coverageFolderDir.mkdirs(); 
            System.out.println("(App.java) coverageFolderDir created"+ coverageFolderDir);
        }
    } 

    private void deleteTemporaryDirectories(Config config) {
        try {
            FileUtils.deleteDirectory(new File(config.retrievePathCompiler()));
            System.out.println("Temporary directories deleted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    /**
     * Metodo per salvare un file ".java"
     * @param nameclass Nome della classe da salvare.
     * @param code      Codice java da salvare.
     * @param path      Stringa che descrive il path dove slavare il file.
     * @return Un oggetto Path che localizza il file salvato.
     * @throws IOException Se ci sono errori I/O di lettura o scrittura su file.
     */
    private Path saveCodeToFile(String nameclass, String code, String path, Config config) throws IOException {
        String packageDeclaration = config.getpackageDeclaretion();
        code = packageDeclaration + code;
        File tempFile = new File(path + nameclass);
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(code);
        }
        
        return tempFile.toPath();
    }
   


    //Data Transfer Object
    private static class RequestDTO {

        private String testingClassName;
        private String testingClassCode;
        
        private String underTestClassName;
        private String underTestClassCode;


        public String getTestingClassName(){
            return testingClassName;
        }
        
        public String getTestingClassCode() {
            return testingClassCode;
        }


        public String getUnderTestClassName(){
            return underTestClassName;
        }

        public String getUnderTestClassCode() {
            return underTestClassCode;
        }     
        
    }

    private static class ResponseDTO{

        private Boolean error;
        private String outCompile;
        private String coverage;


        public Boolean getError(){
            return error;
        }

        public String getOutCompile(){
            return outCompile;
        }
        public String getCoverage(){
            return coverage;
        }

        public void setError(boolean error)
        {
            this.error = error;
        }
        
        public void setoutCompile(String outCompile)
        {
            this.outCompile = outCompile;
        }

        public void setCoverage(String coverage)
        {
            this.coverage = coverage;
        }


    }


}
