package com.groom.manvsclass.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.SimpleFileVisitor;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


@Service
public class FileStorageService {

    private static final String JACOCO_COVERAGE_FILE = "coveragetot.xml";
    private static final String EVOSUITE_COVERAGE_FILE = "statistics.csv";


    public void saveFileInFileSystem(String fileName, Path directory, MultipartFile file) throws IOException {
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = directory.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }


    public void extractZipIn(Path folder) throws IOException {
        Path zipFile = Objects.requireNonNull((new File(String.valueOf(folder))).listFiles())[0].toPath();
        unzip(String.valueOf(zipFile), folder.toFile());
        Files.delete(zipFile);
    }


    public void deleteDirectoryRecursively(Path dirPath) throws IOException {
        if (Files.isDirectory(dirPath)) {
            Files.walkFileTree(dirPath, new java.nio.file.SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc != null) {
                        throw exc;
                    }
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }


    public void copyDirectoryRecursively(Path sourcePath, Path destinationPath) throws IOException {
        if (!Files.exists(sourcePath) || !Files.isDirectory(sourcePath)) {
            throw new IllegalArgumentException(String.format("Il percorso %s sorgente non esiste o non è una directory.", sourcePath));
        }
        Files.walkFileTree(sourcePath, new java.nio.file.SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = destinationPath.resolve(sourcePath.relativize(dir));
                Files.createDirectories(targetDir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path targetFile = destinationPath.resolve(sourcePath.relativize(file));
                Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }


    public void zipDirectory(String sourceDirPath, String zipFilePath) throws IOException {
        File sourceDir = new File(sourceDirPath);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new IOException("La directory specificata non esiste o non è una cartella valida.");
        }
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            zipFile(sourceDir, sourceDir.getName(), zos);
        }
    }

    private void zipFile(File fileToZip, String parentDir, ZipOutputStream zos) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            File[] children = fileToZip.listFiles();
            if (children != null) {
                for (File childFile : children) {
                    zipFile(childFile, parentDir + File.separator + childFile.getName(), zos);
                }
            }
            return;
        }
        try (FileInputStream fis = new FileInputStream(fileToZip)) {
            ZipEntry zipEntry = new ZipEntry(parentDir);
            zos.putNextEntry(zipEntry);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }

    private void unzip(String fileZip, File destDir) throws IOException {
        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(Paths.get(fileZip)))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(destDir, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
    }


    public void writeStringToFile(String content, File file) throws IOException {
        File parent = file.getParentFile();
        if (parent != null) {
            Files.createDirectories(parent.toPath());
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    public String[] extractTestPackageNameFromCode(String code) {
        Pattern pattern = Pattern.compile("\\bpackage\\s*([\\w_][\\w0-9_]*(\\.[\\w_][\\w0-9_]*)*)\\s*;", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(code);

        if (matcher.find()) {
            String packageName = matcher.group(1).trim();
            return packageName.split("\\.");
        }

        return null;
    }

    public String[] extractSrcPackageFromCode(String code, String className, String robotType) {
        Pattern pattern;
        Matcher matcher;

        if ("Evosuite".equalsIgnoreCase(robotType)) {
            String regex = "org\\.evosuite\\.runtime\\.RuntimeSettings\\.className\\s*=\\s*\"([\\w.]+)\\." + className + "\"";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(code);

            if (matcher.find()) {
                String packageName = matcher.group(1);
                return packageName.split("\\.");
            }

            return null;
        } else {
            pattern = Pattern.compile("\\bimport\\s+([\\w_][\\w0-9_]*(\\.[\\w_][\\w0-9_]*)*)\\." + className + "\\s*;");
            matcher = pattern.matcher(code);

            if (matcher.find()) {
                String packageName = matcher.group(1);
                return packageName.split("\\.");
            }

            return null;
        }
    }

    public String[][] saveTestFilesInVolume(Path fromTestPath, Path toTestPath, String className, String robotType) throws IOException {
        String[] testPackageName = null;
        String[] srcPackageName = null;

        File[] files = Objects.requireNonNull(fromTestPath.toFile().listFiles());
        for (File src : files) {
            if (!src.getName().contains(".java"))
                continue;

            String content = Files.readString(src.toPath());

            testPackageName = extractTestPackageNameFromCode(content);
            if (srcPackageName == null) {
                srcPackageName = extractSrcPackageFromCode(content, className, robotType);
            }

            String testPackagePath = "";
            if (testPackageName != null) {
                testPackagePath = String.join("/", testPackageName);
            }

            Path targetDir = toTestPath.resolve(testPackagePath).normalize();
            Files.createDirectories(targetDir);
            Files.copy(src.toPath(), targetDir.resolve(src.getName()).normalize(), StandardCopyOption.REPLACE_EXISTING);
        }

        return new String[][]{srcPackageName, testPackageName};
    }

    public void modifyAndSaveSrcFile(String fileName, Path directory, MultipartFile originalFile, String edit) throws IOException {
        String content = new String(originalFile.getBytes());
        String modifiedContent = "package " + edit + ";\n" + content;
        Path filePath = directory.resolve(fileName);
        File outputFile = filePath.toFile();
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(modifiedContent);
        }
    }

    public void saveSrcFileInVolume(MultipartFile src, Path srcPath, String[] srcPackageName, String srcFileName) throws IOException {
        String srcPackagePath = "";

        if (srcPackageName != null) {
            srcPackagePath = String.join("/", srcPackageName);
            String srcPackageCodeLine = String.join(".", srcPackagePath);
            Files.createDirectories(Paths.get(String.format("%s/%s", srcPath, srcPackagePath)));
            modifyAndSaveSrcFile(srcFileName, Paths.get(String.format("%s/%s", srcPath, srcPackagePath)), src, srcPackageCodeLine);
        } else {
            Files.createDirectories(Paths.get(String.format("%s/%s", srcPath, srcPackagePath)));
            saveFileInFileSystem(srcFileName, Paths.get(String.format("%s/%s", srcPath, srcPackagePath)), src);
        }
    }

    public boolean[] saveCoverageFilesInVolume(Path searchIn, Path coveragePath) throws IOException {
        boolean jacocoFound = false;
        boolean evosuiteFound = false;

        if (!Files.exists(searchIn)) {
            return new boolean[]{false, false};
        }

        for (File coverageFile : Objects.requireNonNull(searchIn.toFile().listFiles())) {
            Files.createDirectories(Paths.get(String.format("%s", coveragePath)));

            if (coverageFile.getName().equals(JACOCO_COVERAGE_FILE)) {
                String coverage = Files.readString(coverageFile.toPath());
                if (coverage.contains("<coverage type=\"line, %\" value=")) {
                    continue;
                }

                Files.copy(coverageFile.toPath(), Paths.get(String.format("%s/%s", coveragePath, coverageFile.getName())), StandardCopyOption.REPLACE_EXISTING);
                jacocoFound = true;
            }

            if (coverageFile.getName().equals(EVOSUITE_COVERAGE_FILE)) {
                Files.copy(coverageFile.toPath(), Paths.get(String.format("%s/%s", coveragePath, coverageFile.getName())), StandardCopyOption.REPLACE_EXISTING);
                evosuiteFound = true;
            }
        }

        return new boolean[]{jacocoFound, evosuiteFound};
    }
}
