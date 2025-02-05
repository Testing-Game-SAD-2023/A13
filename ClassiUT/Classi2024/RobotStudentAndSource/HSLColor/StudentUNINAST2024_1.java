/*Compila i campi "Nome" e "Cognome" con le informazioni richieste
Nome: Alessandro
Cognome: Cajafa
Username: a.cajafa@studenti.unina.it
UserID: 12
Date: 19/11/2024
*/

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*; 
import static org.junit.Assume.assumeTrue;

public class TestHSLColor {
  
  private HSLColor hslcolor;

  @BeforeClass
  public static void setUpClass() {
    // Eseguito una volta prima dell'inizio dei test nella classe
    // Inizializza risorse condivise 
    // o esegui altre operazioni di setup
  }

  @AfterClass
  public static void tearDownClass() {
    // Eseguito una volta alla fine di tutti i test nella classe
    // Effettua la pulizia delle risorse condivise 
    // o esegui altre operazioni di teardown
  }

  @Before
  public void setUp() {
    // Eseguito prima di ogni metodo di test
    // Preparazione dei dati di input specifici per il test

    hslcolor = new HSLColor();
  }

  @After
  public void tearDown() {
    // Eseguito dopo ogni metodo di test
    // Pulizia delle risorse o ripristino dello stato iniziale

    hslcolor = null;

    assumeTrue(hslcolor == null);
  }


  // Caso di test 1: Chiamata a initHSLbyRGB() con cMax == cMin e chiamata a getRed()		
  @Test
  public void testinitHSLbyRGB1() {
    // Preparazione dei dati di input
    // Esegui il metodo da testare
    // Verifica l'output o il comportamento atteso
    // Utilizza assert per confrontare il risultato atteso 
    // con il risultato effettivo

    hslcolor.initHSLbyRGB(100, 100, 100);
    assertEquals(100, hslcolor.getRed());
  }

  // Caso di test 2: Chiamata a initHSLbyRGB() con cMax != cMin e pLum <= (HSLMAX / 2) e chiamata a getBlue()
  @Test
  public void testinitHSLbyRGB2() {

    hslcolor.initHSLbyRGB(50, 70, 90);
    assertEquals(90, hslcolor.getBlue());
  }

  // Caso di test 3: Chiamata a initHSLbyRGB() con cMax != cMin e pLum > (HSLMAX / 2) e chiamata a getGreen()
  @Test
  public void testinitHSLbyRGB3() {

    hslcolor.initHSLbyRGB(10, 70, 250);
    assertEquals(70, hslcolor.getGreen());
  }

  // Caso di test 4: Chiamata a initHSLbyRGB() con cMax != cMin, pLum > (HSLMAX / 2) e cMax == B e chiamata a getHue()
  @Test
  public void testinitHSLbyRGB4() {

    hslcolor.initHSLbyRGB(250, 10, 70);
    assertEquals(244, hslcolor.getHue());
  }

  // Caso di test 5: Chiamata a initHSLbyRGB() con cMax != cMin, pLum > (HSLMAX / 2) e cMax == G e chiamata a getLuminence()
  @Test
  public void testinitHSLbyRGB5() {

    hslcolor.initHSLbyRGB(70, 250, 10);
    assertEquals(130, hslcolor.getLuminence());
  }

  // Caso di test 6: Chiamata a initRGBbyHSL() con S != 0 e L <= HSLMAX / 2 e chiamata a getSaturation()
  @Test
  public void testinitRGBbyHSL1() {

    hslcolor.initRGBbyHSL(0, 100, 50);
    assertEquals(100, hslcolor.getSaturation());
  }

  // Caso di test 7: Chiamata a initRGBbyHSL() con S = 0 
  @Test
  public void testinitRGBbyHSL2() {

    hslcolor.initRGBbyHSL(120, 0, 50);
    assertEquals(120, hslcolor.getHue());
  }

  // Caso di test 8: Chiamata a initRGBbyHSL() con S != 0, L > HSLMAX / 2 e Hue > HSLMAX
  @Test
  public void testinitRGBbyHSL3() {

    hslcolor.initRGBbyHSL(300, 10, 150);
    assertEquals(150, hslcolor.getLuminence());
  }
  // Caso di test 9: Chiamata a initRGBbyHSL() con S != 0, pGreen > RGBMAX e pBlue > RGBMAX
  @Test
  public void testinitRGBbyHSL4() {

    hslcolor.initRGBbyHSL(255, -255, 255);
    assertEquals(255, hslcolor.getLuminence());
  }

  // Caso di test 10: Chiamata a initRGBbyHSL() con S != 0, pRed > RGBMAX
  @Test
  public void testinitRGBbyHSL5() {

    hslcolor.initRGBbyHSL(255, -300, 300);
    assertEquals(300, hslcolor.getLuminence());
  }


  // Caso di test 11: Chiamata a setHue() con 0 < hue < HSLMAX
  @Test
  public void testSetHue1() {

    hslcolor.setHue(10);
    assertEquals(10, hslcolor.getHue());
  }

  // Caso di test 12: Chiamata a setHue() con hue < 0
  @Test
  public void testSetHue2() {

    hslcolor.setHue(-10);
    assertEquals(245, hslcolor.getHue());
  }

  // Caso di test 13: Chiamata a setHue() con hue > HSLMAX
  @Test
  public void testSetHue3() {

    hslcolor.setHue(260);
    assertEquals(5, hslcolor.getHue());
  }

  // Caso di test 14: Chiamata a setSaturation() con 0 < saturation < HSLMAX
  @Test
  public void testSetSaturation1() {

    hslcolor.setSaturation(10);
    assertEquals(10, hslcolor.getSaturation());
  }

  // Caso di test 15: Chiamata a setSaturation() con saturation < 0
  @Test
  public void testSetSaturation2() {

    hslcolor.setSaturation(-10);
    assertEquals(0, hslcolor.getSaturation());
  }

  // Caso di test 16: Chiamata a setSaturation() con saturation > HSLMAX
  @Test
  public void testSetSaturation3() {

    hslcolor.setSaturation(260);
    assertEquals(255, hslcolor.getSaturation());
  }

  // Caso di test 17: Chiamata a setLuminence() con 0 < luminence < HSLMAX
  @Test
  public void testSetLuminence1() {

    hslcolor.setLuminence(10);
    assertEquals(10, hslcolor.getLuminence());
  }

  // Caso di test 18: Chiamata a setLuminence() con luminence < 0
  @Test
  public void testSetLuminence2() {

    hslcolor.setLuminence(-10);
    assertEquals(0, hslcolor.getLuminence());
  }

  // Caso di test 19: Chiamata a setLuminence() con luminence > HSLMAX
  @Test
  public void testSetLuminence3() {

    hslcolor.setLuminence(260);
    assertEquals(255, hslcolor.getLuminence());
  }

  // Caso di test 20: Chiamata a reverseColor()
  @Test
  public void testReverseColor() {

    hslcolor.initHSLbyRGB(100, 100, 100);
    hslcolor.reverseColor();
    assertEquals(42, hslcolor.getHue());
  }

  // Caso di test 21: Chiamata a brighten() con fPercent = 0
  @Test
  public void testBrighten1() {

    hslcolor.initHSLbyRGB(100, 100, 100);
    hslcolor.brighten(0);
    assertEquals(100, hslcolor.getLuminence());
  }

  // Caso di test 22: Chiamata a brighten() con fPercent != 0 e L > HSLMAX
  @Test
  public void testBrighten2() {

    hslcolor.initHSLbyRGB(100, 100, 100);
    hslcolor.brighten((float)3.0);
    assertEquals(255, hslcolor.getLuminence());
  }

  // Caso di test 23: Chiamata a brighten() con fPercent != 0 e L < 0
  @Test
  public void testBrighten3() {

    hslcolor.initHSLbyRGB(100, 100, 100);
    hslcolor.brighten((float)-0.5);
    assertEquals(0, hslcolor.getLuminence());
  }

  // Caso di test 24: Chiamata a blend() con 0 < fPercent < 1
  @Test
  public void testBlend1() {

    hslcolor.initHSLbyRGB(100, 100, 100);
    hslcolor.blend(50, 50, 50, (float) 0.8);
    assertEquals(59, hslcolor.getRed());
  }

  // Caso di test 25: Chiamata a blend() con fPercent > 1
  @Test
  public void testBlend2() {

    hslcolor.initHSLbyRGB(100, 100, 100);
    hslcolor.blend(50, 50, 50, (float) 1.4);
    assertEquals(50, hslcolor.getRed());
  }

  // Caso di test 26: Chiamata a blend() con fPercent < 0
  @Test
  public void testBlend3() {

    hslcolor.initHSLbyRGB(100, 100, 100);
    hslcolor.blend(50, 50, 50, (float) -0.5);
    assertEquals(100, hslcolor.getRed());
  }
	
	
}

						
