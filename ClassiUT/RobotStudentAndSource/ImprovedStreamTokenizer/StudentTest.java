package student.alessandraMaraiaUno;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.ImprovedStreamTokenizer;

class StudentTest {

    private ImprovedStreamTokenizer tokenizer;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        // Eseguo l'inizializzazione generale prima dell'esecuzione di tutti i test, se necessario
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        // Eseguo la pulizia generale dopo l'esecuzione di tutti i test, se necessario
    }

    @BeforeEach
    void setUp() throws Exception {
        // Inizializzo il tokenizer con uno StringReader vuoto prima di ogni test
        tokenizer = new ImprovedStreamTokenizer(new StringReader(""));
    }

    @AfterEach
    void tearDown() throws Exception {
        // Eseguo la pulizia dopo l'esecuzione di ogni test
        tokenizer = null;
    }

    @Test
    void testImprovedStreamTokenizerReader() {
        // Verifico il costruttore con un ImprovedStreamTokenizer basato su StringReader
        tokenizer = new ImprovedStreamTokenizer(new StringReader("Test string"));

        // Verifico la funzionalità di base di nextWord
        try {
            assertEquals("Test", tokenizer.nextWord());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assertEquals("string", tokenizer.nextWord());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assertNull(tokenizer.nextWord());
        } catch (IOException e) {
            e.printStackTrace();
        } // Dovrebbe essere nullo alla fine dello stream
    }

    @Test
    void testImprovedStreamTokenizerReaderStringStringBoolean() {
        // Verifico il costruttore con ImprovedStreamTokenizer basato su StringReader,
        // specificando delimiters, quoteChars e gestione delle citazioni
         tokenizer = new ImprovedStreamTokenizer(new StringReader("123 456 'test string'"), " \t\n\r", "'\"", true);

        // Verifico nextInteger
        try {
            assertEquals(123, tokenizer.nextInteger().intValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Verifico nextInt
        try {
            assertEquals(456, tokenizer.nextInt());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Verifico nextWord con stringa quotata
        try {
            assertEquals("t string", tokenizer.nextWord());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assertNull(tokenizer.nextWord());
        } catch (IOException e) {
            e.printStackTrace();
        } // Dovrebbe essere nullo alla fine dello stream
    }

    @Test 
    // Verifico il metodo nextWord(): verifica se il tokenizer estrae correttamente le parole
    void testNextWord() {
        tokenizer = new ImprovedStreamTokenizer(new StringReader(" Corso di Software Testing "));
        try {
            assertEquals("Corso", tokenizer.nextWord());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assertEquals("di", tokenizer.nextWord());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assertEquals("Software", tokenizer.nextWord());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assertEquals("Testing", tokenizer.nextWord());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testNextInteger() {
        // Verifico nextInteger: verifica se il tokenizer estrae correttamente gli interi
        tokenizer = new ImprovedStreamTokenizer(new StringReader("42"));
        try {
            assertEquals(Integer.valueOf(42), tokenizer.nextInteger());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Verifico il metodo nextInt(): verifica se il tokenizer estrae correttamente gli interi primitivi
    @Test
    void testNextInt()  {
        tokenizer = new ImprovedStreamTokenizer(new StringReader("123"));
        try {
            assertEquals(123, tokenizer.nextInt());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Verifico il metodo nextBoolean(): verifica se il tokenizer estrae correttamente i booleani
    @Test
    void testNextBoolean()  {
        tokenizer = new ImprovedStreamTokenizer(new StringReader("true"));
        try {
            assertTrue(tokenizer.nextBoolean());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Verifico il metodo nextBool(): verifica se il tokenizer estrae correttamente i booleani primitivi
    @Test
    void testNextBool()  {
        tokenizer = new ImprovedStreamTokenizer(new StringReader("false"));
        try {
            assertFalse(tokenizer.nextBool());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Verifico il metodo nextByteObject(): verifica se il tokenizer estrae correttamente i byte come oggetti
    @Test
    void testNextByteObject() {
        tokenizer = new ImprovedStreamTokenizer(new StringReader("1A"));
        try {
            assertEquals(Byte.valueOf((byte) 0x1A), tokenizer.nextByteObject());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Verifico il metodo nextByte(): verifica se il tokenizer estrae correttamente i byte primitivi
    @Test
    void testNextByte()  {
        tokenizer = new ImprovedStreamTokenizer(new StringReader("0F"));
        try {
            assertEquals((byte) 0x0F, tokenizer.nextByte());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Verifico il metodo parseByte(): verifica se il parser converte correttamente le stringhe in byte
    @Test
    void testParseByte() {
        assertEquals((byte) 0x0A, ImprovedStreamTokenizer.parseByte("0A"));
        assertEquals((byte) 0x1F, ImprovedStreamTokenizer.parseByte("1F"));
    }

    // Verifico il metodo charToHex(): verifica se il metodo converte correttamente i caratteri esadecimali
    @Test
    void testCharToHex() {
        assertEquals((byte) 0x0A, ImprovedStreamTokenizer.charToHex('A'));
        assertEquals((byte) 0x0F, ImprovedStreamTokenizer.charToHex('F'));
        assertEquals((byte) 0x05, ImprovedStreamTokenizer.charToHex('5'));
    }

    // Verifico il metodo initializeSyntax(): verifica se il tokenizer viene inizializzato correttamente
    @Test
    void testInitializeSyntax() throws IOException {
        // Creo un ImprovedStreamTokenizer con un Reader di esempio
        tokenizer = new ImprovedStreamTokenizer(new StringReader("test input"));

        // Chiamo il metodo initializeSyntax per inizializzare la grammatica
        tokenizer.initializeSyntax();

        // Verifico che il tokenizer sia stato inizializzato correttamente
        assertEquals(StreamTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals(StreamTokenizer.TT_WORD, tokenizer.nextToken());
        assertEquals(StreamTokenizer.TT_EOF, tokenizer.nextToken());
    }

    // Verifico il metodo whiteSpaceCharacter(): verifica se il metodo imposta correttamente il carattere di spaziatura
    @Test
    void testWhiteSpaceCharacter() {
        // Creo un ImprovedStreamTokenizer con un Reader di esempio
        tokenizer = new ImprovedStreamTokenizer(new StringReader("   test   citazioni"));

        try {
            // Verifico che il tokenizer ignori i primi spazi
            assertEquals(StreamTokenizer.TT_WORD, tokenizer.nextToken());
            assertEquals("test", tokenizer.sval);

            // Verifico che il tokenizer ignori gli spazi intermedi
            assertEquals(StreamTokenizer.TT_WORD, tokenizer.nextToken());
            assertEquals("citazioni", tokenizer.sval);

            // Verifico che la fine del file sia riconosciuta correttamente
            assertEquals(StreamTokenizer.TT_EOF, tokenizer.nextToken());
        } catch (IOException e) {
            fail("Errore durante la lettura del token: " + e.getMessage());
        }
    }

    // Verifico il metodo whiteSpaceCharacters(): verifica se il metodo imposta correttamente i caratteri di spaziatura
    @Test
    void testWhiteSpaceCharacters() {
        // Creo un ImprovedStreamTokenizer con un Reader di esempio
        ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(new StringReader("test input"));

        // Inizializzo i caratteri di spaziatura per includere solo il carattere 't'
        tokenizer.whiteSpaceCharacter('t');

        try {
            // Verifico che il tokenizer ignori i primi spazi e legga correttamente la parola "test"
            assertEquals(StreamTokenizer.TT_WORD, tokenizer.nextToken());
            assertEquals("es", tokenizer.sval);  // il token dovrebbe essere "es", poiché 't' è uno spazio

            // Verifico che il tokenizer ignori gli spazi intermedi e legga correttamente la parola "input"
            assertEquals(StreamTokenizer.TT_WORD, tokenizer.nextToken());
            assertEquals("inpu", tokenizer.sval);  // il token dovrebbe essere "inpu", poiché 't' è uno spazio

            // Verifico che la fine del file sia riconosciuta correttamente
            assertEquals(StreamTokenizer.TT_EOF, tokenizer.nextToken());
        } catch (IOException e) {
            fail("Errore durante la lettura del token: " + e.getMessage());
        }
    }

    @Test
    void testQuoteCharacters() {
        // Creo un ImprovedStreamTokenizer con un Reader di esempio
        tokenizer = new ImprovedStreamTokenizer(new StringReader("'test' \"citazioni\""));

        try {
            // Chiamo il metodo quoteCharacters per impostare i caratteri di citazione
            tokenizer.quoteCharacters("\"'");

            // Verifico che il primo carattere di citazione sia riconosciuto correttamente
            assertEquals('\'', (char) tokenizer.nextToken(), "Primo carattere di citazione non corretto");

            // Verifico che la stringa racchiusa tra apici singoli sia riconosciuta correttamente
            assertEquals("test", tokenizer.sval, "Stringa tra apici singoli non corretta");

            // Verifico che il secondo carattere di citazione sia riconosciuto correttamente
            assertEquals('\"', (char) tokenizer.nextToken(), "Secondo carattere di citazione non corretto");

            // Verifico che la stringa racchiusa tra virgolette doppie sia riconosciuta correttamente
            assertEquals("citazioni", tokenizer.sval, "Stringa tra virgolette doppie non corretta");

            // Verifico che la fine del file sia riconosciuta correttamente
            assertEquals(StreamTokenizer.TT_EOF, tokenizer.nextToken(), "Fine del file non corretta");
        } catch (IOException e) {
            fail("Errore durante la lettura del token: " + e.getMessage());
        }
    }

    @Test
    void testInitializeSyntaxStringStringBoolean() throws IOException {
        // Creo un ImprovedStreamTokenizer con un Reader di esempio
        tokenizer = new ImprovedStreamTokenizer(new StringReader("test input"));

        // Chiamo il metodo initializeSyntax con specifiche caratteristiche
        tokenizer.initializeSyntax(" \t\n\r", "\"'", true);

        // Verifico che il tokenizer gestisca correttamente i token in base agli input forniti
        assertEquals(StreamTokenizer.TT_WORD, tokenizer.nextToken());  // Token "test"
        assertEquals(StreamTokenizer.TT_WORD, tokenizer.nextToken());  // Token "input"
        assertEquals(StreamTokenizer.TT_EOF, tokenizer.nextToken());   // Fine del file
    }
}
