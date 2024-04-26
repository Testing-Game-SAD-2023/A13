package mypackage;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0_it4_livello3_it5_livello4 {

  public static boolean debug = false;

  @Test
  public void test01() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test01"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array2 = new char[] {  };
    java.util.Map map3 = parameterParser0.parse("hi!", char_array2);
    char[] char_array4 = new char[] {  };
    // The following exception was thrown during execution in test generation
    try {
    java.util.Map map8 = parameterParser0.parse(char_array4, 0, (int)(short)100, '#');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array4);

  }

  @Test
  public void test02() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test02"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array2 = new char[] {  };
    java.util.Map map3 = parameterParser0.parse("hi!", char_array2);
    char[] char_array7 = new char[] { '4', '#', '4' };
    java.util.Map map9 = parameterParser0.parse(char_array7, 'a');
    char[] char_array10 = null;
    java.util.Map map14 = parameterParser0.parse(char_array10, (-1), 1, 'a');
    ParameterParser parameterParser15 = new ParameterParser();
    char[] char_array17 = new char[] {  };
    java.util.Map map18 = parameterParser15.parse("hi!", char_array17);
    ParameterParser parameterParser19 = new ParameterParser();
    char[] char_array21 = new char[] {  };
    java.util.Map map22 = parameterParser19.parse("hi!", char_array21);
    char[] char_array26 = new char[] { '4', '#', '4' };
    java.util.Map map28 = parameterParser19.parse(char_array26, 'a');
    java.util.Map map32 = parameterParser15.parse(char_array26, (int)'#', 0, '#');
    char[] char_array34 = new char[] { '4' };
    java.util.Map map36 = parameterParser15.parse(char_array34, 'a');
    // The following exception was thrown during execution in test generation
    try {
    java.util.Map map40 = parameterParser0.parse(char_array34, (-1), (int)'a', ' ');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array34);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map36);

  }

  @Test
  public void test03() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test03"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array2 = new char[] {  };
    java.util.Map map3 = parameterParser0.parse("hi!", char_array2);
    ParameterParser parameterParser4 = new ParameterParser();
    char[] char_array6 = new char[] {  };
    java.util.Map map7 = parameterParser4.parse("hi!", char_array6);
    char[] char_array11 = new char[] { '4', '#', '4' };
    java.util.Map map13 = parameterParser4.parse(char_array11, 'a');
    java.util.Map map17 = parameterParser0.parse(char_array11, (int)'#', 0, '#');
    boolean b18 = parameterParser0.isLowerCaseNames();
    ParameterParser parameterParser19 = new ParameterParser();
    char[] char_array21 = new char[] {  };
    java.util.Map map22 = parameterParser19.parse("hi!", char_array21);
    ParameterParser parameterParser23 = new ParameterParser();
    char[] char_array25 = new char[] {  };
    java.util.Map map26 = parameterParser23.parse("hi!", char_array25);
    char[] char_array30 = new char[] { '4', '#', '4' };
    java.util.Map map32 = parameterParser23.parse(char_array30, 'a');
    java.util.Map map36 = parameterParser19.parse(char_array30, (int)'#', 0, '#');
    java.util.Map map38 = parameterParser0.parse(char_array30, 'a');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b18 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map36);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map38);

  }

  @Test
  public void test04() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test04"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array2 = new char[] {  };
    java.util.Map map3 = parameterParser0.parse("hi!", char_array2);
    char[] char_array7 = new char[] { '4', '#', '4' };
    java.util.Map map9 = parameterParser0.parse(char_array7, 'a');
    char[] char_array10 = null;
    java.util.Map map14 = parameterParser0.parse(char_array10, (-1), 1, 'a');
    parameterParser0.setLowerCaseNames(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map14);

  }

  @Test
  public void test05() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test05"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array2 = new char[] {  };
    java.util.Map map3 = parameterParser0.parse("hi!", char_array2);
    ParameterParser parameterParser4 = new ParameterParser();
    char[] char_array6 = new char[] {  };
    java.util.Map map7 = parameterParser4.parse("hi!", char_array6);
    char[] char_array11 = new char[] { '4', '#', '4' };
    java.util.Map map13 = parameterParser4.parse(char_array11, 'a');
    java.util.Map map17 = parameterParser0.parse(char_array11, (int)'#', 0, '#');
    char[] char_array19 = new char[] { '4' };
    java.util.Map map21 = parameterParser0.parse(char_array19, 'a');
    java.util.Map map24 = parameterParser0.parse("", 'a');
    java.util.Map map27 = parameterParser0.parse("hi!", 'a');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map27);

  }

  @Test
  public void test06() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test06"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array2 = new char[] {  };
    java.util.Map map3 = parameterParser0.parse("hi!", char_array2);
    char[] char_array7 = new char[] { '4', '#', '4' };
    java.util.Map map9 = parameterParser0.parse(char_array7, 'a');
    char[] char_array10 = null;
    java.util.Map map14 = parameterParser0.parse(char_array10, (-1), 1, 'a');
    ParameterParser parameterParser15 = new ParameterParser();
    char[] char_array17 = new char[] {  };
    java.util.Map map18 = parameterParser15.parse("hi!", char_array17);
    ParameterParser parameterParser19 = new ParameterParser();
    char[] char_array21 = new char[] {  };
    java.util.Map map22 = parameterParser19.parse("hi!", char_array21);
    char[] char_array26 = new char[] { '4', '#', '4' };
    java.util.Map map28 = parameterParser19.parse(char_array26, 'a');
    java.util.Map map32 = parameterParser15.parse(char_array26, (int)'#', 0, '#');
    java.util.Map map35 = parameterParser15.parse("", 'a');
    ParameterParser parameterParser36 = new ParameterParser();
    char[] char_array38 = new char[] {  };
    java.util.Map map39 = parameterParser36.parse("hi!", char_array38);
    ParameterParser parameterParser40 = new ParameterParser();
    char[] char_array42 = new char[] {  };
    java.util.Map map43 = parameterParser40.parse("hi!", char_array42);
    char[] char_array47 = new char[] { '4', '#', '4' };
    java.util.Map map49 = parameterParser40.parse(char_array47, 'a');
    java.util.Map map53 = parameterParser36.parse(char_array47, (int)'#', 0, '#');
    char[] char_array55 = new char[] { '4' };
    java.util.Map map57 = parameterParser36.parse(char_array55, 'a');
    java.util.Map map59 = parameterParser15.parse(char_array55, '#');
    // The following exception was thrown during execution in test generation
    try {
    java.util.Map map63 = parameterParser0.parse(char_array55, (int)' ', (int)(byte)100, '#');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map35);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array38);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map39);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array42);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map43);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array47);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map49);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map53);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array55);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map57);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map59);

  }

  @Test
  public void test07() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test07"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array2 = new char[] {  };
    java.util.Map map3 = parameterParser0.parse("hi!", char_array2);
    char[] char_array7 = new char[] { '4', '#', '4' };
    java.util.Map map9 = parameterParser0.parse(char_array7, 'a');
    java.util.Map map12 = parameterParser0.parse("", 'a');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map12);

  }

  @Test
  public void test08() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test08"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array2 = new char[] {  };
    java.util.Map map3 = parameterParser0.parse("hi!", char_array2);
    boolean b4 = parameterParser0.isLowerCaseNames();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);

  }

  @Test
  public void test09() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test09"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array2 = new char[] {  };
    java.util.Map map3 = parameterParser0.parse("hi!", char_array2);
    ParameterParser parameterParser4 = new ParameterParser();
    char[] char_array6 = new char[] {  };
    java.util.Map map7 = parameterParser4.parse("hi!", char_array6);
    char[] char_array11 = new char[] { '4', '#', '4' };
    java.util.Map map13 = parameterParser4.parse(char_array11, 'a');
    char[] char_array15 = null;
    java.util.Map map16 = parameterParser4.parse("", char_array15);
    ParameterParser parameterParser17 = new ParameterParser();
    char[] char_array19 = new char[] {  };
    java.util.Map map20 = parameterParser17.parse("hi!", char_array19);
    ParameterParser parameterParser21 = new ParameterParser();
    char[] char_array23 = new char[] {  };
    java.util.Map map24 = parameterParser21.parse("hi!", char_array23);
    char[] char_array28 = new char[] { '4', '#', '4' };
    java.util.Map map30 = parameterParser21.parse(char_array28, 'a');
    java.util.Map map34 = parameterParser17.parse(char_array28, (int)'#', 0, '#');
    char[] char_array36 = new char[] { '4' };
    java.util.Map map38 = parameterParser17.parse(char_array36, 'a');
    java.util.Map map40 = parameterParser4.parse(char_array36, 'a');
    java.util.Map map42 = parameterParser0.parse(char_array36, '4');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map34);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array36);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map38);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map40);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map42);

  }

  @Test
  public void test10() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test10"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array2 = new char[] {  };
    java.util.Map map3 = parameterParser0.parse("hi!", char_array2);
    char[] char_array7 = new char[] { '4', '#', '4' };
    java.util.Map map9 = parameterParser0.parse(char_array7, 'a');
    char[] char_array11 = null;
    java.util.Map map12 = parameterParser0.parse("", char_array11);
    ParameterParser parameterParser13 = new ParameterParser();
    char[] char_array15 = new char[] {  };
    java.util.Map map16 = parameterParser13.parse("hi!", char_array15);
    ParameterParser parameterParser17 = new ParameterParser();
    char[] char_array19 = new char[] {  };
    java.util.Map map20 = parameterParser17.parse("hi!", char_array19);
    char[] char_array24 = new char[] { '4', '#', '4' };
    java.util.Map map26 = parameterParser17.parse(char_array24, 'a');
    java.util.Map map30 = parameterParser13.parse(char_array24, (int)'#', 0, '#');
    char[] char_array32 = new char[] { '4' };
    java.util.Map map34 = parameterParser13.parse(char_array32, 'a');
    java.util.Map map36 = parameterParser0.parse(char_array32, 'a');
    char[] char_array37 = new char[] {  };
    java.util.Map map41 = parameterParser0.parse(char_array37, (int)(short)0, (int)(short)-1, 'a');
    java.util.Map map44 = parameterParser0.parse("", '4');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map34);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map36);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map41);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map44);

  }

  @Test
  public void test11() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test11"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array2 = new char[] {  };
    java.util.Map map3 = parameterParser0.parse("hi!", char_array2);
    ParameterParser parameterParser4 = new ParameterParser();
    char[] char_array6 = new char[] {  };
    java.util.Map map7 = parameterParser4.parse("hi!", char_array6);
    char[] char_array11 = new char[] { '4', '#', '4' };
    java.util.Map map13 = parameterParser4.parse(char_array11, 'a');
    java.util.Map map17 = parameterParser0.parse(char_array11, (int)'#', 0, '#');
    boolean b18 = parameterParser0.isLowerCaseNames();
    ParameterParser parameterParser19 = new ParameterParser();
    char[] char_array21 = new char[] {  };
    java.util.Map map22 = parameterParser19.parse("hi!", char_array21);
    ParameterParser parameterParser23 = new ParameterParser();
    char[] char_array25 = new char[] {  };
    java.util.Map map26 = parameterParser23.parse("hi!", char_array25);
    char[] char_array30 = new char[] { '4', '#', '4' };
    java.util.Map map32 = parameterParser23.parse(char_array30, 'a');
    java.util.Map map36 = parameterParser19.parse(char_array30, (int)'#', 0, '#');
    java.util.Map map39 = parameterParser19.parse("", 'a');
    ParameterParser parameterParser40 = new ParameterParser();
    char[] char_array42 = new char[] {  };
    java.util.Map map43 = parameterParser40.parse("hi!", char_array42);
    ParameterParser parameterParser44 = new ParameterParser();
    char[] char_array46 = new char[] {  };
    java.util.Map map47 = parameterParser44.parse("hi!", char_array46);
    char[] char_array51 = new char[] { '4', '#', '4' };
    java.util.Map map53 = parameterParser44.parse(char_array51, 'a');
    java.util.Map map57 = parameterParser40.parse(char_array51, (int)'#', 0, '#');
    char[] char_array59 = new char[] { '4' };
    java.util.Map map61 = parameterParser40.parse(char_array59, 'a');
    java.util.Map map63 = parameterParser19.parse(char_array59, '#');
    char[] char_array66 = new char[] { '4', '#' };
    java.util.Map map68 = parameterParser19.parse(char_array66, 'a');
    char[] char_array71 = new char[] { 'a' };
    java.util.Map map72 = parameterParser19.parse("hi!", char_array71);
    // The following exception was thrown during execution in test generation
    try {
    java.util.Map map76 = parameterParser0.parse(char_array71, 10, (int)'#', '#');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b18 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map36);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map39);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array42);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map43);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array46);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map47);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array51);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map53);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map57);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array59);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map61);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map63);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array66);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map68);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array71);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map72);

  }

  @Test
  public void test12() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test12"); }


    ParameterParser parameterParser0 = new ParameterParser();
    boolean b1 = parameterParser0.isLowerCaseNames();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b1 == false);

  }

  @Test
  public void test13() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test13"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array2 = new char[] {  };
    java.util.Map map3 = parameterParser0.parse("hi!", char_array2);
    ParameterParser parameterParser4 = new ParameterParser();
    char[] char_array6 = new char[] {  };
    java.util.Map map7 = parameterParser4.parse("hi!", char_array6);
    char[] char_array11 = new char[] { '4', '#', '4' };
    java.util.Map map13 = parameterParser4.parse(char_array11, 'a');
    java.util.Map map17 = parameterParser0.parse(char_array11, (int)'#', 0, '#');
    boolean b18 = parameterParser0.isLowerCaseNames();
    char[] char_array26 = new char[] { '#', ' ', 'a', '#', 'a', 'a' };
    java.util.Map map27 = parameterParser0.parse("hi!", char_array26);
    parameterParser0.setLowerCaseNames(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b18 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map27);

  }

  @Test
  public void test14() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test14"); }


    ParameterParser parameterParser0 = new ParameterParser();
    char[] char_array2 = new char[] {  };
    java.util.Map map3 = parameterParser0.parse("hi!", char_array2);
    ParameterParser parameterParser4 = new ParameterParser();
    char[] char_array6 = new char[] {  };
    java.util.Map map7 = parameterParser4.parse("hi!", char_array6);
    char[] char_array11 = new char[] { '4', '#', '4' };
    java.util.Map map13 = parameterParser4.parse(char_array11, 'a');
    java.util.Map map17 = parameterParser0.parse(char_array11, (int)'#', 0, '#');
    java.util.Map map20 = parameterParser0.parse("", 'a');
    ParameterParser parameterParser21 = new ParameterParser();
    char[] char_array23 = new char[] {  };
    java.util.Map map24 = parameterParser21.parse("hi!", char_array23);
    ParameterParser parameterParser25 = new ParameterParser();
    char[] char_array27 = new char[] {  };
    java.util.Map map28 = parameterParser25.parse("hi!", char_array27);
    char[] char_array32 = new char[] { '4', '#', '4' };
    java.util.Map map34 = parameterParser25.parse(char_array32, 'a');
    java.util.Map map38 = parameterParser21.parse(char_array32, (int)'#', 0, '#');
    char[] char_array40 = new char[] { '4' };
    java.util.Map map42 = parameterParser21.parse(char_array40, 'a');
    java.util.Map map44 = parameterParser0.parse(char_array40, '#');
    char[] char_array47 = new char[] { '4', '#' };
    java.util.Map map49 = parameterParser0.parse(char_array47, 'a');
    char[] char_array52 = new char[] { 'a' };
    java.util.Map map53 = parameterParser0.parse("hi!", char_array52);
    ParameterParser parameterParser54 = new ParameterParser();
    char[] char_array56 = new char[] {  };
    java.util.Map map57 = parameterParser54.parse("hi!", char_array56);
    char[] char_array61 = new char[] { '4', '#', '4' };
    java.util.Map map63 = parameterParser54.parse(char_array61, 'a');
    java.util.Map map67 = parameterParser0.parse(char_array61, 100, 0, '4');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array27);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map34);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map38);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array40);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map42);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map44);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array47);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map49);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array52);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map53);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array56);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map57);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array61);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map63);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map67);

  }

}
