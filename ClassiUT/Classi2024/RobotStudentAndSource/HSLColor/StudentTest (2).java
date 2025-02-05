package student.giovanniDiStazioUno;

import ClassUnderTest.HSLColor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentTest {

	
	static HSLColor color;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		color = new HSLColor();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		color = null;
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testInitHSLbyRGB() {
		color.initHSLbyRGB(255,234,112);
		assertEquals(255,color.getRed());
		assertEquals(234,color.getGreen());
		assertEquals(112,color.getBlue());
		color.initHSLbyRGB(100, 100, 100);
		assertEquals(0,color.getSaturation());
		assertEquals(170,color.getHue());
		color.initHSLbyRGB(2,3,4);
		assertTrue(color.getLuminence() <= 255/2);
		color.initHSLbyRGB(25, 255, 3);
		color.initHSLbyRGB(0, 0, 255); //la seconda branch non è raggiungibile
		color.initHSLbyRGB(200, 3, 21); //if(pHue < 0)
	}

	@Test
	void testInitRGBbyHSL() {
		color.initRGBbyHSL(23, 25, 255);
		assertEquals(23,color.getHue());
		assertEquals(255,color.getLuminence());
		assertEquals(25,color.getSaturation());
		color.initRGBbyHSL(11, 0, 100);
		assertEquals(100,color.getRed());
		assertEquals(100,color.getGreen());
		assertEquals(100,color.getBlue());
		color.initRGBbyHSL(2, 150, 1);
		color.initRGBbyHSL(3,127,2);
		color.initRGBbyHSL(888, 666, 777);
		color.initRGBbyHSL(150, 33, 4);
	}

	@Test
	void testGetHue() {
		color.initRGBbyHSL(1, 1, 1);
		assertEquals(1,color.getHue());
	}

	@Test
	void testSetHue() {
		color.setHue(-1);
		color.setHue(257);
		color.setHue(11);
		
	}

	@Test
	void testGetSaturation() {
		color.initRGBbyHSL(1, 1, 1);
		assertEquals(1,color.getSaturation());
	}

	@Test
	void testSetSaturation() {
		color.setSaturation(-1);
		color.setSaturation(257);
		color.setSaturation(11);		
	}

	@Test
	void testGetLuminence() {
		color.initRGBbyHSL(1, 1, 1);
		assertEquals(1,color.getLuminence());
	}

	@Test
	void testSetLuminence() {
		color.setLuminence(-1);
		color.setLuminence(257);
		color.setLuminence(11);
	}

	@Test
	void testGetRed() {
		color.initHSLbyRGB(255,255,255);
		assertEquals(255,color.getRed());
	}

	@Test
	void testGetGreen() {
		color.initHSLbyRGB(255,255,255);
		assertEquals(255,color.getGreen());
	}

	@Test
	void testGetBlue() {
		color.initHSLbyRGB(255,255,255);
		assertEquals(255,color.getBlue());
	}

	@Test
	void testReverseColor() {
		color.reverseColor();
	}

	@Test
	void testBrighten() {
		color.brighten(0);
		color.setLuminence(-1);
		color.brighten(15);
		color.setLuminence(257);
		color.brighten(11);
	}

	@Test
	void testBlend() {
		color.blend(11, 10, 20, 30);
		color.blend(1, 4, 88, 1);
		color.blend(1, 0, 4, 0);
		color.blend(3, 5, 1, -2);
		color.blend(2,11,2,(float)0.4);
	}

	
}
