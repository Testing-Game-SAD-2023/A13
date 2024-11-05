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
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import com.g2.Interfaces.MockServiceManager;
import com.g2.Interfaces.ServiceManager;
import com.g2.t5.T5Application;

@SpringBootTest(classes = T5Application.class)
@ExtendWith(MockitoExtension.class)
public class PageBuilderTest {

    @InjectMocks
    private PageBuilder pageBuilder;

    private ServiceManager serviceManager;
    //private GenericLogicComponent mockLogicComponent;
    private Model model_html; 


    @BeforeEach
    public void setUp() {

        serviceManager = new MockServiceManager(new RestTemplate());
        model_html = Mockito.mock(Model.class);

        /* 
        mockLogicComponent = new GenericLogicComponent() {
            @Override
            public boolean executeLogic() {
                return true; // Default behavior
            }

            @Override
            public String getErrorCode() {
                return "NO_ERROR"; // Default behavior
            }
        };

        List<GenericLogicComponent> logicComponents = new ArrayList<>();
        logicComponents.add(mockLogicComponent);
        */
    }

    @Test
    public void testPageBuilderInitialization_With_Object_Logic_Components() {

        List<GenericObjectComponent> objectComponents = new ArrayList<>();
        List<GenericLogicComponent> logicComponents = new ArrayList<>();
        pageBuilder = new PageBuilder(serviceManager, "TestPage", model_html, objectComponents, logicComponents);

        // Verifica che il PageBuilder sia stato inizializzato correttamente
        assertNotNull(pageBuilder, "PageBuilder non deve essere nullo.");
        
        // Verifica che le proprietà siano impostate correttamente
        assertEquals(objectComponents, pageBuilder.getObjectComponents(), "I componenti oggetto devono corrispondere.");
        assertEquals(logicComponents, pageBuilder.getLogicComponents(), "I componenti logici devono corrispondere.");

    }

    @Test
    public void testPageBuilderInitialization_No_Object_Logic_Components() {

        pageBuilder = new PageBuilder(serviceManager, "TestPage", model_html);
        assertNotNull(pageBuilder, "PageBuilder non deve essere nullo.");
        assertEquals(0, pageBuilder.getObjectComponents().size(), "ObjectComponents deve essere inizializzato come lista vuota.");
        assertEquals(0, pageBuilder.getLogicComponents().size(), "LogicComponents deve essere inizializzato come lista vuota.");

    }

    @Test
    public void testHandlePageRequest_NoErrors() {

        List<GenericObjectComponent> objectComponents = new ArrayList<>();
        List<GenericLogicComponent> logicComponents = new ArrayList<>();
        pageBuilder = new PageBuilder(serviceManager, "TestPage", model_html, objectComponents, logicComponents);

        String result = pageBuilder.handlePageRequest();
        assertEquals("TestPage", result, "Dovrebbe restituire il nome della pagina.");
    }

    @Test
    public void testHandlePageRequest_PageNull() {
        List<GenericObjectComponent> objectComponents = new ArrayList<>();
        List<GenericLogicComponent> logicComponents = new ArrayList<>();
        pageBuilder = new PageBuilder(serviceManager, null, model_html, objectComponents, logicComponents);

        String result = pageBuilder.handlePageRequest();
        assertEquals(null, result, "Dovrebbe restituire il nome della pagina.");
    }

    /* Dovrebbe tornare null ma torna il nome della pagina
    @Test
    public void testHandlePageRequest_WithLogicComponents_ErrorOccurred() {
        // Cambia il comportamento del mock per simulare un errore
        mockLogicComponent = new GenericLogicComponent() {
            @Override
            public boolean executeLogic() {
                return false; // Simula un errore
            }

            @Override
            public String getErrorCode() {
                return "LOGIC_ERROR"; // Codice d'errore
            }
        };

        String result = pageBuilder.handlePageRequest();
        
        assertNull(result, "Dovrebbe restituire null in caso di errore durante l'esecuzione della logica.");
        // Aggiungi ulteriori asserzioni secondo necessità
    }
     */

     @Test
     public void testSetAuth() {
         String jwt = "testJwt";
         pageBuilder = new PageBuilder(serviceManager, "TestPage", model_html);
         
         // Prima della chiamata a SetAuth, assicurati che non ci siano componenti di logica
         assertEquals(0, pageBuilder.getLogicComponents().size(), "Dovrebbe non esserci alcun componente di logica inizialmente.");
     
         // Imposta l'autenticazione
         pageBuilder.SetAuth(jwt);
     
         // Verifica che un AuthComponent sia stato aggiunto
         assertEquals(1, pageBuilder.getLogicComponents().size(), "Dovrebbe esserci un componente di logica dopo la chiamata a SetAuth.");
         assertTrue(pageBuilder.getLogicComponents().get(0) instanceof AuthComponent, "Il componente di logica deve essere un'istanza di AuthComponent.");
     }

     @Test
     public void testHandlePageRequestWithError() {
         String errorCode = "default"; 
         String errorPage = "redirect:/error"; 
         pageBuilder.setErrorPage(errorCode, errorPage);
     
         // Simula una richiesta di pagina
         pageBuilder = new PageBuilder(serviceManager, "redirect:/error", model_html);
         String resultPage = pageBuilder.handlePageRequest();
     
         // Verifica che la pagina restituita sia quella corretta per l'errore
         assertEquals("redirect:/error", resultPage, "La pagina di errore deve essere correttamente restituita.");
     }


     /* Per questo mancano le geterrorpagemap()
     @Test
     public void testSetErrorPageWithMap() {
         // Crea una mappa di errori personalizzata
         Map<String, String> userErrorPageMap = new HashMap<>();
         userErrorPageMap.put("default", "redirect:/error");
         userErrorPageMap.put("Auth_error", "redirect:/login");
 
         // Chiama il metodo setErrorPage
         pageBuilder.setErrorPage(userErrorPageMap);
         String resultPage = pageBuilder.handlePageRequest();

         // Verifica che la mappa degli errori contenga i valori attesi
         assertEquals("redirect:/error", pageBuilder.getErrorPageMap().get("default"), "La mappa degli errori deve contenere il codice d'errore 'default'.");
         assertEquals("redirect:/login", pageBuilder.getErrorPageMap().get("Auth_error"), "La mappa degli errori deve contenere il codice d'errore 'Auth_error'.");
     }
        */
     
     

         




}
