package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class InstanceOf extends AbstractExpr {

    private AbstractExpr expression;
    private AbstractIdentifier type;

    public InstanceOf(AbstractExpr expression, AbstractIdentifier type) {
        this.expression = expression;
        this.type = type;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expression.iter(f);
        type.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, false);
        type.prettyPrint(s, prefix, true);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        expression.decompile(s);
        s.print(" instanceof ");
        type.decompile(s);
    }

    // @Override
    // protected void codeGenExpr(DecacCompiler compiler, GPRegister register) {
    // throw new UnsupportedOperationException("not yet implemented");
    // }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // (3.40)
        // expr ↓env_types ↓env_exp ↓class ↑type1
        Type type1 = expression.verifyExpr(compiler, localEnv, currentClass);
        // type ↓env_types ↑type2
        Type type2 = type.verifyType(compiler);
        // type := type_instanceof_op(type1, type2)
        typeInstanceOfOp(type1, type2);
        setType(compiler.environmentType.BOOLEAN);
        return getType();
    }

    protected void typeInstanceOfOp(Type t1, Type t2)
            throws ContextualError {
        if (!t1.isClassOrNull()) {
            throw new ContextualError("Cannot apply instanceOf operation because " + t1 + " is not a class or null",
                    getLocation());
        }
        if (!t2.isClassOrNull()) {
            throw new ContextualError("Cannot apply instanceOf operation because " + t2 + " is not a class or null",
                    getLocation());
        }
    }
}
