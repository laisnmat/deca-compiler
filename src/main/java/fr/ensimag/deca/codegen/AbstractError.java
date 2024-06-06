package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Instruction;

public abstract class AbstractError {

    public AbstractError() {
    }

    abstract public void generateError(DecacCompiler compiler);
}
