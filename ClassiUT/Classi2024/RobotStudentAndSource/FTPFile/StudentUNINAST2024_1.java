/*Compila i campi "Nome" e "Cognome" con le informazioni richieste
Nome: Fabio
Cognome: Accurso
Username: fa.accurso@studenti.unina.it
UserID: 18
Date: 19/11/2024
*/

import java.util.Calendar;

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class TestFTPFile {
  		public static FTPFile ftpFile;


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
      	ftpFile = new FTPFile();
	}
				
	@After
	public void tearDown() {
		// Eseguito dopo ogni metodo di test
		// Pulizia delle risorse o ripristino dello stato iniziale
	}
				
	// TEST -----------------------------------------------------------------------------------------------------
  	// Test 1: test that checks if the ftpFile object correspond to a File	
	@Test
	public void testIsFile() {
		ftpFile.setType(FTPFile.FILE_TYPE);
		assertTrue(ftpFile.isFile());
	}
    
	//Test 2: test that checks if the name set to a ftpFile is the same one that can be read from it
  	@Test
	public void testGetNameAndSetName() {
		ftpFile.setName("testFile.txt");
		assertEquals("testFile.txt", ftpFile.getName());
	}
  	
  	//---------------------------------------------------------------------------------------------------------------
  	
  	
  	//Test 3: Test di set e get Row Listing
  	@Test
  	public void testSet_Get_RowListing() {
  		ftpFile.setRawListing("-rwxr--r-- 2 user group 1024 Nov 19 11:02 file1.txt");
  		assertEquals("-rwxr--r-- 2 user group 1024 Nov 19 11:02 file1.txt",ftpFile.getRawListing());
  	}

  	//Test 4: Test del costruttore non vuoto che prende in ingresso una stringa rawListing e verifica della restituzione falso del metodo isValid
  	@Test
  	public void testCostructorNotVoid(){
  		ftpFile=new FTPFile("-rwxr--r-- 2 user group 1024 Nov 19 11:02 file1.txt");
  		assertEquals("-rwxr--r-- 2 user group 1024 Nov 19 11:02 file1.txt",ftpFile.getRawListing());
  		assertFalse(ftpFile.isValid());
  	}
  	
  	//Test 5: verifica che IsDirecory ritorna vero e tutte le altre ritornano falso dopo aver usato setType per impostare il tipo come Directory
  	@Test
  	public void testIsDirectory() {
  		ftpFile.setType(FTPFile.DIRECTORY_TYPE);
		assertTrue(ftpFile.isDirectory());
		assertFalse(ftpFile.isFile());
		assertFalse(ftpFile.isSymbolicLink());
		assertFalse(ftpFile.isUnknown());
  	}
  	
  
    
    //Test 6: verifica che isSymbolicLink ritorna vero e isDirectory ritona falso
  	@Test
    public void testIsSymbolicLink() {
  		ftpFile.setType(FTPFile.SYMBOLIC_LINK_TYPE);
		assertTrue(ftpFile.isSymbolicLink());
		assertFalse(ftpFile.isDirectory());
    }
  	
  	//Test 7: verifica che isUnknown ritorna vero e che il metodo getType ritorni 3, corrispondente all'unknown type per definizione.
  	@Test
    public void testIsUnknown() {
  		ftpFile.setType(FTPFile.UNKNOWN_TYPE);
		assertTrue(ftpFile.isUnknown());
		assertEquals(3, ftpFile.getType());
    }
  	
  	//Test 8: verifica isValid() restituisca true sull'oggetto istanziato con il costruttore vuoto
    @Test
    public void testIsValid() {
    	assertTrue(ftpFile.isValid());
    }
  	
    //Test 9: testa il funzionamento di set e get Size 
    @Test
    public void testSet_Get_Size() {
  	  	ftpFile.setSize(1024);
  	  	assertEquals(1024, ftpFile.getSize());
    }
  	
    //Test 10: testa il funzionamento di set e get HardLink 
    @Test
    public void testSet_Get_HardLink() {
    	ftpFile.setHardLinkCount(3);
  	  	assertEquals(3, ftpFile.getHardLinkCount());
    }
    
    //Test 11: testa il funzionamento di set e get Group 
    @Test
    public void testSet_Get_Group() {
    	ftpFile.setGroup("group");
  	  	assertEquals("group", ftpFile.getGroup());
    }
    
    //Test 12: testa il funzionamento di set e get User
    @Test
    public void testSet_Get_User() {
    	ftpFile.setUser("user");
  	  	assertEquals("user", ftpFile.getUser());
    }
    
    //Test 13: testa il funzionamento di set e get Link
    @Test
    public void testSet_Get_Link() {
    	ftpFile.setLink("file1.txt");
  	  	assertEquals("file1.txt", ftpFile.getLink());
    }
    
    //Test 14: testa il funzionamento di set e get Timestamp
    @Test
    public void testSet_Get_Timestamp() {
    	Calendar calendar = Calendar.getInstance();
    	ftpFile.setTimestamp(calendar);
    	assertNotNull(ftpFile.getTimestamp());
    }
    
    //Test 15: imposta a true il read_permission (0) a user_access (0) e poi verifica la corretta impostazione con il metodo hasPermission
    @Test
    public void testSet_Has_Permission() {
  	  	ftpFile.setPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION, true);
  	  	assertTrue(ftpFile.hasPermission(0, 0));
  	  	
    }
    
    //Test 16: index out of boards exception
    @Test
    public void testOutOfBounds() {
  	  	assertThrows(ArrayIndexOutOfBoundsException.class, ()->ftpFile.setPermission(FTPFile.USER_ACCESS, 5, false));  	
    }
    
    //Test 17: permesso nullo
    @Test
    public void testNullPermission() {
    	ftpFile=new FTPFile("-rwxr--r-- 2 user group 1024 Nov 19 11:02 file1.txt");
    	assertFalse(ftpFile.hasPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION));
    }
    
    //Test 18: imposta la raw listing e la stampa con toString
    @Test
    public void testToString() {
    	ftpFile.setRawListing("-rwxr--r-- 2 user group 1024 Nov 19 11:02 file1.txt");
    	assertEquals("-rwxr--r-- 2 user group 1024 Nov 19 11:02 file1.txt", ftpFile.toString());
    }
    
    //Test 19: verifica la stampa dei valori di default per toFormattedString senza argomento
    @Test
    public void testToFormattedStringVoid() {  
    	assertEquals("?---------    0                         -1 null",ftpFile.toFormattedString());
    }
    
    //Test 20: ToFormattedString -> newZone!=timezone
    @Test
    public void testToFormattedString() { 
    	ftpFile.setType(FTPFile.FILE_TYPE);
    	ftpFile.setPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION, true);
    	ftpFile.setTimestamp(Calendar.getInstance());
    	assertNotNull(ftpFile.toFormattedString("UTC"));
    }
    
    //Test 21: ToFormattedString -> timezone null
    @Test
    public void testToFormattedString_NullTimezone() { 
    	ftpFile.setType(FTPFile.SYMBOLIC_LINK_TYPE);
    	ftpFile.setPermission(FTPFile.USER_ACCESS, FTPFile.EXECUTE_PERMISSION, true);
    	ftpFile.setTimestamp(Calendar.getInstance());
    	assertNotNull(ftpFile.toFormattedString(null));
    }
    
    //Test 22: ToFormattedString -> newZone == timezone
    @Test
    public void testToFormattedString_EqualZone() { 
    	ftpFile.setType(FTPFile.DIRECTORY_TYPE);
    	ftpFile.setPermission(FTPFile.USER_ACCESS, FTPFile.WRITE_PERMISSION, true);
    	ftpFile.setTimestamp(Calendar.getInstance());
    	assertNotNull(ftpFile.toFormattedString("Europe/Rome"));
    }
    
    //Test 23: ToFormattedString -> notValid
    @Test
    public void testToFormattedString_CalendarHourOfDay() { 
    	ftpFile=new FTPFile("-rwxr--r-- 2 user group 1024 Nov 19 11:02 file1.txt");
    	assertEquals("[Invalid: could not parse file entry]",ftpFile.toFormattedString());
    }  	
  
}

						