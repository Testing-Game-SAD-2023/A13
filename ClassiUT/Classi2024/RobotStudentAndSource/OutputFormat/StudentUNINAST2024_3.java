/*Compila i campi "Nome" e "Cognome" con le informazioni richieste
Nome: "Anna Flavia"
Cognome: "De Rosa"
Username: annafl.derosa@studenti.unina.it
UserID: 11
Date: 18/11/2024
*/

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class OutputFormatAnnaFlaviaDeRosaRandoopHard {
  	private OutputFormat format;
	@BeforeClass
	public static void setUpClass() {
		// Eseguito una volta prima dell'inizio dei test nella classe
		// Inizializza risorse condivise 
		// o esegui altre operazioni di setup
	}
				
	@AfterClass
	public static void tearDownClass() {
		// Eseguito una volta alla fine di tutti i test nella classe
		// Effettua la pulizia delle risorse condivise 
		// o esegui altre operazioni di teardown
	}
				
	@Before
	public void setUp() {
      format = new OutputFormat();
		// Eseguito prima di ogni metodo di test
		// Preparazione dei dati di input specifici per il test
	}
				
	@After
	public void tearDown() {
		// Eseguito dopo ogni metodo di test
      format=null;
		// Pulizia delle risorse o ripristino dello stato iniziale
	}
  	
    /**
     * Test del costruttore predefinito.
     * Verifica che l'istanza della classe OutputFormat sia non nulla.
     */
  	@Test
  	public void testOutputFormat(){
  		assertNotNull(format);
    }
  
  
    /**
     * Test del costruttore con parametro indent.
     * Verifica che l'indentazione sia correttamente impostata.
     */
  	@Test
    public void testConstructorWithIndent() {	
        String indent = "    "; // 4 spazi
        OutputFormat format= new OutputFormat(indent);
        // Verifica che l'indentazione sia correttamente impostata
        assertEquals(indent, format.getIndent());
	}
  
    /**
     * Test del costruttore con parametri indent e newlines.
     * Verifica che le proprietà indent e newlines siano correttamente impostate.
     */
  	@Test
  	public void testConstructorWithIndentAndNewlines(){
      	String indent = "    "; // 4 spazi
        OutputFormat format= new OutputFormat(indent, true);
      	assertEquals(indent, format.getIndent());
      assertTrue(format.isNewlines()); 
    }
  
    /**
     * Test del costruttore con parametri indent, newlines e encoding.
     * Verifica che tutte le proprietà siano correttamente impostate.
     */  	
    @Test
  	public void testConstructorWithIndentNewlinesEncoding(){
      	String indent = "    "; // caso di stringa non vuota, ad es. 4 spazi
        OutputFormat format= new OutputFormat(indent, true,"ISO-8859-1");
      	assertEquals(indent, format.getIndent());
        assertTrue(format.isNewlines()); 
        assertEquals("ISO-8859-1",format.getEncoding());
    }
  
    /**
     * Test del metodo setIndent con stringa non vuota.
     * Verifica che l'indentazione sia correttamente impostata.
     */
  	@Test
    public void testSetIndent() {
        String indent = "    "; // caso di stringa non vuota, ad es. 4 spazi
        format.setIndent(indent);
        // Verifica che l'indentazione sia correttamente impostata
        assertEquals( indent, format.getIndent());
	}
  
  
    /**
     * Test del metodo setIndent con stringa vuota.
     * Verifica che l'indentazione sia impostata a null.
     */
  	@Test
    public void testSetIndent2() {
        String indent =""; // caso di stringa vuota
        format.setIndent(indent);
        // Verifica che l'indentazione sia correttamente impostata
        assertNull(format.getIndent());
	}
    /**
     * Test del metodo setIndent con booleano (false).
     * Verifica che l'indentazione sia impostata a null.
     */
  	@Test
    public void testSetIndent3() {
      	format.setIndent(false);
        assertNull(format.getIndent());
	}
  
    /**
     * Test del metodo setIndent con booleano (true).
     * Verifica che l'indentazione sia impostata al valore standard.
     */
  	@Test
    public void testSetIndent4() {
      	String STANDARD_INDENT = "  ";
      	format.setIndent(true);
        assertEquals(STANDARD_INDENT, format.getIndent());
	}
  
    
    /**
     * Test del metodo setLineSeparator.
     * Verifica che il separatore di linea sia correttamente impostato.
     */
  	@Test
    public void testSetLineSeparator() {
      	format.setLineSeparator("\r\n");
        assertEquals("\r\n",format.getLineSeparator());
	}
  
  	  /**
     * Test del metodo setNewlines.
     * Verifica che l'opzione newlines sia correttamente attivata.
     */
  	@Test
    public void testSetNewlines() {
      	format.setNewlines(true);
        assertTrue(format.isNewlines());
	}
  	
  
    /**
     * Test del metodo setEncoding.
     * Verifica che il valore di encoding sia correttamente impostato.
     */
  	@Test
    public void testSetEncoding() {
  
      	format.setEncoding("ISO-8859-1");
        assertEquals("ISO-8859-1",format.getEncoding());
	}
  
    
    /**
     * Test del metodo setOmitEncoding.
     * Verifica che l'opzione omitEncoding sia correttamente impostata.
     */
  	@Test
    public void testSetOmitEncoding() {
      	format.setOmitEncoding(true);
        assertTrue(format.isOmitEncoding());
	}
  
    /**
     * Test del metodo setSuppressDeclaration.
     * Verifica che l'opzione suppressDeclaration sia correttamente impostata (true).
     */
  	@Test
    public void testSetSuppressDeclaration() {
      	format.setSuppressDeclaration(true);
        assertTrue(format.isSuppressDeclaration());
	}
    /**
     * Test del metodo setSuppressDeclaration.
     * Verifica che l'opzione suppressDeclaration sia correttamente impostata (false).
     */
  	@Test
    public void testSetSuppressDeclaration2() {
      	format.setSuppressDeclaration(false);
        assertFalse(format.isSuppressDeclaration());
	}
  
  
  /** Test del metodo setNewLineAfterDeclaration: caso true
  * Verifica che il metodo setNewLineAfterDeclaration(true) 
   * imposti correttamente l'opzione newLineAfterDeclaration su true.
   */
  	@Test
    public void testSetNewLineAfterDeclaration() {
      	format.setNewLineAfterDeclaration(true);
        assertTrue(format.isNewLineAfterDeclaration());
	}
  /**  Test del metodo setNewLineAfterDeclaration: caso false
 * Verifica che il metodo setNewLineAfterDeclaration(false) 
 * imposti correttamente l'opzione newLineAfterDeclaration su false.
 */
  	@Test
    public void testNewLineAfterDeclaration() {
      	format.setNewLineAfterDeclaration(false);
        assertFalse(format.isNewLineAfterDeclaration());
	}
  
/** Test del metodo setExpandEmptyElements: caso true
 * Verifica che il metodo setExpandEmptyElements(true) 
 * imposti correttamente l'opzione expandEmptyElements su true.
 */

  	@Test
    public void testSetExpandEmptyElements() {
      	format.setExpandEmptyElements(true);
        assertTrue(format.isExpandEmptyElements());
	}
  /** Test del metodo setExpandEmptyElements: caso false
 * Verifica che il metodo setExpandEmptyElements(false) 
 * imposti correttamente l'opzione expandEmptyElements su false.
 */
  	@Test
    public void testSetExpandEmptyElements2() {
      	format.setExpandEmptyElements(false);
        assertFalse(format.isExpandEmptyElements());
	}
  

  
/** Test del metodo setTrimText: caso true
 * Verifica che il metodo setTrimText(true) 
 * imposti correttamente l'opzione trimText su true.
 */
  	@Test
    public void testSetTrimText() {
      	format.setTrimText(true);
        assertTrue(format.isTrimText());
	}
  
/** Test del metodo setPadText
 * Verifica che il metodo setPadText(true) 
 * imposti correttamente l'opzione padText su true.
 */
  	@Test
    public void testSetPadText() {
      	format.setPadText(true);
        assertTrue(format.isPadText());
	}

  
/** Test del metodo setIndentSize: caso IndentSize > 0
 * Verifica che il metodo setIndentSize con un valore maggiore di zero 
 * imposti correttamente l'indentazione al numero di spazi specificato.
 */
  	@Test
    public void testSetIndentSize() {
      	format.setIndentSize(2);
        assertEquals("La IndentSize deve essere 2","  ",format.getIndent());
	}
/** Test del metodo setIndentSize: caso IndentSize = 0
 * Verifica che il metodo setIndentSize con un valore pari a zero 
 * imposti correttamente l'indentazione su una stringa vuota.
 */
  	@Test
	public void testSetIndentSizeZero() {
    format.setIndentSize(0);
    assertEquals("La IndentSize deve essere 0","", format.getIndent()); // Nessuno spazio
	}
  
  /** Test del metodo setXHTML: caso true
 * Verifica che il metodo setXHTML(true) 
 * imposti correttamente l'opzione XHTML su true.
 */
  	@Test
  	public void testSetXHTML(){
      format.setXHTML(true);
      assertTrue(format.isXHTML());
    }
  
  /** Test del metodo setNewLineAfterNTags
 * Verifica che il metodo setNewLineAfterNTags imposti correttamente 
 * il numero di tag dopo i quali deve essere inserita una nuova linea.
 */
  	@Test
  	public void testSetNewLineAfterNTags(){
       format.setNewLineAfterNTags(10);
       assertEquals("Il valore di newLineAfterNTags dovrebbe essere 10",10, format.getNewLineAfterNTags());
    }
  
  
  
/** Test del metodo setAttributeQuoteCharacter: caso '"'
 * Verifica che il metodo setAttributeQuoteCharacter imposti 
 * correttamente il carattere di quote per gli attributi su '"'.
 */
  	@Test
  	public void testSetAttributeQuoteCharacter(){
       format.setAttributeQuoteCharacter('"');
       assertEquals('"',format.getAttributeQuoteCharacter());
    }
  
/** Test del metodo setAttributeQuoteCharacter: caso '\''
 * Verifica che il metodo setAttributeQuoteCharacter imposti 
 * correttamente il carattere di quote per gli attributi su '\''.
 */
  	@Test
  	public void testSetAttributeQuoteCharacter2(){
       format.setAttributeQuoteCharacter('\'');
       assertEquals('\'',format.getAttributeQuoteCharacter());
    }
  
 	/** Test del metodo setAttributeQuoteCharacter: eccezione
 * Verifica che il metodo setAttributeQuoteCharacter lanci 
 * un'IllegalArgumentException quando viene passato un carattere non valido.
 */
    @Test
    public void testSetAttributeQuoteCharacter3() {
        // Verifica che venga lanciata un'IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> format.setAttributeQuoteCharacter('/'));
    }
	
  
  //SEGUONO I TEST PER IL METODO PARSEOPTIONS

     // Test per l'Indentazione
    @Test
    public void testParseIndentOption() {
        String[] args = {"-indent", "    "};  
        int nextIndex = format.parseOptions(args, 0);
        assertEquals("    ", format.getIndent());
          // Controlla che l'indice sia avanzato di 2 (entrambi gli argomenti sono stati elaborati).
        assertEquals(2, nextIndex);
    }

    // Test per l'opzione -indentSize
    @Test
    public void testParseIndentSizeOption() {
        String[] args = {"-indentSize", "4"};  // Specifica indentSize
        int nextIndex = format.parseOptions(args, 0);
        assertEquals("    ", format.getIndent());  // Verifica che l'indentazione sia di 4 spazi
         // L'indice deve avanzare di 2 (opzione + valore).
        assertEquals(2, nextIndex);
    }

    // Test per l'opzione -newlines
    @Test
    public void testParseNewlinesOption() {
        String[] args = {"-newlines"};
        int nextIndex = format.parseOptions(args, 0);
        assertTrue(format.isNewlines());
        // L'indice deve avanzare di 1 
        assertEquals(1, nextIndex);
    }

    // Test per l'opzione -trimText
    @Test
    public void testParseTrimTextOption() {
        String[] args = {"-trimText"};
        int nextIndex = format.parseOptions(args, 0);
        assertTrue(format.isTrimText());
        // L'indice deve avanzare di 1
        assertEquals(1, nextIndex);
    }

      // Test per l'opzione -padText
    @Test
    public void testParsePadTextOption() {
 
        String[] args = {"-padText"};
        int nextIndex = format.parseOptions(args, 0);
        assertTrue(format.isPadText());
        // L'indice deve avanzare di 1
        assertEquals(1, nextIndex);
    }
    // Test per l'opzione -expandEmpty
    @Test
    public void testParseExpandEmptyElementsOption() {
        String[] args = {"-expandEmpty"};
        int nextIndex = format.parseOptions(args, 0);
        assertTrue(format.isExpandEmptyElements());
        // L'indice deve avanzare di 1
        assertEquals(1, nextIndex);
    }

    // Test per l'opzione -omitEncoding
    @Test
    public void testParseOmitEncodingOption() {
        String[] args = {"-omitEncoding"};
        int nextIndex = format.parseOptions(args, 0);
        assertTrue(format.isOmitEncoding());
        // L'indice deve avanzare di 1
        assertEquals(1, nextIndex);
    }

    // Test per l'opzione -xhtml
    @Test
    public void testParseXHTMLOption() {
        String[] args = {"-xhtml"};
        int nextIndex = format.parseOptions(args, 0);
        assertTrue(format.isXHTML());
        // L'indice deve avanzare di 1
        assertEquals(1, nextIndex);
    }

    // Test per l'opzione -encoding
    @Test
    public void testParseEncodingOption() {
        String[] args = {"-encoding", "UTF-8"};
        int nextIndex = format.parseOptions(args, 0);

        assertEquals("UTF-8", format.getEncoding());
        // L'indice deve avanzare di 2 (opzione + valore).
        assertEquals(2, nextIndex);
    }

    // Test per l'opzione -lineSeparator
    @Test
    public void testParseLineSeparatorOption() {
        String[] args = {"-lineSeparator", "\r\n"};
        int nextIndex = format.parseOptions(args, 0);
        assertEquals("\r\n", format.getLineSeparator());
        // L'indice deve avanzare di 2 (opzione + valore).
        assertEquals(2, nextIndex);
    }

    // Test per l'opzione -suppressDeclaration
    @Test
    public void testParseSuppressDeclarationOption() {
        String[] args = {"-suppressDeclaration"};
        int nextIndex = format.parseOptions(args, 0);
        assertTrue(format.isSuppressDeclaration());
        // L'indice deve avanzare di 1
        assertEquals(1, nextIndex);
    }

    // Test per il parsing di opzioni multiple
    @Test
    public void testParseMultipleOptions() {
        String[] args = {"-indent", "    ", "-encoding", "UTF-8", "-newlines", "-trimText"};
        int nextIndex = format.parseOptions(args, 0);

        assertEquals("    ", format.getIndent());  // Verifica indentazione
        assertEquals("UTF-8", format.getEncoding());  // Verifica encoding
        assertTrue(format.isNewlines());  // Verifica newlines
        assertTrue(format.isTrimText());  // Verifica trimText
        // L'indice deve avanzare in base al numero di argomenti elaborati.
        assertEquals(6, nextIndex);
        
    }

    // Test per la gestione di opzioni non valide
    @Test
    public void testParseInvalidOption() {
        String[] args = {"-invalidOption"};
        int nextIndex = format.parseOptions(args, 0);
        // Verifica che l'indice non venga modificato (l'argomento non valido non dovrebbe essere consumato)
        assertEquals(0, nextIndex);
    }
  
  
  // SEGUONO I TEST PER IL METODO CREATEPRETTYPRINT
     // Test che verifica tutti i valori predefiniti relativi alla formattazione
    @Test
    public void testPrettyPrintFormattingDefaults() {
        OutputFormat format = OutputFormat.createPrettyPrint();
        
        // Verifica l'indentazione predefinita
        assertEquals("  ", format.getIndent());
        // Verifica che le nuove linee siano abilitate
        assertTrue(format.isNewlines());
        // Verifica che il testo venga trimmato
        assertTrue(format.isTrimText());
        // Verifica che il padding del testo sia attivo.
        assertTrue(format.isPadText());
    }

    // Test che verifica l'impostazione predefinita di XHTML
    @Test
    public void testPrettyPrintXHTMLDefault() {
        OutputFormat format = OutputFormat.createPrettyPrint();
        // Verifica che XHTML sia disabilitato di default
        assertFalse(format.isXHTML());
    } 

	// Test che verifica tutti i valori predefiniti per il formato compatto
    @Test
    public void testCompactFormatDefaults() {
        OutputFormat format = OutputFormat.createCompactFormat();
        // Verifica che l'indentazione sia disabilitata
        assertNull(format.getIndent());
        // Verifica che le nuove linee siano disabilitate
        assertFalse( format.isNewlines());
        // Verifica che il testo venga trimmato
        assertTrue( format.isTrimText());
    }

    // Test per garantire che il formato compatto sia indipendente dal formato pretty
    @Test
    public void testCompactFormatIsIndependentFromPrettyPrint() {

        OutputFormat compactFormat = OutputFormat.createCompactFormat();
        OutputFormat prettyFormat = OutputFormat.createPrettyPrint();

        // Confronta le impostazioni tra i due formati.
        assertNotEquals(prettyFormat.getIndent(), compactFormat.getIndent());
        assertNotEquals(prettyFormat.isNewlines(), compactFormat.isNewlines());
    }

  
}