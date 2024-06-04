package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;

/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class Lower extends AbstractOpIneq {

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "<";
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int registerName) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    protected void codeGenExpBool(DecacCompiler compiler, boolean branchIf, String labelName) {
        super.codeGenExp(compiler, 2);
        if (branchIf) {
            compiler.addInstruction(new BLT(new Label(labelName)));
        } else {
            compiler.addInstruction(new BGE(new Label(labelName)));
        }
    }
}
