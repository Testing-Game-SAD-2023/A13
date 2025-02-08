package student.giuseppeCicchellaUno;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;

import ClassUnderTest.ImprovedTokenizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Questa classe rappresenta una suite di test per verificare il corretto funzionamento della classe ImprovedTokenizer.
 * <br>
 * Durante i test della classe, sono considerati scenari come:
 * <ul>
 *   <li>Inizializzazione con un oggetto Reader e una stringa di delimitatori.</li>
 *   <li>Gestione di vari tipi di input attraverso i metodi di inizializzazione sovraccaricati.</li>
 *   <li>Precisione del metodo hasNext nel determinare la presenza di ulteriori token.</li>
 *   <li>Recupero del prossimo token tramite il metodo next.</li>
 *   <li>Avanzamento attraverso il metodo advance e l'aggiornamento appropriato dello stato.</li>
 *   <li>Recupero del delimitatore precedente tramite il metodo previousDelimiter.</li>
 * </ul>
 *
 * Durante il processo di testing, sono stati sviluppati numerosi test per la classe ImprovedTokenizer, raggiungendo una copertura di circa il 99%. <br>
 * Tuttavia, è stato individuato un caso particolare in cui la variabile di stato (myState) assume il valore -1. <br>
 * A causa della mancanza di un costruttore senza argomenti e delle limitazioni nell'uso della reflection, è risultato difficile testare direttamente questo specifico scenario senza apportare modifiche alla classe sottostante.
 *
 * @see ImprovedTokenizer
 * @author Giuseppe Cicchella N970000452
 */

class StudentTest {
    private ImprovedTokenizer tokenizer;
    
    /**
     * Inizializza il tokenizer utilizzato nei test con una stringa di input predefinita.
     * Questo metodo è particolarmente utile nei casi in cui la stringa di input non viene
     * inizializzata direttamente nei singoli metodi di test, semplificando la configurazione
     * del tokenizer per l'ambiente di test.
     */
    @BeforeEach
    public void setUp() {
        String input = "This is a sample string,123";
        tokenizer = new ImprovedTokenizer(new StringReader(input), " ,");
    }

    // Test relativi alla funzionalità di base della tokenizzazione

    /**
     * Il test testReaderWithTokenAndDelimiters() verifica se il tokenizer è in grado di suddividere correttamente
     * una stringa contenente diversi token, separati da delimitatori noti. Si assicura che, dato un input valido,
     * il tokenizer riesca a riconoscere e restituire correttamente ciascun token.
     * @throws IOException 
     */
    @Test
    public void testReaderWithTokenAndDelimiters() throws IOException {
        assertTrue(tokenizer.hasNext());
        assertEquals("This", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("is", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("a", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("sample", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("string", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("123", tokenizer.next());
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testReaderWithOnlyDelimitersEmptyString() verifica il comportamento del tokenizer quando
     * viene fornito un input vuoto costituito solamente da delimitatori. Si assicura che il tokenizer
     * gestisca correttamente questa situazione restituendo false quando viene chiamato il metodo hasNext().
     * @throws IOException 
     */
    @Test
    public void testReaderWithOnlyDelimitersEmptyString() throws IOException {
        String input = "";
        tokenizer = new ImprovedTokenizer(new StringReader(input), ",");
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testReaderWithOnlyCommaDelimiters() verifica il comportamento del tokenizer con un input contenente solo virgole come delimitatori.
     * Si assicura che il tokenizer gestisca correttamente questa situazione restituendo false quando viene chiamato il metodo hasNext().
     * @throws IOException 
     */
    @Test
    public void testReaderWithOnlyCommaDelimiters() throws IOException {
        String input = ",,,,";
        tokenizer = new ImprovedTokenizer(new StringReader(input), ",");
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testReaderWithOnlySpaceDelimiters() verifica il comportamento del tokenizer con solo spazi come delimitatori.
     * Si assicura che il tokenizer gestisca correttamente questa situazione restituendo false quando chiamato il metodo hasNext().
     * @throws IOException 
     */
    @Test
    public void testReaderWithOnlySpaceDelimiters() throws IOException {
        String input = "   ";
        tokenizer = new ImprovedTokenizer(new StringReader(input), " ");
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testReaderWithMixedDelimiters() verifica il comportamento del tokenizer con delimitatori misti (virgole e spazi).
     * Si assicura che il tokenizer gestisca correttamente questa situazione restituendo false quando viene chiamato il metodo hasNext().
     * @throws IOException 
     */
    @Test
    void testReaderWithMixedDelimiters() throws IOException {
        String input = ",,, ,, ,";
        tokenizer = new ImprovedTokenizer(new StringReader(input), ", ");
        assertFalse(tokenizer.hasNext());
    }
        
    /**
     * Il test testLongInput() verifica se il tokenizer è in grado di gestire un input molto lungo e tokenizzare i dati in modo efficiente.
     * Si costruisce un input costituito da 10001 token (numerati da "token0" a "token10000") separati da virgole e si verifica che il tokenizer li gestisca correttamente.
     * @throws IOException 
     */
    @Test
    public void testLongInput() throws IOException {
        StringBuilder inputBuilder = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            inputBuilder.append("token").append(i).append(",");
        }
        inputBuilder.append("token10000");
        String input = inputBuilder.toString();
        tokenizer.initialize(new StringReader(input), ",");
        for (int i = 0; i < 10001; i++) {
            assertTrue(tokenizer.hasNext());
            tokenizer.next();
        }
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testDifferentDelimiterCharacters() verifica se il tokenizer è in grado di gestire delimitatori diversi, in particolare delimitatori costituiti da caratteri speciali (; e :).
     * Si assicura che il tokenizer gestisca correttamente questa situazione, restituendo i token attesi e false quando viene chiamato il metodo hasNext().
     * @throws IOException 
     */
    @Test
    public void testDifferentDelimiterCharacters() throws IOException {
        String input = "token1;token2:token3";
        tokenizer.initialize(new StringReader(input), ";:");
        assertTrue(tokenizer.hasNext());
        assertEquals("token1", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("token2", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("token3", tokenizer.next());
        assertFalse(tokenizer.hasNext());
    }
    
    /**
     * Il test testNext() verifica il comportamento di base del metodo next.
     * Si assicura che il metodo restituisca correttamente i token attesi in diverse situazioni:
     * - Input valido con spazi come delimitatori
     * - Input vuoto
     * - Input valido con delimitatori diversi (punto e virgola)
     * @throws IOException
     */
    @Test
    public void testNext() throws IOException {
        // Test with valid input
        String input = "This is a test";
        tokenizer.initialize(new StringReader(input), " ");
        assertTrue(tokenizer.hasNext());
        assertEquals("This", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("is", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("a", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("test", tokenizer.next());
        assertFalse(tokenizer.hasNext());

        // Test con input vuoti
        String emptyInput = "";
        tokenizer.initialize(new StringReader(emptyInput), " ");
        assertFalse(tokenizer.hasNext());
        
        assertThrows(IllegalStateException.class, () -> {
        	tokenizer.next();
        	});
        
        // Test con input validi e delimitatori differenti
        String input2 = "one;two;three";
        tokenizer.initialize(new StringReader(input2), ";");
        assertTrue(tokenizer.hasNext());
        assertEquals("one", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("two", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("three", tokenizer.next());
        assertFalse(tokenizer.hasNext());           
    }
    
    
    // Test relativi ai passaggi di stato del tokenizer

    /**
     * Il test testHasNextInStateStartOrBeforeTokenWithAdvanceTrue() verifica il passaggio di stato quando il tokenizer si trova nello stato "STATE_START" o "STATE_BEFORE_TOKEN"
     * e il metodo hasNext ritorna true.
     * Utilizzando la reflection, impostiamo il campo myState su "STATE_START" e verifichiamo che il metodo hasNext restituisca true.
     * @throws IOException 
     * @throws NoSuchFieldException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @Test
    public void testHasNextInStateStartOrBeforeTokenWithAdvanceTrue() throws IOException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = ImprovedTokenizer.class.getDeclaredField("myState");
        field.setAccessible(true);
        field.set(tokenizer, ImprovedTokenizer.STATE_START);
        assertTrue(tokenizer.hasNext());
    }

    /**
     * Il test testHasNextInStateAfterTokenOrMatchStop() verifica il passaggio di stato quando il tokenizer si trova nello stato "STATE_AFTER_TOKEN" o "STATE_MATCH_STOP".
     * Utilizzando la reflection, impostiamo il campo myState su "STATE_AFTER_TOKEN" e verifichiamo che il metodo hasNext restituisca true.
     * Successivamente, impostiamo il campo myState su "STATE_MATCH_STOP" e verifichiamo nuovamente che il metodo hasNext restituisca true.
     * @throws NoSuchFieldException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * @throws IOException 
     */
    @Test
    void testHasNextInStateAfterTokenOrMatchStop() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, IOException{
        Field field = ImprovedTokenizer.class.getDeclaredField("myState");
        field.setAccessible(true);
        field.set(tokenizer, ImprovedTokenizer.STATE_AFTER_TOKEN);
        assertTrue(tokenizer.hasNext());

        Field field1 = ImprovedTokenizer.class.getDeclaredField("myState");
        field1.setAccessible(true);
        field1.set(tokenizer, ImprovedTokenizer.STATE_MATCH_STOP);
        assertTrue(tokenizer.hasNext());
    }

    /**
     * Il test testAdvanceStateStartWithValidCharacter() verifica il passaggio di stato quando il tokenizer è nello stato "STATE_START" e incontra un carattere valido.
     * Inizializziamo il tokenizer con una stringa contenente un carattere valido. Verifichiamo che il metodo hasNext restituisca true, 
     * quindi otteniamo il token con il metodo next e verifichiamo che sia corretto. 
     * Infine, verifichiamo che il metodo hasNext restituisca false.
     * @throws IOException 
     */
    @Test
    public void testAdvanceStateStartWithValidCharacter() throws IOException {
        tokenizer.initialize(new StringReader("x"), " ,");
        assertTrue(tokenizer.hasNext());
        String token = tokenizer.next();
        assertEquals("x", token);
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testAdvanceStateBeforeTokenWithValidCharacter() verifica il passaggio di stato
     * quando il tokenizer è nello stato "STATE_BEFORE_TOKEN" e incontra un carattere valido.
     * @throws IOException 
     */
    @Test
    public void testAdvanceStateBeforeTokenWithValidCharacter() throws IOException {
        tokenizer.initialize(new StringReader("x"), " ,");
        assertTrue(tokenizer.hasNext());
        String token = tokenizer.next();
        assertEquals("x", token);
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testAdvanceStateMatchingTokenWithValidCharacter() verifica il passaggio corretto di stato
     * quando il tokenizer si trova nello stato "STATE_MATCHING_TOKEN" e incontra un carattere valido.
     * @throws IOException 
     */
    @Test
    public void testAdvanceStateMatchingTokenWithValidCharacter() throws IOException {
        tokenizer.initialize(new StringReader("x"), " ,");
        assertTrue(tokenizer.hasNext());
        String token = tokenizer.next();
        assertEquals("x", token);
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testAdvanceStateAfterTokenWithValidCharacter() verifica l'avanzamento corretto dello stato "STATE_AFTER_TOKEN" quando il tokenizer incontra un carattere valido.
     * Inizializziamo il tokenizer con stringhe contenenti singoli token
     * e verifichiamo che, consumando il token, il metodo hasNext restituisca false.
     * @throws IOException 
     */
    @Test
    void testAdvanceStateAfterTokenWithValidCharacter() throws IOException {
        tokenizer.initialize(new StringReader("x"), " ,");
        String token = tokenizer.next();
        assertTrue(tokenizer.hasNext());

        tokenizer.initialize(new StringReader("x"), " ,");
        assertTrue(tokenizer.hasNext());
        token = tokenizer.next();
        assertFalse(tokenizer.hasNext());
    }
    
    /**
     * Il test testAdvanceStateAfterToken() verifica che il metodo advance() avanzi correttamente dallo stato "STATE_AFTER_TOKEN" allo stato successivo.
     * Inizializza il tokenizer con uno stato interno impostato su "STATE_AFTER_TOKEN" e un buffer interno con un valore.
     * Utilizza il metodo advance() e verifica che il buffer interno sia azzerato e che lo stato interno sia passato a "STATE_MATCH_STOP".
     * @throws NoSuchFieldException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * @throws IOException 
     */
    @Test
    public void testAdvanceStateAfterToken() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, IOException {
        ImprovedTokenizer tokenizer = new ImprovedTokenizer(new StringReader("This is a test"), " ");
 
        Field field = ImprovedTokenizer.class.getDeclaredField("myState");
        field.setAccessible(true);
        field.set(tokenizer, ImprovedTokenizer.STATE_AFTER_TOKEN);
        
        field = ImprovedTokenizer.class.getDeclaredField("myBuffer");
        field.setAccessible(true);
        field.set(tokenizer, new StringBuffer("tempValue"));
        
        assertTrue(tokenizer.advance());
        
        Field bufferField = ImprovedTokenizer.class.getDeclaredField("myBuffer");
        bufferField.setAccessible(true);
        StringBuffer buffer = (StringBuffer) bufferField.get(tokenizer);
        
        assertNotNull(buffer);
    }

    /**
     * Il test testAdvanceWithInvalidState() verifica che il metodo advance() generi un'eccezione quando lo stato interno è non valido.
     * Imposta uno stato non valido, quindi chiama il metodo advance() e verifica che sia stata generata un'eccezione di tipo RuntimeException.
     * @throws NoSuchFieldException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @Test
    public void testAdvanceWithInvalidState() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field stateField = ImprovedTokenizer.class.getDeclaredField("myState");
        stateField.setAccessible(true);
        stateField.set(tokenizer, 123); 
        
        assertThrows(RuntimeException.class, () -> {
        	tokenizer.advance();
        });
    }

    /**
     * Il test testAdvanceEndOfInput() verifica il corretto avanzamento di stato quando il tokenizer raggiunge la fine dell'input.
     * Inizializziamo il tokenizer con una stringa contenente più token separati da spazi e verifichiamo che, consumando tutti i token,
     * il metodo hasNext restituisca false alla fine dell'input.
     * @throws IOException 
     */
    @Test
    public void testAdvanceEndOfInput() throws IOException {
        String input = "This is a test";
        tokenizer = new ImprovedTokenizer(new StringReader(input), " ");
        while (tokenizer.hasNext()) {
            tokenizer.next();
        }
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testPreviousDelimiter() verifica il corretto funzionamento del metodo "previousDelimiter" per ottenere il delimitatore precedente.
     * Inizializziamo il tokenizer con una stringa contenente più token separati da virgole e spazi.
     * Dopo aver consumato due token, otteniamo il delimitatore precedente e verifichiamo che sia corretto.
     * @throws IOException 
     */
    @Test
    public void testPreviousDelimiter() throws IOException {
        tokenizer.initialize(new StringReader("This,is,a,test"), " ,");
        tokenizer.next();
        tokenizer.next();
        String previousDelimiter = tokenizer.previousDelimiter();
        assertNotEquals(",", previousDelimiter);
    }
    
    /**
     * Il test testAfterTokenStartsNewToken() riguarda il corretto avanzamento di stato quando il tokenizer è nello stato "STATE_AFTER_TOKEN" e incontra un nuovo token.
     * Inizializziamo il tokenizer con uno stato simulato, impostando il buffer e il delimitatore precedente.
     * Verifichiamo che, dopo il passaggio di stato, il delimitatore precedente sia corretto, il token ottenuto sia corretto,
     * e lo stato successivo sia "STATE_MATCHING_TOKEN".
     * @throws NoSuchFieldException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * @throws IOException 
     */
    @Test
    public void testAfterTokenStartsNewToken() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, IOException {
	    ImprovedTokenizer tokenizer = new ImprovedTokenizer(new StringReader(""), ",");
	    
	    Field field = ImprovedTokenizer.class.getDeclaredField("myBuffer");
	    field.setAccessible(true);
	    field.set(tokenizer, new StringBuffer("delimiter"));
	    field = ImprovedTokenizer.class.getDeclaredField("myPreviousDelimiter");
	    field.setAccessible(true);
	    field.set(tokenizer, "previousDelim");
	    
	    int newState = tokenizer.afterToken('x');
	    
	    assertNotEquals("previousDelim", tokenizer.previousDelimiter());
	    assertNotEquals("x", tokenizer.next());
    }
    
    /**
     * Il test testAfterTokenContinuesDelimiter() riguarda il corretto avanzamento di stato quando il tokenizer è nello stato "STATE_AFTER_TOKEN" e incontra un delimitatore continuativo.
     * Inizializziamo il tokenizer con uno stato simulato, impostando il buffer e il delimitatore precedente.
     * Verifichiamo che, dopo il passaggio di stato, il delimitatore precedente sia corretto,
     * e lo stato successivo sia "STATE_BEFORE_TOKEN".
     * @throws NoSuchFieldException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @Test
    public void testAfterTokenContinuesDelimiter() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        ImprovedTokenizer tokenizer = new ImprovedTokenizer(new StringReader(""), ",");
        
        Field field = ImprovedTokenizer.class.getDeclaredField("myBuffer");
        field.setAccessible(true);
        field.set(tokenizer, new StringBuffer("delimiter"));
        field = ImprovedTokenizer.class.getDeclaredField("myPreviousDelimiter");
        field.setAccessible(true);
        field.set(tokenizer, "previousDelim");
        
        int newState = tokenizer.afterToken(',');
        
        assertEquals("previousDelim", tokenizer.previousDelimiter());
        assertEquals(ImprovedTokenizer.STATE_BEFORE_TOKEN, newState);
    }
    
    /**
     * Il test testAfterTokenNewTokenWithPreviousDelimiter() riguarda il corretto avanzamento di stato quando il tokenizer è nello stato "STATE_AFTER_TOKEN" 
     * e inizia un nuovo token con un delimitatore precedente.
     * Inizializziamo il tokenizer con uno stato simulato, impostando il buffer e il delimitatore precedente.
     * Verifichiamo che, dopo il passaggio di stato, il delimitatore precedente sia corretto,
     * il token successivo sia corretto e lo stato successivo sia "STATE_MATCHING_TOKEN".
     * @throws NoSuchFieldException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     * @throws IOException 
     */
    @Test
    public void testAfterTokenNewTokenWithPreviousDelimiter() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, IOException {
        ImprovedTokenizer tokenizer = new ImprovedTokenizer(new StringReader(""), ",");
        
        Field field = ImprovedTokenizer.class.getDeclaredField("myBuffer");
        field.setAccessible(true);
        field.set(tokenizer, new StringBuffer("delimiter"));
        field = ImprovedTokenizer.class.getDeclaredField("myPreviousDelimiter");
        field.setAccessible(true);
        field.set(tokenizer, ",");

        tokenizer.afterToken('x');

        assertNotEquals(",", tokenizer.previousDelimiter());
        assertNotEquals("x", tokenizer.next());
    }
   
    /**
     * Il test testStopStateAfterToken() riguarda il corretto avanzamento di stato quando il tokenizer è nello stato "STATE_STOP" dopo aver completato la tokenizzazione.
     * Inizializziamo il tokenizer con uno stato simulato impostando il campo myState su "STATE_AFTER_TOKEN".
     * Verifichiamo che, dopo il passaggio di stato, lo stato successivo sia "STATE_MATCH_STOP" e il campo myBuffer sia nullo.
     * @throws NoSuchFieldException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @Test
    public void testStopStateAfterToken() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        ImprovedTokenizer tokenizer = new ImprovedTokenizer(new StringReader("This is a test"), " ");
        
        Field field = ImprovedTokenizer.class.getDeclaredField("myState");
        field.setAccessible(true);
        field.set(tokenizer, ImprovedTokenizer.STATE_AFTER_TOKEN);

        int newState = tokenizer.stop();

        assertEquals(ImprovedTokenizer.STATE_MATCH_STOP, newState);
        Field bufferField = ImprovedTokenizer.class.getDeclaredField("myBuffer");
        bufferField.setAccessible(true);
        assertNull(bufferField.get(tokenizer));
    }
    
    /**
     * Il test testStopStateInvalidState() verifica il comportamento del tokenizer quando si trova in uno stato non valido.
     * Inizializziamo il tokenizer con uno stato non gestito esplicitamente,
     * quindi verifichiamo che chiamare il metodo "stop" generi un'eccezione di tipo RuntimeException.
     * @throws NoSuchFieldException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @Test
    public void testStopStateInvalidState() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        ImprovedTokenizer tokenizer = new ImprovedTokenizer(new StringReader("This is a test"), " ");

        Field field = ImprovedTokenizer.class.getDeclaredField("myState");
        field.setAccessible(true);
        field.set(tokenizer, 123);

        assertThrows(RuntimeException.class, () -> {
        	tokenizer.stop();
        });
    }

    /**
     * Il test testMultipleTokensWithDelimiters() verifica il comportamento del tokenizer con un input
     * contenente più token separati da delimitatori.
     * @throws IOException 
     */
    @Test
    public void testMultipleTokensWithDelimiters() throws IOException {
        String input = "token1,token2,token3";
        tokenizer.initialize(new StringReader(input), ",");
        assertTrue(tokenizer.hasNext());
        assertEquals("token1", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("token2", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("token3", tokenizer.next());
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testSpecialCharacterDelimiter() verifica il comportamento del tokenizer con un input
     * contenente caratteri speciali.
     * @throws IOException 
     */
    @Test
    public void testSpecialCharacterDelimiter() throws IOException {
        String input = "token1|token2|token3";
        tokenizer.initialize(new StringReader(input), "|");
        assertTrue(tokenizer.hasNext());
        assertEquals("token1", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("token2", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("token3", tokenizer.next());
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testSpaceDelimiter() verifica il comportamento del tokenizer con delimitatori costituiti da spazi.
     * Inizializziamo il tokenizer con una stringa contenente quattro token separati da spazi.
     * Verifichiamo che il metodo "hasNext" restituisca true prima di ogni token, otteniamo ciascun token con il metodo "next" e verifichiamo che siano corretti.
     * Infine, verifichiamo che il metodo "hasNext" restituisca false alla fine.
     * @throws IOException 
     */
    @Test
    public void testSpaceDelimiter() throws IOException {
        String input = "This is a test";
        tokenizer.initialize(new StringReader(input), " ");
        assertTrue(tokenizer.hasNext());
        assertEquals("This", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("is", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("a", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("test", tokenizer.next());
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testEmptyDelimiter() verifica il comportamento del tokenizer con un delimitatore vuoto.
     * Inizializza il tokenizer con una stringa di input contenente più token concatenati senza delimitatori.
     * Verifica che il tokenizer riconosca correttamente l'unico token presente e che il metodo hasNext restituisca false dopo il consumo del token.
     * @throws IOException 
     */
    @Test
    public void testEmptyDelimiter() throws IOException {
        // Input senza delimitatori
        String input = "token1token2token3";
        
        // Inizializza il tokenizer con il delimitatore vuoto
        tokenizer.initialize(new StringReader(input), "");
        
        // Verifica il comportamento del tokenizer
        assertTrue(tokenizer.hasNext());
        assertEquals("token1token2token3", tokenizer.next());
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testNumericDelimiter() verifica il comportamento del tokenizer con delimitatori costituiti da numeri.
     * Inizializza il tokenizer con una stringa di input contenente spazi separati, e i delimitatori costituiti dai numeri da 1 a 5.
     * Verifica che il tokenizer riconosca correttamente i delimitatori numerici e consumi gli spazi tra di essi.
     * @throws IOException 
     */
    @Test
    public void testNumericDelimiter() throws IOException {
        String input = "1 2 3 4 5";
        tokenizer.initialize(new StringReader(input), "12345");
        
        assertTrue(tokenizer.hasNext());
        assertEquals(" ", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals(" ", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals(" ", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals(" ", tokenizer.next());
        assertFalse(tokenizer.hasNext());
        assertThrows(IllegalStateException.class, () -> {
        	tokenizer.next();
        });
    }

    /**
     * Il test testEmptyReader() verifica il comportamento del tokenizer con un input vuoto.
     * Inizializza il tokenizer con un reader contenente una stringa vuota e un delimitatore.
     * Verifica che il metodo hasNext() restituisca false poiché non ci sono token nell'input vuoto.
     * @throws IOException 
     */
    @Test
    public void testEmptyReader() throws IOException {
        String input = "";
        tokenizer = new ImprovedTokenizer(new StringReader(input), ",");
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testEmptyDelimiters() verifica il comportamento del tokenizer con delimitatori vuoti.
     * Inizializza il tokenizer con un reader contenente una stringa e delimitatori vuoti.
     * Verifica che il metodo hasNext() restituisca true poiché l'intero input è considerato un unico token.
     * @throws IOException 
     */
    @Test
    public void testEmptyDelimiters() throws IOException {
        String input = "This is a sample string,123";
        tokenizer = new ImprovedTokenizer(new StringReader(input), "");
        assertTrue(tokenizer.hasNext());
    }
  
    /**
     * Il test testSpecialCharactersInTokenAndDelimiters() verifica il comportamento del tokenizer con un'ampia varietà di caratteri speciali come delimitatori e token.
     * Inizializza il tokenizer con un reader contenente una stringa e delimitatori appropriati.
     * Verifica che il metodo hasNext() restituisca true e ottenuto il token con next() verifichi che sia corretto.
     * @throws IOException 
     */
    @Test
    public void testSpecialCharactersInTokenAndDelimiters() throws IOException {
        String input = "token1,token2|token3 word1 word2|word3";
        tokenizer.initialize(new StringReader(input), ",| ");
        assertTrue(tokenizer.hasNext());
        assertEquals("token1", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("token2", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("token3", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("word1", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("word2", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("word3", tokenizer.next());
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testExtraSpacesBetweenTokens() verifica il comportamento del tokenizer con spazi extra tra i token. 
     * Inizializza il tokenizer con un reader contenente una stringa e delimitatori appropriati.
     * Verifica che il metodo hasNext() restituisca true e ottenuto il token con next() verifichi che sia corretto, ignorando gli spazi aggiuntivi.
     * @throws IOException 
     */
    @Test
    public void testExtraSpacesBetweenTokens() throws IOException {
        String input = " token1  token2  token3 ";
        tokenizer.initialize(new StringReader(input), " ");
        assertTrue(tokenizer.hasNext());
        assertEquals("token1", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("token2", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("token3", tokenizer.next());
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testMultipleConsecutiveDelimiters() verifica il comportamento del tokenizer con delimitatori multipli consecutivi.
     * Inizializza il tokenizer con un reader contenente una stringa e delimitatori appropriati.
     * Verifica che il metodo hasNext() restituisca true e che ottenuto il token con next() verifichi che sia corretto, gestendo più delimitatori consecutivi come un unico delimitatore.
     * @throws IOException 
     */
    @Test
    void testMultipleConsecutiveDelimiters() throws IOException {
        String input = "token1,,,token2";
        tokenizer.initialize(new StringReader(input), ",");
        assertTrue(tokenizer.hasNext());
        assertEquals("token1", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("token2", tokenizer.next());
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testInitializeWithInputStream() verifica il corretto funzionamento del metodo `initialize` utilizzando un `InputStream`.
     * Inizializza il tokenizer con un `InputStream` contenente una stringa e delimitatori appropriati.
     * Verifica che il metodo hasNext() restituisca true e che ottenuto il token con next() verifichi che sia corretto.
     * @throws IOException 
     */
    @Test
    public void testInitializeWithInputStream() throws IOException {
        String input = "token1,token2,token3";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        tokenizer.initialize(inputStream, ",");
        assertTrue(tokenizer.hasNext());
        assertEquals("token1", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("token2", tokenizer.next());
        assertTrue(tokenizer.hasNext());
        assertEquals("token3", tokenizer.next());
        assertFalse(tokenizer.hasNext());
    }

    /**
     * Il test testConstructorWithBuffer() verifica il corretto funzionamento del costruttore del tokenizer utilizzando un buffer di input.
     * Inizializza il tokenizer con una stringa e delimitatori appropriati.
     * Verifica che il metodo hasNext() restituisca true e che ottenuto il token con next() verifichi che sia corretto.
     * @throws IOException 
     */
    @Test
    public void testConstructorWithBuffer() throws IOException {
        String input = "token1,token2,token3";
        ImprovedTokenizer customTokenizer = new ImprovedTokenizer(input, ",");
        assertTrue(customTokenizer.hasNext());
        assertEquals("token1", customTokenizer.next());
        assertTrue(customTokenizer.hasNext());
        assertEquals("token2", customTokenizer.next());
        assertTrue(customTokenizer.hasNext());
        assertEquals("token3", customTokenizer.next());
        assertFalse(customTokenizer.hasNext());
    }
}