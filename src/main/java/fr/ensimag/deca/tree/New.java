package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class New extends AbstractExpr {

    private AbstractIdentifier type;

    public New(AbstractIdentifier type) {
        this.type = type;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, true);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("New ");
        type.decompile(s);
    }

    // @Override
    // protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
    // throw new UnsupportedOperationException("not yet implemented");
    // }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        type.verifyType(compiler);
        if (!type.getType().isClass()) {
            throw new ContextualError(
                    "New is not possible on a non-class type",
                    getLocation());
        }
        setType(type.getType());
        return type.getType();

    }

}
