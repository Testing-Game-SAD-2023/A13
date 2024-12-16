package com.example.EvoSuiteCompiler;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class Config {

    private final String usrPath;
    private String timestamp;

    private String robotTestDir = null;

    /*
        * Questo enum definisce i vari percorsi utilizzati nel progetto, 
        * migliorando la leggibilità e la manutenibilità del codice. 
        * Puoi facilmente aggiungere o modificare i percorsi nel futuro.
     */
    public enum PathType {
        /*
            *  Ho usato i nomi standard TEST e MAIN, corrispondono a undertestclass e testingclass
         */
        ROOT(""),
        SRC_TEST("src" + File.separator + "test" + File.separator + "java" + File.separator),
        SRC_MAIN("src" + File.separator + "main" + File.separator + "java" + File.separator),
        TARGET_SITE("target" + File.separator + "site" + File.separator + "jacoco" + File.separator + "jacoco.xml");

        private final String path;

        PathType(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
    /*
        *   Questo metodo centralizza la logica di costruzione dei percorsi, 
        *   riducendo la duplicazione del codice e facilitando eventuali modifiche ai percorsi.
     */
    private String buildPath(PathType pathType) {
        return usrPath + timestamp + File.separator + pathType.getPath();
    }

    // Constructor
    public Config() {
        this.usrPath = System.getProperty("user.dir");
        this.timestamp = generateTimestamp();
        System.out.println("[CONFIG] Constructor chiamato timestamp: " + this.timestamp);
    }

    // genero timestamp unico 
    private String generateTimestamp() {
        //questa funzione è thread safe 
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        // Genera 4 cifre randomiche thread-safe
        int randomFourDigits = ThreadLocalRandom.current().nextInt(1000, 10000); // 1000 (incluso) e 10000 (escluso)
        // Concatena il timestamp e le cifre casuali
        return timestamp + randomFourDigits;
    }

    /*
        * Questo può essere utile in scenari di test o per altre necessità specifiche.
     */
    public void setTimestamp(String customTimestamp) {
        this.timestamp = customTimestamp;
    }

    /*
        *  Questi metodi sono diventate tutte semplici GET non c'è più quel retrive
     */
    public String getPathCompiler() {
        return buildPath(PathType.ROOT);
    }

    public String getTestingClassPath() {
        return buildPath(PathType.SRC_TEST);
    }

    public String getUnderTestClassPath() {
        return buildPath(PathType.SRC_MAIN);
    }

    public String getCoverageFolderPath() {
        return buildPath(PathType.TARGET_SITE);
    }

    public String getUsrPath() {
        return usrPath;
    }

    public String getsep() {
        return File.separator;
    }

    // Metodo di Set per la directory in cui sono contenuti i test dei robot
    public void setRobotTestDir(String path) {
        this.robotTestDir = path;
    }

    // Metodo di Get per la directory in cui sono contenuti i test dei robot
    public String getRobotTestDir() {
        if (this.robotTestDir == null) {
            throw new IllegalStateException("[Config] Il path per i test dei robot non è stato configurato.");
        }
        return this.robotTestDir;
    }

}
