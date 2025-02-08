package student.giovanniDiStazioDue;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.SubjectParser;

class StudentTest {
	
	static SubjectParser s,t,p;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		s = new SubjectParser(null);
		t = new SubjectParser("test");
		p = new SubjectParser("(1/2)");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		s=null;
		t=null;
		p=null;
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetId() {
		s.getId();
		t.getId();
		p.getId();
	}

	@Test
	void testGetThisRange() {
		s.getThisRange();
		t.getThisRange();
		p.getThisRange();
		
		SubjectParser str = new SubjectParser("[1/2]");
		str.getThisRange();
		str = null;
	}

	@Test
	void testGetUpperRange() {
		s.getUpperRange();
		t.getUpperRange();
		p.getUpperRange();
	}

	@Test
	void testGetRangeString() {
		s.getRangeString();
		t.getRangeString();
		p.getRangeString();
	}
	
	@Test
	void testGetTitle() {
		s.getTitle();
		t.getTitle();
		p.getTitle();
		SubjectParser str = new SubjectParser("[-1/2]");
		str.getTitle();
		str = null;
		
	}

	@Test
	void testMain() {
		String[] test = new String[1];
		test[0] = "testMainArgs";
		SubjectParser.main(test);
		
		/*
		String[] test2 = new String[0];
		SubjectParser.main(test);*/
	}

}
