package student.carolinaDiDonatoUno;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import ClassUnderTest.VCardBean;

class StudentTest{
	
	private VCardBean card;

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
		card = null;
	}

	@Test
	void testSetVCard() {
		String vCard = "Test";
		card.setVCard(vCard);
	}
	
	@Test
	void testSetVCardNULL() {
		String vCard = null;
		card.setVCard(vCard);
	}

	@ParameterizedTest
	@CsvFileSource(resources = "..\\SetVCard.csv")	
	void testSetVCardParameterized(String vCard) {
		card.setVCard(vCard);
	}
	
	@Test
	void testGetVCardNull() {
		assertNull(card.getVCard());
		//test a parte per l'assert
	}
	
	@Test
	void testGetVCard() {
		card.setFirstName("Carolina");
		card.setLastName("Di Donato");
		card.setEmail("caro.didonato@studenti.unina.it");
		card.setOrganization("Federico II");
		
		System.out.println("Test get v card: "+card.getVCard());
	}
	
	@ParameterizedTest
	@CsvFileSource(resources = "..\\GetVCard.csv")	
	void testGetVCardParameterized(String firstName, String lastName, String email,
			String organization, String formattedName, String middleName, 
			String title, String phone, String fax) {
		card.setFirstName(firstName);
		card.setLastName(lastName);
		card.setEmail(email);
		card.setOrganization(organization);
		card.setFormattedName(formattedName);
		card.setMiddleName(middleName);
		card.setTitle(title);
		card.setPhone(phone);
		card.setFax(fax);
		
		System.out.println("Test get v card: "+card.getVCard());
	}


	@Test
	void testGetFirstName() {
		//System.out.println("Test get first name: "+card.getFirstName());
		assertNull(card.getFirstName());
	}

	
	//implementare nella classe
	@Test
	void testSetFirstName() {
		String firstName = "Carolina";
		card.setFirstName(firstName);
		//System.out.println("Test set first name: "+card.getFirstName());
		assertEquals(firstName,card.getFirstName());

	}

	@Test
	void testGetLastName() {
		//System.out.println("Test get last name: "+card.getLastName());
		assertNull(card.getLastName());
	}

	
	//implementare nella classe
	@Test
	void testSetLastName() {
		String lastName = "Di Donato";
		card.setLastName(lastName);
		//System.out.println("Test set last name: "+card.getLastName());
		assertEquals(lastName,card.getLastName());
	}

	@Test
	void testGetMiddleName() {
		//System.out.println("Test get middle name: "+card.getMiddleName());
		assertNull(card.getMiddleName());
	}

	
	//implementare nella classe
	@Test
	void testSetMiddleName() {
		String middleName = "Carol";
		card.setMiddleName(middleName);
		//System.out.println("Test set middle name: "+card.getMiddleName());
		assertEquals(middleName,card.getMiddleName());
	}

	@Test
	void testGetFormattedName() {
		//System.out.println("Test get formatted name: "+card.getFormattedName());
		assertNull(card.getFormattedName());
	}

	
	//implementare nella classe
	@Test
	void testSetFormattedName() {
		String formattedName = "CDD";
		card.setFormattedName(formattedName);
		//System.out.println("Test set formatted name: "+card.getFormattedName());
		assertEquals(formattedName,card.getFormattedName());
	}

	@Test
	void testGetTitle() {
		//System.out.println("Test get title name: "+card.getTitle());
		assertNull(card.getTitle());
	}
	
	
	//implementare nella classe
	@Test
	void testSetTitle() {
		String title = "Student";
		card.setTitle(title);
		//System.out.println("Test set title name: "+card.getTitle());
		assertEquals(title,card.getTitle());
	}

	@Test
	void testGetOrganization() {
		//System.out.println("Test get organization name: "+card.getOrganization());
		assertNull(card.getOrganization());
	}

	
	//implementare nella classe
	@Test
	void testSetOrganization() {
		String organization = "Federico II";
		card.setOrganization(organization);
		//System.out.println("Test set organization name: "+card.getOrganization());
		assertEquals(organization,card.getOrganization());
	}

	@Test
	void testGetEmail() {
		//System.out.println("Test get email: "+card.getEmail());
		assertNull(card.getEmail());
	}

	
	//implementare nella classe
	@Test
	void testSetEmail() {
		String email = "caro.didonato@studenti.unina.it";
		card.setEmail(email);
		//System.out.println("Test set email: "+card.getEmail());
		assertEquals(email,card.getEmail());
	}

	@Test
	void testGetPhone() {
		//System.out.println("Test get phone: "+card.getPhone());
		assertNull(card.getPhone());
	}

	
	//implementare nella classe
	@Test
	void testSetPhone() {
		String phone = "3275673474";
		card.setPhone(phone);
		//System.out.println("Test set phone: "+card.getPhone());
		assertEquals(phone,card.getPhone());
	}

	@Test
	void testGetFax() {
		//System.out.println("Test get fax: "+card.getFax());
		assertNull(card.getFax());
	}

	
	//implementare nella classe
	@Test
	void testSetFax() {
		String fax = "+1 555-123-4567";
		card.setFax(fax);
		//System.out.println("Test set fax: "+card.getFax());
		assertEquals(fax,card.getFax());
	}

	@Test
	void testToString() {
		//System.out.println("Test toString: "+card.toString());
		assertNull(card.toString());
	}

	@Test
	void testIsValidVCard() {
		//System.out.println("Test isValid: "+card.isValidVCard());
		assertFalse(card.isValidVCard());
		
		card.setVCard("begin:vcard");
		//System.out.println("Test isValid: "+card.isValidVCard());
		assertTrue(card.isValidVCard());
	}

}
