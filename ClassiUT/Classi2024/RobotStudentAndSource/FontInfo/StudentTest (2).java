package student.angeloCalioloUno;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Font;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.FontInfo;

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
/*testHashCode: 
 * Questo test verifica se l'implementazione del metodo hashCode all'interno della classe FontInfo funziona correttamente. 
 * Vengono creati diversi oggetti FontInfo con oggetti Font di input diversi, e viene chiamato il metodo hashCode su ciascun oggetto.
 * */
	
	
	@Test
	void testHashCode() {
		
			Font font = new Font("Verdana", Font.BOLD, 12);
		
			FontInfo fontInfo = new FontInfo(font);
			
			fontInfo.hashCode();
			
			//
			
			Font font1 = new Font(null, Font.PLAIN, 14);
			
			FontInfo fontInfo1 = new FontInfo(font1);
			
			fontInfo1.hashCode();
			
			//
			
			Font font2 = new Font("serif", Font.ITALIC, 14);
			
			FontInfo fontInfo2 = new FontInfo(font2);
			
			fontInfo2.setFamily(null);
			
			fontInfo2.hashCode();	
			
			//
			
			Font font3 = new Font("mono", Font.BOLD | Font.ITALIC, 18);
			
			FontInfo fontInfo3 = new FontInfo(font3);
			
			fontInfo3.hashCode();	
		
		
	}
	
	/*testFontInfo: Questo test verifica il comportamento del costruttore di FontInfo con diversi tipi di input,
	 *  inclusi casi in cui l'oggetto Font è nullo e ci si aspetta un'eccezione IllegalArgumentException.*/

	@Test
	void testFontInfo() {
		
		FontInfo fontInfo = new FontInfo();
		
		Font font1 = new Font("sans_serif", Font.BOLD, 30);
		FontInfo fontInfo1 = new FontInfo(font1);
		
		Font font2 = null;
		
		 Assertions.assertThrows(IllegalArgumentException.class, () -> new FontInfo(font2));
		
	}
	
	/*testFontInfoFont: Questo test verifica le funzionalità generali di un oggetto FontInfo 
	 * attraverso il costruttore FontInfo*/

	@Test
	void testFontInfoFont() {
		  
        Font font = new Font("Arial", Font.BOLD | Font.ITALIC, 18);

        FontInfo fontInfo = new FontInfo(font);

   
        assertEquals("Arial", fontInfo.getFamily());
        assertTrue(fontInfo.isBold());
        assertTrue(fontInfo.isItalic());
        assertEquals(18, fontInfo.getSize());
        
        
        fontInfo.setFamily(null);
        fontInfo.setIsBold(false);
        fontInfo.setIsItalic(false);
        fontInfo.setSize(30);
        
        assertEquals("Monospaced", fontInfo.getFamily());
        assertTrue(!fontInfo.isBold());
        assertTrue(!fontInfo.isItalic());
        assertEquals(30, fontInfo.getSize());
        
        
        fontInfo.setFamily("Times New Roman");
        assertEquals("Times New Roman", fontInfo.getFamily());
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> fontInfo.setFont(null));
        
        
        
	}
	
	/*testClone: Questo test verifica se l'implementazione del metodo clone restituisce una copia esatta dell'oggetto FontInfo.*/

	@Test
	void testClone() {
		Font font = new Font("georgia", Font.PLAIN, 12);
		FontInfo fontInfo = new FontInfo(font);
		
		FontInfo fontCloned = (FontInfo) fontInfo.clone();
		
		assertNotSame(fontInfo, fontCloned); // Verifica se sono diversi
		
		assertEquals(fontCloned.getFamily(), fontInfo.getFamily());
		assertEquals(fontCloned.getSize(), fontInfo.getSize());
		assertEquals(fontCloned.isBold(), fontInfo.isBold());
		assertEquals(fontCloned.isItalic(), fontInfo.isItalic());
		
		// INSERIRE METODO PER RICHIAMARE L'EXCEPTION
		
	}
	
	
/*Setter and Getter: Ci sono diversi test per i metodi di set e get 
 * (testGetFamily, testSetFamily, testIsBold, testSetIsBold, testIsItalic, testSetIsItalic, testGetSize, testSetSize, testSetFont).
 *  Ogni test verifica il corretto funzionamento di uno specifico metodo della classe FontInfo.*/

	@Test
	void testGetFamily() {
		Font font = new Font("Arial", Font.PLAIN, 50);

        FontInfo fontInfo = new FontInfo(font);

   
        assertEquals("Arial", fontInfo.getFamily());
		
	}

	@Test
	void testSetFamily() {
		Font font = new Font("Courier New", Font.ITALIC, 13);
		
		FontInfo fontInfo = new FontInfo();
		
		fontInfo.setFont(font);
		
		
		assertEquals("Courier New", fontInfo.getFamily());
		
		
	}

	@Test
	void testIsBold() {
		
		FontInfo fontInfo = new FontInfo();
		
		assertFalse(fontInfo.isBold());
		
		fontInfo.setIsBold(true);
		
		assertTrue(fontInfo.isBold());
		
		
	}

	@Test
	void testSetIsBold() {
		
		FontInfo fontInfo = new FontInfo();
		
		assertFalse(fontInfo.isBold());
		
		fontInfo.setIsBold(true);
		
		assertTrue(fontInfo.isBold());
		
	
	}

	@Test
	void testIsItalic() {
		
		FontInfo fontInfo = new FontInfo();
		
		assertFalse(fontInfo.isItalic());
		
		fontInfo.setIsItalic(true);
		
		assertTrue(fontInfo.isItalic());
		
		
	}

	@Test
	void testSetIsItalic() {
		
		FontInfo fontInfo = new FontInfo();
		
		assertFalse(fontInfo.isItalic());
		
		fontInfo.setIsItalic(true);
		
		assertTrue(fontInfo.isItalic());
	}

	@Test
	void testGetSize() {
		
		Font font = new Font("Tahoma", Font.PLAIN, 120);
		FontInfo fontInfo = new FontInfo(font);
		
		assertEquals(120, fontInfo.getSize());
		
		
	}

	@Test
	void testSetSize() {
		
		FontInfo fontInfo = new FontInfo();
		
		fontInfo.setSize(40);
		
		assertEquals(40, fontInfo.getSize());
		
		
	}

	@Test
	void testSetFont() {
		FontInfo fontInfo = new FontInfo();
		
		assertEquals("Monospaced", fontInfo.getFamily());
		
		Font font = new Font("Times New Roman", Font.BOLD, 12);
		
		fontInfo.setFont(font);
		assertEquals("Times New Roman", fontInfo.getFamily());
		
		
		
		 Assertions.assertThrows(IllegalArgumentException.class, () -> fontInfo.setFont(null));
	}
	
	/*testDoesFontMatch: Questo test verifica se il metodo doesFontMatch restituisce il risultato corretto quando confronta due oggetti Font,
	 * inizializzati con i corretti costruttori.*/

	@Test
	void testDoesFontMatch() {
		
		Font font = new Font("Times New Roman", Font.ITALIC , 15);
		FontInfo fontInfo = new FontInfo(font);
		
		Font font1 = new Font("georgia", Font.BOLD, 13);
		
		assertFalse(fontInfo.doesFontMatch(font1));
		
		assertFalse(fontInfo.doesFontMatch(null));
		
		assertTrue(fontInfo.doesFontMatch(font));
	}
	
	/*testGenerateStyle: Questo test verifica se il metodo generateStyle restituisce il valore corretto 
	 * per lo stile del font in base alle impostazioni di bold e italic.*/
	

	@Test
	void testGenerateStyle() {
		
		FontInfo fontInfo = new FontInfo();
		
		assertEquals(Font.PLAIN, fontInfo.generateStyle());
		
		fontInfo.setIsBold(true);
		
		assertEquals(Font.BOLD, fontInfo.generateStyle());
		
		fontInfo.setIsBold(false);
		fontInfo.setIsItalic(true);
		
		assertEquals(Font.ITALIC, fontInfo.generateStyle());
		
		
		fontInfo.setIsBold(true);
		
		assertEquals(Font.ITALIC | Font.BOLD, fontInfo.generateStyle());
		
		
		
		
	}
	
	/*testCreateFont: Questo test verifica se il metodo createFont 
	 * restituisce un oggetto Font corrispondente alle informazioni contenute in un oggetto FontInfo.*/

	@Test
	void testCreateFont() {
		Font font1 = new Font("Verdana", Font.BOLD, 12);
		FontInfo fontInfo = new FontInfo(font1);
		
		Font font = fontInfo.createFont();
		
		assertEquals(font, font1);
		
	}
	
	/*testToString: Questo test verifica se il metodo toString 
	 * restituisce la rappresentazione corretta dell'oggetto FontInfo come stringa.
	 * Dove per effettuare il test sono state analizzate le diramazioni
	 * dei vari if presenti nelle funzione per comporre la stringa*/

	@Test
	void testToString() {
		Font font = new Font("Arial", Font.BOLD | Font.ITALIC, 18);

        FontInfo fontInfo = new FontInfo(font);
        
        assertEquals("Arial, 18, bold, italic",fontInfo.toString());
        
        fontInfo = new FontInfo();
        
        assertEquals("Monospaced, 12",fontInfo.toString());
        
        fontInfo.setIsBold(true);
        
        assertEquals("Monospaced, 12, bold",fontInfo.toString());
        
        fontInfo.setIsItalic(true);
        
        fontInfo.setIsBold(false);
        
        assertEquals("Monospaced, 12, italic",fontInfo.toString());
        
        
	}
	
	/*testEqualsObject: Questo test verifica se l'implementazione del metodo equals funziona correttamente, 
	 * confrontando due oggetti FontInfo in base a diversi scenari,
	 *  come famiglia del font, stile bold, stile italic e dimensione del font.*/

	@Test
	void testEqualsObject() {
		
		Font font = new Font("Arial", Font.BOLD | Font.ITALIC, 18);

        FontInfo fontInfo = new FontInfo(font);
        
        Font font2 = new Font("Arial", Font.BOLD | Font.ITALIC, 18);

        FontInfo fontInfo2 = new FontInfo(font2);
        
        
        FontInfo fontInfo3 = new FontInfo();
        
        
        assertTrue(fontInfo.equals(fontInfo2)); // Test per equivalenza
        assertTrue(fontInfo.equals(fontInfo));  // Test per stesso oggetto
        assertFalse(fontInfo.equals(null));		// Test per passaggio parametri null
        assertFalse(fontInfo3.equals(fontInfo)); // Test per FAMILYNAME
        
        fontInfo.setFamily(null);
        assertFalse(fontInfo3.equals(fontInfo)); // Test per BOLD
        
        fontInfo3.setIsBold(true);
        fontInfo3.setFamily("Arial");
        assertFalse(fontInfo2.equals(fontInfo3)); // Test per ITALIC
        
        fontInfo3.setIsItalic(true);
        assertFalse(fontInfo2.equals(fontInfo3)); // Test per SIZE
        
        fontInfo3.setFamily(null);
        assertFalse(fontInfo3.equals(fontInfo2));
        
        
        
        
	}

	
	/* Nella scrittura dei test per le diverse funzioni mi sono inizialmente
	 * concentrato sulla lettura e l'analisi del codice sorgente. In modo tale
	 * da poter comprendere in modo dettagliato cosa ci aspettavamo dalla nostra classe.
	 * 
	 * Successivamente ho implementato una serie di test con lo scopo di ricoprire
	 * tutte le possibili casistiche e scenari previsti dalle nostre funzioni sorgenti.
	 * 
	 * Quindi ho implementato le funzioni test considerando diversi scenari di input assicurandomi
	 * che l'output fosse quello atteso.
	 * 
	 * Complessivamente per completare le funzioni di test per avere un coverage piú alto possibile
	 * sono state impiegate all'incirca 4 ore.
	 */
}
