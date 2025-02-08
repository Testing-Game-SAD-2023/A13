package student.vincenzoCiccarelliUno;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ClassUnderTest.SubjectParser;



class StudentTest {
	
	
	SubjectParser sID1 = new SubjectParser("123");
	SubjectParser sID2 = new SubjectParser("Testo");
	
	String[] prova1 = new String[] {"Vincenzo","Testare"};
	String[] prova2 = new String[] {"123", "456"};
	String[] prova3 = new String[] {};
	
	String title = new String("[Hello world]!");
	String vuota = new String("");
	String number = new String("(1/30)");
	String number2 = new String("[2/20]");
	String number3 = new String("[2/3), (5/6]");
	
	SubjectParser sub = new SubjectParser(vuota);
	SubjectParser sub1 = new SubjectParser(title);
	SubjectParser sub2 = new SubjectParser(number);
	SubjectParser sub3 = new SubjectParser(number2);
	SubjectParser sub4 = new SubjectParser(number3);
	
	SubjectParser s = new SubjectParser(")");
	SubjectParser s1 = new SubjectParser("Testo");
	SubjectParser s2 = new SubjectParser(number2);
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
	void testSubjectParser() {
		assertThrows(Exception.class,()-> new SubjectParser("abc"));
		}
	
	@Test
	 void testClasse() {
		  SubjectParser c= new SubjectParser(vuota);
		  assertNotNull(c);
		  }
	
	@Test
	void testGetId() {
		assertEquals("ID sbagliato",123,sID1.getId());		
		}
	
	@Test
	void testGetId2() {
		 assertNotEquals(123,sID2.getId());
		}
	
	@Test
	void testGetThisRange() {
		assertEquals(1,sub.getThisRange());
	}
	
	@Test
	void testGetUpperRange() {
		assertNotEquals(1,sub2.getUpperRange());
		}
	
	@Test
	void testGetUpperRange2() {
		assertEquals(30,sub2.getUpperRange());
		}
	
	  
	@Test
	void testGetRangeString() {
 		assertEquals(null,sub.getRangeString());
 	}
	
 	
	@Test
	void testGetRangeString2() {
		assertNotEquals("(15/70)",sub2.getRangeString());
		}
	
	@Test
	void testGetRangeString3() {
		assertEquals("(1/30)",sub2.getRangeString());
	}
	
	
	@Test
	void testGetRangeString4() {
		assertEquals("[2/20]",sub3.getRangeString());
	}

	
	@Test
	void testGetTitle() {
		assertNull(s.getTitle());
	}
	
	@Test
	void testGetTitle2() {
		assertNotEquals("[Hello world]!",sub1.getTitle());
		}

	@Test
	void testgetThisRange3() {
		assertNotEquals("123",sub3.getThisRange());
	}

	
	@Test
	void testMain() {
		s2.main(prova1);
		assertEquals(-1,s2.getId());
	}
	

	@Test
	void testMain2(){
		s.main(prova2);
		assertNotEquals(123,s.getId());
		assertNotEquals("testare",s.getTitle());
		assertEquals(1,s.getUpperRange());
		assertEquals(1,s.getThisRange());
		assertEquals(null,s.getRangeString());
	}

	
	/*
	@Test    //voglio testare il main con una stringa nulla 
	void testMain3() {
		Exception exception = assertThrows(Exception.class, ()-> s.main(prova3));
		assertEquals("Usage: java SubjectParser <args>", exception.getMessage());
	}*/
 

	@ParameterizedTest
	@CsvSource({
		"Ciao",
		"Hello",
		"Software testing"
		})
	void testpar(String test) {
		String subject = s1.getTitle();
		assertFalse("Errore, il titolo non è " +test+" ma è "+subject,test.contentEquals(subject));
	}
	

	@ParameterizedTest
	@CsvSource({
		"Ciao",
		"Hello",
		"Software testing",
		"Testo"
		})
	void testpar2(String test) {
		String subject = s1.getTitle();
		assertTrue("Errore, il titolo non è " +test+" ma è "+subject,test.contentEquals(subject));
	}

}
