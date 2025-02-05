package student.RaffaeleDAnnaUno;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Hashtable;
import java.util.Vector;

import ClassUnderTest.XMLParser;


/**
 * Classe di testing per la verifica del corretto funzionamento della classe <b>XMLParser</b>.
 * <br>
 * <br>
 * Sono state identificate classi di equivalenza per i metodi che accettano un parametro, in particolare:
 * <br>
 * Per i parametri di tipo <i>String</i>, le classi di equivalenza includono:
 * <ul>
 * <li>una stringa corretta</li>
 * <li>una stringa errata</li>
 * <li>una stringa vuota</li>
 * <li>una stringa nulla</li>
 * </ul>
 * Per i parametri di tipo <i>Hashtable</i>, le classi di equivalenza includono:
 * <ul>
 * <li>un oggetto Hashtable corretto</li>
 * <li>un oggetto Hashtable errato </li>
 * <li>un oggetto Hashtable vuoto</li>
 * <li>un oggetto Hashtable nullo.</li>
 * </ul>
 * Per garantire il successo dei test, è stato considerato il corretto funzionamento del metodo da testare 
 * in base al suo nome e alla sua firma, oltre che uno studio approfondito del codice di ogni metodo della classe XMLParser.
 * <br>
 * <br>
 * Con i metodi sottoelencati, si è riuscito ad ottenere una coverage della classe da testare di circa il 98%.
 * Il restante 2% residuo riguarda istruzioni le quali sono indipendenti dall'interazione diretta e sono determinate 
 * dalla logica interna del codice.
 * <br>
 * <br>
 * <b>Tempo impiegato</b>
 * <br>
 * L'intero processo di studio della classe XMLParser e dei suoi metodi con relativo sviluppo dei test e stesura
 * della documentazione è stato completato in un periodo complessivo di circa <i>9 ore</i>.
 *  
 * @see ClassUnderTest.XMLParser
 * @author Raffaele D'Anna N97000455
 */
 class StudentTest {
	 
	/**
	 * Verifica se il costruttore della classe `XMLParser` è in grado di gestire correttamente un input XML valido.
	 * Viene generata un'eccezione se il costruttore non riesce a creare un'istanza di XMLParser con un input XML valido,
	 * altrimenti ci si aspetta che l'oggetto con l'XML valido non sia nullo.
	 * @throws Exception 
	 */
	@Test
	public void testXMLParser() throws Exception {
	   assertNotNull(new XMLParser("<data><element1>value1</element1></data>"));
	}

	/**
	 * Verifica se il costruttore della classe `XMLParser` è in grado di gestire correttamente un input XML vuoto.
	 * Viene generata un'eccezione se il costruttore non riesce a creare un'istanza di XMLParser con un input XML vuoto,
	 * altrimenti ci si aspetta che l'oggetto con l'XML vuoto non sia nullo, ma appunto vuoto.
	 * @throws Exception 
	 */
	@Test
	public void testXMLParserWithEmptyXML() throws Exception {
		assertNotNull(new XMLParser(""));
	}

	/**
	 * Verifica se il costruttore della classe `XMLParser` è in grado di gestire correttamente un input nullo.
	 * Ci si aspetta il lancio di un'eccezione con un oggetto XML nullo.
	 * @throws Exception 
	 */
	@Test
	public void testXMLParserWithNullXML() {
	    assertThrows(Exception.class, () -> {
	        new XMLParser(null);
	    });
	}

	/**
	 * Verifica se il costruttore della classe `XMLParser` è in grado di gestire correttamente un input XML non valido.
	 * Ci si aspetta il lancio di un'eccezione con un oggetto XML non valido.
	 * @throws Exception 
	 */
	@Test
	public void testXMLParserWithInvalidXML() {
	    assertThrows(Exception.class, () -> {
	        new XMLParser("<data><element1>value1</element1>");
	    });
	}

    /********************************************************************************************/

	/**
	 * Verifica se il metodo `getXML()` della classe XMLParser restituisce l'input XML originale correttamente.
	 * Viene generata un'eccezione se il metodo non restituisce l'input XML come previsto, altrimenti 
	 * ci si aspetta che l'oggetto con l'XML valido sia uguale a quello ritornato dal metodo `getXML()`.
	 * @throws Exception 
	 */
	@Test
	public void testGetXML() throws Exception {
		String xml = "<data><element1>value1</element1></data>";
	    XMLParser xmlParser = new XMLParser(xml);
	    assertEquals(xml, xmlParser.getXML());
	}

	/**
	 * Verifica se il metodo `getXML()` della classe XMLParser è in grado di gestire correttamente un input XML vuoto.
	 * Viene generata un'eccezione se il metodo non restituisce un input XML vuoto come previsto, altrimenti 
	 * ci si aspetta che l'oggetto con l'XML vuoto sia uguale a quello ritornato dal metodo `getXML()`.
	 * @throws Exception 
	 */
	@Test
	public void testGetXMLWithEmptyXML() throws Exception {
		String xml = "";
        XMLParser xmlParser = new XMLParser(xml);
        assertEquals(xml, xmlParser.getXML());
	}

	/**
	 * Verifica se il metodo `getXML()` della classe XMLParser è in grado di gestire correttamente un input XML nullo.
	 * Ci si aspetta il lancio di un'eccezione con un oggetto XML nullo.
	 * @throws Exception 
	 */
	@Test
	public void testGetXMLWithNullXML() {
	    assertThrows(Exception.class, () -> {
	        XMLParser xmlParser = new XMLParser(null);
	        xmlParser.getXML();
	    });
	}

	/**
	 * Verifica se il metodo `getXML()` della classe XMLParser è in grado di gestire correttamente un input XML non valido.
	 * Ci si aspetta il lancio di un'eccezione con un oggetto XML non valido.
	 * @throws Exception 
	 */
	@Test
	public void testGetXMLWithInvalidXML() {
	    assertThrows(Exception.class, () -> {
	        XMLParser xmlParser = new XMLParser("<data><element1>value1</element1>");
	        xmlParser.getXML();
	    });
	}

    /********************************************************************************************/

	/**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia 
	 * modificare il valore di un elemento appartenente ad un tag specifico con un id dato, all'interno di un XML vuoto.
	 * Ci si aspetta che il metodo mantenga l'XML vuoto.
	 * @throws Exception 
	 */
	@Test
	public void testSetPropertyWithEmptyXML() throws Exception {
		XMLParser xmlParser = new XMLParser("");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value1");
        elements.put("element2", "value2");
        String tagName = "element";
        String id = "1";
        xmlParser.setProperty(tagName, id, elements);
        String expectedXml = "";
        assertEquals(expectedXml, xmlParser.getXML());
	}

	/**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia 
	 * modificare il valore di un elemento appartenente ad un tag specifico con un id dato, all'interno di un XML nullo.
	 * Ci si aspetta che il metodo generi un'eccezione.
	 * @throws Exception 
	 */
	@Test
	public void testSetPropertyWithNullXML() {
	    assertThrows(Exception.class, () -> {
	        XMLParser xmlParser = new XMLParser(null);
	        Hashtable<String, String> elements = new Hashtable<>();
	        elements.put("element1", "value1");
	        elements.put("element2", "value2");
	        String tagName = "element";
	        String id = "1";
	        xmlParser.setProperty(tagName, id, elements);
	    });
	}

	/**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia 
	 * modificare il valore di un elemento appartenente ad un tag specifico con un id dato, all'interno di un XML non valido.
	 * Ci si aspetta che il metodo generi un'eccezione.
	 * @throws Exception 
	 */
	@Test
	public void testSetPropertyWithInvalidXML() {
		assertThrows(Exception.class, () -> {
			XMLParser xmlParser = new XMLParser("<data>");
	        Hashtable<String, String> elements = new Hashtable<>();
	        elements.put("element1", "value1");
	        elements.put("element2", "value2");
	        String tagName = "element";
	        String id = "1";
	        xmlParser.setProperty(tagName, id, elements);
        });
	}

    
	/**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia 
	 * modificare il valore di un elemento appartenente ad un tag specifico con un id già esistente nell'XML
	 * ma fornendo un nuovo elemento tramite un oggetto Hashtable con chiave corretta ma valore di tipo errato.
	 * Ci si aspetta che il metodo generi un'eccezione.
	 * @throws Exception 
	 */
	@Test
	 public void testSetPropertyWithTagNameExistsIdExistsWrongElements() {
	    assertThrows(Exception.class, () -> {
	        XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
	        Hashtable<String, Integer> elements = new Hashtable<>();
	        elements.put("element1", 1);
	        String tagName = "element";
	        String id = "1";
	        xmlParser.setProperty(tagName, id, elements);
	    });
	}

	/**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia 
	 * modificare il valore di un elemento appartenente ad un tag specifico con un id già esistente nell'XML,
	 * ma fornendo un elemento errato tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione.
	 * @throws Exception 
	 */
	@Test
	 public void testSetPropertyWithTagNameExistsIdExistsWrongElements2() {
	    assertThrows(Exception.class, () -> {
	        XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");

	        Hashtable<Integer, Integer> elements = new Hashtable<>();
	        elements.put(1, 1);

	        String tagName = "element";
	        String id = "1";
	        xmlParser.setProperty(tagName, id, elements);
	    });
	}

	/**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag specifico con un id già esistente nell'XML,
	 * ma fornendo un elemento corretto tramite un oggetto Hashtable.
	 * Ci si aspetta che l'oggetto istanziato venga modificato con il nuovo valore dell'elemento.
	 * @throws Exception 
	 */
	@Test
	 public void testSetPropertyWithTagNameExistsIdExistsElementsExists() throws Exception {
		XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "element";
        String id = "1";
        xmlParser.setProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value2</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
	}

	/**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag specifico con un id già esistente nell'XML,
	 * ma fornendo un elemento non esistente nell'XML tramite un oggetto Hashtable.
	 * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente l'elemento nell'XML.
	 * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameExistsIdExistsElementsNotExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value2");
        String tagName = "element";
        String id = "1";
        xmlParser.setProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag specifico con un id già esistente nell'XML,
	 * ma fornendo un elemento vuoto tramite un oggetto Hashtable.
	 * Ci si aspetta che l'oggetto istanziato non venga modificato siccome si vuole modificare il valore di un elemento vuoto.
     * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameExistsIdExistsElementsEmpty() throws Exception {
    	 XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
         String tagName = "element";
         String id = "1";
         xmlParser.setProperty(tagName, id, new Hashtable<>());
         String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
         assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag specifico con un id già esistente nell'XML,
	 * ma fornendo un elemento nullo tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameExistsIdExistsElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String tagName = "element";
            String id = "1";
            xmlParser.setProperty(tagName, id, null);
        });
    }

    /**
     * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * modificare il valore di un elemento appartenente ad un tag specifico con un id non esistente nell'XML,
     * ma fornendo un elemento corretto tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente l'elemento con quell'id nell'XML.
     * @throws Exception
     */
    @Test
    public void testSetPropertyWithTagNameExistsIdNotExistsElementsExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "element";
        String id = "2";
        xmlParser.setProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * modificare il valore di un elemento appartenente ad un tag specifico con un id non esistente nell'XML,
	 * ma fornendo un elemento non esistente nell'XML tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente l'elemento con quell'id nell'XML.
     * @throws Exception 
     */
    @Test
    public void testSetPropertyWithTagNameExistsIdNotExistsElementsNotExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value2");
        String tagName = "element";
        String id = "2";
        xmlParser.setProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * modificare il valore di un elemento appartenente ad un tag specifico con un id non esistente nell'XML,
	 * ma fornendo un elemento vuoto tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente l'elemento con quell'id nell'XML.
     * @throws Exception 
	 */
    @Test
    public void testSetPropertyWithTagNameExistsIdNotExistsElementsEmpty() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        String tagName = "element";
        String id = "2";
        xmlParser.setProperty(tagName, id, new Hashtable<>());
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * modificare il valore di un elemento appartenente ad un tag specifico con un id non esistente nell'XML,
	 * ma fornendo un elemento nullo tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione.
	 * @throws Exception
	 */
    @Test
    public void testSetPropertyWithTagNameExistsIdNotExistsElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String tagName = "element";
            String id = "2";
            xmlParser.setProperty(tagName, id, null);
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * modificare il valore di un elemento appartenente ad un tag specifico con un id nell'XML, ma passando un id vuoto e
	 * un elemento corretto tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente quel tag con id vuoto nell'XML.
     * @throws Exception 
	 */
    @Test
    public void testSetPropertyWithTagNameExistsIdEmptyElementsExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "element";
        xmlParser.setProperty(tagName, "", elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * modificare il valore di un elemento appartenente ad un tag specifico con un id nell'XML, ma passando un id vuoto e
	 * un elemento non esistente nell'XML tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente quel tag con id vuoto nell'XML.
     * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameExistsIdEmptyElementsNotExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value2");
        String tagName = "element";
        xmlParser.setProperty(tagName, "", elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * modificare il valore di un elemento appartenente ad un tag specifico con un id nell'XML, ma passando un id vuoto e
	 * un elemento vuoto tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente quel tag con id vuoto nell'XML.
     * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameExistsIdEmptyElementsEmpty() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        String tagName = "element";
        xmlParser.setProperty(tagName, "", new Hashtable<>());
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag specifico con un id nell'XML, ma passando un id vuoto e
	 * un elemento nullo tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione.
	 */
    @Test
     public void testSetPropertyWithTagNameExistsIdEmptyElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String tagName = "element";
            xmlParser.setProperty(tagName, "", null);
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * modificare il valore di un elemento appartenente ad un tag specifico con un id nell'XML, ma passando un id nullo e
	 * un elemento corretto tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente quel tag con id nullo nell'XML.
     * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameExistsIdNullElementsExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "element";
        xmlParser.setProperty(tagName, null, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * modificare il valore di un elemento appartenente ad un tag specifico con un id nell'XML, ma passando un id nullo e
	 * un elemento non esistente nell'XML tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente quel tag con id nullo nell'XML.
     * @throws Exception 
	 */
    @Test
    public void testSetPropertyWithTagNameExistsIdNullElementsNotExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value2");
        String tagName = "element";
        xmlParser.setProperty(tagName, null, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * modificare il valore di un elemento appartenente ad un tag specifico con un id nell'XML, ma passando un id nullo e
	 * un elemento vuoto tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente quel tag con id nullo nell'XML.
     * @throws Exception 
	 */
    @Test
    public void testSetPropertyWithTagNameExistsIdNullElementsEmpty() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        String tagName = "element";
        xmlParser.setProperty(tagName, null, new Hashtable<>());
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag specifico con un id nell'XML, ma passando un id nullo e
	 * un elemento nullo tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione.
	 * @throws Exception
	 */
    @Test
    public void testSetPropertyWithTagNameExistsIdNullElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String tagName = "element";
            xmlParser.setProperty(tagName, null, null);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag non esistente ma con un id valido nell'XML e fornendo 
	 * un elemento corretto tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente il tag specificato.
     * @throws Exception 
	 */
    @Test
    public void testSetPropertyWithTagNameNotExistsIdExistsElementsExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "test";
        String id = "1";
        xmlParser.setProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag non esistente ma con un id valido nell'XML e fornendo 
	 * un elemento non esistente tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente il tag specificato.
     * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameNotExistsIdExistsElementsNotExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value2");
        String tagName = "test";
        String id = "1";
        xmlParser.setProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag non esistente ma con un id valido nell'XML e fornendo 
	 * un elemento vuoto tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente il tag specificato.
     * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameNotExistsIdExistsElementsEmpty() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        String tagName = "test";
        String id = "1";
        xmlParser.setProperty(tagName, id, new Hashtable<>());
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag non esistente ma con un id valido nell'XML e fornendo 
	 * un elemento nullo tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNotExistsIdExistsElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String tagName = "test";
            String id = "1";
            xmlParser.setProperty(tagName, id, null);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag e id non esistente ma fornendo un elemento corretto
	 * tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente il tag e l'id specificato.
     * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameNotExistsIdNotExistsElementsExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "test";
        String id = "2";
        xmlParser.setProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag e id non esistente e fornendo un elemento anch'esso non esistente
	 * tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente il tag e l'id specificato.
     * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameNotExistsIdNotExistsElementsNotExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value2");
        String tagName = "test";
        String id = "2";
        xmlParser.setProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag e id non esistente ma fornendo un elemento vuoto
	 * tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente il tag e l'id specificato.
     * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameNotExistsIdNotExistsElementsEmpty() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        String tagName = "test";
        String id = "2";
        xmlParser.setProperty(tagName, id, new Hashtable<>());
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag e id non esistente ma fornendo un elemento nullo
	 * tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente il tag e l'id specificato.
     * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameNotExistsIdNotExistsElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String tagName = "test";
            String id = "2";
            xmlParser.setProperty(tagName, id, null);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag non esistente con un id vuoto, fornendo un elemento corretto
	 * tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente il tag e l'id specificato.
     * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameNotExistsIdEmptyElementsExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "test";
        xmlParser.setProperty(tagName, "", elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag non esistente con un id vuoto, fornendo un elemento non esistente
	 * tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente il tag e l'id specificato.
     * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameNotExistsIdEmptyElementsNotExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value2");
        String tagName = "test";
        xmlParser.setProperty(tagName, "", elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag non esistente con un id vuoto, fornendo un elemento vuoto
	 * tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente il tag e l'id specificato.
     * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameNotExistsIdEmptyElementsEmpty() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        String tagName = "test";
        xmlParser.setProperty(tagName, "", new Hashtable<>());
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag non esistente con un id vuoto, fornendo un elemento nullo
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione.
	 * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameNotExistsIdEmptyElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String tagName = "test";
            xmlParser.setProperty(tagName, "", null);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag non esistente con un id nullo, fornendo un elemento corretto
	 * tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente il tag e l'id specificato.
     * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameNotExistsIdNullElementsExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "test";
        xmlParser.setProperty(tagName, null, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag non esistente con un id nullo, fornendo un elemento errato
	 * tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente il tag e l'id specificato.
     * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameNotExistsIdNullElementsNotExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value2");
        String tagName = "test";
        xmlParser.setProperty(tagName, null, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag non esistente con un id nullo, fornendo un vuoto corretto
	 * tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato essendo non presente il tag e l'id specificato.
     * @throws Exception 
	 */
    @Test
     public void testSetPropertyWithTagNameNotExistsIdNullElementsEmpty() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        String tagName = "test";
        xmlParser.setProperty(tagName, null, new Hashtable<>());
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag non esistente con un id nullo, fornendo un elemento nullo
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNotExistsIdNullElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String tagName = "test";
            xmlParser.setProperty(tagName, null, null);
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag vuoto con un id esistente, fornendo un elemento corretto
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag vuoto in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameEmptyIdExistsElementsExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value2");
            String id = "1";
            xmlParser.setProperty("", id, elements);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag vuoto con un id esistente, fornendo un elemento errato
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag vuoto in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameEmptyIdExistsElementsNotExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");
            String id = "1";
            xmlParser.setProperty("", id, elements);
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag vuoto con un id esistente, fornendo un elemento vuoto
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag vuoto in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameEmptyIdExistsElementsEmpty() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");
            String id = "1";
            xmlParser.setProperty("", id, new Hashtable<>());
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag vuoto con un id esistente, fornendo un elemento nullo
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag vuoto in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameEmptyIdExistsElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String id = "1";
            xmlParser.setProperty("", id, null);
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag vuoto con un id non esistente, fornendo un elemento corretto
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag vuoto in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameEmptyIdNotExistsElementsExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value2");
            String id = "2";
            xmlParser.setProperty("", id, elements);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag vuoto con un id non esistente, fornendo un elemento errato
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag vuoto in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameEmptyIdNotExistsElementsNotExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");
            String id = "2";
            xmlParser.setProperty("", id, elements);
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag vuoto con un id non esistente, fornendo un elemento vuoto
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag vuoto in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameEmptyIdNotExistsElementsEmpty() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String id = "2";
            xmlParser.setProperty("", id, new Hashtable<>());
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag vuoto con un id non esistente, fornendo un elemento nullo
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag vuoto in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameEmptyIdNotExistsElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String id = "2";
            xmlParser.setProperty("", id, null);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag ed un id vuoto, fornendo un elemento corretto
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag vuoto in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameEmptyIdEmptyElementsExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value2");
            xmlParser.setProperty("", "", elements);
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag ed un id vuoto, fornendo un elemento errato
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag vuoto in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameEmptyIdEmptyElementsNotExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");
            xmlParser.setProperty("", "", elements);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag ed un id vuoto, fornendo un elemento vuoto
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag vuoto in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameEmptyIdEmptyElementsEmpty() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            xmlParser.setProperty("", "", new Hashtable<>());
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag ed un id vuoto, fornendo un elemento nullo
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag vuoto in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameEmptyIdEmptyElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            xmlParser.setProperty("", "", null);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag vuoto con un id nullo, fornendo un elemento corretto
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag vuoto in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameEmptyIdNullElementsExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value2");
            xmlParser.setProperty("", null, elements);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag vuoto con un id nullo, fornendo un elemento errato
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag vuoto in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameEmptyIdNullElementsNotExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");
            xmlParser.setProperty("", null, elements);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag vuoto con un id nullo, fornendo un elemento vuoto
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag vuoto in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameEmptyIdNullElementsEmpty() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            xmlParser.setProperty("", null, new Hashtable<>());
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag vuoto con un id e un oggetto Hashtable nullo.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag vuoto in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameEmptyIdNullElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            xmlParser.setProperty("", null, null);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag nullo ma con un id valido e un oggetto Hashtable corretto nell'XML.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNullIdExistsElementsExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value2");
            String id = "1";
            xmlParser.setProperty(null, id, elements);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag nullo ma con un id valido nell'XML, fornendo un elemento errato
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNullIdExistsElementsNotExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");
            String id = "1";
            xmlParser.setProperty(null, id, elements);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag nullo ma con un id valido nell'XML, fornendo un elemento vuoto
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNullIdExistsElementsEmpty() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String id = "1";
            xmlParser.setProperty(null, id, new Hashtable<>());
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag nullo ma con un id valido nell'XML, fornendo un elemento nullo
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNullIdExistsElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String id = "1";
            xmlParser.setProperty(null, id, null);
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag nullo con un id non esistente nell'XML, fornendo un elemento corretto
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNullIdNotExistsElementsExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value2");
            String id = "2";
            xmlParser.setProperty(null, id, elements);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag nullo con un id non esistente nell'XML, fornendo un elemento errato
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNullIdNotExistsElementsNotExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");
            String id = "2";
            xmlParser.setProperty(null, id, elements);
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag nullo con un id non esistente nell'XML, fornendo un elemento vuoto
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNullIdNotExistsElementsEmpty() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String id = "2";
            xmlParser.setProperty(null, id,  new Hashtable<>());
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag nullo con un id non esistente nell'XML, fornendo un elemento nullo
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNullIdNotExistsElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String id = "2";
            xmlParser.setProperty(null, id, null);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag nullo con un id vuoto ma fornendo un elemento corretto
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNullIdEmptyElementsExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value2");
            xmlParser.setProperty(null, "", elements);
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag nullo con un id vuoto, fornendo un elemento errato
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNullIdEmptyElementsNotExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");
            xmlParser.setProperty(null, "", elements);
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag nullo con un id vuoto, fornendo un elemento vuoto
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNullIdEmptyElementsEmpty() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            xmlParser.setProperty(null, "", new Hashtable<>());
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag nullo con un id vuoto, fornendo un elemento nullo
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNullIdEmptyElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            xmlParser.setProperty(null, "", null);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag ed un id nullo e fornendo un elemento corretto
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNullIdNullElementsExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");

            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value2");

            xmlParser.setProperty(null, null, elements);
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag ed un id nullo e fornendo un elemento errato
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 */
    @Test
     public void testSetPropertyWithTagNameNullIdNullElementsNotExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");
            xmlParser.setProperty(null, null, elements);
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag ed un id nullo e fornendo un elemento vuoto
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNullIdNullElementsEmpty() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            xmlParser.setProperty(null, null, new Hashtable<>());
        });
    }

    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag ed un id nullo e fornendo un elemento nullo
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che il metodo generi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 * @throws Exception
	 */
    @Test
     public void testSetPropertyWithTagNameNullIdNullElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            xmlParser.setProperty(null, null, null);
        });
    }
    
    /**
	 * Verifica se il metodo `setProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * modificare il valore di un elemento appartenente ad un tag con un id esistente e fornendo un elemento esistente ma avente un valore diverso
	 * tramite un oggetto Hashtable.
	 * Ci si aspetta che l'oggetto istanziato venga modificato con il nuovo valore dell'elemento.
     * @throws Exception 
	 */
    @Test
    public void testSetPropertyWithDuplicateValue() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element2>value1</element2><element2>value2</element2></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value1");
        String tagName = "element";
        String id = "1";
        xmlParser.setProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element2>value1</element2><element2>value1</element2></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /******************************************************************************************/

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id dato, all'interno di un XML vuoto.
     * Ci si aspetta che il metodo ritorni l'XML con l'aggiunta del nuovi elementi.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithEmptyXML() throws Exception {
    	XMLParser xmlParser = new XMLParser("");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value1");
        elements.put("element2", "value2");
        String tagName = "element";
        String id = "1";
        xmlParser.addProperty(tagName, id, elements);
        String expectedXml = "<element id=\"1\"><element1>value1</element1><element2>value2</element2></element>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id dato, all'interno di un XML nullo.
     * Ci si aspetta che il metodo generi un'eccezione.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithNullXML() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser(null);
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value1");
            elements.put("element2", "value2");
            String tagName = "element";
            String id = "1";
            xmlParser.addProperty(tagName, id, elements);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id dato, all'interno di un XML non valido.
     * Ci si aspetta che il metodo generi un'eccezione.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithInvalidXML() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value1");
            elements.put("element2", "value2");
            String tagName = "element";
            String id = "1";
            xmlParser.addProperty(tagName, id, elements);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id già esistente nell'XML
     * ma fornendo un nuovo elemento tramite un oggetto Hashtable con chiave corretta ma valore di tipo errato.
     * Ci si aspetta che il metodo generi un'eccezione.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdExistsWrongElements() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, Integer> elements = new Hashtable<>();
            elements.put("element1", 1);
            String tagName = "element";
            String id = "1";
            xmlParser.addProperty(tagName, id, elements);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id già esistente nell'XML
     * ma fornendo un elemento errato tramite un oggetto Hashtable.
     * Ci si aspetta che il metodo generi un'eccezione.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdExistsWrongElements2() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<Integer, Integer> elements = new Hashtable<>();
            elements.put(1, 1);
            String tagName = "element";
            String id = "1";
            xmlParser.addProperty(tagName, id, elements);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id già esistente nell'XML e fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag è già esistente nell'XML.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo elemento.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdExistsElementsExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "element";
        String id = "1";
        xmlParser.addProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1><element1>value2</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id già esistente nell'XML fornendo un elemento tramite un oggetto
     * Hashtable il cui il tag non è ancora presente nell'XML.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo elemento.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdExistsElementsNotExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value2");
        String tagName = "element";
        String id = "1";
        xmlParser.addProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1><element2>value2</element2></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id già esistente nell'XML fornendo un elemento vuoto tramite un oggetto
     * Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdExistsElementsEmpty() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        String tagName = "element";
        String id = "1";
        xmlParser.addProperty(tagName, id, new Hashtable<>());
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id già esistente nell'XML ma fornendo un elemento nullo tramite un
     * oggetto Hashtable.
     * Ci si aspetta che il metodo generi un'eccezione.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdExistsElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String tagName = "element";
            String id = "1";
            xmlParser.addProperty(tagName, id, null);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id non esistente nell'XML e fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag è già esistente nell'XML.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo tag con id diverso da quello esistente e
     * aggiunta del nuovo elemento.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdNotExistsElementsExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "element";
        String id = "2";
        xmlParser.addProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element><element id=\"2\"><element1>value2</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id non esistente nell'XML e fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag non è ancora presente nell'XML.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo tag con id diverso da quello esistente e
     * aggiunta del nuovo elemento.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdNotExistsElementsNotExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value2");
        String tagName = "element";
        String id = "2";
        xmlParser.addProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element><element id=\"2\"><element2>value2</element2></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id non esistente nell'XML e fornendo un elemento vuoto tramite un oggetto
     * Hashtable.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo tag con id diverso da quello esistente.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdNotExistsElementsEmpty() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        String tagName = "element";
        String id = "2";
        xmlParser.addProperty(tagName, id, new Hashtable<>());
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element><element id=\"2\"></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id non esistente nell'XML e fornendo un elemento nullo tramite un oggetto
     * Hashtable.
     * Ci si aspetta che il metodo generi un'eccezione.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdNotExistsElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String tagName = "element";
            String id = "2";
            xmlParser.addProperty(tagName, id, null);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id vuoto fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag è già esistente nell'XML.
     * Ci si aspetta che l'oggetto istanziato non venga modificato.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdEmptyElementsExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "element";
        xmlParser.addProperty(tagName, "", elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id vuoto fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag non è ancora presente nell'XML.
     * Ci si aspetta che l'oggetto istanziato non venga modificato.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdEmptyElementsNotExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value2");
        String tagName = "element";
        xmlParser.addProperty(tagName, "", elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id vuoto e fornendo un elemento vuoto tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdEmptyElementsEmpty() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        String tagName = "element";
        xmlParser.addProperty(tagName, "", new Hashtable<>());
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id vuoto fornendo un elemento nullo tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdEmptyElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String tagName = "element";
            xmlParser.addProperty(tagName, "", null);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id nullo fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag è già esistente nell'XML.
     * Ci si aspetta che l'oggetto istanziato non venga modificato.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdNullElementsExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "element";
        xmlParser.addProperty(tagName, null, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id nullo fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag non è ancora presente nell'XML.
     * Ci si aspetta che l'oggetto istanziato non venga modificato.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdNullElementsNotExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value2");
        String tagName = "element";
        xmlParser.addProperty(tagName, null, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id nullo fornendo un elemento vuoto tramite un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato non venga modificato.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdNullElementsEmpty() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        String tagName = "element";
        xmlParser.addProperty(tagName, null, new Hashtable<>());
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag specifico con un id nullo fornendo un elemento nullo tramite un oggetto Hashtable.
     * Ci si aspetta che il metodo generi un'eccezione.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameExistsIdNullElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String tagName = "element";
            xmlParser.addProperty(tagName, null, null);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag non esistente con un id valido nell'XML fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag è già esistente nell'XML.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo tag con all'interno l'elemento.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameNotExistsIdExistsElementsExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "test";
        String id = "1";
        xmlParser.addProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element><test id=\"1\"><element1>value2</element1></test></data>";
        assertEquals(expectedXml, xmlParser.getXML()); 
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag non esistente con un id valido nell'XML fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag non è ancora presente nell'XML.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo tag con all'interno il nuovo elemento.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameNotExistsIdExistsElementsNotExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value2");
        String tagName = "test";
        String id = "1";
        xmlParser.addProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element><test id=\"1\"><element2>value2</element></test></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag non esistente con un id valido nell'XML fornendo un elemento vuoto tramite un oggetto
     * Hashtable.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo tag.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameNotExistsIdExistsElementsEmpty() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        String tagName = "test";
        String id = "1";
        xmlParser.addProperty(tagName, id, new Hashtable<>());
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element><test id=\"1\"></test></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }
    
    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag non esistente con un id valido nell'XML fornendo un elemento nullo tramite un oggetto
     * Hashtable.
     * Ci si aspetta che il metodo generi un'eccezione.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameNotExistsIdExistsElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String tagName = "test";
            String id = "1";
            xmlParser.addProperty(tagName, id, null);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag ed un id non esistente nell'XML fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag è già esistente nell'XML.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo tag con l'elemento esistente.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameNotExistsIdNotExistsElementsExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "test";
        String id = "2";
        xmlParser.addProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element><test id=\"2\"><element1>value2</element1></test></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }
    
    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag ed un id non esistente nell'XML fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag non esiste nell'XML.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo tag e del nuovo elemento.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameNotExistsIdNotExistsElementsNotExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value2");
        String tagName = "test";
        String id = "2";
        xmlParser.addProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element><test id=\"2\"><element2>value2</element1></test></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag ed un id non esistente nell'XML fornendo un elemento vuoto tramite un oggetto
     * Hashtable.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo tag.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameNotExistsIdNotExistsElementsEmpty() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        String tagName = "test";
        String id = "2";
        xmlParser.addProperty(tagName, id, new Hashtable<>());
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element><test id=\"2\"></test></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag ed un id non esistente nell'XML fornendo un elemento nullo tramite un oggetto
     * Hashtable.
     * Ci si aspetta che il metodo generi un'eccezione.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameNotExistsIdNotExistsElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String tagName = "test";
            String id = "2";
            xmlParser.addProperty(tagName, id, null);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag non esistente con un id vuoto nell'XML fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag è già esistente nell'XML.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo tag e del nuovo elemento.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameNotExistsIdEmptyElementsExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "test";
        xmlParser.addProperty(tagName, "", elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element><test><element1>value2</element1></test></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag non esistente con un id vuoto nell'XML fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag non esiste nell'XML.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo tag e del nuovo elemento.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameNotExistsIdEmptyElementsNotExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value2");
        String tagName = "test";
        xmlParser.addProperty(tagName, "", elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element><test><element2>value2</element2></test></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag non esistente con un id vuoto nell'XML fornendo un elemento vuoto tramite un oggetto
     * Hashtable.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo tag.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameNotExistsIdEmptyElementsEmpty() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        String tagName = "test";
        xmlParser.addProperty(tagName, "", new Hashtable<>());
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element><test></test></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag non esistente con un id vuoto nell'XML fornendo un elemento nullo tramite un oggetto
     * Hashtable.
     * Ci si aspetta che il metodo generi un'eccezione.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameNotExistsIdEmptyElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String tagName = "test";
            xmlParser.addProperty(tagName, "", null);
        });
    }
    
    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag non esistente con un id nullo nell'XML fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag è già esistente nell'XML.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo tag e del nuovo elemento.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameNotExistsIdNullElementsExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "test";
        xmlParser.addProperty(tagName, null, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element><test><element1>value2</element1></test></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }
    
    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag non esistente con un id nullo nell'XML fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag non esiste nell'XML.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo tag e del nuovo elemento.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameNotExistsIdNullElementsNotExists() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value2");
        String tagName = "test";
        xmlParser.addProperty(tagName, null, elements);
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element><test><element2>value2</element2></test></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }
    
    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag non esistente con un id nullo nell'XML fornendo un elemento vuoto tramite un oggetto
     * Hashtable.
     * Ci si aspetta che l'oggetto istanziato venga modificato con l'aggiunta del nuovo tag.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithTagNameNotExistsIdNullElementsEmpty() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
        String tagName = "test";
        xmlParser.addProperty(tagName, null, new Hashtable<>());
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1></element><test></test></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag non esistente con un id nullo nell'XML fornendo un elemento nullo tramite un oggetto
     * Hashtable.
     * Ci si aspetta che il metodo generi un'eccezione.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameNotExistsIdNullElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String tagName = "test";
            xmlParser.addProperty(tagName, null, null);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag vuoto con un id esistente nell'XML fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag è già esistente nell'XML.
     * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameEmptyIdExistsElementsExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value2");
            String id = "1";
            xmlParser.addProperty("", id, elements);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag vuoto con un id esistente nell'XML fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag non esiste nell'XML.
     * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameEmptyIdExistsElementsNotExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");
            String id = "1";
            xmlParser.addProperty("", id, elements);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag vuoto con un id esistente nell'XML fornendo un elemento vuoto tramite un oggetto
     * Hashtable.
     * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameEmptyIdExistsElementsEmpty() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");
            String id = "1";
            xmlParser.addProperty("", id, new Hashtable<>());
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag vuoto con un id esistente nell'XML fornendo un elemento nullo tramite un oggetto
     * Hashtable.
     * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameEmptyIdExistsElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String id = "1";
            xmlParser.addProperty("", id, null);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag vuoto con un id non esistente nell'XML fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag è già esistente nell'XML.
     * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameEmptyIdNotExistsElementsExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value2");
            String id = "2";
            xmlParser.addProperty("", id, elements);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag vuoto con un id non esistente nell'XML fornendo un elemento tramite un oggetto
     * Hashtable di cui il tag è non esiste nell'XML.
     * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameEmptyIdNotExistsElementsNotExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");
            String id = "2";
            xmlParser.addProperty("", id, elements);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag vuoto con un id non esistente nell'XML fornendo un elemento vuoto tramite un oggetto
     * Hashtable.
     * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameEmptyIdNotExistsElementsEmpty() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String id = "2";
            xmlParser.addProperty("", id, new Hashtable<>());
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere nuovi elementi ad un tag vuoto con un id non esistente nell'XML fornendo un elemento nullo tramite un oggetto
     * Hashtable.
     * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
     * @throws Exception
     */
    @Test
    public void testAddPropertyWithTagNameEmptyIdNotExistsElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String id = "2";
            xmlParser.addProperty("", id, null);
        });
    }
    
    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag ed un id vuoto nell'XML fornendo un elemento tramite un oggetto
    * Hashtable di cui il tag è già esistente nell'XML.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameEmptyIdEmptyElementsExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value2");
            xmlParser.addProperty("", "", elements);
        });
    }
    
    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag ed un id vuoto nell'XML fornendo un elemento tramite un oggetto
    * Hashtable di cui il tag non esiste nell'XML.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameEmptyIdEmptyElementsNotExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");
            xmlParser.addProperty("", "", elements);
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag ed un id vuoto nell'XML fornendo un elemento vuoto tramite un oggetto
    * Hashtable.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameEmptyIdEmptyElementsEmpty() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            xmlParser.addProperty("", "", new Hashtable<>());
        });
    }
    
    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag ed un id vuoto nell'XML fornendo un elemento nullo tramite un oggetto
    * Hashtable.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameEmptyIdEmptyElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            xmlParser.addProperty("", "", null);
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag vuoto ed un id nullo nell'XML fornendo un elemento tramite un oggetto
    * Hashtable di cui il tag è già esistente nell'XML.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameEmptyIdNullElementsExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value2");
            xmlParser.addProperty("", null, elements);
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag vuoto ed un id nullo nell'XML fornendo un elemento tramite un oggetto
    * Hashtable di cui il tag non esiste nell'XML.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
    */
    @Test
    public void testAddPropertyWithTagNameEmptyIdNullElementsNotExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");

            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");

            xmlParser.addProperty("", null, elements);
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag vuoto ed un id nullo nell'XML fornendo un elemento vuoto tramite un oggetto
    * Hashtable.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameEmptyIdNullElementsEmpty() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            xmlParser.addProperty("", null, new Hashtable<>());
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag vuoto ed un id nullo nell'XML fornendo un elemento nullo tramite un oggetto
    * Hashtable.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameEmptyIdNullElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            xmlParser.addProperty("", null, null);
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag nullo con un id esistente nell'XML fornendo un elemento tramite un oggetto
    * Hashtable di cui il tag è già esistente nell'XML.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag nullo nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameNullIdExistsElementsExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value2");
            String id = "1";
            xmlParser.addProperty(null, id, elements);
        });
    }
    
    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag nullo con un id esistente nell'XML fornendo un elemento tramite un oggetto
    * Hashtable di cui il tag non esiste nell'XML.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
    */
    @Test
    public void testAddPropertyWithTagNameNullIdExistsElementsNotExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");

            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");

            String id = "1";
            xmlParser.addProperty(null, id, elements);
        });
    }
    
    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag nullo con un id esistente nell'XML fornendo un elemento vuoto tramite un oggetto
    * Hashtable.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameNullIdExistsElementsEmpty() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String id = "1";
            xmlParser.addProperty(null, id, new Hashtable<>());
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag nullo con un id esistente nell'XML fornendo un elemento nullo tramite un oggetto
    * Hashtable.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag vuoto nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameNullIdExistsElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String id = "1";
            xmlParser.addProperty(null, id, null);
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag nullo con un id non esistente nell'XML fornendo un elemento tramite un oggetto
    * Hashtable di cui il tag è già esistente nell'XML.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag nullo nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameNullIdNotExistsElementsExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value2");
            String id = "2";
            xmlParser.addProperty(null, id, elements);
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag nullo con un id non esistente nell'XML fornendo un elemento tramite un oggetto
    * Hashtable di cui il tag non esiste nell'XML.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag nullo nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameNullIdNotExistsElementsNotExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");
            String id = "2";
            xmlParser.addProperty(null, id, elements);
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag nullo con un id non esistente nell'XML fornendo un elemento vuoto tramite un oggetto
    * Hashtable.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag nullo nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameNullIdNotExistsElementsEmpty() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String id = "2";
            xmlParser.addProperty(null, id,  new Hashtable<>());
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag nullo con un id non esistente nell'XML fornendo un elemento nullo tramite un oggetto
    * Hashtable.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag nullo nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameNullIdNotExistsElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            String id = "2";
            xmlParser.addProperty(null, id, null);
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag nullo con un id vuoto nell'XML fornendo un elemento tramite un oggetto
    * Hashtable di cui il tag è già esistente nell'XML.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag nullo nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameNullIdEmptyElementsExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value2");
            xmlParser.addProperty(null, "", elements);
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag nullo con un id vuoto nell'XML fornendo un elemento tramite un oggetto
    * Hashtable di cui il tag non esiste nell'XML.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag nullo nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameNullIdEmptyElementsNotExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");
            xmlParser.addProperty(null, "", elements);
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag nullo con un id vuoto nell'XML fornendo un elemento vuoto tramite un oggetto
    * Hashtable.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag nullo nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameNullIdEmptyElementsEmpty() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            xmlParser.addProperty(null, "", new Hashtable<>());
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag nullo con un id vuoto nell'XML fornendo un elemento nullo tramite un oggetto
    * Hashtable.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag nullo nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameNullIdEmptyElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            xmlParser.addProperty(null, "", null);
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag ed un id nullo nell'XML fornendo un elemento tramite un oggetto
    * Hashtable di cui il tag è già esistente nell'XML.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag nullo nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameNullIdNullElementsExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element1", "value2");
            xmlParser.addProperty(null, null, elements);
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag ed un id nullo nell'XML fornendo un elemento tramite un oggetto
    * Hashtable di cui il tag non esiste nell'XML.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag nullo nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameNullIdNullElementsNotExists() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            Hashtable<String, String> elements = new Hashtable<>();
            elements.put("element2", "value2");
            xmlParser.addProperty(null, null, elements);
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag ed un id nullo nell'XML fornendo un elemento vuoto tramite un oggetto
    * Hashtable.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag nullo nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameNullIdNullElementsEmpty() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            xmlParser.addProperty(null, null, new Hashtable<>());
        });
    }

    /**
    * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
    * aggiungere nuovi elementi ad un tag ed un id nullo nell'XML fornendo un elemento nullo tramite un oggetto
    * Hashtable.
    * Ci si aspetta che il metodo generi un'eccezione aggiungendo un tag nullo nell'XML.
    * @throws Exception
    */
    @Test
    public void testAddPropertyWithTagNameNullIdNullElementsNull() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element1>value1</element1></element></data>");
            xmlParser.addProperty(null, null, null);
        });
    }

    /**
     * Verifica se il metodo `addProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
     * aggiungere elementi appartenenti ad un tag con un id esistente fornendo un elemento con tag già esistente tramite 
     * un oggetto Hashtable.
     * Ci si aspetta che l'oggetto istanziato venga modificato con il l'aggiunta dell'elemento.
     * @throws Exception 
     */
    @Test
    public void testAddPropertyWithDuplicateValue() throws Exception {
    	XMLParser xmlParser = new XMLParser("<data><element id=\"1\"><element2>value1</element2><element2>value2</element2></element></data>");
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element2", "value1");
        String tagName = "element";
        String id = "1";
        xmlParser.setProperty(tagName, id, elements);
        String expectedXml = "<data><element id=\"1\"><element2>value1</element2><element2>value2</element2><element2>value1</element2></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /******************************************************************************************/
    
    /**
     * Verifica se il metodo `testGetProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia 
     * ottenere il valore di un elemento che appartiene a un tag esistente all'interno di un XML.
     * Il comportamento atteso è che il metodo ritorni il valore del tag specificato. In particolare, il test verifica se il metodo è in grado di:
     * - Identificare correttamente più elementi con lo stesso nome di tag all'interno dell'XML.
     * - Estrarre correttamente i valori degli attributi e degli elementi figli per ciascun elemento identificato.
     * @throws Exception 
     */
    @Test
    public void testGetProperty() throws Exception {
    	String xml = "<data><element id=\"1\"><element1>value1</element1><element2>value2</element2></element><element id=\"2\"><element1>value3</element1><element2>value4</element2></element></data>";
        XMLParser xmlParser = new XMLParser(xml);
        String tagName = "element";
        Vector<Hashtable<String, String>> result = xmlParser.getProperty(tagName);
        assertNotNull(result);
        assertEquals(2, result.size());
        Hashtable<String, String> firstElement = result.get(0);
        assertEquals("1", firstElement.get("id"));
        assertEquals("value1", firstElement.get("element1"));
        assertEquals("value2", firstElement.get("element2"));
        Hashtable<String, String> secondElement = result.get(1);
        assertEquals("2", secondElement.get("id"));
        assertEquals("value3", secondElement.get("element1"));
        assertEquals("value4", secondElement.get("element2"));
    }
    
    /**
     * Verifica se il metodo `testGetProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia 
     * ottenere il valore di un elemento che appartiene a un tag esistente all'interno di un XML vuoto.
     * Il comportamento atteso è che il metodo ritorni un valore vuoto del tag specificato essendo vuoto l'XML.
     * @throws Exception 
     */
    @Test
    public void testGetPropertyWithEmptyXML() throws Exception {
    	XMLParser xmlParser = new XMLParser("");
        String tagName = "element";
        assertNotNull(xmlParser.getProperty(tagName));
    }
    
    /**
     * Verifica se il metodo `testGetProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia 
     * ottenere il valore di un elemento che appartiene a un tag esistente all'interno di un XML nullo.
     * Il comportamento atteso è che il metodo sollevi un'eccezione essendo nullo l'XML.
     * @throws Exception
     */
    @Test
    public void testGetPropertyWithNullXML() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser(null);
            String tagName = "element";
            xmlParser.getProperty(tagName);
        });
    }
    
    /**
     * Verifica se il metodo `testGetProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia 
     * ottenere il valore di un elemento che appartiene a un tag esistente all'interno di un XML non valido.
     * Il comportamento atteso è che il metodo sollevi un'eccezione essendo non valido l'XML.
     * @throws Exception
     */
    @Test
    public void testGetPropertyWithInvalidXML() {
        assertThrows(Exception.class, () -> {
            String xml = "<data><element id=\"1\"><element1>value1</element1><element2>value2</element2></element>";
            XMLParser xmlParser = new XMLParser(xml);
            String tagName = "element";
            xmlParser.getProperty(tagName);
        });
    }
    
    /**
     * Verifica se il metodo `testGetProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia 
     * ottenere il valore di un elemento che appartiene a un tag vuoto all'interno di un XML.
     * Il comportamento atteso è che il metodo sollevi un'eccezione siccome non è possibile avere un tag vuoto nell'XML.
     * @throws Exception
     */
    @Test
    public void testGetPropertyWithEmptyTag() {
        assertThrows(Exception.class, () -> {
            String xml = "<data></data>";
            XMLParser xmlParser = new XMLParser(xml);
            
            xmlParser.getProperty("");
        });
    }

    /**
     * Verifica se il metodo `testGetProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia 
     * ottenere il valore di un elemento che appartiene a un tag nullo all'interno di un XML.
     * Il comportamento atteso è che il metodo sollevi un'eccezione siccome non è possibile avere un tag nullo nell'XML.
     * @throws Exception
     */
    @Test
    public void testGetPropertyWithNullTag() {
        assertThrows(Exception.class, () -> {
            String xml = "<data></data>";
            XMLParser xmlParser = new XMLParser(xml);
            xmlParser.getProperty(null);
        });
    }

    /**
     * Verifica se il metodo `testGetProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia 
     * ottenere il valore di un elemento che appartiene a un tag che non esiste all'interno di un XML.
     * Il comportamento atteso è che il metodo ritorni un valore vuoto siccome non è presente quel tag specificato all'interno dell'XML.
     * @throws Exception 
     */
    @Test
    public void testGetPropertyWithMissingTag() throws Exception {
    	String xml = "<data><element1>value1</element1></data>";
        XMLParser xmlParser = new XMLParser(xml);
        String tagName = "element2";
        Vector<Hashtable<String, String>> result = xmlParser.getProperty(tagName);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /******************************************************************************************/
    
	/**
	 * Verifica se il metodo `testGetSingleProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * visualizzare il valore di un elemento appartenente ad un tag esistente nell'XML.
     * Ci si aspetta che il metodo ritorni il valore del tag che si è specificato.
	 * @throws Exception 
	 */
    @Test
    public void testGetSingleProperty() throws Exception {
    	String xml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        XMLParser xmlParser = new XMLParser(xml);
        String tagName = "element1";
        assertEquals("value1", xmlParser.getSingleProperty(tagName));
    }
    
	/**
	 * Verifica se il metodo `testGetSingleProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * visualizzare il valore di un elemento appartenente ad un tag in un XML vuoto.
     * Ci si aspetta che il metodo non ritorni nessun valore essendo vuoto l'XML.
	 * @throws Exception 
	 */
    @Test
    public void testGetSinglePropertyWithEmptyXML() throws Exception {
    	XMLParser xmlParser = new XMLParser("");
        String tagName = "element";
        assertNotNull(xmlParser.getSingleProperty(tagName));
    }
    
	/**
	 * Verifica se il metodo `testGetSingleProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * visualizzare il valore di un elemento appartenente ad un tag in un XML nullo.
     * Ci si aspetta che il metodo sollevi un'eccezione essendo nullo l'XML.
     * @throws Exception
	 */
    @Test
    public void testGetSinglePropertyWithNullXML() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser(null);
            String tagName = "element";
            xmlParser.getSingleProperty(tagName);
        });
    }
    
	/**
	 * Verifica se il metodo `testGetSingleProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * visualizzare il valore di un elemento appartenente ad un tag in un XML non valido.
     * Ci si aspetta che il metodo sollevi un'eccezione essendo non valido l'XML.
     * @throws Exception
	 */
    @Test
    public void testGetSinglePropertyWithInvalidXML() {
        assertThrows(Exception.class, () -> {
            String xml = "<data><element id=\"1\"><element1>value1</element1><element2>value2</element2></element>";
            XMLParser xmlParser = new XMLParser(xml);
            String tagName = "element";
            xmlParser.getSingleProperty(tagName);
        });
    }

	/**
	 * Verifica se il metodo `testGetSingleProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * visualizzare il valore di un elemento appartenente ad un tag vuoto in un XML.
     * Ci si aspetta che il metodo sollevi un'eccezione siccome non è possibile avere un tag vuoto in XML.
     * @throws Exception
	 */
    @Test
    public void testGetSinglePropertyWithEmptyTag() {
        assertThrows(Exception.class, () -> {
            String xml = "<data></data>";
            XMLParser xmlParser = new XMLParser(xml);
            String tagName = "";
            xmlParser.getSingleProperty(tagName);
        });
    }
    
	/**
	 * Verifica se il metodo `testGetSingleProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * visualizzare il valore di un elemento appartenente ad un tag nullo in un XML.
     * Ci si aspetta che il metodo sollevi un'eccezione siccome non è possibile avere un tag nullo in XML.
	 */
    @Test
    public void testGetSinglePropertyWithNullTag() {
        assertThrows(Exception.class, () -> {
            String xml = "<data></data>";
            XMLParser xmlParser = new XMLParser(xml);

            String tagName = null;
            xmlParser.getSingleProperty(tagName);
        });
    }
    
	/**
	 * Verifica se il metodo `testGetSingleProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * visualizzare il valore di un elemento appartenente ad un tag non esistente nell'XML.
     * Ci si aspetta che il metodo non ritorni nessun valore non essendoci quel determinato tag nell'XML.
     * @throws Exception
	 */
    @Test
    public void testGetSinglePropertyWithMissingTag() throws Exception {
    	String xml = "<data><element1>value1</element1></data>";
        XMLParser xmlParser = new XMLParser(xml);
        String tagName = "element2";
        assertNotNull("", xmlParser.getSingleProperty(tagName));
    }

	/**
	 * Verifica se il metodo `testGetSingleProperty()` della classe XMLParser è in grado di gestire correttamente il caso in cui si voglia
	 * visualizzare il valore di un elemento appartenente ad un tag che si ripete più volte nell'XML.
     * Ci si aspetta che il metodo ritorni l'ultimo valore di quel determinato tag nell'XML.
	 * @throws Exception 
	 */
    @Test
    public void testGetSinglePropertyWithDuplicateTag() throws Exception {
    	String xml = "<data><element1>value1</element1><element1>value2</element1><element1>value3</element1></data>";
        XMLParser xmlParser = new XMLParser(xml);
        String tagName = "element1";
        assertEquals("value3", xmlParser.getSingleProperty(tagName));
    }

    /******************************************************************************************/
    
    /**
     * Verifica se il metodo `store()` della classe XMLParser memorizza correttamente l'XML come stringa.
     * Viene creato un oggetto XMLParser con un dato XML e ci si aspetta che il risultato del metodo store() sia uguale all'XML di partenza.
     * @throws Exception 
     */
    @Test
    public void testStore() throws Exception {
    	String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><data><element id=\"1\"><element1>value1</element1></element></data>";
        XMLParser xmlParser = new XMLParser(xml);
        xmlParser.store();
        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><data><element id=\"1\"><element1>value1</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `store()` della classe XMLParser memorizza correttamente l'XML come stringa.
     * In primis, viene creato un oggetto XMLParser con un dato XML; viene poi aggiunta una proprietà e ci si aspetta che il risultato del
     * metodo store() sia uguale all'XML di partenza con la proprietà aggiunta.
     * @throws Exception 
     */
    @Test
    public void testStore2() throws Exception {
    	String xml = "<data><element id=\"1\"><element1>value1</element1></element></data>";
        XMLParser xmlParser = new XMLParser(xml);
        Hashtable<String, String> elements = new Hashtable<>();
        elements.put("element1", "value2");
        String tagName = "element";
        String id = "1";
        xmlParser.addProperty(tagName, id, elements);
        xmlParser.store();
        String expectedXml = "<data><element id=\"1\"><element1>value1</element1><element1>value2</element1></element></data>";
        assertEquals(expectedXml, xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `store()` della classe XMLParser gestisce correttamente un XML vuoto.
     * Ci si aspetta che il risultato del metodo store() sia un oggetto con XML vuoto.
     * @throws Exception 
     */
    @Test
    public void testStoreWithEmptyXML() throws Exception {
    	XMLParser xmlParser = new XMLParser("");
        xmlParser.store();
        assertEquals("", xmlParser.getXML());
    }

    /**
     * Verifica se il metodo `store()` della classe XMLParser gestisce correttamente un XML nullo.
     * Ci si aspetta il lancio di un'eccezione con un oggetto XML nullo.
     * @throws Exception 
     */
    @Test
    public void testStoreWithNullXML() {
        assertThrows(Exception.class, () -> {
            XMLParser xmlParser = new XMLParser(null);
            xmlParser.store();
        });
    }

    /**
     * Verifica se il metodo `store()` della classe XMLParser gestisce correttamente un XML non valido.
     * Ci si aspetta il lancio di un'eccezione con un oggetto XML non valido.
     * @throws Exception 
     */
    @Test
    public void testStoreWithInvalidXML() {
        assertThrows(Exception.class, () -> {
            String xml = "<data><element1>value1</element1>";
            XMLParser xmlParser = new XMLParser(xml);
            xmlParser.store();
        });
    }


    /******************************************************************************************/
    
    /**
     * Verifica se il metodo `returnSpecial()` della classe XMLParser converte correttamente le entità speciali quali: &amp;lt; e &amp;gt; presenti nella stringa di input.
     * Ci si aspetta che l'entità speciali vengano convertite correttamente in: &lt; e &gt;
     */
    @Test
    public void testReturnSpecial() {
        String input = "This is &lt;special&gt;";
        String expectedOutput = "This is <special>";
        assertEquals(expectedOutput, XMLParser.returnSpecial(input));
    }

    /**
     * Verifica se il metodo `returnSpecial()` della classe XMLParser converte correttamente l'entità speciale &amp;lt; presente nella stringa di input.
     * Ci si aspetta che l'entità speciale venga convertita correttamente in: 
     */
    @Test
    public void testReturnSpecial2() {
        String input = "This is &lt;special>";
        String expectedOutput = "This is <special>";
        assertEquals(expectedOutput, XMLParser.returnSpecial(input));
    }

    /**
     * Verifica se il metodo `returnSpecial()` della classe XMLParser gestisce correttamente l'entità speciale &amp;gt; presente nella stringa di input.
     * Ci si aspetta che l'entità speciale venga convertita correttamente in: &gt;
     */
    @Test
    public void testReturnSpecial3() {
        String input = "This is <special&gt;";
        String expectedOutput = "This is <special>";
        assertEquals(expectedOutput, XMLParser.returnSpecial(input));
    }

    /**
     * Verifica se il metodo `returnSpecial()` della classe XMLParser gestisce correttamente le entità speciali quali: &amp;lt; | &amp;gt; | &amp;amp; | &amp;quot; | &amp;apos; presenti 
     * nella stringa di input.
     * Ci si aspetta che l'entità speciali vengano convertite correttamente in: &lt; | &gt; | &amp; | " | '
     */
    @Test
    public void testReturnSpecialWithSpecialEntities() {
        String input = "This is &lt;special&gt; &amp; &quot; &apos;";
        String expectedOutput = "This is <special> & \" '";
        assertEquals(expectedOutput, XMLParser.returnSpecial(input));
    }

    /**
     * Verifica se il metodo `returnSpecial()` della classe XMLParser restituisce una stringa vuota quando la stringa di input è vuota.
     */
    @Test
    public void testReturnSpecialWithEmptyInput() {
        assertEquals("",  XMLParser.returnSpecial(""));
    }

    /**
     * Verifica se il metodo `returnSpecial()` della classe XMLParser gestisce correttamente il caso in cui la stringa di input è nulla.
     * Ci si aspetta il lancio di un'eccezione.
     * @throws Exception
     */
    @Test
    public void testReturnSpecialWithNullInput() {
        assertThrows(Exception.class, () -> {
            assertEquals("", XMLParser.returnSpecial(null));
        });
    }
}

