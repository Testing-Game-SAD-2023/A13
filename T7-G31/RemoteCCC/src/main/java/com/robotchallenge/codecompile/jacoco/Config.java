/*
    *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
    *   All rights reserved.

    *   Licensed under the Apache License, Version 2.0 (the "License");
    *   you may not use this file except in compliance with the License.
    *   You may obtain a copy of the License at

    *   http://www.apache.org/licenses/LICENSE-2.0

    *   Unless required by applicable law or agreed to in writing, software
    *   distributed under the License is distributed on an "AS IS" BASIS,
    *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    *   See the License for the specific language governing permissions and
    *   limitations under the License.
 */
package com.robotchallenge.codecompile.jacoco;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {

    /*
        *  Stefano: queste prima erano variabili statiche, è un errore poiché devo fare multi threading.
     */
    private final String usrPath;
    private String timestamp;
    private final Logger logger = LoggerFactory.getLogger(Config.class);

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
        logger.info("[CONFIG] Constructor chiamato timestamp: {}", this.timestamp);
    }

    // genero timestamp unico 
    private String generateTimestamp() {
        //questa funzione è thread safe 
        String currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        // Genera 4 cifre randomiche thread-safe
        int randomFourDigits = ThreadLocalRandom.current().nextInt(1000, 10000); // 1000 (incluso) e 10000 (escluso)
        // Concatena il currentTimestamp e le cifre casuali
        return currentTimestamp + randomFourDigits;
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

    public String getPackageDeclaration() {
        return "package ClientProject;\n";
    }

    public String getUsrPath() {
        return usrPath;
    }

    public String getsep() {
        return File.separator;
    }

}
