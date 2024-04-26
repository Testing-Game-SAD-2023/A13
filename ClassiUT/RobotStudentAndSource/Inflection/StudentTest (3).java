package student.rosilenaCarannanteUno;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.Inflection;

class StudentTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testInflectionString() {
		//Test case per la creazione di un'istanza di inflection con il costruttore
		// Creo un'istanza di Inflection con il pattern "s$"
        Inflection inflection = new Inflection("s$");
        
        //Dato che l'attributo pattern della classe è privato vado ad utilizzare il metodo match 
        //Il pattern "s$" cerca parole che terminano con s quindi con la parola "cats" l'assert true è verificata
        assertTrue(inflection.match("cats"));
        assertFalse(inflection.match("bird"));
       
    }


	@Test
	void testInflectionStringString() {
		//Test case per la creazione di un'istanza di inflection con il costruttore
		// Creo un'istanza di Inflection con il pattern "s$"
        Inflection inflection = new Inflection("s$", "");
        
        //Dato che gli attributi della classe sono privati vado ad utilizzare i metodi match e replace
        //Il pattern "s$" cerca parole che terminano con s quindi con la parola "cats" l'assert true è verificata
        assertTrue(inflection.match("cats"));
        assertFalse(inflection.match("bird"));
        
        //Mi assicuro che il pattern "s$" sia sostituito da "" perchè il metodo replace dovrebbe sostituire la "s" con una stringa vuota ""
        assertEquals("cat", inflection.replace("cats"));
	}

	@Test
	void testInflectionStringStringBoolean() {
		//Test case per la creazione di un'istanza di inflection con il costruttore
		// Creo un'istanza di Inflection con il pattern "s$"
        Inflection inflection = new Inflection("s$", "", false);
        
        //Dato che gli attributi della classe sono privati vado ad utilizzare i metodi match e replace
        //Il pattern "s$" cerca parole che terminano con s quindi con la parola "cats" l'assert true è verificata
        assertTrue(inflection.match("cats"));
        assertFalse(inflection.match("bird"));
        
        //Mi assicuro che il pattern "s$" sia sostituito da "" perchè il metodo replace dovrebbe sostituire la "s" con una stringa vuota ""
        assertEquals("cat", inflection.replace("cats"));
        
        //Mi assicuro che ignorecase dell'istanza creata sia pari a false
        assertFalse(inflection.match("cat"));
	}

	@Test
	void testMatch() {
		// Creo un'istanza di Inflection con il pattern "s$"
		Inflection inflection = new Inflection("s$");

        //Verifico che il metodo match restituisca true per parole che terminano con "s"
        assertTrue(inflection.match("cats"));

        //Verifico che il metodo match restituisca false per parole che non terminano con "s"
        assertFalse(inflection.match("bird"));

        //Creo un'istanza di Inflection con il pattern "Dog" e replacement "" e ignoreCase impostato su true
        Inflection caseInsensitiveInflection = new Inflection("Dog", "", true);

        //Verifico che il metodo match restituisca true ignorando la distinzione tra maiuscole e minuscole
        assertTrue(caseInsensitiveInflection.match("dog"));
        assertTrue(caseInsensitiveInflection.match("Dog"));

        //Verifico che il metodo match restituisca false per una parola che non corrisponde esattamente al pattern
        assertFalse(caseInsensitiveInflection.match("doog"));
    }

	@Test
    void testReplace() {
        // Creo un'istanza di Inflection con il pattern "s$" e replacement ""
        Inflection inflection = new Inflection("s$", "");

        // Verifico che il metodo replace sostituisca correttamente il pattern nella parola
        assertEquals("cat", inflection.replace("cats"));

        // Creo un'istanza di Inflection con il pattern "Cat" e replacement "Dog" e ignoreCase impostato su true
        Inflection caseInsensitiveInflection = new Inflection("Cat", "Dog", true);

        // Verifico che il metodo replace sostituisca correttamente il pattern nella parola ignorando la distinzione tra maiuscole e minuscole
        assertEquals("Dogs", caseInsensitiveInflection.replace("Cats"));
        //Se il test è verificato, significa che la sostituzione è avvenuta correttamente e
        //che il pattern "Cat" nella parola "Cats" è stato sostituito con "Dog".

    }

	@Test
	void testPluralize() {
		// Test case per un plurale regolare
        assertEquals("dogs", Inflection.pluralize("dog"));

        // Test case per un plurale irregolare
        assertEquals("men", Inflection.pluralize("man"));

        // Test case per una parola uncountable
        assertEquals("equipment", Inflection.pluralize("equipment"));

        // Test case per una parola già al plurale
        assertEquals("cats", Inflection.pluralize("cats"));

        // Test case per matching case-sensitive
        assertEquals("CHILDREN", Inflection.pluralize("child").toUpperCase());
	}

	@Test
	void testSingularize() {
		// Test case per un singolare regolare
		assertEquals("dog", Inflection.singularize("dogs"));
		
		// Test case per un singolare irregolare
		assertEquals("man", Inflection.singularize("men"));
		
		// Test case per una parola uncountable
		assertEquals("money", Inflection.singularize("money"));
		
		// Test case per una parola già al singolare
		assertEquals("cat", Inflection.singularize("cat"));
		
		// Test case per un matching case-sensitive
		assertEquals("MOVE", Inflection.singularize("moves").toUpperCase());
		
	}

	@Test
	void testIsUncountable() {
		 // Test case per parole uncountable
        assertTrue(Inflection.isUncountable("equipment"));

        // Test case per parole countable
        assertFalse(Inflection.isUncountable("dogs"));

        // Test case per matching case-sensitive
        assertTrue(Inflection.isUncountable("EQUIPMENT"));
	}
	

}
