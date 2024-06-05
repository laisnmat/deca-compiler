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
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return ">";
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int registerName) {
        // nothing is returned
    }

    @Override
    protected void codeGenExpBool(DecacCompiler compiler, boolean branchIf, String labelName) {
        super.codeGenExp(compiler, 2);
        if (!branchIf) {
            compiler.addInstruction(new BLE(new Label(labelName)));
        } else {
            compiler.addInstruction(new BGT(new Label(labelName)));
        }
    }
}
