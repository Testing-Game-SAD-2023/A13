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
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import com.g2.t5.T5Application;

@SpringBootTest(classes = T5Application.class)
@ExtendWith(MockitoExtension.class)
public class PageBuilderTest {

    private StubServiceManager ServiceManager;
    
    @Mock
    private Model mockModel;
    private PageBuilder pageBuilder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        pageBuilder = new PageBuilder(ServiceManager, "testPage", mockModel);
    }

    /*
     * Testo un page builder senza componenti, deve restituire a prescindere il PageName deciso
     */
    @Test
    public void testHandlePageRequest_NoComponents_ReturnsPageName() {
        String result = pageBuilder.handlePageRequest();
        assertEquals("testPage", result);
    }

    /*
    *  Testo un page builder in cui il componente logico funziona, mi aspetto che 
    *  l'handler mi restituisca la pagina di default 
    */
    @Test
    public void testHandlePageRequest_LogicComponent_ReturnsPage() {
        ServiceLogicComponent mockLogicComponent = 
            new ServiceLogicComponent(ServiceManager, "mockService", "executeLogic_true");
        pageBuilder.setLogicComponents(mockLogicComponent);
        String result = pageBuilder.handlePageRequest();
        assertEquals("testPage", result); 
    }

    /*
    *  Testo un page builder in cui fallisce il componente logico, mi aspetto che 
    *  l'handler mi restituisca la pagina d'errore di default 
    */
    @Test
    public void testHandlePageRequest_LogicComponentFails_ReturnsErrorPage() {
        ServiceLogicComponent mockLogicComponent = 
            new ServiceLogicComponent(ServiceManager, "mockService", "executeLogic_false");
        mockLogicComponent.setErrorCode("mock_error");
        pageBuilder.setLogicComponents(mockLogicComponent);
        String result = pageBuilder.handlePageRequest();
        assertEquals("redirect:/error", result); // Verifica il redirect all'errore
    }
    /*
    *  Testo un page builder in cui fallisce il componente logico e in cui setto la pagina d'errore 
    *   mi aspetto che l'handler mi restituisca la pagina d'errore specificata 
    */
    @Test
    public void testHandlePageRequest_LogicComponentFails_ReturnsSpecificErrorPage() {
        ServiceLogicComponent mockLogicComponent = 
            new ServiceLogicComponent(ServiceManager, "mockService", "executeLogic_false");

        mockLogicComponent.setErrorCode("mock_error");
        pageBuilder.setLogicComponents(mockLogicComponent);
        pageBuilder.setErrorPage("mock_error", "mockPageError");

        String result = pageBuilder.handlePageRequest();
        assertEquals("mockPageError", result); // Verifica il redirect all'errore

    }

    /*
    *  Testo un page builder con un object componente, quindi deve restituire dati e il giusto page name
    */
    @Test
    public void testHandlePageRequest_ObjectComponent_SetsModelAttributes() {
        GenericObjectComponent mockObjectComponent = new GenericObjectComponent("mock_key", "mock_value");
        pageBuilder.setObjectComponents(mockObjectComponent);
        
        String result = pageBuilder.handlePageRequest();
        assertEquals("testPage", result);

        Map<String, Object> modelData = new HashMap<>();
        modelData.put("mock_key", "mock_value");
        verify(mockModel).addAllAttributes(modelData); // Verifica che i dati siano aggiunti al modello
    }


    @Test
    public void testHandlePageRequest_DuplicateKeysInModel_LogsError() {
        // Simula due componenti oggetto con chiavi duplicate
        Map<String, Object> modelData1 = new HashMap<>();
        modelData1.put("key1", "value1");
        
        Map<String, Object> modelData2 = new HashMap<>();
        modelData2.put("key1", "value2"); // Chiave duplicata

        GenericObjectComponent mockObjectComponent_1 = 
            new GenericObjectComponent("mock_key", "mock_value");
        GenericObjectComponent mockObjectComponent_2 = 
            new GenericObjectComponent("mock_key", "mock_value");


        pageBuilder.setObjectComponents(mockObjectComponent_1, mockObjectComponent_2);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            pageBuilder.handlePageRequest();
        });
        assertEquals("[PAGEBULDER][buildModel] individuate chiavi duplicate:mock_key", 
            exception.getMessage());
    }
     
     

         




}
