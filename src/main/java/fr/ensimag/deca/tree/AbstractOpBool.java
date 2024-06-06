package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import org.apache.log4j.Logger;

/**
 *
 * @author gl03
 * @date 14/04/2024
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(AbstractOpBool.class);

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    /**
     * Verify rule (3.33) for Operand in {And, Or}. Verifies rules (3.54) (3.55).
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify AbstractOpBool: " + this.getOperatorName() + " at location " + this.getLocation());

        Type typeRightOp = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeLeftOp = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

        // verify type_binary_op()
        if (typeRightOp != compiler.environmentType.BOOLEAN) {
            throw new ContextualError(
                    "RightOperand should be of type 'boolean', but is of type '"
                            + typeRightOp + "'.",
                    getLocation());
        }

        if (typeLeftOp != compiler.environmentType.BOOLEAN) {
            throw new ContextualError(
                    "LeftOperand should be of type 'boolean', but is of type '"
                            + typeLeftOp + "'.",
                    getLocation());
        }

        // type verification
        setType(compiler.environmentType.BOOLEAN);
        return this.getType();
    }

}
