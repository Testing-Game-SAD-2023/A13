/*Compila i campi "Nome" e "Cognome" con le informazioni richieste
	Nome: "Andres"
	Cognome: "Carballo Perez"
	Username: a.carballoperez@unina.it
	UserID: 1
	Date: 20/11/2024
	*/

	import org.junit.Before;
	import org.junit.After;
	import org.junit.BeforeClass;
	import org.junit.AfterClass;
	import org.junit.Test;
	import static org.junit.Assert.*;
	import java.text.DateFormat;
	import java.text.SimpleDateFormat;
	import java.util.Date;
	import java.util.Locale;
	import java.util.TimeZone;

	public class TestTimeStamp {
      
      	private static final long BASE_1900_NTP_TIME = 0x8000000000000000L;
    	private static final long BASE_2036_NTP_TIME = 0x0000000100000000L;
    	private static final long FRACTIONAL_NTP_TIME = 0x80000000FFFFFFFFL;
    	private static final long ZERO_NTP_TIME = 0x0000000000000000L;
      	private static final long msb0baseTime = 2085978496000L;
    	private static final long msb1baseTime = -2208988800000L;
      
      	private TimeStamp sampleTimeStamp;
    	private final long sampleNtpTime = 0x83AA7E8000000000L;
      
		@BeforeClass
		public static void setUpClass() {
			// Eseguito una volta prima dell'inizio dei test nella classe
			// Inizializza risorse condivise 
			// o esegui altre operazioni di setup
            System.out.println("Initializing all resources before testing");
		}
					
		@AfterClass
		public static void tearDownClass() {
			// Eseguito una volta alla fine di tutti i test nella classe
			// Effettua la pulizia delle risorse condivise 
			// o esegui altre operazioni di teardown
          System.out.println("Cleaning all resources after testing");
		}
					
		@Before
		public void setUp() {
			// Eseguito prima di ogni metodo di test
			// Preparazione dei dati di input specifici per il test
          	sampleTimeStamp = new TimeStamp(sampleNtpTime);
		}
					
		@After
		public void tearDown() {
			// Eseguito dopo ogni metodo di test
			// Pulizia delle risorse o ripristino dello stato iniziale
            sampleTimeStamp = null;
		}
		
      	/** 
     	* Test constructor with long value.
     	*/
		@Test 
      	public void testConstructorWithLong() {
        	long ntpValue = 0x83AA7E80L;
        	TimeStamp timeStamp = new TimeStamp(ntpValue);
        	assertEquals(ntpValue, timeStamp.ntpValue());
    	}
      
      	/**
     	* Test constructor with valid Date.
     	*/
      	@Test
    	public void testConstructorWithValidDate() {
        	Date validDate = new Date(0);
        	TimeStamp timeStamp = new TimeStamp(validDate);

        	long expectedNtpTime = TimeStamp.toNtpTime(validDate.getTime());
        	assertEquals(expectedNtpTime, timeStamp.ntpValue());
    	}
      
      	/**
     	* Test constructor with null Date.
     	*/
      	@Test
    	public void testConstructorWithNullDate() {
        	TimeStamp timeStamp = new TimeStamp((Date) null);

        	assertEquals(0, timeStamp.ntpValue());
    	}
      
      	/**
     	* Test constructor with valid Hex String
     	*/
      	@Test
    	public void testConstructorWithHexString() {
        	String hexStamp = "c1a089bd.fc904f6d";
        	TimeStamp timeStamp = new TimeStamp(hexStamp);
        	assertNotNull(timeStamp);
        	assertEquals(hexStamp.toLowerCase(), timeStamp.toString());
    	}

      	/**
     	* Test constructor with invalid Hex String
     	*/
      	@Test
    	public void testConstructorWithInvalidHexString() {
        	String invalidHexStamp = "invalid_hex";
        	assertThrows(NumberFormatException.class, () -> new 	TimeStamp(invalidHexStamp));
    	}
      	
      	/**
     	* Test getSeconds method
     	*/
      	@Test
    	public void testGetSeconds() {
        	String hexStamp = "c1a089bd.fc904f6d";
        	TimeStamp timeStamp = new TimeStamp(hexStamp);
        	assertEquals(0xc1a089bdL, timeStamp.getSeconds());
    	}
      
      	/**
     	* Test getFraction method
     	*/
      	@Test
      	public void testGetFraction() {
        	String hexStamp = "c1a089bd.fc904f6d";
        	TimeStamp timeStamp = new TimeStamp(hexStamp);
        	assertEquals(0xfc904f6dL, timeStamp.getFraction());
      	}
      
      	/**
     	* Test getTime method
     	*/
     	@Test
      	public void testGetTime() {
        	long expectedJavaTime = TimeStamp.getTime(sampleNtpTime);
        	long actualJavaTime = sampleTimeStamp.getTime();

        	assertEquals(expectedJavaTime, actualJavaTime);
      	}
      
      	@Test
    	public void testGetTimeAfter2036() {
          
        	long seconds = 0x00000001L; 
        	long fraction = 0;      
        	long ntpTimeValue = (seconds << 32) | fraction;

        	long expectedTime = msb0baseTime + (seconds * 1000);
        	long actualTime = TimeStamp.getTime(ntpTimeValue);
          
        	assertEquals(expectedTime, actualTime);
    	}
            
      	/**
     	* Test getDate method
     	*/
      	@Test
      	public void testGetDate() {
        	Date expectedDate = new Date(TimeStamp.getTime(sampleNtpTime));
        	Date actualDate = sampleTimeStamp.getDate();

        	assertEquals(expectedDate, actualDate);
    	}
      
      	@Test
    	public void testToNtpTimeBefore2036() {
        
        	long javaTime = 946684800000L;
        	long expectedNtpTime = (javaTime - msb1baseTime) / 1000L << 32;
        	expectedNtpTime |= (((javaTime - msb1baseTime) % 1000) * 0x100000000L) / 1000;

        	long actualNtpTime = TimeStamp.toNtpTime(javaTime);

        	assertEquals(expectedNtpTime, actualNtpTime);
    	}
      
      	@Test
    	public void testToNtpTimeAfter2036() {

        	long javaTime = 2208988800000L;
        	long expectedNtpTime = (javaTime - msb0baseTime) / 1000L << 32;
        	expectedNtpTime |= (((javaTime - msb0baseTime) % 1000) * 0x100000000L) / 1000;

        	long actualNtpTime = TimeStamp.toNtpTime(javaTime);

        	assertEquals(expectedNtpTime, actualNtpTime);
    	}
      
      	/**
     	* Test decodeNtpHexString with a valid hex string containing seconds 			and fraction.
     	*/
    	@Test
    	public void testDecodeNtpHexStringValidFull() {
        	String hexString = "c1a089bd.fc904f6d";
        	long expectedNtpValue = (0xc1a089bdL << 32) | 0xfc904f6dL;

        	long actualNtpValue = TimeStamp.decodeNtpHexString(hexString);
        	assertEquals(expectedNtpValue, actualNtpValue);
    	}
      
      	/**
     	* Test decodeNtpHexString with a valid hex string containing only 				seconds.
     	*/
    	@Test
    	public void testDecodeNtpHexStringValidSecondsOnly() {
        	String hexString = "c1a089bd";
        	long expectedNtpValue = 0xc1a089bdL << 32;

        	long actualNtpValue = TimeStamp.decodeNtpHexString(hexString);
        	assertEquals(expectedNtpValue, actualNtpValue);
    	}
      
      	/**
     	* Test decodeNtpHexString with an empty string.
     	* Ensures that the method returns 0 when the string is empty.
     	*/
    	@Test
    	public void testDecodeNtpHexStringEmptyString() {
        	String hexString = "";

        	long actualNtpValue = TimeStamp.decodeNtpHexString(hexString);
        	assertEquals(0, actualNtpValue);
    	}
      
      	/**
     	* Test decodeNtpHexString with a null string.
     	* Ensures that the method throws a NumberFormatException.
     	*/
    	@Test(expected = NumberFormatException.class)
    	public void testDecodeNtpHexStringNullString() {
        	TimeStamp.decodeNtpHexString(null);
    	}
      
      	/**
     	* Test decodeNtpHexString with an invalid hex string.
     	* Ensures that the method throws a NumberFormatException.
     	*/
    	@Test(expected = NumberFormatException.class)
    	public void testDecodeNtpHexStringInvalidString() {
        	String hexString = "invalid_hex";
        	TimeStamp.decodeNtpHexString(hexString);
    	}
      
      	/**
     	* Test parseNtpString with a valid full hexadecimal string.
     	* Ensures that the TimeStamp object is created correctly.
     	*/
    	@Test
    	public void testParseNtpStringValidFull() {
        	String hexString = "c1a089bd.fc904f6d";
        	TimeStamp timeStamp = TimeStamp.parseNtpString(hexString);

        	assertNotNull(timeStamp);
        	long expectedNtpValue = (0xc1a089bdL << 32) | 0xfc904f6dL;
        	assertEquals(expectedNtpValue, timeStamp.ntpValue());
    	}
      
      	@Test
    	public void testToNtpTimeAndToJavaTimeConversion() {
        	long javaTime = System.currentTimeMillis();
        	TimeStamp timeStamp = TimeStamp.getNtpTime(javaTime);
        	assertNotNull(timeStamp);
        	assertEquals(javaTime, timeStamp.getTime());
    	}
      
      	/**
     	* Test static getCurrentTime method.
     	*/
    	@Test
    	public void testGetCurrentTime() {
        	TimeStamp current = TimeStamp.getCurrentTime();
        	assertNotNull(current);
    	}
      
      	@Test
    	public void testToString() {
        	long ntpValue = 0x83AA7E8000000000L;
        	TimeStamp timeStamp = new TimeStamp(ntpValue);
        	String expectedString = "83aa7e80.00000000";
        	assertEquals(expectedString, timeStamp.toString());
    	}
      
      	/**
     	* Test hashCode with two TimeStamp objects having the same ntpTime.
     	* Ensures that the hashCode method returns the same value for equal 			objects.
     	*/
    	@Test
    	public void testHashCodeEquality() {
        	long ntpTime = 0x83AA7E8000000000L;
        	TimeStamp timeStamp1 = new TimeStamp(ntpTime);
        	TimeStamp timeStamp2 = new TimeStamp(ntpTime);

        	assertEquals(timeStamp1.hashCode(), timeStamp2.hashCode());
    	}
      
      	/**
     	* Test equals with the same TimeStamp object.
     	* Ensures that the method returns true when comparing the object with 			itself.
     	*/
    	@Test
    	public void testEqualsSameObject() {
        	TimeStamp timeStamp = new TimeStamp(0x83AA7E8000000000L);
        	assertTrue(timeStamp.equals(timeStamp));
    	}
      
      	/**
     	* Test equals with a different TimeStamp object having the same value.
     	* Ensures that the method returns true for objects with the same 				ntpTime value.
     	*/
    	@Test
    	public void testEqualsSameValue() {
        	TimeStamp timeStamp1 = new TimeStamp(0x83AA7E8000000000L);
        	TimeStamp timeStamp2 = new TimeStamp(0x83AA7E8000000000L);

        	assertTrue(timeStamp1.equals(timeStamp2));
    	}
      
      	/**
     	* Test equals with a different TimeStamp object having a different 				value.
     	* Ensures that the method returns false for objects with different 				ntpTime values.
     	*/
    	@Test
    	public void testEqualsDifferentValue() {
        	TimeStamp timeStamp1 = new TimeStamp(0x83AA7E8000000000L);
        	TimeStamp timeStamp2 = new TimeStamp(0x83AA7E8000000001L);

        	assertFalse(timeStamp1.equals(timeStamp2));
    	}
      
      	/**
     	* Test equals with a null object.
     	* Ensures that the method returns false when comparing with null.
     	*/
    	@Test
    	public void testEqualsNull() {
        	TimeStamp timeStamp = new TimeStamp(0x83AA7E8000000000L);
        	assertFalse(timeStamp.equals(null));
    	}
      
      	/**
     	* Test equals with an object of a different class.
     	* Ensures that the method returns false when comparing with a non-				TimeStamp object.
     	*/
    	@Test
    	public void testEqualsDifferentClass() {
        	TimeStamp timeStamp = new TimeStamp(0x83AA7E8000000000L);
        	String objStr = "Not a TimeStamp";

        	assertFalse(timeStamp.equals(objStr));
    	}

      	/**
     	* Test toDateString method 
     	*/
      	@Test
    	public void testToDateString() {
	        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss.SSS", Locale.US);
    	    formatter.setTimeZone(java.util.TimeZone.getDefault());

	        long ntpValue = 0x83AA7E8000000000L; // 1970-01-01
	        TimeStamp timeStamp = new TimeStamp(ntpValue);
	        Date expectedDate = new Date(timeStamp.getTime());
	        assertEquals(formatter.format(expectedDate), timeStamp.toDateString());
	    }
      
      	/**
     	* Test toUTCString with a valid TimeStamp object.
     	* Ensures that the returned string is formatted correctly in UTC.
     	*/
    	@Test
    	public void testToUTCStringValid() {
        	TimeStamp timeStamp = new TimeStamp(0x83AA7E8000000000L);

        	SimpleDateFormat expectedFormatter = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss.SSS 'UTC'", Locale.US);
        	expectedFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        	String expectedUTCString = expectedFormatter.format(new Date(timeStamp.getTime()));

        	assertEquals(expectedUTCString, timeStamp.toUTCString());
    	}
      
      	@Test
    	public void testCompareTo1() {
        	TimeStamp timeStamp1 = new TimeStamp(0x83AA7E8000000000L);
        	TimeStamp timeStamp2 = new TimeStamp(0x83AA7E8000000001L);
        	assertTrue(timeStamp1.compareTo(timeStamp2) < 0);
        	assertTrue(timeStamp2.compareTo(timeStamp1) > 0);
        	assertEquals(0, timeStamp1.compareTo(new TimeStamp(0x83AA7E8000000000L)));
    	}
      
      	/**
     	* Test compareTo method for ordering.
     	*/
    	@Test
    	public void testCompareTo2() {
        	TimeStamp earlier = new TimeStamp(sampleNtpTime - 1);
        	TimeStamp later = new TimeStamp(sampleNtpTime + 1);

        	assertTrue(sampleTimeStamp.compareTo(earlier) > 0);
        	assertTrue(sampleTimeStamp.compareTo(later) < 0);
        	assertEquals(0, sampleTimeStamp.compareTo(new TimeStamp(sampleNtpTime)));
    	}
      	
}



							