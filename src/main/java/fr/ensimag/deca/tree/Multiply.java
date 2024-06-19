package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.MUL;

/**
 * @author gl03
 * @date 14/04/2024
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "*";
    }

    /**
     * Generate assembly code for the expression (Multiply)
     * 
     * @param compiler
     *                 the compiler
     * @param register
     *                 the register to store the result
     */
    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        DVal rDval = this.codeGenOp(compiler, register);
        compiler.addInstruction(new MUL(rDval, register));
        super.codeGenOpError(compiler);
    }
}
