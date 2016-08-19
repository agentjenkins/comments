package ribomation.clean_code.comments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A bounded queue, that drops old elements when full.
 */
@SuppressWarnings("unchecked")
public class BoundedDroppingQueue<ElementType> implements Iterable<ElementType> {
    private static int defaultSize = 1;
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

    public static void setDefaultSize(int defaultSize) {
        BoundedDroppingQueue.defaultSize = defaultSize;
    }

    /**
     * Inserts an element and overwrites old one when full.
     *
     * @param x element to insert
     */
    public void put(ElementType x) {
        //handling of full queue
        if (full()) { //if full, then
            getIndex = (getIndex + 1) % elements.length; //just advance the index and overwrite old  entries
        } else {
            queueSize++;  //increment queueSize by one
        }//end if

        // insert element
        elements[putIndex] = x;
        putIndex = (putIndex + 1) % elements.length; //advance index modulo the capacity
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

    private ElementType getElementAndRemoveIt() {
        ElementType x = (ElementType) elements[getIndex];
        getIndex = (getIndex + 1) % elements.length;
        queueSize--;
        return x;
    }

    /**
     * Returns its current queueSize
     *
     * @return number of elements in the queue
     */
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

//    public boolean empty() {
//        return queueSize == 0;
//    }

    /**
     * Returns true if queueSize==capacity
     *
     * @return true if full
     */
    public boolean full() {
        return size() == elements.length; //checks if the current queueSize is equals to the capacity
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

            /**
             * return true if it has a next thingy
             * @return true if we can carry on
             */
            public boolean hasNext() {
                return N > 0;
            }

            /**
             * Returns the next entity
             * @return the next thingy
             */
            public ElementType next() {
                ElementType x = (ElementType) elements[idx];
                idx = (idx + 1) % elements.length;
                N--;
                return x;
            }

            //not used, throws an exceptions if invoked
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }

    //moved to StringUtils. (Kilroy was here)
//    /**
//     * Returns true if the given string is null, empty or filled with spaces.
//     * @param s     string to investigate
//     * @return  true if blank
//     */
//    public static boolean isBlank(String s) {
//        return s == null || s.trim().length() == 0;
//    }
//
//    /**
//     * Makes the initial letter upper-case.
//     * @param s the string
//     * @return first letter is now in upper-case
//     */
//    public static String toInitialCase(String s) {
//        if (isBlank(s)) return s;
//        if (s.length() == 1) return s.toUpperCase();
//        return s.substring(0, 1).toUpperCase() + s.substring(1);
//    }


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

    /**
     * This is the toString() function overridden from java.lang.Object
     *
     * @return a string
     */
    public String toString() {
        return toList().toString(); //invokes toString() on the created list
    }

}// END OF CLASS
