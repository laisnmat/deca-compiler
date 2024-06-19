package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Null literal
 *
 * @author gl03
 * @date 14/04/2024
 */
public class NullLiteral extends AbstractExpr {

    private static Logger LOG = Logger.getLogger(ThisLiteral.class);

    public NullLiteral() {
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify NullLiteral: setting type");
        this.setType(compiler.environmentType.NULL);
        return this.getType();
    }

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        compiler.addInstruction(new LOAD(new NullOperand(), register));
    }

    @Override
    String prettyPrintNode() {
        return "Null";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("Null");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
