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
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                getLeftOperand().verifyExpr(compiler, localEnv,currentClass);
                if (getLeftOperand().getType() != compiler.environmentType.BOOLEAN){
                    throw new ContextualError("L'opérande gauche n'est pas une expression booléenne", getLocation());
                }
                getRightOperand().verifyExpr(compiler, localEnv,currentClass);
                if (getRightOperand().getType() != compiler.environmentType.BOOLEAN){
                    throw new ContextualError("L'opérande droite n'est pas une expression booléenne", getLocation());
                }
                setType(compiler.environmentType.BOOLEAN);
                return compiler.environmentType.BOOLEAN;
    }

}
