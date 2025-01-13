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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemService {

    private static final int TIMEOUTSECONDS = 30;

    private static final Logger logger = LoggerFactory.getLogger(FileSystemService.class);

    private static class LockWrapper {
        final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        final ReentrantLock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();
        final AtomicBoolean isReady = new AtomicBoolean(false);
    }

    private final Map<Path, LockWrapper> lockMap = new ConcurrentHashMap<>();

    private final ThreadLocal<Map<Path, byte[]>> threadLocalPath = ThreadLocal.withInitial(LinkedHashMap::new);

    // Absolute path
    @Value("${filesystem.rootPath}")
    private String rootFolder;

    // Absolute path
    @Value("${filesystem.classesPath}")
    private String classesFolder;

    @Value("${filesystem.sourceFolder}")
    private String sourceFolder;

    @Value("${filesystem.testsFolder}")
    private String testsFolder;

    public LockWrapper getOrCreateLock(Path path) {
        logger.info("[{}] Getting or creating lock for path {}", Thread.currentThread().getName(), path.toString());
        return lockMap.computeIfAbsent(path, p -> new LockWrapper());
    }

    public void notify(Path path) throws InterruptedException {
        LockWrapper lockWrapper = getOrCreateLock(path);

        if (lockWrapper == null) {
            throw new InterruptedException("LockWrapper doesn't exist");
        }

        lockWrapper.lock.lock();
        try {
            lockWrapper.isReady.set(true);
            lockWrapper.condition.signal();
        } finally {
            lockWrapper.lock.unlock();
        }
    }

    public boolean wait(Path path) throws InterruptedException {
        LockWrapper lockWrapper = getOrCreateLock(path);
        boolean signalled = false;

        lockWrapper.lock.lock();
        try {
            while (!lockWrapper.isReady.get()) {
                signalled = lockWrapper.condition.await(TIMEOUTSECONDS, TimeUnit.SECONDS);
            }
        } finally {
            lockWrapper.lock.unlock();
        }

        return signalled;
    }

    public void readLock(Path path) {
        logger.info("[{}] ReadLocking the path {}", Thread.currentThread().getName(), path.toString());
        getOrCreateLock(path).readWriteLock.readLock().lock();
    }

    public void readUnlock(Path path) {
        ReentrantReadWriteLock lock = getOrCreateLock(path).readWriteLock;
        if (lock != null) {
            lock.readLock().unlock();
            logger.info("[{}] ReadUnlocking the path {}", Thread.currentThread().getName(), path.toString());
            if (!lock.hasQueuedThreads()) {
                lockMap.remove(path, lock);
            }
        }
    }

    public void writeLock(Path path) {
        logger.info("[{}] WriteLocking the path {}", Thread.currentThread().getName(), path.toString());
        getOrCreateLock(path).readWriteLock.writeLock().lock();
    }

    public void writeUnlock(Path path) {
        ReentrantReadWriteLock lock = getOrCreateLock(path).readWriteLock;
        if (lock != null) {
            lock.writeLock().unlock();
            logger.info("[{}] WriteUnlocking the path {}", Thread.currentThread().getName(), path.toString());
            if (!lock.hasQueuedThreads()) {
                lockMap.remove(path, lock);
            }
        }
    }

    public Path saveClass(String className, MultipartFile classFile) throws FileSystemException {
        Path classPath = Paths.get(classesFolder).resolve(className);

        writeLock(classPath);
        try {
            Path path = createFolder(classPath);

            path = path.resolve(sourceFolder);
            createFolder(path);

            path = saveFile(classFile, path);

            logger.info("Class saved successfully: {}", className);
            return path;
        } catch (IOException e) {
            logger.error("Upload of class {} unsuccessful", className);
            deleteAll(className);
            throw new FileSystemException(classFile.getOriginalFilename());
        } finally {
            writeUnlock(classPath);
        }

    }

    public Path saveTest(String className, String robotName, MultipartFile testFile) throws FileSystemException {
        Path classPath = Paths.get(classesFolder).resolve(className);

        writeLock(classPath);
        try {
            logger.debug("Saving test of Robot: {}", robotName);

            Path path = classPath.resolve(testsFolder).resolve(robotName);
            createFolder(path);

            unzip(testFile, path);

            logger.info("Test saved successfully: {} | {}", className, robotName);
            return path;
        } catch (IOException e) {
            logger.error("Error occurred while saving: {} | {}", className, robotName);
            deleteAll(className);
            throw new FileSystemException(testFile.getOriginalFilename());
        } finally {
            writeUnlock(classPath);
        }
    }

    public Path deleteAll(String className) throws FileSystemException {

        Path path = deleteDirectory(Paths.get(classesFolder).resolve(className));

        logger.info("Class and its tests deleted successfully: {}", className);

        return path;
    }

    public Path deleteClass(String className) throws FileSystemException {

        Path path = deleteDirectory(Paths.get(classesFolder + className).resolve(sourceFolder));

        logger.info("SourceCode deleted successfully: {}", className);

        return path;
    }

    public Path deleteTest(String className, String robotName) throws FileSystemException {

        Path path = deleteDirectory(Paths.get(classesFolder + className).resolve(testsFolder + robotName));

        logger.info("Test deleted successfully: {} | {}", className, robotName);

        return path;
    }

    private Path rawCreateFolder(Path path) throws FileSystemException {

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            // Catching exception and rolling back
            logger.warn("Error in creating {}", path.toString());
            throw new FileSystemException(path.toString());
        }

        logger.info("Folder created: {}", path.toString());

        return path;
    }

    public Path createFolder(String path) throws FileSystemException {
        return createFolder(Paths.get(path));
    }

    public Path createFolder(Path path) throws FileSystemException {

        writeLock(path);
        try {
            return rawCreateFolder(path);
        } catch (IOException exception) {
            // Catching exception and rolling back
            deleteDirectory(path);
            throw exception;
        } finally {
            writeUnlock(path);
        }
    }

    private Path rawSaveFile(MultipartFile file, Path path) throws FileSystemException {
        Path filePath = path.resolve(file.getOriginalFilename());

        try {
            file.transferTo(filePath);
        } catch (IOException e) {
            logger.warn("Error during transfer of file {}", file.getOriginalFilename());
            throw new FileSystemException(file.getOriginalFilename());
        }

        logger.info("File saved: {}", filePath.toString());

        return filePath;
    }

    public Path saveFile(MultipartFile file, String path) throws FileSystemException {
        return saveFile(file, Paths.get(path));
    }

    public Path saveFile(MultipartFile file, Path path) throws FileSystemException {
        Path filePath = path.resolve(file.getOriginalFilename());

        writeLock(path);
        writeLock(filePath);
        try {
            return rawSaveFile(file, path);
        } catch (IOException e) {
            logger.warn("Error during transfer of file {}", file.getOriginalFilename());
            deleteDirectory(filePath);
            throw new FileSystemException(file.getOriginalFilename());
        } finally {
            writeUnlock(filePath);
            writeUnlock(path);
        }
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
                    throw new FileSystemException(extractedPath.toString(), null,
                            "Tentativo di estrarre file fuori dal percorso consentito: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    // If directory, create it
                    createFolder(extractedPath);
                } else {
                    // If not save file
                    createFolder(extractedPath.getParent());

                    writeLock(extractedPath);
                    try (OutputStream os = Files.newOutputStream(extractedPath)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = zis.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                    } finally {
                        writeUnlock(extractedPath);
                    }
                }

                zis.closeEntry();
            }
        } catch (IOException e) {
            logger.warn("Error during the unzip of {}", zipFile.toString());
            throw new FileSystemException(zipFile.toString());
        }

        logger.info("ZIP file extracted successfully.");
        return unzipPath;
    }

    public Path deleteDirectory(Path path) throws FileSystemException {
        Map<Path, byte[]> localFiles = threadLocalPath.get();

        // ! CHECK: Directory existance
        if (!Files.exists(path)) {
            logger.warn("The directory {} doesn't exist", path.toString());
            throw new FileSystemException(path.toString());
        }

        // Usa un FileVisitor per attraversare ricorsivamente la directory
        writeLock(path);
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                // Action done when visiting a file
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                    writeLock(file);
                    try {
                        logger.debug("Backup of file: {}", file.toString());
                        localFiles.put(file, Files.readAllBytes(file));
                        logger.debug("Deleting: {}", file.toString());
                        Files.delete(file);
                    } finally {
                        writeUnlock(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                // Action done after visiting a directory
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exception) throws IOException {
                    writeLock(dir);
                    try {
                        logger.debug("Backup of folder: {}", dir);
                        localFiles.put(dir, null);
                        logger.debug("Deleting: {}", dir.toString());
                        Files.delete(dir);
                    } finally {
                        writeUnlock(dir);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            logger.warn("Deletion of {} gone wrong", path.toString());

            localFiles.forEach((key, value) -> {
                deleteRollback(key, value);
            });

            throw new FileSystemException(path.toString());
        } finally {
            writeUnlock(path);
            localFiles.clear();
        }

        logger.info("Deletion completed successfully: {}", path.toString());

        return path;
    }

    private void deleteRollback(Path path, byte[] file) {
        try {
            if (file == null) {
                createFolder(path);
            } else {
                Files.write(path, file);
            }
        } catch (IOException e) {
            logger.error("Rollback not working: {}", path.toString());
            return;
        }
    }

    public boolean existsPath(Path path) {
        return Files.exists(path);
    }

    public boolean validatePath(Path path) {
        return path.startsWith(rootFolder)
                && !(path.equals(Paths.get("/Volume")) || path.equals(Paths.get(classesFolder))
                        || path.equals(Paths.get(rootFolder)));
    }
}
