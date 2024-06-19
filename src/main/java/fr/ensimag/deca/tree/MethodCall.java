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
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.BSR;

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

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        compiler.addInstruction(new BSR(method.getMethodDefinition().getLabel()));
    }

    public void verifySignature(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass,
            Signature signature) throws ContextualError {
        if (listExpr.getList().size() != signature.size()) {
            throw new ContextualError("MethodCall: Wrong number of arguments", getLocation());
        }
        for (int i = 0; i < listExpr.getList().size(); i++) {
            try {
                listExpr.getList().get(i).verifyRValue(compiler, localEnv, currentClass, signature.paramNumber(i));
            } catch (ContextualError e) {
                throw new ContextualError("MethodCall: Cannot call method " + method.getName() + " with parameters "
                        + listExpr.getList() + " instead of " + signature.getArgs(), getLocation());
            }
        }

    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type t = classCall.verifyExpr(compiler, localEnv, currentClass);
        if (!t.isClass()) {
            throw new ContextualError("Incompatible Types: Try to call a function of a non class variable",
                    getLocation());
        }
        ClassType classType = (ClassType) t;
        Signature sig = method.verifyMethodIdent(compiler, classType.getDefinition().getMembers(), currentClass);

        this.verifySignature(compiler, localEnv, currentClass, sig);

        Type returnType = method.getType();
        setType(returnType);
        return returnType;
    }
}
