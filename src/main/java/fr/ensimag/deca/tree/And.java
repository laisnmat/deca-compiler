package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.ima.pseudocode.Label;


/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int registerName) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenExpBool(DecacCompiler compiler, boolean branchIf, String labelName) {
        if (branchIf) {
            String endLabel = compiler.labelManager.getLabel("end");
            this.getLeftOperand().codeGenExpBool(compiler, false, endLabel);
            this.getRightOperand().codeGenExpBool(compiler, true, labelName);
            compiler.addLabel(new Label(endLabel));
        }
        else {
            this.getLeftOperand().codeGenExpBool(compiler, false, labelName);
            this.getRightOperand().codeGenExpBool(compiler, false, labelName);
        }
    }
}
