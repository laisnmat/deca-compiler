package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                getOperand().verifyExpr(compiler, localEnv,currentClass);
                if (getOperand().getType() != compiler.environmentType.BOOLEAN){
                    throw new ContextualError("L'opérande de la négation n'est pas une expression booléenne", getLocation());
                }
                setType(compiler.environmentType.BOOLEAN);
                return compiler.environmentType.BOOLEAN;
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }
}
