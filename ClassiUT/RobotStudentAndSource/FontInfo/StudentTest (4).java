package student.marcoDellIsolaDue;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Font;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.FontInfo;

class StudentTest {
	
	private static FontInfo fontInfo;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		fontInfo = new FontInfo();
	}

	@AfterEach
	void tearDown() throws Exception {
		fontInfo = null;
	}

	@Test
	void testHashCode() {
		//Inizializzo due variabili FontInfo oltre a quello base, creato per ogni test, per effettuare diverse verifiche
		FontInfo fontInfo2 = new FontInfo();
		Font font = new Font("SansSerif", Font.PLAIN, 14);
        FontInfo fontInfo3 = new FontInfo(font);
        
        fontInfo3.setIsBold(true);
        fontInfo3.setIsItalic(true);
		
        //Verifico che il metodo sia invocato e restituisca un valore come ci si aspetti faccia
		assertNotNull(fontInfo.hashCode());
		//Verifico che questo valore di hashCode dell'oggetto base sia effettivamente uguale ad un suo simile e quindi prodotto correttamente
		assertEquals(fontInfo.hashCode(), fontInfo2.hashCode());
		//Verifico che il metodo sia invocato e restituisca un valore come ci si aspetti faccia
		assertNotNull(fontInfo3.hashCode());
		//Verifico che questo valore di hashCode di un oggetto diverso sia diverso da quello base e che quindi il metodo funzioni correttamente
		assertNotEquals(fontInfo3.hashCode(), fontInfo.hashCode());
	}

	@Test
	void testFontInfo() {
		/* Poichè l'oggetto fontInfo è già instanziato con il suo costruttore di default,
		verifico direttamente che questo non sia nullo e che quindi il costruttore funzioni */
		assertNotNull(fontInfo);
	}

	@Test
	void testFontInfoFont() {
		//Creo una variabile di tipo Font per poter instanziare una variabile con il costruttore che imposta i valori a quelli indicati 
		Font font2 = new Font("SansSerif", Font.PLAIN, 14);
        FontInfo fontInfo2 = new FontInfo(font2);
		
        //Verifico che la variabile è stata instanziata correttamente
        assertNotNull(fontInfo2);
        
	}
	
	@Test
	void testFontInfoFontException() {
		//Creo questo ulteriore caso di test per verificare il corretto lancio  dell'eccezione nel caso in cui il font passato come parametro al costruttore sia nullo
		//Creo una variabile di tipo Font per poter instanziare una variabile con il costruttore che imposta i valori a quelli indicati 
		Font font2 = null;
		
		//Poichè ci si aspetta che venga lanciata una eccezione, la salvo in una variabile e poi controllo se il messaggio lanciato sia effettivamente quello che ci si aspetta
        Exception exception = assertThrows(Exception.class, () -> new FontInfo(font2));
        assertEquals("Null Font passed", exception.getMessage());
	}

	@Test
	void testClone() {
		//Instanzio una nuova variabile inizialmente nulla per poter poi clonare un'altra variabile attraverso il metodo clone()
		FontInfo fontInfo2 = null;
		
		fontInfo2 = (FontInfo) fontInfo.clone(); 
		
		//Verifico se fontInfo è stato copiato e fontInfo2 non è più nullo
		assertNotNull(fontInfo2);
		//Verifico se effettivamente i valori clonati sono giusti
		assertEquals(fontInfo, fontInfo2);
	}

	@Test
	void testGetFamily() {
		//Data la variabile già instanziata all'inizio del test verifico direttamente se il metodo viene chiamato correttamente
		assertNotNull(fontInfo.getFamily());
		//Verifico se il font family restituito corrisponde a quello che ci aspettiamo sia per una variabile instanzia con il costruttore di base
		assertEquals("Monospaced", fontInfo.getFamily());
	}

	@Test
	void testSetFamily() {
		//Creo una nuova variabile per poter effettuare un confronto tra due variabili che hanno un font family diverso
		FontInfo fontInfo2 = new FontInfo();
		
		fontInfo2.setFamily("SansSerif");
		
		/* Verifico se il font family della variabile creata è effettivamente diverso da quello della variabile base creata ad inizio test e quindi il metodo setFamily
		ha funzionato correttamente */
		assertNotEquals(fontInfo2, fontInfo);
	}
	
	@Test
	void testSetFamilyNull() {
		//Creo una nuova variabile per poter effettuare un confronto tra due variabili che hanno un font family diverso
		FontInfo fontInfo2 = new FontInfo();
		
		fontInfo2.setFamily(null);
		
		/* Verifico se il font family della variabile creata (con valore passato null) è uguale a quello della variabile base creata ad inizio test,
		 * in modo da verificare la copertura di tutte le casistiche in cui evolve il metodo setFamily (in questo caso per valori nulli il font family è posto uguale
		 * al valore di default "Monospaced") */
		assertEquals(fontInfo2, fontInfo);
	}

	@Test
	void testIsBold() {
		//Verifico direttamente con la variabile base instanziata ad inizio test, se il metodo isBold() restituisce un valore
		assertNotNull(fontInfo.isBold());
	}

	@Test
	void testSetIsBold() {
		/* Instanzio una nuova variabile con il costruttore di default, per poi applicare una variazione dello stato isBold e verificare se è stato applicato
		e quindi diverso dalla variabile di base creata ad inizio test che non risulta essere bold */
		FontInfo fontInfo2 = new FontInfo();
		
		fontInfo2.setIsBold(true);
		
		//Verifico se il metodo è stato eseguito correttamente cambiando lo stato di default e quindi diverso dallo stato di default stesso
		assertNotEquals(fontInfo.isBold(), fontInfo2.isBold());
	}

	@Test
	void testIsItalic() {
		//Verifico direttamente con la variabile base instanziata ad inizio test, se il metodo isItalic() restituisce un valore
		assertNotNull(fontInfo.isBold());
	}

	@Test
	void testSetIsItalic() {
		/* Instanzio una nuova variabile con il costruttore di default, per poi applicare una variazione dello stato isItalic e verificare se è stato applicato
		e quindi diverso dalla variabile di base creata ad inizio test che non risulta essere italic */
		FontInfo fontInfo2 = new FontInfo();
		
		fontInfo2.setIsItalic(true);
		
		//Verifico se il metodo è stato eseguito correttamente cambiando lo stato di default e quindi diverso dallo stato di default stesso
		assertNotEquals(fontInfo.isItalic(), fontInfo2.isItalic());
	}

	@Test
	void testGetSize() {
		//Verifico direttamente con la variabile base instanziata ad inizio test, se il metodo getSize() restituisce un valore
		assertNotNull(fontInfo.getSize());
	}

	@Test
	void testSetSize() {
		/* Instanzio una nuova variabile con il costruttore di default, per poi applicare una variazione del size e verificare se è stato applicato
		e quindi diverso dalla variabile di base creata ad inizio test che non risulta avere un size pari a 0 */
		FontInfo fontInfo2 = new FontInfo();
		
		fontInfo2.setSize(5);
		
		//Verifico se il metodo è stato eseguito correttamente cambiando lo stato di default e quindi diverso dallo stato di default stesso
		assertNotEquals(fontInfo.getSize(), fontInfo2.getSize());
	}

	@Test
	void testSetFont() {
        //Creo una nuova variabile con il costruttore di base per verificare se il font viene effettivamente variato dallo stato di default
		FontInfo fontInfo2 = new FontInfo();
		
		fontInfo2.setFont(new Font("SansSerif", Font.PLAIN, 14));
		
        //Verifico se il font è stato variato rispetto a quello di default
		assertNotEquals(fontInfo, fontInfo2);
        
	}
	
	@Test
	void testSetFont2() {
        //Verifico che in caso di Font nullo come parametro del metodo setFont() l'eccezione venga lanciata
		 Exception exception = assertThrows(Exception.class, () -> fontInfo.setFont(null));
	     assertEquals("Null Font passed", exception.getMessage());
	}

	@Test
	void testDoesFontMatch() {
		//Creo due nuove variabile per verificare che il metodo doesFontMatch restituisca il corretto valore per un font coincidente e per uno non coincidente e per un font nullo
		Font font2 = new Font(fontInfo.getFamily(), fontInfo.generateStyle(),fontInfo.getSize());
		Font font3 = new Font("SansSerif", Font.BOLD, 14);
		
		//Verifico l'uguaglianza tra il font base creato ad inzio del test e fontInfo2
		assertTrue(fontInfo.doesFontMatch(font2));
		//Verifico la non uguaglianza tra il font base creato ad inzio del test e fontInfo3
		assertFalse(fontInfo.doesFontMatch(font3));
		//Verifico il corretto funzionamento e quindi la totale copertura del metodo per casi in cui il font passato è nullo
		assertFalse(fontInfo.doesFontMatch(null));
	}

	@Test
	void testGenerateStyle() {
		//Creo più variabile per testare il metodo generateStyle() su diversi tipi di font, in modo tale da testare tutte le possibili evoluzioni del metodo
        FontInfo fontInfo2 = new FontInfo();
        FontInfo fontInfo3 = new FontInfo();
        
        //Setto le condizioni per la generazione dello style
        fontInfo2.setIsBold(true);
        fontInfo3.setIsItalic(true);
        
        //Verifico lo style delle variabili per vedere se queste producono il risutato atteso
        assertEquals(Font.PLAIN, fontInfo.generateStyle());
        assertEquals(Font.BOLD, fontInfo2.generateStyle());
        assertEquals(Font.ITALIC, fontInfo3.generateStyle());
	}

	@Test
	void testCreateFont() {
		//Genero una variabile sul quale applicare il metodo createFont() e una variabile per verificarne poi il corretto risultato
		Font font = fontInfo.createFont();
		Font font2 = new Font("Monospaced", Font.PLAIN, 12);
		
		//Verifico prima di tutto che il metodo createFont() abbia funzionato correttamente e successivamente verifico se il risultato che ha prodotto è giusto
		assertNotNull(font);
		assertEquals(font, font2);
	}

	@Test
	void testToString() {
		//Creo due variabili per verificare tutte le possibili evoluzioni del metodo
		FontInfo fontInfo2 = new FontInfo();
		FontInfo fontInfo3 = new FontInfo();
		FontInfo fontInfo4 = new FontInfo();
		
		//Imposto i valori delle variabili per vedere le possibili evoluzioni
		fontInfo2.setIsBold(true);
		fontInfo3.setIsItalic(true);
		fontInfo4.setIsBold(true);
		fontInfo4.setIsItalic(true);
		
		/* Verifico sulla variabile di base creata ad inizio test se prima di tutto il metodo viene eseguito correttamente e successivamente ne verifico il risultato, eseguo
		il test su tutte le variabili */
		assertNotNull(fontInfo.toString());
		assertEquals("Monospaced, 12", fontInfo.toString());
		assertNotNull(fontInfo2.toString());
		assertEquals("Monospaced, 12, bold", fontInfo2.toString());
		assertNotNull(fontInfo3.toString());
		assertEquals("Monospaced, 12, italic", fontInfo3.toString());
		assertNotNull(fontInfo4.toString());
		assertEquals("Monospaced, 12, bold, italic", fontInfo4.toString());	
	}

	@Test
	void testEqualsObject() {
		//Per verificare tutte le possibili evoluzioni del metodo creo nuove variabile con il quale confrontare la variabile di base instanziata ad inizio test
		Font font2 = new Font("Arial", Font.BOLD, 14);
		FontInfo fontInfo2 = new FontInfo(font2);
		FontInfo fontInfo3 = new FontInfo();
		FontInfo fontInfo4 = new FontInfo();
		FontInfo fontInfo5 = new FontInfo();
		
		fontInfo3.setIsBold(true);
		fontInfo4.setIsItalic(true);
	    fontInfo5.setSize(18);
		
		//Verifica con un valore uguale
        assertTrue(fontInfo.equals(fontInfo));
        //Verifica con un valore null
        assertFalse(fontInfo.equals(null));
        //Verifica con un oggetto di tipo diverso
        assertFalse(fontInfo.equals("test"));
        //Verifica con un valore diverso
        assertFalse(fontInfo.equals(fontInfo2));
        //Verifica con un valore di bold diverso
        assertFalse(fontInfo.equals(fontInfo3));
        //Verifica con un valore di italic diverso
        assertFalse(fontInfo.equals(fontInfo4));
        //Verifica con un valore di size diverso
        assertFalse(fontInfo.equals(fontInfo5));
	}

}
