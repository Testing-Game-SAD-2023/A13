package student.claudioSpasianoUno;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.HSLColor;

class StudentTest {
	
	private HSLColor x;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		
		x = new HSLColor();
		assertNotNull(x);
	}

	@AfterEach
	void tearDown() throws Exception {
		x=null;
		assertNull(x);
	}


	@Test
	void testInitHSLbyRGB() {    //Test per testarlo con valori normali (GMAX)
		x.initHSLbyRGB(106,191, 64);
		assertEquals(106,x.getRed());
		assertEquals(191,x.getGreen());
		assertEquals(64,x.getBlue());
		assertEquals(71,x.getHue());
		assertEquals(127,x.getSaturation());
		assertEquals(128,x.getLuminence());
	}
	
	
	@Test
	void testInitHSLbyRGB_With_RMAX() { //Test per ricadere nella condizione in cui RMAX è quello max
		x.initHSLbyRGB(100, 40,20);		
	}
	
	
	@Test
	void testInitHSLbyRGB_With_BMAX() //Test per ricadere nella condizione in cui RMAX è quello max
	{
		x.initHSLbyRGB(12, 20, 40);
	}
	
	@Test
	void testInitHSLbyRGB_WithCMAXequalCMin() { //Test per ricadere nella condizione in cui CMAX==CMIN
		x.initHSLbyRGB(70,70,70);	
	}
	
	@Test
	void testInitRGBbyHSL() {					//Test classico
		x.initRGBbyHSL(10,100,50);
		assertEquals(10,x.getHue());
		assertEquals(100,x.getSaturation());
		assertEquals(50,x.getLuminence());
		assertEquals(70,x.getRed());
		assertEquals(40,x.getGreen());
		assertEquals(30,x.getBlue());
		
	}
	
	@Test 
	void testInitRGBbyHSL_Over_Limits() {
		x.initRGBbyHSL(300,301,302);
		assertEquals(300,x.getHue());
		assertEquals(301,x.getSaturation());
		assertEquals(302,x.getLuminence());
	}
	
		
	@Test
	void testGetHue() {
		x.initRGBbyHSL(100, 0, 0);
		assertEquals(100,x.getHue());
	}
	
	@Test
	void testSetHue() {
		int valore=10;
		x.setHue(valore);
		assertEquals(valore,x.getHue());
	}
	
	@Test
	void testSetHue_HueLessThan0() {
		int valore=-3;
		x.setHue(valore);
		assertEquals(255-3,x.getHue());	
	}

	@Test
	void testSetHue_HueMoreThanLimit() {
		int valore=270;
		x.setHue(valore);
		assertEquals(valore-255,x.getHue());
		
	}

	@Test
	void testGetSaturation() {
		x.initRGBbyHSL(25, 50, 75);
		assertEquals(50,x.getSaturation());
	}

	@Test
	void testSetSaturation() {
		int valore = 12;
		x.setSaturation(valore);
		assertEquals(valore,x.getSaturation());
	}
	
	@Test
	void testSetSaturation_LessThan0() {
		int valore = -10;
		x.setSaturation(valore);
		assertEquals(0,x.getSaturation());
	}
	
	@Test
	void testSetSaturation_MoreThanLimit() {
		int valore = 257;
		x.setSaturation(valore);
		assertEquals(255,x.getSaturation());
	}

	@Test
	void testGetLuminence() {
		x.initRGBbyHSL(0, 0, 16);
		assertEquals(16,x.getLuminence());	
	}

	@Test
	void testSetLuminence() {
		int valore = 14;
		x.setLuminence(valore);
		assertEquals(valore,x.getLuminence());
	}
	
	@Test
	void testSetLuminenceLessThanZero() {
		int valore = -4;
		x.setLuminence(valore);
		assertEquals(0,x.getLuminence());
	}
	
	@Test
	void testSetLuminenceMoreThanLimit() {
		int valore = 314;
		x.setLuminence(valore);
		assertEquals(255,x.getLuminence());
	}


	@Test
	void testGetRed() {
		x.initHSLbyRGB(23, 0, 0);
		assertEquals(23,x.getRed());
	}

	@Test
	void testGetGreen() {
		x.initHSLbyRGB(0, 11, 0);
		assertEquals(11,x.getGreen());
	}

	@Test
	void testGetBlue() {
		x.initHSLbyRGB(0, 0, 14);
		assertEquals(14,x.getBlue());
	}

	@Test
	void testReverseColor() {
		x.initRGBbyHSL(20, 0, 0);
		x.reverseColor();
		assertEquals(147,x.getHue());
	}

	@Test
	void testBrighten() {
		x.initHSLbyRGB(100, 30, 40); 
		x.brighten(0.5f);
	}
	
	@Test
	void testBrighten_With0_Brighten() {
		x.initHSLbyRGB(1, 3, 4);
		x.brighten(0f);
	}
	
	@Test
	void testBrighten_WithL_Less_Than_Zero() {
		x.initHSLbyRGB(200, 0, 0); 
		x.brighten(-10f);
	}
	
	@Test
	void testBrighten_WithL_More_Than_Limit() {
		x.initHSLbyRGB(255, 0, 0); 
		x.brighten(256f);
		assertEquals(255,x.getLuminence());
	}

	@Test
	void testBlend() {
		x.initHSLbyRGB(30,10,40);
		x.blend(30,10, 40, 0.5f);

		
	}
	
	@Test
	void testBlend_WithFPercentMajorThan1() {
		x.initHSLbyRGB(200,10,40);
		x.blend(200,10,40, 2f);
	}
	
	@Test
	void testBlend_WithFPercentLessThan0() {
		x.initHSLbyRGB(240,20,40);
		x.blend(240,20,40, -2f);		
	}


}
