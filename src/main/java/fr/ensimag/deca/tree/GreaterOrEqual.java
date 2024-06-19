package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;

/**
 * Operator "x >= y"
 * 
 * @author gl03
 * @date 14/04/2024
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public String getOperatorName() {
        return ">=";
    }

    protected void codeGenExp(DecacCompiler compiler, int registerName) {
        // nothing is returned
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, boolean jumpOnTrue, Label label) {
        // generate comparison before branching
        super.addComment(compiler, jumpOnTrue, label);
        super.codeGenCondition(compiler, jumpOnTrue, label);
        compiler.addInstruction(jumpOnTrue ? new BGE(label) : new BLT(label));
    }
}
