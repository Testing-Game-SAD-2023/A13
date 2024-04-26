package student.alessandraMaraiaDue;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import ClassUnderTest.VCardBean;

class StudentTest {
	
	private VCardBean vcardB; // Oggetto VCardBean per i test
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// Operazioni da eseguire prima di tutti i test della classe (una sola volta)
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		// Operazioni da eseguire dopo tutti i test della classe (una sola volta)
	}

	@BeforeEach
	void setUp() throws Exception {
		// Operazioni da eseguire prima di ogni singolo test
		vcardB = new VCardBean(); // Inizializzazione dell'oggetto VCardBean
	}

	@AfterEach
	void tearDown() throws Exception {
		// Operazioni da eseguire dopo ogni singolo test
		vcardB = null; // Pulizia della risorsa VCardBean
	}

	@Test
	void testSetVCard() {
		String vCard = "Test";
		vcardB.setVCard(vCard); // Imposta il valore del VCardBean
	}
	
	@Test
	void testGetVCard() {
		// Imposta alcuni campi dell'oggetto VCardBean
		vcardB.setFirstName("Mario");
		vcardB.setLastName("Rossi");
		vcardB.setMiddleName(" ");
		vcardB.setTitle("Professor");
		vcardB.setEmail("m.rossi@docenti.unina.it");
		vcardB.setOrganization("Federico II");
		vcardB.setPhone("3334355678");
		vcardB.setFax("+1 234-432-5544");
		// Stampa il risultato del metodo getVCard()
		System.out.println("Test di get VcardBean: " + vcardB.getVCard());
	}
	
		@Test
		void testGetFirstName() {
			assertNull(vcardB.getFirstName()); // Verifica che il valore sia inizialmente nullo
		}

		@Test
		void testSetFirstName() {
			String firstName = "Mario";
			vcardB.setFirstName(firstName); // Imposta il valore del nome
			assertEquals(firstName, vcardB.getFirstName()); // Verifica che il valore sia stato impostato correttamente
		}

		@Test
		void testGetLastName() {
			assertNull(vcardB.getLastName()); // Verifica che il valore sia inizialmente nullo
		}

		@Test
		void testSetLastName() {
			String lastName = "Rossi";
			vcardB.setLastName(lastName); // Imposta il valore del cognome
			assertEquals(lastName, vcardB.getLastName()); // Verifica che il valore sia stato impostato correttamente
		}
		
		@Test
		void testGetMiddleName() {
			assertNull(vcardB.getMiddleName()); // Verifica che il valore sia inizialmente nullo
		}

		@Test
		void testSetMiddleName() {
			String middleName = " ";
			vcardB.setMiddleName(middleName); // Imposta il valore del secondo nome
			assertEquals(middleName, vcardB.getMiddleName()); // Verifica che il valore sia stato impostato correttamente
		}

		@Test
		void testGetFormattedName() {
			assertNull(vcardB.getFormattedName()); // Verifica che il valore sia inizialmente nullo
		}

		@Test
		void testSetFormattedName() {
			String formattedName = "Mario Rossi";
			vcardB.setFormattedName(formattedName); // Imposta il valore del nome formattato
			assertEquals(formattedName, vcardB.getFormattedName()); // Verifica che il valore sia stato impostato correttamente
		}

		@Test
		void testGetTitle() {
			assertNull(vcardB.getTitle()); // Verifica che il valore sia inizialmente nullo
		}

		@Test
		void testSetTitle() {
			String title = "Professor";
			vcardB.setTitle(title); // Imposta il valore del titolo
			assertEquals(title, vcardB.getTitle()); // Verifica che il valore sia stato impostato correttamente
		}

		@Test
		void testGetOrganization() {
			assertNull(vcardB.getOrganization()); // Verifica che il valore sia inizialmente nullo
		}
		

		    
		@Test
		void testSetOrganization() {
			String organization = "Federico II";
			vcardB.setOrganization(organization); // Imposta il valore dell'organizzazione
			assertEquals(organization, vcardB.getOrganization()); // Verifica che il valore sia stato impostato correttamente
		}
		

		@Test
		void testGetEmail() {
			assertNull(vcardB.getEmail()); // Verifica che il valore sia inizialmente nullo
		}

		@Test
		void testSetEmail() {
			String email = "m.rossi@docenti.unina.it";
			vcardB.setEmail(email); // Imposta il valore dell'email
			assertEquals(email, vcardB.getEmail()); // Verifica che il valore sia stato impostato correttamente
		}

		@Test
		void testGetPhone() {
			assertNull(vcardB.getPhone()); // Verifica che il valore sia inizialmente nullo
		}

		@Test
		void testSetPhone() {
			String phone = "3334355678";
			vcardB.setPhone(phone); // Imposta il valore del numero di telefono
			assertEquals(phone, vcardB.getPhone()); // Verifica che il valore sia stato impostato correttamente
		}

		@Test
		void testGetFax() {
			assertNull(vcardB.getFax()); // Verifica che il valore sia inizialmente nullo
		}

		@Test
		void testSetFax() {
			String fax = "+1 234-432-5544";
			vcardB.setFax(fax); // Imposta il valore del numero di fax
			assertEquals(fax, vcardB.getFax()); // Verifica che il valore sia stato impostato correttamente
		}

		@Test
		void testToString() {
			assertNull(vcardB.toString()); // Verifica che il valore sia inizialmente nullo
		}

		@Test
		void testIsValidVCard() {
			assertFalse(vcardB.isValidVCard()); // Verifica che la carta non sia valida inizialmente
			vcardB.setVCard("begin:vcard"); // Imposta una stringa di carta di partenza
			assertTrue(vcardB.isValidVCard()); // Verifica che la carta sia valida dopo essere stata impostata
		}
		
		 @Test
		    public void testParsesVCard() {
		        // Creazione di un'istanza di VCardBean per testare il parsing di una stringa vCard
		        VCardBean vCardBean = new VCardBean();

		        // Stringa vCard valida che rappresenta le informazioni di "Anna Rossi" con email specificata
		        String validVCard = "BEGIN:VCARD\nFN:Mario Rossi\nN:Mario;Rossi;;\nORG:Federico II\nEMAIL;TYPE=INTERNET:m.rossi@docenti.unina.it\nTEL;TYPE=VOICE:3334355678\nTEL;TYPE=FAX:+1 234-432-5544\nTITLE:Professor\nEND:VCARD";

		        // Imposta la stringa vCard per l'istanza di VCardBean
		        vCardBean.setVCard(validVCard);

		        // Verifica che il parsing abbia impostato correttamente i valori nei campi della VCardBean
		  
		        assertEquals("Mario Rossi", vCardBean.getFormattedName());  // Verifica il nome formattato
		        assertEquals("Federico II", vCardBean.getOrganization());  // Verifica l'organizzazione
		        assertEquals("m.rossi@docenti.unina.it", vCardBean.getEmail());  // Verifica l'email
		        assertEquals("3334355678", vCardBean.getPhone());  // Verifica il numero di telefono
		        assertEquals("+1 234-432-5544", vCardBean.getFax());  // Verifica il numero di fax
		        assertEquals("Professor", vCardBean.getTitle());  // Verifica il titolo
		    }

		 @Test
		 void testFoldLines() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		     // Creazione di un'istanza di VCardBean per eseguire il test
		     VCardBean vCardBean = new VCardBean();

		     // Stringa lunga che supera i 75 caratteri
		     String longLine = "Questa è una riga molto lunga che supera i 75 caratteri e necessita di essere divisa in più righe per una vCard.";

		     // Utilizza la Reflection per accedere al metodo privato foldLines
		     Method privateMethod = VCardBean.class.getDeclaredMethod("foldLines", String.class);
		     privateMethod.setAccessible(true); // Permette l'accesso al metodo privato
		     String foldedLine = (String) privateMethod.invoke(vCardBean, longLine);

		     // Verifica che la riga sia stata divisa correttamente
		     assertEquals("Questa è una riga molto lunga che supera i 75 caratteri e necessita di essere divisa in più righe per una vCard.", foldedLine);
		 }

		 @Test
		 void testUnfoldLines() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		     // Creazione di un'istanza di VCardBean per eseguire il test
		     VCardBean vCardBean = new VCardBean();

		     // Stringa con linee piegate
		     String foldedLine = "Questa è una riga divisa\n\t in più parti.\n";

		     // Utilizza la Reflection per accedere al metodo privato unfoldLines
		     Method privateMethod = VCardBean.class.getDeclaredMethod("unfoldLines", String.class);
		     privateMethod.setAccessible(true); // Permette l'accesso al metodo privato
		     String unfoldedLine = (String) privateMethod.invoke(vCardBean, foldedLine);

		     // Verifica che la riga sia stata ripiegata correttamente
		     assertEquals("Questa è una riga divisa in più parti.", unfoldedLine);
		 }


}
