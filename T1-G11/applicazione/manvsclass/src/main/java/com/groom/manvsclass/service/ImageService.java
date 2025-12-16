package com.groom.manvsclass.service;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.groom.manvsclass.exception.InvalidImageException;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {

    private final Path imageStorageLocation;

    private static final List<String> allowedExtensions = List.of(
            "image/png", "image/jpeg", "image/jpg", "image/xbm", "image/tif", "image/jfif",
            "image/ico", "image/gif", "image/svg", "image/svgz", "image/webp",
            "image/bmp", "image/pjp", "image/apng", "image/pjpeg", "image/avif"
    );

    private static final Tika apacheTika = new Tika();

    public ImageService(@Value("${images.storage-path}") String storagePath) {

        this.imageStorageLocation = Paths.get(storagePath).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.imageStorageLocation);
            } catch (Exception ex) {
                throw new RuntimeException("Impossibile creare la directory dove archiviare i file.", ex);
        }
    }

    public void storeImage(MultipartFile image, String imageName) throws IOException {

        if (image == null || image.isEmpty()) {
            throw new InvalidImageException("File immagine mancante o vuoto");
        }

        String detectedType;
        try (InputStream stream = image.getInputStream()) {
            detectedType = apacheTika.detect(stream);
        }

        if (!allowedExtensions.contains(detectedType)) {
            throw new InvalidImageException("Tipo file non supportato: " + detectedType);
        }

        Path targetLocation = this.imageStorageLocation.resolve(imageName);

        Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    }

    public void deleteImage(String imageName) throws IOException {

        Path targetLocation = this.imageStorageLocation.resolve(imageName);

        Files.deleteIfExists(targetLocation);
    }

}