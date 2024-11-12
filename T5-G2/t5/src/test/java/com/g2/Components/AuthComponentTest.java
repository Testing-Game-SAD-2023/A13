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
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    /*
     * Test 1: testAuthComponentInitialization
     * Precondizioni: AuthComponent deve essere inizializzato tramite il metodo
     * setUp.
     * Azioni: Verifica che l'istanza di AuthComponent non sia nulla e che
     * l'error code sia impostato correttamente.
     * Postcondizioni: L'AuthComponent è stato correttamente inizializzato
     * e l'error code è "Auth_error".
     */
    @Test
    public void testAuthComponentInitialization() {
        // Verifica che l'AuthComponent sia stato inizializzato correttamente
        assertNotNull(authComponent, "AuthComponent non deve essere nullo.");
        // Verifica che l'error code sia stato impostato correttamente
        assertEquals("Auth_error", authComponent.getErrorCode(),
                "L'error code deve essere 'Auth_error'.");
    }

}