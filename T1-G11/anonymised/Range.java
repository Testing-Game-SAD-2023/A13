import java.io.Serializable;
import java.util.Comparator;

/**
 * An immutable range of objects from a minimum to maximum point inclusive.
 * 
 * The objects need to either be implementations of Comparable
 * or you need to supply a Comparator. 
 *
 */
public final class Range<T> implements Serializable {

    /**
     * Serialization version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The ordering scheme used in this range.
     */
    private final Comparator<T> comparator;
    /**
     * The minimum value in this range (inclusive).
     */
    private final T minimum;
    /**
     * The maximum value in this range (inclusive).
     */
    private final T maximum;
    /**
     * Cached output hashCode (class is immutable).
     */
    private transient int hashCode;
    /**
     * Cached output toString (class is immutable).
     */
    private transient String toString;

    /**
     * Obtains a range using the specified element as both the minimum
     * and maximum in this range.
     * 
     * The range uses the natural ordering of the elements to determine where
     * values lie in the range.
     *
     * @param <T> the type of the elements in this range
     * @param element  the value to use for this range, not null
     * @return the range object, not null
     * @throws IllegalArgumentException if the element is null
     * @throws ClassCastException if the element is not Comparable
     */
    public static <T extends Comparable<T>> Range<T> is(final T element) {
        return between(element, element, null);
    }

    /**
     * Obtains a range using the specified element as both the minimum
     * and maximum in this range.
     * 
     * The range uses the specified Comparator to determine where
     * values lie in the range.
     *
     * @param <T> the type of the elements in this range
     * @param element  the value to use for this range, must not be null
     * @param comparator  the comparator to be used, null for natural ordering
     * @return the range object, not null
     * @throws IllegalArgumentException if the element is null
     * @throws ClassCastException if using natural ordering and the elements are not Comparable
     */
    public static <T> Range<T> is(final T element, final Comparator<T> comparator) {
        return between(element, element, comparator);
    }

    /**
     * Obtains a range with the specified minimum and maximum values (both inclusive).
     * 
     * The range uses the natural ordering of the elements to determine where
     * values lie in the range.
     *
     * The arguments may be passed in the order (min,max) or (max,min).
     * The getMinimum and getMaximum methods will return the correct values.
     *
     * @param <T> the type of the elements in this range
     * @param fromInclusive  the first value that defines the edge of the range, inclusive
     * @param toInclusive  the second value that defines the edge of the range, inclusive
     * @return the range object, not null
     * @throws IllegalArgumentException if either element is null
     * @throws ClassCastException if the elements are not Comparable
     */
    public static <T extends Comparable<T>> Range<T> between(final T fromInclusive, final T toInclusive) {
        return between(fromInclusive, toInclusive, null);
    }

    /**
     * Obtains a range with the specified minimum and maximum values (both inclusive).
     * 
     * The range uses the specified Comparator to determine where
     * values lie in the range.
     *
     * The arguments may be passed in the order (min,max) or (max,min).
     * The getMinimum and getMaximum methods will return the correct values.
     *
     * @param <T> the type of the elements in this range
     * @param fromInclusive  the first value that defines the edge of the range, inclusive
     * @param toInclusive  the second value that defines the edge of the range, inclusive
     * @param comparator  the comparator to be used, null for natural ordering
     * @return the range object, not null
     * @throws IllegalArgumentException if either element is null
     * @throws ClassCastException if using natural ordering and the elements are not Comparable
     */
    public static <T> Range<T> between(final T fromInclusive, final T toInclusive, final Comparator<T> comparator) {
        return new Range<T>(fromInclusive, toInclusive, comparator);
    }

    /**
     * Creates an instance.
     *
     * @param element1  the first element, not null
     * @param element2  the second element, not null
     * @param comp  the comparator to be used, null for natural ordering
     */
    @SuppressWarnings("unchecked")
    private Range(final T element1, final T element2, final Comparator<T> comp) {
        if (element1 == null || element2 == null) {
            throw new IllegalArgumentException("Elements in a range must not be null: element1=" +
                                               element1 + ", element2=" + element2);
        }
        if (comp == null) {
            this.comparator = ComparableComparator.INSTANCE;
        } else {
            this.comparator = comp;            
        }
        if (this.comparator.compare(element1, element2) < 1) {
            this.minimum = element1;
            this.maximum = element2;
        } else {
            this.minimum = element2;
            this.maximum = element1;
        }
    }

    // Accessors
    //--------------------------------------------------------------------

    /**
     * Gets the minimum value in this range.
     *
     * @return the minimum value in this range, not null
     */
    public T getMinimum() {
        return minimum;
    }

    /**
     * Gets the maximum value in this range.
     *
     * @return the maximum value in this range, not null
     */
    public T getMaximum() {
        return maximum;
    }

    /**
     * Gets the comparator being used to determine if objects are within the range.
     * 
     * Natural ordering uses an internal comparator implementation, thus this
     * method never returns null.
     *
     * @return the comparator being used, not null
     */
    public Comparator<T> getComparator() {
        return comparator;
    }

    /**
     * Whether or not the Range is using the natural ordering of the elements.
     * 
     * Natural ordering uses an internal comparator implementation, thus this
     * method is the only way to check if a null comparator was specified.
     *
     * @return true if using natural ordering
     */
    public boolean isNaturalOrdering() {
        return comparator == ComparableComparator.INSTANCE;
    }

    // Element tests
    //--------------------------------------------------------------------

    /**
     * Checks whether the specified element occurs within this range.
     *
     * @param element  the element to check for, null returns false
     * @return true if the specified element occurs within this range
     */
    public boolean contains(final T element) {
        if (element == null) {
            return false;
        }
        return comparator.compare(element, minimum) > -1 && comparator.compare(element, maximum) < 1;
    }

    /**
     * Checks whether this range is after the specified element.
     *
     * @param element  the element to check for, null returns false
     * @return true if this range is entirely after the specified element
     */
    public boolean isAfter(final T element) {
        if (element == null) {
            return false;
        }
        return comparator.compare(element, minimum) < 0;
    }

    /**
     * Checks whether this range starts with the specified element.
     *
     * @param element  the element to check for, null returns false
     * @return true if the specified element occurs within this range
     */
    public boolean isStartedBy(final T element) {
        if (element == null) {
            return false;
        }
        return comparator.compare(element, minimum) == 0;
    }

    /**
     * Checks whether this range starts with the specified element.
     *
     * @param element  the element to check for, null returns false
     * @return true if the specified element occurs within this range
     */
    public boolean isEndedBy(final T element) {
        if (element == null) {
            return false;
        }
        return comparator.compare(element, maximum) == 0;
    }

    /**
     * Checks whether this range is before the specified element.
     *
     * @param element  the element to check for, null returns false
     * @return true if this range is entirely before the specified element
     */
    public boolean isBefore(final T element) {
        if (element == null) {
            return false;
        }
        return comparator.compare(element, maximum) > 0;
    }

    /**
     * Checks where the specified element occurs relative to this range.
     * 
     * The API is reminiscent of the Comparable interface returning -1 if
     * the element is before the range, 0 if contained within the range and
     * code 1 if the element is after the range. 
     *
     * @param element  the element to check for, not null
     * @return -1, 0 or +1 depending on the element's location relative to the range
     */
    public int elementCompareTo(final T element) {
        if (element == null) {
            // Comparable API says throw NPE on null
            throw new NullPointerException("Element is null");
        }
        if (isAfter(element)) {
            return -1;
        } else if (isBefore(element)) {
            return 1;
        } else {
            return 0;
        }
    }

    // Range tests
    //--------------------------------------------------------------------

    /**
     * Checks whether this range contains all the elements of the specified range.
     *
     * This method may fail if the ranges have two different comparators or element types.
     *
     * @param otherRange  the range to check, null returns false
     * @return true if this range contains the specified range
     * @throws RuntimeException if ranges cannot be compared
     */
    public boolean containsRange(final Range<T> otherRange) {
        if (otherRange == null) {
            return false;
        }
        return contains(otherRange.minimum)
            && contains(otherRange.maximum);
    }

    /**
     * Checks whether this range is completely after the specified range.
     *
     * This method may fail if the ranges have two different comparators or element types.
     *
     * @param otherRange  the range to check, null returns false
     * @return true if this range is completely after the specified range
     * @throws RuntimeException if ranges cannot be compared
     */
    public boolean isAfterRange(final Range<T> otherRange) {
        if (otherRange == null) {
            return false;
        }
        return isAfter(otherRange.maximum);
    }

    /**
     * Checks whether this range is overlapped by the specified range.
     * 
     * Two ranges overlap if there is at least one element in common.
     *
     * This method may fail if the ranges have two different comparators or element types.
     *
     * @param otherRange  the range to test, null returns false
     * @return true if the specified range overlaps with this
     *  range; otherwise, {@code false}
     * @throws RuntimeException if ranges cannot be compared
     */
    public boolean isOverlappedBy(final Range<T> otherRange) {
        if (otherRange == null) {
            return false;
        }
        return otherRange.contains(minimum)
            || otherRange.contains(maximum)
            || contains(otherRange.minimum);
    }

    /**
     * Checks whether this range is completely before the specified range.
     *
     * This method may fail if the ranges have two different comparators or element types.
     *
     * @param otherRange  the range to check, null returns false
     * @return true if this range is completely before the specified range
     * @throws RuntimeException if ranges cannot be compared
     */
    public boolean isBeforeRange(final Range<T> otherRange) {
        if (otherRange == null) {
            return false;
        }
        return isBefore(otherRange.minimum);
    }

    /**
     * Calculate the intersection of this and an overlapping Range.
     * @param other overlapping Range
     * @return range representing the intersection of this and other (this if equal)
     * @throws IllegalArgumentException if other does not overlap this
     */
    public Range<T> intersectionWith(final Range<T> other) {
        if (!this.isOverlappedBy(other)) {
            throw new IllegalArgumentException(String.format(
                "Cannot calculate intersection with non-overlapping range %s", other));
        }
        if (this.equals(other)) {
            return this;
        }
        final T min = getComparator().compare(minimum, other.minimum) < 0 ? other.minimum : minimum;
        final T max = getComparator().compare(maximum, other.maximum) < 0 ? maximum : other.maximum;
        return between(min, max, getComparator());
    }

    // Basics
    //--------------------------------------------------------------------

    /**
     * Compares this range to another object to test if they are equal..
     *
     * To be equal, the minimum and maximum values must be equal, which
     * ignores any differences in the comparator.
     *
     * @param obj the reference object with which to compare
     * @return true if this object is equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || obj.getClass() != getClass()) {
            return false;
        } else {
            @SuppressWarnings("unchecked") // OK because we checked the class above
            final
            Range<T> range = (Range<T>) obj;
            return minimum.equals(range.minimum) &&
                   maximum.equals(range.maximum);
        }
    }

    /**
     * Gets a suitable hash code for the range.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        int result = hashCode;
        if (hashCode == 0) {
            result = 17;
            result = 37 * result + getClass().hashCode();
            result = 37 * result + minimum.hashCode();
            result = 37 * result + maximum.hashCode();
            hashCode = result;
        }
        return result;
    }

    /**
     * Gets the range as a String.
     *
     * The format of the String is '[min..max]'.
     *
     * @return the String representation of this range
     */
    @Override
    public String toString() {
        String result = toString;
        if (result == null) {
            final StringBuilder buf = new StringBuilder(32);
            buf.append('[');
            buf.append(minimum);
            buf.append("..");
            buf.append(maximum);
            buf.append(']');
            result = buf.toString();
            toString = result;
        }
        return result;
    }

    /**
     * Formats the receiver using the given format.
     * 
     * This uses java.util.Formattable to perform the formatting. Three variables may
     * be used to embed the minimum, maximum and comparator.
     * Use %1$s for the minimum element, %2$s for the maximum element
     * and %3$s for the comparator.
     * The default format used by toString() is [%1$s..%2$s].
     * 
     * @param format  the format string, optionally containing %1$s, %2$s and %3$s, not null
     * @return the formatted string, not null
     */
    public String toString(final String format) {
        return String.format(format, minimum, maximum, comparator);
    }

    //-----------------------------------------------------------------------
    @SuppressWarnings({"rawtypes", "unchecked"})
    private enum ComparableComparator implements Comparator {
        INSTANCE;
        /**
         * Comparable based compare implementation. 
         *
         * @param obj1 left hand side of comparison
         * @param obj2 right hand side of comparison
         * @return negative, 0, positive comparison value
         */
        @Override
        public int compare(final Object obj1, final Object obj2) {
            return ((Comparable) obj1).compareTo(obj2);
        }
    }

}
