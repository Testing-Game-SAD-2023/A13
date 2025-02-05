package student.lorenzoCappellieriDue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ClassUnderTest.VCardBean;



class StudentTest {
	
	public VCardBean card;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		card = new VCardBean();
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	 @Test
	    public void testParseVCard1() {
	        card.setVCard("BEGIN:VCARD\nFN:Vincenzo Oshimen\nEND:VCARD");
	        assertNotEquals("Vincenzo", card.getFirstName());
	        assertNotEquals("Oshimen", card.getLastName());
	    }
	 @Test
	    public void testParseVCard2() {
	        card.setVCard("FN:Vincenzo Oshimen\nEND:VCARD");
	        assertNotEquals("Vincenzo", card.getFirstName());
	        assertNotEquals("Oshimen", card.getLastName());
	    }
	 @Test
	    public void testParseVCard2_End() {
	        card.setVCard("FN:Vincenzo Oshimen\n");
	        assertNotEquals("Vincenzo", card.getFirstName());
	        assertNotEquals("Oshimen", card.getLastName());
	    }
		String snull = null;
	 @Test
	    public void testParseVCard3() {
	        card.setVCard(snull);
	        assertNotEquals("Vincenzo", card.getFirstName());
	        assertNotEquals("Oshimen", card.getLastName());
	    }
	 
	 @Test
	    public void testParseVCard4() { 
		 card.setVCard("BEGIN:VCARD\nFN:Marco Antonio \n N:Marco\n ORG: Google \n EMAIL: imperatore@roma.it \n TEL: 000000001 \n TITLE: Imperatore END:VCARD");
		 assertTrue(card.isValidVCard());
	 }
	 
	 @Test
	    public void testParseVCard4_fax() { 
		 card.setVCard("BEGIN:VCARD\nFN:Marco Antonio \n N:Marco ;antonio \n ORG: Google \n EMAIL: imperatore@roma.it \n tel type = fax : 000000001 \n TITLE: Imperatore END:VCARD");
		 assertTrue(card.isValidVCard());
	 }
	 
	 @Test
	    public void testParseVCard4_() { 
		 card.setVCard("BEGIN:VCARD\nFN:Marco \n N:Marco;Antonio;Secondo\n ORG: Google \n EMAIL: imperatore@roma.it \n TEL;TYPE=FAX: 000000001; \n TITLE: Imperatore END:VCARD");
		 assertEquals("Marco",card.getLastName());
		 assertEquals("Antonio",card.getMiddleName());
		 assertEquals("Secondo",card.getFirstName());
		 }
	 
	 @Test
	    public void testParseVCard5_() { 
		 card.setVCard("BEGIN:VCARD\nFN:Marco \n N:Marco;Antonio;Secondo\n ORG: Google \n EMAIL: imperatore@roma.it \n TEL;TYPE=FAX: 000000001; \n TITLE: Imperatore END:VCARD");
		 assertEquals("Marco",card.getLastName());
		 assertEquals("Antonio",card.getMiddleName());
		 assertEquals("Secondo",card.getFirstName());
		 }
	 @Test
	    public void testgenerateVCard1() { 
		 card.setFirstName("Antonio");
		 card.setLastName("Marco");
		 card.setEmail("imperatore@roma.it");
		 card.setOrganization("Impero Romano");
		 assertNotEquals("Antonio",card.getVCard());
	 }
	 @Test
	    public void testgenerateVCard_a() { 
		 card.setFirstName("Antonio");
		 card.setLastName("Marco");
		 card.setEmail("imperatore@roma.it");
		 assertNotEquals("Antonio",card.getVCard());
	 }
	 
	 @Test
	    public void testgenerateVCard_b() { 
		 card.setFirstName("Antonio");
		 card.setEmail("imperatore@roma.it");
		 card.setOrganization("Impero Romano");
		 assertNotEquals("Antonio",card.getVCard());
	 }
	 @Test
	    public void testgenerateVCard_c() { 
		 card.setFirstName("Antonio");
		 card.setLastName("Marco");
		 card.setEmail("imperatore@roma.it");
		 card.setOrganization("Impero Romano");
		 assertNotEquals("Antonio",card.getVCard());
	 }
	
		
	 @Test
	    public void testgenerateVCard2() { 
		 card.setFirstName("Antonio");
		 card.setLastName("Marco");
		 card.setMiddleName("Secondo");
		 card.setFormattedName("Antonio;Marco;Secondo");
		 card.setEmail("imperatore@roma.it");
		 card.setOrganization("Impero Romano");
		 card.setTitle("Imperatore");
		 card.setFax("000000011");
		 card.setPhone("7000000011");
		 assertNotEquals("Antonio",card.getVCard());
	 }	 
	 
	 @Test
	    public void testgenerateVCard3() { 
		 card.setFirstName("Antonio");
		 card.setLastName("Marco");
		 card.setMiddleName("Secondo");
		 card.setEmail("imperatore@roma.it");
		 card.setOrganization("Impero Romano");
		 card.setTitle("Imperatore");
		 card.setFax("000000011");
		 card.setPhone("7000000011");
		 assertNotEquals("Antonio",card.getVCard());
	 }
	 
	 
		@Test
		void testToString() {
			card.setVCard("SPQR");
			assertNotEquals(card,card.toString());
		}
		
	
	@Test
	void testSetEGetVCard() {
        card.setVCard("Vincenzo");
        assertEquals("Vincenzo", card.getVCard());
	}

	@Test
	void testNullVCard() {
        card.setVCard(snull);
        assertNull(card.getVCard());
	}
	@Test
	void testSetEGetFirstName() {
        card.setFirstName("Vincenzo");
        assertEquals("Vincenzo", card.getFirstName());
	}

	@Test
	void testSetEGetLastName() {
        card.setLastName("Osimhen");
        assertEquals("Osimhen", card.getLastName());
	}
	
	
	@Test
	void testSetEGetMiddleName() {
        card.setMiddleName("Marechiaro");
        assertEquals("Marechiaro", card.getMiddleName());
	}
	
	@Test
	void testSetEGetFormattedName() {
        card.setFormattedName("Benevento");
        assertEquals("Benevento", card.getFormattedName());
	}
	
	@Test
	void testSetEGetTitle() {
        card.setTitle("Doc");
        assertEquals("Doc", card.getTitle());
	}
	
	@Test
	void testSetEGetPhone() {
		card.setPhone("081-0001926");
		assertEquals("081-0001926",card.getPhone());
	}
	
	@Test
	void testSetEGetFax() {
		card.setFax("081-0001926");
		assertEquals("081-0001926",card.getFax());
	}
	
	@Test
	void testSetEGetEmail() {
		card.setEmail("Vincic@gmail.com");
		assertEquals("Vincic@gmail.com",card.getEmail());
	}
	
	@Test
	void testSetEGetOrganization() {
		card.setOrganization("Redis");
		assertEquals("Redis",card.getOrganization());
	}
	
}
