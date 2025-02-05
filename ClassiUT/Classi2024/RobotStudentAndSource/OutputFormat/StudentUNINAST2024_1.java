/*Compila i campi "Nome" e "Cognome" con le informazioni richieste
Nome: Andrea
Cognome: Bertolero
Username: a.bertolero@studenti.unina.it
UserID: 26
Date: 20/11/2024
*/

import org.junit.Test;
import static org.junit.Assert.*;

public class TestOutputFormat {

    @Test
    public void testOutputFormat1() 
    {
        OutputFormat of = new OutputFormat();

        assertEquals("\n", of.getLineSeparator());
        assertFalse(of.isNewlines());
        assertEquals("UTF-8", of.getEncoding());
        assertFalse(of.isOmitEncoding());
      	assertFalse(of.isSuppressDeclaration());
      	assertTrue(of.isNewLineAfterDeclaration());
      	assertFalse(of.isExpandEmptyElements());
      	assertFalse(of.isTrimText());
      	assertFalse(of.isPadText());
      	assertNull(of.getIndent());
      	assertFalse(of.isXHTML());
      	assertEquals(0, of.getNewLineAfterNTags());
      	assertEquals('\"', of.getAttributeQuoteCharacter());     
    }
  
  	@Test
  	public void testOutputFormat2()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	
      	assertEquals("ciao", of.getIndent());
    }

  	 @Test
  	public void testOutputFormat3()
    {
      	OutputFormat of = new OutputFormat("ciao", false);
      	
      	assertFalse(of.isNewlines());
    }
  
   	@Test
  	public void testOutputFormat4()
    {
      	OutputFormat of = new OutputFormat("ciao", true, "ciao");
      	
      	assertEquals("ciao", of.getEncoding());
    }
  
   	@Test
  	public void testsetLineSeparator()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setLineSeparator("prova");
      	
      	assertEquals("prova", of.getLineSeparator());
    }
  
    @Test
  	public void testsetNewlines()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setNewlines(true);
      	
      	assertTrue(of.isNewlines());
    }
  
    @Test
  	public void testsetEncoding1()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setEncoding(null);
      	
      	assertEquals("UTF-8", of.getEncoding());
    }
  
    @Test
  	public void testsetEncoding2()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setEncoding("prova");
      	
      	assertEquals("prova", of.getEncoding());
    }
  
	@Test
  	public void testsetOmitEncoding()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setOmitEncoding(true);
      	
      	assertTrue(of.isOmitEncoding());
    }
  
 	@Test
  	public void testsetSuppressDeclaration()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setSuppressDeclaration(true);
      	
      	assertTrue(of.isSuppressDeclaration());
    }
  
   	@Test
  	public void testsetNewLineAfterDeclaration()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setNewLineAfterDeclaration(true);
      	
      	assertTrue(of.isNewLineAfterDeclaration());
    }
  
    @Test
  	public void testsetExpandEmptyElements()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setExpandEmptyElements(true);
      	
      	assertTrue(of.isExpandEmptyElements());
    }
  
    @Test
  	public void testsetTrimText()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setTrimText(true);
      	
      	assertTrue(of.isTrimText());
    }
  
    @Test
  	public void testsetPadText()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setPadText(true);
      	
      	assertTrue(of.isPadText());
    }
  
  	@Test
  	public void testsetIndent1()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setIndent("prova");
      	
      	assertEquals("prova", of.getIndent());
    }
  	
  	@Test
  	public void testsetIndent2()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setIndent("");
      	
      	assertNull(of.getIndent());
    }
  	
  	@Test
  	public void testsetIndent3()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setIndent(null);
      	
      	assertNull(of.getIndent());
    }
  
    @Test
  	public void testsetIndent4()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setIndent(true);
      	
      	assertEquals("  ", of.getIndent());
    }
  
  	@Test
  	public void testsetIndent5()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setIndent(false);
      	
      	assertEquals(null, of.getIndent());
    }
  
    @Test
  	public void testsetIndentSize()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setIndentSize(2);
      	
      	assertEquals("  ", of.getIndent());
    }
  
    @Test
  	public void testsetXHTML()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setXHTML(true);
      	
      	assertTrue(of.isXHTML());
    }
  
    @Test
  	public void testsetNewLineAfterNTags()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setNewLineAfterNTags(1);
      	
      	assertEquals(1, of.getNewLineAfterNTags());
    }
  
    @Test
  	public void testsetAttributeQuoteCharacter1()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setAttributeQuoteCharacter('\'');
      	
      	assertEquals('\'', of.getAttributeQuoteCharacter());
    }
  
  	@Test
  	public void testsetAttributeQuoteCharacter2()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	of.setAttributeQuoteCharacter('"');
      	
      	assertEquals('"', of.getAttributeQuoteCharacter());
    }
  
   	@Test
  	public void testsetAttributeQuoteCharacter3()
    {
      	OutputFormat of = new OutputFormat("ciao");
      	
      	assertThrows(IllegalArgumentException.class, () -> {
            of.setAttributeQuoteCharacter('p'); 
        });
    }

    @Test
    public void testparseOptions1() {
        OutputFormat of = new OutputFormat("ciao");
        String[] prova = {
            " ", 
        };

      	assertEquals(0, of.parseOptions(prova, 0));

    }
  
   @Test
    public void testparseOptions2() {
        OutputFormat of = new OutputFormat("ciao");
        String[] prova = {
            "-suppressDeclaration", 
            "-omitEncoding", 
            "-indent", "ciao", 
            "-indentSize", "2", 
            "-expandEmpty", 
            "-encoding", "prova", 
            "-newlines", 
            "-lineSeparator", "prova", 
            "-trimText", 
            "-padText", 
            "-xhtml"
        };
        
        assertNotNull(of.parseOptions(prova, 0));
    }
  
    @Test
    public void testcreatePrettyPrint()
    {
        OutputFormat of = new OutputFormat();
        
        assertNotNull(of.createPrettyPrint());
      	assertEquals(2, of.createPrettyPrint().getIndent().length());

    }
  
    @Test
    public void testcreateCompactFormat()
    {
        OutputFormat of = new OutputFormat();
        
        assertNotNull(of.createCompactFormat());

    }
  
  


 


  
}
