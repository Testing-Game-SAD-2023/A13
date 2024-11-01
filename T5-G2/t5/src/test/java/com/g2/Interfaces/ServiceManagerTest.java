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
package com.g2.Interfaces;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.g2.Interfaces.BaseService;
import com.g2.Interfaces.ServiceActionDefinition;
import com.g2.Interfaces.ServiceInterface;
import com.g2.Interfaces.T1Service;
import com.g2.Interfaces.T23Service;
import com.g2.Interfaces.T4Service;
import com.g2.Interfaces.T7Service;

@ExtendWith(MockitoExtension.class)
public class ServiceManagerTest {

    private final List<String> serviceNames = List.of("T1", "T4", "T7", "T23");
    private final List<Class<? extends ServiceInterface>> serviceClasses = List.of(T1Service.class, T4Service.class, T7Service.class, T23Service.class);

    @MockBean
    private RestTemplate restTemplate;

    private MockServiceManager serviceManager; // Iniezione automatica del mock in ServiceManager

    private void setUp_registrazione() {
        // Registrare il servizio di mock
        serviceManager = new MockServiceManager(restTemplate);
        serviceManager.registerService("T1Service", T1Service.class, restTemplate);
        serviceManager.registerService("T4Service", T4Service.class, restTemplate);
        serviceManager.registerService("T7Service", T7Service.class, restTemplate);
        serviceManager.registerService("T23Service", T23Service.class, restTemplate);
    }

    // Metodo per creare un mock di RestTemplate con una risposta specificata
    private RestTemplate createMockRestTemplate(HttpStatus status, String responseBody) {
        ResponseEntity<String> responseEntity = createResponseEntity(status, responseBody);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);

        return restTemplate;
    }

    // Metodo per creare una ResponseEntity con uno stato e un corpo specificati
    private ResponseEntity<String> createResponseEntity(HttpStatus status, String responseBody) {
        return new ResponseEntity<>(responseBody, status);
    }

    // Test T1: Registrazione servizio con RestTemplate nullo
    @Test
    public void testRegisterService_InvalidService() {
        this.restTemplate = null;
        // Pre condizione restTemplate non funzionante        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            setUp_registrazione();
        });
        assertTrue(exception.getMessage().contains("RestTemplate Nullo"));
    }

    // Test T3: handleRequest servizio valido
    @Test
    public void testHandleRequest_RestTemplateSuccessResponse() {

        class MockService extends BaseService {
            public MockService(RestTemplate restTemplate) {
                super(restTemplate, "MockURL");
                // Registrazione delle azioni
                registerAction("MockAction", new ServiceActionDefinition(
                        params -> MockAction() //Metodo senza argomenti
                ));
            }

            @Override
            public Object handleRequest(String action, Object... params) {
                return "Success"; // Implementazione del mock
            }

            private int MockAction(){
                //Do nothing
                return 0; 
            }
        }

        this.restTemplate = new RestTemplate();
        //  when(restTemplate.getForObject(anyString(), any())).thenThrow(new RestClientException("Connection error"));
        //  Se hai un metodo che fa una richiesta POST
        //  when(restTemplate.postForEntity("http://mock_url", any(), eq(String.class))).thenReturn(new ResponseEntity<>("MOCKPost", HttpStatus.OK));
        serviceManager = new MockServiceManager(restTemplate);
        serviceManager.registerService("MockService", MockService.class, this.restTemplate);
    }

    // Test T4: handleRequest servizio non valido
    @Test
    public void testHandleRequest_ServiceNotFound() {
        // Pre-condizioni: Non registriamo alcun servizio
        // Azione: Tentativo di gestire richiesta per un servizio non registrato
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            serviceManager.handleRequest("T_invalid", "start");
        });

        // Post-condizioni: Verifica che l'eccezione sia stata sollevata con il
        // messaggio corretto
        assertTrue(exception.getMessage().contains("Servizio non trovato"));
    }

    // Test T5: handleRequest con errore di esecuzione
    @Test
    public void testHandleRequest_ErrorInServiceExecution() {
        // Simula che il servizio lanci l'eccezione MissingParametersException
        // when(mockService.handleRequest("someAction", new Object[]{})).thenThrow(new RuntimeException("Errore Runtime"));
        // Esegui la richiesta e verifica che il risultato sia null
        // Object result = serviceManager.handleRequest("T1", "someAction");
        // assertNull(result, "Expected handleRequest to return null when a RuntimeException occurs.");
    }

    // Test T6: createService con costruttore valido
    @Test
    public void testCreateService_ValidConstructor() {
        for (Class<? extends ServiceInterface> serviceClass : serviceClasses) {
            System.out.println("Inizio test di creazione servizio per classe: " + serviceClass.getSimpleName());
            // Pre-condizioni: Mock di RestTemplate con risposta OK
            RestTemplate mockRestTemplate = createMockRestTemplate(HttpStatus.OK, "Servizio creato");
            // Azione: Creazione del servizio
            ServiceInterface service = serviceManager.createService(serviceClass, mockRestTemplate);
            // Post-condizioni: Verifica che il servizio non sia nullo
            assertNotNull(service, "Il servizio creato non deve essere nullo.");
        }
    }

    // Test T7: createService con RESTTemplate non valido
    @Test
    public void testCreateService_InvalidRestTemplate() {
        // Classe di servizio senza costruttore valido
        for (Class<? extends ServiceInterface> serviceClass : serviceClasses) {
            System.out.println("Inizio test di creazione servizio per classe: " + serviceClass.getSimpleName());
            // Pre-condizioni: Mock di RestTemplate con risposta OK
            RestTemplate mockRestTemplate = createMockRestTemplate(HttpStatus.BAD_REQUEST, "Errore nella creazione");
            // Azione: Creazione del servizio
            Exception exception = assertThrows(RuntimeException.class, () -> {
                serviceManager.createService(serviceClass, mockRestTemplate);
            });
            // Post-condizioni: Verifica che l'eccezione sia stata sollevata con il
            // messaggio corretto
            assertTrue(exception.getMessage().contains("Impossibile creare l'istanza del servizio"));
        }
    }

    // Test T8_A: valuto la creazione di un servizio senza costruttore 
    @Test
    public void testCreateService_NoSuchMethodException() {
        // Classe di servizio senza costruttore valido
        class InvalidService implements ServiceInterface {

            /*
			*  Servizio non valido poiché non ha costruttore 
             */
            @Override
            public Object handleRequest(String action, Object... params) {
                return null;
            }
        }
        // Simula una classe di servizio senza un costruttore che accetta RestTemplate
        Class<InvalidService> invalidServiceClass = InvalidService.class; // Classe fittizia
        Exception exception = assertThrows(RuntimeException.class, () -> {
            serviceManager.createService(invalidServiceClass, restTemplate);
        });
        assertTrue(exception.getMessage().contains("Impossibile creare l'istanza del servizio"));
    }

    // Test T8_B: valuto la creazione di un servizio con istanza sbagliata 
    @Test
    public void testCreateService_InstantiationException() {
        // Classe di servizio senza costruttore valido
        abstract class AbstractService implements ServiceInterface {
            // Classe astratta
        }
        // Simula una classe di servizio senza un costruttore che accetta RestTemplate
        Class<AbstractService> invalidServiceClass = AbstractService.class; // Classe fittizia
        Exception exception = assertThrows(RuntimeException.class, () -> {
            serviceManager.createService(invalidServiceClass, restTemplate);
        });
        assertTrue(exception.getMessage().contains("Impossibile creare l'istanza del servizio"));
    }

    // Test T8_C: valuto la creazione di un servizio con costruttore privato  
    @Test
    public void testCreateService_IllegalAccessException() {
        // Classe di servizio senza costruttore valido
        class PrivateConstructorService implements ServiceInterface {

            private PrivateConstructorService(RestTemplate restTemplate) {
                // Costruttore privato
            }

            @Override
            public Object handleRequest(String action, Object... params) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'handleRequest'");
            }
        }
        // Simula una classe di servizio senza un costruttore che accetta RestTemplate
        Class<PrivateConstructorService> invalidServiceClass = PrivateConstructorService.class; // Classe fittizia
        Exception exception = assertThrows(RuntimeException.class, () -> {
            serviceManager.createService(invalidServiceClass, restTemplate);
        });
        assertTrue(exception.getMessage().contains("Impossibile creare l'istanza del servizio"));
    }

    // Test T8: createService con costruttore non valido e RestTemplate nullo
    @Test
    public void testCreateService_InvalidConstructorAndRestTemplate() {
        // Classe di servizio senza costruttore valido
        class InvalidService implements ServiceInterface {

            @Override
            public Object handleRequest(String action, Object... params) {
                return null;
            }
        }

        // Pre-condizioni: RestTemplate nullo
        RestTemplate invalidRestTemplate = null;

        // Azione: Tentativo di creare InvalidService con RestTemplate nullo
        Exception exception = assertThrows(RuntimeException.class, () -> {
            serviceManager.createService(InvalidService.class, invalidRestTemplate);
        });

        // Post-condizioni: Verifica che l'eccezione sia stata sollevata con il
        // messaggio corretto
        assertTrue(exception.getMessage().contains("Impossibile creare l'istanza del servizio"));
    }

    // Test T9: createService con costruttore valido e RestTemplate nullo
    @Test
    public void testCreateService_ValidConstructorAndInvalidRestTemplate() {
        // Classe di servizio con costruttore valido
        class ValidService implements ServiceInterface {

            public ValidService(RestTemplate restTemplate) {
            }

            @Override
            public Object handleRequest(String action, Object... params) {
                return null;
            }
        }

        // Pre-condizioni: RestTemplate nullo
        RestTemplate invalidRestTemplate = null;

        // Azione: Tentativo di creare ValidService con RestTemplate nullo
        Exception exception = assertThrows(RuntimeException.class, () -> {
            serviceManager.createService(ValidService.class, invalidRestTemplate);
        });

        // Post-condizioni: Verifica che l'eccezione sia stata sollevata con il
        // messaggio corretto
        assertTrue(exception.getMessage().contains("Impossibile creare l'istanza del servizio"));
    }

    // Test T10: handleRequest con azione sconosciuta
    @Test
    public void testHandleRequest_UnknownAction() {
        for (int i = 0; i < serviceNames.size(); i++) {
            String serviceName = serviceNames.get(i);
            System.out.println("Inizio test di gestione richiesta con azione sconosciuta per servizio: " + serviceName);

            // Pre-condizioni: Mock di un servizio e registrazione del servizio
            ServiceInterface mockService = mock(ServiceInterface.class);
            when(mockService.handleRequest("unknownAction", new Object[]{}))
                    .thenThrow(new UnsupportedOperationException("Azione non supportata"));
            //serviceManager.services.put(serviceName, mockService);
            RestTemplate mockRestTemplate = createMockRestTemplate(HttpStatus.BAD_REQUEST, "Azione non supportata");

            // Azione: Gestione di un'azione sconosciuta
            Object result = serviceManager.handleRequest(serviceName, "unknownAction");

            // Post-condizioni: Verifica che il risultato sia null
            assertNull(result,
                    "Il risultato dovrebbe essere null per un'azione sconosciuta per il servizio: " + serviceName);
        }
    }

    // Test T11: Verifica tipi di istanza dei servizi registrati
    @Test
    public void testRegisteredServices_CorrectInstanceType() {
        for (int i = 0; i < serviceNames.size(); i++) {
            String serviceName = serviceNames.get(i);
            Class<? extends ServiceInterface> serviceClass = serviceClasses.get(i);
            System.out.println("Inizio test di verifica tipi di istanza per servizio: " + serviceName);

            // Pre-condizioni: Mock di RestTemplate con risposta OK
            RestTemplate mockRestTemplate = createMockRestTemplate(HttpStatus.OK, "Registrazione completata");
            serviceManager.registerService(serviceName, serviceClass, mockRestTemplate);

            // Azione: Verifica i tipi di istanza
            //ServiceInterface service = serviceManager.services.get(serviceName);
            // Post-condizioni: Verifica che il servizio sia un'istanza del tipo corretto
            //assertTrue(serviceClass.isInstance(service), "Il servizio registrato non è del tipo corretto.");
        }
    }

    

}
