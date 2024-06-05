package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;

/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "!=";
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int registerName) {
        // avoids generation of useless code when given "x != y;" for example
    }

    /**
     * Implementation of !=
     *
     * @param compiler
     * @param branchIf
     * @param labelName
     */
    @Override
    protected void codeGenExpBool(DecacCompiler compiler, boolean branchIf, String labelName) {
        super.codeGenExp(compiler, 2);
        if (branchIf) {
            compiler.addInstruction(new BNE(new Label(labelName)));
        } else {
            compiler.addInstruction(new BEQ(new Label(labelName)));
        }
    }
}
