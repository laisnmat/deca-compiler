package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
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
     * @param compiler
     *                     The compiler
     * @param registerName
     *                     The register to store the result of the operation
     */
    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister registerName) {
        DVal rDval = this.codeGenOp(compiler, registerName);
        compiler.addInstruction(new ADD(rDval, registerName));
    }
}
