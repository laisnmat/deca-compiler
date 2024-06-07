package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.BOV;

/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "/";
    }

    /**
     * Generate assembly code for the expression (Divide)
     * 
     * @param compiler
     *            the compiler
     * @param register
     *            the register to store the result
     */
    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        DVal nextRegister = super.codeGenOpperand(compiler, register);
        compiler.addInstruction(new PUSH(register));
        compiler.addInstruction(new LOAD(0, register));
        compiler.addInstruction(new CMP(nextRegister, register));
        compiler.addInstruction(new BOV(compiler.labelManager.getDivByZeroError()));
        compiler.labelManager.setDivByZeroError(true);
        compiler.addInstruction(new POP(register));
        if (this.getType().isInt()) {
            compiler.addInstruction(new QUO(nextRegister, register));
        } else {
            compiler.addInstruction(new DIV(nextRegister, register));
        }
    }

}
