package com.g2.Components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.g2.t5.T5Application;

@SpringBootTest(classes = T5Application.class)
class ServiceLogicComponentTest {

    private ServiceLogicComponent serviceLogicComponent;
    private StubServiceManager serviceManager;

    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        serviceManager = new StubServiceManager(restTemplate);
        serviceLogicComponent = new ServiceLogicComponent(serviceManager, "Test_service", "executelogic", "params");
    }

    @Test
    public void testExecuteLogicSuccess() {
        // Caso di successo: handleRequest restituisce true
        serviceManager.setShouldReturnTrue(true);
        boolean result = serviceLogicComponent.executeLogic();
        assertTrue(result, "executeLogic deve restituire true quando l'esecuzione ha successo.");
    }

    @Test
    public void testExecuteLogicFailure() {
        // Caso di insuccesso: handleRequest restituisce false
        serviceManager.setShouldReturnTrue(false);
        boolean result = serviceLogicComponent.executeLogic();
        assertFalse(result, "executeLogic deve restituire false quando handleRequest restituisce false.");
    }

    @Test
    public void testExecuteLogicException() {
        // Caso di eccezione: handleRequest lancia un'eccezione
        serviceManager.setShouldThrowException(true);
        boolean result = serviceLogicComponent.executeLogic();
        assertFalse(result, "executeLogic deve restituire false quando viene lanciata un'eccezione.");
    }

    @Test
    public void testGetAndSetErrorCode() {
        // Test number: 4
        // Precondizioni: ErrorCode viene impostato su un nuovo valore.
        // Azioni: chiamare setErrorCode.
        // Postcondizioni: getErrorCode deve restituire il nuovo valore.

        String newErrorCode = "Custom_Error_Code";
        serviceLogicComponent.setErrorCode(newErrorCode);

        assertEquals(newErrorCode, serviceLogicComponent.getErrorCode(), "getErrorCode deve restituire il valore aggiornato di ErrorCode.");
    }
}
