package ribomation.clean_code.comments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A bounded queue, that drops old elements when full.
 */
@SuppressWarnings("unchecked")
public class BoundedDroppingQueue<ElementType> implements Iterable<ElementType> {
    private final Object[] elements;
    private int putIndex = 0;
    private int getIndex = 0;
    private int queueSize = 0;

    /**
     * Creates the queue with the given capacity.
     *
     * @param queueSize the (fixed) capacity
     */
    public BoundedDroppingQueue(int queueSize) {
        elements = new Object[queueSize];
    }

    /**
     * Inserts an element and overwrites old one when full.
     *
     * @param x element to insert
     */
    public void put(ElementType x) {
        //handling of full queue
        if (full()) {
            handleFullQueue();
        } else {
            queueSize++;
        }

        elements[putIndex] = x;
        putIndex = (putIndex + 1) % elements.length; //advance index modulo the capacity
    }

    private void handleFullQueue() {
        getIndex = (getIndex + 1) % elements.length;
    }

    /**
     * Removes the 'first' element.
     *
     * @return first element in the queue
     * @throws IllegalArgumentException if empty
     */
    public ElementType get() {
        if (empty()) {
            throw new IllegalArgumentException("Empty queue");
        }
        return getElementAndRemoveIt();
    }

    public int size() {
        return queueSize;
    }

    /**
     * Returns true if queueSize==0
     *
     * @return true if empty
     */
    public boolean empty() {
        return size() == 0; //checks is the current queueSize if zero
    }

    /**
     * Returns true if queueSize==capacity
     *
     * @return true if full
     */
    public boolean full() {
        return size() == elements.length; //checks if the current queueSize is equals to the capacity
    }

    private ElementType getElementAndRemoveIt() {
        ElementType x = (ElementType) elements[getIndex];
        handleFullQueue();
        queueSize--;
        return x;
    }

    /**
     * Returns an iterator intended usage in a foreach loop
     *
     * @return an iterator that iterates over all elements in the queue
     */
    public Iterator<ElementType> iterator() {
        return new Iterator<ElementType>() {
            int idx = getIndex;
            int N = queueSize;

            public boolean hasNext() {
                return N > 0;
            }

            public ElementType next() {
                ElementType x = (ElementType) elements[idx];
                idx = (idx + 1) % elements.length;
                N--;
                return x;
            }

            public void remove() {
                throw new UnsupportedOperationException("not implemented!");
            }
        };
    }

    /**
     * Returns a new list with all the elements in order
     *
     * @return a list
     */
    public List<ElementType> toList() {
        List<ElementType> result = new ArrayList<ElementType>(size()); //we put all elements into this list
        for (ElementType e : this) result.add(e); //copy all elements
        return result; //now return the copy
    }

    public String toString() {
        return toList().toString();
    }

}
