package student.andreaDiMarcoDue;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.Inflection;

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
	void testInflectionString() {
		assertThrows(Exception.class,()-> new Inflection("(.*)ix$)"));
	}

	@Test
	void testInflectionStringString() {
		assertThrows(Exception.class,()-> new Inflection("(.*)ix$","$1ices"));
	}

	@Test
	void testInflectionStringStringBoolean() {
		assertThrows(Exception.class,()-> new Inflection("(.*)ix$","$1ices",false));
	}
	
	@Test
	void testMatch() {
		Inflection word = new Inflection("(.*)ix$","$1ices",true);			//Faccio due test per testare il metodo Match per poter coprire entrambi
		assertTrue(word.match("fix"));										//i branch dell'if(ignorCase)
	}
	
	@Test
	void testMatch2() {
		Inflection word = new Inflection("(.*)ix$","$1ices",false);		
		assertTrue(word.match("fix"));
	}
	
	@Test
	void testReplace() {												 //Provo il replace della parola sia considerando il caso case sensitive
		Inflection word = new Inflection("(.*)ix$","$1ices",false);      //sia il caso case insensitive
		assertEquals("fices",word.replace("fix"));
	}
	
	@Test
	void testReplace2() {
		Inflection word = new Inflection("(.*)ix$","$1ices",true);
		assertEquals("Fices",word.replace("FIX"));
	}
	
	
	@Test
	void testPluralize() {										  //Per il test del metodo Pluralize provo una stringa uncountable, una stringa che segue
		assertEquals("parties",Inflection.pluralize("party"));    //una delle regole di pluralizzazione e poi un ultima parola che non rientra in nessuna 
	}														      //delle due categorie menzionate precedentemente e quindi verrà restituita non modificata dalla funzione

	@Test
	void testPluralize2() {
		
		assertEquals("fish",Inflection.pluralize("fish"));
	}
	
	@Test
	void testPluralize3() {
		System.out.println(Inflection.pluralize("88"));				    //Non riuscirò mai ad entrare nel return word finale poichè questa prima regola 
		assertThrows(Exception.class, ()-> Inflection.pluralize("88")); //plural("$", "s") mi aggiunge la s a qualsiasi sequenza di caratteri anche a
	}																		//questa che non ha senso compiuto e contiene numeri alla fine
			
	@Test																	//Per il test Singularize do ingresso una parola uncountable così che mi copra 
	void testSingularize() {												//ed entri nel primo if, una parola plurale da rendere singolare con le regole
		assertEquals("fish",Inflection.singularize("fish")); 				//di singolarizzazione presenti e infine una parola singolare che mi ritorna 
	}																		//così com'è
	
	@Test
	void testSingularize2() {
		assertEquals("shoe",Inflection.singularize("shoes")); 
	}
	
	@Test
	void testSingularize3() {
		assertEquals("tomato",Inflection.singularize("tomato"));
	}
	
	@Test
	void testIsUncountable() {												 //Per il test del metodo Uncountable do in input sia una parola uncountable
		assertTrue(Inflection.isUncountable("sheep")); //così da entrare nell'if (w.equalsIgnoreCase(word)) una volta e l'altra no
	}																		
	
	@Test
	void testIsUncountable2() {
		assertFalse(Inflection.isUncountable("tomato"));
	}
	
}
