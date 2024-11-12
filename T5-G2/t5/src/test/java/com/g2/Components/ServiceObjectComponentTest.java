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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.g2.Interfaces.MockServiceManager;
import com.g2.t5.T5Application;

@SpringBootTest(classes = T5Application.class)
class ServiceObjectComponentTest {

    private ServiceObjectComponent serviceObjectComponent;
    private StubServiceManager serviceManager;

    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        serviceManager = new StubServiceManager(restTemplate);
        serviceObjectComponent = new ServiceObjectComponent(serviceManager, "testKey", "TestService", "TestAction", "param1");
    }

    @Test
    public void testGetModelSuccess() {
        // Configura il MockServiceManager per restituire un oggetto valido
        serviceManager.setShouldReturnTrue(true);
        Object expectedObject = "Valid Object";
        serviceManager.setReturnedObject(expectedObject);  // Metodo nel MockServiceManager per impostare l'oggetto di ritorno
        Map<String, Object> model = serviceObjectComponent.getModel();
        // Verifica che l'oggetto sia stato inserito correttamente nel modello
        assertNotNull(model, "Il modello non dovrebbe essere nullo.");
        assertEquals(expectedObject, model.get("testKey"), "L'oggetto nel modello dovrebbe essere quello restituito da handleRequest.");
    }

    @Test
    public void testGetModelNullObject() {
        // Configura il MockServiceManager per restituire null
        serviceManager.setReturnedObject(null);
        Map<String, Object> model = serviceObjectComponent.getModel();
        // Verifica che il modello contenga "Object not found" per la chiave specificata
        assertNotNull(model, "Il modello non dovrebbe essere nullo.");
        assertEquals("Object not found", model.get("testKey"), "Quando handleRequest restituisce null, il modello dovrebbe contenere 'Object not found'.");
    }

    @Test
    public void testGetModelException() {
        // Configura il MockServiceManager per lanciare un'eccezione
        serviceManager.setShouldThrowException();
        Map<String, Object> model = serviceObjectComponent.getModel();
        // Verifica che il metodo gestisca l'eccezione e restituisca null
        assertNull(model, "Il modello dovrebbe essere null quando si verifica un'eccezione.");
    }

    @Test
    public void testServiceObjectComponentFieldsWithReflection() throws Exception {
        String modelKey = "testKey";
        String serviceName = "TestService";
        String action = "TestAction";
        MockServiceManager mockServiceManager = new MockServiceManager(new RestTemplate());
        ServiceObjectComponent serviceObjectComponent = new ServiceObjectComponent(mockServiceManager, modelKey, serviceName, action);
        // Accesso ai campi privati tramite riflessione
        java.lang.reflect.Field serviceNameField = ServiceObjectComponent.class.getDeclaredField("serviceName");
        java.lang.reflect.Field actionField = ServiceObjectComponent.class.getDeclaredField("action");
        serviceNameField.setAccessible(true);
        actionField.setAccessible(true);
        // Verifica che i campi siano stati inizializzati correttamente
        assertEquals(serviceName, serviceNameField.get(serviceObjectComponent), "serviceName deve essere impostato correttamente.");
        assertEquals(action, actionField.get(serviceObjectComponent), "action deve essere impostato correttamente.");
        assertEquals(modelKey, serviceObjectComponent.getModelKey(), "modelKey deve essere impostato correttamente.");
    }

    @Test
    public void testGetSetModelKey() {
        String modelKey = "testKey";
        serviceObjectComponent = new ServiceObjectComponent(serviceManager, modelKey, "TestService", "TestAction");
        assertEquals(modelKey, serviceObjectComponent.getModelKey(), "getModelKey deve restituire la chiave di modello corretta.");
        String newModelKey = "newTestKey";
        serviceObjectComponent.setModelKey(newModelKey);
        assertEquals(newModelKey, serviceObjectComponent.getModelKey(), "getModelKey deve restituire la nuova chiave di modello.");
    }

    @Test
    public void testSetParams() {
        Object[] params = new Object[]{"param1", "param2"};
        serviceObjectComponent = new ServiceObjectComponent(serviceManager, "testKey", "TestService", "TestAction");
        serviceObjectComponent.setParams(params);
        Map<String, Object> model = serviceObjectComponent.getModel();
        assertNotNull(model, "Il modello non dovrebbe essere nullo.");
    }

}
