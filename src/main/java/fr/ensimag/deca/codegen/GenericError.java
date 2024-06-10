package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

public class GenericError extends AbstractError {

    private final Label label;
    private final String message;

    public GenericError(int i, String msg) {
        label = new Label(msg + i);
        message = msg;
    }

    public Label getLabel() {
        return label;
    }

    @Override
    public void generateError(DecacCompiler compiler) {
        compiler.addLabel(label);
        compiler.addInstruction(new WSTR(message));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }
}
