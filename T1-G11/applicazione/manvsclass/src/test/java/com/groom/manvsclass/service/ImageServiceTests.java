package com.groom.manvsclass.service;

import com.groom.manvsclass.exception.InvalidImageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceTests {

    @TempDir
    private Path tempDir;  // Directory temporanea per i test

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        // crea un ImageService che usa la directory temporanea (inietta tempDir)
        imageService = new ImageService(tempDir.toString());
    }

    // TEST STORE_IMAGE

    @Test
    void testStoreImage_Success() throws IOException {
        // INPUT
        String imageName = "Calcolatrice_1.png";
        byte[] pngSignature = new byte[] {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
        byte[] fileContent = new byte[1024];

        // inserisce byte casuali in fileContent
        Random rand = new Random();
        rand.nextBytes(fileContent);

        // copia la png signature nei primi byte di fileContent, per simulare che il file sia un file png
        System.arraycopy(pngSignature, 0, fileContent, 0, pngSignature.length);

        // crea il multipart file mock
        MockMultipartFile multipartFile = new MockMultipartFile(
                "image",
                imageName,
                "image/png",
                fileContent
        );

        // ESECUZIONE DEL SERVICE
        imageService.storeImage(multipartFile, imageName);

        // VERIFICA CHE IL FILE SIA STATO SALVATO
        Path storedFile = tempDir.resolve(imageName);
        assertTrue(Files.exists(storedFile));

        byte[] savedContent = Files.readAllBytes(storedFile);
        assertArrayEquals(fileContent, savedContent);
    }

    @Test
    void testStoreImage_FakePng() throws IOException {
        // INPUT
        String imageName = "Calcolatrice_1.png";
        byte[] fileContent = "contenuto_immagine".getBytes();   // simula l'immagine come array di bytes a partire da un testo

        MockMultipartFile multipartFile = new MockMultipartFile(
                "image",
                imageName,
                "image/png",        // simula che il testo sia un png
                fileContent
        );

        // ESECUZIONE DEL SERVICE
        assertThrows(InvalidImageException.class, () -> imageService.storeImage(multipartFile, imageName));

        // VERIFICA CHE IL FILE NON SIA STATO SALVATO
        Path storedFile = tempDir.resolve(imageName);
        assertFalse(Files.exists(storedFile));
    }

    @Test
    void testStoreImage_ErrorDuringCopy() throws IOException {
        // INPUT
        String imageName = "Calcolatrice_1.png";

        // crea un mock per simulare il lancio di un'eccezione
        MultipartFile multipartFile = mock(MultipartFile.class);

        // Simula un errore nello stream
        when(multipartFile.getInputStream())
                .thenThrow(new IOException("Stream error"));

        // ESECUZIONE DEL SERVICE
        assertThrows(IOException.class, () -> {
            imageService.storeImage(multipartFile, imageName);
        });

        // Verifica che il file non sia stato creato
        Path storedFile = tempDir.resolve(imageName);
        assertFalse(Files.exists(storedFile));
    }

    // TEST DELETE_IMAGE

    @Test
    void testDeleteImage_Success() throws IOException {
        // INPUT
        String imageName = "Calcolatrice_1.png";
        Path targetLocation = tempDir.resolve(imageName);

        // Crea il file
        Files.write(targetLocation, "test content".getBytes());
        assertTrue(Files.exists(targetLocation));

        // ESECUZIONE DEL SERVICE
        imageService.deleteImage(imageName);

        // VERIFICA CHE IL FILE SIA STATO CANCELLATO
        assertFalse(Files.exists(targetLocation));
    }

    @Test
    void testDeleteImage_FileNotExists() throws IOException {
        // INPUT
        String imageName = "Calcolatrice_123.png";

        // ESECUZIONE DEL SERVICE (in realtà è una deleteIfExists, quindi non deve avere effetto)
        imageService.deleteImage(imageName);
    }

}