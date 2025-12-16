package com.groom.manvsclass.service.upload;

import com.groom.manvsclass.util.upload.JavaMetadataExtractor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * Service for handling Java source file operations including test and source files
 * with package management and transformations.
 */
@Service
public class JavaSourceFileService {

    private static final String JACOCO_COVERAGE_FILE = "coveragetot.xml";
    private static final String EVOSUITE_COVERAGE_FILE = "statistics.csv";

    private final FileStorageService fileStorageService;

    public JavaSourceFileService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    /**
     * Saves test files from source path to destination, extracting and preserving package structure.
     * Returns both source and test package names extracted from the test files.
     *
     * @param fromTestPath source path containing test files
     * @param toTestPath destination path for test files
     * @param className name of the class under test
     * @param robotType type of test robot (e.g., "Evosuite", "Randoop")
     * @return two-dimensional array: [0] = source package, [1] = test package
     */
    public String[][] saveTestFilesInVolume(Path fromTestPath, Path toTestPath, String className, String robotType) throws IOException {
        String[] testPackageName = null;
        String[] srcPackageName = null;

        File[] files = fromTestPath.toFile().listFiles();
        if (files == null || files.length == 0) {
            throw new IOException(
                "La cartella dei test '" + fromTestPath.getFileName() + "' è vuota o non accessibile. " +
                "Verificare che il livello di test contenga file Java validi."
            );
        }
        for (File src : files) {
            if (!src.getName().endsWith(".java")) {
                continue;
            }

            try {
                String content = Files.readString(src.toPath());

                testPackageName = JavaMetadataExtractor.extractPackageFromDeclaration(content);
                if (srcPackageName == null) {
                    srcPackageName = JavaMetadataExtractor.extractSourcePackageFromTestCode(content, className, robotType);
                }

                String testPackagePath = (testPackageName != null) ? String.join("/", testPackageName) : "";

                Path targetDir = toTestPath.resolve(testPackagePath).normalize();
                Files.createDirectories(targetDir);
                Files.copy(src.toPath(), targetDir.resolve(src.getName()).normalize(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException(
                    "Errore durante l'elaborazione del file di test '" + src.getName() + "': " + e.getMessage() + 
                    ". Verificare che il file sia un file Java valido e leggibile.", e
                );
            }
        }

        return new String[][]{srcPackageName, testPackageName};
    }

    /**
     * Saves source file with optional package declaration modification.
     * If package name is provided, adds package declaration at the beginning of the file.
     *
     * @param src source file to save
     * @param srcPath destination path for source file
     * @param srcPackageName package name components (e.g., ["com", "example", "test"])
     * @param srcFileName name of the source file
     */
    public void saveSrcFileInVolume(MultipartFile src, Path srcPath, String[] srcPackageName, String srcFileName) throws IOException {
        String srcPackagePath = "";

        if (srcPackageName != null) {
            srcPackagePath = String.join("/", srcPackageName);
            String srcPackageCodeLine = String.join(".", srcPackageName);
            Path fullPath = Paths.get(srcPath.toString(), srcPackagePath);
            Files.createDirectories(fullPath);
            modifyAndSaveSrcFile(srcFileName, fullPath, src, srcPackageCodeLine);
        } else {
            Path fullPath = Paths.get(srcPath.toString(), srcPackagePath);
            Files.createDirectories(fullPath);
            fileStorageService.saveFileInFileSystem(srcFileName, fullPath, src);
        }
    }

    /**
     * Saves coverage files (Jacoco and Evosuite) from source to destination.
     * Returns flags indicating which coverage files were found and copied.
     *
     * @param searchIn source path to search for coverage files
     * @param coveragePath destination path for coverage files
     * @return boolean array: [0] = Jacoco found, [1] = Evosuite found
     */
    public boolean[] saveCoverageFilesInVolume(Path searchIn, Path coveragePath) throws IOException {
        boolean jacocoFound = false;
        boolean evosuiteFound = false;

        if (!Files.exists(searchIn)) {
            return new boolean[]{false, false};
        }

        Files.createDirectories(coveragePath);

        File[] coverageFiles = searchIn.toFile().listFiles();
        if (coverageFiles == null) {
            return new boolean[]{false, false};
        }
        
        for (File coverageFile : coverageFiles) {
            String fileName = coverageFile.getName();
            
            if (fileName.equals(JACOCO_COVERAGE_FILE) && isValidJacocoCoverage(coverageFile)) {
                Files.copy(coverageFile.toPath(), 
                         coveragePath.resolve(fileName), 
                         StandardCopyOption.REPLACE_EXISTING);
                jacocoFound = true;
            } else if (fileName.equals(EVOSUITE_COVERAGE_FILE)) {
                Files.copy(coverageFile.toPath(), 
                         coveragePath.resolve(fileName), 
                         StandardCopyOption.REPLACE_EXISTING);
                evosuiteFound = true;
            }
        }

        return new boolean[]{jacocoFound, evosuiteFound};
    }

    /**
     * Validates Jacoco coverage file by checking for invalid content.
     * Files with "<coverage type="line, %"" are considered invalid.
     */
    private boolean isValidJacocoCoverage(File coverageFile) throws IOException {
        String coverage = Files.readString(coverageFile.toPath());
        return !coverage.contains("<coverage type=\"line, %\" value=");
    }

    /**
     * Modifies source file by adding package declaration and saves it.
     *
     * @param fileName name of the file
     * @param directory destination directory
     * @param originalFile original source file
     * @param packageDeclaration package name to add (e.g., "com.example.test")
     */
    private void modifyAndSaveSrcFile(String fileName, Path directory, MultipartFile originalFile, String packageDeclaration) throws IOException {
        String content = new String(originalFile.getBytes());
        String modifiedContent = "package " + packageDeclaration + ";\n" + content;
        Path filePath = directory.resolve(fileName);
        File outputFile = filePath.toFile();
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(modifiedContent);
        }
    }
}
