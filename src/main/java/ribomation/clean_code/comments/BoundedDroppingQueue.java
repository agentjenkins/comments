package ribomation.clean_code.comments;

// --- Revisions ----
// 2015-06-26 (ribomation) Fixed missing default param
// 2015-06-19 (ribomation) updated its distribution repo url
// 2015-06-05 (ribomation) commit after merge
// 2015-06-05 (Jens Riboe) final commit before merge into master
// 2015-06-05 (ribomation) final commit before merge into master
// 2015-05-29 (ribomation) Added support for setting the X-Load-Impact_Agent request header from jenkins plugin
// 2015-05-29 (ribomation) enabled exec bit on gradlew
// 2015-05-29 (ribomation) minor editorial edits
// 2015-05-29 (ribomation) Added X-Load-Impact_Agent request header showing the sdk version
// 2015-05-29 (ribomation) Added build-data properties file bundled in class-path. Added agent version req header.
// 2015-05-27 (ribomation) minor updates
// 2015-05-26 (ribomation) Updated the docs
// 2015-05-26 (ribomation) tracked down the problem with progress-result.
// 2015-05-25 (ribomation) Updated README with gradle instructions
// 2015-05-24 (ribomation) Completed the gradle build migration. Added integration-test CLI configurations. Added a check-my-account app intended to check the fatjar.
// 2015-05-23 (ribomation) added the wrapper
// 2015-05-21 (ribomation) Added Gradle build. Refactored the source organization. Added separate integrationTest task. Refined the running-load tests.
// 2015-05-20 (ribomation) Updated LoadZone to better handle city names with spaces and non-ascii letters
// 2015-05-20 (ribomation) Added integration-test for running a load-test. All integration-tests implemented so far, now succeeds.
// 2015-05-18 (ribomation) Enabled all integration tests to run by 'mvn test'
// 2015-05-15 (ribomation) Added integration test for usage of starting/aborting a load-test
// 2015-05-15 (ribomation) very minor refactoring of some unit tests and its test-data
// 2015-05-15 (ribomation) Added integration test for usage of test-configurations
// 2015-05-15 (ribomation) Added integration tests for:  token validation  load-zone usage  data-store usage  scenario usage
// 2015-05-11 (ribomation) Investigation and initial bugfix. still investigating more response problems
// 2014-05-14 (vagrant) [maven-release-plugin] prepare for next development iteration
// 2014-05-14 (Robin Gustafsson) Fix pom merge conflict.
// 2014-05-14 (vagrant) [maven-release-plugin] prepare release Load-Impact-Java-SDK-1.2
// 2014-05-14 (Robin Gustafsson) Update SDK version in README.
// 2014-05-07 (Robin Gustafsson) Fix indentation in README.


/***** IMPORTS *****************************************/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A bounded queue, that drops old elements when full.
 *
 * @author jens
 * @author robin
 * @author ribomation
 */
@SuppressWarnings("unchecked")
public class BoundedDroppingQueue<ElementType> implements Iterable<ElementType> {
    // ---- private instance variables ----
    private static int defaultSize = 1;     //its default size
    private final Object[] elements;        //this is where we store all elements
    private int putIdx = 0;                 //index of put()
    private int getIdx = 0;                 //index of get()
    private int size   = 0;                 //the current size of the queue

    // ---- constructors ----

    /**
     * Creates the queue with the given capacity.
     *
     * @param size the (fixed) capacity
     */
    public BoundedDroppingQueue(int size) {
        elements = new Object[size];
    }

    /* default constructor */
    public BoundedDroppingQueue() {
        this(defaultSize);
    }

    // ----setters ----

    /**
     * Sets the default size
     * @param defaultSize its new default size
     */
    public static void setDefaultSize(int defaultSize) {
        BoundedDroppingQueue.defaultSize = defaultSize;
    }

    // ---- business operations ----

    /**
     * Inserts an element and overwrites old one when full.
     *
     * @param x element to insert
     */
    public void put(ElementType x) {
        //handling of full queue
        if (full()) { //if full, then
            getIdx = (getIdx + 1) % elements.length; //just advance the index and overwrite old  entries
        } else {
            size++;  //increment size by one
        }//end if

        // insert element
        elements[putIdx] = x;
        putIdx = (putIdx + 1) % elements.length; //advance index modulo the capacity
    }

    /**
     * Removes the 'first' element.
     *
     * @return first element in the queue
     * @throws IllegalArgumentException if empty
     */
    public ElementType get() {
        if (empty()) { //if empty, throw an exception
            throw new IllegalArgumentException("Empty queue");
        }

        //--- removing an element and returning it ----
        ElementType x = (ElementType) elements[getIdx]; //get it
        getIdx = (getIdx + 1) % elements.length;        //advance index
        size--;                                         //decrement size by one
        return x;
    }

    /**
     * Returns its current size
     *
     * @return number of elements in the queue
     */
    public int size() {
        return size;
    }

    /**
     * Returns true if size==0
     *
     * @return true if empty
     */
    public boolean empty() {
        return size() == 0; //checks is the current size if zero
    }

//    public boolean empty() {
//        return size == 0;
//    }

    /**
     * Returns true if size==capacity
     *
     * @return true if full
     */
    public boolean full() {
        return size() == elements.length; //checks if the current size is equals to the capacity
    }

    /**
     * Returns an iterator intended usage in a foreach loop
     *
     * @return an iterator that iterates over all elements in the queue
     */
    public Iterator<ElementType> iterator() {
        return new Iterator<ElementType>() {
            int idx = getIdx;
            int N = size;

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
