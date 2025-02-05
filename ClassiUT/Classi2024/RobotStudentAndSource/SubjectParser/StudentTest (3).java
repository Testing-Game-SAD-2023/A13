package student.lucaMigliaccioUno;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.SubjectParser;

// librerie per gestire lo stream
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
	void testSubjectParser() {
		new SubjectParser("2023 sw testing (2/10)");
	}
	
	// INIZIO TEST GETID
	@Test
	void testGetId() {
		// Caso di test 0: Stringa valida con identificatore numerico
        SubjectParser subjectParser = new SubjectParser("2023 sw testing (2/10)");
        // mi prendo l'identificatore numerico (sarà di tipo long)
        long id = subjectParser.getId();
        assertEquals(2023L, id);
	}
	
	@Test
	void testGetId1() {
		// Caso di test 1: Stringa senza identificatore numerico
        SubjectParser subjectParser1 = new SubjectParser("sw testing (2/10)");
        assertEquals(-1, subjectParser1.getId());
	}
	
	@Test
	void testGetId2() {
		// Caso di test 2: Stringa con identificatore numerico non valido
        SubjectParser subjectParser2 = new SubjectParser("2023sw testing (2/10)");
        assertEquals(-1, subjectParser2.getId());
	}
	
	// INIZIO TEST THIS RANGE (LOWER RANGE)
	@Test
	void testGetThisRange() {
		// Caso di test 0: Assumo che il LowerRange di default sia 1
		SubjectParser subjectParser = new SubjectParser("2023 sw testing");
	    assertEquals(1, subjectParser.getThisRange());
	}
	
	@Test
	void testGetThisRange1() {
	    // Caso di test 1: Stringa con formato non valido
	    SubjectParser subjectParser1 = new SubjectParser("sw testing");
	    assertNotNull(subjectParser1.getThisRange());
	}
	
	@Test
	void testGetThisRange2() {
		// Caso di test 2: Stringa con range specificato
	    SubjectParser subjectParser2 = new SubjectParser("2023 sw testing (3/8)");
	    assertEquals(3, subjectParser2.getThisRange());
	}
	
	@Test
	void testGetThisRange3() {
		// Caso di test 3: Stringa valida con range specificato con parentesi quadre
	    SubjectParser subjectParser3 = new SubjectParser("2023 sw testing [3/8]");
	    assertEquals(3, subjectParser3.getThisRange());
	}
	
	// INIZIO TEST UPPER RANGE
	@Test
	void testGetUpperRange() {
		// Caso di test 0: Assumo che l'UpperRange di default sia 1
		SubjectParser subjectParser = new SubjectParser("2023 sw testing");
		assertEquals(1, subjectParser.getUpperRange());
	}
	
	@Test
	void testGetUpperRange1() {
	    // Caso di test 1: Stringa con formato non valido
	    SubjectParser subjectParser1 = new SubjectParser("sw testing");
	    assertNotNull(subjectParser1.getThisRange());
	}
	
	@Test
	void testGetUpperRange2() {
		// Caso di test 2: Stringa con range specificato
	    SubjectParser subjectParser2 = new SubjectParser("2023 sw testing (3/8)");
	    assertEquals(8, subjectParser2.getUpperRange());
	}
	
	@Test
	void testGetUpperRange3() {
		// Caso di test 3: Stringa valida con range specificato con parentesi quadre
	    SubjectParser subjectParser2 = new SubjectParser("2023 sw testing [3/8]");
	    assertEquals(8, subjectParser2.getUpperRange());
	}
	
	// INIZIO TEST RANGE STRING
	@Test
	void testGetRangeString() {
		SubjectParser subjectParser = new SubjectParser("2023 sw testing");
		assertEquals(null, subjectParser.getRangeString());
	}
	
	// INIZIO TEST GET TITLE
	@Test
	void testGetTitle() {
		// Caso 0: nella stringa in ingresso non è specificato il range (null)
		SubjectParser subjectParser = new SubjectParser(null);
        assertNull(subjectParser.getTitle());
	}
	
	@Test
	void testGetTitle1() {
		// Caso 1: ultimo carattere )
		SubjectParser subjectParser1 = new SubjectParser("2023 sw testing )");
	    String result = subjectParser1.getTitle();
	    assertNotNull(result);
	    assertTrue(result.contains(")"));
	}
	
	@Test
	void testGetTitle2() {
		// Caso 3: ultimo carattere ]
		SubjectParser subjectParser2 = new SubjectParser("2023 sw testing ]");
	    String result = subjectParser2.getTitle();
	    assertNotNull(result);
	    assertTrue(result.contains("]"));
	}
	
	@Test
	void testGetTitle3() {
		// Caso 4: ()
		SubjectParser subjectParser3 = new SubjectParser("2023 sw testing ()");
	    String result = subjectParser3.getTitle();
	    assertNotNull(result);
	}
	
	@Test
	void testGetTitle4() {
		// Caso 5: []
		SubjectParser subjectParser4 = new SubjectParser("2023 sw testing []");
	    String result = subjectParser4.getTitle();
	    assertNotNull(result);
	}
	
	// INIZIO TEST MAIN
    @Test
    void testMain() {
    	// memorizzo l'output corrente per ripristinarlo alla fine del test
    	PrintStream originalOutput = System.out;
    	
        try {
        	// creo un nuovo stream di output e lo imposto come output standard
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));
            
            // passo argomenti specifici
            String[] args = {"2023 sw testing (3/8)"};
            SubjectParser.main(args);
            
            // recuper il contenuto dell'output e lo verifico tramite asserzioni
            String consoleOutput = outContent.toString();
            assertTrue(consoleOutput.contains("The upper range is 8"));
            assertTrue(consoleOutput.contains("The lower range is 3"));
            assertTrue(consoleOutput.contains("The message id is 2023"));
            assertTrue(consoleOutput.contains("The subject title is sw testing"));
        } finally {
        	// ripristino dell'output standard originale (sempre eseguito)
            System.setOut(originalOutput);
        }
    }
    
    @Test
    void testMainWithZeroArguments() {
    	PrintStream originalOutput = System.out;
    	
        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));
            
            // non passo nessun argomento
            String[] args = {};
            SubjectParser.main(args);
            
            String consoleOutput = outContent.toString();
            assertTrue(consoleOutput.contains("Usage: java SubjectParser <args>"));
        } finally {
            System.setOut(originalOutput);
        }
    }
}