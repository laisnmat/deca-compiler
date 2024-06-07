package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
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

    /**
     * Generate assembly code for the expression of a minus
     * 
     * @param compiler     The compiler
     * @param registerName The register to store the result of the operation
     */
    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister registerName) {
        GPRegister nextRegister = super.codeGenOpperand(compiler, registerName);
        compiler.addInstruction(new SUB(nextRegister, registerName));
    }
}
