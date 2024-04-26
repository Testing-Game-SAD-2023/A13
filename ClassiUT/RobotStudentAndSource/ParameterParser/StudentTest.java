package student.chiaraPeruzziUno;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.ParameterParser;

class StudentTest{
	
	private ParameterParser parser;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		//creo il mio parser
		parser = new ParameterParser();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testParameterParser() {
		//verifica che il parser non sia nullo
		assertNotNull(parser);
	}

	@Test
	void testIsLowerCaseNames() {
		//verifica che l'attributo isLowerCaseNames sia settato a false dal costruttore
		assertFalse(parser.isLowerCaseNames());
		
	}

	@Test
	void testSetLowerCaseNames() {
		//controlla che il metodo setLowerCaseNames funzioni correttamente
		
		//imposta il valore di isLowerCaseNames a true e verifica
		parser.setLowerCaseNames(true);
		assertTrue(parser.isLowerCaseNames());
		
		//imposta il valore di isLowerCaseNames a true e verifica
		parser.setLowerCaseNames(false);
		assertFalse(parser.isLowerCaseNames());
	}

	@Test
	void testParseStringCharArray() {
		//stringa e array di separatori
		//test di parse(final String str, char[] separators) 
		
		char[] separator = {';'}; 
        String inputString = "nome=Chiara;cognome=Peruzzi;eta=24";
        
		//inizializza il parser   
        var map = parser.parse(inputString, separator);
        
		//verifica che il risultato sia una mappa non vuota e che abbia dimensione 3
        assertNotNull(map);
        assertFalse(map.isEmpty());
        assertEquals(3, map.size());
        
        //verifica che la mappa contenga le coppie chiave-valore corrette
        assertEquals("Chiara", map.get("nome"));
        assertEquals("Peruzzi", map.get("cognome"));
        assertEquals("24", map.get("eta"));
	}
	
	@Test
	void testParseStringCharArrayNullString() {
		//stringa nulla e array di separatori
		//test di parse(final String str, char[] separators) 
		
        char[] separator = {';'};
        String nullString = null;
        
        //inizializza il parser      
        var map = parser.parse(nullString, separator);
        
        assertNull(nullString);
        
        //verifica che il risultato sia una mappa vuota
        assertNotNull(map);
        assertEquals(0, map.size());
		assertTrue(map.isEmpty());
	}

	@Test
	void testParseStringCharArrayNullSeparators() {
		//stringa e array di separatori nullo
		//test di parse(final String str, char[] separators) 
		
		char[] separatorsNull = null;
        String inputString = "nome=Chiara;cognome=Peruzzi;eta=24";
		
        var map = parser.parse(inputString, separatorsNull);
       
        assertNull(separatorsNull);
        
        //verifica che il risultato sia una mappa vuota
        assertNotNull(map);
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
	}
	
	@Test
	void testParseStringChar() {
		//stringa e carattere separatore
		//test di parse(final String str, char separator) 
		
        char separator = ';';
        String inputString = "nome=Chiara;cognome=Peruzzi;eta=24";
 
        //inizializza il parser  
        var map = parser.parse(inputString, separator);
        
        //verifica che il risultato sia una mappa non vuota e che abbia dimensione 3
        assertNotNull(map);
        assertEquals(3, map.size());
        
        //verifica che la mappa contenga le coppie chiave-valore corrette
        assertEquals("Chiara", map.get("nome"));
        assertEquals("Peruzzi", map.get("cognome"));
        assertEquals("24", map.get("eta"));
	}

	@Test
	void testParseCharArrayChar() {
		//array di caratteri e carattere separatore
		//test di parse(final char[] chars, char separator) 
		
		char separator = '|';
		String inputString = "nome=Chiara|cognome=Peruzzi|eta=24";
		
		//converte la stringa in un array di caratteri
        char[] inputChars = inputString.toCharArray();
 
        //inizializza il parser 
        var map = parser.parse(inputChars, separator);
        
        //verifica che il risultato sia una mappa non vuota e che abbia dimensione 3
        assertNotNull(map);
        assertEquals(3, map.size());
 
        //verifica che la mappa contenga le coppie chiave-valore corrette
        assertEquals("Chiara", map.get("nome"));
        assertEquals("Peruzzi", map.get("cognome"));
        assertEquals("24", map.get("eta"));
	}

	@Test
	void testParseCharArrayIntIntChar() {
		//array di caratteri e carattere separatore
		//test di parse(final char[] chars, int offset, int length, char separator)
		
		char separator = ';';
        String inputString = "nome=Chiara;cognome=Peruzzi;eta=24";

        //converte la stringa in un array di caratteri
        char[] inputChars = inputString.toCharArray();
 
        //offset e lunghezza
        int offset = 0;
        int length = inputChars.length;
 
        //inizializza il parser 
        var map = parser.parse(inputChars, offset, length, separator);
        
        //verifica che il risultato sia una mappa non vuota e che abbia dimensione 3
        assertNotNull(map);
        assertEquals(3, map.size());
 
        //verifica che la mappa contenga le coppie chiave-valore corrette
        assertEquals("Chiara", map.get("nome"));
        assertEquals("Peruzzi", map.get("cognome"));
        assertEquals("24", map.get("eta"));
	}
	
	@Test
	void testParseCharArrayIntIntCharNotSeparator() {
		//caso in cui l'array di caratteri non presenta separatori
		//test di parse(final char[] chars, int offset, int length, char separator)
		
		char separator = ';';
		String inputString = "nomeChiara";
		
        //converte la stringa in un array di caratteri
        char[] inputChars = inputString.toCharArray();
 
        //offset e lunghezza
        int offset = 0;
        int length = inputChars.length;
 
        //inizializza il parser	
        var map = parser.parse(inputChars, offset, length, separator);
 
        //verifica che la mappa sia stata creata
        assertNotNull(map);
        
        //verifica che la mappa non contenga nessuna coppia chiave-valore
        assertNotEquals("Chiara", map.get("nome"));
	}

	@Test
	void testParseCharArrayIntIntCharNullInput() {
		//caso in cui l'array di caratteri è nullo
		//test di parse(final char[] chars, int offset, int length, char separator)
		
		char separator = ';';
        char[] inputChars = null;
 
        //offset e lunghezza
        int offset = 0;
        int length = 0;
 
        //inizializza il parser
        var map = parser.parse(inputChars, offset, length, separator);
        
        assertNull(inputChars);
        
        //verifica che il risultato sia una mappa vuota
        assertNotNull(map);
        assertEquals(0, map.size());
		assertTrue(map.isEmpty());
	}
	
	@Test
	void testParseCharArrayIntIntCharToLowerCase() {
		//caso in cui l'array di caratteri presenta caratteri maiuscoli e minuscoli da convertire
		//test di parse(final char[] chars, int offset, int length, char separator)
		
		char separator = '&';
        String inputString = "NOME=chiara&COGNOME=peruzzi&ETA=24";
        
        //converte la stringa in un array di caratteri
        char[] inputChars = inputString.toCharArray();
 
        //offset e lunghezza
        int offset = 0;
        int length = inputChars.length;  
        
        //imposta lowerCaseNames a true
        parser.setLowerCaseNames(true); 
 
        //inizializza il parser 
        var map = parser.parse(inputChars, offset, length, separator);
        
        //verifica che il risultato sia una mappa non vuota e che abbia dimensione 3
        assertNotNull(map);
        assertEquals(3, map.size());
   
        //verifica che la mappa contenga le coppie chiave-valore corrette
        //verifica che le lettere siano state convertite in minuscolo
        assertEquals("chiara", map.get("nome"));
        assertEquals("peruzzi", map.get("cognome"));
        assertEquals("24", map.get("eta"));
	}
	
	@Test
	void testParseCharsCharNullInput() {

		//caso in cui l'array di caratteri è nullo
		//test di parse(final char[] chars, char separator)
		
		char[] nullChars = null;
		char separator = ';';
	     
		//inizializza il parser
	    var map = parser.parse(nullChars, separator);
	     
	    //verifica che venga creata una nuova mappa e che questa sia vuota
	    assertEquals(new HashMap(), map);
	    assertTrue(map.isEmpty());
	}

	@Test
	void testQuotedString() {
		//array di caratteri che contiene le virgolette
		//test di parse(final char[] chars, char separator)
		
		//input: nome="chiara"
		
		char separator = ';';
        char[] inputChars = new char[] {'n', 'o', 'm', 'e', '=', '"', 'c', 'h', 'i', 'a', 'r', 'a', '"' };

        //inizializza il parser 
        var map = parser.parse(inputChars, separator);
        
        //verifica che venga creata una nuova mappa di dimensione 1
        assertNotNull(map);
        assertEquals(1, map.size());
        
        //verifica che le virgolette siano state rimosse e che la coppia sia corretta
        assertEquals("chiara", map.get("nome"));

	}
	
	@Test
	void testStringWithBackslash() {
		//array di caratteri che contiene i \\
		//test di parse(final char[] chars, char separator)
		
		//input: nome=\\chiara\\
		
		char separator = ';';
		String inputString = "nome=\\Chiara\\";
		
		//converte la stringa in un array di caratteri
		char[] inputChars = inputString.toCharArray();;

		//inizializza il parser 
        var map = parser.parse(inputChars, separator);
        
        //verifica che venga creata una nuova mappa di dimensione 1
        assertNotNull(map);
        assertEquals(1, map.size());
        
        //verifica che la mappa contenga la coppia sia corretta
        assertEquals("\\Chiara\\", map.get("nome"));

	}
	
	@Test
	void testStringWithWhiteSpaces() {
		//array di caratteri che contiene spazi bianchi (all'inizio, in mezzo e alla fine)
		//test di parse(final char[] chars, char separator)
				
		//input:  nome = chiara 
		
		char separator = ';';
		String inputString = " nome = chiara ";
		
		//converte la stringa in un array di caratteri
		char[] inputChars = inputString.toCharArray();;

		//inizializza il parser 
        var map = parser.parse(inputChars, separator);
        
        //verifica che venga creata una nuova mappa di dimensione 1
        assertNotNull(map);
        assertEquals(1, map.size());
        
        //verifica che gli spazi siano stati rimossi e che la coppia sia corretta
        assertEquals("chiara", map.get("nome"));

	}	

}
