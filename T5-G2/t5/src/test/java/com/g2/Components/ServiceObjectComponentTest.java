package com.g2.Components;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.g2.Interfaces.MockServiceManager;
import com.g2.Interfaces.ServiceManager;
import com.g2.t5.T5Application;

@SpringBootTest(classes = T5Application.class)
class ServiceObjectComponentTest {

    private ServiceObjectComponent serviceObjectComponent;


    @Autowired
    private RestTemplate restTemplate;
    private MockServiceManager serviceManager;

    /*
     * Metodo di inizializzazione per la registrazione dei servizi.
     * Precondizioni: RestTemplate non nullo.
     * Azioni: Registrazione del servizio ServiceManager.
     */
    @BeforeEach
    private void setUp_registrazione() {
        serviceManager = new MockServiceManager(restTemplate);
    }
}