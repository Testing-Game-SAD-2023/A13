package student.stefanoAngeloRivielloDue;

import static org.junit.jupiter.api.Assertions.*;
//importato libreria mockito 
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
//Aggiungo tale importazione per poter avere lo stesso tipo passato al mio costruttore 
import java.io.StringReader;

//
import java.io.StreamTokenizer;
//


import ClassUnderTest.ImprovedStreamTokenizer;

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

	
/////////INIZIO TEST->ImprovedStreamTokenizerReader/////////
	@Test
	void testImprovedStreamTokenizerReader() {
		ImprovedStreamTokenizer tokEse2=new ImprovedStreamTokenizer(new StringReader("SWTesting"));
		assertNotNull(tokEse2, "tokEse2 non dovrebbe essere nullo");
	}
	
	
	
/////////	
	
	
/////////INIZIO TEST->ImprovedStreamTokenizerReaderStringStringBoolean/////////
	@Test
	void testImprovedStreamTokenizerReaderStringStringBoolean(){
		String whitespaceConfig=" "; //carattere con spazio
		String quotesConfig=" '\" "; //caratteri citazione
		boolean useSlashSlashCommentsConfig = true;
		
		ImprovedStreamTokenizer tokEse2=new ImprovedStreamTokenizer(new StringReader(""),whitespaceConfig,quotesConfig,useSlashSlashCommentsConfig);
		assertNotNull(tokEse2, "tokEse2 non dovrebbe essere nullo");
			
	}

	
/////////	
	
//NB: NEI TEST DOVE VI E' 'throws IOException' SI POTEVA ANCHE LEVARE E METTERE IN AALTERNATIVA ASSSERT : 'assertDoesNotThrow(() -> tokEse2.METODODACHIAMARE())'
//CONTROLLANDO NEI CASI IN CUI INPUT FOSSE GIUSTO CHE NON VENIVA LANCIATA L'ECCEZIONE 
	
/////////INIZIO TEST->NEXT WORD/////////	
	
	@Test
	void testNextWordBasicWordORQuoteCitazione() throws IOException {
		ImprovedStreamTokenizer tokEse2=new ImprovedStreamTokenizer(new StringReader("SWTesting"));
		//assertDoesNotThrow(() -> tokEse2.nextWord());
		 assertEquals("SWTesting", tokEse2.nextWord());

	}
	
	@Test
	void testNextWordEOF() throws IOException{
		ImprovedStreamTokenizer tokEse2 = new ImprovedStreamTokenizer(new StringReader(""));
		assertDoesNotThrow(() -> tokEse2.nextWord());//o assertNull(tokEse2.nextWord());
	}
	

	@Test
	public void testNextWordException(){
       
        ImprovedStreamTokenizer tokEse2 = new ImprovedStreamTokenizer(new StringReader("'EseSWTesting'"));
        IOException exception = assertThrows(IOException.class, () -> tokEse2.nextWord());
        assertEquals("non-string", exception.getMessage());
       
	}
	
	
/////////INIZIO TEST->NEXT INTEGER/////////	
	@Test
	void testNextIntegerEOF() throws IOException {
		ImprovedStreamTokenizer tokEse2 = new ImprovedStreamTokenizer(new StringReader(""));
		//assertDoesNotThrow(() -> tokEse2.nextInteger());
		assertNull(tokEse2.nextInteger());
      
	}
	
	
	@Test
	void testNextIntegerTTWord() throws IOException {
		ImprovedStreamTokenizer tokEse2=new ImprovedStreamTokenizer(new StringReader("22"));
		//assertDoesNotThrow(() -> tokEse2.nextInteger());
		assertEquals(Integer.valueOf(22), tokEse2.nextInteger());
	}
	
	
	
	@Test
	public void testNextIntegerException(){
       
        ImprovedStreamTokenizer tokEse2 = new ImprovedStreamTokenizer(new StringReader("'#'"));
        IOException exception = assertThrows(IOException.class, () -> tokEse2.nextInteger());
        assertEquals("non-number", exception.getMessage());
    }
	
	
/////////
	
	
/////////INIZIO TEST->NEXT INT/////////	
	@Test
	void testNextInt() throws IOException {
		ImprovedStreamTokenizer tokEse2=new ImprovedStreamTokenizer(new StringReader("22"));
		//oppure assertDoesNotThrow(() -> tokEse2.nextInt());
		int result = tokEse2.nextInt();
        assertEquals(22, result);    
	}
	
	@Test
    public void testNextIntNullException(){
        ImprovedStreamTokenizer tokEse2 = new ImprovedStreamTokenizer(new StringReader(""));
        IOException exception = assertThrows(IOException.class, () -> tokEse2.nextInt());
        assertEquals("unexpected end of input", exception.getMessage());
        //oppure assertNull(tokEse2.nextInt());
       }
    ///chiamando nextToken e passandogli uno stream vuoto esso vede la EOF e quindi non restituisce niente perchè è vuoto , null
	
/////////
	
	
	
/////////INIZIO TEST->NEXT BOOLEAN/////////		
	@Test
	void testNextBooleanEOF() throws IOException {
		 ImprovedStreamTokenizer tokEse2 = new ImprovedStreamTokenizer(new StringReader(""));
		 assertNull(tokEse2.nextBoolean());
	}
	@Test
	void testNextBooleanWord() throws IOException {
		ImprovedStreamTokenizer tokEse2=new ImprovedStreamTokenizer(new StringReader("true"));
		 assertTrue(tokEse2.nextBoolean());
		 
	}
	
	
	@Test
	void testNextBooleanNumberZero() throws IOException {
		ImprovedStreamTokenizer tokEse2=new ImprovedStreamTokenizer(new StringReader("0"));
		assertFalse(tokEse2.nextBoolean());
		
	}
	
	@Test
	public void testNextBooleanWithNonZeroNumber() throws IOException {
	    ImprovedStreamTokenizer tok = new ImprovedStreamTokenizer(new StringReader("22"));
	    Boolean result = tok.nextBoolean();
	    //assertEquals(true, result);
	}

	
	@Test
	void testNextBooleanException(){
		ImprovedStreamTokenizer tokEse2=new ImprovedStreamTokenizer(new StringReader("'"));
		IOException exception = assertThrows(IOException.class, () -> tokEse2.nextBoolean());
        assertEquals("non-boolean", exception.getMessage());
	}
	
	
	
	
/////////
	

	
/////////INIZIO TEST->NEXT BOOL/////////	
	
	@Test
	void testNextBool() throws IOException {
		ImprovedStreamTokenizer tokEse2=new ImprovedStreamTokenizer(new StringReader("true"));
		boolean result = tokEse2.nextBool();
		assertEquals(true, result);//oppure assertTrue(tokEse2.nextBool());
		
	}
	@Test
	void testNextBoolNull() {
		ImprovedStreamTokenizer tokEse2=new ImprovedStreamTokenizer(new StringReader(""));
		 assertThrows(IOException.class, () -> tokEse2.nextBool());
	}
	
	
/////////	
	
	
	
/////////INIZIO TEST->NEXT BYTE OBJECT/////////		
	@Test
	void testNextByteObjectEOF() throws IOException {
		ImprovedStreamTokenizer tokEse2 = new ImprovedStreamTokenizer(new StringReader(""));
		assertNull(tokEse2.nextByteObject());
	}
	
	@Test
	void testNextByteObjectWORD() throws IOException {
		ImprovedStreamTokenizer tokEse2 = new ImprovedStreamTokenizer(new StringReader("1A"));
		//verifico non nullo perchè il metodo parse invocato se ho una word mi converto la word da esadecimale in decimale
		 assertEquals((byte) 0x1A, tokEse2.nextByteObject());
	    
	}

	
	@Test
	void testNextByteObjectException(){
		ImprovedStreamTokenizer tokEse2=new ImprovedStreamTokenizer(new StringReader("'"));
		IOException exception = assertThrows(IOException.class, () -> tokEse2.nextByteObject());
        assertEquals("non-byte", exception.getMessage());
	}
	
/////////

/////////INIZIO TEST->NEXT BYTE /////////		
	@Test
	void testNextByte() throws IOException {
		ImprovedStreamTokenizer tokEse2 = new ImprovedStreamTokenizer(new StringReader("1A"));
		assertEquals(26,tokEse2.nextByte());
		
	}	
	
/////////
	
	
/////////TESTATO GIà TRAMITE testNextByteObject DATO CHE LO RICHIAMA /////////
	

	/*@Test
	void testParseByte() {
		assertEquals((byte) 0x1A, ImprovedStreamTokenizer.parseByte("1A"));;
	}*/
	
/////////
	
	
	
/////////INIZIO TEST->CHAR TO HEX/////////	 funzione converte il carattere ('A') nell'alfabeto esadecimale, nel suo valore numerico corrispondente(10)
	
	@Test
    public void testCharToHexCase1() {
        assertEquals(1, ImprovedStreamTokenizer.charToHex('1')); // valido nel range  '0' - '9' né 'A' - 'F',
    }
	
	@Test
    public void testCharToHexCase2() {
        assertNotEquals(35, ImprovedStreamTokenizer.charToHex('#'));// non valido nel range  '0' - '9' né 'A' - 'F',
    }
	
	
	@Test
    public void testCharToHexCase3() {
        assertEquals(10, ImprovedStreamTokenizer.charToHex('A'));// valido in uno dei range  '0' - '9' né 'A' - 'F',
    }
	
	
	
	
/////////GIA TESTATI CON ALTRI METODI CHE LI INVOCAVANO A SUA VOLTA /////////
	
	
	//TESTATO GIà TRAMITE ImprovedStreamTokenizerREADER E ImprovedStreamTokenizerReadeStringStringboolean  DATO CHE LI RICHIAMA 
	
	
	/*@Test
	void testInitializeSyntax() throws IOException {
		ImprovedStreamTokenizer tokEse2 = new ImprovedStreamTokenizer(new StringReader("SW TESTING"));

        tokEse2.initializeSyntax();

        assertEquals(StreamTokenizer.TT_WORD, tokEse2.nextToken());
        assertEquals(StreamTokenizer.TT_WORD, tokEse2.nextToken());
        assertEquals(StreamTokenizer.TT_EOF, tokEse2.nextToken());;
	}*/

	/*@Test
    void testWhiteSpaceCharactersInt() throws IOException {
		 ImprovedStreamTokenizer tokEse2 = new ImprovedStreamTokenizer(new StringReader("\t"));

		    // riconoscimento del carattere di spazio bianco
		 	tokEse2.whiteSpaceCharacter('\t');

		    
		    assertEquals(StreamTokenizer.TT_EOF, tokEse2.nextToken());
    
    }*/



	/*@Test
	void testWhiteSpaceCharacterString() throws IOException {
		ImprovedStreamTokenizer tokEse2 = new ImprovedStreamTokenizer(new StringReader(" SW TESTING"));
		
		tokEse2.whiteSpaceCharacters(" ");
		
		assertEquals(StreamTokenizer.TT_WORD, tokEse2.nextToken());
		assertEquals("SW", tokEse2.sval);
		
		assertEquals(StreamTokenizer.TT_WORD, tokEse2.nextToken());
        assertEquals("TESTING", tokEse2.sval);

           
        assertEquals(StreamTokenizer.TT_EOF, tokEse2.nextToken());
		
		
	}*/



	/*
	@Test
	  void testQuoteCharacters() throws IOException {
		 ImprovedStreamTokenizer tokEse2 = new ImprovedStreamTokenizer(new StringReader("'"));

	     // riconoscimento dei caratteri di citazione
		 tokEse2.quoteCharacters("'");

	      // Verifica  token successivo sia di tipo citazione
	      assertEquals('\'', tokEse2.nextToken());
	    }
	
	*/
	/*@Test
	void testInitializeSyntaxStringStringBoolean() throws IOException {
		ImprovedStreamTokenizer tokEse2 = new ImprovedStreamTokenizer(new StringReader("SW TESTING"));

        // Chiamo metodo initializeSyntax con  caratteristiche specifiche 
        tokEse2.initializeSyntax(" \t\n\r", "\"'", true);

       
        assertEquals(StreamTokenizer.TT_WORD, tokEse2.nextToken()); 
        assertEquals(StreamTokenizer.TT_WORD, tokEse2.nextToken());  
        assertEquals(StreamTokenizer.TT_EOF, tokEse2.nextToken());   
	}*/

	//MIAO
/////////	
	
}
