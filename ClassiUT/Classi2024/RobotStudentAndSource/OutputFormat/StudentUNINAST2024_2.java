import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import ClassUnderTest.HSLColor;

import static org.junit.Assert.*;

public class TestHSLColor {
	@BeforeClass
	public static void setUpClass() {
	}
				
	@AfterClass
	public static void tearDownClass() {
	}
				
	@Before
	public void setUp() {
		// Eseguito prima di ogni metodo di test
		// Preparazione dei dati di input specifici per il test
	}
				
	@After
	public void tearDown() {
		// Eseguito dopo ogni metodo di test
		// Pulizia delle risorse o ripristino dello stato iniziale
	}
		
	
//TEST INIT_HSL_BY_RGB
	//cMax==cMin
	@Test
	public void testInitHSLbyRGB1() {	
		HSLColor c = new HSLColor(); 
		c.initHSLbyRGB(0, 0, 0);
		}
	
	//cMax!=cMin
	@Test
	public void testInitHSLbyRGB2() {	
		HSLColor c = new HSLColor(); 
		c.initHSLbyRGB(100, 0, 0);
		}
	
	//cMax!=cMin
	@Test
	public void testInitHSLbyRGB3() {	
		HSLColor c = new HSLColor(); 
		c.initHSLbyRGB(255, 100, 200);
		}
	
	//cMax!=R
	@Test
	public void testInitHSLbyRGB4() {	
		HSLColor c = new HSLColor(); 
		c.initHSLbyRGB(10, 100, 200);
		}
	
	//cMax==G
	@Test
	public void testInitHSLbyRGB5() {	
		HSLColor c = new HSLColor(); 
		c.initHSLbyRGB(10, 255, 200);
		}
	

//TEST INIT_RGG_BY_HSL
	//S==0
	@Test
	public void testInitRGBbyHSL1() {
		HSLColor c= new HSLColor();
		c.initRGBbyHSL(100, 0, 100);
	}
	
	//S!=0
	@Test
	public void testInitRGBbyHSL2() {
		HSLColor c= new HSLColor();
		c.initRGBbyHSL(100, 10, 100);
		}
	
	//L>HSLMAX
	@Test
	public void testInitRGBbyHSL3() {
		HSLColor c= new HSLColor();
		c.initRGBbyHSL(100, 150, 255);
		}	
	
	//pGreen&pBlue >RGBMax
	@Test
	public void testInitRGBbyHSL4() {
		HSLColor c= new HSLColor();
		c.initRGBbyHSL(256, 258, 256);
		}		

//TEST GET_HUE
	@Test
	public void testGetHue() {
		HSLColor c = new HSLColor(); 
		c.initHSLbyRGB(0, 0, 0);
		int h=c.getHue();
	}
	
//TEST SET_HUE
	//iToValue<0
	@Test
	public void testSetHue1() {
		HSLColor c = new HSLColor(); 
		c.initHSLbyRGB(1, 1, 1);
		c.setHue(-104);
		}	
	
	//iToValue>HSLMAX
	@Test
	public void testSetHue2() {
		HSLColor c = new HSLColor(); 
		c.initHSLbyRGB(1, 1, 1);
		c.setHue(256);
		}	
	
//TEST GET_SATURATION
	@Test
	public void testGetSaturation() {
		HSLColor c = new HSLColor(); 
		c.initHSLbyRGB(1, 1, 1);
		int s=c.getSaturation();
	}
	
	
//TEST SET_SATURATION
	//iToValue<0
	@Test
	public void testSetSaturation1() {
		HSLColor c = new HSLColor(); 
		c.initHSLbyRGB(1, 1, 1);
		c.setSaturation(-1);
		}	
		
	//iToValue>HSLMAX
	@Test
	public void testSetSaturation2() {
		HSLColor c = new HSLColor(); 
		c.initHSLbyRGB(1, 1, 1);
		c.setSaturation(256);
		}	

	
	//iToValue<HSLMAX
	@Test
	public void testSetSaturation3() {
		HSLColor c = new HSLColor(); 
		c.initHSLbyRGB(1, 1, 1);
		c.setSaturation(255);
		}	
	
//TEST GET_LUMINENCE
	@Test
	public void testGetLuminence() {
		HSLColor c = new HSLColor(); 
		c.initHSLbyRGB(1, 1, 1);
		int l=c.getLuminence();
	}
	
//TEST SET_LUMINENCE
	//iToValue<0
	@Test
	public void testSetLuminence1() {
		HSLColor c = new HSLColor(); 
		c.initHSLbyRGB(1, 1, 1);
		c.setLuminence(-1);
	}
	
	//iToValue>HSLMAX
	@Test
	public void testSetLuminence2() {
		HSLColor c = new HSLColor(); 
		c.initHSLbyRGB(1, 1, 1);
		c.setLuminence(256);
	}	
	
	//iToValue>HSLMAX
	@Test
	public void testSetLuminence3() {
		HSLColor c = new HSLColor(); 
		c.initHSLbyRGB(1, 1, 1);
		c.setLuminence(255);
		}		
	
		
//TEST GET_RED
	@Test	
	public void testGetRed() {
		HSLColor c= new HSLColor();
		c.initRGBbyHSL(1, 1, 1);
		int r=c.getRed();
	}
		
//TEST GET_GREEN
	@Test	
	public void testGetGreen() {
		HSLColor c= new HSLColor();
		c.initRGBbyHSL(1, 1, 1);
		int g=c.getGreen();
	}
		
//TEST GET_BLUE
	@Test	
	public void testGetBlue() {
		HSLColor c= new HSLColor();
		c.initRGBbyHSL(1, 1, 1);
		int g=c.getBlue();
		}		
		
//TEST REVERSE_COLOR
	@Test
	public void testReverseColor() {
		HSLColor c= new HSLColor();
		c.initHSLbyRGB(1, 1, 1);
		c.reverseColor();
	}
		

//TEST BRIGHTEN
	//fPercent==0
	@Test
	public void testBrighten1() {
		HSLColor c= new HSLColor();
		c.initHSLbyRGB(1, 1, 1);	
		c.brighten(0);
	}
	
	//fPercent!=0
	@Test
	public void testBrighten2() {
		HSLColor c= new HSLColor();
		c.initHSLbyRGB(10, 10, 10);	
		c.setLuminence(256);
		c.brighten(3);
	}
		
//TEST BLEND
	//fPercent>=1
	@Test
	public void testBlend1() {
		HSLColor c= new HSLColor();
		c.initHSLbyRGB(10, 10, 10);
		c.blend(1, 1, 1, 2);
	}
	
	//fPercent<=0
	@Test
	public void testBlend2() {
		HSLColor c= new HSLColor();
		c.initHSLbyRGB(10, 10, 10);
		c.blend(1, 1, 1, -2);
		c.blend(1, 1, 1, 0);
	}
	
	//0<fPercent<1
	@Test
	public void testBlend3() {
		HSLColor c= new HSLColor();
		c.initHSLbyRGB(10, 10, 10);
		c.blend(1, 1, 1, 0.3f);
		}				
}