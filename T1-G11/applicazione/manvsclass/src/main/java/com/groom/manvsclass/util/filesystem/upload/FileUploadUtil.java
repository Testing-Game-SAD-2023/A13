package com.groom.manvsclass.util.filesystem.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    /**
     * @param fileName  nome del file
     * @param className nome della classe
     * @param classFile file inviato come parte della richiesta multipart
     *                  L'interfaccia MultipartFile fornisce i metodi per
     *                  accedere ai dati del file che nel nostro caso sono i
     *                  parametri descrittivi della classe ovvero difficoltà,
     *                  data di caricamento ecc
     * @throws IOException
     */
    public static void saveCLassFile(String fileName, String className, MultipartFile classFile) throws IOException {

        // Percorso della directory dove verranno salvati i file
        String directoryPath = "Files-Upload/" + className;
        // Converto la directory espressa come stringa in un oggetto di tipo Path
        Path directory = Paths.get(directoryPath);
        System.out.println("creazione directory:" + directory);
        try {
            // Verifica se la directory esiste già
            if (!Files.exists(directory)) {
                // Crea la directory
                Files.createDirectories(directory);
                System.out.println("La directory:" + directory + "è stata creata con successo.");
            } else {
                System.out.println("La directory:" + directory + " esiste già.");
            }
        } catch (Exception e) {
            System.out.println("Errore durante la creazione della directory: " + directory + e.getMessage());
        }

        // Percorso completo della directory di upload
        Path uploadDirectory = Paths.get("Files-Upload/" + className);


        try (InputStream inputStream = classFile.getInputStream()) {
            // Risolve il percorso del file all'interno della directory
            // ovvero viene creato un oggetto Path che rappresenta il percorso completo del
            // file all'interno della directory di destinazione
            // Il metodo resolve è utilizzato per ottenere il percorso completo concatenando
            // il percorso della directory (uploadDirectory) con il nome del file
            // (fileName).
            Path filePath = uploadDirectory.resolve(fileName);

            // Copia il file nell'uploadDirectory con opzione di sovrascrittura se esiste già
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore durante il salvataggio del file in Files-Upload:" + e.getMessage());
        }
    }

    public static void deleteDirectory(File directory) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    if (!file.delete()) {
                        throw new IOException("Impossibile eliminare il file: " + file.getAbsolutePath());
                    }
                }
            }
        } else {
            directory.delete();

        }

        if (!directory.delete()) {
            throw new IOException("Impossibile eliminare la cartella: " + directory.getAbsolutePath());
        }
    }

}

