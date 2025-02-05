package mypackage;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0_it1_livello2_it2_livello3 {

  public static boolean debug = false;

  @Test
  public void test01() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test01"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str10 = xmlElement5.getAttribute("hi!", "");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);

  }

  @Test
  public void test02() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test02"); }


    XmlElement xmlElement0 = new XmlElement();
    XmlElement xmlElement1 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement0.append(xmlElement1);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test03() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test03"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    // The following exception was thrown during execution in test generation
    try {
    java.util.Enumeration enumeration8 = xmlElement5.getAttributeNames();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);

  }

  @Test
  public void test04() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test04"); }


    XmlElement xmlElement2 = new XmlElement("hi!", "hi!");
    java.lang.Object obj3 = null;
    xmlElement2.notifyObservers(obj3);
    xmlElement2.setName("hi!");

  }

  @Test
  public void test05() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test05"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.lang.Object obj3 = xmlElement2.clone();
    xmlElement2.setName("");
    boolean b7 = xmlElement2.equals((java.lang.Object)(short)0);
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
    org.junit.Assert.assertNotNull(obj3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);

  }

  @Test
  public void test06() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test06"); }


    XmlElement xmlElement2 = new XmlElement("hi!", "hi!");
    java.lang.Object obj3 = null;
    xmlElement2.notifyObservers(obj3);
    java.lang.String str7 = xmlElement2.getAttribute("hi!", "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + ""+ "'", str7.equals(""));

  }

  @Test
  public void test07() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test07"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    xmlElement5.setAttributes(hashtable_str_str8);
    int i10 = xmlElement5.count();
    int i11 = xmlElement5.count();
    java.util.List list12 = xmlElement5.getElements();
    java.util.List list13 = xmlElement5.getElements();
    XmlElement xmlElement15 = xmlElement5.getElement("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i11 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement15);

  }

  @Test
  public void test08() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test08"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    xmlElement5.setAttributes(hashtable_str_str8);
    int i10 = xmlElement5.count();
    int i11 = xmlElement5.count();
    java.util.List list12 = xmlElement5.getElements();
    XmlElement xmlElement15 = new XmlElement("hi!", "hi!");
    java.lang.Object obj16 = null;
    xmlElement15.notifyObservers(obj16);
    // The following exception was thrown during execution in test generation
    try {
    xmlElement5.insertElement(xmlElement15, (-1));
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i11 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list12);

  }

  @Test
  public void test09() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test09"); }


    XmlElement xmlElement2 = new XmlElement("hi!", "hi!");
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
  public void test10() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test10"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.lang.Object obj3 = xmlElement2.clone();
    xmlElement2.setName("");
    boolean b7 = xmlElement2.equals((java.lang.Object)(short)0);
    java.lang.Object obj8 = xmlElement2.clone();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj8);

  }

  @Test
  public void test11() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test11"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    xmlElement5.setAttributes(hashtable_str_str8);
    java.util.Observer observer10 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement5.addObserver(observer10);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);

  }

  @Test
  public void test12() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test12"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.lang.Object obj3 = xmlElement2.clone();
    xmlElement2.setName("");
    java.util.Observer observer6 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement2.addObserver(observer6);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);

  }

  @Test
  public void test13() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test13"); }


    XmlElement xmlElement2 = new XmlElement("", "");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("", hashtable_str_str7);
    java.lang.Object obj9 = xmlElement8.clone();
    boolean b10 = xmlElement5.equals((java.lang.Object)xmlElement8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    xmlElement8.setAttributes(hashtable_str_str11);
    int i13 = xmlElement8.count();
    int i14 = xmlElement8.count();
    boolean b15 = xmlElement2.equals((java.lang.Object)xmlElement8);
    XmlElement xmlElement16 = xmlElement8.getParent();
    // The following exception was thrown during execution in test generation
    try {
    java.util.Enumeration enumeration17 = xmlElement16.getAttributeNames();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i14 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement16);

  }

  @Test
  public void test14() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test14"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    xmlElement5.setAttributes(hashtable_str_str8);
    int i10 = xmlElement5.count();
    int i11 = xmlElement5.count();
    java.util.Observer observer12 = null;
    xmlElement5.deleteObserver(observer12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i11 == 0);

  }

  @Test
  public void test15() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test15"); }


    XmlElement xmlElement2 = new XmlElement("", "");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("", hashtable_str_str7);
    java.lang.Object obj9 = xmlElement8.clone();
    boolean b10 = xmlElement5.equals((java.lang.Object)xmlElement8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    xmlElement8.setAttributes(hashtable_str_str11);
    int i13 = xmlElement8.count();
    int i14 = xmlElement8.count();
    boolean b15 = xmlElement2.equals((java.lang.Object)xmlElement8);
    XmlElement xmlElement16 = xmlElement2.getParent();
    // The following exception was thrown during execution in test generation
    try {
    java.lang.Object obj17 = xmlElement16.clone();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i14 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement16);

  }

  @Test
  public void test16() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test16"); }


    XmlElement xmlElement2 = new XmlElement("", "");
    XmlElement xmlElement4 = new XmlElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str6 = null;
    XmlElement xmlElement7 = new XmlElement("", hashtable_str_str6);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("", hashtable_str_str9);
    java.lang.Object obj11 = xmlElement10.clone();
    boolean b12 = xmlElement7.equals((java.lang.Object)xmlElement10);
    boolean b13 = xmlElement4.addElement(xmlElement10);
    XmlElement xmlElement14 = xmlElement2.removeElement(xmlElement4);
    // The following exception was thrown during execution in test generation
    try {
    xmlElement14.notifyObservers((java.lang.Object)100L);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);

  }

  @Test
  public void test17() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test17"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    xmlElement5.setAttributes(hashtable_str_str8);
    int i10 = xmlElement5.count();
    java.util.List list11 = xmlElement5.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = xmlElement5.getAttributes();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(hashtable_str_str12);

  }

  @Test
  public void test18() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test18"); }


    XmlElement xmlElement2 = new XmlElement("hi!", "hi!");
    java.lang.Object obj3 = null;
    xmlElement2.notifyObservers(obj3);
    java.lang.String str6 = xmlElement2.getAttribute("hi!");
    int i7 = xmlElement2.count();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement9 = xmlElement2.removeElement((int)(short)10);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i7 == 0);

  }

  @Test
  public void test19() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test19"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    xmlElement2.notifyObservers((java.lang.Object)(-1));
    int i5 = xmlElement2.countObservers();
    XmlElement xmlElement8 = new XmlElement("hi!", "hi!");
    java.lang.Object obj9 = null;
    xmlElement8.notifyObservers(obj9);
    java.lang.String str12 = xmlElement8.getAttribute("hi!");
    XmlElement.printNode(xmlElement8, "hi!");
    xmlElement2.setParent(xmlElement8);
    java.util.Observer observer16 = null;
    xmlElement8.deleteObserver(observer16);
    XmlElement xmlElement18 = new XmlElement();
    java.util.Observer observer19 = null;
    xmlElement18.deleteObserver(observer19);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str22 = null;
    XmlElement xmlElement23 = new XmlElement("", hashtable_str_str22);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str25 = null;
    XmlElement xmlElement26 = new XmlElement("", hashtable_str_str25);
    java.lang.Object obj27 = xmlElement26.clone();
    boolean b28 = xmlElement23.equals((java.lang.Object)xmlElement26);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str29 = null;
    xmlElement26.setAttributes(hashtable_str_str29);
    int i31 = xmlElement26.count();
    int i32 = xmlElement26.count();
    java.util.List list33 = xmlElement26.getElements();
    java.util.List list34 = xmlElement26.getElements();
    boolean b35 = xmlElement18.equals((java.lang.Object)list34);
    // The following exception was thrown during execution in test generation
    try {
    xmlElement8.insertElement(xmlElement18, (int)(short)100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj27);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b28 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i31 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i32 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list33);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list34);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b35 == false);

  }

  @Test
  public void test20() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test20"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    xmlElement5.setAttributes(hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("", hashtable_str_str11);
    java.lang.Object obj13 = xmlElement12.clone();
    xmlElement5.setParent(xmlElement12);
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str16 = xmlElement12.getAttribute("");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj13);

  }

  @Test
  public void test21() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test21"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    XmlElement xmlElement10 = new XmlElement("", "");
    xmlElement5.append(xmlElement10);
    XmlElement xmlElement12 = new XmlElement();
    xmlElement12.removeFromParent();
    xmlElement10.append(xmlElement12);
    boolean b15 = xmlElement12.hasChanged();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);

  }

  @Test
  public void test22() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test22"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    xmlElement5.setAttributes(hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("", hashtable_str_str11);
    java.lang.Object obj13 = xmlElement12.clone();
    xmlElement5.setParent(xmlElement12);
    xmlElement12.removeAllElements();
    XmlElement xmlElement18 = new XmlElement("hi!", "hi!");
    xmlElement12.setParent(xmlElement18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj13);

  }

  @Test
  public void test23() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test23"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    xmlElement5.setAttributes(hashtable_str_str8);
    int i10 = xmlElement5.count();
    java.util.Observer observer11 = null;
    xmlElement5.deleteObserver(observer11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);

  }

  @Test
  public void test24() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test24"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement3 = xmlElement1.removeElement(10);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test25() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test25"); }


    XmlElement xmlElement2 = new XmlElement("hi!", "hi!");
    xmlElement2.setName("");

  }

  @Test
  public void test26() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test26"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    XmlElement xmlElement10 = new XmlElement("", "");
    XmlElement xmlElement12 = new XmlElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("", hashtable_str_str14);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str17 = null;
    XmlElement xmlElement18 = new XmlElement("", hashtable_str_str17);
    java.lang.Object obj19 = xmlElement18.clone();
    boolean b20 = xmlElement15.equals((java.lang.Object)xmlElement18);
    boolean b21 = xmlElement12.addElement(xmlElement18);
    XmlElement xmlElement22 = xmlElement10.removeElement(xmlElement12);
    xmlElement2.setParent(xmlElement10);
    XmlElement xmlElement26 = xmlElement10.addSubElement("hi!", "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b20 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement26);

  }

  @Test
  public void test27() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test27"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    boolean b8 = xmlElement2.hasChanged();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);

  }

  @Test
  public void test28() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test28"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    xmlElement2.notifyObservers((java.lang.Object)(-1));
    int i5 = xmlElement2.countObservers();
    XmlElement xmlElement8 = new XmlElement("hi!", "hi!");
    java.lang.Object obj9 = null;
    xmlElement8.notifyObservers(obj9);
    java.lang.String str12 = xmlElement8.getAttribute("hi!");
    XmlElement.printNode(xmlElement8, "hi!");
    xmlElement2.setParent(xmlElement8);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement17 = xmlElement8.removeElement(100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str12);

  }

  @Test
  public void test29() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test29"); }


    XmlElement xmlElement2 = new XmlElement("", "");
    XmlElement xmlElement4 = new XmlElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str6 = null;
    XmlElement xmlElement7 = new XmlElement("", hashtable_str_str6);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("", hashtable_str_str9);
    java.lang.Object obj11 = xmlElement10.clone();
    boolean b12 = xmlElement7.equals((java.lang.Object)xmlElement10);
    boolean b13 = xmlElement4.addElement(xmlElement10);
    XmlElement xmlElement14 = xmlElement2.removeElement(xmlElement4);
    java.lang.String str15 = xmlElement4.getName();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b12 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str15 + "' != '" + ""+ "'", str15.equals(""));

  }

  @Test
  public void test30() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test30"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    xmlElement2.notifyObservers((java.lang.Object)(-1));
    int i5 = xmlElement2.countObservers();
    XmlElement xmlElement8 = new XmlElement("", "");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("", hashtable_str_str10);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str13 = null;
    XmlElement xmlElement14 = new XmlElement("", hashtable_str_str13);
    java.lang.Object obj15 = xmlElement14.clone();
    boolean b16 = xmlElement11.equals((java.lang.Object)xmlElement14);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str17 = null;
    xmlElement14.setAttributes(hashtable_str_str17);
    int i19 = xmlElement14.count();
    int i20 = xmlElement14.count();
    boolean b21 = xmlElement8.equals((java.lang.Object)xmlElement14);
    XmlElement xmlElement22 = xmlElement14.getParent();
    xmlElement2.setParent(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b16 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i19 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i20 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);

  }

  @Test
  public void test31() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test31"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    XmlElement xmlElement10 = new XmlElement("", "");
    xmlElement5.append(xmlElement10);
    XmlElement xmlElement13 = xmlElement10.getElement("");
    XmlElement xmlElement14 = new XmlElement();
    xmlElement10.append(xmlElement14);
    xmlElement10.notifyObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement13);

  }

  @Test
  public void test32() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test32"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    XmlElement xmlElement10 = new XmlElement("", "");
    xmlElement5.append(xmlElement10);
    XmlElement xmlElement13 = xmlElement10.getElement("");
    XmlElement xmlElement14 = new XmlElement();
    xmlElement10.append(xmlElement14);
    java.lang.String str16 = xmlElement14.getName();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str16);

  }

  @Test
  public void test33() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test33"); }


    XmlElement xmlElement0 = new XmlElement();
    java.util.Observer observer1 = null;
    xmlElement0.deleteObserver(observer1);
    java.util.List list3 = xmlElement0.getElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list3);

  }

  @Test
  public void test34() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test34"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    XmlElement xmlElement10 = new XmlElement("", "");
    XmlElement xmlElement12 = new XmlElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("", hashtable_str_str14);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str17 = null;
    XmlElement xmlElement18 = new XmlElement("", hashtable_str_str17);
    java.lang.Object obj19 = xmlElement18.clone();
    boolean b20 = xmlElement15.equals((java.lang.Object)xmlElement18);
    boolean b21 = xmlElement12.addElement(xmlElement18);
    XmlElement xmlElement22 = xmlElement10.removeElement(xmlElement12);
    xmlElement2.setParent(xmlElement10);
    xmlElement2.removeAllElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b20 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);

  }

  @Test
  public void test35() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test35"); }


    XmlElement xmlElement2 = new XmlElement("", "hi!");
    xmlElement2.removeFromParent();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("", hashtable_str_str8);
    java.lang.Object obj10 = xmlElement9.clone();
    boolean b11 = xmlElement6.equals((java.lang.Object)xmlElement9);
    XmlElement xmlElement14 = new XmlElement("", "");
    xmlElement9.append(xmlElement14);
    XmlElement xmlElement16 = new XmlElement();
    xmlElement16.removeFromParent();
    xmlElement14.append(xmlElement16);
    xmlElement14.removeAllElements();
    // The following exception was thrown during execution in test generation
    try {
    xmlElement2.insertElement(xmlElement14, (int)(byte)100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == true);

  }

  @Test
  public void test36() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test36"); }


    XmlElement xmlElement2 = new XmlElement("", "");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("", hashtable_str_str7);
    java.lang.Object obj9 = xmlElement8.clone();
    boolean b10 = xmlElement5.equals((java.lang.Object)xmlElement8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    xmlElement8.setAttributes(hashtable_str_str11);
    int i13 = xmlElement8.count();
    int i14 = xmlElement8.count();
    boolean b15 = xmlElement2.equals((java.lang.Object)xmlElement8);
    xmlElement2.removeAllElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str18 = null;
    XmlElement xmlElement19 = new XmlElement("", hashtable_str_str18);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str21 = null;
    XmlElement xmlElement22 = new XmlElement("", hashtable_str_str21);
    java.lang.Object obj23 = xmlElement22.clone();
    boolean b24 = xmlElement19.equals((java.lang.Object)xmlElement22);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str25 = null;
    xmlElement22.setAttributes(hashtable_str_str25);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str28 = null;
    XmlElement xmlElement29 = new XmlElement("", hashtable_str_str28);
    java.lang.Object obj30 = xmlElement29.clone();
    xmlElement22.setParent(xmlElement29);
    xmlElement2.append(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i14 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b24 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj30);

  }

  @Test
  public void test37() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test37"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.util.Enumeration enumeration2 = xmlElement1.getAttributeNames();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(enumeration2);

  }

  @Test
  public void test38() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test38"); }


    XmlElement xmlElement1 = new XmlElement("");
    java.lang.Object obj4 = xmlElement1.addAttribute("hi!", "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj4);

  }

  @Test
  public void test39() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test39"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    xmlElement5.setAttributes(hashtable_str_str8);
    int i10 = xmlElement5.count();
    int i11 = xmlElement5.count();
    java.util.List list12 = xmlElement5.getElements();
    xmlElement5.removeAllElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = xmlElement5.getAttributes();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i11 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(hashtable_str_str14);

  }

  @Test
  public void test40() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test40"); }


    XmlElement xmlElement2 = new XmlElement("", "");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("", hashtable_str_str7);
    java.lang.Object obj9 = xmlElement8.clone();
    boolean b10 = xmlElement5.equals((java.lang.Object)xmlElement8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    xmlElement8.setAttributes(hashtable_str_str11);
    int i13 = xmlElement8.count();
    int i14 = xmlElement8.count();
    boolean b15 = xmlElement2.equals((java.lang.Object)xmlElement8);
    XmlElement xmlElement16 = xmlElement2.getParent();
    // The following exception was thrown during execution in test generation
    try {
    xmlElement16.removeFromParent();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i14 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement16);

  }

  @Test
  public void test41() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test41"); }


    XmlElement xmlElement2 = new XmlElement("", "hi!");
    xmlElement2.removeFromParent();
    XmlElement.printNode(xmlElement2, "");

  }

  @Test
  public void test42() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test42"); }


    XmlElement xmlElement2 = new XmlElement("hi!", "");

  }

  @Test
  public void test43() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test43"); }


    XmlElement xmlElement2 = new XmlElement("", "");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("", hashtable_str_str7);
    java.lang.Object obj9 = xmlElement8.clone();
    boolean b10 = xmlElement5.equals((java.lang.Object)xmlElement8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    xmlElement8.setAttributes(hashtable_str_str11);
    int i13 = xmlElement8.count();
    int i14 = xmlElement8.count();
    boolean b15 = xmlElement2.equals((java.lang.Object)xmlElement8);
    XmlElement xmlElement16 = new XmlElement();
    java.util.Observer observer17 = null;
    xmlElement16.deleteObserver(observer17);
    java.lang.String str21 = xmlElement16.getAttribute("", "");
    xmlElement16.setName("hi!");
    // The following exception was thrown during execution in test generation
    try {
    xmlElement8.insertElement(xmlElement16, (-1));
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i14 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str21 + "' != '" + ""+ "'", str21.equals(""));

  }

  @Test
  public void test44() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test44"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    xmlElement5.setAttributes(hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("", hashtable_str_str11);
    java.lang.Object obj13 = xmlElement12.clone();
    xmlElement5.setParent(xmlElement12);
    xmlElement5.notifyObservers((java.lang.Object)'4');
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj13);

  }

  @Test
  public void test45() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test45"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    XmlElement xmlElement10 = new XmlElement("", "");
    xmlElement5.append(xmlElement10);
    XmlElement xmlElement12 = new XmlElement();
    xmlElement12.removeFromParent();
    xmlElement10.append(xmlElement12);
    java.util.List list15 = xmlElement10.getElements();
    java.lang.String str16 = xmlElement10.getData();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str16 + "' != '" + ""+ "'", str16.equals(""));

  }

  @Test
  public void test46() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test46"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    xmlElement5.setAttributes(hashtable_str_str8);
    int i10 = xmlElement5.count();
    int i11 = xmlElement5.count();
    java.util.List list12 = xmlElement5.getElements();
    xmlElement5.notifyObservers();
    int i14 = xmlElement5.countObservers();
    java.util.List list15 = xmlElement5.getElements();
    XmlElement xmlElement18 = new XmlElement("", "");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str20 = null;
    XmlElement xmlElement21 = new XmlElement("", hashtable_str_str20);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str23 = null;
    XmlElement xmlElement24 = new XmlElement("", hashtable_str_str23);
    java.lang.Object obj25 = xmlElement24.clone();
    boolean b26 = xmlElement21.equals((java.lang.Object)xmlElement24);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str27 = null;
    xmlElement24.setAttributes(hashtable_str_str27);
    int i29 = xmlElement24.count();
    int i30 = xmlElement24.count();
    boolean b31 = xmlElement18.equals((java.lang.Object)xmlElement24);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str32 = xmlElement24.getAttributes();
    xmlElement5.setParent(xmlElement24);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i11 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i14 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b26 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i29 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i30 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b31 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(hashtable_str_str32);

  }

  @Test
  public void test47() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test47"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    XmlElement xmlElement10 = new XmlElement("", "");
    xmlElement5.append(xmlElement10);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str13 = null;
    XmlElement xmlElement14 = new XmlElement("", hashtable_str_str13);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str16 = null;
    XmlElement xmlElement17 = new XmlElement("", hashtable_str_str16);
    java.lang.Object obj18 = xmlElement17.clone();
    boolean b19 = xmlElement14.equals((java.lang.Object)xmlElement17);
    XmlElement xmlElement22 = new XmlElement("", "");
    XmlElement xmlElement24 = new XmlElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str26 = null;
    XmlElement xmlElement27 = new XmlElement("", hashtable_str_str26);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str29 = null;
    XmlElement xmlElement30 = new XmlElement("", hashtable_str_str29);
    java.lang.Object obj31 = xmlElement30.clone();
    boolean b32 = xmlElement27.equals((java.lang.Object)xmlElement30);
    boolean b33 = xmlElement24.addElement(xmlElement30);
    XmlElement xmlElement34 = xmlElement22.removeElement(xmlElement24);
    xmlElement14.setParent(xmlElement22);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str37 = null;
    XmlElement xmlElement38 = new XmlElement("", hashtable_str_str37);
    xmlElement38.notifyObservers((java.lang.Object)(-1));
    int i41 = xmlElement38.countObservers();
    XmlElement xmlElement44 = new XmlElement("hi!", "hi!");
    java.lang.Object obj45 = null;
    xmlElement44.notifyObservers(obj45);
    java.lang.String str48 = xmlElement44.getAttribute("hi!");
    XmlElement.printNode(xmlElement44, "hi!");
    xmlElement38.setParent(xmlElement44);
    java.util.Observer observer52 = null;
    xmlElement44.deleteObserver(observer52);
    boolean b54 = xmlElement22.addElement(xmlElement44);
    xmlElement10.notifyObservers((java.lang.Object)b54);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b19 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj31);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b32 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b33 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement34);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i41 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str48);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b54 == true);

  }

  @Test
  public void test48() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test48"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    xmlElement5.setAttributes(hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("", hashtable_str_str11);
    java.lang.Object obj13 = xmlElement12.clone();
    xmlElement5.setParent(xmlElement12);
    XmlElement xmlElement17 = new XmlElement("hi!", "hi!");
    xmlElement17.notifyObservers();
    // The following exception was thrown during execution in test generation
    try {
    xmlElement5.insertElement(xmlElement17, (int)'#');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj13);

  }

  @Test
  public void test49() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test49"); }


    XmlElement xmlElement2 = new XmlElement("", "");
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement4 = xmlElement2.removeElement(1);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test50() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test50"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.lang.Object obj3 = xmlElement2.clone();
    xmlElement2.setName("");
    boolean b7 = xmlElement2.equals((java.lang.Object)(short)0);
    boolean b8 = xmlElement2.hasChanged();
    int i9 = xmlElement2.countObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i9 == 0);

  }

  @Test
  public void test51() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test51"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    XmlElement xmlElement10 = new XmlElement("", "");
    xmlElement5.append(xmlElement10);
    XmlElement xmlElement12 = new XmlElement();
    xmlElement12.removeFromParent();
    xmlElement10.append(xmlElement12);
    xmlElement10.removeAllElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str17 = null;
    XmlElement xmlElement18 = new XmlElement("", hashtable_str_str17);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str20 = null;
    XmlElement xmlElement21 = new XmlElement("", hashtable_str_str20);
    java.lang.Object obj22 = xmlElement21.clone();
    boolean b23 = xmlElement18.equals((java.lang.Object)xmlElement21);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str24 = null;
    xmlElement21.setAttributes(hashtable_str_str24);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str27 = null;
    XmlElement xmlElement28 = new XmlElement("", hashtable_str_str27);
    java.lang.Object obj29 = xmlElement28.clone();
    xmlElement21.setParent(xmlElement28);
    // The following exception was thrown during execution in test generation
    try {
    xmlElement10.insertElement(xmlElement21, 1);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b23 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj29);

  }

  @Test
  public void test52() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test52"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    XmlElement xmlElement10 = new XmlElement("", "");
    XmlElement xmlElement12 = new XmlElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str14 = null;
    XmlElement xmlElement15 = new XmlElement("", hashtable_str_str14);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str17 = null;
    XmlElement xmlElement18 = new XmlElement("", hashtable_str_str17);
    java.lang.Object obj19 = xmlElement18.clone();
    boolean b20 = xmlElement15.equals((java.lang.Object)xmlElement18);
    boolean b21 = xmlElement12.addElement(xmlElement18);
    XmlElement xmlElement22 = xmlElement10.removeElement(xmlElement12);
    xmlElement2.setParent(xmlElement10);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str25 = null;
    XmlElement xmlElement26 = new XmlElement("", hashtable_str_str25);
    java.lang.Object obj27 = xmlElement26.clone();
    xmlElement26.setData("hi!");
    int i30 = xmlElement26.count();
    xmlElement2.append(xmlElement26);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement33 = xmlElement2.removeElement((int)(short)100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b20 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj27);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i30 == 0);

  }

  @Test
  public void test53() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test53"); }


    XmlElement xmlElement1 = new XmlElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str2 = xmlElement1.getAttributes();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("", hashtable_str_str7);
    java.lang.Object obj9 = xmlElement8.clone();
    boolean b10 = xmlElement5.equals((java.lang.Object)xmlElement8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    xmlElement8.setAttributes(hashtable_str_str11);
    int i13 = xmlElement8.count();
    int i14 = xmlElement8.count();
    java.util.List list15 = xmlElement8.getElements();
    xmlElement8.notifyObservers();
    XmlElement xmlElement19 = new XmlElement("hi!", "hi!");
    xmlElement19.removeAllElements();
    boolean b21 = xmlElement8.addElement(xmlElement19);
    boolean b22 = xmlElement1.addElement(xmlElement8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str24 = null;
    XmlElement xmlElement25 = new XmlElement("", hashtable_str_str24);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str27 = null;
    XmlElement xmlElement28 = new XmlElement("", hashtable_str_str27);
    java.lang.Object obj29 = xmlElement28.clone();
    boolean b30 = xmlElement25.equals((java.lang.Object)xmlElement28);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str31 = null;
    xmlElement28.setAttributes(hashtable_str_str31);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str34 = null;
    XmlElement xmlElement35 = new XmlElement("", hashtable_str_str34);
    java.lang.Object obj36 = xmlElement35.clone();
    xmlElement28.setParent(xmlElement35);
    xmlElement35.removeAllElements();
    xmlElement1.append(xmlElement35);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i14 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b21 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b22 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj29);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b30 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj36);

  }

  @Test
  public void test54() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test54"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    xmlElement2.notifyObservers((java.lang.Object)(-1));
    int i5 = xmlElement2.countObservers();
    XmlElement xmlElement8 = new XmlElement("hi!", "hi!");
    java.lang.Object obj9 = null;
    xmlElement8.notifyObservers(obj9);
    java.lang.String str12 = xmlElement8.getAttribute("hi!");
    XmlElement.printNode(xmlElement8, "hi!");
    xmlElement2.setParent(xmlElement8);
    java.util.Observer observer16 = null;
    xmlElement8.deleteObserver(observer16);
    XmlElement xmlElement18 = new XmlElement();
    xmlElement8.notifyObservers((java.lang.Object)xmlElement18);
    java.lang.String str20 = xmlElement8.getName();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str20 + "' != '" + "hi!"+ "'", str20.equals("hi!"));

  }

  @Test
  public void test55() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test55"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.lang.Object obj3 = xmlElement2.clone();
    xmlElement2.setName("");
    boolean b7 = xmlElement2.equals((java.lang.Object)(short)0);
    boolean b8 = xmlElement2.hasChanged();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str10 = null;
    XmlElement xmlElement11 = new XmlElement("", hashtable_str_str10);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str13 = null;
    XmlElement xmlElement14 = new XmlElement("", hashtable_str_str13);
    java.lang.Object obj15 = xmlElement14.clone();
    boolean b16 = xmlElement11.equals((java.lang.Object)xmlElement14);
    XmlElement xmlElement17 = xmlElement2.removeElement(xmlElement11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b8 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b16 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement17);

  }

  @Test
  public void test56() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test56"); }


    XmlElement xmlElement2 = new XmlElement("", "");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("", hashtable_str_str7);
    java.lang.Object obj9 = xmlElement8.clone();
    boolean b10 = xmlElement5.equals((java.lang.Object)xmlElement8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    xmlElement8.setAttributes(hashtable_str_str11);
    int i13 = xmlElement8.count();
    int i14 = xmlElement8.count();
    boolean b15 = xmlElement2.equals((java.lang.Object)xmlElement8);
    XmlElement xmlElement17 = xmlElement8.getElement("hi!");
    XmlElement xmlElement19 = new XmlElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str20 = xmlElement19.getAttributes();
    // The following exception was thrown during execution in test generation
    try {
    xmlElement17.setAttributes(hashtable_str_str20);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i14 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str20);

  }

  @Test
  public void test57() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test57"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    xmlElement5.setAttributes(hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("", hashtable_str_str11);
    java.lang.Object obj13 = xmlElement12.clone();
    xmlElement5.setParent(xmlElement12);
    xmlElement5.setData("hi!");
    // The following exception was thrown during execution in test generation
    try {
    XmlElement.printNode(xmlElement5, "hi!");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj13);

  }

  @Test
  public void test58() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test58"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.lang.Object obj3 = xmlElement2.clone();
    xmlElement2.setData("hi!");
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str7 = xmlElement2.getAttribute("");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);

  }

  @Test
  public void test59() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test59"); }


    XmlElement xmlElement2 = new XmlElement("", "");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("", hashtable_str_str7);
    java.lang.Object obj9 = xmlElement8.clone();
    boolean b10 = xmlElement5.equals((java.lang.Object)xmlElement8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    xmlElement8.setAttributes(hashtable_str_str11);
    int i13 = xmlElement8.count();
    int i14 = xmlElement8.count();
    boolean b15 = xmlElement2.equals((java.lang.Object)xmlElement8);
    XmlElement xmlElement17 = xmlElement8.getElement("hi!");
    XmlElement xmlElement20 = new XmlElement("hi!", "hi!");
    xmlElement20.notifyObservers();
    boolean b22 = xmlElement8.addElement(xmlElement20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i14 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement17);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b22 == true);

  }

  @Test
  public void test60() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test60"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    xmlElement5.setAttributes(hashtable_str_str8);
    int i10 = xmlElement5.count();
    int i11 = xmlElement5.count();
    java.util.List list12 = xmlElement5.getElements();
    java.util.List list13 = xmlElement5.getElements();
    java.lang.String str14 = xmlElement5.getData();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i10 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i11 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str14);

  }

  @Test
  public void test61() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test61"); }


    XmlElement xmlElement0 = new XmlElement();
    java.util.Observer observer1 = null;
    xmlElement0.deleteObserver(observer1);
    java.util.Observer observer3 = null;
    xmlElement0.deleteObserver(observer3);
    XmlElement xmlElement5 = null;
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement6 = xmlElement0.addSubElement(xmlElement5);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }

  }

  @Test
  public void test62() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test62"); }


    XmlElement xmlElement2 = new XmlElement("", "");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("", hashtable_str_str7);
    java.lang.Object obj9 = xmlElement8.clone();
    boolean b10 = xmlElement5.equals((java.lang.Object)xmlElement8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    xmlElement8.setAttributes(hashtable_str_str11);
    int i13 = xmlElement8.count();
    int i14 = xmlElement8.count();
    boolean b15 = xmlElement2.equals((java.lang.Object)xmlElement8);
    XmlElement xmlElement17 = xmlElement8.getElement("hi!");
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str19 = xmlElement17.getAttribute("hi!");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i14 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement17);

  }

  @Test
  public void test63() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test63"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    xmlElement5.setAttributes(hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("", hashtable_str_str11);
    java.lang.Object obj13 = xmlElement12.clone();
    xmlElement5.setParent(xmlElement12);
    xmlElement12.removeAllElements();
    int i16 = xmlElement12.countObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i16 == 0);

  }

  @Test
  public void test64() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test64"); }


    XmlElement xmlElement0 = new XmlElement();
    xmlElement0.removeFromParent();
    int i2 = xmlElement0.count();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i2 == 0);

  }

  @Test
  public void test65() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test65"); }


    XmlElement xmlElement1 = new XmlElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str2 = xmlElement1.getAttributes();
    java.lang.String str3 = xmlElement1.getData();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("", hashtable_str_str5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("", hashtable_str_str8);
    java.lang.Object obj10 = xmlElement9.clone();
    boolean b11 = xmlElement6.equals((java.lang.Object)xmlElement9);
    XmlElement xmlElement14 = new XmlElement("", "");
    XmlElement xmlElement16 = new XmlElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str18 = null;
    XmlElement xmlElement19 = new XmlElement("", hashtable_str_str18);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str21 = null;
    XmlElement xmlElement22 = new XmlElement("", hashtable_str_str21);
    java.lang.Object obj23 = xmlElement22.clone();
    boolean b24 = xmlElement19.equals((java.lang.Object)xmlElement22);
    boolean b25 = xmlElement16.addElement(xmlElement22);
    XmlElement xmlElement26 = xmlElement14.removeElement(xmlElement16);
    xmlElement6.setParent(xmlElement14);
    java.lang.Object obj28 = xmlElement14.clone();
    xmlElement1.append(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str3 + "' != '" + ""+ "'", str3.equals(""));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b11 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b24 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b25 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj28);

  }

  @Test
  public void test66() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test66"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.lang.Object obj3 = xmlElement2.clone();
    xmlElement2.setName("");
    xmlElement2.setData("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj3);

  }

  @Test
  public void test67() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test67"); }


    XmlElement xmlElement0 = new XmlElement();
    xmlElement0.removeFromParent();
    XmlElement xmlElement3 = new XmlElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = xmlElement3.getAttributes();
    xmlElement0.setAttributes(hashtable_str_str4);
    java.lang.String str8 = xmlElement0.getAttribute("hi!", "hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str4);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str8 + "' != '" + "hi!"+ "'", str8.equals("hi!"));

  }

  @Test
  public void test68() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test68"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    xmlElement2.notifyObservers((java.lang.Object)(-1));
    int i5 = xmlElement2.countObservers();
    // The following exception was thrown during execution in test generation
    try {
    java.util.Enumeration enumeration6 = xmlElement2.getAttributeNames();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);

  }

  @Test
  public void test69() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test69"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    xmlElement2.notifyObservers((java.lang.Object)(-1));
    int i5 = xmlElement2.countObservers();
    XmlElement xmlElement8 = new XmlElement("hi!", "hi!");
    java.lang.Object obj9 = null;
    xmlElement8.notifyObservers(obj9);
    java.lang.String str12 = xmlElement8.getAttribute("hi!");
    XmlElement.printNode(xmlElement8, "hi!");
    xmlElement2.setParent(xmlElement8);
    XmlElement xmlElement18 = xmlElement8.addSubElement("hi!", "hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i5 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str12);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement18);

  }

  @Test
  public void test70() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test70"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    XmlElement xmlElement10 = new XmlElement("", "");
    xmlElement5.append(xmlElement10);
    XmlElement xmlElement13 = xmlElement10.getElement("");
    XmlElement xmlElement14 = new XmlElement();
    xmlElement10.append(xmlElement14);
    XmlElement xmlElement16 = new XmlElement();
    java.util.Observer observer17 = null;
    xmlElement16.deleteObserver(observer17);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str20 = null;
    XmlElement xmlElement21 = new XmlElement("", hashtable_str_str20);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str23 = null;
    XmlElement xmlElement24 = new XmlElement("", hashtable_str_str23);
    java.lang.Object obj25 = xmlElement24.clone();
    boolean b26 = xmlElement21.equals((java.lang.Object)xmlElement24);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str27 = null;
    xmlElement24.setAttributes(hashtable_str_str27);
    int i29 = xmlElement24.count();
    int i30 = xmlElement24.count();
    java.util.List list31 = xmlElement24.getElements();
    java.util.List list32 = xmlElement24.getElements();
    boolean b33 = xmlElement16.equals((java.lang.Object)list32);
    xmlElement14.notifyObservers((java.lang.Object)list32);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement36 = xmlElement14.getElement((int)(short)0);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b26 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i29 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i30 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list31);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list32);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b33 == false);

  }

  @Test
  public void test71() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test71"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    xmlElement5.setAttributes(hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("", hashtable_str_str11);
    java.lang.Object obj13 = xmlElement12.clone();
    xmlElement5.setParent(xmlElement12);
    xmlElement5.setData("hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str18 = null;
    XmlElement xmlElement19 = new XmlElement("", hashtable_str_str18);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str21 = null;
    XmlElement xmlElement22 = new XmlElement("", hashtable_str_str21);
    java.lang.Object obj23 = xmlElement22.clone();
    boolean b24 = xmlElement19.equals((java.lang.Object)xmlElement22);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str25 = null;
    xmlElement22.setAttributes(hashtable_str_str25);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str28 = null;
    XmlElement xmlElement29 = new XmlElement("", hashtable_str_str28);
    java.lang.Object obj30 = xmlElement29.clone();
    xmlElement22.setParent(xmlElement29);
    xmlElement22.setData("hi!");
    int i34 = xmlElement22.count();
    xmlElement5.setParent(xmlElement22);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj23);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b24 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj30);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i34 == 0);

  }

  @Test
  public void test72() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test72"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.lang.Object obj6 = xmlElement5.clone();
    boolean b7 = xmlElement2.equals((java.lang.Object)xmlElement5);
    XmlElement xmlElement10 = new XmlElement("", "");
    xmlElement5.append(xmlElement10);
    XmlElement xmlElement13 = xmlElement5.getElement((int)(byte)0);
    java.lang.Object obj14 = xmlElement5.clone();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement13);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj14);

  }

  @Test
  public void test73() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test73"); }


    XmlElement xmlElement2 = new XmlElement("", "");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("", hashtable_str_str4);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("", hashtable_str_str7);
    java.lang.Object obj9 = xmlElement8.clone();
    boolean b10 = xmlElement5.equals((java.lang.Object)xmlElement8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    xmlElement8.setAttributes(hashtable_str_str11);
    int i13 = xmlElement8.count();
    int i14 = xmlElement8.count();
    boolean b15 = xmlElement2.equals((java.lang.Object)xmlElement8);
    XmlElement xmlElement16 = xmlElement2.getParent();
    // The following exception was thrown during execution in test generation
    try {
    int i17 = xmlElement16.count();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj9);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i13 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i14 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement16);

  }

  @Test
  public void test74() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test74"); }


    XmlElement xmlElement2 = new XmlElement("", "");
    XmlElement xmlElement5 = new XmlElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str6 = xmlElement5.getAttributes();
    XmlElement xmlElement7 = new XmlElement("", hashtable_str_str6);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("", hashtable_str_str9);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = null;
    XmlElement xmlElement13 = new XmlElement("", hashtable_str_str12);
    java.lang.Object obj14 = xmlElement13.clone();
    boolean b15 = xmlElement10.equals((java.lang.Object)xmlElement13);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str16 = null;
    xmlElement13.setAttributes(hashtable_str_str16);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str19 = null;
    XmlElement xmlElement20 = new XmlElement("", hashtable_str_str19);
    java.lang.Object obj21 = xmlElement20.clone();
    xmlElement13.setParent(xmlElement20);
    xmlElement20.removeAllElements();
    XmlElement xmlElement24 = xmlElement7.removeElement(xmlElement20);
    xmlElement2.setParent(xmlElement7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(hashtable_str_str6);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement24);

  }

}
