package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class MethodCall extends AbstractExpr {

    private AbstractExpr classCall;
    private AbstractIdentifier method;
    private ListExpr listExpr;

    public MethodCall(AbstractExpr classCall, AbstractIdentifier method, ListExpr listExpr) {
        this.classCall = classCall;
        this.method = method;
        this.listExpr = listExpr;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classCall.iter(f);
        method.iter(f);
        listExpr.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classCall.prettyPrint(s, prefix, false);
        method.prettyPrint(s, prefix, false);
        listExpr.prettyPrint(s, prefix, true);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        classCall.decompile(s);
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

    public void verifySignature(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass,
            Signature signature) throws ContextualError {
        if (listExpr.getList().size() != signature.size()) {
            throw new ContextualError("Wrong number of arguments", getLocation());
        }
        for (int i = 0; i < listExpr.getList().size(); i++) {
            try {
                listExpr.getList().get(i).verifyRValue(compiler, localEnv, currentClass, signature.paramNumber(i));
            } catch (ContextualError e) {
                throw new ContextualError("Cannot call method " + method.getName() + " with parameters "
                        + listExpr.getList() + " instead of " + signature.getArgs(), getLocation());
            }
        }

    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type t = classCall.verifyExpr(compiler, localEnv, currentClass);
        if (!t.isClass()) {
            throw new ContextualError("The expression of the method is not a class", getLocation());
        }
        ClassType classType = (ClassType) t;
        Signature sig = method.verifyMethodIdent(compiler, classType.getDefinition().getMembers(), currentClass);

        this.verifySignature(compiler, localEnv, currentClass, sig);

        Type returnType = method.getType();
        setType(returnType);
        return returnType;
    }
}
