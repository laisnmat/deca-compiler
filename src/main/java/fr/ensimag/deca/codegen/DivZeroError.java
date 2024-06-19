package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

public class DivZeroError extends AbstractError {

    public DivZeroError() {
        super();
    }

    @Override
    public void generateError(DecacCompiler compiler) {
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addLabel(compiler.labelManager.getDivByZeroError());
            compiler.addInstruction(new WSTR("Error: Division by zero"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }
    }
}
