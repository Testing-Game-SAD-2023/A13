package student.lucaAntonioScollettaDue;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.FontInfo;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;
import java.awt.Font;

class StudentTest {

	FontInfo test;
	
	//	Test HashCode
	@Test
	void testHashCode1() {
		test = new FontInfo();
		assertThat(test.hashCode(), equalTo(-1059600276));
	}
	
	@Test
	void testHashCode2() {
		test = new FontInfo();
		test.setIsBold(true);
		assertThat(test.hashCode(), equalTo(-1059606042));
	}
	
	@Test
	void testHashCode3() {
		test = new FontInfo();
		test.setIsItalic(true);
		assertThat(test.hashCode(), equalTo(-1059600462));
	}

	//	Test costruttore di default
	@Test
	void testFontInfo() {
		test = new FontInfo();
		assertThat(test.getFamily(), equalTo("Monospaced"));
		assertThat(test.getSize(), equalTo(12));
	}
	
	//	Test costruttore con parametro
	@Test
	void testFontInfoFont() {
		Font font = new Font("Arial", Font.BOLD, 16);
		test = new FontInfo(font);
		assertThat(test.getFamily(), equalTo("Arial"));
		assertThat(test.isBold(), equalTo(true));
		assertThat(test.getSize(), equalTo(16));
	}
	
	@Test
	void testFontInfoFontError() {
		Exception exception = assertThrows(Exception.class, () -> test = new FontInfo(null));
		assertThat(exception.getMessage(), equalTo("Null Font passed"));
	}

	//	Test clone
	@Test
	void testClone() {
		test = new FontInfo();
		FontInfo cloned = (FontInfo) test.clone();
		assertThat(cloned, equalTo(test));
	}

	//	Test Set Family
	@Test
	void testSetFamily() {
		test = new FontInfo();
		test.setFamily("Times New Roman");
		assertThat(test.getFamily(), equalTo("Times New Roman"));
	}
	
	@Test
	void testSetFamilyDefault() {
		test = new FontInfo(new Font("Arial", Font.BOLD, 16));
		test.setFamily(null);
		assertThat(test.getFamily(), equalTo("Monospaced"));
	}

	//	Test SetIsBold
	@Test
	void testSetIsBold() {
		test = new FontInfo();
		test.setIsBold(false);
		assertThat(test.isBold(), equalTo(false));
	}
	
	@Test
	void testSetIsItalic() {
		test = new FontInfo();
		test.setIsItalic(false);
		assertThat(test.isItalic(), equalTo(false));
	}

	//	Test SetSize
	@Test
	void testSetSize() {
		test = new FontInfo();
		test.setSize(27);
		assertThat(test.getSize(), equalTo(27));
	}
	
	@Test
	void testSetSizeNegative() {
		test = new FontInfo();
		test.setSize(-8);
		assertThat(test.getSize(), equalTo(-8));
	}

	//	Test SetFont
	@Test
	void testSetFont() {
		test = new FontInfo();
		test.setFont(new Font("Arial", Font.BOLD, 16));
		assertThat(test.getFamily(), equalTo("Arial"));
		assertThat(test.isBold(), equalTo(true));
		assertThat(test.getSize(), equalTo(16));
	}
	
	@Test
	void testSetFontError() {
		test = new FontInfo();
		Exception exception = assertThrows(Exception.class, () -> test.setFont(null));
		assertThat(exception.getMessage(), equalTo("Null Font passed"));
	}

	//	Test FontMatch
	@Test
	void testDoesFontMatch() {
		test = new FontInfo(new Font("Times New Roman", Font.ITALIC, 14));
		Font testFont = new Font("Times New Roman", Font.ITALIC, 14);
		assertThat(test.doesFontMatch(testFont), equalTo(true));
	}
	
	@Test
	void testDoesNotFontMatch1() {
		test = new FontInfo(new Font("Times New Roman", Font.ITALIC, 14));
		Font testFont = new Font("Arial", Font.BOLD, 12);
		assertThat(test.doesFontMatch(testFont), equalTo(false));
	}
	
	@Test
	void testDoesNotFontMatch2() {
		test = new FontInfo(new Font("Times New Roman", Font.ITALIC, 14));
		Font testFont = new Font("Times New Roman", Font.BOLD, 12);
		assertThat(test.doesFontMatch(testFont), equalTo(false));
	}
	
	@Test
	void testDoesNotFontMatch3() {
		test = new FontInfo(new Font("Times New Roman", Font.ITALIC, 14));
		Font testFont = new Font("Times New Roman", Font.BOLD, 14);
		assertThat(test.doesFontMatch(testFont), equalTo(false));
	}
	
	@Test
	void testDoesFontMatchError() {
		test = new FontInfo(new Font("Times New Roman", Font.ITALIC, 14));
		assertThat(test.doesFontMatch(null), equalTo(false));
	}

	//	Test GenerateStyle
	@Test
	void testGenerateStyle1() {
		test = new FontInfo();
		test.setIsBold(false);
		test.setIsItalic(false);
		assertThat(test.generateStyle(), equalTo(Font.PLAIN));
	}
	
	@Test
	void testGenerateStyle2() {
		test = new FontInfo();
		test.setIsBold(true);
		assertThat(test.generateStyle(), equalTo(Font.BOLD));
	}
	
	@Test
	void testGenerateStyle3() {
		test = new FontInfo();
		test.setIsItalic(true);
		assertThat(test.generateStyle(), equalTo(Font.ITALIC));
	}

	//	Test CreateFont
	@Test
	void testCreateFont() {
		test = new FontInfo();
		assertThat(test.createFont(), equalTo(new Font("Monospaced", Font.PLAIN, 12)));
	}

	//	Test ToString
	@Test
	void testToString1() {
		test = new FontInfo();
		test.setIsBold(true);
		assertThat(test.toString(), equalTo("Monospaced, 12, bold"));
	}
	
	@Test
	void testToString2() {
		test = new FontInfo();
		test.setIsItalic(true);
		assertThat(test.toString(), equalTo("Monospaced, 12, italic"));
	}

	@Test
	void testEqualsObject1() {
		test = new FontInfo();
		FontInfo copy = test;
		assertThat(test.equals(copy), equalTo(true));
	}
	
	@Test
	void testEqualsObject2() {
		test = new FontInfo();
		assertThat(test.equals(null), equalTo(false));
	}
	
	@Test
	void testEqualsObject3() {
		test = new FontInfo();
		String notSameType = "prova";
		assertThat(test.equals(notSameType), equalTo(false));
	}
	
	@Test
	void testEqualsObject4() {
		test = new FontInfo(new Font("Times New Roman", Font.ITALIC, 14));
		FontInfo copy = new FontInfo(new Font("Arial", Font.ITALIC, 14));
		assertThat(test.equals(copy), equalTo(false));
	}
	
	@Test
	void testEqualsObject5() {
		test = new FontInfo(new Font("Times New Roman", Font.BOLD, 14));
		FontInfo copy = new FontInfo(new Font("Times New Roman", Font.ITALIC, 14));
		assertThat(test.equals(copy), equalTo(false));
	}
	
	@Test
	void testEqualsObject6() {
		test = new FontInfo(new Font("Times New Roman", Font.ITALIC, 14));
		FontInfo copy = new FontInfo(new Font("Times New Roman", Font.PLAIN, 14));
		assertThat(test.equals(copy), equalTo(false));
	}
	
	@Test
	void testEqualsObject7() {
		test = new FontInfo(new Font("Times New Roman", Font.ITALIC, 14));
		FontInfo copy = new FontInfo(new Font("Times New Roman", Font.ITALIC, 15));
		assertThat(test.equals(copy), equalTo(false));
	}
	
	@Test
	void testEqualsObject8() {
		test = new FontInfo(new Font("Times New Roman", Font.ITALIC, 14));
		FontInfo copy = new FontInfo(new Font("Times New Roman", Font.ITALIC, 14));
		assertThat(test.equals(copy), equalTo(true));
	}

}
