package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.OPP;

/**
 * @author gl03
 * @date 14/04/2024
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    /**
     * Verify the unary minus expression
     * 
     * @param compiler     The compiler
     * @param localEnv     The environment in which the expression is verified
     * @param currentClass The current class
     * @return The type of the expression
     * @throws ContextualError The expression is not valid
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type type = getOperand().verifyExpr(compiler, localEnv, currentClass);
        setType(type);
        return type;
    }

    /**
     * Generate assembly code for the expression of a unary minus
     * 
     * @param compiler     The compiler
     * @param registerName The register to store the result of the operation
     */
    @Override
    protected void codeGenExp(DecacCompiler compiler, int registerName) {
        if (getOperand().getDVal() == null) {
            getOperand().codeGenExp(compiler, registerName + 1);
            compiler.addInstruction(new OPP(Register.getR(registerName + 1), Register.getR(registerName)));
        } else {
            compiler.addInstruction(new OPP(getOperand().getDVal(), Register.getR(registerName)));
        }
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

}
