package mypackage;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0_it4_livello5 {

  public static boolean debug = false;

  @Test
  public void test001() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test001"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    XmlElement xmlElement3 = xmlElement1.getParent();
    XmlElement xmlElement5 = new XmlElement("hi!");
    XmlElement xmlElement8 = xmlElement5.addSubElement("hi!", "hi!");
    // The following exception was thrown during execution in test generation
    try {
    boolean b9 = xmlElement3.addElement(xmlElement5);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement8);

  }

  @Test
  public void test002() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test002"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    XmlElement xmlElement3 = xmlElement1.getParent();
    // The following exception was thrown during execution in test generation
    try {
    xmlElement3.removeAllElements();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement3);

  }

  @Test
  public void test003() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test003"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement xmlElement6 = xmlElement4.getElement("");
    // The following exception was thrown during execution in test generation
    try {
    xmlElement6.notifyObservers();
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
    org.junit.Assert.assertNull(xmlElement6);

  }

  @Test
  public void test004() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test004"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement xmlElement6 = xmlElement4.getElement("");
    // The following exception was thrown during execution in test generation
    try {
    int i7 = xmlElement6.countObservers();
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
    org.junit.Assert.assertNull(xmlElement6);

  }

  @Test
  public void test005() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test005"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement5 = xmlElement1.removeElement((int)'a');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);

  }

  @Test
  public void test006() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test006"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement3 = new XmlElement("hi!");
    int i4 = xmlElement3.countObservers();
    java.lang.Object obj5 = xmlElement3.clone();
    XmlElement xmlElement7 = new XmlElement("hi!");
    XmlElement xmlElement10 = xmlElement7.addSubElement("hi!", "hi!");
    java.util.Observer observer11 = null;
    xmlElement10.deleteObserver(observer11);
    java.lang.String str13 = xmlElement10.getName();
    XmlElement xmlElement14 = xmlElement3.removeElement(xmlElement10);
    // The following exception was thrown during execution in test generation
    try {
    xmlElement1.insertElement(xmlElement14, 0);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i4 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str13 + "' != '" + "hi!"+ "'", str13.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);

  }

  @Test
  public void test007() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test007"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement3 = xmlElement1.removeElement((int)(short)10);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test008() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test008"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj3 = xmlElement1.clone();
    XmlElement xmlElement5 = new XmlElement("hi!");
    XmlElement xmlElement8 = xmlElement5.addSubElement("hi!", "hi!");
    java.util.Observer observer9 = null;
    xmlElement8.deleteObserver(observer9);
    java.lang.String str11 = xmlElement8.getName();
    XmlElement xmlElement12 = xmlElement1.removeElement(xmlElement8);
    boolean b14 = xmlElement1.equals((java.lang.Object)10L);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str11 + "' != '" + "hi!"+ "'", str11.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == false);

  }

  @Test
  public void test009() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test009"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj3 = xmlElement1.clone();
    XmlElement xmlElement5 = new XmlElement("hi!");
    XmlElement xmlElement8 = xmlElement5.addSubElement("hi!", "hi!");
    java.util.Observer observer9 = null;
    xmlElement8.deleteObserver(observer9);
    java.lang.String str11 = xmlElement8.getName();
    XmlElement xmlElement12 = xmlElement1.removeElement(xmlElement8);
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str15 = xmlElement12.getAttribute("hi!", "");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str11 + "' != '" + "hi!"+ "'", str11.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement12);

  }

  @Test
  public void test010() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test010"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = xmlElement1.addSubElement("");
    XmlElement xmlElement8 = new XmlElement("hi!");
    XmlElement xmlElement11 = xmlElement8.addSubElement("hi!", "hi!");
    java.util.Observer observer12 = null;
    xmlElement11.deleteObserver(observer12);
    XmlElement xmlElement14 = xmlElement1.removeElement(xmlElement11);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement16 = xmlElement14.getElement(10);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement14);

  }

  @Test
  public void test011() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test011"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    java.util.Observer observer4 = null;
    xmlElement1.deleteObserver(observer4);
    xmlElement1.removeAllElements();
    XmlElement.printNode(xmlElement1, "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);

  }

  @Test
  public void test012() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test012"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement xmlElement6 = new XmlElement("hi!");
    int i7 = xmlElement6.countObservers();
    XmlElement.printNode(xmlElement6, "hi!");
    XmlElement xmlElement10 = xmlElement4.removeElement(xmlElement6);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement12 = xmlElement10.getElement("hi!");
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
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement10);

  }

  @Test
  public void test013() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test013"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement.printNode(xmlElement4, "");
    XmlElement xmlElement8 = new XmlElement("hi!");
    int i9 = xmlElement8.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = xmlElement8.getAttributes();
    xmlElement4.setAttributes(hashtable_str_str10);
    java.util.Enumeration enumeration12 = xmlElement4.getAttributeNames();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i9 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration12);

  }

  @Test
  public void test014() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test014"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Enumeration enumeration3 = xmlElement1.getAttributeNames();
    XmlElement.printNode(xmlElement1, "");
    XmlElement xmlElement7 = new XmlElement("hi!");
    java.util.Observer observer8 = null;
    xmlElement7.deleteObserver(observer8);
    xmlElement7.setName("");
    XmlElement xmlElement13 = new XmlElement("hi!");
    int i14 = xmlElement13.countObservers();
    java.util.Enumeration enumeration15 = xmlElement13.getAttributeNames();
    xmlElement7.insertElement(xmlElement13, (int)(byte)0);
    XmlElement xmlElement20 = xmlElement7.addSubElement("hi!", "");
    // The following exception was thrown during execution in test generation
    try {
    xmlElement1.insertElement(xmlElement7, 10);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i14 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement20);

  }

  @Test
  public void test015() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test015"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    int i4 = xmlElement1.count();
    boolean b6 = xmlElement1.equals((java.lang.Object)1.0f);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement8 = xmlElement1.removeElement((-1));
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i4 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);

  }

  @Test
  public void test016() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test016"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement xmlElement6 = xmlElement4.getElement("");
    // The following exception was thrown during execution in test generation
    try {
    java.util.List list7 = xmlElement6.getElements();
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
    org.junit.Assert.assertNull(xmlElement6);

  }

  @Test
  public void test017() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test017"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    XmlElement xmlElement3 = xmlElement1.getParent();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement5 = xmlElement3.getElement((int)' ');
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement3);

  }

  @Test
  public void test018() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test018"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = new XmlElement("hi!");
    int i7 = xmlElement6.countObservers();
    XmlElement.printNode(xmlElement6, "hi!");
    java.util.List list10 = xmlElement6.getElements();
    boolean b11 = xmlElement1.addElement(xmlElement6);
    xmlElement1.removeFromParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == true);

  }

  @Test
  public void test019() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test019"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement xmlElement6 = xmlElement4.getElement("");
    // The following exception was thrown during execution in test generation
    try {
    java.util.Enumeration enumeration7 = xmlElement6.getAttributeNames();
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
    org.junit.Assert.assertNull(xmlElement6);

  }

  @Test
  public void test020() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test020"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    java.util.Observer observer4 = null;
    xmlElement1.deleteObserver(observer4);
    XmlElement xmlElement7 = new XmlElement("hi!");
    int i8 = xmlElement7.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = xmlElement7.getAttributes();
    XmlElement xmlElement11 = new XmlElement("hi!");
    int i12 = xmlElement11.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str13 = xmlElement11.getAttributes();
    boolean b14 = xmlElement7.addElement(xmlElement11);
    xmlElement1.append(xmlElement11);
    xmlElement11.setData("hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str18 = xmlElement11.getAttributes();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i12 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str18);

  }

  @Test
  public void test021() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test021"); }


    XmlElement xmlElement0 = new XmlElement();
    java.lang.String str2 = xmlElement0.getAttribute("");
    java.lang.Object obj3 = xmlElement0.clone();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);

  }

  @Test
  public void test022() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test022"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    XmlElement.printNode(xmlElement1, "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);

  }

  @Test
  public void test023() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test023"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Enumeration enumeration3 = xmlElement1.getAttributeNames();
    XmlElement xmlElement4 = xmlElement1.getParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement4);

  }

  @Test
  public void test024() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test024"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    XmlElement xmlElement3 = xmlElement1.getParent();
    // The following exception was thrown during execution in test generation
    try {
    java.util.List list4 = xmlElement3.getElements();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement3);

  }

  @Test
  public void test025() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test025"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    XmlElement.printNode(xmlElement1, "hi!");
    xmlElement1.notifyObservers((java.lang.Object)(short)10);
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
    org.junit.Assert.assertTrue(i2 == 0);

  }

  @Test
  public void test026() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test026"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    XmlElement xmlElement5 = new XmlElement("hi!");
    int i6 = xmlElement5.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = xmlElement5.getAttributes();
    boolean b8 = xmlElement1.addElement(xmlElement5);
    XmlElement xmlElement10 = new XmlElement("hi!");
    XmlElement xmlElement13 = xmlElement10.addSubElement("hi!", "hi!");
    java.util.Observer observer14 = null;
    xmlElement13.deleteObserver(observer14);
    java.lang.String str16 = xmlElement13.getName();
    XmlElement xmlElement18 = new XmlElement("hi!");
    int i19 = xmlElement18.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str20 = xmlElement18.getAttributes();
    xmlElement13.setAttributes(hashtable_str_str20);
    boolean b22 = xmlElement5.addElement(xmlElement13);
    XmlElement xmlElement24 = new XmlElement("hi!");
    java.util.Observer observer25 = null;
    xmlElement24.deleteObserver(observer25);
    xmlElement24.setName("");
    XmlElement xmlElement30 = new XmlElement("hi!");
    int i31 = xmlElement30.countObservers();
    java.util.Enumeration enumeration32 = xmlElement30.getAttributeNames();
    xmlElement24.insertElement(xmlElement30, (int)(byte)0);
    xmlElement13.append(xmlElement30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str16 + "' != '" + "hi!"+ "'", str16.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i19 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b22 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i31 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration32);

  }

  @Test
  public void test027() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test027"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    XmlElement xmlElement5 = new XmlElement("hi!");
    int i6 = xmlElement5.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = xmlElement5.getAttributes();
    boolean b8 = xmlElement1.addElement(xmlElement5);
    XmlElement xmlElement10 = new XmlElement("hi!");
    XmlElement xmlElement13 = xmlElement10.addSubElement("hi!", "hi!");
    java.util.Observer observer14 = null;
    xmlElement13.deleteObserver(observer14);
    java.lang.String str16 = xmlElement13.getName();
    XmlElement xmlElement18 = new XmlElement("hi!");
    int i19 = xmlElement18.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str20 = xmlElement18.getAttributes();
    xmlElement13.setAttributes(hashtable_str_str20);
    boolean b22 = xmlElement5.addElement(xmlElement13);
    java.util.Enumeration enumeration23 = xmlElement5.getAttributeNames();
    java.lang.String str26 = xmlElement5.getAttribute("", "hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str16 + "' != '" + "hi!"+ "'", str16.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i19 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b22 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str26 + "' != '" + "hi!"+ "'", str26.equals("hi!"));

  }

  @Test
  public void test028() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test028"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement xmlElement6 = xmlElement4.getElement("");
    XmlElement xmlElement8 = new XmlElement("hi!");
    int i9 = xmlElement8.countObservers();
    java.lang.Object obj10 = xmlElement8.clone();
    XmlElement xmlElement11 = xmlElement4.addSubElement(xmlElement8);
    xmlElement4.removeFromParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i9 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement11);

  }

  @Test
  public void test029() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test029"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Enumeration enumeration3 = xmlElement1.getAttributeNames();
    XmlElement.printNode(xmlElement1, "");
    boolean b6 = xmlElement1.hasChanged();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);

  }

  @Test
  public void test030() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test030"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Enumeration enumeration3 = xmlElement1.getAttributeNames();
    XmlElement.printNode(xmlElement1, "");
    int i6 = xmlElement1.countObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);

  }

  @Test
  public void test031() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test031"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    boolean b5 = xmlElement1.hasChanged();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);

  }

  @Test
  public void test032() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test032"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    int i4 = xmlElement1.count();
    boolean b6 = xmlElement1.equals((java.lang.Object)1.0f);
    XmlElement xmlElement8 = new XmlElement("hi!");
    int i9 = xmlElement8.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = xmlElement8.getAttributes();
    boolean b11 = xmlElement8.hasChanged();
    XmlElement xmlElement13 = xmlElement8.addSubElement("");
    XmlElement xmlElement14 = xmlElement1.removeElement(xmlElement8);
    XmlElement xmlElement15 = null;
    // The following exception was thrown during execution in test generation
    try {
    boolean b16 = xmlElement14.addElement(xmlElement15);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i4 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i9 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);

  }

  @Test
  public void test033() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test033"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = xmlElement1.addSubElement("");
    java.lang.String str8 = xmlElement6.getAttribute("");
    XmlElement xmlElement9 = new XmlElement();
    XmlElement xmlElement11 = new XmlElement("hi!");
    java.util.Observer observer12 = null;
    xmlElement11.deleteObserver(observer12);
    boolean b15 = xmlElement11.equals((java.lang.Object)100L);
    xmlElement9.setParent(xmlElement11);
    XmlElement xmlElement19 = new XmlElement("hi!", "hi!");
    boolean b20 = xmlElement9.equals((java.lang.Object)"hi!");
    boolean b21 = xmlElement6.addElement(xmlElement9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b20 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == true);

  }

  @Test
  public void test034() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test034"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = xmlElement1.addSubElement("");
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement8 = xmlElement6.removeElement((int)' ');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);

  }

  @Test
  public void test035() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test035"); }


    XmlElement xmlElement0 = new XmlElement();
    XmlElement xmlElement2 = new XmlElement("hi!");
    java.util.Observer observer3 = null;
    xmlElement2.deleteObserver(observer3);
    boolean b6 = xmlElement2.equals((java.lang.Object)100L);
    xmlElement0.setParent(xmlElement2);
    java.lang.Object obj10 = xmlElement2.addAttribute("", "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj10);

  }

  @Test
  public void test036() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test036"); }


    XmlElement xmlElement2 = new XmlElement("", "hi!");
    int i3 = xmlElement2.countObservers();
    java.lang.String str4 = xmlElement2.getData();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i3 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str4 + "' != '" + "hi!"+ "'", str4.equals("hi!"));

  }

  @Test
  public void test037() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test037"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    int i4 = xmlElement1.count();
    XmlElement xmlElement6 = xmlElement1.addSubElement("hi!");
    int i7 = xmlElement6.countObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i4 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);

  }

  @Test
  public void test038() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test038"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    java.util.Observer observer4 = null;
    xmlElement1.deleteObserver(observer4);
    XmlElement xmlElement7 = new XmlElement("hi!");
    int i8 = xmlElement7.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = xmlElement7.getAttributes();
    XmlElement xmlElement11 = new XmlElement("hi!");
    int i12 = xmlElement11.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str13 = xmlElement11.getAttributes();
    boolean b14 = xmlElement7.addElement(xmlElement11);
    xmlElement1.append(xmlElement11);
    int i16 = xmlElement11.count();
    java.lang.String str17 = xmlElement11.getData();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i12 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i16 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str17 + "' != '" + ""+ "'", str17.equals(""));

  }

  @Test
  public void test039() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test039"); }


    XmlElement xmlElement0 = new XmlElement();
    java.lang.String str2 = xmlElement0.getAttribute("");
    XmlElement xmlElement5 = new XmlElement("hi!", "hi!");
    xmlElement0.insertElement(xmlElement5, 0);
    int i8 = xmlElement5.count();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 0);

  }

  @Test
  public void test040() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test040"); }


    XmlElement xmlElement0 = new XmlElement();
    XmlElement xmlElement2 = new XmlElement("hi!");
    java.util.Observer observer3 = null;
    xmlElement2.deleteObserver(observer3);
    boolean b6 = xmlElement2.equals((java.lang.Object)100L);
    xmlElement0.setParent(xmlElement2);
    xmlElement0.removeAllElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);

  }

  @Test
  public void test041() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test041"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = xmlElement1.addSubElement("");
    xmlElement6.setData("hi!");
    XmlElement xmlElement10 = new XmlElement("hi!");
    int i11 = xmlElement10.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = xmlElement10.getAttributes();
    boolean b13 = xmlElement10.hasChanged();
    XmlElement xmlElement15 = xmlElement10.addSubElement("");
    XmlElement xmlElement17 = new XmlElement("hi!");
    XmlElement xmlElement20 = xmlElement17.addSubElement("hi!", "hi!");
    java.util.Observer observer21 = null;
    xmlElement20.deleteObserver(observer21);
    XmlElement xmlElement23 = xmlElement10.removeElement(xmlElement20);
    boolean b24 = xmlElement6.equals((java.lang.Object)xmlElement10);
    XmlElement xmlElement26 = xmlElement10.getElement("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i11 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b24 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement26);

  }

  @Test
  public void test042() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test042"); }


    XmlElement xmlElement0 = new XmlElement();
    xmlElement0.removeFromParent();

  }

  @Test
  public void test043() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test043"); }


    XmlElement xmlElement0 = new XmlElement();
    java.lang.String str2 = xmlElement0.getAttribute("");
    XmlElement xmlElement5 = new XmlElement("hi!", "hi!");
    xmlElement0.insertElement(xmlElement5, 0);
    int i8 = xmlElement0.count();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 1);

  }

  @Test
  public void test044() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test044"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj3 = xmlElement1.clone();
    XmlElement xmlElement5 = new XmlElement("hi!");
    XmlElement xmlElement8 = xmlElement5.addSubElement("hi!", "hi!");
    java.util.Observer observer9 = null;
    xmlElement8.deleteObserver(observer9);
    java.lang.String str11 = xmlElement8.getName();
    XmlElement xmlElement12 = xmlElement1.removeElement(xmlElement8);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement.printNode(xmlElement12, "");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str11 + "' != '" + "hi!"+ "'", str11.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement12);

  }

  @Test
  public void test045() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test045"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    java.util.Observer observer5 = null;
    xmlElement4.deleteObserver(observer5);
    java.lang.String str7 = xmlElement4.getName();
    java.lang.Object obj8 = xmlElement4.clone();
    XmlElement xmlElement10 = xmlElement4.getElement("hi!");
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str11 = xmlElement10.getName();
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
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement10);

  }

  @Test
  public void test046() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test046"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    java.util.Observer observer4 = null;
    xmlElement1.deleteObserver(observer4);
    java.util.List list6 = xmlElement1.getElements();
    java.lang.String str7 = xmlElement1.getName();
    java.util.List list8 = xmlElement1.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = xmlElement1.getAttributes();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str9);

  }

  @Test
  public void test047() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test047"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = new XmlElement("hi!");
    int i7 = xmlElement6.countObservers();
    XmlElement.printNode(xmlElement6, "hi!");
    java.util.List list10 = xmlElement6.getElements();
    boolean b11 = xmlElement1.addElement(xmlElement6);
    java.util.List list12 = xmlElement6.getElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list12);

  }

  @Test
  public void test048() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test048"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    java.util.Observer observer5 = null;
    xmlElement4.deleteObserver(observer5);
    java.util.Observer observer7 = null;
    xmlElement4.deleteObserver(observer7);
    xmlElement4.setData("");
    java.util.List list11 = xmlElement4.getElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list11);

  }

  @Test
  public void test049() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test049"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = xmlElement1.addSubElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = xmlElement1.getAttributes();
    XmlElement xmlElement9 = new XmlElement("hi!");
    int i10 = xmlElement9.countObservers();
    java.lang.Object obj13 = xmlElement9.addAttribute("hi!", "");
    boolean b14 = xmlElement1.addElement(xmlElement9);
    boolean b15 = xmlElement9.hasChanged();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);

  }

  @Test
  public void test050() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test050"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = xmlElement1.addSubElement("");
    XmlElement xmlElement8 = new XmlElement("hi!");
    XmlElement xmlElement11 = xmlElement8.addSubElement("hi!", "hi!");
    java.util.Observer observer12 = null;
    xmlElement11.deleteObserver(observer12);
    XmlElement xmlElement14 = xmlElement1.removeElement(xmlElement11);
    java.lang.Object obj17 = xmlElement1.addAttribute("", "");
    xmlElement1.notifyObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj17);

  }

  @Test
  public void test051() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test051"); }


    XmlElement xmlElement2 = new XmlElement("hi!");
    int i3 = xmlElement2.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = xmlElement2.getAttributes();
    boolean b5 = xmlElement2.hasChanged();
    XmlElement xmlElement7 = xmlElement2.addSubElement("");
    XmlElement xmlElement9 = new XmlElement("hi!");
    int i10 = xmlElement9.countObservers();
    java.lang.Object obj13 = xmlElement9.addAttribute("hi!", "");
    XmlElement xmlElement15 = new XmlElement("hi!");
    int i16 = xmlElement15.countObservers();
    java.util.Enumeration enumeration17 = xmlElement15.getAttributeNames();
    XmlElement.printNode(xmlElement15, "");
    boolean b20 = xmlElement9.addElement(xmlElement15);
    XmlElement xmlElement22 = new XmlElement("hi!");
    XmlElement xmlElement25 = xmlElement22.addSubElement("hi!", "hi!");
    XmlElement.printNode(xmlElement25, "");
    XmlElement xmlElement29 = new XmlElement("hi!");
    int i30 = xmlElement29.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str31 = xmlElement29.getAttributes();
    xmlElement25.setAttributes(hashtable_str_str31);
    xmlElement9.setAttributes(hashtable_str_str31);
    xmlElement2.setAttributes(hashtable_str_str31);
    XmlElement xmlElement35 = new XmlElement("", hashtable_str_str31);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i3 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b5 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i16 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b20 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i30 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str31);

  }

  @Test
  public void test052() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test052"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj3 = xmlElement1.clone();
    XmlElement.printNode(xmlElement1, "hi!");
    java.util.List list6 = xmlElement1.getElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list6);

  }

  @Test
  public void test053() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test053"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = xmlElement1.addSubElement("");
    xmlElement6.setData("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);

  }

  @Test
  public void test054() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test054"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    XmlElement.printNode(xmlElement1, "hi!");
    xmlElement1.notifyObservers((java.lang.Object)(short)10);
    XmlElement xmlElement7 = xmlElement1.getParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement7);

  }

  @Test
  public void test055() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test055"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    java.util.Observer observer5 = null;
    xmlElement4.deleteObserver(observer5);
    java.util.Observer observer7 = null;
    xmlElement4.deleteObserver(observer7);
    java.lang.Object obj11 = xmlElement4.addAttribute("", "hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj11);

  }

  @Test
  public void test056() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test056"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement xmlElement6 = new XmlElement("hi!");
    int i7 = xmlElement6.countObservers();
    XmlElement.printNode(xmlElement6, "hi!");
    XmlElement xmlElement10 = xmlElement4.removeElement(xmlElement6);
    xmlElement6.notifyObservers();
    XmlElement xmlElement14 = xmlElement6.addSubElement("", "");
    XmlElement xmlElement16 = new XmlElement("hi!");
    int i17 = xmlElement16.countObservers();
    java.util.List list18 = xmlElement16.getElements();
    int i19 = xmlElement16.count();
    boolean b21 = xmlElement16.equals((java.lang.Object)1.0f);
    XmlElement xmlElement23 = new XmlElement("hi!");
    int i24 = xmlElement23.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str25 = xmlElement23.getAttributes();
    boolean b26 = xmlElement23.hasChanged();
    XmlElement xmlElement28 = xmlElement23.addSubElement("");
    XmlElement xmlElement29 = xmlElement16.removeElement(xmlElement23);
    java.lang.Object obj30 = xmlElement23.clone();
    // The following exception was thrown during execution in test generation
    try {
    xmlElement14.insertElement(xmlElement23, 100);
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
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i17 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i19 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i24 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b26 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement28);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj30);

  }

  @Test
  public void test057() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test057"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    java.util.Observer observer5 = null;
    xmlElement4.deleteObserver(observer5);
    XmlElement xmlElement8 = new XmlElement("hi!");
    XmlElement xmlElement11 = xmlElement8.addSubElement("hi!", "hi!");
    XmlElement.printNode(xmlElement11, "");
    xmlElement11.setData("");
    boolean b16 = xmlElement11.hasChanged();
    xmlElement4.notifyObservers((java.lang.Object)xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b16 == false);

  }

  @Test
  public void test058() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test058"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Enumeration enumeration3 = xmlElement1.getAttributeNames();
    java.util.List list4 = xmlElement1.getElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list4);

  }

  @Test
  public void test059() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test059"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    XmlElement.printNode(xmlElement1, "hi!");
    xmlElement1.notifyObservers((java.lang.Object)(short)10);
    xmlElement1.removeAllElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);

  }

  @Test
  public void test060() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test060"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement xmlElement6 = xmlElement4.getElement("");
    XmlElement xmlElement8 = new XmlElement("hi!");
    int i9 = xmlElement8.countObservers();
    java.lang.Object obj10 = xmlElement8.clone();
    XmlElement xmlElement11 = xmlElement4.addSubElement(xmlElement8);
    XmlElement xmlElement13 = new XmlElement("hi!");
    int i14 = xmlElement13.countObservers();
    java.util.List list15 = xmlElement13.getElements();
    java.util.Observer observer16 = null;
    xmlElement13.deleteObserver(observer16);
    xmlElement13.removeAllElements();
    XmlElement xmlElement20 = xmlElement13.addSubElement("");
    XmlElement xmlElement22 = new XmlElement("hi!");
    int i23 = xmlElement22.countObservers();
    XmlElement.printNode(xmlElement22, "hi!");
    java.util.List list26 = xmlElement22.getElements();
    xmlElement20.notifyObservers((java.lang.Object)xmlElement22);
    xmlElement20.notifyObservers();
    xmlElement11.setParent(xmlElement20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i9 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i14 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i23 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list26);

  }

  @Test
  public void test061() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test061"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    java.util.Observer observer5 = null;
    xmlElement4.deleteObserver(observer5);
    java.lang.String str7 = xmlElement4.getName();
    java.lang.Object obj8 = xmlElement4.clone();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = xmlElement4.getAttributes();
    XmlElement xmlElement11 = new XmlElement("hi!");
    java.util.Observer observer12 = null;
    xmlElement11.deleteObserver(observer12);
    xmlElement11.setName("");
    XmlElement xmlElement17 = new XmlElement("hi!");
    int i18 = xmlElement17.countObservers();
    java.util.Enumeration enumeration19 = xmlElement17.getAttributeNames();
    xmlElement11.insertElement(xmlElement17, (int)(byte)0);
    XmlElement xmlElement24 = xmlElement11.addSubElement("hi!", "");
    XmlElement xmlElement25 = xmlElement4.removeElement(xmlElement11);
    // The following exception was thrown during execution in test generation
    try {
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str26 = xmlElement25.getAttributes();
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
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i18 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement25);

  }

  @Test
  public void test062() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test062"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj3 = xmlElement1.clone();
    XmlElement xmlElement5 = new XmlElement("hi!");
    XmlElement xmlElement8 = xmlElement5.addSubElement("hi!", "hi!");
    java.util.Observer observer9 = null;
    xmlElement8.deleteObserver(observer9);
    java.lang.String str11 = xmlElement8.getName();
    XmlElement xmlElement12 = xmlElement1.removeElement(xmlElement8);
    XmlElement xmlElement14 = new XmlElement("hi!");
    int i15 = xmlElement14.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str16 = xmlElement14.getAttributes();
    boolean b17 = xmlElement14.hasChanged();
    XmlElement xmlElement20 = new XmlElement("hi!", "hi!");
    XmlElement xmlElement21 = new XmlElement();
    XmlElement xmlElement22 = xmlElement20.removeElement(xmlElement21);
    boolean b23 = xmlElement14.addElement(xmlElement21);
    xmlElement8.append(xmlElement14);
    java.lang.String str26 = xmlElement14.getAttribute("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str11 + "' != '" + "hi!"+ "'", str11.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i15 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b23 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str26);

  }

  @Test
  public void test063() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test063"); }


    XmlElement xmlElement0 = new XmlElement();
    XmlElement xmlElement2 = new XmlElement("hi!");
    java.util.Observer observer3 = null;
    xmlElement2.deleteObserver(observer3);
    boolean b6 = xmlElement2.equals((java.lang.Object)100L);
    xmlElement0.setParent(xmlElement2);
    XmlElement xmlElement10 = new XmlElement("hi!", "hi!");
    boolean b11 = xmlElement0.equals((java.lang.Object)"hi!");
    XmlElement xmlElement12 = new XmlElement();
    java.lang.String str14 = xmlElement12.getAttribute("");
    XmlElement xmlElement17 = new XmlElement("hi!", "hi!");
    xmlElement12.insertElement(xmlElement17, 0);
    xmlElement0.notifyObservers((java.lang.Object)xmlElement17);
    xmlElement0.setData("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str14);

  }

  @Test
  public void test064() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test064"); }


    XmlElement xmlElement2 = new XmlElement("hi!");
    int i3 = xmlElement2.countObservers();
    java.util.List list4 = xmlElement2.getElements();
    int i5 = xmlElement2.count();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str6 = xmlElement2.getAttributes();
    XmlElement xmlElement7 = new XmlElement("hi!", hashtable_str_str6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i3 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str6);

  }

  @Test
  public void test065() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test065"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    java.util.Observer observer5 = null;
    xmlElement4.deleteObserver(observer5);
    java.util.Observer observer7 = null;
    xmlElement4.deleteObserver(observer7);
    xmlElement4.setData("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = xmlElement4.getAttributes();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str11);

  }

  @Test
  public void test066() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test066"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Enumeration enumeration3 = xmlElement1.getAttributeNames();
    XmlElement.printNode(xmlElement1, "");
    java.lang.String str8 = xmlElement1.getAttribute("", "hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str8 + "' != '" + "hi!"+ "'", str8.equals("hi!"));

  }

  @Test
  public void test067() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test067"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj5 = xmlElement1.addAttribute("hi!", "");
    xmlElement1.removeAllElements();
    XmlElement xmlElement8 = new XmlElement("hi!");
    int i9 = xmlElement8.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = xmlElement8.getAttributes();
    XmlElement xmlElement12 = new XmlElement("hi!");
    int i13 = xmlElement12.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = xmlElement12.getAttributes();
    boolean b15 = xmlElement8.addElement(xmlElement12);
    xmlElement12.removeFromParent();
    XmlElement xmlElement17 = xmlElement1.addSubElement(xmlElement12);
    xmlElement17.removeAllElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i9 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);

  }

  @Test
  public void test068() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test068"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement.printNode(xmlElement4, "");
    XmlElement xmlElement8 = new XmlElement("hi!");
    int i9 = xmlElement8.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = xmlElement8.getAttributes();
    xmlElement4.setAttributes(hashtable_str_str10);
    XmlElement xmlElement13 = xmlElement4.addSubElement("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i9 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement13);

  }

  @Test
  public void test069() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test069"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement.printNode(xmlElement4, "");
    xmlElement4.setData("");
    boolean b9 = xmlElement4.hasChanged();
    XmlElement.printNode(xmlElement4, "hi!");
    java.util.Observer observer12 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement4.addObserver(observer12);
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
    org.junit.Assert.assertTrue(b9 == false);

  }

  @Test
  public void test070() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test070"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    int i4 = xmlElement1.count();
    boolean b6 = xmlElement1.equals((java.lang.Object)1.0f);
    XmlElement xmlElement8 = new XmlElement("hi!");
    int i9 = xmlElement8.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = xmlElement8.getAttributes();
    boolean b11 = xmlElement8.hasChanged();
    XmlElement xmlElement13 = xmlElement8.addSubElement("");
    XmlElement xmlElement14 = xmlElement1.removeElement(xmlElement8);
    java.lang.Object obj15 = xmlElement8.clone();
    java.lang.String str17 = xmlElement8.getAttribute("");
    java.util.Enumeration enumeration18 = xmlElement8.getAttributeNames();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i4 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i9 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration18);

  }

  @Test
  public void test071() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test071"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Observer observer2 = null;
    xmlElement1.deleteObserver(observer2);
    xmlElement1.setName("");
    XmlElement xmlElement7 = new XmlElement("hi!");
    int i8 = xmlElement7.countObservers();
    java.util.Enumeration enumeration9 = xmlElement7.getAttributeNames();
    xmlElement1.insertElement(xmlElement7, (int)(byte)0);
    XmlElement xmlElement14 = xmlElement1.addSubElement("hi!", "");
    java.lang.Object obj15 = xmlElement1.clone();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj15);

  }

  @Test
  public void test072() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test072"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    java.util.Observer observer5 = null;
    xmlElement4.deleteObserver(observer5);
    java.lang.String str7 = xmlElement4.getName();
    java.lang.Object obj8 = xmlElement4.clone();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = xmlElement4.getAttributes();
    XmlElement xmlElement11 = new XmlElement("hi!");
    java.util.Observer observer12 = null;
    xmlElement11.deleteObserver(observer12);
    xmlElement11.setName("");
    XmlElement xmlElement17 = new XmlElement("hi!");
    int i18 = xmlElement17.countObservers();
    java.util.Enumeration enumeration19 = xmlElement17.getAttributeNames();
    xmlElement11.insertElement(xmlElement17, (int)(byte)0);
    XmlElement xmlElement24 = xmlElement11.addSubElement("hi!", "");
    XmlElement xmlElement25 = xmlElement4.removeElement(xmlElement11);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str26 = xmlElement4.getAttributes();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i18 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str26);

  }

  @Test
  public void test073() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test073"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj3 = xmlElement1.clone();
    XmlElement.printNode(xmlElement1, "hi!");
    java.util.Observer observer6 = null;
    xmlElement1.deleteObserver(observer6);
    java.util.List list8 = xmlElement1.getElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list8);

  }

  @Test
  public void test074() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test074"); }


    XmlElement xmlElement1 = new XmlElement("");

  }

  @Test
  public void test075() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test075"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = xmlElement1.addSubElement("");
    java.lang.Object obj7 = xmlElement1.clone();
    java.util.Observer observer8 = null;
    xmlElement1.deleteObserver(observer8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj7);

  }

  @Test
  public void test076() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test076"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    XmlElement.printNode(xmlElement1, "hi!");
    java.util.List list5 = xmlElement1.getElements();
    java.util.Observer observer6 = null;
    xmlElement1.deleteObserver(observer6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list5);

  }

  @Test
  public void test077() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test077"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    java.util.Observer observer4 = null;
    xmlElement1.deleteObserver(observer4);
    xmlElement1.removeAllElements();
    XmlElement xmlElement9 = xmlElement1.addSubElement("", "hi!");
    xmlElement1.setData("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement9);

  }

  @Test
  public void test078() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test078"); }


    XmlElement xmlElement0 = new XmlElement();
    XmlElement xmlElement2 = new XmlElement("hi!");
    java.util.Observer observer3 = null;
    xmlElement2.deleteObserver(observer3);
    boolean b6 = xmlElement2.equals((java.lang.Object)100L);
    xmlElement0.setParent(xmlElement2);
    XmlElement xmlElement10 = new XmlElement("hi!", "hi!");
    boolean b11 = xmlElement0.equals((java.lang.Object)"hi!");
    XmlElement xmlElement12 = new XmlElement();
    java.lang.String str14 = xmlElement12.getAttribute("");
    XmlElement xmlElement16 = new XmlElement("hi!");
    XmlElement xmlElement19 = xmlElement16.addSubElement("hi!", "hi!");
    XmlElement xmlElement21 = new XmlElement("hi!");
    int i22 = xmlElement21.countObservers();
    XmlElement.printNode(xmlElement21, "hi!");
    XmlElement xmlElement25 = xmlElement19.removeElement(xmlElement21);
    xmlElement21.notifyObservers();
    XmlElement xmlElement29 = xmlElement21.addSubElement("", "");
    XmlElement xmlElement31 = new XmlElement("hi!");
    int i32 = xmlElement31.countObservers();
    java.util.Enumeration enumeration33 = xmlElement31.getAttributeNames();
    boolean b34 = xmlElement21.addElement(xmlElement31);
    boolean b35 = xmlElement12.addElement(xmlElement21);
    XmlElement xmlElement36 = xmlElement0.addSubElement(xmlElement12);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str37 = xmlElement12.getAttributes();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i22 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i32 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration33);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b34 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b35 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement36);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str37);

  }

  @Test
  public void test079() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test079"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    int i4 = xmlElement1.count();
    java.lang.String str5 = xmlElement1.getName();
    xmlElement1.deleteObservers();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement8 = xmlElement1.getElement((int)(byte)100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i4 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str5 + "' != '" + "hi!"+ "'", str5.equals("hi!"));

  }

  @Test
  public void test080() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test080"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj3 = xmlElement1.clone();
    XmlElement.printNode(xmlElement1, "hi!");
    XmlElement xmlElement7 = new XmlElement("hi!");
    int i8 = xmlElement7.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = xmlElement7.getAttributes();
    boolean b10 = xmlElement7.hasChanged();
    XmlElement xmlElement12 = xmlElement7.addSubElement("");
    java.lang.String str14 = xmlElement12.getAttribute("");
    XmlElement xmlElement16 = new XmlElement("hi!");
    XmlElement xmlElement19 = xmlElement16.addSubElement("hi!", "hi!");
    java.util.Observer observer20 = null;
    xmlElement19.deleteObserver(observer20);
    java.util.Observer observer22 = null;
    xmlElement19.deleteObserver(observer22);
    java.lang.String str24 = xmlElement19.getData();
    XmlElement xmlElement26 = new XmlElement("hi!");
    XmlElement xmlElement29 = xmlElement26.addSubElement("hi!", "hi!");
    XmlElement xmlElement31 = new XmlElement("hi!");
    int i32 = xmlElement31.countObservers();
    XmlElement.printNode(xmlElement31, "hi!");
    XmlElement xmlElement35 = xmlElement29.removeElement(xmlElement31);
    xmlElement19.append(xmlElement31);
    boolean b37 = xmlElement12.equals((java.lang.Object)xmlElement31);
    boolean b38 = xmlElement1.equals((java.lang.Object)xmlElement31);
    java.util.Observer observer39 = null;
    xmlElement1.deleteObserver(observer39);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str24 + "' != '" + "hi!"+ "'", str24.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i32 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement35);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b37 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b38 == true);

  }

  @Test
  public void test081() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test081"); }


    XmlElement xmlElement0 = new XmlElement();
    XmlElement xmlElement2 = new XmlElement("hi!");
    java.util.Observer observer3 = null;
    xmlElement2.deleteObserver(observer3);
    boolean b6 = xmlElement2.equals((java.lang.Object)100L);
    xmlElement0.setParent(xmlElement2);
    XmlElement xmlElement9 = new XmlElement("hi!");
    int i10 = xmlElement9.countObservers();
    java.util.List list11 = xmlElement9.getElements();
    int i12 = xmlElement9.count();
    xmlElement9.deleteObservers();
    java.util.List list14 = xmlElement9.getElements();
    xmlElement2.notifyObservers((java.lang.Object)list14);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str16 = xmlElement2.getAttributes();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i12 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str16);

  }

  @Test
  public void test082() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test082"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    XmlElement xmlElement5 = new XmlElement("hi!");
    int i6 = xmlElement5.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = xmlElement5.getAttributes();
    boolean b8 = xmlElement1.addElement(xmlElement5);
    XmlElement xmlElement10 = new XmlElement("hi!");
    XmlElement xmlElement13 = xmlElement10.addSubElement("hi!", "hi!");
    java.util.Observer observer14 = null;
    xmlElement13.deleteObserver(observer14);
    java.lang.String str16 = xmlElement13.getName();
    XmlElement xmlElement18 = new XmlElement("hi!");
    int i19 = xmlElement18.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str20 = xmlElement18.getAttributes();
    xmlElement13.setAttributes(hashtable_str_str20);
    boolean b22 = xmlElement5.addElement(xmlElement13);
    xmlElement5.setName("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str16 + "' != '" + "hi!"+ "'", str16.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i19 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b22 == true);

  }

  @Test
  public void test083() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test083"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement7 = new XmlElement("hi!", "hi!");
    XmlElement xmlElement8 = new XmlElement();
    XmlElement xmlElement9 = xmlElement7.removeElement(xmlElement8);
    boolean b10 = xmlElement1.addElement(xmlElement8);
    xmlElement1.setName("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);

  }

  @Test
  public void test084() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test084"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    java.util.Observer observer5 = null;
    xmlElement4.deleteObserver(observer5);
    java.util.Observer observer7 = null;
    xmlElement4.deleteObserver(observer7);
    xmlElement4.setData("");
    xmlElement4.setData("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);

  }

  @Test
  public void test085() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test085"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    java.util.Observer observer5 = null;
    xmlElement4.deleteObserver(observer5);
    java.lang.String str7 = xmlElement4.getName();
    xmlElement4.notifyObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));

  }

  @Test
  public void test086() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test086"); }


    XmlElement xmlElement2 = new XmlElement("hi!");
    XmlElement xmlElement5 = xmlElement2.addSubElement("hi!", "hi!");
    java.util.Observer observer6 = null;
    xmlElement5.deleteObserver(observer6);
    java.lang.String str8 = xmlElement5.getName();
    java.lang.Object obj9 = xmlElement5.clone();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = xmlElement5.getAttributes();
    XmlElement xmlElement11 = new XmlElement("", hashtable_str_str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str8 + "' != '" + "hi!"+ "'", str8.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str10);

  }

  @Test
  public void test087() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test087"); }


    XmlElement xmlElement0 = new XmlElement();
    XmlElement xmlElement2 = new XmlElement("hi!");
    java.util.Observer observer3 = null;
    xmlElement2.deleteObserver(observer3);
    boolean b6 = xmlElement2.equals((java.lang.Object)100L);
    xmlElement0.setParent(xmlElement2);
    XmlElement xmlElement10 = new XmlElement("hi!", "hi!");
    boolean b11 = xmlElement0.equals((java.lang.Object)"hi!");
    XmlElement xmlElement12 = new XmlElement();
    java.lang.String str14 = xmlElement12.getAttribute("");
    XmlElement xmlElement17 = new XmlElement("hi!", "hi!");
    xmlElement12.insertElement(xmlElement17, 0);
    xmlElement0.notifyObservers((java.lang.Object)xmlElement17);
    xmlElement0.removeFromParent();
    java.util.Observer observer22 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement0.addObserver(observer22);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str14);

  }

  @Test
  public void test088() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test088"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement xmlElement6 = new XmlElement("hi!");
    int i7 = xmlElement6.countObservers();
    XmlElement.printNode(xmlElement6, "hi!");
    XmlElement xmlElement10 = xmlElement4.removeElement(xmlElement6);
    xmlElement6.notifyObservers();
    XmlElement xmlElement14 = xmlElement6.addSubElement("", "");
    XmlElement xmlElement16 = new XmlElement("hi!");
    int i17 = xmlElement16.countObservers();
    java.util.Enumeration enumeration18 = xmlElement16.getAttributeNames();
    boolean b19 = xmlElement6.addElement(xmlElement16);
    java.lang.Object obj20 = xmlElement6.clone();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i17 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b19 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj20);

  }

  @Test
  public void test089() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test089"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    int i4 = xmlElement1.count();
    java.lang.String str5 = xmlElement1.getName();
    XmlElement xmlElement7 = new XmlElement("hi!");
    int i8 = xmlElement7.countObservers();
    XmlElement.printNode(xmlElement7, "hi!");
    xmlElement7.notifyObservers((java.lang.Object)(short)10);
    XmlElement xmlElement13 = xmlElement1.removeElement(xmlElement7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i4 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str5 + "' != '" + "hi!"+ "'", str5.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement13);

  }

  @Test
  public void test090() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test090"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = new XmlElement("hi!");
    int i7 = xmlElement6.countObservers();
    XmlElement.printNode(xmlElement6, "hi!");
    java.util.List list10 = xmlElement6.getElements();
    boolean b11 = xmlElement1.addElement(xmlElement6);
    xmlElement6.notifyObservers();
    XmlElement xmlElement14 = new XmlElement("hi!");
    XmlElement xmlElement17 = xmlElement14.addSubElement("hi!", "hi!");
    xmlElement6.append(xmlElement17);
    java.lang.String str20 = xmlElement6.getAttribute("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str20);

  }

  @Test
  public void test091() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test091"); }


    XmlElement xmlElement0 = new XmlElement();
    XmlElement xmlElement2 = new XmlElement("hi!");
    java.util.Observer observer3 = null;
    xmlElement2.deleteObserver(observer3);
    boolean b6 = xmlElement2.equals((java.lang.Object)100L);
    xmlElement0.setParent(xmlElement2);
    XmlElement xmlElement9 = new XmlElement("hi!");
    int i10 = xmlElement9.countObservers();
    java.util.List list11 = xmlElement9.getElements();
    int i12 = xmlElement9.count();
    xmlElement9.deleteObservers();
    java.util.List list14 = xmlElement9.getElements();
    xmlElement2.notifyObservers((java.lang.Object)list14);
    xmlElement2.notifyObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i12 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list14);

  }

  @Test
  public void test092() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test092"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj5 = xmlElement1.addAttribute("hi!", "");
    xmlElement1.removeAllElements();
    XmlElement xmlElement8 = new XmlElement("hi!");
    int i9 = xmlElement8.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = xmlElement8.getAttributes();
    XmlElement xmlElement12 = new XmlElement("hi!");
    int i13 = xmlElement12.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = xmlElement12.getAttributes();
    boolean b15 = xmlElement8.addElement(xmlElement12);
    xmlElement12.removeFromParent();
    XmlElement xmlElement17 = xmlElement1.addSubElement(xmlElement12);
    int i18 = xmlElement17.count();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i9 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i18 == 0);

  }

  @Test
  public void test093() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test093"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    java.util.Observer observer5 = null;
    xmlElement4.deleteObserver(observer5);
    java.util.Observer observer7 = null;
    xmlElement4.deleteObserver(observer7);
    java.lang.String str9 = xmlElement4.getData();
    XmlElement xmlElement11 = new XmlElement("hi!");
    int i12 = xmlElement11.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str13 = xmlElement11.getAttributes();
    boolean b14 = xmlElement11.hasChanged();
    XmlElement xmlElement16 = xmlElement11.addSubElement("");
    XmlElement xmlElement18 = new XmlElement("hi!");
    int i19 = xmlElement18.countObservers();
    java.lang.Object obj22 = xmlElement18.addAttribute("hi!", "");
    XmlElement xmlElement24 = new XmlElement("hi!");
    int i25 = xmlElement24.countObservers();
    java.util.Enumeration enumeration26 = xmlElement24.getAttributeNames();
    XmlElement.printNode(xmlElement24, "");
    boolean b29 = xmlElement18.addElement(xmlElement24);
    XmlElement xmlElement31 = new XmlElement("hi!");
    XmlElement xmlElement34 = xmlElement31.addSubElement("hi!", "hi!");
    XmlElement.printNode(xmlElement34, "");
    XmlElement xmlElement38 = new XmlElement("hi!");
    int i39 = xmlElement38.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str40 = xmlElement38.getAttributes();
    xmlElement34.setAttributes(hashtable_str_str40);
    xmlElement18.setAttributes(hashtable_str_str40);
    xmlElement11.setAttributes(hashtable_str_str40);
    // The following exception was thrown during execution in test generation
    try {
    xmlElement4.insertElement(xmlElement11, (-1));
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
    org.junit.Assert.assertTrue("'" + str9 + "' != '" + "hi!"+ "'", str9.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i12 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i19 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i25 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b29 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement34);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i39 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str40);

  }

  @Test
  public void test094() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test094"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement xmlElement6 = new XmlElement("hi!");
    int i7 = xmlElement6.countObservers();
    XmlElement.printNode(xmlElement6, "hi!");
    XmlElement xmlElement10 = xmlElement4.removeElement(xmlElement6);
    // The following exception was thrown during execution in test generation
    try {
    xmlElement10.deleteObservers();
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
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement10);

  }

  @Test
  public void test095() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test095"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    java.util.Observer observer4 = null;
    xmlElement1.deleteObserver(observer4);
    XmlElement xmlElement7 = new XmlElement("hi!");
    int i8 = xmlElement7.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = xmlElement7.getAttributes();
    XmlElement xmlElement11 = new XmlElement("hi!");
    int i12 = xmlElement11.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str13 = xmlElement11.getAttributes();
    boolean b14 = xmlElement7.addElement(xmlElement11);
    xmlElement1.append(xmlElement11);
    int i16 = xmlElement11.count();
    java.util.Enumeration enumeration17 = xmlElement11.getAttributeNames();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i12 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i16 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration17);

  }

  @Test
  public void test096() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test096"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    XmlElement xmlElement5 = new XmlElement("hi!");
    int i6 = xmlElement5.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = xmlElement5.getAttributes();
    boolean b8 = xmlElement1.addElement(xmlElement5);
    XmlElement xmlElement10 = new XmlElement("hi!");
    XmlElement xmlElement13 = xmlElement10.addSubElement("hi!", "hi!");
    java.util.Observer observer14 = null;
    xmlElement13.deleteObserver(observer14);
    java.lang.String str16 = xmlElement13.getName();
    XmlElement xmlElement18 = new XmlElement("hi!");
    int i19 = xmlElement18.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str20 = xmlElement18.getAttributes();
    xmlElement13.setAttributes(hashtable_str_str20);
    boolean b22 = xmlElement5.addElement(xmlElement13);
    XmlElement xmlElement24 = new XmlElement("hi!");
    int i25 = xmlElement24.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str26 = xmlElement24.getAttributes();
    boolean b27 = xmlElement24.hasChanged();
    XmlElement xmlElement29 = xmlElement24.addSubElement("");
    XmlElement xmlElement31 = new XmlElement("hi!");
    XmlElement xmlElement34 = xmlElement31.addSubElement("hi!", "hi!");
    java.util.Observer observer35 = null;
    xmlElement34.deleteObserver(observer35);
    XmlElement xmlElement37 = xmlElement24.removeElement(xmlElement34);
    xmlElement5.insertElement(xmlElement34, (int)(short)1);
    XmlElement xmlElement41 = new XmlElement("hi!");
    XmlElement xmlElement44 = xmlElement41.addSubElement("hi!", "hi!");
    java.util.Observer observer45 = null;
    xmlElement44.deleteObserver(observer45);
    java.lang.String str47 = xmlElement44.getName();
    XmlElement xmlElement49 = new XmlElement("hi!");
    int i50 = xmlElement49.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str51 = xmlElement49.getAttributes();
    xmlElement44.setAttributes(hashtable_str_str51);
    xmlElement5.setAttributes(hashtable_str_str51);
    XmlElement xmlElement55 = new XmlElement("hi!");
    XmlElement xmlElement58 = xmlElement55.addSubElement("hi!", "hi!");
    java.util.Observer observer59 = null;
    xmlElement58.deleteObserver(observer59);
    java.lang.String str61 = xmlElement58.getName();
    java.lang.Object obj62 = xmlElement58.clone();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str63 = xmlElement58.getAttributes();
    XmlElement xmlElement65 = new XmlElement("hi!");
    java.util.Observer observer66 = null;
    xmlElement65.deleteObserver(observer66);
    xmlElement65.setName("");
    XmlElement xmlElement71 = new XmlElement("hi!");
    int i72 = xmlElement71.countObservers();
    java.util.Enumeration enumeration73 = xmlElement71.getAttributeNames();
    xmlElement65.insertElement(xmlElement71, (int)(byte)0);
    XmlElement xmlElement78 = xmlElement65.addSubElement("hi!", "");
    XmlElement xmlElement79 = xmlElement58.removeElement(xmlElement65);
    // The following exception was thrown during execution in test generation
    try {
    xmlElement5.insertElement(xmlElement79, (int)'a');
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str16 + "' != '" + "hi!"+ "'", str16.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i19 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b22 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i25 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b27 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement34);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement44);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str47 + "' != '" + "hi!"+ "'", str47.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i50 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str51);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement58);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str61 + "' != '" + "hi!"+ "'", str61.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj62);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str63);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i72 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration73);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement78);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement79);

  }

  @Test
  public void test097() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test097"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj3 = xmlElement1.clone();
    XmlElement.printNode(xmlElement1, "hi!");
    java.util.Observer observer6 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement1.addObserver(observer6);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);

  }

  @Test
  public void test098() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test098"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement xmlElement7 = xmlElement1.addSubElement("hi!", "");
    xmlElement7.setData("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement7);

  }

  @Test
  public void test099() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test099"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Observer observer2 = null;
    xmlElement1.deleteObserver(observer2);
    xmlElement1.setName("");
    XmlElement xmlElement7 = new XmlElement("hi!");
    int i8 = xmlElement7.countObservers();
    java.util.Enumeration enumeration9 = xmlElement7.getAttributeNames();
    xmlElement1.insertElement(xmlElement7, (int)(byte)0);
    int i12 = xmlElement1.count();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i12 == 1);

  }

  @Test
  public void test100() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test100"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = xmlElement1.addSubElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = xmlElement1.getAttributes();
    XmlElement xmlElement9 = new XmlElement("hi!");
    int i10 = xmlElement9.countObservers();
    java.lang.Object obj13 = xmlElement9.addAttribute("hi!", "");
    boolean b14 = xmlElement1.addElement(xmlElement9);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str15 = xmlElement1.getAttributes();
    XmlElement xmlElement17 = new XmlElement("hi!");
    int i18 = xmlElement17.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str19 = xmlElement17.getAttributes();
    boolean b20 = xmlElement17.hasChanged();
    XmlElement xmlElement22 = xmlElement17.addSubElement("");
    java.lang.String str24 = xmlElement22.getAttribute("");
    boolean b25 = xmlElement1.addElement(xmlElement22);
    xmlElement22.setName("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i18 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b20 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b25 == true);

  }

  @Test
  public void test101() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test101"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    XmlElement xmlElement5 = new XmlElement("hi!");
    int i6 = xmlElement5.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = xmlElement5.getAttributes();
    boolean b8 = xmlElement1.addElement(xmlElement5);
    XmlElement xmlElement10 = new XmlElement("hi!");
    XmlElement xmlElement13 = xmlElement10.addSubElement("hi!", "hi!");
    java.util.Observer observer14 = null;
    xmlElement13.deleteObserver(observer14);
    java.lang.String str16 = xmlElement13.getName();
    XmlElement xmlElement18 = new XmlElement("hi!");
    int i19 = xmlElement18.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str20 = xmlElement18.getAttributes();
    xmlElement13.setAttributes(hashtable_str_str20);
    boolean b22 = xmlElement5.addElement(xmlElement13);
    java.util.Enumeration enumeration23 = xmlElement5.getAttributeNames();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str24 = xmlElement5.getAttributes();
    xmlElement5.notifyObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str16 + "' != '" + "hi!"+ "'", str16.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i19 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b22 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str24);

  }

  @Test
  public void test102() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test102"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement xmlElement6 = new XmlElement("hi!");
    int i7 = xmlElement6.countObservers();
    XmlElement.printNode(xmlElement6, "hi!");
    XmlElement xmlElement10 = xmlElement4.removeElement(xmlElement6);
    xmlElement6.notifyObservers();
    java.lang.Object obj14 = xmlElement6.addAttribute("hi!", "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj14);

  }

  @Test
  public void test103() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test103"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    XmlElement xmlElement5 = new XmlElement("hi!");
    int i6 = xmlElement5.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = xmlElement5.getAttributes();
    boolean b8 = xmlElement1.addElement(xmlElement5);
    XmlElement xmlElement10 = new XmlElement("hi!");
    XmlElement xmlElement13 = xmlElement10.addSubElement("hi!", "hi!");
    java.util.Observer observer14 = null;
    xmlElement13.deleteObserver(observer14);
    java.lang.String str16 = xmlElement13.getName();
    XmlElement xmlElement18 = new XmlElement("hi!");
    int i19 = xmlElement18.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str20 = xmlElement18.getAttributes();
    xmlElement13.setAttributes(hashtable_str_str20);
    boolean b22 = xmlElement5.addElement(xmlElement13);
    XmlElement xmlElement24 = new XmlElement("hi!");
    int i25 = xmlElement24.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str26 = xmlElement24.getAttributes();
    boolean b27 = xmlElement24.hasChanged();
    XmlElement xmlElement29 = xmlElement24.addSubElement("");
    XmlElement xmlElement31 = new XmlElement("hi!");
    XmlElement xmlElement34 = xmlElement31.addSubElement("hi!", "hi!");
    java.util.Observer observer35 = null;
    xmlElement34.deleteObserver(observer35);
    XmlElement xmlElement37 = xmlElement24.removeElement(xmlElement34);
    xmlElement5.insertElement(xmlElement34, (int)(short)1);
    XmlElement xmlElement41 = new XmlElement("hi!");
    XmlElement xmlElement44 = xmlElement41.addSubElement("hi!", "hi!");
    java.util.Observer observer45 = null;
    xmlElement44.deleteObserver(observer45);
    java.lang.String str47 = xmlElement44.getName();
    XmlElement xmlElement49 = new XmlElement("hi!");
    int i50 = xmlElement49.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str51 = xmlElement49.getAttributes();
    xmlElement44.setAttributes(hashtable_str_str51);
    xmlElement5.setAttributes(hashtable_str_str51);
    XmlElement xmlElement55 = new XmlElement("hi!");
    XmlElement xmlElement58 = xmlElement55.addSubElement("hi!", "hi!");
    XmlElement xmlElement59 = xmlElement5.removeElement(xmlElement58);
    XmlElement xmlElement60 = null;
    xmlElement5.setParent(xmlElement60);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str16 + "' != '" + "hi!"+ "'", str16.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i19 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b22 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i25 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b27 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement34);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement44);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str47 + "' != '" + "hi!"+ "'", str47.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i50 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str51);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement58);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement59);

  }

  @Test
  public void test104() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test104"); }


    XmlElement xmlElement2 = new XmlElement("", "hi!");
    int i3 = xmlElement2.countObservers();
    int i4 = xmlElement2.countObservers();
    XmlElement xmlElement6 = new XmlElement("hi!");
    int i7 = xmlElement6.countObservers();
    java.util.List list8 = xmlElement6.getElements();
    int i9 = xmlElement6.count();
    boolean b11 = xmlElement6.equals((java.lang.Object)1.0f);
    XmlElement xmlElement13 = new XmlElement("hi!");
    int i14 = xmlElement13.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str15 = xmlElement13.getAttributes();
    boolean b16 = xmlElement13.hasChanged();
    XmlElement xmlElement18 = xmlElement13.addSubElement("");
    XmlElement xmlElement19 = xmlElement6.removeElement(xmlElement13);
    java.lang.Object obj20 = xmlElement13.clone();
    java.lang.String str22 = xmlElement13.getAttribute("");
    xmlElement2.setParent(xmlElement13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i3 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i4 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i9 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i14 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b16 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str22);

  }

  @Test
  public void test105() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test105"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    XmlElement.printNode(xmlElement1, "hi!");
    int i5 = xmlElement1.count();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);

  }

  @Test
  public void test106() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test106"); }


    XmlElement xmlElement0 = new XmlElement();
    java.lang.String str2 = xmlElement0.getAttribute("");
    XmlElement xmlElement5 = new XmlElement("hi!", "hi!");
    xmlElement0.insertElement(xmlElement5, 0);
    XmlElement xmlElement9 = xmlElement0.addSubElement("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement9);

  }

  @Test
  public void test107() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test107"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = xmlElement1.addSubElement("");
    xmlElement6.setData("hi!");
    XmlElement xmlElement10 = new XmlElement("hi!");
    int i11 = xmlElement10.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = xmlElement10.getAttributes();
    boolean b13 = xmlElement10.hasChanged();
    XmlElement xmlElement15 = xmlElement10.addSubElement("");
    XmlElement xmlElement17 = new XmlElement("hi!");
    XmlElement xmlElement20 = xmlElement17.addSubElement("hi!", "hi!");
    java.util.Observer observer21 = null;
    xmlElement20.deleteObserver(observer21);
    XmlElement xmlElement23 = xmlElement10.removeElement(xmlElement20);
    boolean b24 = xmlElement6.equals((java.lang.Object)xmlElement10);
    XmlElement xmlElement26 = new XmlElement("hi!");
    int i27 = xmlElement26.countObservers();
    XmlElement.printNode(xmlElement26, "hi!");
    xmlElement26.notifyObservers((java.lang.Object)(short)10);
    boolean b32 = xmlElement6.addElement(xmlElement26);
    java.lang.String str33 = xmlElement26.getData();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i11 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b24 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i27 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b32 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str33 + "' != '" + ""+ "'", str33.equals(""));

  }

  @Test
  public void test108() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test108"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    int i4 = xmlElement1.count();
    java.lang.String str5 = xmlElement1.getName();
    boolean b6 = xmlElement1.hasChanged();
    XmlElement xmlElement8 = new XmlElement("hi!");
    XmlElement xmlElement11 = xmlElement8.addSubElement("hi!", "hi!");
    java.util.Observer observer12 = null;
    xmlElement11.deleteObserver(observer12);
    java.util.Observer observer14 = null;
    xmlElement11.deleteObserver(observer14);
    java.lang.String str16 = xmlElement11.getData();
    XmlElement xmlElement18 = new XmlElement("hi!");
    XmlElement xmlElement21 = xmlElement18.addSubElement("hi!", "hi!");
    XmlElement xmlElement23 = new XmlElement("hi!");
    int i24 = xmlElement23.countObservers();
    XmlElement.printNode(xmlElement23, "hi!");
    XmlElement xmlElement27 = xmlElement21.removeElement(xmlElement23);
    xmlElement11.append(xmlElement23);
    XmlElement xmlElement29 = xmlElement1.removeElement(xmlElement23);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement.printNode(xmlElement29, "hi!");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i4 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str5 + "' != '" + "hi!"+ "'", str5.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str16 + "' != '" + "hi!"+ "'", str16.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i24 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement27);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement29);

  }

  @Test
  public void test109() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test109"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = xmlElement1.addSubElement("");
    xmlElement6.setData("hi!");
    XmlElement xmlElement10 = new XmlElement("hi!");
    java.util.Observer observer11 = null;
    xmlElement10.deleteObserver(observer11);
    xmlElement10.setName("");
    XmlElement xmlElement16 = new XmlElement("hi!");
    int i17 = xmlElement16.countObservers();
    java.util.Enumeration enumeration18 = xmlElement16.getAttributeNames();
    xmlElement10.insertElement(xmlElement16, (int)(byte)0);
    XmlElement xmlElement23 = xmlElement10.addSubElement("hi!", "");
    java.lang.String str26 = xmlElement23.getAttribute("hi!", "");
    XmlElement xmlElement28 = new XmlElement("hi!");
    XmlElement xmlElement31 = xmlElement28.addSubElement("hi!", "hi!");
    java.util.Observer observer32 = null;
    xmlElement31.deleteObserver(observer32);
    java.util.Observer observer34 = null;
    xmlElement31.deleteObserver(observer34);
    xmlElement31.setData("");
    xmlElement31.setName("hi!");
    xmlElement31.setName("");
    XmlElement xmlElement42 = xmlElement23.addSubElement(xmlElement31);
    xmlElement6.setParent(xmlElement42);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i17 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str26 + "' != '" + ""+ "'", str26.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement31);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement42);

  }

  @Test
  public void test110() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test110"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement xmlElement6 = xmlElement4.getElement("");
    XmlElement xmlElement8 = new XmlElement("hi!");
    int i9 = xmlElement8.countObservers();
    java.lang.Object obj10 = xmlElement8.clone();
    XmlElement xmlElement11 = xmlElement4.addSubElement(xmlElement8);
    XmlElement xmlElement13 = new XmlElement("hi!");
    java.util.Observer observer14 = null;
    xmlElement13.deleteObserver(observer14);
    boolean b17 = xmlElement13.equals((java.lang.Object)100L);
    xmlElement13.removeFromParent();
    XmlElement xmlElement19 = xmlElement8.removeElement(xmlElement13);
    java.util.List list20 = xmlElement13.getElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i9 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list20);

  }

  @Test
  public void test111() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test111"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    xmlElement1.notifyObservers((java.lang.Object)(byte)-1);

  }

  @Test
  public void test112() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test112"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    java.util.Observer observer5 = null;
    xmlElement4.deleteObserver(observer5);
    java.lang.String str7 = xmlElement4.getName();
    java.lang.Object obj8 = xmlElement4.clone();
    XmlElement xmlElement10 = xmlElement4.getElement("hi!");
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement12 = xmlElement10.removeElement((int)(short)-1);
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
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement10);

  }

  @Test
  public void test113() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test113"); }


    XmlElement xmlElement0 = new XmlElement();
    XmlElement xmlElement2 = new XmlElement("hi!");
    java.util.Observer observer3 = null;
    xmlElement2.deleteObserver(observer3);
    boolean b6 = xmlElement2.equals((java.lang.Object)100L);
    xmlElement0.setParent(xmlElement2);
    XmlElement xmlElement9 = new XmlElement("hi!");
    int i10 = xmlElement9.countObservers();
    java.util.List list11 = xmlElement9.getElements();
    int i12 = xmlElement9.count();
    xmlElement9.deleteObservers();
    java.util.List list14 = xmlElement9.getElements();
    xmlElement2.notifyObservers((java.lang.Object)list14);
    XmlElement xmlElement18 = xmlElement2.addSubElement("", "hi!");
    XmlElement xmlElement21 = xmlElement2.addSubElement("", "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i12 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement21);

  }

  @Test
  public void test114() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test114"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj3 = xmlElement1.clone();
    XmlElement.printNode(xmlElement1, "hi!");
    XmlElement xmlElement7 = new XmlElement("hi!");
    int i8 = xmlElement7.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = xmlElement7.getAttributes();
    boolean b10 = xmlElement7.hasChanged();
    XmlElement xmlElement12 = xmlElement7.addSubElement("");
    java.lang.String str14 = xmlElement12.getAttribute("");
    XmlElement xmlElement16 = new XmlElement("hi!");
    XmlElement xmlElement19 = xmlElement16.addSubElement("hi!", "hi!");
    java.util.Observer observer20 = null;
    xmlElement19.deleteObserver(observer20);
    java.util.Observer observer22 = null;
    xmlElement19.deleteObserver(observer22);
    java.lang.String str24 = xmlElement19.getData();
    XmlElement xmlElement26 = new XmlElement("hi!");
    XmlElement xmlElement29 = xmlElement26.addSubElement("hi!", "hi!");
    XmlElement xmlElement31 = new XmlElement("hi!");
    int i32 = xmlElement31.countObservers();
    XmlElement.printNode(xmlElement31, "hi!");
    XmlElement xmlElement35 = xmlElement29.removeElement(xmlElement31);
    xmlElement19.append(xmlElement31);
    boolean b37 = xmlElement12.equals((java.lang.Object)xmlElement31);
    boolean b38 = xmlElement1.equals((java.lang.Object)xmlElement31);
    java.util.Enumeration enumeration39 = xmlElement1.getAttributeNames();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str24 + "' != '" + "hi!"+ "'", str24.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i32 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement35);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b37 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b38 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration39);

  }

  @Test
  public void test115() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test115"); }


    XmlElement xmlElement0 = new XmlElement();
    XmlElement xmlElement2 = new XmlElement("hi!");
    java.util.Observer observer3 = null;
    xmlElement2.deleteObserver(observer3);
    boolean b6 = xmlElement2.equals((java.lang.Object)100L);
    xmlElement0.setParent(xmlElement2);
    XmlElement xmlElement10 = new XmlElement("hi!", "hi!");
    boolean b11 = xmlElement0.equals((java.lang.Object)"hi!");
    XmlElement xmlElement12 = new XmlElement();
    java.lang.String str14 = xmlElement12.getAttribute("");
    XmlElement xmlElement17 = new XmlElement("hi!", "hi!");
    xmlElement12.insertElement(xmlElement17, 0);
    xmlElement0.notifyObservers((java.lang.Object)xmlElement17);
    java.lang.Object obj23 = xmlElement0.addAttribute("", "");
    XmlElement.printNode(xmlElement0, "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj23);

  }

  @Test
  public void test116() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test116"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    java.util.Observer observer5 = null;
    xmlElement4.deleteObserver(observer5);
    java.util.Observer observer7 = null;
    xmlElement4.deleteObserver(observer7);
    java.lang.String str9 = xmlElement4.getData();
    XmlElement xmlElement11 = new XmlElement("hi!");
    XmlElement xmlElement14 = xmlElement11.addSubElement("hi!", "hi!");
    XmlElement xmlElement16 = new XmlElement("hi!");
    int i17 = xmlElement16.countObservers();
    XmlElement.printNode(xmlElement16, "hi!");
    XmlElement xmlElement20 = xmlElement14.removeElement(xmlElement16);
    xmlElement4.append(xmlElement16);
    XmlElement xmlElement23 = xmlElement16.addSubElement("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str9 + "' != '" + "hi!"+ "'", str9.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i17 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement23);

  }

  @Test
  public void test117() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test117"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement.printNode(xmlElement4, "");
    xmlElement4.setData("");
    boolean b9 = xmlElement4.hasChanged();
    xmlElement4.notifyObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);

  }

  @Test
  public void test118() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test118"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj5 = xmlElement1.addAttribute("hi!", "");
    XmlElement xmlElement7 = new XmlElement("hi!");
    int i8 = xmlElement7.countObservers();
    java.util.Enumeration enumeration9 = xmlElement7.getAttributeNames();
    XmlElement.printNode(xmlElement7, "");
    boolean b12 = xmlElement1.addElement(xmlElement7);
    XmlElement xmlElement13 = null;
    XmlElement xmlElement14 = xmlElement1.removeElement(xmlElement13);
    int i15 = xmlElement1.countObservers();
    XmlElement xmlElement17 = new XmlElement("hi!");
    java.util.Observer observer18 = null;
    xmlElement17.deleteObserver(observer18);
    xmlElement17.setName("");
    XmlElement xmlElement23 = new XmlElement("hi!");
    int i24 = xmlElement23.countObservers();
    java.util.Enumeration enumeration25 = xmlElement23.getAttributeNames();
    xmlElement17.insertElement(xmlElement23, (int)(byte)0);
    boolean b28 = xmlElement1.addElement(xmlElement23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i15 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i24 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b28 == true);

  }

  @Test
  public void test119() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test119"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = xmlElement1.addSubElement("");
    boolean b7 = xmlElement1.hasChanged();
    xmlElement1.deleteObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test120() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test120"); }


    XmlElement xmlElement0 = new XmlElement();
    XmlElement xmlElement2 = new XmlElement("hi!");
    java.util.Observer observer3 = null;
    xmlElement2.deleteObserver(observer3);
    boolean b6 = xmlElement2.equals((java.lang.Object)100L);
    xmlElement0.setParent(xmlElement2);
    XmlElement xmlElement10 = new XmlElement("hi!", "hi!");
    boolean b11 = xmlElement0.equals((java.lang.Object)"hi!");
    xmlElement0.removeAllElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);

  }

  @Test
  public void test121() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test121"); }


    XmlElement xmlElement2 = new XmlElement("hi!");
    int i3 = xmlElement2.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = xmlElement2.getAttributes();
    XmlElement xmlElement6 = new XmlElement("hi!");
    int i7 = xmlElement6.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = xmlElement6.getAttributes();
    boolean b9 = xmlElement2.addElement(xmlElement6);
    XmlElement xmlElement11 = new XmlElement("hi!");
    XmlElement xmlElement14 = xmlElement11.addSubElement("hi!", "hi!");
    java.util.Observer observer15 = null;
    xmlElement14.deleteObserver(observer15);
    java.lang.String str17 = xmlElement14.getName();
    XmlElement xmlElement19 = new XmlElement("hi!");
    int i20 = xmlElement19.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str21 = xmlElement19.getAttributes();
    xmlElement14.setAttributes(hashtable_str_str21);
    boolean b23 = xmlElement6.addElement(xmlElement14);
    java.util.Enumeration enumeration24 = xmlElement6.getAttributeNames();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str25 = xmlElement6.getAttributes();
    XmlElement xmlElement26 = new XmlElement("hi!", hashtable_str_str25);
    java.lang.String str27 = xmlElement26.getData();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i3 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str17 + "' != '" + "hi!"+ "'", str17.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i20 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b23 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str27);

  }

  @Test
  public void test122() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test122"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj5 = xmlElement1.addAttribute("hi!", "");
    java.util.Enumeration enumeration6 = xmlElement1.getAttributeNames();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement8 = xmlElement1.getElement((int)(byte)-1);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration6);

  }

  @Test
  public void test123() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test123"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    java.util.Observer observer5 = null;
    xmlElement4.deleteObserver(observer5);
    java.lang.String str7 = xmlElement4.getName();
    java.lang.Object obj8 = xmlElement4.clone();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = xmlElement4.getAttributes();
    XmlElement xmlElement11 = new XmlElement("hi!");
    XmlElement xmlElement14 = xmlElement11.addSubElement("hi!", "hi!");
    XmlElement xmlElement16 = xmlElement14.getElement("");
    XmlElement xmlElement18 = new XmlElement("hi!");
    int i19 = xmlElement18.countObservers();
    java.lang.Object obj20 = xmlElement18.clone();
    XmlElement xmlElement21 = xmlElement14.addSubElement(xmlElement18);
    // The following exception was thrown during execution in test generation
    try {
    xmlElement4.insertElement(xmlElement21, (int)'#');
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
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i19 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement21);

  }

  @Test
  public void test124() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test124"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj5 = xmlElement1.addAttribute("hi!", "");
    XmlElement xmlElement7 = new XmlElement("hi!");
    int i8 = xmlElement7.countObservers();
    java.util.Enumeration enumeration9 = xmlElement7.getAttributeNames();
    XmlElement.printNode(xmlElement7, "");
    boolean b12 = xmlElement1.addElement(xmlElement7);
    xmlElement7.deleteObservers();
    xmlElement7.setData("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == true);

  }

  @Test
  public void test125() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test125"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    java.util.Observer observer4 = null;
    xmlElement1.deleteObserver(observer4);
    xmlElement1.removeAllElements();
    XmlElement xmlElement8 = xmlElement1.addSubElement("");
    boolean b9 = xmlElement1.hasChanged();
    XmlElement xmlElement10 = new XmlElement();
    java.lang.String str12 = xmlElement10.getAttribute("");
    xmlElement10.setData("");
    XmlElement xmlElement16 = xmlElement10.getElement("hi!");
    boolean b17 = xmlElement1.equals((java.lang.Object)"hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);

  }

  @Test
  public void test126() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test126"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    java.util.Observer observer5 = null;
    xmlElement4.deleteObserver(observer5);
    java.util.Observer observer7 = null;
    xmlElement4.deleteObserver(observer7);
    xmlElement4.setData("");
    xmlElement4.setName("");
    XmlElement xmlElement14 = new XmlElement("hi!");
    int i15 = xmlElement14.countObservers();
    java.util.Enumeration enumeration16 = xmlElement14.getAttributeNames();
    XmlElement.printNode(xmlElement14, "");
    xmlElement4.insertElement(xmlElement14, (int)(byte)0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i15 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration16);

  }

  @Test
  public void test127() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test127"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    java.util.Observer observer4 = null;
    xmlElement1.deleteObserver(observer4);
    XmlElement xmlElement7 = xmlElement1.addSubElement("hi!");
    XmlElement xmlElement9 = xmlElement1.getElement("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement9);

  }

  @Test
  public void test128() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test128"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    java.util.Observer observer5 = null;
    xmlElement4.deleteObserver(observer5);
    java.util.Observer observer7 = null;
    xmlElement4.deleteObserver(observer7);
    java.lang.String str9 = xmlElement4.getData();
    xmlElement4.setName("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str9 + "' != '" + "hi!"+ "'", str9.equals("hi!"));

  }

  @Test
  public void test129() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test129"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = xmlElement1.addSubElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = xmlElement1.getAttributes();
    XmlElement xmlElement9 = new XmlElement("hi!");
    int i10 = xmlElement9.countObservers();
    java.lang.Object obj13 = xmlElement9.addAttribute("hi!", "");
    boolean b14 = xmlElement1.addElement(xmlElement9);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str15 = xmlElement1.getAttributes();
    XmlElement xmlElement17 = new XmlElement("hi!");
    int i18 = xmlElement17.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str19 = xmlElement17.getAttributes();
    boolean b20 = xmlElement17.hasChanged();
    XmlElement xmlElement22 = xmlElement17.addSubElement("");
    java.lang.String str24 = xmlElement22.getAttribute("");
    boolean b25 = xmlElement1.addElement(xmlElement22);
    int i26 = xmlElement1.countObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i18 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b20 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b25 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i26 == 0);

  }

  @Test
  public void test130() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test130"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement xmlElement6 = new XmlElement("hi!");
    int i7 = xmlElement6.countObservers();
    XmlElement.printNode(xmlElement6, "hi!");
    XmlElement xmlElement10 = xmlElement4.removeElement(xmlElement6);
    boolean b11 = xmlElement6.hasChanged();
    xmlElement6.removeAllElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);

  }

  @Test
  public void test131() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test131"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    java.util.Observer observer4 = null;
    xmlElement1.deleteObserver(observer4);
    XmlElement xmlElement7 = new XmlElement("hi!");
    int i8 = xmlElement7.countObservers();
    java.util.List list9 = xmlElement7.getElements();
    java.util.Observer observer10 = null;
    xmlElement7.deleteObserver(observer10);
    xmlElement7.removeAllElements();
    XmlElement xmlElement14 = xmlElement7.addSubElement("");
    boolean b15 = xmlElement1.equals((java.lang.Object)xmlElement14);
    java.util.List list16 = xmlElement1.getElements();
    java.lang.String str18 = xmlElement1.getAttribute("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str18);

  }

  @Test
  public void test132() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test132"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj3 = xmlElement1.clone();
    XmlElement xmlElement5 = new XmlElement("hi!");
    XmlElement xmlElement8 = xmlElement5.addSubElement("hi!", "hi!");
    java.util.Observer observer9 = null;
    xmlElement8.deleteObserver(observer9);
    java.lang.String str11 = xmlElement8.getName();
    XmlElement xmlElement12 = xmlElement1.removeElement(xmlElement8);
    XmlElement xmlElement14 = new XmlElement("hi!");
    int i15 = xmlElement14.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str16 = xmlElement14.getAttributes();
    boolean b17 = xmlElement14.hasChanged();
    XmlElement xmlElement20 = new XmlElement("hi!", "hi!");
    XmlElement xmlElement21 = new XmlElement();
    XmlElement xmlElement22 = xmlElement20.removeElement(xmlElement21);
    boolean b23 = xmlElement14.addElement(xmlElement21);
    xmlElement8.append(xmlElement14);
    XmlElement.printNode(xmlElement8, "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str11 + "' != '" + "hi!"+ "'", str11.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i15 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b23 == true);

  }

  @Test
  public void test133() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test133"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement xmlElement6 = xmlElement4.getElement("");
    XmlElement xmlElement8 = new XmlElement("hi!");
    int i9 = xmlElement8.countObservers();
    java.lang.Object obj10 = xmlElement8.clone();
    XmlElement xmlElement11 = xmlElement4.addSubElement(xmlElement8);
    int i12 = xmlElement4.countObservers();
    java.util.List list13 = xmlElement4.getElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i9 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i12 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list13);

  }

  @Test
  public void test134() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test134"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    java.util.Observer observer4 = null;
    xmlElement1.deleteObserver(observer4);
    java.util.List list6 = xmlElement1.getElements();
    java.lang.String str7 = xmlElement1.getName();
    java.lang.Object obj10 = xmlElement1.addAttribute("", "hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj10);

  }

  @Test
  public void test135() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test135"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement.printNode(xmlElement4, "");
    XmlElement xmlElement8 = new XmlElement("hi!");
    int i9 = xmlElement8.countObservers();
    java.lang.Object obj10 = xmlElement8.clone();
    XmlElement.printNode(xmlElement8, "hi!");
    java.util.Observer observer13 = null;
    xmlElement8.deleteObserver(observer13);
    XmlElement xmlElement15 = xmlElement4.addSubElement(xmlElement8);
    java.lang.String str16 = xmlElement4.getData();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement18 = xmlElement4.getElement((-1));
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
    org.junit.Assert.assertTrue(i9 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str16 + "' != '" + "hi!"+ "'", str16.equals("hi!"));

  }

  @Test
  public void test136() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test136"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement3 = new XmlElement("hi!");
    java.util.Observer observer4 = null;
    xmlElement3.deleteObserver(observer4);
    xmlElement3.setName("");
    XmlElement xmlElement9 = new XmlElement("hi!");
    int i10 = xmlElement9.countObservers();
    java.util.Enumeration enumeration11 = xmlElement9.getAttributeNames();
    xmlElement3.insertElement(xmlElement9, (int)(byte)0);
    XmlElement xmlElement16 = xmlElement3.addSubElement("hi!", "");
    java.lang.String str19 = xmlElement16.getAttribute("hi!", "");
    XmlElement xmlElement21 = new XmlElement("hi!");
    XmlElement xmlElement24 = xmlElement21.addSubElement("hi!", "hi!");
    java.util.Observer observer25 = null;
    xmlElement24.deleteObserver(observer25);
    java.util.Observer observer27 = null;
    xmlElement24.deleteObserver(observer27);
    xmlElement24.setData("");
    xmlElement24.setName("hi!");
    xmlElement24.setName("");
    XmlElement xmlElement35 = xmlElement16.addSubElement(xmlElement24);
    xmlElement1.append(xmlElement24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str19 + "' != '" + ""+ "'", str19.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement35);

  }

  @Test
  public void test137() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test137"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = xmlElement1.addSubElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = xmlElement1.getAttributes();
    XmlElement xmlElement9 = new XmlElement("hi!");
    int i10 = xmlElement9.countObservers();
    java.lang.Object obj13 = xmlElement9.addAttribute("hi!", "");
    boolean b14 = xmlElement1.addElement(xmlElement9);
    XmlElement xmlElement16 = xmlElement9.addSubElement("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement16);

  }

  @Test
  public void test138() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test138"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj5 = xmlElement1.addAttribute("hi!", "");
    xmlElement1.removeAllElements();
    XmlElement xmlElement8 = new XmlElement("hi!");
    int i9 = xmlElement8.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = xmlElement8.getAttributes();
    XmlElement xmlElement12 = new XmlElement("hi!");
    int i13 = xmlElement12.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = xmlElement12.getAttributes();
    boolean b15 = xmlElement8.addElement(xmlElement12);
    xmlElement12.removeFromParent();
    XmlElement xmlElement17 = xmlElement1.addSubElement(xmlElement12);
    java.lang.Object obj20 = xmlElement1.addAttribute("", "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i9 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj20);

  }

  @Test
  public void test139() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test139"); }


    XmlElement xmlElement0 = new XmlElement();
    XmlElement xmlElement2 = new XmlElement("hi!");
    java.util.Observer observer3 = null;
    xmlElement2.deleteObserver(observer3);
    boolean b6 = xmlElement2.equals((java.lang.Object)100L);
    xmlElement0.setParent(xmlElement2);
    XmlElement xmlElement10 = new XmlElement("hi!", "hi!");
    boolean b11 = xmlElement0.equals((java.lang.Object)"hi!");
    XmlElement xmlElement12 = new XmlElement();
    java.lang.String str14 = xmlElement12.getAttribute("");
    XmlElement xmlElement16 = new XmlElement("hi!");
    XmlElement xmlElement19 = xmlElement16.addSubElement("hi!", "hi!");
    XmlElement xmlElement21 = new XmlElement("hi!");
    int i22 = xmlElement21.countObservers();
    XmlElement.printNode(xmlElement21, "hi!");
    XmlElement xmlElement25 = xmlElement19.removeElement(xmlElement21);
    xmlElement21.notifyObservers();
    XmlElement xmlElement29 = xmlElement21.addSubElement("", "");
    XmlElement xmlElement31 = new XmlElement("hi!");
    int i32 = xmlElement31.countObservers();
    java.util.Enumeration enumeration33 = xmlElement31.getAttributeNames();
    boolean b34 = xmlElement21.addElement(xmlElement31);
    boolean b35 = xmlElement12.addElement(xmlElement21);
    XmlElement xmlElement36 = xmlElement0.addSubElement(xmlElement12);
    java.util.Observer observer37 = null;
    xmlElement36.deleteObserver(observer37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i22 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i32 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration33);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b34 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b35 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement36);

  }

  @Test
  public void test140() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test140"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    java.lang.String str7 = xmlElement1.getAttribute("", "");
    XmlElement.printNode(xmlElement1, "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + ""+ "'", str7.equals(""));

  }

  @Test
  public void test141() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test141"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement6 = new XmlElement("hi!");
    int i7 = xmlElement6.countObservers();
    XmlElement.printNode(xmlElement6, "hi!");
    java.util.List list10 = xmlElement6.getElements();
    boolean b11 = xmlElement1.addElement(xmlElement6);
    java.util.Observer observer12 = null;
    xmlElement1.deleteObserver(observer12);
    XmlElement xmlElement16 = new XmlElement("hi!");
    XmlElement xmlElement19 = xmlElement16.addSubElement("hi!", "hi!");
    java.util.Observer observer20 = null;
    xmlElement19.deleteObserver(observer20);
    java.lang.String str22 = xmlElement19.getName();
    java.lang.Object obj23 = xmlElement19.clone();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str24 = xmlElement19.getAttributes();
    XmlElement xmlElement25 = new XmlElement("hi!", hashtable_str_str24);
    xmlElement1.notifyObservers((java.lang.Object)xmlElement25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str22 + "' != '" + "hi!"+ "'", str22.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str24);

  }

  @Test
  public void test142() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test142"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Observer observer2 = null;
    xmlElement1.deleteObserver(observer2);
    boolean b5 = xmlElement1.equals((java.lang.Object)100L);
    xmlElement1.removeFromParent();
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
    org.junit.Assert.assertTrue(b5 == false);

  }

  @Test
  public void test143() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test143"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.lang.Object obj5 = xmlElement1.addAttribute("hi!", "");
    XmlElement xmlElement6 = xmlElement1.getParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement6);

  }

  @Test
  public void test144() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test144"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str3 = xmlElement1.getAttributes();
    boolean b4 = xmlElement1.hasChanged();
    XmlElement xmlElement7 = new XmlElement("hi!", "hi!");
    XmlElement xmlElement8 = new XmlElement();
    XmlElement xmlElement9 = xmlElement7.removeElement(xmlElement8);
    boolean b10 = xmlElement1.addElement(xmlElement8);
    xmlElement8.removeAllElements();
    java.util.List list12 = xmlElement8.getElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b4 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list12);

  }

  @Test
  public void test145() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test145"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    int i4 = xmlElement1.count();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = xmlElement1.getAttributes();
    XmlElement xmlElement7 = xmlElement1.getElement("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i4 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement7);

  }

  @Test
  public void test146() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test146"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    java.util.Observer observer5 = null;
    xmlElement4.deleteObserver(observer5);
    java.lang.Object obj7 = xmlElement4.clone();
    XmlElement xmlElement9 = new XmlElement("hi!");
    XmlElement xmlElement12 = xmlElement9.addSubElement("hi!", "hi!");
    XmlElement xmlElement14 = new XmlElement("hi!");
    int i15 = xmlElement14.countObservers();
    XmlElement.printNode(xmlElement14, "hi!");
    XmlElement xmlElement18 = xmlElement12.removeElement(xmlElement14);
    xmlElement14.notifyObservers();
    // The following exception was thrown during execution in test generation
    try {
    xmlElement4.insertElement(xmlElement14, (int)(byte)1);
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
    org.junit.Assert.assertNotNull(obj7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i15 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement18);

  }

  @Test
  public void test147() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test147"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    int i2 = xmlElement1.countObservers();
    java.util.List list3 = xmlElement1.getElements();
    int i4 = xmlElement1.count();
    XmlElement xmlElement6 = xmlElement1.addSubElement("hi!");
    xmlElement6.setData("hi!");
    XmlElement xmlElement10 = new XmlElement("hi!");
    xmlElement6.setParent(xmlElement10);
    xmlElement10.deleteObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i4 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement6);

  }

  @Test
  public void test148() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test148"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement xmlElement6 = new XmlElement("hi!");
    int i7 = xmlElement6.countObservers();
    XmlElement.printNode(xmlElement6, "hi!");
    XmlElement xmlElement10 = xmlElement4.removeElement(xmlElement6);
    XmlElement xmlElement13 = xmlElement6.addSubElement("", "hi!");
    java.util.Observer observer14 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement6.addObserver(observer14);
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
    org.junit.Assert.assertTrue(i7 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement13);

  }

  @Test
  public void test149() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test149"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    XmlElement xmlElement4 = xmlElement1.addSubElement("hi!", "hi!");
    XmlElement.printNode(xmlElement4, "");
    XmlElement xmlElement8 = new XmlElement("hi!");
    int i9 = xmlElement8.countObservers();
    java.lang.Object obj10 = xmlElement8.clone();
    XmlElement.printNode(xmlElement8, "hi!");
    java.util.Observer observer13 = null;
    xmlElement8.deleteObserver(observer13);
    XmlElement xmlElement15 = xmlElement4.addSubElement(xmlElement8);
    java.lang.String str16 = xmlElement8.getData();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i9 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str16 + "' != '" + ""+ "'", str16.equals(""));

  }

}
