package mypackage;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0_it0_livello1 {

  public static boolean debug = false;

  @Test
  public void test001() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test001"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setEncoding("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);

  }

  @Test
  public void test002() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test002"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    java.lang.String str2 = outputFormat0.getEncoding();
    outputFormat0.setSuppressDeclaration(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str2 + "' != '" + "UTF-8"+ "'", str2.equals("UTF-8"));

  }

  @Test
  public void test003() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test003"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    boolean b8 = outputFormat2.isSuppressDeclaration();
    boolean b9 = outputFormat2.isOmitEncoding();
    java.lang.String[] str_array11 = new java.lang.String[] { "hi!" };
    // The following exception was thrown during execution in test generation
    try {
    int i13 = outputFormat2.parseOptions(str_array11, (int)(short)-1);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array11);

  }

  @Test
  public void test004() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test004"); }


    OutputFormat outputFormat0 = new OutputFormat();
    outputFormat0.setIndent("UTF-8");

  }

  @Test
  public void test005() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test005"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setPadText(false);
    boolean b11 = outputFormat2.isOmitEncoding();
    java.lang.String str12 = outputFormat2.getEncoding();
    java.lang.String str13 = outputFormat2.getEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str12 + "' != '" + "UTF-8"+ "'", str12.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str13 + "' != '" + "UTF-8"+ "'", str13.equals("UTF-8"));

  }

  @Test
  public void test006() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test006"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    boolean b6 = outputFormat2.isOmitEncoding();
    outputFormat2.setLineSeparator("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);

  }

  @Test
  public void test007() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test007"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    boolean b6 = outputFormat2.isOmitEncoding();
    boolean b7 = outputFormat2.isOmitEncoding();
    outputFormat2.setOmitEncoding(false);
    // The following exception was thrown during execution in test generation
    try {
    outputFormat2.setAttributeQuoteCharacter('#');
      org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException");
    } catch (java.lang.IllegalArgumentException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.IllegalArgumentException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test008() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test008"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setEncoding("hi!");
    outputFormat2.setNewLineAfterNTags(0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));

  }

  @Test
  public void test009() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test009"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    java.lang.String str5 = outputFormat2.getIndent();
    outputFormat2.setExpandEmptyElements(false);
    java.lang.String[] str_array14 = new java.lang.String[] { "hi!", "UTF-8", "UTF-8", "", "", "UTF-8" };
    int i16 = outputFormat2.parseOptions(str_array14, 0);
    boolean b17 = outputFormat2.isSuppressDeclaration();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str5 + "' != '" + "hi!"+ "'", str5.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i16 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);

  }

  @Test
  public void test010() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test010"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isTrimText();
    outputFormat0.setIndent("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);

  }

  @Test
  public void test011() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test011"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setLineSeparator("");
    java.lang.String str8 = outputFormat2.getEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str8 + "' != '" + "UTF-8"+ "'", str8.equals("UTF-8"));

  }

  @Test
  public void test012() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test012"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    // The following exception was thrown during execution in test generation
    try {
    outputFormat2.setAttributeQuoteCharacter('#');
      org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException");
    } catch (java.lang.IllegalArgumentException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.IllegalArgumentException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test013() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test013"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    boolean b6 = outputFormat2.isOmitEncoding();
    boolean b7 = outputFormat2.isOmitEncoding();
    outputFormat2.setAttributeQuoteCharacter('\"');
    java.lang.String str10 = outputFormat2.getEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str10 + "' != '" + "UTF-8"+ "'", str10.equals("UTF-8"));

  }

  @Test
  public void test014() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test014"); }


    OutputFormat outputFormat1 = new OutputFormat("\n");

  }

  @Test
  public void test015() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test015"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setNewlines(true);
    outputFormat2.setIndentSize((int)(byte)10);
    boolean b8 = outputFormat2.isNewlines();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == true);

  }

  @Test
  public void test016() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test016"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getLineSeparator();
    outputFormat2.setNewLineAfterNTags((int)(short)10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "\n"+ "'", str3.equals("\n"));

  }

  @Test
  public void test017() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test017"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    outputFormat2.setEncoding("hi!");
    java.lang.String str7 = outputFormat2.getIndent();
    java.lang.String str8 = outputFormat2.getLineSeparator();
    java.lang.String str9 = outputFormat2.getLineSeparator();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str8 + "' != '" + "\n"+ "'", str8.equals("\n"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str9 + "' != '" + "\n"+ "'", str9.equals("\n"));

  }

  @Test
  public void test018() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test018"); }


    OutputFormat outputFormat0 = OutputFormat.createCompactFormat();
    outputFormat0.setExpandEmptyElements(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(outputFormat0);

  }

  @Test
  public void test019() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test019"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setNewlines(false);
    // The following exception was thrown during execution in test generation
    try {
    outputFormat2.setAttributeQuoteCharacter('a');
      org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException");
    } catch (java.lang.IllegalArgumentException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.IllegalArgumentException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == true);

  }

  @Test
  public void test020() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test020"); }


    OutputFormat outputFormat3 = new OutputFormat("", true, "");

  }

  @Test
  public void test021() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test021"); }


    OutputFormat outputFormat1 = new OutputFormat("");
    outputFormat1.setNewLineAfterNTags(0);

  }

  @Test
  public void test022() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test022"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    java.lang.String str2 = outputFormat0.getEncoding();
    boolean b3 = outputFormat0.isTrimText();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str2 + "' != '" + "UTF-8"+ "'", str2.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);

  }

  @Test
  public void test023() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test023"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setNewLineAfterDeclaration(true);
    outputFormat2.setExpandEmptyElements(true);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setLineSeparator("UTF-8");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));

  }

  @Test
  public void test024() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test024"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setNewlines(true);
    boolean b7 = outputFormat2.isTrimText();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test025() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test025"); }


    OutputFormat outputFormat0 = new OutputFormat();
    outputFormat0.setIndent(true);
    outputFormat0.setNewLineAfterDeclaration(true);
    outputFormat0.setOmitEncoding(false);

  }

  @Test
  public void test026() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test026"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    java.lang.String str4 = outputFormat2.getEncoding();
    boolean b5 = outputFormat2.isPadText();
    java.lang.String str6 = outputFormat2.getEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "UTF-8"+ "'", str4.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + "UTF-8"+ "'", str6.equals("UTF-8"));

  }

  @Test
  public void test027() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test027"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setIndent("");
    java.lang.String str7 = outputFormat2.getLineSeparator();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "\n"+ "'", str7.equals("\n"));

  }

  @Test
  public void test028() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test028"); }


    OutputFormat outputFormat0 = new OutputFormat();
    outputFormat0.setIndent(true);
    java.lang.String[] str_array9 = new java.lang.String[] { "hi!", "", "", "hi!", "UTF-8", "hi!" };
    int i11 = outputFormat0.parseOptions(str_array9, (int)(short)10);
    outputFormat0.setIndentSize((int)(byte)1);
    outputFormat0.setNewLineAfterNTags((int)'#');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i11 == 10);

  }

  @Test
  public void test029() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test029"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    outputFormat2.setEncoding("hi!");
    boolean b7 = outputFormat2.isNewlines();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test030() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test030"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    char char8 = outputFormat2.getAttributeQuoteCharacter();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char8 == '\"');

  }

  @Test
  public void test031() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test031"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    boolean b6 = outputFormat2.isNewLineAfterDeclaration();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);

  }

  @Test
  public void test032() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test032"); }


    OutputFormat outputFormat3 = new OutputFormat(" ", true, "");

  }

  @Test
  public void test033() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test033"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", true);

  }

  @Test
  public void test034() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test034"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    outputFormat0.setIndent("\n");
    outputFormat0.setTrimText(true);
    char char7 = outputFormat0.getAttributeQuoteCharacter();
    outputFormat0.setIndentSize((int)(byte)10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char7 == '\"');

  }

  @Test
  public void test035() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test035"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isNewlines();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);

  }

  @Test
  public void test036() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test036"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isPadText();
    OutputFormat outputFormat5 = new OutputFormat("", false);
    boolean b6 = outputFormat5.isNewLineAfterDeclaration();
    outputFormat5.setSuppressDeclaration(false);
    OutputFormat outputFormat9 = new OutputFormat();
    outputFormat9.setIndent(true);
    java.lang.String[] str_array18 = new java.lang.String[] { "hi!", "", "", "hi!", "UTF-8", "hi!" };
    int i20 = outputFormat9.parseOptions(str_array18, (int)(short)10);
    int i22 = outputFormat5.parseOptions(str_array18, (int)' ');
    // The following exception was thrown during execution in test generation
    try {
    int i24 = outputFormat0.parseOptions(str_array18, (int)(byte)-1);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i20 == 10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i22 == 32);

  }

  @Test
  public void test037() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test037"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    outputFormat0.setIndent("\n");
    outputFormat0.setIndent(true);
    java.lang.String str7 = outputFormat0.getEncoding();
    outputFormat0.setIndentSize((int)'4');
    int i10 = outputFormat0.getNewLineAfterNTags();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "UTF-8"+ "'", str7.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);

  }

  @Test
  public void test038() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test038"); }


    OutputFormat outputFormat3 = new OutputFormat("", false, "");
    outputFormat3.setExpandEmptyElements(false);
    outputFormat3.setNewLineAfterDeclaration(false);

  }

  @Test
  public void test039() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test039"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setNewLineAfterDeclaration(true);
    outputFormat2.setExpandEmptyElements(true);
    outputFormat2.setOmitEncoding(false);
    boolean b11 = outputFormat2.isNewlines();
    int i12 = outputFormat2.getNewLineAfterNTags();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i12 == 0);

  }

  @Test
  public void test040() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test040"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    boolean b6 = outputFormat2.isOmitEncoding();
    boolean b7 = outputFormat2.isOmitEncoding();
    boolean b8 = outputFormat2.isPadText();
    java.lang.String str9 = outputFormat2.getEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str9 + "' != '" + "UTF-8"+ "'", str9.equals("UTF-8"));

  }

  @Test
  public void test041() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test041"); }


    OutputFormat outputFormat0 = new OutputFormat();
    outputFormat0.setSuppressDeclaration(false);

  }

  @Test
  public void test042() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test042"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    boolean b5 = outputFormat2.isOmitEncoding();
    boolean b6 = outputFormat2.isPadText();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);

  }

  @Test
  public void test043() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test043"); }


    OutputFormat outputFormat2 = new OutputFormat("", false);
    boolean b3 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setNewLineAfterDeclaration(true);
    java.lang.String str6 = outputFormat2.getEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + "UTF-8"+ "'", str6.equals("UTF-8"));

  }

  @Test
  public void test044() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test044"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setIndent("");
    outputFormat2.setNewLineAfterNTags(0);
    outputFormat2.setIndentSize(32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));

  }

  @Test
  public void test045() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test045"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    outputFormat0.setIndent("\n");
    outputFormat0.setTrimText(true);
    char char7 = outputFormat0.getAttributeQuoteCharacter();
    java.lang.String str8 = outputFormat0.getLineSeparator();
    outputFormat0.setPadText(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char7 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str8 + "' != '" + "\n"+ "'", str8.equals("\n"));

  }

  @Test
  public void test046() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test046"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    java.lang.String str4 = outputFormat2.getEncoding();
    outputFormat2.setSuppressDeclaration(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "UTF-8"+ "'", str4.equals("UTF-8"));

  }

  @Test
  public void test047() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test047"); }


    OutputFormat outputFormat3 = new OutputFormat("", false, "");
    outputFormat3.setIndent(true);

  }

  @Test
  public void test048() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test048"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setNewLineAfterDeclaration(true);
    outputFormat2.setExpandEmptyElements(true);
    outputFormat2.setOmitEncoding(false);
    boolean b11 = outputFormat2.isSuppressDeclaration();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);

  }

  @Test
  public void test049() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test049"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    boolean b8 = outputFormat2.isSuppressDeclaration();
    boolean b9 = outputFormat2.isOmitEncoding();
    boolean b10 = outputFormat2.isOmitEncoding();
    char char11 = outputFormat2.getAttributeQuoteCharacter();
    boolean b12 = outputFormat2.isNewlines();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char11 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == false);

  }

  @Test
  public void test050() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test050"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setExpandEmptyElements(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);

  }

  @Test
  public void test051() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test051"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    boolean b8 = outputFormat2.isSuppressDeclaration();
    outputFormat2.setIndent("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);

  }

  @Test
  public void test052() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test052"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    boolean b5 = outputFormat2.isNewlines();
    boolean b6 = outputFormat2.isXHTML();
    OutputFormat outputFormat9 = new OutputFormat("", false);
    boolean b10 = outputFormat9.isNewLineAfterDeclaration();
    outputFormat9.setSuppressDeclaration(false);
    OutputFormat outputFormat13 = new OutputFormat();
    outputFormat13.setIndent(true);
    java.lang.String[] str_array22 = new java.lang.String[] { "hi!", "", "", "hi!", "UTF-8", "hi!" };
    int i24 = outputFormat13.parseOptions(str_array22, (int)(short)10);
    int i26 = outputFormat9.parseOptions(str_array22, (int)' ');
    // The following exception was thrown during execution in test generation
    try {
    int i28 = outputFormat2.parseOptions(str_array22, (int)(short)-1);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i24 == 10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i26 == 32);

  }

  @Test
  public void test053() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test053"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    java.lang.String str6 = outputFormat2.getIndent();
    outputFormat2.setExpandEmptyElements(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + "hi!"+ "'", str6.equals("hi!"));

  }

  @Test
  public void test054() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test054"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setNewLineAfterDeclaration(true);
    outputFormat2.setExpandEmptyElements(true);
    outputFormat2.setOmitEncoding(false);
    boolean b11 = outputFormat2.isNewlines();
    outputFormat2.setIndentSize((int)'4');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);

  }

  @Test
  public void test055() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test055"); }


    OutputFormat outputFormat3 = new OutputFormat("UTF-8", true, " ");

  }

  @Test
  public void test056() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test056"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setSuppressDeclaration(false);
    boolean b6 = outputFormat2.isXHTML();
    char char7 = outputFormat2.getAttributeQuoteCharacter();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char7 == '\"');

  }

  @Test
  public void test057() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test057"); }


    OutputFormat outputFormat3 = new OutputFormat("", true, "hi!");

  }

  @Test
  public void test058() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test058"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    boolean b5 = outputFormat2.isNewlines();
    outputFormat2.setNewLineAfterNTags(0);
    boolean b8 = outputFormat2.isTrimText();
    boolean b9 = outputFormat2.isOmitEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);

  }

  @Test
  public void test059() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test059"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    boolean b5 = outputFormat2.isNewlines();
    java.lang.String str6 = outputFormat2.getIndent();
    boolean b7 = outputFormat2.isPadText();
    java.lang.String str8 = outputFormat2.getLineSeparator();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + "hi!"+ "'", str6.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str8 + "' != '" + "\n"+ "'", str8.equals("\n"));

  }

  @Test
  public void test060() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test060"); }


    OutputFormat outputFormat0 = new OutputFormat();
    int i1 = outputFormat0.getNewLineAfterNTags();
    boolean b2 = outputFormat0.isPadText();
    outputFormat0.setNewLineAfterNTags((int)(byte)0);
    int i5 = outputFormat0.getNewLineAfterNTags();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i1 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);

  }

  @Test
  public void test061() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test061"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setIndent(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');

  }

  @Test
  public void test062() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test062"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setNewlines(false);

  }

  @Test
  public void test063() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test063"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    boolean b9 = outputFormat2.isExpandEmptyElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);

  }

  @Test
  public void test064() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test064"); }


    OutputFormat outputFormat0 = OutputFormat.createPrettyPrint();
    outputFormat0.setExpandEmptyElements(false);
    outputFormat0.setIndent("UTF-8");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(outputFormat0);

  }

  @Test
  public void test065() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test065"); }


    OutputFormat outputFormat1 = new OutputFormat("UTF-8");
    outputFormat1.setIndentSize((int)(byte)1);
    java.lang.String str4 = outputFormat1.getIndent();
    outputFormat1.setIndent(true);
    outputFormat1.setAttributeQuoteCharacter('\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + " "+ "'", str4.equals(" "));

  }

  @Test
  public void test066() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test066"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    outputFormat0.setIndent("\n");
    outputFormat0.setTrimText(true);
    outputFormat0.setIndent(false);
    boolean b9 = outputFormat0.isNewLineAfterDeclaration();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == true);

  }

  @Test
  public void test067() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test067"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    boolean b6 = outputFormat2.isXHTML();
    outputFormat2.setXHTML(true);
    outputFormat2.setLineSeparator("UTF-8");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);

  }

  @Test
  public void test068() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test068"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isTrimText();
    outputFormat0.setIndent(false);
    outputFormat0.setEncoding("\n");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);

  }

  @Test
  public void test069() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test069"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setXHTML(false);
    java.lang.String str8 = outputFormat2.getEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str8 + "' != '" + "UTF-8"+ "'", str8.equals("UTF-8"));

  }

  @Test
  public void test070() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test070"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setIndent("");
    outputFormat2.setNewLineAfterNTags(0);
    java.lang.String str9 = outputFormat2.getEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str9 + "' != '" + "UTF-8"+ "'", str9.equals("UTF-8"));

  }

  @Test
  public void test071() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test071"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    java.lang.String str5 = outputFormat2.getIndent();
    boolean b6 = outputFormat2.isNewLineAfterDeclaration();
    char char7 = outputFormat2.getAttributeQuoteCharacter();
    boolean b8 = outputFormat2.isPadText();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str5 + "' != '" + "hi!"+ "'", str5.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char7 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);

  }

  @Test
  public void test072() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test072"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setNewlines(true);
    boolean b6 = outputFormat2.isXHTML();
    outputFormat2.setNewlines(true);
    java.lang.String str9 = outputFormat2.getIndent();
    outputFormat2.setPadText(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str9 + "' != '" + "hi!"+ "'", str9.equals("hi!"));

  }

  @Test
  public void test073() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test073"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setPadText(false);
    boolean b11 = outputFormat2.isOmitEncoding();
    java.lang.String str12 = outputFormat2.getEncoding();
    boolean b13 = outputFormat2.isTrimText();
    outputFormat2.setLineSeparator("hi!");
    boolean b16 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setIndentSize(1);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str12 + "' != '" + "UTF-8"+ "'", str12.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b16 == true);

  }

  @Test
  public void test074() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test074"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setPadText(false);
    boolean b11 = outputFormat2.isOmitEncoding();
    boolean b12 = outputFormat2.isOmitEncoding();
    outputFormat2.setExpandEmptyElements(false);
    boolean b15 = outputFormat2.isXHTML();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);

  }

  @Test
  public void test075() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test075"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    java.lang.String str6 = outputFormat2.getIndent();
    boolean b7 = outputFormat2.isSuppressDeclaration();
    boolean b8 = outputFormat2.isNewlines();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + "hi!"+ "'", str6.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);

  }

  @Test
  public void test076() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test076"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setSuppressDeclaration(false);
    boolean b6 = outputFormat2.isXHTML();
    outputFormat2.setOmitEncoding(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);

  }

  @Test
  public void test077() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test077"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setNewlines(true);
    boolean b6 = outputFormat2.isXHTML();
    outputFormat2.setNewlines(true);
    java.lang.String str9 = outputFormat2.getIndent();
    boolean b10 = outputFormat2.isNewlines();
    java.lang.String str11 = outputFormat2.getIndent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str9 + "' != '" + "hi!"+ "'", str9.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str11 + "' != '" + "hi!"+ "'", str11.equals("hi!"));

  }

  @Test
  public void test078() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test078"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    outputFormat2.setEncoding("hi!");
    java.lang.String str7 = outputFormat2.getIndent();
    outputFormat2.setTrimText(false);
    outputFormat2.setNewlines(true);
    boolean b12 = outputFormat2.isXHTML();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == false);

  }

  @Test
  public void test079() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test079"); }


    OutputFormat outputFormat2 = new OutputFormat("  ", true);

  }

  @Test
  public void test080() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test080"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    boolean b6 = outputFormat2.isXHTML();
    outputFormat2.setNewLineAfterDeclaration(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);

  }

  @Test
  public void test081() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test081"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    outputFormat2.setEncoding("hi!");
    boolean b7 = outputFormat2.isTrimText();
    outputFormat2.setEncoding("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test082() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test082"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    boolean b5 = outputFormat2.isPadText();
    outputFormat2.setNewLineAfterDeclaration(true);
    outputFormat2.setIndent(" ");
    // The following exception was thrown during execution in test generation
    try {
    outputFormat2.setAttributeQuoteCharacter('4');
      org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException");
    } catch (java.lang.IllegalArgumentException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.IllegalArgumentException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);

  }

  @Test
  public void test083() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test083"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setNewlines(true);
    outputFormat2.setPadText(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);

  }

  @Test
  public void test084() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test084"); }


    OutputFormat outputFormat0 = OutputFormat.createCompactFormat();
    outputFormat0.setNewLineAfterNTags(1);
    outputFormat0.setNewLineAfterDeclaration(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(outputFormat0);

  }

  @Test
  public void test085() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test085"); }


    OutputFormat outputFormat0 = OutputFormat.createPrettyPrint();
    outputFormat0.setExpandEmptyElements(false);
    OutputFormat outputFormat5 = new OutputFormat("hi!", false);
    java.lang.String str6 = outputFormat5.getIndent();
    boolean b7 = outputFormat5.isPadText();
    java.lang.String str8 = outputFormat5.getIndent();
    outputFormat5.setExpandEmptyElements(false);
    java.lang.String[] str_array17 = new java.lang.String[] { "hi!", "UTF-8", "UTF-8", "", "", "UTF-8" };
    int i19 = outputFormat5.parseOptions(str_array17, 0);
    int i21 = outputFormat0.parseOptions(str_array17, (int)(byte)0);
    java.lang.String str22 = outputFormat0.getIndent();
    // The following exception was thrown during execution in test generation
    try {
    outputFormat0.setAttributeQuoteCharacter('a');
      org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException");
    } catch (java.lang.IllegalArgumentException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.IllegalArgumentException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(outputFormat0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + "hi!"+ "'", str6.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str8 + "' != '" + "hi!"+ "'", str8.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i19 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i21 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str22 + "' != '" + "  "+ "'", str22.equals("  "));

  }

  @Test
  public void test086() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test086"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setEncoding("UTF-8");
    java.lang.String[] str_array11 = new java.lang.String[] { "\n", "\n", "  ", " ", "hi!" };
    int i13 = outputFormat2.parseOptions(str_array11, (int)(short)0);
    outputFormat2.setPadText(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);

  }

  @Test
  public void test087() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test087"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    outputFormat0.setIndent("\n");
    outputFormat0.setTrimText(true);
    outputFormat0.setIndent("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);

  }

  @Test
  public void test088() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test088"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    boolean b8 = outputFormat2.isSuppressDeclaration();
    boolean b9 = outputFormat2.isOmitEncoding();
    boolean b10 = outputFormat2.isTrimText();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);

  }

  @Test
  public void test089() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test089"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    outputFormat2.setLineSeparator("");
    outputFormat2.setIndent("UTF-8");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');

  }

  @Test
  public void test090() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test090"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    java.lang.String str5 = outputFormat2.getIndent();
    boolean b6 = outputFormat2.isOmitEncoding();
    outputFormat2.setTrimText(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str5 + "' != '" + "hi!"+ "'", str5.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);

  }

  @Test
  public void test091() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test091"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isXHTML();
    boolean b5 = outputFormat2.isPadText();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);

  }

  @Test
  public void test092() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test092"); }


    OutputFormat outputFormat0 = OutputFormat.createPrettyPrint();
    outputFormat0.setLineSeparator("hi!");
    outputFormat0.setIndent(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(outputFormat0);

  }

  @Test
  public void test093() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test093"); }


    OutputFormat outputFormat2 = new OutputFormat("", false);
    boolean b3 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setSuppressDeclaration(false);
    java.lang.String[] str_array8 = new java.lang.String[] { "", "hi!" };
    int i10 = outputFormat2.parseOptions(str_array8, (int)'#');
    outputFormat2.setSuppressDeclaration(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 35);

  }

  @Test
  public void test094() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test094"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    outputFormat2.setIndent(false);
    java.lang.String str7 = outputFormat2.getIndent();
    outputFormat2.setIndent(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);

  }

  @Test
  public void test095() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test095"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setLineSeparator("");
    java.lang.String str8 = outputFormat2.getIndent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str8 + "' != '" + "hi!"+ "'", str8.equals("hi!"));

  }

  @Test
  public void test096() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test096"); }


    OutputFormat outputFormat2 = new OutputFormat("\n", true);

  }

  @Test
  public void test097() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test097"); }


    OutputFormat outputFormat0 = new OutputFormat();
    int i1 = outputFormat0.getNewLineAfterNTags();
    boolean b2 = outputFormat0.isPadText();
    outputFormat0.setIndent("");
    boolean b5 = outputFormat0.isOmitEncoding();
    boolean b6 = outputFormat0.isNewlines();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i1 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);

  }

  @Test
  public void test098() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test098"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setNewlines(true);
    outputFormat2.setXHTML(true);
    boolean b12 = outputFormat2.isOmitEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == false);

  }

  @Test
  public void test099() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test099"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    outputFormat2.setEncoding("hi!");
    boolean b7 = outputFormat2.isTrimText();
    boolean b8 = outputFormat2.isNewLineAfterDeclaration();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == true);

  }

  @Test
  public void test100() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test100"); }


    OutputFormat outputFormat0 = OutputFormat.createCompactFormat();
    outputFormat0.setNewLineAfterNTags(1);
    boolean b3 = outputFormat0.isNewLineAfterDeclaration();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(outputFormat0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == true);

  }

  @Test
  public void test101() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test101"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setNewlines(true);
    int i10 = outputFormat2.getNewLineAfterNTags();
    outputFormat2.setIndent("                                                                                                    ");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);

  }

  @Test
  public void test102() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test102"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setPadText(false);
    java.lang.String str5 = outputFormat2.getIndent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str5 + "' != '" + "hi!"+ "'", str5.equals("hi!"));

  }

  @Test
  public void test103() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test103"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isTrimText();
    outputFormat0.setNewLineAfterNTags((-1));
    outputFormat0.setEncoding("");
    outputFormat0.setOmitEncoding(true);
    boolean b9 = outputFormat0.isXHTML();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);

  }

  @Test
  public void test104() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test104"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    boolean b3 = outputFormat0.isPadText();
    outputFormat0.setSuppressDeclaration(false);
    outputFormat0.setPadText(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);

  }

  @Test
  public void test105() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test105"); }


    OutputFormat outputFormat2 = new OutputFormat("", true);

  }

  @Test
  public void test106() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test106"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    boolean b5 = outputFormat2.isPadText();
    outputFormat2.setIndentSize(100);
    OutputFormat outputFormat10 = new OutputFormat("hi!", false);
    java.lang.String str11 = outputFormat10.getIndent();
    boolean b12 = outputFormat10.isPadText();
    java.lang.String str13 = outputFormat10.getIndent();
    outputFormat10.setExpandEmptyElements(false);
    java.lang.String[] str_array22 = new java.lang.String[] { "hi!", "UTF-8", "UTF-8", "", "", "UTF-8" };
    int i24 = outputFormat10.parseOptions(str_array22, 0);
    int i26 = outputFormat2.parseOptions(str_array22, (int)(byte)10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str11 + "' != '" + "hi!"+ "'", str11.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str13 + "' != '" + "hi!"+ "'", str13.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i24 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i26 == 10);

  }

  @Test
  public void test107() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test107"); }


    OutputFormat outputFormat2 = new OutputFormat("", false);
    boolean b3 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setNewLineAfterDeclaration(true);
    char char6 = outputFormat2.getAttributeQuoteCharacter();
    boolean b7 = outputFormat2.isNewLineAfterDeclaration();
    java.lang.String str8 = outputFormat2.getIndent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char6 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str8 + "' != '" + ""+ "'", str8.equals(""));

  }

  @Test
  public void test108() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test108"); }


    OutputFormat outputFormat1 = new OutputFormat("UTF-8");
    outputFormat1.setIndentSize((int)(byte)1);
    java.lang.String str4 = outputFormat1.getIndent();
    outputFormat1.setIndent(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + " "+ "'", str4.equals(" "));

  }

  @Test
  public void test109() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test109"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    outputFormat2.setIndent(false);
    outputFormat2.setIndentSize((int)'\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);

  }

  @Test
  public void test110() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test110"); }


    OutputFormat outputFormat3 = new OutputFormat("\n", true, "hi!");
    outputFormat3.setTrimText(true);
    outputFormat3.setNewLineAfterDeclaration(false);

  }

  @Test
  public void test111() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test111"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    java.lang.String str5 = outputFormat2.getIndent();
    outputFormat2.setExpandEmptyElements(false);
    java.lang.String[] str_array14 = new java.lang.String[] { "hi!", "UTF-8", "UTF-8", "", "", "UTF-8" };
    int i16 = outputFormat2.parseOptions(str_array14, 0);
    outputFormat2.setIndentSize((int)(short)1);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str5 + "' != '" + "hi!"+ "'", str5.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i16 == 0);

  }

  @Test
  public void test112() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test112"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    boolean b5 = outputFormat2.isExpandEmptyElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);

  }

  @Test
  public void test113() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test113"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setPadText(false);
    boolean b11 = outputFormat2.isOmitEncoding();
    boolean b12 = outputFormat2.isOmitEncoding();
    boolean b13 = outputFormat2.isSuppressDeclaration();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);

  }

  @Test
  public void test114() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test114"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    boolean b6 = outputFormat2.isXHTML();
    outputFormat2.setXHTML(true);
    outputFormat2.setExpandEmptyElements(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);

  }

  @Test
  public void test115() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test115"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    boolean b8 = outputFormat2.isSuppressDeclaration();
    boolean b9 = outputFormat2.isOmitEncoding();
    boolean b10 = outputFormat2.isOmitEncoding();
    outputFormat2.setLineSeparator("\n");
    java.lang.String str13 = outputFormat2.getIndent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str13 + "' != '" + "hi!"+ "'", str13.equals("hi!"));

  }

  @Test
  public void test116() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test116"); }


    OutputFormat outputFormat0 = new OutputFormat();
    int i1 = outputFormat0.getNewLineAfterNTags();
    boolean b2 = outputFormat0.isPadText();
    outputFormat0.setIndent("");
    boolean b5 = outputFormat0.isOmitEncoding();
    boolean b6 = outputFormat0.isNewLineAfterDeclaration();
    outputFormat0.setOmitEncoding(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i1 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);

  }

  @Test
  public void test117() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test117"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    boolean b5 = outputFormat2.isNewlines();
    boolean b6 = outputFormat2.isOmitEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);

  }

  @Test
  public void test118() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test118"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setNewlines(true);
    outputFormat2.setXHTML(true);
    outputFormat2.setSuppressDeclaration(true);
    boolean b14 = outputFormat2.isExpandEmptyElements();
    int i15 = outputFormat2.getNewLineAfterNTags();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i15 == 0);

  }

  @Test
  public void test119() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test119"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setPadText(false);
    outputFormat2.setTrimText(false);

  }

  @Test
  public void test120() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test120"); }


    OutputFormat outputFormat0 = new OutputFormat();
    outputFormat0.setIndent(true);
    java.lang.String str3 = outputFormat0.getEncoding();
    OutputFormat outputFormat6 = new OutputFormat("hi!", false);
    java.lang.String str7 = outputFormat6.getIndent();
    boolean b8 = outputFormat6.isPadText();
    java.lang.String str9 = outputFormat6.getIndent();
    outputFormat6.setExpandEmptyElements(false);
    java.lang.String[] str_array18 = new java.lang.String[] { "hi!", "UTF-8", "UTF-8", "", "", "UTF-8" };
    int i20 = outputFormat6.parseOptions(str_array18, 0);
    int i22 = outputFormat0.parseOptions(str_array18, (int)'4');
    outputFormat0.setOmitEncoding(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "UTF-8"+ "'", str3.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str9 + "' != '" + "hi!"+ "'", str9.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i20 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i22 == 52);

  }

  @Test
  public void test121() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test121"); }


    OutputFormat outputFormat1 = new OutputFormat("UTF-8");
    outputFormat1.setIndentSize((int)(byte)1);
    java.lang.String str4 = outputFormat1.getIndent();
    outputFormat1.setIndent(true);
    outputFormat1.setLineSeparator("                                                                                                    ");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + " "+ "'", str4.equals(" "));

  }

  @Test
  public void test122() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test122"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    boolean b8 = outputFormat2.isSuppressDeclaration();
    boolean b9 = outputFormat2.isOmitEncoding();
    java.lang.String str10 = outputFormat2.getLineSeparator();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str10 + "' != '" + "\n"+ "'", str10.equals("\n"));

  }

  @Test
  public void test123() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test123"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setNewlines(true);
    outputFormat2.setXHTML(true);
    boolean b12 = outputFormat2.isPadText();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == false);

  }

  @Test
  public void test124() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test124"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isTrimText();
    outputFormat0.setIndent(false);
    outputFormat0.setNewLineAfterDeclaration(false);
    outputFormat0.setNewLineAfterDeclaration(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);

  }

  @Test
  public void test125() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test125"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isTrimText();
    outputFormat0.setIndent(false);
    outputFormat0.setPadText(false);
    outputFormat0.setIndent(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);

  }

  @Test
  public void test126() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test126"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    outputFormat2.setTrimText(false);
    java.lang.String str7 = outputFormat2.getEncoding();
    java.lang.String str8 = outputFormat2.getLineSeparator();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "UTF-8"+ "'", str7.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str8 + "' != '" + "\n"+ "'", str8.equals("\n"));

  }

  @Test
  public void test127() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test127"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setNewlines(true);
    boolean b6 = outputFormat2.isXHTML();
    outputFormat2.setNewlines(true);
    java.lang.String str9 = outputFormat2.getIndent();
    boolean b10 = outputFormat2.isNewlines();
    outputFormat2.setEncoding("                                                                                                    ");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str9 + "' != '" + "hi!"+ "'", str9.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);

  }

  @Test
  public void test128() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test128"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    boolean b8 = outputFormat2.isSuppressDeclaration();
    boolean b9 = outputFormat2.isOmitEncoding();
    boolean b10 = outputFormat2.isOmitEncoding();
    char char11 = outputFormat2.getAttributeQuoteCharacter();
    boolean b12 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setIndent(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char11 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == true);

  }

  @Test
  public void test129() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test129"); }


    OutputFormat outputFormat0 = new OutputFormat();
    outputFormat0.setIndent(true);
    outputFormat0.setNewLineAfterDeclaration(true);
    outputFormat0.setNewlines(false);

  }

  @Test
  public void test130() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test130"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    outputFormat0.setIndent("\n");
    outputFormat0.setPadText(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);

  }

  @Test
  public void test131() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test131"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    outputFormat2.setLineSeparator("");
    outputFormat2.setTrimText(false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setExpandEmptyElements(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');

  }

  @Test
  public void test132() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test132"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setPadText(true);

  }

  @Test
  public void test133() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test133"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    boolean b5 = outputFormat2.isNewlines();
    outputFormat2.setNewLineAfterNTags(0);
    boolean b8 = outputFormat2.isTrimText();
    outputFormat2.setTrimText(false);
    boolean b11 = outputFormat2.isSuppressDeclaration();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);

  }

  @Test
  public void test134() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test134"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setNewLineAfterDeclaration(true);
    outputFormat2.setNewLineAfterNTags((-1));
    outputFormat2.setXHTML(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));

  }

  @Test
  public void test135() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test135"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setNewlines(true);
    boolean b6 = outputFormat2.isXHTML();
    outputFormat2.setNewlines(true);
    java.lang.String str9 = outputFormat2.getIndent();
    boolean b10 = outputFormat2.isNewlines();
    java.lang.String str11 = outputFormat2.getEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str9 + "' != '" + "hi!"+ "'", str9.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str11 + "' != '" + "UTF-8"+ "'", str11.equals("UTF-8"));

  }

  @Test
  public void test136() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test136"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setNewLineAfterDeclaration(true);
    outputFormat2.setExpandEmptyElements(true);
    outputFormat2.setOmitEncoding(false);
    boolean b11 = outputFormat2.isNewlines();
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setPadText(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);

  }

  @Test
  public void test137() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test137"); }


    OutputFormat outputFormat2 = new OutputFormat("", false);
    boolean b3 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setNewLineAfterDeclaration(true);
    int i6 = outputFormat2.getNewLineAfterNTags();
    java.lang.String str7 = outputFormat2.getLineSeparator();
    outputFormat2.setIndent(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "\n"+ "'", str7.equals("\n"));

  }

  @Test
  public void test138() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test138"); }


    OutputFormat outputFormat2 = new OutputFormat("UTF-8", false);
    boolean b3 = outputFormat2.isOmitEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);

  }

  @Test
  public void test139() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test139"); }


    OutputFormat outputFormat2 = new OutputFormat("", false);
    java.lang.String str3 = outputFormat2.getLineSeparator();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "\n"+ "'", str3.equals("\n"));

  }

  @Test
  public void test140() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test140"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setNewlines(false);
    // The following exception was thrown during execution in test generation
    try {
    outputFormat2.setAttributeQuoteCharacter('4');
      org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException");
    } catch (java.lang.IllegalArgumentException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.IllegalArgumentException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == true);

  }

  @Test
  public void test141() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test141"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getLineSeparator();
    outputFormat2.setOmitEncoding(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "\n"+ "'", str3.equals("\n"));

  }

  @Test
  public void test142() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test142"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setNewLineAfterDeclaration(true);
    outputFormat2.setNewLineAfterNTags((-1));
    boolean b9 = outputFormat2.isXHTML();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);

  }

  @Test
  public void test143() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test143"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    boolean b6 = outputFormat2.isOmitEncoding();
    java.lang.String str7 = outputFormat2.getIndent();
    boolean b8 = outputFormat2.isNewLineAfterDeclaration();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == true);

  }

  @Test
  public void test144() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test144"); }


    OutputFormat outputFormat0 = new OutputFormat();
    int i1 = outputFormat0.getNewLineAfterNTags();
    boolean b2 = outputFormat0.isPadText();
    outputFormat0.setIndent("");
    boolean b5 = outputFormat0.isOmitEncoding();
    outputFormat0.setPadText(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i1 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);

  }

  @Test
  public void test145() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test145"); }


    OutputFormat outputFormat1 = new OutputFormat(" ");
    boolean b2 = outputFormat1.isXHTML();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);

  }

  @Test
  public void test146() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test146"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isTrimText();
    char char3 = outputFormat0.getAttributeQuoteCharacter();
    boolean b4 = outputFormat0.isXHTML();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);

  }

  @Test
  public void test147() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test147"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    boolean b8 = outputFormat2.isSuppressDeclaration();
    boolean b9 = outputFormat2.isOmitEncoding();
    boolean b10 = outputFormat2.isOmitEncoding();
    outputFormat2.setAttributeQuoteCharacter('\"');
    java.lang.String str13 = outputFormat2.getEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str13 + "' != '" + "UTF-8"+ "'", str13.equals("UTF-8"));

  }

  @Test
  public void test148() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test148"); }


    OutputFormat outputFormat2 = new OutputFormat(" ", false);
    outputFormat2.setTrimText(false);

  }

  @Test
  public void test149() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test149"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setNewlines(true);
    boolean b7 = outputFormat2.isExpandEmptyElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test150() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test150"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setPadText(false);
    boolean b11 = outputFormat2.isOmitEncoding();
    java.lang.String str12 = outputFormat2.getEncoding();
    boolean b13 = outputFormat2.isTrimText();
    java.lang.String str14 = outputFormat2.getIndent();
    boolean b15 = outputFormat2.isNewLineAfterDeclaration();
    OutputFormat outputFormat16 = new OutputFormat();
    outputFormat16.setIndent(true);
    java.lang.String str19 = outputFormat16.getEncoding();
    OutputFormat outputFormat22 = new OutputFormat("hi!", false);
    java.lang.String str23 = outputFormat22.getIndent();
    boolean b24 = outputFormat22.isPadText();
    java.lang.String str25 = outputFormat22.getIndent();
    outputFormat22.setExpandEmptyElements(false);
    java.lang.String[] str_array34 = new java.lang.String[] { "hi!", "UTF-8", "UTF-8", "", "", "UTF-8" };
    int i36 = outputFormat22.parseOptions(str_array34, 0);
    int i38 = outputFormat16.parseOptions(str_array34, (int)'4');
    int i40 = outputFormat2.parseOptions(str_array34, (int)' ');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str12 + "' != '" + "UTF-8"+ "'", str12.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str14 + "' != '" + "                                                                                                    "+ "'", str14.equals("                                                                                                    "));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str19 + "' != '" + "UTF-8"+ "'", str19.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str23 + "' != '" + "hi!"+ "'", str23.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b24 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str25 + "' != '" + "hi!"+ "'", str25.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array34);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i36 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i38 == 52);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i40 == 32);

  }

  @Test
  public void test151() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test151"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    outputFormat2.setEncoding("hi!");
    java.lang.String str7 = outputFormat2.getIndent();
    outputFormat2.setTrimText(false);
    char char10 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setXHTML(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char10 == '\"');

  }

  @Test
  public void test152() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test152"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    boolean b8 = outputFormat2.isSuppressDeclaration();
    boolean b9 = outputFormat2.isOmitEncoding();
    boolean b10 = outputFormat2.isOmitEncoding();
    outputFormat2.setLineSeparator("\n");
    boolean b13 = outputFormat2.isSuppressDeclaration();
    boolean b14 = outputFormat2.isNewlines();
    boolean b15 = outputFormat2.isSuppressDeclaration();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);

  }

  @Test
  public void test153() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test153"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setNewlines(true);
    outputFormat2.setXHTML(true);
    outputFormat2.setSuppressDeclaration(true);
    boolean b14 = outputFormat2.isExpandEmptyElements();
    outputFormat2.setXHTML(false);
    outputFormat2.setTrimText(false);
    outputFormat2.setTrimText(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == false);

  }

  @Test
  public void test154() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test154"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    outputFormat2.setIndent(false);
    java.lang.String str7 = outputFormat2.getIndent();
    outputFormat2.setPadText(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);

  }

  @Test
  public void test155() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test155"); }


    OutputFormat outputFormat1 = new OutputFormat("  ");

  }

  @Test
  public void test156() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test156"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setNewlines(true);
    outputFormat2.setXHTML(true);
    java.lang.String str12 = outputFormat2.getEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str12 + "' != '" + "UTF-8"+ "'", str12.equals("UTF-8"));

  }

  @Test
  public void test157() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test157"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    java.lang.String str2 = outputFormat0.getEncoding();
    outputFormat0.setIndentSize(52);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str2 + "' != '" + "UTF-8"+ "'", str2.equals("UTF-8"));

  }

  @Test
  public void test158() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test158"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    outputFormat2.setEncoding("hi!");
    boolean b7 = outputFormat2.isXHTML();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test159() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test159"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setNewlines(true);
    outputFormat2.setXHTML(true);
    outputFormat2.setSuppressDeclaration(true);
    boolean b14 = outputFormat2.isExpandEmptyElements();
    outputFormat2.setXHTML(false);
    outputFormat2.setTrimText(false);
    java.lang.String str19 = outputFormat2.getIndent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str19 + "' != '" + "hi!"+ "'", str19.equals("hi!"));

  }

  @Test
  public void test160() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test160"); }


    OutputFormat outputFormat0 = new OutputFormat();
    int i1 = outputFormat0.getNewLineAfterNTags();
    java.lang.String str2 = outputFormat0.getLineSeparator();
    outputFormat0.setSuppressDeclaration(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i1 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str2 + "' != '" + "\n"+ "'", str2.equals("\n"));

  }

  @Test
  public void test161() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test161"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    boolean b8 = outputFormat2.isSuppressDeclaration();
    boolean b9 = outputFormat2.isOmitEncoding();
    boolean b10 = outputFormat2.isOmitEncoding();
    char char11 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setOmitEncoding(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char11 == '\"');

  }

  @Test
  public void test162() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test162"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    java.lang.String str6 = outputFormat2.getIndent();
    boolean b7 = outputFormat2.isSuppressDeclaration();
    outputFormat2.setNewlines(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + "hi!"+ "'", str6.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test163() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test163"); }


    OutputFormat outputFormat2 = new OutputFormat("", false);
    boolean b3 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setSuppressDeclaration(false);
    outputFormat2.setExpandEmptyElements(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == true);

  }

  @Test
  public void test164() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test164"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setPadText(false);
    boolean b11 = outputFormat2.isOmitEncoding();
    java.lang.String str12 = outputFormat2.getEncoding();
    boolean b13 = outputFormat2.isTrimText();
    outputFormat2.setLineSeparator("hi!");
    boolean b16 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setIndent("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str12 + "' != '" + "UTF-8"+ "'", str12.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b16 == true);

  }

  @Test
  public void test165() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test165"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setNewlines(true);
    outputFormat2.setOmitEncoding(false);
    boolean b8 = outputFormat2.isNewlines();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == true);

  }

  @Test
  public void test166() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test166"); }


    OutputFormat outputFormat2 = new OutputFormat("", false);
    boolean b3 = outputFormat2.isNewLineAfterDeclaration();
    boolean b4 = outputFormat2.isOmitEncoding();
    outputFormat2.setXHTML(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);

  }

  @Test
  public void test167() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test167"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    outputFormat2.setLineSeparator("");
    outputFormat2.setTrimText(false);
    outputFormat2.setOmitEncoding(false);
    java.lang.String str14 = outputFormat2.getEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str14 + "' != '" + "UTF-8"+ "'", str14.equals("UTF-8"));

  }

  @Test
  public void test168() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test168"); }


    OutputFormat outputFormat3 = new OutputFormat("", true, "\n");
    boolean b4 = outputFormat3.isExpandEmptyElements();
    outputFormat3.setExpandEmptyElements(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);

  }

  @Test
  public void test169() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test169"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setEncoding("UTF-8");
    java.lang.String[] str_array11 = new java.lang.String[] { "\n", "\n", "  ", " ", "hi!" };
    int i13 = outputFormat2.parseOptions(str_array11, (int)(short)0);
    boolean b14 = outputFormat2.isOmitEncoding();
    OutputFormat outputFormat15 = OutputFormat.createPrettyPrint();
    outputFormat15.setExpandEmptyElements(false);
    OutputFormat outputFormat20 = new OutputFormat("hi!", false);
    java.lang.String str21 = outputFormat20.getIndent();
    boolean b22 = outputFormat20.isPadText();
    java.lang.String str23 = outputFormat20.getIndent();
    outputFormat20.setExpandEmptyElements(false);
    java.lang.String[] str_array32 = new java.lang.String[] { "hi!", "UTF-8", "UTF-8", "", "", "UTF-8" };
    int i34 = outputFormat20.parseOptions(str_array32, 0);
    int i36 = outputFormat15.parseOptions(str_array32, (int)(byte)0);
    int i38 = outputFormat2.parseOptions(str_array32, (int)(short)1);
    boolean b39 = outputFormat2.isSuppressDeclaration();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(outputFormat15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str21 + "' != '" + "hi!"+ "'", str21.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b22 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str23 + "' != '" + "hi!"+ "'", str23.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i34 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i36 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i38 == 1);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b39 == false);

  }

  @Test
  public void test170() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test170"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    boolean b5 = outputFormat2.isPadText();
    outputFormat2.setXHTML(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);

  }

  @Test
  public void test171() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test171"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    outputFormat2.setIndent(false);
    outputFormat2.setNewLineAfterNTags((int)'4');
    outputFormat2.setNewlines(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);

  }

  @Test
  public void test172() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test172"); }


    OutputFormat outputFormat0 = OutputFormat.createCompactFormat();
    boolean b1 = outputFormat0.isNewLineAfterDeclaration();
    outputFormat0.setTrimText(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(outputFormat0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b1 == true);

  }

  @Test
  public void test173() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test173"); }


    OutputFormat outputFormat1 = new OutputFormat("UTF-8");
    outputFormat1.setIndentSize((int)(byte)1);
    java.lang.String str4 = outputFormat1.getIndent();
    boolean b5 = outputFormat1.isExpandEmptyElements();
    boolean b6 = outputFormat1.isPadText();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + " "+ "'", str4.equals(" "));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);

  }

  @Test
  public void test174() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test174"); }


    OutputFormat outputFormat0 = new OutputFormat();
    outputFormat0.setIndent(true);
    outputFormat0.setNewLineAfterDeclaration(true);
    outputFormat0.setIndent(false);
    java.lang.String str7 = outputFormat0.getEncoding();
    boolean b8 = outputFormat0.isSuppressDeclaration();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "UTF-8"+ "'", str7.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);

  }

  @Test
  public void test175() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test175"); }


    OutputFormat outputFormat0 = new OutputFormat();
    outputFormat0.setIndent(true);
    outputFormat0.setNewLineAfterDeclaration(true);
    char char5 = outputFormat0.getAttributeQuoteCharacter();
    outputFormat0.setSuppressDeclaration(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');

  }

  @Test
  public void test176() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test176"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setEncoding(" ");

  }

  @Test
  public void test177() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test177"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    outputFormat0.setLineSeparator("");
    outputFormat0.setNewLineAfterNTags(52);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);

  }

  @Test
  public void test178() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test178"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setNewLineAfterDeclaration(true);
    outputFormat2.setExpandEmptyElements(true);
    outputFormat2.setOmitEncoding(false);
    boolean b11 = outputFormat2.isNewlines();
    outputFormat2.setPadText(true);
    boolean b14 = outputFormat2.isPadText();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == true);

  }

  @Test
  public void test179() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test179"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setPadText(false);
    outputFormat2.setLineSeparator("\n");
    boolean b13 = outputFormat2.isExpandEmptyElements();
    boolean b14 = outputFormat2.isExpandEmptyElements();
    boolean b15 = outputFormat2.isTrimText();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);

  }

  @Test
  public void test180() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test180"); }


    OutputFormat outputFormat3 = new OutputFormat(" ", true, " ");
    outputFormat3.setIndentSize(32);
    outputFormat3.setNewlines(false);

  }

  @Test
  public void test181() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test181"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setIndent("");
    outputFormat2.setEncoding("");
    outputFormat2.setIndent("");
    outputFormat2.setExpandEmptyElements(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));

  }

  @Test
  public void test182() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test182"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setNewlines(true);
    java.lang.String str6 = outputFormat2.getLineSeparator();
    outputFormat2.setTrimText(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + "\n"+ "'", str6.equals("\n"));

  }

  @Test
  public void test183() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test183"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setNewlines(true);
    boolean b6 = outputFormat2.isXHTML();
    outputFormat2.setOmitEncoding(true);
    boolean b9 = outputFormat2.isExpandEmptyElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);

  }

  @Test
  public void test184() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test184"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setPadText(false);
    outputFormat2.setEncoding("UTF-8");
    boolean b7 = outputFormat2.isNewlines();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test185() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test185"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setNewLineAfterDeclaration(true);
    outputFormat2.setExpandEmptyElements(true);
    outputFormat2.setOmitEncoding(false);
    boolean b11 = outputFormat2.isOmitEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);

  }

  @Test
  public void test186() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test186"); }


    OutputFormat outputFormat3 = new OutputFormat("", false, "");
    outputFormat3.setIndent(false);

  }

  @Test
  public void test187() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test187"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    outputFormat0.setLineSeparator("");
    outputFormat0.setOmitEncoding(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);

  }

  @Test
  public void test188() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test188"); }


    OutputFormat outputFormat0 = new OutputFormat();
    int i1 = outputFormat0.getNewLineAfterNTags();
    boolean b2 = outputFormat0.isPadText();
    boolean b3 = outputFormat0.isNewLineAfterDeclaration();
    boolean b4 = outputFormat0.isPadText();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i1 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);

  }

  @Test
  public void test189() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test189"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setPadText(false);
    boolean b11 = outputFormat2.isOmitEncoding();
    java.lang.String str12 = outputFormat2.getEncoding();
    boolean b13 = outputFormat2.isTrimText();
    outputFormat2.setLineSeparator("hi!");
    boolean b16 = outputFormat2.isNewLineAfterDeclaration();
    boolean b17 = outputFormat2.isSuppressDeclaration();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str12 + "' != '" + "UTF-8"+ "'", str12.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b16 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);

  }

  @Test
  public void test190() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test190"); }


    OutputFormat outputFormat3 = new OutputFormat("", true, "\n");
    outputFormat3.setXHTML(true);

  }

  @Test
  public void test191() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test191"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setNewlines(true);
    char char8 = outputFormat2.getAttributeQuoteCharacter();
    boolean b9 = outputFormat2.isXHTML();
    outputFormat2.setIndent(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char8 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);

  }

  @Test
  public void test192() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test192"); }


    OutputFormat outputFormat0 = OutputFormat.createPrettyPrint();
    outputFormat0.setExpandEmptyElements(false);
    outputFormat0.setNewLineAfterDeclaration(true);
    outputFormat0.setIndentSize((int)'a');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(outputFormat0);

  }

  @Test
  public void test193() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test193"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    outputFormat2.setEncoding("hi!");
    int i7 = outputFormat2.getNewLineAfterNTags();
    java.lang.String str8 = outputFormat2.getIndent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str8 + "' != '" + "hi!"+ "'", str8.equals("hi!"));

  }

  @Test
  public void test194() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test194"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    java.lang.String str6 = outputFormat2.getIndent();
    boolean b7 = outputFormat2.isSuppressDeclaration();
    outputFormat2.setNewLineAfterNTags((int)(short)1);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + "hi!"+ "'", str6.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test195() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test195"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isTrimText();
    outputFormat0.setIndent(false);
    outputFormat0.setXHTML(true);
    boolean b7 = outputFormat0.isTrimText();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test196() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test196"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isPadText();
    outputFormat0.setTrimText(true);
    outputFormat0.setNewLineAfterDeclaration(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);

  }

  @Test
  public void test197() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test197"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isTrimText();
    outputFormat0.setNewLineAfterNTags((-1));
    outputFormat0.setEncoding("");
    outputFormat0.setEncoding("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);

  }

  @Test
  public void test198() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test198"); }


    OutputFormat outputFormat2 = new OutputFormat("", false);
    boolean b3 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setNewLineAfterDeclaration(true);
    outputFormat2.setExpandEmptyElements(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == true);

  }

  @Test
  public void test199() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test199"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    outputFormat2.setLineSeparator("");
    outputFormat2.setTrimText(false);
    boolean b12 = outputFormat2.isNewlines();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == false);

  }

  @Test
  public void test200() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test200"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    boolean b5 = outputFormat2.isTrimText();
    boolean b6 = outputFormat2.isPadText();
    outputFormat2.setIndent(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);

  }

  @Test
  public void test201() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test201"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setPadText(false);
    boolean b11 = outputFormat2.isOmitEncoding();
    java.lang.String str12 = outputFormat2.getEncoding();
    boolean b13 = outputFormat2.isTrimText();
    outputFormat2.setSuppressDeclaration(false);
    outputFormat2.setIndent("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str12 + "' != '" + "UTF-8"+ "'", str12.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);

  }

  @Test
  public void test202() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test202"); }


    OutputFormat outputFormat0 = OutputFormat.createPrettyPrint();
    outputFormat0.setExpandEmptyElements(false);
    outputFormat0.setNewLineAfterDeclaration(true);
    java.lang.String str5 = outputFormat0.getEncoding();
    boolean b6 = outputFormat0.isXHTML();
    outputFormat0.setNewLineAfterNTags((int)'#');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(outputFormat0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str5 + "' != '" + "UTF-8"+ "'", str5.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);

  }

  @Test
  public void test203() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test203"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setNewlines(true);
    outputFormat2.setXHTML(true);
    outputFormat2.setSuppressDeclaration(true);
    boolean b14 = outputFormat2.isExpandEmptyElements();
    java.lang.String str15 = outputFormat2.getEncoding();
    boolean b16 = outputFormat2.isSuppressDeclaration();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str15 + "' != '" + "UTF-8"+ "'", str15.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b16 == true);

  }

  @Test
  public void test204() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test204"); }


    OutputFormat outputFormat1 = new OutputFormat("hi!");

  }

  @Test
  public void test205() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test205"); }


    OutputFormat outputFormat3 = new OutputFormat("", false, "\n");

  }

  @Test
  public void test206() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test206"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isTrimText();
    outputFormat0.setNewLineAfterNTags((-1));
    boolean b5 = outputFormat0.isOmitEncoding();
    outputFormat0.setTrimText(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);

  }

  @Test
  public void test207() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test207"); }


    OutputFormat outputFormat3 = new OutputFormat("UTF-8", true, "                                                                                                    ");

  }

  @Test
  public void test208() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test208"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setEncoding("UTF-8");
    java.lang.String[] str_array11 = new java.lang.String[] { "\n", "\n", "  ", " ", "hi!" };
    int i13 = outputFormat2.parseOptions(str_array11, (int)(short)0);
    boolean b14 = outputFormat2.isOmitEncoding();
    int i15 = outputFormat2.getNewLineAfterNTags();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i15 == 0);

  }

  @Test
  public void test209() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test209"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    boolean b6 = outputFormat2.isOmitEncoding();
    boolean b7 = outputFormat2.isOmitEncoding();
    int i8 = outputFormat2.getNewLineAfterNTags();
    outputFormat2.setNewLineAfterNTags((int)'\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 0);

  }

  @Test
  public void test210() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test210"); }


    OutputFormat outputFormat2 = new OutputFormat("", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');

  }

  @Test
  public void test211() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test211"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    outputFormat0.setIndent("\n");
    outputFormat0.setIndent(true);
    boolean b7 = outputFormat0.isNewlines();
    char char8 = outputFormat0.getAttributeQuoteCharacter();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char8 == '\"');

  }

  @Test
  public void test212() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test212"); }


    OutputFormat outputFormat3 = new OutputFormat("                                                                                                    ", true, "UTF-8");

  }

  @Test
  public void test213() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test213"); }


    OutputFormat outputFormat0 = new OutputFormat();
    outputFormat0.setIndent(true);
    java.lang.String[] str_array9 = new java.lang.String[] { "hi!", "", "", "hi!", "UTF-8", "hi!" };
    int i11 = outputFormat0.parseOptions(str_array9, (int)(short)10);
    outputFormat0.setSuppressDeclaration(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i11 == 10);

  }

  @Test
  public void test214() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test214"); }


    OutputFormat outputFormat1 = new OutputFormat("UTF-8");
    outputFormat1.setIndentSize((int)'#');

  }

  @Test
  public void test215() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test215"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isTrimText();
    outputFormat0.setNewLineAfterNTags((-1));
    outputFormat0.setEncoding("");
    boolean b7 = outputFormat0.isNewlines();
    boolean b8 = outputFormat0.isNewLineAfterDeclaration();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == true);

  }

  @Test
  public void test216() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test216"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setPadText(false);
    boolean b11 = outputFormat2.isOmitEncoding();
    java.lang.String str12 = outputFormat2.getEncoding();
    boolean b13 = outputFormat2.isTrimText();
    outputFormat2.setLineSeparator("hi!");
    boolean b16 = outputFormat2.isNewLineAfterDeclaration();
    boolean b17 = outputFormat2.isOmitEncoding();
    outputFormat2.setEncoding("                                                                                                    ");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str12 + "' != '" + "UTF-8"+ "'", str12.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b16 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);

  }

  @Test
  public void test217() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test217"); }


    OutputFormat outputFormat3 = new OutputFormat("\n", true, "hi!");
    outputFormat3.setTrimText(true);
    java.lang.String str6 = outputFormat3.getEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + "hi!"+ "'", str6.equals("hi!"));

  }

  @Test
  public void test218() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test218"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    java.lang.String str5 = outputFormat2.getIndent();
    outputFormat2.setExpandEmptyElements(false);
    java.lang.String[] str_array14 = new java.lang.String[] { "hi!", "UTF-8", "UTF-8", "", "", "UTF-8" };
    int i16 = outputFormat2.parseOptions(str_array14, 0);
    // The following exception was thrown during execution in test generation
    try {
    outputFormat2.setAttributeQuoteCharacter('4');
      org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException");
    } catch (java.lang.IllegalArgumentException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.IllegalArgumentException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str5 + "' != '" + "hi!"+ "'", str5.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i16 == 0);

  }

  @Test
  public void test219() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test219"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    boolean b5 = outputFormat2.isTrimText();
    boolean b6 = outputFormat2.isNewlines();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);

  }

  @Test
  public void test220() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test220"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    outputFormat0.setIndent("\n");
    boolean b5 = outputFormat0.isXHTML();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);

  }

  @Test
  public void test221() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test221"); }


    OutputFormat outputFormat2 = new OutputFormat("", false);
    outputFormat2.setXHTML(false);
    outputFormat2.setXHTML(false);

  }

  @Test
  public void test222() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test222"); }


    OutputFormat outputFormat3 = new OutputFormat("UTF-8", true, "hi!");
    outputFormat3.setEncoding("hi!");
    outputFormat3.setEncoding(" ");
    boolean b8 = outputFormat3.isOmitEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);

  }

  @Test
  public void test223() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test223"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setPadText(false);
    boolean b11 = outputFormat2.isOmitEncoding();
    java.lang.String str12 = outputFormat2.getEncoding();
    boolean b13 = outputFormat2.isTrimText();
    outputFormat2.setLineSeparator("hi!");
    outputFormat2.setPadText(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str12 + "' != '" + "UTF-8"+ "'", str12.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);

  }

  @Test
  public void test224() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test224"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    boolean b8 = outputFormat2.isSuppressDeclaration();
    boolean b9 = outputFormat2.isOmitEncoding();
    boolean b10 = outputFormat2.isOmitEncoding();
    outputFormat2.setIndentSize((int)(short)100);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);

  }

  @Test
  public void test225() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test225"); }


    OutputFormat outputFormat0 = OutputFormat.createPrettyPrint();
    outputFormat0.setAttributeQuoteCharacter('\"');
    outputFormat0.setNewlines(false);
    boolean b5 = outputFormat0.isExpandEmptyElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(outputFormat0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);

  }

  @Test
  public void test226() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test226"); }


    OutputFormat outputFormat2 = new OutputFormat("UTF-8", true);
    boolean b3 = outputFormat2.isNewLineAfterDeclaration();
    boolean b4 = outputFormat2.isOmitEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);

  }

  @Test
  public void test227() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test227"); }


    OutputFormat outputFormat0 = new OutputFormat();
    outputFormat0.setIndent(true);
    outputFormat0.setNewLineAfterDeclaration(true);
    outputFormat0.setIndent(false);
    outputFormat0.setSuppressDeclaration(true);
    outputFormat0.setXHTML(false);
    outputFormat0.setXHTML(false);

  }

  @Test
  public void test228() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test228"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    boolean b5 = outputFormat2.isPadText();
    char char6 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setXHTML(false);
    boolean b9 = outputFormat2.isNewlines();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char6 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);

  }

  @Test
  public void test229() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test229"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    outputFormat0.setIndent("\n");
    outputFormat0.setTrimText(true);
    boolean b7 = outputFormat0.isNewlines();
    outputFormat0.setXHTML(true);
    outputFormat0.setIndent("\n");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test230() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test230"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    java.lang.String str2 = outputFormat0.getLineSeparator();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str2 + "' != '" + "\n"+ "'", str2.equals("\n"));

  }

  @Test
  public void test231() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test231"); }


    OutputFormat outputFormat0 = new OutputFormat();
    outputFormat0.setIndent(true);
    java.lang.String str3 = outputFormat0.getEncoding();
    outputFormat0.setNewLineAfterNTags(100);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "UTF-8"+ "'", str3.equals("UTF-8"));

  }

  @Test
  public void test232() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test232"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    boolean b6 = outputFormat2.isOmitEncoding();
    java.lang.String str7 = outputFormat2.getIndent();
    boolean b8 = outputFormat2.isXHTML();
    boolean b9 = outputFormat2.isSuppressDeclaration();
    boolean b10 = outputFormat2.isXHTML();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);

  }

  @Test
  public void test233() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test233"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setNewlines(true);
    boolean b6 = outputFormat2.isXHTML();
    outputFormat2.setNewlines(true);
    java.lang.String str9 = outputFormat2.getIndent();
    outputFormat2.setIndentSize((int)(short)10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str9 + "' != '" + "hi!"+ "'", str9.equals("hi!"));

  }

  @Test
  public void test234() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test234"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setNewLineAfterDeclaration(true);
    outputFormat2.setExpandEmptyElements(true);
    outputFormat2.setOmitEncoding(false);
    boolean b11 = outputFormat2.isNewlines();
    outputFormat2.setPadText(true);
    outputFormat2.setTrimText(true);
    outputFormat2.setPadText(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);

  }

  @Test
  public void test235() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test235"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    boolean b3 = outputFormat0.isPadText();
    outputFormat0.setNewlines(true);
    java.lang.String str6 = outputFormat0.getEncoding();
    outputFormat0.setSuppressDeclaration(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + "UTF-8"+ "'", str6.equals("UTF-8"));

  }

  @Test
  public void test236() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test236"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    boolean b6 = outputFormat2.isOmitEncoding();
    java.lang.String str7 = outputFormat2.getIndent();
    boolean b8 = outputFormat2.isXHTML();
    boolean b9 = outputFormat2.isSuppressDeclaration();
    boolean b10 = outputFormat2.isNewLineAfterDeclaration();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);

  }

  @Test
  public void test237() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test237"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setPadText(false);
    boolean b11 = outputFormat2.isOmitEncoding();
    java.lang.String str12 = outputFormat2.getEncoding();
    boolean b13 = outputFormat2.isTrimText();
    java.lang.String str14 = outputFormat2.getIndent();
    outputFormat2.setExpandEmptyElements(true);
    outputFormat2.setNewLineAfterDeclaration(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str12 + "' != '" + "UTF-8"+ "'", str12.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str14 + "' != '" + "                                                                                                    "+ "'", str14.equals("                                                                                                    "));

  }

  @Test
  public void test238() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test238"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setIndent("");
    outputFormat2.setNewLineAfterNTags(0);
    boolean b9 = outputFormat2.isSuppressDeclaration();
    outputFormat2.setIndentSize((int)(byte)100);
    java.lang.String str12 = outputFormat2.getIndent();
    char char13 = outputFormat2.getAttributeQuoteCharacter();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str12 + "' != '" + "                                                                                                    "+ "'", str12.equals("                                                                                                    "));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char13 == '\"');

  }

  @Test
  public void test239() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test239"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    outputFormat2.setEncoding("hi!");
    java.lang.String str7 = outputFormat2.getIndent();
    outputFormat2.setTrimText(false);
    char char10 = outputFormat2.getAttributeQuoteCharacter();
    java.lang.String str11 = outputFormat2.getIndent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char10 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str11 + "' != '" + "hi!"+ "'", str11.equals("hi!"));

  }

  @Test
  public void test240() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test240"); }


    OutputFormat outputFormat3 = new OutputFormat("", true, "\n");
    outputFormat3.setIndent(false);
    outputFormat3.setSuppressDeclaration(false);

  }

  @Test
  public void test241() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test241"); }


    OutputFormat outputFormat2 = new OutputFormat("  ", false);

  }

  @Test
  public void test242() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test242"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    outputFormat0.setIndent("\n");
    outputFormat0.setTrimText(true);
    outputFormat0.setIndent(false);
    outputFormat0.setSuppressDeclaration(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);

  }

  @Test
  public void test243() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test243"); }


    OutputFormat outputFormat0 = OutputFormat.createPrettyPrint();
    outputFormat0.setExpandEmptyElements(false);
    outputFormat0.setNewLineAfterDeclaration(true);
    java.lang.String str5 = outputFormat0.getEncoding();
    boolean b6 = outputFormat0.isXHTML();
    boolean b7 = outputFormat0.isXHTML();
    outputFormat0.setNewLineAfterNTags(0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(outputFormat0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str5 + "' != '" + "UTF-8"+ "'", str5.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test244() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test244"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    boolean b5 = outputFormat2.isNewlines();
    outputFormat2.setNewLineAfterNTags(0);
    outputFormat2.setExpandEmptyElements(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);

  }

  @Test
  public void test245() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test245"); }


    OutputFormat outputFormat2 = new OutputFormat("", false);
    boolean b3 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setNewLineAfterDeclaration(true);
    int i6 = outputFormat2.getNewLineAfterNTags();
    java.lang.String str7 = outputFormat2.getLineSeparator();
    outputFormat2.setXHTML(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "\n"+ "'", str7.equals("\n"));

  }

  @Test
  public void test246() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test246"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setEncoding("UTF-8");
    outputFormat2.setNewLineAfterNTags((int)(short)1);
    java.lang.String str8 = outputFormat2.getLineSeparator();
    outputFormat2.setPadText(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str8 + "' != '" + "\n"+ "'", str8.equals("\n"));

  }

  @Test
  public void test247() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test247"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isTrimText();
    outputFormat0.setNewLineAfterNTags((-1));
    outputFormat0.setEncoding("");
    java.lang.String str7 = outputFormat0.getIndent();
    boolean b8 = outputFormat0.isXHTML();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);

  }

  @Test
  public void test248() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test248"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setNewlines(true);
    outputFormat2.setXHTML(true);
    outputFormat2.setSuppressDeclaration(true);
    boolean b14 = outputFormat2.isExpandEmptyElements();
    outputFormat2.setXHTML(false);
    outputFormat2.setTrimText(false);
    outputFormat2.setNewLineAfterDeclaration(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == false);

  }

  @Test
  public void test249() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test249"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setIndent("");
    outputFormat2.setNewLineAfterNTags(0);
    boolean b9 = outputFormat2.isSuppressDeclaration();
    outputFormat2.setIndentSize((int)(byte)100);
    java.lang.String str12 = outputFormat2.getIndent();
    java.lang.String str13 = outputFormat2.getEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str12 + "' != '" + "                                                                                                    "+ "'", str12.equals("                                                                                                    "));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str13 + "' != '" + "UTF-8"+ "'", str13.equals("UTF-8"));

  }

  @Test
  public void test250() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test250"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setPadText(false);
    boolean b11 = outputFormat2.isOmitEncoding();
    java.lang.String str12 = outputFormat2.getEncoding();
    OutputFormat outputFormat15 = new OutputFormat("", false);
    boolean b16 = outputFormat15.isNewLineAfterDeclaration();
    outputFormat15.setNewLineAfterDeclaration(true);
    char char19 = outputFormat15.getAttributeQuoteCharacter();
    OutputFormat outputFormat22 = new OutputFormat("hi!", false);
    java.lang.String str23 = outputFormat22.getIndent();
    boolean b24 = outputFormat22.isPadText();
    java.lang.String str25 = outputFormat22.getIndent();
    outputFormat22.setExpandEmptyElements(false);
    java.lang.String[] str_array34 = new java.lang.String[] { "hi!", "UTF-8", "UTF-8", "", "", "UTF-8" };
    int i36 = outputFormat22.parseOptions(str_array34, 0);
    int i38 = outputFormat15.parseOptions(str_array34, 0);
    int i40 = outputFormat2.parseOptions(str_array34, 32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str12 + "' != '" + "UTF-8"+ "'", str12.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b16 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char19 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str23 + "' != '" + "hi!"+ "'", str23.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b24 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str25 + "' != '" + "hi!"+ "'", str25.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array34);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i36 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i38 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i40 == 32);

  }

  @Test
  public void test251() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test251"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setNewlines(false);
    boolean b7 = outputFormat2.isSuppressDeclaration();
    outputFormat2.setNewLineAfterNTags(1);
    java.lang.String str10 = outputFormat2.getLineSeparator();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str10 + "' != '" + "\n"+ "'", str10.equals("\n"));

  }

  @Test
  public void test252() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test252"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setNewlines(true);
    outputFormat2.setIndentSize((int)(byte)10);
    outputFormat2.setTrimText(true);
    outputFormat2.setXHTML(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);

  }

  @Test
  public void test253() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test253"); }


    OutputFormat outputFormat2 = new OutputFormat("UTF-8", false);
    boolean b3 = outputFormat2.isXHTML();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);

  }

  @Test
  public void test254() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test254"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setSuppressDeclaration(false);
    int i6 = outputFormat2.getNewLineAfterNTags();
    boolean b7 = outputFormat2.isNewlines();
    boolean b8 = outputFormat2.isNewlines();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);

  }

  @Test
  public void test255() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test255"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    boolean b8 = outputFormat2.isSuppressDeclaration();
    boolean b9 = outputFormat2.isOmitEncoding();
    boolean b10 = outputFormat2.isOmitEncoding();
    char char11 = outputFormat2.getAttributeQuoteCharacter();
    boolean b12 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setNewLineAfterNTags((-1));
    outputFormat2.setNewlines(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char11 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == true);

  }

  @Test
  public void test256() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test256"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndentSize(100);
    outputFormat2.setTrimText(false);
    outputFormat2.setPadText(false);
    boolean b11 = outputFormat2.isOmitEncoding();
    java.lang.String str12 = outputFormat2.getEncoding();
    boolean b13 = outputFormat2.isTrimText();
    outputFormat2.setLineSeparator("hi!");
    boolean b16 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setNewLineAfterDeclaration(true);
    outputFormat2.setIndent("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str12 + "' != '" + "UTF-8"+ "'", str12.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b16 == true);

  }

  @Test
  public void test257() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test257"); }


    OutputFormat outputFormat3 = new OutputFormat("  ", false, "");

  }

  @Test
  public void test258() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test258"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isTrimText();
    char char3 = outputFormat0.getAttributeQuoteCharacter();
    outputFormat0.setOmitEncoding(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');

  }

  @Test
  public void test259() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test259"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isNewLineAfterDeclaration();
    outputFormat2.setNewlines(false);
    boolean b7 = outputFormat2.isSuppressDeclaration();
    outputFormat2.setNewLineAfterNTags(1);
    boolean b10 = outputFormat2.isOmitEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);

  }

  @Test
  public void test260() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test260"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    java.lang.String str6 = outputFormat2.getIndent();
    boolean b7 = outputFormat2.isSuppressDeclaration();
    boolean b8 = outputFormat2.isOmitEncoding();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + "hi!"+ "'", str6.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);

  }

  @Test
  public void test261() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test261"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setNewLineAfterNTags((int)(short)0);
    boolean b10 = outputFormat2.isExpandEmptyElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);

  }

  @Test
  public void test262() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test262"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    boolean b3 = outputFormat2.isTrimText();
    outputFormat2.setNewlines(true);
    boolean b6 = outputFormat2.isXHTML();
    outputFormat2.setNewlines(true);
    java.lang.String str9 = outputFormat2.getEncoding();
    outputFormat2.setIndent("UTF-8");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str9 + "' != '" + "UTF-8"+ "'", str9.equals("UTF-8"));

  }

  @Test
  public void test263() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test263"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    outputFormat0.setIndent("\n");
    outputFormat0.setTrimText(true);
    boolean b7 = outputFormat0.isNewlines();
    outputFormat0.setXHTML(true);
    char char10 = outputFormat0.getAttributeQuoteCharacter();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char10 == '\"');

  }

  @Test
  public void test264() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test264"); }


    OutputFormat outputFormat2 = new OutputFormat("UTF-8", true);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');

  }

  @Test
  public void test265() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test265"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    boolean b8 = outputFormat2.isSuppressDeclaration();
    boolean b9 = outputFormat2.isOmitEncoding();
    boolean b10 = outputFormat2.isOmitEncoding();
    outputFormat2.setLineSeparator("\n");
    boolean b13 = outputFormat2.isSuppressDeclaration();
    boolean b14 = outputFormat2.isXHTML();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == false);

  }

  @Test
  public void test266() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test266"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    java.lang.String str5 = outputFormat2.getIndent();
    boolean b6 = outputFormat2.isOmitEncoding();
    boolean b7 = outputFormat2.isXHTML();
    outputFormat2.setNewLineAfterNTags((int)'#');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str5 + "' != '" + "hi!"+ "'", str5.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test267() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test267"); }


    OutputFormat outputFormat0 = OutputFormat.createCompactFormat();
    boolean b1 = outputFormat0.isNewLineAfterDeclaration();
    outputFormat0.setSuppressDeclaration(true);
    int i4 = outputFormat0.getNewLineAfterNTags();
    outputFormat0.setPadText(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(outputFormat0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b1 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i4 == 0);

  }

  @Test
  public void test268() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test268"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setPadText(false);
    outputFormat2.setEncoding("UTF-8");
    char char7 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setEncoding("\n");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char7 == '\"');

  }

  @Test
  public void test269() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test269"); }


    OutputFormat outputFormat3 = new OutputFormat(" ", true, " ");
    outputFormat3.setIndentSize(1);
    outputFormat3.setEncoding("UTF-8");

  }

  @Test
  public void test270() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test270"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setNewlines(true);
    outputFormat2.setXHTML(true);
    outputFormat2.setSuppressDeclaration(true);
    outputFormat2.setNewLineAfterNTags(32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));

  }

  @Test
  public void test271() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test271"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    outputFormat0.setIndent("\n");
    outputFormat0.setTrimText(true);
    boolean b7 = outputFormat0.isNewlines();
    outputFormat0.setXHTML(true);
    outputFormat0.setTrimText(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test272() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test272"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    outputFormat2.setOmitEncoding(false);
    boolean b5 = outputFormat2.isTrimText();
    OutputFormat outputFormat8 = new OutputFormat("hi!", false);
    outputFormat8.setOmitEncoding(false);
    outputFormat8.setIndentSize(100);
    outputFormat8.setTrimText(false);
    outputFormat8.setPadText(false);
    outputFormat8.setLineSeparator("\n");
    OutputFormat outputFormat19 = new OutputFormat();
    outputFormat19.setIndent(true);
    java.lang.String str22 = outputFormat19.getEncoding();
    OutputFormat outputFormat25 = new OutputFormat("hi!", false);
    java.lang.String str26 = outputFormat25.getIndent();
    boolean b27 = outputFormat25.isPadText();
    java.lang.String str28 = outputFormat25.getIndent();
    outputFormat25.setExpandEmptyElements(false);
    java.lang.String[] str_array37 = new java.lang.String[] { "hi!", "UTF-8", "UTF-8", "", "", "UTF-8" };
    int i39 = outputFormat25.parseOptions(str_array37, 0);
    int i41 = outputFormat19.parseOptions(str_array37, (int)'4');
    int i43 = outputFormat8.parseOptions(str_array37, (int)(byte)0);
    int i45 = outputFormat2.parseOptions(str_array37, (int)'#');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str22 + "' != '" + "UTF-8"+ "'", str22.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str26 + "' != '" + "hi!"+ "'", str26.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b27 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str28 + "' != '" + "hi!"+ "'", str28.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i39 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i41 == 52);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i43 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i45 == 35);

  }

  @Test
  public void test273() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test273"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setNewLineAfterNTags((int)(short)0);
    outputFormat2.setOmitEncoding(true);
    boolean b12 = outputFormat2.isTrimText();
    boolean b13 = outputFormat2.isNewlines();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);

  }

  @Test
  public void test274() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test274"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setNewlines(true);
    int i10 = outputFormat2.getNewLineAfterNTags();
    outputFormat2.setIndentSize(32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);

  }

  @Test
  public void test275() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test275"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setNewLineAfterDeclaration(true);
    outputFormat2.setExpandEmptyElements(true);
    outputFormat2.setOmitEncoding(false);
    boolean b11 = outputFormat2.isNewlines();
    java.lang.String str12 = outputFormat2.getEncoding();
    outputFormat2.setNewLineAfterDeclaration(true);
    outputFormat2.setIndent("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str12 + "' != '" + "UTF-8"+ "'", str12.equals("UTF-8"));

  }

  @Test
  public void test276() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test276"); }


    OutputFormat outputFormat3 = new OutputFormat("", true, "\n");
    java.lang.String str4 = outputFormat3.getLineSeparator();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "\n"+ "'", str4.equals("\n"));

  }

  @Test
  public void test277() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test277"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    boolean b4 = outputFormat2.isPadText();
    java.lang.String str5 = outputFormat2.getIndent();
    outputFormat2.setExpandEmptyElements(false);
    outputFormat2.setSuppressDeclaration(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str5 + "' != '" + "hi!"+ "'", str5.equals("hi!"));

  }

  @Test
  public void test278() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test278"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    boolean b6 = outputFormat2.isOmitEncoding();
    boolean b7 = outputFormat2.isOmitEncoding();
    outputFormat2.setAttributeQuoteCharacter('\"');
    outputFormat2.setLineSeparator("  ");
    boolean b12 = outputFormat2.isNewlines();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == false);

  }

  @Test
  public void test279() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test279"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isXHTML();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    boolean b6 = outputFormat2.isOmitEncoding();
    boolean b7 = outputFormat2.isOmitEncoding();
    outputFormat2.setOmitEncoding(false);
    outputFormat2.setIndent(true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test280() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test280"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    char char3 = outputFormat2.getAttributeQuoteCharacter();
    boolean b4 = outputFormat2.isTrimText();
    char char5 = outputFormat2.getAttributeQuoteCharacter();
    outputFormat2.setExpandEmptyElements(true);
    outputFormat2.setLineSeparator("");
    boolean b10 = outputFormat2.isNewlines();
    boolean b11 = outputFormat2.isXHTML();
    char char12 = outputFormat2.getAttributeQuoteCharacter();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char3 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char5 == '\"');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(char12 == '\"');

  }

  @Test
  public void test281() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test281"); }


    OutputFormat outputFormat2 = new OutputFormat("hi!", false);
    java.lang.String str3 = outputFormat2.getIndent();
    java.lang.String str4 = outputFormat2.getIndent();
    outputFormat2.setIndent("");
    outputFormat2.setEncoding("");
    outputFormat2.setNewlines(false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));

  }

  @Test
  public void test282() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test282"); }


    OutputFormat outputFormat0 = new OutputFormat();
    java.lang.String str1 = outputFormat0.getEncoding();
    boolean b2 = outputFormat0.isXHTML();
    outputFormat0.setIndent("\n");
    outputFormat0.setIndent(true);
    boolean b7 = outputFormat0.isNewlines();
    outputFormat0.setTrimText(true);
    boolean b10 = outputFormat0.isExpandEmptyElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str1 + "' != '" + "UTF-8"+ "'", str1.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);

  }

  @Test
  public void test283() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test283"); }


    OutputFormat outputFormat0 = OutputFormat.createCompactFormat();
    outputFormat0.setExpandEmptyElements(false);
    outputFormat0.setExpandEmptyElements(false);
    OutputFormat outputFormat7 = new OutputFormat("hi!", false);
    outputFormat7.setOmitEncoding(false);
    outputFormat7.setIndentSize(100);
    outputFormat7.setTrimText(false);
    outputFormat7.setPadText(false);
    outputFormat7.setLineSeparator("\n");
    OutputFormat outputFormat18 = new OutputFormat();
    outputFormat18.setIndent(true);
    java.lang.String str21 = outputFormat18.getEncoding();
    OutputFormat outputFormat24 = new OutputFormat("hi!", false);
    java.lang.String str25 = outputFormat24.getIndent();
    boolean b26 = outputFormat24.isPadText();
    java.lang.String str27 = outputFormat24.getIndent();
    outputFormat24.setExpandEmptyElements(false);
    java.lang.String[] str_array36 = new java.lang.String[] { "hi!", "UTF-8", "UTF-8", "", "", "UTF-8" };
    int i38 = outputFormat24.parseOptions(str_array36, 0);
    int i40 = outputFormat18.parseOptions(str_array36, (int)'4');
    int i42 = outputFormat7.parseOptions(str_array36, (int)(byte)0);
    int i44 = outputFormat0.parseOptions(str_array36, (int)(byte)10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(outputFormat0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str21 + "' != '" + "UTF-8"+ "'", str21.equals("UTF-8"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str25 + "' != '" + "hi!"+ "'", str25.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b26 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str27 + "' != '" + "hi!"+ "'", str27.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(str_array36);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i38 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i40 == 52);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i42 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i44 == 10);

  }

}
