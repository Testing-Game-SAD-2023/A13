/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.g2.Components;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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

  /*
     * Test1: testExecuteLogicWithAllowedValues
     * Precondizioni: La variabile da controllare è inizializzata a "testValue".
     * Azioni: Impostare i valori consentiti e chiamare executeLogic.
     * Post-condizioni: Dovrebbe restituire true, poiché "testValue" è incluso nei
     * valori consentiti.
     */
    @Test
    public void testExecuteLogicWithAllowedValues() {
        variableValidationLogicComponent.setCheckAllowedValues("testValue", "anotherValue");
        boolean result = variableValidationLogicComponent.executeLogic();

        assertTrue(result);
        assertNull(variableValidationLogicComponent.getErrorCode());
    }

    /*
     * Test2: testExecuteLogicWithNotAllowedValue
     * Precondizioni: La variabile da controllare è inizializzata a "testValue".
     * Azioni: Impostare valori consentiti che non includono "testValue" e chiamare
     * executeLogic.
     * Post-condizioni: Dovrebbe restituire false e impostare ErrorCode a
     * "VALUE_NOT_ALLOWED".
     */
    @Test
    public void testExecuteLogicWithNotAllowedValue() {
        variableValidationLogicComponent.setCheckAllowedValues("anotherValue");
        boolean result = variableValidationLogicComponent.executeLogic();

        assertFalse(result);
        assertEquals("VALUE_NOT_ALLOWED", variableValidationLogicComponent.getErrorCode());
    }

    /*
     * Test3: testExecuteLogicWithoutChecks
     * Precondizioni: La variabile da controllare è inizializzata a "testValue".
     * Azioni: Chiamare executeLogic senza impostare alcun controllo.
     * Post-condizioni: Dovrebbe restituire true, senza errori.
     */
    @Test
    public void testExecuteLogicWithoutChecks() {
        boolean result = variableValidationLogicComponent.executeLogic();

        assertTrue(result);
        assertNull(variableValidationLogicComponent.getErrorCode());
    }

    /*
     * Test4: testSetCheckAllowedValuesWithList
     * Precondizioni: La variabile da controllare è inizializzata a "testValue".
     * Azioni: Impostare i valori consentiti tramite una lista e verificare il
     * modello.
     * Post-condizioni: Dovrebbe contenere i valori consentiti e impostare il flag.
     */
    @Test
    public void testSetCheckAllowedValuesWithList() {
        variableValidationLogicComponent.setCheckAllowedValues(Arrays.asList("testValue", "newValue"));

        assertTrue(variableValidationLogicComponent.executeLogic());
        assertNull(variableValidationLogicComponent.getErrorCode());
    }

    /*
     * Test5: testExecuteLogicReturnsTrueWhenVariableIsNotNull
     * Precondizioni: checkNull è attivato e variableToCheck è impostato a "null".
     * Azioni: Chiamare executeLogic.
     * Post-condizioni: Dovrebbe restituire false e impostare ErrorCode.
     */
    @Test
    public void testExecuteLogicReturnsTrueWhenVariableIsNotNull() {
        String NullVariable = null;
        variableValidationLogicComponent = new VariableValidationLogicComponent(NullVariable);

        variableValidationLogicComponent.setCheckNull();
        boolean result = variableValidationLogicComponent.executeLogic();

        assertFalse(result);
        assertEquals("NULL_VARIABLE", variableValidationLogicComponent.getErrorCode());
    }

    /*
     * Test6: testExecuteLogic_NoChecks_NullValue
     * Precondizioni: La variabile da controllare è inizializzata a null.
     * Azioni: Chiamare executeLogic senza impostare alcun controllo.
     * Post-condizioni: Dovrebbe restituire true, senza errori.
     */
    @Test
    public void testExecuteLogic_NoChecks_NullValue() {
        variableValidationLogicComponent = new VariableValidationLogicComponent(null);
        boolean result = variableValidationLogicComponent.executeLogic();
        assertTrue(result);
        assertNull(variableValidationLogicComponent.getErrorCode());
    }

    /*
     * Test7: testExecuteLogic_NoChecks_AllowedValue
     * Precondizioni: La variabile da controllare è inizializzata a "allowedValue".
     * Azioni: Chiamare executeLogic senza impostare alcun controllo.
     * Post-condizioni: Dovrebbe restituire true, senza errori.
     */
    @Test
    public void testExecuteLogic_NoChecks_AllowedValue() {
        variableValidationLogicComponent = new VariableValidationLogicComponent("allowedValue");
        boolean result = variableValidationLogicComponent.executeLogic();
        assertTrue(result);
        assertNull(variableValidationLogicComponent.getErrorCode());
    }

    /*
     * Test8: testExecuteLogic_NoChecks_DisallowedValue
     * Precondizioni: La variabile da controllare è inizializzata a
     * "disallowedValue".
     * Azioni: Chiamare executeLogic senza impostare alcun controllo.
     * Post-condizioni: Dovrebbe restituire true, senza errori.
     */
    @Test
    public void testExecuteLogic_NoChecks_DisallowedValue() {
        variableValidationLogicComponent = new VariableValidationLogicComponent("disallowedValue");
        boolean result = variableValidationLogicComponent.executeLogic();
        assertTrue(result);
        assertNull(variableValidationLogicComponent.getErrorCode());
    }

    /*
     * Test9: testExecuteLogic_CheckNullEnabled_NullValue
     * Precondizioni: La variabile da controllare è inizializzata a null.
     * Azioni: Attivare il controllo di null e chiamare executeLogic.
     * Post-condizioni: Dovrebbe restituire false e impostare ErrorCode.
     */
    @Test
    public void testExecuteLogic_CheckNullEnabled_NullValue() {
        variableValidationLogicComponent = new VariableValidationLogicComponent(null);
        variableValidationLogicComponent.setCheckNull();
        boolean result = variableValidationLogicComponent.executeLogic();
        assertFalse(result);
        assertEquals("NULL_VARIABLE", variableValidationLogicComponent.getErrorCode());
    }

    /*
     * Test10: testExecuteLogic_CheckNullEnabled_AllowedValue
     * Precondizioni: La variabile da controllare è inizializzata a "allowedValue".
     * Azioni: Attivare il controllo di null e chiamare executeLogic.
     * Post-condizioni: Dovrebbe restituire true, senza errori.
     */
    @Test
    public void testExecuteLogic_CheckNullEnabled_AllowedValue() {
        variableValidationLogicComponent = new VariableValidationLogicComponent("allowedValue");
        variableValidationLogicComponent.setCheckNull();
        boolean result = variableValidationLogicComponent.executeLogic();
        assertTrue(result);
        assertNull(variableValidationLogicComponent.getErrorCode());
    }




}