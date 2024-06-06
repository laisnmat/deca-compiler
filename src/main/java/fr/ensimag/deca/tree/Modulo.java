package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.REM;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

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

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        this.getLeftOperand().codeGenExp(compiler, register);
        GPRegister nextRegister = GPRegister.getR(register.getNumber() + 1);
        this.getRightOperand().codeGenExp(compiler, nextRegister);
        compiler.addInstruction(new REM(register, nextRegister));
    }

}
