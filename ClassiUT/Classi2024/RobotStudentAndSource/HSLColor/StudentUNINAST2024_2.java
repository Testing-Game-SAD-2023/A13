/*Compila i campi "Nome" e "Cognome" con le informazioni richieste
Nome: "Alessio"
Cognome: "Matarazzo"
Username: ale.matarazzo@studenti.unina.it
UserID: 15
Date: 22/11/2024
*/

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class HSLColorAlessioMatarazzoEvoSuiteHard {
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
	}
				
	@After
	public void tearDown() {
		// Eseguito dopo ogni metodo di test
		// Pulizia delle risorse o ripristino dello stato iniziale
	}
				
	@Test 
   public void TestinitHSLbyRGB() {
	   
	HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(0, 0, 0);
	   
	   
	   
	   
	   
   }
   
   
   @Test 
   public void TestinitHSLbyRGB2() {
	   
	HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(1, 3, 2);
	   
	   
	   
	   
	   
   }
   
   @Test 
   public void TestinitHSLbyRGB3() {
	   
	HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
	   
	   
	   
	   
	   
   }
   
   @Test 
   public void TestinitHSLbyRGB4() {
	   
	HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(100, 100, 250);
	   
	   
	   
	   
	   
   }
	
   @Test 
   public void TestinitRGBbyHSL() {
	   
	HSLColor Color = new HSLColor();   
	Color.initRGBbyHSL(100, 100, 250);
	   
	   
	   
	   
	   
   }
	
   @Test 
   public void TestinitRGBbyHSL2() {
	   
	HSLColor Color = new HSLColor();   
	Color.initRGBbyHSL(0, 0, 0);
	   
	   
	   
	   
	   
   }
	
   @Test 
   public void TestinitRGBbyHSL3() {
	   
	HSLColor Color = new HSLColor();   
	Color.initRGBbyHSL(100, 100, 42);
	   
	   
	   
	   
	   
   }
	
   @Test 
   public void TestinitRGBbyHSL4() {
	   
	HSLColor Color = new HSLColor();   
	Color.initRGBbyHSL(1000, 100, -100);
	   
	   
	   
	   
	   
   }
	
   @Test 
   public void TestinitRGBbyHSL5() {
	   
	HSLColor Color = new HSLColor();   
	Color.initRGBbyHSL(-100, 100, -100);
	   
	   
	   
	   
	   
   }
  
   @Test 
   public void TestinitRGBbyHSL6() {
	   
	HSLColor Color = new HSLColor();   
	Color.initRGBbyHSL(1000, 100, 1000);
	   
	   
	   
	   
	   
   }
				
  @Test 
  public void TestgetHue(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.getHue();
    
    
    
  }
  
  @Test 
  public void TestsetHue(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.setHue(-42);
    
    
    
  }
  @Test 
  public void TestsetHue2(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.setHue(420);
    
    
    
  }
  @Test 
  public void TestgetSaturation(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.getSaturation();
    
    
    
  }
  
  @Test 
  public void TestsetSaturation(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.setSaturation(-42);
    
    
    
  }
  
  @Test 
  public void TestsetSaturation2(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.setSaturation(420);
    
    
    
  }
  
  @Test 
  public void TestgetLuminence(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.getLuminence();
  
  }
  
   @Test 
  public void TestsetLuminence(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.setLuminence(-42);
  }
  
   @Test 
  public void TestsetLuminence2(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.setLuminence(420);
    
  }
  
  @Test 
  public void TestgetRed(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.getRed();
  
  }

  
  
  @Test 
  public void TestgetGreen(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.getGreen();
  
  }
  
  
  
  
  @Test 
  public void TestgetBlue(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.getBlue();
  
  }
  
  @Test
  public void TestReverseColor(){
    
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.reverseColor();
  
    
    
  }
  
  @Test 
  public void Testbrighten(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.brighten(0);
    
  }
  
  @Test 
  public void Testbrighten2(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.brighten(42);
    
  }
  
   @Test 
  public void Testbrighten3(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.brighten(-42);
    
  }
  
  @Test 
  public void Testblend(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.blend(100,255,100,1);
    
  }
  
  @Test 
  public void Testblend2(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.blend(100,255,100,0);
    
  }
  
  @Test 
  public void Testblend3(){
    HSLColor Color = new HSLColor();   
	Color.initHSLbyRGB(255, 100, 111);
    Color.blend(100,255,100,(float)0.5);
    
  }
  
	// Aggiungi altri metodi di test se necessario
}

						