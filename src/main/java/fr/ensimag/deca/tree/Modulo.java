package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
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
            throw new ContextualError("Operands must be of type int for operator %", this.getLocation());
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
        compiler.addInstruction(new REM(nextRegister, register));
    }

}
