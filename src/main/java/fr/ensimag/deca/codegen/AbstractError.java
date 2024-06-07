package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Instruction;

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
