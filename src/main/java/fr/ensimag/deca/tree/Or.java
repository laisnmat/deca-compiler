package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;


/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenExpBool(DecacCompiler compiler, boolean branchIf, String labelName) {
        branchIf = !branchIf;
        String endLabel = compiler.labelManager.getLabel("end");
        if (branchIf) {
            this.getLeftOperand().codeGenExpBool(compiler, true, endLabel);
            this.getRightOperand().codeGenExpBool(compiler, false, labelName);
            compiler.addLabel(new Label(endLabel));
        }
        else {
            this.getLeftOperand().codeGenExpBool(compiler, true, labelName);
            this.getRightOperand().codeGenExpBool(compiler, true, labelName);
        }
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int registerName) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
