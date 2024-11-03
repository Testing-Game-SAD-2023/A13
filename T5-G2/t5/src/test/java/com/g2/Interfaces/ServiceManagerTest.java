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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.g2.t5.T5Application;

@SpringBootTest(classes = T5Application.class)
public class ServiceManagerTest {

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
        // Verifica che l'eccezione venga sollevata quando si tenta di registrare i
        // servizi
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            // Esegui la creazione del servizio
            serviceManager.registerService("T1Service", T1Service.class, null);
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
            public ValidService(RestTemplate restTemplate) {}
            @Override
            public Object handleRequest(String action, Object... params) {
                return null;
            }
        }
        RestTemplate invalidRestTemplate = null;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
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
        assertEquals("Impossibile creare l'istanza del servizio: com.g2.Interfaces.ServiceManagerTest$1IncompatibleService", exception.getMessage());
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
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            serviceManager.registerService("T1Service", null, restTemplate);
        });
        assertEquals("[SERVICE MANAGER] serviceClass Nullo !", exception.getMessage());
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


    @Test
    public void handleRequest_NoExistAction() {
        serviceManager.registerService("MockService", T1Service.class, this.restTemplate);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            serviceManager.handleRequest("MockService", "MockAction");
        });
        assertEquals("[HANDLEREQUEST] Azione non riconosciuta: MockAction", exception.getMessage());
    }

}
