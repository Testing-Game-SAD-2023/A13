import org.junit.*;
import static org.junit.Assert.*;

// Game: 1003
public class TestChunkedLongArray {

	@Test(timeout = 4000)
	public void test_1_2086() throws Throwable {
		ChunkedLongArray cla = new ChunkedLongArray();
		
		cla.add(0);
		cla.add(10000000l);
		cla.add(42);
		cla.set(1,24);
		
		String result = "";
		result += cla.get(0);
		result += cla.get(1);
		result += cla.get(2);
		result += cla.size();
		result += cla.toString();
		result += cla.capacity();
		cla.add(1000);
		cla.add(-5);
		cla.sort();
		result += cla.get(0);
		result += cla.get(1);
		result += cla.get(2);
		result += cla.size();
		result += cla.capacity();
		result += cla.toString();
		
		assertEquals("024423ChunkedLongArray(3 entries, 1 chunks, 8MB)1048576-502451048576ChunkedLongArray(5 entries, 1 chunks, 8MB)", result);
	}

	@Test(timeout = 4000)
	public void test_2_2089() throws Throwable {
		ChunkedLongArray cla = new ChunkedLongArray();
		
		cla.add(0); cla.add(10000000l); cla.add(42); cla.set(1,24); cla.add(111); cla.add(72); cla.add(Long.MAX_VALUE); cla.add(Long.MIN_VALUE);
		
		String result = "";
		result += cla.get(0);
		result += cla.get(1);
		result += cla.get(2);
		result += cla.size();
		result += cla.toString();
		result += cla.capacity();
		result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
		cla.set(cla, 0, 1,2);
		result += cla.toString();
		result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
		cla.set(cla, 2, 3,7);
		result += cla.toString();
		result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
		cla.set(cla, 1, 4,0);
		result += cla.toString();
		result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
		cla.add(-5);
		cla.sort();
		result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
		result += cla.size();
		result += cla.capacity();
		result += cla.toString();
		
		assertEquals("024427ChunkedLongArray(7 entries, 1 chunks, 8MB)104857602442111729223372036854775807ChunkedLongArray(7 entries, 1 chunks, 8MB)0024111729223372036854775807ChunkedLongArray(7 entries, 1 chunks, 8MB)00242411172ChunkedLongArray(7 entries, 1 chunks, 8MB)00242411172-50024247281048576ChunkedLongArray(8 entries, 1 chunks, 8MB)", result);
	}

	@Test(timeout = 4000)
	public void test_3_2092() throws Throwable {
		ChunkedLongArray cla = new ChunkedLongArray();
		
		cla.add(-10); cla.add(10000000l); cla.add(42); cla.set(1,24); cla.add(111); cla.add(72); cla.add(Long.MAX_VALUE); cla.add(Long.MIN_VALUE);
		
		String result = "";
		result += cla.get(0);
		result += cla.get(1);
		result += cla.get(2);
		result += cla.size();
		result += cla.toString();
		result += cla.capacity();
		result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
		cla.set(cla, 10, 10,12);
		cla.sort();
		result += cla.toString();
		result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
		cla.set(cla, 20, 3,7);
		result += cla.toString();
		result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
		cla.set(cla, 100, 40,1);
		result += cla.toString();
		result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
		cla.add(-5);
		cla.sort();
		result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
		result += cla.size();
		result += cla.capacity();
		result += cla.toString();
		
		try{
			cla.get(1000);
			fail();
		} catch(ArrayIndexOutOfBoundsException e){result += e.getMessage();result+= e.getClass().getCanonicalName();}
		
		assertEquals("-1024427ChunkedLongArray(7 entries, 1 chunks, 8MB)1048576-102442111729223372036854775807ChunkedLongArray(7 entries, 1 chunks, 8MB)-9223372036854775808-10244272111ChunkedLongArray(7 entries, 1 chunks, 8MB)-9223372036854775808-1024000ChunkedLongArray(7 entries, 1 chunks, 8MB)-9223372036854775808-1024000-9223372036854775808-10-500081048576ChunkedLongArray(8 entries, 1 chunks, 8MB)Index 1000 requested with array length 8java.lang.ArrayIndexOutOfBoundsException", result);
	}

	@Test(timeout = 4000)
	public void test_4_2100() throws Throwable {
		ChunkedLongArray cla = new ChunkedLongArray();
		ChunkedLongArray cla2 = new ChunkedLongArray();
		
		cla.add(-10); cla.add(10000000l); cla2.add(42); cla.set(1,24); cla.add(111); cla.add(72); cla.add(Long.MAX_VALUE); cla.add(Long.MIN_VALUE);
		
		String result = "";
		result += cla.get(0);
		result += cla.get(1);
		result += cla.get(2);
		result += cla.size();
		result += cla.toString();
		result += cla.capacity();
		result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
		cla2.set(cla, 10, 10,12);
		cla.sort();
		result += cla.toString();
		result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
		cla.set(cla2, 20, 3,7);
		result += cla.toString();
		result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
		cla2.set(cla, 100, 40,1);
		result += cla.toString();
		result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
		cla.add(-5);
		cla.sort();
		result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
		result += cla.size();
		result += cla.capacity();
		result += cla.toString();
		result += cla.toString();
		try{
			result += cla2.get(0); result += cla2.get(1); result += cla2.get(2); result += cla2.get(3); result += cla2.get(4); result += cla.get(5);
		} catch(ArrayIndexOutOfBoundsException e){result += e.getMessage();result+= e.getClass().getCanonicalName();}
		result += cla2.toString();
		
		try{
			cla.get(1000);
			fail();
		} catch(ArrayIndexOutOfBoundsException e){result += e.getMessage();result+= e.getClass().getCanonicalName();}
		
		assertEquals("-10241116ChunkedLongArray(6 entries, 1 chunks, 8MB)1048576-1024111729223372036854775807-9223372036854775808ChunkedLongArray(6 entries, 1 chunks, 8MB)-9223372036854775808-1024721119223372036854775807ChunkedLongArray(6 entries, 1 chunks, 8MB)-9223372036854775808-1024000ChunkedLongArray(6 entries, 1 chunks, 8MB)-9223372036854775808-1024000-9223372036854775808-10-500071048576ChunkedLongArray(7 entries, 1 chunks, 8MB)ChunkedLongArray(7 entries, 1 chunks, 8MB)42Index 1 requested with array length 1java.lang.ArrayIndexOutOfBoundsExceptionChunkedLongArray(1 entries, 1 chunks, 8MB)Index 1000 requested with array length 7java.lang.ArrayIndexOutOfBoundsException", result);
	}

	@Test(timeout = 4000)
	public void test_5_2102() throws Throwable {
		 ChunkedLongArray cla = new ChunkedLongArray();
    ChunkedLongArray cla2 = new ChunkedLongArray();
    
    cla.add(-10); cla.add(10000000l); cla2.add(42); cla.set(1,24); cla.add(111); cla.add(72); cla.add(Long.MAX_VALUE); cla.add(Long.MIN_VALUE);
    
    String result = "";
    result += cla.get(0);
    result += cla.get(1);
    result += cla.get(2);
    result += cla.size();
    result += cla.toString();
    result += cla.capacity();
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
    try{
    cla2.set(cla, 10, 10,Integer.MAX_VALUE);
    }catch(Exception e){}
    cla.sort();
    result += cla.toString();
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);

    result += cla.toString();
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
    cla2.set(cla, 4, 2,1);
    result += cla.toString();
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
    cla.add(-5);
    cla.sort();
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5);
    result += cla.size();
    result += cla.capacity();
    result += cla.toString();
    result += cla.toString();
    try{
        result += cla2.get(0); result += cla2.get(1); result += cla2.get(2); result += cla2.get(3); result += cla2.get(4); result += cla.get(5);
    } catch(ArrayIndexOutOfBoundsException e){result += e.getMessage();result+= e.getClass().getCanonicalName();}
    result += cla2.toString();
    
    try{
        cla.get(1000);
        fail();
    } catch(ArrayIndexOutOfBoundsException e){result += e.getMessage();result+= e.getClass().getCanonicalName();}
    
    assertEquals("1119563883", "" + result.hashCode());
	}

	@Test(timeout = 4000)
	public void test_6_2103() throws Throwable {
		ChunkedLongArray cla = new ChunkedLongArray();
    ChunkedLongArray cla2 = new ChunkedLongArray();
    
    cla.add(-10); cla.add(10000000l); cla.set(1,24); cla.set(0,42); cla.add(Long.MAX_VALUE); cla.add(Long.MIN_VALUE); cla.add(7); cla.add(70);
    cla2.add(142);
    
    String result = "";
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    try{
      cla2.set(cla, 10, 10,Integer.MAX_VALUE);
    }catch(Exception e){}
    result += cla2.get(0); result += cla2.get(1); result += cla2.get(2); result += cla2.get(3); result += cla2.get(4); result += cla2.get(5); result += cla2.toString(); result += cla2.capacity();
    cla2.set(cla, 4, 0, 5);
    result += cla2.get(0); result += cla2.get(1); result += cla2.get(2); result += cla2.get(3); result += cla2.get(4); result += cla2.get(5); result += cla2.toString(); result += cla2.capacity();
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    
    cla2.set(cla, 40, 20,100);
    result += cla2.get(0); result += cla2.get(1); result += cla2.get(2); result += cla2.get(3); result += cla2.get(4); result += cla2.get(5); result += cla2.toString(); result += cla2.capacity();
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    
    cla.add(-5);
    cla.sort();
    
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();

    
    assertEquals("2115836544", "" + result.hashCode());
	}

	@Test(timeout = 4000)
	public void test_7_2104() throws Throwable {
		ChunkedLongArray cla = new ChunkedLongArray();
    ChunkedLongArray cla2 = new ChunkedLongArray();
    
    cla.add(-10); cla.add(10000000l); cla.set(1,24); cla.set(0,42); cla.add(Long.MAX_VALUE); cla.add(Long.MIN_VALUE); cla.add(7); cla.add(70);
    cla2.add(142);
    
    String result = "";
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    try{
      cla2.set(cla, 10, 10,0);
    }catch(Exception e){}
    result += cla2.get(0);  result += cla2.toString(); result += cla2.capacity();
    cla2.set(cla, 4, 0, 500);
    result += cla2.get(0); result += cla2.toString(); result += cla2.capacity();
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    
    cla2.set(cla, 40, 20,-2);
    result += cla2.get(0);result += cla2.toString(); result += cla2.capacity();
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    
    cla.add(-5);
    cla.sort();
    
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();

    
    assertEquals("1794066150", "" + result.hashCode());
	}

	@Test(timeout = 4000)
	public void test_8_2105() throws Throwable {
		ChunkedLongArray cla = new ChunkedLongArray(7);
    ChunkedLongArray cla2 = new ChunkedLongArray(1);
    
    cla.add(-10); cla.add(10000000l); cla.set(1,24); cla.set(0,42); cla.add(Long.MAX_VALUE); cla.add(Long.MIN_VALUE); cla.add(7); cla.add(70);
    cla2.add(142);
    
    long[] data = new long[4];
    data[0] = 42; data[1] = 24; data[3] = Long.MAX_VALUE; 
    
    cla2.set(data, 1, 10,1);
    cla2.set(data, 1, 10,2);
    cla2.set(data, 1, 10,3);
    //cla2.set(data, 1, 10,4);
    cla2.set(data, 2, 1,1);
    cla2.set(data, 2, 1,2);
    cla2.set(data, 0, 1,3);
    cla2.set(data, 0, 1,4);
    String result = "";
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    try{
      cla2.set(data, 10, 10,0);
    }catch(Exception e){}
    result += cla2.get(0);  result += cla2.toString(); result += cla2.capacity();
    try{
      cla2.set(data, 4, 0, 500);
    fail();
    }catch(ArrayIndexOutOfBoundsException e){}
    result += cla2.get(0); result += cla2.toString(); result += cla2.capacity();
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    
    cla2.set(data, 40, 20,-2);
    result += cla2.get(0);result += cla2.toString(); result += cla2.capacity();
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    
    cla.add(-5);
    cla.sort();
    
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();

    
    assertEquals("1088510076", "" + result.hashCode());
	}

	@Test(timeout = 4000)
	public void test_9_2106() throws Throwable {
		ChunkedLongArray cla = new ChunkedLongArray(7);
    ChunkedLongArray cla2 = new ChunkedLongArray(1);
    
    cla.add(-10); cla.add(10000000l); cla.set(1,24); cla.set(0,42); cla.add(Long.MAX_VALUE); cla.add(Long.MIN_VALUE); cla.add(7); cla.add(70);
    cla2.add(142);
    String result = "";

    long[] data = new long[4];
    data[0] = 42; data[1] = 24; data[3] = Long.MAX_VALUE; 
    
    cla2.set(data, 1, 10,1);
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    result += cla2.get(0);  result += cla2.toString(); result += cla2.capacity();
    cla2.set(data, 1, 10,2);
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    result += cla2.get(0);  result += cla2.toString(); result += cla2.capacity();
    cla2.set(data, 1, 10,3);
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    result += cla2.get(0);  result += cla2.toString(); result += cla2.capacity();
    //cla2.set(data, 1, 10,4);
    cla2.set(data, 2, 1,1);
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    result += cla2.get(0); result += cla2.toString(); result += cla2.capacity();
    cla2.set(data, 2, 1,2);
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    result += cla2.get(0); result += cla2.toString(); result += cla2.capacity();
    cla2.set(data, 0, 1,3);
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    result += cla2.get(0);  result += cla2.toString(); result += cla2.capacity();
    cla2.set(data, 0, 1,4);
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    result += cla2.get(0);  result += cla2.toString(); result += cla2.capacity();
    try{
      cla2.set(data, 10, 10,0);
    }catch(Exception e){}
    result += cla2.get(0);  result += cla2.toString(); result += cla2.capacity();
    try{
      cla2.set(data, 4, 0, 500);
    fail();
    }catch(ArrayIndexOutOfBoundsException e){}
    result += cla2.get(0); result += cla2.toString(); result += cla2.capacity();
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    
    cla2.set(data, 40, 20,-2);
    result += cla2.get(0);result += cla2.toString(); result += cla2.capacity();
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    
    cla.add(-5);
    cla.sort();
    
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();

    
    assertEquals("-260141292", "" + result.hashCode());
	}

	@Test(timeout = 4000)
	public void test_10_2107() throws Throwable {
		ChunkedLongArray cla = new ChunkedLongArray(7);
    ChunkedLongArray cla2 = new ChunkedLongArray(1);
    
    cla.add(-10); cla.add(10000000l); cla.set(1,24); cla.set(0,42); cla.add(Long.MAX_VALUE); cla.add(Long.MIN_VALUE); cla.add(7); cla.add(70);
    cla2.add(142);
    String result = "";

    long[] data = new long[4];
    data[0] = 42; data[1] = 24; data[3] = Long.MAX_VALUE; 
    
    cla.set(cla2, 1, 10,1);
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    result += cla2.get(0);  result += cla2.toString(); result += cla2.capacity();
    
    cla.set(cla2, 0, 0,0);
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    result += cla2.get(0);  result += cla2.toString(); result += cla2.capacity();
    
    cla2.set(cla, 1, 10,1);
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    result += cla2.get(0);  result += cla2.toString(); result += cla2.capacity();
    cla2.set(cla, 1, 10,2);
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    result += cla2.get(0);  result += cla2.toString(); result += cla2.capacity();
    cla2.set(cla, 1, 10,0);
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    result += cla2.get(0);  result += cla2.toString(); result += cla2.capacity();
    //cla2.set(data, 1, 10,4);
    cla2.set(cla, 2, 1,1);
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    result += cla2.get(0); result += cla2.toString(); result += cla2.capacity();
    cla2.set(cla, 2, 1,0);
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    result += cla2.get(0); result += cla2.toString(); result += cla2.capacity();
    cla2.set(cla, 0, 1,3);
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    result += cla2.get(0);  result += cla2.toString(); result += cla2.capacity();
    cla2.set(cla, 0, 1,4);
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    result += cla2.get(0);  result += cla2.toString(); result += cla2.capacity();
    try{
      cla2.set(cla, 10, 10,0);
    }catch(Exception e){}
    result += cla2.get(0);  result += cla2.toString(); result += cla2.capacity();
    try{
      cla2.set(cla, 4, 0, 500);
    fail();
    }catch(IndexOutOfBoundsException e){}
    result += cla2.get(0); result += cla2.toString(); result += cla2.capacity();
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    
    cla2.set(cla, 40, 20,-2);
    result += cla2.get(0);result += cla2.toString(); result += cla2.capacity();
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();
    
    cla.add(-5);
    cla.sort();
    
    result += cla.get(0); result += cla.get(1); result += cla.get(2); result += cla.get(3); result += cla.get(4); result += cla.get(5); result += cla.toString(); result += cla.capacity();

    
    assertEquals("-137159526", "" + result.hashCode());
	}

	@Test(timeout = 4000)
	public void test_11_2146() throws Throwable {
		ChunkedLongArray  cla = new ChunkedLongArray();
		Assert.assertTrue(cla.getClass().getDeclaredField("chunks").toString().contains("final"));
	}

	@Test(timeout = 4000)
	public void test_12_2151() throws Throwable {
		ChunkedLongArray  cla = new ChunkedLongArray();
		Assert.assertNotNull(cla.getClass().getDeclaredField("locale"));
		
	}

	@Test(timeout = 4000)
	public void test_13_2153() throws Throwable {
		ChunkedLongArray  cla = new ChunkedLongArray();
		Assert.assertFalse(cla.getClass().getDeclaredMethod("quickSort").isAccessible());
	}

	@Test(timeout = 4000)
	public void test_14_2155() throws Throwable {
		ChunkedLongArray  cla = new ChunkedLongArray();
		Assert.assertTrue(java.lang.reflect.Modifier.isFinal(cla.getClass().getDeclaredField("chunkBits").getModifiers()));
	}

	@Test(timeout = 4000)
	public void test_15_2156() throws Throwable {
		// test here!
		ChunkedLongArray  cla = new ChunkedLongArray();	
		Assert.assertTrue(java.lang.reflect.Modifier.isFinal(cla.getClass().getDeclaredField("offsetMask").getModifiers()));
	}

	@Test(timeout = 4000)
	public void test_16_2158() throws Throwable {
		// test here!
		
	    ChunkedLongArray cla = new ChunkedLongArray();
		cla.add(1); cla.add(-1); cla.set(1,1); 
		
		String result = "";
	    result += cla.get(0);
		assertEquals("1", result);
	}

}
