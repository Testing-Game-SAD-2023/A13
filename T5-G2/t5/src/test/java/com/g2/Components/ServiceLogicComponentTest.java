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

/*
     * Test1: testExecuteLogicSuccess
     * Precondizioni: La variabile shouldReturnTrue è impostata su true nel
     * serviceManager.
     * Azioni: Chiamare executeLogic su serviceLogicComponent.
     * Post-condizioni: Dovrebbe restituire true indicando che l'esecuzione ha avuto
     * successo.
     */
    @Test
    public void testExecuteLogicSuccess() {
        serviceManager.setShouldReturnTrue(true);
        boolean result = serviceLogicComponent.executeLogic();
        assertTrue(result, "executeLogic deve restituire true quando l'esecuzione ha successo.");
    }

    /*
     * Test2: testExecuteLogicFailure
     * Precondizioni: La variabile shouldReturnTrue è impostata su false nel
     * serviceManager.
     * Azioni: Chiamare executeLogic su serviceLogicComponent.
     * Post-condizioni: Dovrebbe restituire false indicando che l'esecuzione è
     * fallita.
     */
    @Test
    public void testExecuteLogicFailure() {
        serviceManager.setShouldReturnTrue(false);
        boolean result = serviceLogicComponent.executeLogic();
        assertFalse(result, "executeLogic deve restituire false quando handleRequest restituisce false.");
    }

    /*
     * Test3: testExecuteLogicException
     * Precondizioni: La variabile shouldThrowException è impostata su true nel
     * serviceManager.
     * Azioni: Chiamare executeLogic su serviceLogicComponent.
     * Post-condizioni: Dovrebbe restituire false indicando che è stata lanciata
     * un'eccezione durante l'esecuzione.
     */
    @Test
    public void testExecuteLogicException() {
        serviceManager.setShouldThrowException();
        boolean result = serviceLogicComponent.executeLogic();
        assertFalse(result, "executeLogic deve restituire false quando viene lanciata un'eccezione.");
    }

    /*
     * Test4: testGetAndSetErrorCode
     * Precondizioni: ErrorCode è inizialmente nullo.
     * Azioni: Impostare ErrorCode su un nuovo valore "Custom_Error_Code" e chiamare
     * setErrorCode.
     * Post-condizioni: getErrorCode deve restituire "Custom_Error_Code".
     */
    @Test
    public void testGetAndSetErrorCode() {
        String newErrorCode = "Custom_Error_Code";
        serviceLogicComponent.setErrorCode(newErrorCode);

        assertEquals(newErrorCode, serviceLogicComponent.getErrorCode(),
                "getErrorCode deve restituire il valore aggiornato di ErrorCode.");
    }

}
