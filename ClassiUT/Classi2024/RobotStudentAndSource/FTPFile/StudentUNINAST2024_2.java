/*Compila i campi "Nome" e "Cognome" con le informazioni richieste
Nome: "Alessandra"
Cognome: "Zotti"
Username: ales.zotti@studenti.unina.it
UserID: 2
Date: 24/11/2024
*/

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.TimeZone;

public class TestFTPFile {
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
				
	@Test
	public void test_costruttore_vuoto() {
	FTPFile ftp_file = new FTPFile();
	}
  
    @Test
	public void test_costruttore_con_parametri() {
      String rawListing = "-rw-r--r--  1 user group  1234 Nov 24 10:10 example.txt";
	  FTPFile ftp_file = new FTPFile (rawListing);
	}
    
    
    @Test
  	public void test_setRawListing() {
  	FTPFile ftp_file = new FTPFile();
        String rawListing = "drwxr-xr-x  5 admin staff  4096 Nov 23 09:30 documents";
        ftp_file.setRawListing(rawListing);
  	}
  
    @Test
	public void test_getRawListing() {
	FTPFile ftp_file = new FTPFile();
      ftp_file.getRawListing();
	}
    
    @Test
 	public void test_isDirectory_1() {
 	FTPFile ftp_file = new FTPFile("drwxr-xr-x  5 admin staff  4096 Nov 23 09:30 documents");
 	ftp_file.setType(1);  
 	assertTrue(ftp_file.isDirectory());

 	}
    
    @Test
	public void test_isDirectory_2() {
	FTPFile ftp_file = new FTPFile();
     ftp_file.setType(2);
     assertFalse(ftp_file.isDirectory());

	}
    

    @Test
	public void test_isDirectory_3() {
	FTPFile ftp_file = new FTPFile();
     ftp_file.setType(0);
     assertFalse(ftp_file.isDirectory());
	}
 
      @Test
	public void test_isDirectory_4() {
	FTPFile ftp_file = new FTPFile();
     ftp_file.setType(3);
     assertFalse(ftp_file.isDirectory());
	}
      
      
      @Test
  	public void test_isFile_1() {
  	FTPFile ftp_file = new FTPFile();
        ftp_file.setType(0);
        assertTrue(ftp_file.isFile());
  	}
    
    
       @Test
  	public void test_isFile_2() {
  	FTPFile ftp_file = new FTPFile();
        ftp_file.setType(1);
        assertFalse(ftp_file.isFile());
  	}
    
    
    
        @Test
  	public void test_isFile_3() {
  	FTPFile ftp_file = new FTPFile();
        ftp_file.setType(2);
        assertFalse(ftp_file.isFile());
  	}
    
        @Test
  	public void test_isFile_4() {
  	FTPFile ftp_file = new FTPFile();
        ftp_file.setType(3);
        assertFalse(ftp_file.isFile());
  	}
    
        @Test
    	public void test_isSymbolicLink_1() {
    	FTPFile ftp_file = new FTPFile();
          ftp_file.setType(0);
          assertFalse(ftp_file.isSymbolicLink());
    	}
      
      
       @Test
    	public void test_isSymbolicLink_2() {
    	FTPFile ftp_file = new FTPFile();
          ftp_file.setType(1);
          assertFalse(ftp_file.isSymbolicLink());
    	}
      
        
       @Test
    	public void test_isSymbolicLink_3() {
    	FTPFile ftp_file = new FTPFile();
          ftp_file.setType(2);
          assertTrue(ftp_file.isSymbolicLink());
    	}
      
        @Test
    	public void test_isSymbolicLink_4() {
    	FTPFile ftp_file = new FTPFile();
          ftp_file.setType(3);
          assertFalse(ftp_file.isSymbolicLink());
    	}
        
        @Test
    	public void test_isUnknown_1() {
    	FTPFile ftp_file = new FTPFile();
          ftp_file.setType(0);
          assertFalse(ftp_file.isUnknown());
    	}
      
        @Test
    	public void test_isUnknown_2() {
    	FTPFile ftp_file = new FTPFile();
          ftp_file.setType(1);
          assertFalse(ftp_file.isUnknown());
    	}
      
        @Test
    	public void test_isUnknown_3() {
    	FTPFile ftp_file = new FTPFile();
          ftp_file.setType(2);
          assertFalse(ftp_file.isUnknown());
    	}
      
        @Test
    	public void test_isUnknown_4() {
    	FTPFile ftp_file = new FTPFile();
          ftp_file.setType(3);
          assertTrue(ftp_file.isUnknown());
    	}
        
        @Test
        public void test_isValid_1() {
      	FTPFile ftp_file = new FTPFile("drwxr-xr-x  5 admin staff  4096 Nov 23 09:30 documents");
            ftp_file.isValid();
      	}
        
        
          @Test
        public void test_isValid_2() {
      	FTPFile ftp_file = new FTPFile();
          ftp_file.setPermission(0,0,true);
            ftp_file.isValid();
      	}
          
          @Test
          public void test_getType() {
        	FTPFile ftp_file = new FTPFile();
            ftp_file.getType();
        	}
          
          
          
          
          @Test
          public void test_setName() {
        	FTPFile ftp_file = new FTPFile();
            ftp_file.setName("prova.txt");
        	}
          
          
          @Test
          public void test_getName() {
        	FTPFile ftp_file = new FTPFile();
            ftp_file.getName();
        	}
          
          @Test
          public void test_setSize() {
        	FTPFile ftp_file = new FTPFile();
            ftp_file.setSize(2048);
        	}
          
          
          @Test
          public void test_getSize() {
        	FTPFile ftp_file = new FTPFile();
            ftp_file.getSize();
        	}
            
           @Test
          public void test_setHardLinkCount() {
        	FTPFile ftp_file = new FTPFile();
            ftp_file.setHardLinkCount(3);
        	}
           
           
           @Test
           public void test_getHardLinkCount(){
         	FTPFile ftp_file = new FTPFile();
             ftp_file.getHardLinkCount();
         	}
           
            @Test
           public void test_setGroup(){
         	FTPFile ftp_file = new FTPFile();
             ftp_file.setGroup("Group1");
         	}
          
            
            @Test
          public void test_getGroup(){
        	FTPFile ftp_file = new FTPFile();
            ftp_file.getGroup();
        	}
            @Test
            public void test_setUser(){
          	FTPFile ftp_file = new FTPFile();
              ftp_file.setUser("admin");
          	}
            
               @Test
            public void test_getUser(){
          	FTPFile ftp_file = new FTPFile();
              ftp_file.getUser();
          	}
               
            @Test
            public void test_setLink(){
            FTPFile ftp_file = new FTPFile();
            ftp_file.setLink("link1");
             }
               
             @Test
             public void test_getLink(){
             FTPFile ftp_file = new FTPFile();
             ftp_file.getLink();
            }
               
                  
            @Test
            public void test_setTimeStamp(){
            FTPFile ftp_file = new FTPFile();
            Calendar calendar= Calendar.getInstance(TimeZone.getTimeZone("GMT"));
             calendar.set(2024, Calendar.NOVEMBER, 25, 10, 30, 0);
             ftp_file.setTimestamp(calendar);
                	}
               
             @Test
             public void test_getTimeStamp(){
             FTPFile ftp_file = new FTPFile();
             ftp_file.getTimestamp();
             }
                    
             @Test
             public void test_hasPermission_1(){
             FTPFile ftp_file = new FTPFile("drwxr-xr-x  5 admin staff  4096 Nov 23 09:30 documents");
             ftp_file.hasPermission(0,1);
             } 
                     
             @Test
             public void test_hasPermission2(){
             FTPFile ftp_file = new FTPFile();
             ftp_file.setPermission(0,0,true);
             ftp_file.hasPermission(0,1);
             }
                  
                     @Test
                     public void test_toString(){
                       FTPFile ftp_file = new FTPFile("drwxr-xr-x  5 admin staff  4096 Nov 23 09:30 documents");
                       ftp_file.toString();  
                     }
                     
                     @Test
                     public void test_toFormattedString_1(){
                       FTPFile ftp_file = new FTPFile("drwxr-xr-x  5 admin staff  4096 Nov 23 09:30 documents");
                       ftp_file.toFormattedString("Asia/Tokyo");
                       
                     }
                     
                     
                     @Test
                     public void test_toFormattedString_2(){
                     FTPFile ftp_file = new FTPFile();
                       ftp_file.setType(0);
                       ftp_file.setHardLinkCount(1);
                       ftp_file.setUser("user");
                       ftp_file.setGroup("group");
                       ftp_file.setSize(9999L);
                       ftp_file.setName("testfile");
                      
                       Calendar timestamp = Calendar.getInstance();
                       timestamp.set(2024, Calendar.DECEMBER, 1, 12, 0, 0); 
                       ftp_file.setTimestamp(timestamp);
                       
                       String timezone = "America/New_York";
                      
                       String result = ftp_file.toFormattedString(timezone);
                     
                   }
                     
                  
                     @Test
                     public void testToFormattedString_3() {
                         FTPFile ftp_file = new FTPFile();
                      
                         ftp_file.setType(0);
                         ftp_file.setHardLinkCount(1);
                         ftp_file.setUser("user");
                         ftp_file.setGroup("group");
                         ftp_file.setSize(6789L);
                         ftp_file.setName("examplefile");
                         

                         Calendar timestamp = Calendar.getInstance();
                         timestamp.set(2024, Calendar.DECEMBER, 1, 12, 0, 0);
                         ftp_file.setTimestamp(timestamp);

                         String timezone = null;
                       
                         String result = ftp_file.toFormattedString(timezone);
                       
                     }
                     
                     
                     
                     @Test
                   public void testToFormattedString_4() {
                       FTPFile ftp_file = new FTPFile();
                       

                       ftp_file.setType(0); 
                       ftp_file.setHardLinkCount(1);
                       ftp_file.setUser("user");
                       ftp_file.setGroup("group");
                       ftp_file.setSize(1234L);
                       ftp_file.setName("testfile");
                       
                    
                       ftp_file.setTimestamp(null);
                       
                       String timezone = null;
                       String result = ftp_file.toFormattedString(timezone);
                       
                   }
                     
                     
                     
                     @Test
                   public void test_toFormattedString5() {
                       FTPFile ftp_file = new FTPFile();
                       ftp_file.setType(0); 
                       ftp_file.setHardLinkCount(1);
                       ftp_file.setUser("user");
                       ftp_file.setGroup("group");
                       ftp_file.setSize(1234L);
                       ftp_file.setName("testfile");
                       Calendar timestamp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                       ftp_file.setTimestamp(timestamp);
                       
                       // Impostiamo un fuso orario diverso (es. "America/New_York")
                       String timezone = "America/New_York";
                       
                       String result = ftp_file.toFormattedString(timezone);
                   }

                     @Test
                     public void test_toFormattedString6() {
                      FTPFile ftp_file = new FTPFile();
                                     
                              ftp_file.setType(0); 
                              ftp_file.setHardLinkCount(1);
                              ftp_file.setUser("user");
                              ftp_file.setGroup("group");
                              ftp_file.setSize(1234L);
                              ftp_file.setName("testfile");
                             Calendar timestamp =Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                             ftp_file.setTimestamp(timestamp);
                             
                             // Chiamata al metodo con lo stesso fuso orario
                             String formattedString = ftp_file.toFormattedString("UTC");
                     }
                     
                     @Test
                     public void test_toFormattedString7() {
                      FTPFile ftp_file = new FTPFile();
                                     
                              ftp_file.setType(0); 
                              ftp_file.setHardLinkCount(1);
                              ftp_file.setUser("user");
                              ftp_file.setGroup("group");
                              ftp_file.setSize(1234L);
                              ftp_file.setName("testfile");
                            

                             // Impostiamo la data senza ora, minuto, secondo, millisecondo
                             Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                             calendar.set(2024, Calendar.NOVEMBER, 24); 

                             ftp_file.setTimestamp(calendar); 
                      
                             String formattedString = ftp_file.toFormattedString("UTC");
                     }
  
  
}
						