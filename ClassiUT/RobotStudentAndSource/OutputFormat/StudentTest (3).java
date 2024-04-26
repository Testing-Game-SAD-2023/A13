package student.RosilenaCarannanteDue;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.OutputFormat;

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
	void testOutputFormat() {
		//Test case per la creazione di un'istanza di outputformat con il costruttore
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
		
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertEquals che indent sia pari a null
		assertEquals(null, outputformat.getIndent());
		//Verifico con un assertFalse che newlines sia false
		assertFalse(outputformat.isNewlines());
		//Verifico con un asserEquals che encoding sia pari a UTF-8
		assertEquals("UTF-8", outputformat.getEncoding());
	}

	@Test
	void testOutputFormatString() {
		//Test case per la creazione di un'istanza di outputformat con il costruttore
		// Creo un'istanza di OutputFormat con indent pari a /t (carattere di tabulazione)
		OutputFormat outputformat = new OutputFormat("/t");
		
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertEquals che indent sia pari a /t
		assertEquals("/t", outputformat.getIndent());
		//Verifico con un assertFalse che newlines sia false
		assertFalse(outputformat.isNewlines());
		//Verifico con un asserEquals che encoding sia pari a UTF-8
		assertEquals("UTF-8", outputformat.getEncoding());
		
	}

	@Test
	void testOutputFormatStringBoolean() {
		//Test case per la creazione di un'istanza di outputformat con il costruttore
		// Creo un'istanza di OutputFormat con indent pari a /t (carattere di tabulazione) e newlines pari a true
		OutputFormat outputformat = new OutputFormat("/t", true);
		
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertEquals che indent sia pari a /t
		assertEquals("/t", outputformat.getIndent());
		//Verifico con un assertTrue che newlines sia true
		assertTrue(outputformat.isNewlines());
		//Verifico con un asserEquals che encoding sia pari a UTF-8
		assertEquals("UTF-8", outputformat.getEncoding());
	}

	@Test
	void testOutputFormatStringBooleanString() {
		//Test case per la creazione di un'istanza di outputformat con il costruttore
		// Creo un'istanza di OutputFormat con indent pari a /t (carattere di tabulazione) e newlines pari a true
		OutputFormat outputformat = new OutputFormat("/t", true, "UTF-16");
				
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertEquals che indent sia pari a /t
		assertEquals("/t", outputformat.getIndent());
		//Verifico con un assertTrue che newlines sia true
		assertTrue(outputformat.isNewlines());
		//Verifico con un asserEquals che encoding sia pari a UTF-16
		assertEquals("UTF-16", outputformat.getEncoding());
	}

	@Test
	void testGetLineSeparator() {
		//Test case per il metodo getLineSeparator
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
		
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertEquals che lineSeparator sia pari a \n
		assertEquals("\n", outputformat.getLineSeparator());
	}

	@Test
	void testSetLineSeparator() {
		//Test case per il metodo setLineSeparator
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get e set
		//Setto il valore di lineSeparator pari a \r
		outputformat.setLineSeparator("\r");
		//Verifico con un assertEquals che lineSeparator sia pari a \r
		assertEquals("\r", outputformat.getLineSeparator());
	}

	@Test
	void testIsNewlines() {
		//Test case per il metodo isNewlines
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
		
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertFalse che newlines sia false
		assertFalse(outputformat.isNewlines());
	}

	@Test
	void testSetNewlines() {
		//Test case per il metodo setNewlines
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get e set
		//Setto il valore di newlines pari a true
		outputformat.setNewlines(true);
		//Verifico con un assertTrue che newlines sia pari a true
		assertTrue(outputformat.isNewlines());
	}

	@Test
	void testGetEncoding() {
		//Test case per il metodo getEncoding
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
				
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un asserEquals che encoding sia pari a UTF-8
		assertEquals("UTF-8", outputformat.getEncoding());
	}

	@Test
	void testSetEncoding() {
		//Test case per il metodo setEncoding
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
						
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get e set
		//Setto il valore di encoding pari a UTF-16
		outputformat.setEncoding("UTF-16");
		//Verifico con un asserEquals che encoding sia pari a UTF-8
		assertEquals("UTF-16", outputformat.getEncoding());
		
		//Dato che nel metodo setEncoding setto encoding solo se la stringa non è null
		//Passo come parametro alla funzione una stringa pari a null
		outputformat.setEncoding(null);
		//E con un assertNotEquals verifico che la stringa non sia stata settata pari a null
		assertNotEquals(null, outputformat.getEncoding());
	}

	@Test
	void testIsOmitEncoding() {
		//Test case per il metodo isOmitEncoding
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
				
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertFalse che omitEncoding sia false
		assertFalse(outputformat.isOmitEncoding());
	}

	@Test
	void testSetOmitEncoding() {
		//Test case per il metodo setOmitEncoding
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
				
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get e set
		//Setto il valore di omitEncoding pari a true
		outputformat.setOmitEncoding(true);
		//Verifico con un assertTrue che omitEncoding sia pari a true
		assertTrue(outputformat.isOmitEncoding());
	}

	@Test
	void testSetSuppressDeclaration() {
		//Test case per il metodo setSuppressDeclaration
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
						
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get e set
		//Setto il valore di suppressDeclaration pari a true
		outputformat.setSuppressDeclaration(true);
		//Verifico con un assertTrue che suppressDeclaration sia pari a true
		assertTrue(outputformat.isSuppressDeclaration());
	}

	@Test
	void testIsSuppressDeclaration() {
		//Test case per il metodo isSuppressDeclaration
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
						
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertFalse che suppressDeclaration sia false
		assertFalse(outputformat.isSuppressDeclaration());
	}

	@Test
	void testSetNewLineAfterDeclaration() {
		//Test case per il metodo setNewLineAfterDeclaration
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
								
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get e set
		//Setto il valore di newLineAfterDeclaration pari a false
		outputformat.setNewLineAfterDeclaration(false);
		//Verifico con un assertFalse che newLineAfterDeclaration sia pari a false
		assertFalse(outputformat.isNewLineAfterDeclaration());
	}

	@Test
	void testIsNewLineAfterDeclaration() {
		//Test case per il metodo isNewLineAfterDeclaration
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
								
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertTrue che newLineAfterDeclaration sia true
		assertTrue(outputformat.isNewLineAfterDeclaration());
	}

	@Test
	void testIsExpandEmptyElements() {
		//Test case per il metodo isExpandEmptyElements
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
								
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertFalse che expandEmptyElements sia false
		assertFalse(outputformat.isExpandEmptyElements());
	}

	@Test
	void testSetExpandEmptyElements() {
		//Test case per il metodo setExpandEmptyElements
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
										
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get e set
		//Setto il valore di expandEmptyElements pari a true
		outputformat.setExpandEmptyElements(true);
		//Verifico con un assertTrue che expandEmptyElements sia pari a true
		assertTrue(outputformat.isExpandEmptyElements());
	}

	@Test
	void testIsTrimText() {
		//Test case per il metodo isTrimText
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
										
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertFalse che trimText sia false
		assertFalse(outputformat.isTrimText());
	}

	@Test
	void testSetTrimText() {
		//Test case per il metodo setTrimText
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
												
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get e set
		//Setto il valore di trimText pari a true
		outputformat.setTrimText(true);
		//Verifico con un assertTrue che trimText sia pari a true
		assertTrue(outputformat.isTrimText());
	}

	@Test
	void testIsPadText() {
		//Test case per il metodo isPadText
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
												
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertFalse che padText sia false
		assertFalse(outputformat.isPadText());
	}

	@Test
	void testSetPadText() {
		//Test case per il metodo setPadText
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
												
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get e set
		//Setto il valore di padText pari a true
		outputformat.setPadText(true);
		//Verifico con un assertTrue che padText sia pari a true
		assertTrue(outputformat.isPadText());
	}

	@Test
	void testGetIndent() {
		//Test case per il metodo getIndent
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
		
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertEquals che indent sia pari a null
		assertEquals(null, outputformat.getIndent());
	}

	@Test
	void testSetIndentString() {
		//Test case per il metodo setIndentString
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
				
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get e set
		
		//Test che soddisfa le condizioni dell'if
		//Setto il valore di indent pari ad una stringa vuota
		outputformat.setIndent("");
		//Verifico con un assertEquals che indent sia pari a null
		assertEquals(null, outputformat.getIndent());
		
		//Test che soddisfa le condizioni dell'if
		//Setto il valore di indent pari a /t
		outputformat.setIndent("/t");
		//Verifico con un assertEquals che indent sia pari a /t
		assertEquals("/t", outputformat.getIndent());
		
		//Test che non soddisfa le condizioni dell'if
		//Setto il valore di indent pari a null
		outputformat.setIndent(null);
		//Verifico con un assertEquals che indent sia pari a null
		assertEquals(null, outputformat.getIndent());
	}

	@Test
	void testSetIndentBoolean() {
		//Test case per il metodo setIndentBoolean
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
						
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get e set
				
		//Test che soddisfa le condizioni dell'if
		//Setto il valore di indent pari a true
		outputformat.setIndent(true);
		//Verifico con un assertEquals che indent sia pari al valore della stringa STANDARD_INDENT 
		assertEquals("  ", outputformat.getIndent());
				
		//Test che non soddisfa le condizioni dell'if
		//Setto il valore di indent pari a false
		outputformat.setIndent(false);
		//Verifico con un assertEquals che indent sia pari a null
		assertEquals(null, outputformat.getIndent());
	}

	@Test
	void testSetIndentSize() {
		//Test case per il metodo setIndentSize
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
								
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get e set
						
		//Setto il valore di indentSize pari a 3
		outputformat.setIndentSize(3);
		//Verifico con un assertEquals che indent sia pari a 3 spazi vuoti 
		assertEquals("   ", outputformat.getIndent());
	}

	@Test
	void testIsXHTML() {
		//Test case per il metodo isXHTML
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
														
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertFalse che doXHTML sia false
		assertFalse(outputformat.isXHTML());
	}

	@Test
	void testSetXHTML() {
		//Test case per il metodo setXHTML
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
														
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get e set
		//Setto il valore di doXHTML pari a true
		outputformat.setXHTML(true);
		//Verifico con un assertTrue che doXHTMLt sia pari a true
		assertTrue(outputformat.isXHTML());
	}

	@Test
	void testGetNewLineAfterNTags() {
		//Test case per il metodo getNewLineAfterNTags
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
		
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertEquals che newLineAfterNTags sia pari a 0
		assertEquals(0, outputformat.getNewLineAfterNTags());
	}

	@Test
	void testSetNewLineAfterNTags() {
		//Test case per il metodo setNewLineAfterNTags
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
		
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get e set
		//Setto il valore di newLineAfterNTags pari a 8
		outputformat.setNewLineAfterNTags(8);
		//Verifico con un assertEquals che newLineAfterNTags sia pari a 8
		assertEquals(8, outputformat.getNewLineAfterNTags());
	}

	@Test
	void testGetAttributeQuoteCharacter() {
		//Test case per il metodo getAttributeQuoteCharacter
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
		
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertEquals che attributeQuoteCharacter sia pari a \"
		assertEquals('\"', outputformat.getAttributeQuoteCharacter());
	}

	@Test
	void testSetAttributeQuoteCharacter() {
		//Test case per il metodo setAttributeQuoteCharacter
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
				
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get e set
		//Effettuo due test che soddisfano le condizioni dell'if
		//Setto il valore di attributeQuoteCharacter pari a \'
		outputformat.setAttributeQuoteCharacter('\'');
		//Verifico con un assertEquals che attributeQuoteCharacter sia pari a \'
		assertEquals('\'', outputformat.getAttributeQuoteCharacter());
		
		//Setto il valore di attributeQuoteCharacter pari a "
		outputformat.setAttributeQuoteCharacter('"');
		//Verifico con un assertEquals che attributeQuoteCharacter sia pari a "
		assertEquals('"', outputformat.getAttributeQuoteCharacter());
		
		//Test che non soddisfa le condizioni dell'if e quindi crea l'eccezione
		
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> outputformat.setAttributeQuoteCharacter('@'));

        assertEquals("Invalid attribute quote character (@)", exception.getMessage());
	}
	


	@Test
	void testParseOptions() {
		//Test case per il metodo setAttributeQuoteCharacter
		// Creo un'istanza di OutputFormat
		OutputFormat outputformat = new OutputFormat();
		//Dato che all'interno degli if viene incrementato il valore dell'indice i devo effettuare due array di stringhe per testare tutti gli if
		String[] args = {"-suppressDeclaration", "-omitEncoding", "-indent", "-indentSize", "-expandEmpty", "-encoding", 
				"-newlines","-lineSeparator","-trimText","-padText","-xhtml"};

		assertEquals(11,outputformat.parseOptions(args,0));
		
		String[] args2 = {"-newlines","-trimText","-indentSize","5","fail"};
		
		assertEquals(4,outputformat.parseOptions(args2,0));
	}

	@Test
	void testCreatePrettyPrint() {
		//Test case per il metodo createPrettyPrint
		// Creo un'istanza di OutputFormat con il metodo createPrettyPrint
		OutputFormat outputformat = OutputFormat.createPrettyPrint();
		
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertEquals che indent sia pari a 2 spazi vuoti
		assertEquals("  ",outputformat.getIndent());
		//Verifico con un assertTrue che newLines sia true
		assertTrue(outputformat.isNewlines());
		//Verifico con un assertTrue che trimText sia true
		assertTrue(outputformat.isTrimText());
		//Verifico con un assertTrue che padText sia true
		assertTrue(outputformat.isPadText());
		
	}

	@Test
	void testCreateCompactFormat() {
		//Test case per il metodo createCompactFormat
		// Creo un'istanza di OutputFormat con il metodo createCompactFormat
		OutputFormat outputformat = OutputFormat.createCompactFormat();
		
		//Dato che gli attributi della classe sono privati utilizzo le apposite funzioni get
		//Verifico con un assertEquals che indent sia pari a null
		assertEquals(null, outputformat.getIndent());
		//Verifico con un assertFalse che newLines sia false
		assertFalse(outputformat.isNewlines());
		//Verifico con un assertTrue che trimText sia true
		assertTrue(outputformat.isTrimText());
	}

}
