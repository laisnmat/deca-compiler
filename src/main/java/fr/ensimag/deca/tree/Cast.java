package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.INT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class Cast extends AbstractExpr {

    private AbstractIdentifier type;
    private AbstractExpr expr;

    public Cast(AbstractIdentifier type, AbstractExpr expr) {
        this.type = type;
        this.expr = expr;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        expr.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        expr.prettyPrint(s, prefix, true);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        type.decompile(s);
        s.print(")(");
        expr.decompile(s);
        s.print(")");
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type type1 = this.type.verifyType(compiler);
        Type type2 = expr.verifyExpr(compiler, localEnv, currentClass);
        if (!compiler.getCompilerOptions().getNoCheck())
            if (!castCompatible(compiler, type2, type1))
                throw new ContextualError(
                        "Incompatible Types: Cannot cast " + type2.getName() + " to " + type1.getName(), getLocation());
        setType(type1);
        return type1;
    }

    @Override
    public void codeGenExp(DecacCompiler compiler, GPRegister register) {
        if (this.type.getType().isInt()) {
            compiler.addInstruction(new INT(this.expr.getDVal(), register));
        } else {
            new LOAD(this.expr.getDVal(), register);
        }
    }
}
