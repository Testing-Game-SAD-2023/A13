package student.andreaDiMarcoUno;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

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

	
	@Test																//Per i test sui costruttori controllo che l'oggetto sia creato correttamente
	void testOutputFormat() {
		OutputFormat format = new OutputFormat();
		assertFalse(format.isSuppressDeclaration());
		assertTrue(format.isNewLineAfterDeclaration());
		assertEquals("UTF-8",format.getEncoding());
		assertFalse(format.isOmitEncoding());
		assertNull(format.getIndent());
		assertFalse(format.isExpandEmptyElements());
		assertFalse(format.isNewlines());
		assertEquals("\n",format.getLineSeparator());
		assertFalse(format.isTrimText());
		assertFalse(format.isPadText());
		assertFalse(format.isXHTML());
		assertEquals(0,format.getNewLineAfterNTags());
		assertEquals('\"',format.getAttributeQuoteCharacter());
	}

	String indent = " ";
	@Test
	void testOutputFormatString() {
		OutputFormat format = new OutputFormat(indent);
		assertFalse(format.isSuppressDeclaration());
		assertTrue(format.isNewLineAfterDeclaration());
		assertEquals("UTF-8",format.getEncoding());
		assertFalse(format.isOmitEncoding());
		assertEquals(indent,format.getIndent());			//indent verrà popolata dall'argomento che passo al costruttore se la funzione è corretta
		assertFalse(format.isExpandEmptyElements());
		assertFalse(format.isNewlines());				
		assertEquals("\n",format.getLineSeparator());
		assertFalse(format.isTrimText());
		assertFalse(format.isPadText());
		assertFalse(format.isXHTML());
		assertEquals(0,format.getNewLineAfterNTags());
		assertEquals('\"',format.getAttributeQuoteCharacter());
	}

	@Test
	void testOutputFormatStringBoolean() {
		OutputFormat format = new OutputFormat(indent,true);
		assertFalse(format.isSuppressDeclaration());
		assertTrue(format.isNewLineAfterDeclaration());
		assertEquals("UTF-8",format.getEncoding());
		assertFalse(format.isOmitEncoding());
		assertEquals(indent,format.getIndent());
		assertFalse(format.isExpandEmptyElements());
		assertTrue(format.isNewlines());				//newLines diventa true perchè glielo passo negli argomenti della funzione
		assertEquals("\n",format.getLineSeparator());
		assertFalse(format.isTrimText());
		assertFalse(format.isPadText());
		assertFalse(format.isXHTML());
		assertEquals(0,format.getNewLineAfterNTags());
		assertEquals('\"',format.getAttributeQuoteCharacter());
	}

	@Test
	void testOutputFormatStringBooleanString() {
		OutputFormat format = new OutputFormat(indent,true,"ISO-88");
		assertFalse(format.isSuppressDeclaration());
		assertTrue(format.isNewLineAfterDeclaration());
		assertEquals("ISO-88",format.getEncoding());
		assertFalse(format.isOmitEncoding());
		assertEquals(indent,format.getIndent());
		assertFalse(format.isExpandEmptyElements());
		assertTrue(format.isNewlines());			
		assertEquals("\n",format.getLineSeparator());
		assertFalse(format.isTrimText());
		assertFalse(format.isPadText());
		assertFalse(format.isXHTML());
		assertEquals(0,format.getNewLineAfterNTags());
		assertEquals('\"',format.getAttributeQuoteCharacter());
	}

	String separ = "\r";
	@Test
	void testGetLineSeparator() {
		OutputFormat format = new OutputFormat(separ);
		assertEquals("\n",format.getLineSeparator());
	}
	
	@Test
	void testSetLineSeparator() {
		OutputFormat format = new OutputFormat();
		format.setLineSeparator(separ);
		assertEquals(separ,format.getLineSeparator());
	}
	
	
	@Test
	void testIsNewlines() {
		OutputFormat format = new OutputFormat();
		assertFalse(format.isNewlines());
	}
	
	boolean newLines = true;
	String str = "Questo documento contiene varie informazioni";
	@Test
	void testIsNewlinesCorrect() {
		OutputFormat format = new OutputFormat(str,newLines);
		assertTrue(format.isNewlines());
	}
	
	@Test
	void testSetNewlines() {
		OutputFormat format = new OutputFormat();
		format.setNewlines(newLines);
		assertEquals(newLines,format.isNewlines());
	}

	String encod = "UTF";
	@Test
	void testGetEncoding() {
		OutputFormat format = new OutputFormat(str,newLines,encod);
		assertEquals("UTF",format.getEncoding());
	}
	
	
	@Test
	void testSetEncoding() {
		OutputFormat format = new OutputFormat();
		format.setEncoding(encod);
		assertEquals(encod,format.getEncoding());
	}
	

	@Test
	void testSetEncoding2() {
		OutputFormat format = new OutputFormat();
		format.setEncoding(null);
	}
	
	@Test
	void testIsOmitEncoding() {
		OutputFormat format = new OutputFormat();
		assertFalse(format.isOmitEncoding());
	}

	@Test
	void testSetOmitEncoding() {
		OutputFormat format = new OutputFormat(str);
		format.setEncoding(encod);
		assertEquals(encod,format.getEncoding());
	}

	boolean bol = false;
	@Test
	void testSetSuppressDeclaration() {
		OutputFormat format = new OutputFormat();
		format.setSuppressDeclaration(bol);
		assertFalse(format.isSuppressDeclaration());
	}

	@Test
	void testIsSuppressDeclaration() {
		OutputFormat format = new OutputFormat();
		assertFalse(format.isSuppressDeclaration());
	}

	
	@Test
	void testSetNewLineAfterDeclaration() {
		OutputFormat format = new OutputFormat();
		format.setNewLineAfterDeclaration(bol);
		assertEquals(bol,format.isNewLineAfterDeclaration());
	}

	@Test
	void testIsNewLineAfterDeclaration() {
		OutputFormat format = new OutputFormat();
		assertTrue(format.isNewLineAfterDeclaration());
	}

	@Test
	void testIsExpandEmptyElements() {
		OutputFormat format = new OutputFormat();
		assertFalse(format.isExpandEmptyElements());	
	}

	@Test
	void testSetExpandEmptyElements() {
		OutputFormat format = new OutputFormat();
		format.setExpandEmptyElements(true);
		assertTrue(format.isExpandEmptyElements());
	}

	@Test
	void testIsTrimText() {
		OutputFormat format = new OutputFormat();
		assertFalse(format.isTrimText());
	}

	@Test
	void testSetTrimText() {
		OutputFormat format = new OutputFormat();
		format.setTrimText(true);
		assertTrue(format.isTrimText());
	}

	@Test
	void testIsPadText() {
		OutputFormat format = new OutputFormat();
		assertFalse(format.isPadText());
	}

	@Test
	void testSetPadText() {
		OutputFormat format = new OutputFormat();
		format.setPadText(true);
		assertTrue(format.isPadText());
	}

	@Test
	void testGetIndent() {
		OutputFormat format = new OutputFormat();
		assertNull(format.getIndent());	
	}

	
	@Test
	void testSetIndentString() {					//in questi test sul metodo SetIndent controllo se è stata inizializzata correttamente la variabile indent
		OutputFormat format = new OutputFormat();	//che può essere null come nel secondo test oppure avere un valore come nel primo test
		format.setIndent(indent);
		assertEquals(indent,format.getIndent());
	}
	
	String vuota = null;
	@Test
	void testSetIndentString2() {					
		OutputFormat format = new OutputFormat();
		format.setIndent(vuota);
		assertNull(format.getIndent());
	}
	
	String entr = "";
	@Test
	void testSetIndentString3() {					
		OutputFormat format = new OutputFormat();
		format.setIndent(entr);
		assertNull(format.getIndent());
	}
	
	@Test
	void testSetIndentBoolean() {					//Qui con i due test sul metood SetIndentBoolean provo a dare in ingresso sia false che true e controllo di
		OutputFormat format = new OutputFormat();   //avere i risultati corretti
		format.setIndent(true);
		assertEquals("  ",format.getIndent());
	}
	
	boolean bool;
	@Test
	void testSetIndentBoolean2() {
		OutputFormat format = new OutputFormat();
		 format.setIndent(bool);
		assertNull(format.getIndent());
	}

	
	int indentSize = 2;
	@Test
	void testSetIndentSize() {
		OutputFormat format = new OutputFormat();
		format.setIndentSize(indentSize);
		assertEquals("  ",format.getIndent());		//passo 2 alla funzione setIndentSize quindi ora mi aspetto che l'attributo Indent 
	}												//sia formato da due caratteri vuoti vista la logica della funzione setIndentSize

	@Test
	void testIsXHTML() {
		OutputFormat format = new OutputFormat();
		assertFalse(format.isXHTML());
	}

	@Test
	void testSetXHTML() {
		OutputFormat format = new OutputFormat();
		format.setXHTML(true);
		assertTrue(format.isXHTML());
	}

	@Test
	void testGetNewLineAfterNTags() {
		OutputFormat format = new OutputFormat();
		assertEquals(0,format.getNewLineAfterNTags());
	}

	@Test
	void testSetNewLineAfterNTags() {
		OutputFormat format = new OutputFormat();
		format.setNewLineAfterNTags(3);
		assertEquals(3,format.getNewLineAfterNTags());
	}

	@Test
	void testGetAttributeQuoteCharacter() {
		OutputFormat format = new OutputFormat();
		assertEquals('\"',format.getAttributeQuoteCharacter());
	}
	
	char x = '"';
	char y = '\'';						
	char invalid = 's';
	
	@ParameterizedTest										//nei prossimi due test mi aspetto che il carattere venga settato con successo
	@ValueSource(chars = {'"','\''})
	void testSetAttributeQuoteCharacter(char x) {
		OutputFormat format = new OutputFormat();
		format.setAttributeQuoteCharacter(x);
		assertEquals(x,format.getAttributeQuoteCharacter());
	}
	
	@Test
	void testSetAttributeQuoteCharacter3() {
		OutputFormat format = new OutputFormat();
		assertThrows(Exception.class,()-> format.setAttributeQuoteCharacter(invalid));   //il carattere è invalido e mi aspetto che setAttributeQuoteCharacter mi dia un'eccezione
	}

	//Per il metodo ParseOptions servono due stringhe diverse almeno per coprire tutti gli if poichè entrando in alcuni if viene incrementata 
	//la variabile di conteggio e quindi salto qualche elemento dell'array di stringhe
	String[] args = {"-suppressDeclaration", "-omitEncoding", "-indent", "-indentSize", "-expandEmpty", "-encoding", "-newlines","-lineSeparator","-trimText","-padText","-xhtml"};
	
	@Test
	void testParseOptions() {									
		OutputFormat format = new OutputFormat();
		assertEquals(11,format.parseOptions(args,0));
	}
	
	String[] args2 = {"-newlines","-trimText","-indentSize","5","fail"};
	
	@Test
	void testParseOptions2() {
		OutputFormat format = new OutputFormat();
		assertEquals(4,format.parseOptions(args2,0));
	}

	
	@Test
	void testCreatePrettyPrint() {
		OutputFormat format = OutputFormat.createPrettyPrint();			//L'oggetto creato da CreatePrettyPrint ha gli attributi settati correttamente
		assertEquals("  ",format.getIndent());
		assertTrue(format.isNewlines());
		assertTrue(format.isTrimText());
		assertTrue(format.isPadText());
	}
	
	
	@Test
	void testCreateCompactFormat() {
	
		OutputFormat format = OutputFormat.createCompactFormat();		//L'oggetto creato da CreateCompactFormat ha gli attributi settati correttamente
		assertTrue(format.isTrimText());
		assertFalse(format.isNewlines());
		assertNull(format.getIndent());
	}

}
