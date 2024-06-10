package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
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
public class Not extends AbstractUnaryExpr {
    private static final Logger LOG = Logger.getLogger(Not.class);

    public Not(AbstractExpr operand) {
        super(operand);
    }

    /**
     * Verify rule (3.37) with 'not' operand. Verifies rule (3.63)
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify Not: at location " + this.getLocation());

        Type typeOp = this.getOperand().verifyExpr(compiler, localEnv, currentClass);

        // verify type_unary_op()
        if (typeOp != compiler.environmentType.BOOLEAN) {
            throw new ContextualError(
                    "Incompatible Types: Operand should be of type 'boolean', but is of type '"
                            + typeOp + "'.",
                    getLocation());
        }

        // type verification
        this.setType(compiler.environmentType.BOOLEAN);
        return this.getType();
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("!");
        this.getOperand().decompile(s);
    }

    @Override
    public void codeGenCondition(DecacCompiler compiler, boolean jumpOnTrue, Label label) {
        this.getOperand().codeGenCondition(compiler, !jumpOnTrue, label);
    }

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        this.getOperand().codeGenExp(compiler, register);
    }
}
