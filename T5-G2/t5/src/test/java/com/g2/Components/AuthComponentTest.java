package com.g2.Components;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import com.g2.Interfaces.MockServiceManager;
import com.g2.t5.T5Application;

@SpringBootTest(classes = T5Application.class)
public class AuthComponentTest {

    private AuthComponent authComponent;
    private MockServiceManager serviceManager;

    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        serviceManager = new MockServiceManager(restTemplate);
        authComponent = new AuthComponent(serviceManager, "Jwt");
    }

    @Test
    public void testAuthComponentInitialization() {
        // Verifica che l'AuthComponent sia stato inizializzato correttamente
        assertNotNull(authComponent, "AuthComponent non deve essere nullo.");

        // Verifica che l'error code sia stato impostato correttamente
        assertEquals("Auth_error", authComponent.getErrorCode(), "L'error code deve essere 'Auth_error'.");
    }
}