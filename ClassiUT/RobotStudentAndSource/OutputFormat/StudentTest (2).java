package student.GiuseppeMazzoccaDue;

import ClassUnderTest.OutputFormat;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// durata scrittura test circa 3 ore

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
		assertNotNull(new OutputFormat());
	}

	@Test
	void testOutputFormatString() {
		assertNotNull(new OutputFormat(" "));
	}

	@Test
	void testOutputFormatStringBoolean() {
		assertNotNull(new OutputFormat(" ",false));
	}

	@Test
	void testOutputFormatStringBooleanString() {
		assertNotNull(new OutputFormat(" ",false,"UTF-8"));
	}

	@Test
	void testGetLineSeparator() {
		OutputFormat OF = new OutputFormat();
		assertTrue(OF.getLineSeparator().equals("\n"));
	}

	@Test
	void testSetLineSeparator() {
		OutputFormat OF = new OutputFormat();
		OF.setLineSeparator("-");
		assertTrue(OF.getLineSeparator().equals("-"));
	}

	@Test
	void testIsNewlines() {
		OutputFormat OF = new OutputFormat();
		assertTrue(OF.isNewlines()==false);
	}

	@Test
	void testSetNewlines() {
		OutputFormat OF = new OutputFormat();
		OF.setNewlines(true);
		assertTrue(OF.isNewlines()==true);
	}

	@Test
	void testGetEncoding() {
		OutputFormat OF = new OutputFormat();
		assertTrue(OF.getEncoding().equals("UTF-8"));
	}

	@Test
	void testSetEncoding1() {
		OutputFormat OF = new OutputFormat(" ",false,"UTF-8");
		OF.setEncoding("UTF-8");
		assertTrue(OF.getEncoding().equals("UTF-8"));
	}
	
	@Test
	void testSetEncoding2() {
		OutputFormat OF = new OutputFormat(" ",false,null);
		OF.setEncoding(null);
	}

	@Test
	void testIsOmitEncoding() {
		OutputFormat OF = new OutputFormat();
		assertTrue(OF.isOmitEncoding()==false);
	}

	@Test
	void testSetOmitEncoding() {
		OutputFormat OF = new OutputFormat();
		OF.setOmitEncoding(true);
		assertTrue(OF.isOmitEncoding()==true);
	}

	@Test
	void testSetSuppressDeclaration() {
		OutputFormat OF = new OutputFormat();
		OF.setSuppressDeclaration(true);
		assertTrue(OF.isSuppressDeclaration()==true);
	}

	@Test
	void testIsSuppressDeclaration() {
		OutputFormat OF = new OutputFormat();
		assertTrue(OF.isSuppressDeclaration()==false);
	}

	@Test
	void testSetNewLineAfterDeclaration() {
		OutputFormat OF = new OutputFormat();
		OF.setNewLineAfterDeclaration(true);
		assertTrue(OF.isNewLineAfterDeclaration()==true);
	}

	@Test
	void testIsNewLineAfterDeclaration() {
		OutputFormat OF = new OutputFormat();
		assertTrue(OF.isNewLineAfterDeclaration()==true);
	}

	@Test
	void testIsExpandEmptyElements() {
		OutputFormat OF = new OutputFormat();
		assertTrue(OF.isExpandEmptyElements()==false);
	}

	@Test
	void testSetExpandEmptyElements() {
		OutputFormat OF = new OutputFormat();
		OF.setExpandEmptyElements(true);
		assertTrue(OF.isExpandEmptyElements()==true);
	}

	@Test
	void testIsTrimText() {
		OutputFormat OF = new OutputFormat();
		assertTrue(OF.isTrimText()==false);
	}

	@Test
	void testSetTrimText() {
		OutputFormat OF = new OutputFormat();
		OF.setTrimText(true);
		assertTrue(OF.isTrimText()==true);
	}

	@Test
	void testIsPadText() {
		OutputFormat OF = new OutputFormat();
		assertTrue(OF.isPadText()==false);
	}

	@Test
	void testSetPadText() {
		OutputFormat OF = new OutputFormat();
		OF.setPadText(true);
		assertTrue(OF.isPadText()==true);
	}

	@Test
	void testGetIndent() {
		OutputFormat OF = new OutputFormat();
		assertNull(OF.getIndent());
	}

	@Test
	void testSetIndentString1() {
		OutputFormat OF = new OutputFormat();
		OF.setIndent(" ");
		assertTrue(OF.getIndent().equals(" "));
	}
	
	@Test
	void testSetIndentString2() {
		OutputFormat OF = new OutputFormat();
		OF.setIndent("");
		assertTrue(OF.getIndent()==null);
	}
	
	@Test
	void testSetIndentString3() {
		OutputFormat OF = new OutputFormat();
		OF.setIndent(null);
		assertTrue(OF.getIndent()==null);
	}

	@Test
	void testSetIndentBoolean1() {
		OutputFormat OF = new OutputFormat();
		OF.setIndent(true);
		assertTrue(OF.getIndent().equals("  "));
	}
	
	@Test
	void testSetIndentBoolean2() {
		OutputFormat OF = new OutputFormat();
		OF.setIndent(false);
		assertTrue(OF.getIndent()==null);
	}

	@Test
	void testSetIndentSize1() {
		OutputFormat OF = new OutputFormat();
		OF.setIndentSize(0);
		assertTrue(OF.getIndent().equals(""));
	}
	
	@Test
	void testSetIndentSize2() {
		OutputFormat OF = new OutputFormat();
		OF.setIndentSize(4);
		assertTrue(OF.getIndent().equals("    "));
	}

	@Test
	void testIsXHTML() {
		OutputFormat OF = new OutputFormat();
		assertTrue(OF.isXHTML()==false);
	}

	@Test
	void testSetXHTML() {
		OutputFormat OF = new OutputFormat();
		OF.setXHTML(true);
		assertTrue(OF.isXHTML()==true);
	}

	@Test
	void testGetNewLineAfterNTags() {
		OutputFormat OF = new OutputFormat();
		assertTrue(OF.getNewLineAfterNTags()==0);
	}

	@Test
	void testSetNewLineAfterNTags() {
		OutputFormat OF = new OutputFormat();
		OF.setNewLineAfterNTags(10);
		assertTrue(OF.getNewLineAfterNTags()==10);
	}

	@Test
	void testGetAttributeQuoteCharacter() {
		OutputFormat OF = new OutputFormat();
		assertTrue(OF.getAttributeQuoteCharacter()=='\"');
	}

	@Test
	void testSetAttributeQuoteCharacter1() {
		OutputFormat OF = new OutputFormat();
		OF.setAttributeQuoteCharacter('"');
		assertTrue(OF.getAttributeQuoteCharacter()=='"');
	}
	
	@Test
	void testSetAttributeQuoteCharacter2() {
		OutputFormat OF = new OutputFormat();
		OF.setAttributeQuoteCharacter('\'');
		assertTrue(OF.getAttributeQuoteCharacter()=='\'');
	}
	
	@Test
	void testSetAttributeQuoteCharacter3() {
		OutputFormat OF = new OutputFormat();
		assertThrows("Doveva dare eccezione", IllegalArgumentException.class,()->OF.setAttributeQuoteCharacter('#'));
	}

	
	@Test
	void testParseOptions1() {
		OutputFormat OF = new OutputFormat();
		String[] args ={""};
		OF.parseOptions(args, 0);
	}

	
	@Test
	void testParseOptions2() {
		OutputFormat OF = new OutputFormat();
		String[] args ={"","-suppressDeclaration"};
		OF.parseOptions(args, 1);
		assertTrue(OF.isSuppressDeclaration()==true);
	}
	
	@Test
	void testParseOptions3() {
		OutputFormat OF = new OutputFormat();
		String[] args ={"","-omitEncoding"};
		OF.parseOptions(args, 1);
		assertTrue(OF.isOmitEncoding()==true);
	}
	
	@Test
	void testParseOptions4() {
		OutputFormat OF = new OutputFormat();
		String[] args ={"","-indent"," "};
		OF.parseOptions(args, 1);
		assertTrue(OF.getIndent().equals(" "));
	}
	
	@Test
	void testParseOptions5() {
		OutputFormat OF = new OutputFormat();
		String[] args ={"","-indentSize","1"};
		OF.parseOptions(args, 1);
		assertTrue(OF.getIndent().equals(" "));
	}
	
	@Test
	void testParseOptions6() {
		OutputFormat OF = new OutputFormat();
		String[] args ={"","-expandEmpty"};
		OF.parseOptions(args, 1);
		assertTrue(OF.isExpandEmptyElements()==true);
	}
	
	@Test
	void testParseOptions7() {
		OutputFormat OF = new OutputFormat();
		String[] args ={"","-encoding","UTF-8"};
		OF.parseOptions(args, 1);
		assertTrue(OF.getEncoding().equals("UTF-8"));
	}
	
	@Test
	void testParseOptions8() {
		OutputFormat OF = new OutputFormat();
		String[] args ={"","-newlines"};
		OF.parseOptions(args, 1);
		assertTrue(OF.isNewlines()==true);
	}
	
	@Test
	void testParseOptions9() {
		OutputFormat OF = new OutputFormat();
		String[] args ={"","-lineSeparator","-"};
		OF.parseOptions(args, 1);
		assertTrue(OF.getLineSeparator().equals("-"));
	}
	
	@Test
	void testParseOptions10() {
		OutputFormat OF = new OutputFormat();
		String[] args ={"","-trimText"};
		OF.parseOptions(args, 1);
		assertTrue(OF.isTrimText()==true);
	}
	
	@Test
	void testParseOptions11() {
		OutputFormat OF = new OutputFormat();
		String[] args ={"","-padText"};
		OF.parseOptions(args, 1);
		assertTrue(OF.isPadText()==true);
	}
	
	@Test
	void testParseOptions12() {
		OutputFormat OF = new OutputFormat();
		String[] args ={"","-xhtml"};
		OF.parseOptions(args, 1);
		assertTrue(OF.isXHTML()==true);
	}
	
	
	@Test
	void testCreatePrettyPrint() {
		assertNotNull(OutputFormat.createPrettyPrint());
	}

	@Test
	void testCreateCompactFormat() {
		assertNotNull(OutputFormat.createCompactFormat());
	}

}
