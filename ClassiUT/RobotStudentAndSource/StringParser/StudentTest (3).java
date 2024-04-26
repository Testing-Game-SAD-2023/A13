package student.salvatoreDellaRagioneDue;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.StringParser;

class StudentTest extends StringParser{

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		StringParser s = new StringParser();
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
    public void testBuildString_IncompleteUnicodeSequence() {
        CharSequence str = "This is a \\u1 test string"; // Incomplete Unicode sequence 

        String result = StringParser.buildString(str, false);

        assertEquals("This is a \\u1 test string", result); // Assert that the sequence remains unchanged
    }
	
	 @Test
	    public void testBuildString_NonHexCharacter() {
	        CharSequence str = "This is a \\uZ123 test string"; // 'Z' is not within 'a' to 'h' range

	        String result = StringParser.buildString(str, false);

	        assertEquals("This is a \\uZ123 test string", result);
	        // Assert that the sequence remains unchanged as 'Z' is not a valid character within Unicode escape
	}
	 
	 @Test
	    public void testBuildString_ASCIICharacter() {
	        CharSequence str = "This is a test string with an ASCII character: A"; // 'A' is an ASCII character

	        String result = StringParser.buildString(str, false);

	        assertEquals("This is a test string with an ASCII character: A", result);
	        // Assert that 'A' is preserved as it is an ASCII character
	    }
	 
	@Test
	void testStringParser() {
		// Definisci una stringa di input
        String inputString = "Example String";

        // Chiamata al metodo che vuoi testare
        String result = StringParser.buildString(inputString, false);

        // Assert per verificare che il risultato sia corretto
        assertEquals("Example String", result);
		//fail("Not yet implemented");
	}

	@Test
	void testBuildString() {
		 // Definisci la stringa di input e il risultato atteso
        String inputString = "Example String";
        String expectedResult = "Example String"; // Sostituisci con il risultato atteso

        // Chiamata al metodo che vuoi testare
        String result = StringParser.buildString(inputString, false);

        // Assert per verificare che il risultato sia corretto
        assertEquals(expectedResult, result);
		//fail("Not yet implemented");
	}

	@Test
	void testReadString() {
		 // Definisci una stringa di input con caratteri di escape e un separatore specifico
        String inputString = "This is a 'test' string";

        // Prepara uno StringBuilder per raccogliere l'output
        StringBuilder resultBuilder = new StringBuilder();

        // Chiamata al metodo readString con il separatore specifico '
        int endIndex = StringParser.readString(resultBuilder, inputString, 0, '\'');

        // Ottieni il risultato come stringa
        String result = resultBuilder.toString();

        // Assert per verificare che il risultato sia corretto
        assertEquals("This is a '", result);
        assertEquals(10, endIndex); // Verifica l'indice finale del separatore
		//fail("Not yet implemented");
	}
	
	@Test
    void testReadStringWithEmptyInput() {
		// Verifica che la funzione gestisca correttamente l'input vuoto
        StringBuilder result = new StringBuilder();
        int offset = StringParser.readString(result, "", 0, '\'');
        assertEquals(0, offset);// Verifica che l'offset sia 0
        assertEquals("", result.toString());// Verifica che il risultato sia una stringa vuota
    }

    @Test
    void testReadStringWithoutEscapes() {
    	// Verifica che la funzione gestisca correttamente una stringa senza escape characters
        StringBuilder result = new StringBuilder();
        int offset = StringParser.readString(result, "TestString", 0, '\'');
        assertEquals(10, offset);// Verifica che l'offset sia 10
        assertEquals("TestString", result.toString());// Verifica che il risultato sia la stessa stringa di input
    }

    @Test
    void testReadStringWithEscapes() {
    	// Verifica che la funzione di escape gestisca correttamente una stringa con escape characters
        StringBuilder result = new StringBuilder();
        int offset = StringParser.readString(result, "\\t\\n\\'Test\\\"\\", 0, '\'');
        assertEquals(13, offset);
        assertEquals("\\t\\n'Test\\\"", result.toString());// Verifica che l'output sia come previsto
    }
    
    
    @Test
    void testReadStringWithUnicodeEscape() {
    	// Test per una stringa di input con sequenze di escape
        StringBuilder result = new StringBuilder();
        int offset = StringParser.readString(result, "\u0041\u0042\u0043", 0, '\"');
        assertEquals(3, offset);// Verifica che l'offset sia la lunghezza della stringa di input con escape
        assertEquals("ABC", result.toString());// Verifica che la stringa risultante contenga le sequenze di escape
    }

    @Test
    void testReadStringWithSeparator() {
    	// Test per una stringa di input con un separatore
        StringBuilder result = new StringBuilder();
        int offset = StringParser.readString(result, "StringWith'Separator'", 0, '\'');
        assertEquals(10, offset);// Verifica che l'offset sia la posizione del separatore
        assertEquals("StringWith'", result.toString());// Verifica che la stringa risultante contenga il separatore e i caratteri precedenti
    }

    @Test
    void testReadStringWithEmptySeparator() {
    	// Test per una stringa di input con un separatore vuoto
        StringBuilder result = new StringBuilder();
        int offset = StringParser.readString(result, "StringWithoutSeparator", 0, '\0');
        assertEquals(22, offset);// Verifica che l'offset sia la lunghezza della stringa di input
        assertEquals("StringWithoutSeparator", result.toString());// Verifica che la stringa risultante sia identica alla stringa di input
    }
	
	

	@Test
	void testEscapeString() {
		// Test per la funzione escapeString con una stringa di input contenente sequenze di escape
		String inputString = "\\\b\f\n\r\t\'\"";
        char delim = '"';
        
        String escapedString = StringParser.escapeString(inputString, delim);

        String expectedResult ="\\\\\\b\\f\\n\\r\\t\\\'\"";
        assertEquals(expectedResult, escapedString);
		//fail("Not yet implemented");
	}
	
	
	
	@Test
	void testEscapeString2() {
		// Test per la funzione escapeString con una stringa di input null
		String inputString = null;
        char delim = '"';
        
        String escapedString = StringParser.escapeString(inputString, delim);

        String expectedResult = null;
        assertEquals(expectedResult, escapedString);
		//fail("Not yet implemented");
	}
	


	
	@Test
	void testEscapeString4() {
		// Test per la funzione escapeString con una stringa di input contenente caratteri non ASCII
		String inputString = "© ® € ¥";
        char delim = '"';
        String escapedString = StringParser.escapeString(inputString, delim);
        String expected = "\u00A9 \u00AE \u20AC \u00A5";
        assertEquals(expected, escapedString);
		//fail("Not yet implemented");
	}
	
	
	
	@Test
	void testEscapeString5() {
		// Test per la funzione escapeString con un carattere nullo nella stringa di input
		char nullChar = '\0';
	    char delim = '"';
	    
	    String inputString = "Test with null character:" + nullChar ;
	    String expectedOutput = "\"Test with null character:\"";

	    String escapedString = StringParser.escapeString(inputString, delim);

	    assertEquals(expectedOutput, escapedString);
	}
	
	// Test per una sequenza Unicode valida in minuscolo
    @Test
    public void testReadUnicodeChar_ValidHexDigits_Lowercase() {
        StringBuilder strb = new StringBuilder();
        CharSequence str = "\\u0061"; // Unicode per 'a' minuscola
        int result = StringParser.readString(strb, str, 0,'"');
        assertEquals(6, result); // Verifica che siano stati letti 4 caratteri
        assertEquals('a', strb.charAt(0)); // Verifica che il carattere letto sia 'a'
    }
    
    @Test
    public void testReadUnicodeChar_ValidHexDigits_Lowercase2() {
        StringBuilder strb = new StringBuilder();
        CharSequence str = "r"; // Unicode per 'a' minuscola
        int result = StringParser.readString(strb, str, 0,'"');
        assertEquals(1, result); // Verifica che siano stati letti 4 caratteri
        assertEquals('r', strb.charAt(0)); // Verifica che il carattere non sia letto
    }
    
 

    // Test per una sequenza Unicode valida in maiuscolo
    @Test
    public void testReadUnicodeChar_ValidHexDigits_Uppercase() {
        StringBuilder strb = new StringBuilder();
        CharSequence str = "\\u0042"; // Unicode per 'B' maiuscola
        int result = StringParser.readString(strb, str, 0,'"');
        assertEquals(6, result); // Verifica che siano stati letti 4 caratteri
        assertEquals('B', strb.charAt(0)); // Verifica che il carattere letto sia 'B'
    }
	
	
	
	// Test per una sequenza Unicode con escape sequence non valida
    @Test
    public void testReadUnicodeChar_InvalidEscapeSequence() {
        StringBuilder strb = new StringBuilder();
        CharSequence str = "\\u006x"; // Escape sequence non valida
        int result = StringParser.readString(strb, str, 0,'"');
        assertEquals(6, result); // Verifica che non siano stati letti caratteri
        assertEquals(6, strb.length()); // Verifica che non siano stati aggiunti caratteri al StringBuilder
    }
    
    //stessa cosa del test sopra
    @Test
    public void testReadUnicodeChar_InvalidHexDigits() {
        StringBuilder strb = new StringBuilder();
        CharSequence str = "\\u00G1"; // Invalid hex digit 'G'
        int result = StringParser.readString(strb, str, 0,'"');
        assertEquals(6, result);
        assertEquals(1,strb.length());
    }
    
    
}
