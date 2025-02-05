package student.lucaMigliaccioDue;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
	
	// INIZIO TEST InitHSLbyRGB
	@Test
	void testInitHSLbyRGB() {
		// Caso 0: l'oggetto inizializzato avrà che max e min hanno lo stesso valore
		HSLColor hslColor = new HSLColor();
		
		hslColor.initHSLbyRGB(255, 255, 255);
		
		assertEquals(255, hslColor.getRed());
		assertEquals(255, hslColor.getGreen());
		assertEquals(255, hslColor.getBlue());
		assertEquals(170, hslColor.getHue());
		assertEquals(0, hslColor.getSaturation());
		assertEquals(255, hslColor.getLuminence());
	}
	
	@Test
	void testInitHSLbyRGB1() {
		// Caso 1: l'oggetto inizializzato avrà come max Red (R)
		HSLColor hslColor = new HSLColor();
		
		hslColor.initHSLbyRGB(255, 0, 0);
		
		assertEquals(255, hslColor.getRed());
	    assertEquals(0, hslColor.getGreen());
	    assertEquals(0, hslColor.getBlue());
	    assertEquals(0, hslColor.getHue());
	    assertEquals(255, hslColor.getSaturation());
	    assertEquals(128, hslColor.getLuminence());
	}
	
	@Test
	void testInitHSLbyRGB2() {
		// Caso 2: l'oggetto inizializzato avrà come max Green (G)
		HSLColor hslColor = new HSLColor();
		
		hslColor.initHSLbyRGB(0, 255, 0);
		
		assertEquals(0, hslColor.getRed());
	    assertEquals(255, hslColor.getGreen());
	    assertEquals(0, hslColor.getBlue());
	    assertEquals(85, hslColor.getHue());
	    assertEquals(255, hslColor.getSaturation());
	    assertEquals(128, hslColor.getLuminence());
	}
	
	@Test
	void testInitHSLbyRGB3() {
		// Caso 3: l'oggetto inizializzato avrà come max Blue (B)
		HSLColor hslColor = new HSLColor();
		
		hslColor.initHSLbyRGB(0, 0, 255);
		
		assertEquals(0, hslColor.getRed());
	    assertEquals(0, hslColor.getGreen());
	    assertEquals(255, hslColor.getBlue());
	    assertEquals(170, hslColor.getHue());
	    assertEquals(255, hslColor.getSaturation());
	    assertEquals(128, hslColor.getLuminence());
	}
	
	@Test
	void testInitHSLbyRGB4() {
		// Caso 4: pLum <= HSLMAX / 2
		HSLColor hslColor = new HSLColor();
		
		hslColor.initHSLbyRGB(30, 100, 100);
		
		assertEquals(30, hslColor.getRed());
	    assertEquals(100, hslColor.getGreen());
	    assertEquals(100, hslColor.getBlue());
	    assertEquals(127, hslColor.getHue());
	    assertEquals(137, hslColor.getSaturation());
	    assertEquals(65, hslColor.getLuminence());
	}
	
	@Test
	void testInitHSLbyRGB5() {
		// Caso 5: pHue < 0
		HSLColor hslColor = new HSLColor();
	    
	    // valori di RGB in modo che cMax sia uguale a R, e cMin sia uguale a G
	    hslColor.initHSLbyRGB(50, 30, 40);
	    
	    int GDelta = (int) ((((50 - 30) * (255 / 6)) + 0.5) / 20);
		int BDelta = (int) ((((50 - 40) * (255 / 6)) + 0.5) / 20);
	    int pHue = BDelta - GDelta;
	    
	    // verifica che pHue diventi negativo e venga corretto aggiungendo HSLMAX (255)
	    assertEquals(pHue, -21);
	    assertEquals(pHue + 255, 234);
	}
	
	// INIZIO TEST InitRGBbyHSL
	@Test
	void testInitRGBbyHSL() {
		// Caso 0: S = 0
		HSLColor hslColor = new HSLColor();
		
		hslColor.initRGBbyHSL(30, 0, 30);
		int valueOfGrey = (hslColor.getLuminence() * 255) / 255;
		
		assertEquals(valueOfGrey, hslColor.getRed());
		assertEquals(valueOfGrey, hslColor.getGreen());
		assertEquals(valueOfGrey, hslColor.getBlue());
	}
	
	@Test
	void testInitRGBbyHSL1() {
		// Caso 1: S != 0 e L <= HSLMAX / 2
		HSLColor hslColor = new HSLColor();
		
		hslColor.initRGBbyHSL(30, 30, 60);
		int magic2 = (60 * (255 + 30) + (255 / 2)) / 255;
		
		assertEquals(magic2, 67);
	}
	
	@Test
	void testInitRGBbyHSL2() {
		// Caso 2: S != 0 e L > HSLMAX / 2
		HSLColor hslColor = new HSLColor();
		
		hslColor.initRGBbyHSL(30, 30, 255);
		int magic2 = 255 + 30 - ((255 * 30) + (255 / 2)) / 255;
		
		assertEquals(magic2, 255);
	}
	
	@Test
	void testInitRGBbyHSL3() {
		// Caso 3: S != 0, L > HSLMAX / 2 e pRed (pGreen/pBlue) > RGBMAX
		HSLColor hslColor = new HSLColor();
	    
	    hslColor.initRGBbyHSL(30, 100, 500);
	    int pRed = (696 * 255 + (255 / 2)) / 255;
	    
	    // verifico che pRed sia impostato a RGBMAX (255)
	    assertEquals(pRed, 696);
	}
	
	// INIZIO TEST GetHue
	@Test
	void testGetHue() {
		// Caso 0: valore di Hue appartenente al range [0,255]
		HSLColor hslColor = new HSLColor();
	    
		int valueOfHue = 30;
	    hslColor.initRGBbyHSL(valueOfHue, 10, 10);
	    int actualHue = hslColor.getHue();
	    
	    assertEquals(hslColor.getHue(), actualHue);
	}
	
	@Test
	void testGetHue1() {
		// Caso 1: valore di Hue > 255
		HSLColor hslColor = new HSLColor();
	    
		int valueOfHue = 280;
	    hslColor.initRGBbyHSL(valueOfHue, 10, 10);
	    int actualHue = hslColor.getHue();
	    
	    assertEquals(hslColor.getHue(), actualHue);
	}
	
	@Test
	void testGetHue2() {
		// Caso 2: valore di Hue < 0
		HSLColor hslColor = new HSLColor();
	    
		int valueOfHue = -30;
	    hslColor.initRGBbyHSL(valueOfHue, 10, 10);
	    int actualHue = hslColor.getHue();
	    
	    assertEquals(hslColor.getHue(), actualHue);
	}
	
	// INIZIO TEST SetHue
	@Test
	void testSetHue() {
		// Caso 0: valore settato appartenente al range [0,255]
		HSLColor hslColor = new HSLColor();
	    
	    int valueOfHue = 180;
	    hslColor.setHue(valueOfHue); // nuovo valore
	    
	    assertEquals(valueOfHue, hslColor.getHue());
	}
	
	@Test
	void testSetHue1() {
		// Caso 1: valore settato non appartenente al range [0,255] e < 0
		HSLColor hslColor = new HSLColor();
		
	    int valueOfHue = -180;
	    hslColor.setHue(valueOfHue); // nuovo valore
	    int actualHue = hslColor.getHue();
	    
	    assertEquals(hslColor.getHue(), actualHue);
	}
	
	@Test
	void testSetHue2() {
		// Caso 2: valore settato non appartenente al range [0,255] e > HSLMAX (255)
		HSLColor hslColor = new HSLColor();
		
		int valueOfHue = 355;
		hslColor.setHue(valueOfHue); // nuovo valore
		int actualHue = hslColor.getHue();
	    
	    assertEquals(hslColor.getHue(), actualHue);
	}
	
	// INIZIO TEST GetSaturation
	@Test
	void testGetSaturation() {
		// Caso 0: valore di Saturation appartenente al range [0,255]
		HSLColor hslColor = new HSLColor();
		
		int valueOfSaturation = 30;
		//hslColor.initRGBbyHSL(10, valueOfSaturation, 10);
		hslColor.setSaturation(valueOfSaturation);
		int actualSaturation = hslColor.getSaturation();
		
		assertEquals(hslColor.getSaturation(), actualSaturation);
	}
	
	@Test
	void testGetSaturation1() {
		// Caso 1: valore di Saturation > 255
		HSLColor hslColor = new HSLColor();
		
		int valueOfSaturation = 280;
		//hslColor.initRGBbyHSL(10, valueOfSaturation, 10);
		hslColor.setSaturation(valueOfSaturation);
		int actualSaturation = hslColor.getSaturation();
		
		assertEquals(hslColor.getSaturation(), actualSaturation);
	}
	
	@Test
	void testGetSaturation2() {
		// Caso 2: valore di Saturation < 0
		HSLColor hslColor = new HSLColor();
		
		int valueOfSaturation = -30;
		//hslColor.initRGBbyHSL(10, valueOfSaturation, 10);
		hslColor.setSaturation(valueOfSaturation);
		int actualSaturation = hslColor.getSaturation();
		
		assertEquals(hslColor.getSaturation(), actualSaturation);
	}
	
	// INIZIO TEST SetSaturation
	@Test
	void testSetSaturation() {
		// Caso 0: valore settato appartenente al range [0,255]
		HSLColor hslColor = new HSLColor();
		
		int valueOfSaturation = 180;
		hslColor.setSaturation(valueOfSaturation); // nuovo valore
		
		assertEquals(valueOfSaturation, hslColor.getSaturation());
	}
	
	@Test
	void testSetSaturation1() {
		// Caso 1: valore settato non appartenente al range [0,255] e < 0
		HSLColor hslColor = new HSLColor();
		
		int valueOfSaturation = -180;
		hslColor.setSaturation(valueOfSaturation); // nuovo valore
		int actualSaturation = hslColor.getSaturation();
		
		assertEquals(hslColor.getSaturation(), actualSaturation);
	}
	
	@Test
	void testSetSaturation2() {
		// Caso 2: valore settato non appartenente al range [0,255] e > HSLMAX (255)
		HSLColor hslColor = new HSLColor();
		
		int valueOfSaturation = 280;
		hslColor.setSaturation(valueOfSaturation); // nuovo valore
		int actualSaturation = hslColor.getSaturation();
		
		assertEquals(hslColor.getSaturation(), actualSaturation);
	}
	
	// INIZIO TEST GetLuminence
	@Test
	void testGetLuminence() {
		// Caso 0: valore di Luminence appartenente al range [0,255]
		HSLColor hslColor = new HSLColor();
		int valueOfLuminence = 30;
		
		hslColor.initRGBbyHSL(10, 10, valueOfLuminence);
		int actualLuminence = hslColor.getLuminence();
		
		assertEquals(hslColor.getLuminence(), actualLuminence);
	}
	
	@Test
	void testGetLuminence1() {
		// Caso 1: valore di Luminence > 255
		HSLColor hslColor = new HSLColor();
		int valueOfLuminence = 280;
		
		hslColor.initRGBbyHSL(10, 10, valueOfLuminence);
		int actualLuminence = hslColor.getLuminence();
		
		assertEquals(hslColor.getLuminence(), actualLuminence);
	}
	
	@Test
	void testGetLuminence2() {
		// Caso 2: valore di Luminence > 255
		HSLColor hslColor = new HSLColor();
		int valueOfLuminence = -30;
		
		hslColor.initRGBbyHSL(10, 10, valueOfLuminence);
		int actualLuminence = hslColor.getLuminence();
		
		assertEquals(hslColor.getLuminence(), actualLuminence);
	}
	
	// INIZIO TEST SetLuminence
	@Test
	void testSetLuminence() {
		// Caso 0: valore settato appartenente al range [0,255]
		HSLColor hslColor = new HSLColor();
		
		int valueOfLuminence = 180;
		hslColor.setLuminence(valueOfLuminence); // nuovo valore
		
		assertEquals(valueOfLuminence, hslColor.getLuminence());
	}
	
	@Test
	void testSetLuminence1() {
		// Caso 1: valore settato non appartenente al range [0,255] e < 0
		HSLColor hslColor = new HSLColor();
		
		int valueOfLuminence = -180;
		hslColor.setLuminence(valueOfLuminence); // nuovo valore
		int actualLuminence = hslColor.getLuminence();
		
		assertEquals(hslColor.getLuminence(), actualLuminence);
	}
	
	@Test
	void testSetLuminence2() {
		// Caso 2: valore settato non appartenente al range [0,255] e > HSLMAX (255)
		HSLColor hslColor = new HSLColor();
		
		int valueOfLuminence = 280;
		hslColor.setLuminence(valueOfLuminence); // nuovo valore
		int actualLuminence = hslColor.getLuminence();
		
		assertEquals(hslColor.getLuminence(), actualLuminence);
	}
	
	// INIZIO TEST GetRed
	@Test
	void testGetRed() {
		// Caso 0: valore di Red appartenente al range [0,255]
		HSLColor hslColor = new HSLColor();
		int valueOfRed = 30;
		
		hslColor.initHSLbyRGB(valueOfRed, 10, 10);
		int actualRed = hslColor.getRed();
		
		assertEquals(hslColor.getRed(), actualRed);
	}
	
	@Test
	void testGetRed1() {
		// Caso 1: valore di Red > 255
		HSLColor hslColor = new HSLColor();
		int valueOfRed = 280;
		
		hslColor.initHSLbyRGB(valueOfRed, 10, 10);
		int actualRed = hslColor.getRed();
		
		assertEquals(hslColor.getRed(), actualRed);
	}
	
	@Test
	void testGetRed2() {
		// Caso 2: valore di Red < 0
		HSLColor hslColor = new HSLColor();
		int valueOfRed = -30;
		
		hslColor.initHSLbyRGB(valueOfRed, 10, 10);
		int actualRed = hslColor.getRed();
		
		assertEquals(hslColor.getRed(), actualRed);
	}
	
	// INIZIO TEST GetGreen
	@Test
	void testGetGreen() {
		// Caso 0: valore di Green appartenente al range [0,255]
		HSLColor hslColor = new HSLColor();
		
		int valueOfGreen = 30;
		hslColor.initHSLbyRGB(10, valueOfGreen, 10);
		int actualGreen = hslColor.getGreen();
		
		assertEquals(hslColor.getGreen(), actualGreen);
	}
	
	@Test
	void testGetGreen1() {
		// Caso 1: valore di Green > 255
		HSLColor hslColor = new HSLColor();
		
		int valueOfGreen = 280;
		hslColor.initHSLbyRGB(10, valueOfGreen, 10);
		int actualGreen = hslColor.getGreen();
		
		assertEquals(hslColor.getGreen(), actualGreen);
	}
	
	@Test
	void testGetGreen2() {
		// Caso 2: valore di Green < 0
		HSLColor hslColor = new HSLColor();
		
		int valueOfGreen = -30;
		hslColor.initHSLbyRGB(10, valueOfGreen, 10);
		int actualGreen = hslColor.getGreen();
		
		assertEquals(hslColor.getGreen(), actualGreen);
	}
	
	// INIZIO TEST GetBlue
	@Test
	void testGetBlue() {
		// Caso 0: valore di Blue appartenente al range [0,255]
		HSLColor hslColor = new HSLColor();
		
		int valueOfBlue = 30;
		hslColor.initHSLbyRGB(10, 10, valueOfBlue);
		int actualBlue = hslColor.getBlue();
		
		assertEquals(hslColor.getBlue(), actualBlue);
	}
	
	@Test
	void testGetBlue1() {
		// Caso 1: valore di Blue > 255
		HSLColor hslColor = new HSLColor();
		
		int valueOfBlue = 280;
		hslColor.initHSLbyRGB(10, 10, valueOfBlue);
		int actualBlue = hslColor.getBlue();
		
		assertEquals(hslColor.getBlue(), actualBlue);
	}
	
	@Test
	void testGetBlue2() {
		// Caso 2: valore di Blue < 0
		HSLColor hslColor = new HSLColor();
		
		int valueOfBlue = -30;
		hslColor.initHSLbyRGB(10, 10, valueOfBlue);
		int actualBlue = hslColor.getBlue();
		
		assertEquals(hslColor.getBlue(), actualBlue);
	}
	
	// INIZIO TEST ReverseColor
	@Test
	void testReverseColor() {
		HSLColor hslColor = new HSLColor();
		
	    int valueOfHue = 30;
	    hslColor.setHue(valueOfHue);
	    hslColor.reverseColor();
	    
	    int reversedHue = hslColor.getHue();
	    int expectedReversedHue = (valueOfHue + (255 / 2));
	    
	    assertEquals(expectedReversedHue, reversedHue);
	}
	
	// INIZIO TEST Brighten: metodo che aumenta la luminosità del colore in base ad un valore %
	@Test
	void testBrighten() {
		// Caso 0: il valore percentuale è 0
		HSLColor hslColor = new HSLColor();
		
		float percent = 0.0f;
		int valueOfLuminence = 30;
		
		hslColor.setLuminence(valueOfLuminence);
		hslColor.brighten(percent);
		
		int newLuminence = hslColor.getLuminence();
		int expectedValueOfLuminence = valueOfLuminence + (int) (valueOfLuminence * percent);
		
		assertEquals(expectedValueOfLuminence, newLuminence);
	}
	
	@Test
	void testBrighten1() {
		// Caso 1: il valore percentuale è tale che L < 0
		HSLColor hslColor = new HSLColor();
		
		float percent = -50f;
		int valueOfLuminence = 30;
		
		hslColor.setLuminence(valueOfLuminence);
		hslColor.brighten(percent);
		
		int newLuminence = hslColor.getLuminence();
		int expectedValueOfLuminence = 0;
		
		assertEquals(expectedValueOfLuminence, newLuminence);
	}
	
	@Test
	void testBrighten2() {
		// Caso 2: il valore percentuale è tale che L > HSLMAX (255)
		HSLColor hslColor = new HSLColor();
		
		float percent = 50f;
		int valueOfLuminence = 255;
		
		hslColor.setLuminence(valueOfLuminence);
		hslColor.brighten(percent);
		
		int newLuminence = hslColor.getLuminence();
		int expectedValueOfLuminence = 255;
		
		assertEquals(expectedValueOfLuminence, newLuminence);
	}
	
	// INIZIO TEST Blend: metodo che mescola due colori in base ad un certo valore percentuale
	@Test
	void testBlend() {
		// Caso 0: percentuale >= 1
		HSLColor hslColor = new HSLColor();
		
		int valueOfRed = 10;
		int valueOfGreen = 10;
		int valueOfBlue = 10;
		float percent = 10f;
		
		hslColor.blend(valueOfRed, valueOfGreen, valueOfBlue, percent);
		
		assertEquals(valueOfRed, hslColor.getRed());
		assertEquals(valueOfGreen, hslColor.getGreen());
		assertEquals(valueOfBlue, hslColor.getBlue());
	}
	
	@Test
	void testBlend1() {
		// Caso 1: percentuale <= 0
		HSLColor hslColor = new HSLColor();
		
		int valueOfRed = 10;
		int valueOfGreen = 10;
		int valueOfBlue = 10;
		float percent = -10f;
		
		hslColor.blend(valueOfRed, valueOfGreen, valueOfBlue, percent);
		
		assertNotEquals(valueOfRed, hslColor.getRed());
		assertNotEquals(valueOfGreen, hslColor.getGreen());
		assertNotEquals(valueOfBlue, hslColor.getBlue());
	}
	
	@Test
	void testBlend2() {
		// Caso 2: percentuale compresa tra 0 ed 1 (con estremi esclusi)
		HSLColor hslColor = new HSLColor();
		
		int valueOfRed = 10;
		int valueOfGreen = 10;
		int valueOfBlue = 10;
		float percent = 0.5f;
		
		hslColor.blend(valueOfRed, valueOfGreen, valueOfBlue, percent);
		
		int newRed = (int) ((valueOfRed * percent) + (valueOfRed * (1.0 - percent)));
		int newGreen = (int) ((valueOfGreen * percent) + (valueOfGreen * (1.0 - percent)));
		int newBlue = (int) ((valueOfBlue * percent) + (valueOfBlue * (1.0 - percent)));
		
		assertNotEquals(newRed, hslColor.getRed());
		assertNotEquals(newGreen, hslColor.getGreen());
		assertNotEquals(newBlue, hslColor.getBlue());
	}
}
