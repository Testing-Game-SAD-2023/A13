package com.groom.manvsclass.model.filesystem.upload;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
public class FileUploadUtil {
	
	public static void saveCLassFile(String fileName,String cname,MultipartFile multipartFile) throws IOException {
		
		
		 String directoryPath = "Files-Upload/"+cname;
	        Path directory = Paths.get(directoryPath);
	        
	        try {
	            // Verifica se la directory esiste già
	            if (!Files.exists(directory)) {
	                // Crea la directory
	                Files.createDirectories(directory);
	                System.out.println("La directory è stata creata con successo.");
	            } else {
	                System.out.println("La directory esiste già.");
	            }
	        } catch (Exception e) {
	            System.out.println("Errore durante la creazione della directory: " + e.getMessage());
	        }
	    
		
		Path uploadDirectory = Paths.get("Files-Upload/"+cname);
		
		
	    
		try (InputStream inputStream = multipartFile.getInputStream()){
			Path filePath = uploadDirectory.resolve(fileName);
			
			
			Files.copy(inputStream,filePath,StandardCopyOption.REPLACE_EXISTING);
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
        }
        else {
        	directory.delete();
        }
        if (!directory.delete()) {
            throw new IOException("Impossibile eliminare la cartella: " + directory.getAbsolutePath());
        }
    }
	
}

