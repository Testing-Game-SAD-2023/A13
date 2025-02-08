package student.LorenzoCappellieriUno;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

	//Test dei Costruttori 
	Reader s1 = new StringReader("Ciao a tutti.");
	@Test
	void testImprovedStreamTokenizerReader() {
		assertThrows(Exception.class,()-> new ImprovedStreamTokenizer(s1));

		}

	Reader s2 = new StringReader("Ciao-a-tutti.*Prova //Prova commento");
	@Test
	void testImprovedStreamTokenizerReaderStringStringBoolean() {
		assertThrows(Exception.class,()-> new ImprovedStreamTokenizer(s2,"-","*",true));
	}

	
	//Test della funzione NextWord
	
	Reader s3 = new StringReader("Vincenzo");
	@Test
	void testNextWord1() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s3);
		assertEquals("Vincenzo",tokenizer.nextWord());
	}
	
    Reader s_vuota = new StringReader("");
	@Test
	void testNextWord2() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s_vuota);	
		assertNull(tokenizer.nextWord());
	}   
	
	Reader s_exception = new StringReader("''");
	
	@Test
	void testNextWord3() {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s_exception);
		Exception exception = assertThrows(Exception.class,()-> tokenizer.nextWord());
		assertEquals("non-string", exception.getMessage());	
	}
	
	@Test
	void testNextWord4() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s3);
		assertNotEquals("Stessa Spiaggia,Stesso Mare.",tokenizer.nextWord());
	}
	
	@ParameterizedTest
	@CsvSource({
		"1234",
		"Vincenzo"
	})
	void testParametraized(String s) throws IOException {
		Reader strings = new StringReader(s);
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(strings);
		assertEquals(s,tokenizer.nextWord());
	}
	
    
	//Test per la funzione NextInteger
	Reader nums = new StringReader("1234");
	@Test
	void testNextInteger1() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(nums);
		assertEquals(1234,tokenizer.nextInteger());
	}
	
	@Test
	void testNextInteger2() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s_vuota);
		assertNull(tokenizer.nextInteger());
	}
	@Test
	void testNextInteger3() {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s_exception);
		Exception exception = assertThrows(Exception.class,()-> tokenizer.nextInteger());
		assertEquals("non-number", exception.getMessage());
	}
	
	@Test
	void testNextInteger4() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(nums);
		assertNotEquals(0,tokenizer.nextInteger());
	}
	
	//Test per la funzione NextInt	
	@Test
	void testNextInt1() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(nums);
		assertEquals(1234,tokenizer.nextInt());
	}
	
	@Test
	void testNextInt2() {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s_vuota);
		Exception exception = assertThrows(Exception.class,()-> tokenizer.nextInt());
		assertEquals("unexpected end of input", exception.getMessage());
	}
	
	@Test
	void testNextInt3() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(nums);
		assertNotEquals(23, tokenizer.nextInt());
	}

	//Test per la funzione NextBoolean
   	
	@Test
	void testNextBoolean1() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s_vuota);
		assertNull(tokenizer.nextBoolean());
	}

	@Test
	void testNextBoolean2() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s3);
		assertEquals(false,tokenizer.nextBoolean());
	}
	
	
	@Test
	void testNextBoolean3() {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s_exception);
		Exception exception = assertThrows(Exception.class,()-> tokenizer.nextBoolean());
		assertEquals("non-boolean", exception.getMessage());
	}
	Reader s4 = new StringReader("1");
	@Test
	void testNextBoolean4() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s4);
		assertFalse(tokenizer.nextBoolean());
	}
	
	StringReader s5 = new StringReader("0");
	
	@Test
	void testNextBoolean5() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s5);
		assertFalse(tokenizer.nextBoolean());
	}
	/*Non sono riuscito a eseguire questi test perchè non legge che sto passando un numero,
	 ho comunque pensato a questi test da svolgere.

	
	@Test
	void testNextBoolean6() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s5);
		assertTrue(tokenizer.nextBoolean());
	}
	@Test
	void testNextBoolean7() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(nums);
		assertTrue(tokenizer.nextBoolean());
	}
	*/
	
	//Test per la funzione NextBool
	@Test
	void testNextBool1() {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s_vuota);
		Exception exception = assertThrows(Exception.class,()-> tokenizer.nextBool());
		assertEquals("unexpected end of input", exception.getMessage());
	}
	
	@Test
	void testNextBool2() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s1);
		assertFalse(tokenizer.nextBool());
	}
	
	//Test per la funzione nextByteObject
	String inputString = "Andrea";
	byte[] byteArray = inputString.getBytes();
	@Test
	void testByteObject1() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s3);
		assertNotEquals(byteArray,tokenizer.nextByteObject());
	}
	
	@Test
	void testByteObject2() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s_vuota);
		assertNull(tokenizer.nextByteObject());
	}
	
	@Test
	void testByteObject3() {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s_exception);
		Exception exception = assertThrows(Exception.class,()-> tokenizer.nextByteObject());
		assertEquals("non-byte", exception.getMessage());
	}
	
	//Test per la funzione nextByte
	@Test
	void testByte1() throws IOException {
		ImprovedStreamTokenizer tokenizer = new ImprovedStreamTokenizer(s1);
		assertNotEquals(byteArray,tokenizer.nextByte());
	}
	
	//Test per la funzione charToHex
	@Test
	void testcharToHex1() {
		assertNotEquals(byteArray,ImprovedStreamTokenizer.charToHex('a'));
	}
	
	@Test
	void testcharToHex2() {
		assertNotEquals(byteArray,ImprovedStreamTokenizer.charToHex('2'));
	}
	
}
