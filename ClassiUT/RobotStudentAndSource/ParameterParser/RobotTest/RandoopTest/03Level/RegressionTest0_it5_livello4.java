package mypackage;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0_it5_livello4 {

  public static boolean debug = false;

  @Test
  public void test01() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test01"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", ' ');
    char[] char_array10 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map11 = parameterParser0.parse("hi!", char_array10);
    ParameterParser parameterParser12 = new ParameterParser();
    java.util.Map map15 = parameterParser12.parse("", ' ');
    char[] char_array22 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map23 = parameterParser12.parse("hi!", char_array22);
    // The following exception was thrown during execution in test generation
    try {
    java.util.Map map27 = parameterParser0.parse(char_array22, (-1), (int)(byte)1, '#');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map23);

  }

  @Test
  public void test02() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test02"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", ' ');
    parameterParser0.setLowerCaseNames(true);
    char[] char_array6 = null;
    java.util.Map map10 = parameterParser0.parse(char_array6, (int)(byte)-1, 0, '#');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map10);

  }

  @Test
  public void test03() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test03"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", ' ');
    parameterParser0.setLowerCaseNames(true);
    ParameterParser parameterParser6 = new ParameterParser();
    java.util.Map map9 = parameterParser6.parse("", ' ');
    parameterParser6.setLowerCaseNames(true);
    ParameterParser parameterParser13 = new ParameterParser();
    java.util.Map map16 = parameterParser13.parse("", ' ');
    parameterParser13.setLowerCaseNames(true);
    java.util.Map map21 = parameterParser13.parse("", '4');
    ParameterParser parameterParser22 = new ParameterParser();
    java.util.Map map25 = parameterParser22.parse("", ' ');
    ParameterParser parameterParser27 = new ParameterParser();
    java.util.Map map30 = parameterParser27.parse("", ' ');
    char[] char_array37 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map38 = parameterParser27.parse("hi!", char_array37);
    java.util.Map map39 = parameterParser22.parse("", char_array37);
    java.util.Map map43 = parameterParser13.parse(char_array37, (int)(short)100, (int)'a', 'a');
    java.util.Map map44 = parameterParser6.parse("", char_array37);
    // The following exception was thrown during execution in test generation
    try {
    java.util.Map map48 = parameterParser0.parse(char_array37, (int)'a', (int)(short)100, 'a');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map38);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map39);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map43);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map44);

  }

  @Test
  public void test04() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test04"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", ' ');
    char[] char_array10 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map11 = parameterParser0.parse("hi!", char_array10);
    ParameterParser parameterParser12 = new ParameterParser();
    java.util.Map map15 = parameterParser12.parse("", ' ');
    parameterParser12.setLowerCaseNames(true);
    java.util.Map map20 = parameterParser12.parse("", '4');
    ParameterParser parameterParser21 = new ParameterParser();
    java.util.Map map24 = parameterParser21.parse("", ' ');
    ParameterParser parameterParser26 = new ParameterParser();
    java.util.Map map29 = parameterParser26.parse("", ' ');
    char[] char_array36 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map37 = parameterParser26.parse("hi!", char_array36);
    java.util.Map map38 = parameterParser21.parse("", char_array36);
    java.util.Map map42 = parameterParser12.parse(char_array36, (int)(short)100, (int)'a', 'a');
    // The following exception was thrown during execution in test generation
    try {
    java.util.Map map46 = parameterParser0.parse(char_array36, (int)(short)-1, 0, '#');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array36);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map38);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map42);

  }

  @Test
  public void test05() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test05"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", ' ');
    parameterParser0.setLowerCaseNames(true);
    char[] char_array10 = new char[] { '4', '4', '4', 'a' };
    // The following exception was thrown during execution in test generation
    try {
    java.util.Map map14 = parameterParser0.parse(char_array10, (-1), 100, 'a');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array10);

  }

  @Test
  public void test06() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test06"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", ' ');
    parameterParser0.setLowerCaseNames(true);
    java.util.Map map8 = parameterParser0.parse("", '4');
    ParameterParser parameterParser9 = new ParameterParser();
    java.util.Map map12 = parameterParser9.parse("", ' ');
    ParameterParser parameterParser14 = new ParameterParser();
    java.util.Map map17 = parameterParser14.parse("", ' ');
    char[] char_array24 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map25 = parameterParser14.parse("hi!", char_array24);
    java.util.Map map26 = parameterParser9.parse("", char_array24);
    java.util.Map map30 = parameterParser0.parse(char_array24, (int)(short)100, (int)'a', 'a');
    char[] char_array31 = null;
    java.util.Map map33 = parameterParser0.parse(char_array31, ' ');
    boolean b34 = parameterParser0.isLowerCaseNames();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map33);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b34 == true);

  }

  @Test
  public void test07() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test07"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", ' ');
    parameterParser0.setLowerCaseNames(true);
    ParameterParser parameterParser6 = new ParameterParser();
    java.util.Map map9 = parameterParser6.parse("", ' ');
    ParameterParser parameterParser11 = new ParameterParser();
    java.util.Map map14 = parameterParser11.parse("", ' ');
    char[] char_array21 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map22 = parameterParser11.parse("hi!", char_array21);
    java.util.Map map23 = parameterParser6.parse("", char_array21);
    java.util.Map map27 = parameterParser0.parse(char_array21, 0, 0, '4');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map27);

  }

  @Test
  public void test08() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test08"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", ' ');
    parameterParser0.setLowerCaseNames(true);
    java.util.Map map8 = parameterParser0.parse("", '4');
    java.util.Map map11 = parameterParser0.parse("hi!", ' ');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map11);

  }

  @Test
  public void test09() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test09"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", ' ');
    parameterParser0.setLowerCaseNames(true);
    boolean b6 = parameterParser0.isLowerCaseNames();
    java.util.Map map9 = parameterParser0.parse("hi!", '4');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map9);

  }

  @Test
  public void test10() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test10"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", ' ');
    parameterParser0.setLowerCaseNames(true);
    boolean b6 = parameterParser0.isLowerCaseNames();
    ParameterParser parameterParser8 = new ParameterParser();
    java.util.Map map11 = parameterParser8.parse("", ' ');
    char[] char_array18 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map19 = parameterParser8.parse("hi!", char_array18);
    java.util.Map map20 = parameterParser0.parse("hi!", char_array18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map20);

  }

  @Test
  public void test11() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test11"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", ' ');
    ParameterParser parameterParser5 = new ParameterParser();
    java.util.Map map8 = parameterParser5.parse("", ' ');
    char[] char_array15 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map16 = parameterParser5.parse("hi!", char_array15);
    java.util.Map map17 = parameterParser0.parse("", char_array15);
    ParameterParser parameterParser19 = new ParameterParser();
    java.util.Map map22 = parameterParser19.parse("", ' ');
    parameterParser19.setLowerCaseNames(true);
    ParameterParser parameterParser26 = new ParameterParser();
    java.util.Map map29 = parameterParser26.parse("", ' ');
    parameterParser26.setLowerCaseNames(true);
    java.util.Map map34 = parameterParser26.parse("", '4');
    ParameterParser parameterParser35 = new ParameterParser();
    java.util.Map map38 = parameterParser35.parse("", ' ');
    ParameterParser parameterParser40 = new ParameterParser();
    java.util.Map map43 = parameterParser40.parse("", ' ');
    char[] char_array50 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map51 = parameterParser40.parse("hi!", char_array50);
    java.util.Map map52 = parameterParser35.parse("", char_array50);
    java.util.Map map56 = parameterParser26.parse(char_array50, (int)(short)100, (int)'a', 'a');
    java.util.Map map57 = parameterParser19.parse("", char_array50);
    java.util.Map map58 = parameterParser0.parse("hi!", char_array50);
    char[] char_array59 = null;
    java.util.Map map63 = parameterParser0.parse(char_array59, (int)(short)-1, (int)(short)100, '#');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map34);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map38);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map43);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array50);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map51);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map52);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map56);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map57);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map58);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map63);

  }

  @Test
  public void test12() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test12"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", ' ');
    ParameterParser parameterParser5 = new ParameterParser();
    java.util.Map map8 = parameterParser5.parse("", ' ');
    char[] char_array15 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map16 = parameterParser5.parse("hi!", char_array15);
    java.util.Map map17 = parameterParser0.parse("", char_array15);
    ParameterParser parameterParser19 = new ParameterParser();
    java.util.Map map22 = parameterParser19.parse("", ' ');
    parameterParser19.setLowerCaseNames(true);
    ParameterParser parameterParser26 = new ParameterParser();
    java.util.Map map29 = parameterParser26.parse("", ' ');
    parameterParser26.setLowerCaseNames(true);
    java.util.Map map34 = parameterParser26.parse("", '4');
    ParameterParser parameterParser35 = new ParameterParser();
    java.util.Map map38 = parameterParser35.parse("", ' ');
    ParameterParser parameterParser40 = new ParameterParser();
    java.util.Map map43 = parameterParser40.parse("", ' ');
    char[] char_array50 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map51 = parameterParser40.parse("hi!", char_array50);
    java.util.Map map52 = parameterParser35.parse("", char_array50);
    java.util.Map map56 = parameterParser26.parse(char_array50, (int)(short)100, (int)'a', 'a');
    java.util.Map map57 = parameterParser19.parse("", char_array50);
    java.util.Map map58 = parameterParser0.parse("hi!", char_array50);
    ParameterParser parameterParser59 = new ParameterParser();
    java.util.Map map62 = parameterParser59.parse("", ' ');
    parameterParser59.setLowerCaseNames(true);
    java.util.Map map67 = parameterParser59.parse("", '4');
    ParameterParser parameterParser68 = new ParameterParser();
    java.util.Map map71 = parameterParser68.parse("", ' ');
    ParameterParser parameterParser73 = new ParameterParser();
    java.util.Map map76 = parameterParser73.parse("", ' ');
    char[] char_array83 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map84 = parameterParser73.parse("hi!", char_array83);
    java.util.Map map85 = parameterParser68.parse("", char_array83);
    java.util.Map map89 = parameterParser59.parse(char_array83, (int)(short)100, (int)'a', 'a');
    java.util.Map map93 = parameterParser0.parse(char_array83, (int)' ', (int)(byte)1, ' ');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map34);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map38);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map43);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array50);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map51);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map52);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map56);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map57);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map58);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map62);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map67);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map71);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map76);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array83);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map84);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map85);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map89);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map93);

  }

  @Test
  public void test13() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test13"); }


    ParameterParser parameterParser0 = new ParameterParser();
    ParameterParser parameterParser1 = new ParameterParser();
    java.util.Map map4 = parameterParser1.parse("", ' ');
    parameterParser1.setLowerCaseNames(true);
    boolean b7 = parameterParser1.isLowerCaseNames();
    ParameterParser parameterParser8 = new ParameterParser();
    java.util.Map map11 = parameterParser8.parse("", ' ');
    parameterParser8.setLowerCaseNames(true);
    java.util.Map map16 = parameterParser8.parse("", '4');
    ParameterParser parameterParser17 = new ParameterParser();
    java.util.Map map20 = parameterParser17.parse("", ' ');
    ParameterParser parameterParser22 = new ParameterParser();
    java.util.Map map25 = parameterParser22.parse("", ' ');
    char[] char_array32 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map33 = parameterParser22.parse("hi!", char_array32);
    java.util.Map map34 = parameterParser17.parse("", char_array32);
    java.util.Map map38 = parameterParser8.parse(char_array32, (int)(short)100, (int)'a', 'a');
    java.util.Map map40 = parameterParser1.parse(char_array32, ' ');
    java.util.Map map42 = parameterParser0.parse(char_array32, '4');
    java.util.Map map45 = parameterParser0.parse("", '4');
    boolean b46 = parameterParser0.isLowerCaseNames();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map33);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map34);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map38);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map40);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map42);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map45);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b46 == false);

  }

  @Test
  public void test14() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test14"); }


    ParameterParser parameterParser0 = new ParameterParser();
    java.util.Map map3 = parameterParser0.parse("", ' ');
    parameterParser0.setLowerCaseNames(true);
    boolean b6 = parameterParser0.isLowerCaseNames();
    ParameterParser parameterParser7 = new ParameterParser();
    java.util.Map map10 = parameterParser7.parse("", ' ');
    parameterParser7.setLowerCaseNames(true);
    java.util.Map map15 = parameterParser7.parse("", '4');
    ParameterParser parameterParser16 = new ParameterParser();
    java.util.Map map19 = parameterParser16.parse("", ' ');
    ParameterParser parameterParser21 = new ParameterParser();
    java.util.Map map24 = parameterParser21.parse("", ' ');
    char[] char_array31 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map32 = parameterParser21.parse("hi!", char_array31);
    java.util.Map map33 = parameterParser16.parse("", char_array31);
    java.util.Map map37 = parameterParser7.parse(char_array31, (int)(short)100, (int)'a', 'a');
    java.util.Map map39 = parameterParser0.parse(char_array31, ' ');
    ParameterParser parameterParser41 = new ParameterParser();
    java.util.Map map44 = parameterParser41.parse("", ' ');
    ParameterParser parameterParser46 = new ParameterParser();
    java.util.Map map49 = parameterParser46.parse("", ' ');
    char[] char_array56 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map57 = parameterParser46.parse("hi!", char_array56);
    java.util.Map map58 = parameterParser41.parse("", char_array56);
    java.util.Map map59 = parameterParser0.parse("hi!", char_array56);
    ParameterParser parameterParser60 = new ParameterParser();
    java.util.Map map63 = parameterParser60.parse("", ' ');
    ParameterParser parameterParser65 = new ParameterParser();
    java.util.Map map68 = parameterParser65.parse("", ' ');
    char[] char_array75 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map76 = parameterParser65.parse("hi!", char_array75);
    java.util.Map map77 = parameterParser60.parse("", char_array75);
    java.util.Map map79 = parameterParser0.parse(char_array75, 'a');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array31);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map33);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map39);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map44);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map49);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array56);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map57);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map58);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map59);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map63);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map68);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array75);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map76);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map77);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map79);

  }

  @Test
  public void test15() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test15"); }


    ParameterParser parameterParser0 = new ParameterParser();
    parameterParser0.setLowerCaseNames(false);
    ParameterParser parameterParser3 = new ParameterParser();
    java.util.Map map6 = parameterParser3.parse("", ' ');
    parameterParser3.setLowerCaseNames(true);
    java.util.Map map11 = parameterParser3.parse("", '4');
    ParameterParser parameterParser12 = new ParameterParser();
    java.util.Map map15 = parameterParser12.parse("", ' ');
    ParameterParser parameterParser17 = new ParameterParser();
    java.util.Map map20 = parameterParser17.parse("", ' ');
    char[] char_array27 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map28 = parameterParser17.parse("hi!", char_array27);
    java.util.Map map29 = parameterParser12.parse("", char_array27);
    java.util.Map map33 = parameterParser3.parse(char_array27, (int)(short)100, (int)'a', 'a');
    java.util.Map map35 = parameterParser0.parse(char_array27, ' ');
    ParameterParser parameterParser37 = new ParameterParser();
    java.util.Map map40 = parameterParser37.parse("", ' ');
    parameterParser37.setLowerCaseNames(true);
    boolean b43 = parameterParser37.isLowerCaseNames();
    ParameterParser parameterParser44 = new ParameterParser();
    java.util.Map map47 = parameterParser44.parse("", ' ');
    parameterParser44.setLowerCaseNames(true);
    java.util.Map map52 = parameterParser44.parse("", '4');
    ParameterParser parameterParser53 = new ParameterParser();
    java.util.Map map56 = parameterParser53.parse("", ' ');
    ParameterParser parameterParser58 = new ParameterParser();
    java.util.Map map61 = parameterParser58.parse("", ' ');
    char[] char_array68 = new char[] { '4', 'a', '#', ' ', '#' };
    java.util.Map map69 = parameterParser58.parse("hi!", char_array68);
    java.util.Map map70 = parameterParser53.parse("", char_array68);
    java.util.Map map74 = parameterParser44.parse(char_array68, (int)(short)100, (int)'a', 'a');
    java.util.Map map76 = parameterParser37.parse(char_array68, ' ');
    java.util.Map map77 = parameterParser0.parse("hi!", char_array68);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array27);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map33);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map35);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map40);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b43 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map47);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map52);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map56);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map61);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(char_array68);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map69);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map70);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map74);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map76);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(map77);

  }

}
