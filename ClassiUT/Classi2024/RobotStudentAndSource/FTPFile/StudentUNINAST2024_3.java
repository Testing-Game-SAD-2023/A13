/*Compila i campi "Nome" e "Cognome" con le informazioni richieste
Nome: Alessandro
Cognome: Cajafa
Username: a.cajafa@studenti.unina.it
UserID: 30
Date: 23/11/2024
*/

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;
import java.util.Calendar;

public class TestFTPFile {
	
  private FTPFile ftpfile;
	
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

    ftpfile = null;

    assumeTrue(ftpfile == null);
  }

  // Caso di test 1: Chiamata al costruttore senza argomenti e chiamata a getSize()
  @Test
  public void testCostruttore1() {

    ftpfile = new FTPFile();
    assertEquals(-1, ftpfile.getSize());
  }

  // Caso di test 2: Chiamata a setRawListing() e chiamata getRawListing()
  @Test
  public void testRawListing() {

    ftpfile = new FTPFile();
    ftpfile.setRawListing("-rw-r--r--   1 user  group  12345 Jan  1 14:20 example.txt");
    assertEquals("-rw-r--r--   1 user  group  12345 Jan  1 14:20 example.txt", ftpfile.getRawListing());
  }

  // Caso di test 3: Chiamata a setType() e chiamata a getType()
  @Test
  public void testType() {

    ftpfile = new FTPFile();
    ftpfile.setType(FTPFile.DIRECTORY_TYPE);
    assertEquals(FTPFile.DIRECTORY_TYPE, ftpfile.getType());
  }

  // Caso di test 4: Chiamata a setName() e chiamata a getName()
  @Test
  public void testName() {

    ftpfile = new FTPFile();
    ftpfile.setName("test.txt");
    assertEquals("test.txt", ftpfile.getName());
  }

  // Caso di test 5: Chiamata a setHardLinkCount() e chiamata a getHardLinkCount()
  @Test
  public void testHardLinkCount() {

    ftpfile = new FTPFile();
    ftpfile.setHardLinkCount(3);
    assertEquals(3, ftpfile.getHardLinkCount());
  }

  // Caso di test 6: Chiamata a setGroup() e chiamata a getGroup()
  @Test
  public void testGroup() {

    ftpfile = new FTPFile();
    ftpfile.setGroup("prova");
    assertEquals("prova", ftpfile.getGroup());
  }

  // Caso di test 7: Chiamata a setUser() e chiamata a getUser()
  @Test
  public void testUser() {

    ftpfile = new FTPFile();
    ftpfile.setUser("Mario Rossi");
    assertEquals("Mario Rossi", ftpfile.getUser());
  }

  // Caso di test 8: Chiamata a setLink() e chiamata a getLink()
  @Test
  public void testLink() {

    ftpfile = new FTPFile();
    ftpfile.setType(2); // File di tipo Symbolic Link
    ftpfile.setLink("symlink.txt");
    assertEquals("symlink.txt", ftpfile.getLink());
  }

  // Caso di test 9: Chiamata a setTimeStamp() e chiamata a getTimeStamp()
  @Test
  public void testTimeStamp() {

    ftpfile = new FTPFile();
    Calendar date = Calendar.getInstance();
    ftpfile.setTimestamp(date);
    assertNotNull(ftpfile.getTimestamp());
  }

  // Caso di test 10: Chiamata a setPermission() e chiamata a hasPermission()
  @Test
  public void testPermission1() {

    ftpfile = new FTPFile();
    ftpfile.setPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION, true);
    assertTrue(ftpfile.hasPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION));
    assertFalse(ftpfile.hasPermission(FTPFile.GROUP_ACCESS, FTPFile.WRITE_PERMISSION));
  }

  // Caso di test 11: Chiamata a toFormattedString() e chiamata a isDirectory()
  @Test
  public void testToFormattedString1() {

    ftpfile = new FTPFile();
    ftpfile.setUser("tester");
    ftpfile.setName("test.txt");
    ftpfile.setGroup("prova");
    ftpfile.setSize(4096);
    ftpfile.setType(FTPFile.DIRECTORY_TYPE);
    assertEquals("d---------    0 tester   prova        4096 test.txt", ftpfile.toFormattedString());
    assertTrue(ftpfile.isDirectory());
    assertFalse(ftpfile.isFile());
  }

  // Caso di test 12: Chiamata a toFormattedString() con date != null e chiamata a isFile()
  @Test
  public void testToFormattedString2() {

    ftpfile = new FTPFile();
    ftpfile.setUser("tester");
    ftpfile.setName("test.txt");
    ftpfile.setGroup("prova");
    ftpfile.setSize(4096);
    ftpfile.setType(FTPFile.FILE_TYPE);
    ftpfile.setPermission(FTPFile.WORLD_ACCESS, FTPFile.EXECUTE_PERMISSION, true);
    Calendar date = Calendar.getInstance();
    ftpfile.setTimestamp(date);
    ftpfile.toFormattedString();
    assertTrue(ftpfile.isFile());
    assertFalse(ftpfile.isDirectory());
  }

  // Caso di test 13: Chiamata a toFormattedString() con date != null e timezone != null e chiamata a isSymbolicLink()
  @Test
  public void testToFormattedString3() {

    ftpfile = new FTPFile();
    ftpfile.setUser("tester");
    ftpfile.setName("test.txt");
    ftpfile.setGroup("prova");
    ftpfile.setSize(4096);
    ftpfile.setType(FTPFile.SYMBOLIC_LINK_TYPE);
    ftpfile.setPermission(FTPFile.GROUP_ACCESS, FTPFile.WRITE_PERMISSION, true);
    Calendar date = Calendar.getInstance();
    ftpfile.setTimestamp(date);
    ftpfile.toFormattedString("Europe/Rome");
    assertTrue(ftpfile.isSymbolicLink());
    assertFalse(ftpfile.isUnknown());
  }
  
  // Caso di test 14: Chiamata a toString
  @Test
  public void testToString() {

    ftpfile = new FTPFile();
    ftpfile.setRawListing("-rw-r--r-- 1 tester prova 1111 Oct 02 10:30 test.txt");
    assertEquals("-rw-r--r-- 1 tester prova 1111 Oct 02 10:30 test.txt", ftpfile.toString());
  }
  
  // Caso di test 14: Chiamata a toFormattedString() con date != null, timezone != null e timezone diverse e chiamata a isUnknown()
  @Test
  public void testToFormattedString4() {

    ftpfile = new FTPFile();
    ftpfile.setUser("tester");
    ftpfile.setName("test.txt");
    ftpfile.setGroup("prova");
    ftpfile.setSize(4096);
    ftpfile.setPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION, true);
    Calendar date = Calendar.getInstance();
    ftpfile.setTimestamp(date);
    ftpfile.toFormattedString("UTC");
    assertTrue(ftpfile.isUnknown());
    assertFalse(ftpfile.isSymbolicLink());
  }

  // Caso di test 15: Chiamata al costruttore con rawListing
  @Test
  public void testCostruttore2() {

    ftpfile = new FTPFile("-rw-r--r--   1 user  group  12345 Jan  1 14:20 example.txt");
    assertFalse(ftpfile.isValid());
    assertFalse(ftpfile.hasPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION));
    ftpfile.toFormattedString("UTC");
  }

}




