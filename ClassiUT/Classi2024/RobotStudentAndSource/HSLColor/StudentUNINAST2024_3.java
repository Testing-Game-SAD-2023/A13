/*Compila i campi "Nome" e "Cognome" con le informazioni richieste
Nome: Andrea
Cognome: Bertolero
Username: a.bertolero@studenti.unina.it
UserID: 26
Date: 19/11/2024
*/

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestHSLColor {
	@BeforeClass
	public static void setUpClass() 
    {
		
	}
				
	@AfterClass
	public static void tearDownClass() 
    {
	
	}
				
	@Before
	public void setUp() 
    {
		
	}
				
	@After
	public void tearDown() 
    {

	}
				
	@Test
	public void testinitHSLbyRGB1() 
    {
        HSLColor hsl = new HSLColor();
        hsl.initHSLbyRGB(0, 0, 0);

        assertEquals(170, hsl.getHue());
        assertEquals(0, hsl.getSaturation());
        assertEquals(0, hsl.getLuminence());
	}
  
  	@Test
	public void testinitHSLbyRGB2() 
    {
        HSLColor hsl = new HSLColor();
        hsl.initHSLbyRGB(0, 255, 0);

        assertEquals(85, hsl.getHue());
        assertEquals(255, hsl.getSaturation());
        assertEquals(128, hsl.getLuminence());
	}
  
   	@Test
	public void testinitHSLbyRGB3() 
    {
        HSLColor hsl = new HSLColor();
        hsl.initHSLbyRGB(0, 0, 255);

        assertEquals(170, hsl.getHue());
        assertEquals(255, hsl.getSaturation());
        assertEquals(128, hsl.getLuminence());
	}
  
  	@Test 
  	public void testinitRGBbyHSL1()
    {
      	HSLColor hsl = new HSLColor();
      	hsl.initRGBbyHSL(255, 0, 255);
          
        assertEquals(255, hsl.getRed());
        assertEquals(255, hsl.getGreen());
        assertEquals(255, hsl.getBlue());  
    }
  
    @Test 
  	public void testinitRGBbyHSL2()
    {
      	HSLColor hsl = new HSLColor();
      	hsl.initRGBbyHSL(0, 300, 300);
          
        assertEquals(247, hsl.getRed());
        assertEquals(255, hsl.getGreen());
        assertEquals(255, hsl.getBlue());  
    }
  
    
    @Test 
  	public void testinitRGBbyHSL3()
    {
      	HSLColor hsl = new HSLColor();
      	hsl.initRGBbyHSL(120, 300, 300);
          
        assertEquals(255, hsl.getRed());
        assertEquals(247, hsl.getGreen());
        assertEquals(255, hsl.getBlue());  
    }
    
    @Test 
  	public void testinitRGBbyHSL4()
    {
      	HSLColor hsl = new HSLColor();
      	hsl.initHSLbyRGB(80, 2, 10);
          
        assertEquals(80, hsl.getRed());
        assertEquals(2, hsl.getGreen());
        assertEquals(10, hsl.getBlue());  
    }
    
    @Test 
  	public void testinitRGBbyHSL5()
    {
      	HSLColor hsl = new HSLColor();
      	hsl.initRGBbyHSL(255, 2, 155);
          
        assertEquals(156, hsl.getRed());
        assertEquals(154, hsl.getGreen());
        assertEquals(154, hsl.getBlue());  
    }
  
  @Test
  public void testsetHue1()
  {
        HSLColor hsl = new HSLColor();
        hsl.setHue(-1);
    
    	assertEquals(254, hsl.getHue());
  }
  
  @Test
  public void testsetHue2()
  {
        HSLColor hsl = new HSLColor();
        hsl.setHue(256);
    
    	assertEquals(1, hsl.getHue());
  }
  
  @Test
  public void testsetSaturation1()
  {
        HSLColor hsl = new HSLColor();
        hsl.setSaturation(-1);
    
    	assertEquals(0, hsl.getSaturation());
  }
  
  @Test
  public void testsetSaturation2()
  {
        HSLColor hsl = new HSLColor();
        hsl.setSaturation(256);
    
    	assertEquals(255, hsl.getSaturation());
  }
  
  @Test 
  public void testtestsetSaturation3() {
	  HSLColor hsl = new HSLColor();
      hsl.setSaturation(10);
  
  		assertEquals(10, hsl.getSaturation());
  }
  
  @Test
  public void testsetLuminence1()
  {
        HSLColor hsl = new HSLColor();
        hsl.setLuminence(-1);
    
    	assertEquals(0, hsl.getLuminence());
  }
  
  @Test
  public void testsetLuminence2()
  {
        HSLColor hsl = new HSLColor();
        hsl.setLuminence(256);
    
    	assertEquals(255, hsl.getLuminence());
  }
  
  @Test
  public void testreverseColor1()
  {
        HSLColor hsl = new HSLColor();
        hsl.reverseColor();
    
    	assertEquals(127, hsl.getHue());
  }
  
  @Test
  public void testbrighten1()
  {
        HSLColor hsl = new HSLColor();
        hsl.brighten(0);
    
    	assertEquals(0, hsl.getRed());
  }
  
  @Test
  public void testbrighten2()
  {
        HSLColor hsl = new HSLColor();
        hsl.initHSLbyRGB(80, 2, 10);
        hsl.brighten(-1);
    
    	assertEquals(0, hsl.getRed());
  }
  
  @Test
  public void testbrighten3()
  {
        HSLColor hsl = new HSLColor();
        hsl.initHSLbyRGB(80, 2, 10);
        hsl.brighten(255);
    
    	assertEquals(255, hsl.getRed());
  }
  
  @Test
  public void testblend1()
  {
        HSLColor hsl = new HSLColor();
        hsl.blend(1,1,1, 1);

        assertEquals(1, hsl.getRed());
        assertEquals(1, hsl.getGreen());
        assertEquals(1, hsl.getBlue());
  }
  
  @Test
  public void testblend2()
  {
        HSLColor hsl = new HSLColor();
        hsl.blend(1,1,1,0);

        assertEquals(0, hsl.getRed());
  }
   
  @Test
  public void testblend3() 
  {
        HSLColor hsl = new HSLColor();
    	hsl.blend(1,1,1, 0.5f); 
        
    	assertEquals(0, hsl.getRed());
        assertEquals(0, hsl.getGreen());
        assertEquals(0, hsl.getBlue());
    }
  
  

  
}

						