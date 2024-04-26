package student.giuseppeMazzoccaUno;

import static org.junit.Assert.assertTrue;
import ClassUnderTest.Inflection;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentTest {
	
// la tecnica utilizzata per cercare di avere la coverage maggiore è stata quella di testare con una classe di valore e, una volta testata, valutare
// quale parte del codice non era stata ancora coperta e, di conseguenza, ho effettuato altri test fino a quando non ho coperto tutto il codice del
// metodo da testare

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
	void testInflectionString() {
		assertNotNull(new Inflection("pattern"));
	}

	
	@Test
	void testInflectionStringString() {
		assertNotNull(new Inflection("pettern","replacement"));
	}

	
	@Test
	void testInflectionStringStringBoolean() {
		assertNotNull(new Inflection("pattern","replacement",false));
	}

	@Test
	void testMatch() {
		Inflection inflection = new Inflection("pattern");
		assertTrue(inflection.match("man")==false);
	}
	

	@Test
	void testReplace() {
		Inflection inflection = new Inflection("pattern");
		assertTrue(inflection.replace("man").equals("man"));
	}
	
	/*
	@Test
	void testPluralize() {
		assertTrue(Inflection.pluralize("book").equals("books"));
	}*/
	
	// commentando plural("$", "s");
	@Test
	void testPluralize2() {
		assertTrue(Inflection.pluralize("book").equals("book"));
	}
	
	@Test
	void testPluralizeCountable() {
		assertTrue(Inflection.pluralize("sex").equals("sexes"));
	}
	
	
	@Test
	void testPluralizeUnCountable() {
		assertTrue(Inflection.pluralize("sheep").equals("sheep"));
	}
	
	
	@Test
	void testPluralizeIrregular() {
		assertTrue(Inflection.pluralize("person").equals("people"));
	}
	
	// commentando singular("s$", "")
	@Test
	void testSingularize() {
		assertTrue(Inflection.singularize("books").equals("books"));
	}
	
	@Test
	void testSingularizeCountable() {
		assertTrue(Inflection.singularize("sexes").equals("sex"));
	}
	
	
	@Test
	void testSingularizeUnCountable() {
		assertTrue(Inflection.singularize("series").equals("series"));
	}
	
	
	@Test
	void testSingularizeIrregular() {
		assertTrue(Inflection.singularize("men").equals("man"));
	}

	@Test
	void testIsUncountable() {
		assertTrue(Inflection.isUncountable("rice")==true);
	}

}
