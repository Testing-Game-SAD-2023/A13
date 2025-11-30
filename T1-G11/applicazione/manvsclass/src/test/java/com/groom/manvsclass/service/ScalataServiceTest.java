package com.groom.manvsclass.service;

import com.groom.manvsclass.model.Scalata;
import com.groom.manvsclass.model.repository.ScalataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test semplificato - Solo upload e list scalate
 */
@ExtendWith(MockitoExtension.class)
class ScalataServiceTest {

    @Mock
    private ScalataRepository scalataRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private ScalataService scalataService;

    private Scalata scalataTest;
    private String validJwt = "valid-jwt-token";

    @BeforeEach
    void setUp() {
        // Prepara una scalata di test
        scalataTest = new Scalata();
        scalataTest.setScalataName("ScalataTest");
        scalataTest.setUsername("TestUser");
        scalataTest.setScalataDescription("Descrizione test");
        scalataTest.setNumberOfLevels(3);
        List<Integer> levelIds = Arrays.asList(1, 2, 3);
        scalataTest.setLevels(levelIds);
    }

    /* 
    @Test
    void testUploadScalata_Success() {
        System.out.println("=== TEST 1: Upload Scalata ===");
        
        // Given: JWT valido e repository che salva correttamente
        when(jwtService.isJwtValid(validJwt)).thenReturn(true);
        when(scalataRepository.save(any(Scalata.class))).thenReturn(scalataTest);

        // When: chiamo uploadScalata
        ResponseEntity<?> response = scalataService.uploadScalata(scalataTest);

        // Then: verifico risposta OK
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Body: " + response.getBody());
        
        assertEquals(HttpStatus.OK, response.getStatusCode(), 
                     "L'upload dovrebbe ritornare 200 OK");
        assertNotNull(response.getBody(), 
                      "Il body della risposta non dovrebbe essere null");
        
        // Verifico che save sia stato chiamato
        verify(scalataRepository, times(1)).save(any(Scalata.class));
        
        System.out.println("✅ Test Upload PASSED\n");
    }
    */

    @Test
    void testListScalate_Success() {
        System.out.println("=== TEST 2: List Scalate ===");
        
        // Given: repository ritorna lista con 2 scalate
        Scalata scalata2 = new Scalata();
        scalata2.setScalataName("Scalata2");
        scalata2.setUsername("User2");
        scalata2.setNumberOfLevels(5);
        
        List<Scalata> scalate = Arrays.asList(scalataTest, scalata2);
        when(scalataRepository.findAll()).thenReturn(scalate);

        // When: chiamo listScalate
        ResponseEntity<?> response = scalataService.listScalate();

        // Then: verifico risposta OK con lista
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Numero scalate: " + ((List<?>) response.getBody()).size());
        
        assertEquals(HttpStatus.OK, response.getStatusCode(),
                     "La lista dovrebbe ritornare 200 OK");
        assertTrue(response.getBody() instanceof List,
                   "Il body dovrebbe essere una List");
        assertEquals(2, ((List<?>) response.getBody()).size(),
                     "Dovrebbero esserci 2 scalate nella lista");
        
        // Verifico che findAll sia stato chiamato
        verify(scalataRepository, times(1)).findAll();
        
        System.out.println("✅ Test List PASSED\n");
    }

}
