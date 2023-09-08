import org.junit.*;
import static org.junit.Assert.*;

// Game: 1004
public class TestFontInfo {

	@Test(timeout = 4000)
	public void test_1_791() throws Throwable {
		FontInfo f = new FontInfo();
		assertEquals(12, f.getSize());
		assertEquals("Monospaced", f.getFamily());
	}

	@Test(timeout = 4000)
	public void test_2_792() throws Throwable {
		FontInfo f = new FontInfo();
		f.setFamily("");
		assertEquals("", f.getFamily());
		f.setFamily("Arial");
		assertEquals("Arial", f.getFamily());
	}

	@Test(timeout = 4000)
	public void test_3_794() throws Throwable {
		FontInfo f = new FontInfo();
		f.setFamily(null);
		assertEquals("Monospaced", f.getFamily());
	}

	@Test(timeout = 4000)
	public void test_4_795() throws Throwable {
		// test here!
		FontInfo f = new FontInfo();
		f.setIsItalic(true);
		assertEquals(true, f.isItalic());
	}

	@Test(timeout = 4000)
	public void test_5_797() throws Throwable {
		FontInfo f = new FontInfo();
		assertFalse(f.isBold());
		f.setIsBold(true);
		assertTrue(f.isBold());
	}

	@Test(timeout = 4000)
	public void test_6_806() throws Throwable {
		FontInfo f = new FontInfo();
		try {
			f.setFont(null);
			fail("Null Font passed");
		} catch (IllegalArgumentException e) {
			// expected
		}

		java.awt.Font font = f.createFont();
		f.setFont(font);
		assertFalse(f.isItalic());
	}

	@Test(timeout = 4000)
	public void test_7_811() throws Throwable {
		FontInfo f = new FontInfo();
		assertFalse(f.doesFontMatch(null));
		java.awt.Font font = f.createFont();
		assertTrue(f.doesFontMatch(font));
	}

	@Test(timeout = 4000)
	public void test_8_814() throws Throwable {
		// test here!
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12);
		FontInfo fi = new FontInfo(font);
		String fstring = fi.toString();
		assertEquals("Dialog, 12, bold",fstring);

	}

	@Test(timeout = 4000)
	public void test_9_818() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12);
		try{
		   FontInfo fi = new FontInfo(null);
		}catch(IllegalArgumentException ex){
		     String m = ex.getMessage()	;
			 assertEquals("Null Font passed",m); 
		}
		//String fstring = fi.toString();
		//assertEquals("Dialog, 12, bold",fstring);
	}

	@Test(timeout = 4000)
	public void test_10_819() throws Throwable {
		FontInfo f = new FontInfo();
		java.awt.Font font = f.createFont();
		f.setSize(3397);
		assertFalse(f.doesFontMatch(font));
	}

	@Test(timeout = 4000)
	public void test_11_821() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12);
		FontInfo fi = new FontInfo(font);
		int x = fi.generateStyle();
		assertEquals(1,x);
		
	}

	@Test(timeout = 4000)
	public void test_12_822() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.ITALIC, 12);
		FontInfo fi = new FontInfo(font);
		int x = fi.generateStyle();
		assertEquals(2,x);
	}

	@Test(timeout = 4000)
	public void test_13_823() throws Throwable {
		// test here!
		FontInfo f1 = new FontInfo();
		FontInfo f2 = new FontInfo();
		
		f1.setIsItalic(false);
		f2.setIsItalic(true);	
		assertEquals(false,f1.equals(f2));
	}

	@Test(timeout = 4000)
	public void test_14_824() throws Throwable {
		FontInfo f = new FontInfo();
		f.setIsBold(true);

		int style = 0;
		style |= java.awt.Font.BOLD;
		assertEquals(style, f.generateStyle());
	}

	@Test(timeout = 4000)
	public void test_15_827() throws Throwable {
		FontInfo f = new FontInfo();
		f.setIsBold(false);
		f.setIsItalic(true);

		int style = 0;
		style |= java.awt.Font.ITALIC;
		assertEquals(style, f.generateStyle());
	}

	@Test(timeout = 4000)
	public void test_16_828() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12);
		FontInfo fi = new FontInfo(font);
		FontInfo fi2 = (FontInfo)fi.clone();
		
		assertEquals(fi,fi2);
		
		fi.setFamily("jose");
		assertFalse(fi.equals(fi2));
	}

	@Test(timeout = 4000)
	public void test_17_830() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12);
		FontInfo fi = new FontInfo(font);
		FontInfo fi2 = (FontInfo)fi.clone();
		
		assertEquals(fi,fi2);
		
		fi.setFamily("jose");
		fi2.setIsItalic(true);	
		assertFalse(fi.equals(fi2));
		
	}

	@Test(timeout = 4000)
	public void test_18_832() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12);
		FontInfo fi = new FontInfo(font);
		FontInfo fi2 = (FontInfo)fi.clone();
		
		assertEquals(fi,fi2);
		
		fi.setFamily(null);
		//fi2.setIsItalic(true);	
		assertFalse(fi.equals(fi2));
	}

	@Test(timeout = 4000)
	public void test_19_833() throws Throwable {
		FontInfo f = new FontInfo();
		f.setIsBold(false);
		f.setIsItalic(false);
		assertEquals(0, f.generateStyle());
	}

	@Test(timeout = 4000)
	public void test_20_834() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12);
		FontInfo fi = new FontInfo(font);
		FontInfo fi2 = (FontInfo)fi.clone();
		
		assertEquals(fi,fi2);
		
		fi2.setFamily(null);
		//fi2.setIsItalic(true);	
		assertFalse(fi.equals(fi2));
	}

	@Test(timeout = 4000)
	public void test_21_835() throws Throwable {
		// test here!
		FontInfo f1 = new FontInfo();
		TestFontInfo l = new TestFontInfo();
		
		assertEquals(false,f1.equals(l));
	}

	@Test(timeout = 4000)
	public void test_22_836() throws Throwable {
		try {
			FontInfo f = new FontInfo(null);
			fail("Null Font passed");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test(timeout = 4000)
	public void test_23_837() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12);
		FontInfo fi = new FontInfo(font);
		FontInfo fi2 = (FontInfo)fi.clone();
		
		assertEquals(fi,fi2);
		
		fi.setFamily(null);
		fi2.setFamily("jose");
		//fi2.setIsItalic(true);	
		assertFalse(fi.equals(fi2));
	}

	@Test(timeout = 4000)
	public void test_24_838() throws Throwable {
		FontInfo f = new FontInfo();
		assertEquals(f, f.clone());
	}

	@Test(timeout = 4000)
	public void test_25_840() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12);
		FontInfo fi = new FontInfo(font);
		FontInfo fi2 = (FontInfo)fi.clone();
		
		assertEquals(-948144129,fi.hashCode());
		
		//fi.setFamily(null);
		//fi2.setFamily("jose");
		//fi2.setIsItalic(true);	
		//assertFalse(fi.equals(fi2));
	}

	@Test(timeout = 4000)
	public void test_26_844() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.ITALIC, 12);
		FontInfo fi = new FontInfo(font);
		FontInfo fi2 = (FontInfo)fi.clone();
		fi.setFamily(null);
		assertEquals(-1059600462,fi.hashCode());
	}

	@Test(timeout = 4000)
	public void test_27_847() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.ITALIC, 12);
		FontInfo fi = new FontInfo(font);
		FontInfo fi2 = (FontInfo)fi.clone();
		assertEquals(-948138549,fi.hashCode());
	}

	@Test(timeout = 4000)
	public void test_28_849() throws Throwable {
		java.awt.Font font = new java.awt.Font("", 0, 0);
		FontInfo f = new FontInfo(font);
		Object clone = f.clone();
		assertEquals(f, clone);
	}

	@Test(timeout = 4000)
	public void test_29_852() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.ITALIC, 12);
		FontInfo fi = new FontInfo(font);
		FontInfo fi2 = (FontInfo)fi.clone();
		fi2.setIsItalic(false);	
		assertEquals(false,fi.equals(fi2));
	}

	@Test(timeout = 4000)
	public void test_30_855() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.ITALIC+ java.awt.Font.BOLD, 12);
		FontInfo fi = new FontInfo(font);
		int x = fi.generateStyle();
		assertEquals(3,x);
	}

	@Test(timeout = 4000)
	public void test_31_857() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", 0, 12);
		FontInfo fi = new FontInfo(font);
		int x = fi.generateStyle();
		assertEquals(0,x);
	}

	@Test(timeout = 4000)
	public void test_32_861() throws Throwable {
		java.awt.Font font = new java.awt.Font("Arial", java.awt.Font.BOLD + java.awt.Font.ITALIC, 12);
		FontInfo fi = new FontInfo(font);
		String fstring = fi.toString();
		assertEquals("Dialog, 12, bold, italic",fstring);
	}

	@Test(timeout = 4000)
	public void test_33_863() throws Throwable {
		java.awt.Font font = new java.awt.Font("Arial", java.awt.Font.BOLD + java.awt.Font.ITALIC, 12);
		FontInfo fi = new FontInfo(font);
		
		assertEquals(false,fi.doesFontMatch(null));
	}

	@Test(timeout = 4000)
	public void test_34_867() throws Throwable {
		java.awt.Font font = new java.awt.Font("Arial", java.awt.Font.BOLD + java.awt.Font.ITALIC, 12);
		FontInfo fi = new FontInfo(font);
		
		java.awt.Font font2 = new java.awt.Font("Arial", java.awt.Font.BOLD + java.awt.Font.ITALIC, 12);
		assertEquals(true,fi.doesFontMatch(font2));
	}

	@Test(timeout = 4000)
	public void test_35_868() throws Throwable {
		FontInfo f = new FontInfo();
    	f.setFamily("Arial");
		f.setSize(24);
    	f.setIsBold(true);
    	f.setIsItalic(true);
    	assertEquals("Arial, 24, bold, italic", f.toString());
	}

	@Test(timeout = 4000)
	public void test_36_869() throws Throwable {
		FontInfo f = new FontInfo();
    	f.setFamily("Arial");
		f.setSize(24);
    	f.setIsBold(false);
    	f.setIsItalic(true);
    	assertEquals("Arial, 24, italic", f.toString());
	}

	@Test(timeout = 4000)
	public void test_37_870() throws Throwable {
		FontInfo f = new FontInfo();
    	f.setFamily("Arial");
		f.setSize(24);
    	f.setIsBold(true);
    	f.setIsItalic(false);
    	assertEquals("Arial, 24, bold", f.toString());
	}

	@Test(timeout = 4000)
	public void test_38_871() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.ITALIC, 12);
		FontInfo fi = new FontInfo(font);
		FontInfo fi2 = (FontInfo)fi.clone();
		assertEquals(true,fi.equals(fi2));
		fi2.setSize(42);	
		assertEquals(false,fi.equals(fi2));
	}

	@Test(timeout = 4000)
	public void test_39_872() throws Throwable {
		FontInfo f = new FontInfo();
    	f.setFamily("Arial");
		f.setSize(24);
    	f.setIsBold(false);
    	f.setIsItalic(false);
    	assertEquals("Arial, 24", f.toString());
	}

	@Test(timeout = 4000)
	public void test_40_873() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.ITALIC, 12);
		FontInfo fi = new FontInfo(font);
		FontInfo fi2 = (FontInfo)fi.clone();
		assertEquals(true,fi.equals(fi2));
		fi2.setSize(42);	
		assertEquals(false,fi.equals(null));
	}

	@Test(timeout = 4000)
	public void test_41_875() throws Throwable {
		FontInfo f = new FontInfo();
    	assertTrue(f.equals(f));
	}

	@Test(timeout = 4000)
	public void test_42_876() throws Throwable {
		FontInfo f = new FontInfo();
    	assertFalse(f.equals(null));
	}

	@Test(timeout = 4000)
	public void test_43_878() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.ITALIC, 12);
		FontInfo fi = new FontInfo(font);
		FontInfo fi2 = (FontInfo)fi.clone();
		assertEquals(false,fi.isBold());
		fi2.setIsBold(true);	
		assertEquals(false,fi.equals(fi2));
	}

	@Test(timeout = 4000)
	public void test_44_879() throws Throwable {
		FontInfo f = new FontInfo();
    	assertFalse(f.equals(new String()));
	}

	@Test(timeout = 4000)
	public void test_45_881() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.ITALIC, 12);
		FontInfo fi = new FontInfo(font);
		FontInfo fi2 = (FontInfo)fi.clone();
		assertEquals(false,fi.isBold());
		fi.setIsBold(true);	
		assertEquals(true,fi.isBold());
	}

	@Test(timeout = 4000)
	public void test_46_883() throws Throwable {
		// test here!
		FontInfo f = new FontInfo();
		
		f.setIsBold(false);
		f.setIsItalic(true);
		
		assertEquals(2,f.generateStyle());
	}

	@Test(timeout = 4000)
	public void test_47_884() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.ITALIC, 12);
		FontInfo fi = new FontInfo(font);
		assertEquals(true,fi.equals(fi));
		FontInfo fi2 = (FontInfo)fi.clone();
		fi.setFamily(null);
		fi2.setFamily("JAVA");
		assertEquals(false,fi.equals(fi2));
	}

	@Test(timeout = 4000)
	public void test_48_886() throws Throwable {
		// test here!
		FontInfo f = new FontInfo();
		
		f.setIsBold(true);
		f.setIsItalic(false);
		
		assertEquals(1,f.generateStyle());

	}

	@Test(timeout = 4000)
	public void test_49_887() throws Throwable {
		FontInfo f1 = new FontInfo();
		f1.setFamily("Helvetiva");
		FontInfo f2 = new FontInfo();
		f1.setFamily("Arial");
    	assertFalse(f1.equals(f2));
	}

	@Test(timeout = 4000)
	public void test_50_889() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.ITALIC, 42);
		FontInfo fi = new FontInfo(font);
		assertEquals(42,fi.getSize());
		fi.setSize(72);
		assertEquals(72,fi.getSize());
		
		FontInfo fi2 = (FontInfo)fi.clone();
		fi.setFamily(null);
		fi2.setFamily("JAVA");
		//assertEquals(false,fi.equals(fi2));
	}

	@Test(timeout = 4000)
	public void test_51_890() throws Throwable {
		FontInfo f1 = new FontInfo();
		f1.setIsBold(true);
		FontInfo f2 = new FontInfo();
		f2.setIsBold(false);
    	assertFalse(f1.equals(f2));
	}

	@Test(timeout = 4000)
	public void test_52_891() throws Throwable {
		FontInfo f1 = new FontInfo();
		f1.setIsBold(false);
		FontInfo f2 = new FontInfo();
		f2.setIsBold(true);
    	assertFalse(f1.equals(f2));
	}

	@Test(timeout = 4000)
	public void test_53_893() throws Throwable {
		FontInfo f1 = new FontInfo();
		f1.setIsItalic(false);
		FontInfo f2 = new FontInfo();
		f2.setIsItalic(true);
    	assertFalse(f1.equals(f2));
	}

	@Test(timeout = 4000)
	public void test_54_894() throws Throwable {
		FontInfo f1 = new FontInfo();
		f1.setIsItalic(true);
		FontInfo f2 = new FontInfo();
		f2.setIsItalic(false);
    	assertFalse(f1.equals(f2));
	}

	@Test(timeout = 4000)
	public void test_55_895() throws Throwable {
		java.awt.Font font = new java.awt.Font("Tahoma", java.awt.Font.ITALIC, 42);
		FontInfo fi = new FontInfo(font);
		fi.setSize(72);
		
		
		FontInfo fi2 = (FontInfo)fi.clone();
		
		fi2.setSize(-1);
		assertEquals(-1,fi2.getSize());
		assertEquals(false,fi.equals(fi2));
	}

	@Test(timeout = 4000)
	public void test_56_896() throws Throwable {
		FontInfo f1 = new FontInfo();
		f1.setSize(-1);
		FontInfo f2 = new FontInfo();
		f2.setSize(1000);
    	assertFalse(f1.equals(f2));
	}

	@Test(timeout = 4000)
	public void test_57_897() throws Throwable {
		java.awt.Font font = new java.awt.Font("my_font", 5, 55);
		FontInfo f1 = new FontInfo(font);
		FontInfo f2 = new FontInfo();
    	assertFalse(f1.equals(f2));
	}

	@Test(timeout = 4000)
	public void test_58_899() throws Throwable {
		FontInfo f1 = new FontInfo();
		FontInfo f2 = new FontInfo(f1.createFont());
    	assertTrue(f1.equals(f2));
	}

	@Test(timeout = 4000)
	public void test_59_900() throws Throwable {
		FontInfo f = new FontInfo();
		f.setIsBold(false);
		f.setIsItalic(false);
		f.setFamily("Monospaced");
		f.setSize(12);
		assertEquals(-1059600276, f.hashCode());

		f.setFamily("Arial");
		assertEquals(-1484230230, f.hashCode());
	}

	@Test(timeout = 4000)
	public void test_60_901() throws Throwable {
		FontInfo f = new FontInfo();
		f.setIsBold(false);
		f.setIsItalic(false);
		f.setFamily("Monospaced");
		f.setSize(12);
		f.setFamily("Arial");

		f.setIsBold(true);
		assertEquals(-1484235996, f.hashCode());

		f.setIsItalic(true);
		assertEquals(-1484236182, f.hashCode());
	}

	@Test(timeout = 4000)
	public void test_61_902() throws Throwable {
		FontInfo f1 = new FontInfo();
		f1.setSize(1000);
		FontInfo f2 = new FontInfo();
		f2.setSize(-1);
    	assertFalse(f1.equals(f2));
	}

	@Test(timeout = 4000)
	public void test_62_903() throws Throwable {
		// test here!
		FontInfo f1 = new FontInfo();
		FontInfo f2 = new FontInfo();
		
		f1.setSize(34);
		f2.setSize(22);	
		assertEquals(false,f1.equals(f2));
	}

	@Test(timeout = 4000)
	public void test_63_904() throws Throwable {
		// test here!
               FontInfo f1 = new FontInfo();
               FontInfo f2 = new FontInfo();
               
               f1.setSize(34);
               f2.setSize(22);        
               assertEquals(false,f1.equals(f2));
	}

	@Test(timeout = 4000)
	public void test_64_905() throws Throwable {
		// test here!
		FontInfo f1 = new FontInfo();
               FontInfo f2 = new FontInfo();
               
               f1.setSize(34);
               f2.setSize(22);        
               assertEquals(false,f1.equals(f2));
	}

	@Test(timeout = 4000)
	public void test_65_906() throws Throwable {
		// test here!
		FontInfo f1 = new FontInfo();
               FontInfo f2 = new FontInfo();
               
               f1.setSize(34);
               f2.setSize(22);        
               assertEquals(false,f1.equals(f2));
	}

	@Test(timeout = 4000)
	public void test_66_907() throws Throwable {
		// test here!
		FontInfo f1 = new FontInfo();
               FontInfo f2 = new FontInfo();
               
               f1.setSize(34);
               f2.setSize(22);        
               assertEquals(false,f1.equals(f2));
	}

	@Test(timeout = 4000)
	public void test_67_909() throws Throwable {
		// test here!
		FontInfo f1 = new FontInfo();
               FontInfo f2 = new FontInfo();
               
               f1.setSize(34);
               f2.setSize(22);        
               assertEquals(false,f1.equals(f2));
	}

	@Test(timeout = 4000)
	public void test_68_910() throws Throwable {
		// test here!
		FontInfo f1 = new FontInfo();
               FontInfo f2 = new FontInfo();
               
               f1.setSize(34);
               f2.setSize(22);        
               assertEquals(false,f1.equals(f2));
	}

}
