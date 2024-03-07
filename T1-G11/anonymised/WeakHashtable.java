import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of Hashtable that uses WeakReference's
 * to hold its keys thus allowing them to be reclaimed by the garbage collector.
 */
public final class WeakHashtable extends Hashtable {

    /** Serializable version identifier. */
    private static final long serialVersionUID = -1546036869799732453L;

    /**
     * The maximum number of times put() or remove() can be called before
     * the map will be purged of all cleared entries.
     */
    private static final int MAX_CHANGES_BEFORE_PURGE = 100;

    /**
     * The maximum number of times put() or remove() can be called before
     * the map will be purged of one cleared entry.
     */
    private static final int PARTIAL_PURGE_COUNT     = 10;

    /* ReferenceQueue we check for gc'd keys */
    private final ReferenceQueue queue = new ReferenceQueue();
    /* Counter used to control how often we purge gc'd entries */
    private int changeCount = 0;

    /**
     * Constructs a WeakHashtable with the Hashtable default
     * capacity and load factor.
     */
    public WeakHashtable() {}

    /**
     *@see Hashtable
     */
    public boolean containsKey(Object key) {
        // purge should not be required
        Referenced referenced = new Referenced(key);
        return super.containsKey(referenced);
    }

    /**
     *@see Hashtable
     */
    public Enumeration elements() {
        purge();
        return super.elements();
    }

    /**
     *@see Hashtable
     */
    public Set entrySet() {
        purge();
        Set referencedEntries = super.entrySet();
        Set unreferencedEntries = new HashSet();
        for (Iterator it=referencedEntries.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            Referenced referencedKey = (Referenced) entry.getKey();
            Object key = referencedKey.getValue();
            Object value = entry.getValue();
            if (key != null) {
                Entry dereferencedEntry = new Entry(key, value);
                unreferencedEntries.add(dereferencedEntry);
            }
        }
        return unreferencedEntries;
    }

    /**
     *@see Hashtable
     */
    public Object get(Object key) {
        // for performance reasons, no purge
        Referenced referenceKey = new Referenced(key);
        return super.get(referenceKey);
    }

    /**
     *@see Hashtable
     */
    public Enumeration keys() {
        purge();
        final Enumeration enumer = super.keys();
        return new Enumeration() {
            public boolean hasMoreElements() {
                return enumer.hasMoreElements();
            }
            public Object nextElement() {
                 Referenced nextReference = (Referenced) enumer.nextElement();
                 return nextReference.getValue();
            }
        };
    }

    /**
     *@see Hashtable
     */
    public Set keySet() {
        purge();
        Set referencedKeys = super.keySet();
        Set unreferencedKeys = new HashSet();
        for (Iterator it=referencedKeys.iterator(); it.hasNext();) {
            Referenced referenceKey = (Referenced) it.next();
            Object keyValue = referenceKey.getValue();
            if (keyValue != null) {
                unreferencedKeys.add(keyValue);
            }
        }
        return unreferencedKeys;
    }

    /**
     *@see Hashtable
     */
    public synchronized Object put(Object key, Object value) {
        // check for nulls, ensuring semantics match superclass
        if (key == null) {
            throw new NullPointerException("Null keys are not allowed");
        }
        if (value == null) {
            throw new NullPointerException("Null values are not allowed");
        }

        // for performance reasons, only purge every
        // MAX_CHANGES_BEFORE_PURGE times
        if (changeCount++ > MAX_CHANGES_BEFORE_PURGE) {
            purge();
            changeCount = 0;
        }
        // do a partial purge more often
        else if (changeCount % PARTIAL_PURGE_COUNT == 0) {
            purgeOne();
        }

        Referenced keyRef = new Referenced(key, queue);
        return super.put(keyRef, value);
    }

    /**
     *@see Hashtable
     */
    public void putAll(Map t) {
        if (t != null) {
            Set entrySet = t.entrySet();
            for (Iterator it=entrySet.iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     *@see Hashtable
     */
    public Collection values() {
        purge();
        return super.values();
    }

    /**
     *@see Hashtable
     */
    public synchronized Object remove(Object key) {
        // for performance reasons, only purge every
        // MAX_CHANGES_BEFORE_PURGE times
        if (changeCount++ > MAX_CHANGES_BEFORE_PURGE) {
            purge();
            changeCount = 0;
        }
        // do a partial purge more often
        else if (changeCount % PARTIAL_PURGE_COUNT == 0) {
            purgeOne();
        }
        return super.remove(new Referenced(key));
    }

    /**
     *@see Hashtable
     */
    public boolean isEmpty() {
        purge();
        return super.isEmpty();
    }

    /**
     *@see Hashtable
     */
    public int size() {
        purge();
        return super.size();
    }

    /**
     *@see Hashtable
     */
    public String toString() {
        purge();
        return super.toString();
    }

    /**
     * @see Hashtable
     */
    protected void rehash() {
        // purge here to save the effort of rehashing dead entries
        purge();
        super.rehash();
    }

    /**
     * Purges all entries whose wrapped keys
     * have been garbage collected.
     */
    private void purge() {
        final List toRemove = new ArrayList();
        synchronized (queue) {
            WeakKey key;
            while ((key = (WeakKey) queue.poll()) != null) {
                toRemove.add(key.getReferenced());
            }
        }

        // LOGGING-119: do the actual removal of the keys outside the sync block
        // to prevent deadlock scenarios as purge() may be called from
        // non-synchronized methods too
        final int size = toRemove.size();
        for (int i = 0; i < size; i++) {
            super.remove(toRemove.get(i));
        }
    }

    /**
     * Purges one entry whose wrapped key
     * has been garbage collected.
     */
    private void purgeOne() {
        synchronized (queue) {
            WeakKey key = (WeakKey) queue.poll();
            if (key != null) {
                super.remove(key.getReferenced());
            }
        }
    }

    /** Entry implementation */
    private final static class Entry implements Map.Entry {

        private final Object key;
        private final Object value;

        private Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public boolean equals(Object o) {
            boolean result = false;
            if (o != null && o instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) o;
                result =    (getKey()==null ?
                                            entry.getKey() == null :
                                            getKey().equals(entry.getKey())) &&
                            (getValue()==null ?
                                            entry.getValue() == null :
                                            getValue().equals(entry.getValue()));
            }
            return result;
        }

        public int hashCode() {
            return (getKey()==null ? 0 : getKey().hashCode()) ^
                (getValue()==null ? 0 : getValue().hashCode());
        }

        public Object setValue(Object value) {
            throw new UnsupportedOperationException("Entry.setValue is not supported.");
        }

        public Object getValue() {
            return value;
        }

        public Object getKey() {
            return key;
        }
    }

    /** Wrapper giving correct symantics for equals and hashcode */
    private final static class Referenced {

        private final WeakReference reference;
        private final int           hashCode;

        /**
         *
         * @throws NullPointerException if referant is null
         */
        private Referenced(Object referant) {
            reference = new WeakReference(referant);
            // Calc a permanent hashCode so calls to Hashtable.remove()
            // work if the WeakReference has been cleared
            hashCode  = referant.hashCode();
        }

        /**
         *
         * @throws NullPointerException if key is null
         */
        private Referenced(Object key, ReferenceQueue queue) {
            reference = new WeakKey(key, queue, this);
            // Calc a permanent hashCode so calls to Hashtable.remove()
            // work if the WeakReference has been cleared
            hashCode  = key.hashCode();

        }

        public int hashCode() {
            return hashCode;
        }

        private Object getValue() {
            return reference.get();
        }

        public boolean equals(Object o) {
            boolean result = false;
            if (o instanceof Referenced) {
                Referenced otherKey = (Referenced) o;
                Object thisKeyValue = getValue();
                Object otherKeyValue = otherKey.getValue();
                if (thisKeyValue == null) {
                    result = otherKeyValue == null;

                    // Since our hashcode was calculated from the original
                    // non-null referant, the above check breaks the
                    // hashcode/equals contract, as two cleared Referenced
                    // objects could test equal but have different hashcodes.
                    // We can reduce (not eliminate) the chance of this
                    // happening by comparing hashcodes.
                    result = result && this.hashCode() == otherKey.hashCode();
                    // In any case, as our c'tor does not allow null referants
                    // and Hashtable does not do equality checks between
                    // existing keys, normal hashtable operations should never
                    // result in an equals comparison between null referants
                }
                else
                {
                    result = thisKeyValue.equals(otherKeyValue);
                }
            }
            return result;
        }
    }

    /**
     * WeakReference subclass that holds a hard reference to an
     * associated value and also makes accessible
     * the Referenced object holding it.
     */
    private final static class WeakKey extends WeakReference {

        private final Referenced referenced;

        private WeakKey(Object key,
                        ReferenceQueue queue,
                        Referenced referenced) {
            super(key, queue);
            this.referenced = referenced;
        }

        private Referenced getReferenced() {
            return referenced;
        }
     }
}
