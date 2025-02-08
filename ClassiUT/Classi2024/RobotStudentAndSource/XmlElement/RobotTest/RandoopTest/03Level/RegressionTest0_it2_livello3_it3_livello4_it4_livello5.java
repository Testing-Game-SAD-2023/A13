package mypackage;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0_it2_livello3_it3_livello4_it4_livello5 {

  public static boolean debug = false;

  @Test
  public void test001() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test001"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    // The following exception was thrown during execution in test generation
    try {
    java.lang.Object obj5 = xmlElement2.addAttribute("", "");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test002() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test002"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement4 = xmlElement2.getElement((int)'4');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test003() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test003"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = xmlElement2.getParent();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement4 = xmlElement3.getParent();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement3);

  }

  @Test
  public void test004() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test004"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = xmlElement2.getParent();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("hi!", hashtable_str_str5);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement7 = xmlElement3.removeElement(xmlElement6);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement3);

  }

  @Test
  public void test005() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test005"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement5 = xmlElement2.removeElement((int)(byte)0);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test006() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test006"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    // The following exception was thrown during execution in test generation
    try {
    java.lang.Object obj7 = xmlElement2.addAttribute("", "");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);

  }

  @Test
  public void test007() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test007"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.util.List list6 = xmlElement5.getElements();
    // The following exception was thrown during execution in test generation
    try {
    xmlElement2.insertElement(xmlElement5, 10);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list6);

  }

  @Test
  public void test008() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test008"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement.printNode(xmlElement2, "");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test009() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test009"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    java.util.Observer observer9 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement6.addObserver(observer9);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);

  }

  @Test
  public void test010() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test010"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = xmlElement2.getParent();
    // The following exception was thrown during execution in test generation
    try {
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = xmlElement3.getAttributes();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement3);

  }

  @Test
  public void test011() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test011"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    xmlElement2.setName("");
    int i7 = xmlElement2.countObservers();
    java.util.Observer observer8 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement2.addObserver(observer8);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);

  }

  @Test
  public void test012() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test012"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    XmlElement xmlElement10 = xmlElement2.getElement("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement10);

  }

  @Test
  public void test013() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test013"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    boolean b3 = xmlElement2.hasChanged();
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str6 = xmlElement2.getAttribute("", "");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);

  }

  @Test
  public void test014() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test014"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    xmlElement6.setName("hi!");
    // The following exception was thrown during execution in test generation
    try {
    java.util.Enumeration enumeration11 = xmlElement6.getAttributeNames();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);

  }

  @Test
  public void test015() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test015"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    xmlElement6.notifyObservers((java.lang.Object)(-1));
    java.util.List list11 = xmlElement6.getElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list11);

  }

  @Test
  public void test016() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test016"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement4 = xmlElement2.addSubElement("");
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement6 = xmlElement2.removeElement((int)(byte)100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);

  }

  @Test
  public void test017() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test017"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    xmlElement2.setName("");
    int i7 = xmlElement2.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("", hashtable_str_str9);
    XmlElement xmlElement11 = xmlElement2.removeElement(xmlElement10);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement.printNode(xmlElement10, "hi!");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);

  }

  @Test
  public void test018() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test018"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement10 = xmlElement6.getElement((int)(short)10);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);

  }

  @Test
  public void test019() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test019"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    xmlElement2.setName("");
    int i7 = xmlElement2.count();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);

  }

  @Test
  public void test020() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test020"); }


    XmlElement xmlElement2 = new XmlElement("hi!", "hi!");
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement4 = xmlElement2.removeElement(0);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test021() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test021"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.List list3 = xmlElement2.getElements();
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str6 = xmlElement2.getAttribute("hi!", "hi!");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);

  }

  @Test
  public void test022() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test022"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.setData("");

  }

  @Test
  public void test023() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test023"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.List list3 = xmlElement2.getElements();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement5 = xmlElement2.getElement((int)(byte)100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);

  }

  @Test
  public void test024() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test024"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = xmlElement2.getParent();
    // The following exception was thrown during execution in test generation
    try {
    xmlElement3.deleteObservers();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement3);

  }

  @Test
  public void test025() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test025"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    java.lang.String str9 = xmlElement2.getData();
    java.util.Observer observer10 = null;
    xmlElement2.deleteObserver(observer10);
    xmlElement2.removeAllElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("hi!", hashtable_str_str14);
    XmlElement xmlElement16 = null;
    XmlElement xmlElement17 = xmlElement15.removeElement(xmlElement16);
    xmlElement15.setName("");
    int i20 = xmlElement15.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str22 = null;
    XmlElement xmlElement23 = new XmlElement("", hashtable_str_str22);
    XmlElement xmlElement24 = xmlElement15.removeElement(xmlElement23);
    xmlElement15.deleteObservers();
    int i26 = xmlElement15.count();
    boolean b27 = xmlElement2.addElement(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i20 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i26 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b27 == true);

  }

  @Test
  public void test026() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test026"); }


    XmlElement xmlElement0 = new XmlElement();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str2 = null;
    XmlElement xmlElement3 = new XmlElement("hi!", hashtable_str_str2);
    XmlElement xmlElement4 = xmlElement3.getParent();
    XmlElement xmlElement6 = xmlElement3.getElement("");
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement7 = xmlElement0.addSubElement(xmlElement6);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);

  }

  @Test
  public void test027() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test027"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    xmlElement2.setName("");
    int i7 = xmlElement2.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("", hashtable_str_str9);
    XmlElement xmlElement11 = xmlElement2.removeElement(xmlElement10);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("hi!", hashtable_str_str14);
    XmlElement xmlElement17 = xmlElement15.addSubElement("");
    xmlElement2.append(xmlElement17);
    boolean b20 = xmlElement17.equals((java.lang.Object)100.0d);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b20 == false);

  }

  @Test
  public void test028() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test028"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("hi!", hashtable_str_str9);
    xmlElement10.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str13 = null;
    XmlElement xmlElement14 = new XmlElement("", hashtable_str_str13);
    java.util.List list15 = xmlElement14.getElements();
    boolean b16 = xmlElement10.equals((java.lang.Object)xmlElement14);
    java.lang.String str17 = xmlElement10.getData();
    xmlElement6.setParent(xmlElement10);
    XmlElement xmlElement19 = xmlElement2.addSubElement(xmlElement10);
    // The following exception was thrown during execution in test generation
    try {
    java.util.Enumeration enumeration20 = xmlElement19.getAttributeNames();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b16 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement19);

  }

  @Test
  public void test029() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test029"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = xmlElement2.getParent();
    xmlElement2.deleteObservers();
    int i5 = xmlElement2.count();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("", hashtable_str_str7);
    java.util.List list9 = xmlElement8.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    xmlElement12.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str15 = null;
    XmlElement xmlElement16 = new XmlElement("", hashtable_str_str15);
    java.util.List list17 = xmlElement16.getElements();
    boolean b18 = xmlElement12.equals((java.lang.Object)xmlElement16);
    java.lang.String str19 = xmlElement12.getData();
    xmlElement8.setParent(xmlElement12);
    boolean b22 = xmlElement12.equals((java.lang.Object)0.0d);
    xmlElement12.setName("");
    // The following exception was thrown during execution in test generation
    try {
    xmlElement2.insertElement(xmlElement12, (int)'4');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b18 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b22 == false);

  }

  @Test
  public void test030() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test030"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement.printNode(xmlElement4, "hi!");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);

  }

  @Test
  public void test031() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test031"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    xmlElement2.setName("");
    int i7 = xmlElement2.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("", hashtable_str_str9);
    XmlElement xmlElement11 = xmlElement2.removeElement(xmlElement10);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("hi!", hashtable_str_str14);
    XmlElement xmlElement17 = xmlElement15.addSubElement("");
    xmlElement2.append(xmlElement17);
    xmlElement2.notifyObservers();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement21 = xmlElement2.removeElement((int)(short)10);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);

  }

  @Test
  public void test032() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test032"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Observer observer3 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement2.addObserver(observer3);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test033() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test033"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.notifyObservers();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement5 = xmlElement2.removeElement((-1));
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test034() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test034"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement4 = xmlElement2.addSubElement("");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement7 = xmlElement4.getElement((int)(short)-1);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);

  }

  @Test
  public void test035() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test035"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    boolean b3 = xmlElement2.hasChanged();
    int i4 = xmlElement2.countObservers();
    XmlElement xmlElement5 = xmlElement2.getParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i4 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement5);

  }

  @Test
  public void test036() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test036"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    xmlElement2.setName("");
    int i7 = xmlElement2.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("", hashtable_str_str9);
    XmlElement xmlElement11 = xmlElement2.removeElement(xmlElement10);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("hi!", hashtable_str_str14);
    XmlElement xmlElement16 = null;
    XmlElement xmlElement17 = xmlElement15.removeElement(xmlElement16);
    xmlElement15.setName("");
    int i20 = xmlElement15.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str22 = null;
    XmlElement xmlElement23 = new XmlElement("", hashtable_str_str22);
    XmlElement xmlElement24 = xmlElement15.removeElement(xmlElement23);
    boolean b25 = xmlElement2.addElement(xmlElement23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i20 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b25 == true);

  }

  @Test
  public void test037() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test037"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    xmlElement2.setData("hi!");
    XmlElement xmlElement12 = xmlElement2.getElement("hi!");
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str13 = xmlElement12.getName();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement12);

  }

  @Test
  public void test038() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test038"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    xmlElement2.setName("");
    int i7 = xmlElement2.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("", hashtable_str_str9);
    XmlElement xmlElement11 = xmlElement2.removeElement(xmlElement10);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("hi!", hashtable_str_str14);
    XmlElement xmlElement17 = xmlElement15.addSubElement("");
    xmlElement2.append(xmlElement17);
    xmlElement17.setName("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);

  }

  @Test
  public void test039() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test039"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.notifyObservers();
    xmlElement2.removeAllElements();
    xmlElement2.removeAllElements();

  }

  @Test
  public void test040() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test040"); }


    XmlElement xmlElement0 = new XmlElement();
    int i1 = xmlElement0.count();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = null;
    XmlElement xmlElement4 = new XmlElement("hi!", hashtable_str_str3);
    xmlElement4.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("", hashtable_str_str7);
    java.util.List list9 = xmlElement8.getElements();
    boolean b10 = xmlElement4.equals((java.lang.Object)xmlElement8);
    xmlElement8.setName("hi!");
    xmlElement0.append(xmlElement8);
    java.util.Observer observer14 = null;
    xmlElement0.deleteObserver(observer14);
    XmlElement xmlElement17 = xmlElement0.addSubElement("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i1 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);

  }

  @Test
  public void test041() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test041"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str6 = null;
    XmlElement xmlElement7 = new XmlElement("", hashtable_str_str6);
    java.util.List list8 = xmlElement7.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("", hashtable_str_str14);
    java.util.List list16 = xmlElement15.getElements();
    boolean b17 = xmlElement11.equals((java.lang.Object)xmlElement15);
    java.lang.String str18 = xmlElement11.getData();
    xmlElement7.setParent(xmlElement11);
    boolean b21 = xmlElement11.equals((java.lang.Object)0.0d);
    XmlElement xmlElement22 = xmlElement2.removeElement(xmlElement11);
    XmlElement xmlElement23 = xmlElement11.getParent();
    java.util.Observer observer24 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement11.addObserver(observer24);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement23);

  }

  @Test
  public void test042() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test042"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("hi!", hashtable_str_str9);
    xmlElement10.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str13 = null;
    XmlElement xmlElement14 = new XmlElement("", hashtable_str_str13);
    java.util.List list15 = xmlElement14.getElements();
    boolean b16 = xmlElement10.equals((java.lang.Object)xmlElement14);
    java.lang.String str17 = xmlElement10.getData();
    xmlElement6.setParent(xmlElement10);
    XmlElement xmlElement19 = xmlElement2.addSubElement(xmlElement10);
    java.util.Observer observer20 = null;
    xmlElement10.deleteObserver(observer20);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str23 = null;
    XmlElement xmlElement24 = new XmlElement("hi!", hashtable_str_str23);
    xmlElement24.deleteObservers();
    boolean b26 = xmlElement10.addElement(xmlElement24);
    xmlElement10.removeAllElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b16 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b26 == true);

  }

  @Test
  public void test043() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test043"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    xmlElement2.setName("");
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str8 = xmlElement2.getAttribute("hi!");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);

  }

  @Test
  public void test044() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test044"); }


    XmlElement xmlElement2 = new XmlElement("", "hi!");

  }

  @Test
  public void test045() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test045"); }


    XmlElement xmlElement0 = new XmlElement();
    int i1 = xmlElement0.count();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = null;
    XmlElement xmlElement4 = new XmlElement("hi!", hashtable_str_str3);
    xmlElement4.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("", hashtable_str_str7);
    java.util.List list9 = xmlElement8.getElements();
    boolean b10 = xmlElement4.equals((java.lang.Object)xmlElement8);
    xmlElement8.setName("hi!");
    xmlElement0.append(xmlElement8);
    java.util.Observer observer14 = null;
    xmlElement0.deleteObserver(observer14);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement17 = xmlElement0.removeElement(100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i1 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);

  }

  @Test
  public void test046() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test046"); }


    XmlElement xmlElement0 = new XmlElement();
    java.util.List list1 = xmlElement0.getElements();
    boolean b2 = xmlElement0.hasChanged();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list1);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);

  }

  @Test
  public void test047() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test047"); }


    XmlElement xmlElement0 = new XmlElement();
    int i1 = xmlElement0.count();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = null;
    XmlElement xmlElement4 = new XmlElement("hi!", hashtable_str_str3);
    xmlElement4.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("", hashtable_str_str7);
    java.util.List list9 = xmlElement8.getElements();
    boolean b10 = xmlElement4.equals((java.lang.Object)xmlElement8);
    xmlElement8.setName("hi!");
    xmlElement0.append(xmlElement8);
    java.util.Observer observer14 = null;
    xmlElement0.deleteObserver(observer14);
    XmlElement xmlElement16 = xmlElement0.getParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i1 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement16);

  }

  @Test
  public void test048() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test048"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    xmlElement2.setName("");
    int i7 = xmlElement2.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("", hashtable_str_str9);
    XmlElement xmlElement11 = xmlElement2.removeElement(xmlElement10);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("hi!", hashtable_str_str14);
    XmlElement xmlElement17 = xmlElement15.addSubElement("");
    xmlElement2.append(xmlElement17);
    java.lang.String str19 = xmlElement2.getName();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement21 = xmlElement2.getElement((-1));
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str19 + "' != '" + ""+ "'", str19.equals(""));

  }

  @Test
  public void test049() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test049"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    xmlElement2.setName("");
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement8 = xmlElement2.getElement((int)(byte)10);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);

  }

  @Test
  public void test050() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test050"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    int i3 = xmlElement2.count();
    xmlElement2.setName("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i3 == 0);

  }

  @Test
  public void test051() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test051"); }


    XmlElement xmlElement1 = new XmlElement("hi!");

  }

  @Test
  public void test052() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test052"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    int i3 = xmlElement2.count();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = xmlElement2.getAttributes();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i3 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(hashtable_str_str4);

  }

  @Test
  public void test053() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test053"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement4 = xmlElement2.addSubElement("");
    int i5 = xmlElement4.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str6 = xmlElement4.getAttributes();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    XmlElement xmlElement10 = null;
    XmlElement xmlElement11 = xmlElement9.removeElement(xmlElement10);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str13 = null;
    XmlElement xmlElement14 = new XmlElement("", hashtable_str_str13);
    java.util.List list15 = xmlElement14.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str17 = null;
    XmlElement xmlElement18 = new XmlElement("hi!", hashtable_str_str17);
    xmlElement18.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str21 = null;
    XmlElement xmlElement22 = new XmlElement("", hashtable_str_str21);
    java.util.List list23 = xmlElement22.getElements();
    boolean b24 = xmlElement18.equals((java.lang.Object)xmlElement22);
    java.lang.String str25 = xmlElement18.getData();
    xmlElement14.setParent(xmlElement18);
    boolean b28 = xmlElement18.equals((java.lang.Object)0.0d);
    XmlElement xmlElement29 = xmlElement9.removeElement(xmlElement18);
    xmlElement4.append(xmlElement9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b24 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b28 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement29);

  }

  @Test
  public void test054() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test054"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    XmlElement xmlElement5 = xmlElement2.addSubElement("hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("hi!", hashtable_str_str7);
    xmlElement8.deleteObservers();
    XmlElement xmlElement11 = xmlElement8.addSubElement("hi!");
    xmlElement11.deleteObservers();
    xmlElement2.append(xmlElement11);
    xmlElement11.setName("hi!");
    java.util.Observer observer16 = null;
    xmlElement11.deleteObserver(observer16);
    XmlElement xmlElement19 = xmlElement11.getElement("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement19);

  }

  @Test
  public void test055() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test055"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    xmlElement6.setName("hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    xmlElement6.setAttributes(hashtable_str_str11);
    // The following exception was thrown during execution in test generation
    try {
    java.util.Enumeration enumeration13 = xmlElement6.getAttributeNames();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);

  }

  @Test
  public void test056() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test056"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    xmlElement2.setName("");
    int i7 = xmlElement2.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("", hashtable_str_str9);
    XmlElement xmlElement11 = xmlElement2.removeElement(xmlElement10);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("hi!", hashtable_str_str14);
    XmlElement xmlElement17 = xmlElement15.addSubElement("");
    xmlElement2.append(xmlElement17);
    java.lang.String str19 = xmlElement2.getName();
    int i20 = xmlElement2.countObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str19 + "' != '" + ""+ "'", str19.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i20 == 0);

  }

  @Test
  public void test057() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test057"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    XmlElement xmlElement5 = xmlElement2.addSubElement("hi!");
    xmlElement5.deleteObservers();
    XmlElement xmlElement8 = xmlElement5.getElement("");
    xmlElement5.removeAllElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);

  }

  @Test
  public void test058() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test058"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    xmlElement2.setName("");
    int i7 = xmlElement2.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("", hashtable_str_str9);
    XmlElement xmlElement11 = xmlElement2.removeElement(xmlElement10);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("hi!", hashtable_str_str14);
    XmlElement xmlElement17 = xmlElement15.addSubElement("");
    xmlElement2.append(xmlElement17);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str20 = null;
    XmlElement xmlElement21 = new XmlElement("hi!", hashtable_str_str20);
    xmlElement21.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str24 = null;
    XmlElement xmlElement25 = new XmlElement("", hashtable_str_str24);
    java.util.List list26 = xmlElement25.getElements();
    boolean b27 = xmlElement21.equals((java.lang.Object)xmlElement25);
    java.util.Observer observer28 = null;
    xmlElement21.deleteObserver(observer28);
    boolean b30 = xmlElement17.addElement(xmlElement21);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str32 = null;
    XmlElement xmlElement33 = new XmlElement("hi!", hashtable_str_str32);
    xmlElement33.notifyObservers();
    XmlElement xmlElement35 = xmlElement17.removeElement(xmlElement33);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b27 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b30 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement35);

  }

  @Test
  public void test059() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test059"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement4 = xmlElement2.addSubElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str6 = null;
    XmlElement xmlElement7 = new XmlElement("hi!", hashtable_str_str6);
    xmlElement7.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("", hashtable_str_str10);
    java.util.List list12 = xmlElement11.getElements();
    boolean b13 = xmlElement7.equals((java.lang.Object)xmlElement11);
    java.lang.String str14 = xmlElement7.getData();
    java.util.Observer observer15 = null;
    xmlElement7.deleteObserver(observer15);
    XmlElement xmlElement17 = xmlElement2.removeElement(xmlElement7);
    int i18 = xmlElement2.count();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i18 == 1);

  }

  @Test
  public void test060() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test060"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.notifyObservers();
    xmlElement2.removeAllElements();
    java.util.Observer observer5 = null;
    xmlElement2.deleteObserver(observer5);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement8 = xmlElement2.removeElement(1);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test061() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test061"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    java.lang.String str9 = xmlElement2.getData();
    XmlElement xmlElement10 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement2.insertElement(xmlElement10, (int)'a');
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str9);

  }

  @Test
  public void test062() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test062"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    XmlElement xmlElement5 = xmlElement2.addSubElement("hi!");
    int i6 = xmlElement2.count();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 1);

  }

  @Test
  public void test063() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test063"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement4 = xmlElement2.addSubElement("");
    int i5 = xmlElement4.countObservers();
    int i6 = xmlElement4.count();
    java.util.Observer observer7 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement4.addObserver(observer7);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);

  }

  @Test
  public void test064() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test064"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.List list3 = xmlElement2.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("hi!", hashtable_str_str5);
    xmlElement6.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("", hashtable_str_str9);
    java.util.List list11 = xmlElement10.getElements();
    boolean b12 = xmlElement6.equals((java.lang.Object)xmlElement10);
    java.lang.String str13 = xmlElement6.getData();
    xmlElement2.setParent(xmlElement6);
    boolean b16 = xmlElement6.equals((java.lang.Object)0.0d);
    xmlElement6.setName("");
    boolean b19 = xmlElement6.hasChanged();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b16 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b19 == false);

  }

  @Test
  public void test065() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test065"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str6 = null;
    XmlElement xmlElement7 = new XmlElement("", hashtable_str_str6);
    java.util.List list8 = xmlElement7.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("", hashtable_str_str14);
    java.util.List list16 = xmlElement15.getElements();
    boolean b17 = xmlElement11.equals((java.lang.Object)xmlElement15);
    java.lang.String str18 = xmlElement11.getData();
    xmlElement7.setParent(xmlElement11);
    boolean b21 = xmlElement11.equals((java.lang.Object)0.0d);
    XmlElement xmlElement22 = xmlElement2.removeElement(xmlElement11);
    XmlElement xmlElement25 = new XmlElement("", "");
    xmlElement25.setData("");
    XmlElement xmlElement28 = xmlElement2.addSubElement(xmlElement25);
    java.lang.String str29 = xmlElement28.getName();
    java.lang.Object obj32 = xmlElement28.addAttribute("hi!", "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str29 + "' != '" + ""+ "'", str29.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj32);

  }

  @Test
  public void test066() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test066"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.List list3 = xmlElement2.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("hi!", hashtable_str_str5);
    xmlElement6.notifyObservers();
    XmlElement xmlElement8 = xmlElement2.removeElement(xmlElement6);
    java.util.Observer observer9 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement2.addObserver(observer9);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);

  }

  @Test
  public void test067() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test067"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("hi!", hashtable_str_str9);
    xmlElement10.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str13 = null;
    XmlElement xmlElement14 = new XmlElement("", hashtable_str_str13);
    java.util.List list15 = xmlElement14.getElements();
    boolean b16 = xmlElement10.equals((java.lang.Object)xmlElement14);
    java.lang.String str17 = xmlElement10.getData();
    xmlElement6.setParent(xmlElement10);
    XmlElement xmlElement19 = xmlElement2.addSubElement(xmlElement10);
    java.util.Observer observer20 = null;
    xmlElement10.deleteObserver(observer20);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str23 = null;
    XmlElement xmlElement24 = new XmlElement("hi!", hashtable_str_str23);
    xmlElement24.deleteObservers();
    boolean b26 = xmlElement10.addElement(xmlElement24);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement28 = xmlElement24.getElement((int)(byte)1);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b16 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b26 == true);

  }

  @Test
  public void test068() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test068"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str6 = null;
    XmlElement xmlElement7 = new XmlElement("", hashtable_str_str6);
    java.util.List list8 = xmlElement7.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("", hashtable_str_str14);
    java.util.List list16 = xmlElement15.getElements();
    boolean b17 = xmlElement11.equals((java.lang.Object)xmlElement15);
    java.lang.String str18 = xmlElement11.getData();
    xmlElement7.setParent(xmlElement11);
    boolean b21 = xmlElement11.equals((java.lang.Object)0.0d);
    XmlElement xmlElement22 = xmlElement2.removeElement(xmlElement11);
    XmlElement xmlElement25 = new XmlElement("", "");
    xmlElement25.setData("");
    XmlElement xmlElement28 = xmlElement2.addSubElement(xmlElement25);
    XmlElement xmlElement30 = xmlElement25.addSubElement("");
    XmlElement xmlElement32 = xmlElement25.getElement("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement32);

  }

  @Test
  public void test069() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test069"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    xmlElement6.setName("hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = null;
    XmlElement xmlElement13 = new XmlElement("hi!", hashtable_str_str12);
    XmlElement xmlElement14 = null;
    XmlElement xmlElement15 = xmlElement13.removeElement(xmlElement14);
    xmlElement13.setName("");
    int i18 = xmlElement13.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str20 = null;
    XmlElement xmlElement21 = new XmlElement("", hashtable_str_str20);
    XmlElement xmlElement22 = xmlElement13.removeElement(xmlElement21);
    xmlElement13.deleteObservers();
    int i24 = xmlElement13.count();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str26 = null;
    XmlElement xmlElement27 = new XmlElement("hi!", hashtable_str_str26);
    XmlElement xmlElement28 = xmlElement27.getParent();
    xmlElement27.deleteObservers();
    int i30 = xmlElement27.count();
    xmlElement13.append(xmlElement27);
    boolean b32 = xmlElement6.addElement(xmlElement27);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i18 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i24 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i30 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b32 == true);

  }

  @Test
  public void test070() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test070"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    XmlElement xmlElement6 = xmlElement2.getElement("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);

  }

  @Test
  public void test071() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test071"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    xmlElement2.setData("hi!");
    XmlElement xmlElement12 = xmlElement2.addSubElement("hi!");
    // The following exception was thrown during execution in test generation
    try {
    java.util.Enumeration enumeration13 = xmlElement2.getAttributeNames();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement12);

  }

  @Test
  public void test072() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test072"); }


    XmlElement xmlElement2 = new XmlElement("", "");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    XmlElement xmlElement6 = null;
    XmlElement xmlElement7 = xmlElement5.removeElement(xmlElement6);
    xmlElement5.setName("");
    int i10 = xmlElement5.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = null;
    XmlElement xmlElement13 = new XmlElement("", hashtable_str_str12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement13);
    xmlElement5.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str17 = null;
    XmlElement xmlElement18 = new XmlElement("hi!", hashtable_str_str17);
    XmlElement xmlElement20 = xmlElement18.addSubElement("");
    xmlElement5.append(xmlElement20);
    XmlElement xmlElement22 = xmlElement2.addSubElement(xmlElement20);
    XmlElement xmlElement24 = xmlElement20.addSubElement("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement24);

  }

  @Test
  public void test073() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test073"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    xmlElement2.setName("");
    int i7 = xmlElement2.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("", hashtable_str_str9);
    XmlElement xmlElement11 = xmlElement2.removeElement(xmlElement10);
    xmlElement2.deleteObservers();
    int i13 = xmlElement2.count();
    xmlElement2.notifyObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);

  }

  @Test
  public void test074() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test074"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    xmlElement2.setData("hi!");
    XmlElement xmlElement11 = xmlElement2.getParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);

  }

  @Test
  public void test075() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test075"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    XmlElement xmlElement5 = xmlElement2.addSubElement("hi!");
    xmlElement2.removeAllElements();
    java.lang.Object obj7 = xmlElement2.clone();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = xmlElement2.getAttributes();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(hashtable_str_str8);

  }

  @Test
  public void test076() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test076"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement4 = xmlElement2.addSubElement("");
    int i5 = xmlElement4.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    XmlElement xmlElement11 = xmlElement9.addSubElement("");
    int i12 = xmlElement11.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str13 = xmlElement11.getAttributes();
    XmlElement xmlElement14 = new XmlElement("hi!", hashtable_str_str13);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str16 = null;
    XmlElement xmlElement17 = new XmlElement("hi!", hashtable_str_str16);
    XmlElement xmlElement19 = xmlElement17.addSubElement("");
    int i20 = xmlElement19.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str21 = xmlElement19.getAttributes();
    xmlElement14.setAttributes(hashtable_str_str21);
    xmlElement4.setAttributes(hashtable_str_str21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i12 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i20 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str21);

  }

  @Test
  public void test077() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test077"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    xmlElement6.setName("hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = null;
    XmlElement xmlElement13 = new XmlElement("hi!", hashtable_str_str12);
    XmlElement xmlElement15 = xmlElement13.addSubElement("");
    java.util.Enumeration enumeration16 = xmlElement15.getAttributeNames();
    XmlElement xmlElement17 = xmlElement6.addSubElement(xmlElement15);
    xmlElement6.notifyObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str20 = null;
    XmlElement xmlElement21 = new XmlElement("hi!", hashtable_str_str20);
    XmlElement xmlElement23 = xmlElement21.addSubElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str25 = null;
    XmlElement xmlElement26 = new XmlElement("hi!", hashtable_str_str25);
    xmlElement26.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str29 = null;
    XmlElement xmlElement30 = new XmlElement("", hashtable_str_str29);
    java.util.List list31 = xmlElement30.getElements();
    boolean b32 = xmlElement26.equals((java.lang.Object)xmlElement30);
    java.lang.String str33 = xmlElement26.getData();
    java.util.Observer observer34 = null;
    xmlElement26.deleteObserver(observer34);
    XmlElement xmlElement36 = xmlElement21.removeElement(xmlElement26);
    boolean b37 = xmlElement6.equals((java.lang.Object)xmlElement21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list31);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b32 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str33);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement36);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b37 == true);

  }

  @Test
  public void test078() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test078"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    java.lang.String str9 = xmlElement2.getData();
    XmlElement xmlElement12 = xmlElement2.addSubElement("hi!", "hi!");
    XmlElement xmlElement13 = xmlElement2.getParent();
    java.lang.String str14 = xmlElement2.getName();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str14 + "' != '" + "hi!"+ "'", str14.equals("hi!"));

  }

  @Test
  public void test079() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test079"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    XmlElement xmlElement5 = xmlElement2.addSubElement("hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("hi!", hashtable_str_str7);
    xmlElement8.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("", hashtable_str_str11);
    java.util.List list13 = xmlElement12.getElements();
    boolean b14 = xmlElement8.equals((java.lang.Object)xmlElement12);
    xmlElement12.setName("hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str18 = null;
    XmlElement xmlElement19 = new XmlElement("hi!", hashtable_str_str18);
    XmlElement xmlElement21 = xmlElement19.addSubElement("");
    java.util.Enumeration enumeration22 = xmlElement21.getAttributeNames();
    XmlElement xmlElement23 = xmlElement12.addSubElement(xmlElement21);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str25 = null;
    XmlElement xmlElement26 = new XmlElement("hi!", hashtable_str_str25);
    xmlElement26.deleteObservers();
    XmlElement xmlElement29 = xmlElement26.addSubElement("hi!");
    xmlElement29.deleteObservers();
    int i31 = xmlElement29.count();
    xmlElement21.notifyObservers((java.lang.Object)xmlElement29);
    xmlElement5.append(xmlElement29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i31 == 0);

  }

  @Test
  public void test080() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test080"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str6 = null;
    XmlElement xmlElement7 = new XmlElement("", hashtable_str_str6);
    java.util.List list8 = xmlElement7.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("", hashtable_str_str14);
    java.util.List list16 = xmlElement15.getElements();
    boolean b17 = xmlElement11.equals((java.lang.Object)xmlElement15);
    java.lang.String str18 = xmlElement11.getData();
    xmlElement7.setParent(xmlElement11);
    boolean b21 = xmlElement11.equals((java.lang.Object)0.0d);
    XmlElement xmlElement22 = xmlElement2.removeElement(xmlElement11);
    XmlElement xmlElement25 = new XmlElement("", "");
    xmlElement25.setData("");
    XmlElement xmlElement28 = xmlElement2.addSubElement(xmlElement25);
    java.lang.String str29 = xmlElement28.getName();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str31 = null;
    XmlElement xmlElement32 = new XmlElement("hi!", hashtable_str_str31);
    xmlElement32.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str35 = null;
    XmlElement xmlElement36 = new XmlElement("", hashtable_str_str35);
    java.util.List list37 = xmlElement36.getElements();
    boolean b38 = xmlElement32.equals((java.lang.Object)xmlElement36);
    xmlElement36.notifyObservers((java.lang.Object)(-1));
    XmlElement xmlElement41 = xmlElement28.removeElement(xmlElement36);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str29 + "' != '" + ""+ "'", str29.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b38 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement41);

  }

  @Test
  public void test081() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test081"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("hi!", hashtable_str_str9);
    xmlElement10.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str13 = null;
    XmlElement xmlElement14 = new XmlElement("", hashtable_str_str13);
    java.util.List list15 = xmlElement14.getElements();
    boolean b16 = xmlElement10.equals((java.lang.Object)xmlElement14);
    java.lang.String str17 = xmlElement10.getData();
    xmlElement6.setParent(xmlElement10);
    XmlElement xmlElement19 = xmlElement2.addSubElement(xmlElement10);
    java.util.Observer observer20 = null;
    xmlElement10.deleteObserver(observer20);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str23 = null;
    XmlElement xmlElement24 = new XmlElement("hi!", hashtable_str_str23);
    xmlElement24.deleteObservers();
    boolean b26 = xmlElement10.addElement(xmlElement24);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement28 = xmlElement10.getElement((int)(short)1);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b16 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b26 == true);

  }

  @Test
  public void test082() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test082"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    xmlElement2.setName("");
    int i7 = xmlElement2.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("", hashtable_str_str9);
    XmlElement xmlElement11 = xmlElement2.removeElement(xmlElement10);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("hi!", hashtable_str_str14);
    XmlElement xmlElement17 = xmlElement15.addSubElement("");
    xmlElement2.append(xmlElement17);
    java.lang.String str19 = xmlElement2.getName();
    xmlElement2.setData("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str19 + "' != '" + ""+ "'", str19.equals(""));

  }

  @Test
  public void test083() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test083"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str4 = xmlElement2.getAttribute("hi!");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test084() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test084"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    boolean b3 = xmlElement2.hasChanged();
    XmlElement xmlElement4 = xmlElement2.getParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);

  }

  @Test
  public void test085() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test085"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    XmlElement xmlElement5 = xmlElement2.addSubElement("hi!");
    xmlElement5.deleteObservers();
    int i7 = xmlElement5.countObservers();
    XmlElement xmlElement8 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement5.append(xmlElement8);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);

  }

  @Test
  public void test086() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test086"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    xmlElement6.setName("hi!");
    int i11 = xmlElement6.countObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i11 == 0);

  }

  @Test
  public void test087() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test087"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = null;
    XmlElement xmlElement4 = new XmlElement("hi!", hashtable_str_str3);
    XmlElement xmlElement6 = xmlElement4.addSubElement("");
    int i7 = xmlElement6.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = xmlElement6.getAttributes();
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    XmlElement xmlElement10 = new XmlElement("", hashtable_str_str8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str8);

  }

  @Test
  public void test088() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test088"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str6 = null;
    XmlElement xmlElement7 = new XmlElement("", hashtable_str_str6);
    java.util.List list8 = xmlElement7.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("", hashtable_str_str14);
    java.util.List list16 = xmlElement15.getElements();
    boolean b17 = xmlElement11.equals((java.lang.Object)xmlElement15);
    java.lang.String str18 = xmlElement11.getData();
    xmlElement7.setParent(xmlElement11);
    boolean b21 = xmlElement11.equals((java.lang.Object)0.0d);
    XmlElement xmlElement22 = xmlElement2.removeElement(xmlElement11);
    XmlElement xmlElement23 = xmlElement11.getParent();
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str24 = xmlElement23.getName();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement23);

  }

  @Test
  public void test089() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test089"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement4 = xmlElement2.addSubElement("");
    java.util.List list5 = xmlElement2.getElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list5);

  }

  @Test
  public void test090() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test090"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement4 = xmlElement2.addSubElement("");
    int i5 = xmlElement4.countObservers();
    int i6 = xmlElement4.count();
    java.lang.Object obj9 = xmlElement4.addAttribute("", "");
    XmlElement xmlElement12 = xmlElement4.addSubElement("hi!", "hi!");
    XmlElement xmlElement14 = xmlElement4.getElement("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);

  }

  @Test
  public void test091() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test091"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    xmlElement2.setName("");
    boolean b7 = xmlElement2.hasChanged();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test092() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test092"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = xmlElement2.getParent();
    XmlElement xmlElement5 = xmlElement2.getElement("");
    xmlElement2.setName("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement5);

  }

  @Test
  public void test093() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test093"); }


    XmlElement xmlElement1 = new XmlElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = null;
    XmlElement xmlElement4 = new XmlElement("hi!", hashtable_str_str3);
    XmlElement xmlElement6 = xmlElement4.addSubElement("");
    int i7 = xmlElement6.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = xmlElement6.getAttributes();
    xmlElement1.setAttributes(hashtable_str_str8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str8);

  }

  @Test
  public void test094() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test094"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    xmlElement2.setName("");
    int i7 = xmlElement2.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("", hashtable_str_str9);
    XmlElement xmlElement11 = xmlElement2.removeElement(xmlElement10);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("hi!", hashtable_str_str14);
    XmlElement xmlElement17 = xmlElement15.addSubElement("");
    xmlElement2.append(xmlElement17);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str20 = null;
    XmlElement xmlElement21 = new XmlElement("hi!", hashtable_str_str20);
    xmlElement21.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str24 = null;
    XmlElement xmlElement25 = new XmlElement("", hashtable_str_str24);
    java.util.List list26 = xmlElement25.getElements();
    boolean b27 = xmlElement21.equals((java.lang.Object)xmlElement25);
    java.util.Observer observer28 = null;
    xmlElement21.deleteObserver(observer28);
    boolean b30 = xmlElement17.addElement(xmlElement21);
    java.lang.String str31 = xmlElement17.getName();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b27 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b30 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str31 + "' != '" + ""+ "'", str31.equals(""));

  }

  @Test
  public void test095() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test095"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str6 = null;
    XmlElement xmlElement7 = new XmlElement("", hashtable_str_str6);
    java.util.List list8 = xmlElement7.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("", hashtable_str_str14);
    java.util.List list16 = xmlElement15.getElements();
    boolean b17 = xmlElement11.equals((java.lang.Object)xmlElement15);
    java.lang.String str18 = xmlElement11.getData();
    xmlElement7.setParent(xmlElement11);
    boolean b21 = xmlElement11.equals((java.lang.Object)0.0d);
    XmlElement xmlElement22 = xmlElement2.removeElement(xmlElement11);
    XmlElement xmlElement25 = new XmlElement("", "");
    xmlElement25.setData("");
    XmlElement xmlElement28 = xmlElement2.addSubElement(xmlElement25);
    XmlElement xmlElement30 = xmlElement25.addSubElement("");
    java.lang.String str33 = xmlElement25.getAttribute("hi!", "");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str35 = null;
    XmlElement xmlElement36 = new XmlElement("hi!", hashtable_str_str35);
    xmlElement36.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str39 = null;
    XmlElement xmlElement40 = new XmlElement("", hashtable_str_str39);
    java.util.List list41 = xmlElement40.getElements();
    boolean b42 = xmlElement36.equals((java.lang.Object)xmlElement40);
    xmlElement40.setName("hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str46 = null;
    XmlElement xmlElement47 = new XmlElement("hi!", hashtable_str_str46);
    XmlElement xmlElement49 = xmlElement47.addSubElement("");
    java.util.Enumeration enumeration50 = xmlElement49.getAttributeNames();
    XmlElement xmlElement51 = xmlElement40.addSubElement(xmlElement49);
    boolean b52 = xmlElement25.equals((java.lang.Object)xmlElement40);
    XmlElement xmlElement53 = xmlElement25.getParent();
    java.lang.String str54 = xmlElement25.getData();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str33 + "' != '" + ""+ "'", str33.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list41);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b42 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement49);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration50);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement51);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b52 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement53);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str54 + "' != '" + ""+ "'", str54.equals(""));

  }

  @Test
  public void test096() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test096"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str6 = null;
    XmlElement xmlElement7 = new XmlElement("", hashtable_str_str6);
    java.util.List list8 = xmlElement7.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("", hashtable_str_str14);
    java.util.List list16 = xmlElement15.getElements();
    boolean b17 = xmlElement11.equals((java.lang.Object)xmlElement15);
    java.lang.String str18 = xmlElement11.getData();
    xmlElement7.setParent(xmlElement11);
    boolean b21 = xmlElement11.equals((java.lang.Object)0.0d);
    XmlElement xmlElement22 = xmlElement2.removeElement(xmlElement11);
    XmlElement xmlElement25 = new XmlElement("", "");
    xmlElement25.setData("");
    XmlElement xmlElement28 = xmlElement2.addSubElement(xmlElement25);
    XmlElement xmlElement30 = xmlElement25.addSubElement("");
    xmlElement25.removeFromParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement30);

  }

  @Test
  public void test097() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test097"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.notifyObservers();
    xmlElement2.removeAllElements();
    java.util.Observer observer5 = null;
    xmlElement2.deleteObserver(observer5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    xmlElement9.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = null;
    XmlElement xmlElement13 = new XmlElement("", hashtable_str_str12);
    java.util.List list14 = xmlElement13.getElements();
    boolean b15 = xmlElement9.equals((java.lang.Object)xmlElement13);
    java.util.Observer observer16 = null;
    xmlElement9.deleteObserver(observer16);
    xmlElement9.setName("hi!");
    xmlElement9.removeAllElements();
    boolean b21 = xmlElement2.addElement(xmlElement9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == true);

  }

  @Test
  public void test098() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test098"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    XmlElement xmlElement5 = xmlElement2.addSubElement("hi!");
    xmlElement5.deleteObservers();
    int i7 = xmlElement5.countObservers();
    java.util.Observer observer8 = null;
    xmlElement5.deleteObserver(observer8);
    java.lang.String str10 = xmlElement5.getData();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = null;
    XmlElement xmlElement13 = new XmlElement("hi!", hashtable_str_str12);
    XmlElement xmlElement15 = xmlElement13.addSubElement("");
    int i16 = xmlElement15.countObservers();
    int i17 = xmlElement15.count();
    xmlElement5.setParent(xmlElement15);
    XmlElement xmlElement19 = null;
    xmlElement5.setParent(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str10 + "' != '" + ""+ "'", str10.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i16 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i17 == 0);

  }

  @Test
  public void test099() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test099"); }


    XmlElement xmlElement2 = new XmlElement("hi!", "");

  }

  @Test
  public void test100() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test100"); }


    XmlElement xmlElement0 = new XmlElement();
    java.util.List list1 = xmlElement0.getElements();
    int i2 = xmlElement0.countObservers();
    xmlElement0.setName("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list1);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);

  }

  @Test
  public void test101() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test101"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    java.lang.String str9 = xmlElement2.getData();
    java.util.Observer observer10 = null;
    xmlElement2.deleteObserver(observer10);
    java.lang.Object obj12 = xmlElement2.clone();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj12);

  }

  @Test
  public void test102() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test102"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    java.lang.String str9 = xmlElement2.getData();
    java.util.Observer observer10 = null;
    xmlElement2.deleteObserver(observer10);
    xmlElement2.removeAllElements();
    XmlElement xmlElement14 = xmlElement2.getElement("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);

  }

  @Test
  public void test103() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test103"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    XmlElement xmlElement5 = xmlElement2.addSubElement("hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("hi!", hashtable_str_str7);
    xmlElement8.deleteObservers();
    XmlElement xmlElement11 = xmlElement8.addSubElement("hi!");
    xmlElement11.deleteObservers();
    xmlElement2.append(xmlElement11);
    xmlElement11.setName("hi!");
    java.util.Observer observer16 = null;
    xmlElement11.deleteObserver(observer16);
    int i18 = xmlElement11.count();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i18 == 0);

  }

  @Test
  public void test104() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test104"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    XmlElement xmlElement5 = xmlElement2.addSubElement("hi!");
    xmlElement5.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    xmlElement9.deleteObservers();
    XmlElement xmlElement12 = xmlElement9.addSubElement("hi!");
    xmlElement9.removeAllElements();
    xmlElement5.setParent(xmlElement9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement12);

  }

  @Test
  public void test105() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test105"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    xmlElement6.setName("hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = null;
    XmlElement xmlElement13 = new XmlElement("hi!", hashtable_str_str12);
    XmlElement xmlElement15 = xmlElement13.addSubElement("");
    java.util.Enumeration enumeration16 = xmlElement15.getAttributeNames();
    XmlElement xmlElement17 = xmlElement6.addSubElement(xmlElement15);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str19 = null;
    XmlElement xmlElement20 = new XmlElement("hi!", hashtable_str_str19);
    xmlElement20.deleteObservers();
    XmlElement xmlElement23 = xmlElement20.addSubElement("hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str25 = null;
    XmlElement xmlElement26 = new XmlElement("hi!", hashtable_str_str25);
    xmlElement26.deleteObservers();
    XmlElement xmlElement29 = xmlElement26.addSubElement("hi!");
    xmlElement29.deleteObservers();
    xmlElement20.append(xmlElement29);
    boolean b32 = xmlElement29.hasChanged();
    boolean b33 = xmlElement6.addElement(xmlElement29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b32 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b33 == true);

  }

  @Test
  public void test106() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test106"); }


    XmlElement xmlElement0 = new XmlElement();
    java.util.Observer observer1 = null;
    xmlElement0.deleteObserver(observer1);

  }

  @Test
  public void test107() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test107"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str6 = null;
    XmlElement xmlElement7 = new XmlElement("", hashtable_str_str6);
    java.util.List list8 = xmlElement7.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("", hashtable_str_str14);
    java.util.List list16 = xmlElement15.getElements();
    boolean b17 = xmlElement11.equals((java.lang.Object)xmlElement15);
    java.lang.String str18 = xmlElement11.getData();
    xmlElement7.setParent(xmlElement11);
    boolean b21 = xmlElement11.equals((java.lang.Object)0.0d);
    XmlElement xmlElement22 = xmlElement2.removeElement(xmlElement11);
    XmlElement xmlElement24 = xmlElement11.addSubElement("hi!");
    boolean b25 = xmlElement11.hasChanged();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b25 == false);

  }

  @Test
  public void test108() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test108"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement4 = xmlElement2.addSubElement("");
    int i5 = xmlElement2.count();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("hi!", hashtable_str_str7);
    XmlElement xmlElement9 = xmlElement2.removeElement(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 1);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement9);

  }

  @Test
  public void test109() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test109"); }


    XmlElement xmlElement0 = new XmlElement();
    int i1 = xmlElement0.count();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = null;
    XmlElement xmlElement4 = new XmlElement("hi!", hashtable_str_str3);
    xmlElement4.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("", hashtable_str_str7);
    java.util.List list9 = xmlElement8.getElements();
    boolean b10 = xmlElement4.equals((java.lang.Object)xmlElement8);
    xmlElement8.setName("hi!");
    xmlElement0.append(xmlElement8);
    XmlElement xmlElement14 = xmlElement8.getParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i1 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement14);

  }

  @Test
  public void test110() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test110"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    boolean b3 = xmlElement2.hasChanged();
    boolean b4 = xmlElement2.hasChanged();
    java.lang.String str5 = xmlElement2.getName();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str5 + "' != '" + ""+ "'", str5.equals(""));

  }

  @Test
  public void test111() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test111"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.List list4 = xmlElement2.getElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list4);

  }

  @Test
  public void test112() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test112"); }


    XmlElement xmlElement2 = new XmlElement("", "");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    XmlElement xmlElement6 = null;
    XmlElement xmlElement7 = xmlElement5.removeElement(xmlElement6);
    xmlElement5.setName("");
    int i10 = xmlElement5.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = null;
    XmlElement xmlElement13 = new XmlElement("", hashtable_str_str12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement13);
    xmlElement5.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str17 = null;
    XmlElement xmlElement18 = new XmlElement("hi!", hashtable_str_str17);
    XmlElement xmlElement20 = xmlElement18.addSubElement("");
    xmlElement5.append(xmlElement20);
    XmlElement xmlElement22 = xmlElement2.addSubElement(xmlElement20);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str23 = xmlElement20.getAttributes();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str23);

  }

  @Test
  public void test113() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test113"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = xmlElement2.getParent();
    xmlElement2.deleteObservers();
    java.lang.String str5 = xmlElement2.getData();
    XmlElement xmlElement7 = xmlElement2.getElement("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement7);

  }

  @Test
  public void test114() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test114"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.List list7 = xmlElement6.getElements();
    boolean b8 = xmlElement2.equals((java.lang.Object)xmlElement6);
    xmlElement6.setName("hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = null;
    XmlElement xmlElement13 = new XmlElement("hi!", hashtable_str_str12);
    XmlElement xmlElement15 = xmlElement13.addSubElement("");
    java.util.Enumeration enumeration16 = xmlElement15.getAttributeNames();
    XmlElement xmlElement17 = xmlElement6.addSubElement(xmlElement15);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str19 = null;
    XmlElement xmlElement20 = new XmlElement("hi!", hashtable_str_str19);
    xmlElement20.deleteObservers();
    XmlElement xmlElement23 = xmlElement20.addSubElement("hi!");
    xmlElement23.deleteObservers();
    int i25 = xmlElement23.count();
    xmlElement15.notifyObservers((java.lang.Object)xmlElement23);
    xmlElement15.removeFromParent();
    XmlElement xmlElement30 = xmlElement15.addSubElement("", "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i25 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement30);

  }

  @Test
  public void test115() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test115"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = null;
    XmlElement xmlElement4 = xmlElement2.removeElement(xmlElement3);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str6 = null;
    XmlElement xmlElement7 = new XmlElement("", hashtable_str_str6);
    java.util.List list8 = xmlElement7.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("", hashtable_str_str14);
    java.util.List list16 = xmlElement15.getElements();
    boolean b17 = xmlElement11.equals((java.lang.Object)xmlElement15);
    java.lang.String str18 = xmlElement11.getData();
    xmlElement7.setParent(xmlElement11);
    boolean b21 = xmlElement11.equals((java.lang.Object)0.0d);
    XmlElement xmlElement22 = xmlElement2.removeElement(xmlElement11);
    XmlElement xmlElement25 = new XmlElement("", "");
    xmlElement25.setData("");
    XmlElement xmlElement28 = xmlElement2.addSubElement(xmlElement25);
    XmlElement xmlElement30 = xmlElement25.addSubElement("");
    java.lang.String str33 = xmlElement25.getAttribute("hi!", "");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str35 = null;
    XmlElement xmlElement36 = new XmlElement("hi!", hashtable_str_str35);
    xmlElement36.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str39 = null;
    XmlElement xmlElement40 = new XmlElement("", hashtable_str_str39);
    java.util.List list41 = xmlElement40.getElements();
    boolean b42 = xmlElement36.equals((java.lang.Object)xmlElement40);
    xmlElement40.setName("hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str46 = null;
    XmlElement xmlElement47 = new XmlElement("hi!", hashtable_str_str46);
    XmlElement xmlElement49 = xmlElement47.addSubElement("");
    java.util.Enumeration enumeration50 = xmlElement49.getAttributeNames();
    XmlElement xmlElement51 = xmlElement40.addSubElement(xmlElement49);
    boolean b52 = xmlElement25.equals((java.lang.Object)xmlElement40);
    java.util.List list53 = xmlElement40.getElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str33 + "' != '" + ""+ "'", str33.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list41);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b42 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement49);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration50);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement51);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b52 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list53);

  }

}
