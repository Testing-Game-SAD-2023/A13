package student.stefanoAngeloRivielloUno;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.VCardBean;

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


	
/////////INIZIO TEST-> SET VCARD /////////////
	
	//Caso1: SetVCardWithVCard
	@Test
	void testSetVCardWithVCard() {
		VCardBean vCardEse2 = new VCardBean();
		String VCard2 = "BEGIN:VCARD\nFN:Stefano\nN:Riviello;Angelo;StAR\nTITLE:Studente\nORG:SWTesting\nEMAIL;TYPE=INTERNET:st.riviello@studenti.unina.it\nTEL;TYPE=VOICE:123456789\nTEL;TYPE=FAX:123SAR\nEND:VCARD";
		vCardEse2.setVCard(VCard2);
		
	}


	//Caso2: SetVCardWithNullParameterVCard
	@Test
    void testSetVCardWithNullParameterVCard() {
        VCardBean vCardEse2 = new VCardBean();
        String VCard2FullEmpty  = null;
        vCardEse2.setVCard(VCard2FullEmpty);
        
    }
	
	
	
	

	
/////////INIZIO TEST-> GET VCARD /////////////
	
	 
	@Test
	 void testGetVCardNullParameter() {
	    VCardBean vCardEse2 = new VCardBean();
	    assertNull(vCardEse2.getVCard());        
	 }
	 
	 
	 @Test
	 void testGetVCardWithVCard() {
		VCardBean vCardEse2 = new VCardBean();
		String validVCard = "BEGIN:VCARD\nFN:Stefano\nN:Riviello;Angelo;StAR\nTITLE:Studente\nORG:SWTesting\nEMAIL;TYPE=INTERNET:st.riviello@studenti.unina.it\nTEL;TYPE=VOICE:123456789\nTEL;TYPE=FAX:123SAR\nEND:VCARD";   
		vCardEse2.setVCard(validVCard);
	    assertEquals(validVCard, vCardEse2.getVCard());
	}
	

	 
/////////INIZIO TEST-> GET E SET DEI VARI ATTRIBUTI /////////////
	 
	@Test
	void testGetFirstName() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setFirstName("Stefano");
		assertEquals("Stefano",vCardEse2.getFirstName());
	}

	@Test
	void testSetFirstName() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setFirstName("Stefano");
		assertEquals("Stefano",vCardEse2.getFirstName());
	}

	@Test
	void testGetLastName() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setLastName("Riviello");
		assertEquals("Riviello",vCardEse2.getLastName());
	}

	@Test
	void testSetLastName() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setLastName("Riviello");
		assertEquals("Riviello",vCardEse2.getLastName());
	}
	
	@Test
	void testGetMiddleName() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setMiddleName("Angelo");
		assertEquals("Angelo",vCardEse2.getMiddleName());
	}

	@Test
	void testSetMiddleName() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setMiddleName("Angelo");
		assertEquals("Angelo",vCardEse2.getMiddleName());
	}

	@Test
	void testGetFormattedName() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setFormattedName("StAR");
		assertEquals("StAR",vCardEse2.getFormattedName());
	}

	@Test
	void testSetFormattedName() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setFormattedName("StAR");
		assertEquals("StAR",vCardEse2.getFormattedName());
	}

	@Test
	void testGetTitle() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setTitle("Studente");
		assertEquals("Studente",vCardEse2.getTitle());
	}
	

	@Test
	void testSetTitle() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setTitle("Studente");
		assertEquals("Studente",vCardEse2.getTitle());
	}

	@Test
	void testGetOrganization() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setOrganization("SWTesting");
		assertEquals("SWTesting",vCardEse2.getOrganization());
	}

	@Test
	void testSetOrganization() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setOrganization("SWTesting");
		assertEquals("SWTesting",vCardEse2.getOrganization());
	}

	@Test
	void testGetEmail() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setEmail("st.riviello@studenti.unina.it");
		assertEquals("st.riviello@studenti.unina.it",vCardEse2.getEmail());
	}

	@Test
	void testSetEmail() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setEmail("st.riviello@studenti.unina.it");
		assertEquals("st.riviello@studenti.unina.it",vCardEse2.getEmail());
	}

	@Test
	void testGetPhone() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setPhone("123456789");
		assertEquals("123456789",vCardEse2.getPhone());
	}

	@Test
	void testSetPhone() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setPhone("123456789");
		assertEquals("123456789",vCardEse2.getPhone());
	}

	@Test
	void testGetFax() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setFax("123SAR");
		assertEquals("123SAR",vCardEse2.getFax());
	}

	@Test
	void testSetFax() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setFax("123SAR");
		assertEquals("123SAR",vCardEse2.getFax());
	}

/////////INIZIO TEST-> GENERATE  VCARD /////////////
	
	
	//// TEST PER SODDISFARE TUTTI I RAMI DEL PRIMO IF DI GENERATE VCARD
	
	
	/// Caso 1: Tutti i campi richiesti sono non null
	
	@Test
    void GenerateVCard1() {
		 VCardBean vCardEse2 = new VCardBean();
		 vCardEse2.setFirstName("Stefano");
		 vCardEse2.setLastName("Riviello");
		 vCardEse2.setFormattedName("StAR");
		 vCardEse2.setMiddleName("Angelo");
		 vCardEse2.setTitle("Studente");
		 vCardEse2.setPhone("123456789");
		 vCardEse2.setFax("123SAR");
		 vCardEse2.setEmail("st.riviello@studenti.unina.it");
		 vCardEse2.setOrganization("Studente");
		 assertNotNull(vCardEse2.getVCard());
                       
    }
	
	
	
	
	/// Caso 2: firstName è null
	@Test
	 void GenerateVCard2() {
		 VCardBean vCardEse2 = new VCardBean();
		 vCardEse2.setFirstName(null);
		 vCardEse2.setLastName("Riviello");
		 vCardEse2.setEmail("st.riviello@studenti.unina.it");
		 vCardEse2.setOrganization("Studente");
		 assertNull(vCardEse2.getVCard());
                       
    }
	
	 /// Caso 3: lastName è null
	@Test
	 void GenerateVCard3() {
		 VCardBean vCardEse2 = new VCardBean();
		 vCardEse2.setFirstName("Stefano");
		 vCardEse2.setLastName(null);
		 vCardEse2.setEmail("st.riviello@studenti.unina.it");
		 vCardEse2.setOrganization("Studente");
		 assertNull(vCardEse2.getVCard());
                       
    }
	 
	/// Caso 4: email è null
	@Test
    void GenerateVCard4() {
		 VCardBean vCardEse2 = new VCardBean();
		 vCardEse2.setFirstName("Stefano");
		 vCardEse2.setLastName("Riviello");
		 vCardEse2.setEmail(null);
		 vCardEse2.setOrganization("Studente");
		 assertNull(vCardEse2.getVCard());
                       
    }
	
    /// Caso 5: organization è null
	@Test
    void GenerateVCard5() {
		 VCardBean vCardEse2 = new VCardBean();
		 vCardEse2.setFirstName("Stefano");
		 vCardEse2.setLastName("Riviello");
		 vCardEse2.setEmail("st.riviello@studenti.unina.it");
		 vCardEse2.setOrganization(null);
		 assertNull(vCardEse2.getVCard());
                       
    }
		
	 
	//////TEST PER SODDISFARE TUTTI I RAMI DELLA CONDIZIONE -> MIDDEL NAME,PHONE,FAX ////////	
	@Test
	void testGenerateVCardWithMiddleNamePhoneFax() {
	    VCardBean vCardEse2 = new VCardBean();
	    vCardEse2.setFirstName("Stefano");
	    vCardEse2.setLastName("Riviello");
	    vCardEse2.setEmail("st.riviello@studenti.unina.it");
	    vCardEse2.setOrganization("SWTesting");
	    vCardEse2.setMiddleName("Angelo");
	    vCardEse2.setPhone("123456789");
	    vCardEse2.setFax("123SAR");
	    
	    String result = vCardEse2.getVCard();
	    assertNotNull(result);
	    assertTrue(result.contains("Angelo"));
	    assertTrue(result.contains("123456789"));
	    assertTrue(result.contains("123SAR"));
	}

	@Test
	void testGenerateVCardWithoutMiddleNamePhoneFax() {
	    VCardBean vCardEse2 = new VCardBean();
	    vCardEse2.setFirstName("Stefano");
	    vCardEse2.setLastName("Riviello");
	    vCardEse2.setEmail("st.riviello@studenti.unina.it");
	    vCardEse2.setOrganization("SWTesting");
	    
	    
	    String result = vCardEse2.getVCard();
	    assertNotNull(result);
	    assertFalse(result.contains("Angelo"));
	    assertFalse(result.contains("123456789"));
	    assertFalse(result.contains("123SAR"));
	}

	@Test
	void testGenerateVCardWithNullMiddleNamePhoneFax() {
		VCardBean vCardEse2 = new VCardBean();
	    vCardEse2.setFirstName("Stefano");
	    vCardEse2.setLastName("Riviello");
	    vCardEse2.setEmail("st.riviello@studenti.unina.it");
	    vCardEse2.setOrganization("SWTesting");
	    vCardEse2.setMiddleName(null);
	    vCardEse2.setPhone(null);
	    vCardEse2.setFax(null);
	    
	    String result = vCardEse2.getVCard();
	    assertNotNull(result);
	    assertFalse(result.contains("Angelo"));
	    assertFalse(result.contains("123456789"));
	    assertFalse(result.contains("123SAR"));
	}

	@Test
	void testGenerateVCardWithEmptyMiddleNamePhoneFax() {
	    VCardBean vCardEse2 = new VCardBean();
	    vCardEse2.setFirstName("Stefano");
	    vCardEse2.setLastName("Riviello");
	    vCardEse2.setEmail("st.riviello@studenti.unina.it");
	    vCardEse2.setOrganization("SWTesting");
	    vCardEse2.setMiddleName("");
	    vCardEse2.setPhone("");
	    vCardEse2.setFax("");
	    
	    String result = vCardEse2.getVCard();
	    assertNotNull(result);
	    assertFalse(result.contains("Angelo"));
	    assertFalse(result.contains("123456789"));
	    assertFalse(result.contains("123SAR"));
	}

	
	
/////////INIZIO TEST-> PARSE  VCARD /////////////

	@Test
	void testParseVCardWithNameMinusThreeAndEmptyFNVCard() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setVCard("BEGIN:VCARD\nN:Ri\nEND:VCARD");
		vCardEse2.getVCard();
		
		assertNotNull(vCardEse2.getVCard());
	    
		
	}
	
	
	@Test
	void testParseVCardWithInValidStartVCard() {
		VCardBean vCardEse2 = new VCardBean();
		vCardEse2.setVCard("FN:Stefano\nEND:VCARD");
		vCardEse2.getVCard();
		
		assertNotNull(vCardEse2.getVCard());
	    
		
	}
	
	@Test
	 void testParseVCardWithDifferentTypeTitle() {
	    VCardBean vCardEse2 = new VCardBean();
	    vCardEse2.setVCard("BEGIN:VCARD\nFN:Stefano\nOTHER:Title\nEND:VCARD");
	    vCardEse2.getVCard();

	    assertNull(vCardEse2.getTitle());
	 }
	    
	    
	//Caso1: (senza ":") -> Malformed Card
	@Test
    void testParseMalformedVCardWithNegativeIndex() {
        VCardBean vCardEse2 = new VCardBean();
       
        vCardEse2.setVCard("BEGIN:VCARD\nFNStefano\nEND:VCARD");
        
        assertNotNull(vCardEse2.getVCard());
        assertNull(vCardEse2.getLastName());
       
    }

	//Caso2: (con ":" finali ) -> VCardWithIndexEqualsLengthMinusOne
    @Test
    void testParseVCardWithIndexEqualsLengthMinusOne() {
        VCardBean vCardEse2 = new VCardBean();
        
        vCardEse2.setVCard("BEGIN:VCARD\nFN:Stefano:\nEND:VCARD");

        vCardEse2.getVCard();
        
        assertNotNull(vCardEse2.getVCard());
        assertNull(vCardEse2.getFirstName());
      
    }
   
    
   
/////////INIZIO TEST-> TO STRING /////////////	
		
  
 @Test
void testToString() {

    VCardBean vCardEse2 = new VCardBean();

    // Verifico che il metodo restituisca una stringa vuota o null quando non sono stati impostati dati
    assertNull(vCardEse2.toString());

    //setto i  dati 
    vCardEse2.setFirstName("Stefano");
    vCardEse2.setLastName("Riviello");
    vCardEse2.setEmail("st.riviello@studenti.unina.it");
    vCardEse2.setOrganization("Studente");

    // Verifico che il metodo restituisca una stringa non nulla
    assertNotNull(vCardEse2.toString());
}

/////////INIZIO TEST-> IS VALID /////////////	

	///Caso 1: Verifica che il metodo isValidVCard restituisca true quando vCard è non null
	@Test
	void testIsValidVCard() {
		 VCardBean vCardEse2 = new VCardBean();
		 String validVCard = "BEGIN:VCARD\nFN:Stefano\nN:Riviello;Angelo;StAR\nTITLE:Studente\nORG:SWTesting\nEMAIL;TYPE=INTERNET:st.riviello@studenti.unina.it\nTEL;TYPE=VOICE:123456789\nTEL;TYPE=FAX:123SAR\nEND:VCARD"; 
		 vCardEse2.setVCard(validVCard);
		 assertTrue(vCardEse2.isValidVCard());
	}
	
	///Caso 2: Verifica che il metodo isValidVCard restituisca false quando vCard è null
	@Test
    void testIsValidVCardWhenNull() {
        VCardBean vCardEse2 = new VCardBean();
        String invalidVCard =null;
		vCardEse2.setVCard(invalidVCard);
        assertFalse(vCardEse2.isValidVCard());
    }

	
/////////INIZIO TEST-> FOLDLINES /////////////	
	
	/* @Test
    void testFoldLinesWithLongString() {
        String str = "Stringa con caratari > 75 : " + "A".repeat(80);
        String foldedString = foldLines(str);
        
        assertTrue(foldedString.contains("\r\n\t"));
    }*/
	

}
