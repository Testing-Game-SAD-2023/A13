package student.marioMigliuoloUno;

import static org.junit.Assert.assertThrows;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.ImprovedTokenizer;

@SuppressWarnings("unused")
class StudentTest {

	static ImprovedTokenizer it;
	static StringReader r;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {

	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {

	}

	@BeforeEach
	void setUp() throws Exception {
		
		String buffer = "";
		it = new ImprovedTokenizer(buffer,"");
		assumeNotNull(it);
	}

	@AfterEach
	void tearDown() throws Exception {
		it=null;
		r=null;
		assertTrue(it==null);
		assertTrue(r==null);
	}

	@Test
	void testImprovedTokenizerReaderString() throws FileNotFoundException {
		//fail("Not yet implemented");
		Reader input= new FileReader("./test.txt");
		ImprovedTokenizer it = new ImprovedTokenizer(input,"");
		assumeNotNull(it);
	}

	@Test
	void testInitializeReaderString() {
		//metodo già completamente coperto con il test precedente
	}

	@Test
	void testInitializeInputStreamString() throws FileNotFoundException {
		InputStream in= new FileInputStream("./test.txt");
		it.initialize(in,"-");
	}

	@Test
	void testImprovedTokenizerStringString() {
		String buffer = "";
		ImprovedTokenizer it = new ImprovedTokenizer(buffer,"");
		assumeNotNull(it);
	}

	@Test
	void testHasNext() {
		StringReader r= new StringReader("buffer");
		it.initialize(r,"-");
		assertAll(()->it.hasNext());
	}
	
	@Test
	void testHasNext2() {
		String buffer = "prova test senza token";
		ImprovedTokenizer it = new ImprovedTokenizer(buffer,"pdf");
		assumeNotNull(it);
		assertAll(()-> it.hasNext());
	}

	@Test
	void testKeepParsing() {
		String buffer="after-\0";
		ImprovedTokenizer it = new ImprovedTokenizer(buffer,"-");
		assertTrue(it.keepParsing(0));
	}

	@Test
	void testAdvance() throws IOException {
		//implicitamente coperto da altri test
		String buffer="token-token-tok";
		ImprovedTokenizer it = new ImprovedTokenizer(buffer,"-");
		//assertAll(()-> it.next());
		assertAll(()->it.advance());
	}

	@Test
	void testStop() {
		String buffer="token1-token2";
		ImprovedTokenizer it = new ImprovedTokenizer(buffer,"-");
		assertAll(()-> it.next());
		assertAll(()->it.stop());
		assertAll(()->it.hasNext());
	}
	
	@Test
	void testStop2() throws IOException {
		String buffer="-s";
		ImprovedTokenizer it = new ImprovedTokenizer(buffer,"-");
		it.next();
		assertAll(()->it.advance());
	}


	@Test
	void testStart() {
		//fail("Not yet implemented");
		//implicitamente testata con i metodi precedenti
	}

	@Test
	void testBeforeToken() {
		String buffer="token-token-tok";
		ImprovedTokenizer it = new ImprovedTokenizer(buffer,"-");
		assertAll(()-> it.next());
		assertAll(()->it.beforeToken('-'));
	}

	@Test
	void testMatchingToken() {
		//implicitamente testata con i metodi precedenti
	}
	
	@Test
	void testAfterToken() throws IOException {
		String buffer="token-token-tok";
		ImprovedTokenizer it = new ImprovedTokenizer(buffer,"-");
		assertAll(()-> it.next());
		assertAll(()->it.afterToken('-'));
		assertAll(()->it.advance());
	}
	
	@Test
	void testNext() throws IOException {
		String buffer="prova-token-";
		ImprovedTokenizer it = new ImprovedTokenizer(buffer,"-");
		assertAll(()-> it.hasNext());
		assertAll(()-> it.next());
	}
	
	@Test
	void testNext2() throws IOException {
		String buffer="";
		ImprovedTokenizer it = new ImprovedTokenizer(buffer,"");
		assertAll(()-> it.hasNext());
		it.next();
	}
	
	@Test
	void testNext3() throws IOException {
		String buffer="provasenzatoken e stringa lunga";
		ImprovedTokenizer it = new ImprovedTokenizer(buffer,"-");
		assertAll(()-> it.hasNext());
		assertAll(()-> it.next());
		assertAll(()-> it.hasNext());
	}
	
	@Test
	void testPreviousDelimiter() {
		String buffer="provasenza token";
		ImprovedTokenizer it = new ImprovedTokenizer(buffer,"-");
		assertAll(()-> it.previousDelimiter());
	}
	
}