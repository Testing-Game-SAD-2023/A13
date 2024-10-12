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

package RemoteCCC.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.FileUtils;

public class CompilationService {

    /*
     * Config gestisce i path delle directory e fornisce un id univoco 
     * serve solo per non tenere tutta la logica qua
     */
    private final Config config;
    private final String testingClassName;
    private final String testingClassCode;
    private final String underTestClassName;
    private final String underTestClassCode; 
    /*
     *  Qui metto tutte le variabili che ottengo come output
     *  Attenzione outputMaven viene settato da compileExecuteCoverageWithMaven()
     *  mentre errors e Coverage in base al suo valore vengono settati da compileAndTest
     */
    public String outputMaven; 
    public Boolean Errors;
    public String Coverage;

    public CompilationService(String testingClassName, String testingClassCode, 
    String underTestClassName, String underTestClassCode) {
        this.config = new Config();
        this.testingClassName = testingClassName;
        this.testingClassCode = testingClassCode;
        this.underTestClassName = underTestClassName;
        this.underTestClassCode = underTestClassCode;
        this.outputMaven = null;
    }

    public  void compileAndTest() throws IOException, InterruptedException {
        createDirectoriesAndCopyPom();
        saveCodeToFile(this.testingClassName, this.testingClassCode, config.getTestingClassPath());
        saveCodeToFile(this.underTestClassName, this.underTestClassCode, config.getUnderTestClassPath());

        if (compileExecuteCoverageWithMaven()) {
            this.Coverage = readFileToString(config.getCoverageFolderPath());
            this.Errors = false;
        } else {
            this.Errors = true;
            this.Coverage = null;     
        }
        
        deleteFile(underTestClassName, testingClassName);
        deleteTemporaryDirectories();
    }

    private  void createDirectoriesAndCopyPom() {
        System.out.println("[Compilation Service] Creazioen directory e pom file");
        try{
            createDirectoryIfNotExists(config.getPathCompiler());
            copyPomFile();
            createDirectoryIfNotExists(config.getTestingClassPath());
            createDirectoryIfNotExists(config.getUnderTestClassPath());
            createDirectoryIfNotExists(config.getCoverageFolderPath());
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
    
    private  void createDirectoryIfNotExists(String path) throws IOException {
        File directory = new File(path);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("[Compilation Service] Creata a:" + directory.getAbsolutePath());
            } else {
                throw new IOException("[Compilation Service] Impossibile creare la directory: " + path);
            }
        }else{
            throw new IOException("[Compilation Service] Errore esiste già la directory: " + path);
        }
    }

    private void copyPomFile() throws IOException {
        File pomFile     = new File(config.getUsrPath() + config.getsep() + "ClientProject" + config.getsep() + "pom.xml");
        File destPomFile = new File(config.getPathCompiler() + "pom.xml");
        File lockFile    = new File(config.getPathCompiler() + "pom.lock"); // File di lock
    
        // Controlla se il file pom.xml esiste prima di tentare di copiarlo
        if (!pomFile.exists()) {
            throw new IOException("[Compilation Service] Il file pom.xml non esiste: " + pomFile.getAbsolutePath());
        } else {
            //Col filechannel rende l'operazione atomica
            try (FileChannel lockChannel = FileChannel.open(lockFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                 FileChannel sourceChannel = FileChannel.open(pomFile.toPath(), StandardOpenOption.READ);
                 FileChannel destChannel = FileChannel.open(destPomFile.toPath(), StandardOpenOption.CREATE,
                     StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
    
                // Acquisisci un lock sul file di lock
                try (FileLock lock = lockChannel.lock()) {
                    // Acquisisci un lock sul file sorgente per evitare accessi concorrenti
                    try (FileLock sourceLock = sourceChannel.lock(0, Long.MAX_VALUE, true)) {
                        // Copia il contenuto
                        long size = sourceChannel.size();
                        long position = 0;
    
                        while (position < size) {
                            position += sourceChannel.transferTo(position, size - position, destChannel);
                        }
                    }
                }
    
                System.out.println("[Compilation Service] pom.xml copiato con successo in: " + destPomFile.getAbsolutePath());
            } catch (IOException e) {
                throw new IOException("[Compilation Service] Errore copia pom.xml: " + e.getMessage(), e);
            }
        }
    }
    

    private void saveCodeToFile(String nameclass, String code, String path) throws IOException {
        // Controlla che il nome della classe e il percorso siano validi
        if (nameclass == null || nameclass.isEmpty()) {
            throw new IllegalArgumentException("[Compilation Service] Il nome della classe non può essere nullo o vuoto.");
        }
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("[Compilation Service] Il percorso non può essere nullo o vuoto.");
        }
        // Aggiungi la dichiarazione del pacchetto al codice
        String packageDeclaration = config.getPackageDeclaration();
        code = packageDeclaration + code;
        //creo il file di destinazione 
        File tempFile = new File(path + nameclass);
        // Imposta il file per la cancellazione all'uscita
        tempFile.deleteOnExit();
        // Utilizza FileChannel per scrivere in modo atomico
        try (FileChannel channel = FileChannel.open(tempFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            // Acquisisci un lock sul file per evitare accessi concorrenti
            try (FileLock lock = channel.lock()) {
                // Scrivi il codice nel file
                ByteBuffer buffer = StandardCharsets.UTF_8.encode(code);
                channel.write(buffer);
                System.out.println("[Compilation Service] Codice salvato con successo in: " + tempFile.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new IOException("[Compilation Service] Errore durante la scrittura nel file: " + tempFile.getAbsolutePath(), e);
        }
    }

    private boolean compileExecuteCoverageWithMaven() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("mvn", "clean", "compile", "test");
        processBuilder.directory(new File(config.getPathCompiler()));
    
        Process process = processBuilder.start();
        
        // Utilizza StringBuilder per accumulare l'output
        StringBuilder output = new StringBuilder();
        int exitCode = readProcessOutput(process, output);
    
        // Aggiorna l'array ret con l'output del processo
        this.outputMaven += output.toString();
        
        return exitCode == 0;
    }
    
    private int readProcessOutput(Process process, StringBuilder output) throws IOException, InterruptedException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        
        // Attende la terminazione del processo e restituisce il codice di uscita
        return process.waitFor();
    }

    private void deleteFile(String underTestClassName, String testingClassName) throws IOException {
        File file1 = new File(config.getUnderTestClassPath() + underTestClassName);
        file1.delete();
        File file2 = new File(config.getTestingClassPath() + testingClassName);
        file2.delete();
    }

    private void deleteTemporaryDirectories() {
        try {
            FileUtils.deleteDirectory(new File(config.getPathCompiler()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFileToString(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        return new String(bytes);
    }
}