package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
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

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        this.getLeftOperand().codeGenExp(compiler, register);
        GPRegister nextRegister = GPRegister.getR(register.getNumber());
        this.getRightOperand().codeGenExp(compiler, nextRegister);
        compiler.addInstruction(new MUL(register, nextRegister));
    }
}
