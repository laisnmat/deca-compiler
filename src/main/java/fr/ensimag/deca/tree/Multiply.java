package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

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
        this.getLeftOperand().codeGenExp(compiler, register);
        DVal dvalRightOp = this.getRightOperand().getDVal();
        if (dvalRightOp != null) {
            compiler.addInstruction(new MUL(dvalRightOp, register));
        } else {
            if (register.getNumber() == DecacCompiler.MAXREGISTER) {
                compiler.addInstruction(new PUSH(register), "sauvegarde ");
                this.getRightOperand().codeGenExp(compiler, register);
                compiler.addInstruction(new LOAD(register, GPRegister.R1));
                compiler.addInstruction(new POP(register), "restauration ");
                compiler.addInstruction(new MUL(GPRegister.R1, register));
            } else {
                GPRegister nextRegister = GPRegister.getR(register.getNumber() + 1);
                this.getRightOperand().codeGenExp(compiler, nextRegister);
                compiler.addInstruction(new MUL(nextRegister, register));
            }
        }
    }
}
