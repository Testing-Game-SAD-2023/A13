package student.pasqualeCriscuoloUno;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ClassUnderTest.TimeStamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
		TimeStamp ts = new TimeStamp(1081078496000L);
		assertEquals((int) 1081078496000L^(1081078496000L >>> 32), ts.hashCode());
	}

	@Test
	void testTimeStampString() {
		new TimeStamp("c1a089bd.fc904f6d");
		Exception exception = assertThrows(Exception.class, () -> TimeStamp.parseNtpString(null));
		assertEquals("null",exception.getMessage());
	}

	@Test
	void testTimeStampDate() {
		Date d = null;
		TimeStamp ts = new TimeStamp(d);
		assertEquals(0,ts.ntpValue());
	}
	
	@ParameterizedTest
	@ValueSource(longs = {1081078496000L, 3081078496000L})
	void testTimeStampDate(long candidate) {
		TimeStamp ts = new TimeStamp(new Date(candidate));
		assertEquals(candidate, ts.getTime());
	}

	@Test
	void testGetSeconds() {
		TimeStamp ts = new TimeStamp(1081078496000L);
		assertEquals(((1081078496000L >>> 32) & 0xffffffffL), ts.getSeconds());
	}

	@Test
	void testGetFraction() {
		TimeStamp ts = new TimeStamp(1081078496000L);
		assertEquals(1081078496000L & 0xffffffffL, ts.getFraction());
	}

	@ParameterizedTest
	@ValueSource(longs = {1081078496000L, 3081078496000L})
	void testGetTimeLong(long candidate) {
		TimeStamp ts = new TimeStamp(candidate);
		ts.getTime();
	}

	@Test
	void testGetCurrentTime() {
		assertEquals(TimeStamp.getNtpTime(System.currentTimeMillis()),TimeStamp.getCurrentTime());
	}

	@ParameterizedTest
	@ValueSource(strings = {"c1a089bd.fc904f6d", "", "c1a089bd"})
	void testParseNtpString(String candidate) {
		TimeStamp.parseNtpString(candidate);
	}

	@Test
	void testEqualsObject() {
		Date d = new Date();
		TimeStamp ts = new TimeStamp(d);
		assertTrue(ts.equals(new TimeStamp(d)));
		assertFalse(ts.equals(new TimeStamp(new Date())));
		assertFalse(ts.equals(d));
	}

	@ParameterizedTest
	@ValueSource(longs = {1081078496000L})
	void testToStringLong(long candidate) {
		TimeStamp ts = new TimeStamp(candidate);
		assertEquals(ts.toString(),TimeStamp.toString(candidate));
	}
	
	@Test
	void testToDateString() {
		Date d = new Date(); 
		TimeStamp ts = new TimeStamp(d);
		DateFormat df = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss.SSS", Locale.US);
		assertEquals(df.format(d), ts.toDateString());
		assertEquals(df.format(d), ts.toDateString());
	}

	@Test
	void testToUTCString() {
		Date d = new Date(); 
		TimeStamp ts = new TimeStamp(d);
		DateFormat df = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss.SSS" + " 'UTC'", Locale.US);
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		assertEquals(df.format(d), ts.toUTCString());
		assertEquals(df.format(d), ts.toUTCString());	
		}

	@Test
	void testCompareTo() {
		TimeStamp ts = new TimeStamp(1081078496000L);
		TimeStamp ts1 = new TimeStamp(-1081078496000L);
		TimeStamp ts2 = new TimeStamp(2081078496000L);
		TimeStamp ts3 = new TimeStamp(1081078496000L);
		assertEquals(1, ts.compareTo(ts1));
		assertEquals(-1, ts.compareTo(ts2));
		assertEquals(0, ts.compareTo(ts3));
	}

}
