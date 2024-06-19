package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;

public abstract class AbstractError {

    public AbstractError() {
    }

    /**
     * Generate eroor body of this Error
     * 
     * @param compiler
     */
    abstract public void generateError(DecacCompiler compiler);
}
