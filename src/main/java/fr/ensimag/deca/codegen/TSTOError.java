package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

public class TSTOError extends AbstractError {

    public TSTOError() {
    }

    @Override
    public void generateError(DecacCompiler compiler) {
        compiler.addLabel(compiler.labelManager.getStackOverflowError());
        compiler.addInstruction(new WSTR("Error: Stack overflow"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }

}
