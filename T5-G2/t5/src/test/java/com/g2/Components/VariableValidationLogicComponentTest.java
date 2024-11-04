package com.g2.Components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import com.g2.t5.T5Application;

@SpringBootTest(classes = T5Application.class)
public class VariableValidationLogicComponentTest {

    private VariableValidationLogicComponent variableValidationLogicComponent;

    @BeforeEach
    public void setUp() {
        // Imposta una variabile di esempio per il test
        String variableToCheck = "testValue";
        variableValidationLogicComponent = new VariableValidationLogicComponent(variableToCheck);
    }


    @Test
    public void testExecuteLogicWithAllowedValues() {
        // Test number: 1
        // Precondizioni: La variabile da controllare è inizializzata a "testValue".
        // Azioni: Impostare i valori consentiti e chiamare executeLogic.
        // Postcondizioni: Dovrebbe restituire true, poiché "testValue" è incluso nei valori consentiti.

        variableValidationLogicComponent.setCheckAllowedValues("testValue", "anotherValue");
        boolean result = variableValidationLogicComponent.executeLogic();

        assertTrue(result);
        assertNull(variableValidationLogicComponent.getErrorCode());
    }

    @Test
    public void testExecuteLogicWithNotAllowedValue() {
        // Test number: 2
        // Precondizioni: La variabile da controllare è inizializzata a "testValue".
        // Azioni: Impostare valori consentiti che non includono "testValue" e chiamare executeLogic.
        // Postcondizioni: Dovrebbe restituire false e impostare ErrorCode a "VALUE_NOT_ALLOWED".

        variableValidationLogicComponent.setCheckAllowedValues("anotherValue");
        boolean result = variableValidationLogicComponent.executeLogic();

        assertFalse(result);
        assertEquals("VALUE_NOT_ALLOWED", variableValidationLogicComponent.getErrorCode());
    }

    @Test
    public void testExecuteLogicWithoutChecks() {
        // Test number: 3
        // Precondizioni: La variabile da controllare è inizializzata a "testValue".
        // Azioni: Chiamare executeLogic senza impostare alcun controllo.
        // Postcondizioni: Dovrebbe restituire true, senza errori.

        boolean result = variableValidationLogicComponent.executeLogic();

        assertTrue(result);
        assertNull(variableValidationLogicComponent.getErrorCode());
    }

    @Test
    public void testSetCheckAllowedValuesWithList() {
        // Test number: 4
        // Precondizioni: La variabile da controllare è inizializzata a "testValue".
        // Azioni: Impostare i valori consentiti tramite una lista e verificare il modello.
        // Postcondizioni: Dovrebbe contenere i valori consentiti e impostare il flag.

        variableValidationLogicComponent.setCheckAllowedValues(Arrays.asList("testValue", "newValue"));

        assertTrue(variableValidationLogicComponent.executeLogic());
        assertNull(variableValidationLogicComponent.getErrorCode());
    }

    @Test
    public void testExecuteLogicReturnsTrueWhenVariableIsNotNull() {
        // Test number: 5
        // Precondizioni: checkNull è attivato e variableToCheck è impostato a "null".
        // Azioni: Chiamare executeLogic.
        // Postcondizioni: Dovrebbe restituire false e impostare ErrorCode.

        String NullVariable = null;
        variableValidationLogicComponent = new VariableValidationLogicComponent(NullVariable);

        variableValidationLogicComponent.setCheckNull();
        boolean result = variableValidationLogicComponent.executeLogic();

        assertFalse(result);
        assertEquals("NULL_VARIABLE", variableValidationLogicComponent.getErrorCode());
    }



}