package student.gennaroIannicelliDue;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ClassUnderTest.XMLParser;
import java.util.Hashtable;

class StudentTest {

	static XMLParser xmlparser;
	static String fileXML;
	static String fileXML2;
	static XMLParser xmlparser2;
	
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		//XML vuoto
		fileXML2="";
		xmlparser2 = new XMLParser(fileXML2);	
}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		fileXML = null;
		xmlparser = null;
		fileXML2=null;
		xmlparser2=null;
	}

	@BeforeEach
	void setUp() throws Exception {
		fileXML = "<data>\r\n"
				+ "    <person id=\"1\">\r\n"
				+ "        <name>John</name>\r\n"
				+ "        <age>30</age>\r\n"
				+ "    </person>\r\n"
				+ "    <person id=\"2\">\r\n"
				+ "        <name>Alice</name>\r\n"
				+ "        <age>25</age>\r\n"
				+ "    </person>\r\n"
				+ "</data>\r\n"
				+ "";
		xmlparser = new XMLParser(fileXML);
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	void testXMLParser() {
		//assertThrows(SAXParseException.class,()->xmlparser=new XMLParser("<abc><abc>"));
	}
	
	@Test
	void testXMLParser4() {
		//assertThrows(Exception.class,()->xmlparser = new XMLParser("ABC"));
	}
	
	@Test
	void testXMLParser3() throws Exception{
		xmlparser = new XMLParser("");
		assertNotNull(xmlparser);
	}
	
	@Test
	void testXMLParser6() throws Exception{
		assertNotNull(xmlparser2);
	}
	
	@Test
	void testXMLParser5() throws Exception{
		//assertThrows(TransformerConfigurationException.class,()->new XMLParser("<note><value></value><note>"));
	}

	@Test
	void testGetXML() {
		assertEquals(xmlparser.getXML(),fileXML,"Err getXML e out non coincidono");
	}
	
	@Test
	void testGetXML2() {
		assertEquals(xmlparser2.getXML(),fileXML2,"Err getXML e out non coincidono");
	}
	
	
	
	@Test
	void testGetProperty() {
		Hashtable<String,String> getValues = new Hashtable<>();
		getValues.put("id", "1");
		getValues.put("name", "John");
		getValues.put("age", "30");
		
		assertTrue(xmlparser.getProperty("person").contains(getValues));
		
		getValues=null;
	}
	
	@Test
	void testGetProperty2() {
		assertEquals(xmlparser2.getProperty("people").toString(),"[]");		
	}

	
	@Test
	void testSetProperty() {
		Hashtable<String,String> getValues = new Hashtable<>();
		getValues.put("id", "2");
		getValues.put("name", "Giovanni");
		getValues.put("age", "36");
		xmlparser.setProperty("person", "2", getValues);
		assertTrue(xmlparser.getProperty("person").toString().contains("age=36, name=Giovanni, id=2"));
		getValues=null;
	}
	
	@Test
	void testSetProperty2() {
		Hashtable<String,String> setValues = new Hashtable<>();
		setValues.put("id", "4");
		setValues.put("name", "Peppe");
		setValues.put("age", "23");
		xmlparser.setProperty("person", "4", setValues);
		String nuova = new String(xmlparser.getProperty("person").toString());
		assertTrue(nuova.contains("id=4"));
		assertTrue(nuova.contains("name=Peppe"));
		assertTrue(nuova.contains("age=23"));
		setValues=null;
		nuova = null;
	}

	@Test
	void testAddProperty() {
		Hashtable<String, String> newPersonValues = new Hashtable<>();
		newPersonValues.put("name", "Bob");
		newPersonValues.put("age", "40");
		newPersonValues.put("id", "3");

		xmlparser.addProperty("person", "3", newPersonValues);
		assertTrue(xmlparser.getProperty("person").contains(newPersonValues));
		
		newPersonValues=null;
		
	}
	
	@Test
	void testAddProperty2() throws Exception {
		Hashtable<String, String> newPersonValues = new Hashtable<>();

		xmlparser2.addProperty("person", "3", newPersonValues);
		System.out.println(xmlparser2.getProperty("person"));
		assertTrue(xmlparser2.getProperty("person").toString().contains("id=3"));
		assertFalse(xmlparser2.getProperty("person").toString().contains("name"));
		
		newPersonValues=null;
		
	}
	
	@Test
	void testAddProperty3() {
		Hashtable<String, String> newPersonValues = new Hashtable<>();
		newPersonValues.put("name", "Bob");
		newPersonValues.put("age", "40");
		newPersonValues.put("id", "1");

		xmlparser.addProperty("person", "1", newPersonValues);
		assertTrue(xmlparser.getProperty("person").contains(newPersonValues));
		
		newPersonValues=null;
		
	}

	@Test
	void testGetSingleProperty() {
		Hashtable<String, String> newPersonValues = new Hashtable<>();
		newPersonValues.put("name", "Bob");
		newPersonValues.put("age", "40");
		newPersonValues.put("id", "3");

		xmlparser.addProperty("person", "3", newPersonValues);
		assertEquals(xmlparser.getSingleProperty("age"),"40");
	}
	
	@Test
	void testGetSingleProperty2() {
		Hashtable<String, String> newPersonValues = new Hashtable<>();
		newPersonValues.put("name", "Bob");
		newPersonValues.put("age", "40");
		newPersonValues.put("id", "3");

		xmlparser.addProperty("person", "3", newPersonValues);
		assertEquals(xmlparser.getSingleProperty("nomignolo"),"");
	}


	@Test
	void testStore() throws Exception {
		Hashtable<String,String> newValue = new Hashtable<>();
		newValue.put("name", "Peppe");
		newValue.put("age", "89");
		
		xmlparser.addProperty("person", "5", newValue);
		xmlparser.store();
		assertTrue(xmlparser.getXML().contains("<person id=\"5\"><age>89</age><name>Peppe</name></person>"));

	}

	@SuppressWarnings("static-access")
	@Test
	void testReturnSpecial() {
		String str1 = "Stringa d'esempio: &lt;tag>;content&lt;/tag>;";
		String str2 = "Stringa d'esempio: <tag>;content</tag>;"; //output atteso
		assertEquals(str2,xmlparser.returnSpecial(str1));
	}
	
	@SuppressWarnings("static-access")
	@Test
	void testReturnSpecial2() {
		String str1 = " ";
		String str2 = " ";
		assertEquals(str2,xmlparser.returnSpecial(str1));
	}
}
