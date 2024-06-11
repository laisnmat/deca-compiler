package fr.ensimag.deca.codegen;

/**
 * TSTO class
 *
 * @author wendelsj
 * @date 27/05/2024
 */
public class StackManager {

    private int varGlobalCount;
    private int varLocalCount;
    public int maxPushCount;
    public int currentPushCount;
    private int methodTableSize;

    /**
     * Constructs a new StackManager object with an initial count of 0.
     */
    public StackManager() {
        this.methodTableSize = 0;
        this.varGlobalCount = 0;
        this.varLocalCount = 0;
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
     * Increments the local var count by 1.
     */
    public void incrementVarLocalCount() {
        this.varLocalCount++;
    }

    /**
     * Increments the global count by 1.
     */
    public void incrementVarGlobalCount() {
        this.varGlobalCount++;
    }

    /**
     * Adds the specified value to the local var count.
     *
     * @param value
     *            the value to add to the count
     */
    public void addToVarLocalCount(int value) {
        this.varLocalCount += value;
    }

    /**
     * Adds the specified value to the global var count.
     *
     * @param value
     *            the value to add to the count
     */
    public void addToVarGlobalCount(int value) {
        this.varGlobalCount += value;
    }

    /**
     * Returns the current local var count.
     *
     * @return the current count
     */
    public int getVarLocalCount() {
        return this.varLocalCount;
    }

    /**
     * Returns the current global var count.
     *
     * @return the current count
     */
    public int getVarGlobalCount() {
        return this.varGlobalCount;
    }

    /**
     * Resets the local var count to 0.
     */
    public void resetvarLocalCount() {
        this.varLocalCount = 0;
    }

    /**
     * Resets the global var count to 0.
     */
    public void resetvarGlobalCount() {
        this.varGlobalCount = 0;
    }

    /**
     * Sets the local var count to the specified value.
     *
     * @param count
     *            the new value for the count
     */
    public void setLocalCount(int count) {
        this.varLocalCount = count;
    }

    /**
     * Sets the global var count to the specified value.
     *
     * @param count
     *            the new value for the count
     */
    public void setGlobalCount(int count) {
        this.varGlobalCount = count;
    }

    public int getTSTOValue() {
        return maxPushCount + varGlobalCount;
    }

    public int getLocalADDSPValue() {
        return varLocalCount;
    }

    public int getGlobalADDSPValue() {
        return varGlobalCount + methodTableSize;
    }

}
