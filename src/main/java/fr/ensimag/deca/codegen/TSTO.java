package fr.ensimag.deca.codegen;

/**
 * TSTO class
 *
 * @author wendelsj
 * @date 27/05/2024
 */
public class TSTO {

    private int count;

    /**
     * Constructs a new TSTO object with an initial count of 0.
     */
    public TSTO() {
        this.count = 0;
    }

    /**
     * Increments the count by 1.
     */
    public void increment() {
        this.count++;
    }

    /**
     * Adds the specified value to the count.
     * 
     * @param value the value to add to the count
     */
    public void add(int value) {
        this.count += value;
    }

    /**
     * Returns the current count.
     * 
     * @return the current count
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Resets the count to 0.
     */
    public void reset() {
        this.count = 0;
    }

    /**
     * Sets the count to the specified value.
     * 
     * @param count the new value for the count
     */
    public void setCount(int count) {
        this.count = count;
    }

}
