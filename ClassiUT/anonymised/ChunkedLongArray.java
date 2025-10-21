import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChunkedLongArray {
  public static final int DEFAULT_CHUNK_BITS = 20; // 1M entries = 8MB

  private final int chunkBits;
  private final int offsetMask;
  private final int chunkLength;
  private final List<long[]> chunks = new ArrayList<long[]>();
  private int size = 0;

  public ChunkedLongArray() {
    this(DEFAULT_CHUNK_BITS);
  }

  public ChunkedLongArray(int chunkBits) {
    this.chunkBits = chunkBits;
    chunkLength = (int) Math.pow(2, chunkBits);
    offsetMask = ~(~0 << chunkBits);
  }

  private ChunkedLongArray(List<long[]> chunks, int size, int chunkBits) {
    this(chunkBits);
    this.chunks.addAll(chunks);
    this.size = size;
  }

  public void add(long value) {
    set(size++, value);
  }

  public void set(int index, long value) {
    ensureSpace(index);
    chunks.get(index >>> chunkBits)[index &  offsetMask] = value;
    size = Math.max(size, index+1);
  }

  public long get(int index) {
    if (index >= size) {
      throw new ArrayIndexOutOfBoundsException("Index " + index + " requested with array length " + size);
    }
    return chunks.get(index >>> chunkBits)[index &  offsetMask];
  }

  public int size() {
    return size;
  }

  private void ensureSpace(final int index) {
    while (chunks.size() <= (index >>> chunkBits)) {
      try {
        chunks.add(new long[chunkLength]);
      } catch (OutOfMemoryError e) {
        throw new OutOfMemoryError(String.format(
            "OOM (%s) while allocating long[%d] (%dMB) in addition to the existing %d chunks (%d entries, %dMB). %s",
            e.toString(), chunkLength, 1L*chunkLength*8/1048576, chunks.size(), size(),
            1L*chunks.size()*chunkLength*8/1048576, memStats()));
      }
    }
  }

  public void set(ChunkedLongArray src, int srcPos, int destPos, int length) {
    while (length > 0) {
      int srcChunk = srcPos >>> src.chunkBits;
      int srcOffset = srcPos & src.offsetMask;
      if (src.chunks.get(srcChunk).length - srcOffset >= length) {
        set(src.chunks.get(srcChunk), srcOffset, destPos, length);
        return;
      }
      int subLength = src.chunkLength - srcOffset;
      set(src.chunks.get(srcChunk), srcOffset, destPos, subLength);
      length -= subLength;
      srcPos += subLength;
      destPos += subLength;
      size = Math.max(size, destPos+1);
    }
  }

  public void set(long[] src, int srcPos, int destPos, int length) {
    ensureSpace(destPos+length);
    while (length > 0) {
      int destChunk = destPos >>> chunkBits;
      int destOffset = destPos & offsetMask;
      if (chunkLength - destOffset >= length) {
        System.arraycopy(
            src, srcPos, chunks.get(destChunk), destOffset, length);
        return;
      }
      int subLength = chunkLength - destOffset;
      System.arraycopy(src, srcPos, chunks.get(destChunk), destOffset, subLength);
      length -= subLength;
      srcPos += subLength;
      destPos += subLength;
    }
  }

  private void swap(int index1, int index2) {
    long tmp = get(index1);
    set(index1, get(index2));
    set(index2, tmp);
  }

  public void sort() {
    quickSort();
  }

  private void quickSort() {
    quickSort(this, 0, size-1);
  }

  private void quickSort(ChunkedLongArray src, int left, int right) {
    // If the list has < 2 items
    if (left >= right) {
      return;
    }
    // Simple & dumb middle
    int pivotIndex = left + (right-left)/2;

    // Get lists of bigger and smaller items and final position of pivot
    int pivotNewIndex = partition(src, left, right, pivotIndex);

    // Recursively sort elements smaller than the pivot
    quickSort(src, left, pivotNewIndex - 1);

    // Recursively sort elements at least as big as the pivot
    quickSort(src, pivotNewIndex + 1, right);
  }

  // left is the index of the leftmost element of the array
  // right is the index of the rightmost element of the array (inclusive)
  // number of elements in subarray = right-left+1
  private int partition(
      ChunkedLongArray src, int left, int right, int pivotIndex) {
    long pivotValue = src.get(pivotIndex);
    src.swap(pivotIndex, right);  // Move pivot to end
    int storeIndex = left;
    for (int i = left ; i <= right-1 ; i++) {  // left ≤ i < right
      if (src.get(i) < pivotValue) {
        src.swap(i, storeIndex);
        storeIndex++;
      }
    }
    src.swap(storeIndex, right);  // Move pivot to its final place
    return storeIndex;
  }

  private int binarySearch(int start, int end, long value) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * Note: Capacity auto-increases.
   * @return the current capacity.
   */
  public int capacity() {
    return chunks.size()*chunkLength;
  }

  private static final Locale locale = new Locale("en");
  public static String memStats() {
      Runtime r = Runtime.getRuntime();
      return String.format(
          locale,
          "Allocated memory: %s, Allocated unused memory: %s, Heap memory used: %s, Max memory: %s",
          reduce(r.totalMemory()), reduce(r.freeMemory()),
          reduce(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed()), reduce(r.maxMemory())
      );
  }
  private static String reduce(long bytes) {
      return bytes / 1048576 + "MB";
  }

  @Override
  public String toString() {
    return "ChunkedLongArray(" + size + " entries, " + chunks.size()
           + " chunks, " + 1L*chunks.size()*chunkLength*8/1047576 + "MB)";
  }
}

