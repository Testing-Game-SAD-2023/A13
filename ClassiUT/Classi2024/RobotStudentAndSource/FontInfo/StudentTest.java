package student.alessiaMannaUno;
import ClassUnderTest.*;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Font;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


/*Inizio: 13/11/2023, 16.30
  Fine: 13/11/2023, 20.15
*/

class StudentTest {
	
	private FontInfo fontInfo;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	//Setup of a FontInfo object common to the majority of the tests
	void setUp() throws Exception {
		fontInfo = new FontInfo();
		fontInfo.setFamily("Arial");
		fontInfo.setIsBold(false);
		fontInfo.setIsItalic(true);
		fontInfo.setSize(12);
		
	}

	@AfterEach
	//tearDown of the previously set FontInfo object
	void tearDown() throws Exception {
		fontInfo = null;
	}

	@Test
	//Test to ensure that the hashCode method work properly. It checks that two FontInfo objects that share the same properties generate the same hash code. 
	void testHashCode() {
		FontInfo fontInfo_2 = new FontInfo();
		
		fontInfo_2.setFamily("Arial");
		fontInfo_2.setIsBold(false);
		fontInfo_2.setIsItalic(true);
		fontInfo_2.setSize(12);
		
		//assertTrue(fontInfo.equals(fontInfo_2) && fontInfo_2.equals(fontInfo));
		assertEquals(fontInfo.hashCode(), fontInfo_2.hashCode());
	}
	
	@ParameterizedTest
	@CsvSource({"Calibri, false, true, 12", "Arial, true, true, 12", "Arial, false, false, 12", "Arial, false, true, 20", "Calibri, true, false, 36"})
	////Test that checks that two FontInfo objects with different properties generate different hash codes. Different combinations of the properties are checked.
	void testHashCodeNotSame(String fontTest, boolean fontBold, boolean fontItalic, int fontSize) {
		FontInfo fontInfo_2 = new FontInfo();
		fontInfo_2.setFamily(fontTest);
		fontInfo_2.setIsBold(fontBold);
		fontInfo_2.setIsItalic(fontItalic);
		fontInfo_2.setSize(fontSize);
		
		assertNotEquals(fontInfo.hashCode(), fontInfo_2.hashCode());
	
	}

	@Test
	//Test to ensure that the no argument constructor works properly. It should create a FontInfo instance with default values,
	//so the test checks if the default values specified in the constructor are correctly assigned to the newly created object.
	void testFontInfo() {
		FontInfo newFontInfo = new FontInfo();
		assertEquals("Monospaced", newFontInfo.getFamily());
		assertEquals(12, newFontInfo.getSize());
	}

	@Test
	//Test to ensure that the constructor correctly creates a FontInfo instance by passing a Font object as a parameter,
	//The test checks if the properties of the object are set according to the passed Font object.
	void testFontInfoFont() {
		Font exampleFont = new Font("Calibri", Font.ITALIC, 13);
		FontInfo newFontInfo = null;
		newFontInfo = new FontInfo(exampleFont);
		assertNotNull(newFontInfo);
		assertEquals("Calibri", newFontInfo.getFamily());
		assertTrue(newFontInfo.isItalic());
		assertFalse(newFontInfo.isBold());
		assertEquals(13, newFontInfo.getSize());
		assertTrue(newFontInfo.doesFontMatch(exampleFont));
	}
	
	@Test
	//Test to ensure that the constructor throws an IllegalArgumentException in case a null Font object is passed.
	void testFontInfoFontNull() {
		Font exampleFont = null;
		assertThrows("Doveva dare IllegalArgumentException", IllegalArgumentException.class, ()->new FontInfo(exampleFont));
	}
	
	
	@Test
	//Test to verift the cloning functionality of FontInfo instances. A copy of a FontInfo object is created, the test then
	//checks if the cloned object is a different instance of the FontInfo class that shares the same properties as the original one.
	void testClone() {
		FontInfo clonedFontInfo = (FontInfo) fontInfo.clone();
		
		assertNotSame(fontInfo, clonedFontInfo);
		assertEquals(fontInfo.getFamily(), clonedFontInfo.getFamily());
		assertEquals(fontInfo.getSize(), clonedFontInfo.getSize());
		assertEquals(fontInfo.isBold(), clonedFontInfo.isBold());
		assertEquals(fontInfo.isItalic(), clonedFontInfo.isItalic());
		assertEquals(clonedFontInfo, fontInfo);
	}

	@Test
	//Testing the getFamily() method to verify it correctly retrives the set family name.
	void testGetFamily() {
		assertEquals("Arial", fontInfo.getFamily());
	}

	@Test
	//Testing the setFamily() method to ensure it correctly sets the passed parameter.
	void testSetFamily() {
		fontInfo.setFamily("Serif");
		assertEquals("Serif", fontInfo.getFamily());
	}

	@Test
	//Testing the setFamily() method to ensure it correctly sets the default family property when a null parameter is passed.
	void testSetFamilyNull() {
		fontInfo.setFamily(null);
		assertEquals("Monospaced", fontInfo.getFamily());
	}
	
	@Test
	//Testing the isBold() method to ensure it correctly retrieves the bold style property status. 
	void testIsBold() {
		assertFalse(fontInfo.isBold());
	}

	@Test
	//Testing the setIsBold() method to ensure it correctly sets the bold style property.
	void testSetIsBold() {
		fontInfo.setIsBold(true);
		assertTrue(fontInfo.isBold());
	}

	@Test
	//Testing the isItalic() method to ensure it correctly retrieves the italic style property status. 
	void testIsItalic() {
		assertTrue(fontInfo.isItalic());
	}

	@Test
	//Testing the setIsItalic() method to ensure it correctly sets the italic style property.
	void testSetIsItalic() {
		fontInfo.setIsItalic(false);
		assertFalse(fontInfo.isItalic());
	}

	@Test
	//Testing the getSize method to ensure it correctly retrives the size of the font.
	void testGetSize() {
		assertEquals(12, fontInfo.getSize());
	}

	@Test
	//Testing the getSize method to ensure it correctly sets the passed size to the font.
	void testSetSize() {
		fontInfo.setSize(20);
		assertEquals(20, fontInfo.getSize());
	}

	@Test
	//Testing the setFont() method to ensure it correctly sets the FontInfo properties given a Font object. 
	//We set the FontInfo properties passing a Font object to the setFont method, the test checks if the 
	//FontInfo properties are correctly assigned.
	void testSetFont() {
		Font testFont = new Font("Calibri", Font.BOLD, 17);
		fontInfo.setFont(testFont);
		
		assertEquals("Calibri", fontInfo.getFamily());
		assertTrue(fontInfo.isBold());
		assertFalse(fontInfo.isItalic());
		assertEquals(17, fontInfo.getSize());
	}
	
	
	@Test
	
	//Testing the setFont() method to ensure that it throws an IllegalArgumentException if a null Font object is passed.
	void testSetFontThrows() {
		assertThrows("Doveva lanciare un IllegalArgumentException", IllegalArgumentException.class, ()->fontInfo.setFont(null));
	}

	@ParameterizedTest
	@CsvSource({
		//Same font
		"Arial, false, true, 12, true", 
		//Different font family
		"Times New Roman, false, true, 12, false",
		//Different style bold
		"Arial, true, true, 12, false",
		//Different style italic
		"Arial, false, false, 12, false",
		//Different size
		"Arial, false, true, 42, false"
	})
	
	//Test to ensure that the doesFontMatch method correctly determines whether the properties of a FontInfo object match the properties
	//of a provided Font Object. A parameterized test has been used, in order to check multiple combinations of different conditions.
	void testDoesFontMatch(String testFontFamily, boolean testFontBold, boolean testFontItalic, int testFontSize, boolean expected) {
		
		int testFontStyle = (testFontBold ? Font.BOLD : 0) | (testFontItalic ? Font.ITALIC : 0);
		
		Font exampleFont = new Font(testFontFamily, testFontStyle, testFontSize);
		//assertTrue(fontInfo.doesFontMatch(exampleFont));
		assertEquals(expected, fontInfo.doesFontMatch(exampleFont));
	}
	
	@Test
	//Test to ensure that the doesFontMatch method return a false value when a null Font object is passed.
	void testDoesFontMatchNull() {
		Font exampleFont = null;
		assertFalse(fontInfo.doesFontMatch(exampleFont));
	}


	@ParameterizedTest
	@CsvSource({"false, true, 2", "true, false, 1", "false, false, 0"})
	//Test to verify the style generation for FontInfo properties. A parameterized test has been used, in order to check
	//different combinations of values and to cover all the possible scenarios. 
	void testGenerateStyle(boolean testFontBold, boolean testFontItalic, int expectedStyle) {
		fontInfo.setIsBold(testFontBold);
		fontInfo.setIsItalic(testFontItalic);

		assertEquals(expectedStyle, fontInfo.generateStyle());
	}
	
	
	@Test
	//Test method to verify that a Font object is correctly generated from a FontInfo object using the createFont method.
	void testCreateFont() {
		Font exampleFont = new Font("Arial", Font.ITALIC, 12);
		assertEquals(exampleFont, fontInfo.createFont());
	}

	
	@ParameterizedTest
	@CsvSource({
		//Only italic
		"Arial, false, true, 12, 'Arial, 12, italic'",
		//Both bold and italic
		"Arial, true, true, 12, 'Arial, 12, bold, italic'",
		//Neither bold nor italic
		"Arial, false, false, 12, 'Arial, 12'",
		//Only bold
		"Arial, true, false, 12, 'Arial, 12, bold'"
	})
	
	//Test method to ensure that the toString function returns a string with the correct FontInfo object properties. A parameterized
	//test has been used, in order to test all the possible combinations of the values.
	void testToString(String fontFamily, boolean fontBold, boolean fontItalic, int fontSize, String expected) {
		fontInfo.setFamily(fontFamily);
		fontInfo.setIsBold(fontBold);
		fontInfo.setIsItalic(fontItalic);
		fontInfo.setSize(fontSize);
		assertEquals(expected, fontInfo.toString());
	}

	@ParameterizedTest
	@CsvSource({
		//Equal object
		"Arial, false, true, 12, true",
		//Different family
		"Calibri, false, true, 12, false", 
		//Style bold is different
		"Arial, true, true, 12, false",
		//Style italic is different
		"Arial, false, false, 12, false",
		//Size is different
		"Arial, false, true, 20, false"
	})
	
	//Test method to verify that the equals method works properly. All the possible combinations of values are tested, 
	//using a parameterized test, to ensure that all the scenarios are covered.
	void testEqualsObject(String fontFamily, boolean fontBold, boolean fontItalic, int fontSize, boolean expected) {
		
		FontInfo fontInfo_2 = new FontInfo();
		fontInfo_2.setFamily(fontFamily);
		fontInfo_2.setIsBold(fontBold);
		fontInfo_2.setIsItalic(fontItalic);
		fontInfo_2.setSize(fontSize);
		
		assertEquals(expected, fontInfo.equals(fontInfo_2));
		
	}
	
	
	@Test
	//Test to verify that the equals works properly when the same object to which it's applied is passed as a parameter
	void testEqualsObjectSameObj() {
		assertTrue(fontInfo.equals(fontInfo));
	}
	

	@Test
	//Test to verify that the equals method correctly behaves when a FontInfo instance is compared to a null object
	void testEqualsObjectNull() {
		assertFalse(fontInfo.equals(null));
	}

	
	@ParameterizedTest
	@CsvSource({"null, null", "null, Arial", "Arial, null", "Arial, Calibri"})
	//This condition can't be covered, since null value can't be assigned because of the setter behavior
	void testEqualsObjectOtherNull(String fontFamily, String testFamily) {
		
		
		FontInfo fontInfo_2 = new FontInfo();
		fontInfo_2.setFamily(testFamily);
		fontInfo.setFamily(fontFamily);
		assertFalse(fontInfo.equals(fontInfo_2));
		
	}
	
	@Test
	//Test to verify that to objects from different classes can't be equal 
	void testEqualsObjectNotSameClass() {
		 Object fontInfo_2 = new Object();
		 assertTrue(fontInfo.getClass() != fontInfo_2.getClass());
		 assertFalse(fontInfo.equals(fontInfo_2));
	 }
}
