package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

public class ArithmeticOverflowError extends AbstractError {

    @Override
    public void generateError(DecacCompiler compiler) {
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addLabel(compiler.labelManager.getArithmeticOverflowError());
            compiler.addInstruction(new WSTR("Error: Arithmetic overflow"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }
    }

}
