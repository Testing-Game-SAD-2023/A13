package student.raffaeleCuzzanitiUno;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.ParameterParser;

class StudentTest {
	
	// Dichiaro un oggetto parser di tipo ParameterParser
	private ParameterParser parser;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		// Istanzio l'oggetto parser
		parser = new ParameterParser();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testParameterParser() {
		// Verifico l'istanziazione dell'oggetto parser
		assertNotNull(parser);
	}
	
	@Test
	void testIsLowerCaseNames() {
	    // Verifico che isLowerCaseNames restituisca il valore predefinito (false)
	    assertFalse(parser.isLowerCaseNames());

	    // Imposto isLowerCaseNames a true
	    parser.setLowerCaseNames(true);

	    // Verifico che isLowerCaseNames restituisca true dopo essere stato impostato
	    assertTrue(parser.isLowerCaseNames());
	}

	@Test
	void testSetLowerCaseNames() {
		// Imposto isLowerCaseNames a true
		parser.setLowerCaseNames(true);
		
	    // Verifico che isLowerCaseNames restituisca true dopo essere stato impostato
		assertTrue(parser.isLowerCaseNames());
		
		// Imposto isLowerCaseNames a false
		parser.setLowerCaseNames(false);
				
		// Verifico che isLowerCaseNames restituisca false dopo essere stato impostato
		assertFalse(parser.isLowerCaseNames());
	}

	@Test
	void testParseStringCharArray(){
		// Istanzio una stringa con la coppia chiave, valore
		String stringa = "nome=Kvicha;cognome=Kvaratskhelia";
		
		// Istanzio un char[] di separatori
		char[] separatori = {';'};
		
		// Istanzio una map e la inizializzo con i valori definiti precedentemente
		var mappa = parser.parse(stringa, separatori);
		
		// Verifico che la mappa non sia vuota
		assertFalse(mappa.isEmpty());
		
		// Verifico che la mappa contenga la coppia chiave, valore
		assertEquals("Kvicha", mappa.get("nome"));
		assertEquals("Kvaratskhelia", mappa.get("cognome"));
    }

	@Test
	void testParseStringChar() {
		// Istanzio una stringa con la coppia chiave, valore ed inserisco anche degli spazi bianchi
		String stringa = "  nome=Kvicha;cognome=Kvaratskhelia  ";
		
		// Istanzio un char di separatori
		char separatori = ';';
		
		// Istanzio una map e la inizializzo con i valori definiti precedentemente
		var mappa = parser.parse(stringa, separatori);
		
		// Verifico che la mappa non sia vuota
		assertFalse(mappa.isEmpty());
		
		//Verifico che la mappa contenga la coppia chiave, valore
		assertEquals("Kvicha", mappa.get("nome"));
		assertEquals("Kvaratskhelia", mappa.get("cognome"));
	}

	@Test
	void testParseCharArrayChar() {
		// Istanzio un char[] con le virgolette
		char[] stringa = new char[] {'n', 'o', 'm', 'e', '=', '"', 'K', 'v', 'i', 'c', 'h', 'a', '"', ';', 'c', 'o', 'g', 'n', 'o', 'm', 'e', '=', '"', 'K', 'v', 'a', 'r', 'a', 't', 's', 'k', 'h', 'e', 'l', 'i', 'a', '"' };
				
		// Istanzio un char di separatori
		char separatori = ';';
				
		// Istanzio una map e la inizializzo con i valori definiti precedentemente
		var mappa = parser.parse(stringa, separatori);

		// Verifico che la mappa non sia vuota
		assertFalse(mappa.isEmpty());
				
		// Verifico che la mappa contenga la coppia chiave, valore
		assertEquals("Kvicha", mappa.get("nome"));
		assertEquals("Kvaratskhelia", mappa.get("cognome"));
	}

	@Test
	void testParseCharArrayIntIntChar() {
		// Istanzio un char[] con la coppia chiave, valore
		char[] stringa = "nome=Kvicha;cognome=Kvaratskhelia".toCharArray();
		
		// Istanzio offset
		int offset = 0;
		
		// Istanzio lunghezza char
		int lunghezza = stringa.length;
		
		// Istanzio un char di separatori
		char separatori = ';';
		
		// Istanzio una map e la inizializzo con i valori definiti precedentemente
		var mappa = parser.parse(stringa, offset, lunghezza, separatori);
		
		// Verifico che la mappa non sia vuota
		assertFalse(mappa.isEmpty());
		
		// Verifico che la mappa contenga la coppia chiave, valore
		assertEquals("Kvicha", mappa.get("nome"));
	}
	
	@Test
	void testParseNoStringCharArray() {
		// Istanzio una stringa vuota
		String stringa = null;
				
		//Istanzio un char di separatori
		char[] separatori = {';'};
				
		// Istanzio una map e la inizializzo con i valori definiti precedentemente
		var mappa = parser.parse(stringa, separatori);
				
		// Verifico che la mappa sia vuota
		assertTrue(mappa.isEmpty());
	}
	
	@Test
	void testParseStringNoCharArray() {
		// Istanzio una stringa con la coppia chiave, valore
		String stringa = "nome=Kvicha;cognome=Kvaratskhelia";
				
		// Istanzio un char di separatori vuoto
		char[] separatori = {};
				
		// Istanzio una map e la inizializzo con i valori definiti precedentemente
		var mappa = parser.parse(stringa, separatori);
				
		// Verifico che la mappa sia vuota
		assertTrue(mappa.isEmpty());
	}
	
	@Test
	void testParseNoStringChar() {
		// Istanzio una stringa vuota
		String stringa = null;
		
		// Istanzio un char di separatori
		char separatori = ';';
		
		// Istanzio una map e la inizializzo con i valori definiti precedentemente
		var mappa = parser.parse(stringa, separatori);
						
		// Verifico che la mappa sia vuota
		assertTrue(mappa.isEmpty());
	}
	
	@Test
	void testParseCharNoArrayChar() {
		// Istanzio un char[] vuoto
		char[] stringa = null;
		
		// Istanzio un char di separatori
		char separatori = ';';
		
		// Istanzio una map e la inizializzo con i valori definiti precedentemente
		var mappa = parser.parse(stringa, separatori);
		
		// Verifico che la mappa sia vuota
		assertTrue(mappa.isEmpty());
	}
	
	@Test
	void testParseNoCharArrayIntIntChar() {
		// Istanzio un char[] vuoto
		char[] stringa = null;
		
		// Istanzio un char di separatori
		char separatori = ';';
		
		// Istanzio offset
		int offset = 0;
		
		// Istanzio lunghezza
		int lunghezza = 0;
		
		// Istanzio una map e la inizializzo con i valori definiti precedentemente
		var mappa = parser.parse(stringa, offset, lunghezza, separatori);
		
		// Verifico che la mappa sia vuota
		assertTrue(mappa.isEmpty());
	}
	
	@Test
	void testParseCharArrayIntIntCharToLowerCase() {
		// Istanzio un char[] con la coppia chiave, valore
		char[] stringa = "NOME=Kvicha;COGNOME=Kvaratskhelia".toCharArray();
				
		// Istanzio offset
		int offset = 0;
				
		// Istanzio lunghezza char
		int lunghezza = stringa.length;
				
		// Istanzio un char di separatori
		char separatori = ';';
		
		// Imposto i caratteri minuscoli
		parser.setLowerCaseNames(true);
				
		// Istanzio una map e la inizializzo con i valori definiti precedentemente
		var mappa = parser.parse(stringa, offset, lunghezza, separatori);
				
		// Verifico che la mappa non sia vuota
		assertFalse(mappa.isEmpty());
				
		// Verifico che la mappa contenga la coppia chiave, valore
		assertEquals("Kvicha", mappa.get("nome"));
	}

}
