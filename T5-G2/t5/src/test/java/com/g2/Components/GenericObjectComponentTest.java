package com.g2.Components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import com.g2.t5.T5Application;

@SpringBootTest(classes = T5Application.class)
public class GenericObjectComponentTest {
    private GenericObjectComponent genericObjectComponent;

    @BeforeEach
    public void setUp() {
        // Imposta una chiave e un oggetto di esempio
        String key = "testKey";
        Object object = new Object(); // Puoi sostituire con un oggetto specifico per il tuo test

        // Crea un'istanza di GenericObjectComponent
        genericObjectComponent = new GenericObjectComponent(key, object);
    }

        // Test number: 1
        // Precondizioni: genericObjectComponent è stato inizializzato con una chiave e un oggetto.
        // Azioni: Chiamare il metodo getModel.
        // Postcondizioni: Il modello restituito deve contenere l'oggetto con la chiave "testKey".

    @Test
    public void testGetModel() {
        
        Map<String, Object> model = genericObjectComponent.getModel();
        assertNotNull(model);
        assertEquals(1, model.size());
        assertTrue(model.containsKey("testKey"));
        assertNotNull(model.get("testKey"));
    }

        // Test number: 2
        // Precondizioni: genericObjectComponent è stato inizializzato con una chiave e un oggetto.
        // Azioni: Chiamare setObject con una nuova chiave e un nuovo oggetto.
        // Postcondizioni: Il modello deve contenere il nuovo oggetto con la chiave specificata.

    @Test
    public void testSetObject() {

        String newKey = "newKey";
        Object newObject = new Object();
        genericObjectComponent.setObject(newKey, newObject);

        Map<String, Object> model = genericObjectComponent.getModel();
        assertTrue(model.containsKey(newKey));
        assertEquals(newObject, model.get(newKey));
    }

        // Test number: 3
        // Precondizioni: genericObjectComponent è stato inizializzato con una chiave e un oggetto.
        // Azioni: Chiamare setObject con una chiave non nulla e un oggetto nullo.
        // Postcondizioni: Il modello deve contenere la chiave ma il valore deve essere nullo.

    @Test
    public void testSetObjectWithNull() {

        String nullKey = "nullKey";
        genericObjectComponent.setObject(nullKey, null);

        Map<String, Object> model = genericObjectComponent.getModel();
        assertTrue(model.containsKey(nullKey));
        assertNull(model.get(nullKey));
    }

        // Test number: 4
        // Precondizioni: Non ci sono condizioni particolari.
        // Azioni: Creare un nuovo GenericObjectComponent con chiave e oggetto validi.
        // Postcondizioni: Il modello deve contenere l'oggetto con la chiave specificata.

    @Test
    public void testConstructorWithValidKeyAndObject() {

        String key = "validKey";
        Object object = new Object();
        GenericObjectComponent component = new GenericObjectComponent(key, object);

        Map<String, Object> model = component.getModel();
        assertNotNull(model);
        assertEquals(1, model.size());
        assertTrue(model.containsKey(key));
        assertNotNull(model.get(key));
    }

        // Test number: 5
        // Precondizioni: Non ci sono condizioni particolari.
        // Azioni: Creare un nuovo GenericObjectComponent con chiave nulla e oggetto valido.
        // Postcondizioni: Il modello deve essere vuoto.

    @Test
    public void testConstructorWithNullKey() {

        GenericObjectComponent component = new GenericObjectComponent(null, new Object());
        
        Map<String, Object> model = component.getModel();
        assertNotNull(model);
        assertTrue(model.isEmpty());
    }
}
