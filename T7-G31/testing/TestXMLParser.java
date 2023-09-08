import org.junit.*;
import static org.junit.Assert.*;

// Game: 1020
public class TestXMLParser {

	@Test(timeout = 4000)
	public void test_1_1233() throws Throwable {
		XMLParser p = null; 
		try{
			p = new XMLParser(null);
		}catch(Throwable e){
			//assertEquals("Not A Valid Format!",e.getMessage());
		}
		assertEquals(null,p.getXML());
	}

	@Test(timeout = 4000)
	public void test_2_1234() throws Throwable {
		XMLParser p = null; 
		try{
			p = new XMLParser("<xml></xml>");
		}catch(Throwable e){
			//assertEquals("Not A Valid Format!",e.getMessage());
		}
		assertEquals("<xml></xml>",p.getXML());
	}

	@Test(timeout = 4000)
	public void test_3_1235() throws Throwable {
		String donger = "ABCDEFGHIJKLMNOPQRSTUVWXYZ \tabcdefghijlkmnopqrstuvqxyz !@#$%^&*()_+1234567890-=[]{};'\\:\"|,./<>?`~";
			
		assertEquals(donger,XMLParser.returnSpecial(donger));
	}

	@Test(timeout = 4000)
	public void test_4_1236() throws Throwable {
		XMLParser p = null; 

		p = new XMLParser("<xml><poop ass=\"lol\"></poop><butt /><poop lol=\"1\"></poop></xml>");
		assertEquals(2,p.getProperty("poop").size());
	}

	@Test(timeout = 4000)
	public void test_5_1244() throws Throwable {
		// test here!
				try {
			String xml = "<?xml version='1.0' encoding='UTF-8'?><note><to>Tove</to><from>Jani</from><heading>Reminder</heading><body>Don't forget me this weekend!</body></note>";
			//String xml = "xml";
			XMLParser xmlp = new XMLParser(xml);
			//System.out.println(xmlp.getXML());
			assertEquals(true, xml.equals(xmlp.getXML()));
			assertEquals(xml, xmlp.getXML());
	        //fail("Expected an Exception to be thrown");
		} catch (Exception e) {
			e.printStackTrace();
        	//assertEquals(e.getMessage(), new TransformerConfigurationException().getMessage());
		}
	}

	@Test(timeout = 4000)
	public void test_6_1245() throws Throwable {
		Class c = XMLParser.class;
		try {
			c.getMethod("init");
			fail("Public method found");
		} catch (NoSuchMethodException nsme) {
		}
	}

	@Test(timeout = 4000)
	public void test_7_1247() throws Throwable {
		String xml = "<?xml version='1.0' encoding='UTF-8'?><root></root>";
	    XMLParser xmlp = new XMLParser(xml);
	    java.util.Hashtable<String, String> note = new java.util.Hashtable<String, String>();
    	note.put("to", "Tove");
    	note.put("from", "Jani");
    	note.put("heading", "Reminder");
	    note.put("body", "Don't forget me this weekend!");
    	xmlp.addProperty("note", "note1", note);
	    xmlp.store();
    	assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><note id=\"note1\"><body>Don't forget me this weekend!</body><heading>Reminder</heading><from>Jani</from><to>Tove</to></note></root>", xmlp.getXML());
    	note.put("body", "Don't forget me next weekend!");
    	xmlp.setProperty("note", "note1", note);
    	xmlp.store();
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><note id=\"note1\"><body>Don't forget me next weekend!</body><heading>Reminder</heading><from>Jani</from><to>Tove</to></note></root>", xmlp.getXML());
	}

	@Test(timeout = 4000)
	public void test_8_1252() throws Throwable {
		    String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijlkmnopqrstuvqxyz !@#$%^*_+1234567890-=[];\'\\:\",./&gt;&lt;?`~";
    	String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><alphabets><english>" + ALPHABET + "</english></alphabets>";
    	XMLParser parser = new XMLParser(xml);
	    parser.store();
    	assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><alphabets><english>"+ALPHABET+"</english></alphabets>",parser.getXML());
	}

	@Test(timeout = 4000)
	public void test_9_1253() throws Throwable {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijlkmnopqrstuvqxyz !@#$%^*_+1234567890-=[];\'\\:\",./&gt;&lt;?`~";
		String xml = "<?xml version='1.0' encoding='UTF-8'?><root></root>";
    	XMLParser xmlp = new XMLParser(xml);
    	java.util.Hashtable<String, String> note = new java.util.Hashtable<String, String>();
    	note.put("to", "Tove");
    	note.put("from", "Jani");
    	note.put("heading", "Reminder");
    	note.put("body", alphabet);
    	xmlp.addProperty("note", "note1", note);
    	xmlp.store();
		assertEquals(alphabet, xmlp.getSingleProperty("body"));
	}

	@Test(timeout = 4000)
	public void test_10_1254() throws Throwable {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijlkmnopqrstuvqxyz !@#$%^*_+1234567890-=[];\'\\:\",./&gt;&lt;?`~";
		String xml = "<?xml version='1.0' encoding='UTF-8'?><root></root>";
    	XMLParser xmlp = new XMLParser(xml);
    	java.util.Hashtable<String, String> note = new java.util.Hashtable<String, String>();
    	note.put("to", alphabet);
    	note.put("from", "Jani");
    	note.put("heading", "Reminder");
    	note.put("body", "hello");
    	xmlp.addProperty("note", "note1", note);
    	xmlp.store();
		assertEquals(alphabet, xmlp.getSingleProperty("to"));
	}

	@Test(timeout = 4000)
	public void test_11_1255() throws Throwable {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijlkmnopqrstuvqxyz !@#$%^*_+1234567890-=[];\'\\:\",./&gt;&lt;?`~";
		String xml = "<?xml version='1.0' encoding='UTF-8'?><root></root>";
    	XMLParser xmlp = new XMLParser(xml);
    	java.util.Hashtable<String, String> note = new java.util.Hashtable<String, String>();
    	note.put("to", "Tove");
    	note.put("from", alphabet);
    	note.put("heading", "Reminder");
    	note.put("body", alphabet);
    	xmlp.addProperty("note", "note1", note);
    	xmlp.store();
		assertEquals(alphabet, xmlp.getSingleProperty("from"));
	}

	@Test(timeout = 4000)
	public void test_12_1256() throws Throwable {
		// test here!
		try {
			String xml = "<?xml version='1.0'?><catalog><book id='bk101'><author>Gambardella, Matthew</author><title>XML Developers Guide</title><genre>Computer</genre><price>44.95</price><publish_date>2000-10-01</publish_date><description>An in-depth look at creating applications with XML.</description></book></catalog>";
			XMLParser xmlp = new XMLParser(xml);
			String xml2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><catalog><book id=\"bk101\"><author>Gambardella, Matthew</author><title>XML Developers Guide</title><genre>Computer</genre><price>44.95</price><publish_date>2000-10-01</publish_date><description>An in-depth look at creating applications with XML.</description></book><book id=\"100\"><two>2</two><one>1</one><three>3</three></book></catalog>";
			java.util.Hashtable<String, String> numbers = new java.util.Hashtable<String, String>();
			numbers.put("one", "1");
			numbers.put("two", "2");
			numbers.put("three", "3");
			xmlp.addProperty("book", "100", numbers);
			xmlp.store();
			//System.out.println(xmlp.getXML());

			assertEquals(true, xml2.equals(xmlp.getXML()));
			assertEquals(xml2, xmlp.getXML());
			// fail("Expected an Exception to be thrown");
		} catch (Exception e) {
			e.printStackTrace();
			// assertEquals(e.getMessage(), new
			// TransformerConfigurationException().getMessage());
		}

	}

	@Test(timeout = 4000)
	public void test_13_1257() throws Throwable {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijlkmnopqrstuvqxyz !@#$%^*_+1234567890-=[];\'\\:\",./><?`~";
		String xml = "<?xml version='1.0' encoding='UTF-8'?><root></root>";
    	XMLParser xmlp = new XMLParser(xml);
    	java.util.Hashtable<String, String> note = new java.util.Hashtable<String, String>();
    	note.put("to", "Tove");
    	note.put("from", "Jani");
    	note.put("heading", "Reminder");
    	note.put("body", alphabet);
    	xmlp.addProperty("note", "note1", note);
    	xmlp.store();
		assertEquals("", xmlp.getSingleProperty("notthere"));
	}

	@Test(timeout = 4000)
	public void test_14_1258() throws Throwable {
		// test here!
					String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><catalog><book id=\"bk101\"><author>Gambardella, Matthew</author><title>XML Developers Guide</title><genre>Computer</genre><price>44.95</price><publish_date>2000-10-01</publish_date><description>An in-depth look at creating applications with XML.</description></book><book id=\"100\"><two>2</two><one>1</one><three>3</three></book></catalog>";

			XMLParser xmlp = new XMLParser(xml);
			java.util.Hashtable<String, String> numbers = new java.util.Hashtable<String, String>();
			numbers.put("one", "1");
			numbers.put("two", "2");
			numbers.put("three", "3");
			xmlp.addProperty("book", "100", numbers);
			xmlp.store();
			//System.out.println(xmlp.getSingleProperty("author"));
			//System.out.println(xmlp.getXML());

			assertEquals(true, "Gambardella, Matthew".equals(xmlp.getSingleProperty("author")));
			assertEquals("Gambardella, Matthew", xmlp.getSingleProperty("author"));
	}

	@Test(timeout = 4000)
	public void test_15_1259() throws Throwable {
		// test here!
					String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><catalog><book id=\"bk101\"><author>Gambardella, Matthew</author><title>XML Developers Guide</title><genre>Computer</genre><price>44.95</price><publish_date>2000-10-01</publish_date><description>An in-depth look at creating applications with XML.</description></book><book id=\"100\"><two>2</two><one>1</one><three>3</three></book></catalog>";

			XMLParser xmlp = new XMLParser(xml);
			java.util.Hashtable<String, String> numbers = new java.util.Hashtable<String, String>();
			numbers.put("one", "1");
			numbers.put("two", "2");
			numbers.put("three", "3");
			xmlp.addProperty("book", "100", numbers);
			xmlp.store();
			//System.out.println(xmlp.getSingleProperty("author"));
			//System.out.println(xmlp.getXML());

			assertEquals(true, "Gambardella, Matthew".equals(xmlp.getSingleProperty("author")));
			//assertEquals("Gambardella, Matthew", xmlp.getSingleProperty("author"));
			assertEquals("Gambardella, Matthew".length(), xmlp.getSingleProperty("author").length());
	}

	@Test(timeout = 4000)
	public void test_16_1260() throws Throwable {
		// test here!
					String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><catalog><book id=\"bk101\"><author>Gambardella, Matthew</author><title>XML Developers Guide</title><genre>Computer</genre><price>44.95</price><publish_date>2000-10-01</publish_date><description>An in-depth look at creating applications with XML.</description></book><book id=\"100\"><two>2</two><one>1</one><three>3</three></book></catalog>";
			String xml2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><catalog><book id=\"bk101\"><author>Gambardella, Matthew</author><title>XML Developers Guide</title><genre>Computer</genre><price>44.95</price><publish_date>2000-10-01</publish_date><description>An in-depth look at creating applications with XML.</description></book><book id=\"100\"><two>2</two><one>1</one><three>3</three></book></catalog>";

			XMLParser xmlp = new XMLParser(xml);
			java.util.Hashtable<String, String> numbers = new java.util.Hashtable<String, String>();
			numbers.put("one", "1");
			numbers.put("two", "2");
			numbers.put("three", "3");
			xmlp.addProperty("book", "100", numbers);
			xmlp.store();
			//System.out.println(xmlp.getSingleProperty("author"));
			//System.out.println(xmlp.getXML());
			//System.out.println(xmlp.returnSpecial(xml));
			//System.out.println(xml2);
			assertEquals(true, xml2.equals(xmlp.returnSpecial(xml)));
			assertEquals(xml2, xmlp.returnSpecial(xml));

	}

	@Test(timeout = 4000)
	public void test_17_1261() throws Throwable {
		    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijlkmnopqrstuvqxyz !@#$%^*_+1234567890-=[];\'\\:\",./><?`~";
    String xml = "<?xml version='1.0' encoding='UTF-8'?><root><firstElement><childElement><key>Value</key><key2>Value2</key2><key3>Value3</key3></childElement></firstElement></root>";
    XMLParser xmlp = new XMLParser(xml);
    java.util.Hashtable<String, String> note = new java.util.Hashtable<String, String>();
    note.put("to", "Tove");
    note.put("from", "Jani");
    note.put("heading", "Reminder");
    note.put("body", alphabet);
    xmlp.addProperty("note", "note1", note);
    xmlp.store();
    assertEquals("", xmlp.getSingleProperty("firstElement"));
	}

	@Test(timeout = 4000)
	public void test_18_1263() throws Throwable {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijlkmnopqrstuvqxyz !@#$%^*_+1234567890-=[];\'\\:\",./&lt;&gt;?`~";
	    String xml = "<?xml version='1.0' encoding='UTF-8'?><root></root>";
	    XMLParser xmlp = new XMLParser(xml);
	    java.util.Hashtable<String, String> note = new java.util.Hashtable<String, String>();
	    note.put("to", "Tove");
	    note.put("from", "Jani");
	    note.put("heading", "Reminder");
	    note.put("body", alphabet);
	    xmlp.addProperty("note", "note1", note);
	    note.put("body", "hello");
	    xmlp.addProperty("note", "note2", note);
	    xmlp.store();
 	   assertEquals("hello", xmlp.getSingleProperty("body"));
	}

	@Test(timeout = 4000)
	public void test_19_1265() throws Throwable {
	    String xml = "<?xml version='1.0' encoding='UTF-8'?><root></root>";
    	XMLParser xmlp = new XMLParser(xml);
    	java.util.Hashtable<String, String> note = new java.util.Hashtable<String, String>();
  	 	note.put("id", "4");
   		note.put("to", "Tove");
  	 	note.put("from", "Jani");
  	    note.put("heading", "Reminder");
		note.put("body", "hello");
		xmlp.setProperty("note", "note1", note);
 	   	xmlp.store();
		assertEquals(xmlp.getXML(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><note id=\"note1\"><body>hello</body><heading>Reminder</heading><from>Jani</from><to>Tove</to></note></root>");
	}

	@Test(timeout = 4000)
	public void test_20_1266() throws Throwable {
		String xml = "<?xml version='1.0' encoding='UTF-8'?><root></root>";
		XMLParser xmlp = new XMLParser(xml);
		java.util.Hashtable<String, String> note = new java.util.Hashtable<String, String>();
		note.put("id", "4");
		note.put("to", "Tove");
		note.put("from", "Jani");
		note.put("heading", "Reminder");
		note.put("body", "hello");
		xmlp.addProperty("note", "note1", note);
		note.put("from", "Steve");
		xmlp.addProperty("note", "note2", note);
		note.put("body", "new value");
		xmlp.setProperty("note", "note1", note);
		xmlp.store();
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><note id=\"note1\"><body>new value</body><heading>Reminder</heading><from>Steve</from><to>Tove</to></note><note id=\"note2\"><body>hello</body><heading>Reminder</heading><from>Steve</from><to>Tove</to><id>4</id></note></root>", xmlp.getXML());
	}

	@Test(timeout = 4000)
	public void test_21_1267() throws Throwable {
		String xml = "<?xml version='1.0' encoding='UTF-8'?><root></root>";
		XMLParser xmlp = new XMLParser(xml);
		java.util.Hashtable<String, String> note = new java.util.Hashtable<String, String>();
		note.put("id", "4");
		note.put("to", "Tove");
		note.put("from", "Jani");
		note.put("heading", "Reminder");
		note.put("body", "hello");
		xmlp.addProperty("note", "Note1", note);
		note.put("from", "Steve");
		xmlp.addProperty("note", "nOtE2  ", note);
		note.put("body", "new value");
		xmlp.setProperty("note", "note1", note);
		note.put("to", "Dave");
		xmlp.setProperty("note", "nOtE2  ", note);
		xmlp.store();
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><note id=\"note1\"><body>new value</body><heading>Reminder</heading><from>Steve</from><to>Tove</to></note><note id=\"nOtE2  \"><body>new value</body><heading>Reminder</heading><from>Steve</from><to>Dave</to></note></root>", xmlp.getXML());
	}

	@Test(timeout = 4000)
	public void test_22_1268() throws Throwable {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijlkmnopqrstuvqxyz !@#$%^*_+1234567890-=[];\'\\:\",./&gt;&lt;?`~";
		String xml = "<?xml version='1.0' encoding='UTF-8'?><root></root>";
		XMLParser xmlp = new XMLParser(xml);
		java.util.Hashtable<String, String> note = new java.util.Hashtable<String, String>();
		note.put("to", "Tove");
		note.put("from", "Jani");
		note.put("heading", alphabet);
		note.put("body", "hello");
		xmlp.addProperty("note", "Note1", note);
		note.put("from", "Steve");
		xmlp.addProperty("note", "nOtE2  ", note);
		note.put("body", "new value");
		xmlp.setProperty("note", "note1", note);
		note.put("to", "Dave");
		xmlp.setProperty("note", "nOtE2  ", note);
		xmlp.store();
		assertEquals("[{body=new value, heading=ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijlkmnopqrstuvqxyz !@#$%^*_+1234567890-=[];\'\\:\",./&gt;&lt;?`~, to=Tove, from=Steve, id=note1}, {body=new value, heading=ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijlkmnopqrstuvqxyz !@#$%^*_+1234567890-=[];\'\\:\",./&gt;&lt;?`~, to=Dave, from=Steve, id=nOtE2  }]",xmlp.getProperty("note").toString());
	}

	@Test(timeout = 4000)
	public void test_23_1269() throws Throwable {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijlkmnopqrstuvqxyz !@#$%^*_+1234567890-=[];\'\\:\",./><?`~";
		String xml = "<?xml version='1.0' encoding='UTF-8'?><root></root>";
		XMLParser xmlp = new XMLParser(xml);
		java.util.Hashtable<String, String> note = new java.util.Hashtable<String, String>();
		note.put("to", "tove    ");
		note.put("from", "Jani");
		note.put("heading", alphabet);
		note.put("body", "hello");
		xmlp.addProperty("note", "Note1", note);
		note.put("from", "Steve");
		xmlp.addProperty("note", "nOtE2  ", note);
		note.put("body", "new value");
		xmlp.setProperty("note", "note1", note);
		note.put("to", "Dave");
		xmlp.setProperty("note", "nOtE2  ", note);
		xmlp.store();
		assertEquals("[{body=new value, heading=ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijlkmnopqrstuvqxyz !@#$%^*_+1234567890-=[];\'\\:\",./><?`~, to=tove, from=Steve, id=note1}, {body=new value, heading=ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijlkmnopqrstuvqxyz !@#$%^*_+1234567890-=[];\'\\:\",./><?`~, to=Dave, from=Steve, id=nOtE2  }]",xmlp.getProperty("note").toString());
	
	}

	@Test(timeout = 4000)
	public void test_24_1270() throws Throwable {
		String xml = "<?xml version='1.0' encoding='UTF-8'?><root></root>";
		XMLParser xmlp = new XMLParser(xml);
		java.util.Hashtable<String, String> note = new java.util.Hashtable<String, String>();
		note.put("to", "Tove    ");
		note.put("from", "Jani");
		note.put("heading", "Reminder!");
		note.put("body", "hello");
		xmlp.addProperty("note", "note1", note);
		note.put("from", "Steve");
		xmlp.setProperty("note", "note1", note);
		xmlp.store();
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><note id=\"note1\"><body>hello</body><heading>Reminder!</heading><from>Steve</from><to>Tove    </to></note></root>", xmlp.getXML());
		note.put("to", "Dave");
		xmlp.addProperty("note", "note2", note);
		note.put("body", "new value");
		xmlp.setProperty("note", "note2", note);
		xmlp.store();
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><note id=\"note1\"><body>hello</body><heading>Reminder!</heading><from>Steve</from><to>Tove    </to></note><note id=\"note2\"><body>new value</body><heading>Reminder!</heading><from>Steve</from><to>Dave</to></note></root>", xmlp.getXML());
	}

}
