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

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type type = getOperand().verifyExpr(compiler, localEnv, currentClass);
        setType(type);
        return type;
    }

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
