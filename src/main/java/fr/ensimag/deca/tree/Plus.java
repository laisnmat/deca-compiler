package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;

/**
 * @author gl03
 * @date 14/04/2024
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "+";
    }

    /**
     * Generate assembly code for the expression of a plus
     * 
     * @param compiler     The compiler
     * @param registerName The register to store the result of the operation
     */
    @Override
    protected void codeGenExp(DecacCompiler compiler, int registerName) {
        getLeftOperand().codeGenExp(compiler, registerName);
        if (getRightOperand().getDVal() == null) {
            getRightOperand().codeGenExp(compiler, registerName + 1);
            compiler.addInstruction(
                    new ADD(Register.getR(registerName + 1), Register.getR(registerName)));
        } else {
            compiler.addInstruction(
                    new ADD(getRightOperand().getDVal(), Register.getR(registerName)));
        }
    }
}
