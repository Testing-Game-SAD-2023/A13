package student.davideMancinelliDue;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Hashtable;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.XmlElement;


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
		Hashtable <String, String> hash1 = new Hashtable<>();
		Hashtable <String, String> hash2 = new Hashtable<>();
		XmlElement prova = new XmlElement("prova",hash1);
		XmlElement prova2 = new XmlElement("prova",hash2.get("key"));	// Per "rendere" null un'hashtable, che non presenta alcun valore null
		XmlElement prova3 = new XmlElement("prova",hash1);
		prova3.addAttribute("12", "qualcosa");
		System.out.println(prova.hashCode());
		System.out.println(prova2.hashCode());
		System.out.println(prova3.hashCode());
		
		// non riesco a mettere null i subelement, ma in generale in tutte le funzioni
	}

	@Test
	void testNotifyObservers() {
		XmlElement prova = new XmlElement("prova");
		prova.notifyObservers();
	}

	@Test
	void testXmlElement() {
		XmlElement prova = new XmlElement();
		
	}

	@Test
	void testXmlElementString() {
		XmlElement prova = new XmlElement("prova");
	
	}

	@Test
	void testXmlElementStringHashtableOfStringString() {
		Hashtable <String, String> hash = new Hashtable<>();
		hash.put("1","napoli");
		XmlElement prova = new XmlElement("prova",hash);

	}

	@Test
	void testXmlElementStringString() {
		XmlElement prova = new XmlElement("prova", "napoli");

	}

	@Test
	void testAddAttribute() {
		XmlElement prova = new XmlElement();
		String value = "12";
		String name = "prova";
		prova.addAttribute(name, value);
		prova.addAttribute(name, null);
		prova.addAttribute(null,value);
		prova.addAttribute(null, null);

	}

	@Test
	void testGetAttributeString() {
		
		String prova = "prova";
		XmlElement elemento = new XmlElement(); 
		System.out.println("attributeString: "+elemento.getAttribute(prova));
		elemento.addAttribute(prova, "valore");
		System.out.println("attributeString: "+elemento.getAttribute(prova));
		

	}

	@Test
	void testGetAttributeStringString() {
		String qualcosa = new String("prova");
		String generico = new String("default");
		XmlElement elemento = new XmlElement(); 
		elemento.addAttribute(qualcosa, generico);
		System.out.println("attributeStringString: "+elemento.getAttribute(qualcosa,generico));
		System.out.println("attributeStringString: "+elemento.getAttribute("peppe","base"));

	}

	@Test
	void testGetAttributes() {
		XmlElement elemento = new XmlElement();
		System.out.println(elemento.getAttributes());

	}

	@Test
	void testSetAttributes() {
		XmlElement elemento = new XmlElement();
		Hashtable <String, String> hash = new Hashtable<>();
		hash.put("1","napoli");
		elemento.setAttributes(hash);

	}

	@Test
	void testGetAttributeNames() {
		XmlElement elemento = new XmlElement(); 
		elemento.addAttribute("attributo", "nome");
		System.out.println("getattributename: "+elemento.getAttributeNames());
		
	}

	@Test
	void testAddElement() {
		
		XmlElement elemento = new XmlElement();
		XmlElement secondo = new XmlElement("prova");
		elemento.addElement(secondo);

	}

	@Test
	void testRemoveElementXmlElement() {
		
		XmlElement elemento = new XmlElement();
		XmlElement secondo = new XmlElement("prova");
		XmlElement terzo = new XmlElement("napoli");
		elemento.addElement(secondo);
		elemento.removeElement(secondo);			// non riesco a fare l'altro branch
		elemento.removeElement(terzo);

	}

	@Test
	void testRemoveElementInt() {
		
		XmlElement elemento = new XmlElement();
		XmlElement secondo = new XmlElement("prova");
		elemento.addElement(secondo);
		elemento.removeElement(0);
		//elemento.removeElement(1);

	}

	@Test
	void testRemoveAllElements() {
		
		XmlElement elemento = new XmlElement();
		XmlElement secondo = new XmlElement("prova");
		XmlElement terzo = new XmlElement("napoli");
		XmlElement quarto = new XmlElement("qualcosa");
		elemento.addElement(secondo);
		elemento.addElement(terzo);
		elemento.addElement(quarto);
		System.out.println("Prima cancellazione: "+elemento);
		elemento.removeAllElements();
		System.out.println("Dopo cancellazione: "+elemento);

	}

	@Test
	void testRemoveFromParent() {
		
		XmlElement elemento = new XmlElement();
		elemento.removeFromParent();
		
		XmlElement padre = new XmlElement("prova", "napoli");
		elemento.setParent(padre);
		elemento.removeFromParent();

	}

	@Test
	void testAppend() {
		
		XmlElement elemento1 = new XmlElement("prova", "napoli");
		XmlElement elemento2 = new XmlElement("hamsik", "cavani");
		
		elemento1.append(elemento2);
		System.out.println("append: "+elemento1.getElement(0));
		
	}

	@Test
	void testInsertElement() {
		
		XmlElement elemento1 = new XmlElement("prova", "napoli");
		XmlElement secondo = new XmlElement("prova");
		elemento1.insertElement(secondo, 0);	

	}

	@Test
	void testGetElements() {
		
		XmlElement elemento1 = new XmlElement("prova", "napoli");
		XmlElement sub1 = new XmlElement("qualcosa", "rosa");
		XmlElement sub2 = new XmlElement("something", "rose");
		XmlElement sub3 = new XmlElement("alguna cosa", "rosita");
		elemento1.addSubElement(sub1);
		elemento1.addSubElement(sub2);
		elemento1.addSubElement(sub3);
		System.out.println("getElement: "+elemento1.getElements());
	
	}

	@Test
	void testCount() {
		
		XmlElement elemento1 = new XmlElement("prova", "napoli");
		XmlElement elemento2 = new XmlElement("hamsik", "cavani");
		XmlElement elemento3 = new XmlElement("walter", "mazzari");
		elemento1.append(elemento2);
		elemento1.append(elemento3);
		System.out.println(elemento1.count());
		elemento1.removeElement(elemento2);
		System.out.println(elemento1.count());
		

	}

	@Test
	void testGetElementString() {
		
		XmlElement elemento1 = new XmlElement("prova", "napoli");
		elemento1.addSubElement("qualcosa.com");				// per if riga 174 (i>0), con i posizione del .
		elemento1.addSubElement(".qualcosacom");				// per if riga 169 (i==0), con i posizione del .
		System.out.println("getelementstring1: "+elemento1.getElement("qualcosa.com"));
		System.out.println("getelementstring2: "+elemento1.getElement(".qualcosacom"));
		System.out.println("getelementstring3: "+elemento1.getElement("volo.volo"));
	}

	@Test
	void testGetElementInt() {
		XmlElement elemento1 = new XmlElement("prova", "napoli");
		XmlElement elemento2 = new XmlElement("qualcosa", "rosa");
		elemento1.addSubElement(elemento2);
		System.out.println("getElementInt: "+elemento1.getElement(0));
	}

	@Test
	void testAddSubElementString() {
		XmlElement elemento1 = new XmlElement("prova", "napoli");
		elemento1.addSubElement("qualcosa.com");
		elemento1.addSubElement("/qualcosa");
		elemento1.addSubElement(".qualcosa");		// per l'if riga 219 (name si ha prima del .)
		
		// nell'if riga 222 l'altro branch mancante è parent == null, ma non si può fare perchè
		// a inizio funzione viene inizializzato a this. anche con removefromparent non va bene
		// perchè prima rimuove, poi chiamando la funzione, il parent viene rinizializzato
	}

	@Test
	void testAddSubElementXmlElement() {
		XmlElement elemento1 = new XmlElement("prova", "napoli");
		XmlElement elemento2 = new XmlElement("qualcosa", "rosa");
		elemento1.addSubElement(elemento2);
	}

	@Test
	void testAddSubElementStringString() {
		XmlElement elemento1 = new XmlElement("prova", "napoli");
		elemento1.addSubElement("qualcosa", "dati0");
	}

	@Test
	void testSetParent() {
		XmlElement elemento1 = new XmlElement("prova", "napoli");
		XmlElement elemento2 = new XmlElement("hamsik", "cavani");
		elemento1.setParent(elemento2);
	}

	@Test
	void testGetParent() {
		XmlElement elemento1 = new XmlElement("prova", "napoli");
		XmlElement elemento2 = new XmlElement("hamsik", "cavani");
		elemento1.setParent(elemento2);
		System.out.println("Genitore: "+elemento1.getParent());
		
	}

	@Test
	void testSetData() {
		XmlElement elemento1 = new XmlElement("prova", "napoli");
		elemento1.setData("dati");
	}

	@Test
	void testGetData() {
		XmlElement elemento1 = new XmlElement("prova", "napoli");
		elemento1.setData("dati");
		System.out.println("Dati: "+elemento1.getData());
	}

	@Test
	void testGetName() {
		XmlElement nomedamostrare = new XmlElement("prova", "napoli");
		System.out.println("getName: "+nomedamostrare.getName());
	}

	@Test
	void testPrintNode() {
		XmlElement node1 = new XmlElement("node", "nodone");
		node1.setData(null);									// riga 313 data==null TF
		XmlElement node2 = new XmlElement("foro", "foto");
		node2.setData("");									// riga 313 data=="" FT	
		XmlElement node3 = new XmlElement("xml", "forbici");
		node3.setData("abra");									// riga 313 FF
																// impossibile fare riga 313 con if TT, poichè non può mai essere vera e falsa contemporanemante
		XmlElement.printNode(node1, "-");
		XmlElement.printNode(node2, "-");
		XmlElement.printNode(node3, "-");
		
		
		// per riga 323, devo aggiungere degli attributi
		XmlElement node4 = new XmlElement("cuffie", "auricolari");
		node4.addAttribute("pippo", "pluto");
		XmlElement.printNode(node4, "-");
		
		// per riga 332, devo aggiungere degli elementi
		XmlElement node5 = new XmlElement("nuovo", "nodo");
		XmlElement secondo = new XmlElement("secondo");
		XmlElement terzo = new XmlElement("terzo");
		node5.insertElement(secondo, 0);
		node5.insertElement(terzo, 1);
		XmlElement.printNode(node5, "-");
	}

	@Test
	void testClone() {
		XmlElement elemento1 = new XmlElement("prova", "napoli");
		// no attributi (if riga 346) e no sottoelementi (if riga 350)
		System.out.println("testclone1: "+elemento1.clone());
		
		XmlElement elemento2 = new XmlElement("hams","cav");
		System.out.println("testclone2: "+elemento2.clone());
		
		XmlElement elemento3 = new XmlElement("walter", "mazzarri");
		elemento3.setAttributes(null);						// attributes null (riga 346)
		elemento3.addSubElement("qualcosa", "dati0");
		// con sottoelementi (if riga 350) e attributi nulli
		System.out.println("testclone3: "+elemento3.clone());

	}

	@Test
	void testSetName() {
		XmlElement elemento1 = new XmlElement("prova", "napoli");
		System.out.println("Nome originale: "+elemento1.getName());
		elemento1.setName("nuovonome");
		System.out.println("Nome nuovo: "+elemento1.getName());
	}

	@Test
	void testEqualsObject() {
		
		// Casi base senza altri elementi
		XmlElement elemento1 = new XmlElement("prova", "napoli");
		XmlElement elemento2 = new XmlElement("prova", "napoli");
		XmlElement elemento3 = new XmlElement("scrivo", "qualcosa");
		
		System.out.println("elemento1 elemento2: "+elemento1.equals(elemento2));
		assertTrue(elemento1.equals(elemento2));
		
		System.out.println("elemento1 elemento3: "+elemento1.equals(elemento3));
		assertFalse(elemento1.equals(elemento3));
		
		
		// Caso null 
		Hashtable <String, String> hash1 = new Hashtable<>();
		Hashtable <String, String> hash2 = new Hashtable<>();
		XmlElement elemento4 = new XmlElement(null, hash1.get("key"));
		XmlElement elemento5 = new XmlElement(null, hash2.get("key"));
		
		System.out.println("elemento4 elemento5: "+elemento4.equals(elemento5));	// if 395   TT
		assertTrue(elemento4.equals(elemento5));
		
		System.out.println("elemento3 elemento4: "+elemento4.equals(elemento3));	// if 395   TF
		assertFalse(elemento4.equals(elemento3));

		System.out.println("elemento1 elemento4: "+elemento1.equals(elemento4));	// if 395   FT
		assertFalse(elemento1.equals(elemento4));
		
		
		// Casi nell'if 411
		
		// Caso attributi
		XmlElement elemento6 = new XmlElement("prova", "napoli");
		XmlElement elemento7 = new XmlElement("prova", "napoli");
		elemento6.addAttribute("abc", "cosa");
		elemento7.addAttribute("abc", "cosa");
		System.out.println("elemento6 elemento7 attributi: "+elemento6.equals(elemento7));	//riga 411 T
		
		XmlElement elemento8 = new XmlElement("prova", "napoli");
		elemento8.addAttribute("grt", "tyui");
		System.out.println("elemento6 elemento8 attributi: "+elemento6.equals(elemento8));	//riga 411 F
		
		// Caso dati e nome
		XmlElement elemento9 = new XmlElement("prova", "napoli");
		XmlElement elemento10 = new XmlElement("prova", "napoli");
		XmlElement elemento11 = new XmlElement("prova", "napoli");
		elemento9.setData("dato1");
		elemento10.setData("dato2");
		elemento11.setData("dato1");
		elemento9.setName("elementoimp");
		elemento10.setName("elementoimp");
		elemento11.setName("elementodddimp");
		System.out.println("elemento9 elemento11 dati e nome: "+elemento9.equals(elemento11));		//riga 412 nome TF
		
		
		// Caso sottoelementi
		XmlElement elemento12 = new XmlElement("prova", "napoli");
		XmlElement elemento13 = new XmlElement("prova", "napoli");
		elemento12.addSubElement("qual.cosa");
		elemento13.addSubElement("q.ual");
		System.out.println("elemento12 elemento13 sottoelementi: "+elemento12.equals(elemento13));		//riga 413 nome F
	}

}
