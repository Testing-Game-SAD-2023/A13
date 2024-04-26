package mypackage;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0_it2_livello2_it4_livello3 {

  public static boolean debug = false;

  @Test
  public void test1() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test1"); }


    ParameterParser parameterParser0 = new ParameterParser();
    boolean b1 = parameterParser0.isLowerCaseNames();
    char[] char_array3 = new char[] { ' ' };
    // The following exception was thrown during execution in test generation
    try {
    java.util.Map map7 = parameterParser0.parse(char_array3, 0, (int)(short)100, '#');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b1 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array3);

  }

  @Test
  public void test2() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test2"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", 'a');
    ParameterParser parameterParser4 = new ParameterParser();
    java.util.Map map7 = parameterParser4.parse("", 'a');
    char[] char_array14 = new char[] { '4', 'a', 'a', '#', 'a', ' ' };
    java.util.Map map16 = parameterParser4.parse(char_array14, '4');
    java.util.Map map18 = parameterParser0.parse(char_array14, ' ');
    ParameterParser parameterParser19 = new ParameterParser();
    java.util.Map map22 = parameterParser19.parse("", 'a');
    ParameterParser parameterParser23 = new ParameterParser();
    java.util.Map map26 = parameterParser23.parse("", 'a');
    char[] char_array33 = new char[] { '4', 'a', 'a', '#', 'a', ' ' };
    java.util.Map map35 = parameterParser23.parse(char_array33, '4');
    java.util.Map map37 = parameterParser19.parse(char_array33, ' ');
    ParameterParser parameterParser38 = new ParameterParser();
    java.util.Map map41 = parameterParser38.parse("", 'a');
    ParameterParser parameterParser42 = new ParameterParser();
    java.util.Map map45 = parameterParser42.parse("", 'a');
    char[] char_array52 = new char[] { '4', 'a', 'a', '#', 'a', ' ' };
    java.util.Map map54 = parameterParser42.parse(char_array52, '4');
    java.util.Map map56 = parameterParser38.parse(char_array52, ' ');
    java.util.Map map58 = parameterParser19.parse(char_array52, 'a');
    java.util.Map map60 = parameterParser0.parse(char_array52, '#');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array33);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map35);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map41);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map45);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array52);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map54);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map56);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map58);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map60);

  }

  @Test
  public void test3() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test3"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", 'a');
    ParameterParser parameterParser4 = new ParameterParser();
    java.util.Map map7 = parameterParser4.parse("", 'a');
    char[] char_array14 = new char[] { '4', 'a', 'a', '#', 'a', ' ' };
    java.util.Map map16 = parameterParser4.parse(char_array14, '4');
    java.util.Map map18 = parameterParser0.parse(char_array14, ' ');
    ParameterParser parameterParser19 = new ParameterParser();
    java.util.Map map22 = parameterParser19.parse("", 'a');
    ParameterParser parameterParser23 = new ParameterParser();
    java.util.Map map26 = parameterParser23.parse("", 'a');
    char[] char_array33 = new char[] { '4', 'a', 'a', '#', 'a', ' ' };
    java.util.Map map35 = parameterParser23.parse(char_array33, '4');
    java.util.Map map37 = parameterParser19.parse(char_array33, ' ');
    java.util.Map map39 = parameterParser0.parse(char_array33, 'a');
    ParameterParser parameterParser40 = new ParameterParser();
    java.util.Map map43 = parameterParser40.parse("", 'a');
    ParameterParser parameterParser44 = new ParameterParser();
    java.util.Map map47 = parameterParser44.parse("", 'a');
    char[] char_array54 = new char[] { '4', 'a', 'a', '#', 'a', ' ' };
    java.util.Map map56 = parameterParser44.parse(char_array54, '4');
    java.util.Map map58 = parameterParser40.parse(char_array54, ' ');
    java.util.Map map60 = parameterParser0.parse(char_array54, 'a');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array33);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map35);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map39);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map43);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map47);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array54);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map56);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map58);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map60);

  }

  @Test
  public void test4() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test4"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", 'a');
    char[] char_array10 = new char[] { '4', 'a', 'a', '#', 'a', ' ' };
    java.util.Map map12 = parameterParser0.parse(char_array10, '4');
    parameterParser0.setLowerCaseNames(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map12);

  }

  @Test
  public void test5() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test5"); }


    ParameterParser parameterParser0 = new ParameterParser();
    parameterParser0.setLowerCaseNames(true);
    boolean b3 = parameterParser0.isLowerCaseNames();
    java.util.Map map6 = parameterParser0.parse("hi!", 'a');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map6);

  }

  @Test
  public void test6() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test6"); }


    ParameterParser parameterParser0 = new ParameterParser();
    boolean b1 = parameterParser0.isLowerCaseNames();
    boolean b2 = parameterParser0.isLowerCaseNames();
    java.util.Map map5 = parameterParser0.parse("", ' ');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b1 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map5);

  }

  @Test
  public void test7() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test7"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", '4');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);

  }

  @Test
  public void test8() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test8"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", 'a');
    ParameterParser parameterParser4 = new ParameterParser();
    java.util.Map map7 = parameterParser4.parse("", 'a');
    char[] char_array14 = new char[] { '4', 'a', 'a', '#', 'a', ' ' };
    java.util.Map map16 = parameterParser4.parse(char_array14, '4');
    java.util.Map map18 = parameterParser0.parse(char_array14, ' ');
    ParameterParser parameterParser20 = new ParameterParser();
    java.util.Map map23 = parameterParser20.parse("", 'a');
    char[] char_array30 = new char[] { '4', 'a', 'a', '#', 'a', ' ' };
    java.util.Map map32 = parameterParser20.parse(char_array30, '4');
    java.util.Map map33 = parameterParser0.parse("hi!", char_array30);
    ParameterParser parameterParser34 = new ParameterParser();
    java.util.Map map37 = parameterParser34.parse("", 'a');
    ParameterParser parameterParser38 = new ParameterParser();
    java.util.Map map41 = parameterParser38.parse("", 'a');
    char[] char_array48 = new char[] { '4', 'a', 'a', '#', 'a', ' ' };
    java.util.Map map50 = parameterParser38.parse(char_array48, '4');
    java.util.Map map52 = parameterParser34.parse(char_array48, ' ');
    java.util.Map map56 = parameterParser0.parse(char_array48, (int)(byte)100, 1, '#');
    ParameterParser parameterParser57 = new ParameterParser();
    java.util.Map map60 = parameterParser57.parse("", 'a');
    ParameterParser parameterParser61 = new ParameterParser();
    java.util.Map map64 = parameterParser61.parse("", 'a');
    char[] char_array71 = new char[] { '4', 'a', 'a', '#', 'a', ' ' };
    java.util.Map map73 = parameterParser61.parse(char_array71, '4');
    java.util.Map map75 = parameterParser57.parse(char_array71, ' ');
    ParameterParser parameterParser76 = new ParameterParser();
    java.util.Map map79 = parameterParser76.parse("", 'a');
    ParameterParser parameterParser80 = new ParameterParser();
    java.util.Map map83 = parameterParser80.parse("", 'a');
    char[] char_array90 = new char[] { '4', 'a', 'a', '#', 'a', ' ' };
    java.util.Map map92 = parameterParser80.parse(char_array90, '4');
    java.util.Map map94 = parameterParser76.parse(char_array90, ' ');
    java.util.Map map96 = parameterParser57.parse(char_array90, 'a');
    java.util.Map map98 = parameterParser0.parse(char_array90, '4');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map33);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map41);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array48);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map50);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map52);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map56);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map60);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map64);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array71);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map73);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map75);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map79);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map83);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array90);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map92);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map94);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map96);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map98);

  }

}
