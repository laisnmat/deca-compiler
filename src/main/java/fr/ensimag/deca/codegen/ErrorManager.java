package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ErrorManager {
    ArrayList<AbstractError> errors = new ArrayList<>();
    private boolean containsIOError;

    private boolean containsDivZeroError;

    public ErrorManager() {
        containsIOError = false;
    }

    public void generateErrors(DecacCompiler compiler) {

        for (AbstractError err : errors) {
            err.generateError(compiler);
        }
    }

    public void addTSTOError() {
        this.errors.add(new TSTOError());
    }

    public void addDivZeroError() {
        if (!this.containsDivZeroError) {
            this.errors.add(new DivZeroError());
        }
        containsDivZeroError = true;
    }

    public void addIOError() {
        if (!this.containsIOError) {
            this.errors.add(new IOError());
        }
        containsIOError = true;
    }
}
