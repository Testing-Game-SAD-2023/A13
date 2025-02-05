/*Compila i campi "Nome" e "Cognome" con le informazioni richieste
Nome: "Anna Flavia"
Cognome: "De Rosa"
Username: annafl.derosa@studenti.unin.it
UserID: 11
Date: 21/11/2024
*/

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class TimeStampAnnaFlaviaDeRosaEvosuiteHard {
	@BeforeClass
	public static void setUpClass() {
		// Eseguito una volta prima dell'inizio dei test nella classe
		// Inizializza risorse condivise 
		// o esegui altre operazioni di setup
	}
				
	@AfterClass
	public static void tearDownClass() {
		// Eseguito una volta alla fine di tutti i test nella classe
		// Effettua la pulizia delle risorse condivise 
		// o esegui altre operazioni di teardown
	}
				
	@Before
	public void setUp() {
		// Eseguito prima di ogni metodo di test
		// Preparazione dei dati di input specifici per il test
	}
				
	@After
	public void tearDown() {
		// Eseguito dopo ogni metodo di test
		// Pulizia delle risorse o ripristino dello stato iniziale
	}
				
	//Test costruttore con long
	@Test
	public void testTimeStamp() {
      	long ntpValue = 0xC1A089BD_FC904F6DL; // Esempio di timestamp NTP
      	TimeStamp timestamp = new TimeStamp(ntpValue);
		assertNotNull(timestamp);
	}
  
  // Test costruttore con stringa valida
  	@Test
  	public void testTimeStamp2(){
      	String hexStamp = "c1a089bd.fc904f6d"; 
        TimeStamp timestamp = new TimeStamp(hexStamp);
      	assertNotNull(timestamp);     
    }
				
	// Test costruttore con stringa non valida
  	@Test
  	public void testTimeStamp3(){
      	String hexStamp = null; 
        assertThrows(NumberFormatException.class, ()->{TimeStamp timestamp = new TimeStamp(hexStamp);});
    }
  
  
    // Test costruttore con stringa di lunghezza pari a 0
  	@Test
  	public void testTimeStamp4(){
  		String hexStamp = ""; 
        TimeStamp timestamp = new TimeStamp(hexStamp);
      	assertEquals(0,timestamp.ntpValue());   
    }
  
  
    // Test costruttore con stringa valida senza parte frazionaria.
  	@Test
  	public void testTimeStamp5(){
      	String hexStamp = "c1a089bd"; 
        TimeStamp timestamp = new TimeStamp(hexStamp);
      	assertNotNull(timestamp);     
    }
  
      // Test costruttore con Date
  	@Test
    public void testTimeStamp6() {
        Date testDate = new Date(1577836800000L); // 1 gennaio 2020, 00:00:00 UTC in millisecondi
        
        // Crea l'oggetto TimeStamp
        TimeStamp timestamp = new TimeStamp(testDate);
        assertNotNull(timestamp);
        

  	}	
  	
    // Test costruttore con Date: caso di data null
	@Test
  public void testTimeStamp7() {
      Date testDate = null;
      TimeStamp timestamp = new TimeStamp(testDate);
      assertNotNull(timestamp);

	}	
  
  	// Test costruttore con Date: caso di data massima possibile
	  @Test
	  public void testTimeStamp8() {
		  Date date = new Date(Long.MAX_VALUE);
	      TimeStamp ts = new TimeStamp(date);
	      assertNotNull(ts);
	      assertTrue(ts.ntpValue() > 0);
	    }
  
  //Test ntpValue
	@Test
	public void testNtpValue() {
        TimeStamp timeStamp = new TimeStamp(0x12345678L);
        assertEquals(0x12345678L, timeStamp.ntpValue());
	}

	//Test del metodo getSeconds
	@Test
	public void testGetSeconds() {
        long ntpTime = 0x12345678_9ABCDEF0L; // 0x12345678 sono i 32 bit più significativi (i secondi)
        TimeStamp timeStamp = new TimeStamp(ntpTime);
        assertEquals(0x12345678L, timeStamp.getSeconds());
    }

	//Test del metodo getFraction
	@Test
	public void testGetFraction() {
        long ntpTime = 0x12345678_9ABCDEF0L; // 0x9ABCDEF0L sono i 32 bit meno significativi 
        TimeStamp timeStamp = new TimeStamp(ntpTime);
        assertEquals(0x9ABCDEF0L, timeStamp.getFraction());
    }
	

	//Test del metodo getDate
	@Test
	public void testGetDate() {
		long ntpValue = 0xC1A089BD_FC904F6DL; 
        TimeStamp timestamp = new TimeStamp(ntpValue);
        long expectedTime = timestamp.getTime(ntpValue);
        Date expectedDate = new Date(expectedTime); // Creiamo l'oggetto Date a partire dal tempo calcolato
        assertEquals( expectedDate, timestamp.getDate());
	}
	// Test del metodo getTime: caso in cui il most significant bit è 1
	@Test
	public void testGetTimeForMSB1() {
	    // NTP timestamp rappresentante 1 Jan 1900 @ 00:00:00 UTC
	    long ntpTime = 0x80000000L << 32;  // MSB è 1 per base 1900

	    // Calcolo della differenza tra l'epoca NTP (1900) e l'epoca Unix (1970)
	    long secondsBetweenEpochs = 70L * 365 * 24 * 3600 + 17L * 24 * 3600; // 2208988800 secondi

	    // Calcolo del tempo Java atteso (in millisecondi)
	    long expectedJavaTime = ((ntpTime >>> 32) - secondsBetweenEpochs) * 1000; // Convertire in millisecondi

	   
	    TimeStamp timeStamp = new TimeStamp(ntpTime);
	    long actualJavaTime = timeStamp.getTime();

	    assertEquals(expectedJavaTime, actualJavaTime);
	}

	// Test del metodo getTime: caso in cui il most significant bit è 0
	@Test
	public void testGetTimeForMSB0() {
	    // NTP timestamp rappresentante il 7 Feb 2036 @ 06:28:16 UTC
	    long ntpSeconds = 0x00000001L; // 1 secondo dopo il base time
	    long ntpFraction = 0x00000000L; // Nessuna frazione di secondo
	    long ntpTime = (ntpSeconds << 32) | ntpFraction;

	    // Calcolo del tempo Java atteso (msb0baseTime + 1 secondo)
	    long msb0baseTime = 2085978496000L; // Base time: 7 Feb 2036 @ 06:28:16 UTC
	    long expectedJavaTime = msb0baseTime + (ntpSeconds * 1000); // aggiungo 1 secondo in millisecondi


	    TimeStamp timeStamp = new TimeStamp(ntpTime);
	    long actualJavaTime = timeStamp.getTime();

	    assertEquals(expectedJavaTime, actualJavaTime);
	}
  
  //Test del metodo getNtpTime
	@Test
    public void testGetNtpTime() {
        long javaTime = 0xf22cd1fc8aL; // 17 dicembre 2002, 09:07:24.810 UTC
        // Valori NTP attesi:
        long minExpectedNtpTime = 0xC1A9AE1C_CF5C28F5L; // Valore minimo corrispondente
        long maxExpectedNtpTime = 0xC1A9AE1C_CF9DB22CL; // Valore massimo corrispondente
        TimeStamp ntpTimeStamp = TimeStamp.getNtpTime(javaTime);
      	System.out.println("Valore NTP ottenuto: " + ntpTimeStamp);

        assertNotNull(ntpTimeStamp);
        long actualNtpTime = ntpTimeStamp.ntpValue();

        // Controlla che il valore NTP rientri nell'intervallo atteso
        assertTrue(actualNtpTime >= minExpectedNtpTime && actualNtpTime <= maxExpectedNtpTime);
    }
  
  
  		//Test del metodo getCurrentTime
		@Test
	    public void testGetCurrentTime() {
	        // Tolleranza in millisecondi per confronto tra i timestamp
	        final long tolerance = 1000; // 1 secondo
	        // Otteniamo l'ora attuale di sistema
	        long systemCurrentTime = System.currentTimeMillis();
	       // System.out.println("Ora attuale di sistema (millisecondi dall'epoca): " + systemCurrentTime);

	        // Otteniamo il TimeStamp corrente usando il metodo getCurrentTime
	        TimeStamp currentTimeStamp = TimeStamp.getCurrentTime();
	        //System.out.println("TimeStamp generato: " + currentTimeStamp);

	        // conversione del TimeStamp in tempo Java (ms dall'epoca)
	        long timestampTime = currentTimeStamp.getTime();
	       // System.out.println("Ora generata dal TimeStamp (millisecondi dall'epoca): " + timestampTime);

	        // Verifichiamo che il tempo restituito sia vicino al tempo corrente del sistema
	        boolean isWithinTolerance = Math.abs(timestampTime - systemCurrentTime) <= tolerance;
	        //System.out.println("La differenza tra il tempo del sistema e il TimeStamp è " +
	             //   Math.abs(timestampTime - systemCurrentTime) + " ms, tolleranza accettabile: " + tolerance + " ms");

	        assertTrue("Timestamp non è all'interno della tolleranza", isWithinTolerance);

	        assertNotNull("L'istanza di TimeStamp non deve essere null", currentTimeStamp);

	        long ntpValue = currentTimeStamp.ntpValue();
	        //System.out.println("Valore interno del TimeStamp (ntpValue): " + ntpValue);
	        assertNotEquals("Il valore interno di TimeStamp non deve essere zero", 0, ntpValue);
	    }
		
		//Test del metodo ParseNtpString
		@Test
		 public void testParseNtpString() {
		        String hexString = "c1a089bd.fc904f6d"; // Esempio valido
		        long expectedNtpValue = 0xc1a089bd_fc904f6dL;
		        TimeStamp timeStamp = TimeStamp.parseNtpString(hexString);
		        assertNotNull("TimeStamp object should not be null.", timeStamp);
		        assertEquals("The ntpValue should match the expected value.", expectedNtpValue, timeStamp.ntpValue());
		    }
		
		
		
		//Test del metodo toString: 
		
		@Test
	    public void testToString_zero() {
	        TimeStamp ts = new TimeStamp(0x00000000_00000000L); 
	
	        assertEquals("00000000.00000000", ts.toString());
	    }

	    @Test
	    public void testToString_wholeSecondsOnly() {
	        TimeStamp ts = new TimeStamp(0x00000001_00000000L); // 1 secondo

	        assertEquals("00000001.00000000", ts.toString());
	    }

	    @Test
	    public void testToString_fractionalSeconds() {
	        TimeStamp ts = new TimeStamp(0x00000001_80000000L);

	        assertEquals("00000001.80000000", ts.toString());
	    }

	    @Test
	    public void testToString_maxValue() {
	        TimeStamp ts = new TimeStamp(0xFFFFFFFF_FFFFFFFFL); // NTP time massimo

	        assertEquals("ffffffff.ffffffff", ts.toString());
	    }
	    

	
	    @Test
	    public void testGetDateString() {
	        long ntpValue = 0x80000000_00000000L;
	        TimeStamp timestamp = new TimeStamp(ntpValue);
	        
	        long expectedTime = TimeStamp.getTime(ntpValue);
	        Date expectedDate = new Date(expectedTime);
	        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss.SSS", Locale.ENGLISH);
	        
	        String expectedDateString = sdf.format(expectedDate);

	       // System.out.println("Expected Date String: " + expectedDateString);
	        //System.out.println("Timestamp Date String: " + timestamp.toDateString());

	        assertEquals(expectedDateString, timestamp.toDateString());
	    }
  
  
     @Test
	    public void testToUTCString() {
	        long ntpValue = 0x80000000_00000000L;
	        TimeStamp timestamp = new TimeStamp(ntpValue);
	        
	        long expectedTime = TimeStamp.getTime(ntpValue);
	        
	        Date expectedDate = new Date(expectedTime);
	        
	        SimpleDateFormat utcFormat = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss.SSS 'UTC'", Locale.ENGLISH);
	        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Imposta il fuso orario a UTC
	        String expectedDateString = utcFormat.format(expectedDate);

	        //System.out.println("Expected Date String: " + expectedDateString);
	        //System.out.println("Timestamp Date String: " + timestamp.toUTCString());

	        assertEquals(expectedDateString, timestamp.toUTCString());
	    }
  
  	    //Test del metodo compareTo: caso di timestamp uguali
	    @Test
	    public void testCompareTo() {
	        // Creazione di tre valori NTP
	        long ntpValue1 = 0xC1A089BD_FC904F6DL; // Valore base
	        long ntpValue2 = 0xC1A089BE_00000000L; // Valore successivo
	        long ntpValue3 = 0xC1A089BD_00000000L; // Valore precedente

	        TimeStamp timestamp1 = new TimeStamp(ntpValue1);
	        TimeStamp timestamp2 = new TimeStamp(ntpValue2);
	        TimeStamp timestamp3 = new TimeStamp(ntpValue3);

	        // Caso 1: uguaglianza
	        assertEquals(0, timestamp1.compareTo(timestamp1));
	        // Caso 2: timestamp1 < timestamp2 
	        assertTrue(timestamp1.compareTo(timestamp2) < 0);
	        // Test: timestamp1 > timestamp3 
	        assertTrue(timestamp1.compareTo(timestamp3) > 0); 
	    }
  
      // Test del metodo equals
	    @Test
	    public void testEquals() {
	        long ntpValue1 = 0x80000000_00000000L; // Primo valore NTP
	        long ntpValue2 = 0x80000000_00000001L; // Secondo valore NTP (diverso dal primo)

	        TimeStamp timestamp1 = new TimeStamp(ntpValue1);
	        TimeStamp timestamp2 = new TimeStamp(ntpValue1); // Uguale al primo
	        TimeStamp timestamp3 = new TimeStamp(ntpValue2); // Diverso dal primo

	        // Caso 1: confronto di un oggetto con se stesso
	        assertTrue(timestamp1.equals(timestamp1));

	        // Caso 2: confronto di due oggetti con lo stesso valore 
	        assertTrue(timestamp1.equals(timestamp2));

	        // Caso 3: confronto di due oggetti con valori diversi 
	        assertFalse(timestamp1.equals(timestamp3));

	        // Caso 4: confronto con un oggetto di un tipo diverso
	        String notATimestamp = "oggetto di tipo diverso";
	        assertFalse(timestamp1.equals(notATimestamp));
	    }
  
  
  	    // Test del metodo hashCode
	    @Test
	    public void testHashCode() {
	        long ntpValue1 = 0x80000000_00000000L; 
	        long ntpValue2 = 0x80000000_00000001L; 

	        TimeStamp timestamp1 = new TimeStamp(ntpValue1);
	        TimeStamp timestamp2 = new TimeStamp(ntpValue1); // Uguale al primo
	        TimeStamp timestamp3 = new TimeStamp(ntpValue2); // Diverso dal primo

	        // Caso 1: Due oggetti uguali 
	        assertEquals(timestamp1.hashCode(), timestamp2.hashCode());

	        // Caso 2: Due oggetti diversi 
	        assertNotEquals(timestamp1.hashCode(), timestamp3.hashCode());
	        
	       
	    }

	
}

