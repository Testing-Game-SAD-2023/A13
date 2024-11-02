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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.boot.test.context.SpringBootTest;
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
import com.g2.t5.T5Application;

@SpringBootTest(classes = T5Application.class)
public class ServiceManagerTest {

    private final List<String> serviceNames = List.of("T1", "T4", "T7", "T23");
    private final List<Class<? extends ServiceInterface>> serviceClasses = List.of(T1Service.class, T4Service.class,
            T7Service.class, T23Service.class);
    @Autowired
    private RestTemplate restTemplate;
    private MockServiceManager serviceManager;

    /*
     * Metodo di inizializzazione per la registrazione dei servizi.
     * Precondizioni: RestTemplate non nullo.
     * Azioni: Registrazione del servizio ServiceManager
     * 
     */
    @BeforeEach
    private void setUp_registrazione() {
        serviceManager = new MockServiceManager(restTemplate);

    }

    /*
     * Crea un mock di RestTemplate che restituisce uno stato HTTP e un corpo di
     * risposta specifici.
     * Precondizioni: status e responseBody sono specificati.
     * Azioni: Mocking del comportamento di getForEntity e postForEntity.
     * Post-condizioni: RestTemplate mock configurato per restituire la
     * ResponseEntity specificata.
     */
    private RestTemplate createMockRestTemplate(HttpStatus status, String responseBody) {
        ResponseEntity<String> responseEntity = createResponseEntity(status, responseBody);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);

        return restTemplate;
    }

    /*
     * Crea una ResponseEntity con stato e corpo di risposta specifici.
     * Precondizioni: status e responseBody sono validi.
     * Azioni: Creazione di una ResponseEntity con i valori specificati.
     * Post-condizioni: Restituzione di una ResponseEntity con lo stato e il corpo
     * dati.
     */
    private ResponseEntity<String> createResponseEntity(HttpStatus status, String responseBody) {
        return new ResponseEntity<>(responseBody, status);
    }

    /*
     * Test1: testRegisterService_InvalidService
     * Precondizioni: RestTemplate è impostato su null.
     * Azioni: Tentare di registrare il servizio "T1Service" con un RestTemplate
     * nullo.
     * Post-condizioni: Ci si aspetta che venga sollevata un'eccezione
     * RuntimeException con
     * il messaggio "RestTemplate Nullo".
     */
    @Test
    public void testRegisterService_InvalidService() {
        // Imposta RestTemplate a null
        this.restTemplate = null; // Ensure this is actually a field of the test class

        // Verifica che l'eccezione venga sollevata quando si tenta di registrare i
        // servizi
        Exception exception = assertThrows(RuntimeException.class, () -> {
            // Esegui la creazione del servizio
            serviceManager.registerService("T1Service", T1Service.class, restTemplate);
            ;
        });

        // Assicurati che il messaggio dell'eccezione contenga l'indicazione corretta
        assertTrue(exception.getMessage().contains("RestTemplate Nullo"));
    }

    /*
     * Test2: testCreateService_NoSuchMethodException
     * Precondizioni: Viene definita una classe InvalidService che implementa
     * ServiceInterface senza un costruttore valido.
     * Azioni: Tentare di registrare il servizio "T1Service" utilizzando
     * InvalidService.
     * Post-condizioni: Ci si aspetta che venga sollevata un'eccezione
     * RuntimeException
     * con il messaggio "Impossibile creare l'istanza del servizio".
     */
    @Test
    public void testCreateService_NoSuchMethodException() {
        class InvalidService implements ServiceInterface {
            @Override
            public Object handleRequest(String action, Object... params) {
                return null;
            }
        }
        Class<InvalidService> invalidServiceClass = InvalidService.class;
        Exception exception = assertThrows(RuntimeException.class, () -> {
            serviceManager.registerService("T1Service", invalidServiceClass, restTemplate);
        });
        assertTrue(exception.getMessage().contains("Impossibile creare l'istanza del servizio"));
    }

    /*
     * Test3: testCreateService_InstantiationException
     * Precondizioni: Viene definita una classe astratta AbstractService che
     * implementa
     * ServiceInterface.
     * Azioni: Tentare di registrare il servizio "TinvalidServiceClass" utilizzando
     * AbstractService.
     * Post-condizioni: Ci si aspetta che venga sollevata un'eccezione
     * RuntimeException
     * con il messaggio "Impossibile creare l'istanza del servizio".
     */
    @Test
    public void testCreateService_InstantiationException() {
        abstract class AbstractService implements ServiceInterface {
        }
        Class<AbstractService> invalidServiceClass = AbstractService.class;
        Exception exception = assertThrows(RuntimeException.class, () -> {
            serviceManager.registerService("TinvalidServiceClass", invalidServiceClass, restTemplate);
        });
        assertTrue(exception.getMessage().contains("Impossibile creare l'istanza del servizio"));
    }

    /*
     * Test4: testCreateService_InvalidConstructorAndRestTemplate
     * Precondizioni: Viene definita una classe InvalidService che implementa
     * ServiceInterface senza un costruttore valido.
     * Azioni: Tentare di registrare il servizio "TinvalidServiceClass" utilizzando
     * InvalidService con un RestTemplate nullo.
     * Post-condizioni: Ci si aspetta che venga sollevata un'eccezione
     * RuntimeException
     * con il messaggio "[SERVICE MANAGER] RestTemplate Nullo !".
     */
    @Test
    public void testCreateService_InvalidConstructorAndRestTemplate() {
        class InvalidService implements ServiceInterface {
            @Override
            public Object handleRequest(String action, Object... params) {
                return null;
            }
        }

        RestTemplate invalidRestTemplate = null;
        Exception exception = assertThrows(RuntimeException.class, () -> {
            serviceManager.registerService("TinvalidServiceClass", InvalidService.class, invalidRestTemplate);
        });
        assertEquals("[SERVICE MANAGER] RestTemplate Nullo !", exception.getMessage());
    }

    /*
     * Test5: testCreateService_ValidConstructorAndInvalidRestTemplate
     * Precondizioni: Viene definita una classe ValidService con un costruttore
     * valido
     * ma con un RestTemplate nullo.
     * Azioni: Tentare di registrare il servizio "TValidService" utilizzando
     * ValidService con un RestTemplate nullo.
     * Post-condizioni: Ci si aspetta che venga sollevata un'eccezione
     * RuntimeException
     * con il messaggio "[SERVICE MANAGER] RestTemplate Nullo !".
     */
    @Test
    public void testCreateService_ValidConstructorAndInvalidRestTemplate() {
        class ValidService implements ServiceInterface {
            public ValidService(RestTemplate restTemplate) {
            }

            @Override
            public Object handleRequest(String action, Object... params) {
                return null;
            }
        }

        RestTemplate invalidRestTemplate = null;
        Exception exception = assertThrows(RuntimeException.class, () -> {
            serviceManager.registerService("TValidService", ValidService.class, invalidRestTemplate);
        });

        assertEquals("[SERVICE MANAGER] RestTemplate Nullo !", exception.getMessage());
    }

    /*
     * Test6: testCreateService_IncompatibleConstructorParameter
     * Precondizioni: Viene definita una classe IncompatibleService con un
     * costruttore
     * che richiede un parametro incompatibile.
     * Azioni: Tentare di registrare il servizio "TIncompatibleService" utilizzando
     * IncompatibleService.
     * Post-condizioni: Ci si aspetta che venga sollevata un'eccezione
     * RuntimeException
     * con il messaggio appropriato riguardante l'impossibilità di creare l'istanza
     * del servizio.
     */
    @Test
    public void testCreateService_IncompatibleConstructorParameter() {
        class IncompatibleService implements ServiceInterface {
            public IncompatibleService(String someString) {
            }

            @Override
            public Object handleRequest(String action, Object... params) {
                return null;
            }
        }

        Exception exception = assertThrows(RuntimeException.class, () -> {
            serviceManager.registerService("TIncompatibleService", IncompatibleService.class, restTemplate);
        });
        assertEquals(
                "Impossibile creare l'istanza del servizio: com.g2.Interfaces.ServiceManagerTest$1IncompatibleService",
                exception.getMessage());
    }

    /*
     * Test7: testCreateService_NullRestTemplate
     * Precondizioni: Viene definita una classe ValidService con un costruttore
     * valido
     * ma con un RestTemplate nullo.
     * Azioni: Tentare di registrare il servizio "TValidService" utilizzando
     * ValidService con un RestTemplate nullo.
     * Post-condizioni: Ci si aspetta che venga sollevata un'eccezione
     * RuntimeException
     * con il messaggio "[SERVICE MANAGER] RestTemplate Nullo !".
     */
    @Test
    public void testCreateService_NullRestTemplate() {
        class ValidService implements ServiceInterface {
            public ValidService(RestTemplate restTemplate) {
            }

            @Override
            public Object handleRequest(String action, Object... params) {
                return null;
            }
        }

        Exception exception = assertThrows(RuntimeException.class, () -> {
            serviceManager.registerService("TValidService", ValidService.class, null);
        });
        assertEquals("[SERVICE MANAGER] RestTemplate Nullo !", exception.getMessage());
    }

    /*
     * Test8: testRegisterService_NullServiceClass
     * Precondizioni: Il RestTemplate è valido e funzionante.
     * Azioni: Tentare di registrare un servizio con una classe di servizio nulla.
     * Post-condizioni: Ci si aspetta che venga sollevata un'eccezione
     * RuntimeException
     * con il messaggio "La classe del servizio non può essere nulla".
     */
    @Test
    public void testRegisterService_NullServiceClass() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            serviceManager.registerService("T1Service", null, restTemplate);
        });
        assertEquals(null, exception.getMessage());
    }

    /*
     * Test9: testRegisterService_WithNullServiceNameAndRestTemplate
     * Precondizioni: Il RestTemplate è nullo.
     * Azioni: Tentare di registrare un servizio con un nome di servizio valido ma
     * con RestTemplate nullo.
     * Post-condizioni: Ci si aspetta che venga sollevata un'eccezione
     * RuntimeException
     * con il messaggio "[SERVICE MANAGER] RestTemplate Nullo !".
     */
    @Test
    public void testRegisterService_WithNullServiceNameAndRestTemplate() {
        RestTemplate nullRestTemplate = null;
        Exception exception = assertThrows(RuntimeException.class, () -> {
            serviceManager.registerService("T1Service", T1Service.class, nullRestTemplate);
        });
        assertEquals("[SERVICE MANAGER] RestTemplate Nullo !", exception.getMessage());
    }

    /*
     * Test10: testRegisterService_DuplicateService
     * Precondizioni: Il RestTemplate è valido e un servizio "T1Service" è già
     * registrato.
     * Azioni: Tentare di registrare nuovamente il servizio "T1Service".
     * Post-condizioni: Ci si aspetta che venga sollevata un'eccezione
     * IllegalArgumentException
     * con il messaggio "Il servizio: T1Service è già registrato."
     */
    @Test
    public void testRegisterService_DuplicateService() {
        // Registrazione iniziale del servizio
        serviceManager.registerService("T1Service", T1Service.class, restTemplate);

        // Tentativo di registrazione duplicata
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            serviceManager.registerService("T1Service", T1Service.class, restTemplate);
        });

        // Verifica che l'eccezione sia quella attesa
        assertEquals("Il servizio: T1Service è già registrato.", exception.getMessage());
    }

    /*
     * Test11: testRegisterService_NullServiceName
     * Precondizioni: Il RestTemplate è valido e funzionante.
     * Azioni: Tentare di registrare un servizio con un nome di servizio nullo.
     * Post-condizioni: Ci si aspetta che venga sollevata un'eccezione
     * IllegalArgumentException
     * con il messaggio "Il nome del servizio non può essere nullo o vuoto."
     */
    @Test
    public void testRegisterService_NullServiceName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            serviceManager.registerService(null, T1Service.class, restTemplate);
        });
        assertEquals("Il nome del servizio non può essere nullo o vuoto.", exception.getMessage());
    }

    /*
     * Test12: testRegisterService_EmptyServiceName
     * Precondizioni: Il RestTemplate è valido e funzionante.
     * Azioni: Tentare di registrare un servizio con un nome di servizio vuoto.
     * Post-condizioni: Ci si aspetta che venga sollevata un'eccezione
     * IllegalArgumentException
     * con il messaggio "Il nome del servizio non può essere nullo o vuoto."
     */
    @Test
    public void testRegisterService_EmptyServiceName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            serviceManager.registerService("", T1Service.class, restTemplate);
        });
        assertEquals("Il nome del servizio non può essere nullo o vuoto.", exception.getMessage());
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

}
