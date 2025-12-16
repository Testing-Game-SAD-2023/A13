package com.groom.manvsclass.service.upload;

import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.repository.ClassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;

import static com.groom.manvsclass.util.upload.OpponentPathResolver.*;

@Service
public class ClassUTUploadService {

    private static final Logger logger = LoggerFactory.getLogger(ClassUTUploadService.class);

    private final ClassRepository classRepository;
    private final FileStorageService fileStorageService;

    public ClassUTUploadService(ClassRepository classRepository, FileStorageService fileStorageService) {
        this.classRepository = classRepository;
        this.fileStorageService = fileStorageService;
    }

    public void saveClassUTFile(String classUTFileName, String classUTName, MultipartFile classUTFile) throws IOException {
        logger.info("Salvataggio di {} nel filesystem condiviso", classUTFileName);
        
        Path unmodifiedSrcCodePath = getUnmodifiedSrcPath(classUTName);
        fileStorageService.saveFileInFileSystem(classUTFileName, unmodifiedSrcCodePath, classUTFile);
    }

    public void persistClassUTMetadata(ClassUT classe, String classUTFileName) {
        String uri = String.format("%s/%s/%s/%s",
                VOLUME_T0_BASE_PATH,
                UNMODIFIED_SRC,
                classe.getName(),
                classUTFileName);
        
        classe.setUri(uri);
        classe.setDate(LocalDate.now().toString());
        classRepository.save(classe);
        
        logger.info("ClassUT metadata persisted successfully: {}", classe.getName());
    }

    /**
     * Rollback class file upload by deleting the saved class file from filesystem.
     * Used when upload operation fails and needs cleanup.
     * Silently handles cases where files don't exist.
     */
    public void rollbackClassUTFile(String classUTName) {
        try {
            Path unmodifiedSrcCodePath = getUnmodifiedSrcPath(classUTName);
            if (unmodifiedSrcCodePath.toFile().exists()) {
                fileStorageService.deleteDirectoryRecursively(unmodifiedSrcCodePath);
                logger.info("Rollback: deleted class file directory for {}", classUTName);
            } else {
                logger.debug("Rollback: class file directory does not exist for {}", classUTName);
            }
        } catch (Exception e) {
            logger.warn("Error during rollback of class file for {}: {}", classUTName, e.getMessage());
        }
    }

    /**
     * Rollback class metadata by deleting the ClassUT entry from database.
     * Used when upload operation fails and needs cleanup.
     * Silently handles cases where entry doesn't exist.
     */
    public void rollbackClassUTMetadata(String classUTName) {
        try {
            classRepository.deleteByName(classUTName);
            logger.info("Rollback: deleted ClassUT metadata for {}", classUTName);
        } catch (Exception e) {
            logger.warn("Error during rollback of ClassUT metadata for {}: {}", classUTName, e.getMessage());
        }
    }
}
