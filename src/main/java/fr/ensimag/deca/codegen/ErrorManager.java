package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ErrorManager {
    ArrayList<AbstractError> errors = new ArrayList<>();
    private boolean containsIOError;
    private int genericCount;

    private boolean containsDivZeroError;

    public ErrorManager() {
        containsIOError = false;
        genericCount = 0;
    }

    /**
     * This methods will generate the error body of each AbstractError that must be
     * implemented in the code
     * 
     * @param compiler
     */
    public void generateErrors(DecacCompiler compiler) {

        for (AbstractError err : errors) {
            err.generateError(compiler);
        }
    }

    /**
     * Creates a new TSTOError. Behaviour undefined if there's already one
     */
    public void addTSTOError() {
        this.errors.add(new TSTOError());
    }

    public void addDivZeroError() {
        if (!this.containsDivZeroError) {
            this.errors.add(new DivZeroError());
        }
        containsDivZeroError = true;
    }

    /**
     * creates a new IOError, if there's already one, skips the method
     */
    public void addIOError() {
        if (!this.containsIOError) {
            this.errors.add(new IOError());
            containsIOError = true;
        }
    }

    /**
     * Create a new Generic Error
     *
     * @param msg
     *            error message
     * @return Label corresponding to error body
     */
    public Label addGenericError(String msg) {
        GenericError err = new GenericError(genericCount, msg);
        this.errors.add(err);
        return err.getLabel();
    }
}
