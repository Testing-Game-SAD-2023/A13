package student.fabrizioVollaroDue;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.TimeStamp;

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
	void testHashCode() {
		TimeStamp timeStamp1 = new TimeStamp(904521000000L);
	    TimeStamp timeStamp2 = new TimeStamp(904521000000L);
	    assertEquals(timeStamp1.hashCode(), timeStamp2.hashCode());
	}

	@Test
	void testTimeStampLong() {
		TimeStamp timeStamp = new TimeStamp(904521000000L);
		assertEquals(904521000000L, timeStamp.ntpValue());
	}

	@Test
	void testTimeStampString() {
		TimeStamp timeStamp = new TimeStamp("e90e6247.0189374b");		
		assertEquals("e90e6247.0189374b", TimeStamp.toString(timeStamp.ntpValue()));
	}

	@Test
	void testTimeStampDate() {
		Date data = new Date(904521000000L);
		TimeStamp timeStamp = new TimeStamp(data);
        assertEquals(data, timeStamp.getDate());
	}

	@Test
	void testNtpValue() {
		TimeStamp timeStamp = new TimeStamp(904521000000L);
		assertEquals(904521000000L, timeStamp.ntpValue());
	}

	@Test
	void testGetSeconds() {
		TimeStamp timeStamp = new TimeStamp(904521000000L);
		assertEquals(((timeStamp.ntpValue() >>> 32) & 0xffffffffL), timeStamp.getSeconds());
	}
	//(904521000000L >>> 32) sposta i primi 32 bit nella parte meno significativa
	//& 0xffffffffL è una maschera bit a bit, maschera eventuali bit aggiuntivi
	
	
	@Test
	void testGetFraction() {
		TimeStamp timeStamp = new TimeStamp(904521000000L);
		assertEquals((timeStamp.ntpValue() & 0xffffffffL), timeStamp.getFraction());
	}

	@Test
	void testGetTime() {
		TimeStamp timeStamp = new TimeStamp(1000);
        assertEquals(TimeStamp.getTime(timeStamp.ntpValue()), timeStamp.getTime());
	}

	@Test
	void testGetDate() {
		Date data = new Date(904521000000L);
		TimeStamp timeStamp = new TimeStamp(data);
        assertEquals(data, timeStamp.getDate());
	}

	@Test
	void testGetTimeLong() {
		TimeStamp timeStamp = new TimeStamp(904521000000L);
        assertEquals(TimeStamp.getTime(timeStamp.ntpValue()), timeStamp.getTime());
	}

	@Test
	void testGetNtpTime() {
		TimeStamp timeStamp = new TimeStamp(904521000000L);
		long time = TimeStamp.getTime(timeStamp.ntpValue()); 
		assertEquals(TimeStamp.getNtpTime(timeStamp.getTime()), TimeStamp.getNtpTime(time));
	}

	@Test
	void testGetCurrentTime() {
		TimeStamp currentTimeStamp = TimeStamp.getCurrentTime();
		TimeStamp time = TimeStamp.getNtpTime(System.currentTimeMillis());
		assertEquals(time, currentTimeStamp);
	}

	@Test
	void testDecodeNtpHexString() {
		// Metodo privato, ma il metodo pubblico parseNtpString(String)
		// chiama al suo interno il metodo privato, per cui testando questo
		// avremo testato anche decodeNtpHexString(String)
	}

	@Test
	void testParseNtpString() {
		long ntpValue = 904521000000L;
		String hexString = TimeStamp.toString(ntpValue);
		TimeStamp timeStamp = TimeStamp.parseNtpString(hexString);
		assertEquals(timeStamp, TimeStamp.parseNtpString(TimeStamp.toString(904521000000L)));
	}
	
	@Test
	void testParseEmptyString() {
		String hexString = "";
		TimeStamp timeStamp = TimeStamp.parseNtpString(hexString);
		assertEquals(timeStamp, TimeStamp.parseNtpString(""));
	}
	
	@Test
	void testParseNullString() {
		String hexString = null;
		assertThrows(NumberFormatException.class, () -> TimeStamp.parseNtpString(hexString));
	}

	@Test
	void testToNtpTime() {
		// Metodo privato, ma il metodo pubblico getNtpTime
	    // chiama al suo interno il metodo privato, per cui testando questo
		// avremo testato anche toNtpTime
		
	}

	@Test
	void testEqualsObject() {
		TimeStamp timeStamp1 = new TimeStamp(904521000000L);
        TimeStamp timeStamp2 = new TimeStamp(904521000000L);
        assertTrue(timeStamp1.equals(timeStamp2));
	}
	
	@Test
	void test0EqualsObject() {
		TimeStamp timeStamp1 = new TimeStamp(0);
        TimeStamp timeStamp2 = new TimeStamp(0);
        assertTrue(timeStamp1.equals(timeStamp2));
	}

	@Test
	void testToString() {
		TimeStamp timeStamp = new TimeStamp(1000);
		assertEquals("00000000.000003e8", timeStamp.toString());
	}

	@Test
	void testToStringLong() {
		long timestampValue = 904521000000L;
		String timestampString = TimeStamp.toString(timestampValue);
		assertEquals("000000d2.99a72440", timestampString);
	}

	@Test
	void testToDateString() {
		TimeStamp timeStamp = new TimeStamp(904521000000L);
		String dateString = timeStamp.toDateString();
		assertEquals("Mon, Aug 31 1998 01:50:00", dateString);
		fail("Risultato atteso diverso dal risultato effettivo. ");
	}

	@Test
	void testToUTCString() {
		TimeStamp timeStamp = new TimeStamp(904521000000L);
		String utcString = timeStamp.toUTCString();
		assertEquals("Mon, Aug 31 1998 01:50:00 UTC", utcString);
		fail("Risultato atteso diverso dal risultato effettivo. ");
	}

	@Test
	void testCompareTo() {
		TimeStamp timeStamp1 = new TimeStamp(1000L);
        TimeStamp timeStamp2 = new TimeStamp(2000L);
        int result = timeStamp1.compareTo(timeStamp2);
        assertTrue(result < 0, "timeStamp1 è inferiore a timeStamp2. ");
	}
	
	@Test
	void testNullCompareTo() {
		TimeStamp timeStamp = new TimeStamp(904521000000L);
        assertFalse(timeStamp.equals(null));
        assertThrows(NullPointerException.class, () -> timeStamp.compareTo(null));
	}
	
	@Test
    void testParseInvalidString() {
        assertThrows(NumberFormatException.class, () -> TimeStamp.parseNtpString("invalid_timeStamp"));
    } 
	
}
