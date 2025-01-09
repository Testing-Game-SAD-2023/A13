package com.groom.manvsclass.service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystemException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
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

    private final ConcurrentHashMap<Path, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    // Absolute path
    @Value("${filesystem.classesPath}")
    private String classesPath;

    @Value("${filesystem.sourceFolder}")
    private String sourceFolder;

    @Value("${filesystem.testsFolder}")
    private String testsFolder;

    private void lockPath(Path path) {
        lockMap.computeIfAbsent(path, p -> new ReentrantLock()).lock();
    }

    private void unlockPath(Path path) {
        ReentrantLock lock = lockMap.get(path);
        if(lock != null) {
            lock.unlock();
            if(!lock.hasQueuedThreads()) {
                lockMap.remove(path, lock);
            }
        }
    }

    public Path saveClass(String className, MultipartFile classFile) throws FileSystemException {

        Path path = createFolder(classesPath + className);

        path = path.resolve(sourceFolder);
        createFolder(path);

        path = saveFile(classFile, path);

        logger.info("Class saved successfully: {}", className);

        return path;
    }

    public Path saveTest(String className, String robotName, MultipartFile testFile) throws FileSystemException {

        logger.debug("Saving test of Robot: {}", robotName);

        Path path = Paths.get(classesPath + className).resolve(testsFolder + robotName);
        createFolder(path);

        unzip(testFile, path);

        logger.info("Test saved successfully: {} | {}", className, robotName);

        return path;
    }

    public Path deleteAll(String className) throws FileSystemException {

        Path path = deleteDirectory(Paths.get(classesPath + className));

        logger.info("Class and its tests deleted successfully: {}", className);

        return path;
    }

    public Path deleteClass(String className) throws FileSystemException {

        Path path = deleteDirectory(Paths.get(classesPath + className).resolve(sourceFolder));

        logger.info("SourceCode deleted successfully: {}", className);

        return path;
    }

    public Path deleteTest(String className, String robotName) throws FileSystemException {

        Path path = deleteDirectory(Paths.get(classesPath + className).resolve(testsFolder + robotName));

        logger.info("Test deleted successfully: {} | {}", className, robotName);

        return path;
    }

    public Path createFolder(String path) throws FileSystemException {

        Path target = Paths.get(path);
        target = createFolder(target);

        return target;
    }

    public Path createFolder(Path path) throws FileSystemException {

        lockPath(path);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            // Catching exception and rolling back
            logger.warn("Error in creating {}", path.toString());
            deleteDirectory(path);
            throw new FileSystemException(path.toString());
        } finally {
            unlockPath(path);
        }

        logger.info("Folder created: {}", path.toString());

        return path;
    }

    public Path saveFile(MultipartFile file, Path path) throws FileSystemException {
        Path filePath = path.resolve(file.getOriginalFilename());

        lockPath(path);
        lockPath(filePath);
        try {
            file.transferTo(filePath);
        } catch (IOException e) {
            logger.warn("Error during transfer of file {}", file.getOriginalFilename());
            deleteDirectory(filePath);
            throw new FileSystemException(file.getOriginalFilename());
        } finally {
            unlockPath(filePath);
            unlockPath(path);
        }

        logger.info("File saved: {}", path.toString());

        return filePath;
    }

    public Path unzip(MultipartFile zipFile, Path unzipPath) throws FileSystemException {
        // Verifies the existance of the path, creates it if nonexistent
        if (Files.notExists(unzipPath)) {
            createFolder(unzipPath);
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
                    throw new FileSystemException(extractedPath.toString(), null, "Tentativo di estrarre file fuori dal percorso consentito: " + entry.getName());
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
        } catch(IOException e) {
            logger.warn("Error during the unzip of {}", zipFile.toString());
            throw new FileSystemException(zipFile.toString());
        }

        logger.info("ZIP file extracted successfully.");
        return unzipPath;
    }

    public Path deleteDirectory(Path directory) throws FileSystemException {
        //! CHECK: Directory existance
        if (!Files.exists(directory)) {
            logger.warn("The directory {} doesn't exist", directory.toString());
            throw new FileSystemException(directory.toString());
        }

        // Usa un FileVisitor per attraversare ricorsivamente la directory
        lockPath(directory);
        try {
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
        } catch (IOException e) {
            logger.warn("Deletion of {} gone wrong", directory.toString());
            throw new FileSystemException(directory.toString());
        } finally {
            unlockPath(directory);
        }

        logger.info("Deletion completed successfully: {}", directory.toString());

        return directory;
    }
}
