package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.QUO;

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
     *                 the compiler
     * @param register
     *                 the register to store the result
     */
    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        compiler.errorManager.addDivZeroError();
        this.getLeftOperand().codeGenExp(compiler, register);
        DVal dvalRightOp = this.getRightOperand().getDVal();
        if (dvalRightOp != null) {
            if (!compiler.getCompilerOptions().getNoCheck()) {
                if (this.getType().isInt())
                    compiler.addInstruction(new LOAD(0, GPRegister.R1));
                else
                    compiler.addInstruction(new LOAD(new ImmediateFloat(0), GPRegister.R1));
                compiler.addInstruction(new CMP(dvalRightOp, GPRegister.R1));
                compiler.addInstruction(new BEQ(compiler.labelManager.getDivByZeroError()));
            }

            if (this.getType().isInt())
                compiler.addInstruction(new QUO(dvalRightOp, register));
            else
                compiler.addInstruction(new DIV(dvalRightOp, register));
            super.codeGenOpError(compiler);
        } else {
            if (register.getNumber() == DecacCompiler.MAXREGISTER) {
                compiler.addInstruction(new PUSH(register), "sauvegarde ");
                this.getRightOperand().codeGenExp(compiler, register);
                compiler.addInstruction(new LOAD(register, GPRegister.R1));
                compiler.addInstruction(new POP(register), "restauration ");
                codeGenExpSecond(compiler, register, GPRegister.R1);
            } else {
                GPRegister nextRegister = GPRegister.getR(register.getNumber() + 1);
                this.getRightOperand().codeGenExp(compiler, nextRegister);
                codeGenExpSecond(compiler, register, nextRegister);
            }
        }
    }

    /**
     * Generate assembly code for the expression (Divide) only when we use -n flag
     * 
     * @param compiler     The compiler
     * @param register     The register to store the result
     * @param nextRegister The register to store the right opérande
     */

    void codeGenExpSecond(DecacCompiler compiler, GPRegister register, GPRegister nextRegister) {
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addInstruction(
                    new CMP(this.getType().isInt() ? new ImmediateInteger(0) : new ImmediateFloat(0), nextRegister));
            compiler.addInstruction(new BEQ(compiler.labelManager.getDivByZeroError()));
        }
        compiler.addInstruction(
                this.getType().isInt() ? new QUO(nextRegister, register) : new DIV(nextRegister, register));
        super.codeGenOpError(compiler);
    }
}
