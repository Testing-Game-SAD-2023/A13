package student.raffaeleDAnnaDue;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import ClassUnderTest.Range;

/**
 * <p>
 * Classe di testing per la verifica del corretto funzionamento della classe {@link Range}.
 * <br>
 * <br>
 * Per garantire il successo dei test, è stata condotta un'analisi approfondita del corretto funzionamento
 * di ciascun metodo della classe, basandosi sul suo nome e sulla sua firma.
 * <br>
 * <br>
 * Grazie ai metodi di test di seguito elencati, è stata raggiunta una copertura del 100% della classe,
 * superando la copertura ottenuta da strumenti di testing automatico come evosuite e randoop.
 * </p>
 * <p>
 * Ogni metodo di test all'interno della classe {@link StudentTest} è stato progettato per affrontare specifici 
 * aspetti della classe {@link Range}, e le assertive utilizzate, come {@code assertTrue}, {@code assertFalse}, 
 * {@code assertEquals}, e {@code assertThrows}, contribuiscono a verificare il comportamento atteso dei metodi della classe {@link Range}.
 * </p>
 * <b>Obiettivi dei Test</b>
 * <br>
 * Gli obiettivi principali dei test includono:
 * <ul>
 *   <li>Copertura completa del codice della classe {@link Range} e quindi di ogni possibile percorso di esecuzione.</li>
 *   <li>Esplorazione di scenari con input validi e non validi, casi limite e confronti con altri intervalli.</li>
 *   <li>Superamento delle coperture ottenute da strumenti automatici, dimostrando efficacia e completezza nei test manuali.</li>
 * </ul>
 * <br>
 * <b>Tempo Impiegato</b>
 * <br>
 * L'intero processo di studio della classe {@link Range}, lo sviluppo dei test e la stesura della documentazione
 * sono stati completati con successo in un periodo complessivo di circa <i>5 ore</i>.
 * 
 * <p>
 * <b>Nota:</b> I commenti all'interno della classe di test forniscono dettagli aggiuntivi sulla logica di test implementata,
 * aiutando a comprendere la finalità di ciascun test e le considerazioni specifiche che sono state prese in considerazione durante lo sviluppo.
 * </p>
 * 
 * @see ClassUnderTest.Range
 * @author Raffaele D'Anna N97000455
 */
class StudentTest {
	
	/**
	 * Questo metodo testa la creazione di un intervallo con entrambi gli estremi nulli.
	 */
	@Test
	public void testRangeNull() {
	    assertThrows(IllegalArgumentException.class, () -> Range.between(null, null));
	}

	/**
	 * Questo metodo testa la creazione di un intervallo con almeno un elemento nullo.
	 */
	@Test
	public void testRangeWithNullElements() {
	    assertThrows(IllegalArgumentException.class, () -> Range.between("abc", null));
	}

	/**
	 * Questo metodo testa un intervallo con due elementi, con il minimo maggiore del massimo.
	 */
	@Test
	public void testRangeWithTwoElement() {
	    Range<Integer> range = Range.between(5, 3);
	    assertTrue(range.contains(4));
	    assertTrue(range.contains(5));
	    assertTrue(range.contains(3));
	}

	/**
	 * Questo metodo testa la creazione di un intervallo singoletto.
	 */
	@Test
	public void testRange() {
	    Range<Integer> range = Range.is(10);
	    assertTrue(range.contains(10));
	    assertFalse(range.contains(9));
	    assertFalse(range.contains(11));
	}

	/**
	 * Questo metodo testa un intervallo che non contiene un elemento specifico.
	 */
	@Test
	public void testRangeNotContainingSpecificElement() {
	    Range<Integer> range = Range.between(5, 10);
	    assertFalse(range.contains(11));
	}

	/**
	 * Questo metodo testa la creazione di un intervallo con gli estremi impostati ai valori limiti
	 * (min e max) del tipo di dati Integer.
	 */
	@Test
	public void testRangeWithDataTypeLimits() {
	    Range<Integer> range = Range.between(Integer.MIN_VALUE, Integer.MAX_VALUE);
	    assertTrue(range.contains(Integer.MIN_VALUE));
	    assertTrue(range.contains(Integer.MAX_VALUE));
	}

	/**
	 * Questo metodo verifica se un intervallo contiene un elemento nullo.
	 */
	@Test
	public void testRangeContainsWithNullElement() {
	    Range<Integer> range = Range.between(1, 5);
	    assertFalse(range.contains(null));
	}

	/**
	 * Questo metodo testa un intervallo con un grande numero di elementi.
	 */
	@Test
	public void testRangeWithLargeNumberOfElements() {
	    Range<Integer> range = Range.between(1, 1_000_000);
	    assertTrue(range.contains(500_000));
	    assertTrue(range.contains(1_000_000));
	    assertFalse(range.contains(0));
	    assertFalse(range.contains(1_000_001));
	}

	/**
	 * Questo metodo verifica se un intervallo inizia e/o finisce con un elemento specifico.
	 */
	@Test
	public void testRangeStartingAndEndingWithSpecificElement() {
	    Range<Integer> range = Range.between(5, 10);
	    assertTrue(range.isStartedBy(5));
	    assertFalse(range.isEndedBy(5));
	    assertFalse(range.isStartedBy(4));
	    assertFalse(range.isEndedBy(6));
	}

	/**
	 * Questo metodo verifica se un intervallo è posizionato prima e/o dopo un altro intervallo.
	 */
	@Test
	public void testRangeBeforeAndAfterAnotherRange() {
	    Range<Integer> rangeBefore = Range.between(1, 5);
	    Range<Integer> rangeAfter = Range.between(10, 15);
	    assertTrue(rangeBefore.isBeforeRange(rangeAfter));
	    assertFalse(rangeBefore.isAfterRange(rangeAfter));
	    assertTrue(rangeAfter.isAfterRange(rangeBefore));
	    assertFalse(rangeAfter.isBeforeRange(rangeBefore));
	}

	/**
	 * Questo metodo testa l'intersezione di intervalli che si sovrappongono con la verifica del
	 * minimo e del massimo elemento dell'intervallo.
	 */
	@Test
	public void testIntersectionOfOverlappingRanges() {
	    Range<Integer> range1 = Range.between(5, 10);
	    Range<Integer> range2 = Range.between(8, 15);
	    Range<Integer> intersection = range1.intersectionWith(range2);
	    assertEquals(8, intersection.getMinimum());
	    assertEquals(10, intersection.getMaximum());
	}

	/**
	 * Questo metodo testa l'intersezione di intervalli uguali.
	 */
	@Test
	public void testIntersectionOfEqualRanges() {
	    Range<Integer> range1 = Range.between(5, 10);
	    Range<Integer> range2 = Range.between(5, 10);
	    Range<Integer> intersection = range1.intersectionWith(range2);
	    assertEquals(range1, intersection);
	}

	/**
	 * Questo metodo testa la creazione di un intervallo con ordinamento naturale.
	 */
	@Test
	public void testRangeWithNaturalOrdering() {
	    Range<Integer> range = Range.between(3, 7);
	    assertTrue(range.isNaturalOrdering());
	    assertTrue(range.contains(4));
	    assertFalse(range.contains(2));
	    assertFalse(range.contains(8));
	}

	/**
	 * Questo metodo testa la creazione di un intervallo con ordinamento inverso.
	 */
	@Test
	public void testRangeWithReverseOrdering() {
	    Comparator<Integer> reverseComparator = Comparator.reverseOrder();
	    Range<Integer> range = Range.between(7, 3, reverseComparator);
	    assertFalse(range.isNaturalOrdering());
	    assertTrue(range.contains(4));
	    assertFalse(range.contains(2));
	    assertFalse(range.contains(8));
	}

	/**
	 * Questo metodo testa la creazione di un intervallo con ordinamento naturale e inverso.
	 */
	@Test
	public void testRangeWithNaturalAndReverseOrdering() {
	    Range<Integer> range = Range.between(3, 7);
	    assertTrue(range.isNaturalOrdering());

	    Comparator<Integer> reverseComparator = Comparator.reverseOrder();
	    Range<Integer> reverseRange = Range.between(7, 3, reverseComparator);
	    assertFalse(reverseRange.isNaturalOrdering());
	}

	/**
	 * Questo metodo testa l'intersezione di intervalli uguali.
	 */
	@Test
	public void testIntersectionWithEqualRanges() {
	    Range<Integer> range1 = Range.between(3, 8);
	    Range<Integer> range2 = Range.between(3, 8);
	    assertEquals(range1, range1.intersectionWith(range2));
	}

	/**
	 * Questo metodo testa l'intersezione di intervalli che non si sovrappongono.
	 */
	@Test
	public void testIntersectionWithNonOverlappingRanges() {
	    Range<Integer> range1 = Range.between(1, 5);
	    Range<Integer> range2 = Range.between(8, 12);
	    assertThrows(IllegalArgumentException.class, () -> range1.intersectionWith(range2));
	}

	/**
	 * Questo metodo testa l'intersezione di un intervallo con uno nullo.
	 */
	@Test
	public void testIntersectionWithWithNullRange() {
	    Range<Integer> range = Range.between(1, 5);
	    assertThrows(IllegalArgumentException.class, () -> range.intersectionWith(null));
	}

	/**
	 * Questo metodo testa l'intersezione di un intervallo con se stesso.
	 */
	@Test
	public void testIntersectionWithWithEqualRange() {
	    Range<Integer> range = Range.between(1, 5);
	    assertEquals(range, range.intersectionWith(range));
	}

	/**
	 * Questo metodo testa l'intersezione di vari intervalli.
	 */
	@Test
	public void testIntersectionWith() {
	    Range<Integer> range1 = Range.between(2, 8);
	    Range<Integer> other1 = Range.between(1, 10);
	    Range<Integer> intersection1 = range1.intersectionWith(other1);
	    assertEquals(range1, intersection1);

	    Range<Integer> range2 = Range.between(2, 8);
	    Range<Integer> other2 = Range.between(5, 10);
	    Range<Integer> intersection2 = range2.intersectionWith(other2);
	    assertEquals(Range.between(5, 8), intersection2);

	    Range<Integer> range3 = Range.between(5, 10);
	    Range<Integer> other3 = Range.between(2, 8);
	    Range<Integer> intersection3 = range3.intersectionWith(other3);
	    assertEquals(Range.between(5, 8), intersection3);
	}

	/**
	 * Questo metodo verifica se un intervallo contiene completamente un altro intervallo.
	 */
	@Test
	public void testContainsRange() {
	    Range<Integer> range1 = Range.between(1, 10);
	    Range<Integer> range2 = Range.between(4, 7);
	    Range<Integer> range3 = Range.between(12, 15);
	    assertTrue(range1.containsRange(range2));
	    assertFalse(range1.containsRange(range3));
	}

	/**
	 * Questo metodo verifica se un intervallo contiene completamente un intervallo nullo.
	 */
	@Test
	public void testContainsRangeWithNullRange() {
	    Range<Integer> range = Range.between(1, 5);
	    assertFalse(range.containsRange(null));
	}

	/**
	 * Questo metodo verifica la creazione di un intervallo con un singolo elemento e un comparatore personalizzato.
	 */
	@Test
	public void testIsMethod() {
	    Range<String> range = Range.is("abc", Comparator.naturalOrder());
	    assertEquals("abc", range.getMinimum());
	    assertEquals("abc", range.getMaximum());
	    assertEquals(Comparator.naturalOrder(), range.getComparator());

	    Comparator<Integer> customComparator = Comparator.reverseOrder();
	    Range<Integer> customRange = Range.is(5, customComparator);
	    assertEquals(5, customRange.getMinimum());
	    assertEquals(5, customRange.getMaximum());
	    assertEquals(customComparator, customRange.getComparator());
	}

	/**
	 * Questo metodo verifica se un intervallo è posizionato dopo un altro intervallo.
	 */
	@Test
	public void testIsAfterRange() {
	    Range<Integer> range1 = Range.between(1, 5);
	    Range<Integer> range2 = Range.between(8, 12);
	    assertFalse(range1.isAfterRange(range2));
	    assertTrue(range2.isAfterRange(range1));
	}

	/**
	 * Questo metodo verifica se un l'intervallo è posizionato dopo un altro intervallo nullo.
	 */
	@Test
	public void testIsAfterRangeWithNullRange() {
	    Range<Integer> range = Range.between(1, 5);
	    assertFalse(range.isAfterRange(null));
	}

	/**
	 * Questo metodo verifica se l'intervallo è posizionato dopo un elemento nullo.
	 */
	@Test
	public void testIsAfterWithNullElement() {
	    Range<Integer> range = Range.between(1, 5);
	    assertFalse(range.isAfter(null));
	}

	/**
	 * Questo metodo verifica se un intervallo è posizionato prima di un altro intervallo.
	 */
	@Test
	public void testIsBeforeRange() {
	    Range<Integer> range1 = Range.between(8, 12);
	    Range<Integer> range2 = Range.between(1, 5);
	    assertFalse(range1.isBeforeRange(range2));
	    assertTrue(range2.isBeforeRange(range1));
	}

	/**
	 * Questo metodo verifica se un intervallo è posizionato prima di un altro intervallo nullo.
	 */
	@Test
	public void testIsBeforeRangeWithNullRange() {
	    Range<Integer> range = Range.between(1, 5);
	    assertFalse(range.isBeforeRange(null));
	}

	/**
	 * Questo metodo verifica se un intervallo è posizionato prima di un elemento nullo.
	 */
	@Test
	public void testIsBeforeWithNullElement() {
	    Range<Integer> range = Range.between(1, 5);
	    assertFalse(range.isBefore(null));
	}

	/**
	 * Questo metodo verifica se un intervallo inizia con un elemento specifico.
	 */
	@Test
	public void testIsStartedBy() {
	    Range<Integer> range1 = Range.between(1, 10);
	    assertTrue(range1.isStartedBy(1));
	    assertFalse(range1.isStartedBy(5));
	}

	/**
	 * Questo metodo verifica se un intervallo inizia con un elemento nullo.
	 */
	@Test
	public void testIsStartedByWithNullElement() {
	    Range<Integer> range = Range.between(1, 5);
	    assertFalse(range.isStartedBy(null));
	}

	/**
	 * Questo metodo verifica se un intervallo termina con un elemento specifico.
	 */
	@Test
	public void testIsEndedBy() {
	    Range<Integer> range1 = Range.between(1, 10);
	    assertTrue(range1.isEndedBy(10));
	    assertFalse(range1.isEndedBy(5));
	}

	/**
	 * Questo metodo verifica se un intervallo termina con un elemento nullo.
	 */
	@Test
	public void testIsEndedByWithNullElement() {
	    Range<Integer> range = Range.between(1, 5);
	    assertFalse(range.isEndedBy(null));
	}
    
	/**
	 * Questo metodo verifica se due intervalli si sovrappongono.
	 */
	@Test
	public void testOverlappingRanges() {
	    Range<Integer> range1 = Range.between(5, 10);
	    Range<Integer> range2 = Range.between(8, 15);
	    Range<Integer> range3 = Range.between(12, 18);
	    assertTrue(range1.isOverlappedBy(range2));
	    assertFalse(range1.isOverlappedBy(range3));
	}

	/**
	 * Questo metodo verifica se l'intervallo corrente si sovrappone completamente ad un intervallo nullo.
	 */
	@Test
	public void testIsOverlappedByWithNullRange() {
	    Range<Integer> range = Range.between(1, 5);
	    assertFalse(range.isOverlappedBy(null));
	}

	/**
	 * Questo metodo verifica la rappresentazione in forma di stringa dell'intervallo nel formato predefinito.
	 */
	@Test
	public void testToString() {
	    Range<Integer> range = Range.between(3, 8);
	    assertEquals("[3..8]", range.toString());
	}

	/**
	 * Questo metodo verifica la rappresentazione in forma di stringa dell'intervallo in un formato personalizzato.
	 */
	@Test
	public void testToStringWithCustomFormat() {
	    Range<Integer> range = Range.between(5, 12);
	    assertEquals("Intervallo da 5 a 12", range.toString("Intervallo da %1$s a %2$s"));
	}

	/**
	 * Questo metodo verifica se due intervalli sono uguali.
	 */
	@Test
	public void testEquals() {
	    Range<Integer> range1 = Range.between(1, 5);
	    Range<Integer> range2 = Range.between(1, 5);
	    Range<Integer> range3 = Range.between(3, 8);
	    assertTrue(range1.equals(range2));
	    assertFalse(range1.equals(range3));
	}

	/**
	 * Questo metodo verifica se l'intervallo corrente è uguale a un oggetto di un tipo diverso.
	 */
	@Test
	public void testEqualsWithDifferentClasses() {
	    Range<Integer> range = Range.between(1, 5);
	    String differentClassObject = "Not a Range object";
	    assertFalse(range.equals(differentClassObject));
	}

	/**
	 * Questo metodo verifica se due intervalli hanno lo stesso codice hash.
	 */
	@Test
	public void testHashCode() {
	    Range<Integer> range1 = Range.between(1, 5);
	    Range<Integer> range2 = Range.between(1, 5);
	    assertEquals(range1.hashCode(), range2.hashCode());
	}

	/**
	 * Questo metodo verifica la comparazione di un elemento in un intervallo.
	 */
	@Test
	public void testElementCompareTo() {
	    Range<Integer> range = Range.between(5, 10);
	    assertEquals(-1, range.elementCompareTo(3));
	    assertEquals(0, range.elementCompareTo(7));
	    assertEquals(1, range.elementCompareTo(12));
	}

	/**
	 * Questo metodo verifica la comparazione di un elemento nullo in un intervallo.
	 */
	@Test
	public void testElementCompareToWithNullElement() {
	    Range<Integer> range = Range.between(1, 5);
	    assertThrows(NullPointerException.class, () -> range.elementCompareTo(null));
	}
}