package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

public class IOError extends AbstractError {

    public IOError() {
        super();
    }

    @Override
    public void generateError(DecacCompiler compiler) {
        compiler.addLabel(compiler.labelManager.getIoErrorLabel());
        compiler.addInstruction(new WSTR("Error: Input/Output error"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }
}
