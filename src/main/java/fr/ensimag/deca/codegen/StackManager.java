package fr.ensimag.deca.codegen;

/**
 * TSTO class
 *
 * @author wendelsj
 * @date 27/05/2024
 */
public class StackManager {

    private int varCount;
    public int maxPushCount;
    public int currentPushCount;

    /**
     * Constructs a new StackManager object with an initial count of 0.
     */
    public StackManager() {
        this.varCount = 0;
        this.maxPushCount = 0;
        this.currentPushCount = 0;
    }

    /**
     * Called when using a PUSH instruction
     * increments current push count by one, if the current count goes over the max
     * count recorded, it sets maxPushCount to a new high
     */
    public void incrementPushCount() {
        this.currentPushCount++;
        if (currentPushCount > maxPushCount) {
            maxPushCount = currentPushCount;
        }
    }

    /**
     * Called when using a POP instruction
     * decrements pushcount by one
     */
    public void decrementPushCount() {
        this.currentPushCount -= 1;
    }

    /**
     * Increments the count by 1.
     */
    public void incrementVarCount() {
        this.varCount++;
    }

    /**
     * Adds the specified value to the count.
     * 
     * @param value
     *            the value to add to the count
     */
    public void addToVarCount(int value) {
        this.varCount += value;
    }

    /**
     * Returns the current count.
     *
     * @return the current count
     */
    public int getVarCount() {
        return this.varCount;
    }

    /**
     * Resets the count to 0.
     */
    public void resetVarCount() {
        this.varCount = 0;
    }

    /**
     * Sets the count to the specified value.
     *
     * @param count
     *            the new value for the count
     */
    public void setVarCount(int count) {
        this.varCount = count;
    }

    public int getTSTOValue() {
        return maxPushCount + varCount;
    }

    public int getADDSPValue() {
        return varCount;
    }
}
