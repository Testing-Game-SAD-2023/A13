package student.simoneMontellaUno;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import ClassUnderTest.TimeStamp;

class StudentTest {
	
	private static long nowMillis, oneDayMillis;
	private static TimeStamp today, yesterday;
	private static String nowHex;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		nowMillis = System.currentTimeMillis();
		oneDayMillis = 60 * 60 * 24 * 1000;
		
		today = TimeStamp.getNtpTime(nowMillis);
		yesterday = TimeStamp.getNtpTime(nowMillis - oneDayMillis);
		
		nowHex = TimeStamp.toString(today.getTime());
	}

	@Test
	void testTimeStampLong() { //TC1
		assertNotNull(new TimeStamp(nowMillis));
	}

	@Test
	void testTimeStampHexString() { //TC2
		assertNotNull(new TimeStamp(nowHex));
	}
	
	@ParameterizedTest
	@NullSource
	void testTimeStampNullHex(String nullHex) { //TC3
		assertThrows(NumberFormatException.class, () -> new TimeStamp(nullHex));
	}

	@Test
	void testTimeStampDate() { //TC4
        assertNotNull(new TimeStamp(new Date()));
	}
	
	@ParameterizedTest
	@NullSource
	void testTimeStampNullDate(Date nullDate) { //TC5
        assertNotNull(new TimeStamp(nullDate));
	}

	@Test
	void testNtpValue() { //TC6
		assertNotEquals(0, today.ntpValue());
	}

	@Test
	void testGetSeconds() { //TC7
		assertNotEquals(0, today.getSeconds());
	}

	@Test
	void testGetFraction() { //TC8
		assertNotEquals(0, today.getFraction());
	}

	@Test
	void testGetTime() { //TC9
		assertEquals(nowMillis, today.getTime());
	}
	
	@Test
	void testGetDate() { //TC10
		assertNotNull(today.getDate());
	}

	@Test
	void testGetNtpTime() { //TC11
		assertNotNull(today);
	}

	@Test
	void testGetCurrentTime() { //TC12 - A volte fallisce poiche la precisione del currentTimeMillis è minore rispetto a quella del TimeStamp NTP o perche magari non sono stati tradotti nello stesso momento dalla JVM
		assertEquals(new Date(System.currentTimeMillis()), TimeStamp.getCurrentTime().getDate());
	}

	@Test
	void testParseNtpString() { //TC13, TC14, TC15
		assertNotNull(TimeStamp.parseNtpString(nowHex));
		assertNotNull(TimeStamp.parseNtpString(""));
		assertNotNull(TimeStamp.parseNtpString("654F6426"));
	}
	
	@Test
	void testParseFakeNtpString() { //TC16
		assertThrows(NumberFormatException.class, () -> TimeStamp.parseNtpString(null));
	}
	
	@Test
	void testTimeMaxLong() { //TC17
		assertTrue(TimeStamp.getTime(Long.MAX_VALUE) > today.getTime());
	}
	
	@Test
	void testToNtpTime() { //TC18
		assertNotNull(TimeStamp.getNtpTime(Long.MAX_VALUE));
	}

	@Test
	void testToString() { //TC19
		assertNotNull(today.toString());
	}
	
	@Test
	void testToDateString() { //TC20
		SimpleDateFormat simpleFormatter = new SimpleDateFormat(TimeStamp.NTP_DATE_FORMAT, Locale.US);
		simpleFormatter.setTimeZone(TimeZone.getDefault());
		
		assertEquals(simpleFormatter.format(new Date(nowMillis)), today.toDateString());
	}

	@Test
	void testToUTCString() { //TC21
		SimpleDateFormat utcFormatter = new SimpleDateFormat(TimeStamp.NTP_DATE_FORMAT + " 'UTC'", Locale.US);
		utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		assertEquals(utcFormatter.format(new Date(nowMillis)), today.toUTCString());
	}
	
	@Test
	void testEquals() { //TC22
		TimeStamp ts1 = new TimeStamp(nowMillis);
		TimeStamp ts2 = new TimeStamp(nowMillis);
		
		assertTrue(ts1.equals(ts2));
	}
	
	@Test
	void testNotEquals() { //TC23
		assertFalse(today.equals(new TimeStamp(System.currentTimeMillis())));
	}
	
	@Test
	void testEqualsToObject() { //TC24
		assertFalse(today.equals(new Object()));
	}
	
	@Test
	void testHashCode() {  //TC25
		TimeStamp ts1 = new TimeStamp(nowMillis);
		TimeStamp ts2 = new TimeStamp(nowMillis);
		
		assertTrue(ts1.hashCode() == ts2.hashCode());
	}
	
	@Test
	void testCompareTodayToYesterday() { //TC26
		assertEquals(1, today.compareTo(yesterday));
	}
	
	@Test
	void testCompareTodayToToday() { //TC27
		assertEquals(0, today.compareTo(today));
	}
	
	@Test
	void testCompareYesterdayToToday() { //TC28
		assertEquals(-1, yesterday.compareTo(today));
	}

}
