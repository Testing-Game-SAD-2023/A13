package mypackage;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0_it3_livello4_it4_livello5 {

  public static boolean debug = false;

  @Test
  public void test001() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test001"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement3 = new XmlElement("hi!");
    // The following exception was thrown during execution in test generation
    try {
    xmlElement1.insertElement(xmlElement3, 1);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test002() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test002"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement3 = xmlElement1.getElement(1);
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


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.lang.Object obj2 = xmlElement1.clone();
    java.util.Observer observer3 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement1.addObserver(observer3);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj2);

  }

  @Test
  public void test004() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test004"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    XmlElement xmlElement6 = xmlElement2.removeElement(xmlElement5);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement8 = xmlElement5.getElement((int)(byte)1);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);

  }

  @Test
  public void test005() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test005"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Observer observer2 = null;
    xmlElement1.deleteObserver(observer2);
    java.lang.String str6 = xmlElement1.getAttribute("", "");
    xmlElement1.removeAllElements();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement9 = xmlElement1.removeElement(100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + ""+ "'", str6.equals(""));

  }

  @Test
  public void test006() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test006"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement10 = xmlElement4.getElement((int)(byte)-1);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);

  }

  @Test
  public void test007() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test007"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.lang.String str4 = xmlElement1.getAttribute("hi!");
    int i5 = xmlElement1.countObservers();
    java.util.List list6 = xmlElement1.getElements();
    java.util.Observer observer7 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement1.addObserver(observer7);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list6);

  }

  @Test
  public void test008() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test008"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.util.Observer observer3 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement1.addObserver(observer3);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);

  }

  @Test
  public void test009() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test009"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    // The following exception was thrown during execution in test generation
    try {
    java.util.Enumeration enumeration9 = xmlElement8.getAttributeNames();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);

  }

  @Test
  public void test010() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test010"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    boolean b3 = xmlElement1.equals((java.lang.Object)"hi!");
    XmlElement xmlElement5 = new XmlElement("hi!");
    boolean b7 = xmlElement5.equals((java.lang.Object)"hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement5);
    // The following exception was thrown during execution in test generation
    try {
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = xmlElement8.getAttributes();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);

  }

  @Test
  public void test011() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test011"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    xmlElement4.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    xmlElement4.setAttributes(hashtable_str_str10);
    boolean b12 = xmlElement4.hasChanged();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == false);

  }

  @Test
  public void test012() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test012"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.util.Observer observer3 = null;
    xmlElement1.deleteObserver(observer3);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement6 = xmlElement1.getElement(10);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);

  }

  @Test
  public void test013() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test013"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.lang.String str4 = xmlElement1.getAttribute("hi!");
    int i5 = xmlElement1.count();
    boolean b7 = xmlElement1.equals((java.lang.Object)true);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement9 = xmlElement1.removeElement((int)(short)1);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test014() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test014"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    boolean b3 = xmlElement1.hasChanged();
    xmlElement1.removeFromParent();
    boolean b5 = xmlElement1.hasChanged();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);

  }

  @Test
  public void test015() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test015"); }


    XmlElement xmlElement0 = new XmlElement();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement2 = xmlElement0.getElement((int)(byte)-1);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test016() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test016"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.lang.String str4 = xmlElement1.getAttribute("hi!");
    int i5 = xmlElement1.count();
    XmlElement xmlElement7 = new XmlElement("hi!");
    java.util.Enumeration enumeration8 = xmlElement7.getAttributeNames();
    boolean b9 = xmlElement7.hasChanged();
    java.lang.Object obj10 = xmlElement7.clone();
    xmlElement7.deleteObservers();
    xmlElement1.setParent(xmlElement7);
    java.util.Observer observer13 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement1.addObserver(observer13);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj10);

  }

  @Test
  public void test017() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test017"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    boolean b3 = xmlElement1.equals((java.lang.Object)"hi!");
    XmlElement xmlElement5 = new XmlElement("hi!");
    boolean b7 = xmlElement5.equals((java.lang.Object)"hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.removeFromParent();
    XmlElement xmlElement15 = xmlElement11.addSubElement("hi!", "hi!");
    XmlElement xmlElement16 = xmlElement5.addSubElement(xmlElement15);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str18 = null;
    XmlElement xmlElement19 = new XmlElement("hi!", hashtable_str_str18);
    xmlElement19.removeFromParent();
    XmlElement xmlElement23 = xmlElement19.addSubElement("hi!", "hi!");
    // The following exception was thrown during execution in test generation
    try {
    xmlElement15.insertElement(xmlElement23, (int)' ');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement23);

  }

  @Test
  public void test018() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test018"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.lang.Object obj2 = xmlElement1.clone();
    boolean b3 = xmlElement1.hasChanged();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);

  }

  @Test
  public void test019() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test019"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.removeFromParent();
    java.util.Observer observer4 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement2.addObserver(observer4);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test020() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test020"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    // The following exception was thrown during execution in test generation
    try {
    xmlElement8.deleteObservers();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);

  }

  @Test
  public void test021() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test021"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.util.Observer observer3 = null;
    xmlElement1.deleteObserver(observer3);
    java.lang.Object obj7 = xmlElement1.addAttribute("hi!", "hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj7);

  }

  @Test
  public void test022() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test022"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    xmlElement4.deleteObservers();
    XmlElement xmlElement11 = new XmlElement("hi!");
    java.util.Enumeration enumeration12 = xmlElement11.getAttributeNames();
    XmlElement xmlElement14 = new XmlElement("hi!");
    java.util.Enumeration enumeration15 = xmlElement14.getAttributeNames();
    java.lang.String str17 = xmlElement14.getAttribute("hi!");
    XmlElement xmlElement18 = xmlElement11.removeElement(xmlElement14);
    xmlElement4.notifyObservers((java.lang.Object)xmlElement14);
    XmlElement xmlElement20 = xmlElement4.getParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement20);

  }

  @Test
  public void test023() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test023"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    XmlElement xmlElement6 = xmlElement2.removeElement(xmlElement5);
    XmlElement xmlElement8 = new XmlElement("hi!");
    java.util.Enumeration enumeration9 = xmlElement8.getAttributeNames();
    XmlElement xmlElement11 = new XmlElement("hi!");
    java.util.Enumeration enumeration12 = xmlElement11.getAttributeNames();
    java.lang.String str14 = xmlElement11.getAttribute("hi!");
    XmlElement xmlElement15 = xmlElement8.removeElement(xmlElement11);
    XmlElement.printNode(xmlElement11, "hi!");
    // The following exception was thrown during execution in test generation
    try {
    xmlElement6.notifyObservers((java.lang.Object)xmlElement11);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement15);

  }

  @Test
  public void test024() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test024"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    boolean b3 = xmlElement1.equals((java.lang.Object)"hi!");
    XmlElement xmlElement5 = new XmlElement("hi!");
    boolean b7 = xmlElement5.equals((java.lang.Object)"hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement5);
    java.lang.String str10 = xmlElement1.getAttribute("");
    boolean b11 = xmlElement1.hasChanged();
    xmlElement1.removeFromParent();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement14 = xmlElement1.getElement((int)(byte)10);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);

  }

  @Test
  public void test025() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test025"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    boolean b3 = xmlElement1.equals((java.lang.Object)"hi!");
    XmlElement xmlElement5 = new XmlElement("hi!");
    boolean b7 = xmlElement5.equals((java.lang.Object)"hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement5);
    // The following exception was thrown during execution in test generation
    try {
    xmlElement8.deleteObservers();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);

  }

  @Test
  public void test026() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test026"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.List list3 = xmlElement2.getElements();
    xmlElement2.removeAllElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);

  }

  @Test
  public void test027() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test027"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.removeFromParent();
    XmlElement xmlElement6 = xmlElement2.addSubElement("hi!", "hi!");
    xmlElement2.removeAllElements();
    java.util.Observer observer8 = null;
    xmlElement2.deleteObserver(observer8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);

  }

  @Test
  public void test028() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test028"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    boolean b3 = xmlElement1.hasChanged();
    java.lang.Object obj4 = xmlElement1.clone();
    xmlElement1.deleteObservers();
    java.util.List list6 = xmlElement1.getElements();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement8 = xmlElement1.removeElement(100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list6);

  }

  @Test
  public void test029() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test029"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    boolean b3 = xmlElement1.equals((java.lang.Object)"hi!");
    XmlElement xmlElement5 = new XmlElement("hi!");
    boolean b7 = xmlElement5.equals((java.lang.Object)"hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.removeFromParent();
    XmlElement xmlElement15 = xmlElement11.addSubElement("hi!", "hi!");
    XmlElement xmlElement16 = xmlElement5.addSubElement(xmlElement15);
    java.lang.Object obj17 = xmlElement15.clone();
    java.lang.Object obj18 = xmlElement15.clone();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj18);

  }

  @Test
  public void test030() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test030"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.lang.Object obj2 = xmlElement1.clone();
    java.lang.Object obj3 = null;
    boolean b4 = xmlElement1.equals(obj3);
    XmlElement xmlElement7 = new XmlElement("", "hi!");
    XmlElement xmlElement8 = xmlElement1.addSubElement(xmlElement7);
    java.lang.Object obj9 = xmlElement7.clone();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj9);

  }

  @Test
  public void test031() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test031"); }


    XmlElement xmlElement2 = new XmlElement("", "hi!");
    XmlElement xmlElement4 = new XmlElement("hi!");
    xmlElement2.append(xmlElement4);
    java.lang.String str7 = xmlElement2.getAttribute("");
    XmlElement xmlElement9 = new XmlElement("hi!");
    java.util.Enumeration enumeration10 = xmlElement9.getAttributeNames();
    java.lang.String str12 = xmlElement9.getAttribute("hi!");
    int i13 = xmlElement9.count();
    boolean b15 = xmlElement9.equals((java.lang.Object)true);
    XmlElement xmlElement16 = xmlElement2.addSubElement(xmlElement9);
    java.util.Observer observer17 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement2.addObserver(observer17);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement16);

  }

  @Test
  public void test032() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test032"); }


    XmlElement xmlElement3 = new XmlElement("hi!");
    boolean b5 = xmlElement3.equals((java.lang.Object)"hi!");
    XmlElement xmlElement7 = new XmlElement("hi!");
    boolean b9 = xmlElement7.equals((java.lang.Object)"hi!");
    XmlElement xmlElement10 = xmlElement3.removeElement(xmlElement7);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = xmlElement3.getAttributes();
    XmlElement xmlElement12 = new XmlElement("", hashtable_str_str11);
    XmlElement xmlElement13 = new XmlElement("hi!", hashtable_str_str11);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement15 = xmlElement13.getElement((int)(byte)1);
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
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str11);

  }

  @Test
  public void test033() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test033"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    XmlElement xmlElement6 = xmlElement2.removeElement(xmlElement5);
    xmlElement2.removeAllElements();
    XmlElement xmlElement9 = new XmlElement("hi!");
    java.util.Observer observer10 = null;
    xmlElement9.deleteObserver(observer10);
    java.lang.String str14 = xmlElement9.getAttribute("", "");
    XmlElement xmlElement16 = new XmlElement("hi!");
    boolean b18 = xmlElement16.equals((java.lang.Object)"hi!");
    xmlElement9.append(xmlElement16);
    xmlElement2.setParent(xmlElement9);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str22 = null;
    XmlElement xmlElement23 = new XmlElement("hi!", hashtable_str_str22);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str25 = null;
    XmlElement xmlElement26 = new XmlElement("hi!", hashtable_str_str25);
    XmlElement xmlElement27 = xmlElement23.removeElement(xmlElement26);
    java.util.Observer observer28 = null;
    xmlElement23.deleteObserver(observer28);
    XmlElement xmlElement30 = xmlElement2.removeElement(xmlElement23);
    // The following exception was thrown during execution in test generation
    try {
    xmlElement30.setName("");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str14 + "' != '" + ""+ "'", str14.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b18 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement27);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement30);

  }

  @Test
  public void test034() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test034"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    xmlElement4.deleteObservers();
    java.util.Observer observer10 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement4.addObserver(observer10);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);

  }

  @Test
  public void test035() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test035"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.lang.Object obj2 = xmlElement1.clone();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    boolean b6 = xmlElement4.hasChanged();
    java.lang.Object obj7 = xmlElement4.clone();
    xmlElement4.deleteObservers();
    java.util.List list9 = xmlElement4.getElements();
    boolean b10 = xmlElement1.addElement(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);

  }

  @Test
  public void test036() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test036"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.List list3 = xmlElement2.getElements();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement5 = xmlElement2.removeElement((int)(byte)100);
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
  public void test037() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test037"); }


    XmlElement xmlElement2 = new XmlElement("", "hi!");
    XmlElement xmlElement4 = new XmlElement("hi!");
    xmlElement2.append(xmlElement4);
    java.lang.String str7 = xmlElement2.getAttribute("");
    XmlElement xmlElement9 = new XmlElement("hi!");
    java.util.Enumeration enumeration10 = xmlElement9.getAttributeNames();
    java.lang.String str12 = xmlElement9.getAttribute("hi!");
    int i13 = xmlElement9.count();
    boolean b15 = xmlElement9.equals((java.lang.Object)true);
    XmlElement xmlElement16 = xmlElement2.addSubElement(xmlElement9);
    int i17 = xmlElement16.count();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i17 == 0);

  }

  @Test
  public void test038() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test038"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.lang.String str4 = xmlElement1.getAttribute("hi!");
    int i5 = xmlElement1.countObservers();
    java.util.List list6 = xmlElement1.getElements();
    XmlElement xmlElement7 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement1.append(xmlElement7);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list6);

  }

  @Test
  public void test039() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test039"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.removeFromParent();
    XmlElement xmlElement5 = xmlElement2.getElement("");
    // The following exception was thrown during execution in test generation
    try {
    xmlElement5.removeAllElements();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement5);

  }

  @Test
  public void test040() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test040"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Observer observer2 = null;
    xmlElement1.deleteObserver(observer2);
    java.lang.String str6 = xmlElement1.getAttribute("", "");
    int i7 = xmlElement1.count();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + ""+ "'", str6.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);

  }

  @Test
  public void test041() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test041"); }


    XmlElement xmlElement2 = new XmlElement("hi!");
    boolean b4 = xmlElement2.equals((java.lang.Object)"hi!");
    XmlElement xmlElement6 = new XmlElement("hi!");
    boolean b8 = xmlElement6.equals((java.lang.Object)"hi!");
    XmlElement xmlElement9 = xmlElement2.removeElement(xmlElement6);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = xmlElement2.getAttributes();
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    java.lang.String str12 = xmlElement11.getData();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str12);

  }

  @Test
  public void test042() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test042"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    java.util.Enumeration enumeration9 = xmlElement1.getAttributeNames();
    XmlElement xmlElement11 = xmlElement1.getElement("hi!");
    java.lang.Object obj12 = xmlElement1.clone();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj12);

  }

  @Test
  public void test043() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test043"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    boolean b3 = xmlElement1.hasChanged();
    boolean b5 = xmlElement1.equals((java.lang.Object)'4');
    XmlElement xmlElement7 = new XmlElement("hi!");
    java.util.Enumeration enumeration8 = xmlElement7.getAttributeNames();
    java.lang.String str10 = xmlElement7.getAttribute("hi!");
    int i11 = xmlElement7.countObservers();
    java.util.List list12 = xmlElement7.getElements();
    boolean b13 = xmlElement1.equals((java.lang.Object)xmlElement7);
    java.util.Observer observer14 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement7.addObserver(observer14);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i11 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);

  }

  @Test
  public void test044() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test044"); }


    XmlElement xmlElement2 = new XmlElement("", "hi!");
    XmlElement xmlElement4 = new XmlElement("hi!");
    xmlElement2.append(xmlElement4);
    java.lang.String str7 = xmlElement2.getAttribute("");
    int i8 = xmlElement2.count();
    XmlElement xmlElement10 = new XmlElement("hi!");
    java.util.Enumeration enumeration11 = xmlElement10.getAttributeNames();
    XmlElement xmlElement13 = new XmlElement("hi!");
    java.util.Enumeration enumeration14 = xmlElement13.getAttributeNames();
    java.lang.String str16 = xmlElement13.getAttribute("hi!");
    XmlElement xmlElement17 = xmlElement10.removeElement(xmlElement13);
    XmlElement.printNode(xmlElement13, "hi!");
    XmlElement xmlElement21 = new XmlElement("hi!");
    java.util.Enumeration enumeration22 = xmlElement21.getAttributeNames();
    XmlElement xmlElement24 = new XmlElement("hi!");
    java.util.Enumeration enumeration25 = xmlElement24.getAttributeNames();
    java.lang.String str27 = xmlElement24.getAttribute("hi!");
    XmlElement xmlElement28 = xmlElement21.removeElement(xmlElement24);
    XmlElement.printNode(xmlElement24, "hi!");
    XmlElement xmlElement31 = xmlElement13.addSubElement(xmlElement24);
    int i32 = xmlElement24.count();
    boolean b33 = xmlElement2.equals((java.lang.Object)xmlElement24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 1);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str27);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement31);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i32 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b33 == false);

  }

  @Test
  public void test045() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test045"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Observer observer2 = null;
    xmlElement1.deleteObserver(observer2);
    java.lang.String str6 = xmlElement1.getAttribute("", "");
    XmlElement xmlElement8 = new XmlElement("hi!");
    boolean b10 = xmlElement8.equals((java.lang.Object)"hi!");
    xmlElement1.append(xmlElement8);
    xmlElement8.notifyObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + ""+ "'", str6.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);

  }

  @Test
  public void test046() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test046"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    java.util.Enumeration enumeration9 = xmlElement1.getAttributeNames();
    XmlElement xmlElement11 = xmlElement1.getElement("hi!");
    // The following exception was thrown during execution in test generation
    try {
    XmlElement.printNode(xmlElement11, "hi!");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);

  }

  @Test
  public void test047() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test047"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    boolean b3 = xmlElement1.equals((java.lang.Object)"hi!");
    XmlElement xmlElement5 = new XmlElement("hi!");
    boolean b7 = xmlElement5.equals((java.lang.Object)"hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.removeFromParent();
    XmlElement xmlElement15 = xmlElement11.addSubElement("hi!", "hi!");
    XmlElement xmlElement16 = xmlElement5.addSubElement(xmlElement15);
    XmlElement xmlElement18 = new XmlElement("hi!");
    boolean b20 = xmlElement18.equals((java.lang.Object)"hi!");
    XmlElement xmlElement22 = new XmlElement("hi!");
    boolean b24 = xmlElement22.equals((java.lang.Object)"hi!");
    XmlElement xmlElement25 = xmlElement18.removeElement(xmlElement22);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str26 = xmlElement18.getAttributes();
    xmlElement16.setAttributes(hashtable_str_str26);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str28 = xmlElement16.getAttributes();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b20 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b24 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str28);

  }

  @Test
  public void test048() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test048"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    XmlElement.printNode(xmlElement4, "hi!");
    XmlElement xmlElement12 = new XmlElement("hi!");
    java.util.Enumeration enumeration13 = xmlElement12.getAttributeNames();
    XmlElement xmlElement15 = new XmlElement("hi!");
    java.util.Enumeration enumeration16 = xmlElement15.getAttributeNames();
    java.lang.String str18 = xmlElement15.getAttribute("hi!");
    XmlElement xmlElement19 = xmlElement12.removeElement(xmlElement15);
    XmlElement.printNode(xmlElement15, "hi!");
    XmlElement xmlElement22 = xmlElement4.addSubElement(xmlElement15);
    int i23 = xmlElement15.count();
    XmlElement xmlElement26 = new XmlElement("", "hi!");
    XmlElement xmlElement28 = new XmlElement("hi!");
    xmlElement26.append(xmlElement28);
    java.lang.String str31 = xmlElement26.getAttribute("");
    XmlElement xmlElement32 = xmlElement15.removeElement(xmlElement26);
    XmlElement xmlElement33 = null;
    XmlElement xmlElement34 = xmlElement26.removeElement(xmlElement33);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i23 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str31);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement34);

  }

  @Test
  public void test049() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test049"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    XmlElement xmlElement6 = xmlElement2.removeElement(xmlElement5);
    xmlElement2.removeAllElements();
    XmlElement xmlElement9 = new XmlElement("hi!");
    java.util.Observer observer10 = null;
    xmlElement9.deleteObserver(observer10);
    java.lang.String str14 = xmlElement9.getAttribute("", "");
    XmlElement xmlElement16 = new XmlElement("hi!");
    boolean b18 = xmlElement16.equals((java.lang.Object)"hi!");
    xmlElement9.append(xmlElement16);
    xmlElement2.setParent(xmlElement9);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement22 = xmlElement9.removeElement((int)(byte)100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str14 + "' != '" + ""+ "'", str14.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b18 == false);

  }

  @Test
  public void test050() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test050"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    XmlElement xmlElement6 = xmlElement2.removeElement(xmlElement5);
    XmlElement xmlElement8 = new XmlElement("hi!");
    boolean b10 = xmlElement8.equals((java.lang.Object)"hi!");
    XmlElement xmlElement12 = new XmlElement("hi!");
    boolean b14 = xmlElement12.equals((java.lang.Object)"hi!");
    XmlElement xmlElement15 = xmlElement8.removeElement(xmlElement12);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str17 = null;
    XmlElement xmlElement18 = new XmlElement("hi!", hashtable_str_str17);
    xmlElement18.removeFromParent();
    XmlElement xmlElement22 = xmlElement18.addSubElement("hi!", "hi!");
    XmlElement xmlElement23 = xmlElement12.addSubElement(xmlElement22);
    xmlElement5.setParent(xmlElement22);
    java.util.List list25 = xmlElement22.getElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list25);

  }

  @Test
  public void test051() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test051"); }


    XmlElement xmlElement2 = new XmlElement("hi!", "");
    xmlElement2.removeAllElements();
    java.lang.String str4 = xmlElement2.getName();
    XmlElement xmlElement6 = new XmlElement("hi!");
    boolean b8 = xmlElement6.equals((java.lang.Object)"hi!");
    XmlElement xmlElement10 = new XmlElement("hi!");
    boolean b12 = xmlElement10.equals((java.lang.Object)"hi!");
    XmlElement xmlElement13 = xmlElement6.removeElement(xmlElement10);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str15 = null;
    XmlElement xmlElement16 = new XmlElement("hi!", hashtable_str_str15);
    xmlElement16.removeFromParent();
    XmlElement xmlElement20 = xmlElement16.addSubElement("hi!", "hi!");
    XmlElement xmlElement21 = xmlElement10.addSubElement(xmlElement20);
    // The following exception was thrown during execution in test generation
    try {
    xmlElement2.insertElement(xmlElement21, (int)(byte)100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement21);

  }

  @Test
  public void test052() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test052"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.lang.String str4 = xmlElement1.getAttribute("hi!");
    int i5 = xmlElement1.countObservers();
    XmlElement xmlElement7 = new XmlElement("hi!");
    java.util.Enumeration enumeration8 = xmlElement7.getAttributeNames();
    java.lang.String str10 = xmlElement7.getAttribute("hi!");
    int i11 = xmlElement7.count();
    XmlElement xmlElement13 = xmlElement7.getElement("");
    xmlElement1.setParent(xmlElement7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i11 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement13);

  }

  @Test
  public void test053() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test053"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.removeFromParent();
    XmlElement xmlElement6 = xmlElement2.addSubElement("hi!", "hi!");
    xmlElement2.removeAllElements();
    XmlElement xmlElement8 = xmlElement2.getParent();
    xmlElement2.deleteObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);

  }

  @Test
  public void test054() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test054"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    XmlElement xmlElement6 = xmlElement2.removeElement(xmlElement5);
    java.util.Observer observer7 = null;
    xmlElement2.deleteObserver(observer7);
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str10 = xmlElement2.getAttribute("");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);

  }

  @Test
  public void test055() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test055"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    boolean b3 = xmlElement1.hasChanged();
    boolean b5 = xmlElement1.equals((java.lang.Object)'4');
    java.lang.Object obj8 = xmlElement1.addAttribute("hi!", "");
    java.util.Observer observer9 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement1.addObserver(observer9);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj8);

  }

  @Test
  public void test056() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test056"); }


    XmlElement xmlElement1 = new XmlElement("");

  }

  @Test
  public void test057() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test057"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    XmlElement xmlElement6 = xmlElement2.removeElement(xmlElement5);
    java.util.List list7 = xmlElement5.getElements();
    xmlElement5.setName("hi!");
    xmlElement5.notifyObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);

  }

  @Test
  public void test058() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test058"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.lang.Object obj2 = xmlElement1.clone();
    XmlElement xmlElement5 = new XmlElement("", "hi!");
    XmlElement xmlElement7 = new XmlElement("hi!");
    xmlElement5.append(xmlElement7);
    java.lang.String str10 = xmlElement5.getAttribute("");
    xmlElement1.setParent(xmlElement5);
    XmlElement xmlElement13 = xmlElement1.getElement("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement13);

  }

  @Test
  public void test059() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test059"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    boolean b3 = xmlElement1.equals((java.lang.Object)"hi!");
    XmlElement xmlElement5 = new XmlElement("hi!");
    boolean b7 = xmlElement5.equals((java.lang.Object)"hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.removeFromParent();
    XmlElement xmlElement15 = xmlElement11.addSubElement("hi!", "hi!");
    XmlElement xmlElement16 = xmlElement5.addSubElement(xmlElement15);
    java.util.Observer observer17 = null;
    xmlElement16.deleteObserver(observer17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement16);

  }

  @Test
  public void test060() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test060"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    xmlElement4.deleteObservers();
    XmlElement xmlElement11 = new XmlElement("hi!");
    java.util.Enumeration enumeration12 = xmlElement11.getAttributeNames();
    XmlElement xmlElement14 = new XmlElement("hi!");
    java.util.Enumeration enumeration15 = xmlElement14.getAttributeNames();
    java.lang.String str17 = xmlElement14.getAttribute("hi!");
    XmlElement xmlElement18 = xmlElement11.removeElement(xmlElement14);
    xmlElement4.notifyObservers((java.lang.Object)xmlElement14);
    boolean b20 = xmlElement4.hasChanged();
    XmlElement xmlElement22 = new XmlElement("hi!");
    java.util.Enumeration enumeration23 = xmlElement22.getAttributeNames();
    java.lang.String str25 = xmlElement22.getAttribute("hi!");
    int i26 = xmlElement22.countObservers();
    java.util.List list27 = xmlElement22.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str29 = null;
    XmlElement xmlElement30 = new XmlElement("hi!", hashtable_str_str29);
    xmlElement30.removeFromParent();
    XmlElement xmlElement33 = xmlElement30.getElement("");
    boolean b34 = xmlElement22.equals((java.lang.Object)xmlElement33);
    // The following exception was thrown during execution in test generation
    try {
    boolean b35 = xmlElement4.addElement(xmlElement33);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b20 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i26 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list27);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement33);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b34 == false);

  }

  @Test
  public void test061() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test061"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    XmlElement.printNode(xmlElement4, "hi!");
    int i11 = xmlElement4.count();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i11 == 0);

  }

  @Test
  public void test062() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test062"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Observer observer2 = null;
    xmlElement1.deleteObserver(observer2);
    java.lang.String str6 = xmlElement1.getAttribute("", "");
    XmlElement xmlElement8 = new XmlElement("hi!");
    boolean b10 = xmlElement8.equals((java.lang.Object)"hi!");
    xmlElement1.append(xmlElement8);
    xmlElement1.setName("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + ""+ "'", str6.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);

  }

  @Test
  public void test063() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test063"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    boolean b3 = xmlElement1.equals((java.lang.Object)"hi!");
    XmlElement xmlElement5 = new XmlElement("hi!");
    boolean b7 = xmlElement5.equals((java.lang.Object)"hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement5);
    java.lang.String str10 = xmlElement1.getAttribute("");
    java.lang.String str12 = xmlElement1.getAttribute("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str12);

  }

  @Test
  public void test064() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test064"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    XmlElement xmlElement6 = xmlElement2.removeElement(xmlElement5);
    xmlElement2.removeAllElements();
    XmlElement xmlElement9 = new XmlElement("hi!");
    boolean b11 = xmlElement9.equals((java.lang.Object)"hi!");
    XmlElement xmlElement13 = new XmlElement("hi!");
    boolean b15 = xmlElement13.equals((java.lang.Object)"hi!");
    XmlElement xmlElement16 = xmlElement9.removeElement(xmlElement13);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str17 = xmlElement9.getAttributes();
    xmlElement2.setAttributes(hashtable_str_str17);
    java.util.Enumeration enumeration19 = xmlElement2.getAttributeNames();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration19);

  }

  @Test
  public void test065() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test065"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    boolean b3 = xmlElement1.hasChanged();
    java.lang.Object obj4 = xmlElement1.clone();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement6 = xmlElement1.removeElement((int)(short)100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj4);

  }

  @Test
  public void test066() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test066"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.lang.Object obj2 = xmlElement1.clone();
    int i3 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    boolean b7 = xmlElement1.equals((java.lang.Object)xmlElement6);
    java.util.Observer observer8 = null;
    xmlElement1.deleteObserver(observer8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i3 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test067() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test067"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    XmlElement xmlElement6 = xmlElement2.removeElement(xmlElement5);
    java.util.List list7 = xmlElement5.getElements();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement9 = xmlElement5.removeElement((-1));
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list7);

  }

  @Test
  public void test068() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test068"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    java.util.Enumeration enumeration9 = xmlElement1.getAttributeNames();
    XmlElement xmlElement11 = xmlElement1.getElement("hi!");
    int i12 = xmlElement1.countObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i12 == 0);

  }

  @Test
  public void test069() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test069"); }


    XmlElement xmlElement2 = new XmlElement("hi!");
    java.util.Enumeration enumeration3 = xmlElement2.getAttributeNames();
    boolean b4 = xmlElement2.hasChanged();
    boolean b6 = xmlElement2.equals((java.lang.Object)'4');
    XmlElement xmlElement8 = new XmlElement("hi!");
    java.util.Enumeration enumeration9 = xmlElement8.getAttributeNames();
    java.lang.String str11 = xmlElement8.getAttribute("hi!");
    int i12 = xmlElement8.countObservers();
    java.util.List list13 = xmlElement8.getElements();
    boolean b14 = xmlElement2.equals((java.lang.Object)xmlElement8);
    XmlElement xmlElement16 = new XmlElement("hi!");
    boolean b18 = xmlElement16.equals((java.lang.Object)"hi!");
    XmlElement xmlElement20 = new XmlElement("hi!");
    boolean b22 = xmlElement20.equals((java.lang.Object)"hi!");
    XmlElement xmlElement23 = xmlElement16.removeElement(xmlElement20);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str25 = null;
    XmlElement xmlElement26 = new XmlElement("hi!", hashtable_str_str25);
    xmlElement26.removeFromParent();
    XmlElement xmlElement30 = xmlElement26.addSubElement("hi!", "hi!");
    XmlElement xmlElement31 = xmlElement20.addSubElement(xmlElement30);
    XmlElement xmlElement33 = new XmlElement("hi!");
    boolean b35 = xmlElement33.equals((java.lang.Object)"hi!");
    XmlElement xmlElement37 = new XmlElement("hi!");
    boolean b39 = xmlElement37.equals((java.lang.Object)"hi!");
    XmlElement xmlElement40 = xmlElement33.removeElement(xmlElement37);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str41 = xmlElement33.getAttributes();
    xmlElement31.setAttributes(hashtable_str_str41);
    xmlElement8.setAttributes(hashtable_str_str41);
    XmlElement xmlElement44 = new XmlElement("hi!", hashtable_str_str41);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i12 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b18 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b22 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement31);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b35 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b39 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement40);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str41);

  }

  @Test
  public void test070() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test070"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    XmlElement xmlElement3 = xmlElement2.getParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement3);

  }

  @Test
  public void test071() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test071"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.lang.String str4 = xmlElement1.getAttribute("hi!");
    int i5 = xmlElement1.count();
    XmlElement xmlElement7 = new XmlElement("hi!");
    java.util.Enumeration enumeration8 = xmlElement7.getAttributeNames();
    boolean b9 = xmlElement7.hasChanged();
    java.lang.Object obj10 = xmlElement7.clone();
    xmlElement7.deleteObservers();
    xmlElement1.setParent(xmlElement7);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("hi!", hashtable_str_str14);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str17 = null;
    XmlElement xmlElement18 = new XmlElement("hi!", hashtable_str_str17);
    XmlElement xmlElement19 = xmlElement15.removeElement(xmlElement18);
    xmlElement15.removeAllElements();
    XmlElement xmlElement22 = new XmlElement("hi!");
    boolean b24 = xmlElement22.equals((java.lang.Object)"hi!");
    XmlElement xmlElement26 = new XmlElement("hi!");
    boolean b28 = xmlElement26.equals((java.lang.Object)"hi!");
    XmlElement xmlElement29 = xmlElement22.removeElement(xmlElement26);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str31 = null;
    XmlElement xmlElement32 = new XmlElement("hi!", hashtable_str_str31);
    xmlElement32.removeFromParent();
    XmlElement xmlElement36 = xmlElement32.addSubElement("hi!", "hi!");
    XmlElement xmlElement37 = xmlElement26.addSubElement(xmlElement36);
    xmlElement15.setParent(xmlElement37);
    xmlElement1.setParent(xmlElement15);
    XmlElement xmlElement41 = new XmlElement("hi!");
    java.util.Enumeration enumeration42 = xmlElement41.getAttributeNames();
    boolean b43 = xmlElement41.hasChanged();
    boolean b45 = xmlElement41.equals((java.lang.Object)'4');
    java.lang.Object obj48 = xmlElement41.addAttribute("hi!", "");
    XmlElement.printNode(xmlElement41, "hi!");
    xmlElement41.notifyObservers();
    // The following exception was thrown during execution in test generation
    try {
    xmlElement1.insertElement(xmlElement41, (int)'4');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b24 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b28 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement36);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration42);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b43 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b45 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj48);

  }

  @Test
  public void test072() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test072"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement3 = xmlElement1.getParent();
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
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement3);

  }

  @Test
  public void test073() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test073"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.lang.String str4 = xmlElement1.getAttribute("hi!");
    int i5 = xmlElement1.count();
    XmlElement xmlElement7 = new XmlElement("hi!");
    java.util.Enumeration enumeration8 = xmlElement7.getAttributeNames();
    boolean b9 = xmlElement7.hasChanged();
    java.lang.Object obj10 = xmlElement7.clone();
    xmlElement7.deleteObservers();
    xmlElement1.setParent(xmlElement7);
    int i13 = xmlElement1.countObservers();
    xmlElement1.setData("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);

  }

  @Test
  public void test074() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test074"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    boolean b3 = xmlElement1.equals((java.lang.Object)"hi!");
    XmlElement xmlElement5 = new XmlElement("hi!");
    boolean b7 = xmlElement5.equals((java.lang.Object)"hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement5);
    java.lang.String str10 = xmlElement1.getAttribute("");
    boolean b11 = xmlElement1.hasChanged();
    XmlElement xmlElement13 = new XmlElement("hi!");
    java.util.Enumeration enumeration14 = xmlElement13.getAttributeNames();
    XmlElement xmlElement16 = new XmlElement("hi!");
    java.util.Enumeration enumeration17 = xmlElement16.getAttributeNames();
    java.lang.String str19 = xmlElement16.getAttribute("hi!");
    XmlElement xmlElement20 = xmlElement13.removeElement(xmlElement16);
    XmlElement.printNode(xmlElement16, "hi!");
    XmlElement xmlElement24 = new XmlElement("hi!");
    java.util.Enumeration enumeration25 = xmlElement24.getAttributeNames();
    XmlElement xmlElement27 = new XmlElement("hi!");
    java.util.Enumeration enumeration28 = xmlElement27.getAttributeNames();
    java.lang.String str30 = xmlElement27.getAttribute("hi!");
    XmlElement xmlElement31 = xmlElement24.removeElement(xmlElement27);
    XmlElement.printNode(xmlElement27, "hi!");
    XmlElement xmlElement34 = xmlElement16.addSubElement(xmlElement27);
    XmlElement xmlElement35 = xmlElement1.addSubElement(xmlElement16);
    java.util.Enumeration enumeration36 = xmlElement1.getAttributeNames();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement31);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement34);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement35);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration36);

  }

  @Test
  public void test075() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test075"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.List list3 = xmlElement2.getElements();
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
    org.junit.Assert.assertNotNull(list3);

  }

  @Test
  public void test076() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test076"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    boolean b3 = xmlElement1.hasChanged();
    boolean b5 = xmlElement1.equals((java.lang.Object)'4');
    java.lang.Object obj8 = xmlElement1.addAttribute("hi!", "");
    xmlElement1.notifyObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj8);

  }

  @Test
  public void test077() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test077"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    boolean b3 = xmlElement1.equals((java.lang.Object)"hi!");
    xmlElement1.setData("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);

  }

  @Test
  public void test078() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test078"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    boolean b3 = xmlElement1.hasChanged();
    boolean b5 = xmlElement1.equals((java.lang.Object)'4');
    XmlElement xmlElement7 = new XmlElement("hi!");
    java.util.Enumeration enumeration8 = xmlElement7.getAttributeNames();
    java.lang.String str10 = xmlElement7.getAttribute("hi!");
    int i11 = xmlElement7.countObservers();
    java.util.List list12 = xmlElement7.getElements();
    boolean b13 = xmlElement1.equals((java.lang.Object)xmlElement7);
    XmlElement xmlElement15 = new XmlElement("hi!");
    boolean b17 = xmlElement15.equals((java.lang.Object)"hi!");
    XmlElement xmlElement19 = new XmlElement("hi!");
    boolean b21 = xmlElement19.equals((java.lang.Object)"hi!");
    XmlElement xmlElement22 = xmlElement15.removeElement(xmlElement19);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str24 = null;
    XmlElement xmlElement25 = new XmlElement("hi!", hashtable_str_str24);
    xmlElement25.removeFromParent();
    XmlElement xmlElement29 = xmlElement25.addSubElement("hi!", "hi!");
    XmlElement xmlElement30 = xmlElement19.addSubElement(xmlElement29);
    XmlElement xmlElement32 = new XmlElement("hi!");
    boolean b34 = xmlElement32.equals((java.lang.Object)"hi!");
    XmlElement xmlElement36 = new XmlElement("hi!");
    boolean b38 = xmlElement36.equals((java.lang.Object)"hi!");
    XmlElement xmlElement39 = xmlElement32.removeElement(xmlElement36);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str40 = xmlElement32.getAttributes();
    xmlElement30.setAttributes(hashtable_str_str40);
    xmlElement7.setAttributes(hashtable_str_str40);
    xmlElement7.notifyObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i11 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b34 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b38 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement39);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str40);

  }

  @Test
  public void test079() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test079"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.lang.String str4 = xmlElement1.getAttribute("hi!");
    int i5 = xmlElement1.count();
    XmlElement xmlElement7 = new XmlElement("hi!");
    java.util.Enumeration enumeration8 = xmlElement7.getAttributeNames();
    boolean b9 = xmlElement7.hasChanged();
    java.lang.Object obj10 = xmlElement7.clone();
    xmlElement7.deleteObservers();
    xmlElement1.setParent(xmlElement7);
    xmlElement1.removeFromParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj10);

  }

  @Test
  public void test080() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test080"); }


    XmlElement xmlElement2 = new XmlElement("hi!", "");
    xmlElement2.removeAllElements();
    java.lang.String str4 = xmlElement2.getData();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + ""+ "'", str4.equals(""));

  }

  @Test
  public void test081() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test081"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    boolean b3 = xmlElement1.equals((java.lang.Object)"hi!");
    XmlElement xmlElement5 = new XmlElement("hi!");
    boolean b7 = xmlElement5.equals((java.lang.Object)"hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.removeFromParent();
    XmlElement xmlElement15 = xmlElement11.addSubElement("hi!", "hi!");
    XmlElement xmlElement16 = xmlElement5.addSubElement(xmlElement15);
    java.lang.Object obj19 = xmlElement5.addAttribute("hi!", "hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj19);

  }

  @Test
  public void test082() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test082"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    boolean b3 = xmlElement1.equals((java.lang.Object)"hi!");
    XmlElement xmlElement5 = new XmlElement("hi!");
    boolean b7 = xmlElement5.equals((java.lang.Object)"hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.removeFromParent();
    XmlElement xmlElement15 = xmlElement11.addSubElement("hi!", "hi!");
    XmlElement xmlElement16 = xmlElement5.addSubElement(xmlElement15);
    java.lang.Object obj19 = xmlElement5.addAttribute("hi!", "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj19);

  }

  @Test
  public void test083() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test083"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.util.Observer observer3 = null;
    xmlElement1.deleteObserver(observer3);
    xmlElement1.notifyObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("hi!", hashtable_str_str7);
    xmlElement8.removeFromParent();
    XmlElement xmlElement12 = xmlElement8.addSubElement("hi!", "hi!");
    xmlElement8.removeAllElements();
    xmlElement1.append(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement12);

  }

  @Test
  public void test084() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test084"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.lang.Object obj2 = xmlElement1.clone();
    XmlElement xmlElement5 = new XmlElement("", "hi!");
    XmlElement xmlElement7 = new XmlElement("hi!");
    xmlElement5.append(xmlElement7);
    java.lang.String str10 = xmlElement5.getAttribute("");
    XmlElement xmlElement12 = new XmlElement("hi!");
    java.util.Enumeration enumeration13 = xmlElement12.getAttributeNames();
    java.lang.String str15 = xmlElement12.getAttribute("hi!");
    int i16 = xmlElement12.count();
    boolean b18 = xmlElement12.equals((java.lang.Object)true);
    XmlElement xmlElement19 = xmlElement5.addSubElement(xmlElement12);
    xmlElement1.notifyObservers((java.lang.Object)xmlElement19);
    java.lang.String str21 = xmlElement1.getName();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i16 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b18 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str21 + "' != '" + "hi!"+ "'", str21.equals("hi!"));

  }

  @Test
  public void test085() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test085"); }


    XmlElement xmlElement2 = new XmlElement("", "hi!");
    XmlElement xmlElement4 = new XmlElement("hi!");
    xmlElement2.append(xmlElement4);
    java.lang.Object obj8 = xmlElement2.addAttribute("hi!", "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj8);

  }

  @Test
  public void test086() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test086"); }


    XmlElement xmlElement2 = new XmlElement("", "");
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement4 = xmlElement2.getElement(10);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test087() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test087"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.removeFromParent();
    XmlElement xmlElement6 = xmlElement2.addSubElement("hi!", "hi!");
    xmlElement2.setData("");
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str10 = xmlElement2.getAttribute("");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);

  }

  @Test
  public void test088() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test088"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.lang.String str4 = xmlElement1.getAttribute("hi!");
    int i5 = xmlElement1.count();
    boolean b7 = xmlElement1.equals((java.lang.Object)true);
    XmlElement xmlElement9 = new XmlElement("hi!");
    java.util.Enumeration enumeration10 = xmlElement9.getAttributeNames();
    boolean b11 = xmlElement9.hasChanged();
    java.lang.Object obj12 = xmlElement9.clone();
    xmlElement9.deleteObservers();
    xmlElement1.setParent(xmlElement9);
    XmlElement xmlElement17 = new XmlElement("", "hi!");
    XmlElement xmlElement19 = new XmlElement("hi!");
    xmlElement17.append(xmlElement19);
    // The following exception was thrown during execution in test generation
    try {
    xmlElement9.insertElement(xmlElement19, (int)'a');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj12);

  }

  @Test
  public void test089() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test089"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    boolean b3 = xmlElement1.hasChanged();
    java.lang.Object obj4 = xmlElement1.clone();
    xmlElement1.deleteObservers();
    java.lang.String str6 = xmlElement1.getData();
    XmlElement.printNode(xmlElement1, "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + ""+ "'", str6.equals(""));

  }

  @Test
  public void test090() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test090"); }


    XmlElement xmlElement2 = new XmlElement("", "hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    xmlElement2.notifyObservers((java.lang.Object)xmlElement5);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement8 = xmlElement5.removeElement((int)(byte)10);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test091() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test091"); }


    XmlElement xmlElement2 = new XmlElement("", "hi!");
    XmlElement xmlElement4 = new XmlElement("hi!");
    xmlElement2.append(xmlElement4);
    java.lang.String str7 = xmlElement2.getAttribute("");
    XmlElement xmlElement9 = new XmlElement("hi!");
    java.util.Enumeration enumeration10 = xmlElement9.getAttributeNames();
    java.lang.String str12 = xmlElement9.getAttribute("hi!");
    int i13 = xmlElement9.count();
    boolean b15 = xmlElement9.equals((java.lang.Object)true);
    XmlElement xmlElement16 = xmlElement2.addSubElement(xmlElement9);
    XmlElement xmlElement18 = new XmlElement("hi!");
    java.util.Enumeration enumeration19 = xmlElement18.getAttributeNames();
    XmlElement xmlElement21 = new XmlElement("hi!");
    java.util.Enumeration enumeration22 = xmlElement21.getAttributeNames();
    java.lang.String str24 = xmlElement21.getAttribute("hi!");
    XmlElement xmlElement25 = xmlElement18.removeElement(xmlElement21);
    java.util.Enumeration enumeration26 = xmlElement18.getAttributeNames();
    java.util.Enumeration enumeration27 = xmlElement18.getAttributeNames();
    XmlElement xmlElement29 = new XmlElement("hi!");
    java.util.Enumeration enumeration30 = xmlElement29.getAttributeNames();
    java.lang.String str32 = xmlElement29.getAttribute("hi!");
    int i33 = xmlElement29.count();
    boolean b35 = xmlElement29.equals((java.lang.Object)true);
    java.lang.String str36 = xmlElement29.getData();
    boolean b37 = xmlElement18.equals((java.lang.Object)str36);
    // The following exception was thrown during execution in test generation
    try {
    xmlElement2.insertElement(xmlElement18, (int)(short)10);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration27);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i33 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b35 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str36 + "' != '" + ""+ "'", str36.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b37 == false);

  }

  @Test
  public void test092() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test092"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    XmlElement xmlElement6 = xmlElement2.removeElement(xmlElement5);
    java.util.Observer observer7 = null;
    xmlElement2.deleteObserver(observer7);
    xmlElement2.setName("hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = null;
    XmlElement xmlElement13 = new XmlElement("hi!", hashtable_str_str12);
    xmlElement13.removeFromParent();
    XmlElement xmlElement17 = xmlElement13.addSubElement("hi!", "hi!");
    xmlElement13.removeAllElements();
    XmlElement xmlElement19 = xmlElement13.getParent();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement20 = xmlElement2.addSubElement(xmlElement19);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement19);

  }

  @Test
  public void test093() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test093"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.util.Observer observer3 = null;
    xmlElement1.deleteObserver(observer3);
    xmlElement1.notifyObservers();
    int i6 = xmlElement1.countObservers();
    XmlElement xmlElement9 = new XmlElement("hi!");
    java.util.Enumeration enumeration10 = xmlElement9.getAttributeNames();
    boolean b11 = xmlElement9.hasChanged();
    boolean b13 = xmlElement9.equals((java.lang.Object)'4');
    XmlElement xmlElement15 = new XmlElement("hi!");
    java.util.Enumeration enumeration16 = xmlElement15.getAttributeNames();
    java.lang.String str18 = xmlElement15.getAttribute("hi!");
    int i19 = xmlElement15.countObservers();
    java.util.List list20 = xmlElement15.getElements();
    boolean b21 = xmlElement9.equals((java.lang.Object)xmlElement15);
    XmlElement xmlElement23 = new XmlElement("hi!");
    boolean b25 = xmlElement23.equals((java.lang.Object)"hi!");
    XmlElement xmlElement27 = new XmlElement("hi!");
    boolean b29 = xmlElement27.equals((java.lang.Object)"hi!");
    XmlElement xmlElement30 = xmlElement23.removeElement(xmlElement27);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str32 = null;
    XmlElement xmlElement33 = new XmlElement("hi!", hashtable_str_str32);
    xmlElement33.removeFromParent();
    XmlElement xmlElement37 = xmlElement33.addSubElement("hi!", "hi!");
    XmlElement xmlElement38 = xmlElement27.addSubElement(xmlElement37);
    XmlElement xmlElement40 = new XmlElement("hi!");
    boolean b42 = xmlElement40.equals((java.lang.Object)"hi!");
    XmlElement xmlElement44 = new XmlElement("hi!");
    boolean b46 = xmlElement44.equals((java.lang.Object)"hi!");
    XmlElement xmlElement47 = xmlElement40.removeElement(xmlElement44);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str48 = xmlElement40.getAttributes();
    xmlElement38.setAttributes(hashtable_str_str48);
    xmlElement15.setAttributes(hashtable_str_str48);
    XmlElement xmlElement51 = new XmlElement("", hashtable_str_str48);
    boolean b52 = xmlElement1.addElement(xmlElement51);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement54 = xmlElement51.removeElement((int)(short)10);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i19 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b25 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b29 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement38);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b42 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b46 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement47);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str48);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b52 == true);

  }

  @Test
  public void test094() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test094"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    XmlElement.printNode(xmlElement4, "hi!");
    XmlElement xmlElement12 = new XmlElement("hi!");
    java.util.Enumeration enumeration13 = xmlElement12.getAttributeNames();
    XmlElement xmlElement15 = new XmlElement("hi!");
    java.util.Enumeration enumeration16 = xmlElement15.getAttributeNames();
    java.lang.String str18 = xmlElement15.getAttribute("hi!");
    XmlElement xmlElement19 = xmlElement12.removeElement(xmlElement15);
    XmlElement.printNode(xmlElement15, "hi!");
    XmlElement xmlElement22 = xmlElement4.addSubElement(xmlElement15);
    XmlElement xmlElement25 = xmlElement22.addSubElement("", "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement25);

  }

  @Test
  public void test095() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test095"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    boolean b3 = xmlElement1.equals((java.lang.Object)"hi!");
    XmlElement xmlElement5 = new XmlElement("hi!");
    boolean b7 = xmlElement5.equals((java.lang.Object)"hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.removeFromParent();
    XmlElement xmlElement15 = xmlElement11.addSubElement("hi!", "hi!");
    XmlElement xmlElement16 = xmlElement5.addSubElement(xmlElement15);
    java.lang.Object obj17 = xmlElement15.clone();
    XmlElement xmlElement19 = new XmlElement("hi!");
    java.util.Enumeration enumeration20 = xmlElement19.getAttributeNames();
    java.lang.String str21 = xmlElement19.getName();
    xmlElement15.insertElement(xmlElement19, 0);
    java.lang.Object obj24 = xmlElement15.clone();
    xmlElement15.setData("");
    java.lang.Object obj29 = xmlElement15.addAttribute("hi!", "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str21 + "' != '" + "hi!"+ "'", str21.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj29);

  }

  @Test
  public void test096() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test096"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.removeFromParent();
    XmlElement xmlElement6 = xmlElement2.addSubElement("hi!", "hi!");
    xmlElement2.setData("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("hi!", hashtable_str_str14);
    XmlElement xmlElement16 = xmlElement12.removeElement(xmlElement15);
    xmlElement12.removeAllElements();
    XmlElement xmlElement19 = new XmlElement("hi!");
    boolean b21 = xmlElement19.equals((java.lang.Object)"hi!");
    XmlElement xmlElement23 = new XmlElement("hi!");
    boolean b25 = xmlElement23.equals((java.lang.Object)"hi!");
    XmlElement xmlElement26 = xmlElement19.removeElement(xmlElement23);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str28 = null;
    XmlElement xmlElement29 = new XmlElement("hi!", hashtable_str_str28);
    xmlElement29.removeFromParent();
    XmlElement xmlElement33 = xmlElement29.addSubElement("hi!", "hi!");
    XmlElement xmlElement34 = xmlElement23.addSubElement(xmlElement33);
    xmlElement12.setParent(xmlElement34);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str36 = xmlElement34.getAttributes();
    XmlElement xmlElement37 = new XmlElement("", hashtable_str_str36);
    boolean b38 = xmlElement2.equals((java.lang.Object)xmlElement37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b25 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement33);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement34);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str36);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b38 == false);

  }

  @Test
  public void test097() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test097"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.lang.String str4 = xmlElement1.getAttribute("hi!");
    XmlElement xmlElement6 = new XmlElement("hi!");
    boolean b8 = xmlElement6.equals((java.lang.Object)"hi!");
    XmlElement xmlElement10 = new XmlElement("hi!");
    boolean b12 = xmlElement10.equals((java.lang.Object)"hi!");
    XmlElement xmlElement13 = xmlElement6.removeElement(xmlElement10);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str15 = null;
    XmlElement xmlElement16 = new XmlElement("hi!", hashtable_str_str15);
    xmlElement16.removeFromParent();
    XmlElement xmlElement20 = xmlElement16.addSubElement("hi!", "hi!");
    XmlElement xmlElement21 = xmlElement10.addSubElement(xmlElement20);
    java.util.List list22 = xmlElement21.getElements();
    boolean b23 = xmlElement1.addElement(xmlElement21);
    XmlElement xmlElement26 = new XmlElement("", "hi!");
    XmlElement xmlElement28 = new XmlElement("hi!");
    xmlElement26.append(xmlElement28);
    java.lang.String str31 = xmlElement26.getAttribute("");
    XmlElement xmlElement33 = xmlElement26.addSubElement("");
    xmlElement1.setParent(xmlElement26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b23 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str31);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement33);

  }

  @Test
  public void test098() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test098"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    XmlElement xmlElement6 = xmlElement2.removeElement(xmlElement5);
    xmlElement2.removeAllElements();
    XmlElement xmlElement9 = new XmlElement("hi!");
    boolean b11 = xmlElement9.equals((java.lang.Object)"hi!");
    XmlElement xmlElement13 = new XmlElement("hi!");
    boolean b15 = xmlElement13.equals((java.lang.Object)"hi!");
    XmlElement xmlElement16 = xmlElement9.removeElement(xmlElement13);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str18 = null;
    XmlElement xmlElement19 = new XmlElement("hi!", hashtable_str_str18);
    xmlElement19.removeFromParent();
    XmlElement xmlElement23 = xmlElement19.addSubElement("hi!", "hi!");
    XmlElement xmlElement24 = xmlElement13.addSubElement(xmlElement23);
    xmlElement2.setParent(xmlElement24);
    xmlElement24.removeAllElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement24);

  }

  @Test
  public void test099() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test099"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    boolean b3 = xmlElement1.hasChanged();
    java.lang.Object obj4 = xmlElement1.clone();
    java.util.Enumeration enumeration5 = xmlElement1.getAttributeNames();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);

  }

  @Test
  public void test100() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test100"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    xmlElement2.removeFromParent();
    XmlElement xmlElement6 = xmlElement2.addSubElement("hi!", "hi!");
    xmlElement2.removeAllElements();
    XmlElement xmlElement11 = new XmlElement("hi!");
    boolean b13 = xmlElement11.equals((java.lang.Object)"hi!");
    XmlElement xmlElement15 = new XmlElement("hi!");
    boolean b17 = xmlElement15.equals((java.lang.Object)"hi!");
    XmlElement xmlElement18 = xmlElement11.removeElement(xmlElement15);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str19 = xmlElement11.getAttributes();
    XmlElement xmlElement20 = new XmlElement("", hashtable_str_str19);
    XmlElement xmlElement21 = new XmlElement("hi!", hashtable_str_str19);
    xmlElement2.setAttributes(hashtable_str_str19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str19);

  }

  @Test
  public void test101() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test101"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    xmlElement4.deleteObservers();
    XmlElement xmlElement11 = xmlElement4.addSubElement("");
    XmlElement xmlElement13 = new XmlElement("hi!");
    java.util.Enumeration enumeration14 = xmlElement13.getAttributeNames();
    java.lang.String str16 = xmlElement13.getAttribute("hi!");
    int i17 = xmlElement13.count();
    XmlElement xmlElement19 = xmlElement13.getElement("");
    boolean b20 = xmlElement4.equals((java.lang.Object)xmlElement13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i17 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b20 == false);

  }

  @Test
  public void test102() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test102"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Observer observer2 = null;
    xmlElement1.deleteObserver(observer2);
    java.lang.String str6 = xmlElement1.getAttribute("", "");
    XmlElement xmlElement8 = new XmlElement("hi!");
    boolean b10 = xmlElement8.equals((java.lang.Object)"hi!");
    xmlElement1.append(xmlElement8);
    xmlElement1.deleteObservers();
    xmlElement1.setData("hi!");
    java.lang.String str17 = xmlElement1.getAttribute("hi!", "hi!");
    XmlElement xmlElement19 = new XmlElement("hi!");
    java.util.Enumeration enumeration20 = xmlElement19.getAttributeNames();
    java.lang.String str21 = xmlElement19.getName();
    xmlElement19.removeAllElements();
    xmlElement1.insertElement(xmlElement19, (int)(short)0);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str26 = null;
    XmlElement xmlElement27 = new XmlElement("hi!", hashtable_str_str26);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str29 = null;
    XmlElement xmlElement30 = new XmlElement("hi!", hashtable_str_str29);
    XmlElement xmlElement31 = xmlElement27.removeElement(xmlElement30);
    xmlElement27.removeAllElements();
    XmlElement xmlElement34 = new XmlElement("hi!");
    java.util.Observer observer35 = null;
    xmlElement34.deleteObserver(observer35);
    java.lang.String str39 = xmlElement34.getAttribute("", "");
    XmlElement xmlElement41 = new XmlElement("hi!");
    boolean b43 = xmlElement41.equals((java.lang.Object)"hi!");
    xmlElement34.append(xmlElement41);
    xmlElement27.setParent(xmlElement34);
    XmlElement xmlElement47 = new XmlElement("hi!");
    boolean b49 = xmlElement47.equals((java.lang.Object)"hi!");
    XmlElement xmlElement51 = new XmlElement("hi!");
    boolean b53 = xmlElement51.equals((java.lang.Object)"hi!");
    XmlElement xmlElement54 = xmlElement47.removeElement(xmlElement51);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str56 = null;
    XmlElement xmlElement57 = new XmlElement("hi!", hashtable_str_str56);
    xmlElement57.removeFromParent();
    XmlElement xmlElement61 = xmlElement57.addSubElement("hi!", "hi!");
    XmlElement xmlElement62 = xmlElement51.addSubElement(xmlElement61);
    XmlElement xmlElement65 = new XmlElement("hi!", "hi!");
    xmlElement51.notifyObservers((java.lang.Object)xmlElement65);
    XmlElement xmlElement67 = xmlElement27.addSubElement(xmlElement65);
    XmlElement xmlElement68 = xmlElement19.removeElement(xmlElement27);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + ""+ "'", str6.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str17 + "' != '" + "hi!"+ "'", str17.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str21 + "' != '" + "hi!"+ "'", str21.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement31);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str39 + "' != '" + ""+ "'", str39.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b43 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b49 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b53 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement54);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement61);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement62);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement67);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement68);

  }

  @Test
  public void test103() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test103"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.util.Observer observer3 = null;
    xmlElement1.deleteObserver(observer3);
    xmlElement1.notifyObservers();
    java.lang.String str8 = xmlElement1.getAttribute("hi!", "hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str8 + "' != '" + "hi!"+ "'", str8.equals("hi!"));

  }

  @Test
  public void test104() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test104"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.lang.String str4 = xmlElement1.getAttribute("hi!");
    int i5 = xmlElement1.count();
    xmlElement1.deleteObservers();
    java.lang.String str8 = xmlElement1.getAttribute("");
    XmlElement xmlElement11 = xmlElement1.addSubElement("", "hi!");
    XmlElement xmlElement12 = xmlElement1.getParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement12);

  }

  @Test
  public void test105() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test105"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Observer observer2 = null;
    xmlElement1.deleteObserver(observer2);
    java.lang.String str6 = xmlElement1.getAttribute("", "");
    XmlElement xmlElement8 = new XmlElement("hi!");
    boolean b10 = xmlElement8.equals((java.lang.Object)"hi!");
    xmlElement1.append(xmlElement8);
    xmlElement1.deleteObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("hi!", hashtable_str_str14);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str17 = null;
    XmlElement xmlElement18 = new XmlElement("hi!", hashtable_str_str17);
    XmlElement xmlElement19 = xmlElement15.removeElement(xmlElement18);
    xmlElement15.removeAllElements();
    XmlElement xmlElement22 = new XmlElement("hi!");
    java.util.Observer observer23 = null;
    xmlElement22.deleteObserver(observer23);
    java.lang.String str27 = xmlElement22.getAttribute("", "");
    XmlElement xmlElement29 = new XmlElement("hi!");
    boolean b31 = xmlElement29.equals((java.lang.Object)"hi!");
    xmlElement22.append(xmlElement29);
    xmlElement15.setParent(xmlElement22);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str35 = null;
    XmlElement xmlElement36 = new XmlElement("hi!", hashtable_str_str35);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str38 = null;
    XmlElement xmlElement39 = new XmlElement("hi!", hashtable_str_str38);
    XmlElement xmlElement40 = xmlElement36.removeElement(xmlElement39);
    java.util.Observer observer41 = null;
    xmlElement36.deleteObserver(observer41);
    XmlElement xmlElement43 = xmlElement15.removeElement(xmlElement36);
    xmlElement1.append(xmlElement15);
    XmlElement xmlElement46 = xmlElement1.addSubElement("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + ""+ "'", str6.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str27 + "' != '" + ""+ "'", str27.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b31 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement40);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement43);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement46);

  }

  @Test
  public void test106() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test106"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    xmlElement4.deleteObservers();
    XmlElement xmlElement11 = new XmlElement("hi!");
    java.util.Enumeration enumeration12 = xmlElement11.getAttributeNames();
    XmlElement xmlElement14 = new XmlElement("hi!");
    java.util.Enumeration enumeration15 = xmlElement14.getAttributeNames();
    java.lang.String str17 = xmlElement14.getAttribute("hi!");
    XmlElement xmlElement18 = xmlElement11.removeElement(xmlElement14);
    xmlElement4.notifyObservers((java.lang.Object)xmlElement14);
    boolean b20 = xmlElement4.hasChanged();
    XmlElement.printNode(xmlElement4, "hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b20 == false);

  }

  @Test
  public void test107() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test107"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Observer observer2 = null;
    xmlElement1.deleteObserver(observer2);
    java.lang.String str6 = xmlElement1.getAttribute("", "");
    XmlElement xmlElement8 = new XmlElement("hi!");
    boolean b10 = xmlElement8.equals((java.lang.Object)"hi!");
    xmlElement1.append(xmlElement8);
    XmlElement xmlElement14 = xmlElement8.addSubElement("hi!", "hi!");
    XmlElement xmlElement17 = new XmlElement("", "");
    java.util.Observer observer18 = null;
    xmlElement17.deleteObserver(observer18);
    xmlElement8.notifyObservers((java.lang.Object)observer18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str6 + "' != '" + ""+ "'", str6.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement14);

  }

  @Test
  public void test108() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test108"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    boolean b3 = xmlElement1.equals((java.lang.Object)"hi!");
    XmlElement xmlElement5 = new XmlElement("hi!");
    boolean b7 = xmlElement5.equals((java.lang.Object)"hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.removeFromParent();
    XmlElement xmlElement15 = xmlElement11.addSubElement("hi!", "hi!");
    XmlElement xmlElement16 = xmlElement5.addSubElement(xmlElement15);
    XmlElement xmlElement19 = new XmlElement("hi!", "hi!");
    xmlElement5.notifyObservers((java.lang.Object)xmlElement19);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str21 = xmlElement19.getAttributes();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str21);

  }

  @Test
  public void test109() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test109"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    boolean b3 = xmlElement1.equals((java.lang.Object)"hi!");
    XmlElement xmlElement5 = new XmlElement("hi!");
    boolean b7 = xmlElement5.equals((java.lang.Object)"hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement5);
    java.lang.String str10 = xmlElement1.getAttribute("");
    java.util.Observer observer11 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement1.addObserver(observer11);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str10);

  }

  @Test
  public void test110() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test110"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.lang.Object obj2 = xmlElement1.clone();
    java.lang.Object obj3 = null;
    boolean b4 = xmlElement1.equals(obj3);
    XmlElement xmlElement7 = new XmlElement("", "hi!");
    XmlElement xmlElement8 = xmlElement1.addSubElement(xmlElement7);
    java.util.Observer observer9 = null;
    xmlElement8.deleteObserver(observer9);
    int i11 = xmlElement8.count();
    XmlElement xmlElement13 = new XmlElement("hi!");
    boolean b15 = xmlElement13.equals((java.lang.Object)"hi!");
    XmlElement xmlElement17 = new XmlElement("hi!");
    boolean b19 = xmlElement17.equals((java.lang.Object)"hi!");
    XmlElement xmlElement20 = xmlElement13.removeElement(xmlElement17);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str22 = null;
    XmlElement xmlElement23 = new XmlElement("hi!", hashtable_str_str22);
    xmlElement23.removeFromParent();
    XmlElement xmlElement27 = xmlElement23.addSubElement("hi!", "hi!");
    XmlElement xmlElement28 = xmlElement17.addSubElement(xmlElement27);
    java.util.List list29 = xmlElement28.getElements();
    xmlElement8.notifyObservers((java.lang.Object)xmlElement28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i11 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b19 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement27);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list29);

  }

  @Test
  public void test111() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test111"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    boolean b3 = xmlElement1.hasChanged();
    boolean b5 = xmlElement1.equals((java.lang.Object)'4');
    XmlElement xmlElement7 = new XmlElement("hi!");
    java.util.Observer observer8 = null;
    xmlElement7.deleteObserver(observer8);
    XmlElement xmlElement11 = new XmlElement("hi!");
    java.util.Enumeration enumeration12 = xmlElement11.getAttributeNames();
    XmlElement xmlElement14 = new XmlElement("hi!");
    java.util.Enumeration enumeration15 = xmlElement14.getAttributeNames();
    java.lang.String str17 = xmlElement14.getAttribute("hi!");
    XmlElement xmlElement18 = xmlElement11.removeElement(xmlElement14);
    boolean b19 = xmlElement7.addElement(xmlElement11);
    xmlElement1.notifyObservers((java.lang.Object)b19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b19 == true);

  }

  @Test
  public void test112() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test112"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    boolean b3 = xmlElement1.equals((java.lang.Object)"hi!");
    XmlElement xmlElement5 = new XmlElement("hi!");
    boolean b7 = xmlElement5.equals((java.lang.Object)"hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("hi!", hashtable_str_str10);
    xmlElement11.removeFromParent();
    XmlElement xmlElement15 = xmlElement11.addSubElement("hi!", "hi!");
    XmlElement xmlElement16 = xmlElement5.addSubElement(xmlElement15);
    XmlElement xmlElement18 = new XmlElement("hi!");
    java.util.Enumeration enumeration19 = xmlElement18.getAttributeNames();
    java.util.Observer observer20 = null;
    xmlElement18.deleteObserver(observer20);
    xmlElement18.notifyObservers();
    int i23 = xmlElement18.countObservers();
    XmlElement xmlElement26 = new XmlElement("hi!");
    java.util.Enumeration enumeration27 = xmlElement26.getAttributeNames();
    boolean b28 = xmlElement26.hasChanged();
    boolean b30 = xmlElement26.equals((java.lang.Object)'4');
    XmlElement xmlElement32 = new XmlElement("hi!");
    java.util.Enumeration enumeration33 = xmlElement32.getAttributeNames();
    java.lang.String str35 = xmlElement32.getAttribute("hi!");
    int i36 = xmlElement32.countObservers();
    java.util.List list37 = xmlElement32.getElements();
    boolean b38 = xmlElement26.equals((java.lang.Object)xmlElement32);
    XmlElement xmlElement40 = new XmlElement("hi!");
    boolean b42 = xmlElement40.equals((java.lang.Object)"hi!");
    XmlElement xmlElement44 = new XmlElement("hi!");
    boolean b46 = xmlElement44.equals((java.lang.Object)"hi!");
    XmlElement xmlElement47 = xmlElement40.removeElement(xmlElement44);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str49 = null;
    XmlElement xmlElement50 = new XmlElement("hi!", hashtable_str_str49);
    xmlElement50.removeFromParent();
    XmlElement xmlElement54 = xmlElement50.addSubElement("hi!", "hi!");
    XmlElement xmlElement55 = xmlElement44.addSubElement(xmlElement54);
    XmlElement xmlElement57 = new XmlElement("hi!");
    boolean b59 = xmlElement57.equals((java.lang.Object)"hi!");
    XmlElement xmlElement61 = new XmlElement("hi!");
    boolean b63 = xmlElement61.equals((java.lang.Object)"hi!");
    XmlElement xmlElement64 = xmlElement57.removeElement(xmlElement61);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str65 = xmlElement57.getAttributes();
    xmlElement55.setAttributes(hashtable_str_str65);
    xmlElement32.setAttributes(hashtable_str_str65);
    XmlElement xmlElement68 = new XmlElement("", hashtable_str_str65);
    boolean b69 = xmlElement18.addElement(xmlElement68);
    xmlElement15.notifyObservers((java.lang.Object)b69);
    java.lang.Object obj73 = xmlElement15.addAttribute("hi!", "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i23 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration27);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b28 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b30 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration33);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str35);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i36 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b38 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b42 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b46 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement47);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement54);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement55);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b59 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b63 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement64);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str65);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b69 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj73);

  }

  @Test
  public void test113() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test113"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.lang.String str4 = xmlElement1.getAttribute("hi!");
    int i5 = xmlElement1.count();
    XmlElement xmlElement7 = new XmlElement("hi!");
    java.util.Enumeration enumeration8 = xmlElement7.getAttributeNames();
    boolean b9 = xmlElement7.hasChanged();
    java.lang.Object obj10 = xmlElement7.clone();
    xmlElement7.deleteObservers();
    xmlElement1.setParent(xmlElement7);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("hi!", hashtable_str_str14);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str17 = null;
    XmlElement xmlElement18 = new XmlElement("hi!", hashtable_str_str17);
    XmlElement xmlElement19 = xmlElement15.removeElement(xmlElement18);
    xmlElement15.removeAllElements();
    XmlElement xmlElement22 = new XmlElement("hi!");
    boolean b24 = xmlElement22.equals((java.lang.Object)"hi!");
    XmlElement xmlElement26 = new XmlElement("hi!");
    boolean b28 = xmlElement26.equals((java.lang.Object)"hi!");
    XmlElement xmlElement29 = xmlElement22.removeElement(xmlElement26);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str31 = null;
    XmlElement xmlElement32 = new XmlElement("hi!", hashtable_str_str31);
    xmlElement32.removeFromParent();
    XmlElement xmlElement36 = xmlElement32.addSubElement("hi!", "hi!");
    XmlElement xmlElement37 = xmlElement26.addSubElement(xmlElement36);
    xmlElement15.setParent(xmlElement37);
    xmlElement1.setParent(xmlElement15);
    XmlElement xmlElement40 = xmlElement1.getParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b24 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b28 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement36);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement40);

  }

  @Test
  public void test114() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test114"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    java.util.Enumeration enumeration9 = xmlElement1.getAttributeNames();
    java.util.Enumeration enumeration10 = xmlElement1.getAttributeNames();
    xmlElement1.notifyObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration10);

  }

  @Test
  public void test115() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test115"); }


    XmlElement xmlElement2 = new XmlElement("", "hi!");
    XmlElement xmlElement4 = new XmlElement("hi!");
    xmlElement2.append(xmlElement4);
    java.lang.String str7 = xmlElement2.getAttribute("");
    XmlElement xmlElement9 = new XmlElement("hi!");
    java.util.Enumeration enumeration10 = xmlElement9.getAttributeNames();
    java.lang.String str12 = xmlElement9.getAttribute("hi!");
    int i13 = xmlElement9.count();
    boolean b15 = xmlElement9.equals((java.lang.Object)true);
    XmlElement xmlElement16 = xmlElement2.addSubElement(xmlElement9);
    XmlElement xmlElement18 = xmlElement2.addSubElement("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement18);

  }

  @Test
  public void test116() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test116"); }


    XmlElement xmlElement2 = new XmlElement("hi!");
    boolean b4 = xmlElement2.equals((java.lang.Object)"hi!");
    XmlElement xmlElement6 = new XmlElement("hi!");
    boolean b8 = xmlElement6.equals((java.lang.Object)"hi!");
    XmlElement xmlElement9 = xmlElement2.removeElement(xmlElement6);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = xmlElement2.getAttributes();
    XmlElement xmlElement11 = new XmlElement("", hashtable_str_str10);
    xmlElement11.notifyObservers();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement14 = xmlElement11.removeElement((int)(byte)100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str10);

  }

  @Test
  public void test117() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test117"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    XmlElement xmlElement6 = xmlElement2.removeElement(xmlElement5);
    xmlElement2.removeAllElements();
    XmlElement xmlElement9 = new XmlElement("hi!");
    java.util.Observer observer10 = null;
    xmlElement9.deleteObserver(observer10);
    java.lang.String str14 = xmlElement9.getAttribute("", "");
    XmlElement xmlElement16 = new XmlElement("hi!");
    boolean b18 = xmlElement16.equals((java.lang.Object)"hi!");
    xmlElement9.append(xmlElement16);
    xmlElement2.setParent(xmlElement9);
    xmlElement2.setName("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str14 + "' != '" + ""+ "'", str14.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b18 == false);

  }

  @Test
  public void test118() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test118"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.lang.String str4 = xmlElement1.getAttribute("hi!");
    int i5 = xmlElement1.count();
    xmlElement1.deleteObservers();
    boolean b7 = xmlElement1.hasChanged();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test119() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test119"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    java.lang.String str3 = xmlElement1.getName();
    xmlElement1.removeAllElements();
    java.lang.Object obj7 = xmlElement1.addAttribute("hi!", "hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + "hi!"+ "'", str3.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj7);

  }

  @Test
  public void test120() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test120"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    boolean b3 = xmlElement1.hasChanged();
    XmlElement.printNode(xmlElement1, "hi!");
    java.util.Observer observer6 = null;
    xmlElement1.deleteObserver(observer6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);

  }

  @Test
  public void test121() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test121"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.lang.Object obj2 = xmlElement1.clone();
    XmlElement xmlElement5 = new XmlElement("", "hi!");
    XmlElement xmlElement7 = new XmlElement("hi!");
    xmlElement5.append(xmlElement7);
    java.lang.String str10 = xmlElement5.getAttribute("");
    XmlElement xmlElement12 = new XmlElement("hi!");
    java.util.Enumeration enumeration13 = xmlElement12.getAttributeNames();
    java.lang.String str15 = xmlElement12.getAttribute("hi!");
    int i16 = xmlElement12.count();
    boolean b18 = xmlElement12.equals((java.lang.Object)true);
    XmlElement xmlElement19 = xmlElement5.addSubElement(xmlElement12);
    xmlElement1.notifyObservers((java.lang.Object)xmlElement19);
    java.util.Observer observer21 = null;
    xmlElement19.deleteObserver(observer21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i16 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b18 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement19);

  }

  @Test
  public void test122() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test122"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = new XmlElement("hi!");
    java.util.Enumeration enumeration5 = xmlElement4.getAttributeNames();
    java.lang.String str7 = xmlElement4.getAttribute("hi!");
    XmlElement xmlElement8 = xmlElement1.removeElement(xmlElement4);
    xmlElement4.deleteObservers();
    XmlElement xmlElement11 = xmlElement4.addSubElement("");
    XmlElement.printNode(xmlElement11, "hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement11);

  }

  @Test
  public void test123() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test123"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    boolean b3 = xmlElement1.hasChanged();
    boolean b5 = xmlElement1.equals((java.lang.Object)'4');
    java.lang.Object obj8 = xmlElement1.addAttribute("hi!", "");
    XmlElement.printNode(xmlElement1, "hi!");
    xmlElement1.notifyObservers();
    boolean b13 = xmlElement1.equals((java.lang.Object)(short)100);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b3 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);

  }

  @Test
  public void test124() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test124"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    XmlElement xmlElement6 = xmlElement2.removeElement(xmlElement5);
    xmlElement2.removeAllElements();
    XmlElement xmlElement9 = new XmlElement("hi!");
    java.util.Observer observer10 = null;
    xmlElement9.deleteObserver(observer10);
    java.lang.String str14 = xmlElement9.getAttribute("", "");
    XmlElement xmlElement16 = new XmlElement("hi!");
    boolean b18 = xmlElement16.equals((java.lang.Object)"hi!");
    xmlElement9.append(xmlElement16);
    xmlElement2.setParent(xmlElement9);
    XmlElement xmlElement22 = new XmlElement("hi!");
    boolean b24 = xmlElement22.equals((java.lang.Object)"hi!");
    XmlElement xmlElement26 = new XmlElement("hi!");
    boolean b28 = xmlElement26.equals((java.lang.Object)"hi!");
    XmlElement xmlElement29 = xmlElement22.removeElement(xmlElement26);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str31 = null;
    XmlElement xmlElement32 = new XmlElement("hi!", hashtable_str_str31);
    xmlElement32.removeFromParent();
    XmlElement xmlElement36 = xmlElement32.addSubElement("hi!", "hi!");
    XmlElement xmlElement37 = xmlElement26.addSubElement(xmlElement36);
    XmlElement xmlElement40 = new XmlElement("hi!", "hi!");
    xmlElement26.notifyObservers((java.lang.Object)xmlElement40);
    XmlElement xmlElement42 = xmlElement2.addSubElement(xmlElement40);
    xmlElement42.setData("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str14 + "' != '" + ""+ "'", str14.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b18 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b24 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b28 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement36);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement42);

  }

}
