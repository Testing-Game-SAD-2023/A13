package com.groom.manvsclass.model.filesystem.download;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import java.nio.file.Path;
import java.nio.file.Paths;
public class FileDownloadUtil {
	
	public static ResponseEntity<Resource> downloadClassFile(String downloadpath) throws Exception {
     
		// Percorso del file Java da scaricare
		 Path path = Paths.get(downloadpath);
        // Rappresentazione del file come Resource
        Resource resource = new UrlResource(path.toUri());

        // Verifica che il file esista e sia leggibile
        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Impossibile accedere al file");
        }

        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ByteVector.java");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/x-java-source"))
                .body(resource);
    }
	
	
}