package RemoteCCC;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.SpringApplication;

import RemoteCCC.App.App;

/*
 Objective: Each time the Config class is loaded, a new timestamp is generated and used
 to create unique paths for compiling, testing, and coverage
 */

public class Config {

    private static String usr_path = System.getProperty("user.dir"); 
    private static String sep = File.separator;
    private static String timestamp = null;

    final static String packageDeclaration  = "package ClientProject;\n";

    // Constructor
    public Config() {

        // Create a new timestamp every time the Config class is loaded
        System.out.println("(Config.java) Constructor called");
        getTimestamp();
    }

    // Create a unique timestamp for each execution
    private static String getTimestamp() {
        if (timestamp == null) {
            timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            System.out.println("(Config.java) Timestamp generated: " + timestamp);
        }
        return timestamp;
    }
    public static String setTimestamp(){
        return timestamp = null;
    }

    /*
     * pathCompiler: path to the folder where the code will be compiled
     * testingClassPath: path to the folder where the testing class will be saved
     * underTestClassPath: path to the folder where the under test class will be saved
     * coverageFolder: path to the folder where the coverage report will be saved
     */

     //Generate paths based on the timestamp
     private  String getPathCompiler() {
        return usr_path + sep + "ClientProject" + sep + getTimestamp() + sep;
    }

    private  String getTestingClassPath() {
        return usr_path + sep + "ClientProject" + sep + timestamp + sep + "src" + sep + "test" + sep + "java" + sep + "ClientProject" + sep;
    }
    private  String getUnderTestClassPath() {
        return usr_path + sep + "ClientProject" + sep + timestamp + sep + "src" + sep + "main" + sep + "java" + sep + "ClientProject" + sep;
    }
    private  String getcoverageFolderPath() {
        return usr_path + sep + "ClientProject" + sep + timestamp + sep + "target" + sep + "site" + sep + "jacoco" + sep + "jacoco.xml" + sep;
    }

    public  String retrievePathCompiler() {return getPathCompiler();}
    public  String retrieveTestingClassPath() {return getTestingClassPath();}
    public  String retrieveUnderTestClassPath() {return getUnderTestClassPath();}
    public  String retrieveCoverageFolder() {return getcoverageFolderPath();}

    // (OLD VERSION) pathCompiler = usr_path/ClientProject/
    // (NEW VERSION) pathCompiler = usr_path/ClientProject/timestamp/

    // (OLD VERSION) testingClassPath = usr_path/ClientProject/src/test/java/ClientProject/
    // (NEW VERSION) testingClassPath = usr_path/ClientProject/timestamp/src/test/java/ClientProject/
   
    // (OLD VERSION) underTestClassPath = usr_path/ClientProject/src/main/java/ClientProject/
    // (NEW VERSION) underTestClassPath = usr_path/ClientProject/timestamp/src/main/java/ClientProject/
    
    // (OLD VERSION) coverageFolder = usr_path/ClientProject/target/site/jacoco/jacoco.xml
    // (NEW VERSION) coverageFolder = usr_path/ClientProject/timestamp/target/site/jacoco/jacoco.xml
    
    public  String getpackageDeclaretion(){return packageDeclaration;}
    public  String getUsrPath() {return usr_path;}
    public  String getsep() {return sep;}
}
