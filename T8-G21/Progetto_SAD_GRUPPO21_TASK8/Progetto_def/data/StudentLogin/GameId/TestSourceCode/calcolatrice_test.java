package calcolatrice;

import junit.framework.TestCase;

public class calcolatrice_test extends TestCase {

	calcolatrice c;
	protected void setUp() throws Exception {
		c=new calcolatrice();
		
		//PRECONDIZIONE
		assertNotNull(c);
	}

	protected void tearDown() throws Exception {
		c=null;
		
		//POSTCONDIZIONE
		assertNull(c);
	}

	public void testSomma() {
		//INPUT
		int a=22;
		int b=20;
		
		//ELABORAZIONE
		int s=c.somma(a, b);
		
		assertEquals("Somma errata",42,s);
		
	}

	public void testSomma2() {
		//INPUT
		int a=41;
		int b=-5;
		
		//ELABORAZIONE
		int s=c.somma(a, b);
		
		assertEquals("Somma errata",36,s);
		
	}

	public void testDivisione(){
		
		int a=15,b=2;
		double s=c.divisione(a,b);
		assertEquals("Errore nella divisione",7.5,s);
	}
	
	public void testDivisioneDelta(){
		
		int a=8,b=3;
		double s=c.divisione(a,b);
		assertEquals("Errore nella divisione",2.66,s,0.01);
	}
	
	public void testDivisioneZero(){
		
		int a=8,b=0;
		double s=c.divisione(a,b);
		assertEquals("Errore nella divisione",Double.POSITIVE_INFINITY,s);
	}
	
	
}
