package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelManager {
    // structure counters, used for unique label generation
    private int ifCount = 0;
    private int whileCount = 0;
    private int andCount = 0;
    private int orCount = 0;
    private int boolAssignCount = 0;
    private int boolExpCount = 0;

    /**
     * True if there is at least one call to an IO function, else False.
     */
    private boolean ioError = false;

    /**
     * True if there is a possibility of division by zero in the code, else False.
     */
    private boolean divByZeroError = false;

    /**
     * Prefix for the label.
     */
    private String prefix = "";
    private String prefix1 = null;
    private String prefix2 = null;
    private String prefix3 = null;

    /**
     * Get a new LabelIf according to the current ifCount.
     *
     * @return
     *         a LabelIf Object
     */
    public LabelIf getIf() {
        ifCount++;
        return new LabelIf(ifCount, this);
    }

    /**
     * Get a new LabelWhile according to the current whileCount.
     *
     * @return
     *         a LabelWhile Object
     */
    public LabelWhile getWhile() {
        whileCount++;
        return new LabelWhile(whileCount, this);
    }

    /**
     * Get a new LabelAnd according to the current andCount.
     *
     * @return
     *         a LabelAnd Object
     */
    public LabelAnd getAnd() {
        andCount++;
        return new LabelAnd(andCount, this);
    }

    /**
     * Get a new LabelOr according to the current orCount.
     *
     * @return
     *         a LabelOr Object
     */
    public LabelOr getOr() {
        orCount++;
        return new LabelOr(orCount, this);
    }

    /**
     * Get a new LabelBoolAssign according to the current boolAssignCount.
     *
     * @return
     *         a LabelBoolAssign Object
     */
    public LabelBoolAssign getBoolAssign() {
        boolAssignCount++;
        return new LabelBoolAssign(boolAssignCount, this);
    }

    /**
     * Get a new LabelBoolExp according to the current boolExpCount.
     *
     * @return
     *         a LabelBoolExp Object
     */
    public LabelBoolExp getBoolExp() {
        boolExpCount++;
        return new LabelBoolExp(boolExpCount, this);
    }

    /**
     * Get the current prefix of the labels.
     *
     * @return
     *         the current prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Add a prefix for the labels. If there is already 3 prefix set, it will act as
     * a FIFO and pop the oldest prefix in order to make space and stop labels from
     * being too long
     *
     * @param addPrefix
     *            the new prefix
     */
    public void addPrefix(String addPrefix) {
        if (this.prefix1 == null) {
            this.prefix1 = addPrefix;
            this.updatePrefix();
            return;
        }
        if (this.prefix2 == null) {
            this.prefix2 = addPrefix;
            this.updatePrefix();
            return;
        }

        if (this.prefix3 != null) {
            this.prefix1 = this.prefix2;
            this.prefix2 = this.prefix3;
        }
        this.prefix3 = addPrefix;
        this.updatePrefix();
    }

    /**
     * Act as a LIFO, pops out the latest prefix
     *
     */
    public void removePrefix() {
        if (this.prefix3 != null) {
            this.prefix3 = null;
            this.updatePrefix();
            return;
        }
        if (this.prefix2 != null) {
            this.prefix2 = null;
            this.updatePrefix();
            return;
        }

        this.prefix1 = null;
        this.updatePrefix();
    }

    /**
     * Called when there's a change from methods addPrefix or removePrefix
     * Concatenate each prefix variable to make the current prefix
     */
    private void updatePrefix() {
        this.prefix = (prefix1 == null ? "" : prefix1) + (prefix2 == null ? "" : prefix2)
                + (prefix3 == null ? "" : prefix3);
    }

    /**
     * Get if there is a possibility of IO error in the code.
     *
     * @return
     *         True if there is at least one call to an IO function, else False.
     */
    public boolean isIoError() {
        return ioError;
    }

    public Label getIoErrorLabel() {
        return new Label("io_error");
    }

    public Label getStackOverflowError() {
        return new Label("stack_overflow_error");
    }

    /**
     * Get if there is a possibility of division by zero in the code.
     *
     * @return
     *         True if there is a possibility of division by zero in the code, else
     *         False.
     */
    public boolean isDivByZeroError() {
        return divByZeroError;
    }

    public Label getDivByZeroError() {
        return new Label("div_zero_error");
    }
}
