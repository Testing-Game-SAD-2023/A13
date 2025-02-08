package student.claudioSpasianoDue;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.SubjectParser;

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
	void testSubjectParser() {
		SubjectParser x = new SubjectParser("12 FirstTest [3/8]");
		assertEquals(12,x.getId());
		assertEquals("FirstTest ",x.getTitle());
		assertEquals(8,x.getUpperRange());
		assertEquals(3,x.getThisRange());
		x=null;
		assertNull(x);
	}

	@Test
	void testGetId() {
		SubjectParser x = new SubjectParser("02 SecondTest");
		assertEquals(02,x.getId());
		x=null;
		assertNull(x);
	}
	

	@Test
	void testGetThisRange() {
		SubjectParser x = new SubjectParser("ThirdTest [2/7]");
		assertEquals(2,x.getThisRange());
		x=null;
		assertNull(x);
	}
	
	
	@Test
	void testGetThisRangeWithError() {
		//se non viene specificato nulla 1 viene fornito come valore di default dal costruttore
		SubjectParser x = new SubjectParser("Test senza range");
		assertEquals(1,x.getThisRange()); 
		x=null;
		assertNull(x);
	}

	@Test
	void testGetUpperRange() {
		SubjectParser x = new SubjectParser("4th [3/8]");
		assertEquals(8,x.getUpperRange());
		x=null;
		assertNull(x);
	}
	
	@Test
	void testGetUpperRangeWithError() {
		//se non viene specificato nulla 1 viene fornito come valore di default dal costruttore
		SubjectParser x = new SubjectParser("Test senza UpperRange");
		assertEquals(1,x.getUpperRange());
		x=null;
		assertNull(x);
	}

	@Test
	void testGetRangeString() {
		SubjectParser x = new SubjectParser("5th [3/6]");
		assertEquals("[3/6]",x.getRangeString());
		x=null;
		assertNull(x);
	}
	

@Test
	void testGetTitle() {
		SubjectParser x = new SubjectParser("6 Test of GetTitle");
		assertEquals("Test of GetTitle",x.getTitle());
		x=null;
		assertNull(x);
	} 
	

	
	@Test
	void testMain() {
		String[] args= {"7 MainTest [1/10]"};
		SubjectParser.main(args);
	}
	
	
	@Test 
	void TestPerParentesiTonde() 
	//percorre il primo blocco try di messageParts che è relativo alle parentesi tonde
	{
		String s = "8 Test Tonde (5/10)";
		SubjectParser x = new SubjectParser(s);
		assertEquals(5,x.getThisRange());
		x=null;
		assertNull(x);
	}
	
	

	@Test
	void TestSenzaCifreSlash() 
	//realizza il caso Character.isDigit(nextchar) == false) && nextchar != '/'
	{
		String s = "Test ParentesiSenzaCifre (ABC)";
		SubjectParser x = new SubjectParser(s);
		assertEquals("ParentesiSenzaCifre (ABC)",x.getTitle());
		x=null;
		assertNull(x);
	}
	
	@Test
	void TestParentesiCifreSenzaSlash()
	{	
		//realizza il caso in cui ci sono delle parentesi con cifre ma senza slash
		String s = "Test ParentesiSenzaSlash [123]";
		SubjectParser x = new SubjectParser(s);
		assertEquals("ParentesiSenzaSlash ",x.getTitle());
		x=null;
		assertNull(x);
	}
	
	@Test
		void TestMainSenzaArgomenti()
		{
		String[] args2= {};
		SubjectParser.main(args2);		
		}
	
}
