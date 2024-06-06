package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 * @author gl03
 * @date 14/04/2024
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int registerName) {
        getLeftOperand().codeGenExp(compiler, registerName);
        if (getRightOperand().getDVal() == null) {
            getRightOperand().codeGenExp(compiler, registerName + 1);
            compiler.addInstruction(
                    new SUB(Register.getR(registerName + 1), Register.getR(registerName)));
        } else {
            compiler.addInstruction(
                    new SUB(getRightOperand().getDVal(), Register.getR(registerName)));
        }
    }
}
