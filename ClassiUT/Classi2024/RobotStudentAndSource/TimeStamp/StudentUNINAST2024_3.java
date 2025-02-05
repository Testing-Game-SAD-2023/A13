/*Compila i campi "Nome" e "Cognome" con le informazioni richieste
Nome: Antonio
Cognome: Tufo
Username: antoniotufo2001@gmail.com
UserID: 5
Date: 22/11/2024
*/

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;

public class TestTimeStamp {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testToDateString() {
		TimeStamp timeStamp = new TimeStamp(904521000000L);
		String dateString = timeStamp.toDateString();
		assertEquals("Thu, Feb 07 2036 06:31:46.600", dateString);
	}
	
	@Test
	public void testTimeStamp_String() {
		TimeStamp timeStamp = new TimeStamp("c1a089bd.fc904f6d");
		String dateString = timeStamp.toDateString();
		assertEquals("Tue, Dec 10 2002 15:41:49.987", dateString);
	}
	
	@Test
	public void testTimeStamp_String_1() {
		TimeStamp timeStamp = new TimeStamp("");
		assertEquals(0, timeStamp.ntpValue());
	}
	
	@Test
	public void testTimeStamp_String_2() {
		TimeStamp timeStamp = new TimeStamp("00000000");
		assertEquals(0, timeStamp.ntpValue());
	}
	
	@Test(expected = NumberFormatException.class)
	public void testTimeStamp_String_3() {
	    String x = null;
		TimeStamp timeStamp = new TimeStamp(x);
	}
  
  	@Test
	public void testGetSeconds() {
		TimeStamp timeStamp = new TimeStamp(904521000000L);
		assertEquals(210, timeStamp.getSeconds());
	}	
	
	@Test
	public void testTimeStamp_Date() {
		Date d = new Date();
		TimeStamp timeStamp = new TimeStamp(d);
		assertNotNull(timeStamp.getDate());
	}
	
	@Test
	public void testTimeStamp_Date_null() {
		Date d = null;
		TimeStamp timeStamp = new TimeStamp(d);
		assertNotNull(timeStamp.getDate());
	}
}

						