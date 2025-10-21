package com.groom.manvsclass.util.filesystem;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileOperationUtil {
    public static void zipDirectory(String sourceDirPath, String zipFilePath) {
        File sourceDir = new File(sourceDirPath);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            System.err.println("La directory specificata non esiste o non è una cartella valida.");
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            zipFile(sourceDir, sourceDir.getName(), zos);
            System.out.println("Compressione in formato zip completata: " + zipFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void zipFile(File fileToZip, String parentDir, ZipOutputStream zos) throws IOException {
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

    public static void unzip(String fileZip, File destDir) throws IOException {

        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(Files.newInputStream(Paths.get(fileZip)));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = new File(destDir, zipEntry.getName());
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // Aggiustamento per archivi creati con Windows
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // Scrittura del contenuto del file
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    public static void extractZipIn(Path path) throws IOException {
        Path zipFile = Objects.requireNonNull((new File(String.valueOf(path))).listFiles())[0].toPath();
        unzip(String.valueOf(zipFile), path.toFile());
        Files.delete(zipFile);
    }

    public static void writeStringToFile(String content, File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    public static void deleteDirectoryRecursively(Path dirPath) throws IOException {
        if (Files.isDirectory(dirPath)) {
            Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc != null) {
                        throw exc; // Solleva l'eccezione se la directory non può essere cancellata
                    }
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    public static void copyDirectoryRecursively(Path sourcePath, Path destinationPath) throws IOException {
        if (!Files.exists(sourcePath) || !Files.isDirectory(sourcePath)) {
            throw new IllegalArgumentException(String.format("Il percorso %s sorgente non esiste o non è una directory.", sourcePath));
        }

        Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
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

    public static void saveFileInFileSystem(String fileName, Path directory, MultipartFile file) throws IOException {
        try {
            // Verifica se la directory esiste già
            if (!Files.exists(directory)) {
                // Crea la directory
                Files.createDirectories(directory);
            }
        } catch (Exception e) {
            System.out.println("Errore durante la creazione della directory: " + e.getMessage());
        }
        // Legge l'input stream del file caricato e lo copia nella directory specificata
        try (InputStream inputStream = file.getInputStream()) {
            // Risolve il percorso completo del file all'interno della directory
            // specificata.
            // Viene utilizzato il metodo 'directory.resolve(fileNameClass)' per ottenere il
            // percorso completo
            // del file all'interno della directory 'directory'. Questo percorso completo
            // sarà utilizzato
            // successivamente per copiare il contenuto dell'input stream del file nella
            // posizione desiderata.
            Path filePath = directory.resolve(fileName);

            // copio il contenuto dell'input stream nel file di destinazione
            // l'ultimo parametro di questa funzione indica che se il file già esiste deve
            // essere sostituito
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}

