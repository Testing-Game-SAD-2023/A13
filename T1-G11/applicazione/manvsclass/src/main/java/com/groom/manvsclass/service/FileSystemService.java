package com.groom.manvsclass.service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemService {

    private static final Logger logger = LoggerFactory.getLogger(FileSystemService.class);

    @Value("${filesystem.classesPath}")
    private String classesPath;

    @Value("${filesystem.sourceFolder}")
    private String sourceFolder;

    @Value("${filesystem.testsFolder}")
    private String testsFolder;

    /**
     * 
     * @param className name of the class to save
     * @param classFile class.java file to save
     * @return the path to classFile.
     * @throws IOException
     */
    public Path saveClass(String className, MultipartFile classFile) throws IOException {

        Path path = createFolder(classesPath + className);

        path = path.resolve(sourceFolder);
        createFolder(path);

        path = saveFile(classFile, path);

        logger.info("Class saved successfully: {}", className);

        return path;
    }

    /**
     * 
     * @param className name of the class in which the test is being added
     * @param robotName name of the generator of the test
     * @param testFile  test.zip file to save
     * @return the path to the robotName folder.
     * @throws IOException
     */
    public Path saveTest(String className, String robotName, MultipartFile testFile) throws IOException {

        logger.debug("Saving test of Robot: {}", robotName);

        Path path = Paths.get(classesPath + className).resolve(testsFolder + robotName);
        createFolder(path);

        unzip(testFile, path);

        logger.info("Test saved successfully: {} | {}", className, robotName);

        return path;
    }

    /**
     * 
     * @param className name of the class to delete
     * @return path of the deleted folder
     * @throws IOException
     */
    public Path deleteAll(String className) throws IOException {

        Path path = deleteDirectory(Paths.get(classesPath + className));

        logger.info("Class and its tests deleted successfully: {}", className);

        return path;
    }

    /**
     * 
     * @param className name of the class' SourceCode to delete
     * @return path of the deleted folder
     * @throws IOException
     */
    public Path deleteClass(String className) throws IOException {

        Path path = deleteDirectory(Paths.get(classesPath + className).resolve(sourceFolder));

        logger.info("SourceCode deleted successfully: {}", className);

        return path;
    }

    /**
     * 
     * @param className name of the class' test to remove
     * @param robotName name of the robot's test to remove
     * @return path of the deleted folder
     * @throws IOException
     */
    public Path deleteTest(String className, String robotName) throws IOException {

        Path path = deleteDirectory(Paths.get(classesPath + className).resolve(testsFolder + robotName));

        logger.info("Test deleted successfully: {} | {}", className, robotName);

        return path;
    }

    /**
     * 
     * @param path is a String which contains the path to the folder to create
     * @return the Path of the created folder
     * @throws IOException
     */
    public static Path createFolder(String path) throws IOException {

        Path target = Paths.get(path);
        target = Files.createDirectories(target);

        logger.info("Folder created: {}", target.toString());

        return target;
    }

    /**
     * 
     * @param path is the Path where the folder is created
     * @return the Path of the created folder
     * @throws IOException
     */
    public static Path createFolder(Path path) throws IOException {

        path = Files.createDirectories(path);

        logger.info("Folder created: {}", path.toString());

        return path;
    }

    /**
     * 
     * @param file is the file to be saved
     * @param path is the destination folder of the file
     * @return the path to the file.
     * @throws IOException
     */
    public static Path saveFile(MultipartFile file, Path path) throws IOException {

        path = path.resolve(file.getOriginalFilename());
        file.transferTo(path);

        logger.info("File saved: {}", path.toString());

        return path;
    }

    /**
     * 
     * @param zipFile   is the file to unzip
     * @param unzipPath is the destination folder of unzip operation
     * @return the destination folder of the unzip operation.
     * @throws IOException
     */
    public static Path unzip(MultipartFile zipFile, Path unzipPath) throws IOException {
        // Verifies the existance of the path, creates it if nonexistent
        // ? Should've created them before. Just do a check or throw an error?
        if (Files.notExists(unzipPath)) {
            Files.createDirectories(unzipPath);
        }

        logger.debug("Beginning extraction of: {}", unzipPath.toString());

        // Creates ZipInputStream from the zipFile
        try (ZipInputStream zis = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry entry;

            // Iterates on the content of the zipFile
            while ((entry = zis.getNextEntry()) != null) {

                // Creates path of extractedFile
                Path extractedPath = unzipPath.resolve(entry.getName()).normalize();

                logger.debug("Extracting: {}", extractedPath.toString());

                // Checks for PathTraversal attacks
                if (!extractedPath.startsWith(unzipPath)) {
                    throw new IOException(
                            "Tentativo di estrarre file fuori dal percorso consentito: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    // If directory, create it
                    Files.createDirectories(extractedPath);
                } else {
                    // If not save file
                    Files.createDirectories(extractedPath.getParent());

                    try (OutputStream os = Files.newOutputStream(extractedPath)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = zis.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                    }
                }

                zis.closeEntry();
            }
        }

        logger.info("ZIP file extracted successfully.");
        return unzipPath;
    }

    /**
     * 
     * @param directory to delete
     * @return the deleted directory Path.
     * @throws IOException
     */
    public static Path deleteDirectory(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            logger.warn("The directory {} doesn't exist", directory.toString());
            throw new IOException("La directory non esiste.");
        }

        // Usa un FileVisitor per attraversare ricorsivamente la directory
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

            // Action done when visiting a file
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                logger.debug("Deleting: {}", file.toString());
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            // Action done after visiting a directory
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exception) throws IOException {
                logger.debug("Deleting: {}", dir.toString());
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }

        });
        logger.info("Deletion completed successfully: {}", directory.toString());

        return directory;
    }
}
