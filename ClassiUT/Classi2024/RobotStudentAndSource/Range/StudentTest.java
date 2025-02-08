package student.gennaroIannicelliUno;

import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.Range;


class StudentTest {
	
	static Range<Double> isRange = Range.is(3.1415);
	static Range<Double> betweenRange = Range.between(3.0, 5.0);
	static Range<Double> isRangeComparator = Range.is(3.1415,Comparator.reverseOrder());
	static Range<Double> betweenRangeComparator = Range.between(3.0,5.0,Comparator.reverseOrder());

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		isRange = Range.is(3.1415);
		betweenRange = Range.between(3.0, 5.0);
		isRangeComparator = Range.is(3.1415,Comparator.reverseOrder());
		betweenRangeComparator = Range.between(3.0,5.0,Comparator.reverseOrder());		
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		isRange=null;
		betweenRange=null;
	}

	@BeforeEach
	void setUp() throws Exception {
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testHashCodeIs() {
		//fail("Not yet implemented");
		Range<Double> Range2 = Range.is(3.1415,Comparator.reverseOrder());
		assertEquals(isRangeComparator.hashCode(),Range2.hashCode());
	}
	
	@Test
	void testHashCodeBetween() {
		//fail("Not yet implemented");
		Range<Double> Range2 = Range.between(3.0,5.0,Comparator.reverseOrder());
		assertEquals(betweenRangeComparator.hashCode(),Range2.hashCode());
	}

	@Test
	void testConstructorIsNoComparatorIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,()->Range.is(null));
	}
	
	@Test
	void testConstructorIsComparatorIllegalArgumentException() {
		Comparator<Double> comp = Comparator.reverseOrder();
		assertThrows(IllegalArgumentException.class,()->Range.is(null,comp));
	}
	
	//TODO: Testare ClassCastException
	
	@Test
	void testConstructorBetweenNoComparator2IllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,()->Range.between(null, 5.0));
	}
	
	@Test
	void testConstructorBetweenNoComparator3IllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,()->Range.between(3.0, null));
	}
	
	@Test
	void testConstructorBetweenNoComparator4IllegalArgumentException() {
		assertThrows(IllegalArgumentException.class,()->Range.between(null, null));
	}
	

	
	@Test
	void testConstructorBetweenWithComparator2IllegalArgumentException() {
		Comparator<Double> comp = Comparator.reverseOrder();
		assertThrows(IllegalArgumentException.class,()->Range.between(null, 5.0,comp));
	}
	
	@Test
	void testConstructorBetweenWithComparator3IllegalArgumentException() {
		Comparator<Double> comp = Comparator.reverseOrder();
		assertThrows(IllegalArgumentException.class,()->Range.between(3.0, null,comp));
	}
	
	@Test
	void testConstructorBetweenWithComparator4IllegalArgumentException() {
		Comparator<Double> comp = Comparator.reverseOrder();
		assertThrows(IllegalArgumentException.class,()->Range.between(null, null,comp));
	}

	@Test
	void testGetMinimumIs() {
		//fail("Not yet implemented");
		Double elem = 3.1415;
		System.out.println("TestIsMinimum: Expected: "+elem+", given: "+isRange.getMinimum());
		assertEquals(isRange.getMinimum(),elem,"Error, expected "+elem+", given: " + isRange.getMinimum() );
	}
	
	@Test
	void testGetMinimumIsWithComparator() {
		//fail("Not yet implemented");
		Double elem = 3.1415;
		System.out.println("TestIsMinimumWithComparator: Expected: "+elem+", given: "+isRangeComparator.getMinimum());
		assertEquals(isRangeComparator.getMinimum(),elem,"Error, expected "+elem+", given: " + isRange.getMinimum() );
	}
	
	@Test
	void testGetMinimumBetween() {
		//fail("Not yet implemented");
		Double min = 3.0;
		System.out.println("TestBetweenMinimum: Expected: " + min+", given: " + betweenRange.getMinimum());
		assertEquals(betweenRange.getMinimum(),min,"Error, expected" + min+", given: " + betweenRange.getMinimum() );
	}
	
	@Test
	void testGetMinimumBetweenWithComparator() {
		//fail("Not yet implemented");
		Double min = 5.0; //con reverse ordering 5 è considerato minore di 3
		System.out.println("TestBetweenMinimumWithComparator: Expected: " + min+", given: " + betweenRangeComparator.getMinimum());
		assertEquals(betweenRangeComparator.getMinimum(),min,"Error, expected" + min+", given: " + betweenRangeComparator.getMinimum() );
	}

	@Test
	void testGetMaximumIs() {
		//fail("Not yet implemented");
		Double elem = 3.1415;
		System.out.println("TestIsMaximum: Expected: "+elem+", given: "+isRange.getMaximum());
		assertEquals(isRange.getMaximum(),elem,"Error, expected "+elem+", given: " + isRange.getMaximum() );
	}
	
	@Test
	void testGetMaximumBetween() {
		//fail("Not yet implemented");
		Double max = 5.0;
		System.out.println("TestBetweenMaximum: Expected: "+max+", given: "+betweenRange.getMaximum());
		assertEquals(betweenRange.getMaximum(),max,"Error, expected "+max+", given: " + betweenRange.getMaximum() );
	}
	
	@Test
	void testGetMaximumIsWithComparator() {
		Double elem = 3.1415;
		System.out.println("TestIsMaximumWithComparator: Expected: " + elem + ", given: " + isRangeComparator.getMaximum());
		assertEquals(isRangeComparator.getMaximum(),elem,"Errpr. expected: " + elem+", given: "+ isRangeComparator.getMaximum());
	}
	
	@Test
	void testGetMaximumBetweenWithComparator() {
		Double max = 3.0;
		System.out.println("TestIsMaximumWithComparator: Expected: " + max + ", given: " + betweenRangeComparator.getMaximum());
		assertEquals(betweenRangeComparator.getMaximum(),max,"Errpr. expected: " + max+", given: "+ betweenRangeComparator.getMaximum());
	}

	//Distinguo i casi in cui la classe è con comparator e in cui è null (natural order)
	@Test
	void testGetComparatorIs1() {
		//fail("Not yet implemented");
		Double elem=3.1415;
		Range<Double> supportRange = Range.is(elem+1);
		System.out.println("TestComparatorIsNaturalOrder: Expected: "+supportRange.getComparator()+", given: "+isRange.getComparator());
		//Siccome i due range sono definiti allo stesso modo, dovrebbero risultare nell'avere lo stesso comparatore
		assertEquals(isRange.getComparator(),supportRange.getComparator(),"Error, expected: "+Comparator.naturalOrder()+", given: "+isRange.getComparator());
	}
	
	@Test
	void testGetComparatorIs2() {
		//fail("Not yet implemented");
		Double elem=3.1415;
		Comparator<Double> comp = Comparator.reverseOrder();
		System.out.println("TestComparatorIsReverseOrder: Expected: "+comp+", given: "+isRangeComparator.getComparator());
		assertEquals(isRangeComparator.getComparator(),comp,"Error, expected: "+comp+", given: "+isRangeComparator.getComparator());
	}
	
	@Test
	void testGetComparatorBetween1() {
		Double min=3.0;
		Double max=5.0;
		Range<Double> supportRange = Range.between(min+1, max+1);
		System.out.println("TestComparatorBetweenNaturalOrder: Expected: "+supportRange.getComparator()+", given: "+betweenRange.getComparator());
		assertEquals(betweenRange.getComparator(),supportRange.getComparator(),"Error, expected: "+Comparator.naturalOrder()+" , given: "+betweenRange.getComparator());
	}
	
	@Test
	void testGetComparatorBetween2() {
		Comparator<Double> comp = Comparator.reverseOrder();
		System.out.println("TestComparatorBetweenReverseOrder: Expected: "+comp+", given: "+betweenRangeComparator.getComparator());
		assertEquals(betweenRangeComparator.getComparator(),comp,"Error, expected: "+comp+" , given: "+betweenRangeComparator.getComparator());
	}

	@Test
	void testIsNaturalOrderingIs() {
		//fail("Not yet implemented");
		//isRange è definito senza comparatore, dunque il costruttore gli da natural ordering
		assertTrue(isRange.isNaturalOrdering());
	}
	
	@Test
	void testIsNaturalOrderingIsFalse() {
		assertFalse(isRangeComparator.isNaturalOrdering());
	}
	
	@Test
	void testIsNaturalOrderingBetween() {
		//fail("Not yet implemented");
		assertTrue(betweenRange.isNaturalOrdering());
		
	}
	
	@Test
	void testIsNaturalOrderingBetweenFalse() {
		assertFalse(betweenRangeComparator.isNaturalOrdering());
	}

	@Test
	void testContainsIsNoComparatorInvalid() {
		//fail("Not yet implemented");
		assertFalse(isRange.contains(2.0));
	}
	
	@Test
	void testContainsIsNoComparatorValid() {
		assertTrue(isRange.contains(3.1415));
	}
	
	@Test
	void testContainsNullInput() {
		assertFalse(isRange.contains(null));
	}
	
	@Test
	void testContainsIsWithComparatorInvalid() {
		//fail("Not yet implemented");
		assertFalse(isRangeComparator.contains(2.0));
	}
	
	@Test
	void testContainsIsWithComparatorValid() {
		assertTrue(isRangeComparator.contains(3.1415));
	}
	
	@Test
	void testContainsBetweenNoComparatorInvalid() {
		assertFalse(betweenRange.contains(2.0));
	}
	
	@Test
	void testContainsBetweenNoComparatorBoundary() {
		assertTrue(betweenRange.contains(3.0));
	}
	
	@Test
	void testContainsBetweenNoComparatorValid() {
		assertTrue(betweenRange.contains(3.5));
	}
	
	@Test
	void testContainsBetweenWithComparatorValid() {
		assertTrue(betweenRangeComparator.contains(3.5));
	}
	
	@Test
	void testContainsBetweenWithComparatorBoundary() {
		assertTrue(betweenRangeComparator.contains(3.0));
	}
	
	@Test
	void testContainsBetweenWithComparatorInvalid() {
		assertFalse(betweenRangeComparator.contains(2.0));
	}

	@Test
	void testContainsBetweenNullInput() {
		assertFalse(betweenRange.contains(null));
	}
	
	@Test
	void testIsAfterIsValid() {
		//fail("Not yet implemented");
		assertTrue(isRange.isAfter(2.0));
	}
	
	@Test
	void testIsAfterIsInvalid() {
		//fail("Not yet implemented");
		assertFalse(isRange.isAfter(3.1415));
	}
	
	@Test
	void testIsAfterIsNull() {
		//fail("Not yet implemented");
		assertFalse(isRange.isAfter(null));
	}
	
	@Test
	void testIsAfterBetweenValid() {
		//fail("Not yet implemented");
		assertTrue(betweenRange.isAfter(2.0));
	}
	
	@Test
	void testIsAfterBetweenInvalid() {
		//fail("Not yet implemented");
		assertFalse(betweenRange.isAfter(3.1415));
	}
	
	@Test
	void testIsAfterBetweenNull() {
		//fail("Not yet implemented");
		assertFalse(betweenRange.isAfter(null));
	}
	
	@Test
	void testIsAfterIsValidComparator() {
		//fail("Not yet implemented");
		assertTrue(isRangeComparator.isAfter(7.0));
	}
	
	@Test
	void testIsAfterIsInvalidComparator() {
		//fail("Not yet implemented");
		assertFalse(isRangeComparator.isAfter(3.1415));
	}
	
	@Test
	void testIsAfterIsNullComparator() {
		//fail("Not yet implemented");
		assertFalse(isRangeComparator.isAfter(null));
	}
	
	@Test
	void testIsAfterBetweenValidComparator() {
		//fail("Not yet implemented");
		assertTrue(betweenRangeComparator.isAfter(7.0));
	}
	
	@Test
	void testIsAfterBetweenInvalidComparator() {
		//fail("Not yet implemented");
		assertFalse(betweenRangeComparator.isAfter(3.1415));
	}
	
	@Test
	void testIsAfterBetweenNullComparator() {
		//fail("Not yet implemented");
		assertFalse(betweenRangeComparator.isAfter(null));
	}

	@Test
	void testIsStartedByIsNoComp() {
		//fail("Not yet implemented");
		assertTrue(isRange.isStartedBy(3.1415));
	}
	
	@Test
	void testIsStartedByIsNoCompInvalid() {
		//fail("Not yet implemented");
		assertFalse(isRange.isStartedBy(3.0));
	}
	
	@Test
	void testIsStartedByIsNoCompNull() {
		//fail("Not yet implemented");
		assertFalse(isRange.isStartedBy(null));
	}
	
	@Test
	void testIsStartedByIsComp() {
		//fail("Not yet implemented");
		assertTrue(isRangeComparator.isStartedBy(3.1415));
	}
	
	@Test
	void testIsStartedByIsCompInvalid() {
		//fail("Not yet implemented");
		assertFalse(isRangeComparator.isStartedBy(3.0));
	}
	
	@Test
	void testIsStartedByIsCompNull() {
		//fail("Not yet implemented");
		assertFalse(isRangeComparator.isStartedBy(null));
	}
	
	@Test
	void testIsStartedByBetweenNoComp() {
		//fail("Not yet implemented");
		assertTrue(betweenRange.isStartedBy(3.0));
	}
	
	@Test
	void testIsStartedByBetweenNoCompInvalid() {
		//fail("Not yet implemented");
		assertFalse(betweenRange.isStartedBy(3.1415));
	}
	
	@Test
	void testIsStartedByBetweenNoCompNull() {
		//fail("Not yet implemented");
		assertFalse(betweenRange.isStartedBy(null));
	}
	
	@Test
	void testIsStartedByBetweenComp() {
		//fail("Not yet implemented");
		assertTrue(betweenRangeComparator.isStartedBy(5.0));
	}
	
	@Test
	void testIsStartedByBetweenCompInvalid() {
		//fail("Not yet implemented");
		assertFalse(betweenRangeComparator.isStartedBy(3.1415));
	}
	
	@Test
	void testIsStartedByBetweenCompNull() {
		//fail("Not yet implemented");
		assertFalse(betweenRangeComparator.isStartedBy(null));
	}
	
	//Test Is Ended By
	
	@Test
	void testIsEndeddByIsNoComp() {
		//fail("Not yet implemented");
		assertTrue(isRange.isEndedBy(3.1415));
	}
	
	@Test
	void testIsEndedByIsNoCompInvalid() {
		//fail("Not yet implemented");
		assertFalse(isRange.isEndedBy(3.0));
	}
	
	@Test
	void testIsEndedByIsNoCompNull() {
		//fail("Not yet implemented");
		assertFalse(isRange.isEndedBy(null));
	}
	
	@Test
	void testIsEndedByIsComp() {
		//fail("Not yet implemented");
		assertTrue(isRangeComparator.isEndedBy(3.1415));
	}
	
	@Test
	void testIsEndedByIsCompInvalid() {
		//fail("Not yet implemented");
		assertFalse(isRangeComparator.isEndedBy(3.0));
	}
	
	@Test
	void testIsEndedByIsCompNull() {
		//fail("Not yet implemented");
		assertFalse(isRangeComparator.isEndedBy(null));
	}
	
	@Test
	void testIsEndedByBetweenNoComp() {
		//fail("Not yet implemented");
		assertTrue(betweenRange.isEndedBy(5.0));
	}
	
	@Test
	void testIsEndedByBetweenNoCompInvalid() {
		//fail("Not yet implemented");
		assertFalse(betweenRange.isEndedBy(3.1415));
	}
	
	@Test
	void testIsEndedByBetweenNoCompNull() {
		//fail("Not yet implemented");
		assertFalse(betweenRange.isEndedBy(null));
	}
	
	@Test
	void testIsEndedByBetweenComp() {
		//fail("Not yet implemented");
		assertTrue(betweenRangeComparator.isEndedBy(3.0));
	}
	
	@Test
	void testIsEndedByBetweenCompInvalid() {
		//fail("Not yet implemented");
		assertFalse(betweenRangeComparator.isEndedBy(3.1415));
	}
	
	@Test
	void testIsEndedByBetweenCompNull() {
		//fail("Not yet implemented");
		assertFalse(betweenRangeComparator.isEndedBy(null));
	}

	//Test isBefore
	@Test
	void testIsBeforeIsValid() {
		//fail("Not yet implemented");
		assertTrue(isRangeComparator.isBefore(2.0));
	}
	
	@Test
	void testIsBeforeIsInvalid() {
		//fail("Not yet implemented");
		assertFalse(isRangeComparator.isBefore(7.0));
	}
	
	@Test
	void testIsBeforeIsNull() {
		//fail("Not yet implemented");
		assertFalse(isRangeComparator.isBefore(null));
	}
	
	@Test
	void testIsBeforeBetweenValid() {
		//fail("Not yet implemented");
		assertTrue(betweenRangeComparator.isBefore(2.0));
	}
	
	@Test
	void testIsBeforeBetweenInvalid() {
		//fail("Not yet implemented");
		assertFalse(betweenRangeComparator.isBefore(7.0));
	}
	
	@Test
	void testIsBeforeBetweenNull() {
		//fail("Not yet implemented");
		assertFalse(betweenRangeComparator.isBefore(null));
	}

	@Test
	void testElementCompareToExceptionIs() {
		//fail("Not yet implemented");
		assertThrows(NullPointerException.class,()->isRangeComparator.elementCompareTo(null));
	}
	
	@Test
	void testElementCompareToExceptionBetween() {
		//fail("Not yet implemented");
		assertThrows(NullPointerException.class,()->betweenRangeComparator.elementCompareTo(null));
	}
	
	@Test
	void testElementCompareToIsInsideRange() {
		assertEquals(isRangeComparator.elementCompareTo(3.1415),0);
	}
	
	@Test
	void testElementCompareToIsBeforeRange() {
		assertEquals(isRangeComparator.elementCompareTo(7.0),-1);
	}
	
	@Test
	void testElementCompareToIsAfterRange() {
		assertEquals(isRangeComparator.elementCompareTo(2.0),1);
	}
	
	@Test
	void testElementCompareToBetweenInsideRange() {
		assertEquals(betweenRangeComparator.elementCompareTo(3.1415),0);
	}
	
	@Test
	void testElementCompareToBetweenBeforeRange() {
		assertEquals(betweenRangeComparator.elementCompareTo(7.0),-1);
	}
	
	@Test
	void testElementCompareToBetweenAfterRange() {
		assertEquals(betweenRangeComparator.elementCompareTo(2.0),1);
	}

	@Test
	void testContainsRangeIsNull() {
		Range<Double> Range2 = null;
		//fail("Not yet implemented");
		assertFalse(isRangeComparator.containsRange(Range2));
	}
	
	@Test
	void testContainsRangeBetweenNull() {
		Range<Double> Range2 = null;
		//fail("Not yet implemented");
		assertFalse(betweenRangeComparator.containsRange(Range2));
	}
	
	@Test
	void testContainsRangeIsValid() {
		Range<Double> Range2 = Range.is(3.1415,Comparator.reverseOrder());
		assertTrue(isRangeComparator.containsRange(Range2));
	}
	
	@Test
	void testContainsRangeIsInvalid() {
		Range<Double> Range2 = Range.is(2.0,Comparator.reverseOrder());
		assertFalse(isRangeComparator.containsRange(Range2));
	}
	
	@Test
	void testContainsRangeBetweenValid() {
		Range<Double> Range2 = Range.between(3.5, 4.5,Comparator.reverseOrder());
		assertTrue(betweenRangeComparator.containsRange(Range2));
	}
	
	@Test
	void testContainsRangeBetweenInvalid() {
		Range<Double> Range2 = Range.between(1.0,2.0,Comparator.reverseOrder());
		assertFalse(betweenRangeComparator.containsRange(Range2));
	}

	@Test
	void testIsAfterRangeIsValid() {
		//fail("Not yet implemented");
		Range<Double> Range2 = Range.is(2.0,Comparator.reverseOrder());
		assertFalse(isRangeComparator.isAfterRange(Range2));
	}
	
	@Test
	void testIsAfterRangeIsNull() {
		Range<Double> Range2 = null;
		assertFalse(isRangeComparator.isAfterRange(Range2));
	}
	
	@Test
	void testIsAfterRangeIsInvalid() {
		Range<Double> Range2 = Range.is(4.0);
		assertTrue(isRangeComparator.isAfterRange(Range2));
	}
	
	//TODO: Completare le ultime 4 TC di isAfterRange
	@Test
	void testIsAfterRangeBetweenValid() {
		//fail("Not yet implemented");
		Range<Double> Range2 = Range.between(1.0,2.0,Comparator.reverseOrder());
		assertFalse(betweenRangeComparator.isAfterRange(Range2));
	}
	
	@Test
	void testIsAfterRangeBetweenNull() {
		Range<Double> Range2 = null;
		assertFalse(betweenRangeComparator.isAfterRange(Range2));
	}
	
	@Test
	void testIsAfterRangeBetweenInvalid() {
		Range<Double> Range2 = Range.between(4.0,7.0,Comparator.reverseOrder());
		assertTrue(isRangeComparator.isAfterRange(Range2));
	}

	@Test
	void testIsOverlappedByIsNull() {
		//fail("Not yet implemented");
		Range<Double> Range2 = null;
		assertFalse(isRangeComparator.isOverlappedBy(Range2));
	}
	
	@Test
	void testIsOverlappedByIsValid() {
		//fail("Not yet implemented");
		Range<Double> Range2 = Range.is(3.1415,Comparator.reverseOrder());
		assertTrue(isRangeComparator.isOverlappedBy(Range2));
	}
	
	@Test
	void testIsOverlappedByIsInvalid() {
		//fail("Not yet implemented");
		Range<Double> Range2 = Range.is(2.0,Comparator.reverseOrder());
		assertFalse(isRangeComparator.isOverlappedBy(Range2));
	}
	
	@Test
	void testIsOverlappedByBetweenNull() {
		//fail("Not yet implemented");
		Range<Double> Range2 = null;
		assertFalse(betweenRangeComparator.isOverlappedBy(Range2));
	}
	
	@Test
	void testIsOverlappedByBetweenValid() {
		//fail("Not yet implemented");
		Range<Double> Range2 = Range.between(5.0, 11.0,Comparator.reverseOrder());
		assertTrue(betweenRangeComparator.isOverlappedBy(Range2));
	}

	@Test
	void testIsBeforeRangeIsNull() {
		//fail("Not yet implemented");
		Range<Double> Range2=null;
		assertFalse(isRangeComparator.isBeforeRange(Range2));
	}
	
	@Test
	void testIsBeforeRangeBetweenNull() {
		//fail("Not yet implemented");
		Range<Double> Range2=null;
		assertFalse(betweenRangeComparator.isBeforeRange(Range2));
	}
	
	@Test
	void testIsBeforeRangeIsInvalid() {
		//fail("Not yet implemented");
		Range<Double> Range2=Range.is(5.0,Comparator.reverseOrder());
		assertFalse(isRangeComparator.isBeforeRange(Range2));
	}
	
	@Test
	void testIsBeforeRangeIsValid() {
		//fail("Not yet implemented");
		Range<Double> Range2=Range.is(1.0,Comparator.naturalOrder());
		assertTrue(isRangeComparator.isBeforeRange(Range2));
	}
	
	@Test
	void testIsBeforeRangeBetweenInvalid() {
		//fail("Not yet implemented");
		Range<Double> Range2=Range.between(9.0,10.0,Comparator.naturalOrder());
		assertFalse(betweenRangeComparator.isBeforeRange(Range2));
	}
	
	@Test
	void testIsBeforeRangeBetweenValid() {
		//fail("Not yet implemented");
		Range<Double> Range2=Range.between(1.0,2.0,Comparator.naturalOrder());
		assertTrue(isRangeComparator.isBeforeRange(Range2));
	}

	@Test
	void testIntersectionWithIsOutOfBound() {
		Range<Double> Range2 = Range.is(1.0,Comparator.reverseOrder());
		assertThrows(IllegalArgumentException.class,()->isRangeComparator.intersectionWith(Range2));
	}
	
	@Test
	void testIntersectionWithBetweenOutOfBound() {
		Range<Double> Range2 = Range.between(1.0,2.0,Comparator.reverseOrder());
		assertThrows(IllegalArgumentException.class,()->betweenRangeComparator.intersectionWith(Range2));
	}
	
	@Test
	void testIntersectionWithIsValid() {
		Range<Double> Range2 = Range.is(3.1415);
		assertEquals(isRangeComparator.intersectionWith(Range2),isRangeComparator);
	}
	
	@Test
	void testIntersectionWithBetweenValid() {
		Range<Double> Range2 = Range.between(3.0,4.0,Comparator.reverseOrder());
		assertEquals(betweenRangeComparator.intersectionWith(Range2),Range.between(3.0,4.0,Comparator.reverseOrder()));
	}
	
	@Test
	void testIntersectionWithBetweenSame() {
		Range<Double> Range2 = Range.between(3.0, 4.0,Comparator.reverseOrder());
		assertEquals(betweenRangeComparator.intersectionWith(Range2),Range.between(3.0,4.0,Comparator.reverseOrder()));
	}

	@Test
	void testEqualsObjectIsNull() {
		Range<Double> Range2 =null;
		assertFalse(isRangeComparator.equals(Range2));
	}
	
	@Test
	void testEqualsObjectBetweenNull() {
		Range<Double> Range2 =null;
		assertFalse(betweenRangeComparator.equals(Range2));
	}
	
	@Test
	void testEqualsObjIsNotEqual() {
		Range<Double> Range2 = Range.is(2.0,Comparator.reverseOrder());
		assertFalse(isRangeComparator.equals(Range2));
	}
	
	@Test
	void testEqualsObjIsEqual() {
		Range<Double> Range2 = Range.is(3.1415,Comparator.reverseOrder());
		assertTrue(isRangeComparator.equals(Range2));
	}
	
	@Test
	void testEqualesObjBetweenNotEqual() {
		Range<Double> Range2 = Range.between(2.0,5.0,Comparator.reverseOrder());
		assertFalse(betweenRangeComparator.equals(Range2));
	}
	
	@Test
	void testEqualesObjBetweenEqual() {
		Range<Double> Range2 = Range.between(3.0,5.0,Comparator.reverseOrder());
		assertTrue(betweenRangeComparator.equals(Range2));
	}

	@Test
	void testToStringIs() {
		//fail("Not yet implemented");
		assertEquals(isRangeComparator.toString(),"[3.1415..3.1415]");
	}

	@Test
	void testToStringBetween() {
		//fail("Not yet implemented");
		assertEquals(betweenRangeComparator.toString(),"[5.0..3.0]");
	}

}
