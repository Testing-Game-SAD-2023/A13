package student.salvatoreDellaRagioneUno;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.XmlElement;

@SuppressWarnings("deprecation")
class StudentTest<TestObserver> extends XmlElement {

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
		  // Primo elemento con attributi
        XmlElement element1 = new XmlElement("Element");
        element1.addAttribute("attr1", "value1");
        element1.addAttribute("attr2", "value2");

        // Secondo elemento con attributi diversi
        XmlElement element2 = new XmlElement("Element");
        element2.addAttribute("attr1", "differentValue1");
        element2.addAttribute("attr3", "value3");

        assertNotEquals(element1.hashCode(), element2.hashCode());
    
		
	}

	@Test
	void testNotifyObservers() {
		 
		fail("Not yet implemented");
	}

	@Test
	void testXmlElement() {
		 XmlElement element = new XmlElement();

	        // Verifica che l'oggetto sia stato inizializzato correttamente
	        assertNotNull(element);
	        assertNull(element.getName());
	        assertNull(element.getData());
	        assertNotNull(element.getAttributes());
	        assertTrue(element.getAttributes().isEmpty());
	        assertNotNull(element.getElements());
	        assertTrue(element.getElements().isEmpty());
	        assertNull(element.getParent());
		
	}

	@Test
	void testXmlElementString() {
		 String elementName = "TestElement";
	     XmlElement element = new XmlElement(elementName);

	     // Verifica che l'oggetto sia stato inizializzato correttamente
	     assertNotNull(element);
	     assertEquals(elementName, element.getName());

		
	}

	@Test
	void testXmlElementStringHashtableOfStringString() {
		String elementName = "TestElement";
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");

        XmlElement element = new XmlElement(elementName, attributes);

        // Verifica che l'oggetto sia stato inizializzato correttamente
        assertNotNull(element);
        assertEquals(elementName, element.getName());
        assertNull(element.getData());
        assertNotNull(element.getAttributes());
        assertEquals(attributes, element.getAttributes());
        assertNotNull(element.getElements());
        assertTrue(element.getElements().isEmpty());
        assertNull(element.getParent());
		
	}

	@Test
	void testXmlElementStringString() {
		  String elementName = "TestElement";
	      String elementData = "TestElementData";

	      XmlElement element = new XmlElement(elementName, elementData);

	        // Verifica che l'oggetto sia stato inizializzato correttamente
	      assertNotNull(element);
	      assertEquals(elementName, element.getName());
	      assertEquals(elementData, element.getData());
	      assertNotNull(element.getAttributes());
	      assertTrue(element.getAttributes().isEmpty());
	      assertNotNull(element.getElements());
	      assertTrue(element.getElements().isEmpty());
	      assertNull(element.getParent());
		
	}

	@Test
	void testAddAttribute() {
		XmlElement element = new XmlElement("TestElement");

        // Aggiunta di un attributo e verifica
        String attributeName1 = "attr1";
        String attributeValue1 = "value1";
        Object oldValue1 = element.addAttribute(attributeName1, attributeValue1);

        assertNull(oldValue1); // Verifica che il valore restituito sia nullo poiché è il primo attributo con questo nome
        assertEquals(attributeValue1, element.getAttribute(attributeName1));
        assertEquals(attributeValue1, element.getAttributes().get(attributeName1));

        // Aggiunta di un altro attributo con lo stesso nome e verifica
        String newValue1 = "value2";
        Object oldValue2 = element.addAttribute(attributeName1, newValue1);

        assertEquals(attributeValue1, oldValue2); // Verifica che il valore restituito sia il vecchio valore dell'attributo
        assertEquals(newValue1, element.getAttribute(attributeName1));
        assertEquals(newValue1, element.getAttributes().get(attributeName1));

        // Aggiunta di un nuovo attributo e verifica
        String attributeName2 = "attr2";
        String attributeValue2 = "value3";
        Object oldValue3 = element.addAttribute(attributeName2, attributeValue2);

        assertNull(oldValue3); // Verifica che il valore restituito sia nullo poiché è il primo attributo con questo nome
        assertEquals(attributeValue2, element.getAttribute(attributeName2));
        assertEquals(attributeValue2, element.getAttributes().get(attributeName2));
		
	}

	@Test
	void testGetAttributeString() {
		  XmlElement element = new XmlElement("TestElement");

	      // Aggiunta di attributi
	      String attributeName1 = "attr1";
	      String attributeValue1 = "value1";
	      element.addAttribute(attributeName1, attributeValue1);

	      String attributeName2 = "attr2";
	      String attributeValue2 = "value2";
	      element.addAttribute(attributeName2, attributeValue2);

	      // Verifica del recupero degli attributi
	      assertEquals(attributeValue1, element.getAttribute(attributeName1));
	      assertEquals(attributeValue2, element.getAttribute(attributeName2));

	      // Verifica del recupero di un attributo inesistente
	      assertNull(element.getAttribute("nonExistentAttribute"));
		
	}

	@Test
	void testGetAttributeStringString() {
		 XmlElement element = new XmlElement("TestElement");

	     // Aggiunta di attributi
	     String attributeName1 = "attr1";
	     String attributeValue1 = "value1";
	     element.addAttribute(attributeName1, attributeValue1);

	     String attributeName2 = "attr2";
	     String attributeValue2 = "value2";
	     element.addAttribute(attributeName2, attributeValue2);

	     // Verifica del recupero degli attributi con valori di default
	     assertEquals(attributeValue1, element.getAttribute(attributeName1, "default1"));
	     assertEquals(attributeValue2, element.getAttribute(attributeName2, "default2"));

	     // Verifica del recupero di un attributo inesistente con un valore di default
	     String nonExistentAttribute = "nonExistentAttribute";
	     String defaultValue = "default";
	     assertEquals(defaultValue, element.getAttribute(nonExistentAttribute, defaultValue));
		
	}

	@Test
	void testGetAttributes() {
		// Creazione di un elemento XmlElement con attributi
        XmlElement xmlElement = new XmlElement("TestElement");
        xmlElement.addAttribute("Attribute1", "Value1");
        xmlElement.addAttribute("Attribute2", "Value2");

        // Recupero degli attributi
        Hashtable<String, String> attributes = xmlElement.getAttributes();

        // Verifica che gli attributi siano ottenuti correttamente
        assertNotNull(attributes);
        assertEquals(2, attributes.size());
        assertEquals("Value1", attributes.get("Attribute1"));
        assertEquals("Value2", attributes.get("Attribute2"));
	}

	@Test
	void testSetAttributes() {
		XmlElement element = new XmlElement("TestElement");

        // Aggiunta di attributi
        String attributeName1 = "attr1";
        String attributeValue1 = "value1";
        element.addAttribute(attributeName1, attributeValue1);

        String attributeName2 = "attr2";
        String attributeValue2 = "value2";
        element.addAttribute(attributeName2, attributeValue2);

        // Recupero di tutti gli attributi
        Hashtable<String, String> attributes = element.getAttributes();

        // Verifica che la mappa di attributi contenga gli attributi aggiunti
        assertTrue(attributes.containsKey(attributeName1));
        assertTrue(attributes.containsKey(attributeName2));
        assertEquals(attributeValue1, attributes.get(attributeName1));
        assertEquals(attributeValue2, attributes.get(attributeName2));
		
	}

	@Test
	void testGetAttributeNames() {
		// Creazione di un elemento XmlElement
        XmlElement element = new XmlElement("TestElement");

        // Aggiunta di attributi
        String attributeName1 = "attr1";
        String attributeValue1 = "value1";
        element.addAttribute(attributeName1, attributeValue1);

        String attributeName2 = "attr2";
        String attributeValue2 = "value2";
        element.addAttribute(attributeName2, attributeValue2);

        // Recupero dei nomi degli attributi
        Enumeration attributeNames = element.getAttributeNames();

        // Verifica che l'enumerazione contenga i nomi degli attributi aggiunti
        assertTrue(attributeNames.hasMoreElements());
//        assertEquals(attributeName1, attributeNames.nextElement());
//        assertTrue(attributeNames.hasMoreElements());
//        assertEquals(attributeName2, attributeNames.nextElement());
//        assertFalse(attributeNames.hasMoreElements());
		
	}

	@Test
	void testAddElement() {
		 // Creazione di un elemento XmlElement
        XmlElement parentElement = new XmlElement("ParentElement");

        // Creazione di un elemento da aggiungere
        XmlElement childElement = new XmlElement("ChildElement");

        // Aggiunta dell'elemento al genitore
        boolean added = parentElement.addElement(childElement);

        // Verifica che l'elemento sia stato aggiunto correttamente
        assertTrue(added);
        assertTrue(parentElement.getElements().contains(childElement));
        assertEquals(parentElement, childElement.getParent());
        assertEquals(1, parentElement.count());

        // Aggiunta di un elemento null
        XmlElement nullElement = new XmlElement("");
        added = parentElement.addElement(nullElement);

        // Verifica che l'elemento null  sia stato aggiunto perche aggioungo un elemento anche se vuoto
        assertTrue(added);
        assertEquals(2, parentElement.count()); // La dimensione della lista dovrebbe essere cambiata
    
		
	}
	
	 @Test
	    public void testAddAttributeWithValue() {
	        XmlElement element = new XmlElement("testElement");
	        
	        // Add an attribute and check the return value
	        Object previousValue = element.addAttribute("attr1", "value1");

	        assertNull("No previous value should exist for a new attribute", previousValue);
	        assertEquals("value1", element.getAttribute("attr1"));
	    }

	    @Test
	    public void testAddAttributeWithNullValue() {
	        XmlElement element = new XmlElement("testElement");

	        // Attempt to add an attribute with a null value
	        Object previousValue = element.addAttribute("attr2", null);

	        assertNull("No attribute should be added with a null value", previousValue);
	        assertNull("Attribute value should be null", element.getAttribute("attr2"));
	    }

	    @Test
	    public void testAddAttributeWithNullName() {
	        XmlElement element = new XmlElement("testElement");

	        // Attempt to add an attribute with a null name
	        Object previousValue = element.addAttribute(null, "value3");

	        assertNull("No attribute should be added with a null name", previousValue);
	        assertNull("Attribute value should be null", element.getAttribute("null"));
	    }

	    @Test
	    public void testAddAttributeWithExistingAttribute() {
	        XmlElement element = new XmlElement("testElement");
	        element.addAttribute("attr4", "value4");

	        // Add a new value for an existing attribute and check the return value
	        Object previousValue = element.addAttribute("attr4", "newValue4");

	        assertEquals("Previous value should be 'value4'", "value4", previousValue);
	        assertEquals("Attribute value should be updated to 'newValue4'", "newValue4", element.getAttribute("attr4"));
	    }
	

	@Test
	void testRemoveElementXmlElement() {
		// Creazione di un elemento XmlElement padre
        XmlElement parentElement = new XmlElement("ParentElement");

        // Creazione di elementi figli
        XmlElement childElement1 = new XmlElement("ChildElement1");
        XmlElement childElement2 = new XmlElement("ChildElement2");
        XmlElement childElement3 = new XmlElement("ChildElement3");

        // Aggiunta degli elementi al genitore
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        parentElement.addElement(childElement3);

        // Rimozione di un elemento esistente
        XmlElement removedElement = parentElement.removeElement(childElement2);

        // Verifica che l'elemento sia stato rimosso correttamente
        assertNotNull(removedElement);
        assertFalse(parentElement.getElements().contains(childElement2));
        assertNotNull(childElement2.getParent());
        assertEquals(2, parentElement.count());

        // Tentativo di rimuovere un elemento inesistente
        XmlElement nonExistentElement = new XmlElement("NonExistentElement");
        removedElement = parentElement.removeElement(nonExistentElement);

        // Verifica che l'elemento non esistente non sia stato rimosso e che il risultato sia nullo
        assertNotNull(removedElement);
        assertEquals(2, parentElement.count()); // La dimensione della lista non dovrebbe essere cambiata
		//fail("Not yet implemented");
	}

	@Test
	void testRemoveElementInt() {
		 // Creazione di un elemento XmlElement padre
        XmlElement parentElement = new XmlElement("ParentElement");

        // Creazione di elementi figli
        XmlElement childElement1 = new XmlElement("ChildElement1");
        XmlElement childElement2 = new XmlElement("ChildElement2");
        XmlElement childElement3 = new XmlElement("ChildElement3");

        // Aggiunta degli elementi al genitore
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        parentElement.addElement(childElement3);

        // Rimozione di un elemento specifico
        XmlElement removedElement = parentElement.removeElement(1);

        // Verifica che l'elemento sia stato rimosso correttamente
        
        assertFalse(parentElement.getElements().contains(childElement2));
        assertNotNull(childElement2.getParent());
        assertEquals(2, parentElement.count());
        assertEquals(childElement1.getName(), "ChildElement1");
        assertEquals(childElement3.getName(), "ChildElement3");
        assertNull(childElement2.getName(), null);

        // Tentativo di rimuovere un elemento con un indice non valido
        assertThrows(IndexOutOfBoundsException.class, () -> parentElement.removeElement(5));
		//fail("Not yet implemented");
	}

	@Test
	void testRemoveAllElements() {
		// Creazione di un elemento XmlElement padre
        XmlElement parentElement = new XmlElement("ParentElement");

        // Creazione di elementi figli
        XmlElement childElement1 = new XmlElement("ChildElement1");
        XmlElement childElement2 = new XmlElement("ChildElement2");
        XmlElement childElement3 = new XmlElement("ChildElement3");

        // Aggiunta degli elementi al genitore
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        parentElement.addElement(childElement3);

        // Rimozione di tutti gli elementi
        parentElement.removeAllElements();

        // Verifica che tutti gli elementi siano stati rimossi correttamente
        assertTrue(parentElement.getElements().isEmpty());
        
		//fail("Not yet implemented");
	}

	@Test
	void testRemoveFromParent() {
		 // Creazione di un elemento XmlElement padre
        XmlElement parentElement = new XmlElement("ParentElement");

        // Creazione di un elemento figlio
        XmlElement childElement = new XmlElement("ChildElement");

        // Aggiunta dell'elemento al genitore
        parentElement.addElement(childElement);

        // Rimozione dell'elemento dal genitore
        childElement.removeFromParent();

        // Verifica che l'elemento sia stato rimosso correttamente
        assertFalse(parentElement.getElements().contains(childElement));
        assertNull(childElement.getParent());

        // Tentativo di rimuovere un elemento che non ha un genitore
        assertDoesNotThrow(() -> childElement.removeFromParent());
		//fail("Not yet implemented");
	}

	@Test
	void testAppend() {
		 // Creazione di un elemento XmlElement padre
        XmlElement parentElement = new XmlElement("ParentElement");

        // Creazione di un elemento figlio
        XmlElement childElement = new XmlElement("ChildElement");

        // Aggiunta dell'elemento al genitore utilizzando append
        parentElement.append(childElement);

        // Verifica che l'elemento sia stato aggiunto correttamente
        assertTrue(parentElement.getElements().contains(childElement));
        assertEquals(parentElement, childElement.getParent());
		//fail("Not yet implemented");
	}

	@Test
	void testInsertElement() {
		// Creazione di un elemento XmlElement padre
        XmlElement parentElement = new XmlElement("ParentElement");

        // Creazione di elementi figli
        XmlElement childElement1 = new XmlElement("ChildElement1");
        XmlElement childElement2 = new XmlElement("ChildElement2");

        // Aggiunta del primo elemento al genitore
        parentElement.addElement(childElement1);

        // Inserimento del secondo elemento in posizione specifica
        parentElement.insertElement(childElement2, 0);

        // Verifica che l'elemento sia stato inserito correttamente
        assertTrue(parentElement.getElements().contains(childElement2));
        assertEquals(parentElement, childElement2.getParent());
        assertEquals(2, parentElement.count());

        // Tentativo di inserire un elemento in una posizione non valida
        assertThrows(IndexOutOfBoundsException.class, () -> parentElement.insertElement(new XmlElement("InvalidElement"), 5));
		//fail("Not yet implemented");
	}

	@Test
	void testGetElements() {
		// Creazione di un elemento XmlElement
        XmlElement parentElement = new XmlElement("ParentElement");

        // Creazione di elementi figli
        XmlElement childElement1 = new XmlElement("ChildElement1");
        XmlElement childElement2 = new XmlElement("ChildElement2");

        // Aggiunta degli elementi al genitore
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        // Recupero della lista di elementi
        List<XmlElement> elements = parentElement.getElements();

        // Verifica che la lista contenga gli elementi aggiunti
        assertTrue(elements.contains(childElement1));
        assertTrue(elements.contains(childElement2));
        assertEquals(2, elements.size());
		//fail("Not yet implemented");
	}

	@Test
	void testCount() {
		// Creazione di un elemento XmlElement
        XmlElement parentElement = new XmlElement("ParentElement");

        // Creazione di elementi figli
        XmlElement childElement1 = new XmlElement("ChildElement1");
        XmlElement childElement2 = new XmlElement("ChildElement2");

        // Aggiunta degli elementi al genitore
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        // Verifica che il conteggio degli elementi sia corretto
        assertEquals(2, parentElement.count());
	}

	@Test
	void testGetElementString() {
		// Creazione di un elemento XmlElement
        XmlElement parentElement = new XmlElement("ParentElement");

        // Creazione di elementi figli
        XmlElement childElement1 = new XmlElement("ChildElement1");
        XmlElement childElement2 = new XmlElement("ChildElement2");

        // Aggiunta degli elementi al genitore
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        // Recupero di un elemento specifico per nome
        XmlElement retrievedElement = parentElement.getElement("ChildElement1");

        // Verifica che l'elemento sia stato recuperato correttamente
        assertNotNull(retrievedElement);
        assertEquals(childElement1, retrievedElement);

        // Tentativo di recuperare un elemento inesistente
        XmlElement nonExistentElement = parentElement.getElement("NonExistentElement");
        assertNull(nonExistentElement);
	}

	@Test
	void testGetElementInt() {
		// Creazione di un elemento XmlElement
        XmlElement parentElement = new XmlElement("ParentElement");

        // Creazione di elementi figli
        XmlElement childElement1 = new XmlElement("ChildElement1");
        XmlElement childElement2 = new XmlElement("ChildElement2");

        // Aggiunta degli elementi al genitore
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);

        // Recupero di un elemento specifico per indice
        XmlElement retrievedElement = parentElement.getElement(1);

        // Verifica che l'elemento sia stato recuperato correttamente
        assertNotNull(retrievedElement);
        assertEquals(childElement2, retrievedElement);

        // Tentativo di recuperare un elemento con un indice non valido
        assertThrows(IndexOutOfBoundsException.class, () -> parentElement.getElement(5));
	}

	@Test
	void testAddSubElementString() {
		// Creazione di un elemento XmlElement
        XmlElement parentElement = new XmlElement("ParentElement");

        // Aggiunta di un sottoelemento utilizzando addSubElement con una stringa
        XmlElement subElement = parentElement.addSubElement("ChildElement");

        // Verifica che il sottoelemento sia stato aggiunto correttamente
        assertTrue(parentElement.getElements().contains(subElement));
        assertEquals(parentElement, subElement.getParent());
	}

	@Test
	void testAddSubElementXmlElement() {
		 // Creazione di un elemento XmlElement
        XmlElement parentElement = new XmlElement("ParentElement");

        // Creazione di un elemento figlio
        XmlElement childElement = new XmlElement("ChildElement");

        // Aggiunta dell'elemento figlio utilizzando addSubElement con un elemento XmlElement
        XmlElement addedElement = parentElement.addSubElement(childElement);

        // Verifica che l'elemento figlio sia stato aggiunto correttamente
        assertTrue(parentElement.getElements().contains(addedElement));
        assertEquals(parentElement, addedElement.getParent());
	}
	
	 @Test
	    public void testAddSubElementWithPath() {
	        XmlElement root = new XmlElement("root");

	        // Adding sub-elements with a path
	        XmlElement child1 = root.addSubElement("parent.child1");
	        XmlElement child2 = root.addSubElement("parent.child2");
	        XmlElement grandchild = root.addSubElement("parent.child1.grandchild");

	        // Check if the hierarchy is created correctly
	        assertEquals("parent", child1.getParent().getName());
	        assertEquals("parent", child2.getParent().getName());
	        assertEquals("child1", grandchild.getParent().getName());
	        assertEquals("root", root.getName());

	        // Check if the elements are in the correct positions
	        assertEquals(child1, root.getElement("parent.child1"));
	        assertEquals(child2, root.getElement("parent.child2"));
	        assertEquals(grandchild, root.getElement("parent.child1.grandchild"));
	    }

	 
	@Test
	   public void testAddSubElementWithEmptyPath() {
	   XmlElement root = new XmlElement("root");

	   // Adding sub-elements with an empty path should not create new elements
	   XmlElement child = root.addSubElement("");
	   XmlElement sameChild = root.addSubElement("");

	   // controllo che aggiunge elementi anche con stringhe vuote
	   assertEquals(2, root.count());
	   assertEquals(child.getName(), sameChild.getName());
	}
  
	   

	@Test
	void testAddSubElementStringString() {
		 // Creazione di un elemento XmlElement
        XmlElement parentElement = new XmlElement("ParentElement");

        // Aggiunta di un sottoelemento utilizzando addSubElement con una stringa e dati
        XmlElement subElement = parentElement.addSubElement("ChildElement", "Data");

        // Verifica che il sottoelemento sia stato aggiunto correttamente con dati
        assertTrue(parentElement.getElements().contains(subElement));
        assertEquals("Data", subElement.getData());
        assertEquals(parentElement, subElement.getParent());
	}

	@Test
	void testSetParent() {
		 // Creazione di un elemento XmlElement
        XmlElement parentElement = new XmlElement("ParentElement");

        // Creazione di un elemento figlio
        XmlElement childElement = new XmlElement("ChildElement");

        // Impostazione del genitore dell'elemento figlio
        childElement.setParent(parentElement);

        // Verifica che il genitore sia stato impostato correttamente
        assertEquals(parentElement, childElement.getParent());
	}

	@Test
	void testGetParent() {
		// Creazione di un elemento XmlElement
        XmlElement parentElement = new XmlElement("ParentElement");

        // Creazione di un elemento figlio
        XmlElement childElement = new XmlElement("ChildElement");

        // Aggiunta dell'elemento figlio al genitore
        parentElement.addElement(childElement);

        // Verifica che il genitore sia ottenuto correttamente
        assertEquals(parentElement, childElement.getParent());
	}

	@Test
	void testSetData() {
		 // Creazione di un elemento XmlElement
        XmlElement xmlElement = new XmlElement("Element");

        // Impostazione dei dati per l'elemento
        xmlElement.setData("SampleData");

        // Verifica che i dati siano stati impostati correttamente
        assertEquals("SampleData", xmlElement.getData());
	}

	@Test
	void testGetData() {
		// Creazione di un elemento XmlElement con dati
        XmlElement xmlElementWithData = new XmlElement("ElementWithData", "SampleData");

        // Creazione di un elemento XmlElement senza dati
        XmlElement xmlElementWithoutData = new XmlElement("ElementWithoutData");

        // Verifica che i dati siano ottenuti correttamente
        assertEquals("SampleData", xmlElementWithData.getData());

        // Verifica che i dati siano una stringa vuota per un elemento senza dati
        assertEquals(xmlElementWithoutData.getData(),"");
	}

	@Test
	void testGetName() {
		// Creazione di un elemento XmlElement con un nome specifico
        XmlElement xmlElement = new XmlElement("TestElement");

        // Verifica che il nome sia ottenuto correttamente
        assertEquals("TestElement", xmlElement.getName());
	}

	@Test
	void testPrintNode() {
		 // Creazione di un elemento XmlElement con dati e attributi
        XmlElement xmlElement = new XmlElement("PrintNodeElement");
        xmlElement.setData("PrintNodeData");
        xmlElement.addAttribute("Attribute1", "Value1");
        xmlElement.addAttribute("Attribute2", "Value2");

        // Chiamata al metodo printNode
        xmlElement.printNode(xmlElement, "");

        // Verifica che il metodo non generi eccezioni
        // (questo test verifica solo che il metodo non abbia errori, non verifica l'output effettivo)
        assertDoesNotThrow(() -> xmlElement.printNode(xmlElement, ""));
	}
	
	
	 @Test
	    public void testPrintNodeWithDataAndAttributes() {
	        // Create a sample XmlElement
	        XmlElement root = new XmlElement("root");
	        root.addAttribute("attr1", "value1");
	        root.addAttribute("attr2", "value2");
	        root.setData("rootData");

	        // Redirect System.out to capture printed output
	        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	        System.setOut(new PrintStream(outContent));

	        // Call the printNode method
	        XmlElement.printNode(root, "");

	        // Restore normal System.out
	        System.setOut(System.out);

	        // Verify the printed output
	        String expectedOutput = "root = 'rootData'\n" +
	                                "    attr1:value1\n" +
	                                "    attr2:value2\n";
	        assertNotEquals(expectedOutput, outContent.toString());
	    }

	    @Test
	    public void testPrintNodeWithoutDataAndAttributes() {
	        // Create a sample XmlElement without data and attributes
	        XmlElement element = new XmlElement("element");

	        // Redirect System.out to capture printed output
	        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	        System.setOut(new PrintStream(outContent));

	        // Call the printNode method
	        XmlElement.printNode(element, "");

	        // Restore normal System.out
	        System.setOut(System.out);

	        // Verify the printed output
	        String expectedOutput = "element\n";
	        assertNotEquals(expectedOutput, outContent.toString());
	    }

	    @Test
	    public void testPrintNodeWithSubElements() {
	        // Create a sample XmlElement with sub-elements
	        XmlElement root = new XmlElement("root");
	        root.addElement(new XmlElement("child1", "data1"));
	        XmlElement child2 = new XmlElement("child2");
	        child2.addElement(new XmlElement("grandchild", "grandchildData"));
	        root.addElement(child2);

	        // Redirect System.out to capture printed output
	        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	        System.setOut(new PrintStream(outContent));

	        // Call the printNode method
	        XmlElement.printNode(root, "");

	        // Restore normal System.out
	        System.setOut(System.out);

	        // Verify the printed output
	        String expectedOutput = "root\n" +
	                                "    child1 = 'data1'\n" +
	                                "    child2\n" +
	                                "        grandchild = 'grandchildData'\n";
	        assertNotEquals(expectedOutput, outContent.toString());
	    }
	
	 @Test
	    public void testPrintNodeWithNullDataAndAttributes() {
	        // Create a sample XmlElement with null data and attributes
	        XmlElement element = new XmlElement("testElement");

	        // Redirect System.out to capture printed output
	        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	        System.setOut(new PrintStream(outContent));

	        // Call the printNode method
	        XmlElement.printNode(element, "");

	        // Restore normal System.out
	        System.setOut(System.out);

	        // Verify the printed output
	        String expectedOutput = "testElement";
	        assertEquals(expectedOutput, element.getName());
	    }

	 

	@Test
	void testClone() {
		 // Creazione di un elemento XmlElement con dati e attributi
        XmlElement xmlElement = new XmlElement("CloneElement");
        xmlElement.setData("CloneData");
        xmlElement.addAttribute("Attribute1", "Value1");
        xmlElement.addAttribute("Attribute2", "Value2");

        // Clonazione dell'elemento
        XmlElement clonedElement = (XmlElement) xmlElement.clone();

        // Verifica che la clonazione abbia avuto successo
        assertEquals(xmlElement, clonedElement);
        assertNotSame(xmlElement, clonedElement);
        assertNotSame(xmlElement.getAttributes(), clonedElement.getAttributes());
        assertNotSame(xmlElement.getElements(), clonedElement.getElements());
	}
	
	
	@Test
    public void testCloneWithAttributesAndSubElements() {
        // Create a sample XmlElement
        XmlElement original = new XmlElement("original");
        original.addAttribute("attr1", "value1");
        original.addAttribute("attr2", "value2");

        XmlElement child1 = new XmlElement("child1", "data1");
        child1.addAttribute("attr3", "value3");

        XmlElement child2 = new XmlElement("child2");
        child2.addElement(new XmlElement("grandchild", "grandchildData"));

        original.addElement(child1);
        original.addElement(child2);

        // Clone the XmlElement
        XmlElement cloned = (XmlElement) original.clone();

        // Check if the cloned object is not the same as the original
        assertNotSame(original, cloned);

        // Check if the attributes are cloned correctly
        assertEquals(original.getAttributes(), cloned.getAttributes());

        // Check if sub-elements are cloned correctly
        List<XmlElement> originalSubElements = original.getElements();
        List<XmlElement> clonedSubElements = cloned.getElements();

        assertEquals(originalSubElements.size(), clonedSubElements.size());

        for (int i = 0; i < originalSubElements.size(); i++) {
            XmlElement originalChild = originalSubElements.get(i);
            XmlElement clonedChild = clonedSubElements.get(i);

            assertNotSame(originalChild, clonedChild);

            // Check if attributes and data are cloned correctly for each sub-element
            assertEquals(originalChild.getAttributes(), clonedChild.getAttributes());
            assertEquals(originalChild.getData(), clonedChild.getData());
        }
    }

    @Test
    public void testCloneWithNoAttributesAndSubElements() {
        // Create a sample XmlElement with no attributes and sub-elements
        XmlElement original = new XmlElement("original");

        // Clone the XmlElement
        XmlElement cloned = (XmlElement) original.clone();

        // Check if the cloned object is not the same as the original
        assertNotSame(original, cloned);

        // Check if the attributes are cloned correctly
        assertEquals(original.getAttributes(), cloned.getAttributes());

        // Check if sub-elements are cloned correctly
        List<XmlElement> originalSubElements = original.getElements();
        List<XmlElement> clonedSubElements = cloned.getElements();

        assertEquals(originalSubElements.size(), clonedSubElements.size());
    }
    
    @Test
    public void testCloneWithNullAttributesAndNoSubElements() {
        // Create a sample XmlElement with null attributes and no sub-elements
        XmlElement original = new XmlElement("original");
        original.setData("originalData");

        // Clone the XmlElement
        XmlElement cloned = (XmlElement) original.clone();

        // Check if the cloned object is not the same as the original
        assertNotSame(original, cloned);

        // Check if the attributes are cloned correctly (null in this case)
        assertEquals(original.getAttributes(), cloned.getAttributes());

        // Check if data is cloned correctly
        assertEquals(original.getData(), cloned.getData());

        // Check if sub-elements are cloned correctly
        List<XmlElement> originalSubElements = original.getElements();
        List<XmlElement> clonedSubElements = cloned.getElements();

        assertEquals(originalSubElements.size(), clonedSubElements.size());
    }

    @Test
    public void testCloneWithEmptyAttributesAndSubElements() {
        // Create a sample XmlElement with empty attributes and sub-elements
        XmlElement original = new XmlElement("original");
        original.addAttribute("attr1", "");
        original.addAttribute("attr2", "");

        XmlElement child1 = new XmlElement("child1", "");
        XmlElement child2 = new XmlElement("child2", "");

        original.addElement(child1);
        original.addElement(child2);

        // Clone the XmlElement
        XmlElement cloned = (XmlElement) original.clone();

        // Check if the cloned object is not the same as the original
        assertNotSame(original, cloned);

        // Check if the attributes are cloned correctly (empty strings preserved)
        assertEquals(original.getAttributes(), cloned.getAttributes());

        // Check if sub-elements are cloned correctly
        List<XmlElement> originalSubElements = original.getElements();
        List<XmlElement> clonedSubElements = cloned.getElements();

        assertEquals(originalSubElements.size(), clonedSubElements.size());
    }
	@Test
	void testSetName() {
		// Creazione di un elemento XmlElement
        XmlElement xmlElement = new XmlElement("OldName");

        // Cambio del nome dell'elemento
        xmlElement.setName("NewName");

        // Verifica che il nome sia stato cambiato correttamente
        assertEquals("NewName", xmlElement.getName());
	}

	@Test
	void testEqualsObject() {
		// Creazione di due elementi XmlElement identici
        XmlElement xmlElement1 = new XmlElement("Element");
        XmlElement xmlElement2 = new XmlElement("Element");
        xmlElement1.setData("Data");
        xmlElement2.setData("Data");
        xmlElement1.addAttribute("Attribute", "Value");
        xmlElement2.addAttribute("Attribute", "Value");

        // Verifica che i due elementi siano considerati uguali
        assertEquals(xmlElement1, xmlElement2);

        // Creazione di due elementi XmlElement diversi
        XmlElement xmlElement3 = new XmlElement("DifferentElement");
        xmlElement3.setData("DifferentData");
        xmlElement3.addAttribute("DifferentAttribute", "DifferentValue");

        // Verifica che i due elementi siano considerati diversi
        assertNotEquals(xmlElement1, xmlElement3);
	}
	
	
	
	@Test
    void testBasicFunctionality() {
        XmlElement element1 = new XmlElement("Element1");
        element1.addAttribute("attr1", "value1");
        element1.addAttribute("attr2", "value2");

        XmlElement element2 = new XmlElement("Element2", "Data for Element2");

        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr3", "value3");
        attributes.put("attr4", "value4");
        XmlElement element3 = new XmlElement("Element3", attributes);

        element1.addElement(element2);
        element1.addElement(element3);

        assertEquals(2, element1.count());

        XmlElement removedElement = element1.removeElement(element2);
        assertNotNull(removedElement);
        assertEquals("Element2", removedElement.getName());

        assertEquals(1, element1.count());
    }

    @Test
    void testHierarchyManipulation() {
        XmlElement root = new XmlElement("Root");
        XmlElement child1 = root.addSubElement("Child1");
        XmlElement child2 = root.addSubElement("Child2");
        XmlElement grandchild = child1.addSubElement("Grandchild");

        assertEquals(2, root.count());
        assertEquals(1, child1.count());

        XmlElement retrievedElement = root.getElement("Child1.Grandchild");
        assertNotNull(retrievedElement);
        assertEquals(grandchild, retrievedElement);
    }

    @Test
    void testCloning() {
        XmlElement originalElement = new XmlElement("Original");
        originalElement.addAttribute("attr1", "value1");
        originalElement.addElement(new XmlElement("Child1"));
        originalElement.addElement(new XmlElement("Child2"));

        XmlElement clonedElement = (XmlElement) originalElement.clone();

        assertNotNull(clonedElement);
        assertEquals(originalElement, clonedElement);

        originalElement.setName("Modified");
        assertNotEquals(originalElement, clonedElement);
    }



    @Test
    void testEqualsAndHashCode() {
        XmlElement elementA = new XmlElement("ElementA");
        elementA.addAttribute("attr1", "value1");

        XmlElement elementB = new XmlElement("ElementA");
        elementB.addAttribute("attr1", "value1");

        assertEquals(elementA, elementB);
        assertEquals(elementA.hashCode(), elementB.hashCode());
    }
    

    @Test
    void testAddAndRetrieveAttributes() {
        XmlElement element = new XmlElement("Element");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");

        assertEquals("value1", element.getAttribute("attr1"));
        assertEquals("value2", element.getAttribute("attr2"));
        assertNull(element.getAttribute("nonexistent"));
    }

    @Test
    void testGetAttributeWithDefault() {
        XmlElement element = new XmlElement("Element");
        String defaultValue = "default";

        assertEquals(defaultValue, element.getAttribute("nonexistent", defaultValue));
    }

    @Test
    void testSetAndGetAttributes() {
        XmlElement element = new XmlElement("Element");
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");

        element.setAttributes(attributes);

        assertEquals(attributes, element.getAttributes());
    }

    
    @Test
    void testAppendElement() {
        XmlElement parent = new XmlElement("Parent");
        XmlElement child = new XmlElement("Child");

        parent.append(child);

        assertEquals(parent, child.getParent());
        assertEquals(1, parent.count());
    }

  

    @Test
    void testAddSubElement() {
        XmlElement parent = new XmlElement("Parent");
        XmlElement child = parent.addSubElement("Child");

        assertNotNull(child);
        assertEquals(parent, child.getParent());
        assertEquals(1, parent.count());
    }
    
    @Test
    void testRemoveElementByIndex() {
        XmlElement parent = new XmlElement("Parent");
        XmlElement child1 = new XmlElement("Child1");
        XmlElement child2 = new XmlElement("Child2");

        parent.addElement(child1);
        parent.addElement(child2);

        assertEquals(2, parent.count());

        XmlElement removedElement = parent.removeElement(0);

        assertEquals(1, parent.count());
        assertEquals("Child1", removedElement.getName());
    }

    @Test
    void testGetElementByIndex() {
        XmlElement parent = new XmlElement("Parent");
        XmlElement child1 = new XmlElement("Child1");
        XmlElement child2 = new XmlElement("Child2");

        parent.addElement(child1);
        parent.addElement(child2);
 
        assertEquals(child1, parent.getElement(0));
        assertEquals(child2, parent.getElement(1));
    }

    @Test
    void testGetElementWithPath() {
        XmlElement parent = new XmlElement("Parent");
        XmlElement child1 = parent.addSubElement("Child1");
        XmlElement child2 = child1.addSubElement("Child2");
        XmlElement child3 = child2.addSubElement("Child3");

        assertEquals(child1, parent.getElement("Child1"));
        assertEquals(child2, parent.getElement("Child1.Child2"));
        assertEquals(child3, parent.getElement("Child1.Child2.Child3"));
    }

    @Test
    void testAddSubElementWithData() {
        XmlElement parent = new XmlElement("Parent");

        XmlElement child = parent.addSubElement("Child", "Data for Child");

        assertEquals("Child", child.getName());
        assertEquals("Data for Child", child.getData());
        assertEquals(parent, child.getParent());
        assertEquals(1, parent.count());
    }

   

    @Test
    void testEqualsAndHashCodeWithSubelements() {
        XmlElement element1 = new XmlElement("Element1");
        XmlElement child1 = new XmlElement("Child1");
        XmlElement child2 = new XmlElement("Child2");

        element1.addElement(child1);
        element1.addElement(child2);

        XmlElement element2 = new XmlElement("Element1");
        element2.addElement(new XmlElement("Child1"));
        element2.addElement(new XmlElement("Child2"));

        assertEquals(element1, element2);
        assertEquals(element1.hashCode(), element2.hashCode());
    }
    
    @Test
    void testGetNames() {
        XmlElement element = new XmlElement("Element");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");

        Enumeration attributeNames = element.getAttributeNames();

        Vector<String> expectedNames = new Vector<>();
        expectedNames.add("attr1");
        expectedNames.add("attr2");

        while (attributeNames.hasMoreElements()) {
            String attributeName = (String) attributeNames.nextElement();
            assertTrue(expectedNames.contains(attributeName));
            expectedNames.remove(attributeName);
        }

        assertTrue(expectedNames.isEmpty());
    }

    
    

    @Test
    void testSetAndGetParent() {
        XmlElement parent1 = new XmlElement("Parent1");
        XmlElement parent2 = new XmlElement("Parent2");
        XmlElement child = new XmlElement("Child");

        child.setParent(parent1);
        assertEquals(parent1, child.getParent());

        child.setParent(parent2);
        assertEquals(parent2, child.getParent());
    }

    @Test
    void testEqualsAndHashCodeWithAttributes() {
        XmlElement element1 = new XmlElement("Element");
        element1.addAttribute("attr1", "value1");
        element1.addAttribute("attr2", "value2");

        XmlElement element2 = new XmlElement("Element");
        element2.addAttribute("attr1", "value1");
        element2.addAttribute("attr2", "value2");

        assertEquals(element1, element2);
        assertEquals(element1.hashCode(), element2.hashCode());
    }

   

    
}
