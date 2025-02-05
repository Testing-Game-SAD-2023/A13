package student.vincenzoCiccarelliDue;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;





import ClassUnderTest.HSLColor;

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
	void testInitHSLbyRGB() {
		HSLColor color = new HSLColor();
		color.initHSLbyRGB(1, 2, 3);
		assertNotNull(color);
	}
	
	@Test
	void testInitHSLbyRGB2() {
		HSLColor color = new HSLColor();
		color.initHSLbyRGB(1, 1, 1);
		assertNotNull(color);
	}
	
	@Test
	void testInitHSLbyRGB3() {
		HSLColor color = new HSLColor();
		color.initHSLbyRGB(100, 255 , 170);
		assertNotNull(color);
	}
	
	@Test
	void testInitHSLbyRGB4() {
		HSLColor color = new HSLColor();
		color.initHSLbyRGB(255, 100, 170);
		assertNotNull(color);
	}
	
	
	@Test
	void testInitRGBbyHSL() {
		HSLColor color = new HSLColor();
		color.initRGBbyHSL(130, 0, 200);
		assertNotNull(color);
	}

	@Test
	void testInitRGBbyHSL2() {
		HSLColor color = new HSLColor();
		color.initRGBbyHSL(130, 20, 200);
		assertNotNull(color);
	}
	
	@Test
	void testInitRGBbyHSL3() {
		HSLColor color = new HSLColor();
		color.initRGBbyHSL(130, 20, 120);
		assertNotNull(color);
	}
	
	
	@Test
	void testGetHue() {
		HSLColor color = new HSLColor();
		color.initRGBbyHSL(130, 20, 200);
		assertEquals(color.getHue(),130);
	}

	@Test
	void testSetHue() {
		HSLColor color = new HSLColor();
		color.setHue(-1);
		assertEquals(color.getHue(),254);
	}
	
	@Test
	void testSetHue2() {
		HSLColor color = new HSLColor();
		color.setHue(280);
		assertEquals(color.getHue(),25);
	}

	@Test
	void testGetSaturation() {
		HSLColor color = new HSLColor();
		color.initRGBbyHSL(120, -200, 50);
		assertEquals(color.getSaturation(),-200);
	}

	@Test
	void testSetSaturation() {
		HSLColor color = new HSLColor();
		color.setSaturation(280);
		assertEquals(color.getSaturation(),255);
	}
	
	@Test
	void testSetSaturation2() {
		HSLColor color = new HSLColor();
		color.setSaturation(-20);
		assertEquals(color.getSaturation(),0);
	}
	
	@Test
	void testSetSaturation3() {
		HSLColor color = new HSLColor();
		color.setSaturation(255);
		assertEquals(color.getSaturation(),255);
	}

	@Test
	void testGetLuminence() {
		HSLColor color = new HSLColor();
		color.initRGBbyHSL(120, -200, 50);
		assertEquals(color.getLuminence(),50);
	}

	@Test
	void testSetLuminence() {
		HSLColor color = new HSLColor();
		color.setLuminence(-20);
		assertEquals(color.getLuminence(),0);
	}
	
	@Test
	void testSetLuminence2() {
		HSLColor color = new HSLColor();
		color.setLuminence(280);
		assertEquals(color.getLuminence(),255);
	}
	
	@Test
	void testSetLuminence3() {
		HSLColor color = new HSLColor();
		color.setLuminence(255);
		assertEquals(color.getLuminence(),255);
	}

	@Test
	void testGetRed() {
		HSLColor color = new HSLColor();
		color.initRGBbyHSL(120, -200, 50);
		assertEquals(color.getRed(),89);
	}

	@Test
	void testGetGreen() {
		HSLColor color = new HSLColor();
		color.initHSLbyRGB(1, 2, 3);
		assertEquals(color.getGreen(),2);
	}

	@Test
	void testGetBlue() {
		HSLColor color = new HSLColor();
		color.initHSLbyRGB(10, 20, 35);
		assertEquals(color.getBlue(),35);
	}

	@Test
	void testReverseColor() {
		HSLColor color = new HSLColor();
		color.initRGBbyHSL(10, -30, 120);
		color.reverseColor();
		assertEquals(color.getHue(),137);
	}

	@Test
	void testBrighten() {
		HSLColor color = new HSLColor();
		color.initRGBbyHSL(-210, 190, 80);
		color.brighten(0);
		assertEquals(color.getLuminence(),80);
	}
	
	@Test
	void testBrighten2() {
		HSLColor color = new HSLColor();
		color.initRGBbyHSL(-210, 190, 80);
		color.brighten(10);
		assertEquals(color.getLuminence(),255);
	}
	
	@Test
	void testBrighten3() {
		HSLColor color = new HSLColor();
		color.initRGBbyHSL(-210, 190, 80);
		color.brighten(-10);
		assertEquals(color.getLuminence(),0);
	}

	
	@ParameterizedTest
	@CsvSource({
		"50,60,30,2",
		"50,60,30,0",
		"10,20,50,-2.90",
		"-12,67,80,0.80"
	})
	void testBlend(int r, int g, int b, float per) {
		HSLColor color = new HSLColor();
		color.blend(r, g, b, per);
		assertNotNull(color);
	}
	
	@Test
	void testInitRGBbyHSL4() {
		HSLColor color = new HSLColor();
		color.initRGBbyHSL(130, 20, 260);
		assertNotNull(color);
	}
	

}
