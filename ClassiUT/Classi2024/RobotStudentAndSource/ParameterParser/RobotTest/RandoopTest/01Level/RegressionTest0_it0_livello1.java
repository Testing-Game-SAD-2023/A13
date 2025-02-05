package mypackage;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0_it0_livello1 {

  public static boolean debug = false;

  @Test
  public void test1() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test1"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array4 = new char[] { 'a', 'a' };
    java.util.Map map5 = parameterParser0.parse("hi!", char_array4);
    java.util.Map map8 = parameterParser0.parse("hi!", ' ');
    char[] char_array14 = new char[] { ' ', '#', 'a', '4', '#' };
    // The following exception was thrown during execution in test generation
    try {
    java.util.Map map18 = parameterParser0.parse(char_array14, 0, (int)' ', '4');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array14);

  }

  @Test
  public void test2() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test2"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array4 = new char[] { 'a', 'a' };
    java.util.Map map5 = parameterParser0.parse("hi!", char_array4);
    boolean b6 = parameterParser0.isLowerCaseNames();
    ParameterParser parameterParser8 = new ParameterParser();
    char[] char_array12 = new char[] { 'a', 'a' };
    java.util.Map map13 = parameterParser8.parse("hi!", char_array12);
    java.util.Map map16 = parameterParser8.parse("hi!", ' ');
    char[] char_array17 = new char[] {  };
    java.util.Map map21 = parameterParser8.parse(char_array17, (int)(short)100, 0, ' ');
    java.util.Map map22 = parameterParser0.parse("hi!", char_array17);
    ParameterParser parameterParser23 = new ParameterParser();
    char[] char_array27 = new char[] { 'a', 'a' };
    java.util.Map map28 = parameterParser23.parse("hi!", char_array27);
    boolean b29 = parameterParser23.isLowerCaseNames();
    ParameterParser parameterParser31 = new ParameterParser();
    char[] char_array35 = new char[] { 'a', 'a' };
    java.util.Map map36 = parameterParser31.parse("hi!", char_array35);
    java.util.Map map39 = parameterParser31.parse("hi!", ' ');
    char[] char_array40 = new char[] {  };
    java.util.Map map44 = parameterParser31.parse(char_array40, (int)(short)100, 0, ' ');
    java.util.Map map45 = parameterParser23.parse("hi!", char_array40);
    // The following exception was thrown during execution in test generation
    try {
    java.util.Map map49 = parameterParser0.parse(char_array40, (int)' ', (int)'#', ' ');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array27);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b29 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array35);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map36);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map39);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array40);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map44);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map45);

  }

  @Test
  public void test3() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test3"); }


    ParameterParser parameterParser0 = new ParameterParser();
    parameterParser0.setLowerCaseNames(false);
    java.util.Map map5 = parameterParser0.parse("hi!", 'a');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map5);

  }

  @Test
  public void test4() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test4"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array4 = new char[] { 'a', 'a' };
    java.util.Map map5 = parameterParser0.parse("hi!", char_array4);
    java.util.Map map8 = parameterParser0.parse("hi!", ' ');
    char[] char_array9 = new char[] {  };
    java.util.Map map13 = parameterParser0.parse(char_array9, (int)(short)100, 0, ' ');
    ParameterParser parameterParser15 = new ParameterParser();
    char[] char_array19 = new char[] { 'a', 'a' };
    java.util.Map map20 = parameterParser15.parse("hi!", char_array19);
    java.util.Map map21 = parameterParser0.parse("", char_array19);
    java.util.Map map24 = parameterParser0.parse("hi!", '#');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map24);

  }

  @Test
  public void test5() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test5"); }


    ParameterParser parameterParser0 = new ParameterParser();
    parameterParser0.setLowerCaseNames(false);
    parameterParser0.setLowerCaseNames(true);

  }

  @Test
  public void test6() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test6"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array4 = new char[] { 'a', 'a' };
    java.util.Map map5 = parameterParser0.parse("hi!", char_array4);
    boolean b6 = parameterParser0.isLowerCaseNames();
    parameterParser0.setLowerCaseNames(true);
    ParameterParser parameterParser9 = new ParameterParser();
    char[] char_array13 = new char[] { 'a', 'a' };
    java.util.Map map14 = parameterParser9.parse("hi!", char_array13);
    java.util.Map map17 = parameterParser9.parse("hi!", ' ');
    char[] char_array18 = new char[] {  };
    java.util.Map map22 = parameterParser9.parse(char_array18, (int)(short)100, 0, ' ');
    ParameterParser parameterParser24 = new ParameterParser();
    char[] char_array28 = new char[] { 'a', 'a' };
    java.util.Map map29 = parameterParser24.parse("hi!", char_array28);
    java.util.Map map30 = parameterParser9.parse("", char_array28);
    ParameterParser parameterParser32 = new ParameterParser();
    char[] char_array36 = new char[] { 'a', 'a' };
    java.util.Map map37 = parameterParser32.parse("hi!", char_array36);
    java.util.Map map40 = parameterParser32.parse("hi!", ' ');
    char[] char_array41 = new char[] {  };
    java.util.Map map45 = parameterParser32.parse(char_array41, (int)(short)100, 0, ' ');
    ParameterParser parameterParser47 = new ParameterParser();
    char[] char_array51 = new char[] { 'a', 'a' };
    java.util.Map map52 = parameterParser47.parse("hi!", char_array51);
    java.util.Map map53 = parameterParser32.parse("", char_array51);
    java.util.Map map54 = parameterParser9.parse("hi!", char_array51);
    java.util.Map map56 = parameterParser0.parse(char_array51, ' ');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array36);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map40);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array41);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map45);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array51);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map52);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map53);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map54);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map56);

  }

}
