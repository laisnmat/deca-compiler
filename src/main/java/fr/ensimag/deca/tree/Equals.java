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
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, boolean jumpOnTrue, Label label) {
        // generate comparison before branching
        super.codeGenCondition(compiler, jumpOnTrue, label);
        compiler.addInstruction(jumpOnTrue ? new BEQ(label) : new BNE(label));
    }

    @Override
    protected String getOperatorName() {
        return "==";
    }

}
