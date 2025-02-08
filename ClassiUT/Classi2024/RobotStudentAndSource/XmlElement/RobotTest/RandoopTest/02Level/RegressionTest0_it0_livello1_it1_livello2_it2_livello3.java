package mypackage;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0_it0_livello1_it1_livello2_it2_livello3 {

  public static boolean debug = false;

  @Test
  public void test01() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test01"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.lang.String str7 = xmlElement5.getName();
    int i8 = xmlElement5.count();
    // The following exception was thrown during execution in test generation
    try {
    java.lang.Object obj11 = xmlElement5.addAttribute("hi!", "");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 0);

  }

  @Test
  public void test02() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test02"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement9);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement16 = xmlElement5.getElement((int)(short)100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);

  }

  @Test
  public void test03() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test03"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.lang.String str7 = xmlElement5.getName();
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str10 = xmlElement5.getAttribute("", "");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));

  }

  @Test
  public void test04() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test04"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.lang.String str7 = xmlElement5.getName();
    boolean b9 = xmlElement5.equals((java.lang.Object)(byte)100);
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str12 = xmlElement5.getAttribute("", "");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == false);

  }

  @Test
  public void test05() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test05"); }


    XmlElement xmlElement0 = new XmlElement();
    xmlElement0.notifyObservers((java.lang.Object)(short)0);

  }

  @Test
  public void test06() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test06"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement9);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str16 = null;
    XmlElement xmlElement17 = new XmlElement("hi!", hashtable_str_str16);
    XmlElement xmlElement18 = xmlElement9.removeElement(xmlElement17);
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str20 = xmlElement9.getAttribute("hi!");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement18);

  }

  @Test
  public void test07() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test07"); }


    XmlElement xmlElement0 = new XmlElement();
    xmlElement0.setData("");
    int i3 = xmlElement0.count();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("hi!", hashtable_str_str5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    boolean b10 = xmlElement6.addElement(xmlElement9);
    java.lang.String str11 = xmlElement9.getName();
    int i12 = xmlElement9.count();
    // The following exception was thrown during execution in test generation
    try {
    xmlElement0.insertElement(xmlElement9, 100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i3 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str11 + "' != '" + "hi!"+ "'", str11.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i12 == 0);

  }

  @Test
  public void test08() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test08"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    int i3 = xmlElement2.countObservers();
    java.util.Observer observer4 = null;
    xmlElement2.deleteObserver(observer4);
    boolean b6 = xmlElement2.hasChanged();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    java.lang.String str14 = xmlElement12.getName();
    // The following exception was thrown during execution in test generation
    try {
    xmlElement2.insertElement(xmlElement12, (int)(short)100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i3 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str14 + "' != '" + "hi!"+ "'", str14.equals("hi!"));

  }

  @Test
  public void test09() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test09"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.lang.String str7 = xmlElement5.getName();
    java.lang.Object obj8 = xmlElement5.clone();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj8);

  }

  @Test
  public void test10() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test10"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement9);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str16 = null;
    XmlElement xmlElement17 = new XmlElement("hi!", hashtable_str_str16);
    XmlElement xmlElement18 = xmlElement9.removeElement(xmlElement17);
    xmlElement17.setName("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement18);

  }

  @Test
  public void test11() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test11"); }


    XmlElement xmlElement0 = new XmlElement();
    int i1 = xmlElement0.countObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i1 == 0);

  }

  @Test
  public void test12() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test12"); }


    XmlElement xmlElement0 = new XmlElement();
    boolean b1 = xmlElement0.hasChanged();
    java.util.Observer observer2 = null;
    xmlElement0.deleteObserver(observer2);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b1 == false);

  }

  @Test
  public void test13() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test13"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement9);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str16 = null;
    XmlElement xmlElement17 = new XmlElement("hi!", hashtable_str_str16);
    XmlElement xmlElement18 = xmlElement9.removeElement(xmlElement17);
    java.lang.String str19 = xmlElement9.getName();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str19 + "' != '" + "hi!"+ "'", str19.equals("hi!"));

  }

  @Test
  public void test14() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test14"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement9);
    boolean b15 = xmlElement5.hasChanged();
    // The following exception was thrown during execution in test generation
    try {
    java.lang.String str17 = xmlElement5.getAttribute("");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);

  }

  @Test
  public void test15() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test15"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.lang.String str7 = xmlElement5.getName();
    xmlElement5.setName("");
    XmlElement xmlElement11 = xmlElement5.getElement("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement11);

  }

  @Test
  public void test16() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test16"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.lang.Object obj7 = xmlElement2.clone();
    java.lang.String str8 = xmlElement2.getData();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str8);

  }

  @Test
  public void test17() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test17"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement9);
    boolean b15 = xmlElement5.hasChanged();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str16 = null;
    xmlElement5.setAttributes(hashtable_str_str16);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str19 = null;
    XmlElement xmlElement20 = new XmlElement("hi!", hashtable_str_str19);
    int i21 = xmlElement20.countObservers();
    java.util.Observer observer22 = null;
    xmlElement20.deleteObserver(observer22);
    boolean b24 = xmlElement20.hasChanged();
    xmlElement5.setParent(xmlElement20);
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
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i21 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b24 == false);

  }

  @Test
  public void test18() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test18"); }


    XmlElement xmlElement1 = new XmlElement("");

  }

  @Test
  public void test19() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test19"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.lang.String str7 = xmlElement5.getName();
    xmlElement5.setName("");
    java.util.List list10 = xmlElement5.getElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list10);

  }

  @Test
  public void test20() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test20"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement9);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str16 = null;
    XmlElement xmlElement17 = new XmlElement("hi!", hashtable_str_str16);
    XmlElement xmlElement18 = xmlElement9.removeElement(xmlElement17);
    java.util.List list19 = xmlElement9.getElements();
    xmlElement9.setName("hi!");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str23 = null;
    XmlElement xmlElement24 = new XmlElement("hi!", hashtable_str_str23);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str26 = null;
    XmlElement xmlElement27 = new XmlElement("hi!", hashtable_str_str26);
    boolean b28 = xmlElement24.addElement(xmlElement27);
    // The following exception was thrown during execution in test generation
    try {
    xmlElement9.insertElement(xmlElement27, (int)(byte)100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b28 == true);

  }

  @Test
  public void test21() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test21"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str15 = null;
    XmlElement xmlElement16 = new XmlElement("hi!", hashtable_str_str15);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str18 = null;
    XmlElement xmlElement19 = new XmlElement("hi!", hashtable_str_str18);
    boolean b20 = xmlElement16.addElement(xmlElement19);
    XmlElement xmlElement21 = xmlElement12.removeElement(xmlElement16);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str23 = null;
    XmlElement xmlElement24 = new XmlElement("hi!", hashtable_str_str23);
    XmlElement xmlElement25 = xmlElement16.removeElement(xmlElement24);
    java.util.List list26 = xmlElement16.getElements();
    xmlElement16.setName("hi!");
    boolean b29 = xmlElement2.addElement(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b20 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement21);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement25);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list26);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b29 == true);

  }

  @Test
  public void test22() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test22"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.lang.String str7 = xmlElement5.getName();
    int i8 = xmlElement5.count();
    xmlElement5.deleteObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!"+ "'", str7.equals("hi!"));
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i8 == 0);

  }

  @Test
  public void test23() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test23"); }


    XmlElement xmlElement0 = new XmlElement();
    boolean b2 = xmlElement0.equals((java.lang.Object)(byte)100);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    int i6 = xmlElement5.countObservers();
    XmlElement xmlElement7 = xmlElement0.removeElement(xmlElement5);
    // The following exception was thrown during execution in test generation
    try {
    java.lang.Object obj10 = xmlElement7.addAttribute("", "");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement7);

  }

  @Test
  public void test24() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test24"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement9);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str16 = null;
    XmlElement xmlElement17 = new XmlElement("hi!", hashtable_str_str16);
    XmlElement xmlElement18 = xmlElement9.removeElement(xmlElement17);
    java.util.List list19 = xmlElement9.getElements();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str20 = null;
    xmlElement9.setAttributes(hashtable_str_str20);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list19);

  }

  @Test
  public void test25() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test25"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement9);
    boolean b15 = xmlElement5.hasChanged();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str16 = null;
    xmlElement5.setAttributes(hashtable_str_str16);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str19 = null;
    XmlElement xmlElement20 = new XmlElement("hi!", hashtable_str_str19);
    int i21 = xmlElement20.countObservers();
    java.util.Observer observer22 = null;
    xmlElement20.deleteObserver(observer22);
    boolean b24 = xmlElement20.hasChanged();
    xmlElement5.setParent(xmlElement20);
    int i26 = xmlElement5.count();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i21 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b24 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i26 == 0);

  }

  @Test
  public void test26() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test26"); }


    XmlElement xmlElement0 = new XmlElement();
    boolean b2 = xmlElement0.equals((java.lang.Object)(byte)100);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    int i6 = xmlElement5.countObservers();
    XmlElement xmlElement7 = xmlElement0.removeElement(xmlElement5);
    java.util.Observer observer8 = null;
    // The following exception was thrown during execution in test generation
    try {
    xmlElement5.addObserver(observer8);
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement7);

  }

  @Test
  public void test27() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test27"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement9);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str16 = null;
    XmlElement xmlElement17 = new XmlElement("hi!", hashtable_str_str16);
    XmlElement xmlElement18 = xmlElement9.removeElement(xmlElement17);
    java.util.List list19 = xmlElement17.getElements();
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement21 = xmlElement17.removeElement((int)'4');
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list19);

  }

  @Test
  public void test28() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test28"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    int i3 = xmlElement2.countObservers();
    XmlElement xmlElement5 = xmlElement2.getElement("hi!");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i3 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement5);

  }

  @Test
  public void test29() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test29"); }


    XmlElement xmlElement0 = new XmlElement();
    boolean b2 = xmlElement0.equals((java.lang.Object)(byte)100);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("hi!", hashtable_str_str7);
    boolean b9 = xmlElement5.addElement(xmlElement8);
    XmlElement xmlElement10 = xmlElement0.addSubElement(xmlElement8);
    // The following exception was thrown during execution in test generation
    try {
    java.util.Enumeration enumeration11 = xmlElement8.getAttributeNames();
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement10);

  }

  @Test
  public void test30() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test30"); }


    XmlElement xmlElement0 = new XmlElement();
    boolean b1 = xmlElement0.hasChanged();
    java.util.List list2 = xmlElement0.getElements();
    xmlElement0.removeAllElements();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b1 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list2);

  }

  @Test
  public void test31() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test31"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    int i3 = xmlElement2.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("hi!", hashtable_str_str5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    boolean b10 = xmlElement6.addElement(xmlElement9);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = null;
    XmlElement xmlElement13 = new XmlElement("hi!", hashtable_str_str12);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str15 = null;
    XmlElement xmlElement16 = new XmlElement("hi!", hashtable_str_str15);
    boolean b17 = xmlElement13.addElement(xmlElement16);
    XmlElement xmlElement18 = xmlElement9.removeElement(xmlElement13);
    XmlElement xmlElement19 = xmlElement2.addSubElement(xmlElement13);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str20 = xmlElement2.getAttributes();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i3 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(hashtable_str_str20);

  }

  @Test
  public void test32() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test32"); }


    XmlElement xmlElement0 = new XmlElement();
    xmlElement0.setData("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("hi!", hashtable_str_str7);
    boolean b9 = xmlElement5.addElement(xmlElement8);
    boolean b10 = xmlElement0.addElement(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);

  }

  @Test
  public void test33() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test33"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    int i3 = xmlElement2.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("hi!", hashtable_str_str5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    boolean b10 = xmlElement6.addElement(xmlElement9);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = null;
    XmlElement xmlElement13 = new XmlElement("hi!", hashtable_str_str12);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str15 = null;
    XmlElement xmlElement16 = new XmlElement("hi!", hashtable_str_str15);
    boolean b17 = xmlElement13.addElement(xmlElement16);
    XmlElement xmlElement18 = xmlElement9.removeElement(xmlElement13);
    XmlElement xmlElement19 = xmlElement2.addSubElement(xmlElement13);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement.printNode(xmlElement2, "hi!");
      org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException");
    } catch (java.lang.NullPointerException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.NullPointerException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i3 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement19);

  }

  @Test
  public void test34() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test34"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement9);
    boolean b15 = xmlElement5.hasChanged();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str16 = null;
    xmlElement5.setAttributes(hashtable_str_str16);
    xmlElement5.notifyObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);

  }

  @Test
  public void test35() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test35"); }


    XmlElement xmlElement0 = new XmlElement();
    boolean b2 = xmlElement0.equals((java.lang.Object)(byte)100);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    int i6 = xmlElement5.countObservers();
    XmlElement xmlElement7 = xmlElement0.removeElement(xmlElement5);
    XmlElement xmlElement10 = xmlElement5.addSubElement("", "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement7);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement10);

  }

  @Test
  public void test36() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test36"); }


    XmlElement xmlElement2 = new XmlElement("hi!", "");
    java.util.Observer observer3 = null;
    xmlElement2.deleteObserver(observer3);
    XmlElement xmlElement5 = new XmlElement();
    boolean b7 = xmlElement5.equals((java.lang.Object)(byte)100);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str9 = null;
    XmlElement xmlElement10 = new XmlElement("hi!", hashtable_str_str9);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = null;
    XmlElement xmlElement13 = new XmlElement("hi!", hashtable_str_str12);
    boolean b14 = xmlElement10.addElement(xmlElement13);
    XmlElement xmlElement15 = xmlElement5.addSubElement(xmlElement13);
    xmlElement5.setData("");
    // The following exception was thrown during execution in test generation
    try {
    xmlElement2.insertElement(xmlElement5, (int)(byte)100);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b7 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b14 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement15);

  }

  @Test
  public void test37() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test37"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    int i3 = xmlElement2.countObservers();
    java.util.Observer observer4 = null;
    xmlElement2.deleteObserver(observer4);
    java.lang.Object obj6 = xmlElement2.clone();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i3 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj6);

  }

  @Test
  public void test38() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test38"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.lang.Object obj7 = xmlElement5.clone();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj7);

  }

  @Test
  public void test39() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test39"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement9);
    boolean b15 = xmlElement5.hasChanged();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str16 = null;
    xmlElement5.setAttributes(hashtable_str_str16);
    // The following exception was thrown during execution in test generation
    try {
    XmlElement xmlElement19 = xmlElement5.getElement((int)(byte)1);
      org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException");
    } catch (java.lang.ArrayIndexOutOfBoundsException e) {
      // Expected exception.
      if (! e.getClass().getCanonicalName().equals("java.lang.ArrayIndexOutOfBoundsException")) {
        org.junit.Assert.fail("Expected exception of type java.lang.ArrayIndexOutOfBoundsException, got " + e.getClass().getCanonicalName());
      }
    }
    
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);

  }

  @Test
  public void test40() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test40"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement9);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str15 = xmlElement9.getAttributes();
    XmlElement xmlElement16 = xmlElement9.getParent();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str18 = null;
    XmlElement xmlElement19 = new XmlElement("hi!", hashtable_str_str18);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str21 = null;
    XmlElement xmlElement22 = new XmlElement("hi!", hashtable_str_str21);
    boolean b23 = xmlElement19.addElement(xmlElement22);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str25 = null;
    XmlElement xmlElement26 = new XmlElement("hi!", hashtable_str_str25);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str28 = null;
    XmlElement xmlElement29 = new XmlElement("hi!", hashtable_str_str28);
    boolean b30 = xmlElement26.addElement(xmlElement29);
    XmlElement xmlElement31 = xmlElement22.removeElement(xmlElement26);
    boolean b32 = xmlElement22.hasChanged();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str33 = null;
    xmlElement22.setAttributes(hashtable_str_str33);
    xmlElement9.append(xmlElement22);
    XmlElement xmlElement37 = xmlElement22.addSubElement("");
    XmlElement xmlElement38 = xmlElement22.getParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(hashtable_str_str15);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement16);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b23 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b30 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement31);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b32 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement37);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement38);

  }

  @Test
  public void test41() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test41"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    int i3 = xmlElement2.countObservers();
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str5 = null;
    XmlElement xmlElement6 = new XmlElement("hi!", hashtable_str_str5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    boolean b10 = xmlElement6.addElement(xmlElement9);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str12 = null;
    XmlElement xmlElement13 = new XmlElement("hi!", hashtable_str_str12);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str15 = null;
    XmlElement xmlElement16 = new XmlElement("hi!", hashtable_str_str15);
    boolean b17 = xmlElement13.addElement(xmlElement16);
    XmlElement xmlElement18 = xmlElement9.removeElement(xmlElement13);
    XmlElement xmlElement19 = xmlElement2.addSubElement(xmlElement13);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str21 = null;
    XmlElement xmlElement22 = new XmlElement("hi!", hashtable_str_str21);
    int i23 = xmlElement22.countObservers();
    xmlElement22.notifyObservers((java.lang.Object)10);
    xmlElement13.insertElement(xmlElement22, (int)(short)0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i3 == 0);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b10 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b17 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement19);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i23 == 0);

  }

  @Test
  public void test42() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test42"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement9);
    boolean b15 = xmlElement5.hasChanged();
    XmlElement xmlElement18 = new XmlElement("", "hi!");
    xmlElement5.setParent(xmlElement18);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b15 == false);

  }

  @Test
  public void test43() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test43"); }


    XmlElement xmlElement2 = new XmlElement("hi!", "");
    java.util.Observer observer3 = null;
    xmlElement2.deleteObserver(observer3);
    xmlElement2.deleteObservers();

  }

  @Test
  public void test44() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test44"); }


    XmlElement xmlElement0 = new XmlElement();
    boolean b2 = xmlElement0.equals((java.lang.Object)(byte)100);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("hi!", hashtable_str_str7);
    boolean b9 = xmlElement5.addElement(xmlElement8);
    XmlElement xmlElement10 = xmlElement0.addSubElement(xmlElement8);
    java.util.List list11 = xmlElement8.getElements();
    java.lang.Object obj12 = xmlElement8.clone();
    xmlElement8.removeFromParent();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b2 == false);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b9 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement10);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(list11);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(obj12);

  }

  @Test
  public void test45() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test45"); }


    XmlElement xmlElement1 = new XmlElement("hi!");
    java.lang.Object obj4 = xmlElement1.addAttribute("hi!", "");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(obj4);

  }

  @Test
  public void test46() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test46"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str4 = null;
    XmlElement xmlElement5 = new XmlElement("hi!", hashtable_str_str4);
    boolean b6 = xmlElement2.addElement(xmlElement5);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str8 = null;
    XmlElement xmlElement9 = new XmlElement("hi!", hashtable_str_str8);
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str11 = null;
    XmlElement xmlElement12 = new XmlElement("hi!", hashtable_str_str11);
    boolean b13 = xmlElement9.addElement(xmlElement12);
    XmlElement xmlElement14 = xmlElement5.removeElement(xmlElement9);
    int i15 = xmlElement5.countObservers();
    xmlElement5.setName("");
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b6 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(b13 == true);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(xmlElement14);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i15 == 0);

  }

  @Test
  public void test47() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test47"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.lang.String str3 = xmlElement2.getData();
    XmlElement xmlElement5 = xmlElement2.addSubElement("");
    int i6 = xmlElement2.countObservers();
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i6 == 0);

  }

  @Test
  public void test48() throws Throwable {

    if (debug) { System.out.format("%n%s%n","RegressionTest0.test48"); }


    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str1 = null;
    XmlElement xmlElement2 = new XmlElement("hi!", hashtable_str_str1);
    java.lang.String str3 = xmlElement2.getData();
    XmlElement xmlElement5 = xmlElement2.addSubElement("");
    java.util.Hashtable<java.lang.String,java.lang.String> hashtable_str_str7 = null;
    XmlElement xmlElement8 = new XmlElement("hi!", hashtable_str_str7);
    int i9 = xmlElement8.countObservers();
    xmlElement2.setParent(xmlElement8);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNull(str3);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertNotNull(xmlElement5);
    
    // Regression assertion (captures the current behavior of the code)
    org.junit.Assert.assertTrue(i9 == 0);

  }

}
