package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class Return extends AbstractInst {

    private AbstractExpr expression;

    public Return(AbstractExpr expression) {
        this.expression = expression;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("Return ");
        expression.decompile(s);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected boolean verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
            Type returnType)
            throws ContextualError {
        // (3.24)
        // return e; is authorized only if the method is not void
        if (returnType.isVoid()) {
            if (currentClass == null)
                throw new ContextualError("ReturnError: Illegal return statement in main", getLocation());
            throw new ContextualError("ReturnError: Illegal return statement in void method", getLocation());
        }

        // (3.24)
        // rvalue ↓env_types ↓env_exp ↓class ↓return
        try {
            expression.verifyRValue(compiler, localEnv, currentClass, returnType);
        } catch (ContextualError e) {
            if (e.getMessage().startsWith("Incompatible Types: RValue must have the same type or subtype as LValue:")) {
                throw new ContextualError("Incompatible Types: Return statement with wrong type: "
                        + expression.getType() + " instead of " + returnType, getLocation());
            }
            throw e;
        }
        return true;
    }

}
