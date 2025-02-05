package genclass.gc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.io.OutputStreamWriter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utils {

	
	 
	 public static void createTargetDirectory(String targetDirPath) throws IOException {
	     Path targetDir = Paths.get(targetDirPath);
	     if (!Files.exists(targetDir)) {
	         Files.createDirectory(targetDir);
	     }
	 }
	 



	 public static void moveFilesToTargetDirectory(String sourceDirPath, String targetDirPath,String startWord) throws IOException {
		 /**Cerca i file in sourceDirPath che inziano con startWord e li sposta in targetDirPath */
		 
	     Path sourceDir = Paths.get(sourceDirPath);
	     Path targetDir = Paths.get(targetDirPath);
	     Files.walk(sourceDir)
	         .filter(path -> !Files.isDirectory(path) && path.getFileName().toString().startsWith(startWord))
	         .forEach(path -> {
	             try {
	                 Files.move(path, targetDir.resolve(path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
	             } catch (IOException e) {
	                 System.err.println("Failed to move file: " + path);
	                 e.printStackTrace();
	             }
	         });
	 }

	 public static void zipFiles(String sourceDirPath, String zipFilePath,String startFolder ) throws IOException {
	 /**Zippa i file in sourceDirPath mantendo la struttura e salva in zipFilePath con prima cartella startFolder*/
		 Path zipFile = Files.createFile(Paths.get(zipFilePath));
	     try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(zipFile))) {
	         Path sourceDir = Paths.get(sourceDirPath);
	         Files.walk(sourceDir)
	             .filter(path -> !Files.isDirectory(path))
	             .forEach(path -> {
	                 String zipEntryName = startFolder + sourceDir.relativize(path).toString();
	                 ZipEntry zipEntry = new ZipEntry(zipEntryName);
	                 try {
	                     zipOut.putNextEntry(zipEntry);
	                     Files.copy(path, zipOut);
	                     zipOut.closeEntry();
	                 } catch (IOException e) {
	                     System.err.println("Failed to zip file: " + path);
	                     e.printStackTrace();
	                 }
	             });
	     }
	 }
		 
	     
	 

	 
	 
	 public static void renameDirectory(String targetDirPath, String renamedDirPath) throws IOException {
	     Path targetDir = Paths.get(targetDirPath);
	     Path renamedDir = Paths.get(renamedDirPath);
	     Files.move(targetDir, renamedDir, StandardCopyOption.REPLACE_EXISTING);
	 }
	 
	 
	 public static boolean deleteDirectory(File directoryToBeDeleted) {
	     File[] allContents = directoryToBeDeleted.listFiles();
	     if (allContents != null) {
	         for (File file : allContents) {
	             deleteDirectory(file);
	         }
	     }
	     return directoryToBeDeleted.delete();
	 }
	
	
	 
	    public  static String extractClassName(String code) {
	    	/**Estrae nome data la classe*/
	        String[] lines = code.split("\\n");
	        for (String line : lines) {
	            line = line.trim();
	            if (line.startsWith("public class ")) {
	                String[] parts = line.split(" ");
	                return parts[2];
	            }
	        }
	        return null; // Se non trova una definizione di classe
	    }

	    public  static File createBashTempScript(String command) throws IOException {
	        File tempScript = File.createTempFile("script", null);

	        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
	                tempScript));
	        PrintWriter printWriter = new PrintWriter(streamWriter);

	        printWriter.println("#!/bin/bash");
	        printWriter.println(command);

	        printWriter.close();

	        return tempScript;
	    }	    
	    
}
