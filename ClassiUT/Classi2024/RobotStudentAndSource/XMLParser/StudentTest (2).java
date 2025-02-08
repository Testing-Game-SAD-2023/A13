package student.pasqualeMottolaDue;


import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Hashtable;

import javax.xml.transform.TransformerConfigurationException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.XMLParser;

class StudentTest {
	
	XMLParser test;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		
	}

	@BeforeEach
	void setUp() throws Exception {
	String file="<?xml version=\"1.0\"?>\r\n"
			+ "<Catalog>\r\n"
			+ "   <Book id=\"bk101\">\r\n"
			+ "      <Author>Garghentini, Davide</Author>\r\n"
			+ "      <Title>XML Developer's Guide</Title>\r\n"
			+ "      <Genre>Computer</Genre>\r\n"
			+ "      <Price>44.95</Price>\r\n"
			+ "      <PublishDate>2000-10-01</PublishDate>\r\n"
			+ "      <Description>An in-depth look at creating applications\r\n"
			+ "      with XML.</Description>\r\n"
			+ "   </Book>\r\n"
			+ "   <Book id=\"bk102\">\r\n"
			+ "      <Author>Garcia, Debra</Author>\r\n"
			+ "      <Title>Midnight Rain</Title>\r\n"
			+ "      <Genre>Fantasy</Genre>\r\n"
			+ "      <Price>5.95</Price>\r\n"
			+ "      <PublishDate>2000-12-16</PublishDate>\r\n"
			+ "      <Description>A former architect battles corporate zombies,\r\n"
			+ "      an evil sorceress, and her own childhood to become queen\r\n"
			+ "      of the world.</Description>\r\n"
			+ "   </Book>\r\n"
			+ "</Catalog>";
	test=new XMLParser(file);
	}

	@AfterEach
	void tearDown() throws Exception {
		test=null;
	}

	@Test
	void testXMLParser() throws Exception {
		String file="<?xml version=\"1.0\"?>\r\n"
				+ "<Catalog>\r\n"
				+ "   <Book id=\"bk101\">\r\n"
				+ "      <Author>Garghentini, Davide</Author>\r\n"
				+ "      <Title>XML Developer's Guide</Title>\r\n"
				+ "      <Genre>Computer</Genre>\r\n"
				+ "      <Price>44.95</Price>\r\n"
				+ "      <PublishDate>2000-10-01</PublishDate>\r\n"
				+ "      <Description>An in-depth look at creating applications\r\n"
				+ "      with XML.</Description>\r\n"
				+ "   </Book>\r\n"
				+ "   <Book id=\"bk102\">\r\n"
				+ "      <Author>Garcia, Debra</Author>\r\n"
				+ "      <Title>Midnight Rain</Title>\r\n"
				+ "      <Genre>Fantasy</Genre>\r\n"
				+ "      <Price>5.95</Price>\r\n"
				+ "      <PublishDate>2000-12-16</PublishDate>\r\n"
				+ "      <Description>A former architect battles corporate zombies,\r\n"
				+ "      an evil sorceress, and her own childhood to become queen\r\n"
				+ "      of the world.</Description>\r\n"
				+ "   </Book>\r\n"
				+ "</Catalog>";
		XMLParser test1=new XMLParser(file);
	}
	
	@Test
	void testXMLParserInvalid()throws Exception{
		String temp=null;
		String temp2="";	
	
		XMLParser test1=new XMLParser(temp);
		XMLParser test2=new XMLParser(temp2);
			
	}
	@Test
	void testXMLParserInvalid2()throws Exception{
		String temp="<invalid>";
		Exception e=assertThrows(Exception.class,()->new XMLParser(temp));
		assertEquals("Not A Valid Format!",e.getMessage());
			
	}
	@Test
	void testGetXML() throws Exception {
		test.getXML();	
		
	}

	@Test
	void testSetProperty() {
		Hashtable<String, String> elements = new Hashtable<>();
		elements.put("element1","value1");
		elements.put("element2","value2");
		elements.put("Author","Author");
		elements.put("Book","bk100");
		test.setProperty("Book", "bk100", elements);
		test.setProperty("Book", "bk102", elements);
	}

	@Test
	void testAddProperty() {
		Hashtable<String, String> elements = new Hashtable<>();
		elements.put("element1","value1");
		elements.put("element2","value2");
		elements.put("Author","Author");
		elements.put("Book","bk102");
		test.addProperty("Book", "bk102", elements);
	}

	@Test
	void testGetProperty() {
		test.getProperty("Book");
	}
	@Test
	void testGetPropertyNull() {		
		test.getProperty("");
	}
	@Test
	void testGetProperty2() throws Exception {
		XMLParser prova = new XMLParser("<root><childElement>Prova</childElement></root>");
        prova.getProperty("childElement");
	}
	@Test
	void testGetPropertyFalse() {
		test.getProperty("ImpossibleeeeeeeeeeToFound");
	}
	@Test
	void testGetSingleProperty() {
		test.getSingleProperty("");
	}
	@Test
	void testGetSingleProperty2() {
		test.getSingleProperty("Price");
	}
	@Test
	void testGetSingleProperty3() {
		test.getSingleProperty("Catalog");
	}
	
	@Test
	void testGetSingleProperty4() {
		test.getSingleProperty("123");
	}
	
	
	@Test
	void testStore() throws Exception{
		test.store();
	}
	
	@Test
	void testReturnSpecial() {
		test.returnSpecial(test.getXML());
	}

}
