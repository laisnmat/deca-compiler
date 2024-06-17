package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.REM;

/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!this.getLeftOperand().getType().isInt() || !this.getRightOperand().getType().isInt()) {
            throw new ContextualError("Incompatible Types: Operands must be of type int for operator %",
                    this.getLocation());
        }
        this.setType(compiler.environmentType.INT);
        return this.getType();
    }

    @Override
    protected String getOperatorName() {
        return "%";
    }

    /**
     * Generate assembly code for the expression (Modulo)
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
            compiler.addInstruction(new LOAD(0, GPRegister.R1));
            compiler.addInstruction(new CMP(dvalRightOp, GPRegister.R1));
            compiler.addInstruction(new BEQ(compiler.labelManager.getDivByZeroError()));

            compiler.addInstruction(new REM(dvalRightOp, register));
        } else {
            if (register.getNumber() == DecacCompiler.MAXREGISTER) {
                compiler.addInstruction(new PUSH(register), "sauvegarde ");
                this.getRightOperand().codeGenExp(compiler, register);
                compiler.addInstruction(new LOAD(register, GPRegister.R1));
                compiler.addInstruction(new POP(register), "restauration ");

                compiler.addInstruction(new CMP(new ImmediateInteger(0), GPRegister.R1));
                compiler.addInstruction(new BEQ(compiler.labelManager.getDivByZeroError()));

                compiler.addInstruction(new REM(GPRegister.R1, register));
            } else {
                GPRegister nextRegister = GPRegister.getR(register.getNumber() + 1);
                this.getRightOperand().codeGenExp(compiler, nextRegister);

                compiler.addInstruction(new CMP(new ImmediateInteger(0), nextRegister));
                compiler.addInstruction(new BEQ(compiler.labelManager.getDivByZeroError()));

                compiler.addInstruction(new MUL(nextRegister, register));
            }
        }
    }

}
