package fr.ensimag.deca.tree;

import java.io.PrintStream;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

public class MethodCall extends AbstractExpr {

    private AbstractExpr classe;
    private AbstractExpr method;
    private ListExpr listExpr;

    public MethodCall(AbstractExpr classe, AbstractExpr method, ListExpr listExpr) {
        this.classe = classe;
        this.method = method;
        this.listExpr = listExpr;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classe.iter(f);
        method.iter(f);
        listExpr.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classe.prettyPrint(s, prefix, false);
        method.prettyPrint(s, prefix, false);
        listExpr.prettyPrint(s, prefix, true);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        classe.decompile(s);
        s.print(".");
        method.decompile(s);
        s.print("(");
        listExpr.decompile(s);
        s.print(")");
    }

    // @Override
    // protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
    // throw new UnsupportedOperationException("not yet implemented");
    // }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
        // Type type = this.type.verifyType(compiler);
        // if (!type.isClass()) {
        // throw new ContextualError("Incompatible Type: " + type.getName() + " is not a
        // class", getLocation());
        // }
        // this.setType(type);
        // return type;
    }

}
