package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;

/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class LowerOrEqual extends AbstractOpIneq {
    public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, boolean jumpOnTrue, Label label) {
        // generate comparison before branching
        super.addComment(compiler, jumpOnTrue, label);
        super.codeGenCondition(compiler, jumpOnTrue, label);
        compiler.addInstruction(jumpOnTrue ? new BLE(label) : new BGT(label));
    }

    @Override
    protected String getOperatorName() {
        return "<=";
    }
}
